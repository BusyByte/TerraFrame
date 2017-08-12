package org.terraframe

import java.awt.image._
import java.io.Serializable

sealed trait ItemCollectionType
case object Crafting        extends ItemCollectionType //old name CIC
case object Armor           extends ItemCollectionType
case object Workbench       extends ItemCollectionType
case object WoodenChest     extends ItemCollectionType
case object StoneChest      extends ItemCollectionType
case object CopperChest     extends ItemCollectionType
case object IronChest       extends ItemCollectionType
case object SilverChest     extends ItemCollectionType
case object GoldChest       extends ItemCollectionType
case object ZincChest       extends ItemCollectionType
case object RhymestoneChest extends ItemCollectionType
case object ObduriteChest   extends ItemCollectionType
case object Furnace         extends ItemCollectionType

//TODO: should is be an Array[Int] to avoid converting Short to Int every time if only used for indexing?
case class ItemCollection(icType: ItemCollectionType, ids: Array[Short], nums: Array[Short], durs: Array[Short])
    extends Serializable {
  @transient var image: BufferedImage = _
  var FUELP: Double                   = 0
  var SMELTP: Double                  = 0
  var F_ON: Boolean                   = false
}
