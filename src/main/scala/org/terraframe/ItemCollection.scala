package org.terraframe

import java.awt.image._
import java.io.Serializable

import org.terraframe.Images.loadImage

object ChestItemCollection {
  def unapply(icType: ItemCollectionType): Option[ItemCollectionType] = icType match {
    case WoodenChest    => Some(icType)
    case StoneChest     => Some(icType)
    case CopperChest    => Some(icType)
    case IronChest      => Some(icType)
    case SilverChest    => Some(icType)
    case GoldChest      => Some(icType)
    case ZincChest      => Some(icType)
    case RhymestoneChest => Some(icType)
    case ObduriteChest  => Some(icType)
    case _ => None
  }
}

sealed abstract class ItemCollectionType(val collectionSize: Int) {
  def image: BufferedImage
}
case object Crafting        extends ItemCollectionType(5) {
  lazy val image = loadImage("interface/cic.png").get
} //old name CIC
case object Armor           extends ItemCollectionType(4) {
  lazy val image = loadImage("interface/armor.png").get
}
case object Workbench       extends ItemCollectionType(10) {
  lazy val image = loadImage("interface/workbench.png").get
}
case object WoodenChest     extends ItemCollectionType(9) {
  lazy val image = loadImage("interface/wooden_chest.png").get
}
case object StoneChest      extends ItemCollectionType(15) {
  lazy val image = loadImage("interface/stone_chest.png").get
}
case object CopperChest     extends ItemCollectionType(20) {
  lazy val image = loadImage("interface/copper_chest.png").get
}
case object IronChest       extends ItemCollectionType(28) {
  lazy val image = loadImage("interface/iron_chest.png").get
}
case object SilverChest     extends ItemCollectionType(35) {
  lazy val image = loadImage("interface/silver_chest.png").get
}
case object GoldChest       extends ItemCollectionType(42) {
  lazy val image = loadImage("interface/gold_chest.png").get
}
case object ZincChest       extends ItemCollectionType(56) {
  lazy val image = loadImage("interface/zinc_chest.png").get
}
case object RhymestoneChest extends ItemCollectionType(72) {
  lazy val image = loadImage("interface/rhymestone_chest.png").get
}
case object ObduriteChest   extends ItemCollectionType(100) {
  lazy val image = loadImage("interface/obdurite_chest.png").get
}
case object Furnace         extends ItemCollectionType(4) {
  lazy val image = loadImage("interface/furnace.png").get
}

case class ItemCollection(icType: ItemCollectionType, ids: Array[UiItem], nums: Array[Short], durs: Array[Short])
    extends Serializable {

  def this(icType: ItemCollectionType) =
    this(
      icType,
      Array.fill(icType.collectionSize)(EmptyUiItem),
      Array.fill(icType.collectionSize)(0),
      Array.fill(icType.collectionSize)(0))

  var FUELP: Double                   = 0
  var SMELTP: Double                  = 0
  var F_ON: Boolean                   = false
}
