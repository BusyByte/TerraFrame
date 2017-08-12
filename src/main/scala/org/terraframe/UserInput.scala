package org.terraframe

case class UserInput() {
  private[this] val queue
    : Array[Boolean]                     = Array.ofDim(7) // left(0) right(1) up(2) mouse(3) rightmouse(4) shift(5) down(6)
  private[this] val mousePos: Array[Int] = Array.ofDim(2)

  def isLeftKeyPressed: Boolean    = queue(0)
  def isRightKeyPressed: Boolean   = queue(1)
  def isUpKeyPressed: Boolean      = queue(2)
  def isLeftMousePressed: Boolean  = queue(3)
  def isRightMousePressed: Boolean = queue(4)
  def isShiftKeyPressed: Boolean   = queue(5)
  def isDownKeyPressed: Boolean    = queue(6)

  def setLeftKeyPressed(b: Boolean): Unit    = queue(0) = b
  def setRightKeyPressed(b: Boolean): Unit   = queue(1) = b
  def setUpKeyPressed(b: Boolean): Unit      = queue(2) = b
  def setLeftMousePressed(b: Boolean): Unit  = queue(3) = b
  def setRightMousePressed(b: Boolean): Unit = queue(4) = b
  def setShiftKeyPressed(b: Boolean): Unit   = queue(5) = b
  def setDownKeyPressed(b: Boolean): Unit    = queue(6) = b

  def currentMousePosition: (Int, Int) = (mousePos(0), mousePos(1))

  def setCurrentMousePosition(x: Int, y: Int): Unit = {
    mousePos(0) = x
    mousePos(1) = y
  }
}
