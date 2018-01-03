package org.terraframe

import java.awt.image._
import java.io.Serializable

import org.terraframe.Images.loadImage

object ChestItemCollectionType {
  def unapply(icType: ItemCollectionType): Option[ItemCollectionType] = icType match {
    case WoodenChest     => Some(icType)
    case StoneChest      => Some(icType)
    case CopperChest     => Some(icType)
    case IronChest       => Some(icType)
    case SilverChest     => Some(icType)
    case GoldChest       => Some(icType)
    case ZincChest       => Some(icType)
    case RhymestoneChest => Some(icType)
    case ObduriteChest   => Some(icType)
    case _               => None
  }
}

sealed abstract class ItemCollectionType(val collectionSize: Int, val CX: Int, val CY: Int) {
  def image: BufferedImage
}
case object Crafting extends ItemCollectionType(5, 2, 2) { // 2 X 2 + 1
  lazy val image = loadImage("interface/cic.png").get
} //old name CIC
case object Armor extends ItemCollectionType(4, 1, 4) {
  lazy val image = loadImage("interface/armor.png").get
}
case object Workbench extends ItemCollectionType(10, 3, 3) { // 3 X 3 + 1
  lazy val image = loadImage("interface/workbench.png").get
}
case object WoodenChest extends ItemCollectionType(9, 3, 3) {
  lazy val image = loadImage("interface/wooden_chest.png").get
}
case object StoneChest extends ItemCollectionType(15, 5, 3) {
  lazy val image = loadImage("interface/stone_chest.png").get
}
case object CopperChest extends ItemCollectionType(20, 5, 4) {
  lazy val image = loadImage("interface/copper_chest.png").get
}
case object IronChest extends ItemCollectionType(28, 7, 4) {
  lazy val image = loadImage("interface/iron_chest.png").get
}
case object SilverChest extends ItemCollectionType(35, 7, 5) {
  lazy val image = loadImage("interface/silver_chest.png").get
}
case object GoldChest extends ItemCollectionType(42, 7, 6) {
  lazy val image = loadImage("interface/gold_chest.png").get
}
case object ZincChest extends ItemCollectionType(56, 7, 8) {
  lazy val image = loadImage("interface/zinc_chest.png").get
}
case object RhymestoneChest extends ItemCollectionType(72, 8, 9) {
  lazy val image = loadImage("interface/rhymestone_chest.png").get
}
case object ObduriteChest extends ItemCollectionType(100, 10, 10) {
  lazy val image = loadImage("interface/obdurite_chest.png").get
}
case object Furnace extends ItemCollectionType(4, 2, 3) {
  lazy val image = loadImage("interface/furnace.png").get
}

case class ItemCollection(icType: ItemCollectionType,
                          ids: Array[UiItem],
                          nums: Array[Short],
                          durs: Array[Short],
                          fuelPower: Double,
                          smeltPower: Double,
                          furnaceOn: Boolean)
    extends Serializable {

  def this(icType: ItemCollectionType) =
    this(
      icType,
      Array.fill(icType.collectionSize)(EmptyUiItem),
      Array.fill(icType.collectionSize)(0),
      Array.fill(icType.collectionSize)(0),
      0,
      0,
      false)

  def this(icType: ItemCollectionType, ids: Array[UiItem], nums: Array[Short], durs: Array[Short]) =
    this(icType, ids, nums, durs, 0, 0, false)
}
