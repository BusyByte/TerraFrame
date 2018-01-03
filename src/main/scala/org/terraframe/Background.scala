package org.terraframe

import org.terraframe.Images.loadImage

object Background {
  def onBackgroundImage(background: Background)(f: BackgroundImage => Unit): Unit = background match {
    case bgi: BackgroundImage => f(bgi)
    case _                    => ()
  }
}

sealed abstract class Background(val imageName: String, val id: Short)
sealed abstract class BackgroundImage(imageName: String, id: Short) extends Background(imageName, id) {
  lazy val image = loadImage("backgrounds/" + imageName + ".png").get
}
object EmptyBackground              extends Background("solid/empty", 0)
object DirtNoneDownLeftBackground   extends Background("dirt_none/downleft", 1)
object DirtNoneDownRightBackground  extends Background("dirt_none/downright", 2)
object DirtNoneLeftBackground       extends Background("dirt_none/left", 3)
object DirtNoneRightBackground      extends Background("dirt_none/right", 4)
object DirtNoneNoneUpBackground     extends Background("dirt_none/up", 5)
object DirtNoneNoneUpLeftBackground extends Background("dirt_none/upleft", 6)
object DirtNoneUpRightBackground    extends Background("dirt_none/upright", 7)
object DirtBackground               extends Background("solid/dirt", 8)
object StoneDirtDownLeftBackground  extends Background("stone_dirt/downleft", 9)
object StoneDirtDownRightBackground extends Background("stone_dirt/downright", 10)
object StoneDirtLeftBackground      extends Background("stone_dirt/left", 11)
object StoneDirtRightBackground     extends Background("stone_dirt/right", 12)
object StoneDirtUpBackground        extends Background("stone_dirt/up", 13)
object StoneDirtUpLeftBackground    extends Background("stone_dirt/upleft", 14)
object StoneDirtUpRightBackground   extends Background("stone_dirt/upright", 15)
object StoneBackground              extends Background("solid/stone", 16)
object StoneNoneDownBackground      extends Background("stone_none/down", 17)
