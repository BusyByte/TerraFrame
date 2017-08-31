package org.terraframe


import java.awt.Rectangle
import java.awt.image._
import java.io.Serializable

import Images.loadImage

import scala.math._
import scala.util.Random
import TypeSafeComparisons._

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
    val ai: AI,
    val drops: Drops
)
import Drops._
case object BlueBubble extends EntityStrategy("blue_bubble", 18, 0, 2, BubbleAI, BlueBubbleDrops)

case object GreenBubble extends EntityStrategy("green_bubble", 25, 0, 4, BubbleAI, GreenBubbleDrops)

case object RedBubble extends EntityStrategy("red_bubble", 40, 0, 6, BubbleAI, RedBubbleDrops)

case object YellowBubble extends EntityStrategy("yellow_bubble", 65, 1, 9, BubbleAI, YellowBubbleDrops)

case object BlackBubble extends EntityStrategy("black_bubble", 100, 3, 14, BubbleAI, BlackBubbleDrops)

case object WhiteBubble extends EntityStrategy("white_bubble", 70, 2, 11, FastBubbleAI, WhiteBubbleDrops)

case object Zombie extends EntityStrategy("zombie", 35, 0, 5, ZombieAI, ZombieDrops)

case object ArmoredZombie extends EntityStrategy("armored_zombie", 45, 2, 7, ZombieAI, ArmoredZombieDrops)

case object ShootingStar extends EntityStrategy("shooting_star", 25, 0, 5, ShootingStarAI, ShootingStarDrops)

case object Sandbot extends EntityStrategy("sandbot", 50, 2, 3, SandbotAI, SandbotDrops)

case object SandbotBullet extends EntityStrategy("sandbot_bullet", 1, 0, 7, BulletAI, SandbotBulletDrops)

case object Snowman extends EntityStrategy("snowman", 40, 0, 6, ZombieAI, SnowmanDrops)

case object Bat extends EntityStrategy("bat", 15, 0, 5, BatAI, BatDrops)

case object Bee extends EntityStrategy("bee", 1, 0, 5, BeeAI, BeeDrops)

case object Skeleton extends EntityStrategy("skeleton", 50, 1, 7, ZombieAI, SkeletonDrops)


trait Drops {
  def generateDrops(random: Random): List[ImageUiItem]
}
object Drops {
  def addWithOneInNChance(random: Random, n: Int, imageUiItem: ImageUiItem): List[ImageUiItem] = {
    if(random.nextInt(n) === 0) {
      List(imageUiItem)
    } else {
      List.empty
    }
  }

  def fillRandomN(random: Random, n: Int, imageUiItem: ImageUiItem): List[ImageUiItem] = {
    List.fill(random.nextInt(n))(imageUiItem)
  }


  object BlueBubbleDrops extends Drops {
    override def generateDrops(random: Random): List[ImageUiItem] = {
      fillRandomN(random, 3, BlueGooUiItem)
    }
  }

   object GreenBubbleDrops extends Drops {
    override def generateDrops(random: Random): List[ImageUiItem] = {
      fillRandomN(random, 3, GreenGooUiItem)
    }
  }

   object RedBubbleDrops extends Drops {
    override def generateDrops(random: Random): List[ImageUiItem] = {
      fillRandomN(random, 3, RedGooUiItem)
    }
  }

   object YellowBubbleDrops extends Drops {
    override def generateDrops(random: Random): List[ImageUiItem] = {
      fillRandomN(random, 3, YellowGooUiItem)
    }
  }

   object BlackBubbleDrops extends Drops {
    override def generateDrops(random: Random): List[ImageUiItem] = {
      fillRandomN(random, 3, BlackGooUiItem)
    }
  }

   object WhiteBubbleDrops extends Drops {
    override def generateDrops(random: Random): List[ImageUiItem] = {
      fillRandomN(random, 3, WhiteGooUiItem)
    }
  }

   object ShootingStarDrops extends Drops {
    override def generateDrops(random: Random): List[ImageUiItem] = {
      fillRandomN(random, 2, AstralShardUiItem)
    }
  }

   object ZombieDrops extends Drops {
    override def generateDrops(random: Random): List[ImageUiItem] = {
      fillRandomN(random, 3, RottenChunkUiItem)
    }
  }

