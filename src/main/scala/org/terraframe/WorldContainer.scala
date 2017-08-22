package org.terraframe

import java.io.Serializable

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

case class WorldContainer(blocks: Array3D[BlockType],
                          blockds: Array3D[Byte],
                          blockdns: Array2D[Byte],
                          blockbgs: Array2D[Background],
                          blockts: Array2D[Byte],
                          lights: Array2D[Float],
                          power: Array3D[Float],
                          drawn: Array2D[Boolean],
                          ldrawn: Array2D[Boolean],
                          rdrawn: Array2D[Boolean],
                          player: Player,
                          inventory: Inventory,
                          cic: Option[ItemCollection],
                          entities: ArrayBuffer[Entity],
                          cloudsx: ArrayBuffer[Double],
                          cloudsy: ArrayBuffer[Double],
                          cloudsv: ArrayBuffer[Double],
                          cloudsn: ArrayBuffer[Int],
                          machinesx: ArrayBuffer[Int],
                          machinesy: ArrayBuffer[Int],
                          lsources: Array2D[Boolean],
                          lqx: ArrayBuffer[Int],
                          lqy: ArrayBuffer[Int],
                          lqd: Array2D[Boolean],
                          rgnc1: Int,
                          rgnc2: Int,
                          layer: Int,
                          layerTemp: Int,
                          blockTemp: BlockType,
                          mx: Int,
                          my: Int,
                          icx: Int,
                          icy: Int,
                          mining: Int,
                          immune: Int,
                          moveItem: UiItem,
                          moveNum: Short,
                          moveItemTemp: UiItem,
                          moveNumTemp: Short,
                          msi: Int,
                          toolAngle: Double,
                          toolSpeed: Double,
                          timeOfDay: Double,
                          currentSkyLight: Int,
                          day: Int,
                          mobCount: Int,
                          ready: Boolean,
                          showTool: Boolean,
                          showInv: Boolean,
                          doMobSpawn: Boolean,
                          WIDTH: Int,
                          HEIGHT: Int,
                          random: Random,
                          WORLDWIDTH: Int,
                          WORLDHEIGHT: Int,
                          resunlight: Int,
                          ic: Option[ItemCollection],
                          kworlds: Array2D[Boolean],
                          icmatrix: Array3D[Option[ItemCollection]],
                          version: String)
    extends Serializable
