package org.terraframe

object MathHelper {
  @inline def max(a: Int, b: Int, c: Int): Int = {
    math.max(math.max(a, b), c)
  }

  @inline def max(a: Float, b: Float, c: Float): Float = {
    math.max(math.max(a, b), c)
  }

  @inline def mod(a: Int, q: Int): Int = {
    ((a % q) + q) % q
  }
}