   object ArmoredZombieDrops extends Drops {
    override def generateDrops(random: Random): List[ImageUiItem] = {
      fillRandomN(random, 3, RottenChunkUiItem) ++
      addWithOneInNChance(random, 15, IronHelmetUiItem) ++
      addWithOneInNChance(random, 15, IronChestplateUiItem)
      addWithOneInNChance(random, 15, IronLeggingsUiItem)
      addWithOneInNChance(random, 15, IronGreavesUiItem)
    }
  }


   object SandbotDrops extends Drops {
    override def generateDrops(random: Random): List[ImageUiItem] = {
      fillRandomN(random, 3, SandUiItem) ++
      addWithOneInNChance(random, 2, ZythiumOreUiItem) ++
      addWithOneInNChance(random, 6,SiliconOreUiItem)

    }
  }

   object SnowmanDrops extends Drops {
    override def generateDrops(random: Random): List[ImageUiItem] = {
      fillRandomN(random, 3, SnowUiItem)
    }
  }

   object BatDrops extends Drops {
    override def generateDrops(random: Random): List[ImageUiItem] = {
      List.empty //TODO: figure out what this drops
    }
  }

   object BeeDrops extends Drops {
    override def generateDrops(random: Random): List[ImageUiItem] = {
      List.empty //TODO: figure out what this drops
    }
  }

   object SkeletonDrops extends Drops {
    override def generateDrops(random: Random): List[ImageUiItem] = {
      List.empty //TODO: figure out what this drops
    }
  }

   object SandbotBulletDrops extends Drops {
    override def generateDrops(random: Random): List[ImageUiItem] = {
      List.empty //TODO: figure out what this drops
    }
  }

}

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

  def update(blocks: Array2D[BlockType], player: Player, u: Int, v: Int): Boolean
}

object AIEntity {
  sealed trait ImageState
  object StillLeft extends ImageState
  object StillRight extends ImageState
  object WalkRight1 extends ImageState
  object WalkRight2 extends ImageState
  object WalkLeft1 extends ImageState
  object WalkLeft2 extends ImageState
  object NormalRight extends ImageState
  object NormalLeft extends ImageState
  object FlapLeft extends ImageState
  object FlapRight extends ImageState

}

