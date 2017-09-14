package org.terraframe

import enumeratum.values._

sealed abstract class Layer(val value: Int) extends IntEnumEntry {
  @inline def num: Int = value // TODO: search for references and make sure can't be replace with enum value
}

case object Layer extends IntEnum[Layer] {

  case object Layer0 extends Layer(0)
  case object Layer1 extends Layer(1)
  case object Layer2 extends Layer(2)

  val values = findValues.sortBy(_.num) // TODO: need spec to make sure these return in order

  def withNum(num: Int) = withValue(num)
}
