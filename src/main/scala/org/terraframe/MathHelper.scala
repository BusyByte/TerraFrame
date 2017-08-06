package org.terraframe

object MathHelper {
  def max(a: Int, b: Int, c: Int): Int = {
    Math.max(Math.max(a, b), c)
  }

  def max(a: Float, b: Float, c: Float): Float = {
    Math.max(Math.max(a, b), c)
  }

  def max(a: Double, b: Double, c: Double): Double = {
    Math.max(Math.max(a, b), c)
  }

  def max(a: Int, b: Int, c: Int, d: Int): Int = {
    Math.max(Math.max(a, b), Math.max(c, d))
  }

  def max(a: Float, b: Float, c: Float, d: Float): Float = {
    Math.max(Math.max(a, b), Math.max(c, d))
  }

  def max(a: Double, b: Double, c: Double, d: Double): Double = {
    Math.max(Math.max(a, b), Math.max(c, d))
  }

  def mod(a: Int, q: Int): Int = {
    ((a % q) + q) % q
  }
}
