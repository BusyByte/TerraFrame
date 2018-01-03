package org.terraframe
import TypeSafeComparisons._
import TerraFrame.{ HEIGHT, WIDTH }
import org.terraframe.Layer.PrimaryLayer
import Block._

sealed abstract class Biome(val name: String)
object DesertBiome extends Biome("desert")
object JungleBiome extends Biome("jungle")
object SwampBiome  extends Biome("swamp")
object FrostBiome  extends Biome("frost")
object CavernBiome extends Biome("cavern")
object OtherBiome  extends Biome("other")

object Biome {
  def checkBiome(x: Int, y: Int, u: Int, v: Int, blocks: Array3D[Block], blockbgs: Array2D[Background]): Biome = {
    var desert: Int = 0
    var frost: Int  = 0
    var swamp: Int  = 0
    var jungle: Int = 0
    var cavern: Int = 0
    (x - 15 until x + 16).foreach { x2 =>
      (y - 15 until y + 16).foreach { y2 =>
        if (x2 + u >= 0 && x2 + u < WIDTH && y2 + v >= 0 && y2 + v < HEIGHT) {
          if (blocks(PrimaryLayer.num)(y2 + v)(x2 + u) === SandBlock || blocks(PrimaryLayer.num)(y2 + v)(x2 + u) === SandstoneBlock) {
            desert += 1
          } else if (blocks(PrimaryLayer.num)(y2 + v)(x2 + u) =/= AirBlock) {
            desert -= 1
          }
          if (blocks(PrimaryLayer.num)(y2 + v)(x2 + u) === DirtBlock || blocks(PrimaryLayer.num)(y2 + v)(x2 + u) === GrassBlock || blocks(
                PrimaryLayer.num)(y2 + v)(x2 + u) === JungleGrassBlock) {
            jungle += 1
          } else if (blocks(PrimaryLayer.num)(y2 + v)(x2 + u) =/= AirBlock) {
            jungle -= 1
          }
          if (blocks(PrimaryLayer.num)(y2 + v)(x2 + u) === SwampGrassBlock || blocks(PrimaryLayer.num)(y2 + v)(x2 + u) === MudBlock) {
            swamp += 1
          } else if (blocks(PrimaryLayer.num)(y2 + v)(x2 + u) =/= AirBlock) {
            swamp -= 1
          }
          if (blocks(PrimaryLayer.num)(y2 + v)(x2 + u) === SnowBlock) {
            frost += 1
          } else if (blocks(PrimaryLayer.num)(y2 + v)(x2 + u) =/= AirBlock) {
            frost -= 1
          }
          if (blockbgs(y2 + v)(x2 + u) === EmptyBackground) {
            cavern += 1
          }
          if (blocks(PrimaryLayer.num)(y2 + v)(x2 + u) === DirtBlock || blocks(PrimaryLayer.num)(y2 + v)(x2 + u) === StoneBlock) {
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
