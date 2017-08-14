package org.terraframe

import java.awt._
import java.awt.image._
import java.io.Serializable

import Images.loadImage

import scala.collection.mutable.ArrayBuffer
import scala.math._
import scala.util.Random

sealed trait AI
case object BubbleAI       extends AI
case object FastBubbleAI   extends AI
case object ZombieAI       extends AI
case object ShootingStarAI extends AI
case object SandbotAI      extends AI
case object BulletAI       extends AI
case object BatAI          extends AI
case object BeeAI          extends AI

sealed abstract class EntityStrategy(
    val imageName: String,
    val thp: Int,
    val ap: Int,
    val atk: Int,
    val ai: AI
)

object BlueBubble extends EntityStrategy("blue_bubble", 18, 0, 2, BubbleAI)

object GreenBubble extends EntityStrategy("green_bubble", 25, 0, 4, BubbleAI)

object RedBubble extends EntityStrategy("red_bubble", 40, 0, 6, BubbleAI)

object YellowBubble extends EntityStrategy("yellow_bubble", 65, 1, 9, BubbleAI)

object BlackBubble extends EntityStrategy("black_bubble", 100, 3, 14, BubbleAI)

object WhiteBubble extends EntityStrategy("white_bubble", 70, 2, 11, FastBubbleAI)

object Zombie extends EntityStrategy("zombie", 35, 0, 5, ZombieAI)

object ArmoredZombie extends EntityStrategy("armored_zombie", 45, 2, 7, ZombieAI)

object ShootingStar extends EntityStrategy("shooting_star", 25, 0, 5, ShootingStarAI)

object Sandbot extends EntityStrategy("sandbot", 50, 2, 3, SandbotAI)

object SandbotBullet extends EntityStrategy("sandbot_bullet", 1, 0, 7, BulletAI)

object Snowman extends EntityStrategy("snowman", 40, 0, 6, ZombieAI)

object Bat extends EntityStrategy("bat", 15, 0, 5, BatAI)

object Bee extends EntityStrategy("bee", 1, 0, 5, BeeAI)

object Skeleton extends EntityStrategy("skeleton", 50, 1, 7, ZombieAI)

sealed trait Entity {
  def image: BufferedImage
  def ix: Int
  def iy: Int
  def x: Double
  def y: Double
  def width: Int
  def height: Int
  def reloadImage(): Unit
  var immune: Boolean

  def update(blocks: Array2D[Int], player: Player, u: Int, v: Int): Boolean
}

