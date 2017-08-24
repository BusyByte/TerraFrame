package org.terraframe
import TypeSafeComparisons._
import TerraFrame.{WIDTH, HEIGHT}


sealed trait Biome
object DesertBiome extends Biome
object JungleBiome extends Biome
object SwampBiome extends Biome
object FrostBiome extends Biome
object CavernBiome extends Biome
object OtherBiome extends Biome


object Biome {
  def checkBiome(x: Int, y: Int, u: Int, v: Int, blocks: Array3D[BlockType], blockbgs: Array2D[Background]): Biome = {
    var desert: Int = 0
    var frost: Int  = 0
    var swamp: Int  = 0
    var jungle: Int = 0
    var cavern: Int = 0
    (x - 15 until x + 16).foreach { x2 =>
      (y - 15 until y + 16).foreach { y2 =>
        if (x2 + u >= 0 && x2 + u < WIDTH && y2 + v >= 0 && y2 + v < HEIGHT) {
          if (blocks(1)(y2 + v)(x2 + u) === SandBlockType || blocks(1)(y2 + v)(x2 + u) === SandstoneBlockType) {
            desert += 1
          } else if (blocks(1)(y2 + v)(x2 + u) =/= AirBlockType) {
            desert -= 1
          }
          if (blocks(1)(y2 + v)(x2 + u) === DirtBlockType || blocks(1)(y2 + v)(x2 + u) === GrassBlockType || blocks(1)(
            y2 + v)(x2 + u) === JungleGrassBlockType) {
            jungle += 1
          } else if (blocks(1)(y2 + v)(x2 + u) =/= AirBlockType) {
            jungle -= 1
          }
          if (blocks(1)(y2 + v)(x2 + u) === SwampGrassBlockType || blocks(1)(y2 + v)(x2 + u) === MudBlockType) {
            swamp += 1
          } else if (blocks(1)(y2 + v)(x2 + u) =/= AirBlockType) {
            swamp -= 1
          }
          if (blocks(1)(y2 + v)(x2 + u) === SnowBlockType) {
            frost += 1
          } else if (blocks(1)(y2 + v)(x2 + u) =/= AirBlockType) {
            frost -= 1
          }
          if (blockbgs(y2 + v)(x2 + u) === EmptyBackground) {
            cavern += 1
          }
          if (blocks(1)(y2 + v)(x2 + u) === DirtBlockType || blocks(1)(y2 + v)(x2 + u) === StoneBlockType) {
            cavern += 1
          } else {
            cavern -= 1
          }
        }
      }
    }
    if (desert > 0) {
      DesertBiome
    } else if (jungle > 0) {
      JungleBiome
    } else if (swamp > 0) {
      SwampBiome
    } else if (frost > 0) {
      FrostBiome
    } else if (cavern > 0) {
      CavernBiome
    } else {
      OtherBiome
    }
  }
}
