package org.terraframe

import java.awt._
import java.awt.image._
import java.io.Serializable

import Images.loadImage

import scala.collection.mutable.ArrayBuffer
import scala.math._
import scala.util.Random

case class Entity(var x: Double,
                  var y: Double,
                  var vx: Double,
                  var vy: Double,
                  id: Short,
                  num: Short,
                  dur: Short,
                  var mdelay: Int,
                  name: Option[String])
    extends Serializable {

  import TerraFrame.BLOCKSIZE

  //Begin constructor
  var oldx: Double = x
  var oldy: Double = y

  var nohit: Boolean = false

  var thp, ap, atk: Int                                  = _
  var AI: Option[String]                                 = None
  var imgState: String                                   = _
  var onGround, immune, grounded, onGroundDelay: Boolean = _

  var n: Double               = _
  var bx1, bx2, by1, by2: Int = _

  var newMob: Option[Entity]          = None
  @transient var image: BufferedImage = _

  // TODO: this should probably be an entity base type and a named entity with AI vs some other type without AI and just id
  name match {
    case Some("blue_bubble") => // TODO: sum type for name
      thp = 18
      ap = 0
      atk = 2
      AI = Some("bubble")

    case Some("green_bubble") =>
      thp = 25
      ap = 0
      atk = 4
      AI = Some("bubble")

    case Some("red_bubble") =>
      thp = 40
      ap = 0
      atk = 6
      AI = Some("bubble")

    case Some("yellow_bubble") =>
      thp = 65
      ap = 1
      atk = 9
      AI = Some("bubble")

    case Some("black_bubble") =>
      thp = 100
      ap = 3
      atk = 14
      AI = Some("bubble")

    case Some("white_bubble") =>
      thp = 70
      ap = 2
      atk = 11
      AI = Some("fast_bubble")

    case Some("zombie") =>
      thp = 35
      ap = 0
      atk = 5
      AI = Some("zombie")

    case Some("armored_zombie") =>
      thp = 45
      ap = 2
      atk = 7
      AI = Some("zombie")

    case Some("shooting_star") =>
      thp = 25
      ap = 0
      atk = 5
      AI = Some("shooting_star")

    case Some("sandbot") =>
      thp = 50
      ap = 2
      atk = 3
      AI = Some("sandbot")

    case Some("sandbot_bullet") =>
      thp = 1
      ap = 0
      atk = 7
      AI = Some("bullet")
      nohit = false

    case Some("snowman") =>
      thp = 40
      ap = 0
      atk = 6
      AI = Some("zombie")

    case Some("bat") =>
      thp = 15
      ap = 0
      atk = 5
      AI = Some("bat")

    case Some("bee") =>
      thp = 1
      ap = 0
      atk = 5
      AI = Some("bee")

    case Some("skeleton") =>
      thp = 50
      ap = 1
      atk = 7
      AI = Some("zombie")

    case _ =>
  }

  AI match {
    case Some("bubble" | "fast_bubble" | "shooting_star" | "sandbot" | "bullet" | "bee") =>
      name.foreach { nameTemp =>
        image = loadImage("sprites/monsters/" + nameTemp + "/normal.png")
      }

    case Some("zombie") =>
      name.foreach { nameTemp =>
        image = loadImage("sprites/monsters/" + nameTemp + "/right_still.png")
      }

    case Some("bat") =>
      name.foreach { nameTemp =>
        image = loadImage("sprites/monsters/" + nameTemp + "/normal_right.png")
      }

    case None =>
      TerraFrame.itemImgs.get(id).foreach { i =>
        image = i
      }
  }

  imgState = AI match {
    case Some("bat") =>
      vx = 3
      "normal right"
    case _ => "still right"
  }

  val width: Int  = image.getWidth() * 2
  val height: Int = image.getHeight() * 2

  var ix              = x.toInt
  var iy              = y.toInt
  var ivx             = vx.toInt
  var ivy             = vy.toInt
  val rect = new Rectangle(ix - 1, iy, width + 2, height)
  val intersectRect = new Rectangle(-1, -1, -1, -1)

  var imgDelay: Int = 0
  var bcount: Int   = 0

  var hp: Int = thp

  def this(x: Double, y: Double, vx: Double, vy: Double, name: String) {
    this(x, y, vx, vy, 0, 0, 0, 0, Some(name))
  }

  def this(x: Double, y: Double, vx: Double, vy: Double, id: Short, num: Short) {
    this(x, y, vx, vy, id, num, 0, 0, None)
  }

  def this(x: Double, y: Double, vx: Double, vy: Double, id: Short, num: Short, mdelay: Int) {
    this(x, y, vx, vy, id, num, 0, mdelay, None)
  }

  def this(x: Double, y: Double, vx: Double, vy: Double, id: Short, num: Short, dur: Short) {
    this(x, y, vx, vy, id, num, dur, 0, None)
  }

  def this(x: Double, y: Double, vx: Double, vy: Double, id: Short, num: Short, dur: Short, mdelay: Int) {
    this(x, y, vx, vy, id, num, dur, mdelay, None)
  }

  def update(blocks: Array2D[Int], player: Player, u: Int, v: Int): Boolean = {
    newMob = None
    if (name.isEmpty) {
      if (!onGround) {
        vy = vy + 0.3
        if (vy > 7) {
          vy = 7
        }
      }
      if (vx < -0.15) {
        vx = vx + 0.15
      } else if (vx > 0.15) {
        vx = vx - 0.15
      } else {
        vx = 0
      }
      collide(blocks, player, u, v)
      mdelay -= 1
    }

    AI match {
      case Some("bullet") =>
        collide(blocks, player, u, v)

      case Some("zombie") =>
        if (!onGround) {
          vy = vy + 0.3
          if (vy > 7) {
            vy = 7
          }
        }
        if (x > player.x) {
          vx = max(vx - 0.1, -1.2)
          if (imgState == "still left" || imgState == "still right" ||
              imgState == "walk right 1" || imgState == "walk right 2") {
            imgDelay = 10
            imgState = "walk left 2"
            name.foreach { nameTemp =>
              image = loadImage("sprites/monsters/" + nameTemp + "/left_walk.png")
            }
          }
          if (imgDelay <= 0) {
            if (imgState == "walk left 1") {
              imgDelay = 10
              imgState = "walk left 2"
              name.foreach { nameTemp =>
                image = loadImage("sprites/monsters/" + nameTemp + "/left_walk.png")
              }
            } else {
              if (imgState == "walk left 2") {
                imgDelay = 10
                imgState = "walk left 1"
                name.foreach { nameTemp =>
                  image = loadImage("sprites/monsters/" + nameTemp + "/left_still.png")
                }
              }
            }
          } else {
            imgDelay = imgDelay - 1
          }
        } else {
          vx = min(vx + 0.1, 1.2)
          if (imgState == "still left" || imgState == "still right" ||
              imgState == "walk left 1" || imgState == "walk left 2") {
            imgDelay = 10
            imgState = "walk right 2"
            name.foreach { nameTemp =>
              image = loadImage("sprites/monsters/" + nameTemp + "/right_walk.png")
            }
          }
          if (imgDelay <= 0) {
            if (imgState == "walk right 1") {
              imgDelay = 10
              imgState = "walk right 2"
              name.foreach { nameTemp =>
                image = loadImage("sprites/monsters/" + nameTemp + "/right_walk.png")
              }
            } else {
              if (imgState == "walk right 2") {
                imgDelay = 10
                imgState = "walk right 1"
                name.foreach { nameTemp =>
                  image = loadImage("sprites/monsters/" + nameTemp + "/right_still.png")
                }
              }
            }
          } else {
            imgDelay = imgDelay - 1
          }
        }
        if (!grounded) {
          if (imgState == "still left" || imgState == "walk left 1" ||
              imgState == "walk left 2") {
            name.foreach { nameTemp =>
              image = loadImage("sprites/monsters/" + nameTemp + "/left_jump.png")
            }
          }
          if (imgState == "still right" || imgState == "walk right 1" ||
              imgState == "walk right 2") {
            name.foreach { nameTemp =>
              image = loadImage("sprites/monsters/" + nameTemp + "/right_jump.png")
            }
          }
        }
        collide(blocks, player, u, v)

      case Some("bubble") =>
        if (x > player.x) {
          vx = max(vx - 0.1, -1.2)
        } else {
          vx = min(vx + 0.1, 1.2)
        }
        if (y > player.y) {
          vy = max(vy - 0.1, -1.2)
        } else {
          vy = min(vy + 0.1, 1.2)
        }
        collide(blocks, player, u, v)

      case Some("fast_bubble") =>
        if (x > player.x) {
          vx = max(vx - 0.2, -2.4)
        } else {
          vx = min(vx + 0.2, 2.4)
        }
        if (y > player.y) {
          vy = max(vy - 0.2, -2.4)
        } else {
          vy = min(vy + 0.2, 2.4)
        }
        collide(blocks, player, u, v)

      case Some("shooting_star") =>
        n = atan2(player.y - y, player.x - x)
        vx += cos(n) / 10
        vy += sin(n) / 10
        if (vx < -5) vx = -5
        if (vx > 5) vx = 5
        if (vy < -5) vy = -5
        if (vy > 5) vy = 5
        collide(blocks, player, u, v)

      case Some("sandbot") =>
        if (sqrt(pow(player.x - x, 2) + pow(player.y - y, 2)) > 160) {
          if (x > player.x) {
            vx = max(vx - 0.1, -1.2)
          } else {
            vx = min(vx + 0.1, 1.2)
          }
          if (y > player.y) {
            vy = max(vy - 0.1, -1.2)
          } else {
            vy = min(vy + 0.1, 1.2)
          }
        } else {
          if (x < player.x) {
            vx = max(vx - 0.1, -1.2)
          } else {
            vx = min(vx + 0.1, 1.2)
          }
          if (y < player.y) {
            vy = max(vy - 0.1, -1.2)
          } else {
            vy = min(vy + 0.1, 1.2)
          }
        }
        bcount += 1
        if (bcount == 110) {
          name.foreach { nameTemp =>
            image = loadImage("sprites/monsters/" + nameTemp + "/ready1.png")
          }
        }
        if (bcount == 130) {
          name.foreach { nameTemp =>
            image = loadImage("sprites/monsters/" + nameTemp + "/ready2.png")
          }
        }
        if (bcount == 150) {
          val theta: Double = atan2(player.y - y, player.x - x)
          name.foreach { nameTemp =>
            newMob = Some(new Entity(x, y, cos(theta) * 3.5, sin(theta) * 3.5, nameTemp + "_bullet"))
          }
        }
        if (bcount == 170) {
          name.foreach { nameTemp =>
            image = loadImage("sprites/monsters/" + nameTemp + "/ready1.png")
          }
        }
        if (bcount == 190) {
          name.foreach { nameTemp =>
            image = loadImage("sprites/monsters/" + nameTemp + "/normal.png")
          }
          bcount = 0
        }
        collide(blocks, player, u, v)

      case Some("bat") =>
        if (vx > 3) {
          vx = 3
        }
        if (vx < 3) {
          vx = -3
        }
        if (y > player.y) {
          vy = max(vy - 0.05, -2.0)
        } else {
          vy = min(vy + 0.05, 2.0)
        }
        imgDelay -= 1
        if (vx > 0 && imgState != "normal right") {
          imgState = "normal right"
          name.foreach { nameTemp =>
            image = loadImage("sprites/monsters/" + nameTemp + "/normal_right.png")
          }
          imgDelay = 10
        }
        if (vx < 0 && imgState != "normal left") {
          imgState = "normal left"
          name.foreach { nameTemp =>
            image = loadImage("sprites/monsters/" + nameTemp + "/normal_left.png")
          }
          imgDelay = 10
        }
        if (imgState == "normal left" && imgDelay <= 0) {
          imgState = "flap left"
          name.foreach { nameTemp =>
            image = loadImage("sprites/monsters/" + nameTemp + "/flap_left.png")
          }
          imgDelay = 10
        }
        if (imgState == "normal right" && imgDelay <= 0) {
          imgState = "flap right"
          name.foreach { nameTemp =>
            image = loadImage("sprites/monsters/" + nameTemp + "/flap_right.png")
          }
          imgDelay = 10
        }
        if (imgState == "flap left" && imgDelay <= 0) {
          imgState = "normal left"
          name.foreach { nameTemp =>
            image = loadImage("sprites/monsters/" + nameTemp + "/normal_left.png")
          }
          imgDelay = 10
        }
        if (imgState == "flap right" && imgDelay <= 0) {
          imgState = "normal right"
          name.foreach { nameTemp =>
            image = loadImage("sprites/monsters/" + nameTemp + "/normal_right.png")
          }
          imgDelay = 10
        }
        collide(blocks, player, u, v)

      case Some("bee") =>
        val theta: Double = atan2(player.y - y, player.x - x)
        vx = cos(theta) * 2.5
        vy = sin(theta) * 2.5
        collide(blocks, player, u, v)

      case _ => false
    }
  }

  def collide(blocks: Array2D[Int], player: Player, u: Int, v: Int): Boolean = {
    var rv: Boolean = false

    grounded = onGround || onGroundDelay

    onGroundDelay = onGround

    oldx = x
    oldy = y

    x = x + vx

    (0 until 2).foreach { i =>
      ix = x.toInt
      iy = y.toInt
      ivx = vx.toInt
      ivy = vy.toInt

      rect.setBounds(ix - 1, iy, width + 2, height)

      bx1 = (x / BLOCKSIZE).toInt
      by1 = (y / BLOCKSIZE).toInt
      bx2 = ((x + width) / BLOCKSIZE).toInt
      by2 = ((y + height) / BLOCKSIZE).toInt

      bx1 = max(0, bx1)
      by1 = max(0, by1)
      bx2 = min(blocks(0).length - 1, bx2)
      by2 = min(blocks.length - 1, by2)

      (bx1 to bx2).foreach { i =>
        (by1 to by2).foreach { j =>
          if (blocks(j)(i) != 0 && TerraFrame.BLOCKCD.get(blocks(j + v)(i + u)).exists(identity)) {
            intersectRect.setBounds(i * BLOCKSIZE, j * BLOCKSIZE, BLOCKSIZE, BLOCKSIZE)
            if (rect.intersects(intersectRect)) {
              if (oldx <= i * 16 - width && (vx > 0 || AI.contains("shooting_star"))) {
                x = (i * 16 - width).toDouble
                if (AI.contains("bubble")) {
                  vx = -vx
                } else if (AI.contains("zombie")) {
                  vx = 0
                  if (onGround && player.x > x) {
                    vy = -7
                  }
                } else if (AI.contains("bat")) {
                  vx = -vx
                } else {
                  vx = 0 // right
                }
                rv = true
              }
              if (oldx >= i * 16 + BLOCKSIZE && (vx < 0 || AI.contains("shooting_star"))) {
                x = (i * 16 + BLOCKSIZE).toDouble
                if (AI.contains("bubble")) {
                  vx = -vx
                } else if (AI.contains("zombie")) {
                  vx = 0
                  if (onGround && player.x < x) {
                    vy = -7
                  }
                } else if (AI.contains("bat")) {
                  vx = -vx
                } else {
                  vx = 0 // left
                }
                rv = true
              }
            }
          }
        }
      }
    }

    y = y + vy
    onGround = false

    (0 until 2).foreach { i =>
      ix = x.toInt
      iy = y.toInt
      ivx = vx.toInt
      ivy = vy.toInt

      rect.setBounds(ix, iy - 1, width, height + 2)

      bx1 = (x / BLOCKSIZE).toInt
      by1 = (y / BLOCKSIZE).toInt
      bx2 = ((x + width) / BLOCKSIZE).toInt
      by2 = ((y + height) / BLOCKSIZE).toInt

      bx1 = max(0, bx1)
      by1 = max(0, by1)
      bx2 = min(blocks(0).length - 1, bx2)
      by2 = min(blocks.length - 1, by2)

      (bx1 to bx2).foreach { i =>
        (by1 to by2).foreach { j =>
          if (blocks(j)(i) != 0 && TerraFrame.BLOCKCD.get(blocks(j + v)(i + u)).exists(identity)) {
            intersectRect.setBounds(i * BLOCKSIZE, j * BLOCKSIZE, BLOCKSIZE, BLOCKSIZE)
            if (rect.intersects(intersectRect)) {
              if (oldy <= j * 16 - height && (vy > 0 || AI.contains("shooting_star"))) {
                y = (j * 16 - height).toDouble
                onGround = true
                if (AI.contains("bubble")) {
                  vy = -vy
                } else {
                  vy = 0 // down
                }
                rv = true
              }
              if (oldy >= j * 16 + BLOCKSIZE && (vy < 0 || AI.contains("shooting_star"))) {
                y = (j * 16 + BLOCKSIZE).toDouble
                if (AI.contains("bubble")) {
                  vy = -vy
                } else {
                  vy = 0 // up
                }
                rv = true
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

    rect.setBounds(ix - 1, iy - 1, width + 2, height + 2)

    rv
  }

  def hit(damage: Int, player: Player): Boolean = {
    if (!immune && !nohit) {
      hp -= max(1, damage - ap)
      immune = true
      if (AI.contains("shooting_star")) {
        if (player.x + Player.width / 2 < x + width / 2) {
          vx = 4
        } else {
          vx = -4
        }
      } else {
        if (player.x + Player.width / 2 < x + width / 2) {
          vx += 4
        } else {
          vx -= 4
        }
        vy -= 1.2
      }
    }
    hp <= 0
  }

  def drops(): ArrayBuffer[Short] = {
    val dropList       = ArrayBuffer.empty[Short]
    val random: Random = TerraFrame.random
    if (name.contains("blue_bubble")) {
      (0 until random.nextInt(3)).foreach { i =>
        dropList += 97.toShort
      }
    }
    if (name.contains("green_bubble")) {
      (0 until random.nextInt(3)).foreach { i =>
        dropList += 98.toShort
      }
    }
    if (name.contains("red_bubble")) {
      (0 until random.nextInt(3)).foreach { i =>
        dropList += 99.toShort
      }
    }
    if (name.contains("yellow_bubble")) {
      (0 until random.nextInt(3)).foreach { i =>
        dropList += 100.toShort
      }
    }
    if (name.contains("black_bubble")) {
      (0 until random.nextInt(3)).foreach { i =>
        dropList += 101.toShort
      }
    }
    if (name.contains("white_bubble")) {
      (0 until random.nextInt(3)).foreach { i =>
        dropList += 102.toShort
      }
    }
    if (name.contains("shooting_star")) {
      (0 until random.nextInt(2)).foreach { i =>
        dropList += 103.toShort
      }
    }
    if (name.contains("zombie")) {
      (0 until random.nextInt(3)).foreach { i =>
        dropList += 104.toShort
      }
    }
    if (name.contains("armored_zombie")) {
      (0 until random.nextInt(3)).foreach { i =>
        dropList += 104.toShort
      }
      if (random.nextInt(15) == 0) {
        dropList += 109.toShort
      }
      if (random.nextInt(15) == 0) {
        dropList += 110.toShort
      }
      if (random.nextInt(15) == 0) {
        dropList += 111.toShort
      }
      if (random.nextInt(15) == 0) {
        dropList += 112.toShort
      }
    }
    if (name.contains("sandbot")) {
      (0 until random.nextInt(3)).foreach { i =>
        dropList += 74.toShort
      }
      if (random.nextInt(2) == 0) {
        dropList += 44.toShort
      }
      if (random.nextInt(6) == 0) {
        dropList += 45.toShort
      }
    }
    if (name.contains("snowman")) {
      (0 until random.nextInt(3)).foreach { _ =>
        dropList += 75.toShort
      }
    }
    dropList
  }

  def reloadImage(): Unit = {
    if (AI.contains("bubble") || AI.contains("shooting_star")) {
      name.foreach { nameTemp =>
        image = loadImage("sprites/monsters/" + nameTemp + "/normal.png")
      }
    }
    if (AI.contains("zombie")) {
      name.foreach { nameTemp =>
        image = loadImage("sprites/monsters/" + nameTemp + "/right_still.png")
      }
    }
  }

}
