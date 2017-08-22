package org.terraframe

import java.awt._
import java.awt.image._
import java.io._

import Images.loadImage
import TerraFrame.BLOCKSIZE

import scala.math._

import TypeSafeComparisons._

object Player {
  lazy val width: Int  = TerraFrame.PLAYERSIZEX
  lazy val height: Int = TerraFrame.PLAYERSIZEY

  lazy val leftWalkImage: BufferedImage   = loadImage("sprites/player/left_walk.png").get
  lazy val leftStillImage: BufferedImage  = loadImage("sprites/player/left_still.png").get
  lazy val rightWalkImage: BufferedImage  = loadImage("sprites/player/right_walk.png").get
  lazy val rightStillImage: BufferedImage = loadImage("sprites/player/right_still.png").get
  lazy val leftJumpImage: BufferedImage   = loadImage("sprites/player/left_jump.png").get
  lazy val rightJumpImage: BufferedImage  = loadImage("sprites/player/right_jump.png").get
  lazy val totalHP: Int                   = 50

  sealed trait PlayerImageState
  object StillLeft extends PlayerImageState
  object StillRight extends PlayerImageState
  object WalkRight1 extends PlayerImageState
  object WalkRight2 extends PlayerImageState
  object WalkLeft1 extends PlayerImageState
  object WalkLeft2 extends PlayerImageState
}


