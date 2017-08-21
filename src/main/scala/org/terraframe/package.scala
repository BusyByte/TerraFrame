package org

package object terraframe {
  type Array3D[T] = Array[Array[Array[T]]]
  type Array2D[T] = Array[Array[T]]
}

package terraframe {
  object TypeSafeComparisons {
    @SuppressWarnings(Array("org.wartremover.warts.Equals"))
    implicit final class AnyOps[A](self: A) {
      def ===(other: A): Boolean = self == other
      def =/=(other: A): Boolean = !(self === other)
    }
  }
}