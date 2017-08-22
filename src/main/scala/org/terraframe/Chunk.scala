package org.terraframe

import scala.util.Random

import org.terraframe.World.generateOutlines
import TerraFrame.{ CHUNKBLOCKS => size }
import TypeSafeComparisons._

case class Chunk(
    cx: Int,
    cy: Int,
    blocks: Array3D[BlockType],
    blockds: Array3D[Byte],
    blockdns: Array2D[Byte],
    blockbgs: Array2D[Background],
    blockts: Array2D[Byte],
    lights: Array2D[Float],
    power: Array3D[Float],
    lsources: Array2D[Boolean],
    zqn: Array2D[Byte],
    pzqn: Array3D[Byte],
    arbprd: Array3D[Boolean],
    wcnct: Array2D[Boolean],
    drawn: Array2D[Boolean],
    rdrawn: Array2D[Boolean],
    ldrawn: Array2D[Boolean]
)

object Chunk {

  def apply(cx: Int, cy: Int, random: Random): Chunk = {
    val blocks: Array3D[BlockType]       = Array.ofDim(3, size, size)
    val blockds: Array3D[Byte]     = Array.ofDim(3, size, size)
    val blockdns: Array2D[Byte]    = Array.ofDim(size, size)
    val blockbgs: Array2D[Background]    = Array.ofDim(size, size)
    val blockts: Array2D[Byte]     = Array.ofDim(size, size)
    val lights: Array2D[Float]     = Array.ofDim(size, size)
    val power: Array3D[Float]      = Array.ofDim(3, size, size)
    val lsources: Array2D[Boolean] = Array.ofDim(size, size)
    val zqn: Array2D[Byte]         = Array.ofDim(size, size)
    val pzqn: Array3D[Byte]        = Array.ofDim(3, size, size)
    val arbprd: Array3D[Boolean]   = Array.ofDim(3, size, size)
    val wcnct: Array2D[Boolean]    = Array.ofDim(size, size)
    val drawn: Array2D[Boolean]    = Array.ofDim(size, size)
    val rdrawn: Array2D[Boolean]   = Array.ofDim(size, size)
    val ldrawn: Array2D[Boolean]   = Array.ofDim(size, size)
    (0 until size).foreach { y =>
      (0 until size).foreach { x =>
        (0 until 3).foreach { l =>
          if (l === 1 && cy * size + y >= PerlinNoise.perlinNoise((cx * size + x) / 10.0, 0.5, 0) * 30 + 50) {
            blocks(l)(y)(x) = DirtBlockType // dirt
          } else {
            blocks(l)(y)(x) = AirBlockType
          }
          arbprd(l)(y)(x) = false
          power(l)(y)(x) = 0.toFloat
        }
        blockdns(y)(x) = random.nextInt(5).toByte
        blockbgs(y)(x) = EmptyBackground
        blockts(y)(x) = random.nextInt(8).toByte
        lights(y)(x) = 19.toFloat
        lsources(y)(x) = false
        wcnct(y)(x) = false
        drawn(y)(x) = false
        rdrawn(y)(x) = false
        ldrawn(y)(x) = false
        blockds(0)(y)(x) = 0.toByte
        blockds(2)(y)(x) = 0.toByte
      }
    }
    blockds(1) = generateOutlines(blocks(1))
    Chunk(
      cx,
      cy,
      blocks,
      blockds,
      blockdns,
      blockbgs,
      blockts,
      lights,
      power,
      lsources,
      zqn,
      pzqn,
      arbprd,
      wcnct,
      drawn,
      rdrawn,
      ldrawn
    )
  }

}
