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
import Block._

import org.terraframe.Images.loadImage

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.math._
import scala.util.Random
import scala.util.control.NonFatal
import org.terraframe.{ MathHelper => mh }
import TypeSafeComparisons._
import Layer._

object TerraFrame {
  private val logger = org.log4s.getLogger

  val config: GraphicsConfiguration =
    GraphicsEnvironment.getLocalGraphicsEnvironment.getDefaultScreenDevice.getDefaultConfiguration
  val armor: ItemCollection = new ItemCollection(Armor)
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
    "valnished_wood",
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
    false, false, false, false, false, false, true, true, false, false, false, false, false, false, false, false,
    false, false, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
    true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, true, true, true, true, true, false, false,
    false, false, false, false, false, true, true, true, true, true, true, true, true, true, true, false, false, false,
    false, false, false, false, false, false, true, true, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false)
  val solid: Array[Boolean] = Array(false, true, true, true, true, true, true, true, true, true, true, true, true,
    true, true, true, false, true, true, true, false, false, false, true, false, false, false, false, false, false,
    false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, true, true, true, true, true, false, false, false, true, true,
    true, true, true, true, true, true, true, true, true, true, true, true, false, false, false, false, false, false,
    false, false, false, true, true, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false)
  val ltrans: Array[Boolean] = Array(false, true, true, true, true, true, true, true, false, false, false, false,
    false, false, false, false, false, false, true, true, false, false, false, false, false, false, false, false,
    false, false, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
    true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, true, true, true, true, true, false, false,
    false, false, false, false, false, true, true, true, true, true, true, true, false, false, false, false, false,
    false, false, false, false, false, false, false, true, true, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false)
  val conducts: Array[Double] = Array(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0, 0, 0, -1, -1, -1,
    0, -1, 0, -1, 0, -1, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
  val receives: Array[Boolean] = Array(false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    false, false, false, true, true, true, true, true, true, false, false, false, true, true, false, false, false,
    false, false, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
    true, false, false, false, false, false, false, false, false, false, false, true, true, true, true, true, true,
    true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
    true, true, true, true, true, true, true)
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
            logger.error(s"(ERROR) Could not load block graphic '${blocknames(i)}'.")
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

//  lazy val BLOCKLIGHTS: Map[Int, Int] = {
//    val blockLightsTemp = new jul.HashMap[Int, Int](blocknames.length)
//
//    blocknames.indices.foreach { i =>
//      blockLightsTemp.put(i, 0)
//    }
//
//    blockLightsTemp.put(19, 21)
//    blockLightsTemp.put(20, 15)
//    blockLightsTemp.put(21, 18)
//    blockLightsTemp.put(22, 21)
//    blockLightsTemp.put(23, 15)
//    blockLightsTemp.put(24, 15)
//    blockLightsTemp.put(25, 15)
//    blockLightsTemp.put(26, 18)
//    blockLightsTemp.put(27, 18)
//    blockLightsTemp.put(28, 21)
//    blockLightsTemp.put(29, 21)
//    blockLightsTemp.put(36, 15)
//    blockLightsTemp.put(36, 15)
//    blockLightsTemp.put(38, 18)
//    blockLightsTemp.put(51, 15)
//    blockLightsTemp.put(52, 15)
//    blockLightsTemp.put(53, 15)
//    blockLightsTemp.put(95, 6)
//    blockLightsTemp.put(96, 7)
//    blockLightsTemp.put(97, 8)
//    blockLightsTemp.put(98, 9)
//    blockLightsTemp.put(99, 10)
//    blockLightsTemp.put(100, 12)
//    blockLightsTemp.put(101, 12)
//    blockLightsTemp.put(102, 12)
//    blockLightsTemp.put(104, 21)
//    blockLightsTemp.put(112, 12)
//    blockLightsTemp.put(114, 12)
//    blockLightsTemp.put(116, 12)
//    blockLightsTemp.put(118, 12)
//    blockLightsTemp.put(123, 12)
//    blockLightsTemp.put(124, 12)
//    blockLightsTemp.put(125, 12)
//    blockLightsTemp.put(126, 12)
//
//    blockLightsTemp.put(137, 12)
//    blockLightsTemp.put(138, 12)
//    blockLightsTemp.put(139, 12)
//    blockLightsTemp.put(140, 12)
//    blockLightsTemp.put(145, 12)
//    blockLightsTemp.put(146, 12)
//    blockLightsTemp.put(147, 12)
//    blockLightsTemp.put(148, 12)
//    blockLightsTemp.put(153, 12)
//    blockLightsTemp.put(154, 12)
//    blockLightsTemp.put(155, 12)
//    blockLightsTemp.put(156, 12)
//    blockLightsTemp.put(161, 12)
//    blockLightsTemp.put(162, 12)
//    blockLightsTemp.put(163, 12)
//    blockLightsTemp.put(164, 12)
//
//    blockLightsTemp.asScala.toMap
//  }
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
  lazy val WIREP: Map[Int, Block] = { // TODO: not sure the key here
    val wirepTemp = new jul.HashMap[Int, Block](10)

    wirepTemp.put(0, ZythiumWireBlock)
    wirepTemp.put(1, ZythiumWire1PowerBlock)
    wirepTemp.put(2, ZythiumWire2PowerBlock)
    wirepTemp.put(3, ZythiumWire3PowerBlock)
    wirepTemp.put(4, ZythiumWire4PowerBlock)
    wirepTemp.put(5, ZythiumWire5PowerBlock)

    wirepTemp.asScala.toMap
  }
  lazy val TORCHESL: Map[Block, Block] = {
    val torcheslTemp = new jul.HashMap[Block, Block](10)

    torcheslTemp.put(WoodenTorchBlock, WoodenTorchLeftWallBlock)
    torcheslTemp.put(CoalTorchBlock, CoalTorchLeftWallBlock)
    torcheslTemp.put(LumenstoneTorchBlock, LumenstoneTorchLeftWallBlock)
    torcheslTemp.put(ZythiumTorchBlock, ZythiumTorchLeftWallBlock)
    torcheslTemp.put(LeverBlock, LeverLeftWallBlock)
    torcheslTemp.put(LeverOnBlock, LeverLeftWallOnBlock)
    torcheslTemp.put(ButtonLeftBlock, ButtonLeftBlock)
    torcheslTemp.put(ButtonLeftOnBlock, ButtonLeftOnBlock)

    torcheslTemp.asScala.toMap
  }
  lazy val TORCHESR: Map[Block, Block] = {
    val torchesrTemp = new jul.HashMap[Block, Block](10)

    torchesrTemp.put(WoodenTorchBlock, WoodenTorchRightWallBlock)
    torchesrTemp.put(CoalTorchBlock, CoalTorchRightWallBlock)
    torchesrTemp.put(LumenstoneTorchBlock, LumenstoneTorchRightWallBlock)
    torchesrTemp.put(ZythiumTorchBlock, ZythiumTorchRightWallBlock)
    torchesrTemp.put(LeverBlock, LeverLightWallBlock)
    torchesrTemp.put(LeverOnBlock, LeverRightWallOnBlock)
    torchesrTemp.put(ButtonLeftBlock, ButtonRightBlock)

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
    extends JPanel
    with ChangeListener
    with KeyListener
    with MouseListener
    with MouseMotionListener
    with MouseWheelListener {

  import TerraFrame._
  import GraphicsHelper._
  import Biome._
  import Block._

  var cic: Option[ItemCollection]   = None
  var screen: Option[BufferedImage] = None
  var backgroundColor: Color        = Color.BLACK

  val cl: Array2D[Int] = Array(Array(-1, 0), Array(1, 0), Array(0, -1), Array(0, 1))

  val mainthread: Action = new AbstractAction() {
    def actionPerformed(ae: ActionEvent): Unit = {
      try {
        if (ready) {
          ready = false
          val uNew: Int = ((player.x - getWidth / 2 + Player.width) / CHUNKSIZE.toDouble).toInt
          val vNew: Int = ((player.y - getHeight / 2 + Player.height) / CHUNKSIZE.toDouble).toInt
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
              (0 until 2).foreach { twx =>
                breakable {
                  chunkTemp.toList.zipWithIndex.reverse.foreach { p =>
                    val c     = p._1
                    val index = p._2
                    if (c.cx === twx && c.cy === twy) {
                      chunkMatrix(twy)(twx) = Some(c)
                      chunkTemp.remove(index)
                      break
                    }
                  }
                }
                if (chunkMatrix(twy)(twx).isEmpty) {
                  temporarySaveFile(twy)(twx).fold {
                    logger.info("no save file")
                    chunkMatrix(twy)(twx) = Some(Chunk(twx + ou, twy + ov, random))
                  } { c =>
                    logger.info("using save file")
                    chunkMatrix(twy)(twx) = Some(c)
                  }
                }
              }
            }
            chunkTemp.foreach { chunk =>
              temporarySaveFile(1)(1) = Some(chunk) // TODO: not sure about what this does, used to be twy, twx
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
          var someval: Boolean = false
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
                worlds(twy)(twx) = worlds(twy)(twx)
                  .orElse(Some(config.createCompatibleImage(CHUNKSIZE, CHUNKSIZE, Transparency.TRANSLUCENT)))
                fworlds(twy)(twx) = fworlds(twy)(twx)
                  .orElse(Some(config.createCompatibleImage(CHUNKSIZE, CHUNKSIZE, Transparency.TRANSLUCENT)))

                worlds(twy)(twx).foreach { w =>
                  fworlds(twy)(twx).foreach { fw =>
                    val wg2: Graphics2D  = w.createGraphics()
                    val fwg2: Graphics2D = fw.createGraphics()
                    (max(twy * CHUNKSIZE, (player.iy - getHeight / 2 + Player.height / 2 + v * BLOCKSIZE) - 64) until (min(
                      twy * CHUNKSIZE + CHUNKSIZE,
                      (player.iy + getHeight / 2 - Player.height / 2 + v * BLOCKSIZE) + 128), BLOCKSIZE))
                      .foreach { tly =>
                        (max(twx * CHUNKSIZE, (player.ix - getWidth / 2 + Player.width / 2 + u * BLOCKSIZE) - 64) until (min(
                          twx * CHUNKSIZE + CHUNKSIZE,
                          (player.ix + getWidth / 2 - Player.width / 2 + u * BLOCKSIZE) + 112), BLOCKSIZE))
                          .foreach { tlx =>
                            val tx: Int = tlx / BLOCKSIZE
                            val ty: Int = tly / BLOCKSIZE
                            if (tx >= 0 && tx < theSize && ty >= 0 && ty < theSize) {
                              if (!drawn(ty)(tx)) {
                                someval = true
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
                                  if (blocks(l)(ty)(tx) =/= AirBlock) {
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
                                  if (wcnct(ty)(tx) && blocks(l)(ty)(tx).id >= ZythiumWireBlock.id && blocks(l)(ty)(tx).id <= ZythiumWire5PowerBlock.id) {
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
                                someval = true
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
                                  if (blocks(l)(ty)(tx) =/= AirBlock) {
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
                                  if (wcnct(ty)(tx) && blocks(l)(ty)(tx).id >= ZythiumWireBlock.id && blocks(l)(ty)(tx).id <= ZythiumWire5PowerBlock.id) {
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
                                someval = true
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
                                  if (blocks(l)(ty)(tx) =/= AirBlock) {
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
                                  if (wcnct(ty)(tx) && blocks(l)(ty)(tx).id >= ZythiumWireBlock.id && blocks(l)(ty)(tx).id <= ZythiumWire5PowerBlock.id) {
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
          if (someval) {
            logger.debug("Drew at least one block.")
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
                logger.debug(s"Destroyed image at $twx $twy")
              }
            }
          }
          updateApp()
          updateEnvironment()
          player.update(blocks(PrimaryLayer.num), userInput, u, v)
          if (timeOfDay >= 86400) {
            timeOfDay = 0
            day += 1
          }
          repaint()
          ready = true
        }
      } catch {
        case NonFatal(e) => logger.error(e)("Error reacting to ActionEvent")
      }
    }
  }
  val timer: javax.swing.Timer = new javax.swing.Timer(20, mainthread)
  val actionListener = new ActionListener() {
    def actionPerformed(ae: ActionEvent): Unit = {
      try {
        if (userInput.isLeftMousePressed) {

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
                  currentWorld = Some(worldNames(i))
                  state = LoadingWorld
                  backgroundColor = Color.BLACK
                  if (loadWorld(worldFiles(i))) {
                    menuTimer.stop()
                    backgroundColor = CYANISH
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
                  backgroundColor = Color.BLACK
                  state = GeneratingWorld
                  currentWorld = Some(newWorldName.text)
                  repaint()
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
        case NonFatal(e) => logger.error(e)("Error reacting to menu input")
      }
    }
  }
  val menuTimer: javax.swing.Timer         = new javax.swing.Timer(20, actionListener)
  var worldFiles, worldNames: List[String] = Nil
  var currentWorld: Option[String]         = None
  val newWorldName: TextField              = TextField(400, "New World")

  val blocks: Array3D[Block]             = Array.ofDim(3, theSize, theSize)
  val blockds: Array3D[OutlineDirection] = Array.ofDim(3, theSize, theSize)
  val blockdns: Array2D[Byte]            = Array.ofDim(theSize, theSize)
  val blockbgs: Array2D[Background]      = Array.ofDim(theSize, theSize)
  val blockts: Array2D[Byte]             = Array.ofDim(theSize, theSize)
  val lights: Array2D[Float]             = Array.ofDim(theSize, theSize)
  val power: Array3D[Float]              = Array.ofDim(3, theSize, theSize)
  val lsources: Array2D[Boolean]         = Array.ofDim(theSize, theSize)
  val lqx: mutable.ArrayBuffer[Int]      = mutable.ArrayBuffer.empty[Int]
  val lqy: mutable.ArrayBuffer[Int]      = mutable.ArrayBuffer.empty[Int]
  val pqx: mutable.ArrayBuffer[Int]      = mutable.ArrayBuffer.empty[Int]
  val pqy: mutable.ArrayBuffer[Int]      = mutable.ArrayBuffer.empty[Int]
  val zqx: mutable.ArrayBuffer[Int]      = mutable.ArrayBuffer.empty[Int]
  val zqy: mutable.ArrayBuffer[Int]      = mutable.ArrayBuffer.empty[Int]
  val pzqx: mutable.ArrayBuffer[Int]     = mutable.ArrayBuffer.empty[Int]
  val pzqy: mutable.ArrayBuffer[Int]     = mutable.ArrayBuffer.empty[Int]
  val lqd: Array2D[Boolean]              = Array.fill(theSize, theSize)(false)
  val zqd: Array2D[Boolean]              = Array.fill(theSize, theSize)(false)
  val pqd: Array2D[Boolean]              = Array.fill(theSize, theSize)(false)
  val pzqd: Array2D[Boolean]             = Array.fill(theSize, theSize)(false)
  val zqn: Array2D[Byte]                 = Array.ofDim(theSize, theSize)
  val pzqn: Array3D[Byte]                = Array.ofDim(3, theSize, theSize)
  val arbprd: Array3D[Boolean]           = Array.ofDim(3, theSize, theSize)

  val updatex: mutable.ArrayBuffer[Int]   = mutable.ArrayBuffer.empty[Int]
  val updatey: mutable.ArrayBuffer[Int]   = mutable.ArrayBuffer.empty[Int]
  val updatet: mutable.ArrayBuffer[Int]   = mutable.ArrayBuffer.empty[Int]
  val updatel: mutable.ArrayBuffer[Layer] = mutable.ArrayBuffer.empty[Layer]
  val wcnct: Array2D[Boolean]             = Array.ofDim(theSize, theSize)
  val drawn: Array2D[Boolean]             = Array.ofDim(theSize, theSize)
  val ldrawn: Array2D[Boolean]            = Array.ofDim(theSize, theSize)
  val rdrawn: Array2D[Boolean]            = Array.ofDim(theSize, theSize)
  var player: Player                      = new Player(WIDTH * 0.5 * BLOCKSIZE, 45)
  var inventory: Inventory                = new Inventory()

  val entities: mutable.ArrayBuffer[Entity] = mutable.ArrayBuffer.empty[Entity]
  val cloudsx: mutable.ArrayBuffer[Double]  = mutable.ArrayBuffer.empty[Double]
  val cloudsy: mutable.ArrayBuffer[Double]  = mutable.ArrayBuffer.empty[Double]
  val cloudsv: mutable.ArrayBuffer[Double]  = mutable.ArrayBuffer.empty[Double]
  val cloudsn: mutable.ArrayBuffer[Int]     = mutable.ArrayBuffer.empty[Int]
  val machinesx: mutable.ArrayBuffer[Int]   = mutable.ArrayBuffer.empty[Int]
  val machinesy: mutable.ArrayBuffer[Int]   = mutable.ArrayBuffer.empty[Int]

  val temporarySaveFile: Array2D[Option[Chunk]] = Array.fill(2, 2)(None)
  val chunkMatrix: Array2D[Option[Chunk]]       = Array.fill(2, 2)(None)

  var rgnc1: Int     = 0
  var rgnc2: Int     = 0
  var layer: Layer   = PrimaryLayer
  var iclayer: Layer = PrimaryLayer

  var state: GameState                 = LoadingGraphics
  var mobSpawn: Option[EntityStrategy] = None

  private[this] var width, height                         = 0
  var u, v: Int                                           = 0
  var ux, uy, mx, my, vc, xpos, ypos, x2, y2, mining: Int = 0
  private[this] var x, y                                  = 0

  var miningTool: UiItem = EmptyUiItem

  var moveItem: UiItem                   = EmptyUiItem
  var moveItemTemp: UiItem               = EmptyUiItem
  var moveNum, moveDur: Short            = 0
  var msi, ou, ov, icx, icy, immune: Int = 0

  var toolAngle: Double = 4.7
  var toolSpeed: Double = UiItem.defaultSpeed

  var timeOfDay: Double    = 0 // 28000 (before dusk), 32000 (after dusk)
  var currentSkyLight: Int = 28800
  var day: Int             = 0

  var mobCount: Int = 0

  val loadTextPos: Int = 0

  val createWorldThread: Action = new AbstractAction() {
    def actionPerformed(ae: ActionEvent): Unit = {
      try {
        createNewWorld()
        backgroundColor = CYANISH
        state = InGame
        ready = true
        timer.start()
        createWorldTimer.stop()
      } catch {
        case NonFatal(e) => logger.error(e)("Error reacting to create user input")
      }
    }
  }
  val createWorldTimer: javax.swing.Timer = new javax.swing.Timer(1, createWorldThread)

  val userInput = UserInput()

  val done: Boolean                  = false
  var ready: Boolean                 = true
  var showTool: Boolean              = false
  var showInv: Boolean               = false
  var checkBlocks: Boolean           = true
  var mouseClicked: Boolean          = true
  var mouseClicked2: Boolean         = true
  var mouseNoLongerClicked: Boolean  = false
  var mouseNoLongerClicked2: Boolean = false
  val removeSources: Boolean         = false
  val beginLight: Boolean            = false
  var doMobSpawn: Boolean            = false
  var keepLeaf: Boolean              = false
  val newWorldFocus: Boolean         = false
  var menuPressed: Boolean           = false
  var doGenerateWorld: Boolean       = true
  val reallyAddPower: Boolean        = false
  val reallyRemovePower: Boolean     = false

  var ic: Option[ItemCollection] = None

  val worlds: Array2D[Option[BufferedImage]]  = Array.fill(2, 2)(None)
  val fworlds: Array2D[Option[BufferedImage]] = Array.fill(2, 2)(None)
  val kworlds: Array2D[Boolean]               = Array.fill(2, 2)(false)

  val icmatrix: Array3D[Option[ItemCollection]] = Array.ofDim(3, HEIGHT, WIDTH)

  var tool: Option[BufferedImage] = None

  def init(): Unit = {
    try {
      setLayout(new BorderLayout())

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
      //BLOCKLIGHTS.size
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

      backgroundColor = CYANISH
      state = TitleScreen

      repaint()

      menuTimer.start()
    } catch {
      case NonFatal(e) => logger.error(e)("Error initializing")
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

    inventory.renderCollection(armor)

    toolAngle = 4.7

    logger.info("-> Adding light sources...")

    (0 until WIDTH).foreach { x =>
      //            addSunLighting(x, 0)
    }

    (0 until WIDTH).foreach { x =>
      (0 until HEIGHT).foreach { y =>
        //                addBlockLighting(x, y)
        //                addBlockPower(x, y)
      }
    }

    logger.info("-> Calculating light...")

    resolvePowerMatrix()
    resolveLightMatrix()

    logger.info("Finished generation.")
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
      backgroundColor = Color.BLACK
    } else {
      SKYCOLORS.get(currentSkyLight).foreach { s =>
        backgroundColor = s
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

    def updateItemCollection(l: Int, y: Int, x: Int)(ic: ItemCollection): ItemCollection = {
      if (ic.icType === Furnace) {
        if (ic.furnaceOn) {

          val icUpdated1: ItemCollection = {
            if (ic.ids(1) === EmptyUiItem) {
              FUELS
                .get(ic.ids(2).id)
                .fold {
                  removeBlockLighting(x, y)
                  blocks(l)(y)(x) = FurnaceBlock
                  rdrawn(y)(x) = false
                  ic.copy(furnaceOn = false)
                } { _ =>
                  inventory.addLocationIC(ic, 1, ic.ids(2), 1.toShort)
                  inventory.removeLocationIC(ic, 2, 1.toShort)
                  ic.copy(fuelPower = 1)
                }
            } else {
              ic
            }
          }

          val icUpdated2: ItemCollection = {
            FUELS
              .get(icUpdated1.ids(1).id)
              .fold(icUpdated1) { fuel =>
                val updatedFuelPower = {
                  val fuelPowerTemp = icUpdated1.fuelPower - fuel
                  if (fuelPowerTemp < 0.0d) {
                    inventory.removeLocationIC(icUpdated1, 1, icUpdated1.nums(1))
                    0.0d
                  } else {
                    fuelPowerTemp
                  }
                }

                val maybeSpeed = FSPEED.get(icUpdated1.ids(1).id)
                val maybeIndex = FRI1.indices.find { i =>
                  icUpdated1.ids(0) === FRI1(i) && icUpdated1.nums(0) >= FRN1(i)
                }

                val maybeUpdatedSmeltPower = for {
                  fspeed <- maybeSpeed
                  i      <- maybeIndex
                } yield {
                  val smeltPowerTemp = icUpdated1.smeltPower + fspeed
                  if (smeltPowerTemp > 1.0d) {
                    inventory.removeLocationIC(icUpdated1, 0, FRN1(i))
                    inventory.addLocationIC(icUpdated1, 3, FRI2(i), FRN2(i))
                    0.0d
                  } else {
                    smeltPowerTemp
                  }
                }

                val updatedSmeltPower = maybeUpdatedSmeltPower.getOrElse(icUpdated1.smeltPower)

                icUpdated1
                  .copy(
                    fuelPower = updatedFuelPower,
                    smeltPower = updatedSmeltPower
                  )
              }
          }

          icUpdated2

        } else {
          val smeltTemp         = ic.smeltPower - 0.00025
          val updatedSmeltPower = math.min(0, smeltTemp)
          ic.copy(smeltPower = updatedSmeltPower)
        }
      } else {
        ic
      }
    }

    machinesx.indices.foreach { j =>
      x = machinesx(j)
      y = machinesy(j)
      (0 until 3).foreach { l =>
        icmatrix(l)(y)(x) = icmatrix(l)(y)(x).map(updateItemCollection(l, y, x))
      }
    }

    ic = ic.map(updateItemCollection(iclayer.num, icy, icx))

    if (sqrt(
          pow(player.x + player.image.getWidth() - icx * BLOCKSIZE + BLOCKSIZE / 2, 2) + pow(
            player.y + player.image.getHeight() - icy * BLOCKSIZE + BLOCKSIZE / 2,
            2)) > 160) {
      ic.foreach { icTemp =>
        if (icTemp.icType =/= Workbench) {
          machinesx += icx
          machinesy += icy
          icmatrix(iclayer.num)(icy)(icx) =
            Some(new ItemCollection(icTemp.icType, icTemp.ids, icTemp.nums, icTemp.durs))
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
          icmatrix(iclayer.num)(icy)(icx) = icmatrix(iclayer.num)(icy)(icx).map { icMatrixTemp =>
            icMatrixTemp.copy(
              fuelPower = icTemp.fuelPower,
              smeltPower = icTemp.smeltPower,
              furnaceOn = icTemp.furnaceOn
            )
          }

        }
        ic = None

      }
    }

    (0 until 3).foreach { l =>
      (0 until theSize).foreach { y =>
        (0 until theSize).foreach { x =>
          if (random.nextInt(22500) === 0) {

            def oneInThreeChance =
              random.nextInt(3) === 0 // TODO: need module to handle randomness like this in more readable way
            lazy val blockBiome = checkBiome(x, y, u, v, blocks, blockbgs)
            lazy val daytime    = timeOfDay >= 75913 || timeOfDay < 28883 // TODO: module to handle time of day
            lazy val nighttime  = timeOfDay >= 32302 && timeOfDay < 72093

            val nextStageGrowingPlantBlock: Block = blocks(l)(y)(x) match {
              case SunflowerStage1Block if daytime                             => SunflowerStage2Block
              case SunflowerStage2Block if daytime                             => SunflowerStage3Block
              case MoonflowerStage1Block if nighttime                          => MoonflowerStage2Block
              case MoonflowerStage2Block if nighttime                          => MoonflowerStage3Block
              case DryweedStage1Block if blockBiome === DesertBiome            => DryweedStage2Block
              case DryweedStage2Block if blockBiome === DesertBiome            => DryweedStage3Block
              case GreenleafStage1Block if blockBiome === JungleBiome          => GreenleafStage2Block
              case GreenleafStage2Block if blockBiome === JungleBiome          => GreenleafStage3Block
              case FrostleafStage1Block if blockBiome === FrostBiome           => FrostleafStage2Block
              case FrostleafStage2Block if blockBiome === FrostBiome           => FrostleafStage3Block
              case CaverootStage1Block if blockBiome === CavernBiome || y >= 0 => CaverootStage2Block
              case CaverootStage2Block if blockBiome === CavernBiome || y >= 0 => CaverootStage3Block
              case SkyblossomStage1Block if y <= HEIGHT * 0.08 && oneInThreeChance || y <= HEIGHT * 0.04 =>
                SkyblossomStage2Block
              case SkyblossomStage2Block if y <= HEIGHT * 0.08 && oneInThreeChance || y <= HEIGHT * 0.04 =>
                SkyblossomStage3Block
              case VoidRotStage1Block if y >= HEIGHT * 0.98          => VoidRotStage2Block
              case VoidRotStage2Block if y >= HEIGHT * 0.98          => VoidRotStage3Block
              case MarshleafStage1Block if blockBiome === SwampBiome => MarshleafStage2Block
              case MarshleafStage2Block if blockBiome === SwampBiome => MarshleafStage3Block
              case _                                                 => AirBlock
            }
            if (nextStageGrowingPlantBlock =/= AirBlock) {
              blocks(l)(y)(x) = nextStageGrowingPlantBlock
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

              val currentBlock = blocks(l)(y)(x)

              lazy val isOpen  = hasOpenSpace(x + u, y + v, l)
              lazy val randomY = y + random.nextInt(3) - 1 + u

              lazy val randomX     = x + random.nextInt(3) - 1 + v
              lazy val randomBlock = blocks(l)(randomY)(randomX)

              val (updatedBlock, doGrassGrow): (Block, Boolean) = currentBlock match {
                case DirtBlock if isOpen && randomBlock === GrassBlock       => (GrassBlock, true)
                case DirtBlock if isOpen && randomBlock === JungleGrassBlock => (JungleGrassBlock, true)
                case MudBlock if isOpen && randomBlock === SwampGrassBlock   => (SwampGrassBlock, true)
                case _                                                       => (currentBlock, false)
              }

              blocks(l)(y)(x) = updatedBlock

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
          if (blocks(PrimaryLayer.num)(y)(x) === TreeNoBarkBlock) {
            blocks(PrimaryLayer.num)(y)(x) = TreeBlock
          }
        }
      }
    }

    (updatex.length - 1 until (-1, -1)).foreach { i =>
      updatet.update(i, updatet(i) - 1)
      if (updatet(i) <= 0) {
        if (blocks(updatel(i).num)(updatey(i))(updatex(i)) === ButtonLeftOnBlock) {
          //blockTemp = blocks(updatel(i).num)(updatey(i))(updatex(i)) TODO: Doesn't seem to be used
          removeBlockPower(updatex(i), updatey(i), updatel(i))
          blocks(updatel(i).num)(updatey(i))(updatex(i)) = ButtonLeftBlock
          rdrawn(updatey(i))(updatex(i)) = false
        } else if (blocks(updatel(i).num)(updatey(i))(updatex(i)) === ButtonRightOnBlock) {
          //blockTemp = blocks(updatel(i).num)(updatey(i))(updatex(i)) TODO: Doesn't seem to be used
          removeBlockPower(updatex(i), updatey(i), updatel(i))
          blocks(updatel(i).num)(updatey(i))(updatex(i)) = ButtonRightBlock
          rdrawn(updatey(i))(updatex(i)) = false
        } else if (blocks(updatel(i).num)(updatey(i))(updatex(i)) === WoodenPressurePlateOnBlock) {
          //blockTemp = blocks(updatel(i).num)(updatey(i))(updatex(i)) TODO: Doesn't seem to be used
          removeBlockPower(updatex(i), updatey(i), updatel(i))
          blocks(updatel(i).num)(updatey(i))(updatex(i)) = WoodenPressurePlateBlock
          rdrawn(updatey(i))(updatex(i)) = false
        } else if (blocks(updatel(i).num)(updatey(i))(updatex(i)) === StonePressurePlateOnBlock) {
          //blockTemp = blocks(updatel(i).num)(updatey(i))(updatex(i)) TODO: Doesn't seem to be used
          removeBlockPower(updatex(i), updatey(i), updatel(i))
          blocks(updatel(i).num)(updatey(i))(updatex(i)) = StonePressurePlateBlock
          rdrawn(updatey(i))(updatex(i)) = false
        } else if (blocks(updatel(i).num)(updatey(i))(updatex(i)) === ZythiumPressurePlateOnBlock) {
//          blockTemp = blocks(updatel(i).num)(updatey(i))(updatex(i))  TODO: Doesn't seem to be used
          removeBlockPower(updatex(i), updatey(i), updatel(i))
          blocks(updatel(i).num)(updatey(i))(updatex(i)) = ZythiumPressurePlateBlock
          rdrawn(updatey(i))(updatex(i)) = false
        } else if (blocks(updatel(i).num)(updatey(i))(updatex(i)).id >= ZythiumDelayer1DelayRightOnBlock.id && blocks(
                     updatel(i).num)(updatey(i))(updatex(i)).id <= ZythiumDelayer1DelayUpOnBlock.id || blocks(
                     updatel(i).num)(updatey(i))(updatex(i)).id >= ZythiumDelayer2DelayRightOnBlock.id && blocks(
                     updatel(i).num)(updatey(i))(updatex(i)).id <= ZythiumDelayer2DelayUpOnBlock.id ||
                   blocks(updatel(i).num)(updatey(i))(updatex(i)).id >= ZythiumDelayer4DelayRightOnBlock.id && blocks(
                     updatel(i).num)(updatey(i))(updatex(i)).id <= ZythiumDelayer4DelayUpOnBlock.id || blocks(
                     updatel(i).num)(updatey(i))(updatex(i)).id >= ZythiumDelayer8DelayRightOnBlock.id && blocks(
                     updatel(i).num)(updatey(i))(updatex(i)).id <= ZythiumDelayer8DelayUpOnBlock.id) {
          logger.debug("(DEBUG2R)")
//          blockTemp = blocks(updatel(i).num)(updatey(i))(updatex(i))  TODO: Doesn't seem to be used
          removeBlockPower(updatex(i), updatey(i), updatel(i), false)
          blocks(updatel(i).num)(updatey(i))(updatex(i)) =
            Block.withId(blocks(updatel(i).num)(updatey(i))(updatex(i)).id - 4)
          rdrawn(updatey(i))(updatex(i)) = false
        } else if (blocks(updatel(i).num)(updatey(i))(updatex(i)).id >= ZythiumDelayer1DelayRightBlock.id && blocks(
                     updatel(i).num)(updatey(i))(updatex(i)).id <= ZythiumDelayer1DelayUpBlock.id || blocks(
                     updatel(i).num)(updatey(i))(updatex(i)).id >= ZythiumDelayer2DelayRightBlock.id && blocks(
                     updatel(i).num)(updatey(i))(updatex(i)).id <= ZythiumDelayer2DelayUpBlock.id ||
                   blocks(updatel(i).num)(updatey(i))(updatex(i)).id >= ZythiumDelayer4DelayRightBlock.id && blocks(
                     updatel(i).num)(updatey(i))(updatex(i)).id <= ZythiumDelayer4DelayUpBlock.id || blocks(
                     updatel(i).num)(updatey(i))(updatex(i)).id >= ZythiumDelayer8DelayRightBlock.id && blocks(
                     updatel(i).num)(updatey(i))(updatex(i)).id <= ZythiumDelayer8DelayUpBlock.id) {
          logger.debug("(DEBUG2A)")
          blocks(updatel(i).num)(updatey(i))(updatex(i)) =
            Block.withId(blocks(updatel(i).num)(updatey(i))(updatex(i)).id + 4)
          power(updatel(i).num)(updatey(i))(updatex(i)) = 5.toFloat
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
                val xpos2: Int = ax + random.nextInt(20) - 10
                val ypos2: Int = ay + random.nextInt(20) - 10
                if (xpos > 0 && xpos < WIDTH - 1 && ypos > 0 && ypos < HEIGHT - 1 && (blocks(PrimaryLayer.num)(ypos)(
                      xpos) === AirBlock || !blockcds(blocks(PrimaryLayer.num)(ypos)(xpos).id) &&
                    xpos2 > 0 && xpos2 < WIDTH - 1 && ypos2 > 0 && ypos2 < HEIGHT - 1 && blocks(PrimaryLayer.num)(
                      ypos2)(xpos2) =/= AirBlock && blockcds(blocks(PrimaryLayer.num)(ypos2)(xpos2).id))) {
                  mobSpawn = None
                  if (checkBiome(xpos, ypos, u, v, blocks, blockbgs) =/= CavernBiome) {
                    if ((day =/= 0 || DEBUG_HOSTILE > 1) && (timeOfDay >= 75913 || timeOfDay < 28883)) {
                      if (random.nextInt(350) === 0) {
                        val rnum = random.nextInt(100)
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
                        val rnum = random.nextInt(100)
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
                      val rnum = random.nextInt(100)
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

                  val (xmax, ymax): (Int, Int) = {
                    if (mobSpawn.contains(BlueBubble) ||
                        mobSpawn.contains(GreenBubble) ||
                        mobSpawn.contains(RedBubble) ||
                        mobSpawn.contains(YellowBubble) ||
                        mobSpawn.contains(BlackBubble) ||
                        mobSpawn.contains(WhiteBubble)) {
                      (2, 2)
                    } else if (mobSpawn.contains(Zombie)) {
                      (2, 3)
                    } else if (mobSpawn.contains(ArmoredZombie)) {
                      (2, 3)
                    } else if (mobSpawn.contains(ShootingStar)) {
                      (2, 2)
                    } else if (mobSpawn.contains(Sandbot)) {
                      (2, 2)
                    } else if (mobSpawn.contains(Snowman)) {
                      (2, 3)
                    } else if (mobSpawn.contains(Bat)) {
                      (1, 1)
                    } else if (mobSpawn.contains(Bee)) {
                      (1, 1)
                    } else if (mobSpawn.contains(Skeleton)) {
                      (1, 3)
                    } else {
                      (0, 0)
                    }
                  }

                  doMobSpawn = true
                  ((xpos / BLOCKSIZE) until (xpos / BLOCKSIZE + xmax)).foreach { x =>
                    ((ypos / BLOCKSIZE) until (ypos / BLOCKSIZE + ymax)).foreach { y =>
                      if (y > 0 && y < HEIGHT - 1 && blocks(PrimaryLayer.num)(y)(x) =/= AirBlock && blockcds(
                            blocks(PrimaryLayer.num)(y)(x).id)) {
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
                  val moveNumTemp = inventory.nums(uy * 10 + ux)
                  val moveDurTemp = inventory.durs(uy * 10 + ux)
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
                  val moveNumTemp = c.nums(uy * 2 + ux)
                  val moveDurTemp = c.durs(uy * 2 + ux)

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
        ic = ic.map { icTemp =>
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
                    val moveNumTemp = icTemp.nums(uy * 3 + ux)
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
            icTemp
          } else if (icTemp.icType === WoodenChest || icTemp.icType === StoneChest ||
                     icTemp.icType === CopperChest || icTemp.icType === IronChest ||
                     icTemp.icType === SilverChest || icTemp.icType === GoldChest ||
                     icTemp.icType === ZincChest || icTemp.icType === RhymestoneChest ||
                     icTemp.icType === ObduriteChest) { //TODO: chest trait? use ChestItemCollectionType?
            (0 until icTemp.icType.CX).foreach { ux =>
              (0 until icTemp.icType.CY).foreach { uy =>
                if (mouseX >= ux * 46 + 6 && mouseX < ux * 46 + 46 &&
                    mouseY >= uy * 46 + inventory.image.getHeight() + 46 &&
                    mouseY < uy * 46 + inventory.image.getHeight() + 86) {
                  checkBlocks = false
                  if (mouseClicked) {
                    mouseNoLongerClicked = true
                    moveItemTemp = icTemp.ids(uy * icTemp.icType.CX + ux)
                    val moveNumTemp = icTemp.nums(uy * icTemp.icType.CX + ux)
                    if (moveItem === icTemp.ids(uy * icTemp.icType.CX + ux)) {
                      moveNum = inventory.addLocationIC(icTemp, uy * icTemp.icType.CX + ux, moveItem, moveNum)
                      if (moveNum === 0) {
                        moveItem = EmptyUiItem
                      }
                    } else {
                      inventory.removeLocationIC(
                        icTemp,
                        uy * icTemp.icType.CX + ux,
                        icTemp.nums(uy * icTemp.icType.CX + ux))
                      if (moveItem =/= EmptyUiItem) {
                        inventory.addLocationIC(icTemp, uy * icTemp.icType.CX + ux, moveItem, moveNum)
                      }
                      moveItem = moveItemTemp
                      moveNum = moveNumTemp
                    }
                  }
                }
              }
            }
            icTemp
          } else if (icTemp.icType === Furnace) {
            var updatedSmeltPower = icTemp.smeltPower
            if (mouseX >= 6 && mouseX < 46 &&
                mouseY >= inventory.image.getHeight() + 46 &&
                mouseY < inventory.image.getHeight() + 86) {
              checkBlocks = false
              if (mouseClicked) {
                mouseNoLongerClicked = true
                moveItemTemp = icTemp.ids(0)
                val moveNumTemp = icTemp.nums(0)
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
                  updatedSmeltPower = 0
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
                val moveNumTemp = icTemp.nums(2)
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
            icTemp.copy(smeltPower = updatedSmeltPower)
          } else {
            icTemp
          }
        }
        (0 until 4).foreach { uy =>
          if (mouseX >= inventory.image.getWidth() + 6 && mouseX < inventory.image.getWidth() + 6 + armor.icType.image
                .getWidth() &&
              mouseY >= 6 + uy * 46 && mouseY < 6 + uy * 46 + 40) {
            checkBlocks = false
            if (mouseClicked) {
              mouseNoLongerClicked = true
              val i: Int = uy
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
                  val moveNumTemp = armor.nums(i)
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
          val ux2 = playerMouseXOffset / BLOCKSIZE
          val uy2 = playerMouseYOffset / BLOCKSIZE
          if (sqrt(pow(player.x + player.image.getWidth() - ux2 * BLOCKSIZE + BLOCKSIZE / 2, 2) + pow(
                player.y + player.image.getHeight() - uy2 * BLOCKSIZE + BLOCKSIZE / 2,
                2)) <= 160 ||
              sqrt(
                pow(player.x + player.image.getWidth() - ux2 * BLOCKSIZE + BLOCKSIZE / 2 + WIDTH * BLOCKSIZE, 2) + pow(
                  player.y + player.image.getHeight() - uy2 * BLOCKSIZE + BLOCKSIZE / 2,
                  2)) <= 160 || DEBUG_REACH) {
            if (UiItem.isTool(inventory.tool())) {
              if (layer.num < blocks.length && uy < blocks(layer.num).length && ux < blocks(layer.num)(uy).length && blocks(
                    layer.num)(uy)(ux) =/= AirBlock && Block
                    .blockContainsTool(blocks(layer.num)(uy)(ux), inventory.tool())) {
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
              if (blocks(layer.num)(uy)(ux) === FurnaceBlock || blocks(layer.num)(uy)(ux) === FurnaceOnBlock) {
                icmatrix(layer.num)(uy)(ux) = (icmatrix(layer.num)(uy)(ux).fold {
                  ic = ic.map {
                    icTemp =>
                      if (icTemp.icType === Furnace) {
                        inventory.durs(inventory.selection) = (inventory.durs(inventory.selection) - 1).toShort
                        blocks(layer.num)(icy)(icx) = FurnaceOnBlock
                        addBlockLighting(ux, uy)
                        rdrawn(icy)(icx) = false
                        if (inventory.durs(inventory.selection) <= 0) {
                          inventory.removeLocation(inventory.selection, inventory.nums(inventory.selection))
                        }
                        icTemp.copy(furnaceOn = true)
                      } else {
                        icTemp
                      }
                  }
                  Option.empty[ItemCollection]
                } { icMatrixTemp =>
                  if (icMatrixTemp.icType === Furnace) {
                    inventory.durs(inventory.selection) = (inventory.durs(inventory.selection) - 1).toShort
                    blocks(layer.num)(uy)(ux) = FurnaceOnBlock
                    addBlockLighting(ux, uy)
                    if (inventory.durs(inventory.selection) <= 0) {
                      inventory.removeLocation(inventory.selection, inventory.nums(inventory.selection))
                    }
                    rdrawn(uy)(ux) = false
                    Some(icMatrixTemp.copy(furnaceOn = true))
                  } else {
                    Some(icMatrixTemp)
                  }
                })
              }
            } else if (inventory.tool() === WrenchUiItem) {
              if (blocks(layer.num)(uy)(ux).id >= ZythiumDelayer1DelayRightBlock.id && blocks(layer.num)(uy)(ux).id <= ZythiumDelayer4DelayUpOnBlock.id) { // TODO need a better logic method than comparing ids
                inventory.durs(inventory.selection) = (inventory.durs(inventory.selection) - 1).toShort
                blocks(layer.num)(uy)(ux) = Block.withId(blocks(layer.num)(uy)(ux).id + 8)
                rdrawn(uy)(ux) = false
                if (inventory.durs(inventory.selection) <= 0) {
                  inventory.removeLocation(inventory.selection, inventory.nums(inventory.selection))
                }
              } else if (blocks(layer.num)(uy)(ux).id >= ZythiumDelayer8DelayRightBlock.id && blocks(layer.num)(uy)(ux).id <= ZythiumDelayer8DelayUpOnBlock.id) {
                inventory.durs(inventory.selection) = (inventory.durs(inventory.selection) - 1).toShort
                blocks(layer.num)(uy)(ux) = Block.withId(blocks(layer.num)(uy)(ux).id - 24)
                rdrawn(uy)(ux) = false
                if (inventory.durs(inventory.selection) <= 0) {
                  inventory.removeLocation(inventory.selection, inventory.nums(inventory.selection))
                }
              }
            } else if (inventory.tool() =/= EmptyUiItem) {
              var toolBlock: Block = UiItem.blockForTool(inventory.tool())
              if (layer.num < blocks.length &&
                  uy < blocks(layer.num).length &&
                  ux < blocks(layer.num)(uy).length &&
                  uy >= 1 && (blocks(layer.num)(uy)(ux) === AirBlock) &&
                  (layer.num === 0 && (blocks(layer.num)(uy)(ux - 1) =/= AirBlock || blocks(layer.num)(uy)(ux + 1) =/= AirBlock ||
                  blocks(layer.num)(uy - 1)(ux) =/= AirBlock || blocks(layer.num)(uy + 1)(ux) =/= AirBlock ||
                  blocks(layer.num + 1)(uy)(ux) =/= AirBlock) ||
                  layer.num === 1 && (blocks(layer.num)(uy)(ux - 1) =/= AirBlock || blocks(layer.num)(uy)(ux + 1) =/= AirBlock ||
                  blocks(layer.num)(uy - 1)(ux) =/= AirBlock || blocks(layer.num)(uy + 1)(ux) =/= AirBlock ||
                  blocks(layer.num - 1)(uy)(ux) =/= AirBlock || blocks(layer.num + 1)(uy)(ux) =/= AirBlock) ||
                  layer.num === 2 && (blocks(layer.num)(uy)(ux - 1) =/= AirBlock || blocks(layer.num)(uy)(ux + 1) =/= AirBlock ||
                  blocks(layer.num)(uy - 1)(ux) =/= AirBlock || blocks(layer.num)(uy + 1)(ux) =/= AirBlock ||
                  blocks(layer.num - 1)(uy)(ux) =/= AirBlock)) &&
                  !(toolBlock === SunflowerStage1Block && (blocks(layer.num)(uy + 1)(ux) =/= DirtBlock && blocks(
                    layer.num)(uy + 1)(ux) =/= GrassBlock && blocks(layer.num)(uy + 1)(ux) =/= JungleGrassBlock) || // sunflower
                    toolBlock === MoonflowerStage1Block && (blocks(layer.num)(uy + 1)(ux) =/= DirtBlock && blocks(
                      layer.num)(uy + 1)(ux) =/= GrassBlock && blocks(layer.num)(uy + 1)(ux) =/= JungleGrassBlock) || // moonflower
                    toolBlock === DryweedStage1Block && (blocks(layer.num)(uy + 1)(ux) =/= SandBlock) ||              // dryweed
                    toolBlock === GreenleafStage1Block && (blocks(layer.num)(uy + 1)(ux) =/= JungleGrassBlock) ||     // greenleaf
                    toolBlock === FrostleafStage1Block && (blocks(layer.num)(uy + 1)(ux) =/= SnowBlock) ||            // frostleaf
                    toolBlock === CaverootStage1Block && (blocks(layer.num)(uy + 1)(ux) =/= StoneBlock) ||            // caveroot
                    toolBlock === SkyblossomStage1Block && (blocks(layer.num)(uy + 1)(ux) =/= DirtBlock && blocks(
                      layer.num)(uy + 1)(ux) =/= GrassBlock && blocks(layer.num)(uy + 1)(ux) =/= JungleGrassBlock) || // skyblossom
                    toolBlock === VoidRotStage1Block && (blocks(layer.num)(uy + 1)(ux) =/= StoneBlock))) { // void_rot
                if (TORCHESL
                      .get(toolBlock)
                      .isEmpty || uy < HEIGHT - 1 && (solid(blocks(layer.num)(uy + 1)(ux).id) && toolBlock =/= ButtonLeftBlock || solid(
                      blocks(layer.num)(uy)(ux + 1).id) || solid(blocks(layer.num)(uy)(ux - 1).id))) {
                  if (TORCHESL.get(toolBlock).isDefined) {
                    if (solid(blocks(layer.num)(uy + 1)(ux).id) && toolBlock =/= ButtonLeftBlock) {
                      toolBlock = toolBlock
                    } else if (solid(blocks(layer.num)(uy)(ux - 1).id)) { //TODO: encode solid into Block
                      TORCHESL.get(toolBlock).foreach { t => //TODO encode torchesl into Block
                        toolBlock = t
                      }
                    } else if (solid(blocks(layer.num)(uy)(ux + 1).id)) {
                      TORCHESR.get(toolBlock).foreach { t =>
                        toolBlock = t
                      }
                    }
                  }
                  if (layer === PrimaryLayer && !DEBUG_GPLACE && blockcds(toolBlock.id)) { //TODO: encode blockcs into Block
                    entities
                      .collect {
                        case e: AIEntity => e
                      }
                      .foreach { entity: AIEntity =>
                        if (entity.rect.intersects(
                              new Rectangle(ux * BLOCKSIZE, uy * BLOCKSIZE, BLOCKSIZE, BLOCKSIZE))) {
                          toolBlock = AirBlock
                        }
                      }
                    if (player.playerRect.intersects(
                          new Rectangle(ux * BLOCKSIZE, uy * BLOCKSIZE, BLOCKSIZE, BLOCKSIZE))) {
                      toolBlock = AirBlock
                    }
                  }
                  if (toolBlock =/= AirBlock) {
                    blocks(layer.num)(uy)(ux) = toolBlock
                    if (receives(blocks(layer.num)(uy)(ux).id)) { // TODO: encode receives into Block
                      addAdjacentTilesToPQueue(ux, uy)
                    }
                    if (powers(blocks(layer.num)(uy)(ux))) {
                      addBlockPower(ux, uy)
                    }
                    if (ltrans(blocks(layer.num)(uy)(ux).id)) { //TODO: encode ltrans into Block
                      removeSunLighting(ux, uy)
                      redoBlockLighting(ux, uy)
                    }
                    addBlockLighting(ux, uy)
                  }
                  if (toolBlock =/= AirBlock) {
                    inventory.removeLocation(inventory.selection, 1.toShort)
                    blockds(layer.num) = World.generate2b(blocks(layer.num), blockds(layer.num), ux, uy)
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
                val moveNumTemp = (inventory.nums(uy * 10 + ux) / 2).toShort
                val moveDurTemp = inventory.durs(uy * 10 + ux)
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
                  val moveNumTemp = (c.nums(uy * 2 + ux) / 2).toShort
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
                    val moveNumTemp = (icTemp.nums(uy * 3 + ux) / 2).toShort
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
          } else if (icTemp.icType === WoodenChest || icTemp.icType === StoneChest ||
                     icTemp.icType === CopperChest || icTemp.icType === IronChest ||
                     icTemp.icType === SilverChest || icTemp.icType === GoldChest ||
                     icTemp.icType === ZincChest || icTemp.icType === RhymestoneChest ||
                     icTemp.icType === ObduriteChest) { //TODO: chest trait?
            (0 until icTemp.icType.CX).foreach { ux =>
              (0 until icTemp.icType.CY).foreach { uy =>
                if (mouseX >= ux * 46 + 6 && mouseX < ux * 46 + 46 &&
                    mouseY >= uy * 46 + inventory.image.getHeight() + 46 &&
                    mouseY < uy * 46 + inventory.image.getHeight() + 86) {
                  checkBlocks = false
                  if (mouseClicked2) {
                    mouseNoLongerClicked2 = true
                    moveItemTemp = icTemp.ids(uy * icTemp.icType.CX + ux)
                    val moveNumTemp = (icTemp.nums(uy * icTemp.icType.CX + ux) / 2).toShort
                    if (icTemp.ids(uy * icTemp.icType.CX + ux) === EmptyUiItem) {
                      inventory.addLocationIC(icTemp, uy * icTemp.icType.CX + ux, moveItem, 1.toShort)
                      moveNum = (moveNum - 1).toShort
                      if (moveNum === 0) {
                        moveItem = EmptyUiItem
                      }
                    } else if (moveItem === EmptyUiItem && icTemp.nums(uy * icTemp.icType.CX + ux) =/= 1) {
                      inventory.removeLocationIC(
                        icTemp,
                        uy * icTemp.icType.CX + ux,
                        (icTemp.nums(uy * icTemp.icType.CX + ux) / 2).toShort)
                      moveItem = moveItemTemp
                      moveNum = moveNumTemp
                    } else if (moveItem === icTemp.ids(uy * icTemp.icType.CX + ux)) {
                      val maxstacks = icTemp.ids(uy * icTemp.icType.CX + ux).maxStacks
                      if (icTemp.nums(uy * icTemp.icType.CX + ux) < maxstacks) {
                        inventory.addLocationIC(icTemp, uy * icTemp.icType.CX + ux, moveItem, 1.toShort)
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
          } else if (icTemp.icType === Furnace) {
            if (mouseX >= 6 && mouseX < 46 &&
                mouseY >= inventory.image.getHeight() + 46 &&
                mouseY < inventory.image.getHeight() + 86) {
              checkBlocks = false
              if (mouseClicked2) {
                mouseNoLongerClicked2 = true
                moveItemTemp = icTemp.ids(0)
                val moveNumTemp = (icTemp.nums(0) / 2).toShort
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
                val moveNumTemp = (icTemp.nums(2) / 2).toShort
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
                val moveNumTemp = (icTemp.nums(3) / 2).toShort
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
            if (layer.num < blocks.length && uy < blocks(layer.num).length && ux < blocks(layer.num)(uy).length && blocks(
                  layer.num)(uy)(ux).id >= WorkbenchBlock.id && blocks(layer.num)(uy)(ux).id <= GoldChestBlock.id || blocks(
                  layer.num)(uy)(ux) === FurnaceBlock || blocks(layer.num)(uy)(ux) === FurnaceOnBlock || blocks(
                  layer.num)(uy)(ux).id >= ZincChestBlock.id && blocks(layer.num)(uy)(ux).id <= ObduriteChestBlock.id) {
              ic.foreach { icTemp =>
                if (icTemp.icType =/= Workbench) {
                  machinesx += icx
                  machinesy += icy
                  icmatrix(iclayer.num)(icy)(icx) =
                    Some(new ItemCollection(icTemp.icType, icTemp.ids, icTemp.nums, icTemp.durs))
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
                } else if (icTemp.icType === Furnace) {
                  icmatrix(iclayer.num)(icy)(icx) = icmatrix(iclayer.num)(icy)(icx).map { icMatrixTemp =>
                    icMatrixTemp.copy(
                      fuelPower = icTemp.fuelPower,
                      smeltPower = icTemp.smeltPower,
                      furnaceOn = icTemp.furnaceOn
                    )
                  }
                }
                ic = None
              }
              iclayer = layer
              (0 until 3).foreach { l =>
                if (blocks(l)(uy)(ux) === WorkbenchBlock) {
                  ic = icmatrix(l)(uy)(ux) match {
                    case origIC @ Some(ItemCollection(Workbench, _, _, _, _, _, _)) => origIC
                    case _                                                          => Some(new ItemCollection(Workbench))
                  }

                  icx = ux
                  icy = uy
                  ic.foreach(inventory.renderCollection)
                  showInv = true
                } else if (blocks(l)(uy)(ux) === WoodenChestBlock) {
                  ic = icmatrix(l)(uy)(ux) match {
                    case origIC @ Some(ItemCollection(WoodenChest, _, _, _, _, _, _)) => origIC
                    case _                                                            => Some(new ItemCollection(WoodenChest))
                  }

                  icx = ux
                  icy = uy
                  ic.foreach(inventory.renderCollection)
                  showInv = true
                } else if (blocks(l)(uy)(ux) === StoneChestBlock) {
                  ic = icmatrix(l)(uy)(ux) match {
                    case origIC @ Some(ItemCollection(StoneChest, _, _, _, _, _, _)) => origIC
                    case _                                                           => Some(new ItemCollection(StoneChest))
                  }

                  icx = ux
                  icy = uy
                  ic.foreach(inventory.renderCollection)
                  showInv = true
                } else if (blocks(l)(uy)(ux) === CopperChestBlock) {
                  ic = icmatrix(l)(uy)(ux) match {
                    case origIC @ Some(ItemCollection(CopperChest, _, _, _, _, _, _)) => origIC
                    case _                                                            => Some(new ItemCollection(CopperChest))
                  }

                  icx = ux
                  icy = uy
                  ic.foreach(inventory.renderCollection)
                  showInv = true
                } else if (blocks(l)(uy)(ux) === IronChestBlock) { //TODO: seems like all these blocks only differ by type
                  ic = icmatrix(l)(uy)(ux) match {
                    case origIC @ Some(ItemCollection(IronChest, _, _, _, _, _, _)) => origIC
                    case _                                                          => Some(new ItemCollection(IronChest))
                  }

                  icx = ux
                  icy = uy
                  ic.foreach(inventory.renderCollection)
                  showInv = true
                } else if (blocks(l)(uy)(ux) === SilverChestBlock) {
                  ic = icmatrix(l)(uy)(ux) match {
                    case origIC @ Some(ItemCollection(SilverChest, _, _, _, _, _, _)) => origIC
                    case _                                                            => Some(new ItemCollection(SilverChest))
                  }

                  icx = ux
                  icy = uy
                  ic.foreach(inventory.renderCollection)
                  showInv = true
                } else if (blocks(l)(uy)(ux) === GoldChestBlock) {
                  ic = icmatrix(l)(uy)(ux) match {
                    case origIC @ Some(ItemCollection(GoldChest, _, _, _, _, _, _)) => origIC
                    case _                                                          => Some(new ItemCollection(GoldChest))
                  }

                  icx = ux
                  icy = uy
                  ic.foreach(inventory.renderCollection)
                  showInv = true
                } else if (blocks(l)(uy)(ux) === ZincChestBlock) {
                  ic = icmatrix(l)(uy)(ux) match {
                    case origIC @ Some(ItemCollection(ZincChest, _, _, _, _, _, _)) => origIC
                    case _                                                          => Some(new ItemCollection(ZincChest))
                  }

                  icx = ux
                  icy = uy
                  ic.foreach(inventory.renderCollection)
                  showInv = true
                } else if (blocks(l)(uy)(ux) === RhymestoneChestBlock) {
                  ic = icmatrix(l)(uy)(ux) match {
                    case origIC @ Some(ItemCollection(RhymestoneChest, _, _, _, _, _, _)) => origIC
                    case _                                                                => Some(new ItemCollection(RhymestoneChest))
                  }

                  icx = ux
                  icy = uy
                  ic.foreach(inventory.renderCollection)
                  showInv = true
                } else if (blocks(l)(uy)(ux) === ObduriteChestBlock) {

                  ic = icmatrix(l)(uy)(ux) match {
                    case origIC @ Some(ItemCollection(ObduriteChest, _, _, _, _, _, _)) => origIC
                    case _                                                              => Some(new ItemCollection(ObduriteChest))
                  }
                  icx = ux
                  icy = uy
                  ic.foreach(inventory.renderCollection)
                  showInv = true
                } else if (blocks(l)(uy)(ux) === FurnaceBlock || blocks(l)(uy)(ux) === FurnaceOnBlock) {
                  ic = icmatrix(l)(uy)(ux) match {
                    case origIC @ Some(ItemCollection(Furnace, _, _, _, _, _, _)) => origIC
                    case _                                                        => Some(new ItemCollection(Furnace))
                  }
                  icmatrix(l)(uy)(ux) = None
                  icx = ux
                  icy = uy
                  ic.foreach(inventory.renderCollection)
                  showInv = true
                }

                if (ic.isDefined && blocks(l)(uy)(ux) =/= WorkbenchBlock) {
                  (machinesx.length - 1 until (-1, -1)).foreach { i =>
                    if (machinesx(i) === icx && machinesy(i) === icy) {
                      machinesx.remove(i)
                      machinesy.remove(i)
                    }
                  }
                }
              }
            }
            if (blocks(layer.num)(uy)(ux) === TreeBlock) {
              if (random.nextInt(2) === 0) {
                entities += new IdEntity(
                  (ux * BLOCKSIZE).toDouble,
                  (uy * BLOCKSIZE).toDouble,
                  random.nextDouble() * 8 - 4,
                  -3,
                  BarkUiItem,
                  1.toShort)
              }
              blocks(layer.num)(uy)(ux) = TreeNoBarkBlock
            }
            if (mouseClicked2) {
              mouseNoLongerClicked2 = true
              // blockTemp = blocks(layer.num)(uy)(ux) // TODO: appears to not be used
              if (blocks(layer.num)(uy)(ux) === LeverBlock || blocks(layer.num)(uy)(ux) === LeverLeftWallBlock || blocks(
                    layer.num)(uy)(ux) === LeverLightWallBlock) {
                blocks(layer.num)(uy)(ux) = Block.withId(blocks(layer.num)(uy)(ux).id + 1)
                addBlockPower(ux, uy)
                rdrawn(uy)(ux) = false
              } else if (blocks(layer.num)(uy)(ux) === LeverOnBlock || blocks(layer.num)(uy)(ux) === LeverLeftWallOnBlock || blocks(
                           layer.num)(uy)(ux) === LeverRightWallOnBlock) {
                removeBlockPower(ux, uy, layer)
                if (wcnct(uy)(ux)) {
                  Layer.values.foreach { l =>
                    if (l =/= layer) {
                      rbpRecur(ux, uy, l)
                    }
                  }
                }
                blocks(layer.num)(uy)(ux) = Block.withId(blocks(layer.num)(uy)(ux).id - 1)
                rdrawn(uy)(ux) = false
              }
              if (blocks(layer.num)(uy)(ux).id >= ZythiumWireBlock.id && blocks(layer.num)(uy)(ux).id <= ZythiumWire5PowerBlock.id) {
                wcnct(uy)(ux) = !wcnct(uy)(ux)
                rdrawn(uy)(ux) = false
                redoBlockPower(ux, uy, layer)
              }
              if (blocks(layer.num)(uy)(ux).id >= ZythiumAmplifierRightBlock.id && blocks(layer.num)(uy)(ux).id <= ZythiumAmplifierUpOnBlock.id) {
                removeBlockPower(ux, uy, layer)
                if (blocks(layer.num)(uy)(ux).id >= ZythiumAmplifierRightBlock.id && blocks(layer.num)(uy)(ux).id <= ZythiumAmplifierLeftBlock.id || blocks(
                      layer.num)(uy)(ux).id >= ZythiumAmplifierRightOnBlock.id && blocks(layer.num)(uy)(ux).id <= ZythiumAmplifierLeftOnBlock.id) {
                  blocks(layer.num)(uy)(ux) = Block.withId(blocks(layer.num)(uy)(ux).id + 1)
                } else {
                  blocks(layer.num)(uy)(ux) = Block.withId(blocks(layer.num)(uy)(ux).id - 3)
                }
                blockds(layer.num) = World.generate2b(blocks(layer.num), blockds(layer.num), ux, uy)
                rdrawn(uy)(ux) = false
                addAdjacentTilesToPQueueConditionally(ux, uy)
              }
              if (blocks(layer.num)(uy)(ux).id >= ZythiumInverterRightBlock.id && blocks(layer.num)(uy)(ux).id <= ZythiumInverterUpOnBlock.id) {
                removeBlockPower(ux, uy, layer)
                if (blocks(layer.num)(uy)(ux).id >= ZythiumInverterRightBlock.id && blocks(layer.num)(uy)(ux).id <= ZythiumInverterLeftBlock.id || blocks(
                      layer.num)(uy)(ux).id >= ZythiumInverterRightOnBlock.id && blocks(layer.num)(uy)(ux).id <= ZythiumInverterLeftOnBlock.id) {
                  blocks(layer.num)(uy)(ux) = Block.withId(blocks(layer.num)(uy)(ux).id + 1)
                } else {
                  blocks(layer.num)(uy)(ux) = Block.withId(blocks(layer.num)(uy)(ux).id - 3)
                }
                blockds(layer.num) = World.generate2b(blocks(layer.num), blockds(layer.num), ux, uy)
                rdrawn(uy)(ux) = false
                addAdjacentTilesToPQueueConditionally(ux, uy)
              }
              if (blocks(layer.num)(uy)(ux) === ButtonLeftBlock || blocks(layer.num)(uy)(ux) === ButtonRightBlock) {
                blocks(layer.num)(uy)(ux) = Block.withId(blocks(layer.num)(uy)(ux).id + 1)
                addBlockPower(ux, uy)
                rdrawn(uy)(ux) = false
                logger.debug("Srsly?")
                updatex += ux
                updatey += uy
                updatet += 50
                updatel += layer
              }
              if (blocks(layer.num)(uy)(ux).id >= ZythiumDelayer1DelayRightBlock.id && blocks(layer.num)(uy)(ux).id <= ZythiumDelayer8DelayUpOnBlock.id) {
                if (blocks(layer.num)(uy)(ux).id >= ZythiumDelayer1DelayRightBlock.id && blocks(layer.num)(uy)(ux).id <= ZythiumDelayer1DelayLeftBlock.id || blocks(
                      layer.num)(uy)(ux).id >= ZythiumDelayer1DelayRightOnBlock.id && blocks(layer.num)(uy)(ux).id <= ZythiumDelayer1DelayLeftOnBlock.id ||
                    blocks(layer.num)(uy)(ux).id >= ZythiumDelayer2DelayRightBlock.id && blocks(layer.num)(uy)(ux).id <= ZythiumDelayer2DelayLeftBlock.id || blocks(
                      layer.num)(uy)(ux).id >= ZythiumDelayer2DelayRightOnBlock.id && blocks(layer.num)(uy)(ux).id <= ZythiumDelayer2DelayLeftOnBlock.id ||
                    blocks(layer.num)(uy)(ux).id >= ZythiumDelayer4DelayRightBlock.id && blocks(layer.num)(uy)(ux).id <= ZythiumDelayer4DelayLeftBlock.id || blocks(
                      layer.num)(uy)(ux).id >= ZythiumDelayer4DelayRightOnBlock.id && blocks(layer.num)(uy)(ux).id <= ZythiumDelayer4DelayLeftOnBlock.id ||
                    blocks(layer.num)(uy)(ux).id >= ZythiumDelayer8DelayRightBlock.id && blocks(layer.num)(uy)(ux).id <= ZythiumDelayer8DelayLeftBlock.id || blocks(
                      layer.num)(uy)(ux).id >= ZythiumDelayer8DelayRightOnBlock.id && blocks(layer.num)(uy)(ux).id <= ZythiumDelayer8DelayLeftOnBlock.id) {
                  blocks(layer.num)(uy)(ux) = Block.withId(blocks(layer.num)(uy)(ux).id + 1)
                } else {
                  blocks(layer.num)(uy)(ux) = Block.withId(blocks(layer.num)(uy)(ux).id - 3)
                }
                blockds(layer.num) = World.generate2b(blocks(layer.num), blockds(layer.num), ux, uy)
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

        if (entityTemp.update(blocks(PrimaryLayer.num), player, u, v)) {
          entities.remove(indexTemp)
        } else if (player.playerRect.intersects(entityTemp.rect)) {
          if (immune <= 0) {
            if (!DEBUG_INVINCIBLE) {
              player.damage(entityTemp.strategy.atk, true, inventory)
            }
            rgnc1 = 750
            immune = 40
            if (player.x + Player.width / 2 < entityTemp.x + entityTemp.width / 2) {
              player.vx -= 8
            } else {
              player.vx += 8
            }
            player.vy -= 3.5
          }
        }

      case (entityTemp: IdEntity, indexTemp) =>
        if (entityTemp.update(blocks(PrimaryLayer.num), player, u, v)) {
          entities.remove(indexTemp)
        } else if (player.playerRect.intersects(entityTemp.rect)) {
          if (entityTemp.mdelay <= 0) {
            val n = inventory.addItem(entityTemp.id, entityTemp.num).toInt
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
          icmatrix(iclayer.num)(icy)(icx) =
            Some(new ItemCollection(icTemp.icType, icTemp.ids, icTemp.nums, icTemp.durs))
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
          icmatrix(iclayer.num)(icy)(icx) = icmatrix(iclayer.num)(icy)(icx).map { icMatrixTemp =>
            icMatrixTemp.copy(
              fuelPower = icTemp.fuelPower,
              smeltPower = icTemp.smeltPower,
              furnaceOn = icTemp.furnaceOn
            )
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
            val dropList = entityTemp.drops(random)
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
      var p, q: Double = 0

      if (i === -1) { //TODO: rendering entities should be different than player and abuse of -1 and just iterate entities
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
      bx2 = min(blocks(BackgroundLayer.num).length - 1, bx2)
      by2 = min(blocks.length - 1, by2)

      lazy val isNamedEntity = entities(i) match {
        case _: AIEntity => true
        case _           => false
      }

      (bx1 to bx2).foreach { x =>
        (by1 to by2).foreach { y =>
          if (blocks(layer.num)(y)(x).id >= WoodenPressurePlateBlock.id && blocks(layer.num)(y)(x).id <= ZythiumPressurePlateOnBlock.id && (i === -1 || blocks(
                layer.num)(y)(x).id <= StonePressurePlateOnBlock.id && (x =/= -1 && isNamedEntity || blocks(layer.num)(
                y)(x).id <= WoodenPressurePlateOnBlock.id))) {
            if (blocks(layer.num)(y)(x) === WoodenPressurePlateBlock || blocks(layer.num)(y)(x) === StonePressurePlateBlock || blocks(
                  layer.num)(y)(x) === ZythiumPressurePlateBlock) {
              blocks(layer.num)(y)(x) = Block.withId(blocks(layer.num)(y)(x).id + 1)
              rdrawn(y)(x) = false
              addBlockPower(x, y)
              logger.debug("Srsly?")
              updatex += x
              updatey += y
              updatet += 0
              updatel += BackgroundLayer
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
      blocks(l)(y - 1)(x - 1) === AirBlock || !blockcds(blocks(l)(y - 1)(x - 1).id) ||
      blocks(l)(y - 1)(x) === AirBlock || !blockcds(blocks(l)(y - 1)(x).id) ||
      blocks(l)(y - 1)(x + 1) === AirBlock || !blockcds(blocks(l)(y - 1)(x + 1).id) ||
      blocks(l)(y)(x - 1) === AirBlock || !blockcds(blocks(l)(y)(x - 1).id) ||
      blocks(l)(y)(x + 1) === AirBlock || !blockcds(blocks(l)(y)(x + 1).id) ||
      blocks(l)(y + 1)(x - 1) === AirBlock || !blockcds(blocks(l)(y + 1)(x - 1).id) ||
      blocks(l)(y + 1)(x) === AirBlock || !blockcds(blocks(l)(y + 1)(x).id) ||
      blocks(l)(y + 1)(x + 1) === AirBlock || !blockcds(blocks(l)(y + 1)(x + 1).id)
    } catch {
      case _: ArrayIndexOutOfBoundsException => false
    }
  }

  def breakCurrentBlock(): Unit = {
    if (DEBUG_INSTAMINE || mining >= UiItem.blockDurabilityForTool(inventory.tool(), blocks(layer.num)(uy)(ux))) {
      if (blocks(BackgroundLayer.num)(uy)(ux) === TreeRootBlock) {
        blocks(BackgroundLayer.num)(uy)(ux) = AirBlock
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
      if (blocks(BackgroundLayer.num)(uy + 1)(ux) === TreeRootBlock) {
        blocks(BackgroundLayer.num)(uy + 1)(ux) = AirBlock
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
      if (blocks(layer.num)(uy)(ux).id >= WorkbenchBlock.id && blocks(layer.num)(uy)(ux).id <= GoldChestBlock.id || blocks(
            layer.num)(uy)(ux) === FurnaceBlock || blocks(layer.num)(uy)(ux) === FurnaceOnBlock || blocks(layer.num)(
            uy)(ux).id >= ZincChestBlock.id && blocks(layer.num)(uy)(ux).id <= ObduriteChestBlock.id) {
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
        icmatrix(layer.num)(uy)(ux).foreach { icMatrixTemp =>
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
          icmatrix(layer.num)(uy)(ux) = None
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
      Block.drop(blocks(layer.num)(uy)(ux)).foreach { blockdrops =>
        UiItem.onImageItem(blockdrops) { imgItm =>
          if (blocks(layer.num)(uy)(ux) =/= AirBlock) {
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

      val (tempUiItem, n): (UiItem, Int) = blocks(layer.num)(uy)(ux) match {
        case SunflowerStage1Block  => (SunflowerSeedsUiItem, random.nextInt(4) - 2)
        case SunflowerStage2Block  => (SunflowerSeedsUiItem, random.nextInt(2))
        case SunflowerStage3Block  => (SunflowerSeedsUiItem, random.nextInt(3) + 1)
        case MoonflowerStage1Block => (MoonflowerSeedsUiItem, random.nextInt(4) - 2)
        case MoonflowerStage2Block => (MoonflowerSeedsUiItem, random.nextInt(2))
        case MoonflowerStage3Block => (MoonflowerSeedsUiItem, random.nextInt(3) + 1)
        case DryweedStage1Block    => (DryweedSeedsUiItem, random.nextInt(4) - 2)
        case DryweedStage2Block    => (DryweedSeedsUiItem, random.nextInt(2))
        case DryweedStage3Block    => (DryweedSeedsUiItem, random.nextInt(3) + 1)
        case GreenleafStage1Block  => (GreenleafSeedsUiItem, random.nextInt(4) - 2)
        case GreenleafStage2Block  => (GreenleafSeedsUiItem, random.nextInt(2))
        case GreenleafStage3Block  => (GreenleafSeedsUiItem, random.nextInt(3) + 1)
        case FrostleafStage1Block  => (FrostleafSeedsUiItem, random.nextInt(4) - 2)
        case FrostleafStage2Block  => (FrostleafSeedsUiItem, random.nextInt(2))
        case FrostleafStage3Block  => (FrostleafSeedsUiItem, random.nextInt(3) + 1)
        case CaverootStage1Block   => (CaverootSeedsUiItem, random.nextInt(4) - 2)
        case CaverootStage2Block   => (CaverootSeedsUiItem, random.nextInt(2))
        case CaverootStage3Block   => (CaverootSeedsUiItem, random.nextInt(3) + 1)
        case SkyblossomStage1Block => (SkyblossomSeedsUiItem, random.nextInt(4) - 2)
        case SkyblossomStage2Block => (SkyblossomSeedsUiItem, random.nextInt(2))
        case SkyblossomStage3Block => (SkyblossomSeedsUiItem, random.nextInt(3) + 1)
        case VoidRotStage1Block    => (VoidRotSeedsUiItem, random.nextInt(4) - 2)
        case VoidRotStage2Block    => (VoidRotSeedsUiItem, random.nextInt(2))
        case VoidRotStage3Block    => (VoidRotSeedsUiItem, random.nextInt(3) + 1)
        case MarshleafStage1Block  => (MarshleafSeedsUiItem, random.nextInt(4) - 2)
        case MarshleafStage2Block  => (MarshleafSeedsUiItem, random.nextInt(2))
        case MarshleafStage3Block  => (MarshleafSeedsUiItem, random.nextInt(3) + 1)
        case _                     => (EmptyUiItem, 0)
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
      val userBlock = blocks(layer.num)(uy)(ux)
      blocks(layer.num)(uy)(ux) = AirBlock
      if (userBlock.id >= ZythiumWireBlock.id && userBlock.id <= ZythiumWire5PowerBlock.id) {
        redoBlockPower(ux, uy, layer)
      }
      if (powers(userBlock)) {
        removeBlockPower(ux, uy, layer)
      }
      if (ltrans(userBlock.id)) {
        addSunLighting(ux, uy)
        redoBlockLighting(ux, uy)
      }
      addSunLighting(ux, uy)
      blockds(layer.num) = World.generate2b(blocks(layer.num), blockds(layer.num), ux, uy)
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
            if (uly >= 0 && uly < HEIGHT && blocks(l)(uly)(ulx) === LeavesBlock) {
              keepLeaf = false
              import scala.util.control.Breaks._
              breakable {
                (uly - 4 until uly + 5).foreach { uly2 =>
                  breakable {
                    (ulx - 4 until ulx + 5).foreach { ulx2 =>
                      if (uly2 >= 0 && uly2 < HEIGHT && (blocks(PrimaryLayer.num)(uly2)(ulx2) === TreeBlock || blocks(
                            PrimaryLayer.num)(uly2)(ulx2) === TreeNoBarkBlock)) {
                        keepLeaf = true
                        break
                      }
                    }
                  }
                  if (keepLeaf) break
                }
              }
              if (!keepLeaf) {
                blocks(l)(uly)(ulx) = AirBlock
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
          Block.drop(blocks(layer.num)(uy)(ux - 1)).foreach { blockdrop =>
            val itemblock = UiItem.blockForTool(blockdrop)
            val isTorch = (for {
              torch <- TORCHESR.get(itemblock)
            } yield torch === blocks(layer.num)(uy)(ux - 1)).getOrElse(false)
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
              if (layer === PrimaryLayer) {
                addSunLighting(ux - 1, uy)
              }
              val leftAdjacentBlock = blocks(layer.num)(uy)(ux - 1)
              blocks(layer.num)(uy)(ux - 1) = AirBlock
              if (leftAdjacentBlock.id >= ZythiumWireBlock.id && leftAdjacentBlock.id <= ZythiumWire5PowerBlock.id) {
                redoBlockPower(ux, uy, layer)
              }
              if (powers(leftAdjacentBlock)) {
                removeBlockPower(ux, uy, layer)
              }
              drawn(uy)(ux - 1) = false
            }
          }

          Block.drop(blocks(layer.num)(uy)(ux + 1)).foreach { blockdrop =>
            val itemblock = UiItem.blockForTool(blockdrop)
            val isTorch = (for {
              torch <- TORCHESR.get(itemblock)
            } yield torch === blocks(layer.num)(uy)(ux + 1)).getOrElse(false)

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
              if (layer === PrimaryLayer) {
                addSunLighting(ux + 1, uy)
              }
              val rightAdjacentBlock = blocks(layer.num)(uy)(ux + 1)
              blocks(layer.num)(uy)(ux + 1) = AirBlock
              if (rightAdjacentBlock.id >= ZythiumWireBlock.id && rightAdjacentBlock.id <= ZythiumWire5PowerBlock.id) {
                redoBlockPower(ux, uy, layer)
              }
              if (powers(rightAdjacentBlock)) {
                removeBlockPower(ux, uy, layer)
              }
              drawn(uy)(ux + 1) = false
            }
          }

          uy -= 1
          if (uy === -1 || !Block.isGsupported(blocks(layer.num)(uy)(ux))) {
            addSunLighting(ux, uy)
            break
          }
          Block.drop(blocks(layer.num)(uy)(ux)).foreach { blockdrop =>
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

          val (tempUiItem2, n2): (UiItem, Int) = blocks(layer.num)(uy)(ux) match {
            case SunflowerStage1Block  => (SunflowerSeedsUiItem, random.nextInt(4) - 2)
            case SunflowerStage2Block  => (SunflowerSeedsUiItem, random.nextInt(2))
            case SunflowerStage3Block  => (SunflowerSeedsUiItem, random.nextInt(3) + 1)
            case MoonflowerStage1Block => (MoonflowerSeedsUiItem, random.nextInt(4) - 2)
            case MoonflowerStage2Block => (MoonflowerSeedsUiItem, random.nextInt(2))
            case MoonflowerStage3Block => (MoonflowerSeedsUiItem, random.nextInt(3) + 1)
            case DryweedStage1Block    => (DryweedSeedsUiItem, random.nextInt(4) - 2)
            case DryweedStage2Block    => (DryweedSeedsUiItem, random.nextInt(2))
            case DryweedStage3Block    => (DryweedSeedsUiItem, random.nextInt(3) + 1)
            case GreenleafStage1Block  => (GreenleafSeedsUiItem, random.nextInt(4) - 2)
            case GreenleafStage2Block  => (GreenleafSeedsUiItem, random.nextInt(2))
            case GreenleafStage3Block  => (GreenleafSeedsUiItem, random.nextInt(3) + 1)
            case FrostleafStage1Block  => (FrostleafSeedsUiItem, random.nextInt(4) - 2)
            case FrostleafStage2Block  => (FrostleafSeedsUiItem, random.nextInt(2))
            case FrostleafStage3Block  => (FrostleafSeedsUiItem, random.nextInt(3) + 1)
            case CaverootStage1Block   => (CaverootSeedsUiItem, random.nextInt(4) - 2)
            case CaverootStage2Block   => (CaverootSeedsUiItem, random.nextInt(2))
            case CaverootStage3Block   => (CaverootSeedsUiItem, random.nextInt(3) + 1)
            case SkyblossomStage1Block => (SkyblossomSeedsUiItem, random.nextInt(4) - 2)
            case SkyblossomStage2Block => (SkyblossomSeedsUiItem, random.nextInt(2))
            case SkyblossomStage3Block => (SkyblossomSeedsUiItem, random.nextInt(3) + 1)
            case VoidRotStage1Block    => (VoidRotSeedsUiItem, random.nextInt(4) - 2)
            case VoidRotStage2Block    => (VoidRotSeedsUiItem, random.nextInt(2))
            case VoidRotStage3Block    => (VoidRotSeedsUiItem, random.nextInt(3) + 1)
            case MarshleafStage1Block  => (MarshleafSeedsUiItem, random.nextInt(4) - 2)
            case MarshleafStage2Block  => (MarshleafSeedsUiItem, random.nextInt(2))
            case MarshleafStage3Block  => (MarshleafSeedsUiItem, random.nextInt(3) + 1)
            case _                     => (EmptyUiItem, 0)
          }

          UiItem.onImageItem(tempUiItem2) { imgItm =>
            (0 until max(1, n2)).foreach { _ =>
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
          val userBlock: Block = blocks(layer.num)(uy)(ux)
          blocks(layer.num)(uy)(ux) = AirBlock
          if (userBlock.id >= ZythiumWireBlock.id && userBlock.id <= ZythiumWire5PowerBlock.id) {
            redoBlockPower(ux, uy, layer)
          }
          if (powers(userBlock)) {
            removeBlockPower(ux, uy, layer)
          }
          if (ltrans(userBlock.id)) {
            addSunLighting(ux, uy)
            redoBlockLighting(ux, uy)
          }
          addSunLighting(ux, uy)
          blockds(layer.num) = World.generate2b(blocks(layer.num), blockds(layer.num), ux, uy)
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
                if (uly >= 0 && uly < HEIGHT && blocks(l)(uly)(ulx) === LeavesBlock) {
                  keepLeaf = false
                  breakable {
                    (uly - 4 until uly + 5).foreach { uly2 =>
                      breakable {
                        (ulx - 4 until ulx + 5).foreach { ulx2 =>
                          if (uly2 >= 0 && uly2 < HEIGHT && (blocks(PrimaryLayer.num)(uly2)(ulx2) === TreeBlock || blocks(
                                PrimaryLayer.num)(uly2)(ulx2) === TreeNoBarkBlock)) {
                            keepLeaf = true
                            break
                          }
                        }
                      }
                      if (keepLeaf) break
                    }
                  }
                  if (!keepLeaf) {
                    blocks(l)(uly)(ulx) = AirBlock
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
      val cloud: BufferedImage = clouds(cloudsn(cloudsn.length - 1))
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
    val n: Int = findNonLayeredBlockLightSource(ux, uy)
    if (n =/= 0) {
      addTileToZQueue(ux, uy)
      lights(uy)(ux) = max(lights(uy)(ux), n.toFloat)
      lsources(uy)(ux) = true
      addTileToQueue(ux, uy)
    }
  }

  def addBlockPower(ux: Int, uy: Int): Unit = {
    if (powers(blocks(PrimaryLayer.num)(uy)(ux))) {
      if (blocks(PrimaryLayer.num)(uy)(ux).id >= ZythiumDelayer1DelayRightBlock.id && blocks(PrimaryLayer.num)(uy)(ux).id <= ZythiumDelayer8DelayUpOnBlock.id) {
        logger.debug("Whaaat?")
        updatex += ux
        updatey += uy
        DDELAY.get(blocks(PrimaryLayer.num)(uy)(ux).id).foreach(updatet.+=)
        updatel += PrimaryLayer
      } else {
        addTileToPZQueue(ux, uy)
        power(PrimaryLayer.num)(uy)(ux) = 5.toFloat
        if (conducts(blocks(PrimaryLayer.num)(uy)(ux).id) >= 0 && wcnct(uy)(ux)) {
          if (receives(blocks(BackgroundLayer.num)(uy)(ux).id)) {
            power(BackgroundLayer.num)(uy)(ux) = power(PrimaryLayer.num)(uy)(ux) - conducts(
              blocks(PrimaryLayer.num)(uy)(ux).id).toFloat
          }
          if (receives(blocks(ForegroundLayer.num)(uy)(ux).id)) {
            power(ForegroundLayer.num)(uy)(ux) = power(PrimaryLayer.num)(uy)(ux) - conducts(
              blocks(PrimaryLayer.num)(uy)(ux).id).toFloat
          }
        }
        addTileToPQueue(ux, uy)
      }
    }
    if (powers(blocks(BackgroundLayer.num)(uy)(ux))) {
      if (blocks(BackgroundLayer.num)(uy)(ux).id >= ZythiumDelayer1DelayRightBlock.id && blocks(BackgroundLayer.num)(
            uy)(ux).id <= ZythiumDelayer8DelayUpOnBlock.id) {
        logger.debug("Whaaat?")
        updatex += ux
        updatey += uy
        DDELAY.get(blocks(BackgroundLayer.num)(uy)(ux).id).foreach(updatet.+=)
        updatel += BackgroundLayer
      } else {
        addTileToPZQueue(ux, uy)
        power(BackgroundLayer.num)(uy)(ux) = 5.toFloat
        if (conducts(blocks(BackgroundLayer.num)(uy)(ux).id) >= 0 && wcnct(uy)(ux)) {
          if (receives(blocks(PrimaryLayer.num)(uy)(ux).id)) {
            power(PrimaryLayer.num)(uy)(ux) = power(BackgroundLayer.num)(uy)(ux) - conducts(
              blocks(BackgroundLayer.num)(uy)(ux).id).toFloat
          }
          if (receives(blocks(ForegroundLayer.num)(uy)(ux).id)) {
            power(ForegroundLayer.num)(uy)(ux) = power(BackgroundLayer.num)(uy)(ux) - conducts(
              blocks(BackgroundLayer.num)(uy)(ux).id).toFloat
          }
        }
        addTileToPQueue(ux, uy)
      }
    }
    if (powers(blocks(ForegroundLayer.num)(uy)(ux))) {
      if (blocks(ForegroundLayer.num)(uy)(ux).id >= ZythiumDelayer1DelayRightBlock.id && blocks(ForegroundLayer.num)(
            uy)(ux).id <= ZythiumDelayer8DelayUpOnBlock.id) {
        logger.debug("Whaaat?")
        updatex += ux
        updatey += uy
        DDELAY.get(blocks(ForegroundLayer.num)(uy)(ux).id).foreach(updatet.+=)
        updatel += ForegroundLayer
        ()
      } else {
        addTileToPZQueue(ux, uy)
        power(ForegroundLayer.num)(uy)(ux) = 5.toFloat
        if (conducts(blocks(ForegroundLayer.num)(uy)(ux).id) >= 0 && wcnct(uy)(ux)) {
          if (receives(blocks(BackgroundLayer.num)(uy)(ux).id)) {
            power(BackgroundLayer.num)(uy)(ux) = power(ForegroundLayer.num)(uy)(ux) - conducts(
              blocks(ForegroundLayer.num)(uy)(ux).id).toFloat
          }
          if (receives(blocks(PrimaryLayer.num)(uy)(ux).id)) {
            power(PrimaryLayer.num)(uy)(ux) = power(ForegroundLayer.num)(uy)(ux) - conducts(
              blocks(ForegroundLayer.num)(uy)(ux).id).toFloat
          }
        }
        addTileToPQueue(ux, uy)
      }
    }
  }

  def removeBlockLighting(ux: Int, uy: Int): Unit = {
    removeBlockLighting(ux, uy, layer)
  }

  def removeBlockLighting(ux: Int, uy: Int, layer: Layer): Unit = {
    val n: Int = findNonLayeredBlockLightSource(ux, uy)
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

  def rbpRecur(ux: Int, uy: Int, lyr: Layer): Unit = {
    arbprd(lyr.num)(uy)(ux) = true
    logger.debug(s"(rbpR) $ux $uy")
    addTileToPZQueue(ux, uy)
    val remember: Array[Boolean] = Array(false, false, false, false)
    var ax3, ay3: Int            = 0
    (0 until 4).foreach { ir =>
      ax3 = ux + cl(ir)(0)
      ay3 = uy + cl(ir)(1)
      lazy val block   = blocks(lyr.num)(ay3)(ax3)
      lazy val blockId = block.id
      if (ay3 >= 0 && ay3 < HEIGHT && power(lyr.num)(ay3)(ax3) =/= 0) {
        if (power(lyr.num)(ay3)(ax3) =/= 0 && !(power(lyr.num)(ay3)(ax3) === power(lyr.num)(uy)(ux) - conducts(
              blocks(lyr.num)(uy)(ux).id).toFloat) &&
            (!(blockId >= ZythiumAmplifierRightBlock.id && blockId <= ZythiumAmplifierUpOnBlock.id || blockId >= ZythiumInverterRightBlock.id && blockId <= ZythiumInverterUpOnBlock.id) ||
            !(blockId >= ZythiumAmplifierRightBlock.id && blockId <= ZythiumAmplifierUpOnBlock.id && ux > ax3 && block =/= ZythiumAmplifierRightBlock && block =/= ZythiumAmplifierRightOnBlock ||
              blockId >= ZythiumAmplifierRightBlock.id && blockId <= ZythiumAmplifierUpOnBlock.id && uy > ay3 && block =/= ZythiumAmplifierDownBlock && block =/= ZythiumAmplifierDownOnBlock ||
              blockId >= ZythiumAmplifierRightBlock.id && blockId <= ZythiumAmplifierUpOnBlock.id && ux < ax3 && block =/= ZythiumAmplifierLeftBlock && block =/= ZythiumAmplifierLeftOnBlock ||
              blockId >= ZythiumAmplifierRightBlock.id && blockId <= ZythiumAmplifierUpOnBlock.id && uy < ay3 && block =/= ZythiumAmplifierUpBlock && block =/= ZythiumAmplifierUpOnBlock) &&
            !(blockId >= ZythiumInverterRightBlock.id && blockId <= ZythiumInverterUpOnBlock.id && ux > ax3 && block =/= ZythiumInverterRightBlock && block =/= ZythiumInverterRightOnBlock ||
              blockId >= ZythiumInverterRightBlock.id && blockId <= ZythiumInverterUpOnBlock.id && uy > ay3 && block =/= ZythiumInverterDownBlock && block =/= ZythiumInverterDownOnBlock ||
              blockId >= ZythiumInverterRightBlock.id && blockId <= ZythiumInverterUpOnBlock.id && ux < ax3 && block =/= ZythiumInverterLeftBlock && block =/= ZythiumInverterLeftOnBlock ||
              blockId >= ZythiumInverterRightBlock.id && blockId <= ZythiumInverterUpOnBlock.id && uy < ay3 && block =/= ZythiumInverterUpBlock && block =/= ZythiumInverterUpOnBlock) &&
            !(blockId >= ZythiumDelayer1DelayRightBlock.id && blockId <= ZythiumDelayer8DelayUpOnBlock.id && ux > ax3 && block =/= ZythiumDelayer1DelayRightBlock && block =/= ZythiumDelayer1DelayRightOnBlock && block =/= ZythiumDelayer2DelayRightBlock && block =/= ZythiumDelayer2DelayRightOnBlock && block =/= ZythiumDelayer4DelayRightBlock && block =/= ZythiumDelayer4DelayRightOnBlock && block =/= ZythiumDelayer8DelayRightBlock && block =/= ZythiumDelayer8DelayRightOnBlock ||
              blockId >= ZythiumDelayer1DelayRightBlock.id && blockId <= ZythiumDelayer8DelayUpOnBlock.id && uy > ay3 && block =/= ZythiumDelayer1DelayDownBlock && block =/= ZythiumDelayer1DelayDownOnBlock && block =/= ZythiumDelayer2DelayDownBlock && block =/= ZythiumDelayer2DelayDownOnBlock && block =/= ZythiumDelayer4DelayDownBlock && block =/= ZythiumDelayer4DelayDownOnBlock && block =/= ZythiumDelayer8DelayDownBlock && block =/= ZythiumDelayer8DelayDownOnBlock ||
              blockId >= ZythiumDelayer1DelayRightBlock.id && blockId <= ZythiumDelayer8DelayUpOnBlock.id && ux < ax3 && block =/= ZythiumDelayer1DelayLeftBlock && block =/= ZythiumDelayer1DelayLeftOnBlock && block =/= ZythiumDelayer2DelayLeftBlock && block =/= ZythiumDelayer2DelayLeftOnBlock && block =/= ZythiumDelayer4DelayLeftBlock && block =/= ZythiumDelayer4DelayLeftOnBlock && block =/= ZythiumDelayer8DelayLeftBlock && block =/= ZythiumDelayer8DelayLeftOnBlock ||
              blockId >= ZythiumDelayer1DelayRightBlock.id && blockId <= ZythiumDelayer8DelayUpOnBlock.id && uy < ay3 && block =/= ZythiumDelayer1DelayUpBlock && block =/= ZythiumDelayer1DelayUpOnBlock && block =/= ZythiumDelayer2DelayUpBlock && block =/= ZythiumDelayer2DelayUpOnBlock && block =/= ZythiumDelayer4DelayUpBlock && block =/= ZythiumDelayer4DelayUpOnBlock && block =/= ZythiumDelayer8DelayUpBlock && block =/= ZythiumDelayer8DelayUpOnBlock))) {
          logger.debug(s"Added tile $ax3 $ay3 to PQueue.")
          addTileToPQueue(ax3, ay3)
          remember(ir) = true
        }
      }
    }
    (0 until 4).foreach { ir =>
      logger.debug(s"[liek srsly man?] $ir")
      ax3 = ux + cl(ir)(0)
      ay3 = uy + cl(ir)(1)
      logger.debug(s"(rpbRecur2) $ax3 $ay3 ${power(lyr.num)(ay3)(ax3)}")
      lazy val userBlock   = blocks(lyr.num)(uy)(ux)
      lazy val userBlockId = userBlock.id
      if (ay3 >= 0 && ay3 < HEIGHT && power(lyr.num)(ay3)(ax3) =/= 0) {
        logger.debug(s"(rbpRecur) ${power(lyr.num)(ay3)(ax3)} ${power(lyr.num)(uy)(ux)} ${conducts(userBlockId)}")
        if ((power(lyr.num)(ay3)(ax3) === power(lyr.num)(uy)(ux) - conducts(userBlockId).toFloat) &&
            (!(blocks(lyr.num)(ay3)(ax3).id >= ZythiumAmplifierRightBlock.id && blocks(lyr.num)(ay3)(ax3).id <= ZythiumAmplifierUpOnBlock.id || blocks(
              lyr.num)(ay3)(ax3).id >= ZythiumInverterRightBlock.id && blocks(lyr.num)(ay3)(ax3).id <= ZythiumInverterUpOnBlock.id) ||
            !(userBlockId >= ZythiumAmplifierRightBlock.id && userBlockId <= ZythiumAmplifierUpOnBlock.id && ux < ax3 && userBlock =/= ZythiumAmplifierRightBlock && userBlock =/= ZythiumAmplifierRightOnBlock ||
              userBlockId >= ZythiumAmplifierRightBlock.id && userBlockId <= ZythiumAmplifierUpOnBlock.id && uy < ay3 && userBlock =/= ZythiumAmplifierDownBlock && userBlock =/= ZythiumAmplifierDownOnBlock ||
              userBlockId >= ZythiumAmplifierRightBlock.id && userBlockId <= ZythiumAmplifierUpOnBlock.id && ux > ax3 && userBlock =/= ZythiumAmplifierLeftBlock && userBlock =/= ZythiumAmplifierLeftOnBlock ||
              userBlockId >= ZythiumAmplifierRightBlock.id && userBlockId <= ZythiumAmplifierUpOnBlock.id && uy > ay3 && userBlock =/= ZythiumAmplifierUpBlock && userBlock =/= ZythiumAmplifierUpOnBlock) &&
            !(userBlockId >= ZythiumInverterRightBlock.id && userBlockId <= ZythiumInverterUpOnBlock.id && ux < ax3 && userBlock =/= ZythiumInverterRightBlock && userBlock =/= ZythiumInverterRightOnBlock ||
              userBlockId >= ZythiumInverterRightBlock.id && userBlockId <= ZythiumInverterUpOnBlock.id && uy < ay3 && userBlock =/= ZythiumInverterDownBlock && userBlock =/= ZythiumInverterDownOnBlock ||
              userBlockId >= ZythiumInverterRightBlock.id && userBlockId <= ZythiumInverterUpOnBlock.id && ux > ax3 && userBlock =/= ZythiumInverterLeftBlock && userBlock =/= ZythiumInverterLeftOnBlock ||
              userBlockId >= ZythiumInverterRightBlock.id && userBlockId <= ZythiumInverterUpOnBlock.id && uy > ay3 && userBlock =/= ZythiumInverterUpBlock && userBlock =/= ZythiumInverterUpOnBlock) &&
            !(userBlockId >= ZythiumDelayer1DelayRightBlock.id && userBlockId <= ZythiumDelayer8DelayUpOnBlock.id && ux < ax3 && userBlock =/= ZythiumDelayer1DelayRightBlock && userBlock =/= ZythiumDelayer1DelayRightOnBlock && userBlock =/= ZythiumDelayer2DelayRightBlock && userBlock =/= ZythiumDelayer2DelayRightOnBlock && userBlock =/= ZythiumDelayer4DelayRightBlock && userBlock =/= ZythiumDelayer4DelayRightOnBlock && userBlock =/= ZythiumDelayer8DelayRightBlock && userBlock =/= ZythiumDelayer8DelayRightOnBlock ||
              userBlockId >= ZythiumDelayer1DelayRightBlock.id && userBlockId <= ZythiumDelayer8DelayUpOnBlock.id && uy < ay3 && userBlock =/= ZythiumDelayer1DelayDownBlock && userBlock =/= ZythiumDelayer1DelayDownOnBlock && userBlock =/= ZythiumDelayer2DelayDownBlock && userBlock =/= ZythiumDelayer2DelayDownOnBlock && userBlock =/= ZythiumDelayer4DelayDownBlock && userBlock =/= ZythiumDelayer4DelayDownOnBlock && userBlock =/= ZythiumDelayer8DelayDownBlock && userBlock =/= ZythiumDelayer8DelayDownOnBlock ||
              userBlockId >= ZythiumDelayer1DelayRightBlock.id && userBlockId <= ZythiumDelayer8DelayUpOnBlock.id && ux > ax3 && userBlock =/= ZythiumDelayer1DelayLeftBlock && userBlock =/= ZythiumDelayer1DelayLeftOnBlock && userBlock =/= ZythiumDelayer2DelayLeftBlock && userBlock =/= ZythiumDelayer2DelayLeftOnBlock && userBlock =/= ZythiumDelayer4DelayLeftBlock && userBlock =/= ZythiumDelayer4DelayLeftOnBlock && userBlock =/= ZythiumDelayer8DelayLeftBlock && userBlock =/= ZythiumDelayer8DelayLeftOnBlock ||
              userBlockId >= ZythiumDelayer1DelayRightBlock.id && userBlockId <= ZythiumDelayer8DelayUpOnBlock.id && uy > ay3 && userBlock =/= ZythiumDelayer1DelayUpBlock && userBlock =/= ZythiumDelayer1DelayUpOnBlock && userBlock =/= ZythiumDelayer2DelayUpBlock && userBlock =/= ZythiumDelayer2DelayUpOnBlock && userBlock =/= ZythiumDelayer4DelayUpBlock && userBlock =/= ZythiumDelayer4DelayUpOnBlock && userBlock =/= ZythiumDelayer8DelayUpBlock && userBlock =/= ZythiumDelayer8DelayUpOnBlock))) {
          if (!arbprd(lyr.num)(ay3)(ax3)) {
            rbpRecur(ax3, ay3, lyr)
            if (conducts(blocks(lyr.num)(ay3)(ax3).id) >= 0 && wcnct(ay3)(ax3)) {
              if (lyr === BackgroundLayer) {
                if (receives(blocks(PrimaryLayer.num)(ay3)(ax3).id)) {
                  rbpRecur(ax3, ay3, PrimaryLayer)
                  if (powers(blocks(PrimaryLayer.num)(ay3)(ax3))) {
                    addTileToPQueue(ax3, ay3)
                  }
                }
                if (receives(blocks(ForegroundLayer.num)(ay3)(ax3).id)) {
                  rbpRecur(ax3, ay3, ForegroundLayer)
                  if (powers(blocks(ForegroundLayer.num)(ay3)(ax3))) {
                    addTileToPQueue(ax3, ay3)
                  }
                }
              }
              if (lyr === PrimaryLayer) {
                if (receives(blocks(BackgroundLayer.num)(ay3)(ax3).id)) {
                  rbpRecur(ax3, ay3, BackgroundLayer)
                  if (powers(blocks(BackgroundLayer.num)(ay3)(ax3))) {
                    addTileToPQueue(ax3, ay3)
                  }
                }
                if (receives(blocks(ForegroundLayer.num)(ay3)(ax3).id)) {
                  rbpRecur(ax3, ay3, ForegroundLayer)
                  if (powers(blocks(ForegroundLayer.num)(ay3)(ax3))) {
                    addTileToPQueue(ax3, ay3)
                  }
                }
              }
              if (lyr === ForegroundLayer) {
                if (receives(blocks(BackgroundLayer.num)(ay3)(ax3).id)) {
                  rbpRecur(ax3, ay3, BackgroundLayer)
                  if (powers(blocks(BackgroundLayer.num)(ay3)(ax3))) {
                    addTileToPQueue(ax3, ay3)
                  }
                }
                if (receives(blocks(PrimaryLayer.num)(ay3)(ax3).id)) {
                  rbpRecur(ax3, ay3, PrimaryLayer)
                  if (powers(blocks(PrimaryLayer.num)(ay3)(ax3))) {
                    addTileToPQueue(ax3, ay3)
                  }
                }
              }
            }
          }
        }
      }
      lazy val block   = blocks(lyr.num)(ay3)(ax3)
      lazy val blockId = block.id
      if (block === ZythiumLampOnBlock ||
          (
            blockId >= ZythiumAmplifierRightBlock.id &&
            blockId <= ZythiumAmplifierUpOnBlock.id
            ||
            blockId >= ZythiumInverterRightBlock.id &&
            blockId <= ZythiumInverterUpOnBlock.id
            ||
            blockId >= ZythiumDelayer1DelayRightBlock.id
            && blockId <= ZythiumDelayer8DelayUpOnBlock.id
          )
          &&
          !(
            blockId >= ZythiumAmplifierRightBlock.id &&
              blockId <= ZythiumAmplifierUpOnBlock.id &&
              ux < ax3 &&
              block =/= ZythiumAmplifierRightBlock &&
              block =/= ZythiumAmplifierRightOnBlock
              ||
                blockId >= ZythiumAmplifierRightBlock.id &&
                  blockId <= ZythiumAmplifierUpOnBlock.id &&
                  uy < ay3 &&
                  block =/= ZythiumAmplifierDownBlock &&
                  block =/= ZythiumAmplifierDownOnBlock
              ||
                blockId >= ZythiumAmplifierRightBlock.id &&
                  blockId <= ZythiumAmplifierUpOnBlock.id &&
                  ux > ax3 &&
                  block =/= ZythiumAmplifierLeftBlock &&
                  block =/= ZythiumAmplifierLeftOnBlock
              ||
                blockId >= ZythiumAmplifierRightBlock.id &&
                  blockId <= ZythiumAmplifierUpOnBlock.id &&
                  uy > ay3 &&
                  block =/= ZythiumAmplifierUpBlock &&
                  block =/= ZythiumAmplifierUpOnBlock
          )
          &&
          !(
            blockId >= ZythiumInverterRightBlock.id &&
              blockId <= ZythiumInverterUpOnBlock.id &&
              ux < ax3 &&
              block =/= ZythiumInverterRightBlock &&
              block =/= ZythiumInverterRightOnBlock
              ||
                blockId >= ZythiumInverterRightBlock.id &&
                  blockId <= ZythiumInverterUpOnBlock.id &&
                  uy < ay3 &&
                  block =/= ZythiumInverterDownBlock &&
                  block =/= ZythiumInverterDownOnBlock
              ||
                blockId >= ZythiumInverterRightBlock.id &&
                  blockId <= ZythiumInverterUpOnBlock.id &&
                  ux > ax3 &&
                  block =/= ZythiumInverterLeftBlock &&
                  block =/= ZythiumInverterLeftOnBlock
              ||
                blockId >= ZythiumInverterRightBlock.id &&
                  blockId <= ZythiumInverterUpOnBlock.id &&
                  uy > ay3 &&
                  block =/= ZythiumInverterUpBlock &&
                  block =/= ZythiumInverterUpOnBlock
          )
          &&
          !(blockId >= ZythiumDelayer1DelayRightBlock.id &&
            blockId <= ZythiumDelayer8DelayUpOnBlock.id &&
            ux < ax3 &&
            block =/= ZythiumDelayer1DelayRightBlock &&
            block =/= ZythiumDelayer1DelayRightOnBlock &&
            block =/= ZythiumDelayer2DelayRightBlock &&
            block =/= ZythiumDelayer2DelayRightOnBlock &&
            block =/= ZythiumDelayer4DelayRightBlock &&
            block =/= ZythiumDelayer4DelayRightOnBlock &&
            block =/= ZythiumDelayer8DelayRightBlock &&
            block =/= ZythiumDelayer8DelayRightOnBlock
            ||
              blockId >= ZythiumDelayer1DelayRightBlock.id &&
                blockId <= ZythiumDelayer8DelayUpOnBlock.id &&
                uy < ay3 &&
                block =/= ZythiumDelayer1DelayDownBlock &&
                block =/= ZythiumDelayer1DelayDownOnBlock &&
                block =/= ZythiumDelayer2DelayDownBlock &&
                block =/= ZythiumDelayer2DelayDownOnBlock &&
                block =/= ZythiumDelayer4DelayDownBlock &&
                block =/= ZythiumDelayer4DelayDownOnBlock &&
                block =/= ZythiumDelayer8DelayDownBlock &&
                block =/= ZythiumDelayer8DelayDownOnBlock
            ||
              blockId >= ZythiumDelayer1DelayRightBlock.id &&
                blockId <= ZythiumDelayer8DelayUpOnBlock.id &&
                ux > ax3 &&
                block =/= ZythiumDelayer1DelayLeftBlock &&
                block =/= ZythiumDelayer1DelayLeftOnBlock &&
                block =/= ZythiumDelayer2DelayLeftBlock &&
                block =/= ZythiumDelayer2DelayLeftOnBlock &&
                block =/= ZythiumDelayer4DelayLeftBlock &&
                block =/= ZythiumDelayer4DelayLeftOnBlock &&
                block =/= ZythiumDelayer8DelayLeftBlock &&
                block =/= ZythiumDelayer8DelayLeftOnBlock
            ||
              blockId >= ZythiumDelayer1DelayRightBlock.id &&
                blockId <= ZythiumDelayer8DelayUpOnBlock.id &&
                uy > ay3 &&
                block =/= ZythiumDelayer1DelayUpBlock &&
                block =/= ZythiumDelayer1DelayUpOnBlock &&
                block =/= ZythiumDelayer2DelayUpBlock &&
                block =/= ZythiumDelayer2DelayUpOnBlock &&
                block =/= ZythiumDelayer4DelayUpBlock &&
                block =/= ZythiumDelayer4DelayUpOnBlock &&
                block =/= ZythiumDelayer8DelayUpBlock &&
                block =/= ZythiumDelayer8DelayUpOnBlock)) {
        if (blockId >= ZythiumInverterRightOnBlock.id && blockId <= ZythiumInverterUpOnBlock.id) {
          blocks(lyr.num)(ay3)(ax3) = Block.withId(blockId - 4)
          logger.debug(s"Adding power for inverter at ($ax3, $ay3).")
          addBlockPower(ax3, ay3)
          addBlockLighting(ax3, ay3)
          rdrawn(ay3)(ax3) = false
        }
        removeBlockPower(ax3, ay3, lyr)
      }
    }
    (0 until 4).foreach { ir =>
      if (remember(ir) && uy + cl(ir)(1) >= 0 && uy + cl(ir)(1) < HEIGHT) {
        power(lyr.num)(uy + cl(ir)(1))(ux + cl(ir)(0)) = 5.toFloat
      }
    }
    power(lyr.num)(uy)(ux) = 0.toFloat
    arbprd(lyr.num)(uy)(ux) = false
  }

  def removeBlockPower(ux: Int, uy: Int, lyr: Layer): Unit = {
    removeBlockPower(ux, uy, lyr, true)
  }

  def removeBlockPower(ux: Int, uy: Int, lyr: Layer, turnOffDelayer: Boolean): Unit = {
    arbprd(lyr.num)(uy)(ux) = true
    logger.debug(s"[rbp ] $ux $uy ${lyr.num} $turnOffDelayer")
    if (!((blocks(lyr.num)(uy)(ux).id >= ZythiumDelayer1DelayRightOnBlock.id && blocks(lyr.num)(uy)(ux).id <= ZythiumDelayer1DelayUpOnBlock.id || blocks(
          lyr.num)(uy)(ux).id >= ZythiumDelayer2DelayRightOnBlock.id && blocks(lyr.num)(uy)(ux).id <= ZythiumDelayer2DelayUpOnBlock.id || blocks(
          lyr.num)(uy)(ux).id >= ZythiumDelayer4DelayRightOnBlock.id && blocks(lyr.num)(uy)(ux).id <= ZythiumDelayer4DelayUpOnBlock.id || blocks(
          lyr.num)(uy)(ux).id >= ZythiumDelayer8DelayRightOnBlock.id && blocks(lyr.num)(uy)(ux).id <= ZythiumDelayer8DelayUpOnBlock.id) && turnOffDelayer)) {
      var ax3, ay3: Int = 0
      (0 until 4).foreach { ir =>
        ax3 = ux + cl(ir)(0)
        ay3 = uy + cl(ir)(1)
        lazy val block   = blocks(lyr.num)(ay3)(ax3)
        lazy val blockId = block.id
        if (ay3 >= 0 && ay3 < HEIGHT && power(lyr.num)(ay3)(ax3) =/= 0) {
          if (!(power(lyr.num)(ay3)(ax3) === power(lyr.num)(uy)(ux) - conducts(blocks(lyr.num)(uy)(ux).id).toFloat) &&
              (!(blockId >= ZythiumAmplifierRightBlock.id && blockId <= ZythiumAmplifierUpOnBlock.id || blockId >= ZythiumInverterRightBlock.id && blockId <= ZythiumInverterUpOnBlock.id) ||
              !(blockId >= ZythiumAmplifierRightBlock.id && blockId <= ZythiumAmplifierUpOnBlock.id && ux > ax3 && block =/= ZythiumAmplifierRightBlock && block =/= ZythiumAmplifierRightOnBlock ||
                blockId >= ZythiumAmplifierRightBlock.id && blockId <= ZythiumAmplifierUpOnBlock.id && uy > ay3 && block =/= ZythiumAmplifierDownBlock && block =/= ZythiumAmplifierDownOnBlock ||
                blockId >= ZythiumAmplifierRightBlock.id && blockId <= ZythiumAmplifierUpOnBlock.id && ux < ax3 && block =/= ZythiumAmplifierLeftBlock && block =/= ZythiumAmplifierLeftOnBlock ||
                blockId >= ZythiumAmplifierRightBlock.id && blockId <= ZythiumAmplifierUpOnBlock.id && uy < ay3 && block =/= ZythiumAmplifierUpBlock && block =/= ZythiumAmplifierUpOnBlock) &&
              !(blockId >= ZythiumInverterRightBlock.id && blockId <= ZythiumInverterUpOnBlock.id && ux > ax3 && block =/= ZythiumInverterRightBlock && block =/= ZythiumInverterRightOnBlock ||
                blockId >= ZythiumInverterRightBlock.id && blockId <= ZythiumInverterUpOnBlock.id && uy > ay3 && block =/= ZythiumInverterDownBlock && block =/= ZythiumInverterDownOnBlock ||
                blockId >= ZythiumInverterRightBlock.id && blockId <= ZythiumInverterUpOnBlock.id && ux < ax3 && block =/= ZythiumInverterLeftBlock && block =/= ZythiumInverterLeftOnBlock ||
                blockId >= ZythiumInverterRightBlock.id && blockId <= ZythiumInverterUpOnBlock.id && uy < ay3 && block =/= ZythiumInverterUpBlock && block =/= ZythiumInverterUpOnBlock) &&
              !(blockId >= ZythiumDelayer1DelayRightBlock.id && blockId <= ZythiumDelayer8DelayUpOnBlock.id && ux > ax3 && block =/= ZythiumDelayer1DelayRightBlock && block =/= ZythiumDelayer1DelayRightOnBlock && block =/= ZythiumDelayer2DelayRightBlock && block =/= ZythiumDelayer2DelayRightOnBlock && block =/= ZythiumDelayer4DelayRightBlock && block =/= ZythiumDelayer4DelayRightOnBlock && block =/= ZythiumDelayer8DelayRightBlock && block =/= ZythiumDelayer8DelayRightOnBlock ||
                blockId >= ZythiumDelayer1DelayRightBlock.id && blockId <= ZythiumDelayer8DelayUpOnBlock.id && uy > ay3 && block =/= ZythiumDelayer1DelayDownBlock && block =/= ZythiumDelayer1DelayDownOnBlock && block =/= ZythiumDelayer2DelayDownBlock && block =/= ZythiumDelayer2DelayDownOnBlock && block =/= ZythiumDelayer4DelayDownBlock && block =/= ZythiumDelayer4DelayDownOnBlock && block =/= ZythiumDelayer8DelayDownBlock && block =/= ZythiumDelayer8DelayDownOnBlock ||
                blockId >= ZythiumDelayer1DelayRightBlock.id && blockId <= ZythiumDelayer8DelayUpOnBlock.id && ux < ax3 && block =/= ZythiumDelayer1DelayLeftBlock && block =/= ZythiumDelayer1DelayLeftOnBlock && block =/= ZythiumDelayer2DelayLeftBlock && block =/= ZythiumDelayer2DelayLeftOnBlock && block =/= ZythiumDelayer4DelayLeftBlock && block =/= ZythiumDelayer4DelayLeftOnBlock && block =/= ZythiumDelayer8DelayLeftBlock && block =/= ZythiumDelayer8DelayLeftOnBlock ||
                blockId >= ZythiumDelayer1DelayRightBlock.id && blockId <= ZythiumDelayer8DelayUpOnBlock.id && uy < ay3 && block =/= ZythiumDelayer1DelayUpBlock && block =/= ZythiumDelayer1DelayUpOnBlock && block =/= ZythiumDelayer2DelayUpBlock && block =/= ZythiumDelayer2DelayUpOnBlock && block =/= ZythiumDelayer4DelayUpBlock && block =/= ZythiumDelayer4DelayUpOnBlock && block =/= ZythiumDelayer8DelayUpBlock && block =/= ZythiumDelayer8DelayUpOnBlock))) {
            logger.debug(s"Added tile $ax3 $ay3 to PQueue.")
            addTileToPQueue(ax3, ay3)
          }
        }
      }
      (0 until 4).foreach { ir =>
        ax3 = ux + cl(ir)(0)
        ay3 = uy + cl(ir)(1)
        lazy val userBlock   = blocks(lyr.num)(uy)(ux)
        lazy val userBlockId = userBlock.id
        logger.debug(s"${blocks(lyr.num)(ay3)(ax3)} ${power(lyr.num)(ay3)(ax3)}")
        if (ay3 >= 0 && ay3 < HEIGHT && power(lyr.num)(ay3)(ax3) =/= 0) {
          logger.debug(s"${power(lyr.num)(uy)(ux)} ${power(lyr.num)(ay3)(ax3)} ${conducts(userBlockId)}")
          if (power(lyr.num)(ay3)(ax3) === power(lyr.num)(uy)(ux) - conducts(userBlockId).toFloat) {
            if (!(blocks(lyr.num)(ay3)(ax3).id >= ZythiumAmplifierRightBlock.id && blocks(lyr.num)(ay3)(ax3).id <= ZythiumAmplifierUpOnBlock.id || blocks(
                  lyr.num)(ay3)(ax3).id >= ZythiumInverterRightBlock.id && blocks(lyr.num)(ay3)(ax3).id <= ZythiumInverterUpOnBlock.id) ||
                !(userBlockId >= ZythiumAmplifierRightBlock.id && userBlockId <= ZythiumAmplifierUpOnBlock.id && ux < ax3 && userBlock =/= ZythiumAmplifierRightBlock && userBlock =/= ZythiumAmplifierRightOnBlock ||
                  userBlockId >= ZythiumAmplifierRightBlock.id && userBlockId <= ZythiumAmplifierUpOnBlock.id && uy < ay3 && userBlock =/= ZythiumAmplifierDownBlock && userBlock =/= ZythiumAmplifierDownOnBlock ||
                  userBlockId >= ZythiumAmplifierRightBlock.id && userBlockId <= ZythiumAmplifierUpOnBlock.id && ux > ax3 && userBlock =/= ZythiumAmplifierLeftBlock && userBlock =/= ZythiumAmplifierLeftOnBlock ||
                  userBlockId >= ZythiumAmplifierRightBlock.id && userBlockId <= ZythiumAmplifierUpOnBlock.id && uy > ay3 && userBlock =/= ZythiumAmplifierUpBlock && userBlock =/= ZythiumAmplifierUpOnBlock) &&
                !(userBlockId >= ZythiumInverterRightBlock.id && userBlockId <= ZythiumInverterUpOnBlock.id && ux < ax3 && userBlock =/= ZythiumInverterRightBlock && userBlock =/= ZythiumInverterRightOnBlock ||
                  userBlockId >= ZythiumInverterRightBlock.id && userBlockId <= ZythiumInverterUpOnBlock.id && uy < ay3 && userBlock =/= ZythiumInverterDownBlock && userBlock =/= ZythiumInverterDownOnBlock ||
                  userBlockId >= ZythiumInverterRightBlock.id && userBlockId <= ZythiumInverterUpOnBlock.id && ux > ax3 && userBlock =/= ZythiumInverterLeftBlock && userBlock =/= ZythiumInverterLeftOnBlock ||
                  userBlockId >= ZythiumInverterRightBlock.id && userBlockId <= ZythiumInverterUpOnBlock.id && uy > ay3 && userBlock =/= ZythiumInverterUpBlock && userBlock =/= ZythiumInverterUpOnBlock) &&
                !(userBlockId >= ZythiumDelayer1DelayRightBlock.id && userBlockId <= ZythiumDelayer8DelayUpOnBlock.id && ux < ax3 && userBlock =/= ZythiumDelayer1DelayRightBlock && userBlock =/= ZythiumDelayer1DelayRightOnBlock && userBlock =/= ZythiumDelayer2DelayRightBlock && userBlock =/= ZythiumDelayer2DelayRightOnBlock && userBlock =/= ZythiumDelayer4DelayRightBlock && userBlock =/= ZythiumDelayer4DelayRightOnBlock && userBlock =/= ZythiumDelayer8DelayRightBlock && userBlock =/= ZythiumDelayer8DelayRightOnBlock ||
                  userBlockId >= ZythiumDelayer1DelayRightBlock.id && userBlockId <= ZythiumDelayer8DelayUpOnBlock.id && uy < ay3 && userBlock =/= ZythiumDelayer1DelayDownBlock && userBlock =/= ZythiumDelayer1DelayDownOnBlock && userBlock =/= ZythiumDelayer2DelayDownBlock && userBlock =/= ZythiumDelayer2DelayDownOnBlock && userBlock =/= ZythiumDelayer4DelayDownBlock && userBlock =/= ZythiumDelayer4DelayDownOnBlock && userBlock =/= ZythiumDelayer8DelayDownBlock && userBlock =/= ZythiumDelayer8DelayDownOnBlock ||
                  userBlockId >= ZythiumDelayer1DelayRightBlock.id && userBlockId <= ZythiumDelayer8DelayUpOnBlock.id && ux > ax3 && userBlock =/= ZythiumDelayer1DelayLeftBlock && userBlock =/= ZythiumDelayer1DelayLeftOnBlock && userBlock =/= ZythiumDelayer2DelayLeftBlock && userBlock =/= ZythiumDelayer2DelayLeftOnBlock && userBlock =/= ZythiumDelayer4DelayLeftBlock && userBlock =/= ZythiumDelayer4DelayLeftOnBlock && userBlock =/= ZythiumDelayer8DelayLeftBlock && userBlock =/= ZythiumDelayer8DelayLeftOnBlock ||
                  userBlockId >= ZythiumDelayer1DelayRightBlock.id && userBlockId <= ZythiumDelayer8DelayUpOnBlock.id && uy > ay3 && userBlock =/= ZythiumDelayer1DelayUpBlock && userBlock =/= ZythiumDelayer1DelayUpOnBlock && userBlock =/= ZythiumDelayer2DelayUpBlock && userBlock =/= ZythiumDelayer2DelayUpOnBlock && userBlock =/= ZythiumDelayer4DelayUpBlock && userBlock =/= ZythiumDelayer4DelayUpOnBlock && userBlock =/= ZythiumDelayer8DelayUpBlock && userBlock =/= ZythiumDelayer8DelayUpOnBlock)) {
              if (!arbprd(lyr.num)(ay3)(ax3)) {
                rbpRecur(ax3, ay3, lyr)
                if (conducts(blocks(lyr.num)(ay3)(ax3).id) >= 0 && wcnct(ay3)(ax3)) {
                  if (lyr === BackgroundLayer) {
                    if (receives(blocks(PrimaryLayer.num)(ay3)(ax3).id)) {
                      rbpRecur(ax3, ay3, PrimaryLayer)
                      if (powers(blocks(PrimaryLayer.num)(ay3)(ax3))) {
                        addTileToPQueue(ax3, ay3)
                      }
                    }
                    if (receives(blocks(ForegroundLayer.num)(ay3)(ax3).id)) {
                      rbpRecur(ax3, ay3, ForegroundLayer)
                      if (powers(blocks(ForegroundLayer.num)(ay3)(ax3))) {
                        addTileToPQueue(ax3, ay3)
                      }
                    }
                  }
                  if (lyr === PrimaryLayer) {
                    if (receives(blocks(BackgroundLayer.num)(ay3)(ax3).id)) {
                      rbpRecur(ax3, ay3, BackgroundLayer)
                      if (powers(blocks(BackgroundLayer.num)(ay3)(ax3))) {
                        addTileToPQueue(ax3, ay3)
                      }
                    }
                    if (receives(blocks(ForegroundLayer.num)(ay3)(ax3).id)) {
                      rbpRecur(ax3, ay3, ForegroundLayer)
                      if (powers(blocks(ForegroundLayer.num)(ay3)(ax3))) {
                        addTileToPQueue(ax3, ay3)
                      }
                    }
                  }
                  if (lyr === ForegroundLayer) {
                    if (receives(blocks(BackgroundLayer.num)(ay3)(ax3).id)) {
                      rbpRecur(ax3, ay3, BackgroundLayer)
                      if (powers(blocks(BackgroundLayer.num)(ay3)(ax3))) {
                        addTileToPQueue(ax3, ay3)
                      }
                    }
                    if (receives(blocks(PrimaryLayer.num)(ay3)(ax3).id)) {
                      rbpRecur(ax3, ay3, PrimaryLayer)
                      if (powers(blocks(PrimaryLayer.num)(ay3)(ax3))) {
                        addTileToPQueue(ax3, ay3)
                      }
                    }
                  }
                }
              }
            }
          }
        }
        lazy val block   = blocks(lyr.num)(ay3)(ax3)
        lazy val blockId = block.id
        if (block === ZythiumLampOnBlock || (blockId >= ZythiumAmplifierRightBlock.id && blockId <= ZythiumAmplifierUpOnBlock.id || blockId >= ZythiumInverterRightBlock.id && blockId <= ZythiumInverterUpOnBlock.id || blockId >= ZythiumDelayer1DelayRightBlock.id && blockId <= ZythiumDelayer8DelayUpOnBlock.id) &&
            !(blockId >= ZythiumAmplifierRightBlock.id && blockId <= ZythiumAmplifierUpOnBlock.id && ux < ax3 && block =/= ZythiumAmplifierRightBlock && block =/= ZythiumAmplifierRightOnBlock ||
              blockId >= ZythiumAmplifierRightBlock.id && blockId <= ZythiumAmplifierUpOnBlock.id && uy < ay3 && block =/= ZythiumAmplifierDownBlock && block =/= ZythiumAmplifierDownOnBlock ||
              blockId >= ZythiumAmplifierRightBlock.id && blockId <= ZythiumAmplifierUpOnBlock.id && ux > ax3 && block =/= ZythiumAmplifierLeftBlock && block =/= ZythiumAmplifierLeftOnBlock ||
              blockId >= ZythiumAmplifierRightBlock.id && blockId <= ZythiumAmplifierUpOnBlock.id && uy > ay3 && block =/= ZythiumAmplifierUpBlock && block =/= ZythiumAmplifierUpOnBlock) &&
            !(blockId >= ZythiumInverterRightBlock.id && blockId <= ZythiumInverterUpOnBlock.id && ux < ax3 && block =/= ZythiumInverterRightBlock && block =/= ZythiumInverterRightOnBlock ||
              blockId >= ZythiumInverterRightBlock.id && blockId <= ZythiumInverterUpOnBlock.id && uy < ay3 && block =/= ZythiumInverterDownBlock && block =/= ZythiumInverterDownOnBlock ||
              blockId >= ZythiumInverterRightBlock.id && blockId <= ZythiumInverterUpOnBlock.id && ux > ax3 && block =/= ZythiumInverterLeftBlock && block =/= ZythiumInverterLeftOnBlock ||
              blockId >= ZythiumInverterRightBlock.id && blockId <= ZythiumInverterUpOnBlock.id && uy > ay3 && block =/= ZythiumInverterUpBlock && block =/= ZythiumInverterUpOnBlock) &&
            !(blockId >= ZythiumDelayer1DelayRightBlock.id && blockId <= ZythiumDelayer8DelayUpOnBlock.id && ux < ax3 && block =/= ZythiumDelayer1DelayRightBlock && block =/= ZythiumDelayer1DelayRightOnBlock && block =/= ZythiumDelayer2DelayRightBlock && block =/= ZythiumDelayer2DelayRightOnBlock && block =/= ZythiumDelayer4DelayRightBlock && block =/= ZythiumDelayer4DelayRightOnBlock && block =/= ZythiumDelayer8DelayRightBlock && block =/= ZythiumDelayer8DelayRightOnBlock ||
              blockId >= ZythiumDelayer1DelayRightBlock.id && blockId <= ZythiumDelayer8DelayUpOnBlock.id && uy < ay3 && block =/= ZythiumDelayer1DelayDownBlock && block =/= ZythiumDelayer1DelayDownOnBlock && block =/= ZythiumDelayer2DelayDownBlock && block =/= ZythiumDelayer2DelayDownOnBlock && block =/= ZythiumDelayer4DelayDownBlock && block =/= ZythiumDelayer4DelayDownOnBlock && block =/= ZythiumDelayer8DelayDownBlock && block =/= ZythiumDelayer8DelayDownOnBlock ||
              blockId >= ZythiumDelayer1DelayRightBlock.id && blockId <= ZythiumDelayer8DelayUpOnBlock.id && ux > ax3 && block =/= ZythiumDelayer1DelayLeftBlock && block =/= ZythiumDelayer1DelayLeftOnBlock && block =/= ZythiumDelayer2DelayLeftBlock && block =/= ZythiumDelayer2DelayLeftOnBlock && block =/= ZythiumDelayer4DelayLeftBlock && block =/= ZythiumDelayer4DelayLeftOnBlock && block =/= ZythiumDelayer8DelayLeftBlock && block =/= ZythiumDelayer8DelayLeftOnBlock ||
              blockId >= ZythiumDelayer1DelayRightBlock.id && blockId <= ZythiumDelayer8DelayUpOnBlock.id && uy > ay3 && block =/= ZythiumDelayer1DelayUpBlock && block =/= ZythiumDelayer1DelayUpOnBlock && block =/= ZythiumDelayer2DelayUpBlock && block =/= ZythiumDelayer2DelayUpOnBlock && block =/= ZythiumDelayer4DelayUpBlock && block =/= ZythiumDelayer4DelayUpOnBlock && block =/= ZythiumDelayer8DelayUpBlock && block =/= ZythiumDelayer8DelayUpOnBlock)) {
          if (blockId >= ZythiumInverterRightOnBlock.id && blockId <= ZythiumInverterUpOnBlock.id) {
            blocks(lyr.num)(ay3)(ax3) = Block.withId(blockId - 4)
            logger.debug(s"Adding power for inverter at ($ax3, $ay3).")
            addBlockPower(ax3, ay3)
            addBlockLighting(ax3, ay3)
            rdrawn(ay3)(ax3) = false
          }
          arbprd(lyr.num)(uy)(ux) = false
          removeBlockPower(ax3, ay3, lyr)
        }
      }
    }
    if (blocks(lyr.num)(uy)(ux) === ZythiumLampOnBlock) {
      removeBlockLighting(ux, uy)
      blocks(lyr.num)(uy)(ux) = ZythiumLampBlock
      rdrawn(uy)(ux) = false
    }
    if (blocks(lyr.num)(uy)(ux).id >= ZythiumAmplifierRightOnBlock.id && blocks(lyr.num)(uy)(ux).id <= ZythiumAmplifierUpOnBlock.id) {
      // blockTemp = blocks(lyr.num)(uy)(ux) TODO: appears to not be used
      blocks(lyr.num)(uy)(ux) = Block.withId(blocks(lyr.num)(uy)(ux).id - 4)
      removeBlockPower(ux, uy, lyr)
      removeBlockLighting(ux, uy)
      rdrawn(uy)(ux) = false
    }
    if (turnOffDelayer && blocks(lyr.num)(uy)(ux).id >= ZythiumDelayer1DelayRightBlock.id && blocks(lyr.num)(uy)(ux).id <= ZythiumDelayer8DelayUpOnBlock.id) {
      logger.debug("???")
      updatex += ux
      updatey += uy
      DDELAY.get(blocks(lyr.num)(uy)(ux).id).foreach(updatet.+=)
      updatel += lyr
    }
    if (!((blocks(lyr.num)(uy)(ux).id >= ZythiumDelayer1DelayRightOnBlock.id && blocks(lyr.num)(uy)(ux).id <= ZythiumDelayer1DelayUpOnBlock.id || blocks(
          lyr.num)(uy)(ux).id >= ZythiumDelayer2DelayRightOnBlock.id && blocks(lyr.num)(uy)(ux).id <= ZythiumDelayer2DelayUpOnBlock.id || blocks(
          lyr.num)(uy)(ux).id >= ZythiumDelayer4DelayRightOnBlock.id && blocks(lyr.num)(uy)(ux).id <= ZythiumDelayer4DelayUpOnBlock.id || blocks(
          lyr.num)(uy)(ux).id >= ZythiumDelayer8DelayRightOnBlock.id && blocks(lyr.num)(uy)(ux).id <= ZythiumDelayer8DelayUpOnBlock.id) && turnOffDelayer)) {
      power(lyr.num)(uy)(ux) = 0.toFloat
    }
    arbprd(lyr.num)(uy)(ux) = false
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

  def redoBlockPower(ux: Int, uy: Int, lyr: Layer): Unit = {
    if (powers(blocks(lyr.num)(uy)(ux)) || blocks(lyr.num)(uy)(ux).id >= ZythiumWireBlock.id && blocks(lyr.num)(uy)(ux).id <= ZythiumWire5PowerBlock.id) {
      addAdjacentTilesToPQueue(ux, uy)
    } else {
      removeBlockPower(ux, uy, lyr)
    }
  }

  def addSunLighting(ux: Int, uy: Int): Unit = { // And including
    (0 until uy).foreach { y =>
      if (ltrans(blocks(PrimaryLayer.num)(y)(ux).id)) {
        return
      }
    }
    var addSources: Boolean = false
    (uy until HEIGHT - 1).foreach { y =>
      if (ltrans(blocks(PrimaryLayer.num)(y + 1)(ux - 1).id) || ltrans(blocks(PrimaryLayer.num)(y + 1)(ux + 1).id)) {
        addSources = true
      }
      if (addSources) {
        addTileToQueue(ux, y)
      }
      if (ltrans(blocks(PrimaryLayer.num)(y)(ux).id)) {
        return
      }
      addTileToZQueue(ux, y)
      lights(y)(ux) = sunlightlevel.toFloat
      lsources(y)(ux) = true
    }
  }

  def removeSunLighting(ux: Int, uy: Int): Unit = { // And including
    val n: Int = sunlightlevel
    (0 until uy).foreach { y =>
      if (ltrans(blocks(PrimaryLayer.num)(y)(ux).id)) {
        return
      }
    }
    import scala.util.control.Breaks._
    breakable {
      (uy until HEIGHT).foreach { y =>
        lsources(y)(ux) = isBlockLightSource(ux, y)
        if (y =/= uy && ltrans(blocks(PrimaryLayer.num)(y)(ux).id)) {
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
      if (ltrans(blocks(PrimaryLayer.num)(ay)(ux).id)) {
        return false
      }
    }
    true
  }

  def isBlockLightSource(ux: Int, uy: Int): Boolean = {
    blocks(BackgroundLayer.num)(uy)(ux) =/= AirBlock && isLightBlock(blocks(BackgroundLayer.num)(uy)(ux)) ||
    blocks(PrimaryLayer.num)(uy)(ux) =/= AirBlock && isLightBlock(blocks(PrimaryLayer.num)(uy)(ux)) ||
    blocks(ForegroundLayer.num)(uy)(ux) =/= AirBlock && isLightBlock(blocks(ForegroundLayer.num)(uy)(ux))
  }

  def isNonLayeredBlockLightSource(ux: Int, uy: Int): Boolean = {
    isNonLayeredBlockLightSource(ux, uy, layer)
  }

  def isNonLayeredBlockLightSource(ux: Int, uy: Int, layer: Layer): Boolean = {
    layer =/= BackgroundLayer && blocks(BackgroundLayer.num)(uy)(ux) =/= AirBlock && isLightBlock(
      blocks(BackgroundLayer.num)(uy)(ux)) ||
    layer =/= PrimaryLayer && blocks(PrimaryLayer.num)(uy)(ux) =/= AirBlock && isLightBlock(
      blocks(PrimaryLayer.num)(uy)(ux)) ||
    layer =/= ForegroundLayer && blocks(ForegroundLayer.num)(uy)(ux) =/= AirBlock && isLightBlock(
      blocks(ForegroundLayer.num)(uy)(ux))
  }

  def findBlockLightSource(ux: Int, uy: Int): Int = {
    if (blocks(BackgroundLayer.num)(uy)(ux) =/= AirBlock)
      max(lightIntensity(blocks(BackgroundLayer.num)(uy)(ux)), 0)
    if (blocks(PrimaryLayer.num)(uy)(ux) =/= AirBlock)
      max(lightIntensity(blocks(PrimaryLayer.num)(uy)(ux)), 0)
    if (blocks(ForegroundLayer.num)(uy)(ux) =/= AirBlock)
      max(lightIntensity(blocks(ForegroundLayer.num)(uy)(ux)), 0)
    else
      0

  }

  def findNonLayeredBlockLightSource(ux: Int, uy: Int): Int = {
    if (blocks(BackgroundLayer.num)(uy)(ux) =/= AirBlock)
      max(lightIntensity(blocks(BackgroundLayer.num)(uy)(ux)), 0)
    if (blocks(PrimaryLayer.num)(uy)(ux) =/= AirBlock)
      max(lightIntensity(blocks(PrimaryLayer.num)(uy)(ux)), 0)
    if (blocks(ForegroundLayer.num)(uy)(ux) =/= AirBlock)
      max(lightIntensity(blocks(ForegroundLayer.num)(uy)(ux)), 0)
    else
      0
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
      pzqn(0)(uy)(ux) = power(BackgroundLayer.num)(uy)(ux).toByte
      pzqn(1)(uy)(ux) = power(PrimaryLayer.num)(uy)(ux).toByte
      pzqn(2)(uy)(ux) = power(ForegroundLayer.num)(uy)(ux).toByte
      pzqd(uy)(ux) = true
    }
  }

  def resolveLightMatrix(): Unit = {
    try {
      while (true) {
        x = lqx(0)
        y = lqy(0)
        if (lsources(y)(x)) {
          val n: Int = findBlockLightSource(x, y)
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
            if (!ltrans(blocks(PrimaryLayer.num)(y2)(x2).id)) {
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
            if (!(blocks(l)(y)(x).id >= ZythiumDelayer1DelayRightBlock.id && blocks(l)(y)(x).id <= ZythiumDelayer8DelayUpOnBlock.id)) {
              addTileToPQueue(x, y)
              power(l)(y)(x) = 5.toFloat
            }
          }
        }
        (0 until 4).foreach { i =>
          x2 = x + cl(i)(0)
          y2 = y + cl(i)(1)
          if (y2 >= 0 && y2 < HEIGHT) {
            Layer.values.foreach { l =>
              lazy val block2   = blocks(l.num)(y2)(x2)
              lazy val blockId2 = blocks(l.num)(y2)(x2).id
              lazy val block    = blocks(l.num)(y)(x)
              lazy val blockId  = block.id
              if (power(l.num)(y)(x) > 0) {
                if (conducts(blockId) >= 0 && receives(blockId2) && !(blockId2 >= ZythiumAmplifierRightBlock.id && blockId2 <= ZythiumAmplifierUpOnBlock.id && x < x2 && block2 =/= ZythiumAmplifierRightBlock && block2 =/= ZythiumAmplifierRightOnBlock ||
                      blockId2 >= ZythiumAmplifierRightBlock.id && blockId2 <= ZythiumAmplifierUpOnBlock.id && y < y2 && block2 =/= ZythiumAmplifierDownBlock && block2 =/= ZythiumAmplifierDownOnBlock ||
                      blockId2 >= ZythiumAmplifierRightBlock.id && blockId2 <= ZythiumAmplifierUpOnBlock.id && x > x2 && block2 =/= ZythiumAmplifierLeftBlock && block2 =/= ZythiumAmplifierLeftOnBlock ||
                      blockId2 >= ZythiumAmplifierRightBlock.id && blockId2 <= ZythiumAmplifierUpOnBlock.id && y > y2 && block2 =/= ZythiumAmplifierUpBlock && block2 =/= ZythiumAmplifierUpOnBlock) &&
                    !(blockId >= ZythiumAmplifierRightBlock.id && blockId <= ZythiumAmplifierUpOnBlock.id && x < x2 && block =/= ZythiumAmplifierRightBlock && block =/= ZythiumAmplifierRightOnBlock ||
                      blockId >= ZythiumAmplifierRightBlock.id && blockId <= ZythiumAmplifierUpOnBlock.id && y < y2 && block =/= ZythiumAmplifierDownBlock && block =/= ZythiumAmplifierDownOnBlock ||
                      blockId >= ZythiumAmplifierRightBlock.id && blockId <= ZythiumAmplifierUpOnBlock.id && x > x2 && block =/= ZythiumAmplifierLeftBlock && block =/= ZythiumAmplifierLeftOnBlock ||
                      blockId >= ZythiumAmplifierRightBlock.id && blockId <= ZythiumAmplifierUpOnBlock.id && y > y2 && block =/= ZythiumAmplifierUpBlock && block =/= ZythiumAmplifierUpOnBlock) &&
                    !(blockId2 >= ZythiumInverterRightBlock.id && blockId2 <= ZythiumInverterUpOnBlock.id && x < x2 && block2 =/= ZythiumInverterRightBlock && block2 =/= ZythiumInverterRightOnBlock ||
                      blockId2 >= ZythiumInverterRightBlock.id && blockId2 <= ZythiumInverterUpOnBlock.id && y < y2 && block2 =/= ZythiumInverterDownBlock && block2 =/= ZythiumInverterDownOnBlock ||
                      blockId2 >= ZythiumInverterRightBlock.id && blockId2 <= ZythiumInverterUpOnBlock.id && x > x2 && block2 =/= ZythiumInverterLeftBlock && block2 =/= ZythiumInverterLeftOnBlock ||
                      blockId2 >= ZythiumInverterRightBlock.id && blockId2 <= ZythiumInverterUpOnBlock.id && y > y2 && block2 =/= ZythiumInverterUpBlock && block2 =/= ZythiumInverterUpOnBlock) &&
                    !(blockId >= ZythiumInverterRightBlock.id && blockId <= ZythiumInverterUpOnBlock.id && x < x2 && block =/= ZythiumInverterRightBlock && block =/= ZythiumInverterRightOnBlock ||
                      blockId >= ZythiumInverterRightBlock.id && blockId <= ZythiumInverterUpOnBlock.id && y < y2 && block =/= ZythiumInverterDownBlock && block =/= ZythiumInverterDownOnBlock ||
                      blockId >= ZythiumInverterRightBlock.id && blockId <= ZythiumInverterUpOnBlock.id && x > x2 && block =/= ZythiumInverterLeftBlock && block =/= ZythiumInverterLeftOnBlock ||
                      blockId >= ZythiumInverterRightBlock.id && blockId <= ZythiumInverterUpOnBlock.id && y > y2 && block =/= ZythiumInverterUpBlock && block =/= ZythiumInverterUpOnBlock) &&
                    !(blockId2 >= ZythiumDelayer1DelayRightBlock.id && blockId2 <= ZythiumDelayer8DelayUpOnBlock.id && x < x2 && block2 =/= ZythiumDelayer1DelayRightBlock && block2 =/= ZythiumDelayer1DelayRightOnBlock && block2 =/= ZythiumDelayer2DelayRightBlock && block2 =/= ZythiumDelayer2DelayRightOnBlock && block2 =/= ZythiumDelayer4DelayRightBlock && block2 =/= ZythiumDelayer4DelayRightOnBlock && block2 =/= ZythiumDelayer8DelayRightBlock && block2 =/= ZythiumDelayer8DelayRightOnBlock ||
                      blockId2 >= ZythiumDelayer1DelayRightBlock.id && blockId2 <= ZythiumDelayer8DelayUpOnBlock.id && y < y2 && block2 =/= ZythiumDelayer1DelayDownBlock && block2 =/= ZythiumDelayer1DelayDownOnBlock && block2 =/= ZythiumDelayer2DelayDownBlock && block2 =/= ZythiumDelayer2DelayDownOnBlock && block2 =/= ZythiumDelayer4DelayDownBlock && block2 =/= ZythiumDelayer4DelayDownOnBlock && block2 =/= ZythiumDelayer8DelayDownBlock && block2 =/= ZythiumDelayer8DelayDownOnBlock ||
                      blockId2 >= ZythiumDelayer1DelayRightBlock.id && blockId2 <= ZythiumDelayer8DelayUpOnBlock.id && x > x2 && block2 =/= ZythiumDelayer1DelayLeftBlock && block2 =/= ZythiumDelayer1DelayLeftOnBlock && block2 =/= ZythiumDelayer2DelayLeftBlock && block2 =/= ZythiumDelayer2DelayLeftOnBlock && block2 =/= ZythiumDelayer4DelayLeftBlock && block2 =/= ZythiumDelayer4DelayLeftOnBlock && block2 =/= ZythiumDelayer8DelayLeftBlock && block2 =/= ZythiumDelayer8DelayLeftOnBlock ||
                      blockId2 >= ZythiumDelayer1DelayRightBlock.id && blockId2 <= ZythiumDelayer8DelayUpOnBlock.id && y > y2 && block2 =/= ZythiumDelayer1DelayUpBlock && block2 =/= ZythiumDelayer1DelayUpOnBlock && block2 =/= ZythiumDelayer2DelayUpBlock && block2 =/= ZythiumDelayer2DelayUpOnBlock && block2 =/= ZythiumDelayer4DelayUpBlock && block2 =/= ZythiumDelayer4DelayUpOnBlock && block2 =/= ZythiumDelayer8DelayUpBlock && block2 =/= ZythiumDelayer8DelayUpOnBlock) &&
                    !(blockId >= ZythiumDelayer1DelayRightBlock.id && blockId <= ZythiumDelayer8DelayUpOnBlock.id && x < x2 && block =/= ZythiumDelayer1DelayRightBlock && block =/= ZythiumDelayer1DelayRightOnBlock && block =/= ZythiumDelayer2DelayRightBlock && block =/= ZythiumDelayer2DelayRightOnBlock && block =/= ZythiumDelayer4DelayRightBlock && block =/= ZythiumDelayer4DelayRightOnBlock && block =/= ZythiumDelayer8DelayRightBlock && block =/= ZythiumDelayer8DelayRightOnBlock ||
                      blockId >= ZythiumDelayer1DelayRightBlock.id && blockId <= ZythiumDelayer8DelayUpOnBlock.id && y < y2 && block =/= ZythiumDelayer1DelayDownBlock && block =/= ZythiumDelayer1DelayDownOnBlock && block =/= ZythiumDelayer2DelayDownBlock && block =/= ZythiumDelayer2DelayDownOnBlock && block =/= ZythiumDelayer4DelayDownBlock && block =/= ZythiumDelayer4DelayDownOnBlock && block =/= ZythiumDelayer8DelayDownBlock && block =/= ZythiumDelayer8DelayDownOnBlock ||
                      blockId >= ZythiumDelayer1DelayRightBlock.id && blockId <= ZythiumDelayer8DelayUpOnBlock.id && x > x2 && block =/= ZythiumDelayer1DelayLeftBlock && block =/= ZythiumDelayer1DelayLeftOnBlock && block =/= ZythiumDelayer2DelayLeftBlock && block =/= ZythiumDelayer2DelayLeftOnBlock && block =/= ZythiumDelayer4DelayLeftBlock && block =/= ZythiumDelayer4DelayLeftOnBlock && block =/= ZythiumDelayer8DelayLeftBlock && block =/= ZythiumDelayer8DelayLeftOnBlock ||
                      blockId >= ZythiumDelayer1DelayRightBlock.id && blockId <= ZythiumDelayer8DelayUpOnBlock.id && y > y2 && block =/= ZythiumDelayer1DelayUpBlock && block =/= ZythiumDelayer1DelayUpOnBlock && block =/= ZythiumDelayer2DelayUpBlock && block =/= ZythiumDelayer2DelayUpOnBlock && block =/= ZythiumDelayer4DelayUpBlock && block =/= ZythiumDelayer4DelayUpOnBlock && block =/= ZythiumDelayer8DelayUpBlock && block =/= ZythiumDelayer8DelayUpOnBlock)) {
                  if (power(l.num)(y2)(x2) <= power(l.num)(y)(x) - conducts(blockId)) {
                    addTileToPZQueue(x2, y2)
                    if (blockId2 >= ZythiumDelayer1DelayRightBlock.id && blockId2 <= ZythiumDelayer1DelayUpBlock.id ||
                        blockId2 >= ZythiumDelayer2DelayRightBlock.id && blockId2 <= ZythiumDelayer2DelayUpBlock.id ||
                        blockId2 >= ZythiumDelayer4DelayRightBlock.id && blockId2 <= ZythiumDelayer4DelayUpBlock.id ||
                        blockId2 >= ZythiumDelayer8DelayRightBlock.id && blockId2 <= ZythiumDelayer8DelayUpBlock.id) {
                      logger.debug("(DEBUG1)")
                      updatex += x2
                      updatey += y2
                      DDELAY.get(blockId2).foreach(updatet.+=)
                      updatel += l
                    } else {
                      power(l.num)(y2)(x2) = power(l.num)(y)(x) - conducts(blockId).toFloat
                      if (conducts(blockId2) >= 0 && wcnct(y2)(x2)) {
                        if (l === BackgroundLayer) {
                          if (receives(blocks(PrimaryLayer.num)(y2)(x2).id)) {
                            power(PrimaryLayer.num)(y2)(x2) = power(BackgroundLayer.num)(y2)(x2) - conducts(
                              blocks(BackgroundLayer.num)(y2)(x2).id).toFloat
                          }
                          if (receives(blocks(ForegroundLayer.num)(y2)(x2).id)) {
                            power(ForegroundLayer.num)(y2)(x2) = power(BackgroundLayer.num)(y2)(x2) - conducts(
                              blocks(BackgroundLayer.num)(y2)(x2).id).toFloat
                          }
                        }
                        if (l === PrimaryLayer) {
                          if (receives(blocks(BackgroundLayer.num)(y2)(x2).id)) {
                            power(BackgroundLayer.num)(y2)(x2) = power(PrimaryLayer.num)(y2)(x2) - conducts(
                              blocks(PrimaryLayer.num)(y2)(x2).id).toFloat
                          }
                          if (receives(blocks(ForegroundLayer.num)(y2)(x2).id)) {
                            power(ForegroundLayer.num)(y2)(x2) = power(PrimaryLayer.num)(y2)(x2) - conducts(
                              blocks(PrimaryLayer.num)(y2)(x2).id).toFloat
                          }
                        }
                        if (l === ForegroundLayer) {
                          if (receives(blocks(BackgroundLayer.num)(y2)(x2).id)) {
                            power(BackgroundLayer.num)(y2)(x2) = power(ForegroundLayer.num)(y2)(x2) - conducts(
                              blocks(ForegroundLayer.num)(y2)(x2).id).toFloat
                          }
                          if (receives(blocks(PrimaryLayer.num)(y2)(x2).id)) {
                            power(PrimaryLayer.num)(y2)(x2) = power(ForegroundLayer.num)(y2)(x2) - conducts(
                              blocks(ForegroundLayer.num)(y2)(x2).id).toFloat
                          }
                        }
                      }
                    }
                    if (!(blockId2 >= ZythiumInverterRightBlock.id && blockId2 <= ZythiumInverterUpBlock.id)) {
                      addTileToPQueue(x2, y2)
                    }
                  }
                  if (power(l.num)(y)(x) - conducts(blockId) > 0 && blockId2 >= ZythiumInverterRightBlock.id && blockId2 <= ZythiumInverterUpBlock.id) {
                    removeBlockPower(x2, y2, l)
                    blocks(l.num)(y2)(x2) = Block.withId(blockId2 + 4)
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
          logger.debug(s"(resolvePowerMatrix) $x $y $l ${blocks(l)(y)(x)} ${power(l)(y)(x)}")
          if (power(l)(y)(x) > 0) {
            if (blocks(l)(y)(x) === ZythiumLampBlock) {
              blocks(l)(y)(x) = ZythiumLampOnBlock
              addBlockLighting(x, y)
              rdrawn(y)(x) = false
            }
            if (blocks(l)(y)(x).id >= ZythiumAmplifierRightBlock.id && blocks(l)(y)(x).id <= ZythiumAmplifierUpBlock.id) {
              logger.debug(s"Processed amplifier at $x $y")
              blocks(l)(y)(x) = Block.withId(blocks(l)(y)(x).id + 4)
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
        if (blocks(l)(y)(x).id >= ZythiumWireBlock.id && blocks(l)(y)(x).id <= ZythiumWire5PowerBlock.id && power(l)(
              y)(x).toByte =/= pzqn(l)(y)(x)) {
          removeBlockLighting(x, y, BackgroundLayer)
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
      val screenGraphics: Graphics2D = screenTemp.createGraphics()
      screenGraphics.setColor(backgroundColor)
      screenGraphics.fillRect(0, 0, getWidth, getHeight)

      import GameStateRendering.Implicits._
      val renderer = state match {
        case InGame          => GameStateRendering.renderer[InGame.type]
        case LoadingGraphics => GameStateRendering.renderer[LoadingGraphics.type]
        case GeneratingWorld => GameStateRendering.renderer[GeneratingWorld.type]
        case LoadingWorld    => GameStateRendering.renderer[LoadingWorld.type]
        case NewWorld        => GameStateRendering.renderer[NewWorld.type]
        case SelectWorld     => GameStateRendering.renderer[SelectWorld.type]
        case TitleScreen     => GameStateRendering.renderer[TitleScreen.type]
      }

      renderer.render(screenGraphics, this)

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
      val fileOut            = new FileOutputStream("worlds/" + currentWorld.getOrElse("") + ".dat")
      val out                = new ObjectOutputStream(fileOut)
      out.writeObject(wc)
      out.close()
      fileOut.close()
    } catch {
      case e: IOException => e.printStackTrace()
    }
  }

  def emptyWorldContainer(wc: WorldContainer): Unit = {
    Array.copy(wc.blocks, 0, blocks, 0, wc.blocks.length)
    Array.copy(wc.blockds, 0, blockds, 0, wc.blockds.length)
    Array.copy(wc.blockdns, 0, blockdns, 0, wc.blockdns.length)
    Array.copy(wc.blockbgs, 0, blockbgs, 0, wc.blockbgs.length)
    Array.copy(wc.blockts, 0, blockts, 0, wc.blockts.length)
    Array.copy(wc.lights, 0, lights, 0, wc.lights.length)
    Array.copy(wc.power, 0, power, 0, wc.power.length)
    resetDrawn()
    player = wc.player
    inventory = wc.inventory
    cic = wc.cic

    entities.clear()
    entities ++= wc.entities

    cloudsx.clear()
    cloudsx ++= wc.cloudsx

    cloudsy.clear()
    cloudsy ++= wc.cloudsy

    cloudsv.clear()
    cloudsv ++= wc.cloudsv

    cloudsn.clear()
    cloudsn ++= wc.cloudsn

    machinesx.clear()
    machinesx ++= wc.machinesx

    machinesy.clear()
    machinesy ++= wc.machinesy

    Array.copy(wc.lsources, 0, lsources, 0, wc.lsources.length)

    lqx.clear()
    lqx ++= wc.lqx

    lqy.clear()
    lqy ++= wc.lqy

    Array.copy(wc.lqd, 0, lqd, 0, wc.lqd.length)

    rgnc1 = wc.rgnc1
    rgnc2 = wc.rgnc2
    layer = wc.layer
    mx = wc.mx
    my = wc.my
    icx = wc.icx
    icy = wc.icy
    mining = wc.mining
    immune = wc.immune
    moveItem = wc.moveItem
    moveNum = wc.moveNum
    moveItemTemp = wc.moveItemTemp
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
    Array.copy(wc.kworlds, 0, kworlds, 0, wc.kworlds.length)
    ic = wc.ic
    Array.copy(wc.icmatrix, 0, icmatrix, 0, wc.icmatrix.length)
    version = wc.version
    player.reloadImage()
    inventory.reloadImage()

    cic = cic.orElse(Some(new ItemCollection(Crafting)))
    cic.foreach(inventory.renderCollection)

    ic.foreach(inventory.renderCollection)
    entities.foreach { entity: Entity =>
      entity.reloadImage()
    }
    Array.copy(Array.fill(2, 2)(None), 0, worlds, 0, worlds.length)
    Array.copy(Array.fill(2, 2)(None), 0, fworlds, 0, fworlds.length)
  }

  def resetDrawn(): Unit = {
    Array.copy(Array.fill(theSize, theSize)(false), 0, drawn, 0, theSize)
    Array.copy(Array.fill(theSize, theSize)(false), 0, ldrawn, 0, theSize)
    Array.copy(Array.fill(theSize, theSize)(false), 0, rdrawn, 0, theSize)
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
      mx,
      my,
      icx,
      icy,
      mining,
      immune,
      moveItem,
      moveNum,
      moveItemTemp,
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

  def loadBlock(block: Block,
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
      val left: Boolean = blocks(lyr)(y)(x - 1) === AirBlock || !blockcds(blocks(lyr)(y)(x - 1).id)
      // && (blocks(lyr)(y-1)(x) =/= dn && blocks(lyr)(y+1)(x) =/= dn) && (blocks(lyr)(y-1)(x-1) =/= dn && blocks(lyr)(y+1)(x-1) =/= dn)
      val right: Boolean = blocks(lyr)(y)(x + 1) === AirBlock || !blockcds(blocks(lyr)(y)(x + 1).id)
      // && (blocks(lyr)(y-1)(x) =/= dn && blocks(lyr)(y+1)(x) =/= dn) && (blocks(lyr)(y-1)(x+1) =/= dn && blocks(lyr)(y+1)(x+1) =/= dn)
      val up: Boolean = blocks(lyr)(y - 1)(x) === AirBlock || !blockcds(blocks(lyr)(y - 1)(x).id)
      // && (blocks(lyr)(y)(x-1) =/= dn && blocks(lyr)(y)(x+1) =/= dn) && (blocks(lyr)(y-1)(x-1) =/= dn && blocks(lyr)(y-1)(x+1) =/= dn)
      val down: Boolean = blocks(lyr)(y + 1)(x) === AirBlock || !blockcds(blocks(lyr)(y + 1)(x).id)
      // && (blocks(lyr)(y)(x-1) =/= dn && blocks(lyr)(y)(x+1) =/= dn) && (blocks(lyr)(y+1)(x-1) =/= dn && blocks(lyr)(y+1)(x+1) =/= dn)
      val upleft: Boolean = blocks(lyr)(y - 1)(x - 1) === AirBlock || !blockcds(blocks(lyr)(y - 1)(x - 1).id)
      // && (blocks(lyr)(y-1)(x) =/= dn && blocks(lyr)(y)(x-1) =/= dn && blocks(lyr)(y-1)(x-1) =/= dn && blocks(lyr)(y-2)(x) =/= dn && blocks(lyr)(y)(x-2) =/= dn)
      val upright: Boolean = blocks(lyr)(y - 1)(x + 1) === AirBlock || !blockcds(blocks(lyr)(y - 1)(x + 1).id)
      // && (blocks(lyr)(y-1)(x) =/= dn && blocks(lyr)(y)(x+1) =/= dn && blocks(lyr)(y-1)(x+1) =/= dn && blocks(lyr)(y-2)(x) =/= dn && blocks(lyr)(y)(x+2) =/= dn)
      val downleft: Boolean = blocks(lyr)(y + 1)(x - 1) === AirBlock || !blockcds(blocks(lyr)(y + 1)(x - 1).id)
      // && (blocks(lyr)(y+1)(x) =/= dn && blocks(lyr)(y)(x-1) =/= dn && blocks(lyr)(y+1)(x-1) =/= dn && blocks(lyr)(y+2)(x) =/= dn && blocks(lyr)(y)(x-2) =/= dn)
      val downright: Boolean = blocks(lyr)(y + 1)(x + 1) === AirBlock || !blockcds(blocks(lyr)(y + 1)(x + 1).id)
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
                val n: Int = (255 - 32 * sqrt(pow((dx - dx2).toDouble, 2) + pow((dy - dy2).toDouble, 2))).toInt
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
    import KeyEvent._
    if (keyCode === VK_LEFT || keyCode === VK_A) {
      userInput.setLeftKeyPressed(true)
    }
    if (keyCode === VK_RIGHT || keyCode === VK_D) {
      userInput.setRightKeyPressed(true)
    }
    if (keyCode === VK_UP || keyCode === VK_W) {
      userInput.setUpKeyPressed(true)
    }
    if (keyCode === VK_DOWN || keyCode === VK_S) {
      userInput.setDownKeyPressed(true)
    }
    if (keyCode === VK_SHIFT) {
      userInput.setShiftKeyPressed(true)
    }
    if (state === InGame) {
      if (keyCode === VK_ESCAPE) {
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
            icmatrix(iclayer.num)(icy)(icx) =
              Some(new ItemCollection(icTemp.icType, icTemp.ids, icTemp.nums, icTemp.durs))
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
            icmatrix(iclayer.num)(icy)(icx) = icmatrix(iclayer.num)(icy)(icx).map { icMatrixTemp =>
              icMatrixTemp.copy(
                fuelPower = icTemp.fuelPower,
                smeltPower = icTemp.smeltPower,
                furnaceOn = icTemp.furnaceOn
              )
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
        if (keyCode === VK_1) {
          inventory.select(1)
        }
        if (keyCode === VK_2) {
          inventory.select(2)
        }
        if (keyCode === VK_3) {
          inventory.select(3)
        }
        if (keyCode === VK_4) {
          inventory.select(4)
        }
        if (keyCode === VK_5) {
          inventory.select(5)
        }
        if (keyCode === VK_6) {
          inventory.select(6)
        }
        if (keyCode === VK_7) {
          inventory.select(7)
        }
        if (keyCode === VK_8) {
          inventory.select(8)
        }
        if (keyCode === VK_9) {
          inventory.select(9)
        }
        if (keyCode === VK_0) {
          inventory.select(0)
        }
      }
    }

    val shiftPressed = userInput.isShiftKeyPressed

    val c: Char = keyCode match {
      case VK_Q if shiftPressed => 'Q'
      case VK_Q                 => 'q'

      case VK_W if shiftPressed => 'W'
      case VK_W                 => 'w'

      case VK_E if shiftPressed => 'E'
      case VK_E                 => 'e'

      case VK_R if shiftPressed => 'R'
      case VK_R                 => 'r'

      case VK_T if shiftPressed => 'T'
      case VK_T                 => 't'

      case VK_Y if shiftPressed => 'Y'
      case VK_Y                 => 'y'

      case VK_U if shiftPressed => 'U'
      case VK_U                 => 'u'

      case VK_I if shiftPressed => 'I'
      case VK_I                 => 'i'

      case VK_O if shiftPressed => 'O'
      case VK_O                 => 'o'

      case VK_P if shiftPressed => 'P'
      case VK_P                 => 'p'

      case VK_A if shiftPressed => 'A'
      case VK_A                 => 'a'

      case VK_S if shiftPressed => 'S'
      case VK_S                 => 's'

      case VK_D if shiftPressed => 'D'
      case VK_D                 => 'd'

      case VK_F if shiftPressed => 'F'
      case VK_F                 => 'f'

      case VK_G if shiftPressed => 'G'
      case VK_G                 => 'g'

      case VK_H if shiftPressed => 'H'
      case VK_H                 => 'h'

      case VK_J if shiftPressed => 'J'
      case VK_J                 => 'j'

      case VK_K if shiftPressed => 'K'
      case VK_K                 => 'k'

      case VK_L if shiftPressed => 'L'
      case VK_L                 => 'l'

      case VK_Z if shiftPressed => 'Z'
      case VK_Z                 => 'z'

      case VK_X if shiftPressed => 'X'
      case VK_X                 => 'x'

      case VK_C if shiftPressed => 'C'
      case VK_C                 => 'c'

      case VK_V if shiftPressed => 'V'
      case VK_V                 => 'v'

      case VK_B if shiftPressed => 'B'
      case VK_B                 => 'b'

      case VK_N if shiftPressed => 'N'
      case VK_N                 => 'n'

      case VK_M if shiftPressed => 'M'
      case VK_M                 => 'm'

      case VK_1 if shiftPressed => '!'
      case VK_1                 => '1'

      case VK_2 if shiftPressed => '@'
      case VK_2                 => '2'

      case VK_3 if shiftPressed => '#'
      case VK_3                 => '3'

      case VK_4 if shiftPressed => '$'
      case VK_4                 => '4'

      case VK_5 if shiftPressed => '%'
      case VK_5                 => '5'

      case VK_6 if shiftPressed => '^'
      case VK_6                 => '6'

      case VK_7 if shiftPressed => '&'
      case VK_7                 => '7'

      case VK_8 if shiftPressed => '*'
      case VK_8                 => '8'

      case VK_9 if shiftPressed => '('
      case VK_9                 => '9'

      case VK_0 if shiftPressed => ')'
      case VK_0                 => '0'

      case VK_SPACE => ' '

      case VK_BACK_QUOTE if shiftPressed => '~'
      case VK_BACK_QUOTE                 => '`'

      case VK_MINUS if shiftPressed => '_'
      case VK_MINUS                 => '-'

      case VK_EQUALS if shiftPressed => '+'
      case VK_EQUALS                 => '='

      case VK_OPEN_BRACKET if shiftPressed => '{'
      case VK_OPEN_BRACKET                 => '['

      case VK_CLOSE_BRACKET if shiftPressed => '}'
      case VK_CLOSE_BRACKET                 => ']'

      case VK_BACK_SLASH if shiftPressed => '|'
      case VK_BACK_SLASH                 => '\\'

      case VK_SEMICOLON if shiftPressed => ':'
      case VK_SEMICOLON                 => ';'

      case VK_QUOTE if shiftPressed => '"'
      case VK_QUOTE                 => '\''

      case VK_COMMA if shiftPressed => '<'
      case VK_COMMA                 => ','

      case VK_PERIOD if shiftPressed => '>'
      case VK_PERIOD                 => '.'

      case VK_SLASH if shiftPressed => '?'
      case VK_SLASH                 => '/'

      case _ => 0: Char
    }

    if (state === NewWorld && !newWorldFocus) {
      if (c =/= 0) {
        newWorldName.typeKey(c)
        repaint()
      }

      if (keyCode === VK_BACK_SPACE) {
        newWorldName.deleteKey()
        repaint()
      }
    }

    if (keyCode === VK_EQUALS && layer.num < 2) { // TODO: could create increment and decrement or circular buffer
      layer = Layer.withNum(layer.num + 1)
    }
    if (keyCode === VK_MINUS && layer.num > 0) {
      layer = Layer.withNum(layer.num - 1)
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
