package org.terraframe

/**
 * *
 * TerraFrame (working title) [Pre-alpha 1.3]
 * *
 * developed by Radon Rosborough
 * *
 * Project mission: To program a 2D sandbox game similar to, but with many more
 * features than, Terraria.
 *
  **/
import java.awt.event._
import java.awt.image._
import java.awt.{
  BorderLayout,
  Color,
  Dimension,
  Font,
  Graphics,
  Graphics2D,
  GraphicsConfiguration,
  GraphicsEnvironment,
  Point,
  Rectangle,
  Transparency
}
import java.io._
import java.{ util => jul }
import javax.swing._
import javax.swing.event._

import org.terraframe.Images.loadImage

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.math._
import scala.util.Random
import scala.util.control.NonFatal
import org.terraframe.{ MathHelper => mh }
import TypeSafeComparisons._

object TerraFrame {
  val config: GraphicsConfiguration =
    GraphicsEnvironment.getLocalGraphicsEnvironment.getDefaultScreenDevice.getDefaultConfiguration
  var armor: ItemCollection = _
  var WIDTH: Int            = 2400
  var HEIGHT: Int           = 2400

  val BLOCKSIZE: Int   = 16
  val IMAGESIZE: Int   = 8
  val CHUNKBLOCKS: Int = 96
  val CHUNKSIZE: Int   = CHUNKBLOCKS * BLOCKSIZE
  val PLAYERSIZEX: Int = 20
  val PLAYERSIZEY: Int = 46

  var random = new Random

  val BRIGHTEST: Int = 21

  val DEBUG_INSTAMINE: Boolean              = false
  val DEBUG_ACCEL: Double                   = 1
  val DEBUG_NOCLIP: Boolean                 = false
  val DEBUG_LIGHT: Boolean                  = false
  val DEBUG_REACH: Boolean                  = true
  val DEBUG_PEACEFUL: Boolean               = true
  val DEBUG_HOSTILE: Int                    = 1
  val DEBUG_F1: Boolean                     = false
  val DEBUG_SPEED: Boolean                  = true
  val DEBUG_FLIGHT: Boolean                 = true
  val DEBUG_INVINCIBLE: Boolean             = true
  val DEBUG_HERBGROW: Int                   = 1
  val DEBUG_GRASSGROW: Int                  = 1
  val DEBUG_MOBTEST: Option[EntityStrategy] = None
  val DEBUG_STATS: Boolean                  = true
  val DEBUG_ITEMS: Option[DebugItem]        = Some(TestingDebugItem)
  val DEBUG_GPLACE: Boolean                 = true

  var WORLDWIDTH: Int  = WIDTH / CHUNKBLOCKS + 1
  var WORLDHEIGHT: Int = HEIGHT / CHUNKBLOCKS + 1

  val SUNLIGHTSPEED: Int = 14

  val blocknames: Array[String] = Array(
    "air",
    "dirt",
    "stone",
    "copper_ore",
    "iron_ore",
    "silver_ore",
    "gold_ore",
    "wood",
    "workbench",
    "wooden_chest",
    "stone_chest",
    "copper_chest",
    "iron_chest",
    "silver_chest",
    "gold_chest",
    "tree",
    "leaves",
    "furnace",
    "coal",
    "lumenstone",
    "wooden_torch",
    "coal_torch",
    "lumenstone_torch",
    "furnace_on",
    "wooden_torch_left",
    "wooden_torch_right",
    "coal_torch_left",
    "coal_torch_right",
    "lumenstone_torch_left",
    "lumenstone_torch_right",
    "tree_root",
    "zinc_ore",
    "rhymestone_ore",
    "obdurite_ore",
    "aluminum_ore",
    "lead_ore",
    "uranium_ore",
    "zythium_ore",
    "zythium_ore_on",
    "silicon_ore",
    "irradium_ore",
    "nullstone",
    "meltstone",
    "skystone",
    "magnetite_ore",
    "sand",
    "snow",
    "glass",
    "sunflower_stage_1",
    "sunflower_stage_2",
    "sunflower_stage_3",
    "moonflower_stage_1",
    "moonflower_stage_2",
    "moonflower_stage_3",
    "dryweed_stage_1",
    "dryweed_stage_2",
    "dryweed_stage_3",
    "greenleaf_stage_1",
    "greenleaf_stage_2",
    "greenleaf_stage_3",
    "frostleaf_stage_1",
    "frostleaf_stage_2",
    "frostleaf_stage_3",
    "caveroot_stage_1",
    "caveroot_stage_2",
    "caveroot_stage_3",
    "skyblossom_stage_1",
    "skyblossom_stage_2",
    "skyblossom_stage_3",
    "void_rot_stage_1",
    "void_rot_stage_2",
    "void_rot_stage_3",
    "grass",
    "jungle_grass",
    "swamp_grass",
    "mud",
    "sandstone",
    "marshleaf_stage_1",
    "marshleaf_stage_2",
    "marshleaf_stage_3",
    "zinc_chest",
    "rhymestone_chest",
    "obdurite_chest",
    "tree_no_bark",
    "cobblestone",
    "chiseled_stone",
    "chiseled_cobblestone",
    "stone_bricks",
    "clay",
    "clay_bricks",
    "varnished_wood",
    "dirt_trans",
    "magnetite_ore_trans",
    "grass_trans",
    "zythium_wire_0",
    "zythium_wire_1",
    "zythium_wire_2",
    "zythium_wire_3",
    "zythium_wire_4",
    "zythium_wire_5",
    "zythium_torch",
    "zythium_torch_left",
    "zythium_torch_right",
    "zythium_lamp",
    "zythium_lamp_on",
    "lever",
    "lever_on",
    "lever_left",
    "lever_left_on",
    "lever_right",
    "lever_right_on",
    "zythium_amplifier_right",
    "zythium_amplifier_down",
    "zythium_amplifier_left",
    "zythium_amplifier_up",
    "zythium_amplifier_right_on",
    "zythium_amplifier_down_on",
    "zythium_amplifier_left_on",
    "zythium_amplifier_up_on",
    "zythium_inverter_right",
    "zythium_inverter_down",
    "zythium_inverter_left",
    "zythium_inverter_up",
    "zythium_inverter_right_on",
    "zythium_inverter_down_on",
    "zythium_inverter_left_on",
    "zythium_inverter_up_on",
    "button_left",
    "button_left_on",
    "button_right",
    "button_right_on",
    "wooden_pressure_plate",
    "wooden_pressure_plate_on",
    "stone_pressure_plate",
    "stone_pressure_plate_on",
    "zythium_pressure_plate",
    "zythium_pressure_plate_on",
    "zythium_delayer_1_right",
    "zythium_delayer_1_down",
    "zythium_delayer_1_left",
    "zythium_delayer_1_up",
    "zythium_delayer_1_right_on",
    "zythium_delayer_1_down_on",
    "zythium_delayer_1_left_on",
    "zythium_delayer_1_up_on",
    "zythium_delayer_2_right",
    "zythium_delayer_2_down",
    "zythium_delayer_2_left",
    "zythium_delayer_2_up",
    "zythium_delayer_2_right_on",
    "zythium_delayer_2_down_on",
    "zythium_delayer_2_left_on",
    "zythium_delayer_2_up_on",
    "zythium_delayer_4_right",
    "zythium_delayer_4_down",
    "zythium_delayer_4_left",
    "zythium_delayer_4_up",
    "zythium_delayer_4_right_on",
    "zythium_delayer_4_down_on",
    "zythium_delayer_4_left_on",
    "zythium_delayer_4_up_on",
    "zythium_delayer_8_right",
    "zythium_delayer_8_down",
    "zythium_delayer_8_left",
    "zythium_delayer_8_up",
    "zythium_delayer_8_right_on",
    "zythium_delayer_8_down_on",
    "zythium_delayer_8_left_on",
    "zythium_delayer_8_up_on"
  )

  var version: String = "0.3_01"

  val blockcds: Array[Boolean] = Array(false, true, true, true, true, true, true, true, false, false, false, false,
    false, false, false, false, false, false, true, true, false, false, false, false, false, false, false, false, false,
    false, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, true, true, true, true, true, false, false, false, false,
    false, false, false, true, true, true, true, true, true, true, true, true, true, false, false, false, false, false,
    false, false, false, false, true, true, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false)
  val solid: Array[Boolean] = Array(false, true, true, true, true, true, true, true, true, true, true, true, true, true,
    true, true, false, true, true, true, false, false, false, true, false, false, false, false, false, false, false,
    true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, true, true, true, true, true, false, false, false, true, true, true, true,
    true, true, true, true, true, true, true, true, true, true, false, false, false, false, false, false, false, false,
    false, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false)
  val ltrans: Array[Boolean] = Array(false, true, true, true, true, true, true, true, false, false, false, false, false,
    false, false, false, false, false, true, true, false, false, false, false, false, false, false, false, false, false,
    false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, true, true, true, true, true, false, false, false, false, false,
    false, false, true, true, true, true, true, true, true, false, false, false, false, false, false, false, false,
    false, false, false, false, true, true, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false)
  val conducts: Array[Double] = Array(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0, 0, 0, -1, -1, -1,
    0, -1, 0, -1, 0, -1, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
  val receives: Array[Boolean] = Array(false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, true, true, true, true, true, true, false, false, false, true, true, false, false, false,
    false, false, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
    false, false, false, false, false, false, false, false, false, false, true, true, true, true, true, true, true,
    true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
    true, true, true, true, true, true)
  val wirec: Array[Boolean] = Array(false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
    true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
    true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
    true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
    true)

  val skycolors: Array[Int] = Array(28800, 28980, 29160, 29340, 29520, 29700, 29880, 30060, 30240, 30420, 30600, 30780,
    30960, 31140, 31320, 31500, 31680, 31860, 32040, 32220, 72000, 72180, 72360, 72540, 72720, 72900, 73080, 73260,
    73440, 73620, 73800, 73980, 74160, 74340, 74520, 74700, 74880, 75060, 75240, 75420)


  val font: Font      = new Font("Chalkboard", Font.BOLD, 12)
  val mobFont: Font   = new Font("Chalkboard", Font.BOLD, 16)
  val loadFont: Font  = new Font("Courier", Font.PLAIN, 12)
  val menuFont: Font  = new Font("Chalkboard", Font.PLAIN, 30)
  val worldFont: Font = new Font("Andale Mono", Font.BOLD, 16)
  val CYANISH: Color  = new Color(75, 163, 243)

  lazy val OUTLINES: Map[Int, String] = {
    val outlinesTemp = new jul.HashMap[Int, String](blocknames.length)

    (1 until blocknames.length).foreach { i =>
      outlinesTemp.put(i, "default")
    }

    outlinesTemp.put(7, "wood")
    outlinesTemp.put(8, "none")
    outlinesTemp.put(9, "none")
    outlinesTemp.put(10, "none")
    outlinesTemp.put(11, "none")
    outlinesTemp.put(12, "none")
    outlinesTemp.put(13, "none")
    outlinesTemp.put(14, "none")
    outlinesTemp.put(15, "tree")
    outlinesTemp.put(17, "none")
    outlinesTemp.put(20, "none")
    outlinesTemp.put(21, "none")
    outlinesTemp.put(22, "none")
    outlinesTemp.put(23, "none")
    outlinesTemp.put(24, "none")
    outlinesTemp.put(25, "none")
    outlinesTemp.put(26, "none")
    outlinesTemp.put(27, "none")
    outlinesTemp.put(28, "none")
    outlinesTemp.put(29, "none")
    outlinesTemp.put(30, "tree_root")
    outlinesTemp.put(47, "square")
    outlinesTemp.put(77, "none")
    outlinesTemp.put(78, "none")
    outlinesTemp.put(79, "none")
    outlinesTemp.put(80, "none")
    outlinesTemp.put(81, "none")
    outlinesTemp.put(82, "none")
    outlinesTemp.put(83, "tree")
    outlinesTemp.put(84, "square")
    outlinesTemp.put(85, "square")
    outlinesTemp.put(86, "square")
    outlinesTemp.put(87, "square")
    outlinesTemp.put(89, "square")
    outlinesTemp.put(90, "wood")
    outlinesTemp.put(94, "wire")
    outlinesTemp.put(95, "wire")
    outlinesTemp.put(96, "wire")
    outlinesTemp.put(97, "wire")
    outlinesTemp.put(98, "wire")
    outlinesTemp.put(99, "wire")
    outlinesTemp.put(100, "none")
    outlinesTemp.put(101, "none")
    outlinesTemp.put(102, "none")
    outlinesTemp.put(103, "square")
    outlinesTemp.put(104, "square")
    outlinesTemp.put(105, "none")
    outlinesTemp.put(106, "none")
    outlinesTemp.put(107, "none")
    outlinesTemp.put(108, "none")
    outlinesTemp.put(109, "none")
    outlinesTemp.put(110, "none")
    outlinesTemp.put(111, "none")
    outlinesTemp.put(112, "none")
    outlinesTemp.put(113, "none")
    outlinesTemp.put(114, "none")
    outlinesTemp.put(115, "none")
    outlinesTemp.put(116, "none")
    outlinesTemp.put(117, "none")
    outlinesTemp.put(118, "none")
    outlinesTemp.put(119, "none")
    outlinesTemp.put(120, "none")
    outlinesTemp.put(121, "none")
    outlinesTemp.put(122, "none")
    outlinesTemp.put(123, "none")
    outlinesTemp.put(124, "none")
    outlinesTemp.put(125, "none")
    outlinesTemp.put(126, "none")
    outlinesTemp.put(127, "none")
    outlinesTemp.put(128, "none")
    outlinesTemp.put(129, "none")
    outlinesTemp.put(130, "none")
    outlinesTemp.put(131, "none")
    outlinesTemp.put(132, "none")
    outlinesTemp.put(133, "none")
    outlinesTemp.put(134, "none")
    outlinesTemp.put(135, "none")
    outlinesTemp.put(136, "none")

    (48 until 72).foreach { i =>
      outlinesTemp.put(i, "none")
    }

    (137 until 169).foreach { i =>
      outlinesTemp.put(i, "none")
    }

    outlinesTemp.asScala.toMap
  }

  val UIENTITIES: Map[String, String] = {
    val uiEntitiesTemp = new jul.HashMap[String, String](15)

    uiEntitiesTemp.put("blue_bubble", "Blue Bubble")
    uiEntitiesTemp.put("green_bubble", "Green Bubble")
    uiEntitiesTemp.put("red_bubble", "Red Bubble")
    uiEntitiesTemp.put("black_bubble", "Black Bubble")
    uiEntitiesTemp.put("white_bubble", "White Bubble")
    uiEntitiesTemp.put("zombie", "Zombie")
    uiEntitiesTemp.put("armored_zombie", "Armored Zombie")
    uiEntitiesTemp.put("shooting_star", "Shooting Star")
    uiEntitiesTemp.put("sandbot", "Sandbot")
    uiEntitiesTemp.put("snowman", "Snowman")
    uiEntitiesTemp.put("bat", "Bat")
    uiEntitiesTemp.put("bee", "Bee")
    uiEntitiesTemp.put("skeleton", "Skeleton")

    uiEntitiesTemp.asScala.toMap
  }
  lazy val BLOCKCD: Map[Int, Boolean] = {
    val blockCdTemp = new jul.HashMap[Int, Boolean](blockcds.length)

    (1 until blockcds.length).foreach { i =>
      blockCdTemp.put(i, blockcds(i))
    }

    blockCdTemp.asScala.toMap
  }

  lazy val SKYLIGHTS: Map[Int, Int] = {
    val skyLightsTemp = new jul.HashMap[Int, Int](30)

    skyLightsTemp.put(28883, 18)
    skyLightsTemp.put(29146, 17)
    skyLightsTemp.put(29409, 16)
    skyLightsTemp.put(29672, 15)
    skyLightsTemp.put(29935, 14)
    skyLightsTemp.put(30198, 13)
    skyLightsTemp.put(30461, 12)
    skyLightsTemp.put(30724, 11)
    skyLightsTemp.put(30987, 10)
    skyLightsTemp.put(31250, 9)
    skyLightsTemp.put(31513, 8)
    skyLightsTemp.put(31776, 7)
    skyLightsTemp.put(32039, 6)
    skyLightsTemp.put(32302, 5)
    skyLightsTemp.put(72093, 6)
    skyLightsTemp.put(72336, 7)
    skyLightsTemp.put(72639, 8)
    skyLightsTemp.put(72912, 9)
    skyLightsTemp.put(73185, 10)
    skyLightsTemp.put(73458, 11)
    skyLightsTemp.put(73731, 12)
    skyLightsTemp.put(74004, 13)
    skyLightsTemp.put(74277, 14)
    skyLightsTemp.put(74550, 15)
    skyLightsTemp.put(74823, 16)
    skyLightsTemp.put(75096, 17)
    skyLightsTemp.put(75369, 18)
    skyLightsTemp.put(75642, 19)

    skyLightsTemp.asScala.toMap
  }
  lazy val SKYCOLORS: Map[Int, Color] = {
    val skyColorsTemp = new jul.HashMap[Int, Color](50)

    skyColorsTemp.put(28800, new Color(71, 154, 230))
    skyColorsTemp.put(28980, new Color(67, 146, 218))
    skyColorsTemp.put(29160, new Color(63, 138, 206))
    skyColorsTemp.put(29340, new Color(60, 130, 194))
    skyColorsTemp.put(29520, new Color(56, 122, 182))
    skyColorsTemp.put(29700, new Color(52, 114, 170))
    skyColorsTemp.put(29880, new Color(48, 105, 157))
    skyColorsTemp.put(30060, new Color(45, 97, 145))
    skyColorsTemp.put(30240, new Color(41, 89, 133))
    skyColorsTemp.put(30420, new Color(37, 81, 121))
    skyColorsTemp.put(30600, new Color(33, 73, 109))
    skyColorsTemp.put(30780, new Color(30, 65, 97))
    skyColorsTemp.put(30960, new Color(26, 57, 85))
    skyColorsTemp.put(31140, new Color(22, 48, 72))
    skyColorsTemp.put(31320, new Color(18, 40, 60))
    skyColorsTemp.put(31500, new Color(15, 32, 48))
    skyColorsTemp.put(31680, new Color(11, 24, 36))
    skyColorsTemp.put(31860, new Color(7, 16, 24))
    skyColorsTemp.put(32040, new Color(3, 8, 12))
    skyColorsTemp.put(32220, new Color(0, 0, 0))

    skyColorsTemp.put(72000, new Color(3, 8, 12))
    skyColorsTemp.put(72180, new Color(7, 16, 24))
    skyColorsTemp.put(72360, new Color(11, 24, 36))
    skyColorsTemp.put(72540, new Color(15, 32, 48))
    skyColorsTemp.put(72720, new Color(18, 40, 60))
    skyColorsTemp.put(72900, new Color(22, 48, 72))
    skyColorsTemp.put(73080, new Color(26, 57, 85))
    skyColorsTemp.put(73260, new Color(30, 65, 97))
    skyColorsTemp.put(73440, new Color(33, 73, 109))
    skyColorsTemp.put(73620, new Color(37, 81, 121))
    skyColorsTemp.put(73800, new Color(41, 89, 133))
    skyColorsTemp.put(73980, new Color(45, 97, 145))
    skyColorsTemp.put(74160, new Color(48, 105, 157))
    skyColorsTemp.put(74340, new Color(52, 114, 170))
    skyColorsTemp.put(74520, new Color(56, 122, 182))
    skyColorsTemp.put(74700, new Color(60, 130, 194))
    skyColorsTemp.put(74880, new Color(63, 138, 206))
    skyColorsTemp.put(75060, new Color(67, 146, 218))
    skyColorsTemp.put(75240, new Color(71, 154, 230))
    skyColorsTemp.put(75420, new Color(75, 163, 243))

    skyColorsTemp.asScala.toMap

  }
  lazy val LIGHTLEVELS: Map[Int, BufferedImage] = {
    val lightLevelsTemp = new jul.HashMap[Int, BufferedImage](20)

    (0 until 17).foreach { i =>
      lightLevelsTemp.put(i, loadImage("light/" + i + ".png").get)
    }

    lightLevelsTemp.asScala.toMap
  }

  lazy val blockImgs: Map[String, BufferedImage] = {
    val blockImgsTemp = new jul.HashMap[String, BufferedImage](blocknames.length)

    (1 until blocknames.length).foreach { i =>
      (0 until 8).foreach { j =>
        loadImage("blocks/" + blocknames(i) + "/texture" + (j + 1) + ".png")
          .fold {
            println("(ERROR) Could not load block graphic '" + blocknames(i) + "'.")
          } { img =>
            blockImgsTemp.put("blocks/" + blocknames(i) + "/texture" + (j + 1) + ".png", img)
            ()
          }
      }
    }

    blockImgsTemp.asScala.toMap
  }
  lazy val outlineImgs: Map[String, BufferedImage] = {
    val outlineNameList: Array[String] = Array("default", "wood", "none", "tree", "tree_root", "square", "wire")

    val outlineImgsTemp =
      new jul.HashMap[String, BufferedImage](outlineNameList.length * OutlineDirection.dirs.length * 5)

    outlineNameList.foreach { outlineName =>
      OutlineDirection.dirs.foreach { dir =>
        (0 until 5).foreach { k =>
          outlineImgsTemp.put(
            "outlines/" + outlineName + "/" + dir.imageName + (k + 1) + ".png",
            loadImage("outlines/" + outlineName + "/" + dir.imageName + (k + 1) + ".png").get)
        }
      }
    }

    outlineImgsTemp.asScala.toMap
  }

  lazy val BLOCKLIGHTS: Map[Int, Int] = { //TODO: key should be block, not sure the value
    val blockLightsTemp = new jul.HashMap[Int, Int](blocknames.length)

    blocknames.indices.foreach { i =>
      blockLightsTemp.put(i, 0)
    }

    blockLightsTemp.put(19, 21)
    blockLightsTemp.put(20, 15)
    blockLightsTemp.put(21, 18)
    blockLightsTemp.put(22, 21)
    blockLightsTemp.put(23, 15)
    blockLightsTemp.put(24, 15)
    blockLightsTemp.put(25, 15)
    blockLightsTemp.put(26, 18)
    blockLightsTemp.put(27, 18)
    blockLightsTemp.put(28, 21)
    blockLightsTemp.put(29, 21)
    blockLightsTemp.put(36, 15)
    blockLightsTemp.put(36, 15)
    blockLightsTemp.put(38, 18)
    blockLightsTemp.put(51, 15)
    blockLightsTemp.put(52, 15)
    blockLightsTemp.put(53, 15)
    blockLightsTemp.put(95, 6)
    blockLightsTemp.put(96, 7)
    blockLightsTemp.put(97, 8)
    blockLightsTemp.put(98, 9)
    blockLightsTemp.put(99, 10)
    blockLightsTemp.put(100, 12)
    blockLightsTemp.put(101, 12)
    blockLightsTemp.put(102, 12)
    blockLightsTemp.put(104, 21)
    blockLightsTemp.put(112, 12)
    blockLightsTemp.put(114, 12)
    blockLightsTemp.put(116, 12)
    blockLightsTemp.put(118, 12)
    blockLightsTemp.put(123, 12)
    blockLightsTemp.put(124, 12)
    blockLightsTemp.put(125, 12)
    blockLightsTemp.put(126, 12)

    blockLightsTemp.put(137, 12)
    blockLightsTemp.put(138, 12)
    blockLightsTemp.put(139, 12)
    blockLightsTemp.put(140, 12)
    blockLightsTemp.put(145, 12)
    blockLightsTemp.put(146, 12)
    blockLightsTemp.put(147, 12)
    blockLightsTemp.put(148, 12)
    blockLightsTemp.put(153, 12)
    blockLightsTemp.put(154, 12)
    blockLightsTemp.put(155, 12)
    blockLightsTemp.put(156, 12)
    blockLightsTemp.put(161, 12)
    blockLightsTemp.put(162, 12)
    blockLightsTemp.put(163, 12)
    blockLightsTemp.put(164, 12)

    blockLightsTemp.asScala.toMap
  }
  lazy val GRASSDIRT: Map[Int, Int] = {
    val grassDirtTemp = new jul.HashMap[Int, Int](10)

    grassDirtTemp.put(72, 1)
    grassDirtTemp.put(73, 1)
    grassDirtTemp.put(74, 75)
    grassDirtTemp.put(93, 91)

    grassDirtTemp.asScala.toMap
  }

  lazy val FUELS: Map[Short, Double] = { // TODO: Move to UiItem
    val fuelsTemp = new jul.HashMap[Short, Double](50)

    fuelsTemp.put(15.toShort, 0.01)
    fuelsTemp.put(28.toShort, 0.001)
    fuelsTemp.put(160.toShort, 0.02)
    fuelsTemp.put(168.toShort, 0.01)
    fuelsTemp.put(179.toShort, 0.0035)
    fuelsTemp.put(20.toShort, 0.0025)
    fuelsTemp.put(21.toShort, 0.00125)
    fuelsTemp.put(35.toShort, 0.02)
    fuelsTemp.put(36.toShort, 0.011)
    fuelsTemp.put(77.toShort, 0.02)
    fuelsTemp.put(79.toShort, 0.02)
    fuelsTemp.put(81.toShort, 0.02)
    fuelsTemp.put(83.toShort, 0.02)
    fuelsTemp.put(85.toShort, 0.02)
    fuelsTemp.put(87.toShort, 0.02)
    fuelsTemp.put(89.toShort, 0.0035)
    fuelsTemp.put(91.toShort, 0.02)
    fuelsTemp.put(95.toShort, 0.02)
    fuelsTemp.put(78.toShort, 0.01)
    fuelsTemp.put(80.toShort, 0.01)
    fuelsTemp.put(82.toShort, 0.01)
    fuelsTemp.put(84.toShort, 0.01)
    fuelsTemp.put(86.toShort, 0.01)
    fuelsTemp.put(88.toShort, 0.01)
    fuelsTemp.put(90.toShort, 0.01)
    fuelsTemp.put(92.toShort, 0.01)
    fuelsTemp.put(96.toShort, 0.01)
    (97 until 103).foreach { i =>
      fuelsTemp.put(i.toShort, 0.0035)
    }
    fuelsTemp.put(154.toShort, 0.002)
    fuelsTemp.put(155.toShort, 0.002)
    fuelsTemp.put(156.toShort, 0.00333)

    fuelsTemp.asScala.toMap
  }
  lazy val WIREP: Map[Int, BlockType] = { // TODO: not sure the key here
    val wirepTemp = new jul.HashMap[Int, BlockType](10)

    wirepTemp.put(0, ZythiumWireBlockType)
    wirepTemp.put(1, ZythiumWire1PowerBlockType)
    wirepTemp.put(2, ZythiumWire2PowerBlockType)
    wirepTemp.put(3, ZythiumWire3PowerBlockType)
    wirepTemp.put(4, ZythiumWire4PowerBlockType)
    wirepTemp.put(5, ZythiumWire5PowerBlockType)

    wirepTemp.asScala.toMap
  }
  lazy val TORCHESL: Map[BlockType, BlockType] = {
    val torcheslTemp = new jul.HashMap[BlockType, BlockType](10)

    torcheslTemp.put(WoodenTorchBlockType, WoodenTorchLeftWallBlockType)
    torcheslTemp.put(CoalTorchBlockType, CoalTorchLeftWallBlockType)
    torcheslTemp.put(LumenstoneTorchBlockType, LumenstoneTorchLeftWallBlockType)
    torcheslTemp.put(ZythiumTorchBlockType, ZythiumTorchLeftWallBlockType)
    torcheslTemp.put(LeverBlockType, LeverLeftWallBlockType)
    torcheslTemp.put(LeverOnBlockType, LeverLeftWallOnBlockType)
    torcheslTemp.put(ButtonLeftBlockType, ButtonLeftBlockType)
    torcheslTemp.put(ButtonLeftOnBlockType, ButtonLeftOnBlockType)

    torcheslTemp.asScala.toMap
  }
  lazy val TORCHESR: Map[BlockType, BlockType] = {
    val torchesrTemp = new jul.HashMap[BlockType, BlockType](10)

    torchesrTemp.put(WoodenTorchBlockType, WoodenTorchRightWallBlockType)
    torchesrTemp.put(CoalTorchBlockType, CoalTorchRightWallBlockType)
    torchesrTemp.put(LumenstoneTorchBlockType, LumenstoneTorchRightWallBlockType)
    torchesrTemp.put(ZythiumTorchBlockType, ZythiumTorchRightWallBlockType)
    torchesrTemp.put(LeverBlockType, LeverLightWallBlockType)
    torchesrTemp.put(LeverOnBlockType, LeverRightWallOnBlockType)
    torchesrTemp.put(ButtonLeftBlockType, ButtonRightBlockType)

    torchesrTemp.asScala.toMap

  }

  lazy val TORCHESB: Map[Int, Boolean] = {
    val torchesbTemp = new jul.HashMap[Int, Boolean](blocknames.length)

    blocknames.indices.foreach { i =>
      torchesbTemp.put(i, false)
    }

    torchesbTemp.put(20, true)
    torchesbTemp.put(21, true)
    torchesbTemp.put(22, true)
    torchesbTemp.put(100, true)
    torchesbTemp.put(24, true)
    torchesbTemp.put(26, true)
    torchesbTemp.put(28, true)
    torchesbTemp.put(101, true)
    torchesbTemp.put(25, true)
    torchesbTemp.put(27, true)
    torchesbTemp.put(29, true)
    torchesbTemp.put(102, true)
    torchesbTemp.put(105, true)
    torchesbTemp.put(106, true)
    torchesbTemp.put(107, true)
    torchesbTemp.put(108, true)
    torchesbTemp.put(109, true)
    torchesbTemp.put(110, true)
    torchesbTemp.put(127, true)
    torchesbTemp.put(128, true)
    torchesbTemp.put(129, true)
    torchesbTemp.put(130, true)

    torchesbTemp.asScala.toMap
  }

  lazy val FSPEED: Map[Short, Double] = {
    val fspeedTemp = new jul.HashMap[Short, Double](blocknames.length)

    blocknames.indices.foreach { i =>
      fspeedTemp.put(i.toShort, 0.001)
    }

    fspeedTemp.put(85.toShort, -0.001)
    fspeedTemp.put(86.toShort, -0.001)

    fspeedTemp.asScala.toMap
  }

  lazy val DDELAY: Map[Int, Int] = {
    val ddelayTemp = new jul.HashMap[Int, Int](169)

    (137 until 145).foreach { i =>
      ddelayTemp.put(i, 10)
    }
    (145 until 153).foreach { i =>
      ddelayTemp.put(i, 20)
    }
    (153 until 161).foreach { i =>
      ddelayTemp.put(i, 40)
    }
    (161 until 169).foreach { i =>
      ddelayTemp.put(i, 80)
    }

    ddelayTemp.asScala.toMap
  }

  var log: BufferedWriter = _

  def main(args: Array[String]): Unit = {
    val f = new JFrame("TerraFrame: Infinite worlds!")
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    f.setVisible(true)

    val ap = new TerraFrame()
    ap.setFocusable(true)
    f.add("Center", ap)
    f.pack()

    ap.init()
  }

  def postError(e: Throwable): Unit = {
    val sb = new StringBuilder()
    sb.append("Exception in thread " + e.getClass.getName)
    Option(e.getMessage).foreach { message =>
      sb.append(": ")
      sb.append(message)
    }
    e.getStackTrace.foreach { ste =>
      sb.append("\n        at " + ste.toString)
    }
    try {
      log = new BufferedWriter(new FileWriter("log.txt"))
      log.write(sb.toString())
      log.close()
    } catch {
      case _: IOException =>
    } finally {
      println(sb.toString)
    }
  }

  val theSize: Int = CHUNKBLOCKS * 2

  lazy val logo_white: BufferedImage    = loadImage("interface/logo_white.png").get
  lazy val logo_black: BufferedImage    = loadImage("interface/logo_black.png").get
  lazy val title_screen: BufferedImage  = loadImage("interface/title_screen.png").get
  lazy val select_world: BufferedImage  = loadImage("interface/select_world.png").get
  lazy val new_world: BufferedImage     = loadImage("interface/new_world.png").get
  lazy val save_exit: BufferedImage     = loadImage("interface/save_exit.png").get
  lazy val sun: BufferedImage           = loadImage("environment/sun.png").get
  lazy val moon: BufferedImage          = loadImage("environment/moon.png").get
  lazy val clouds: Array[BufferedImage] = Array(loadImage("environment/cloud1.png").get)
  lazy val wcnct_px: BufferedImage      = loadImage("misc/wcnct.png").get

  //TODO: FR[IN][12] should be on concept smelting recipe or something
  lazy val FRI1: List[UiItem] = {
    val fri1Temp = new jul.ArrayList[UiItem](180)

    fri1Temp.add(CopperOreUiItem)
    fri1Temp.add(IronOreUiItem)
    fri1Temp.add(SilverOreUiItem)
    fri1Temp.add(GoldOreUiItem)
    fri1Temp.add(ZincOreUiItem)
    fri1Temp.add(RhymestoneOreUiItem)
    fri1Temp.add(ObduriteOreUiItem)
    fri1Temp.add(AluminumOreUiItem)
    fri1Temp.add(LeadOreUiItem)
    fri1Temp.add(UraniumOreUiItem)
    fri1Temp.add(ZythiumOreUiItem)
    fri1Temp.add(SiliconOreUiItem)
    fri1Temp.add(IrradiumOreUiItem)
    fri1Temp.add(NullstoneUiItem)
    fri1Temp.add(MeltstoneUiItem)
    fri1Temp.add(SkystoneUiItem)
    fri1Temp.add(MagnetiteOreUiItem)
    (8 until (2, -1)).foreach { _ =>
      fri1Temp.add(SandUiItem)
      fri1Temp.add(StoneUiItem)
      fri1Temp.add(CobblestoneUiItem)
      fri1Temp.add(ClayUiItem)
      fri1Temp.add(WoodUiItem)
    }

    fri1Temp.add(BlueGooUiItem)
    fri1Temp.add(GreenGooUiItem)
    fri1Temp.add(RedGooUiItem)
    fri1Temp.add(YellowGooUiItem)
    fri1Temp.add(BlackGooUiItem)
    fri1Temp.add(WhiteGooUiItem)

    fri1Temp.asScala.toList
  }
  lazy val FRN1: List[Short] = {
    val frn1Temp = new jul.ArrayList[Short](180)

    frn1Temp.add(4.toShort)
    frn1Temp.add(4.toShort)
    frn1Temp.add(4.toShort)
    frn1Temp.add(4.toShort)
    frn1Temp.add(4.toShort)
    frn1Temp.add(4.toShort)
    frn1Temp.add(4.toShort)
    frn1Temp.add(4.toShort)
    frn1Temp.add(4.toShort)
    frn1Temp.add(4.toShort)
    frn1Temp.add(4.toShort)
    frn1Temp.add(4.toShort)
    frn1Temp.add(4.toShort)
    frn1Temp.add(4.toShort)
    frn1Temp.add(4.toShort)
    frn1Temp.add(4.toShort)
    frn1Temp.add(4.toShort)
    (8 until (2, -1)).foreach { i =>
      frn1Temp.add(i.toShort)
      frn1Temp.add(i.toShort)
      frn1Temp.add(i.toShort)
      frn1Temp.add(i.toShort)
      frn1Temp.add(i.toShort)
    }
    (97 until 103).foreach { j =>
      frn1Temp.add(1.toShort)
    }

    frn1Temp.asScala.toList
  }
  lazy val FRI2: List[UiItem] = {
    val fri2Temp = new jul.ArrayList[UiItem](180)

    fri2Temp.add(CopperIngotUiItem)
    fri2Temp.add(IronIngotUiItem)
    fri2Temp.add(SilverIngotUiItem)
    fri2Temp.add(GoldIngotUiItem)
    fri2Temp.add(ZincIngotUiItem)
    fri2Temp.add(RhymestoneIngotUiItem)
    fri2Temp.add(ObduriteIngotUiItem)
    fri2Temp.add(AluminumIngotUiItem)
    fri2Temp.add(LeadIngotUiItem)
    fri2Temp.add(UraniumBarUiItem)
    fri2Temp.add(ZythiumBarUiItem)
    fri2Temp.add(SiliconBarUiItem)
    fri2Temp.add(IrradiumIngotUiItem)
    fri2Temp.add(NullstoneBarUiItem)
    fri2Temp.add(MeltstoneBarUiItem)
    fri2Temp.add(SkystoneBarUiItem)
    fri2Temp.add(MagnetiteIngotUiItem)
    (8 until (2, -1)).foreach { _ =>
      fri2Temp.add(GlassUiItem)
      fri2Temp.add(CobblestoneUiItem)
      fri2Temp.add(ChiseledCobblestoneUiItem)
      fri2Temp.add(ClayBricksUiItem)
      fri2Temp.add(CharcoalUiItem)
    }
    (97 until 103).foreach { j =>
      fri2Temp.add(VarnishUiItem)
    }

    fri2Temp.asScala.toList
  }
  lazy val FRN2: List[Short] = {
    val frn2Temp = new jul.ArrayList[Short](180)

    frn2Temp.add(1.toShort)
    frn2Temp.add(1.toShort)
    frn2Temp.add(1.toShort)
    frn2Temp.add(1.toShort)
    frn2Temp.add(1.toShort)
    frn2Temp.add(1.toShort)
    frn2Temp.add(1.toShort)
    frn2Temp.add(1.toShort)
    frn2Temp.add(1.toShort)
    frn2Temp.add(1.toShort)
    frn2Temp.add(1.toShort)
    frn2Temp.add(1.toShort)
    frn2Temp.add(1.toShort)
    frn2Temp.add(1.toShort)
    frn2Temp.add(1.toShort)
    frn2Temp.add(1.toShort)
    frn2Temp.add(1.toShort)
    (8 until (2, -1)).foreach { i =>
      frn2Temp.add(i.toShort)
      frn2Temp.add(i.toShort)
      frn2Temp.add(i.toShort)
      frn2Temp.add(i.toShort)
      frn2Temp.add(i.toShort)
    }
    (97 until 103).foreach { j =>
      frn2Temp.add(8.toShort)
    }

    frn2Temp.asScala.toList
  }

  lazy val sunlightlevel: Int = 19
}

class TerraFrame
    extends JApplet
    with ChangeListener
    with KeyListener
    with MouseListener
    with MouseMotionListener
    with MouseWheelListener {

  import TerraFrame._
  import GraphicsHelper._
  import Biome._
  import BlockType._

  var cic: Option[ItemCollection]   = None
  var screen: Option[BufferedImage] = None
  var bg: Color                     = _

  val cl: Array2D[Int] = Array(Array(-1, 0), Array(1, 0), Array(0, -1), Array(0, 1))

  var timer, menuTimer, paIntTimer: javax.swing.Timer = _
  var worldFiles, worldNames: List[String]            = _
  var currentWorld: String                            = _
  var newWorldName: TextField                         = _

  var blocks: Array3D[BlockType]                                         = _
  var blockds: Array3D[OutlineDirection]                                 = _
  var blockdns: Array2D[Byte]                                            = _
  var blockbgs: Array2D[Background]                                      = _
  var blockts: Array2D[Byte]                                             = _
  var lights: Array2D[Float]                                             = _
  var power: Array3D[Float]                                              = _
  var lsources: Array2D[Boolean]                                         = _
  var lqx, lqy, pqx, pqy, zqx, zqy, pzqx, pzqy: mutable.ArrayBuffer[Int] = _
  var lqd, zqd, pqd, pzqd: Array2D[Boolean]                              = _
  var zqn: Array2D[Byte]                                                 = _
  var pzqn: Array3D[Byte]                                                = _
  var arbprd: Array3D[Boolean]                                           = _
  var updatex, updatey, updatet, updatel: mutable.ArrayBuffer[Int]       = _
  var wcnct: Array2D[Boolean]                                            = _
  var drawn, ldrawn, rdrawn: Array2D[Boolean]                            = _
  var player: Player                                                     = _
  var inventory: Inventory                                               = _

  var entities: mutable.ArrayBuffer[Entity]                  = _
  var cloudsx, cloudsy, cloudsv: mutable.ArrayBuffer[Double] = _
  var cloudsn: mutable.ArrayBuffer[Int]                      = _
  var machinesx, machinesy: mutable.ArrayBuffer[Int]         = _

  var temporarySaveFile: Array2D[Option[Chunk]] = _
  var chunkMatrix: Array2D[Option[Chunk]]       = _

  var rgnc1: Int              = 0
  var rgnc2: Int              = 0
  var layer: Int              = 1
  var iclayer: Int            = _
  var layerTemp: Int          = _
  var blockTemp: BlockType    = _
  var layerImg: BufferedImage = _

  var state: GameState                 = LoadingGraphics
  var mobSpawn: Option[EntityStrategy] = None

  private[this] var width, height = 0
  var u, v, uNew, vNew: Int       = _
  var i, j, k, wx, wy, lx, ly, tx, ty, twx, twy, tlx, tly, ux, uy, ux2, uy2, uwx, uwy, uwx2, ulx, uly, ulx2, uly2, ucx,
  ucy, uclx, ucly, pwx, pwy, n, m, dx, dy, dx2, dy2, mx, my, lsx, lsy, lsn, ax, ay, axl, ayl, nl, vc, xpos, ypos, xpos2,
  ypos2, x2, y2, rnum, mining, xmin, xmax, ymin, ymax, Intpercent, ground: Int = _
  var t: BlockType                                                             = _
  private[this] var x, y                                                       = 0
  var p, q: Double                                                             = _
  var s: Short                                                                 = _
  var miningTool: UiItem                                                       = _

  var moveItem: UiItem                                  = _
  var moveItemTemp: UiItem                              = _
  var moveNum, moveDur, moveNumTemp, moveDurTemp: Short = _
  var msi, ou, ov, icx, icy, immune: Int                = 0

  var top, bottom, percent: Double = _

  var toolAngle, toolSpeed: Double = _

  var timeOfDay: Double    = 0 // 28000 (before dusk), 32000 (after dusk)
  var currentSkyLight: Int = 28800
  var day: Int             = 0

  var mobCount: Int = 0

  var loadTextPos: Int = 0

  var cloud: BufferedImage = _

  var createWorldTimer: javax.swing.Timer = _

  val userInput = UserInput()

  var done: Boolean                  = false
  var ready: Boolean                 = true
  var showTool: Boolean              = false
  var showInv: Boolean               = false
  var checkBlocks: Boolean           = true
  var mouseClicked: Boolean          = true
  var mouseClicked2: Boolean         = true
  var mouseNoLongerClicked: Boolean  = false
  var mouseNoLongerClicked2: Boolean = false
  var addSources: Boolean            = false
  var removeSources: Boolean         = false
  var beginLight: Boolean            = false
  var doMobSpawn: Boolean            = false
  var keepLeaf: Boolean              = false
  var newWorldFocus: Boolean         = false
  var menuPressed: Boolean           = false
  var doGenerateWorld: Boolean       = true
  var doGrassGrow: Boolean           = false
  var reallyAddPower: Boolean        = false
  var reallyRemovePower: Boolean     = false

  var ic: Option[ItemCollection] = None

  var worlds, fworlds: Array2D[Option[BufferedImage]] = _
  var kworlds: Array2D[Boolean]                       = _

  var icmatrix: Array3D[Option[ItemCollection]] = _

  var tool: Option[BufferedImage] = None

  var wg2, fwg2, pg2: Graphics2D = _

  override def init(): Unit = {
    try {
      setLayout(new BorderLayout())
      bg = Color.BLACK

      addKeyListener(this)
      addMouseListener(this)
      addMouseMotionListener(this)
      addMouseWheelListener(this)

      screen = Some(new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB))

      logo_white.getWidth
      logo_black.getWidth
      title_screen.getWidth
      select_world.getWidth
      new_world.getWidth
      save_exit.getWidth

      state = LoadingGraphics

      repaint()

      //backgroundImgs.size
//TODO: figure out how to initialize all ImageUiItem images
      blockImgs.size
      outlineImgs.size
      //DURABILITY.size
      //BLOCKTOOLS.size
      //BLOCKDROPS.size
      OUTLINES.size
      BLOCKCD.size
      SKYCOLORS.size
      SKYLIGHTS.size
      LIGHTLEVELS.size
      BLOCKLIGHTS.size
      GRASSDIRT.size
      //TOOLDURS.size
      FUELS.size
      WIREP.size
      TORCHESL.size
      TORCHESR.size
      TORCHESB.size
//      GSUPPORT.size
      FSPEED.size
      DDELAY.size
      sun.getWidth
      moon.getWidth
      FRI1.size
      FRN1.size
      FRI2.size
      FRN2.size

      bg = CYANISH
      state = TitleScreen

      repaint()

      val actionListener = new ActionListener() {
        def actionPerformed(ae: ActionEvent): Unit = {
          try {
            if (userInput.isLeftMousePressed) {
              val mainthread: Action = new AbstractAction() {
                def actionPerformed(ae: ActionEvent): Unit = {
                  try {
                    if (ready) {
                      ready = false
                      uNew = ((player.x - getWidth / 2 + Player.width) / CHUNKSIZE.toDouble).toInt
                      vNew = ((player.y - getHeight / 2 + Player.height) / CHUNKSIZE.toDouble).toInt
                      if (ou =/= uNew || ov =/= vNew) {
                        ou = uNew
                        ov = vNew
                        val chunkTemp = mutable.ArrayBuffer.empty[Chunk]
                        (0 until 2).foreach { twy =>
                          (0 until 2).foreach { twx =>
                            chunkMatrix(twy)(twx).foreach { c =>
                              chunkTemp += c
                              chunkMatrix(twy)(twx) = None
                            }
                          }
                        }
                        (0 until 2).foreach { twy =>
                          import scala.util.control.Breaks._
                          breakable {
                            (0 until 2).foreach { twx =>
                              chunkTemp.toList.zipWithIndex.reverse.foreach { p =>
                                val c     = p._1
                                val index = p._2
                                if (c.cx === twx && c.cy === twy) {
                                  chunkMatrix(twy)(twx) = Some(c)
                                  chunkTemp.remove(index)
                                  break
                                }
                              }
                              if (chunkMatrix(twy)(twx).isEmpty) {
                                temporarySaveFile(twy)(twx).fold {
                                  chunkMatrix(twy)(twx) = Some(Chunk(twx + ou, twy + ov, random))
                                } { c =>
                                  chunkMatrix(twy)(twx) = Some(c)
                                }
                              }
                            }
                          }
                        }
                        chunkTemp.foreach { chunk =>
                          temporarySaveFile(twy)(twx) = Some(chunk) //TODO: this line seems fishy
                        }
                        (0 until 2).foreach { twy =>
                          (0 until 2).foreach { twx =>
                            chunkMatrix(twy)(twx).foreach { cMatrix =>
                              (0 until CHUNKBLOCKS).foreach { y =>
                                (0 until CHUNKBLOCKS).foreach { x =>
                                  (0 until 3).foreach { l =>
                                    blocks(l)(twy * CHUNKBLOCKS + y)(twx * CHUNKBLOCKS + x) = cMatrix.blocks(l)(y)(x)
                                    power(l)(twy * CHUNKBLOCKS + y)(twx * CHUNKBLOCKS + x) = cMatrix.power(l)(y)(x)
                                    pzqn(l)(twy * CHUNKBLOCKS + y)(twx * CHUNKBLOCKS + x) = cMatrix.pzqn(l)(y)(x)
                                    arbprd(l)(twy * CHUNKBLOCKS + y)(twx * CHUNKBLOCKS + x) = cMatrix.arbprd(l)(y)(x)
                                    blockds(l)(twy * CHUNKBLOCKS + y)(twx * CHUNKBLOCKS + x) = cMatrix.blockds(l)(y)(x)
                                  }
                                  blockdns(twy * CHUNKBLOCKS + y)(twx * CHUNKBLOCKS + x) = cMatrix.blockdns(y)(x)
                                  blockbgs(twy * CHUNKBLOCKS + y)(twx * CHUNKBLOCKS + x) = cMatrix.blockbgs(y)(x)
                                  blockts(twy * CHUNKBLOCKS + y)(twx * CHUNKBLOCKS + x) = cMatrix.blockts(y)(x)
                                  lights(twy * CHUNKBLOCKS + y)(twx * CHUNKBLOCKS + x) = cMatrix.lights(y)(x)
                                  lsources(twy * CHUNKBLOCKS + y)(twx * CHUNKBLOCKS + x) = cMatrix.lsources(y)(x)
                                  zqn(twy * CHUNKBLOCKS + y)(twx * CHUNKBLOCKS + x) = cMatrix.zqn(y)(x)
                                  wcnct(twy * CHUNKBLOCKS + y)(twx * CHUNKBLOCKS + x) = cMatrix.wcnct(y)(x)
                                  drawn(twy * CHUNKBLOCKS + y)(twx * CHUNKBLOCKS + x) = cMatrix.drawn(y)(x)
                                  rdrawn(twy * CHUNKBLOCKS + y)(twx * CHUNKBLOCKS + x) = cMatrix.rdrawn(y)(x)
                                  ldrawn(twy * CHUNKBLOCKS + y)(twx * CHUNKBLOCKS + x) = cMatrix.ldrawn(y)(x)
                                }
                              }
                            }
                            worlds(twy)(twx) = None
                          }
                        }
                      }
                      u = -ou * CHUNKBLOCKS
                      v = -ov * CHUNKBLOCKS
                      (0 until 2).foreach { twy =>
                        (0 until 2).foreach { twx =>
                          kworlds(twy)(twx) = false
                        }
                      }
                      var somevar: Boolean = false
                      (0 until 2).foreach { twy =>
                        (0 until 2).foreach { twx =>
                          val twxc: Int = twx + ou
                          val twyc: Int = twy + ov
                          if (((player.ix + getWidth / 2 + Player.width >= twxc * CHUNKSIZE &&
                              player.ix + getWidth / 2 + Player.width <= twxc * CHUNKSIZE + CHUNKSIZE) ||
                              (player.ix - getWidth / 2 + Player.width + BLOCKSIZE >= twxc * CHUNKSIZE &&
                              player.ix - getWidth / 2 + Player.width - BLOCKSIZE <= twxc * CHUNKSIZE + CHUNKSIZE)) &&
                              ((player.iy + getHeight / 2 + Player.height >= twyc * CHUNKSIZE &&
                              player.iy + getHeight / 2 + Player.height <= twyc * CHUNKSIZE + CHUNKSIZE) ||
                              (player.iy - getHeight / 2 + Player.height >= twyc * CHUNKSIZE &&
                              player.iy - getHeight / 2 + Player.height <= twyc * CHUNKSIZE + CHUNKSIZE))) {
                            kworlds(twy)(twx) = true
                            worlds(twy)(twx) = worlds(twy)(twx).orElse(
                              Some(config.createCompatibleImage(CHUNKSIZE, CHUNKSIZE, Transparency.TRANSLUCENT)))
                            fworlds(twy)(twx) = fworlds(twy)(twx).orElse(
                              Some(config.createCompatibleImage(CHUNKSIZE, CHUNKSIZE, Transparency.TRANSLUCENT)))

                            worlds(twy)(twx).foreach { w =>
                              fworlds(twy)(twx).foreach { fw =>
                                wg2 = w.createGraphics()
                                fwg2 = fw.createGraphics()
                                (max(
                                  twy * CHUNKSIZE,
                                  (player.iy - getHeight / 2 + Player.height / 2 + v * BLOCKSIZE) - 64) until (min(
                                  twy * CHUNKSIZE + CHUNKSIZE,
                                  (player.iy + getHeight / 2 - Player.height / 2 + v * BLOCKSIZE) + 128), BLOCKSIZE))
                                  .foreach { tly =>
                                    (max(
                                      twx * CHUNKSIZE,
                                      (player.ix - getWidth / 2 + Player.width / 2 + u * BLOCKSIZE) - 64) until (min(
                                      twx * CHUNKSIZE + CHUNKSIZE,
                                      (player.ix + getWidth / 2 - Player.width / 2 + u * BLOCKSIZE) + 112), BLOCKSIZE))
                                      .foreach { tlx =>
                                        tx = tlx / BLOCKSIZE
                                        ty = tly / BLOCKSIZE
                                        if (tx >= 0 && tx < theSize && ty >= 0 && ty < theSize) {
                                          if (!drawn(ty)(tx)) {
                                            somevar = true
                                            blockts(ty)(tx) = random.nextInt(8).toByte
                                            (0 until BLOCKSIZE).foreach { y =>
                                              (0 until BLOCKSIZE).foreach { x =>
                                                try {
                                                  w.setRGB(
                                                    tx * BLOCKSIZE - twxc * CHUNKSIZE + x,
                                                    ty * BLOCKSIZE - twyc * CHUNKSIZE + y,
                                                    9539985)
                                                  fw.setRGB(
                                                    tx * BLOCKSIZE - twxc * CHUNKSIZE + x,
                                                    ty * BLOCKSIZE - twyc * CHUNKSIZE + y,
                                                    9539985)
                                                } catch {
                                                  case _: ArrayIndexOutOfBoundsException =>
                                                }
                                              }
                                            }
                                            Background.onBackgroundImage(blockbgs(ty)(tx)) { img =>
                                              drawImage(
                                                wg2,
                                                img.image,
                                                tx * BLOCKSIZE - twx * CHUNKSIZE,
                                                ty * BLOCKSIZE - twy * CHUNKSIZE,
                                                tx * BLOCKSIZE + BLOCKSIZE - twx * CHUNKSIZE,
                                                ty * BLOCKSIZE + BLOCKSIZE - twy * CHUNKSIZE,
                                                0,
                                                0,
                                                IMAGESIZE,
                                                IMAGESIZE
                                              )
                                              ()
                                            }
                                            (0 until 3).foreach { l =>
                                              if (blocks(l)(ty)(tx) =/= AirBlockType) {
                                                if (l === 2) {
                                                  OUTLINES.get(blocks(l)(ty)(tx).id).foreach { outlineName =>
                                                    drawImage(
                                                      fwg2,
                                                      loadBlock(
                                                        blocks(l)(ty)(tx),
                                                        blockds(l)(ty)(tx),
                                                        blockdns(ty)(tx),
                                                        blockts(ty)(tx),
                                                        blocknames,
                                                        outlineName,
                                                        tx,
                                                        ty,
                                                        l),
                                                      tx * BLOCKSIZE - twx * CHUNKSIZE,
                                                      ty * BLOCKSIZE - twy * CHUNKSIZE,
                                                      tx * BLOCKSIZE + BLOCKSIZE - twx * CHUNKSIZE,
                                                      ty * BLOCKSIZE + BLOCKSIZE - twy * CHUNKSIZE,
                                                      0,
                                                      0,
                                                      IMAGESIZE,
                                                      IMAGESIZE
                                                    )
                                                  }
                                                } else {
                                                  OUTLINES.get(blocks(l)(ty)(tx).id).foreach { outlineName =>
                                                    drawImage(
                                                      wg2,
                                                      loadBlock(
                                                        blocks(l)(ty)(tx),
                                                        blockds(l)(ty)(tx),
                                                        blockdns(ty)(tx),
                                                        blockts(ty)(tx),
                                                        blocknames,
                                                        outlineName,
                                                        tx,
                                                        ty,
                                                        l),
                                                      tx * BLOCKSIZE - twx * CHUNKSIZE,
                                                      ty * BLOCKSIZE - twy * CHUNKSIZE,
                                                      tx * BLOCKSIZE + BLOCKSIZE - twx * CHUNKSIZE,
                                                      ty * BLOCKSIZE + BLOCKSIZE - twy * CHUNKSIZE,
                                                      0,
                                                      0,
                                                      IMAGESIZE,
                                                      IMAGESIZE
                                                    )
                                                  }

                                                }
                                              }
                                              if (wcnct(ty)(tx) && blocks(l)(ty)(tx).id >= ZythiumWireBlockType.id && blocks(
                                                    l)(ty)(tx).id <= ZythiumWire5PowerBlockType.id) {
                                                if (l === 2) {
                                                  drawImage(
                                                    fwg2,
                                                    wcnct_px,
                                                    tx * BLOCKSIZE - twx * CHUNKSIZE,
                                                    ty * BLOCKSIZE - twy * CHUNKSIZE,
                                                    tx * BLOCKSIZE + BLOCKSIZE - twx * CHUNKSIZE,
                                                    ty * BLOCKSIZE + BLOCKSIZE - twy * CHUNKSIZE,
                                                    0,
                                                    0,
                                                    IMAGESIZE,
                                                    IMAGESIZE
                                                  )
                                                } else {
                                                  drawImage(
                                                    wg2,
                                                    wcnct_px,
                                                    tx * BLOCKSIZE - twx * CHUNKSIZE,
                                                    ty * BLOCKSIZE - twy * CHUNKSIZE,
                                                    tx * BLOCKSIZE + BLOCKSIZE - twx * CHUNKSIZE,
                                                    ty * BLOCKSIZE + BLOCKSIZE - twy * CHUNKSIZE,
                                                    0,
                                                    0,
                                                    IMAGESIZE,
                                                    IMAGESIZE
                                                  )
                                                }
                                              }
                                            }
                                            if (!DEBUG_LIGHT) {
                                              LIGHTLEVELS
                                                .get(lights(ty)(tx).toInt)
                                                .foreach(drawImage(
                                                  fwg2,
                                                  _,
                                                  tx * BLOCKSIZE - twx * CHUNKSIZE,
                                                  ty * BLOCKSIZE - twy * CHUNKSIZE,
                                                  tx * BLOCKSIZE + BLOCKSIZE - twx * CHUNKSIZE,
                                                  ty * BLOCKSIZE + BLOCKSIZE - twy * CHUNKSIZE,
                                                  0,
                                                  0,
                                                  IMAGESIZE,
                                                  IMAGESIZE
                                                ))
                                            }
                                            drawn(ty)(tx) = true
                                            rdrawn(ty)(tx) = true
                                            ldrawn(ty)(tx) = true
                                          }
                                          if (!rdrawn(ty)(tx)) {
                                            somevar = true
                                            (0 until BLOCKSIZE).foreach { y =>
                                              (0 until BLOCKSIZE).foreach { x =>
                                                try {
                                                  w.setRGB(
                                                    tx * BLOCKSIZE - twxc * CHUNKSIZE + x,
                                                    ty * BLOCKSIZE - twyc * CHUNKSIZE + y,
                                                    9539985)
                                                  fw.setRGB(
                                                    tx * BLOCKSIZE - twxc * CHUNKSIZE + x,
                                                    ty * BLOCKSIZE - twyc * CHUNKSIZE + y,
                                                    9539985)
                                                } catch {
                                                  case _: ArrayIndexOutOfBoundsException =>
                                                }
                                              }
                                            }
                                            Background.onBackgroundImage(blockbgs(ty)(tx)) { img =>
                                              drawImage(
                                                wg2,
                                                img.image,
                                                tx * BLOCKSIZE - twx * CHUNKSIZE,
                                                ty * BLOCKSIZE - twy * CHUNKSIZE,
                                                tx * BLOCKSIZE + BLOCKSIZE - twx * CHUNKSIZE,
                                                ty * BLOCKSIZE + BLOCKSIZE - twy * CHUNKSIZE,
                                                0,
                                                0,
                                                IMAGESIZE,
                                                IMAGESIZE
                                              )
                                              ()
                                            }
                                            (0 until 3).foreach { l =>
                                              if (blocks(l)(ty)(tx) =/= AirBlockType) {
                                                if (l === 2) {
                                                  OUTLINES.get(blocks(l)(ty)(tx).id).foreach { outlineName =>
                                                    drawImage(
                                                      fwg2,
                                                      loadBlock(
                                                        blocks(l)(ty)(tx),
                                                        blockds(l)(ty)(tx),
                                                        blockdns(ty)(tx),
                                                        blockts(ty)(tx),
                                                        blocknames,
                                                        outlineName,
                                                        tx,
                                                        ty,
                                                        l),
                                                      tx * BLOCKSIZE - twx * CHUNKSIZE,
                                                      ty * BLOCKSIZE - twy * CHUNKSIZE,
                                                      tx * BLOCKSIZE + BLOCKSIZE - twx * CHUNKSIZE,
                                                      ty * BLOCKSIZE + BLOCKSIZE - twy * CHUNKSIZE,
                                                      0,
                                                      0,
                                                      IMAGESIZE,
                                                      IMAGESIZE
                                                    )
                                                  }
                                                } else {
                                                  OUTLINES.get(blocks(l)(ty)(tx).id).foreach { outlineName =>
                                                    drawImage(
                                                      wg2,
                                                      loadBlock(
                                                        blocks(l)(ty)(tx),
                                                        blockds(l)(ty)(tx),
                                                        blockdns(ty)(tx),
                                                        blockts(ty)(tx),
                                                        blocknames,
                                                        outlineName,
                                                        tx,
                                                        ty,
                                                        l),
                                                      tx * BLOCKSIZE - twx * CHUNKSIZE,
                                                      ty * BLOCKSIZE - twy * CHUNKSIZE,
                                                      tx * BLOCKSIZE + BLOCKSIZE - twx * CHUNKSIZE,
                                                      ty * BLOCKSIZE + BLOCKSIZE - twy * CHUNKSIZE,
                                                      0,
                                                      0,
                                                      IMAGESIZE,
                                                      IMAGESIZE
                                                    )
                                                  }

                                                }
                                              }
                                              if (wcnct(ty)(tx) && blocks(l)(ty)(tx).id >= ZythiumWireBlockType.id && blocks(
                                                    l)(ty)(tx).id <= ZythiumWire5PowerBlockType.id) {
                                                if (l === 2) {
                                                  drawImage(
                                                    fwg2,
                                                    wcnct_px,
                                                    tx * BLOCKSIZE - twx * CHUNKSIZE,
                                                    ty * BLOCKSIZE - twy * CHUNKSIZE,
                                                    tx * BLOCKSIZE + BLOCKSIZE - twx * CHUNKSIZE,
                                                    ty * BLOCKSIZE + BLOCKSIZE - twy * CHUNKSIZE,
                                                    0,
                                                    0,
                                                    IMAGESIZE,
                                                    IMAGESIZE
                                                  )
                                                } else {
                                                  drawImage(
                                                    wg2,
                                                    wcnct_px,
                                                    tx * BLOCKSIZE - twx * CHUNKSIZE,
                                                    ty * BLOCKSIZE - twy * CHUNKSIZE,
                                                    tx * BLOCKSIZE + BLOCKSIZE - twx * CHUNKSIZE,
                                                    ty * BLOCKSIZE + BLOCKSIZE - twy * CHUNKSIZE,
                                                    0,
                                                    0,
                                                    IMAGESIZE,
                                                    IMAGESIZE
                                                  )
                                                }
                                              }
                                            }
                                            if (!DEBUG_LIGHT) {
                                              LIGHTLEVELS
                                                .get(lights(ty)(tx).toInt)
                                                .foreach(drawImage(
                                                  fwg2,
                                                  _,
                                                  tx * BLOCKSIZE - twx * CHUNKSIZE,
                                                  ty * BLOCKSIZE - twy * CHUNKSIZE,
                                                  tx * BLOCKSIZE + BLOCKSIZE - twx * CHUNKSIZE,
                                                  ty * BLOCKSIZE + BLOCKSIZE - twy * CHUNKSIZE,
                                                  0,
                                                  0,
                                                  IMAGESIZE,
                                                  IMAGESIZE
                                                ))
                                            }
                                            drawn(ty)(tx) = true
                                            rdrawn(ty)(tx) = true
                                            ldrawn(ty)(tx) = true
                                          }
                                          if (!ldrawn(ty)(tx) && random.nextInt(10) === 0) {
                                            somevar = true
                                            (0 until BLOCKSIZE).foreach { y =>
                                              (0 until BLOCKSIZE).foreach { x =>
                                                try {
                                                  w.setRGB(
                                                    tx * BLOCKSIZE - twxc * CHUNKSIZE + x,
                                                    ty * BLOCKSIZE - twyc * CHUNKSIZE + y,
                                                    9539985)
                                                  fw.setRGB(
                                                    tx * BLOCKSIZE - twxc * CHUNKSIZE + x,
                                                    ty * BLOCKSIZE - twyc * CHUNKSIZE + y,
                                                    9539985)
                                                } catch {
                                                  case _: ArrayIndexOutOfBoundsException =>
                                                }
                                              }
                                            }
                                            Background.onBackgroundImage(blockbgs(ty)(tx)) { img =>
                                              drawImage(
                                                wg2,
                                                img.image,
                                                tx * BLOCKSIZE - twx * CHUNKSIZE,
                                                ty * BLOCKSIZE - twy * CHUNKSIZE,
                                                tx * BLOCKSIZE + BLOCKSIZE - twx * CHUNKSIZE,
                                                ty * BLOCKSIZE + BLOCKSIZE - twy * CHUNKSIZE,
                                                0,
                                                0,
                                                IMAGESIZE,
                                                IMAGESIZE
                                              )
                                              ()
                                            }

                                            (0 until 3).foreach { l =>
                                              if (blocks(l)(ty)(tx) =/= AirBlockType) {
                                                if (l === 2) {
                                                  OUTLINES.get(blocks(l)(ty)(tx).id).foreach { outlineName =>
                                                    drawImage(
                                                      fwg2,
                                                      loadBlock(
                                                        blocks(l)(ty)(tx),
                                                        blockds(l)(ty)(tx),
                                                        blockdns(ty)(tx),
                                                        blockts(ty)(tx),
                                                        blocknames,
                                                        outlineName,
                                                        tx,
                                                        ty,
                                                        l),
                                                      tx * BLOCKSIZE - twx * CHUNKSIZE,
                                                      ty * BLOCKSIZE - twy * CHUNKSIZE,
                                                      tx * BLOCKSIZE + BLOCKSIZE - twx * CHUNKSIZE,
                                                      ty * BLOCKSIZE + BLOCKSIZE - twy * CHUNKSIZE,
                                                      0,
                                                      0,
                                                      IMAGESIZE,
                                                      IMAGESIZE
                                                    )
                                                  }

                                                } else {
                                                  OUTLINES.get(blocks(l)(ty)(tx).id).foreach { outlineName =>
                                                    drawImage(
                                                      wg2,
                                                      loadBlock(
                                                        blocks(l)(ty)(tx),
                                                        blockds(l)(ty)(tx),
                                                        blockdns(ty)(tx),
                                                        blockts(ty)(tx),
                                                        blocknames,
                                                        outlineName,
                                                        tx,
                                                        ty,
                                                        l),
                                                      tx * BLOCKSIZE - twx * CHUNKSIZE,
                                                      ty * BLOCKSIZE - twy * CHUNKSIZE,
                                                      tx * BLOCKSIZE + BLOCKSIZE - twx * CHUNKSIZE,
                                                      ty * BLOCKSIZE + BLOCKSIZE - twy * CHUNKSIZE,
                                                      0,
                                                      0,
                                                      IMAGESIZE,
                                                      IMAGESIZE
                                                    )
                                                  }

                                                }
                                              }
                                              if (wcnct(ty)(tx) && blocks(l)(ty)(tx).id >= ZythiumWireBlockType.id && blocks(
                                                    l)(ty)(tx).id <= ZythiumWire5PowerBlockType.id) {
                                                if (l === 2) {
                                                  drawImage(
                                                    fwg2,
                                                    wcnct_px,
                                                    tx * BLOCKSIZE - twx * CHUNKSIZE,
                                                    ty * BLOCKSIZE - twy * CHUNKSIZE,
                                                    tx * BLOCKSIZE + BLOCKSIZE - twx * CHUNKSIZE,
                                                    ty * BLOCKSIZE + BLOCKSIZE - twy * CHUNKSIZE,
                                                    0,
                                                    0,
                                                    IMAGESIZE,
                                                    IMAGESIZE
                                                  )
                                                } else {
                                                  drawImage(
                                                    wg2,
                                                    wcnct_px,
                                                    tx * BLOCKSIZE - twx * CHUNKSIZE,
                                                    ty * BLOCKSIZE - twy * CHUNKSIZE,
                                                    tx * BLOCKSIZE + BLOCKSIZE - twx * CHUNKSIZE,
                                                    ty * BLOCKSIZE + BLOCKSIZE - twy * CHUNKSIZE,
                                                    0,
                                                    0,
                                                    IMAGESIZE,
                                                    IMAGESIZE
                                                  )
                                                }
                                              }
                                            }
                                            if (!DEBUG_LIGHT) {
                                              LIGHTLEVELS
                                                .get(lights(ty)(tx).toInt)
                                                .foreach(drawImage(
                                                  fwg2,
                                                  _,
                                                  tx * BLOCKSIZE - twx * CHUNKSIZE,
                                                  ty * BLOCKSIZE - twy * CHUNKSIZE,
                                                  tx * BLOCKSIZE + BLOCKSIZE - twx * CHUNKSIZE,
                                                  ty * BLOCKSIZE + BLOCKSIZE - twy * CHUNKSIZE,
                                                  0,
                                                  0,
                                                  IMAGESIZE,
                                                  IMAGESIZE
                                                ))
                                            }
                                            drawn(ty)(tx) = true
                                            rdrawn(ty)(tx) = true
                                            ldrawn(ty)(tx) = true
                                          }
                                        }
                                      }
                                  }
                              }
                            }
                          }
                        }
                      }
                      if (somevar) {
                        println("Drew at least one block.")
                      }
                      (0 until 2).foreach { twy =>
                        (0 until 2).foreach { twx =>
                          if (!kworlds(twy)(twx) && worlds(twy)(twx).isDefined) {
                            worlds(twy)(twx) = None
                            fworlds(twy)(twx) = None
                            (twy * CHUNKBLOCKS until twy * CHUNKBLOCKS + CHUNKBLOCKS).foreach { ty =>
                              (twx * CHUNKBLOCKS until twx * CHUNKBLOCKS + CHUNKBLOCKS).foreach { tx =>
                                if (tx >= 0 && tx < theSize && ty >= 0 && ty < theSize) {
                                  drawn(ty)(tx) = false
                                }
                              }
                            }
                            println("Destroyed image at " + twx + " " + twy)
                          }
                        }
                      }
                      updateApp()
                      updateEnvironment()
                      player.update(blocks(1), userInput, u, v)
                      if (timeOfDay >= 86400) {
                        timeOfDay = 0
                        day += 1
                      }
                      repaint()
                      ready = true
                    }
                  } catch {
                    case NonFatal(e) => postError(e)
                  }
                }
              }
              timer = new javax.swing.Timer(20, mainthread)

              val (mouseX, mouseY) = userInput.currentMousePosition
              if (state === TitleScreen && !menuPressed) {
                if (mouseX >= 239 && mouseX <= 557) {
                  if (mouseY >= 213 && mouseY <= 249) { // singleplayer
                    findWorlds()
                    state = SelectWorld
                    repaint()
                    menuPressed = true
                  }
                }
              }
              if (state === SelectWorld && !menuPressed) {
                if (mouseX >= 186 && mouseX <= 615 &&
                    mouseY >= 458 && mouseY <= 484) { // create new world
                  state = NewWorld
                  newWorldName = TextField(400, "New World")
                  repaint()
                  menuPressed = true
                }
                if (mouseX >= 334 && mouseX <= 457 &&
                    mouseY >= 504 && mouseY <= 530) { // back
                  state = TitleScreen
                  repaint()
                  menuPressed = true
                }
                import scala.util.control.Breaks._
                breakable {
                  worldFiles.indices.foreach { i =>
                    if (mouseX >= 166 && mouseX <= 470 &&
                        mouseY >= 117 + i * 35 && mouseY <= 152 + i * 35) { // load world
                      currentWorld = worldNames(i)
                      state = LoadingWorld
                      bg = Color.BLACK
                      if (loadWorld(worldFiles(i))) {
                        menuTimer.stop()
                        bg = CYANISH
                        state = InGame
                        ready = true
                        timer.start()
                        break
                      }
                    }
                  }
                }
              }
              if (state === NewWorld && !menuPressed) {
                if (mouseX >= 186 && mouseX <= 615 &&
                    mouseY >= 458 && mouseY <= 484) { // create new world
                  if (newWorldName.text =/= "") {
                    findWorlds()
                    doGenerateWorld = true
                    worldNames.indices.foreach { i =>
                      if (newWorldName.text === worldNames(i)) {
                        doGenerateWorld = false
                      }
                    }
                    if (doGenerateWorld) {
                      menuTimer.stop()
                      bg = Color.BLACK
                      state = GeneratingWorld
                      currentWorld = newWorldName.text
                      repaint()
                      val createWorldThread: Action = new AbstractAction() {
                        def actionPerformed(ae: ActionEvent): Unit = {
                          try {
                            createNewWorld()
                            bg = CYANISH
                            state = InGame
                            ready = true
                            timer.start()
                            createWorldTimer.stop()
                          } catch {
                            case NonFatal(e) => postError(e)
                          }
                        }
                      }
                      createWorldTimer = new javax.swing.Timer(1, createWorldThread)
                      createWorldTimer.start()
                    }
                  }
                }
                if (mouseX >= 334 && mouseX <= 457 &&
                    mouseY >= 504 && mouseY <= 530) { // back
                  state = SelectWorld
                  repaint()
                  menuPressed = true
                }
              }
            }
          } catch {
            case NonFatal(e) => postError(e)
          }
        }
      }
      menuTimer = new javax.swing.Timer(20, actionListener)
      menuTimer.start()
    } catch {
      case NonFatal(e) => postError(e)
    }
  }

  def findWorlds(): Unit = {
    val folder: File = new File("worlds")
    folder.mkdir()
    val files = folder.listFiles()

    val (wordFilesTemp, worldNamesTemp) = files
      .filter(f => f.isFile && f.getName.endsWith(".dat"))
      .foldLeft((List.empty[String], List.empty[String])) { (acc, file) =>
        val wfs     = acc._1
        val wns     = acc._2
        val newFile = file.getName
        val newName = newFile.substring(0, newFile.length() - 4)
        (newFile :: wfs, newName :: wns)
      }

    worldFiles = wordFilesTemp
    worldNames = worldNamesTemp

  }

  def createNewWorld(): Unit = {
    temporarySaveFile = Array.fill(2, 2)(None)
    chunkMatrix = Array.fill(2, 2)(None)

    blocks = Array.ofDim(3, theSize, theSize)
    blockds = Array.ofDim(3, theSize, theSize)
    blockdns = Array.ofDim(theSize, theSize)
    blockbgs = Array.ofDim(theSize, theSize)
    blockts = Array.ofDim(theSize, theSize)
    lights = Array.ofDim(theSize, theSize)
    power = Array.ofDim(3, theSize, theSize)
    lsources = Array.ofDim(theSize, theSize)
    zqn = Array.ofDim(theSize, theSize)
    pzqn = Array.ofDim(3, theSize, theSize)
    arbprd = Array.ofDim(3, theSize, theSize)
    wcnct = Array.ofDim(theSize, theSize)
    drawn = Array.ofDim(theSize, theSize)
    rdrawn = Array.ofDim(theSize, theSize)
    ldrawn = Array.ofDim(theSize, theSize)
    lqd = Array.fill(theSize, theSize)(false)
    zqd = Array.fill(theSize, theSize)(false)

    player = new Player(WIDTH * 0.5 * BLOCKSIZE, 45)

    inventory = new Inventory()

    DEBUG_ITEMS match {
      case Some(NormalDebugItem) =>
        inventory.addItem(IrradiumPickUiItem, 1.toShort)
        inventory.addItem(IrradiumAxeUiItem, 1.toShort)
        inventory.addItem(IrradiumSwordUiItem, 1.toShort)
        inventory.addItem(StoneBricksUiItem, 100.toShort)
        inventory.addItem(WoodenTorchUiItem, 100.toShort)
        inventory.addItem(CoalTorchUiItem, 100.toShort)
        inventory.addItem(LumenstoneTorchUiItem, 100.toShort)
        inventory.addItem(WorkbenchUiItem, 5.toShort)
        inventory.addItem(FurnaceUiItem, 5.toShort)
        inventory.addItem(StoneLighterUiItem, 1.toShort)
        inventory.addItem(CoalUiItem, 100.toShort)
        inventory.addItem(MagnetiteOreUiItem, 100.toShort)
        inventory.addItem(DirtUiItem, 100.toShort)
        inventory.addItem(StoneUiItem, 100.toShort)
        inventory.addItem(WoodUiItem, 100.toShort)

      case Some(ToolsDebugItem) =>
        inventory.addItem(WoodenPickUiItem, 1.toShort)
        inventory.addItem(WoodenAxeUiItem, 1.toShort)
        inventory.addItem(WoodenSwordUiItem, 1.toShort)
        inventory.addItem(StonePickUiItem, 1.toShort)
        inventory.addItem(StoneAxeUiItem, 1.toShort)
        inventory.addItem(StoneSwordUiItem, 1.toShort)
        inventory.addItem(CopperPickUiItem, 1.toShort)
        inventory.addItem(CopperAxeUiItem, 1.toShort)
        inventory.addItem(IronAxeUiItem, 1.toShort)
        inventory.addItem(IronPickUiItem, 1.toShort)
        inventory.addItem(SilverAxeUiItem, 1.toShort)
        inventory.addItem(GoldAxeUiItem, 1.toShort)
        inventory.addItem(SilverPickUiItem, 1.toShort)
        inventory.addItem(CopperSwordUiItem, 1.toShort)
        inventory.addItem(IronSwordUiItem, 1.toShort)
        inventory.addItem(GoldPickUiItem, 1.toShort)
        inventory.addItem(SilverSwordUiItem, 1.toShort)
        inventory.addItem(StoneLighterUiItem, 1.toShort)
        inventory.addItem(ZincPickUiItem, 1.toShort)
        inventory.addItem(ZincAxeUiItem, 1.toShort)
        inventory.addItem(ZincSwordUiItem, 1.toShort)
        inventory.addItem(RhymestonePickUiItem, 1.toShort)
        inventory.addItem(RhymestoneAxeUiItem, 1.toShort)
        inventory.addItem(RhymestoneSwordUiItem, 1.toShort)
        inventory.addItem(ObduritePickUiItem, 1.toShort)
        inventory.addItem(ObduriteAxeUiItem, 1.toShort)
        inventory.addItem(ObduriteSwordUiItem, 1.toShort)
        inventory.addItem(AluminumPickUiItem, 1.toShort)
        inventory.addItem(AluminumAxeUiItem, 1.toShort)
        inventory.addItem(AluminumSwordUiItem, 1.toShort)
        inventory.addItem(LeadPickUiItem, 1.toShort)
        inventory.addItem(LeadAxeUiItem, 1.toShort)
        inventory.addItem(LeadSwordUiItem, 1.toShort)
        inventory.addItem(MagnetitePickUiItem, 1.toShort)
        inventory.addItem(MagnetiteAxeUiItem, 1.toShort)
        inventory.addItem(MagnetiteSwordUiItem, 1.toShort)
        inventory.addItem(IrradiumPickUiItem, 1.toShort)
        inventory.addItem(IrradiumAxeUiItem, 1.toShort)
        inventory.addItem(IrradiumSwordUiItem, 1.toShort)

        inventory.addItem(GoldSwordUiItem, 1.toShort)

      case Some(TestingDebugItem) =>
        inventory.addItem(IrradiumPickUiItem, 1.toShort)
        inventory.addItem(IrradiumAxeUiItem, 1.toShort)
        inventory.addItem(ZythiumWireUiItem, 100.toShort)
        inventory.addItem(WoodUiItem, 100.toShort)
        inventory.addItem(WoodenTorchUiItem, 100.toShort)
        inventory.addItem(CoalTorchUiItem, 100.toShort)
        inventory.addItem(LumenstoneTorchUiItem, 100.toShort)
        inventory.addItem(ZythiumTorchUiItem, 100.toShort)
        inventory.addItem(ZythiumLampUiItem, 100.toShort)
        inventory.addItem(LeverUiItem, 100.toShort)
        inventory.addItem(FurnaceUiItem, 100.toShort)
        inventory.addItem(StoneLighterUiItem, 1.toShort)
        inventory.addItem(FrostleafUiItem, 100.toShort)
        inventory.addItem(SkystoneUiItem, 100.toShort)
        inventory.addItem(ZythiumAmplifierUiItem, 100.toShort)
        inventory.addItem(ZythiumInverterUiItem, 100.toShort)
        inventory.addItem(ButtonUiItem, 100.toShort)
        inventory.addItem(WoodenPressurePlateUiItem, 100.toShort)
        inventory.addItem(StonePressurePlateUiItem, 100.toShort)
        inventory.addItem(ZythiumPressurePlateUiItem, 100.toShort)
        inventory.addItem(ZythiumDelayer1UiItem, 100.toShort)
        inventory.addItem(ZythiumDelayer2UiItem, 100.toShort)
        inventory.addItem(ZythiumDelayer4UiItem, 100.toShort)
        inventory.addItem(ZythiumDelayer8UiItem, 100.toShort)
        inventory.addItem(WrenchUiItem, 1.toShort)

      case None =>
    }

    cic = Some(new ItemCollection(Crafting))
    cic.foreach(inventory.renderCollection)

    armor = new ItemCollection(Armor)
    inventory.renderCollection(armor)

    toolAngle = 4.7
    mining = 0
    miningTool = EmptyUiItem
    mx = 0
    my = 0
    moveItem = EmptyUiItem
    moveNum = 0
    moveDur = 0

    entities = mutable.ArrayBuffer.empty[Entity]

    cloudsx = mutable.ArrayBuffer.empty[Double]
    cloudsy = mutable.ArrayBuffer.empty[Double]
    cloudsv = mutable.ArrayBuffer.empty[Double]
    cloudsn = mutable.ArrayBuffer.empty[Int]

    machinesx = mutable.ArrayBuffer.empty[Int]
    machinesy = mutable.ArrayBuffer.empty[Int]

    icmatrix = Array.ofDim(3, HEIGHT, WIDTH)

    worlds = Array.fill(2, 2)(None)
    fworlds = Array.fill(2, 2)(None)
    kworlds = Array.fill(2, 2)(false)

    pqx = mutable.ArrayBuffer.empty[Int]
    pqy = mutable.ArrayBuffer.empty[Int]

    println("-> Adding light sources...")

    lqx = mutable.ArrayBuffer.empty[Int]
    lqy = mutable.ArrayBuffer.empty[Int]
    zqx = mutable.ArrayBuffer.empty[Int]
    zqy = mutable.ArrayBuffer.empty[Int]
    pqx = mutable.ArrayBuffer.empty[Int]
    pqy = mutable.ArrayBuffer.empty[Int]
    pzqx = mutable.ArrayBuffer.empty[Int]
    pzqy = mutable.ArrayBuffer.empty[Int]
    updatex = mutable.ArrayBuffer.empty[Int]
    updatey = mutable.ArrayBuffer.empty[Int]
    updatet = mutable.ArrayBuffer.empty[Int]
    updatel = mutable.ArrayBuffer.empty[Int]

    (0 until WIDTH).foreach { x =>
      //            addSunLighting(x, 0)
    }

    (0 until WIDTH).foreach { x =>
      (0 until HEIGHT).foreach { y =>
        //                addBlockLighting(x, y)
        //                addBlockPower(x, y)
      }
    }

    println("-> Calculating light...")

    resolvePowerMatrix()
    resolveLightMatrix()

    println("Finished generation.")
  }

  def updateApp(): Unit = {
    val (mouseX, mouseY) = userInput.currentMousePosition

    val playerMouseXOffset = mouseX + player.ix - getWidth() / 2 + Player.width / 2
    val playerMouseYOffset = mouseY + player.iy - getHeight() / 2 + Player.height / 2

    currentSkyLight = skycolors(0)
    skycolors.indices.foreach { i =>
      if (timeOfDay >= skycolors(i)) {
        currentSkyLight = skycolors(i)
      }
    }
    if (player.y / 16 > HEIGHT * 0.55) {
      bg = Color.BLACK
    } else {
      SKYCOLORS.get(currentSkyLight).foreach { s =>
        bg = s
      }
    }

    if (rgnc1 === 0) {
      if (rgnc2 === 0) {
        if (player.hp < Player.totalHP) {
          player.hp += 1
          rgnc2 = 125
        }
      } else {
        rgnc2 -= 1
      }
    } else {
      rgnc1 -= 1
    }

    machinesx.indices.foreach { j =>
      x = machinesx(j)
      y = machinesy(j)
      (0 until 3).foreach { l =>
        icmatrix(l)(y)(x).foreach { icMatrixTemp =>
          if (icMatrixTemp.icType === Furnace) {
            if (icMatrixTemp.F_ON) {
              if (icMatrixTemp.ids(1) === EmptyUiItem) {
                FUELS
                  .get(icMatrixTemp.ids(2).id)
                  .fold {
                    icMatrixTemp.F_ON = false
                    removeBlockLighting(x, y)
                    blocks(l)(y)(x) = FurnaceBlockType
                    rdrawn(y)(x) = false
                  } { _ =>
                    inventory.addLocationIC(icMatrixTemp, 1, icMatrixTemp.ids(2), 1.toShort)
                    inventory.removeLocationIC(icMatrixTemp, 2, 1.toShort)
                    icMatrixTemp.FUELP = 1
                  }
              }
              FUELS.get(icMatrixTemp.ids(1).id).foreach { fuel =>
                icMatrixTemp.FUELP -= fuel
                if (icMatrixTemp.FUELP < 0) {
                  icMatrixTemp.FUELP = 0
                  inventory.removeLocationIC(icMatrixTemp, 1, icMatrixTemp.nums(1))
                }
                import scala.util.control.Breaks._
                breakable {
                  FRI1.indices.foreach { i =>
                    FSPEED.get(icMatrixTemp.ids(1).id).foreach { fspeed =>
                      if (icMatrixTemp.ids(0) === FRI1(i) && icMatrixTemp.nums(0) >= FRN1(i)) {
                        icMatrixTemp.SMELTP += fspeed
                        if (icMatrixTemp.SMELTP > 1) {
                          icMatrixTemp.SMELTP = 0
                          inventory.removeLocationIC(icMatrixTemp, 0, FRN1(i))
                          inventory.addLocationIC(icMatrixTemp, 3, FRI2(i), FRN2(i))
                        }
                        break
                      }
                    }
                  }
                }
              }
            } else {
              icMatrixTemp.SMELTP -= 0.00025
              if (icMatrixTemp.SMELTP < 0) {
                icMatrixTemp.SMELTP = 0
              }
            }
          }
        }
      }
    }

    ic.foreach { icTemp =>
      if (icTemp.icType === Furnace) {
        if (icTemp.F_ON) {
          if (icTemp.ids(1) === EmptyUiItem) {
            FUELS
              .get(icTemp.ids(2).id)
              .fold {

                icTemp.F_ON = false
                removeBlockLighting(icx, icy)
                blocks(iclayer)(icy)(icx) = FurnaceBlockType
                rdrawn(icy)(icx) = false

              } { _ =>
                inventory.addLocationIC(icTemp, 1, icTemp.ids(2), 1.toShort)
                inventory.removeLocationIC(icTemp, 2, 1.toShort)
                icTemp.FUELP = 1

              }
          }
          FUELS.get(icTemp.ids(1).id).foreach { fuels =>
            icTemp.FUELP -= fuels
            if (icTemp.FUELP < 0) {
              icTemp.FUELP = 0
              inventory.removeLocationIC(icTemp, 1, icTemp.nums(1))
            }
            import scala.util.control.Breaks._
            breakable {
              FRI1.indices.foreach { i =>
                FSPEED.get(icTemp.ids(1).id).foreach { fspeed =>
                  if (icTemp.ids(0) === FRI1(i) && icTemp.nums(0) >= FRN1(i)) {
                    icTemp.SMELTP += fspeed
                    if (icTemp.SMELTP > 1) {
                      icTemp.SMELTP = 0
                      inventory.removeLocationIC(icTemp, 0, FRN1(i))
                      inventory.addLocationIC(icTemp, 3, FRI2(i), FRN2(i))
                    }
                    break
                  }
                }
              }
            }
          }
        } else {
          icTemp.SMELTP -= 0.00025
          if (icTemp.SMELTP < 0) {
            icTemp.SMELTP = 0
          }
        }
        inventory.updateIC(icTemp, -1)
      }
    }

    if (sqrt(
          pow(player.x + player.image.getWidth() - icx * BLOCKSIZE + BLOCKSIZE / 2, 2) + pow(
            player.y + player.image.getHeight() - icy * BLOCKSIZE + BLOCKSIZE / 2,
            2)) > 160) {
      ic.foreach { icTemp =>
        if (icTemp.icType =/= Workbench) {
          machinesx += icx
          machinesy += icy
          icmatrix(iclayer)(icy)(icx) = Some(ItemCollection(icTemp.icType, icTemp.ids, icTemp.nums, icTemp.durs))
        }
        if (icTemp.icType === Workbench) {
          if (player.imgState === Player.StillRight || player.imgState === Player.WalkRight1 || player.imgState === Player.WalkRight2) {
            (0 until 9).foreach { i =>
              UiItem.onImageItem(icTemp.ids(i)) { imgItm =>
                entities += IdEntity(
                  (icx * BLOCKSIZE).toDouble,
                  (icy * BLOCKSIZE).toDouble,
                  2,
                  -2,
                  imgItm,
                  icTemp.nums(i),
                  icTemp.durs(i),
                  75)
                ()
              }
            }
          }
          if (player.imgState === Player.StillLeft || player.imgState === Player.WalkLeft1 || player.imgState === Player.WalkLeft2) {
            (0 until 9).foreach { i =>
              UiItem.onImageItem(icTemp.ids(i)) { imgItm =>
                entities += IdEntity(
                  (icx * BLOCKSIZE).toDouble,
                  (icy * BLOCKSIZE).toDouble,
                  -2,
                  -2,
                  imgItm,
                  icTemp.nums(i),
                  icTemp.durs(i),
                  75)
                ()
              }
            }
          }
        }
        if (icTemp.icType === Furnace) {
          icmatrix(iclayer)(icy)(icx).foreach { icMatrixTemp =>
            icMatrixTemp.FUELP = icTemp.FUELP
            icMatrixTemp.SMELTP = icTemp.SMELTP
            icMatrixTemp.F_ON = icTemp.F_ON
          }

        }
        ic = None

      }
    }

    (0 until 3).foreach { l =>
      (0 until theSize).foreach { y =>
        (0 until theSize).foreach { x =>
          if (random.nextInt(22500) === 0) {
            t = AirBlockType
            blocks(l)(y)(x) match {
              case SunflowerStage1BlockType if timeOfDay >= 75913 || timeOfDay < 28883  => t = SunflowerStage2BlockType
              case SunflowerStage2BlockType if timeOfDay >= 75913 || timeOfDay < 28883  => t = SunflowerStage3BlockType
              case MoonflowerStage1BlockType if timeOfDay >= 32302 && timeOfDay < 72093 => t = MoonflowerStage2BlockType
              case MoonflowerStage2BlockType if timeOfDay >= 32302 && timeOfDay < 72093 => t = MoonflowerStage3BlockType
              case DryweedStage1BlockType if checkBiome(x, y, u, v, blocks, blockbgs) === DesertBiome =>
                t = DryweedStage2BlockType
              case DryweedStage2BlockType if checkBiome(x, y, u, v, blocks, blockbgs) === DesertBiome =>
                t = DryweedStage3BlockType
              case GreenleafStage1BlockType if checkBiome(x, y, u, v, blocks, blockbgs) === JungleBiome =>
                t = GreenleafStage2BlockType
              case GreenleafStage2BlockType if checkBiome(x, y, u, v, blocks, blockbgs) === JungleBiome =>
                t = GreenleafStage3BlockType
              case FrostleafStage1BlockType if checkBiome(x, y, u, v, blocks, blockbgs) === FrostBiome =>
                t = FrostleafStage2BlockType
              case FrostleafStage2BlockType if checkBiome(x, y, u, v, blocks, blockbgs) === FrostBiome =>
                t = FrostleafStage3BlockType
              case CaverootStage1BlockType
                  if checkBiome(x, y, u, v, blocks, blockbgs) === CavernBiome || y >= 0 /*stonelayer(x)*/ =>
                t = CaverootStage2BlockType
              case CaverootStage2BlockType
                  if checkBiome(x, y, u, v, blocks, blockbgs) === CavernBiome || y >= 0 /*stonelayer(x)*/ =>
                t = CaverootStage3BlockType
              case SkyblossomStage1BlockType if y <= HEIGHT * 0.08 && random.nextInt(3) === 0 || y <= HEIGHT * 0.04 =>
                t = SkyblossomStage2BlockType
              case SkyblossomStage2BlockType if y <= HEIGHT * 0.08 && random.nextInt(3) === 0 || y <= HEIGHT * 0.04 =>
                t = SkyblossomStage3BlockType
              case VoidRotStage1BlockType if y >= HEIGHT * 0.98 => t = VoidRotStage2BlockType
              case VoidRotStage2BlockType if y >= HEIGHT * 0.98 => t = VoidRotStage3BlockType
              case MarshleafStage1BlockType if checkBiome(x, y, u, v, blocks, blockbgs) === SwampBiome =>
                t = MarshleafStage2BlockType
              case MarshleafStage2BlockType if checkBiome(x, y, u, v, blocks, blockbgs) === SwampBiome =>
                t = MarshleafStage3BlockType
              case _ =>
            }
            if (t =/= AirBlockType) {
              blocks(l)(y)(x) = t
              drawn(y)(x) = false
            }
          }
        }
      }
    }

    (0 until 3).foreach { l =>
      (0 until theSize).foreach { y =>
        (0 until theSize).foreach { x =>
          if (random.nextInt(1000) === 0) {
            if (y >= 1 && y < HEIGHT - 1) {
              doGrassGrow = false
              if (blocks(l)(y)(x) === DirtBlockType && hasOpenSpace(x + u, y + v, l) && blocks(l)(
                    y + random.nextInt(3) - 1 + u)(x + random.nextInt(3) - 1 + v) === GrassBlockType) {
                blocks(l)(y)(x) = GrassBlockType
                doGrassGrow = true
              } // TODO: pattern matching
              if (blocks(l)(y)(x) === DirtBlockType && hasOpenSpace(x + u, y + v, l) && blocks(l)(
                    y + random.nextInt(3) - 1 + u)(x + random.nextInt(3) - 1 + v) === JungleGrassBlockType) {
                blocks(l)(y)(x) = JungleGrassBlockType
                doGrassGrow = true
              }
              if (blocks(l)(y)(x) === MudBlockType && hasOpenSpace(x + u, y + v, l) && blocks(l)(
                    y + random.nextInt(3) - 1 + u)(x + random.nextInt(3) - 1 + v) === SwampGrassBlockType) {
                blocks(l)(y)(x) = SwampGrassBlockType
                doGrassGrow = true
              }
              if (doGrassGrow) {
                (y - 1 until y + 2).foreach { y2 =>
                  (x - 1 until x + 2).foreach { x2 =>
                    if (y2 >= 0 && y2 < HEIGHT) {
                      drawn(y2)(x2) = false
                    }
                  }
                }
              }
            }
          }
        }
      }
    }

    (0 until theSize).foreach { y =>
      (0 until theSize).foreach { x =>
        if (random.nextInt(1000) === 0) {
          if (blocks(1)(y)(x) === TreeNoBarkBlockType) {
            blocks(1)(y)(x) = TreeBlockType
          }
        }
      }
    }

    (updatex.length - 1 until (-1, -1)).foreach { i =>
      updatet.update(i, updatet(i) - 1)
      if (updatet(i) <= 0) {
        if (blocks(updatel(i))(updatey(i))(updatex(i)) === ButtonLeftOnBlockType) {
          blockTemp = blocks(updatel(i))(updatey(i))(updatex(i))
          removeBlockPower(updatex(i), updatey(i), updatel(i))
          blocks(updatel(i))(updatey(i))(updatex(i)) = ButtonLeftBlockType
          rdrawn(updatey(i))(updatex(i)) = false
        } else if (blocks(updatel(i))(updatey(i))(updatex(i)) === ButtonRightOnBlockType) {
          blockTemp = blocks(updatel(i))(updatey(i))(updatex(i))
          removeBlockPower(updatex(i), updatey(i), updatel(i))
          blocks(updatel(i))(updatey(i))(updatex(i)) = ButtonRightBlockType
          rdrawn(updatey(i))(updatex(i)) = false
        } else if (blocks(updatel(i))(updatey(i))(updatex(i)) === WoodenPressurePlateOnBlockType) {
          blockTemp = blocks(updatel(i))(updatey(i))(updatex(i))
          removeBlockPower(updatex(i), updatey(i), updatel(i))
          blocks(updatel(i))(updatey(i))(updatex(i)) = WoodenPressurePlateBlockType
          rdrawn(updatey(i))(updatex(i)) = false
        } else if (blocks(updatel(i))(updatey(i))(updatex(i)) === StonePressurePlateOnBlockType) {
          blockTemp = blocks(updatel(i))(updatey(i))(updatex(i))
          removeBlockPower(updatex(i), updatey(i), updatel(i))
          blocks(updatel(i))(updatey(i))(updatex(i)) = StonePressurePlateBlockType
          rdrawn(updatey(i))(updatex(i)) = false
        } else if (blocks(updatel(i))(updatey(i))(updatex(i)) === ZythiumPressurePlateOnBlockType) {
          blockTemp = blocks(updatel(i))(updatey(i))(updatex(i))
          removeBlockPower(updatex(i), updatey(i), updatel(i))
          blocks(updatel(i))(updatey(i))(updatex(i)) = ZythiumPressurePlateBlockType
          rdrawn(updatey(i))(updatex(i)) = false
        } else if (blocks(updatel(i))(updatey(i))(updatex(i)).id >= ZythiumDelayer1DelayRightOnBlockType.id && blocks(
                     updatel(i))(updatey(i))(updatex(i)).id <= ZythiumDelayer1DelayUpOnBlockType.id || blocks(
                     updatel(i))(updatey(i))(updatex(i)).id >= ZythiumDelayer2DelayRightOnBlockType.id && blocks(
                     updatel(i))(updatey(i))(updatex(i)).id <= ZythiumDelayer2DelayUpOnBlockType.id ||
                   blocks(updatel(i))(updatey(i))(updatex(i)).id >= ZythiumDelayer4DelayRightOnBlockType.id && blocks(
                     updatel(i))(updatey(i))(updatex(i)).id <= ZythiumDelayer4DelayUpOnBlockType.id || blocks(
                     updatel(i))(updatey(i))(updatex(i)).id >= ZythiumDelayer8DelayRightOnBlockType.id && blocks(
                     updatel(i))(updatey(i))(updatex(i)).id <= ZythiumDelayer8DelayUpOnBlockType.id) {
          println("(DEBUG2R)")
          blockTemp = blocks(updatel(i))(updatey(i))(updatex(i))
          removeBlockPower(updatex(i), updatey(i), updatel(i), false)
          blocks(updatel(i))(updatey(i))(updatex(i)) =
            BlockType.lookupById(blocks(updatel(i))(updatey(i))(updatex(i)).id - 4)
          rdrawn(updatey(i))(updatex(i)) = false
        } else if (blocks(updatel(i))(updatey(i))(updatex(i)).id >= ZythiumDelayer1DelayRightBlockType.id && blocks(
                     updatel(i))(updatey(i))(updatex(i)).id <= ZythiumDelayer1DelayUpBlockType.id || blocks(updatel(i))(
                     updatey(i))(updatex(i)).id >= ZythiumDelayer2DelayRightBlockType.id && blocks(updatel(i))(
                     updatey(i))(updatex(i)).id <= ZythiumDelayer2DelayUpBlockType.id ||
                   blocks(updatel(i))(updatey(i))(updatex(i)).id >= ZythiumDelayer4DelayRightBlockType.id && blocks(
                     updatel(i))(updatey(i))(updatex(i)).id <= ZythiumDelayer4DelayUpBlockType.id || blocks(updatel(i))(
                     updatey(i))(updatex(i)).id >= ZythiumDelayer8DelayRightBlockType.id && blocks(updatel(i))(
                     updatey(i))(updatex(i)).id <= ZythiumDelayer8DelayUpBlockType.id) {
          println("(DEBUG2A)")
          blocks(updatel(i))(updatey(i))(updatex(i)) =
            BlockType.lookupById(blocks(updatel(i))(updatey(i))(updatex(i)).id + 4)
          power(updatel(i))(updatey(i))(updatex(i)) = 5.toFloat
          addBlockLighting(updatex(i), updatey(i))
          addTileToPQueue(updatex(i), updatey(i))
          rdrawn(updatey(i))(updatex(i)) = false
        }
        updatex.remove(i)
        updatey.remove(i)
        updatet.remove(i)
        updatel.remove(i)
      }
    }

    if (!DEBUG_PEACEFUL && mobCount < 100) {
      if (msi === 1) {
        ((player.iy / BLOCKSIZE) - 125 until (player.iy / BLOCKSIZE) + 125).foreach { ay =>
          ((player.ix / BLOCKSIZE) - 125 until (player.ix / BLOCKSIZE) + 125).foreach { ax =>
            import scala.util.control.Breaks._
            breakable {
              if (random.nextInt(100000 / DEBUG_HOSTILE) === 0) {
                xpos = ax + random.nextInt(20) - 10
                ypos = ay + random.nextInt(20) - 10
                xpos2 = ax + random.nextInt(20) - 10
                ypos2 = ay + random.nextInt(20) - 10
                if (xpos > 0 && xpos < WIDTH - 1 && ypos > 0 && ypos < HEIGHT - 1 && (blocks(1)(ypos)(xpos) === AirBlockType || !blockcds(
                      blocks(1)(ypos)(xpos).id) &&
                    xpos2 > 0 && xpos2 < WIDTH - 1 && ypos2 > 0 && ypos2 < HEIGHT - 1 && blocks(1)(ypos2)(xpos2) =/= AirBlockType && blockcds(
                      blocks(1)(ypos2)(xpos2).id))) {
                  mobSpawn = None
                  if (checkBiome(xpos, ypos, u, v, blocks, blockbgs) =/= CavernBiome) {
                    if ((day =/= 0 || DEBUG_HOSTILE > 1) && (timeOfDay >= 75913 || timeOfDay < 28883)) {
                      if (random.nextInt(350) === 0) {
                        rnum = random.nextInt(100)
                        if (rnum >= 0 && rnum < 45) {
                          mobSpawn = Some(BlueBubble) // 45%
                        }
                        if (rnum >= 45 && rnum < 85) {
                          mobSpawn = Some(GreenBubble) // 40%
                        }
                        if (rnum >= 85 && rnum < 100) {
                          mobSpawn = Some(RedBubble) // 15%
                        }
                      }
                    }
                    if (timeOfDay >= 32302 && timeOfDay < 72093) {
                      if (random.nextInt(200) === 0) {
                        rnum = random.nextInt(100)
                        if (rnum >= 0 && rnum < 80) {
                          mobSpawn = Some(Zombie) // 80%
                        }
                        if (rnum >= 80 && rnum < 90) {
                          mobSpawn = Some(ArmoredZombie) // 10%
                        }
                        if (rnum >= 90 && rnum < 100) {
                          mobSpawn = Some(ShootingStar) // 10%
                        }
                      }
                    }
                  } else {
                    if (random.nextInt(100) === 0) {
                      rnum = random.nextInt(100)
                      if (rnum >= 0 && rnum < 25) {
                        mobSpawn = Some(YellowBubble) // 25%
                      }
                      if (rnum >= 25 && rnum < 45) {
                        mobSpawn = Some(Zombie) // 20%
                      }
                      if (rnum >= 45 && rnum < 60) {
                        mobSpawn = Some(ArmoredZombie) // 15%
                      }
                      if (rnum >= 60 && rnum < 70) {
                        mobSpawn = Some(BlueBubble) // 10%
                      }
                      if (rnum >= 70 && rnum < 85) {
                        mobSpawn = Some(Bat) // 15%
                      }
                      if (rnum >= 85 && rnum < 100) {
                        mobSpawn = Some(Skeleton) // 15%
                      }
                    }
                  }
                  if (mobSpawn.isDefined && checkBiome(xpos, ypos, u, v, blocks, blockbgs) === DesertBiome) {
                    if (random.nextInt(3) === 0) { // 33% of all spawns in desert
                      mobSpawn = Some(Sandbot)
                    }
                  }
                  if (mobSpawn.isDefined && checkBiome(xpos, ypos, u, v, blocks, blockbgs) === FrostBiome) {
                    if (random.nextInt(3) === 0) { // 33% of all spawns in frost
                      mobSpawn = Some(Snowman)
                    }
                  }
                  /*                                if (mobSpawn =/= null && player.y <= HEIGHT*0.08*BLOCKSIZE) {
                                    mobSpawn = "white_bubble" // 100% of all spawns in sky
                                     }
                   */
                  if (mobSpawn.isEmpty) {
                    break // was continue, is same
                  } else if (DEBUG_MOBTEST.isDefined)
                    mobSpawn = DEBUG_MOBTEST
                  // TODO do pattern matching
                  if (mobSpawn.contains(BlueBubble) ||
                      mobSpawn.contains(GreenBubble) ||
                      mobSpawn.contains(RedBubble) ||
                      mobSpawn.contains(YellowBubble) ||
                      mobSpawn.contains(BlackBubble) ||
                      mobSpawn.contains(WhiteBubble)) {
                    xmax = 2
                    ymax = 2
                  } else if (mobSpawn.contains(Zombie)) {
                    xmax = 2
                    ymax = 3
                  } else if (mobSpawn.contains(ArmoredZombie)) {
                    xmax = 2
                    ymax = 3
                  } else if (mobSpawn.contains(ShootingStar)) {
                    xmax = 2
                    ymax = 2
                  } else if (mobSpawn.contains(Sandbot)) {
                    xmax = 2
                    ymax = 2
                  } else if (mobSpawn.contains(Snowman)) {
                    xmax = 2
                    ymax = 3
                  } else if (mobSpawn.contains(Bat)) {
                    xmax = 1
                    ymax = 1
                  } else if (mobSpawn.contains(Bee)) {
                    xmax = 1
                    ymax = 1
                  } else if (mobSpawn.contains(Skeleton)) {
                    xmax = 1
                    ymax = 3
                  }
                  doMobSpawn = true
                  ((xpos / BLOCKSIZE) until (xpos / BLOCKSIZE + xmax)).foreach { x =>
                    ((ypos / BLOCKSIZE) until (ypos / BLOCKSIZE + ymax)).foreach { y =>
                      if (y > 0 && y < HEIGHT - 1 && blocks(1)(y)(x) =/= AirBlockType && blockcds(blocks(1)(y)(x).id)) {
                        doMobSpawn = false
                      }
                    }
                  }
                  if (doMobSpawn) {
                    mobSpawn.foreach { mob =>
                      entities += new AIEntity((xpos * BLOCKSIZE).toDouble, (ypos * BLOCKSIZE).toDouble, 0, 0, mob)
                    }
                    ()
                  }
                }
              }
            }
          }
        }
        msi = 0
      } else {
        msi = 1
      }
    }

    mobCount = 0

    entities.toList.zipWithIndex.reverse
      .collect {
        case (e: AIEntity, index) => (e, index)
      }
      .foreach { p =>
        val entityTemp = p._1
        val indexTemp  = p._2

        mobCount += 1
        if (entityTemp.ix < player.ix - 2000 || entityTemp.ix > player.ix + 2000 ||
            entityTemp.iy < player.iy - 2000 || entityTemp.iy > player.iy + 2000) {
          if (random.nextInt(500) === 0) {
            entities.remove(indexTemp)
          }
        }
      }

    if (userInput.isLeftMousePressed) {
      checkBlocks = true
      if (showInv) {
        if (mouseX >= getWidth - save_exit.getWidth() - 24 && mouseX <= getWidth - 24 &&
            mouseY >= getHeight - save_exit.getHeight() - 24 && mouseY <= getHeight - 24) {
          if (mouseClicked) {
            mouseNoLongerClicked = true
            saveWorld()
            state = TitleScreen
            timer.stop()
            menuTimer.start()
            return
          }
        }
        (0 until 10).foreach { ux =>
          (0 until 4).foreach { uy =>
            if (mouseX >= ux * 46 + 6 && mouseX < ux * 46 + 46 &&
                mouseY >= uy * 46 + 6 && mouseY < uy * 46 + 46) {
              checkBlocks = false
              if (mouseClicked) {
                mouseNoLongerClicked = true
                if (uy =/= 0 || inventory.selection =/= ux || !showTool) {
                  moveItemTemp = inventory.ids(uy * 10 + ux)
                  moveNumTemp = inventory.nums(uy * 10 + ux)
                  moveDurTemp = inventory.durs(uy * 10 + ux)
                  if (moveItem === inventory.ids(uy * 10 + ux)) {
                    moveNum = inventory.addLocation(uy * 10 + ux, moveItem, moveNum)
                    if (moveNum === 0) {
                      moveItem = EmptyUiItem
                      moveDur = 0
                    }
                  } else {
                    inventory.removeLocation(uy * 10 + ux, inventory.nums(uy * 10 + ux))
                    if (moveItem =/= EmptyUiItem) {
                      inventory.addLocation(uy * 10 + ux, moveItem, moveNum)
                    }
                    moveItem = moveItemTemp
                    moveNum = moveNumTemp
                    moveDur = moveDurTemp
                  }
                }
              }
            }
          }
        }
        (0 until 2).foreach { ux =>
          (0 until 2).foreach { uy =>
            if (mouseX >= inventory.image.getWidth() + ux * 40 + 75 &&
                mouseX < inventory.image.getWidth() + ux * 40 + 115 &&
                mouseY >= uy * 40 + 52 && mouseY < uy * 40 + 92) {
              checkBlocks = false
              if (mouseClicked) {
                mouseNoLongerClicked = true
                cic.foreach { c =>
                  moveItemTemp = c.ids(uy * 2 + ux)
                  moveNumTemp = c.nums(uy * 2 + ux)
                  moveDurTemp = c.durs(uy * 2 + ux)

                  if (moveItem === c.ids(uy * 2 + ux)) {
                    moveNum = inventory.addLocationIC(c, uy * 2 + ux, moveItem, moveNum)
                    if (moveNum === 0) {
                      moveItem = EmptyUiItem
                      moveDur = 0
                    }
                  } else {
                    inventory.removeLocationIC(c, uy * 2 + ux, c.nums(uy * 2 + ux))

                    if (moveItem =/= EmptyUiItem) {
                      inventory.addLocationIC(c, uy * 2 + ux, moveItem, moveNum)
                    }

                    moveItem = moveItemTemp
                    moveNum = moveNumTemp
                    moveDur = moveDurTemp
                  }
                }
              }
            }
          }
        }
        if (mouseX >= inventory.image.getWidth() + 3 * 40 + 81 &&
            mouseX < inventory.image.getWidth() + 3 * 40 + 121 &&
            mouseY >= 20 + 52 && mouseY < 20 + 92) {
          checkBlocks = false
          if (mouseClicked) {
            cic.foreach { c =>
              if (moveItem === c.ids(4)) {
                val maxstacks = c.ids(4).maxStacks
                if (moveNum + c.nums(4) <= maxstacks) {
                  moveNum = (moveNum + c.nums(4)).toShort
                  inventory.useRecipeCIC(c)
                }
              }
              if (moveItem === EmptyUiItem) {
                moveItem = c.ids(4)
                moveNum = c.nums(4)
                moveDur = UiItem.toolDurability(c.ids(4))
                inventory.useRecipeCIC(c)
              }
            }
          }
        }
        ic.foreach { icTemp =>
          if (icTemp.icType === Workbench) {
            (0 until 3).foreach { ux =>
              (0 until 3).foreach { uy =>
                if (mouseX >= ux * 40 + 6 && mouseX < ux * 40 + 46 &&
                    mouseY >= uy * 40 + inventory.image.getHeight() + 46 &&
                    mouseY < uy * 40 + inventory.image.getHeight() + 86) {
                  checkBlocks = false
                  if (mouseClicked) {
                    mouseNoLongerClicked = true
                    moveItemTemp = icTemp.ids(uy * 3 + ux)
                    moveNumTemp = icTemp.nums(uy * 3 + ux)
                    if (moveItem === icTemp.ids(uy * 3 + ux)) {
                      moveNum = inventory.addLocationIC(icTemp, uy * 3 + ux, moveItem, moveNum)
                      if (moveNum === 0) {
                        moveItem = EmptyUiItem
                      }
                    } else {
                      inventory.removeLocationIC(icTemp, uy * 3 + ux, icTemp.nums(uy * 3 + ux))
                      if (moveItem =/= EmptyUiItem) {
                        inventory.addLocationIC(icTemp, uy * 3 + ux, moveItem, moveNum)
                      }
                      moveItem = moveItemTemp
                      moveNum = moveNumTemp
                    }
                  }
                }
              }
            }
            if (mouseX >= 4 * 40 + 6 && mouseX < 4 * 40 + 46 &&
                mouseY >= 1 * 40 + inventory.image.getHeight() + 46 &&
                mouseY < 1 * 40 + inventory.image.getHeight() + 86) {
              checkBlocks = false
              if (mouseClicked) {
                val item9 = icTemp.ids(9)
                if (moveItem === item9 && moveNum + icTemp.nums(9) <= item9.maxStacks) {
                  moveNum = (moveNum + icTemp.nums(9)).toShort
                  inventory.useRecipeWorkbench(icTemp)
                }

                if (moveItem === EmptyUiItem) {
                  moveItem = icTemp.ids(9)
                  moveNum = icTemp.nums(9)
                  moveDur = UiItem.toolDurability(item9)
                  inventory.useRecipeWorkbench(icTemp)
                }
              }
            }
          }
          if (icTemp.icType === WoodenChest || icTemp.icType === StoneChest ||
              icTemp.icType === CopperChest || icTemp.icType === IronChest ||
              icTemp.icType === SilverChest || icTemp.icType === GoldChest ||
              icTemp.icType === ZincChest || icTemp.icType === RhymestoneChest ||
              icTemp.icType === ObduriteChest) { //TODO: chest trait?
            (0 until inventory.CX).foreach { ux =>
              (0 until inventory.CY).foreach { uy =>
                if (mouseX >= ux * 46 + 6 && mouseX < ux * 46 + 46 &&
                    mouseY >= uy * 46 + inventory.image.getHeight() + 46 &&
                    mouseY < uy * 46 + inventory.image.getHeight() + 86) {
                  checkBlocks = false
                  if (mouseClicked) {
                    mouseNoLongerClicked = true
                    moveItemTemp = icTemp.ids(uy * inventory.CX + ux)
                    moveNumTemp = icTemp.nums(uy * inventory.CX + ux)
                    if (moveItem === icTemp.ids(uy * inventory.CX + ux)) {
                      moveNum = inventory.addLocationIC(icTemp, uy * inventory.CX + ux, moveItem, moveNum)
                      if (moveNum === 0) {
                        moveItem = EmptyUiItem
                      }
                    } else {
                      inventory.removeLocationIC(icTemp, uy * inventory.CX + ux, icTemp.nums(uy * inventory.CX + ux))
                      if (moveItem =/= EmptyUiItem) {
                        inventory.addLocationIC(icTemp, uy * inventory.CX + ux, moveItem, moveNum)
                      }
                      moveItem = moveItemTemp
                      moveNum = moveNumTemp
                    }
                  }
                }
              }
            }
          }
          if (icTemp.icType === Furnace) {
            if (mouseX >= 6 && mouseX < 46 &&
                mouseY >= inventory.image.getHeight() + 46 &&
                mouseY < inventory.image.getHeight() + 86) {
              checkBlocks = false
              if (mouseClicked) {
                mouseNoLongerClicked = true
                moveItemTemp = icTemp.ids(0)
                moveNumTemp = icTemp.nums(0)
                if (moveItem === icTemp.ids(0)) {
                  moveNum = inventory.addLocationIC(icTemp, 0, moveItem, moveNum)
                  if (moveNum === 0) {
                    moveItem = EmptyUiItem
                  }
                } else {
                  inventory.removeLocationIC(icTemp, 0, icTemp.nums(0))
                  if (moveItem =/= EmptyUiItem) {
                    inventory.addLocationIC(icTemp, 0, moveItem, moveNum)
                  }
                  moveItem = moveItemTemp
                  moveNum = moveNumTemp
                  icTemp.SMELTP = 0
                }
              }
            }
            if (mouseX >= 6 && mouseX < 46 &&
                mouseY >= inventory.image.getHeight() + 142 &&
                mouseY < inventory.image.getHeight() + 182) {
              checkBlocks = false
              if (mouseClicked) {
                mouseNoLongerClicked = true
                moveItemTemp = icTemp.ids(2)
                moveNumTemp = icTemp.nums(2)
                if (moveItem === icTemp.ids(2)) {
                  moveNum = inventory.addLocationIC(icTemp, 2, moveItem, moveNum)
                  if (moveNum === 0) {
                    moveItem = EmptyUiItem
                  }
                } else {
                  inventory.removeLocationIC(icTemp, 2, icTemp.nums(2))
                  if (moveItem =/= EmptyUiItem) {
                    inventory.addLocationIC(icTemp, 2, moveItem, moveNum)
                  }
                  moveItem = moveItemTemp
                  moveNum = moveNumTemp
                }
              }
            }
            if (mouseX >= 62 && mouseX < 102 &&
                mouseY >= inventory.image.getHeight() + 46 &&
                mouseY < inventory.image.getHeight() + 86) {
              checkBlocks = false
              if (mouseClicked) {
                mouseNoLongerClicked = true
                if (moveItem === EmptyUiItem) {
                  moveItem = icTemp.ids(3)
                  moveNum = icTemp.nums(3)
                  inventory.removeLocationIC(icTemp, 3, icTemp.nums(3))
                } else if (moveItem === icTemp.ids(3)) {
                  moveNum = (moveNum + icTemp.nums(3)).toShort
                  inventory.removeLocationIC(icTemp, 3, icTemp.nums(3))
                  val maxstacks = moveItem.maxStacks
                  if (moveNum > maxstacks) {
                    inventory.addLocationIC(icTemp, 3, moveItem, (moveNum - maxstacks).toShort)
                    moveNum = maxstacks
                  }
                }
              }
            }
          }
        }
        (0 until 4).foreach { uy =>
          if (mouseX >= inventory.image.getWidth() + 6 && mouseX < inventory.image.getWidth() + 6 + armor.icType.image
                .getWidth() &&
              mouseY >= 6 + uy * 46 && mouseY < 6 + uy * 46 + 40) {
            checkBlocks = false
            if (mouseClicked) {
              mouseNoLongerClicked = true
              i = uy
              if (uy === 0 && (moveItem === CopperHelmetUiItem || moveItem === IronHelmetUiItem || moveItem === SilverHelmetUiItem || moveItem === GoldHelmetUiItem ||
                  moveItem === ZincHelmetUiItem || moveItem === RhymestoneHelmetUiItem || moveItem === ObduriteHelmetUiItem || moveItem === AluminumHelmetUiItem ||
                  moveItem === LeadHelmetUiItem || moveItem === ZythiumHelmetUiItem) ||
                  uy === 1 && (moveItem === CopperChestplateUiItem || moveItem === IronChestplateUiItem || moveItem === SilverChestplateUiItem || moveItem === GoldChestplateUiItem ||
                  moveItem === ZincChestplateUiItem || moveItem === RhymestoneChestplateUiItem || moveItem === ObduriteChestplateUiItem || moveItem === AluminumChestplateUiItem ||
                  moveItem === LeadChestplateUiItem || moveItem === ZythiumChestplateUiItem) ||
                  uy === 2 && (moveItem === CopperLeggingsUiItem || moveItem === IronLeggingsUiItem || moveItem === SilverLeggingsUiItem || moveItem === GoldLeggingsUiItem ||
                  moveItem === ZincLeggingsUiItem || moveItem === RhymestoneLeggingsUiItem || moveItem === ObduriteLeggingsUiItem || moveItem === AluminumLeggingsUiItem ||
                  moveItem === LeadLeggingsUiItem || moveItem === ZythiumLeggingsUiItem) ||
                  uy === 3 && (moveItem === CopperGreavesUiItem || moveItem === IronGreavesUiItem || moveItem === SilverGreavesUiItem || moveItem === GoldGreavesUiItem ||
                  moveItem === ZincGreavesUiItem || moveItem === RhymestoneGreavesUiItem || moveItem === ObduriteGreavesUiItem || moveItem === AluminumGreavesUiItem ||
                  moveItem === LeadGreavesUiItem || moveItem === ZythiumGreavesUiItem)) { //TODO: need some help functions for this
                if (armor.ids(i) === EmptyUiItem) {
                  inventory.addLocationIC(armor, i, moveItem, moveNum)
                  moveItem = EmptyUiItem
                  moveNum = 0
                } else {
                  moveItemTemp = armor.ids(i)
                  moveNumTemp = armor.nums(i)
                  inventory.removeLocationIC(armor, i, moveNumTemp)
                  inventory.addLocationIC(armor, i, moveItem, moveNum)
                  moveItem = moveItemTemp
                  moveNum = moveNumTemp
                }
              } else if (moveItem === EmptyUiItem) {
                moveItem = armor.ids(i)
                moveNum = armor.nums(i)
                inventory.removeLocationIC(armor, i, moveNum)
              }
            }
          }
        }
      } else {
        (0 until 10).foreach { ux =>
          uy = 0
          if (mouseX >= ux * 46 + 6 && mouseX < ux * 46 + 46 &&
              mouseY >= uy * 46 + 6 && mouseY < uy * 46 + 46) {
            checkBlocks = false
            if (mouseClicked) {
              mouseNoLongerClicked = true
              inventory.select2(ux)
            }
          }
        }
      }
      if (mouseNoLongerClicked) {
        mouseClicked = false
      }
      if (checkBlocks) {
        if (inventory.tool() =/= EmptyUiItem && !showTool) {
          tool = UiItem.image(inventory.tool())
          entities.foreach { entity: Entity =>
            entity.immune = false
          }
          toolSpeed = UiItem.speed(inventory.tool())
          if (inventory.tool() === MagnetitePickUiItem || inventory.tool() === MagnetiteAxeUiItem || inventory
                .tool() === MagnetiteSwordUiItem) {
            val tooldurs = UiItem.toolDurability(inventory.tool())
            toolSpeed *= (inventory.durs(inventory.selection).toDouble / tooldurs) * (-0.714) + 1
          }
          showTool = true
          toolAngle = 4.7
          ux = playerMouseXOffset / BLOCKSIZE
          uy = playerMouseYOffset / BLOCKSIZE
          ux2 = playerMouseXOffset / BLOCKSIZE
          uy2 = playerMouseYOffset / BLOCKSIZE
          if (sqrt(pow(player.x + player.image.getWidth() - ux2 * BLOCKSIZE + BLOCKSIZE / 2, 2) + pow(
                player.y + player.image.getHeight() - uy2 * BLOCKSIZE + BLOCKSIZE / 2,
                2)) <= 160 ||
              sqrt(
                pow(player.x + player.image.getWidth() - ux2 * BLOCKSIZE + BLOCKSIZE / 2 + WIDTH * BLOCKSIZE, 2) + pow(
                  player.y + player.image.getHeight() - uy2 * BLOCKSIZE + BLOCKSIZE / 2,
                  2)) <= 160 || DEBUG_REACH) {
            ucx = ux - CHUNKBLOCKS * (ux / CHUNKBLOCKS)
            ucy = uy - CHUNKBLOCKS * (uy / CHUNKBLOCKS)
            if (UiItem.isTool(inventory.tool())) {
              if (layer < blocks.length && uy < blocks(layer).length && ux < blocks(layer)(uy).length && blocks(layer)(
                    uy)(ux) =/= AirBlockType && BlockType.blockContainsTool(blocks(layer)(uy)(ux), inventory.tool())) {
                blockdns(uy)(ux) = random.nextInt(5).toByte
                drawn(uy)(ux) = false
                if (ux === mx && uy === my && inventory.tool() === miningTool) {
                  mining += 1
                  inventory.durs(inventory.selection) = (inventory.durs(inventory.selection) - 1).toShort
                  breakCurrentBlock()
                  if (inventory.durs(inventory.selection) <= 0) {
                    inventory.removeLocation(inventory.selection, inventory.nums(inventory.selection))
                  }
                } else {
                  mining = 1
                  miningTool = inventory.tool()
                  inventory.durs(inventory.selection) = (inventory.durs(inventory.selection) - 1).toShort
                  breakCurrentBlock()
                  mx = ux
                  my = uy
                  if (inventory.durs(inventory.selection) <= 0) {
                    inventory.removeLocation(inventory.selection, inventory.nums(inventory.selection))
                  }
                }
              }
            } else if (inventory.tool() === StoneLighterUiItem) {
              if (blocks(layer)(uy)(ux) === FurnaceBlockType || blocks(layer)(uy)(ux) === FurnaceOnBlockType) {
                icmatrix(layer)(uy)(ux).fold {
                  ic.foreach { icTemp =>
                    if (icTemp.icType === Furnace) {
                      inventory.durs(inventory.selection) = (inventory.durs(inventory.selection) - 1).toShort
                      icTemp.F_ON = true
                      blocks(layer)(icy)(icx) = FurnaceOnBlockType
                      addBlockLighting(ux, uy)
                      rdrawn(icy)(icx) = false
                      if (inventory.durs(inventory.selection) <= 0) {
                        inventory.removeLocation(inventory.selection, inventory.nums(inventory.selection))
                      }
                    }
                  }
                } { icMatrixTemp =>
                  if (icMatrixTemp.icType === Furnace) {
                    inventory.durs(inventory.selection) = (inventory.durs(inventory.selection) - 1).toShort
                    icMatrixTemp.F_ON = true
                    blocks(layer)(uy)(ux) = FurnaceOnBlockType
                    addBlockLighting(ux, uy)
                    if (inventory.durs(inventory.selection) <= 0) {
                      inventory.removeLocation(inventory.selection, inventory.nums(inventory.selection))
                    }
                    rdrawn(uy)(ux) = false
                  }
                }
              }
            } else if (inventory.tool() === WrenchUiItem) {
              if (blocks(layer)(uy)(ux).id >= ZythiumDelayer1DelayRightBlockType.id && blocks(layer)(uy)(ux).id <= ZythiumDelayer4DelayUpOnBlockType.id) { // TODO need a better logic method than comparing ids
                inventory.durs(inventory.selection) = (inventory.durs(inventory.selection) - 1).toShort
                blocks(layer)(uy)(ux) = BlockType.lookupById(blocks(layer)(uy)(ux).id + 8)
                rdrawn(uy)(ux) = false
                if (inventory.durs(inventory.selection) <= 0) {
                  inventory.removeLocation(inventory.selection, inventory.nums(inventory.selection))
                }
              } else if (blocks(layer)(uy)(ux).id >= ZythiumDelayer8DelayRightBlockType.id && blocks(layer)(uy)(ux).id <= ZythiumDelayer8DelayUpOnBlockType.id) {
                inventory.durs(inventory.selection) = (inventory.durs(inventory.selection) - 1).toShort
                blocks(layer)(uy)(ux) = BlockType.lookupById(blocks(layer)(uy)(ux).id - 24)
                rdrawn(uy)(ux) = false
                if (inventory.durs(inventory.selection) <= 0) {
                  inventory.removeLocation(inventory.selection, inventory.nums(inventory.selection))
                }
              }
            } else if (inventory.tool() =/= EmptyUiItem) {
              blockTemp = UiItem.blockForTool(inventory.tool())
              if (layer < blocks.length &&
                  uy < blocks(layer).length &&
                  ux < blocks(layer)(uy).length &&
                  uy >= 1 && (blocks(layer)(uy)(ux) === AirBlockType) &&
                  (layer === 0 && (blocks(layer)(uy)(ux - 1) =/= AirBlockType || blocks(layer)(uy)(ux + 1) =/= AirBlockType ||
                  blocks(layer)(uy - 1)(ux) =/= AirBlockType || blocks(layer)(uy + 1)(ux) =/= AirBlockType ||
                  blocks(layer + 1)(uy)(ux) =/= AirBlockType) ||
                  layer === 1 && (blocks(layer)(uy)(ux - 1) =/= AirBlockType || blocks(layer)(uy)(ux + 1) =/= AirBlockType ||
                  blocks(layer)(uy - 1)(ux) =/= AirBlockType || blocks(layer)(uy + 1)(ux) =/= AirBlockType ||
                  blocks(layer - 1)(uy)(ux) =/= AirBlockType || blocks(layer + 1)(uy)(ux) =/= AirBlockType) ||
                  layer === 2 && (blocks(layer)(uy)(ux - 1) =/= AirBlockType || blocks(layer)(uy)(ux + 1) =/= AirBlockType ||
                  blocks(layer)(uy - 1)(ux) =/= AirBlockType || blocks(layer)(uy + 1)(ux) =/= AirBlockType ||
                  blocks(layer - 1)(uy)(ux) =/= AirBlockType)) &&
                  !(blockTemp === SunflowerStage1BlockType && (blocks(layer)(uy + 1)(ux) =/= DirtBlockType && blocks(
                    layer)(uy + 1)(ux) =/= GrassBlockType && blocks(layer)(uy + 1)(ux) =/= JungleGrassBlockType) || // sunflower
                    blockTemp === MoonflowerStage1BlockType && (blocks(layer)(uy + 1)(ux) =/= DirtBlockType && blocks(
                      layer)(uy + 1)(ux) =/= GrassBlockType && blocks(layer)(uy + 1)(ux) =/= JungleGrassBlockType) || // moonflower
                    blockTemp === DryweedStage1BlockType && (blocks(layer)(uy + 1)(ux) =/= SandBlockType) ||          // dryweed
                    blockTemp === GreenleafStage1BlockType && (blocks(layer)(uy + 1)(ux) =/= JungleGrassBlockType) || // greenleaf
                    blockTemp === FrostleafStage1BlockType && (blocks(layer)(uy + 1)(ux) =/= SnowBlockType) ||        // frostleaf
                    blockTemp === CaverootStage1BlockType && (blocks(layer)(uy + 1)(ux) =/= StoneBlockType) ||        // caveroot
                    blockTemp === SkyblossomStage1BlockType && (blocks(layer)(uy + 1)(ux) =/= DirtBlockType && blocks(
                      layer)(uy + 1)(ux) =/= GrassBlockType && blocks(layer)(uy + 1)(ux) =/= JungleGrassBlockType) || // skyblossom
                    blockTemp === VoidRotStage1BlockType && (blocks(layer)(uy + 1)(ux) =/= StoneBlockType))) { // void_rot
                if (TORCHESL
                      .get(blockTemp)
                      .isEmpty || uy < HEIGHT - 1 && (solid(blocks(layer)(uy + 1)(ux).id) && blockTemp =/= ButtonLeftBlockType || solid(
                      blocks(layer)(uy)(ux + 1).id) || solid(blocks(layer)(uy)(ux - 1).id))) {
                  if (TORCHESL.get(blockTemp).isDefined) {
                    if (solid(blocks(layer)(uy + 1)(ux).id) && blockTemp =/= ButtonLeftBlockType) {
                      blockTemp = blockTemp
                    } else if (solid(blocks(layer)(uy)(ux - 1).id)) { //TODO: encode solid into BlockType
                      TORCHESL.get(blockTemp).foreach { t => //TODO encode torchesl into BlockType
                        blockTemp = t
                      }
                    } else if (solid(blocks(layer)(uy)(ux + 1).id)) {
                      TORCHESR.get(blockTemp).foreach { t =>
                        blockTemp = t
                      }
                    }
                  }
                  if (layer === 1 && !DEBUG_GPLACE && blockcds(blockTemp.id)) { //TODO: encode blockcs into BlockType
                    entities
                      .collect {
                        case e: AIEntity => e
                      }
                      .foreach { entity: AIEntity =>
                        if (entity.rect.intersects(new Rectangle(ux * BLOCKSIZE, uy * BLOCKSIZE, BLOCKSIZE, BLOCKSIZE))) {
                          blockTemp = AirBlockType
                        }
                      }
                    if (player.playerRect.intersects(
                          new Rectangle(ux * BLOCKSIZE, uy * BLOCKSIZE, BLOCKSIZE, BLOCKSIZE))) {
                      blockTemp = AirBlockType
                    }
                  }
                  if (blockTemp =/= AirBlockType) {
                    blocks(layer)(uy)(ux) = blockTemp
                    if (receives(blocks(layer)(uy)(ux).id)) { // TODO: encode receives into BlockType
                      addAdjacentTilesToPQueue(ux, uy)
                    }
                    if (powers(blocks(layer)(uy)(ux))) {
                      addBlockPower(ux, uy)
                    }
                    if (ltrans(blocks(layer)(uy)(ux).id)) { //TODO: encode ltrans into BlockType
                      removeSunLighting(ux, uy)
                      redoBlockLighting(ux, uy)
                    }
                    addBlockLighting(ux, uy)
                  }
                  if (blockTemp =/= AirBlockType) {
                    inventory.removeLocation(inventory.selection, 1.toShort)
                    blockds(layer) = World.generate2b(blocks(layer), blockds(layer), ux, uy)
                    (uy - 1 until uy + 2).foreach { uly =>
                      (ux - 1 until ux + 2).foreach { ulx =>
                        blockdns(uly)(ulx) = random.nextInt(5).toByte
                      }
                    }
                    (uy - 1 until uy + 2).foreach { uly =>
                      (ux - 1 until ux + 2).foreach { ulx =>
                        drawn(uly)(ulx) = false
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    } else {
      mouseClicked = true
    }
    if (userInput.isRightMousePressed) {
      checkBlocks = true
      if (showInv) {
        (0 until 10).foreach { ux =>
          (0 until 4).foreach { uy =>
            if (mouseX >= ux * 46 + 6 && mouseX < ux * 46 + 46 &&
                mouseY >= uy * 46 + 6 && mouseY < uy * 46 + 46) {
              checkBlocks = false
              if (mouseClicked2) {
                mouseNoLongerClicked2 = true
                moveItemTemp = inventory.ids(uy * 10 + ux)
                moveNumTemp = (inventory.nums(uy * 10 + ux) / 2).toShort
                moveDurTemp = inventory.durs(uy * 10 + ux)
                if (inventory.ids(uy * 10 + ux) === EmptyUiItem) {
                  inventory.addLocation(uy * 10 + ux, moveItem, 1.toShort)
                  moveNum = (moveNum - 1).toShort
                  if (moveNum === 0) {
                    moveItem = EmptyUiItem
                    moveDur = 0
                  }
                } else if (moveItem === EmptyUiItem && inventory.nums(uy * 10 + ux) =/= 1) {
                  inventory.removeLocation(uy * 10 + ux, (inventory.nums(uy * 10 + ux) / 2).toShort)
                  moveItem = moveItemTemp
                  moveNum = moveNumTemp
                  moveDur = moveDurTemp
                } else if (moveItem === inventory.ids(uy * 10 + ux)) {
                  val maxstacks = inventory.ids(uy * 10 + ux).maxStacks
                  if (inventory.nums(uy * 10 + ux) < maxstacks) {
                    inventory.addLocation(uy * 10 + ux, moveItem, 1.toShort)
                    moveNum = (moveNum - 1).toShort
                    if (moveNum === 0) {
                      moveItem = EmptyUiItem
                      moveDur = 0 // TODO do we need moveDur anymore?
                    }
                  }
                }
              }
            }
          }
        }
        (0 until 2).foreach { ux =>
          (0 until 2).foreach { uy =>
            if (mouseX >= inventory.image.getWidth() + ux * 40 + 75 &&
                mouseX < inventory.image.getWidth() + ux * 40 + 121 &&
                mouseY >= uy * 40 + 52 && mouseY < uy * 40 + 92) {
              checkBlocks = false
              if (mouseClicked2) {
                mouseNoLongerClicked2 = true
                cic.foreach { c =>
                  moveItemTemp = c.ids(uy * 2 + ux)
                  moveNumTemp = (c.nums(uy * 2 + ux) / 2).toShort
                  if (c.ids(uy * 2 + ux) === EmptyUiItem) {
                    inventory.addLocationIC(c, uy * 2 + ux, moveItem, 1.toShort)
                    moveNum = (moveNum - 1).toShort
                    if (moveNum === 0) {
                      moveItem = EmptyUiItem
                    }
                  } else if (moveItem === EmptyUiItem && c.nums(uy * 2 + ux) =/= 1) {
                    inventory.removeLocationIC(c, uy * 2 + ux, (c.nums(uy * 2 + ux) / 2).toShort)
                    moveItem = moveItemTemp
                    moveNum = moveNumTemp
                  } else if (moveItem === c.ids(uy * 2 + ux)) {
                    val maxstacks = c.ids(uy * 2 + ux).maxStacks
                    if (c.nums(uy * 2 + ux) < maxstacks) {
                      inventory.addLocationIC(c, uy * 2 + ux, moveItem, 1.toShort)
                      moveNum = (moveNum - 1).toShort
                      if (moveNum === 0) {
                        moveItem = EmptyUiItem
                      }
                    }
                  }
                }
              }
            }
          }
        }
        ic.foreach { icTemp =>
          if (icTemp.icType === Workbench) {
            (0 until 3).foreach { ux =>
              (0 until 3).foreach { uy =>
                if (mouseX >= ux * 40 + 6 && mouseX < ux * 40 + 46 &&
                    mouseY >= uy * 40 + inventory.image.getHeight() + 46 &&
                    mouseY < uy * 40 + inventory.image.getHeight() + 86) {
                  checkBlocks = false
                  if (mouseClicked2) {
                    mouseNoLongerClicked2 = true
                    moveItemTemp = icTemp.ids(uy * 3 + ux)
                    moveNumTemp = (icTemp.nums(uy * 3 + ux) / 2).toShort
                    if (icTemp.ids(uy * 3 + ux) === EmptyUiItem) {
                      inventory.addLocationIC(icTemp, uy * 3 + ux, moveItem, 1.toShort)
                      moveNum = (moveNum - 1).toShort
                      if (moveNum === 0) {
                        moveItem = EmptyUiItem
                      }
                    } else if (moveItem === EmptyUiItem && icTemp.nums(uy * 3 + ux) =/= 1) {
                      inventory.removeLocationIC(icTemp, uy * 3 + ux, (icTemp.nums(uy * 3 + ux) / 2).toShort)
                      moveItem = moveItemTemp
                      moveNum = moveNumTemp
                    } else if (moveItem === icTemp.ids(uy * 3 + ux)) {
                      val maxstacks = icTemp.ids(uy * 3 + ux).maxStacks
                      if (icTemp.nums(uy * 3 + ux) < maxstacks) {
                        if (icTemp.ids(7) === BarkUiItem && icTemp.nums(7) === 51 && moveItem === ClayUiItem && uy * 3 + ux === 3 && icTemp
                              .nums(8) === 0) {
                          inventory.addLocationIC(icTemp, 8, WoodenPickUiItem, 1.toShort)
                        } else {
                          inventory.addLocationIC(icTemp, uy * 3 + ux, moveItem, 1.toShort)
                          moveNum = (moveNum - 1).toShort
                          if (moveNum === 0) {
                            moveItem = EmptyUiItem
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
            if (mouseX >= 4 * 40 + 6 && mouseX < 4 * 40 + 46 &&
                mouseY >= 1 * 40 + inventory.image.getHeight() + 46 &&
                mouseY < 1 * 40 + inventory.image.getHeight() + 86) {
              checkBlocks = false
              if (mouseClicked2) {
                //
              }
            }
          }
          if (icTemp.icType === WoodenChest || icTemp.icType === StoneChest ||
              icTemp.icType === CopperChest || icTemp.icType === IronChest ||
              icTemp.icType === SilverChest || icTemp.icType === GoldChest ||
              icTemp.icType === ZincChest || icTemp.icType === RhymestoneChest ||
              icTemp.icType === ObduriteChest) { //TODO: chest trait?
            (0 until inventory.CX).foreach { ux =>
              (0 until inventory.CY).foreach { uy =>
                if (mouseX >= ux * 46 + 6 && mouseX < ux * 46 + 46 &&
                    mouseY >= uy * 46 + inventory.image.getHeight() + 46 &&
                    mouseY < uy * 46 + inventory.image.getHeight() + 86) {
                  checkBlocks = false
                  if (mouseClicked2) {
                    mouseNoLongerClicked2 = true
                    moveItemTemp = icTemp.ids(uy * inventory.CX + ux)
                    moveNumTemp = (icTemp.nums(uy * inventory.CX + ux) / 2).toShort
                    if (icTemp.ids(uy * inventory.CX + ux) === EmptyUiItem) {
                      inventory.addLocationIC(icTemp, uy * inventory.CX + ux, moveItem, 1.toShort) // TODOShould moveDur go away?
                      moveNum = (moveNum - 1).toShort
                      if (moveNum === 0) {
                        moveItem = EmptyUiItem
                      }
                    } else if (moveItem === EmptyUiItem && icTemp.nums(uy * inventory.CX + ux) =/= 1) {
                      inventory.removeLocationIC(
                        icTemp,
                        uy * inventory.CX + ux,
                        (icTemp.nums(uy * inventory.CX + ux) / 2).toShort)
                      moveItem = moveItemTemp
                      moveNum = moveNumTemp
                    } else if (moveItem === icTemp.ids(uy * inventory.CX + ux)) {
                      val maxstacks = icTemp.ids(uy * inventory.CX + ux).maxStacks
                      if (icTemp.nums(uy * inventory.CX + ux) < maxstacks) {
                        inventory.addLocationIC(icTemp, uy * inventory.CX + ux, moveItem, 1.toShort)
                        moveNum = (moveNum - 1).toShort
                        if (moveNum === 0) {
                          moveItem = EmptyUiItem
                        }
                      }

                    }
                  }
                }
              }
            }
          }
          if (icTemp.icType === Furnace) {
            if (mouseX >= 6 && mouseX < 46 &&
                mouseY >= inventory.image.getHeight() + 46 &&
                mouseY < inventory.image.getHeight() + 86) {
              checkBlocks = false
              if (mouseClicked2) {
                mouseNoLongerClicked2 = true
                moveItemTemp = icTemp.ids(0)
                moveNumTemp = (icTemp.nums(0) / 2).toShort
                if (icTemp.ids(0) === EmptyUiItem) {
                  inventory.addLocationIC(icTemp, 0, moveItem, 1.toShort)
                  moveNum = (moveNum - 1).toShort
                  if (moveNum === 0) {
                    moveItem = EmptyUiItem
                  }
                } else if (moveItem === EmptyUiItem && icTemp.nums(0) =/= 1) {
                  inventory.removeLocationIC(icTemp, 0, (icTemp.nums(0) / 2).toShort)
                  moveItem = moveItemTemp
                  moveNum = moveNumTemp
                } else if (moveItem === icTemp.ids(0)) {
                  val maxstacks = icTemp.ids(0).maxStacks
                  if (icTemp.nums(0) < maxstacks) {
                    inventory.addLocationIC(icTemp, 0, moveItem, 1.toShort)
                    moveNum = (moveNum - 1).toShort
                    if (moveNum === 0) {
                      moveItem = EmptyUiItem
                    }
                  }
                }
              }
            }
            if (mouseX >= 6 && mouseX < 46 &&
                mouseY >= inventory.image.getHeight() + 142 &&
                mouseY < inventory.image.getHeight() + 182) {
              checkBlocks = false
              if (mouseClicked2) {
                mouseNoLongerClicked2 = true
                moveItemTemp = icTemp.ids(2)
                moveNumTemp = (icTemp.nums(2) / 2).toShort
                if (icTemp.ids(2) === EmptyUiItem) {
                  inventory.addLocationIC(icTemp, 2, moveItem, 1.toShort)
                  moveNum = (moveNum - 1).toShort
                  if (moveNum === 0) {
                    moveItem = EmptyUiItem
                  }
                } else if (moveItem === EmptyUiItem && icTemp.nums(2) =/= 1) {
                  inventory.removeLocationIC(icTemp, 2, (icTemp.nums(2) / 2).toShort)
                  moveItem = moveItemTemp
                  moveNum = moveNumTemp
                } else if (moveItem === icTemp.ids(2)) {
                  val maxstacks = icTemp.ids(2).maxStacks
                  if (icTemp.nums(2) < maxstacks) {
                    inventory.addLocationIC(icTemp, 2, moveItem, 1.toShort)
                    moveNum = (moveNum - 1).toShort
                    if (moveNum === 0) {
                      moveItem = EmptyUiItem
                    }
                  }
                }
              }
            }
            if (mouseX >= 62 && mouseX < 102 &&
                mouseY >= inventory.image.getHeight() + 46 &&
                mouseY < inventory.image.getHeight() + 86) {
              checkBlocks = false
              if (mouseClicked2) {
                mouseNoLongerClicked2 = true
                moveItemTemp = icTemp.ids(3)
                moveNumTemp = (icTemp.nums(3) / 2).toShort
                if (moveItem === EmptyUiItem && icTemp.nums(3) =/= 1) {
                  inventory.removeLocationIC(icTemp, 3, (icTemp.nums(3) / 2).toShort)
                  moveItem = moveItemTemp
                  moveNum = moveNumTemp
                }
              }
            }
          }
        }
      }
      if (checkBlocks) {
        if (!(playerMouseXOffset < 0 || playerMouseXOffset >= WIDTH * BLOCKSIZE ||
              playerMouseYOffset < 0 || playerMouseYOffset >= HEIGHT * BLOCKSIZE)) {
          ux = playerMouseXOffset / BLOCKSIZE
          uy = playerMouseYOffset / BLOCKSIZE
          if (DEBUG_REACH || sqrt(pow(player.x + player.image.getWidth() - ux * BLOCKSIZE + BLOCKSIZE / 2, 2) + pow(
                player.y + player.image.getHeight() - uy * BLOCKSIZE + BLOCKSIZE / 2,
                2)) <= 160) {
            ucx = ux - CHUNKBLOCKS * (ux / CHUNKBLOCKS)
            ucy = uy - CHUNKBLOCKS * (uy / CHUNKBLOCKS)
            if (layer < blocks.length && uy < blocks(layer).length && ux < blocks(layer)(uy).length && blocks(layer)(
                  uy)(ux).id >= WorkbenchBlockType.id && blocks(layer)(uy)(ux).id <= GoldChestBlockType.id || blocks(
                  layer)(uy)(ux) === FurnaceBlockType || blocks(layer)(uy)(ux) === FurnaceOnBlockType || blocks(layer)(
                  uy)(ux).id >= ZincChestBlockType.id && blocks(layer)(uy)(ux).id <= ObduriteChestBlockType.id) {
              ic.foreach { icTemp =>
                if (icTemp.icType =/= Workbench) {
                  machinesx += icx
                  machinesy += icy
                  icmatrix(iclayer)(icy)(icx) =
                    Some(ItemCollection(icTemp.icType, icTemp.ids, icTemp.nums, icTemp.durs))
                }
                if (icTemp.icType === Workbench) {
                  if (player.imgState === Player.StillRight || player.imgState === Player.WalkRight1 || player.imgState === Player.WalkRight2) {
                    (0 until 9).foreach { i =>
                      UiItem.onImageItem(icTemp.ids(i)) { imgItm =>
                        entities += new IdEntity(
                          (icx * BLOCKSIZE).toDouble,
                          (icy * BLOCKSIZE).toDouble,
                          2,
                          -2,
                          imgItm,
                          icTemp.nums(i),
                          icTemp.durs(i),
                          75)
                        ()
                      }
                    }
                  }
                  if (player.imgState === Player.StillLeft || player.imgState === Player.WalkLeft1 || player.imgState === Player.WalkLeft2) {
                    (0 until 9).foreach { i =>
                      UiItem.onImageItem(icTemp.ids(i)) { imgItm =>
                        entities += new IdEntity(
                          (icx * BLOCKSIZE).toDouble,
                          (icy * BLOCKSIZE).toDouble,
                          -2,
                          -2,
                          imgItm,
                          icTemp.nums(i),
                          icTemp.durs(i),
                          75)
                        ()
                      }
                    }
                  }
                }
                if (icTemp.icType === Furnace) {
                  icmatrix(iclayer)(icy)(icx).foreach { icMatrixTemp =>
                    icMatrixTemp.FUELP = icTemp.FUELP
                    icMatrixTemp.SMELTP = icTemp.SMELTP
                    icMatrixTemp.F_ON = icTemp.F_ON
                  }
                }
                ic = None
              }
              iclayer = layer
              (0 until 3).foreach { l =>
                if (blocks(l)(uy)(ux) === WorkbenchBlockType) {
                  ic = icmatrix(l)(uy)(ux) match {
                    case Some(ItemCollection(Workbench, ids, nums, durs)) =>
                      Some(ItemCollection(Workbench, ids, nums, durs))
                    case _ => Some(new ItemCollection(Workbench))
                  }

                  icx = ux
                  icy = uy
                  ic.foreach(inventory.renderCollection)
                  showInv = true
                }
                if (blocks(l)(uy)(ux) === WoodenChestBlockType) {
                  ic = icmatrix(l)(uy)(ux) match {
                    case Some(ItemCollection(WoodenChest, ids, nums, durs)) =>
                      Some(ItemCollection(WoodenChest, ids, nums, durs))
                    case _ => Some(new ItemCollection(WoodenChest))
                  }

                  icx = ux
                  icy = uy
                  ic.foreach(inventory.renderCollection)
                  showInv = true
                }
                if (blocks(l)(uy)(ux) === StoneChestBlockType) {
                  ic = icmatrix(l)(uy)(ux) match {
                    case Some(ItemCollection(StoneChest, ids, nums, durs)) =>
                      Some(ItemCollection(StoneChest, ids, nums, durs))
                    case _ => Some(new ItemCollection(StoneChest))
                  }

                  icx = ux
                  icy = uy
                  ic.foreach(inventory.renderCollection)
                  showInv = true
                }
                if (blocks(l)(uy)(ux) === CopperChestBlockType) {
                  ic = icmatrix(l)(uy)(ux) match {
                    case Some(ItemCollection(CopperChest, ids, nums, durs)) =>
                      Some(ItemCollection(CopperChest, ids, nums, durs))
                    case _ => Some(new ItemCollection(CopperChest))
                  }

                  icx = ux
                  icy = uy
                  ic.foreach(inventory.renderCollection)
                  showInv = true
                }
                if (blocks(l)(uy)(ux) === IronChestBlockType) { //TODO: seems like all these blocks only differ by type
                  ic = icmatrix(l)(uy)(ux) match {
                    case Some(ItemCollection(IronChest, ids, nums, durs)) =>
                      Some(ItemCollection(IronChest, ids, nums, durs))
                    case _ => Some(new ItemCollection(IronChest))
                  }

                  icx = ux
                  icy = uy
                  ic.foreach(inventory.renderCollection)
                  showInv = true
                }
                if (blocks(l)(uy)(ux) === SilverChestBlockType) {
                  ic = icmatrix(l)(uy)(ux) match {
                    case Some(ItemCollection(SilverChest, ids, nums, durs)) =>
                      Some(ItemCollection(SilverChest, ids, nums, durs))
                    case _ => Some(new ItemCollection(SilverChest))
                  }

                  icx = ux
                  icy = uy
                  ic.foreach(inventory.renderCollection)
                  showInv = true
                }
                if (blocks(l)(uy)(ux) === GoldChestBlockType) {
                  ic = icmatrix(l)(uy)(ux) match {
                    case Some(ItemCollection(GoldChest, ids, nums, durs)) =>
                      Some(ItemCollection(GoldChest, ids, nums, durs))
                    case _ => Some(new ItemCollection(GoldChest))
                  }

                  icx = ux
                  icy = uy
                  ic.foreach(inventory.renderCollection)
                  showInv = true
                }
                if (blocks(l)(uy)(ux) === ZincChestBlockType) {
                  ic = icmatrix(l)(uy)(ux) match {
                    case Some(ItemCollection(ZincChest, ids, nums, durs)) =>
                      Some(ItemCollection(ZincChest, ids, nums, durs))
                    case _ => Some(new ItemCollection(ZincChest))
                  }

                  icx = ux
                  icy = uy
                  ic.foreach(inventory.renderCollection)
                  showInv = true
                }
                if (blocks(l)(uy)(ux) === RhymestoneChestBlockType) {
                  ic = icmatrix(l)(uy)(ux) match {
                    case Some(ItemCollection(RhymestoneChest, ids, nums, durs)) =>
                      Some(ItemCollection(RhymestoneChest, ids, nums, durs))
                    case _ => Some(new ItemCollection(RhymestoneChest))
                  }

                  icx = ux
                  icy = uy
                  ic.foreach(inventory.renderCollection)
                  showInv = true
                }
                if (blocks(l)(uy)(ux) === ObduriteChestBlockType) {

                  ic = icmatrix(l)(uy)(ux) match {
                    case Some(ItemCollection(ObduriteChest, ids, nums, durs)) =>
                      Some(ItemCollection(ObduriteChest, ids, nums, durs))
                    case _ => Some(new ItemCollection(ObduriteChest))
                  }
                  icx = ux
                  icy = uy
                  ic.foreach(inventory.renderCollection)
                  showInv = true
                }
                if (blocks(l)(uy)(ux) === FurnaceBlockType || blocks(l)(uy)(ux) === FurnaceOnBlockType) {
                  ic = icmatrix(l)(uy)(ux) match {
                    case Some(origIC @ ItemCollection(Furnace, ids, nums, durs)) =>
                      val updatedIC = ItemCollection(Furnace, ids, nums, durs)
                      updatedIC.FUELP = origIC.FUELP
                      updatedIC.SMELTP = origIC.SMELTP
                      updatedIC.F_ON = origIC.F_ON
                      icmatrix(l)(uy)(ux) = None
                      Some(updatedIC)
                    case _ => Some(new ItemCollection(Furnace))
                  }
                  icx = ux
                  icy = uy
                  ic.foreach(inventory.renderCollection)
                  showInv = true
                }

                if (ic.isDefined && blocks(l)(uy)(ux) =/= WorkbenchBlockType) {
                  (machinesx.length - 1 until (-1, -1)).foreach { i =>
                    if (machinesx(i) === icx && machinesy(i) === icy) {
                      machinesx.remove(i)
                      machinesy.remove(i)
                    }
                  }
                }
              }
            }
            if (blocks(layer)(uy)(ux) === TreeBlockType) {
              if (random.nextInt(2) === 0) {
                entities += new IdEntity(
                  (ux * BLOCKSIZE).toDouble,
                  (uy * BLOCKSIZE).toDouble,
                  random.nextDouble() * 8 - 4,
                  -3,
                  BarkUiItem,
                  1.toShort)
              }
              blocks(layer)(uy)(ux) = TreeNoBarkBlockType
            }
            if (mouseClicked2) {
              mouseNoLongerClicked2 = true
              blockTemp = blocks(layer)(uy)(ux)
              if (blocks(layer)(uy)(ux) === LeverBlockType || blocks(layer)(uy)(ux) === LeverLeftWallBlockType || blocks(
                    layer)(uy)(ux) === LeverLightWallBlockType) {
                blocks(layer)(uy)(ux) = BlockType.lookupById(blocks(layer)(uy)(ux).id + 1)
                addBlockPower(ux, uy)
                rdrawn(uy)(ux) = false
              } else if (blocks(layer)(uy)(ux) === LeverOnBlockType || blocks(layer)(uy)(ux) === LeverLeftWallOnBlockType || blocks(
                           layer)(uy)(ux) === LeverRightWallOnBlockType) {
                removeBlockPower(ux, uy, layer)
                if (wcnct(uy)(ux)) {
                  (0 until 3).foreach { l =>
                    if (l =/= layer) {
                      rbpRecur(ux, uy, l)
                    }
                  }
                }
                blocks(layer)(uy)(ux) = BlockType.lookupById(blocks(layer)(uy)(ux).id - 1)
                rdrawn(uy)(ux) = false
              }
              if (blocks(layer)(uy)(ux).id >= ZythiumWireBlockType.id && blocks(layer)(uy)(ux).id <= ZythiumWire5PowerBlockType.id) {
                wcnct(uy)(ux) = !wcnct(uy)(ux)
                rdrawn(uy)(ux) = false
                redoBlockPower(ux, uy, layer)
              }
              if (blocks(layer)(uy)(ux).id >= ZythiumAmplifierRightBlockType.id && blocks(layer)(uy)(ux).id <= ZythiumAmplifierUpOnBlockType.id) {
                removeBlockPower(ux, uy, layer)
                if (blocks(layer)(uy)(ux).id >= ZythiumAmplifierRightBlockType.id && blocks(layer)(uy)(ux).id <= ZythiumAmplifierLeftBlockType.id || blocks(
                      layer)(uy)(ux).id >= ZythiumAmplifierRightOnBlockType.id && blocks(layer)(uy)(ux).id <= ZythiumAmplifierLeftOnBlockType.id) {
                  blocks(layer)(uy)(ux) = BlockType.lookupById(blocks(layer)(uy)(ux).id + 1)
                } else {
                  blocks(layer)(uy)(ux) = BlockType.lookupById(blocks(layer)(uy)(ux).id - 3)
                }
                blockds(layer) = World.generate2b(blocks(layer), blockds(layer), ux, uy)
                rdrawn(uy)(ux) = false
                addAdjacentTilesToPQueueConditionally(ux, uy)
              }
              if (blocks(layer)(uy)(ux).id >= ZythiumInverterRightBlockType.id && blocks(layer)(uy)(ux).id <= ZythiumInverterUpOnBlockType.id) {
                removeBlockPower(ux, uy, layer)
                if (blocks(layer)(uy)(ux).id >= ZythiumInverterRightBlockType.id && blocks(layer)(uy)(ux).id <= ZythiumInverterLeftBlockType.id || blocks(
                      layer)(uy)(ux).id >= ZythiumInverterRightOnBlockType.id && blocks(layer)(uy)(ux).id <= ZythiumInverterLeftOnBlockType.id) {
                  blocks(layer)(uy)(ux) = BlockType.lookupById(blocks(layer)(uy)(ux).id + 1)
                } else {
                  blocks(layer)(uy)(ux) = BlockType.lookupById(blocks(layer)(uy)(ux).id - 3)
                }
                blockds(layer) = World.generate2b(blocks(layer), blockds(layer), ux, uy)
                rdrawn(uy)(ux) = false
                addAdjacentTilesToPQueueConditionally(ux, uy)
              }
              if (blocks(layer)(uy)(ux) === ButtonLeftBlockType || blocks(layer)(uy)(ux) === ButtonRightBlockType) {
                blocks(layer)(uy)(ux) = BlockType.lookupById(blocks(layer)(uy)(ux).id + 1)
                addBlockPower(ux, uy)
                rdrawn(uy)(ux) = false
                println("Srsly?") //TODO: should not use print in tight loops, should use asychronous logging
                updatex += ux
                updatey += uy
                updatet += 50
                updatel += layer
              }
              if (blocks(layer)(uy)(ux).id >= ZythiumDelayer1DelayRightBlockType.id && blocks(layer)(uy)(ux).id <= ZythiumDelayer8DelayUpOnBlockType.id) {
                if (blocks(layer)(uy)(ux).id >= ZythiumDelayer1DelayRightBlockType.id && blocks(layer)(uy)(ux).id <= ZythiumDelayer1DelayLeftBlockType.id || blocks(
                      layer)(uy)(ux).id >= ZythiumDelayer1DelayRightOnBlockType.id && blocks(layer)(uy)(ux).id <= ZythiumDelayer1DelayLeftOnBlockType.id ||
                    blocks(layer)(uy)(ux).id >= ZythiumDelayer2DelayRightBlockType.id && blocks(layer)(uy)(ux).id <= ZythiumDelayer2DelayLeftBlockType.id || blocks(
                      layer)(uy)(ux).id >= ZythiumDelayer2DelayRightOnBlockType.id && blocks(layer)(uy)(ux).id <= ZythiumDelayer2DelayLeftOnBlockType.id ||
                    blocks(layer)(uy)(ux).id >= ZythiumDelayer4DelayRightBlockType.id && blocks(layer)(uy)(ux).id <= ZythiumDelayer4DelayLeftBlockType.id || blocks(
                      layer)(uy)(ux).id >= ZythiumDelayer4DelayRightOnBlockType.id && blocks(layer)(uy)(ux).id <= ZythiumDelayer4DelayLeftOnBlockType.id ||
                    blocks(layer)(uy)(ux).id >= ZythiumDelayer8DelayRightBlockType.id && blocks(layer)(uy)(ux).id <= ZythiumDelayer8DelayLeftBlockType.id || blocks(
                      layer)(uy)(ux).id >= ZythiumDelayer8DelayRightOnBlockType.id && blocks(layer)(uy)(ux).id <= ZythiumDelayer8DelayLeftOnBlockType.id) {
                  blocks(layer)(uy)(ux) = BlockType.lookupById(blocks(layer)(uy)(ux).id + 1)
                } else {
                  blocks(layer)(uy)(ux) = BlockType.lookupById(blocks(layer)(uy)(ux).id - 3)
                }
                blockds(layer) = World.generate2b(blocks(layer), blockds(layer), ux, uy)
                rdrawn(uy)(ux) = false
                redoBlockPower(ux, uy, layer)
              }
            }
          }
        }
      }
      if (mouseNoLongerClicked2) {
        mouseClicked2 = false
      }
    } else {
      mouseClicked2 = true
    }
    if (showTool) {
      toolAngle += toolSpeed
      if (toolAngle >= 7.8) {
        toolAngle = 4.8
        showTool = false
      }
    }
    if (!DEBUG_INVINCIBLE && player.y / 16 > HEIGHT + 10) {
      vc += 1
      if (vc >= 1 / (pow(1.001, player.y / 16 - HEIGHT - 10) - 1.0)) {
        player.damage(1, false, inventory)
        vc = 0
      }
    } else {
      vc = 0
    }

    entities.toList.zipWithIndex.reverse.foreach {
      case (entityTemp: AIEntity, indexTemp) =>
        entityTemp.newMob.foreach { mob =>
          entities += mob
        }

        if (entityTemp.update(blocks(1), player, u, v)) {
          entities.remove(indexTemp)
        } else if (player.playerRect.intersects(entityTemp.rect)) {
          if (immune <= 0) {
            if (!DEBUG_INVINCIBLE) {
              player.damage(entityTemp.strategy.atk, true, inventory)
            }
            rgnc1 = 750
            immune = 40
            if (player.x + Player.width / 2 < entities(i).x + entityTemp.width / 2) {
              player.vx -= 8
            } else {
              player.vx += 8
            }
            player.vy -= 3.5
          }
        }

      case (entityTemp: IdEntity, indexTemp) =>
        if (entityTemp.update(blocks(1), player, u, v)) {
          entities.remove(indexTemp)
        } else if (player.playerRect.intersects(entityTemp.rect)) {
          if (entityTemp.mdelay <= 0) {
            n = inventory.addItem(entityTemp.id, entityTemp.num).toInt
            if (n =/= 0) {
              entities += new IdEntity(
                entityTemp.x,
                entityTemp.y,
                entityTemp.vx,
                entityTemp.vy,
                entityTemp.id,
                (entityTemp.num - n).toShort,
                entityTemp.dur)
            }
            entities.remove(indexTemp)
          }
        }
    }
    if (player.hp <= 0) {
      (0 until 40).foreach { j =>
        UiItem.onImageItem(inventory.ids(j)) { imgItm =>
          entities += new IdEntity(
            player.x,
            player.y,
            -1,
            random.nextDouble() * 6 - 3,
            imgItm,
            inventory.nums(j),
            inventory.durs(j))
          ()
        }
        inventory.removeLocation(j, inventory.nums(j))
      }

      ic.fold {
        if (showInv) {
          (0 until 4).foreach { i =>
            cic.foreach { c =>
              UiItem.onImageItem(c.ids(i)) { imgItm =>
                if (player.imgState === Player.StillRight || player.imgState === Player.WalkRight1 || player.imgState === Player.WalkRight2) {
                  entities += new IdEntity(player.x, player.y, 2, -2, imgItm, c.nums(i), c.durs(i), 75)
                }
                if (player.imgState === Player.StillLeft || player.imgState === Player.WalkLeft1 || player.imgState === Player.WalkLeft2) {
                  entities += new IdEntity(player.x, player.y, -2, -2, imgItm, c.nums(i), c.durs(i), 75)
                }
                inventory.removeLocationIC(c, i, c.nums(i))
                ()
              }
            }
          }
        }
        showInv = !showInv
      } { icTemp =>
        if (icTemp.icType =/= Workbench) {
          machinesx += icx
          machinesy += icy
          icmatrix(iclayer)(icy)(icx) = Some(ItemCollection(icTemp.icType, icTemp.ids, icTemp.nums, icTemp.durs))
        }
        if (icTemp.icType === Workbench) {
          if (player.imgState === Player.StillRight || player.imgState === Player.WalkRight1 || player.imgState === Player.WalkRight2) {
            (0 until 9).foreach { i =>
              UiItem.onImageItem(icTemp.ids(i)) { imgItm =>
                entities += new IdEntity(
                  (icx * BLOCKSIZE).toDouble,
                  (icy * BLOCKSIZE).toDouble,
                  2,
                  -2,
                  imgItm,
                  icTemp.nums(i),
                  icTemp.durs(i),
                  75)
                ()
              }
            }
          }
          if (player.imgState === Player.StillLeft || player.imgState === Player.WalkLeft1 || player.imgState === Player.WalkLeft2) {
            (0 until 9).foreach { i =>
              UiItem.onImageItem(icTemp.ids(i)) { imgItm =>
                entities += new IdEntity(
                  (icx * BLOCKSIZE).toDouble,
                  (icy * BLOCKSIZE).toDouble,
                  -2,
                  -2,
                  imgItm,
                  icTemp.nums(i),
                  icTemp.durs(i),
                  75)
                ()
              }
            }
          }
        }
        if (icTemp.icType === Furnace) {
          icmatrix(iclayer)(icy)(icx).foreach { icMatrixTemp =>
            icMatrixTemp.FUELP = icTemp.FUELP
            icMatrixTemp.SMELTP = icTemp.SMELTP
            icMatrixTemp.F_ON = icTemp.F_ON
          }
        }
        ic = None
      }
      UiItem.onImageItem(moveItem) { mi =>
        if (player.imgState === Player.StillRight || player.imgState === Player.WalkRight1 || player.imgState === Player.WalkRight2) {
          entities += new IdEntity(player.x, player.y, 2, -2, mi, moveNum, moveDur, 75)
        }
        if (player.imgState === Player.StillLeft || player.imgState === Player.WalkLeft1 || player.imgState === Player.WalkLeft2) {
          entities += new IdEntity(player.x, player.y, -2, -2, mi, moveNum, moveDur, 75)
        }
        moveItem = EmptyUiItem
        moveNum = 0
      }
      (0 until 4).foreach { i =>
        UiItem.onImageItem(armor.ids(i)) { ai =>
          if (player.imgState === Player.StillRight || player.imgState === Player.WalkRight1 || player.imgState === Player.WalkRight2) {
            entities += new IdEntity(player.x, player.y, 2, -2, ai, armor.nums(i), armor.durs(i), 75)
          }
          if (player.imgState === Player.StillLeft || player.imgState === Player.WalkLeft1 || player.imgState === Player.WalkLeft2) {
            entities += new IdEntity(player.x, player.y, -2, -2, ai, armor.nums(i), armor.durs(i), 75)
          }
          inventory.removeLocationIC(armor, i, armor.nums(i))
          ()
        }
      }
      player.x = WIDTH * 0.5 * BLOCKSIZE
      player.y = 45
      player.vx = 0
      player.vy = 0
      player.hp = Player.totalHP
      tool = None
      showTool = false
    }
    if (showTool) {
      val points: List[Point] = tool.toList.flatMap { t =>
        player.imgState match {
          case (Player.StillRight | Player.WalkRight1 | Player.WalkRight2) =>
            List[Point](
              new Point((player.x + Player.width / 2 + 6).toInt, (player.y + Player.height / 2).toInt),
              new Point(
                (player.x + Player.width / 2 + 6 + t.getWidth() * 2 * cos(toolAngle) + t.getHeight() * 2 * sin(
                  toolAngle)).toInt,
                (player.y + Player.height / 2 + t.getWidth() * 2 * sin(toolAngle) - t
                  .getHeight() * 2 * cos(toolAngle)).toInt
              ),
              new Point(
                (player.x + Player.width / 2 + 6 + t.getWidth() * 1 * cos(toolAngle) + t.getHeight() * 1 * sin(
                  toolAngle)).toInt,
                (player.y + Player.height / 2 + t.getWidth() * 1 * sin(toolAngle) - t
                  .getHeight() * 1 * cos(toolAngle)).toInt
              ),
              new Point(
                (player.x + Player.width / 2 + 6 + t.getWidth() * 0.5 * cos(toolAngle) + t.getHeight() * 0.5 * sin(
                  toolAngle)).toInt,
                (player.y + Player.height / 2 + t.getWidth() * 0.5 * sin(toolAngle) - t.getHeight() * 0.5 * cos(
                  toolAngle)).toInt
              ),
              new Point(
                (player.x + Player.width / 2 + 6 + t.getWidth() * 1.5 * cos(toolAngle) + t.getHeight() * 1.5 * sin(
                  toolAngle)).toInt,
                (player.y + Player.height / 2 + t.getWidth() * 1.5 * sin(toolAngle) - t.getHeight() * 1.5 * cos(
                  toolAngle)).toInt
              )
            )

          case (Player.StillLeft | Player.WalkLeft1 | Player.WalkLeft2) =>
            List[Point](
              new Point((player.x + Player.width / 2 - 6).toInt, (player.y + Player.height / 2).toInt),
              new Point(
                (player.x + Player.width / 2 - 6 + t.getWidth() * 2 * cos((Pi * 1.5) - toolAngle) + t
                  .getHeight() * 2 * sin((Pi * 1.5) - toolAngle)).toInt,
                (player.y + Player.height / 2 + t.getWidth() * 2 * sin((Pi * 1.5) - toolAngle) - t
                  .getHeight() * 2 * cos((Pi * 1.5) - toolAngle)).toInt
              ),
              new Point(
                (player.x + Player.width / 2 - 6 + t.getWidth() * 1 * cos((Pi * 1.5) - toolAngle) + t
                  .getHeight() * 1 * sin((Pi * 1.5) - toolAngle)).toInt,
                (player.y + Player.height / 2 + t.getWidth() * 1 * sin((Pi * 1.5) - toolAngle) - t
                  .getHeight() * 1 * cos((Pi * 1.5) - toolAngle)).toInt
              ),
              new Point(
                (player.x + Player.width / 2 - 6 + t.getWidth() * 0.5 * cos((Pi * 1.5) - toolAngle) + t
                  .getHeight() * 0.5 * sin((Pi * 1.5) - toolAngle)).toInt,
                (player.y + Player.height / 2 + t.getWidth() * 0.5 * sin((Pi * 1.5) - toolAngle) - t
                  .getHeight() * 0.5 * cos((Pi * 1.5) - toolAngle)).toInt
              ),
              new Point(
                (player.x + Player.width / 2 - 6 + t.getWidth() * 1.5 * cos((Pi * 1.5) - toolAngle) + t
                  .getHeight() * 1.5 * sin((Pi * 1.5) - toolAngle)).toInt,
                (player.y + Player.height / 2 + t.getWidth() * 1.5 * sin((Pi * 1.5) - toolAngle) - t
                  .getHeight() * 1.5 * cos((Pi * 1.5) - toolAngle)).toInt
              )
            )
        }
      }

      entities.toList.zipWithIndex.reverse
        .collect {
          case (e: AIEntity, index) => (e, index)
        }
        .filter { p =>
          val e              = p._1
          val anyPointInRect = points.exists(e.rect.contains)

          !e.nohit &&
          showTool &&
          anyPointInRect &&
          (e.strategy =/= Bee || random.nextInt(4) === 0)
        }
        .foreach { p =>
          val entityTemp = p._1
          val indexTemp  = p._2
          if (entityTemp.hit(UiItem.damage(inventory.tool()), player)) {
            val dropList = entityTemp.drops()
            dropList.foreach { dropId =>
              entities += new IdEntity(
                entityTemp.x,
                entityTemp.y,
                (random.nextInt(4) - 2).toDouble,
                -1,
                dropId,
                1.toShort)
            }
            entities.remove(indexTemp)
          }
          if (!UiItem.isTool(inventory.ids(inventory.selection))) {
            inventory.durs(inventory.selection) = (inventory.durs(inventory.selection) - 1).toShort
          } else {
            inventory.durs(inventory.selection) = (inventory.durs(inventory.selection) - 2).toShort
          }
          if (inventory.durs(inventory.selection) <= 0) {
            inventory.removeLocation(inventory.selection, inventory.nums(inventory.selection))
          }
        }
    }

    (-1 until entities.length).foreach { i =>
      if (i === -1) {
        width = Player.width
        height = Player.height
        p = player.x
        q = player.y
      } else {
        width = entities(i).width
        height = entities(i).height
        p = entities(i).x
        q = entities(i).y
      }
      var bx1 = (p / BLOCKSIZE).toInt
      var by1 = (q / BLOCKSIZE).toInt
      var bx2 = ((p + width) / BLOCKSIZE).toInt
      var by2 = ((q + height) / BLOCKSIZE).toInt

      bx1 = max(0, bx1)
      by1 = max(0, by1)
      bx2 = min(blocks(0).length - 1, bx2)
      by2 = min(blocks.length - 1, by2)

      lazy val isNamedEntity = entities(i) match {
        case _: AIEntity => true
        case _           => false
      }

      (bx1 to bx2).foreach { x =>
        (by1 to by2).foreach { y =>
          if (blocks(layer)(y)(x).id >= WoodenPressurePlateBlockType.id && blocks(layer)(y)(x).id <= ZythiumPressurePlateOnBlockType.id && (i === -1 || blocks(
                layer)(y)(x).id <= StonePressurePlateOnBlockType.id && (x =/= -1 && isNamedEntity || blocks(layer)(y)(x).id <= WoodenPressurePlateOnBlockType.id))) {
            if (blocks(layer)(y)(x) === WoodenPressurePlateBlockType || blocks(layer)(y)(x) === StonePressurePlateBlockType || blocks(
                  layer)(y)(x) === ZythiumPressurePlateBlockType) {
              blocks(layer)(y)(x) = BlockType.lookupById(blocks(layer)(y)(x).id + 1)
              rdrawn(y)(x) = false
              addBlockPower(x, y)
              println("Srsly?")
              updatex += x
              updatey += y
              updatet += 0
              updatel += 0
            }
          }
        }
      }
    }

    resolvePowerMatrix()
    resolveLightMatrix()
    immune -= 1
  }

  def hasOpenSpace(x: Int, y: Int, l: Int): Boolean = {
    try {
      blocks(l)(y - 1)(x - 1) === AirBlockType || !blockcds(blocks(l)(y - 1)(x - 1).id) ||
      blocks(l)(y - 1)(x) === AirBlockType || !blockcds(blocks(l)(y - 1)(x).id) ||
      blocks(l)(y - 1)(x + 1) === AirBlockType || !blockcds(blocks(l)(y - 1)(x + 1).id) ||
      blocks(l)(y)(x - 1) === AirBlockType || !blockcds(blocks(l)(y)(x - 1).id) ||
      blocks(l)(y)(x + 1) === AirBlockType || !blockcds(blocks(l)(y)(x + 1).id) ||
      blocks(l)(y + 1)(x - 1) === AirBlockType || !blockcds(blocks(l)(y + 1)(x - 1).id) ||
      blocks(l)(y + 1)(x) === AirBlockType || !blockcds(blocks(l)(y + 1)(x).id) ||
      blocks(l)(y + 1)(x + 1) === AirBlockType || !blockcds(blocks(l)(y + 1)(x + 1).id)
    } catch {
      case _: ArrayIndexOutOfBoundsException => false
    }
  }

  def empty(x: Int, y: Int): Boolean = {
    (blocks(0)(y)(x) === AirBlockType || BLOCKLIGHTS.get(blocks(0)(y)(x).id).fold(true)(_ === 0)) &&
    (blocks(1)(y)(x) === AirBlockType || BLOCKLIGHTS.get(blocks(1)(y)(x).id).fold(true)(_ === 0)) &&
    (blocks(2)(y)(x) === AirBlockType || BLOCKLIGHTS.get(blocks(2)(y)(x).id).fold(true)(_ === 0))
  }

  def breakCurrentBlock(): Unit = {
    if (DEBUG_INSTAMINE || mining >= UiItem.blockDurabilityForTool(inventory.tool(), blocks(layer)(uy)(ux))) {
      if (blocks(0)(uy)(ux) === TreeRootBlockType) {
        blocks(0)(uy)(ux) = AirBlockType
        (uy - 1 until uy + 2).foreach { uly =>
          (ux - 1 until ux + 2).foreach { ulx =>
            blockdns(uly)(ulx) = random.nextInt(5).toByte
          }
        }
        (uy until uy + 3).foreach { uly =>
          (ux - 1 until ux + 2).foreach { ulx =>
            drawn(uly)(ulx) = false
          }
        }
      }
      if (blocks(0)(uy + 1)(ux) === TreeRootBlockType) {
        blocks(0)(uy + 1)(ux) = AirBlockType
        (uy until uy + 3).foreach { uly =>
          (ux - 1 until ux + 2).foreach { ulx =>
            blockdns(uly)(ulx) = random.nextInt(5).toByte
          }
        }
        (uy until uy + 3).foreach { uly =>
          (ux - 1 until ux + 2).foreach { ulx =>
            drawn(uly)(ulx) = false
          }
        }
      }
      if (blocks(layer)(uy)(ux).id >= WorkbenchBlockType.id && blocks(layer)(uy)(ux).id <= GoldChestBlockType.id || blocks(layer)(
            uy)(ux) === FurnaceBlockType || blocks(layer)(uy)(ux) === FurnaceOnBlockType || blocks(layer)(uy)(ux).id >= ZincChestBlockType.id && blocks(
            layer)(uy)(ux).id <= ObduriteChestBlockType.id) {
        ic.foreach { icTemp =>
          icTemp.ids.indices.foreach { i =>
            UiItem.onImageItem(icTemp.ids(i)) { idsI =>
              if (!(icTemp.icType === Furnace && i === 1)) {
                entities += new IdEntity(
                  (ux * BLOCKSIZE).toDouble,
                  (uy * BLOCKSIZE).toDouble,
                  random.nextDouble() * 4 - 2,
                  -2,
                  idsI,
                  icTemp.nums(i),
                  icTemp.durs(i))
              }
              ()
            }
          }
        }
        icmatrix(layer)(uy)(ux).foreach { icMatrixTemp =>
          icMatrixTemp.ids.indices.foreach { i =>
            UiItem.onImageItem(icMatrixTemp.ids(i)) { icmi =>
              if (!(icMatrixTemp.icType === Furnace && i === 1)) {
                entities += new IdEntity(
                  (ux * BLOCKSIZE).toDouble,
                  (uy * BLOCKSIZE).toDouble,
                  random.nextDouble() * 4 - 2,
                  -2,
                  icmi,
                  icMatrixTemp.nums(i),
                  icMatrixTemp.durs(i))
              }
              ()
            }
          }
          icmatrix(layer)(uy)(ux) = None
        }
        ic = None
        import scala.util.control.Breaks._
        breakable {
          machinesx.indices.foreach { i =>
            if (machinesx(i) === ux && machinesy(i) === uy) {
              machinesx.remove(i)
              machinesy.remove(i)
              break
            }
          }
        }
      }
      BlockType.drop(blocks(layer)(uy)(ux)).foreach { blockdrops =>
        UiItem.onImageItem(blockdrops) { imgItm =>
          if (blocks(layer)(uy)(ux) =/= AirBlockType) {
            entities += new IdEntity(
              (ux * BLOCKSIZE).toDouble,
              (uy * BLOCKSIZE).toDouble,
              random.nextDouble() * 4 - 2,
              -2,
              imgItm,
              1.toShort)
          }
          ()
        }
      }

      var tempUiItem: UiItem = EmptyUiItem
      blocks(layer)(uy)(ux) match {
        case SunflowerStage1BlockType =>
          tempUiItem = SunflowerSeedsUiItem
          n = random.nextInt(4) - 2
        case SunflowerStage2BlockType =>
          tempUiItem = SunflowerSeedsUiItem
          n = random.nextInt(2)
        case SunflowerStage3BlockType =>
          tempUiItem = SunflowerSeedsUiItem
          n = random.nextInt(3) + 1
        case MoonflowerStage1BlockType =>
          tempUiItem = MoonflowerSeedsUiItem
          n = random.nextInt(4) - 2
        case MoonflowerStage2BlockType =>
          tempUiItem = MoonflowerSeedsUiItem
          n = random.nextInt(2)
        case MoonflowerStage3BlockType =>
          tempUiItem = MoonflowerSeedsUiItem
          n = random.nextInt(3) + 1
        case DryweedStage1BlockType =>
          tempUiItem = DryweedSeedsUiItem
          n = random.nextInt(4) - 2
        case DryweedStage2BlockType =>
          tempUiItem = DryweedSeedsUiItem
          n = random.nextInt(2)
        case DryweedStage3BlockType =>
          tempUiItem = DryweedSeedsUiItem
          n = random.nextInt(3) + 1
        case GreenleafStage1BlockType =>
          tempUiItem = GreenleafSeedsUiItem
          n = random.nextInt(4) - 2
        case GreenleafStage2BlockType =>
          tempUiItem = GreenleafSeedsUiItem
          n = random.nextInt(2)
        case GreenleafStage3BlockType =>
          tempUiItem = GreenleafSeedsUiItem
          n = random.nextInt(3) + 1
        case FrostleafStage1BlockType =>
          tempUiItem = FrostleafSeedsUiItem
          n = random.nextInt(4) - 2
        case FrostleafStage2BlockType =>
          tempUiItem = FrostleafSeedsUiItem
          n = random.nextInt(2)
        case FrostleafStage3BlockType =>
          tempUiItem = FrostleafSeedsUiItem
          n = random.nextInt(3) + 1
        case CaverootStage1BlockType =>
          tempUiItem = CaverootSeedsUiItem
          n = random.nextInt(4) - 2
        case CaverootStage2BlockType =>
          tempUiItem = CaverootSeedsUiItem
          n = random.nextInt(2)
        case CaverootStage3BlockType =>
          tempUiItem = CaverootSeedsUiItem
          n = random.nextInt(3) + 1
        case SkyblossomStage1BlockType =>
          tempUiItem = SkyblossomSeedsUiItem
          n = random.nextInt(4) - 2
        case SkyblossomStage2BlockType =>
          tempUiItem = SkyblossomSeedsUiItem
          n = random.nextInt(2)
        case SkyblossomStage3BlockType =>
          tempUiItem = SkyblossomSeedsUiItem
          n = random.nextInt(3) + 1
        case VoidRotStage1BlockType =>
          tempUiItem = VoidRotSeedsUiItem
          n = random.nextInt(4) - 2
        case VoidRotStage2BlockType =>
          tempUiItem = VoidRotSeedsUiItem
          n = random.nextInt(2)
        case VoidRotStage3BlockType =>
          tempUiItem = VoidRotSeedsUiItem
          n = random.nextInt(3) + 1
        case MarshleafStage1BlockType =>
          tempUiItem = MarshleafSeedsUiItem
          n = random.nextInt(4) - 2
        case MarshleafStage2BlockType =>
          tempUiItem = MarshleafSeedsUiItem
          n = random.nextInt(2)
        case MarshleafStage3BlockType =>
          tempUiItem = MarshleafSeedsUiItem
          n = random.nextInt(3) + 1
        case _ =>
      }

      UiItem.onImageItem(tempUiItem) { imgItm =>
        (0 until max(1, n)).foreach { _ =>
          entities += new IdEntity(
            (ux * BLOCKSIZE).toDouble,
            (uy * BLOCKSIZE).toDouble,
            random.nextDouble() * 4 - 2,
            -2,
            imgItm,
            1.toShort)
          ()
        }
      }
      removeBlockLighting(ux, uy)
      blockTemp = blocks(layer)(uy)(ux)
      blocks(layer)(uy)(ux) = AirBlockType
      if (blockTemp.id >= ZythiumWireBlockType.id && blockTemp.id <= ZythiumWire5PowerBlockType.id) {
        redoBlockPower(ux, uy, layer)
      }
      if (powers(blockTemp)) {
        removeBlockPower(ux, uy, layer)
      }
      if (ltrans(blockTemp.id)) {
        addSunLighting(ux, uy)
        redoBlockLighting(ux, uy)
      }
      addSunLighting(ux, uy)
      blockds(layer) = World.generate2b(blocks(layer), blockds(layer), ux, uy)
      (uy - 1 until uy + 2).foreach { uly =>
        (ux - 1 until ux + 2).foreach { ulx =>
          blockdns(uly)(ulx) = random.nextInt(5).toByte
        }
      }
      (uy - 1 until uy + 2).foreach { uly =>
        (ux - 1 until ux + 2).foreach { ulx =>
          drawn(uly)(ulx) = false
        }
      }
      (uy - 4 until uy + 5).foreach { uly =>
        (ux - 4 until ux + 5).foreach { ulx =>
          (0 until (3, 2)).foreach { l =>
            if (uly >= 0 && uly < HEIGHT && blocks(l)(uly)(ulx) === LeavesBlockType) {
              keepLeaf = false
              import scala.util.control.Breaks._
              breakable {
                (uly - 4 until uly + 5).foreach { uly2 =>
                  breakable {
                    (ulx - 4 until ulx + 5).foreach { ulx2 =>
                      if (uly2 >= 0 && uly2 < HEIGHT && (blocks(1)(uly2)(ulx2) === TreeBlockType || blocks(1)(uly2)(
                            ulx2) === TreeNoBarkBlockType)) {
                        keepLeaf = true
                        break
                      }
                    }
                  }
                  if (keepLeaf) break
                }
              }
              if (!keepLeaf) {
                blocks(l)(uly)(ulx) = AirBlockType
                blockds(l) = World.generate2b(blocks(l), blockds(l), ulx, uly)
                (uly - 1 until uly + 2).foreach { uly2 =>
                  (ulx - 1 until ulx + 2).foreach { ulx2 =>
                    drawn(uly2)(ulx2) = false
                  }
                }
              }
            }
          }
        }
      }
      import scala.util.control.Breaks._
      breakable {
        while (true) {
          BlockType.drop(blocks(layer)(uy)(ux - 1)).foreach { blockdrop =>
            val itemblock = UiItem.blockForTool(blockdrop)
            val isTorch = (for {
              torch <- TORCHESR.get(itemblock)
            } yield torch === blocks(layer)(uy)(ux - 1)).getOrElse(false)
            if (isTorch || blockdrop === LeverUiItem || blockdrop === ButtonUiItem) {
              UiItem.onImageItem(blockdrop) { imgItm =>
                entities += new IdEntity(
                  ((ux - 1) * BLOCKSIZE).toDouble,
                  (uy * BLOCKSIZE).toDouble,
                  random.nextDouble() * 4 - 2,
                  -2,
                  imgItm,
                  1.toShort)
                ()
              }
              removeBlockLighting(ux - 1, uy)
              if (layer === 1) {
                addSunLighting(ux - 1, uy)
              }
              blockTemp = blocks(layer)(uy)(ux - 1)
              blocks(layer)(uy)(ux - 1) = AirBlockType
              if (blockTemp.id >= ZythiumWireBlockType.id && blockTemp.id <= ZythiumWire5PowerBlockType.id) {
                redoBlockPower(ux, uy, layer)
              }
              if (powers(blockTemp)) {
                removeBlockPower(ux, uy, layer)
              }
              drawn(uy)(ux - 1) = false
            }
          }

          BlockType.drop(blocks(layer)(uy)(ux + 1)).foreach { blockdrop =>
            val itemblock = UiItem.blockForTool(blockdrop)
            val isTorch = (for {
              torch <- TORCHESR.get(itemblock)
            } yield torch === blocks(layer)(uy)(ux + 1)).getOrElse(false)

            if (isTorch || blockdrop === LeverUiItem || blockdrop === ButtonUiItem) {
              UiItem.onImageItem(blockdrop) { imgItm =>
                entities += new IdEntity(
                  ((ux + 1) * BLOCKSIZE).toDouble,
                  (uy * BLOCKSIZE).toDouble,
                  random.nextDouble() * 4 - 2,
                  -2,
                  imgItm,
                  1.toShort)
                ()
              }
              removeBlockLighting(ux + 1, uy)
              if (layer === 1) {
                addSunLighting(ux + 1, uy)
              }
              blockTemp = blocks(layer)(uy)(ux + 1)
              blocks(layer)(uy)(ux + 1) = AirBlockType
              if (blockTemp.id >= ZythiumWireBlockType.id && blockTemp.id <= ZythiumWire5PowerBlockType.id) {
                redoBlockPower(ux, uy, layer)
              }
              if (powers(blockTemp)) {
                removeBlockPower(ux, uy, layer)
              }
              drawn(uy)(ux + 1) = false
            }
          }

          uy -= 1
          if (uy === -1 || !BlockType.isGsupported(blocks(layer)(uy)(ux))) {
            addSunLighting(ux, uy)
            break
          }
          BlockType.drop(blocks(layer)(uy)(ux)).foreach { blockdrop =>
            UiItem.onImageItem(blockdrop) { imgItm =>
              entities += new IdEntity(
                (ux * BLOCKSIZE).toDouble,
                (uy * BLOCKSIZE).toDouble,
                random.nextDouble() * 4 - 2,
                -2,
                imgItm,
                1.toShort)
              ()
            }
          }

          var tempUiItem: UiItem = EmptyUiItem
          blocks(layer)(uy)(ux) match {
            case SunflowerStage1BlockType =>
              tempUiItem = SunflowerSeedsUiItem
              n = random.nextInt(4) - 2
            case SunflowerStage2BlockType =>
              tempUiItem = SunflowerSeedsUiItem
              n = random.nextInt(2)
            case SunflowerStage3BlockType =>
              tempUiItem = SunflowerSeedsUiItem
              n = random.nextInt(3) + 1
            case MoonflowerStage1BlockType =>
              tempUiItem = MoonflowerSeedsUiItem
              n = random.nextInt(4) - 2
            case MoonflowerStage2BlockType =>
              tempUiItem = MoonflowerSeedsUiItem
              n = random.nextInt(2)
            case MoonflowerStage3BlockType =>
              tempUiItem = MoonflowerSeedsUiItem
              n = random.nextInt(3) + 1
            case DryweedStage1BlockType =>
              tempUiItem = DryweedSeedsUiItem
              n = random.nextInt(4) - 2
            case DryweedStage2BlockType =>
              tempUiItem = DryweedSeedsUiItem
              n = random.nextInt(2)
            case DryweedStage3BlockType =>
              tempUiItem = DryweedSeedsUiItem
              n = random.nextInt(3) + 1
            case GreenleafStage1BlockType =>
              tempUiItem = GreenleafSeedsUiItem
              n = random.nextInt(4) - 2
            case GreenleafStage2BlockType =>
              tempUiItem = GreenleafSeedsUiItem
              n = random.nextInt(2)
            case GreenleafStage3BlockType =>
              tempUiItem = GreenleafSeedsUiItem
              n = random.nextInt(3) + 1
            case FrostleafStage1BlockType =>
              tempUiItem = FrostleafSeedsUiItem
              n = random.nextInt(4) - 2
            case FrostleafStage2BlockType =>
              tempUiItem = FrostleafSeedsUiItem
              n = random.nextInt(2)
            case FrostleafStage3BlockType =>
              tempUiItem = FrostleafSeedsUiItem
              n = random.nextInt(3) + 1
            case CaverootStage1BlockType =>
              tempUiItem = CaverootSeedsUiItem
              n = random.nextInt(4) - 2
            case CaverootStage2BlockType =>
              tempUiItem = CaverootSeedsUiItem
              n = random.nextInt(2)
            case CaverootStage3BlockType =>
              tempUiItem = CaverootSeedsUiItem
              n = random.nextInt(3) + 1
            case SkyblossomStage1BlockType =>
              tempUiItem = SkyblossomSeedsUiItem
              n = random.nextInt(4) - 2
            case SkyblossomStage2BlockType =>
              tempUiItem = SkyblossomSeedsUiItem
              n = random.nextInt(2)
            case SkyblossomStage3BlockType =>
              tempUiItem = SkyblossomSeedsUiItem
              n = random.nextInt(3) + 1
            case VoidRotStage1BlockType =>
              tempUiItem = VoidRotSeedsUiItem
              n = random.nextInt(4) - 2
            case VoidRotStage2BlockType =>
              tempUiItem = VoidRotSeedsUiItem
              n = random.nextInt(2)
            case VoidRotStage3BlockType =>
              tempUiItem = VoidRotSeedsUiItem
              n = random.nextInt(3) + 1
            case MarshleafStage1BlockType =>
              tempUiItem = MarshleafSeedsUiItem
              n = random.nextInt(4) - 2
            case MarshleafStage2BlockType =>
              tempUiItem = MarshleafSeedsUiItem
              n = random.nextInt(2)
            case MarshleafStage3BlockType =>
              tempUiItem = MarshleafSeedsUiItem
              n = random.nextInt(3) + 1
            case _ =>
          }

          UiItem.onImageItem(tempUiItem) { imgItm =>
            (0 until max(1, n)).foreach { _ =>
              entities += new IdEntity(
                (ux * BLOCKSIZE).toDouble,
                (uy * BLOCKSIZE).toDouble,
                random.nextDouble() * 4 - 2,
                -2,
                imgItm,
                1.toShort)
              ()
            }
          }
          removeBlockLighting(ux, uy)
          blockTemp = blocks(layer)(uy)(ux)
          blocks(layer)(uy)(ux) = AirBlockType
          if (blockTemp.id >= ZythiumWireBlockType.id && blockTemp.id <= ZythiumWire5PowerBlockType.id) {
            redoBlockPower(ux, uy, layer)
          }
          if (powers(blockTemp)) {
            removeBlockPower(ux, uy, layer)
          }
          if (ltrans(blockTemp.id)) {
            addSunLighting(ux, uy)
            redoBlockLighting(ux, uy)
          }
          addSunLighting(ux, uy)
          blockds(layer) = World.generate2b(blocks(layer), blockds(layer), ux, uy)
          (uy - 1 until uy + 2).foreach { uly =>
            (ux - 1 until ux + 2).foreach { ulx =>
              blockdns(uly)(ulx) = random.nextInt(5).toByte
            }
          }
          (uy - 1 until uy + 2).foreach { uly =>
            (ux - 1 until ux + 2).foreach { ulx =>
              drawn(uly)(ulx) = false
            }
          }
          (uy - 4 until uy + 5).foreach { uly =>
            (ux - 4 until ux + 5).foreach { ulx =>
              (0 until (3, 2)).foreach { l =>
                if (uly >= 0 && uly < HEIGHT && blocks(l)(uly)(ulx) === LeavesBlockType) {
                  keepLeaf = false
                  breakable {
                    (uly - 4 until uly + 5).foreach { uly2 =>
                      breakable {
                        (ulx - 4 until ulx + 5).foreach { ulx2 =>
                          if (uly2 >= 0 && uly2 < HEIGHT && (blocks(1)(uly2)(ulx2) === TreeBlockType || blocks(1)(uly2)(
                                ulx2) === TreeNoBarkBlockType)) {
                            keepLeaf = true
                            break
                          }
                        }
                      }
                      if (keepLeaf) break
                    }
                  }
                  if (!keepLeaf) {
                    blocks(l)(uly)(ulx) = AirBlockType
                    blockds(l) = World.generate2b(blocks(l), blockds(l), ulx, uly)
                    (uly - 1 until uly + 2).foreach { uly2 =>
                      (ulx - 1 until ulx + 2).foreach { ulx2 =>
                        drawn(uly2)(ulx2) = false
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
      mining = 0
    }
  }

  def updateEnvironment(): Unit = {
    timeOfDay += 1.2 * DEBUG_ACCEL
    (cloudsx.length - 1 until (-1, -1)).foreach { i =>
      cloudsx.update(i, cloudsx(i) + cloudsv(i))
      if (cloudsx(i) < -250 || cloudsx(i) > getWidth + 250) {
        cloudsx.remove(i)
        cloudsy.remove(i)
        cloudsv.remove(i)
        cloudsn.remove(i)
      }
    }
    if (random.nextInt((1500 / DEBUG_ACCEL).toInt) === 0) {
      cloudsn += random.nextInt(1)
      cloud = clouds(cloudsn(cloudsn.length - 1))
      if (random.nextInt(2) === 0) {
        cloudsx += (-cloud.getWidth() * 2).toDouble
        cloudsv += 0.1 * DEBUG_ACCEL
      } else {
        cloudsx += getWidth.toDouble
        cloudsv += -0.1 * DEBUG_ACCEL
      }
      cloudsy += (random.nextDouble() * (getHeight - cloud.getHeight()) + cloud.getHeight())
      ()
    }
  }

  def addBlockLighting(ux: Int, uy: Int): Unit = {
    n = findNonLayeredBlockLightSource(ux, uy)
    if (n =/= 0) {
      addTileToZQueue(ux, uy)
      lights(uy)(ux) = max(lights(uy)(ux), n.toFloat)
      lsources(uy)(ux) = true
      addTileToQueue(ux, uy)
    }
  }

  def addBlockPower(ux: Int, uy: Int): Unit = {
    if (powers(blocks(1)(uy)(ux))) {
      if (blocks(1)(uy)(ux).id >= ZythiumDelayer1DelayRightBlockType.id && blocks(1)(uy)(ux).id <= ZythiumDelayer8DelayUpOnBlockType.id) {
        println("Whaaat?")
        updatex += ux
        updatey += uy
        DDELAY.get(blocks(1)(uy)(ux).id).foreach(updatet.+=)
        updatel += 1
      } else {
        addTileToPZQueue(ux, uy)
        power(1)(uy)(ux) = 5.toFloat
        if (conducts(blocks(1)(uy)(ux).id) >= 0 && wcnct(uy)(ux)) {
          if (receives(blocks(0)(uy)(ux).id)) {
            power(0)(uy)(ux) = power(1)(uy)(ux) - conducts(blocks(1)(uy)(ux).id).toFloat
          }
          if (receives(blocks(2)(uy)(ux).id)) {
            power(2)(uy)(ux) = power(1)(uy)(ux) - conducts(blocks(1)(uy)(ux).id).toFloat
          }
        }
        addTileToPQueue(ux, uy)
      }
    }
    if (powers(blocks(0)(uy)(ux))) {
      if (blocks(0)(uy)(ux).id >= ZythiumDelayer1DelayRightBlockType.id && blocks(0)(uy)(ux).id <= ZythiumDelayer8DelayUpOnBlockType.id) {
        println("Whaaat?")
        updatex += ux
        updatey += uy
        DDELAY.get(blocks(0)(uy)(ux).id).foreach(updatet.+=)
        updatel += 0
      } else {
        addTileToPZQueue(ux, uy)
        power(0)(uy)(ux) = 5.toFloat
        if (conducts(blocks(0)(uy)(ux).id) >= 0 && wcnct(uy)(ux)) {
          if (receives(blocks(1)(uy)(ux).id)) {
            power(1)(uy)(ux) = power(0)(uy)(ux) - conducts(blocks(0)(uy)(ux).id).toFloat
          }
          if (receives(blocks(2)(uy)(ux).id)) {
            power(2)(uy)(ux) = power(0)(uy)(ux) - conducts(blocks(0)(uy)(ux).id).toFloat
          }
        }
        addTileToPQueue(ux, uy)
      }
    }
    if (powers(blocks(2)(uy)(ux))) {
      if (blocks(2)(uy)(ux).id >= ZythiumDelayer1DelayRightBlockType.id && blocks(2)(uy)(ux).id <= ZythiumDelayer8DelayUpOnBlockType.id) {
        println("Whaaat?")
        updatex += ux
        updatey += uy
        DDELAY.get(blocks(2)(uy)(ux).id).foreach(updatet.+=)
        updatel += 2
        ()
      } else {
        addTileToPZQueue(ux, uy)
        power(2)(uy)(ux) = 5.toFloat
        if (conducts(blocks(2)(uy)(ux).id) >= 0 && wcnct(uy)(ux)) {
          if (receives(blocks(0)(uy)(ux).id)) {
            power(0)(uy)(ux) = power(2)(uy)(ux) - conducts(blocks(2)(uy)(ux).id).toFloat
          }
          if (receives(blocks(1)(uy)(ux).id)) {
            power(1)(uy)(ux) = power(2)(uy)(ux) - conducts(blocks(2)(uy)(ux).id).toFloat
          }
        }
        addTileToPQueue(ux, uy)
      }
    }
  }

  def removeBlockLighting(ux: Int, uy: Int): Unit = {
    removeBlockLighting(ux, uy, layer)
  }

  def removeBlockLighting(ux: Int, uy: Int, layer: Int): Unit = {
    n = findNonLayeredBlockLightSource(ux, uy)
    if (n =/= 0) {
      lsources(uy)(ux) = isNonLayeredBlockLightSource(ux, uy, layer)
      (-n until n + 1).foreach { axl =>
        (-n until n + 1).foreach { ayl =>
          if (abs(axl) + abs(ayl) <= n && uy + ayl >= 0 && uy + ayl < HEIGHT && lights(uy + ayl)(ux + axl) =/= 0) {
            addTileToZQueue(ux + axl, uy + ayl)
            lights(uy + ayl)(ux + axl) = 0.toFloat
          }
        }
      }
      (-n - BRIGHTEST until n + 1 + BRIGHTEST).foreach { axl =>
        (-n - BRIGHTEST until n + 1 + BRIGHTEST).foreach { ayl =>
          if (abs(axl) + abs(ayl) <= n + BRIGHTEST && uy + ayl >= 0 && uy + ayl < HEIGHT) {
            if (lsources(uy + ayl)(ux + axl)) {
              addTileToQueue(ux + axl, uy + ayl)
            }
          }
        }
      }
    }
  }

  def rbpRecur(ux: Int, uy: Int, lyr: Int): Unit = {
    arbprd(lyr)(uy)(ux) = true
    println("(rbpR) " + ux + " " + uy)
    addTileToPZQueue(ux, uy)
    val remember: Array[Boolean] = Array(false, false, false, false)
    var ax3, ay3: Int            = 0
    (0 until 4).foreach { ir =>
      ax3 = ux + cl(ir)(0)
      ay3 = uy + cl(ir)(1)
      lazy val block   = blocks(lyr)(ay3)(ax3)
      lazy val blockId = block.id
      if (ay3 >= 0 && ay3 < HEIGHT && power(lyr)(ay3)(ax3) =/= 0) {
        if (power(lyr)(ay3)(ax3) =/= 0 && !(power(lyr)(ay3)(ax3) === power(lyr)(uy)(ux) - conducts(
              blocks(lyr)(uy)(ux).id).toFloat) &&
            (!(blockId >= ZythiumAmplifierRightBlockType.id && blockId <= ZythiumAmplifierUpOnBlockType.id || blockId >= ZythiumInverterRightBlockType.id && blockId <= ZythiumInverterUpOnBlockType.id) ||
            !(blockId >= ZythiumAmplifierRightBlockType.id && blockId <= ZythiumAmplifierUpOnBlockType.id && ux > ax3 && block =/= ZythiumAmplifierRightBlockType && block =/= ZythiumAmplifierRightOnBlockType ||
              blockId >= ZythiumAmplifierRightBlockType.id && blockId <= ZythiumAmplifierUpOnBlockType.id && uy > ay3 && block =/= ZythiumAmplifierDownBlockType && block =/= ZythiumAmplifierDownOnBlockType ||
              blockId >= ZythiumAmplifierRightBlockType.id && blockId <= ZythiumAmplifierUpOnBlockType.id && ux < ax3 && block =/= ZythiumAmplifierLeftBlockType && block =/= ZythiumAmplifierLeftOnBlockType ||
              blockId >= ZythiumAmplifierRightBlockType.id && blockId <= ZythiumAmplifierUpOnBlockType.id && uy < ay3 && block =/= ZythiumAmplifierUpBlockType && block =/= ZythiumAmplifierUpOnBlockType) &&
            !(blockId >= ZythiumInverterRightBlockType.id && blockId <= ZythiumInverterUpOnBlockType.id && ux > ax3 && block =/= ZythiumInverterRightBlockType && block =/= ZythiumInverterRightOnBlockType ||
              blockId >= ZythiumInverterRightBlockType.id && blockId <= ZythiumInverterUpOnBlockType.id && uy > ay3 && block =/= ZythiumInverterDownBlockType && block =/= ZythiumInverterDownOnBlockType ||
              blockId >= ZythiumInverterRightBlockType.id && blockId <= ZythiumInverterUpOnBlockType.id && ux < ax3 && block =/= ZythiumInverterLeftBlockType && block =/= ZythiumInverterLeftOnBlockType ||
              blockId >= ZythiumInverterRightBlockType.id && blockId <= ZythiumInverterUpOnBlockType.id && uy < ay3 && block =/= ZythiumInverterUpBlockType && block =/= ZythiumInverterUpOnBlockType) &&
            !(blockId >= ZythiumDelayer1DelayRightBlockType.id && blockId <= ZythiumDelayer8DelayUpOnBlockType.id && ux > ax3 && block =/= ZythiumDelayer1DelayRightBlockType && block =/= ZythiumDelayer1DelayRightOnBlockType && block =/= ZythiumDelayer2DelayRightBlockType && block =/= ZythiumDelayer2DelayRightOnBlockType && block =/= ZythiumDelayer4DelayRightBlockType && block =/= ZythiumDelayer4DelayRightOnBlockType && block =/= ZythiumDelayer8DelayRightBlockType && block =/= ZythiumDelayer8DelayRightOnBlockType ||
              blockId >= ZythiumDelayer1DelayRightBlockType.id && blockId <= ZythiumDelayer8DelayUpOnBlockType.id && uy > ay3 && block =/= ZythiumDelayer1DelayDownBlockType && block =/= ZythiumDelayer1DelayDownOnBlockType && block =/= ZythiumDelayer2DelayDownBlockType && block =/= ZythiumDelayer2DelayDownOnBlockType && block =/= ZythiumDelayer4DelayDownBlockType && block =/= ZythiumDelayer4DelayDownOnBlockType && block =/= ZythiumDelayer8DelayDownBlockType && block =/= ZythiumDelayer8DelayDownOnBlockType ||
              blockId >= ZythiumDelayer1DelayRightBlockType.id && blockId <= ZythiumDelayer8DelayUpOnBlockType.id && ux < ax3 && block =/= ZythiumDelayer1DelayLeftBlockType && block =/= ZythiumDelayer1DelayLeftOnBlockType && block =/= ZythiumDelayer2DelayLeftBlockType && block =/= ZythiumDelayer2DelayLeftOnBlockType && block =/= ZythiumDelayer4DelayLeftBlockType && block =/= ZythiumDelayer4DelayLeftOnBlockType && block =/= ZythiumDelayer8DelayLeftBlockType && block =/= ZythiumDelayer8DelayLeftOnBlockType ||
              blockId >= ZythiumDelayer1DelayRightBlockType.id && blockId <= ZythiumDelayer8DelayUpOnBlockType.id && uy < ay3 && block =/= ZythiumDelayer1DelayUpBlockType && block =/= ZythiumDelayer1DelayUpOnBlockType && block =/= ZythiumDelayer2DelayUpBlockType && block =/= ZythiumDelayer2DelayUpOnBlockType && block =/= ZythiumDelayer4DelayUpBlockType && block =/= ZythiumDelayer4DelayUpOnBlockType && block =/= ZythiumDelayer8DelayUpBlockType && block =/= ZythiumDelayer8DelayUpOnBlockType))) {
          println("Added tile " + ax3 + " " + ay3 + " to PQueue.")
          addTileToPQueue(ax3, ay3)
          remember(ir) = true
        }
      }
    }
    (0 until 4).foreach { ir =>
      println("[liek srsly man?] " + ir)
      ax3 = ux + cl(ir)(0)
      ay3 = uy + cl(ir)(1)
      println("(rpbRecur2) " + ax3 + " " + ay3 + " " + power(lyr)(ay3)(ax3))
      lazy val userBlock   = blocks(lyr)(uy)(ux)
      lazy val userBlockId = userBlock.id
      if (ay3 >= 0 && ay3 < HEIGHT && power(lyr)(ay3)(ax3) =/= 0) {
        println("(rbpRecur) " + power(lyr)(ay3)(ax3) + " " + power(lyr)(uy)(ux) + " " + conducts(userBlockId))
        if ((power(lyr)(ay3)(ax3) === power(lyr)(uy)(ux) - conducts(userBlockId).toFloat) &&
            (!(blocks(lyr)(ay3)(ax3).id >= ZythiumAmplifierRightBlockType.id && blocks(lyr)(ay3)(ax3).id <= ZythiumAmplifierUpOnBlockType.id || blocks(
              lyr)(ay3)(ax3).id >= ZythiumInverterRightBlockType.id && blocks(lyr)(ay3)(ax3).id <= ZythiumInverterUpOnBlockType.id) ||
            !(userBlockId >= ZythiumAmplifierRightBlockType.id && userBlockId <= ZythiumAmplifierUpOnBlockType.id && ux < ax3 && userBlock =/= ZythiumAmplifierRightBlockType && userBlock =/= ZythiumAmplifierRightOnBlockType ||
              userBlockId >= ZythiumAmplifierRightBlockType.id && userBlockId <= ZythiumAmplifierUpOnBlockType.id && uy < ay3 && userBlock =/= ZythiumAmplifierDownBlockType && userBlock =/= ZythiumAmplifierDownOnBlockType ||
              userBlockId >= ZythiumAmplifierRightBlockType.id && userBlockId <= ZythiumAmplifierUpOnBlockType.id && ux > ax3 && userBlock =/= ZythiumAmplifierLeftBlockType && userBlock =/= ZythiumAmplifierLeftOnBlockType ||
              userBlockId >= ZythiumAmplifierRightBlockType.id && userBlockId <= ZythiumAmplifierUpOnBlockType.id && uy > ay3 && userBlock =/= ZythiumAmplifierUpBlockType && userBlock =/= ZythiumAmplifierUpOnBlockType) &&
            !(userBlockId >= ZythiumInverterRightBlockType.id && userBlockId <= ZythiumInverterUpOnBlockType.id && ux < ax3 && userBlock =/= ZythiumInverterRightBlockType && userBlock =/= ZythiumInverterRightOnBlockType ||
              userBlockId >= ZythiumInverterRightBlockType.id && userBlockId <= ZythiumInverterUpOnBlockType.id && uy < ay3 && userBlock =/= ZythiumInverterDownBlockType && userBlock =/= ZythiumInverterDownOnBlockType ||
              userBlockId >= ZythiumInverterRightBlockType.id && userBlockId <= ZythiumInverterUpOnBlockType.id && ux > ax3 && userBlock =/= ZythiumInverterLeftBlockType && userBlock =/= ZythiumInverterLeftOnBlockType ||
              userBlockId >= ZythiumInverterRightBlockType.id && userBlockId <= ZythiumInverterUpOnBlockType.id && uy > ay3 && userBlock =/= ZythiumInverterUpBlockType && userBlock =/= ZythiumInverterUpOnBlockType) &&
            !(userBlockId >= ZythiumDelayer1DelayRightBlockType.id && userBlockId <= ZythiumDelayer8DelayUpOnBlockType.id && ux < ax3 && userBlock =/= ZythiumDelayer1DelayRightBlockType && userBlock =/= ZythiumDelayer1DelayRightOnBlockType && userBlock =/= ZythiumDelayer2DelayRightBlockType && userBlock =/= ZythiumDelayer2DelayRightOnBlockType && userBlock =/= ZythiumDelayer4DelayRightBlockType && userBlock =/= ZythiumDelayer4DelayRightOnBlockType && userBlock =/= ZythiumDelayer8DelayRightBlockType && userBlock =/= ZythiumDelayer8DelayRightOnBlockType ||
              userBlockId >= ZythiumDelayer1DelayRightBlockType.id && userBlockId <= ZythiumDelayer8DelayUpOnBlockType.id && uy < ay3 && userBlock =/= ZythiumDelayer1DelayDownBlockType && userBlock =/= ZythiumDelayer1DelayDownOnBlockType && userBlock =/= ZythiumDelayer2DelayDownBlockType && userBlock =/= ZythiumDelayer2DelayDownOnBlockType && userBlock =/= ZythiumDelayer4DelayDownBlockType && userBlock =/= ZythiumDelayer4DelayDownOnBlockType && userBlock =/= ZythiumDelayer8DelayDownBlockType && userBlock =/= ZythiumDelayer8DelayDownOnBlockType ||
              userBlockId >= ZythiumDelayer1DelayRightBlockType.id && userBlockId <= ZythiumDelayer8DelayUpOnBlockType.id && ux > ax3 && userBlock =/= ZythiumDelayer1DelayLeftBlockType && userBlock =/= ZythiumDelayer1DelayLeftOnBlockType && userBlock =/= ZythiumDelayer2DelayLeftBlockType && userBlock =/= ZythiumDelayer2DelayLeftOnBlockType && userBlock =/= ZythiumDelayer4DelayLeftBlockType && userBlock =/= ZythiumDelayer4DelayLeftOnBlockType && userBlock =/= ZythiumDelayer8DelayLeftBlockType && userBlock =/= ZythiumDelayer8DelayLeftOnBlockType ||
              userBlockId >= ZythiumDelayer1DelayRightBlockType.id && userBlockId <= ZythiumDelayer8DelayUpOnBlockType.id && uy > ay3 && userBlock =/= ZythiumDelayer1DelayUpBlockType && userBlock =/= ZythiumDelayer1DelayUpOnBlockType && userBlock =/= ZythiumDelayer2DelayUpBlockType && userBlock =/= ZythiumDelayer2DelayUpOnBlockType && userBlock =/= ZythiumDelayer4DelayUpBlockType && userBlock =/= ZythiumDelayer4DelayUpOnBlockType && userBlock =/= ZythiumDelayer8DelayUpBlockType && userBlock =/= ZythiumDelayer8DelayUpOnBlockType))) {
          if (!arbprd(lyr)(ay3)(ax3)) {
            rbpRecur(ax3, ay3, lyr)
            if (conducts(blocks(lyr)(ay3)(ax3).id) >= 0 && wcnct(ay3)(ax3)) {
              if (lyr === 0) {
                if (receives(blocks(1)(ay3)(ax3).id)) {
                  rbpRecur(ax3, ay3, 1)
                  if (powers(blocks(1)(ay3)(ax3))) {
                    addTileToPQueue(ax3, ay3)
                  }
                }
                if (receives(blocks(2)(ay3)(ax3).id)) {
                  rbpRecur(ax3, ay3, 2)
                  if (powers(blocks(2)(ay3)(ax3))) {
                    addTileToPQueue(ax3, ay3)
                  }
                }
              }
              if (lyr === 1) {
                if (receives(blocks(0)(ay3)(ax3).id)) {
                  rbpRecur(ax3, ay3, 0)
                  if (powers(blocks(0)(ay3)(ax3))) {
                    addTileToPQueue(ax3, ay3)
                  }
                }
                if (receives(blocks(2)(ay3)(ax3).id)) {
                  rbpRecur(ax3, ay3, 2)
                  if (powers(blocks(2)(ay3)(ax3))) {
                    addTileToPQueue(ax3, ay3)
                  }
                }
              }
              if (lyr === 2) {
                if (receives(blocks(0)(ay3)(ax3).id)) {
                  rbpRecur(ax3, ay3, 0)
                  if (powers(blocks(0)(ay3)(ax3))) {
                    addTileToPQueue(ax3, ay3)
                  }
                }
                if (receives(blocks(1)(ay3)(ax3).id)) {
                  rbpRecur(ax3, ay3, 1)
                  if (powers(blocks(1)(ay3)(ax3))) {
                    addTileToPQueue(ax3, ay3)
                  }
                }
              }
            }
          }
        }
      }
      lazy val block   = blocks(lyr)(ay3)(ax3)
      lazy val blockId = block.id
      if (block === ZythiumLampOnBlockType ||
        (
          blockId >= ZythiumAmplifierRightBlockType.id &&
          blockId <= ZythiumAmplifierUpOnBlockType.id
          ||
          blockId >= ZythiumInverterRightBlockType.id &&
          blockId <= ZythiumInverterUpOnBlockType.id
          ||
          blockId >= ZythiumDelayer1DelayRightBlockType.id
          && blockId <= ZythiumDelayer8DelayUpOnBlockType.id
        )
          &&
          !(
            blockId >= ZythiumAmplifierRightBlockType.id &&
            blockId <= ZythiumAmplifierUpOnBlockType.id &&
            ux < ax3 &&
            block =/= ZythiumAmplifierRightBlockType &&
            block =/= ZythiumAmplifierRightOnBlockType
            ||
            blockId >= ZythiumAmplifierRightBlockType.id &&
            blockId <= ZythiumAmplifierUpOnBlockType.id &&
            uy < ay3 &&
            block =/= ZythiumAmplifierDownBlockType &&
            block =/= ZythiumAmplifierDownOnBlockType
            ||
            blockId >= ZythiumAmplifierRightBlockType.id &&
            blockId <= ZythiumAmplifierUpOnBlockType.id &&
            ux > ax3 &&
            block =/= ZythiumAmplifierLeftBlockType &&
            block =/= ZythiumAmplifierLeftOnBlockType
            ||
            blockId >= ZythiumAmplifierRightBlockType.id &&
            blockId <= ZythiumAmplifierUpOnBlockType.id &&
            uy > ay3 &&
            block =/= ZythiumAmplifierUpBlockType &&
            block =/= ZythiumAmplifierUpOnBlockType
            )
          &&
          !(
            blockId >= ZythiumInverterRightBlockType.id &&
            blockId <= ZythiumInverterUpOnBlockType.id &&
            ux < ax3 &&
            block =/= ZythiumInverterRightBlockType &&
            block =/= ZythiumInverterRightOnBlockType
            ||
            blockId >= ZythiumInverterRightBlockType.id &&
            blockId <= ZythiumInverterUpOnBlockType.id &&
            uy < ay3 &&
            block =/= ZythiumInverterDownBlockType &&
            block =/= ZythiumInverterDownOnBlockType
            ||
            blockId >= ZythiumInverterRightBlockType.id &&
            blockId <= ZythiumInverterUpOnBlockType.id &&
            ux > ax3 &&
            block =/= ZythiumInverterLeftBlockType &&
            block =/= ZythiumInverterLeftOnBlockType
            ||
            blockId >= ZythiumInverterRightBlockType.id &&
            blockId <= ZythiumInverterUpOnBlockType.id &&
            uy > ay3 &&
            block =/= ZythiumInverterUpBlockType &&
            block =/= ZythiumInverterUpOnBlockType
          )
          &&
          !(
            blockId >= ZythiumDelayer1DelayRightBlockType.id &&
            blockId <= ZythiumDelayer8DelayUpOnBlockType.id &&
            ux < ax3 &&
            block =/= ZythiumDelayer1DelayRightBlockType &&
            block =/= ZythiumDelayer1DelayRightOnBlockType &&
            block =/= ZythiumDelayer2DelayRightBlockType &&
            block =/= ZythiumDelayer2DelayRightOnBlockType &&
            block =/= ZythiumDelayer4DelayRightBlockType &&
            block =/= ZythiumDelayer4DelayRightOnBlockType &&
            block =/= ZythiumDelayer8DelayRightBlockType &&
            block =/= ZythiumDelayer8DelayRightOnBlockType
            ||
            blockId >= ZythiumDelayer1DelayRightBlockType.id &&
            blockId <= ZythiumDelayer8DelayUpOnBlockType.id &&
            uy < ay3 &&
            block =/= ZythiumDelayer1DelayDownBlockType &&
            block =/= ZythiumDelayer1DelayDownOnBlockType &&
            block =/= ZythiumDelayer2DelayDownBlockType &&
            block =/= ZythiumDelayer2DelayDownOnBlockType &&
            block =/= ZythiumDelayer4DelayDownBlockType &&
            block =/= ZythiumDelayer4DelayDownOnBlockType &&
            block =/= ZythiumDelayer8DelayDownBlockType &&
            block =/= ZythiumDelayer8DelayDownOnBlockType
            ||
            blockId >= ZythiumDelayer1DelayRightBlockType.id &&
            blockId <= ZythiumDelayer8DelayUpOnBlockType.id &&
            ux > ax3 &&
            block =/= ZythiumDelayer1DelayLeftBlockType &&
            block =/= ZythiumDelayer1DelayLeftOnBlockType &&
            block =/= ZythiumDelayer2DelayLeftBlockType &&
            block =/= ZythiumDelayer2DelayLeftOnBlockType &&
            block =/= ZythiumDelayer4DelayLeftBlockType &&
            block =/= ZythiumDelayer4DelayLeftOnBlockType &&
            block =/= ZythiumDelayer8DelayLeftBlockType &&
            block =/= ZythiumDelayer8DelayLeftOnBlockType
            ||
            blockId >= ZythiumDelayer1DelayRightBlockType.id &&
            blockId <= ZythiumDelayer8DelayUpOnBlockType.id &&
            uy > ay3 &&
            block =/= ZythiumDelayer1DelayUpBlockType &&
            block =/= ZythiumDelayer1DelayUpOnBlockType &&
            block =/= ZythiumDelayer2DelayUpBlockType &&
            block =/= ZythiumDelayer2DelayUpOnBlockType &&
            block =/= ZythiumDelayer4DelayUpBlockType &&
            block =/= ZythiumDelayer4DelayUpOnBlockType &&
            block =/= ZythiumDelayer8DelayUpBlockType &&
            block =/= ZythiumDelayer8DelayUpOnBlockType)) {
        if (blockId >= ZythiumInverterRightOnBlockType.id && blockId <= ZythiumInverterUpOnBlockType.id) {
          blocks(lyr)(ay3)(ax3) = BlockType.lookupById(blockId - 4)
          println("Adding power for inverter at (" + ax3 + ", " + ay3 + ").")
          addBlockPower(ax3, ay3)
          addBlockLighting(ax3, ay3)
          rdrawn(ay3)(ax3) = false
        }
        removeBlockPower(ax3, ay3, lyr)
      }
    }
    (0 until 4).foreach { ir =>
      if (remember(ir) && uy + cl(ir)(1) >= 0 && uy + cl(ir)(1) < HEIGHT) {
        power(lyr)(uy + cl(ir)(1))(ux + cl(ir)(0)) = 5.toFloat
      }
    }
    power(lyr)(uy)(ux) = 0.toFloat
    arbprd(lyr)(uy)(ux) = false
  }

  def removeBlockPower(ux: Int, uy: Int, lyr: Int): Unit = {
    removeBlockPower(ux, uy, lyr, true)
  }

  def removeBlockPower(ux: Int, uy: Int, lyr: Int, turnOffDelayer: Boolean): Unit = {
    arbprd(lyr)(uy)(ux) = true
    println("[rbp ] " + ux + " " + uy + " " + lyr + " " + turnOffDelayer)
    if (!((blocks(lyr)(uy)(ux).id >= ZythiumDelayer1DelayRightOnBlockType.id && blocks(lyr)(uy)(ux).id <= ZythiumDelayer1DelayUpOnBlockType.id || blocks(
          lyr)(uy)(ux).id >= ZythiumDelayer2DelayRightOnBlockType.id && blocks(lyr)(uy)(ux).id <= ZythiumDelayer2DelayUpOnBlockType.id || blocks(
          lyr)(uy)(ux).id >= ZythiumDelayer4DelayRightOnBlockType.id && blocks(lyr)(uy)(ux).id <= ZythiumDelayer4DelayUpOnBlockType.id || blocks(
          lyr)(uy)(ux).id >= ZythiumDelayer8DelayRightOnBlockType.id && blocks(lyr)(uy)(ux).id <= ZythiumDelayer8DelayUpOnBlockType.id) && turnOffDelayer)) {
      var ax3, ay3: Int = 0
      (0 until 4).foreach { ir =>
        ax3 = ux + cl(ir)(0)
        ay3 = uy + cl(ir)(1)
        lazy val block   = blocks(lyr)(ay3)(ax3)
        lazy val blockId = block.id
        if (ay3 >= 0 && ay3 < HEIGHT && power(lyr)(ay3)(ax3) =/= 0) {
          if (!(power(lyr)(ay3)(ax3) === power(lyr)(uy)(ux) - conducts(blocks(lyr)(uy)(ux).id).toFloat) &&
              (!(blockId >= ZythiumAmplifierRightBlockType.id && blockId <= ZythiumAmplifierUpOnBlockType.id || blockId >= ZythiumInverterRightBlockType.id && blockId <= ZythiumInverterUpOnBlockType.id) ||
              !(blockId >= ZythiumAmplifierRightBlockType.id && blockId <= ZythiumAmplifierUpOnBlockType.id && ux > ax3 && block =/= ZythiumAmplifierRightBlockType && block =/= ZythiumAmplifierRightOnBlockType ||
                blockId >= ZythiumAmplifierRightBlockType.id && blockId <= ZythiumAmplifierUpOnBlockType.id && uy > ay3 && block =/= ZythiumAmplifierDownBlockType && block =/= ZythiumAmplifierDownOnBlockType ||
                blockId >= ZythiumAmplifierRightBlockType.id && blockId <= ZythiumAmplifierUpOnBlockType.id && ux < ax3 && block =/= ZythiumAmplifierLeftBlockType && block =/= ZythiumAmplifierLeftOnBlockType ||
                blockId >= ZythiumAmplifierRightBlockType.id && blockId <= ZythiumAmplifierUpOnBlockType.id && uy < ay3 && block =/= ZythiumAmplifierUpBlockType && block =/= ZythiumAmplifierUpOnBlockType) &&
              !(blockId >= ZythiumInverterRightBlockType.id && blockId <= ZythiumInverterUpOnBlockType.id && ux > ax3 && block =/= ZythiumInverterRightBlockType && block =/= ZythiumInverterRightOnBlockType ||
                blockId >= ZythiumInverterRightBlockType.id && blockId <= ZythiumInverterUpOnBlockType.id && uy > ay3 && block =/= ZythiumInverterDownBlockType && block =/= ZythiumInverterDownOnBlockType ||
                blockId >= ZythiumInverterRightBlockType.id && blockId <= ZythiumInverterUpOnBlockType.id && ux < ax3 && block =/= ZythiumInverterLeftBlockType && block =/= ZythiumInverterLeftOnBlockType ||
                blockId >= ZythiumInverterRightBlockType.id && blockId <= ZythiumInverterUpOnBlockType.id && uy < ay3 && block =/= ZythiumInverterUpBlockType && block =/= ZythiumInverterUpOnBlockType) &&
              !(blockId >= ZythiumDelayer1DelayRightBlockType.id && blockId <= ZythiumDelayer8DelayUpOnBlockType.id && ux > ax3 && block =/= ZythiumDelayer1DelayRightBlockType && block =/= ZythiumDelayer1DelayRightOnBlockType && block =/= ZythiumDelayer2DelayRightBlockType && block =/= ZythiumDelayer2DelayRightOnBlockType && block =/= ZythiumDelayer4DelayRightBlockType && block =/= ZythiumDelayer4DelayRightOnBlockType && block =/= ZythiumDelayer8DelayRightBlockType && block =/= ZythiumDelayer8DelayRightOnBlockType ||
                blockId >= ZythiumDelayer1DelayRightBlockType.id && blockId <= ZythiumDelayer8DelayUpOnBlockType.id && uy > ay3 && block =/= ZythiumDelayer1DelayDownBlockType && block =/= ZythiumDelayer1DelayDownOnBlockType && block =/= ZythiumDelayer2DelayDownBlockType && block =/= ZythiumDelayer2DelayDownOnBlockType && block =/= ZythiumDelayer4DelayDownBlockType && block =/= ZythiumDelayer4DelayDownOnBlockType && block =/= ZythiumDelayer8DelayDownBlockType && block =/= ZythiumDelayer8DelayDownOnBlockType ||
                blockId >= ZythiumDelayer1DelayRightBlockType.id && blockId <= ZythiumDelayer8DelayUpOnBlockType.id && ux < ax3 && block =/= ZythiumDelayer1DelayLeftBlockType && block =/= ZythiumDelayer1DelayLeftOnBlockType && block =/= ZythiumDelayer2DelayLeftBlockType && block =/= ZythiumDelayer2DelayLeftOnBlockType && block =/= ZythiumDelayer4DelayLeftBlockType && block =/= ZythiumDelayer4DelayLeftOnBlockType && block =/= ZythiumDelayer8DelayLeftBlockType && block =/= ZythiumDelayer8DelayLeftOnBlockType ||
                blockId >= ZythiumDelayer1DelayRightBlockType.id && blockId <= ZythiumDelayer8DelayUpOnBlockType.id && uy < ay3 && block =/= ZythiumDelayer1DelayUpBlockType && block =/= ZythiumDelayer1DelayUpOnBlockType && block =/= ZythiumDelayer2DelayUpBlockType && block =/= ZythiumDelayer2DelayUpOnBlockType && block =/= ZythiumDelayer4DelayUpBlockType && block =/= ZythiumDelayer4DelayUpOnBlockType && block =/= ZythiumDelayer8DelayUpBlockType && block =/= ZythiumDelayer8DelayUpOnBlockType))) {
            println("Added tile " + ax3 + " " + ay3 + " to PQueue.")
            addTileToPQueue(ax3, ay3)
          }
        }
      }
      (0 until 4).foreach { ir =>
        ax3 = ux + cl(ir)(0)
        ay3 = uy + cl(ir)(1)
        lazy val userBlock   = blocks(lyr)(uy)(ux)
        lazy val userBlockId = userBlock.id
        println(blocks(lyr)(ay3)(ax3) + " " + power(lyr)(ay3)(ax3))
        if (ay3 >= 0 && ay3 < HEIGHT && power(lyr)(ay3)(ax3) =/= 0) {
          println(power(lyr)(uy)(ux) + " " + power(lyr)(ay3)(ax3) + " " + conducts(userBlockId))
          if (power(lyr)(ay3)(ax3) === power(lyr)(uy)(ux) - conducts(userBlockId).toFloat) {
            if (!(blocks(lyr)(ay3)(ax3).id >= ZythiumAmplifierRightBlockType.id && blocks(lyr)(ay3)(ax3).id <= ZythiumAmplifierUpOnBlockType.id || blocks(
                  lyr)(ay3)(ax3).id >= ZythiumInverterRightBlockType.id && blocks(lyr)(ay3)(ax3).id <= ZythiumInverterUpOnBlockType.id) ||
                !(userBlockId >= ZythiumAmplifierRightBlockType.id && userBlockId <= ZythiumAmplifierUpOnBlockType.id && ux < ax3 && userBlock =/= ZythiumAmplifierRightBlockType && userBlock =/= ZythiumAmplifierRightOnBlockType ||
                  userBlockId >= ZythiumAmplifierRightBlockType.id && userBlockId <= ZythiumAmplifierUpOnBlockType.id && uy < ay3 && userBlock =/= ZythiumAmplifierDownBlockType && userBlock =/= ZythiumAmplifierDownOnBlockType ||
                  userBlockId >= ZythiumAmplifierRightBlockType.id && userBlockId <= ZythiumAmplifierUpOnBlockType.id && ux > ax3 && userBlock =/= ZythiumAmplifierLeftBlockType && userBlock =/= ZythiumAmplifierLeftOnBlockType ||
                  userBlockId >= ZythiumAmplifierRightBlockType.id && userBlockId <= ZythiumAmplifierUpOnBlockType.id && uy > ay3 && userBlock =/= ZythiumAmplifierUpBlockType && userBlock =/= ZythiumAmplifierUpOnBlockType) &&
                !(userBlockId >= ZythiumInverterRightBlockType.id && userBlockId <= ZythiumInverterUpOnBlockType.id && ux < ax3 && userBlock =/= ZythiumInverterRightBlockType && userBlock =/= ZythiumInverterRightOnBlockType ||
                  userBlockId >= ZythiumInverterRightBlockType.id && userBlockId <= ZythiumInverterUpOnBlockType.id && uy < ay3 && userBlock =/= ZythiumInverterDownBlockType && userBlock =/= ZythiumInverterDownOnBlockType ||
                  userBlockId >= ZythiumInverterRightBlockType.id && userBlockId <= ZythiumInverterUpOnBlockType.id && ux > ax3 && userBlock =/= ZythiumInverterLeftBlockType && userBlock =/= ZythiumInverterLeftOnBlockType ||
                  userBlockId >= ZythiumInverterRightBlockType.id && userBlockId <= ZythiumInverterUpOnBlockType.id && uy > ay3 && userBlock =/= ZythiumInverterUpBlockType && userBlock =/= ZythiumInverterUpOnBlockType) &&
                !(userBlockId >= ZythiumDelayer1DelayRightBlockType.id && userBlockId <= ZythiumDelayer8DelayUpOnBlockType.id && ux < ax3 && userBlock =/= ZythiumDelayer1DelayRightBlockType && userBlock =/= ZythiumDelayer1DelayRightOnBlockType && userBlock =/= ZythiumDelayer2DelayRightBlockType && userBlock =/= ZythiumDelayer2DelayRightOnBlockType && userBlock =/= ZythiumDelayer4DelayRightBlockType && userBlock =/= ZythiumDelayer4DelayRightOnBlockType && userBlock =/= ZythiumDelayer8DelayRightBlockType && userBlock =/= ZythiumDelayer8DelayRightOnBlockType ||
                  userBlockId >= ZythiumDelayer1DelayRightBlockType.id && userBlockId <= ZythiumDelayer8DelayUpOnBlockType.id && uy < ay3 && userBlock =/= ZythiumDelayer1DelayDownBlockType && userBlock =/= ZythiumDelayer1DelayDownOnBlockType && userBlock =/= ZythiumDelayer2DelayDownBlockType && userBlock =/= ZythiumDelayer2DelayDownOnBlockType && userBlock =/= ZythiumDelayer4DelayDownBlockType && userBlock =/= ZythiumDelayer4DelayDownOnBlockType && userBlock =/= ZythiumDelayer8DelayDownBlockType && userBlock =/= ZythiumDelayer8DelayDownOnBlockType ||
                  userBlockId >= ZythiumDelayer1DelayRightBlockType.id && userBlockId <= ZythiumDelayer8DelayUpOnBlockType.id && ux > ax3 && userBlock =/= ZythiumDelayer1DelayLeftBlockType && userBlock =/= ZythiumDelayer1DelayLeftOnBlockType && userBlock =/= ZythiumDelayer2DelayLeftBlockType && userBlock =/= ZythiumDelayer2DelayLeftOnBlockType && userBlock =/= ZythiumDelayer4DelayLeftBlockType && userBlock =/= ZythiumDelayer4DelayLeftOnBlockType && userBlock =/= ZythiumDelayer8DelayLeftBlockType && userBlock =/= ZythiumDelayer8DelayLeftOnBlockType ||
                  userBlockId >= ZythiumDelayer1DelayRightBlockType.id && userBlockId <= ZythiumDelayer8DelayUpOnBlockType.id && uy > ay3 && userBlock =/= ZythiumDelayer1DelayUpBlockType && userBlock =/= ZythiumDelayer1DelayUpOnBlockType && userBlock =/= ZythiumDelayer2DelayUpBlockType && userBlock =/= ZythiumDelayer2DelayUpOnBlockType && userBlock =/= ZythiumDelayer4DelayUpBlockType && userBlock =/= ZythiumDelayer4DelayUpOnBlockType && userBlock =/= ZythiumDelayer8DelayUpBlockType && userBlock =/= ZythiumDelayer8DelayUpOnBlockType)) {
              if (!arbprd(lyr)(ay3)(ax3)) {
                rbpRecur(ax3, ay3, lyr)
                if (conducts(blocks(lyr)(ay3)(ax3).id) >= 0 && wcnct(ay3)(ax3)) {
                  if (lyr === 0) {
                    if (receives(blocks(1)(ay3)(ax3).id)) {
                      rbpRecur(ax3, ay3, 1)
                      if (powers(blocks(1)(ay3)(ax3))) {
                        addTileToPQueue(ax3, ay3)
                      }
                    }
                    if (receives(blocks(2)(ay3)(ax3).id)) {
                      rbpRecur(ax3, ay3, 2)
                      if (powers(blocks(2)(ay3)(ax3))) {
                        addTileToPQueue(ax3, ay3)
                      }
                    }
                  }
                  if (lyr === 1) {
                    if (receives(blocks(0)(ay3)(ax3).id)) {
                      rbpRecur(ax3, ay3, 0)
                      if (powers(blocks(0)(ay3)(ax3))) {
                        addTileToPQueue(ax3, ay3)
                      }
                    }
                    if (receives(blocks(2)(ay3)(ax3).id)) {
                      rbpRecur(ax3, ay3, 2)
                      if (powers(blocks(2)(ay3)(ax3))) {
                        addTileToPQueue(ax3, ay3)
                      }
                    }
                  }
                  if (lyr === 2) {
                    if (receives(blocks(0)(ay3)(ax3).id)) {
                      rbpRecur(ax3, ay3, 0)
                      if (powers(blocks(0)(ay3)(ax3))) {
                        addTileToPQueue(ax3, ay3)
                      }
                    }
                    if (receives(blocks(1)(ay3)(ax3).id)) {
                      rbpRecur(ax3, ay3, 1)
                      if (powers(blocks(1)(ay3)(ax3))) {
                        addTileToPQueue(ax3, ay3)
                      }
                    }
                  }
                }
              }
            }
          }
        }
        lazy val block   = blocks(lyr)(ay3)(ax3)
        lazy val blockId = block.id
        if (block === ZythiumLampOnBlockType || (blockId >= ZythiumAmplifierRightBlockType.id && blockId <= ZythiumAmplifierUpOnBlockType.id || blockId >= ZythiumInverterRightBlockType.id && blockId <= ZythiumInverterUpOnBlockType.id || blockId >= ZythiumDelayer1DelayRightBlockType.id && blockId <= ZythiumDelayer8DelayUpOnBlockType.id) &&
            !(blockId >= ZythiumAmplifierRightBlockType.id && blockId <= ZythiumAmplifierUpOnBlockType.id && ux < ax3 && block =/= ZythiumAmplifierRightBlockType && block =/= ZythiumAmplifierRightOnBlockType ||
              blockId >= ZythiumAmplifierRightBlockType.id && blockId <= ZythiumAmplifierUpOnBlockType.id && uy < ay3 && block =/= ZythiumAmplifierDownBlockType && block =/= ZythiumAmplifierDownOnBlockType ||
              blockId >= ZythiumAmplifierRightBlockType.id && blockId <= ZythiumAmplifierUpOnBlockType.id && ux > ax3 && block =/= ZythiumAmplifierLeftBlockType && block =/= ZythiumAmplifierLeftOnBlockType ||
              blockId >= ZythiumAmplifierRightBlockType.id && blockId <= ZythiumAmplifierUpOnBlockType.id && uy > ay3 && block =/= ZythiumAmplifierUpBlockType && block =/= ZythiumAmplifierUpOnBlockType) &&
            !(blockId >= ZythiumInverterRightBlockType.id && blockId <= ZythiumInverterUpOnBlockType.id && ux < ax3 && block =/= ZythiumInverterRightBlockType && block =/= ZythiumInverterRightOnBlockType ||
              blockId >= ZythiumInverterRightBlockType.id && blockId <= ZythiumInverterUpOnBlockType.id && uy < ay3 && block =/= ZythiumInverterDownBlockType && block =/= ZythiumInverterDownOnBlockType ||
              blockId >= ZythiumInverterRightBlockType.id && blockId <= ZythiumInverterUpOnBlockType.id && ux > ax3 && block =/= ZythiumInverterLeftBlockType && block =/= ZythiumInverterLeftOnBlockType ||
              blockId >= ZythiumInverterRightBlockType.id && blockId <= ZythiumInverterUpOnBlockType.id && uy > ay3 && block =/= ZythiumInverterUpBlockType && block =/= ZythiumInverterUpOnBlockType) &&
            !(blockId >= ZythiumDelayer1DelayRightBlockType.id && blockId <= ZythiumDelayer8DelayUpOnBlockType.id && ux < ax3 && block =/= ZythiumDelayer1DelayRightBlockType && block =/= ZythiumDelayer1DelayRightOnBlockType && block =/= ZythiumDelayer2DelayRightBlockType && block =/= ZythiumDelayer2DelayRightOnBlockType && block =/= ZythiumDelayer4DelayRightBlockType && block =/= ZythiumDelayer4DelayRightOnBlockType && block =/= ZythiumDelayer8DelayRightBlockType && block =/= ZythiumDelayer8DelayRightOnBlockType ||
              blockId >= ZythiumDelayer1DelayRightBlockType.id && blockId <= ZythiumDelayer8DelayUpOnBlockType.id && uy < ay3 && block =/= ZythiumDelayer1DelayDownBlockType && block =/= ZythiumDelayer1DelayDownOnBlockType && block =/= ZythiumDelayer2DelayDownBlockType && block =/= ZythiumDelayer2DelayDownOnBlockType && block =/= ZythiumDelayer4DelayDownBlockType && block =/= ZythiumDelayer4DelayDownOnBlockType && block =/= ZythiumDelayer8DelayDownBlockType && block =/= ZythiumDelayer8DelayDownOnBlockType ||
              blockId >= ZythiumDelayer1DelayRightBlockType.id && blockId <= ZythiumDelayer8DelayUpOnBlockType.id && ux > ax3 && block =/= ZythiumDelayer1DelayLeftBlockType && block =/= ZythiumDelayer1DelayLeftOnBlockType && block =/= ZythiumDelayer2DelayLeftBlockType && block =/= ZythiumDelayer2DelayLeftOnBlockType && block =/= ZythiumDelayer4DelayLeftBlockType && block =/= ZythiumDelayer4DelayLeftOnBlockType && block =/= ZythiumDelayer8DelayLeftBlockType && block =/= ZythiumDelayer8DelayLeftOnBlockType ||
              blockId >= ZythiumDelayer1DelayRightBlockType.id && blockId <= ZythiumDelayer8DelayUpOnBlockType.id && uy > ay3 && block =/= ZythiumDelayer1DelayUpBlockType && block =/= ZythiumDelayer1DelayUpOnBlockType && block =/= ZythiumDelayer2DelayUpBlockType && block =/= ZythiumDelayer2DelayUpOnBlockType && block =/= ZythiumDelayer4DelayUpBlockType && block =/= ZythiumDelayer4DelayUpOnBlockType && block =/= ZythiumDelayer8DelayUpBlockType && block =/= ZythiumDelayer8DelayUpOnBlockType)) {
          if (blockId >= ZythiumInverterRightOnBlockType.id && blockId <= ZythiumInverterUpOnBlockType.id) {
            blocks(lyr)(ay3)(ax3) = BlockType.lookupById(blockId - 4)
            println("Adding power for inverter at (" + ax3 + ", " + ay3 + ").")
            addBlockPower(ax3, ay3)
            addBlockLighting(ax3, ay3)
            rdrawn(ay3)(ax3) = false
          }
          arbprd(lyr)(uy)(ux) = false
          removeBlockPower(ax3, ay3, lyr)
        }
      }
    }
    if (blocks(lyr)(uy)(ux) === ZythiumLampOnBlockType) {
      removeBlockLighting(ux, uy)
      blocks(lyr)(uy)(ux) = ZythiumLampBlockType
      rdrawn(uy)(ux) = false
    }
    if (blocks(lyr)(uy)(ux).id >= ZythiumAmplifierRightOnBlockType.id && blocks(lyr)(uy)(ux).id <= ZythiumAmplifierUpOnBlockType.id) {
      blockTemp = blocks(lyr)(uy)(ux)
      blocks(lyr)(uy)(ux) = BlockType.lookupById(blocks(lyr)(uy)(ux).id - 4)
      removeBlockPower(ux, uy, lyr)
      removeBlockLighting(ux, uy)
      rdrawn(uy)(ux) = false
    }
    if (turnOffDelayer && blocks(lyr)(uy)(ux).id >= ZythiumDelayer1DelayRightBlockType.id && blocks(lyr)(uy)(ux).id <= ZythiumDelayer8DelayUpOnBlockType.id) {
      println("???")
      updatex += ux
      updatey += uy
      DDELAY.get(blocks(lyr)(uy)(ux).id).foreach(updatet.+=)
      updatel += lyr
    }
    if (!((blocks(lyr)(uy)(ux).id >= ZythiumDelayer1DelayRightOnBlockType.id && blocks(lyr)(uy)(ux).id <= ZythiumDelayer1DelayUpOnBlockType.id || blocks(
          lyr)(uy)(ux).id >= ZythiumDelayer2DelayRightOnBlockType.id && blocks(lyr)(uy)(ux).id <= ZythiumDelayer2DelayUpOnBlockType.id || blocks(
          lyr)(uy)(ux).id >= ZythiumDelayer4DelayRightOnBlockType.id && blocks(lyr)(uy)(ux).id <= ZythiumDelayer4DelayUpOnBlockType.id || blocks(
          lyr)(uy)(ux).id >= ZythiumDelayer8DelayRightOnBlockType.id && blocks(lyr)(uy)(ux).id <= ZythiumDelayer8DelayUpOnBlockType.id) && turnOffDelayer)) {
      power(lyr)(uy)(ux) = 0.toFloat
    }
    arbprd(lyr)(uy)(ux) = false
  }

  def redoBlockLighting(ux: Int, uy: Int): Unit = {
    (-BRIGHTEST until BRIGHTEST + 1).foreach { ax =>
      (-BRIGHTEST until BRIGHTEST + 1).foreach { ay =>
        if (abs(ax) + abs(ay) <= BRIGHTEST && uy + ay >= 0 && uy + ay < HEIGHT) {
          addTileToZQueue(ux + ax, uy + ay)
          lights(uy + ay)(ux + ax) = 0.toFloat
        }
      }
    }
    (-BRIGHTEST * 2 until BRIGHTEST * 2 + 1).foreach { ax =>
      (-BRIGHTEST * 2 until BRIGHTEST * 2 + 1).foreach { ay =>
        if (abs(ax) + abs(ay) <= BRIGHTEST * 2 && uy + ay >= 0 && uy + ay < HEIGHT) {
          if (lsources(uy + ay)(ux + ax)) {
            addTileToQueue(ux + ax, uy + ay)
          }
        }
      }
    }
  }

  def redoBlockPower(ux: Int, uy: Int, lyr: Int): Unit = {
    if (powers(blocks(lyr)(uy)(ux)) || blocks(lyr)(uy)(ux).id >= ZythiumWireBlockType.id && blocks(lyr)(uy)(ux).id <= ZythiumWire5PowerBlockType.id) {
      addAdjacentTilesToPQueue(ux, uy)
    } else {
      removeBlockPower(ux, uy, lyr)
    }
  }

  def addSunLighting(ux: Int, uy: Int): Unit = { // And including
    (0 until uy).foreach { y =>
      if (ltrans(blocks(1)(y)(ux).id)) {
        return
      }
    }
    addSources = false
    (uy until HEIGHT - 1).foreach { y =>
      if (ltrans(blocks(1)(y + 1)(ux - 1).id) || ltrans(blocks(1)(y + 1)(ux + 1).id)) {
        addSources = true
      }
      if (addSources) {
        addTileToQueue(ux, y)
      }
      if (ltrans(blocks(1)(y)(ux).id)) {
        return
      }
      addTileToZQueue(ux, y)
      lights(y)(ux) = sunlightlevel.toFloat
      lsources(y)(ux) = true
    }
  }

  def removeSunLighting(ux: Int, uy: Int): Unit = { // And including
    n = sunlightlevel
    (0 until uy).foreach { y =>
      if (ltrans(blocks(1)(y)(ux).id)) {
        return
      }
    }
    import scala.util.control.Breaks._
    breakable {
      (uy until HEIGHT).foreach { y =>
        lsources(y)(ux) = isBlockLightSource(ux, y)
        if (y =/= uy && ltrans(blocks(1)(y)(ux).id)) {
          break
        }
      }
    }
    (-n until n + 1).foreach { ax =>
      (-n until n + (y - uy) + 1).foreach { ay =>
        if (uy + ay >= 0 && uy + ay < WIDTH) {
          addTileToZQueue(ux + ax, uy + ay)
          lights(uy + ay)(ux + ax) = 0.toFloat
        }
      }
    }
    (-n - BRIGHTEST until n + 1 + BRIGHTEST).foreach { ax =>
      (-n - BRIGHTEST until n + (y - uy) + 1 + BRIGHTEST).foreach { ay =>
        if (uy + ay >= 0 && uy + ay < HEIGHT) {
          if (lsources(uy + ay)(ux + ax)) {
            addTileToQueue(ux + ax, uy + ay)
          }
        }
      }
    }
  }

  def isReachedBySunlight(ux: Int, uy: Int): Boolean = {
    (0 until uy + 1).foreach { ay =>
      if (ltrans(blocks(1)(ay)(ux).id)) {
        return false
      }
    }
    true
  }

  def isBlockLightSource(ux: Int, uy: Int): Boolean = {
    blocks(0)(uy)(ux) =/= AirBlockType && BLOCKLIGHTS.get(blocks(0)(uy)(ux).id).exists(_ =/= 0) ||
    blocks(1)(uy)(ux) =/= AirBlockType && BLOCKLIGHTS.get(blocks(1)(uy)(ux).id).exists(_ =/= 0) ||
    blocks(2)(uy)(ux) =/= AirBlockType && BLOCKLIGHTS.get(blocks(2)(uy)(ux).id).exists(_ =/= 0)
  }

  def isNonLayeredBlockLightSource(ux: Int, uy: Int): Boolean = {
    isNonLayeredBlockLightSource(ux, uy, layer)
  }

  def isNonLayeredBlockLightSource(ux: Int, uy: Int, layer: Int): Boolean = {
    layer =/= 0 && blocks(0)(uy)(ux) =/= AirBlockType && BLOCKLIGHTS.get(blocks(0)(uy)(ux).id).exists(_ =/= 0) ||
    layer =/= 1 && blocks(1)(uy)(ux) =/= AirBlockType && BLOCKLIGHTS.get(blocks(1)(uy)(ux).id).exists(_ =/= 0) ||
    layer =/= 2 && blocks(2)(uy)(ux) =/= AirBlockType && BLOCKLIGHTS.get(blocks(2)(uy)(ux).id).exists(_ =/= 0)
  }

  def findBlockLightSource(ux: Int, uy: Int): Int = {
    n = 0
    if (blocks(0)(uy)(ux) =/= AirBlockType) n = BLOCKLIGHTS.get(blocks(0)(uy)(ux).id).map(max(_, n)).getOrElse(0)
    if (blocks(1)(uy)(ux) =/= AirBlockType) n = BLOCKLIGHTS.get(blocks(1)(uy)(ux).id).map(max(_, n)).getOrElse(0)
    if (blocks(2)(uy)(ux) =/= AirBlockType) n = BLOCKLIGHTS.get(blocks(2)(uy)(ux).id).map(max(_, n)).getOrElse(0)
    n
  }

  def findNonLayeredBlockLightSource(ux: Int, uy: Int): Int = {
    n = 0
    if (blocks(0)(uy)(ux) =/= AirBlockType) n = BLOCKLIGHTS.get(blocks(0)(uy)(ux).id).map(max(_, n)).getOrElse(0)
    if (blocks(1)(uy)(ux) =/= AirBlockType) n = BLOCKLIGHTS.get(blocks(1)(uy)(ux).id).map(max(_, n)).getOrElse(0)
    if (blocks(2)(uy)(ux) =/= AirBlockType) n = BLOCKLIGHTS.get(blocks(2)(uy)(ux).id).map(max(_, n)).getOrElse(0)
    n
  }

  def addTileToQueue(ux: Int, uy: Int): Unit = {
    if (!lqd(uy)(ux)) {
      lqx += ux
      lqy += uy
      lqd(uy)(ux) = true
    }
  }

  def addTileToZQueue(ux: Int, uy: Int): Unit = {
    if (!zqd(uy)(ux)) {
      zqx += ux
      zqy += uy
      zqn(uy)(ux) = lights(uy)(ux).toByte
      zqd(uy)(ux) = true
    }
  }

  def addTileToPQueue(ux: Int, uy: Int): Unit = {
    if (!pqd(uy)(ux)) {
      pqx += ux
      pqy += uy
      pqd(uy)(ux) = true
    }
  }

  def addAdjacentTilesToPQueue(ux: Int, uy: Int): Unit = {
    (0 until 4).foreach { i2 =>
      if (uy + cl(i2)(1) >= 0 && uy + cl(i2)(1) < HEIGHT) {
        addTileToPQueue(ux + cl(i2)(0), uy + cl(i2)(1))
      }
    }
  }

  def addAdjacentTilesToPQueueConditionally(ux: Int, uy: Int): Unit = {
    (0 until 4).foreach { i2 =>
      (0 until 3).foreach { l =>
        if (uy + cl(i2)(1) >= 0 && uy + cl(i2)(1) < HEIGHT && power(l)(uy + cl(i2)(1))(ux + cl(i2)(0)) > 0) {
          addTileToPQueue(ux + cl(i2)(0), uy + cl(i2)(1))
        }
      }
    }
  }

  def addTileToPZQueue(ux: Int, uy: Int): Unit = {
    if (!pzqd(uy)(ux)) {
      pzqx += ux
      pzqy += uy
      pzqn(0)(uy)(ux) = power(0)(uy)(ux).toByte
      pzqn(1)(uy)(ux) = power(1)(uy)(ux).toByte
      pzqn(2)(uy)(ux) = power(2)(uy)(ux).toByte
      pzqd(uy)(ux) = true
    }
  }

  def resolveLightMatrix(): Unit = {
    try {
      while (true) {
        x = lqx(0)
        y = lqy(0)
        if (lsources(y)(x)) {
          n = findBlockLightSource(x, y)
          if (isReachedBySunlight(x, y)) {
            lights(y)(x) = mh.max(lights(y)(x), n.toFloat, sunlightlevel.toFloat)
          } else {
            lights(y)(x) = max(lights(y)(x), n.toFloat)
          }
          addTileToZQueue(x, y)
        }
        (0 until 4).foreach { i =>
          x2 = x + cl(i)(0)
          y2 = y + cl(i)(1)
          if (y2 >= 0 && y2 < HEIGHT) {
            if (!ltrans(blocks(1)(y2)(x2).id)) {
              if (lights(y2)(x2) <= lights(y)(x) - 1.0f) {
                addTileToZQueue(x2, y2)
                lights(y2)(x2) = lights(y)(x) - 1.0f
                addTileToQueue(x2, y2)
              }
            } else {
              if (lights(y2)(x2) <= lights(y)(x) - 2.0f) {
                addTileToZQueue(x2, y2)
                lights(y2)(x2) = lights(y)(x) - 2.0f
                addTileToQueue(x2, y2)
              }
            }
          }
        }
        lqx.remove(0)
        lqy.remove(0)
        lqd(y)(x) = false
      }
    } catch {
      case _: IndexOutOfBoundsException =>
    }
    zqx.indices.foreach { i =>
      x = zqx(i)
      y = zqy(i)
      if (lights(y)(x).toByte =/= zqn(y)(x)) {
        rdrawn(y)(x) = false
      }
      zqd(y)(x) = false
    }
    zqx.clear()
    zqy.clear()
  }

  def resolvePowerMatrix(): Unit = {
    try {
      while (true) {
        x = pqx(0)
        y = pqy(0)
        (0 until 3).foreach { l =>
          if (powers(blocks(l)(y)(x))) {
            if (!(blocks(l)(y)(x).id >= ZythiumDelayer1DelayRightBlockType.id && blocks(l)(y)(x).id <= ZythiumDelayer8DelayUpOnBlockType.id)) {
              addTileToPQueue(x, y)
              power(l)(y)(x) = 5.toFloat
            }
          }
        }
        (0 until 4).foreach { i =>
          x2 = x + cl(i)(0)
          y2 = y + cl(i)(1)
          if (y2 >= 0 && y2 < HEIGHT) {
            (0 until 3).foreach { l =>
              lazy val block2   = blocks(l)(y2)(x2)
              lazy val blockId2 = blocks(l)(y2)(x2).id
              lazy val block    = blocks(l)(y)(x)
              lazy val blockId  = block.id
              if (power(l)(y)(x) > 0) {
                if (conducts(blockId) >= 0 && receives(blockId2) && !(blockId2 >= ZythiumAmplifierRightBlockType.id && blockId2 <= ZythiumAmplifierUpOnBlockType.id && x < x2 && block2 =/= ZythiumAmplifierRightBlockType && block2 =/= ZythiumAmplifierRightOnBlockType ||
                      blockId2 >= ZythiumAmplifierRightBlockType.id && blockId2 <= ZythiumAmplifierUpOnBlockType.id && y < y2 && block2 =/= ZythiumAmplifierDownBlockType && block2 =/= ZythiumAmplifierDownOnBlockType ||
                      blockId2 >= ZythiumAmplifierRightBlockType.id && blockId2 <= ZythiumAmplifierUpOnBlockType.id && x > x2 && block2 =/= ZythiumAmplifierLeftBlockType && block2 =/= ZythiumAmplifierLeftOnBlockType ||
                      blockId2 >= ZythiumAmplifierRightBlockType.id && blockId2 <= ZythiumAmplifierUpOnBlockType.id && y > y2 && block2 =/= ZythiumAmplifierUpBlockType && block2 =/= ZythiumAmplifierUpOnBlockType) &&
                    !(blockId >= ZythiumAmplifierRightBlockType.id && blockId <= ZythiumAmplifierUpOnBlockType.id && x < x2 && block =/= ZythiumAmplifierRightBlockType && block =/= ZythiumAmplifierRightOnBlockType ||
                      blockId >= ZythiumAmplifierRightBlockType.id && blockId <= ZythiumAmplifierUpOnBlockType.id && y < y2 && block =/= ZythiumAmplifierDownBlockType && block =/= ZythiumAmplifierDownOnBlockType ||
                      blockId >= ZythiumAmplifierRightBlockType.id && blockId <= ZythiumAmplifierUpOnBlockType.id && x > x2 && block =/= ZythiumAmplifierLeftBlockType && block =/= ZythiumAmplifierLeftOnBlockType ||
                      blockId >= ZythiumAmplifierRightBlockType.id && blockId <= ZythiumAmplifierUpOnBlockType.id && y > y2 && block =/= ZythiumAmplifierUpBlockType && block =/= ZythiumAmplifierUpOnBlockType) &&
                    !(blockId2 >= ZythiumInverterRightBlockType.id && blockId2 <= ZythiumInverterUpOnBlockType.id && x < x2 && block2 =/= ZythiumInverterRightBlockType && block2 =/= ZythiumInverterRightOnBlockType ||
                      blockId2 >= ZythiumInverterRightBlockType.id && blockId2 <= ZythiumInverterUpOnBlockType.id && y < y2 && block2 =/= ZythiumInverterDownBlockType && block2 =/= ZythiumInverterDownOnBlockType ||
                      blockId2 >= ZythiumInverterRightBlockType.id && blockId2 <= ZythiumInverterUpOnBlockType.id && x > x2 && block2 =/= ZythiumInverterLeftBlockType && block2 =/= ZythiumInverterLeftOnBlockType ||
                      blockId2 >= ZythiumInverterRightBlockType.id && blockId2 <= ZythiumInverterUpOnBlockType.id && y > y2 && block2 =/= ZythiumInverterUpBlockType && block2 =/= ZythiumInverterUpOnBlockType) &&
                    !(blockId >= ZythiumInverterRightBlockType.id && blockId <= ZythiumInverterUpOnBlockType.id && x < x2 && block =/= ZythiumInverterRightBlockType && block =/= ZythiumInverterRightOnBlockType ||
                      blockId >= ZythiumInverterRightBlockType.id && blockId <= ZythiumInverterUpOnBlockType.id && y < y2 && block =/= ZythiumInverterDownBlockType && block =/= ZythiumInverterDownOnBlockType ||
                      blockId >= ZythiumInverterRightBlockType.id && blockId <= ZythiumInverterUpOnBlockType.id && x > x2 && block =/= ZythiumInverterLeftBlockType && block =/= ZythiumInverterLeftOnBlockType ||
                      blockId >= ZythiumInverterRightBlockType.id && blockId <= ZythiumInverterUpOnBlockType.id && y > y2 && block =/= ZythiumInverterUpBlockType && block =/= ZythiumInverterUpOnBlockType) &&
                    !(blockId2 >= ZythiumDelayer1DelayRightBlockType.id && blockId2 <= ZythiumDelayer8DelayUpOnBlockType.id && x < x2 && block2 =/= ZythiumDelayer1DelayRightBlockType && block2 =/= ZythiumDelayer1DelayRightOnBlockType && block2 =/= ZythiumDelayer2DelayRightBlockType && block2 =/= ZythiumDelayer2DelayRightOnBlockType && block2 =/= ZythiumDelayer4DelayRightBlockType && block2 =/= ZythiumDelayer4DelayRightOnBlockType && block2 =/= ZythiumDelayer8DelayRightBlockType && block2 =/= ZythiumDelayer8DelayRightOnBlockType ||
                      blockId2 >= ZythiumDelayer1DelayRightBlockType.id && blockId2 <= ZythiumDelayer8DelayUpOnBlockType.id && y < y2 && block2 =/= ZythiumDelayer1DelayDownBlockType && block2 =/= ZythiumDelayer1DelayDownOnBlockType && block2 =/= ZythiumDelayer2DelayDownBlockType && block2 =/= ZythiumDelayer2DelayDownOnBlockType && block2 =/= ZythiumDelayer4DelayDownBlockType && block2 =/= ZythiumDelayer4DelayDownOnBlockType && block2 =/= ZythiumDelayer8DelayDownBlockType && block2 =/= ZythiumDelayer8DelayDownOnBlockType ||
                      blockId2 >= ZythiumDelayer1DelayRightBlockType.id && blockId2 <= ZythiumDelayer8DelayUpOnBlockType.id && x > x2 && block2 =/= ZythiumDelayer1DelayLeftBlockType && block2 =/= ZythiumDelayer1DelayLeftOnBlockType && block2 =/= ZythiumDelayer2DelayLeftBlockType && block2 =/= ZythiumDelayer2DelayLeftOnBlockType && block2 =/= ZythiumDelayer4DelayLeftBlockType && block2 =/= ZythiumDelayer4DelayLeftOnBlockType && block2 =/= ZythiumDelayer8DelayLeftBlockType && block2 =/= ZythiumDelayer8DelayLeftOnBlockType ||
                      blockId2 >= ZythiumDelayer1DelayRightBlockType.id && blockId2 <= ZythiumDelayer8DelayUpOnBlockType.id && y > y2 && block2 =/= ZythiumDelayer1DelayUpBlockType && block2 =/= ZythiumDelayer1DelayUpOnBlockType && block2 =/= ZythiumDelayer2DelayUpBlockType && block2 =/= ZythiumDelayer2DelayUpOnBlockType && block2 =/= ZythiumDelayer4DelayUpBlockType && block2 =/= ZythiumDelayer4DelayUpOnBlockType && block2 =/= ZythiumDelayer8DelayUpBlockType && block2 =/= ZythiumDelayer8DelayUpOnBlockType) &&
                    !(blockId >= ZythiumDelayer1DelayRightBlockType.id && blockId <= ZythiumDelayer8DelayUpOnBlockType.id && x < x2 && block =/= ZythiumDelayer1DelayRightBlockType && block =/= ZythiumDelayer1DelayRightOnBlockType && block =/= ZythiumDelayer2DelayRightBlockType && block =/= ZythiumDelayer2DelayRightOnBlockType && block =/= ZythiumDelayer4DelayRightBlockType && block =/= ZythiumDelayer4DelayRightOnBlockType && block =/= ZythiumDelayer8DelayRightBlockType && block =/= ZythiumDelayer8DelayRightOnBlockType ||
                      blockId >= ZythiumDelayer1DelayRightBlockType.id && blockId <= ZythiumDelayer8DelayUpOnBlockType.id && y < y2 && block =/= ZythiumDelayer1DelayDownBlockType && block =/= ZythiumDelayer1DelayDownOnBlockType && block =/= ZythiumDelayer2DelayDownBlockType && block =/= ZythiumDelayer2DelayDownOnBlockType && block =/= ZythiumDelayer4DelayDownBlockType && block =/= ZythiumDelayer4DelayDownOnBlockType && block =/= ZythiumDelayer8DelayDownBlockType && block =/= ZythiumDelayer8DelayDownOnBlockType ||
                      blockId >= ZythiumDelayer1DelayRightBlockType.id && blockId <= ZythiumDelayer8DelayUpOnBlockType.id && x > x2 && block =/= ZythiumDelayer1DelayLeftBlockType && block =/= ZythiumDelayer1DelayLeftOnBlockType && block =/= ZythiumDelayer2DelayLeftBlockType && block =/= ZythiumDelayer2DelayLeftOnBlockType && block =/= ZythiumDelayer4DelayLeftBlockType && block =/= ZythiumDelayer4DelayLeftOnBlockType && block =/= ZythiumDelayer8DelayLeftBlockType && block =/= ZythiumDelayer8DelayLeftOnBlockType ||
                      blockId >= ZythiumDelayer1DelayRightBlockType.id && blockId <= ZythiumDelayer8DelayUpOnBlockType.id && y > y2 && block =/= ZythiumDelayer1DelayUpBlockType && block =/= ZythiumDelayer1DelayUpOnBlockType && block =/= ZythiumDelayer2DelayUpBlockType && block =/= ZythiumDelayer2DelayUpOnBlockType && block =/= ZythiumDelayer4DelayUpBlockType && block =/= ZythiumDelayer4DelayUpOnBlockType && block =/= ZythiumDelayer8DelayUpBlockType && block =/= ZythiumDelayer8DelayUpOnBlockType)) {
                  if (power(l)(y2)(x2) <= power(l)(y)(x) - conducts(blockId)) {
                    addTileToPZQueue(x2, y2)
                    if (blockId2 >= ZythiumDelayer1DelayRightBlockType.id && blockId2 <= ZythiumDelayer1DelayUpBlockType.id ||
                        blockId2 >= ZythiumDelayer2DelayRightBlockType.id && blockId2 <= ZythiumDelayer2DelayUpBlockType.id ||
                        blockId2 >= ZythiumDelayer4DelayRightBlockType.id && blockId2 <= ZythiumDelayer4DelayUpBlockType.id ||
                        blockId2 >= ZythiumDelayer8DelayRightBlockType.id && blockId2 <= ZythiumDelayer8DelayUpBlockType.id) {
                      println("(DEBUG1)")
                      updatex += x2
                      updatey += y2
                      DDELAY.get(blockId2).foreach(updatet.+=)
                      updatel += l
                    } else {
                      power(l)(y2)(x2) = power(l)(y)(x) - conducts(blockId).toFloat
                      if (conducts(blockId2) >= 0 && wcnct(y2)(x2)) {
                        if (l === 0) {
                          if (receives(blocks(1)(y2)(x2).id)) {
                            power(1)(y2)(x2) = power(0)(y2)(x2) - conducts(blocks(0)(y2)(x2).id).toFloat
                          }
                          if (receives(blocks(2)(y2)(x2).id)) {
                            power(2)(y2)(x2) = power(0)(y2)(x2) - conducts(blocks(0)(y2)(x2).id).toFloat
                          }
                        }
                        if (l === 1) {
                          if (receives(blocks(0)(y2)(x2).id)) {
                            power(0)(y2)(x2) = power(1)(y2)(x2) - conducts(blocks(1)(y2)(x2).id).toFloat
                          }
                          if (receives(blocks(2)(y2)(x2).id)) {
                            power(2)(y2)(x2) = power(1)(y2)(x2) - conducts(blocks(1)(y2)(x2).id).toFloat
                          }
                        }
                        if (l === 2) {
                          if (receives(blocks(0)(y2)(x2).id)) {
                            power(0)(y2)(x2) = power(2)(y2)(x2) - conducts(blocks(2)(y2)(x2).id).toFloat
                          }
                          if (receives(blocks(1)(y2)(x2).id)) {
                            power(1)(y2)(x2) = power(2)(y2)(x2) - conducts(blocks(2)(y2)(x2).id).toFloat
                          }
                        }
                      }
                    }
                    if (!(blockId2 >= ZythiumInverterRightBlockType.id && blockId2 <= ZythiumInverterUpBlockType.id)) {
                      addTileToPQueue(x2, y2)
                    }
                  }
                  if (power(l)(y)(x) - conducts(blockId) > 0 && blockId2 >= ZythiumInverterRightBlockType.id && blockId2 <= ZythiumInverterUpBlockType.id) {
                    removeBlockPower(x2, y2, l)
                    blocks(l)(y2)(x2) = BlockType.lookupById(blockId2 + 4)
                    removeBlockLighting(x2, y2)
                    rdrawn(y2)(x2) = false
                  }
                }
              }
            }
          }
        }
        pqx.remove(0)
        pqy.remove(0)
        pqd(y)(x) = false
        (0 until 3).foreach { l =>
          println("(resolvePowerMatrix) " + x + " " + y + " " + l + " " + blocks(l)(y)(x) + " " + power(l)(y)(x))
          if (power(l)(y)(x) > 0) {
            if (blocks(l)(y)(x) === ZythiumLampBlockType) {
              blocks(l)(y)(x) = ZythiumLampOnBlockType
              addBlockLighting(x, y)
              rdrawn(y)(x) = false
            }
            if (blocks(l)(y)(x).id >= ZythiumAmplifierRightBlockType.id && blocks(l)(y)(x).id <= ZythiumAmplifierUpBlockType.id) {
              println("Processed amplifier at " + x + " " + y)
              blocks(l)(y)(x) = BlockType.lookupById(blocks(l)(y)(x).id + 4)
              addTileToPQueue(x, y)
              addBlockLighting(x, y)
              rdrawn(y)(x) = false
            }
          }
        }
      }
    } catch {
      case _: IndexOutOfBoundsException =>
    }
    pzqx.indices.foreach { i =>
      x = pzqx(i)
      y = pzqy(i)
      (0 until 3).foreach { l =>
        if (blocks(l)(y)(x).id >= ZythiumWireBlockType.id && blocks(l)(y)(x).id <= ZythiumWire5PowerBlockType.id && power(
              l)(y)(x).toByte =/= pzqn(l)(y)(x)) {
          removeBlockLighting(x, y, 0)
          WIREP.get(power(l)(y)(x).toInt).foreach { w =>
            blocks(l)(y)(x) = w
          }
          addBlockLighting(x, y)
          rdrawn(y)(x) = false
        }
      }
      pzqd(y)(x) = false
    }
    pzqx.clear()
    pzqy.clear()
  }

  override def paint(g: Graphics): Unit = {
    screen.foreach { screenTemp =>
      pg2 = screenTemp.createGraphics()
      pg2.setColor(bg)
      pg2.fillRect(0, 0, getWidth, getHeight)
      if (state === InGame) {
        /*            if (SKYLIGHTS.get(timeOfDay.toInt) =/= null) {
                  sunlightlevel = SKYLIGHTS.get(timeOfDay.toInt)
                  resunlight = 0
              }
              if (resunlight < WIDTH) {
                  (resunlight until min(resunlight+SUNLIGHTSPEED,WIDTH)).foreach { ux =>
                      removeSunLighting(ux, 0)
                      addSunLighting(ux, 0)
                  }
                  resunlight += SUNLIGHTSPEED
              }
         */
        if (player.y / 16 < HEIGHT * 0.5) {
          pg2.translate((getWidth / 2).toDouble, getHeight * 0.85)
          pg2.rotate((timeOfDay - 70200) / 86400 * Pi * 2)

          drawImage(
            pg2,
            sun,
            (-getWidth * 0.65).toInt,
            0,
            (-getWidth * 0.65 + sun.getWidth() * 2).toInt,
            sun.getHeight() * 2,
            0,
            0,
            sun.getWidth(),
            sun.getHeight())

          pg2.rotate(Pi)

          drawImage(
            pg2,
            moon,
            (-getWidth * 0.65).toInt,
            0,
            (-getWidth * 0.65 + moon.getWidth() * 2).toInt,
            moon.getHeight() * 2,
            0,
            0,
            moon.getWidth(),
            moon.getHeight())

          pg2.rotate(-(timeOfDay - 70200) / 86400 * Pi * 2 - Pi)
          pg2.translate((-getWidth / 2).toDouble, -getHeight * 0.85)

          cloudsx.indices.foreach { i =>
            cloud = clouds(cloudsn(i))
            drawImage(
              pg2,
              clouds(cloudsn(i)),
              cloudsx(i).toInt,
              cloudsy(i).toInt,
              (cloudsx(i) + cloud.getWidth() * 2).toInt,
              (cloudsy(i) + cloud.getHeight() * 2).toInt,
              0,
              0,
              cloud.getWidth(),
              cloud.getHeight()
            )
          }
        }

        (0 until 2).foreach { pwy =>
          (0 until 2).foreach { pwx =>
            val pwxc: Int = pwx + ou
            val pwyc: Int = pwy + ov
            worlds(pwy)(pwx).foreach { w =>
              if (((player.ix + getWidth / 2 + Player.width >= pwxc * CHUNKSIZE &&
                  player.ix + getWidth / 2 + Player.width <= pwxc * CHUNKSIZE + CHUNKSIZE) ||
                  (player.ix - getWidth / 2 + Player.width + BLOCKSIZE >= pwxc * CHUNKSIZE &&
                  player.ix - getWidth / 2 + Player.width - BLOCKSIZE <= pwxc * CHUNKSIZE + CHUNKSIZE)) &&
                  ((player.iy + getHeight / 2 + Player.height >= pwyc * CHUNKSIZE &&
                  player.iy + getHeight / 2 + Player.height <= pwyc * CHUNKSIZE + CHUNKSIZE) ||
                  (player.iy - getHeight / 2 + Player.height >= pwyc * CHUNKSIZE &&
                  player.iy - getHeight / 2 + Player.height <= pwyc * CHUNKSIZE + CHUNKSIZE))) {
                drawImage(
                  pg2,
                  w,
                  pwxc * CHUNKSIZE - player.ix + getWidth / 2 - Player.width / 2,
                  pwyc * CHUNKSIZE - player.iy + getHeight / 2 - Player.height / 2,
                  pwxc * CHUNKSIZE - player.ix + getWidth / 2 - Player.width / 2 + CHUNKSIZE,
                  pwyc * CHUNKSIZE - player.iy + getHeight / 2 - Player.height / 2 + CHUNKSIZE,
                  0,
                  0,
                  CHUNKSIZE,
                  CHUNKSIZE
                )
              }
            }
          }
        }

        drawImage(
          pg2,
          player.image,
          getWidth / 2 - Player.width / 2,
          getHeight / 2 - Player.height / 2,
          getWidth / 2 + Player.width / 2,
          getHeight / 2 + Player.height / 2,
          0,
          0,
          player.image.getWidth(),
          player.image.getHeight()
        )

        entities.foreach { entity =>
          drawImage(
            pg2,
            entity.image,
            entity.ix - player.ix + getWidth / 2 - Player.width / 2,
            entity.iy - player.iy + getHeight / 2 - Player.height / 2,
            entity.ix - player.ix + getWidth / 2 - Player.width / 2 + entity.width,
            entity.iy - player.iy + getHeight / 2 - Player.height / 2 + entity.height,
            0,
            0,
            entity.image.getWidth(),
            entity.image.getHeight()
          )
          drawImage(
            pg2,
            entity.image,
            entity.ix - player.ix + getWidth / 2 - Player.width / 2 - WIDTH * BLOCKSIZE,
            entity.iy - player.iy + getHeight / 2 - Player.height / 2,
            entity.ix - player.ix + getWidth / 2 - Player.width / 2 + entity.width - WIDTH * BLOCKSIZE,
            entity.iy - player.iy + getHeight / 2 - Player.height / 2 + entity.height,
            0,
            0,
            entity.image.getWidth(),
            entity.image.getHeight()
          )
          drawImage(
            pg2,
            entity.image,
            entity.ix - player.ix + getWidth / 2 - Player.width / 2 + WIDTH * BLOCKSIZE,
            entity.iy - player.iy + getHeight / 2 - Player.height / 2,
            entity.ix - player.ix + getWidth() / 2 - Player.width / 2 + entity.width + WIDTH * BLOCKSIZE,
            entity.iy - player.iy + getHeight() / 2 - Player.height / 2 + entity.height,
            0,
            0,
            entity.image.getWidth(),
            entity.image.getHeight()
          )
        }

        if (showTool) {
          tool.foreach { t =>
            if (player.imgState === Player.StillRight || player.imgState === Player.WalkRight1 || player.imgState === Player.WalkRight2) {
              pg2.translate(getWidth / 2 + 6, getHeight / 2)
              pg2.rotate(toolAngle)

              drawImage(pg2, t, 0, -t.getHeight() * 2, t.getWidth() * 2, 0, 0, 0, t.getWidth(), t.getHeight())

              pg2.rotate(-toolAngle)
              pg2.translate(-getWidth / 2 - 6, -getHeight / 2)
            }
            if (player.imgState === Player.StillLeft || player.imgState === Player.WalkLeft1 || player.imgState === Player.WalkLeft2) {
              pg2.translate(getWidth / 2 - 6, getHeight / 2)
              pg2.rotate((Pi * 1.5) - toolAngle)

              drawImage(pg2, t, 0, -t.getHeight() * 2, t.getWidth() * 2, 0, 0, 0, t.getWidth(), t.getHeight())

              pg2.rotate(-((Pi * 1.5) - toolAngle))
              pg2.translate(-getWidth / 2 + 6, -getHeight / 2)
            }
          }

        }

        (0 until 2).foreach { pwy =>
          (0 until 2).foreach { pwx =>
            val pwxc: Int = pwx + ou
            val pwyc: Int = pwy + ov
            fworlds(pwy)(pwx).foreach { fw =>
              if (((player.ix + getWidth / 2 + Player.width >= pwxc * CHUNKSIZE &&
                  player.ix + getWidth / 2 + Player.width <= pwxc * CHUNKSIZE + CHUNKSIZE) ||
                  (player.ix - getWidth / 2 + Player.width + BLOCKSIZE >= pwxc * CHUNKSIZE &&
                  player.ix - getWidth / 2 + Player.width - BLOCKSIZE <= pwxc * CHUNKSIZE + CHUNKSIZE)) &&
                  ((player.iy + getHeight / 2 + Player.height >= pwyc * CHUNKSIZE &&
                  player.iy + getHeight / 2 + Player.height <= pwyc * CHUNKSIZE + CHUNKSIZE) ||
                  (player.iy - getHeight / 2 + Player.height >= pwyc * CHUNKSIZE &&
                  player.iy - getHeight / 2 + Player.height <= pwyc * CHUNKSIZE + CHUNKSIZE))) {
                drawImage(
                  pg2,
                  fw,
                  pwxc * CHUNKSIZE - player.ix + getWidth / 2 - Player.width / 2,
                  pwyc * CHUNKSIZE - player.iy + getHeight / 2 - Player.height / 2,
                  pwxc * CHUNKSIZE - player.ix + getWidth / 2 - Player.width / 2 + CHUNKSIZE,
                  pwyc * CHUNKSIZE - player.iy + getHeight / 2 - Player.height / 2 + CHUNKSIZE,
                  0,
                  0,
                  CHUNKSIZE,
                  CHUNKSIZE
                )
              }
            }
          }
        }

        if (showInv) {
          drawImage(
            pg2,
            inventory.image,
            0,
            0,
            inventory.image.getWidth(),
            inventory.image.getHeight(),
            0,
            0,
            inventory.image.getWidth(),
            inventory.image.getHeight())
          drawImage(
            pg2,
            armor.icType.image,
            inventory.image.getWidth() + 6,
            6,
            inventory.image.getWidth() + 6 + armor.icType.image.getWidth(),
            6 + armor.icType.image.getHeight(),
            0,
            0,
            armor.icType.image.getWidth(),
            armor.icType.image.getHeight()
          )
          cic.foreach { c =>
            drawImage(
              pg2,
              c.icType.image,
              inventory.image.getWidth() + 75,
              52,
              inventory.image.getWidth() + 75 + c.icType.image.getWidth(),
              52 + c.icType.image.getHeight(),
              0,
              0,
              c.icType.image.getWidth(),
              c.icType.image.getHeight()
            )
          }
        } else {
          drawImage(
            pg2,
            inventory.image,
            0,
            0,
            inventory.image.getWidth(),
            inventory.image.getHeight() / 4,
            0,
            0,
            inventory.image.getWidth(),
            inventory.image.getHeight() / 4)
        }

        ic.foreach { icTemp =>
          drawImage(
            pg2,
            icTemp.icType.image,
            6,
            inventory.image.getHeight() + 46,
            6 + icTemp.icType.image.getWidth(),
            inventory.image.getHeight() + 46 + icTemp.icType.image.getHeight(),
            0,
            0,
            icTemp.icType.image.getWidth(),
            icTemp.icType.image.getHeight()
          )
        }

        if (layer === 0) {
          layerImg = loadImage("interface/layersB.png").get //TODO: why are we loading images in the paint method?
        }
        if (layer === 1) {
          layerImg = loadImage("interface/layersN.png").get
        }
        if (layer === 2) {

          layerImg = loadImage("interface/layersF.png").get
        }

        drawImage(
          pg2,
          layerImg,
          inventory.image.getWidth() + 58,
          6,
          inventory.image.getWidth() + 58 + layerImg.getWidth(),
          6 + layerImg.getHeight(),
          0,
          0,
          layerImg.getWidth(),
          layerImg.getHeight()
        )

        if (showInv) {
          drawImage(
            pg2,
            save_exit,
            getWidth - save_exit.getWidth() - 24,
            getHeight - save_exit.getHeight() - 24,
            getWidth - 24,
            getHeight - 24,
            0,
            0,
            save_exit.getWidth(),
            save_exit.getHeight()
          )
        }
        val (mouseX, mouseY)    = userInput.currentMousePosition
        val mouseXWorldPosition = mouseX + player.ix - getWidth() / 2 + Player.width / 2
        val mouseYWorldPosition = mouseY + player.iy - getHeight() / 2 + Player.height / 2

        UiItem.onImageItem(moveItem) { mi =>
          val image = mi.image
          width = image.getWidth
          height = image.getHeight
          drawImage(
            pg2,
            image,
            mouseX + 12 + ((24 - 12.toDouble / mh.max(width, height, 12) * width * 2) / 2).toInt,
            mouseY + 12 + ((24 - 12.toDouble / mh.max(width, height, 12) * height * 2) / 2).toInt,
            mouseX + 36 - ((24 - 12.toDouble / mh.max(width, height, 12) * width * 2) / 2).toInt,
            mouseY + 36 - ((24 - 12.toDouble / mh.max(width, height, 12) * height * 2) / 2).toInt,
            0,
            0,
            width,
            height
          )

          if (moveNum > 1) {
            pg2.setFont(font)
            pg2.setColor(Color.WHITE)
            pg2.drawString(moveNum + " ", mouseX + 13, mouseY + 38)
          }
        }
        import scala.util.control.Breaks._
        breakable {
          entities
            .collect {
              case e @ AIEntity(_, _, _, _, strat) => (e, UIENTITIES.get(strat.imageName))
            }
            .collect {
              case (e, Some(entityName)) => (e, entityName)
            }
            .foreach { p =>
              val entity     = p._1
              val entityName = p._2
              if (entity.rect.contains(new Point(mouseXWorldPosition, mouseYWorldPosition))) {
                pg2.setFont(mobFont)
                pg2.setColor(Color.WHITE)
                pg2.drawString(entityName + " (" + entity.hp + "/" + entity.strategy.thp + ")", mouseX, mouseY)
                break
              }
            }
        }
        if (showInv) {
          ymax = 4
        } else {
          ymax = 1
        }
        //TODO: move somewhere better like UiItem maybe?
        def paintItem(item: UiItem, dur: Short, mouseX: Int, mouseY: Int, g: Graphics2D, f: Font): Unit = {
          val toolDur = UiItem.toolDurability(item)
          g.setFont(f)
          g.setColor(Color.WHITE)

          if (toolDur === 0) {
            g.drawString(item.name, mouseX, mouseY)
          } else {
            g.drawString(item.name + " (" + (dur.toDouble / toolDur * 100).toInt + "%)", mouseX, mouseY)
          }
        }
        (0 until 10).foreach { ux =>
          (0 until ymax).foreach { uy =>
            if (mouseX >= ux * 46 + 6 && mouseX <= ux * 46 + 46 &&
                mouseY >= uy * 46 + 6 && mouseY <= uy * 46 + 46 && inventory.ids(uy * 10 + ux) =/= EmptyUiItem) {
              paintItem(inventory.ids(uy * 10 + ux), inventory.durs(uy * 10 + ux), mouseX, mouseY, pg2, mobFont)
            }
          }
        }
        pg2.setFont(mobFont)
        pg2.setColor(Color.WHITE)
        pg2.drawString("Health: " + player.hp + "/" + Player.totalHP, getWidth - 125, 20)
        pg2.drawString("Armor: " + player.sumArmor(), getWidth - 125, 40)
        if (DEBUG_STATS) {
          pg2.drawString("(" + (player.ix / BLOCKSIZE) + ", " + (player.iy / BLOCKSIZE) + ")", getWidth - 125, 60)
          if (player.iy >= 0 && player.iy < HEIGHT * BLOCKSIZE) {
            pg2.drawString(
              checkBiome(player.ix / BLOCKSIZE + u, player.iy / BLOCKSIZE + v, u, v, blocks, blockbgs).name + " " + lights(
                player.iy / BLOCKSIZE + v)(player.ix / BLOCKSIZE + u),
              getWidth - 125,
              80)
          }
        }
        if (showInv) {
          cic.foreach { c =>
            (0 until 2).foreach { ux =>
              (0 until 2).foreach { uy =>
                if (mouseX >= inventory.image.getWidth() + ux * 40 + 75 &&
                    mouseX < inventory.image.getWidth() + ux * 40 + 115 &&
                    mouseY >= uy * 40 + 52 && mouseY < uy * 40 + 92 && c.ids(uy * 2 + ux) =/= EmptyUiItem) {
                  paintItem(c.ids(uy * 2 + ux), c.durs(uy * 2 + ux), mouseX, mouseY, pg2, mobFont)
                }
              }
            }
            if (mouseX >= inventory.image.getWidth() + 3 * 40 + 75 &&
                mouseX < inventory.image.getWidth() + 3 * 40 + 115 &&
                mouseY >= 20 + 52 && mouseY < 20 + 92 && c.ids(4) =/= EmptyUiItem) {
              paintItem(c.ids(4), c.durs(4), mouseX, mouseY, pg2, mobFont)
            }
          }
          (0 until 4).foreach { uy =>
            if (mouseX >= inventory.image.getWidth() + 6 && mouseX < inventory.image.getWidth() + 6 + armor.icType.image
                  .getWidth() &&
                mouseY >= 6 + uy * 46 && mouseY < 6 + uy * 46 + 46 && armor.ids(uy) =/= EmptyUiItem) {
              paintItem(armor.ids(uy), armor.durs(uy), mouseX, mouseY, pg2, mobFont)
            }
          }
        }
        ic.foreach { icTemp =>

          icTemp.icType match {
            case Workbench =>

              (0 until 3).foreach { ux =>
                (0 until 3).foreach { uy =>
                  if (mouseX >= ux * 40 + 6 && mouseX < ux * 40 + 46 &&
                    mouseY >= uy * 40 + inventory.image.getHeight() + 46 &&
                    mouseY < uy * 40 + inventory.image.getHeight() + 86 &&
                    icTemp.ids(uy * 3 + ux) =/= EmptyUiItem) {
                    paintItem(icTemp.ids(uy * 3 + ux), icTemp.durs(uy * 3 + ux), mouseX, mouseY, pg2, mobFont)
                  }
                }
              }
              if (mouseX >= 4 * 40 + 6 && mouseX < 4 * 40 + 46 &&
                mouseY >= 1 * 40 + inventory.image.getHeight() + 46 &&
                mouseY < 1 * 40 + inventory.image.getHeight() + 86 &&
                icTemp.ids(9) =/= EmptyUiItem) {
                paintItem(icTemp.ids(9), icTemp.durs(9), mouseX, mouseY, pg2, mobFont)
              }

            case ChestItemCollection(_) =>
              (0 until inventory.CX).foreach { ux =>
                (0 until inventory.CY).foreach { uy =>
                  if (mouseX >= ux * 46 + 6 && mouseX < ux * 46 + 46 &&
                    mouseY >= uy * 46 + inventory.image.getHeight() + 46 &&
                    mouseY < uy * 46 + inventory.image.getHeight() + 86 &&
                    icTemp.ids(uy * inventory.CX + ux) =/= EmptyUiItem) {
                    paintItem(
                      icTemp.ids(uy * inventory.CX + ux),
                      icTemp.durs(uy * inventory.CX + ux),
                      mouseX,
                      mouseY,
                      pg2,
                      mobFont)
                  }
                }
              }

            case Furnace =>

              if (mouseX >= 6 && mouseX < 46 &&
                mouseY >= inventory.image.getHeight() + 46 && mouseY < inventory.image.getHeight() + 86 &&
                icTemp.ids(0) =/= EmptyUiItem) {

                paintItem(icTemp.ids(0), icTemp.durs(0), mouseX, mouseY, pg2, mobFont)
              }
              if (mouseX >= 6 && mouseX < 46 &&
                mouseY >= inventory.image.getHeight() + 102 && mouseY < inventory.image.getHeight() + 142 &&
                icTemp.ids(1) =/= EmptyUiItem) {
                pg2.setFont(mobFont)
                pg2.setColor(Color.WHITE)

                paintItem(icTemp.ids(1), icTemp.durs(1), mouseX, mouseY, pg2, mobFont)
              }
              if (mouseX >= 6 && mouseX < 46 &&
                mouseY >= inventory.image.getHeight() + 142 && mouseY < inventory.image.getHeight() + 182 &&
                icTemp.ids(2) =/= EmptyUiItem) {
                paintItem(icTemp.ids(2), icTemp.durs(2), mouseX, mouseY, pg2, mobFont)
              }
              if (mouseX >= 62 && mouseX < 102 &&
                mouseY >= inventory.image.getHeight() + 46 && mouseY < inventory.image.getHeight() + 86 &&
                icTemp.ids(3) =/= EmptyUiItem) {
                paintItem(icTemp.ids(3), icTemp.durs(3), mouseX, mouseY, pg2, mobFont)
              }

            case _ =>
          }
        }
      }
      if (state === LoadingGraphics) {
        pg2.setFont(loadFont)
        pg2.setColor(Color.GREEN)
        pg2.drawString("Loading graphics... Please wait.", getWidth() / 2 - 200, getHeight() / 2 - 5)
      }
      if (state === TitleScreen) {
        drawImage(pg2, title_screen, 0, 0, getWidth(), getHeight(), 0, 0, getWidth(), getHeight())
      }
      if (state === SelectWorld) {
        drawImage(pg2, select_world, 0, 0, getWidth(), getHeight(), 0, 0, getWidth(), getHeight())
        worldNames.indices.foreach { i =>
          pg2.setFont(worldFont)
          pg2.setColor(Color.BLACK)
          pg2.drawString(worldNames(i), 180, 140 + i * 35)
          pg2.fillRect(166, 150 + i * 35, 470, 3)
        }
      }
      if (state === NewWorld) {
        drawImage(pg2, new_world, 0, 0, getWidth(), getHeight(), 0, 0, getWidth(), getHeight())
        drawImage(pg2, newWorldName.image, 200, 240, 600, 270, 0, 0, 400, 30)
      }
      if (state === GeneratingWorld) {
        pg2.setFont(loadFont)
        pg2.setColor(Color.GREEN)
        pg2.drawString("Generating new world... Please wait.", getWidth() / 2 - 200, getHeight() / 2 - 5)
      }
      drawImage(g, screenTemp, 0, 0, getWidth(), getHeight(), 0, 0, getWidth(), getHeight())
      ()
    }
  }

  def loadWorld(worldFile: String): Boolean = {
    try {
      val fileIn = new FileInputStream("worlds/" + worldFile)
      val in     = new ObjectInputStream(fileIn)
      val wc: Option[WorldContainer] = in.readObject() match {
        case w: WorldContainer => Some(w)
        case _                 => None
      }
      in.close()
      fileIn.close()
      wc.foreach(emptyWorldContainer)
      wc.isDefined
    } catch {
      case NonFatal(_) => false
    }
  }

  def saveWorld(): Unit = {
    try {
      val wc: WorldContainer = createWorldContainer()
      val fileOut            = new FileOutputStream("worlds/" + currentWorld + ".dat")
      val out                = new ObjectOutputStream(fileOut)
      out.writeObject(wc)
      out.close()
      fileOut.close()
    } catch {
      case e: IOException => e.printStackTrace()
    }
  }

  def emptyWorldContainer(wc: WorldContainer): Unit = {
    blocks = wc.blocks
    blockds = wc.blockds
    blockdns = wc.blockdns
    blockbgs = wc.blockbgs
    blockts = wc.blockts
    lights = wc.lights
    power = wc.power
    resetDrawn()
    player = wc.player
    inventory = wc.inventory
    cic = wc.cic
    entities = wc.entities
    cloudsx = wc.cloudsx
    cloudsy = wc.cloudsy
    cloudsv = wc.cloudsv
    cloudsn = wc.cloudsn
    machinesx = wc.machinesx
    machinesy = wc.machinesy
    lsources = wc.lsources
    lqx = wc.lqx
    lqy = wc.lqy
    lqd = wc.lqd
    rgnc1 = wc.rgnc1
    rgnc2 = wc.rgnc2
    layer = wc.layer
    layerTemp = wc.layerTemp
    blockTemp = wc.blockTemp
    mx = wc.mx
    my = wc.my
    icx = wc.icx
    icy = wc.icy
    mining = wc.mining
    immune = wc.immune
    moveItem = wc.moveItem
    moveNum = wc.moveNum
    moveItemTemp = wc.moveItemTemp
    moveNumTemp = wc.moveNumTemp
    msi = wc.msi
    toolAngle = wc.toolAngle
    toolSpeed = wc.toolSpeed
    timeOfDay = wc.timeOfDay
    currentSkyLight = wc.currentSkyLight
    day = wc.day
    mobCount = wc.mobCount
    ready = wc.ready
    showTool = wc.showTool
    showInv = wc.showInv
    doMobSpawn = wc.doMobSpawn
    WIDTH = wc.WIDTH
    HEIGHT = wc.HEIGHT
    random = wc.random
    WORLDWIDTH = wc.WORLDWIDTH
    WORLDHEIGHT = wc.WORLDHEIGHT
    kworlds = wc.kworlds
    ic = wc.ic
    icmatrix = wc.icmatrix
    version = wc.version
    player.reloadImage()
    inventory.reloadImage()

    cic = cic.orElse(Some(new ItemCollection(Crafting)))
    cic.foreach(inventory.renderCollection)

    ic.foreach(inventory.renderCollection)
    entities.foreach { entity: Entity =>
      entity.reloadImage()
    }
    worlds = Array.ofDim(2, 2)
    fworlds = Array.fill(2, 2)(None)
  }

  def resetDrawn(): Unit = {
    drawn = Array.ofDim(HEIGHT, WIDTH)
    (0 until HEIGHT).foreach { y =>
      (0 until WIDTH).foreach { x =>
        drawn(y)(x) = false
      }
    }
    ldrawn = Array.ofDim(HEIGHT, WIDTH)
    (0 until HEIGHT).foreach { y =>
      (0 until WIDTH).foreach { x =>
        ldrawn(y)(x) = false
      }
    }
    rdrawn = Array.ofDim(HEIGHT, WIDTH)
    (0 until HEIGHT).foreach { y =>
      (0 until WIDTH).foreach { x =>
        rdrawn(y)(x) = false
      }
    }
  }

  def createWorldContainer(): WorldContainer = {
    WorldContainer(
      blocks,
      blockds,
      blockdns,
      blockbgs,
      blockts,
      lights,
      power,
      drawn,
      ldrawn,
      rdrawn,
      player,
      inventory,
      cic,
      entities,
      cloudsx,
      cloudsy,
      cloudsv,
      cloudsn,
      machinesx,
      machinesy,
      lsources,
      lqx,
      lqy,
      lqd,
      rgnc1,
      rgnc2,
      layer,
      layerTemp,
      blockTemp,
      mx,
      my,
      icx,
      icy,
      mining,
      immune,
      moveItem,
      moveNum,
      moveItemTemp,
      moveNumTemp,
      msi,
      toolAngle,
      toolSpeed,
      timeOfDay,
      currentSkyLight,
      day,
      mobCount,
      ready,
      showTool,
      showInv,
      doMobSpawn,
      WIDTH,
      HEIGHT,
      random,
      WORLDWIDTH,
      WORLDHEIGHT,
      ic,
      kworlds,
      icmatrix,
      version
    )
  }

  def loadBlock(block: BlockType,
                dir: OutlineDirection,
                dirn: Byte,
                tnum: Byte,
                blocknames: Array[String],
                outlineName: String,
                x: Int,
                y: Int,
                lyr: Int): BufferedImage = {
    val dir_i: Int                          = dirn.toInt
    val bName: String                       = blocknames(block.id)
    val image: BufferedImage                = config.createCompatibleImage(IMAGESIZE, IMAGESIZE, Transparency.TRANSLUCENT)
    var maybeTexture: Option[BufferedImage] = blockImgs.get("blocks/" + bName + "/texture" + (tnum + 1) + ".png")
    GRASSDIRT.get(block.id).foreach { grassdirt =>
      val maybeDirtOriginal: Option[BufferedImage] =
        blockImgs.get("blocks/" + blocknames(grassdirt) + "/texture" + (tnum + 1) + ".png")
      val dirt: BufferedImage = config.createCompatibleImage(IMAGESIZE, IMAGESIZE, Transparency.TRANSLUCENT)
      (0 until IMAGESIZE).foreach { dy =>
        (0 until IMAGESIZE).foreach { dx =>
          maybeDirtOriginal.foreach { dirtOriginal =>
            dirt.setRGB(dx, dy, dirtOriginal.getRGB(dx, dy))
          }
        }
      }
      //val dn: Int = GRASSDIRT.get(block.id)
      val left: Boolean = blocks(lyr)(y)(x - 1) === AirBlockType || !blockcds(blocks(lyr)(y)(x - 1).id)
      // && (blocks(lyr)(y-1)(x) =/= dn && blocks(lyr)(y+1)(x) =/= dn) && (blocks(lyr)(y-1)(x-1) =/= dn && blocks(lyr)(y+1)(x-1) =/= dn)
      val right: Boolean = blocks(lyr)(y)(x + 1) === AirBlockType || !blockcds(blocks(lyr)(y)(x + 1).id)
      // && (blocks(lyr)(y-1)(x) =/= dn && blocks(lyr)(y+1)(x) =/= dn) && (blocks(lyr)(y-1)(x+1) =/= dn && blocks(lyr)(y+1)(x+1) =/= dn)
      val up: Boolean = blocks(lyr)(y - 1)(x) === AirBlockType || !blockcds(blocks(lyr)(y - 1)(x).id)
      // && (blocks(lyr)(y)(x-1) =/= dn && blocks(lyr)(y)(x+1) =/= dn) && (blocks(lyr)(y-1)(x-1) =/= dn && blocks(lyr)(y-1)(x+1) =/= dn)
      val down: Boolean = blocks(lyr)(y + 1)(x) === AirBlockType || !blockcds(blocks(lyr)(y + 1)(x).id)
      // && (blocks(lyr)(y)(x-1) =/= dn && blocks(lyr)(y)(x+1) =/= dn) && (blocks(lyr)(y+1)(x-1) =/= dn && blocks(lyr)(y+1)(x+1) =/= dn)
      val upleft: Boolean = blocks(lyr)(y - 1)(x - 1) === AirBlockType || !blockcds(blocks(lyr)(y - 1)(x - 1).id)
      // && (blocks(lyr)(y-1)(x) =/= dn && blocks(lyr)(y)(x-1) =/= dn && blocks(lyr)(y-1)(x-1) =/= dn && blocks(lyr)(y-2)(x) =/= dn && blocks(lyr)(y)(x-2) =/= dn)
      val upright: Boolean = blocks(lyr)(y - 1)(x + 1) === AirBlockType || !blockcds(blocks(lyr)(y - 1)(x + 1).id)
      // && (blocks(lyr)(y-1)(x) =/= dn && blocks(lyr)(y)(x+1) =/= dn && blocks(lyr)(y-1)(x+1) =/= dn && blocks(lyr)(y-2)(x) =/= dn && blocks(lyr)(y)(x+2) =/= dn)
      val downleft: Boolean = blocks(lyr)(y + 1)(x - 1) === AirBlockType || !blockcds(blocks(lyr)(y + 1)(x - 1).id)
      // && (blocks(lyr)(y+1)(x) =/= dn && blocks(lyr)(y)(x-1) =/= dn && blocks(lyr)(y+1)(x-1) =/= dn && blocks(lyr)(y+2)(x) =/= dn && blocks(lyr)(y)(x-2) =/= dn)
      val downright: Boolean = blocks(lyr)(y + 1)(x + 1) === AirBlockType || !blockcds(blocks(lyr)(y + 1)(x + 1).id)
      // && (blocks(lyr)(y+1)(x) =/= dn && blocks(lyr)(y)(x+1) =/= dn && blocks(lyr)(y+1)(x+1) =/= dn && blocks(lyr)(y+2)(x) =/= dn && blocks(lyr)(y)(x+2) =/= dn)
      val pixm: Array2D[Int] = Array.ofDim(IMAGESIZE, IMAGESIZE)
      (0 until 8).foreach { dy =>
        (0 until 8).foreach { dx =>
          pixm(dy)(dx) = 0
        }
      }
      if (left) {
        pixm(3)(0) = 255
        pixm(4)(0) = 255
      }
      if (right) {
        pixm(3)(7) = 255
        pixm(4)(7) = 255
      }
      if (up) {
        pixm(0)(3) = 255
        pixm(0)(4) = 255
      }
      if (down) {
        pixm(7)(3) = 255
        pixm(7)(4) = 255
      }
      if (upleft) {
        pixm(0)(0) = 255
      }
      if (upright) {
        pixm(0)(7) = 255
      }
      if (downleft) {
        pixm(7)(0) = 255
      }
      if (downright) {
        pixm(7)(7) = 255
      }
      if (left && up) {
        (0 until 4).foreach { dy =>
          pixm(dy)(0) = 255
        }
        (0 until 4).foreach { dx =>
          pixm(0)(dx) = 255
        }
      }
      if (up && right) {
        (4 until 8).foreach { dx =>
          pixm(0)(dx) = 255
        }
        (0 until 4).foreach { dy =>
          pixm(dy)(7) = 255
        }
      }
      if (right && down) {
        (4 until 8).foreach { dy =>
          pixm(dy)(7) = 255
        }
        (4 until 8).foreach { dx =>
          pixm(7)(dx) = 255
        }
      }
      if (down && left) {
        (0 until 4).foreach { dx =>
          pixm(7)(dx) = 255
        }
        (4 until 8).foreach { dy =>
          pixm(dy)(0) = 255
        }
      }
      (0 until 8).foreach { dy =>
        (0 until 8).foreach { dx =>
          if (pixm(dy)(dx) === 255) {
            (0 until 8).foreach { dy2 =>
              (0 until 8).foreach { dx2 =>
                n = (255 - 32 * sqrt(pow((dx - dx2).toDouble, 2) + pow((dy - dy2).toDouble, 2))).toInt
                if (pixm(dy2)(dx2) < n) {
                  pixm(dy2)(dx2) = n
                }
              }
            }
          }
        }
      }
      (0 until 8).foreach { dy =>
        (0 until 8).foreach { dx =>
          maybeTexture.foreach { texture =>
            dirt.setRGB(
              dx,
              dy,
              new Color(
                (pixm(dy)(dx) / 255.0 * new Color(texture.getRGB(dx, dy)).getRed + (1 - pixm(dy)(dx) / 255.0) * new Color(
                  dirt.getRGB(dx, dy)).getRed).toInt,
                (pixm(dy)(dx) / 255.0 * new Color(texture.getRGB(dx, dy)).getGreen + (1 - pixm(dy)(dx) / 255.0) * new Color(
                  dirt.getRGB(dx, dy)).getGreen).toInt,
                (pixm(dy)(dx) / 255.0 * new Color(texture.getRGB(dx, dy)).getBlue + (1 - pixm(dy)(dx) / 255.0) * new Color(
                  dirt.getRGB(dx, dy)).getBlue).toInt
              ).getRGB
            )
          }
        }
      }
      maybeTexture = Some(dirt)
    }

    outlineImgs.get("outlines/" + outlineName + "/" + dir.imageName + (dir_i + 1) + ".png").foreach { outline =>
      (0 until IMAGESIZE).foreach { fy =>
        (0 until IMAGESIZE).foreach { fx =>
          if (outline.getRGB(fx, fy) === -65281 || outline.getRGB(fx, fy) === -48897) {
            maybeTexture.foreach { texture =>
              image.setRGB(fx, fy, texture.getRGB(fx, fy))
            }
          } else if (outline.getRGB(fx, fy) === -16777216) {
            image.setRGB(fx, fy, -16777216)
          }
        }
      }
    }
    image
  }

  def keyPressed(key: KeyEvent): Unit = {
    val keyCode = key.getKeyCode
    if (keyCode === KeyEvent.VK_LEFT || keyCode === KeyEvent.VK_A) {
      userInput.setLeftKeyPressed(true)
    }
    if (keyCode === KeyEvent.VK_RIGHT || keyCode === KeyEvent.VK_D) {
      userInput.setRightKeyPressed(true)
    }
    if (keyCode === KeyEvent.VK_UP || keyCode === KeyEvent.VK_W) {
      userInput.setUpKeyPressed(true)
    }
    if (keyCode === KeyEvent.VK_DOWN || keyCode === KeyEvent.VK_S) {
      userInput.setDownKeyPressed(true)
    }
    if (keyCode === KeyEvent.VK_SHIFT) {
      userInput.setShiftKeyPressed(true)
    }
    if (state === InGame) {
      if (keyCode === KeyEvent.VK_ESCAPE) {
        ic.fold {
          if (showInv) {
            (0 until 4).foreach { i =>
              cic.foreach { c =>
                UiItem.onImageItem(c.ids(i)) { imgItm =>
                  if (player.imgState === Player.StillRight || player.imgState === Player.WalkRight1 || player.imgState === Player.WalkRight2) {
                    entities += IdEntity(player.x, player.y, 2, -2, imgItm, c.nums(i), c.durs(i), 75)
                  }
                  if (player.imgState === Player.StillLeft || player.imgState === Player.WalkLeft1 || player.imgState === Player.WalkLeft2) {
                    entities += IdEntity(player.x, player.y, -2, -2, imgItm, c.nums(i), c.durs(i), 75)
                  }
                  inventory.removeLocationIC(c, i, c.nums(i))
                  ()
                }
              }
            }
          }
          showInv = !showInv
        } { icTemp =>
          if (icTemp.icType =/= Workbench) {
            machinesx += icx
            machinesy += icy
            icmatrix(iclayer)(icy)(icx) = Some(ItemCollection(icTemp.icType, icTemp.ids, icTemp.nums, icTemp.durs))
          }
          if (icTemp.icType === Workbench) {
            if (player.imgState === Player.StillRight || player.imgState === Player.WalkRight1 || player.imgState === Player.WalkRight2) {
              (0 until 9).foreach { i =>
                UiItem.onImageItem(icTemp.ids(i)) { imgItm =>
                  entities += IdEntity(
                    (icx * BLOCKSIZE).toDouble,
                    (icy * BLOCKSIZE).toDouble,
                    2,
                    -2,
                    imgItm,
                    icTemp.nums(i),
                    icTemp.durs(i),
                    75)
                  ()
                }
              }
            }
            if (player.imgState === Player.StillLeft || player.imgState === Player.WalkLeft1 || player.imgState === Player.WalkLeft2) {
              (0 until 9).foreach { i =>
                UiItem.onImageItem(icTemp.ids(i)) { imgItm =>
                  entities += IdEntity(
                    (icx * BLOCKSIZE).toDouble,
                    (icy * BLOCKSIZE).toDouble,
                    -2,
                    -2,
                    imgItm,
                    icTemp.nums(i),
                    icTemp.durs(i),
                    75)
                  ()
                }
              }
            }
          }
          if (icTemp.icType === Furnace) {
            icmatrix(iclayer)(icy)(icx).foreach { icMatrixTemp =>
              icMatrixTemp.FUELP = icTemp.FUELP
              icMatrixTemp.SMELTP = icTemp.SMELTP
              icMatrixTemp.F_ON = icTemp.F_ON
            }
          }
          ic = None
        }
        UiItem.onImageItem(moveItem) { imgItm =>
          if (player.imgState === Player.StillRight || player.imgState === Player.WalkRight1 || player.imgState === Player.WalkRight2) {
            entities += IdEntity(player.x, player.y, 2, -2, imgItm, moveNum, moveDur, 75)
          }
          if (player.imgState === Player.StillLeft || player.imgState === Player.WalkLeft1 || player.imgState === Player.WalkLeft2) {
            entities += IdEntity(player.x, player.y, -2, -2, imgItm, moveNum, moveDur, 75)
          }
          moveItem = EmptyUiItem
          moveNum = 0
        }
      }
      if (!showTool) {
        if (keyCode === KeyEvent.VK_1) {
          inventory.select(1)
        }
        if (keyCode === KeyEvent.VK_2) {
          inventory.select(2)
        }
        if (keyCode === KeyEvent.VK_3) {
          inventory.select(3)
        }
        if (keyCode === KeyEvent.VK_4) {
          inventory.select(4)
        }
        if (keyCode === KeyEvent.VK_5) {
          inventory.select(5)
        }
        if (keyCode === KeyEvent.VK_6) {
          inventory.select(6)
        }
        if (keyCode === KeyEvent.VK_7) {
          inventory.select(7)
        }
        if (keyCode === KeyEvent.VK_8) {
          inventory.select(8)
        }
        if (keyCode === KeyEvent.VK_9) {
          inventory.select(9)
        }
        if (keyCode === KeyEvent.VK_0) {
          inventory.select(0)
        }
      }
    }
    var c: Char = 0
    if (keyCode === KeyEvent.VK_Q) c = 'q'
    if (keyCode === KeyEvent.VK_W) c = 'w'
    if (keyCode === KeyEvent.VK_E) c = 'e'
    if (keyCode === KeyEvent.VK_R) c = 'r'
    if (keyCode === KeyEvent.VK_T) c = 't'
    if (keyCode === KeyEvent.VK_Y) c = 'y'
    if (keyCode === KeyEvent.VK_U) c = 'u'
    if (keyCode === KeyEvent.VK_I) c = 'i'
    if (keyCode === KeyEvent.VK_O) c = 'o'
    if (keyCode === KeyEvent.VK_P) c = 'p'
    if (keyCode === KeyEvent.VK_A) c = 'a'
    if (keyCode === KeyEvent.VK_S) c = 's'
    if (keyCode === KeyEvent.VK_D) c = 'd'
    if (keyCode === KeyEvent.VK_F) c = 'f'
    if (keyCode === KeyEvent.VK_G) c = 'g'
    if (keyCode === KeyEvent.VK_H) c = 'h'
    if (keyCode === KeyEvent.VK_J) c = 'j'
    if (keyCode === KeyEvent.VK_K) c = 'k'
    if (keyCode === KeyEvent.VK_L) c = 'l'
    if (keyCode === KeyEvent.VK_Z) c = 'z'
    if (keyCode === KeyEvent.VK_X) c = 'x'
    if (keyCode === KeyEvent.VK_C) c = 'c'
    if (keyCode === KeyEvent.VK_V) c = 'v'
    if (keyCode === KeyEvent.VK_B) c = 'b'
    if (keyCode === KeyEvent.VK_N) c = 'n'
    if (keyCode === KeyEvent.VK_M) c = 'm'
    if (keyCode === KeyEvent.VK_1) c = '1'
    if (keyCode === KeyEvent.VK_2) c = '2'
    if (keyCode === KeyEvent.VK_3) c = '3'
    if (keyCode === KeyEvent.VK_4) c = '4'
    if (keyCode === KeyEvent.VK_5) c = '5'
    if (keyCode === KeyEvent.VK_6) c = '6'
    if (keyCode === KeyEvent.VK_7) c = '7'
    if (keyCode === KeyEvent.VK_8) c = '8'
    if (keyCode === KeyEvent.VK_9) c = '9'
    if (keyCode === KeyEvent.VK_0) c = '0'
    if (keyCode === KeyEvent.VK_SPACE) c = ' '
    if (keyCode === 192) c = '`'
    if (keyCode === KeyEvent.VK_MINUS) c = '-'
    if (keyCode === KeyEvent.VK_EQUALS) c = '='
    if (keyCode === KeyEvent.VK_OPEN_BRACKET) c = '['
    if (keyCode === KeyEvent.VK_CLOSE_BRACKET) c = ']'
    if (keyCode === KeyEvent.VK_BACK_SLASH) c = '\\'
    if (keyCode === KeyEvent.VK_COLON) c = ':'
    if (keyCode === KeyEvent.VK_QUOTE) c = '\''
    if (keyCode === KeyEvent.VK_COMMA) c = ','
    if (keyCode === KeyEvent.VK_PERIOD) c = '.'
    if (keyCode === KeyEvent.VK_SLASH) c = '/'

    if (userInput.isShiftKeyPressed) {
      if (c === 'q') c = 'Q'
      if (c === 'w') c = 'W'
      if (c === 'e') c = 'E'
      if (c === 'r') c = 'R'
      if (c === 't') c = 'T'
      if (c === 'y') c = 'Y'
      if (c === 'u') c = 'U'
      if (c === 'i') c = 'I'
      if (c === 'o') c = 'O'
      if (c === 'p') c = 'P'
      if (c === 'a') c = 'A'
      if (c === 's') c = 'S'
      if (c === 'd') c = 'D'
      if (c === 'f') c = 'F'
      if (c === 'g') c = 'G'
      if (c === 'h') c = 'H'
      if (c === 'j') c = 'J'
      if (c === 'k') c = 'K'
      if (c === 'l') c = 'L'
      if (c === 'z') c = 'Z'
      if (c === 'x') c = 'X'
      if (c === 'c') c = 'C'
      if (c === 'v') c = 'V'
      if (c === 'b') c = 'B'
      if (c === 'n') c = 'N'
      if (c === 'm') c = 'M'
      if (c === '1') c = '!'
      if (c === '2') c = '@'
      if (c === '3') c = '#'
      if (c === '4') c = '$'
      if (c === '5') c = '%'
      if (c === '6') c = '^'
      if (c === '7') c = '&'
      if (c === '8') c = '*'
      if (c === '9') c = '('
      if (c === '0') c = ')'
      if (c === ' ') c = ' '
      if (c === '`') c = '~'
      if (c === '-') c = '_'
      if (c === '=') c = '+'
      if (c === '[') c = '{'
      if (c === ']') c = '}'
      if (c === '\\') c = '|'
      if (c === ';') c = ')'
      if (c === '\'') c = '"'
      if (c === ',') c = '<'
      if (c === '.') c = '>'
      if (c === '/') c = '?'
    }

    if (state === NewWorld && !newWorldFocus) {
      if (c =/= 0) {
        newWorldName.typeKey(c)
        repaint()
      }

      if (keyCode === 8) {
        newWorldName.deleteKey()
        repaint()
      }
    }

    if (keyCode === KeyEvent.VK_EQUALS && layer < 2) {
      layer += 1
    }
    if (keyCode === KeyEvent.VK_MINUS && layer > 0) {
      layer -= 1
    }
  }

  def keyReleased(key: KeyEvent): Unit = {
    val keyCode = key.getKeyCode
    if (keyCode === KeyEvent.VK_LEFT || keyCode === KeyEvent.VK_A) {
      userInput.setLeftKeyPressed(false)
    }
    if (keyCode === KeyEvent.VK_RIGHT || keyCode === KeyEvent.VK_D) {
      userInput.setRightKeyPressed(false)
    }
    if (keyCode === KeyEvent.VK_UP || keyCode === KeyEvent.VK_W) {
      userInput.setUpKeyPressed(false)
    }
    if (keyCode === KeyEvent.VK_SHIFT) {
      userInput.setShiftKeyPressed(false)
    }
    if (keyCode === KeyEvent.VK_DOWN || keyCode === KeyEvent.VK_S) {
      userInput.setDownKeyPressed(false)
    }
  }

  def mousePressed(e: MouseEvent): Unit = {
    val button = e.getButton
    if (button === MouseEvent.BUTTON1) {
      userInput.setLeftMousePressed(true)
    } else if (button === MouseEvent.BUTTON3) {
      userInput.setRightMousePressed(true)
    }
  }

  def mouseReleased(e: MouseEvent): Unit = {
    val button = e.getButton
    if (button === MouseEvent.BUTTON1) {
      userInput.setLeftMousePressed(false)
    } else if (button === MouseEvent.BUTTON3) {
      userInput.setRightMousePressed(false)
    }
    menuPressed = false
  }

  def mouseClicked(e: MouseEvent): Unit = {
    //
  }

  def mouseMoved(e: MouseEvent): Unit = {
    userInput.setCurrentMousePosition(e.getX, e.getY)
  }

  def mouseDragged(e: MouseEvent): Unit = {
    userInput.setCurrentMousePosition(e.getX, e.getY)
  }

  override def getPreferredSize(): Dimension = {
    new Dimension(800, 600)
  }

  def keyTyped(key: KeyEvent): Unit = {
    //
  }

  def stateChanged(e: ChangeEvent): Unit = {
    //
  }

  def mouseEntered(e: MouseEvent): Unit = {
    //
  }

  def mouseExited(e: MouseEvent): Unit = {
    //
  }

  def mouseWheelMoved(e: MouseWheelEvent): Unit = {
    //
  }

}