case class AIEntity(var x: Double, var y: Double, var vx: Double, var vy: Double, strategy: EntityStrategy)
    extends Entity
    with Serializable {
   import AIEntity._

  import TerraFrame.BLOCKSIZE

  //Begin constructor
  var oldx: Double = x
  var oldy: Double = y

  var nohit: Boolean = false

  var imgState: ImageState                                   = _
  var onGround, immune, grounded, onGroundDelay: Boolean = _

  var newMob: Option[Entity] = None
  @transient var image: BufferedImage = strategy.ai match {
    case BubbleAI | FastBubbleAI | ShootingStarAI | SandbotAI | BulletAI | BeeAI =>
      loadImage("sprites/monsters/" + strategy.imageName + "/normal.png").get

    case ZombieAI =>
      loadImage("sprites/monsters/" + strategy.imageName + "/right_still.png").get

    case BatAI =>
      loadImage("sprites/monsters/" + strategy.imageName + "/normal_right.png").get

  }

  val width: Int  = image.getWidth() * 2
  val height: Int = image.getHeight() * 2

  imgState = strategy.ai match {
    case BatAI => NormalRight
    case _     => StillRight
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

  def update(blocks: Array2D[BlockType], player: Player, u: Int, v: Int): Boolean = {
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
          if (imgState === StillLeft || imgState === StillRight ||
              imgState === WalkRight1 || imgState === WalkRight2) {
            imgDelay = 10
            imgState = WalkLeft2
            image = loadImage("sprites/monsters/" + strategy.imageName + "/left_walk.png").get
          }
          if (imgDelay <= 0) {
            if (imgState === WalkLeft1) {
              imgDelay = 10
              imgState = WalkLeft2
              image = loadImage("sprites/monsters/" + strategy.imageName + "/left_walk.png").get
            } else {
              if (imgState === WalkLeft2) {
                imgDelay = 10
                imgState = WalkLeft1
                image = loadImage("sprites/monsters/" + strategy.imageName + "/left_still.png").get
              }
            }
          } else {
            imgDelay = imgDelay - 1
          }
        } else {
          vx = min(vx + 0.1, 1.2)
          if (imgState === StillLeft || imgState === StillRight ||
              imgState === WalkLeft1 || imgState === WalkLeft2) {
            imgDelay = 10
            imgState = WalkRight2
            image = loadImage("sprites/monsters/" + strategy.imageName + "/right_walk.png").get
          }
          if (imgDelay <= 0) {
            if (imgState === WalkRight1) {
              imgDelay = 10
              imgState = WalkRight2
              image = loadImage("sprites/monsters/" + strategy.imageName + "/right_walk.png").get
            } else {
              if (imgState === WalkRight2) {
                imgDelay = 10
                imgState = WalkRight1
                image = loadImage("sprites/monsters/" + strategy.imageName + "/right_still.png").get
              }
            }
          } else {
            imgDelay = imgDelay - 1
          }
        }
        if (!grounded) {
          if (imgState === StillLeft|| imgState === WalkLeft1 ||
              imgState === WalkLeft2) {
            image = loadImage("sprites/monsters/" + strategy.imageName + "/left_jump.png").get
          }
          if (imgState === StillRight || imgState === WalkRight1 ||
              imgState === WalkRight2) {
            image = loadImage("sprites/monsters/" + strategy.imageName + "/right_jump.png").get
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
        if (bcount === 110) {
          image = loadImage("sprites/monsters/" + strategy.imageName + "/ready1.png").get
        }
        if (bcount === 130) {
          image = loadImage("sprites/monsters/" + strategy.imageName + "/ready2.png").get
        }
        if (bcount === 150) {
          val theta: Double = atan2(player.y - y, player.x - x)
          newMob = Some(AIEntity(x, y, cos(theta) * 3.5, sin(theta) * 3.5, SandbotBullet))
        }
        if (bcount === 170) {
          image = loadImage("sprites/monsters/" + strategy.imageName + "/ready1.png").get
        }
        if (bcount === 190) {
          image = loadImage("sprites/monsters/" + strategy.imageName + "/normal.png").get
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
        if (vx > 0 && imgState =/= NormalRight) {
          imgState = NormalRight
          image = loadImage("sprites/monsters/" + strategy.imageName + "/normal_right.png").get
          imgDelay = 10
        }
        if (vx < 0 && imgState =/= NormalLeft) {
          imgState = NormalLeft
          image = loadImage("sprites/monsters/" + strategy.imageName + "/normal_left.png").get
          imgDelay = 10
        }
        if (imgState === NormalLeft && imgDelay <= 0) {
          imgState = FlapLeft
          image = loadImage("sprites/monsters/" + strategy.imageName + "/flap_left.png").get
          imgDelay = 10
        }
        if (imgState === NormalRight && imgDelay <= 0) {
          imgState = FlapRight
          image = loadImage("sprites/monsters/" + strategy.imageName + "/flap_right.png").get
          imgDelay = 10
        }
        if (imgState === FlapLeft && imgDelay <= 0) {
          imgState = NormalLeft
          image = loadImage("sprites/monsters/" + strategy.imageName + "/normal_left.png").get
          imgDelay = 10
        }
        if (imgState === FlapRight && imgDelay <= 0) {
          imgState = NormalRight
          image = loadImage("sprites/monsters/" + strategy.imageName + "/normal_right.png").get
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

  def collide(blocks: Array2D[BlockType], player: Player, u: Int, v: Int): Boolean = {
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
        if (blocks(j)(i) =/= AirBlockType && TerraFrame.BLOCKCD.get(blocks(j + v)(i + u).id).exists(identity)) {
          intersectRect.setBounds(i * BLOCKSIZE, j * BLOCKSIZE, BLOCKSIZE, BLOCKSIZE)
          if (rect.intersects(intersectRect)) {
            if (oldx <= i * 16 - width && (vx > 0 || strategy.ai === ShootingStarAI)) {
              x = (i * 16 - width).toDouble
              if (strategy.ai === BubbleAI) {
                vx = -vx
              } else if (strategy.ai === ZombieAI) {
                vx = 0
                if (onGround && player.x > x) {
                  vy = -7
                }
              } else if (strategy.ai === BatAI) {
                vx = -vx
              } else {
                vx = 0 // right
              }
              rv = true
            }
            if (oldx >= i * 16 + BLOCKSIZE && (vx < 0 || strategy.ai === ShootingStarAI)) {
              x = (i * 16 + BLOCKSIZE).toDouble
              if (strategy.ai === BubbleAI) {
                vx = -vx
              } else if (strategy.ai === ZombieAI) {
                vx = 0
                if (onGround && player.x < x) {
                  vy = -7
                }
              } else if (strategy.ai === BatAI) {
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
        if (blocks(j)(i) =/= AirBlockType && TerraFrame.BLOCKCD.get(blocks(j + v)(i + u).id).exists(identity)) {
          intersectRect.setBounds(i * BLOCKSIZE, j * BLOCKSIZE, BLOCKSIZE, BLOCKSIZE)
          if (rect.intersects(intersectRect)) {
            if (oldy <= j * 16 - height && (vy > 0 || strategy.ai === ShootingStarAI)) {
              y = (j * 16 - height).toDouble
              onGround = true
              if (strategy.ai === BubbleAI) {
                vy = -vy
              } else {
                vy = 0 // down
              }
              rv = true
            }
            if (oldy >= j * 16 + BLOCKSIZE && (vy < 0 || strategy.ai === ShootingStarAI)) {
              y = (j * 16 + BLOCKSIZE).toDouble
              if (strategy.ai === BubbleAI) {
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
      if (strategy.ai === ShootingStarAI) {
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

  def drops(random: Random): List[ImageUiItem] = {
    strategy.drops.generateDrops(random)
  }



  def reloadImage(): Unit = {
    if (strategy.ai === BubbleAI || strategy.ai === ShootingStarAI) {
      image = loadImage("sprites/monsters/" + strategy.imageName + "/normal.png").get // TODO: could move image to strategy
    }
    if (strategy.ai === ZombieAI) {
      image = loadImage("sprites/monsters/" + strategy.imageName + "/right_still.png").get
    }
  }

}

case class IdEntity(var x: Double,
                    var y: Double,
                    var vx: Double,
                    var vy: Double,
                    id: ImageUiItem,//TODO: rename
                    num: Short,
                    dur: Short,
                    var mdelay: Int)
    extends Entity
    with Serializable {

  import TerraFrame.BLOCKSIZE

  var oldx: Double = x
  var oldy: Double = y

  var onGround, immune, grounded, onGroundDelay: Boolean = false
  @transient val image: BufferedImage                    = id.image

  val width: Int  = image.getWidth() * 2
  val height: Int = image.getHeight() * 2

  var ix            = x.toInt
  var iy            = y.toInt
  var ivx           = vx.toInt
  var ivy           = vy.toInt
  val rect          = new Rectangle(ix - 1, iy, width + 2, height)
  val intersectRect = new Rectangle(-1, -1, -1, -1)


  def this(x: Double, y: Double, vx: Double, vy: Double, id: ImageUiItem, num: Short) {
    this(x, y, vx, vy, id, num, 0, 0)
  }

  def this(x: Double, y: Double, vx: Double, vy: Double, id: ImageUiItem, num: Short, mdelay: Int) {
    this(x, y, vx, vy, id, num, 0, mdelay)
  }

  def this(x: Double, y: Double, vx: Double, vy: Double, id: ImageUiItem, num: Short, dur: Short) {
    this(x, y, vx, vy, id, num, dur, 0)
  }

  def update(blocks: Array2D[BlockType], player: Player, u: Int, v: Int): Boolean = {
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

  def collide(blocks: Array2D[BlockType], u: Int, v: Int): Boolean = {
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
        if (blocks(j)(i) =/= AirBlockType && TerraFrame.BLOCKCD.get(blocks(j + v)(i + u).id).exists(identity)) {
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
        if (blocks(j)(i) =/= AirBlockType && TerraFrame.BLOCKCD.get(blocks(j + v)(i + u).id).exists(identity)) {
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
