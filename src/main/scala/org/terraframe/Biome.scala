package org.terraframe
import TypeSafeComparisons._
import TerraFrame.{ HEIGHT, WIDTH }
import org.terraframe.Layer.PrimaryLayer

sealed abstract class Biome(val name: String)
object DesertBiome extends Biome("desert")
object JungleBiome extends Biome("jungle")
object SwampBiome  extends Biome("swamp")
object FrostBiome  extends Biome("frost")
object CavernBiome extends Biome("cavern")
object OtherBiome  extends Biome("other")

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
          if (blocks(PrimaryLayer.num)(y2 + v)(x2 + u) === SandBlockType || blocks(PrimaryLayer.num)(y2 + v)(x2 + u) === SandstoneBlockType) {
            desert += 1
          } else if (blocks(PrimaryLayer.num)(y2 + v)(x2 + u) =/= AirBlockType) {
            desert -= 1
          }
          if (blocks(PrimaryLayer.num)(y2 + v)(x2 + u) === DirtBlockType || blocks(PrimaryLayer.num)(y2 + v)(x2 + u) === GrassBlockType || blocks(
                PrimaryLayer.num)(y2 + v)(x2 + u) === JungleGrassBlockType) {
            jungle += 1
          } else if (blocks(PrimaryLayer.num)(y2 + v)(x2 + u) =/= AirBlockType) {
            jungle -= 1
          }
          if (blocks(PrimaryLayer.num)(y2 + v)(x2 + u) === SwampGrassBlockType || blocks(PrimaryLayer.num)(y2 + v)(
                x2 + u) === MudBlockType) {
            swamp += 1
          } else if (blocks(PrimaryLayer.num)(y2 + v)(x2 + u) =/= AirBlockType) {
            swamp -= 1
          }
          if (blocks(PrimaryLayer.num)(y2 + v)(x2 + u) === SnowBlockType) {
            frost += 1
          } else if (blocks(PrimaryLayer.num)(y2 + v)(x2 + u) =/= AirBlockType) {
            frost -= 1
          }
          if (blockbgs(y2 + v)(x2 + u) === EmptyBackground) {
            cavern += 1
          }
          if (blocks(PrimaryLayer.num)(y2 + v)(x2 + u) === DirtBlockType || blocks(PrimaryLayer.num)(y2 + v)(x2 + u) === StoneBlockType) {
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
