package org.terraframe

object UserInput {
  object Implicits {
    implicit class QueueOps(queue: Array[Boolean]) {
      def isLeftKeyPressed: Boolean = queue(0)
      def isRightKeyPressed: Boolean = queue(1)
      def isUpKeyPressed: Boolean = queue(2)
      def isLeftMousePressed: Boolean = queue(3)
      def isRightMousePressed: Boolean = queue(4)
      def isShiftKeyPressed: Boolean = queue(5)
      def isDownKeyPressed: Boolean = queue(6)

      def setLeftKeyPressed(b: Boolean): Unit = queue(0) = b
      def setRightKeyPressed(b: Boolean): Unit = queue(1) = b
      def setUpKeyPressed(b: Boolean): Unit = queue(2) = b
      def setLeftMousePressed(b: Boolean): Unit = queue(3) = b
      def setRightMousePressed(b: Boolean): Unit = queue(4) = b
      def setShiftKeyPressed(b: Boolean): Unit = queue(5) = b
      def setDownKeyPressed(b: Boolean): Unit = queue(6) = b
    }
  }
}