case class Player(var x: Double, var y: Double) extends Serializable {

  import Player._

  var oldx: Double = x
  var oldy: Double = y

  var vx: Double  = 0
  var vy: Double  = 0
  var pvy: Double = 0

  var ix: Int  = x.toInt
  var iy: Int  = y.toInt
  var ivx: Int = vx.toInt
  var ivy: Int = vy.toInt

  var onGround, onGroundDelay, grounded: Boolean = false

  var imgState: PlayerImageState      = StillRight
  @transient var image: BufferedImage = rightStillImage

  var imgDelay: Int = 0

  var hp: Int = totalHP

  val playerRect    = new Rectangle(ix, iy, width, height)
  val intersectRect = new Rectangle(-1, -1, -1, -1)

  def update(blocks: Array2D[BlockType], userInput: UserInput, u: Int, v: Int): Unit = {
    grounded = onGround || onGroundDelay
    if (userInput.isLeftKeyPressed) {
      if (vx > -4 || TerraFrame.DEBUG_SPEED) {
        vx = vx - 0.5
      }
      if (imgState === StillLeft || imgState === StillRight ||
          imgState === WalkRight1 || imgState === WalkRight2) {
        imgDelay = 5
        imgState = WalkLeft2
        image = leftWalkImage
      }
      if (imgDelay <= 0) {
        if (imgState === WalkLeft1) {
          imgDelay = 5
          imgState = WalkLeft2
          image = leftWalkImage
        } else {
          if (imgState === WalkLeft2) {
            imgDelay = 5
            imgState = WalkLeft1
            image = leftStillImage
          }
        }
      } else {
        imgDelay = imgDelay - 1
      }
    }
    if (userInput.isRightKeyPressed) {
      if (vx < 4 || TerraFrame.DEBUG_SPEED) {
        vx = vx + 0.5
      }
      if (imgState === StillLeft || imgState === StillRight ||
          imgState === WalkLeft1 || imgState === WalkLeft2) {
        imgDelay = 5
        imgState = WalkRight2
        image = rightWalkImage
      }
      if (imgDelay <= 0) {
        if (imgState === WalkRight1) {
          imgDelay = 5
          imgState = WalkRight2
          image = rightWalkImage
        } else {
          if (imgState === WalkRight2) {
            imgDelay = 5
            imgState = WalkRight1
            image = rightStillImage
          }
        }
      } else {
        imgDelay = imgDelay - 1
      }
    }
    if (userInput.isUpKeyPressed) {
      if (TerraFrame.DEBUG_FLIGHT) {
        vy -= 1
        pvy -= 1
      } else {
        if (onGround) {
          vy = -7
          pvy = -7
        }
      }
    }
    if (userInput.isDownKeyPressed) {
      if (TerraFrame.DEBUG_FLIGHT) {
        vy += 1
        pvy += 1
      }
    }
    if (!onGround) {
      vy = vy + 0.3
      pvy = pvy + 0.3
      if (vy > 7 && !TerraFrame.DEBUG_FLIGHT) {
        vy = 7
      }
    }
    if (!userInput.isLeftKeyPressed && !userInput.isRightKeyPressed) {
      if (abs(vx) < 0.3) {
        vx = 0
      }
      if (vx >= 0.3) {
        vx = vx - 0.3
      }
      if (vx <= -0.3) {
        vx = vx + 0.3
      }
      if (grounded) {
        if (imgState === StillLeft || imgState === WalkLeft1 ||
            imgState === WalkLeft2) {
          imgState = StillLeft
          image = leftStillImage
        }
        if (imgState === StillRight || imgState === WalkRight1 ||
            imgState === WalkRight2) {
          imgState = StillLeft
          image = rightStillImage
        }
      }
    }

    if (!grounded) {
      if (imgState === StillLeft || imgState === WalkLeft1 ||
          imgState === WalkLeft2) {
        image = leftJumpImage
      }
      if (imgState === StillRight || imgState === WalkRight1 ||
          imgState === WalkRight2) {
        image = rightJumpImage
      }
    }

    onGroundDelay = onGround

    oldx = x
    oldy = y

    x = x + vx

    if (!TerraFrame.DEBUG_NOCLIP) {
      (0 until 2).foreach { _ =>
        ix = x.toInt
        iy = y.toInt
        ivx = vx.toInt
        ivy = vy.toInt

        playerRect.setBounds(ix - 1, iy, width + 2, height)

        val bx1: Int = (x / BLOCKSIZE).toInt
        val by1: Int = (y / BLOCKSIZE).toInt
        val bx2: Int = ((x + width) / BLOCKSIZE).toInt
        val by2: Int = ((y + height) / BLOCKSIZE).toInt

        (bx1 to bx2).foreach { i =>
          (by1 to by2).foreach { j =>
            if (blocks(j + v)(i + u) =/= AirBlockType && TerraFrame.BLOCKCD.get(blocks(j + v)(i + u).id).exists(identity)) {
              intersectRect.setBounds(i * BLOCKSIZE, j * BLOCKSIZE, BLOCKSIZE, BLOCKSIZE)
              if (playerRect.intersects(intersectRect)) {
                if (oldx <= i * 16 - width && vx > 0) {
                  x = (i * 16 - width).toDouble
                  vx = 0 // right
                }
                if (oldx >= i * 16 + BLOCKSIZE && vx < 0) {
                  x = (i * 16 + BLOCKSIZE).toDouble
                  vx = 0 // left
                }
              }
            }
          }
        }
      }
    }

    y = y + vy
    onGround = false
    if (!TerraFrame.DEBUG_NOCLIP) {
      (0 until 2).foreach { _ =>
        ix = x.toInt
        iy = y.toInt
        ivx = vx.toInt
        ivy = vy.toInt

        playerRect.setBounds(ix, iy - 1, width, height + 2)

        val bx1: Int = (x / BLOCKSIZE).toInt
        val by1: Int = max(0, (y / BLOCKSIZE).toInt)
        val bx2: Int = ((x + width) / BLOCKSIZE).toInt
        val by2: Int = min(blocks.length - 1, ((y + height) / BLOCKSIZE).toInt)

        (bx1 to bx2).foreach { i =>
          (by1 to by2).foreach { j =>
            if (blocks(j + v)(i + u) =/= AirBlockType && TerraFrame.BLOCKCD.get(blocks(j + v)(i + u).id).exists(identity)) {
              intersectRect.setBounds(i * BLOCKSIZE, j * BLOCKSIZE, BLOCKSIZE, BLOCKSIZE)
              if (playerRect.intersects(intersectRect)) {
                if (oldy <= j * 16 - height && vy > 0) {
                  y = (j * 16 - height).toDouble
                  if (pvy >= 10 && !TerraFrame.DEBUG_INVINCIBLE) {
                    hp -= ((pvy - 12.5) * 2).toInt
                  }
                  onGround = true
                  vy = 0 // down
                  pvy = 0
                }
                if (oldy >= j * 16 + BLOCKSIZE && vy < 0) {
                  y = (j * 16 + BLOCKSIZE).toDouble
                  vy = 0 // up
                }
              }
            }
          }
        }
      }
    }

    ix = x.toInt
    iy = y.toInt
    ivx = vx.toInt
    ivy = vy.toInt

    playerRect.setBounds(ix - 1, iy - 1, width + 2, height + 2)
  }

  def reloadImage(): Unit = {
    if (grounded) {
      if (imgState === StillLeft || imgState === WalkLeft1) {
        image = leftStillImage
      }
      if (imgState === WalkLeft2) {
        image = leftWalkImage
      }
      if (imgState === StillRight || imgState === WalkRight1) {
        image = rightStillImage
      }
      if (imgState === WalkRight2) {
        image = rightWalkImage
      }
    } else {
      if (imgState === StillLeft || imgState === WalkLeft1 ||
          imgState === WalkLeft2) {
        image = leftJumpImage
      }
      if (imgState === StillRight || imgState === WalkRight1 ||
          imgState === WalkRight2) {
        image = rightJumpImage
      }
    }
  }

  def damage(damage: Int, useArmor: Boolean, inventory: Inventory): Unit = {
    var fd: Int = damage
    if (useArmor) {
      fd -= sumArmor()
      (0 until 4).foreach { i =>
        TerraFrame.armor.durs(i) = (TerraFrame.armor.durs(i) - 1).toShort
        if (TerraFrame.armor.durs(i) <= 0) {
          inventory.removeLocationIC(TerraFrame.armor, i, TerraFrame.armor.nums(i))
        }
      }
    }
    if (fd < 1) {
      fd = 1
    }
    hp -= fd
  }

  def sumArmor(): Int = {
    val armor0 = UiItem.armor(TerraFrame.armor.ids(0))
    val armor1 = UiItem.armor(TerraFrame.armor.ids(1))
    val armor2 = UiItem.armor(TerraFrame.armor.ids(2))
    val armor3 = UiItem.armor(TerraFrame.armor.ids(3))
    armor0 + armor1 + armor2 + armor3
  }

}
