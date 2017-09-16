package org.terraframe

import enumeratum.values._

sealed abstract class Layer(val value: Int) extends IntEnumEntry {
  @inline def num: Int = value
}

case object Layer extends IntEnum[Layer] {

  case object BackgroundLayer extends Layer(0)
  case object PrimaryLayer extends Layer(1) // Only Layer with Collision
  case object ForegroundLayer extends Layer(2)

  val values = findValues.sortBy(_.num)

  def withNum(num: Int) = withValue(num)
}