case class AIEntity(var x: Double, var y: Double, var vx: Double, var vy: Double, strategy: EntityStrategy)
    extends Entity
    with Serializable {

  import TerraFrame.BLOCKSIZE

  //Begin constructor
  var oldx: Double = x
  var oldy: Double = y

  var nohit: Boolean = false

  var imgState: String                                   = _
  var onGround, immune, grounded, onGroundDelay: Boolean = _

  var newMob: Option[Entity] = None
  @transient var image: BufferedImage = strategy.ai match {
    case BubbleAI | FastBubbleAI | ShootingStarAI | SandbotAI | BulletAI | BeeAI =>
      loadImage("sprites/monsters/" + strategy.imageName + "/normal.png")

    case ZombieAI =>
      loadImage("sprites/monsters/" + strategy.imageName + "/right_still.png")

    case BatAI =>
      loadImage("sprites/monsters/" + strategy.imageName + "/normal_right.png")

  }

  val width: Int  = image.getWidth() * 2
  val height: Int = image.getHeight() * 2

  imgState = strategy.ai match {
    case BatAI => "normal right"
    case _     => "still right"
  }

  vx = strategy.ai match {
    case BatAI => 3.toDouble
    case _     => vx
  }

  var ix            = x.toInt
  var iy            = y.toInt
  var ivx           = vx.toInt
  var ivy           = vy.toInt
  val rect          = new Rectangle(ix - 1, iy, width + 2, height)
  val intersectRect = new Rectangle(-1, -1, -1, -1)

  var imgDelay: Int = 0
  var bcount: Int   = 0

  var hp: Int = strategy.thp

  def update(blocks: Array2D[Int], player: Player, u: Int, v: Int): Boolean = {
    newMob = None

    strategy.ai match {
      case BulletAI =>
        collide(blocks, player, u, v)

      case ZombieAI =>
        if (!onGround) {
          vy = vy + 0.3
          if (vy > 7) {
            vy = 7
          }
        }
        if (x > player.x) {
          vx = max(vx - 0.1, -1.2)
          if (imgState == "still left" || imgState == "still right" ||
              imgState == "walk right 1" || imgState == "walk right 2") { // TODO: sum type image state
            imgDelay = 10
            imgState = "walk left 2"
            image = loadImage("sprites/monsters/" + strategy.imageName + "/left_walk.png")
          }
          if (imgDelay <= 0) {
            if (imgState == "walk left 1") {
              imgDelay = 10
              imgState = "walk left 2"
              image = loadImage("sprites/monsters/" + strategy.imageName + "/left_walk.png")
            } else {
              if (imgState == "walk left 2") {
                imgDelay = 10
                imgState = "walk left 1"
                image = loadImage("sprites/monsters/" + strategy.imageName + "/left_still.png")
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
            image = loadImage("sprites/monsters/" + strategy.imageName + "/right_walk.png")
          }
          if (imgDelay <= 0) {
            if (imgState == "walk right 1") {
              imgDelay = 10
              imgState = "walk right 2"
              image = loadImage("sprites/monsters/" + strategy.imageName + "/right_walk.png")
            } else {
              if (imgState == "walk right 2") {
                imgDelay = 10
                imgState = "walk right 1"
                image = loadImage("sprites/monsters/" + strategy.imageName + "/right_still.png")
              }
            }
          } else {
            imgDelay = imgDelay - 1
          }
        }
        if (!grounded) {
          if (imgState == "still left" || imgState == "walk left 1" ||
              imgState == "walk left 2") {
            image = loadImage("sprites/monsters/" + strategy.imageName + "/left_jump.png")
          }
          if (imgState == "still right" || imgState == "walk right 1" ||
              imgState == "walk right 2") {
            image = loadImage("sprites/monsters/" + strategy.imageName + "/right_jump.png")
          }
        }
        collide(blocks, player, u, v)

      case BubbleAI =>
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

      case FastBubbleAI =>
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

      case ShootingStarAI =>
        val n = atan2(player.y - y, player.x - x)
        vx += cos(n) / 10
        vy += sin(n) / 10
        if (vx < -5) vx = -5
        if (vx > 5) vx = 5
        if (vy < -5) vy = -5
        if (vy > 5) vy = 5
        collide(blocks, player, u, v)

      case SandbotAI =>
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
          image = loadImage("sprites/monsters/" + strategy.imageName + "/ready1.png")
        }
        if (bcount == 130) {
          image = loadImage("sprites/monsters/" + strategy.imageName + "/ready2.png")
        }
        if (bcount == 150) {
          val theta: Double = atan2(player.y - y, player.x - x)
          newMob = Some(AIEntity(x, y, cos(theta) * 3.5, sin(theta) * 3.5, SandbotBullet))
        }
        if (bcount == 170) {
          image = loadImage("sprites/monsters/" + strategy.imageName + "/ready1.png")
        }
        if (bcount == 190) {
          image = loadImage("sprites/monsters/" + strategy.imageName + "/normal.png")
          bcount = 0
        }
        collide(blocks, player, u, v)

      case BatAI =>
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
          image = loadImage("sprites/monsters/" + strategy.imageName + "/normal_right.png")
          imgDelay = 10
        }
        if (vx < 0 && imgState != "normal left") {
          imgState = "normal left"
          image = loadImage("sprites/monsters/" + strategy.imageName + "/normal_left.png")
          imgDelay = 10
        }
        if (imgState == "normal left" && imgDelay <= 0) {
          imgState = "flap left"
          image = loadImage("sprites/monsters/" + strategy.imageName + "/flap_left.png")
          imgDelay = 10
        }
        if (imgState == "normal right" && imgDelay <= 0) {
          imgState = "flap right"
          image = loadImage("sprites/monsters/" + strategy.imageName + "/flap_right.png")
          imgDelay = 10
        }
        if (imgState == "flap left" && imgDelay <= 0) {
          imgState = "normal left"
          image = loadImage("sprites/monsters/" + strategy.imageName + "/normal_left.png")
          imgDelay = 10
        }
        if (imgState == "flap right" && imgDelay <= 0) {
          imgState = "normal right"
          image = loadImage("sprites/monsters/" + strategy.imageName + "/normal_right.png")
          imgDelay = 10
        }
        collide(blocks, player, u, v)

      case BeeAI =>
        val theta: Double = atan2(player.y - y, player.x - x)
        vx = cos(theta) * 2.5
        vy = sin(theta) * 2.5
        collide(blocks, player, u, v)
    }
  }

  def collide(blocks: Array2D[Int], player: Player, u: Int, v: Int): Boolean = {
    var rv: Boolean = false

    grounded = onGround || onGroundDelay

    onGroundDelay = onGround

    oldx = x
    oldy = y

    x = x + vx

    ix = x.toInt
    iy = y.toInt
    ivx = vx.toInt
    ivy = vy.toInt

    rect.setBounds(ix - 1, iy, width + 2, height)

    val bx1 = max(0, (x / BLOCKSIZE).toInt)
    val by1 = max(0, (y / BLOCKSIZE).toInt)
    val bx2 = min(blocks(0).length - 1, ((x + width) / BLOCKSIZE).toInt)
    val by2 = min(blocks.length - 1, ((y + height) / BLOCKSIZE).toInt)

    (bx1 to bx2).foreach { i =>
      (by1 to by2).foreach { j =>
        if (blocks(j)(i) != 0 && TerraFrame.BLOCKCD.get(blocks(j + v)(i + u)).exists(identity)) {
          intersectRect.setBounds(i * BLOCKSIZE, j * BLOCKSIZE, BLOCKSIZE, BLOCKSIZE)
          if (rect.intersects(intersectRect)) {
            if (oldx <= i * 16 - width && (vx > 0 || strategy.ai == ShootingStarAI)) {
              x = (i * 16 - width).toDouble
              if (strategy.ai == BubbleAI) {
                vx = -vx
              } else if (strategy.ai == ZombieAI) {
                vx = 0
                if (onGround && player.x > x) {
                  vy = -7
                }
              } else if (strategy.ai == BatAI) {
                vx = -vx
              } else {
                vx = 0 // right
              }
              rv = true
            }
            if (oldx >= i * 16 + BLOCKSIZE && (vx < 0 || strategy.ai == ShootingStarAI)) {
              x = (i * 16 + BLOCKSIZE).toDouble
              if (strategy.ai == BubbleAI) {
                vx = -vx
              } else if (strategy.ai == ZombieAI) {
                vx = 0
                if (onGround && player.x < x) {
                  vy = -7
                }
              } else if (strategy.ai == BatAI) {
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

    y = y + vy
    onGround = false

    ix = x.toInt
    iy = y.toInt
    ivx = vx.toInt
    ivy = vy.toInt

    rect.setBounds(ix, iy - 1, width, height + 2)

    (bx1 to bx2).foreach { i =>
      (by1 to by2).foreach { j =>
        if (blocks(j)(i) != 0 && TerraFrame.BLOCKCD.get(blocks(j + v)(i + u)).exists(identity)) {
          intersectRect.setBounds(i * BLOCKSIZE, j * BLOCKSIZE, BLOCKSIZE, BLOCKSIZE)
          if (rect.intersects(intersectRect)) {
            if (oldy <= j * 16 - height && (vy > 0 || strategy.ai == ShootingStarAI)) {
              y = (j * 16 - height).toDouble
              onGround = true
              if (strategy.ai == BubbleAI) {
                vy = -vy
              } else {
                vy = 0 // down
              }
              rv = true
            }
            if (oldy >= j * 16 + BLOCKSIZE && (vy < 0 || strategy.ai == ShootingStarAI)) {
              y = (j * 16 + BLOCKSIZE).toDouble
              if (strategy.ai == BubbleAI) {
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

    ix = x.toInt
    iy = y.toInt
    ivx = vx.toInt
    ivy = vy.toInt

    rect.setBounds(ix - 1, iy - 1, width + 2, height + 2)

    rv
  }

  def hit(damage: Int, player: Player): Boolean = {
    if (!immune && !nohit) {
      hp -= max(1, damage - strategy.ap)
      immune = true
      if (strategy.ai == ShootingStarAI) {
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

    // TODO: type-classes could be used here and no need for mutability
    strategy match {
      case BlueBubble =>
        (0 until random.nextInt(3)).foreach { i =>
          dropList += 97.toShort
        }
      case GreenBubble =>
        (0 until random.nextInt(3)).foreach { i =>
          dropList += 98.toShort
        }
      case RedBubble =>
        (0 until random.nextInt(3)).foreach { i =>
          dropList += 99.toShort
        }
      case YellowBubble =>
        (0 until random.nextInt(3)).foreach { i =>
          dropList += 100.toShort
        }
      case BlackBubble =>
        (0 until random.nextInt(3)).foreach { i =>
          dropList += 101.toShort
        }
      case WhiteBubble =>
        (0 until random.nextInt(3)).foreach { i =>
          dropList += 102.toShort
        }
      case ShootingStar =>
        (0 until random.nextInt(2)).foreach { i =>
          dropList += 103.toShort
        }
      case Zombie =>
        (0 until random.nextInt(3)).foreach { i =>
          dropList += 104.toShort
        }
      case ArmoredZombie =>
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
      case Sandbot =>
        (0 until random.nextInt(3)).foreach { i =>
          dropList += 74.toShort
        }
        if (random.nextInt(2) == 0) {
          dropList += 44.toShort
        }
        if (random.nextInt(6) == 0) {
          dropList += 45.toShort
        }
      case Snowman =>
        (0 until random.nextInt(3)).foreach { _ =>
          dropList += 75.toShort
        }
      case Bat           => //TODO: figure out what this drops
      case Bee           => //TODO: figure out what this drops
      case Skeleton      => //TODO: figure out what this drops
      case SandbotBullet => //TODO: figure out what this drops
    }

    dropList
  }

  def reloadImage(): Unit = {
    if (strategy.ai == BubbleAI || strategy.ai == ShootingStarAI) {
      image = loadImage("sprites/monsters/" + strategy.imageName + "/normal.png")
    }
    if (strategy.ai == ZombieAI) {
      image = loadImage("sprites/monsters/" + strategy.imageName + "/right_still.png")
    }
  }

}

case class IdEntity(var x: Double,
                    var y: Double,
                    var vx: Double,
                    var vy: Double,
                    id: Short,
                    num: Short,
                    dur: Short,
                    var mdelay: Int)
    extends Entity
    with Serializable {

  import TerraFrame.BLOCKSIZE

  var oldx: Double = x
  var oldy: Double = y

  var onGround, immune, grounded, onGroundDelay: Boolean = _
  @transient val image: BufferedImage                    = TerraFrame.itemImgs(id)

  val width: Int  = image.getWidth() * 2
  val height: Int = image.getHeight() * 2

  var ix            = x.toInt
  var iy            = y.toInt
  var ivx           = vx.toInt
  var ivy           = vy.toInt
  val rect          = new Rectangle(ix - 1, iy, width + 2, height)
  val intersectRect = new Rectangle(-1, -1, -1, -1)

  def this(x: Double, y: Double, vx: Double, vy: Double) {
    this(x, y, vx, vy, 0, 0, 0, 0)
  }

  def this(x: Double, y: Double, vx: Double, vy: Double, id: Short, num: Short) {
    this(x, y, vx, vy, id, num, 0, 0)
  }

  def this(x: Double, y: Double, vx: Double, vy: Double, id: Short, num: Short, mdelay: Int) {
    this(x, y, vx, vy, id, num, 0, mdelay)
  }

  def this(x: Double, y: Double, vx: Double, vy: Double, id: Short, num: Short, dur: Short) {
    this(x, y, vx, vy, id, num, dur, 0)
  }

  def update(blocks: Array2D[Int], player: Player, u: Int, v: Int): Boolean = {
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
    collide(blocks, u, v)
    mdelay -= 1
    false
  }

  def collide(blocks: Array2D[Int], u: Int, v: Int): Boolean = {
    var rv: Boolean = false

    grounded = onGround || onGroundDelay

    onGroundDelay = onGround

    oldx = x
    oldy = y

    x = x + vx

    ix = x.toInt
    iy = y.toInt
    ivx = vx.toInt
    ivy = vy.toInt

    rect.setBounds(ix - 1, iy, width + 2, height)

    val bx1: Int = max(0, (x / BLOCKSIZE).toInt)
    val by1: Int = max(0, (y / BLOCKSIZE).toInt)
    val bx2: Int = min(blocks(0).length - 1, ((x + width) / BLOCKSIZE).toInt)
    val by2: Int = min(blocks.length - 1, ((y + height) / BLOCKSIZE).toInt)

    (bx1 to bx2).foreach { i =>
      (by1 to by2).foreach { j =>
        if (blocks(j)(i) != 0 && TerraFrame.BLOCKCD.get(blocks(j + v)(i + u)).exists(identity)) {
          intersectRect.setBounds(i * BLOCKSIZE, j * BLOCKSIZE, BLOCKSIZE, BLOCKSIZE)
          if (rect.intersects(intersectRect)) {
            if (oldx <= i * 16 - width && (vx > 0)) {
              x = (i * 16 - width).toDouble
              vx = 0 // right
              rv = true
            }
            if (oldx >= i * 16 + BLOCKSIZE && (vx < 0)) {
              x = (i * 16 + BLOCKSIZE).toDouble
              vx = 0 // left
              rv = true
            }
          }
        }
      }
    }

    y = y + vy
    onGround = false

    ix = x.toInt
    iy = y.toInt
    ivx = vx.toInt
    ivy = vy.toInt

    rect.setBounds(ix, iy - 1, width, height + 2)

    (bx1 to bx2).foreach { i =>
      (by1 to by2).foreach { j =>
        if (blocks(j)(i) != 0 && TerraFrame.BLOCKCD.get(blocks(j + v)(i + u)).exists(identity)) {
          intersectRect.setBounds(i * BLOCKSIZE, j * BLOCKSIZE, BLOCKSIZE, BLOCKSIZE)
          if (rect.intersects(intersectRect)) {
            if (oldy <= j * 16 - height && (vy > 0)) {
              y = (j * 16 - height).toDouble
              onGround = true
              vy = 0 // down
              rv = true
            }
            if (oldy >= j * 16 + BLOCKSIZE && (vy < 0)) {
              y = (j * 16 + BLOCKSIZE).toDouble
              vy = 0 // up
              rv = true
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

  def reloadImage(): Unit = {
    ()
  }

}
