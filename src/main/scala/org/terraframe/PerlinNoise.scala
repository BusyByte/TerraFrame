package org.terraframe

object PerlinNoise {
  def perlinNoise(x: Double, p: Double, n: Int): Double = {
    var total: Double      = 0
    var freq, ampl: Double = 0
    (0 until n + 1).foreach { i =>
      freq = math.pow(2.toDouble, i.toDouble)
      ampl = math.pow(p, i.toDouble)
      total = total + interpolateNoise(x * freq) * ampl
    }
    total
  }

  def interpolateNoise(x: Double): Double = {
    val ix: Int    = x.toInt
    val fx: Double = x - ix
    val v1: Double = smoothNoise(ix)
    val v2: Double = smoothNoise(ix + 1)
    interpolate(v1, v2, fx)
  }

  def smoothNoise(x: Int): Double = {
    noise(x) / 2 + noise(x - 1) / 4 + noise(x + 1) / 4
  }

  def noise(x: Int): Double = {
    val x2 = (x << 13) ^ x
    1.0 - ((x2 * (x2 * x2 * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0
  }

  def interpolate(a: Double, b: Double, x: Double): Double = {
    val ft: Double = x * Math.PI
    val f: Double  = (1 - Math.cos(ft)) / 2
    a * (1 - f) + b * f
  }
}
