package org.terraframe

import java.awt.{ Color, Font, Graphics2D }
import java.awt.image._
import java.io.Serializable

import Images.loadImage
import TypeSafeComparisons._

import scala.collection.mutable
import org.terraframe.{ MathHelper => mh }

object Inventory {

  def f(x: Int): Int = {
    if (x === 9) {
      0
    } else {
      x + 1
    }
  }
}

//TODO: can these be collapsed
case class CicRecipe(ingredients: List[UiItem], result: UiItem, count: Short)          // TODO ingredients or less enforcement 2 x 2 matrix
case class ShapelessCicRecipe(ingredients: List[UiItem], result: UiItem, count: Short) // TODO ingredients or less  enforcement 2 x 2 matrix
case class WorkbenchRecipe(ingredients: List[UiItem], result: UiItem, count: Short)    // TODO ingredient or less enforcement 3 x 3 matrix
case class ShapelessRecipe(ingredients: List[UiItem], result: UiItem, count: Short)    // TODO ingredient or less enforcement 3 x 3 matrix

class Inventory extends Serializable {

  import Inventory._
  import GraphicsHelper._

  var n, px, py, selection, width, height: Int = _
  var fpx, fpy: Double                         = _

  @transient var image, box, box_selected: BufferedImage = _
  val font                                               = new Font("Chalkboard", Font.PLAIN, 12)

  @transient var g2: Graphics2D = _

  val ids: Array[UiItem] = Array.fill(40)(EmptyUiItem)
  val nums: Array[Short] = Array.fill(40)(0)
  val durs: Array[Short] = Array.fill(40)(0)

  var trolx: Int = 37
  var troly: Int = 17

  var CX, CY: Int = _

  var valid: Boolean = false

  //val RECIPES: Map[String, Array2D[UiItem]] = createRecipies
  val cicRecipes: List[CicRecipe]                   = createCicRecipes
  val shapelessCicRecipes: List[ShapelessCicRecipe] = createShapelessCicRecipes
  val workbenchRecipes: List[WorkbenchRecipe]       = createWorkbenchRecipes
  val shapelessRecipes: List[ShapelessRecipe]       = createShapelessRecipes

  //Begin Constructor

  selection = 0
  image = new BufferedImage(466, 190, BufferedImage.TYPE_INT_ARGB)
  box = loadImage("interface/inventory.png").get
  box_selected = loadImage("interface/inventory_selected.png").get
  g2 = image.createGraphics()
  (0 until 10).foreach { x =>
    (0 until 4).foreach { y =>
      if (x === 0 && y === 0) {
        drawImage(g2, box_selected, x * 46 + 6, y * 46 + 6, x * 46 + 46, y * 46 + 46, 0, 0, 40, 40)
        if (y === 0) {
          g2.setFont(font)
          g2.setColor(Color.BLACK)
          g2.drawString(f(x) + " ", x * 46 + trolx, y * 46 + troly)
        }
      } else {
        drawImage(g2, box, x * 46 + 6, y * 46 + 6, x * 46 + 46, y * 46 + 46, 0, 0, 40, 40)
        if (y === 0) {
          g2.setFont(font)
          g2.setColor(Color.BLACK)
          g2.drawString(f(x) + " ", x * 46 + trolx, y * 46 + troly)
        }
      }
    }
  }

  def createCicRecipes: List[CicRecipe] =
    List(
      CicRecipe(WoodUiItem :: WoodUiItem :: WoodUiItem :: WoodUiItem :: Nil, WorkbenchUiItem, 1), // Workbench
      CicRecipe(BarkUiItem :: BarkUiItem :: BarkUiItem :: BarkUiItem :: Nil, WoodUiItem, 1), // Bark -> Wood
      CicRecipe(StoneUiItem :: StoneUiItem :: StoneUiItem :: StoneUiItem :: Nil, CobblestoneUiItem, 4), // Cobblestone
      CicRecipe(
        ChiseledStoneUiItem :: ChiseledStoneUiItem :: ChiseledStoneUiItem :: ChiseledStoneUiItem :: Nil,
        ChiseledCobblestoneUiItem,
        4), // Chiseled Cobblestone
      CicRecipe(
        ChiseledCobblestoneUiItem :: ChiseledCobblestoneUiItem :: ChiseledCobblestoneUiItem :: ChiseledCobblestoneUiItem :: Nil,
        StoneBricksUiItem,
        4), // Stone Bricks
      CicRecipe(StoneUiItem :: EmptyUiItem :: EmptyUiItem :: StoneUiItem :: Nil, StoneLighterUiItem, 1), // Stone Lighter
      CicRecipe(EmptyUiItem :: StoneUiItem :: StoneUiItem :: EmptyUiItem :: Nil, StoneLighterUiItem, 1),
      CicRecipe(WoodUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil, WoodenTorchUiItem, 4), // Wooden Torch
      CicRecipe(EmptyUiItem :: WoodUiItem :: EmptyUiItem :: WoodUiItem :: Nil, WoodenTorchUiItem, 4),
      CicRecipe(CoalUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil, CoalTorchUiItem, 4), // Coal Torch
      CicRecipe(EmptyUiItem :: CoalUiItem :: EmptyUiItem :: WoodUiItem :: Nil, CoalTorchUiItem, 4),
      CicRecipe(LumenstoneUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil, LumenstoneTorchUiItem, 4), // Lumenstone Torch
      CicRecipe(EmptyUiItem :: LumenstoneUiItem :: EmptyUiItem :: WoodUiItem :: Nil, LumenstoneTorchUiItem, 4),
      CicRecipe(ZythiumOreUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil, ZythiumTorchUiItem, 4), // Zythium Torch
      CicRecipe(EmptyUiItem :: ZythiumOreUiItem :: EmptyUiItem :: WoodUiItem :: Nil, ZythiumTorchUiItem, 4),
      CicRecipe(WoodUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil, WoodenPressurePlateUiItem, 1), // Wooden Pressure Plate
      CicRecipe(EmptyUiItem :: EmptyUiItem :: WoodUiItem :: WoodUiItem :: Nil, WoodenPressurePlateUiItem, 1),
      CicRecipe(StoneUiItem :: StoneUiItem :: EmptyUiItem :: EmptyUiItem :: Nil, StonePressurePlateUiItem, 1), // Stone Pressure Plate
      CicRecipe(EmptyUiItem :: EmptyUiItem :: StoneUiItem :: StoneUiItem :: Nil, StonePressurePlateUiItem, 1)
    )

  def createShapelessCicRecipes: List[ShapelessCicRecipe] =
    List( // TODO: don't really need a separate create function, just do lazy val
      ShapelessCicRecipe(WoodUiItem :: VarnishUiItem :: EmptyUiItem :: EmptyUiItem :: Nil, VarnishedWoodUiItem, 1),
      ShapelessCicRecipe(ChiseledStoneUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil, ButtonUiItem, 1)
    )

  def createWorkbenchRecipes: List[WorkbenchRecipe] = List(
    WorkbenchRecipe(
      WoodUiItem :: WoodUiItem :: WoodUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      WoodenPickUiItem,
      1), // Wooden Pick
    WorkbenchRecipe(
      StoneUiItem :: StoneUiItem :: StoneUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      StonePickUiItem,
      1), // Stone Pick
    WorkbenchRecipe(
      CopperIngotUiItem :: CopperIngotUiItem :: CopperIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      CopperPickUiItem,
      1
    ), // Copper Pick
    WorkbenchRecipe(
      IronIngotUiItem :: IronIngotUiItem :: IronIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      IronPickUiItem,
      1), // Iron Pick
    WorkbenchRecipe(
      SilverIngotUiItem :: SilverIngotUiItem :: SilverIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      SilverPickUiItem,
      1
    ), // Silver Pick
    WorkbenchRecipe(
      GoldIngotUiItem :: GoldIngotUiItem :: GoldIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      GoldPickUiItem,
      1), // Gold Pick
    WorkbenchRecipe(
      ZincIngotUiItem :: ZincIngotUiItem :: ZincIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      ZincPickUiItem,
      1), // Zinc Pick
    WorkbenchRecipe(
      RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      RhymestonePickUiItem,
      1
    ), // Rhymestone Pick
    WorkbenchRecipe(
      ObduriteIngotUiItem :: ObduriteIngotUiItem :: ObduriteIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      ObduritePickUiItem,
      1
    ), // Obdurite Pick
    WorkbenchRecipe(
      MagnetiteIngotUiItem :: MagnetiteIngotUiItem :: MagnetiteIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      MagnetitePickUiItem,
      1
    ), // Magnetite Pick
    WorkbenchRecipe(
      IrradiumIngotUiItem :: IrradiumIngotUiItem :: IrradiumIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      IrradiumPickUiItem,
      1
    ), // Irradium Pick
    WorkbenchRecipe(
      WoodUiItem :: WoodUiItem :: EmptyUiItem :: WoodUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      WoodenAxeUiItem,
      1), // Wooden Axe
    WorkbenchRecipe(
      EmptyUiItem :: WoodUiItem :: WoodUiItem :: EmptyUiItem :: WoodUiItem :: WoodUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      WoodenAxeUiItem,
      1),
    WorkbenchRecipe(
      WoodUiItem :: WoodUiItem :: EmptyUiItem :: WoodUiItem :: WoodUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      WoodenAxeUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: WoodUiItem :: WoodUiItem :: EmptyUiItem :: WoodUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: Nil,
      WoodenAxeUiItem,
      1),
    WorkbenchRecipe(
      StoneUiItem :: StoneUiItem :: EmptyUiItem :: StoneUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      StoneAxeUiItem,
      1), // Stone Axe
    WorkbenchRecipe(
      EmptyUiItem :: StoneUiItem :: StoneUiItem :: EmptyUiItem :: WoodUiItem :: StoneUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      StoneAxeUiItem,
      1),
    WorkbenchRecipe(
      StoneUiItem :: StoneUiItem :: EmptyUiItem :: WoodUiItem :: StoneUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      StoneAxeUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: StoneUiItem :: StoneUiItem :: EmptyUiItem :: StoneUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: Nil,
      StoneAxeUiItem,
      1),
    WorkbenchRecipe(
      CopperIngotUiItem :: CopperIngotUiItem :: EmptyUiItem :: CopperIngotUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      CopperAxeUiItem,
      1
    ), // Copper Axe
    WorkbenchRecipe(
      EmptyUiItem :: CopperIngotUiItem :: CopperIngotUiItem :: EmptyUiItem :: WoodUiItem :: CopperIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      CopperAxeUiItem,
      1
    ),
    WorkbenchRecipe(
      CopperIngotUiItem :: CopperIngotUiItem :: EmptyUiItem :: WoodUiItem :: CopperIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      CopperAxeUiItem,
      1
    ),
    WorkbenchRecipe(
      EmptyUiItem :: CopperIngotUiItem :: CopperIngotUiItem :: EmptyUiItem :: CopperIngotUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: Nil,
      CopperAxeUiItem,
      1
    ),
    WorkbenchRecipe(
      IronIngotUiItem :: IronIngotUiItem :: EmptyUiItem :: IronIngotUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      CopperAxeUiItem,
      1), // Iron Axe
    WorkbenchRecipe(
      EmptyUiItem :: IronIngotUiItem :: IronIngotUiItem :: EmptyUiItem :: WoodUiItem :: IronIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      CopperAxeUiItem,
      1),
    WorkbenchRecipe(
      IronIngotUiItem :: IronIngotUiItem :: EmptyUiItem :: WoodUiItem :: IronIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      CopperAxeUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: IronIngotUiItem :: IronIngotUiItem :: EmptyUiItem :: IronIngotUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: Nil,
      CopperAxeUiItem,
      1),
    WorkbenchRecipe(
      SilverIngotUiItem :: SilverIngotUiItem :: EmptyUiItem :: SilverIngotUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      CopperAxeUiItem,
      1
    ), // Silver Axe
    WorkbenchRecipe(
      EmptyUiItem :: SilverIngotUiItem :: SilverIngotUiItem :: EmptyUiItem :: WoodUiItem :: SilverIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      CopperAxeUiItem,
      1
    ),
    WorkbenchRecipe(
      SilverIngotUiItem :: SilverIngotUiItem :: EmptyUiItem :: WoodUiItem :: SilverIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      CopperAxeUiItem,
      1
    ),
    WorkbenchRecipe(
      EmptyUiItem :: SilverIngotUiItem :: SilverIngotUiItem :: EmptyUiItem :: SilverIngotUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: Nil,
      CopperAxeUiItem,
      1
    ),
    WorkbenchRecipe(
      GoldIngotUiItem :: GoldIngotUiItem :: EmptyUiItem :: GoldIngotUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      CopperAxeUiItem,
      1), // Gold Axe
    WorkbenchRecipe(
      EmptyUiItem :: GoldIngotUiItem :: GoldIngotUiItem :: EmptyUiItem :: WoodUiItem :: GoldIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      CopperAxeUiItem,
      1),
    WorkbenchRecipe(
      GoldIngotUiItem :: GoldIngotUiItem :: EmptyUiItem :: WoodUiItem :: GoldIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      CopperAxeUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: GoldIngotUiItem :: GoldIngotUiItem :: EmptyUiItem :: GoldIngotUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: Nil,
      CopperAxeUiItem,
      1),
    WorkbenchRecipe(
      ZincIngotUiItem :: ZincIngotUiItem :: EmptyUiItem :: ZincIngotUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      ZincAxeUiItem,
      1), // Zinc Axe
    WorkbenchRecipe(
      EmptyUiItem :: ZincIngotUiItem :: ZincIngotUiItem :: EmptyUiItem :: WoodUiItem :: ZincIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      ZincAxeUiItem,
      1),
    WorkbenchRecipe(
      ZincIngotUiItem :: ZincIngotUiItem :: EmptyUiItem :: WoodUiItem :: ZincIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      ZincAxeUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: ZincIngotUiItem :: ZincIngotUiItem :: EmptyUiItem :: ZincIngotUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: Nil,
      ZincAxeUiItem,
      1),
    WorkbenchRecipe(
      RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: EmptyUiItem :: RhymestoneIngotUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      RhymestoneAxeUiItem,
      1
    ), // Rhymestone Axe
    WorkbenchRecipe(
      EmptyUiItem :: RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: EmptyUiItem :: WoodUiItem :: RhymestoneIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      RhymestoneAxeUiItem,
      1
    ),
    WorkbenchRecipe(
      RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: EmptyUiItem :: WoodUiItem :: RhymestoneIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      RhymestoneAxeUiItem,
      1
    ),
    WorkbenchRecipe(
      EmptyUiItem :: RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: EmptyUiItem :: RhymestoneIngotUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: Nil,
      RhymestoneAxeUiItem,
      1
    ),
    WorkbenchRecipe(
      ObduriteIngotUiItem :: ObduriteIngotUiItem :: EmptyUiItem :: ObduriteIngotUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      ObduriteAxeUiItem,
      1
    ), // Obdurite Axe
    WorkbenchRecipe(
      EmptyUiItem :: ObduriteIngotUiItem :: ObduriteIngotUiItem :: EmptyUiItem :: WoodUiItem :: ObduriteIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      ObduriteAxeUiItem,
      1
    ),
    WorkbenchRecipe(
      ObduriteIngotUiItem :: ObduriteIngotUiItem :: EmptyUiItem :: WoodUiItem :: ObduriteIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      ObduriteAxeUiItem,
      1
    ),
    WorkbenchRecipe(
      EmptyUiItem :: ObduriteIngotUiItem :: ObduriteIngotUiItem :: EmptyUiItem :: ObduriteIngotUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: Nil,
      ObduriteAxeUiItem,
      1
    ),
    WorkbenchRecipe(
      MagnetiteIngotUiItem :: MagnetiteIngotUiItem :: EmptyUiItem :: MagnetiteIngotUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      MagnetiteAxeUiItem,
      1
    ), // Magnetite Axe
    WorkbenchRecipe(
      EmptyUiItem :: MagnetiteIngotUiItem :: MagnetiteIngotUiItem :: EmptyUiItem :: WoodUiItem :: MagnetiteIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      MagnetiteAxeUiItem,
      1
    ),
    WorkbenchRecipe(
      MagnetiteIngotUiItem :: MagnetiteIngotUiItem :: EmptyUiItem :: WoodUiItem :: MagnetiteIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      MagnetiteAxeUiItem,
      1
    ),
    WorkbenchRecipe(
      EmptyUiItem :: MagnetiteIngotUiItem :: MagnetiteIngotUiItem :: EmptyUiItem :: MagnetiteIngotUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: Nil,
      MagnetiteAxeUiItem,
      1
    ),
    WorkbenchRecipe(
      IrradiumIngotUiItem :: IrradiumIngotUiItem :: EmptyUiItem :: IrradiumIngotUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      MagnetitePickUiItem,
      1
    ), // Irradium Axe
    WorkbenchRecipe(
      EmptyUiItem :: IrradiumIngotUiItem :: IrradiumIngotUiItem :: EmptyUiItem :: WoodUiItem :: IrradiumIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      MagnetitePickUiItem,
      1
    ),
    WorkbenchRecipe(
      IrradiumIngotUiItem :: IrradiumIngotUiItem :: EmptyUiItem :: WoodUiItem :: IrradiumIngotUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      MagnetitePickUiItem,
      1
    ),
    WorkbenchRecipe(
      EmptyUiItem :: IrradiumIngotUiItem :: IrradiumIngotUiItem :: EmptyUiItem :: IrradiumIngotUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: Nil,
      MagnetitePickUiItem,
      1
    ),
    WorkbenchRecipe(
      WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      WoodenSwordUiItem,
      1), // Wooden Sword
    WorkbenchRecipe(
      EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      WoodenSwordUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: Nil,
      WoodenSwordUiItem,
      1),
    WorkbenchRecipe(
      StoneUiItem :: EmptyUiItem :: EmptyUiItem :: StoneUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      StoneSwordUiItem,
      1), // Stone Sword
    WorkbenchRecipe(
      EmptyUiItem :: StoneUiItem :: EmptyUiItem :: EmptyUiItem :: StoneUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      StoneSwordUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: StoneUiItem :: EmptyUiItem :: EmptyUiItem :: StoneUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: Nil,
      StoneSwordUiItem,
      1),
    WorkbenchRecipe(
      CopperIngotUiItem :: EmptyUiItem :: EmptyUiItem :: CopperIngotUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      CopperSwordUiItem,
      1
    ), // Copper Sword
    WorkbenchRecipe(
      EmptyUiItem :: CopperIngotUiItem :: EmptyUiItem :: EmptyUiItem :: CopperIngotUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      CopperSwordUiItem,
      1
    ),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: CopperIngotUiItem :: EmptyUiItem :: EmptyUiItem :: CopperIngotUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: Nil,
      CopperSwordUiItem,
      1
    ),
    WorkbenchRecipe(
      IronIngotUiItem :: EmptyUiItem :: EmptyUiItem :: IronIngotUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      IronSwordUiItem,
      1), // Iron Sword
    WorkbenchRecipe(
      EmptyUiItem :: IronIngotUiItem :: EmptyUiItem :: EmptyUiItem :: IronIngotUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      IronSwordUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: IronIngotUiItem :: EmptyUiItem :: EmptyUiItem :: IronIngotUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: Nil,
      IronSwordUiItem,
      1),
    WorkbenchRecipe(
      SilverIngotUiItem :: EmptyUiItem :: EmptyUiItem :: SilverIngotUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      SilverSwordUiItem,
      1
    ), // Silver Sword
    WorkbenchRecipe(
      EmptyUiItem :: SilverIngotUiItem :: EmptyUiItem :: EmptyUiItem :: SilverIngotUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      SilverSwordUiItem,
      1
    ),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: SilverIngotUiItem :: EmptyUiItem :: EmptyUiItem :: SilverIngotUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: Nil,
      SilverSwordUiItem,
      1
    ),
    WorkbenchRecipe(
      GoldIngotUiItem :: EmptyUiItem :: EmptyUiItem :: GoldIngotUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      GoldSwordUiItem,
      1), // Gold Sword
    WorkbenchRecipe(
      EmptyUiItem :: GoldIngotUiItem :: EmptyUiItem :: EmptyUiItem :: GoldIngotUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      GoldSwordUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: GoldIngotUiItem :: EmptyUiItem :: EmptyUiItem :: GoldIngotUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: Nil,
      GoldSwordUiItem,
      1),
    WorkbenchRecipe(
      ZincOreUiItem :: EmptyUiItem :: EmptyUiItem :: ZincOreUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      GoldSwordUiItem,
      1), // Zinc Sword
    WorkbenchRecipe(
      EmptyUiItem :: ZincOreUiItem :: EmptyUiItem :: EmptyUiItem :: ZincOreUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      GoldSwordUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: ZincOreUiItem :: EmptyUiItem :: EmptyUiItem :: ZincOreUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: Nil,
      GoldSwordUiItem,
      1),
    WorkbenchRecipe(
      RhymestoneOreUiItem :: EmptyUiItem :: EmptyUiItem :: RhymestoneOreUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      GoldSwordUiItem,
      1
    ), // Rhymestone Sword
    WorkbenchRecipe(
      EmptyUiItem :: RhymestoneOreUiItem :: EmptyUiItem :: EmptyUiItem :: RhymestoneOreUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      GoldSwordUiItem,
      1
    ),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: RhymestoneOreUiItem :: EmptyUiItem :: EmptyUiItem :: RhymestoneOreUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: Nil,
      GoldSwordUiItem,
      1
    ),
    WorkbenchRecipe(
      ObduriteOreUiItem :: EmptyUiItem :: EmptyUiItem :: ObduriteOreUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      GoldSwordUiItem,
      1), // Obdurite Sword
    WorkbenchRecipe(
      EmptyUiItem :: ObduriteOreUiItem :: EmptyUiItem :: EmptyUiItem :: ObduriteOreUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      GoldSwordUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: ObduriteOreUiItem :: EmptyUiItem :: EmptyUiItem :: ObduriteOreUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: Nil,
      GoldSwordUiItem,
      1),
    WorkbenchRecipe(
      MagnetiteIngotUiItem :: EmptyUiItem :: EmptyUiItem :: MagnetiteIngotUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      MagnetiteSwordUiItem,
      1
    ), // Magnetite Sword
    WorkbenchRecipe(
      EmptyUiItem :: MagnetiteIngotUiItem :: EmptyUiItem :: EmptyUiItem :: MagnetiteIngotUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      MagnetiteSwordUiItem,
      1
    ),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: MagnetiteIngotUiItem :: EmptyUiItem :: EmptyUiItem :: MagnetiteIngotUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: Nil,
      MagnetiteSwordUiItem,
      1
    ),
    WorkbenchRecipe(
      IrradiumIngotUiItem :: EmptyUiItem :: EmptyUiItem :: IrradiumIngotUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      IrradiumSwordUiItem,
      1
    ), // Irradium Sword
    WorkbenchRecipe(
      EmptyUiItem :: IrradiumIngotUiItem :: EmptyUiItem :: EmptyUiItem :: IrradiumIngotUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      IrradiumSwordUiItem,
      1
    ),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: IrradiumIngotUiItem :: EmptyUiItem :: EmptyUiItem :: IrradiumIngotUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: Nil,
      IrradiumSwordUiItem,
      1
    ),
    WorkbenchRecipe(
      AluminumIngotUiItem :: EmptyUiItem :: AluminumIngotUiItem :: EmptyUiItem :: AluminumIngotUiItem :: EmptyUiItem :: EmptyUiItem :: AluminumIngotUiItem :: EmptyUiItem :: Nil,
      WrenchUiItem,
      1
    ), // Wrench
    WorkbenchRecipe(
      WoodUiItem :: EmptyUiItem :: EmptyUiItem :: StoneUiItem :: EmptyUiItem :: EmptyUiItem :: ZythiumWireUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      LeverUiItem,
      1), // Lever
    WorkbenchRecipe(
      EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: StoneUiItem :: EmptyUiItem :: EmptyUiItem :: ZythiumWireUiItem :: EmptyUiItem :: Nil,
      LeverUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: StoneUiItem :: EmptyUiItem :: EmptyUiItem :: ZythiumWireUiItem :: Nil,
      LeverUiItem,
      1),
    WorkbenchRecipe(
      CopperIngotUiItem :: CopperIngotUiItem :: CopperIngotUiItem :: CopperIngotUiItem :: EmptyUiItem :: CopperIngotUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      CopperHelmetUiItem,
      1
    ), // Copper Helmet
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: CopperIngotUiItem :: CopperIngotUiItem :: CopperIngotUiItem :: CopperIngotUiItem :: EmptyUiItem :: CopperIngotUiItem :: Nil,
      CopperHelmetUiItem,
      1
    ),
    WorkbenchRecipe(
      CopperIngotUiItem :: EmptyUiItem :: CopperIngotUiItem :: CopperIngotUiItem :: CopperIngotUiItem :: CopperIngotUiItem :: CopperIngotUiItem :: CopperIngotUiItem :: CopperIngotUiItem :: Nil,
      CopperChestplateUiItem,
      1
    ), // Copper Chestplate
    WorkbenchRecipe(
      CopperIngotUiItem :: CopperIngotUiItem :: CopperIngotUiItem :: CopperIngotUiItem :: EmptyUiItem :: CopperIngotUiItem :: CopperIngotUiItem :: EmptyUiItem :: CopperIngotUiItem :: Nil,
      CopperLeggingsUiItem,
      1
    ), // Copper Leggings
    WorkbenchRecipe(
      CopperIngotUiItem :: EmptyUiItem :: CopperIngotUiItem :: CopperIngotUiItem :: EmptyUiItem :: CopperIngotUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      CopperGreavesUiItem,
      1
    ), // Copper Greaves
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: CopperIngotUiItem :: EmptyUiItem :: CopperIngotUiItem :: CopperIngotUiItem :: EmptyUiItem :: CopperIngotUiItem :: Nil,
      CopperGreavesUiItem,
      1
    ),
    WorkbenchRecipe(
      IronIngotUiItem :: IronIngotUiItem :: IronIngotUiItem :: IronIngotUiItem :: EmptyUiItem :: IronIngotUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      IronHelmetUiItem,
      1
    ), // Iron Helmet
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: IronIngotUiItem :: IronIngotUiItem :: IronIngotUiItem :: IronIngotUiItem :: EmptyUiItem :: IronIngotUiItem :: Nil,
      IronHelmetUiItem,
      1
    ),
    WorkbenchRecipe(
      IronIngotUiItem :: EmptyUiItem :: IronIngotUiItem :: IronIngotUiItem :: IronIngotUiItem :: IronIngotUiItem :: IronIngotUiItem :: IronIngotUiItem :: IronIngotUiItem :: Nil,
      IronChestplateUiItem,
      1
    ), // Iron Chestplate
    WorkbenchRecipe(
      IronIngotUiItem :: IronIngotUiItem :: IronIngotUiItem :: IronIngotUiItem :: EmptyUiItem :: IronIngotUiItem :: IronIngotUiItem :: EmptyUiItem :: IronIngotUiItem :: Nil,
      IronLeggingsUiItem,
      1
    ), // Iron Leggings
    WorkbenchRecipe(
      IronIngotUiItem :: EmptyUiItem :: IronIngotUiItem :: IronIngotUiItem :: EmptyUiItem :: IronIngotUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      IronGreavesUiItem,
      1
    ), // Iron Greaves
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: IronIngotUiItem :: EmptyUiItem :: IronIngotUiItem :: IronIngotUiItem :: EmptyUiItem :: IronIngotUiItem :: Nil,
      IronGreavesUiItem,
      1
    ),
    WorkbenchRecipe(
      SilverIngotUiItem :: SilverIngotUiItem :: SilverIngotUiItem :: SilverIngotUiItem :: EmptyUiItem :: SilverIngotUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      SilverHelmetUiItem,
      1
    ), // Silver Helmet
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: SilverIngotUiItem :: SilverIngotUiItem :: SilverIngotUiItem :: SilverIngotUiItem :: EmptyUiItem :: SilverIngotUiItem :: Nil,
      SilverHelmetUiItem,
      1
    ),
    WorkbenchRecipe(
      SilverIngotUiItem :: EmptyUiItem :: SilverIngotUiItem :: SilverIngotUiItem :: SilverIngotUiItem :: SilverIngotUiItem :: SilverIngotUiItem :: SilverIngotUiItem :: SilverIngotUiItem :: Nil,
      SilverChestplateUiItem,
      1
    ), // Silver Chestplate
    WorkbenchRecipe(
      SilverIngotUiItem :: SilverIngotUiItem :: SilverIngotUiItem :: SilverIngotUiItem :: EmptyUiItem :: SilverIngotUiItem :: SilverIngotUiItem :: EmptyUiItem :: SilverIngotUiItem :: Nil,
      SilverLeggingsUiItem,
      1
    ), // Silver Leggings
    WorkbenchRecipe(
      SilverIngotUiItem :: EmptyUiItem :: SilverIngotUiItem :: SilverIngotUiItem :: EmptyUiItem :: SilverIngotUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      SilverGreavesUiItem,
      1
    ), // Silver Greaves
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: SilverIngotUiItem :: EmptyUiItem :: SilverIngotUiItem :: SilverIngotUiItem :: EmptyUiItem :: SilverIngotUiItem :: Nil,
      SilverGreavesUiItem,
      1
    ),
    WorkbenchRecipe(
      GoldIngotUiItem :: GoldIngotUiItem :: GoldIngotUiItem :: GoldIngotUiItem :: EmptyUiItem :: GoldIngotUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      GoldHelmetUiItem,
      1
    ), // Gold Helmet
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: GoldIngotUiItem :: GoldIngotUiItem :: GoldIngotUiItem :: GoldIngotUiItem :: EmptyUiItem :: GoldIngotUiItem :: Nil,
      GoldHelmetUiItem,
      1
    ),
    WorkbenchRecipe(
      GoldIngotUiItem :: EmptyUiItem :: GoldIngotUiItem :: GoldIngotUiItem :: GoldIngotUiItem :: GoldIngotUiItem :: GoldIngotUiItem :: GoldIngotUiItem :: GoldIngotUiItem :: Nil,
      GoldChestplateUiItem,
      1
    ), // Gold Chestplate
    WorkbenchRecipe(
      GoldIngotUiItem :: GoldIngotUiItem :: GoldIngotUiItem :: GoldIngotUiItem :: EmptyUiItem :: GoldIngotUiItem :: GoldIngotUiItem :: EmptyUiItem :: GoldIngotUiItem :: Nil,
      GoldLeggingsUiItem,
      1
    ), // Gold Leggings
    WorkbenchRecipe(
      GoldIngotUiItem :: EmptyUiItem :: GoldIngotUiItem :: GoldIngotUiItem :: EmptyUiItem :: GoldIngotUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      GoldGreavesUiItem,
      1
    ), // Gold Greaves
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: GoldIngotUiItem :: EmptyUiItem :: GoldIngotUiItem :: GoldIngotUiItem :: EmptyUiItem :: GoldIngotUiItem :: Nil,
      GoldGreavesUiItem,
      1
    ),
    WorkbenchRecipe(
      ZincIngotUiItem :: ZincIngotUiItem :: ZincIngotUiItem :: ZincIngotUiItem :: EmptyUiItem :: ZincIngotUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      ZincHelmetUiItem,
      1
    ), // Zinc Helmet
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: ZincIngotUiItem :: ZincIngotUiItem :: ZincIngotUiItem :: ZincIngotUiItem :: EmptyUiItem :: ZincIngotUiItem :: Nil,
      ZincHelmetUiItem,
      1
    ),
    WorkbenchRecipe(
      ZincIngotUiItem :: EmptyUiItem :: ZincIngotUiItem :: ZincIngotUiItem :: ZincIngotUiItem :: ZincIngotUiItem :: ZincIngotUiItem :: ZincIngotUiItem :: ZincIngotUiItem :: Nil,
      ZincChestplateUiItem,
      1
    ), // Zinc Chestplate
    WorkbenchRecipe(
      ZincIngotUiItem :: ZincIngotUiItem :: ZincIngotUiItem :: ZincIngotUiItem :: EmptyUiItem :: ZincIngotUiItem :: ZincIngotUiItem :: EmptyUiItem :: ZincIngotUiItem :: Nil,
      ZincLeggingsUiItem,
      1
    ), // Zinc Leggings
    WorkbenchRecipe(
      ZincIngotUiItem :: EmptyUiItem :: ZincIngotUiItem :: ZincIngotUiItem :: EmptyUiItem :: ZincIngotUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      ZincGreavesUiItem,
      1
    ), // Zinc Greaves
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: ZincIngotUiItem :: EmptyUiItem :: ZincIngotUiItem :: ZincIngotUiItem :: EmptyUiItem :: ZincIngotUiItem :: Nil,
      ZincGreavesUiItem,
      1
    ),
    WorkbenchRecipe(
      RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: EmptyUiItem :: RhymestoneIngotUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      RhymestoneHelmetUiItem,
      1
    ), // Rhymestone Helmet
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: EmptyUiItem :: RhymestoneIngotUiItem :: Nil,
      RhymestoneHelmetUiItem,
      1
    ),
    WorkbenchRecipe(
      RhymestoneIngotUiItem :: EmptyUiItem :: RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: Nil,
      RhymestoneChestplateUiItem,
      1
    ), // Rhymestone Chestplate
    WorkbenchRecipe(
      RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: EmptyUiItem :: RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: EmptyUiItem :: RhymestoneIngotUiItem :: Nil,
      RhymestoneLeggingsUiItem,
      1
    ), // Rhymestone Leggings
    WorkbenchRecipe(
      RhymestoneIngotUiItem :: EmptyUiItem :: RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: EmptyUiItem :: RhymestoneIngotUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      RhymestoneGreavesUiItem,
      1
    ), // Rhymestone Greaves
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: RhymestoneIngotUiItem :: EmptyUiItem :: RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: EmptyUiItem :: RhymestoneIngotUiItem :: Nil,
      RhymestoneGreavesUiItem,
      1
    ),
    WorkbenchRecipe(
      ObduriteIngotUiItem :: ObduriteIngotUiItem :: ObduriteIngotUiItem :: ObduriteIngotUiItem :: EmptyUiItem :: ObduriteIngotUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      ObduriteHelmetUiItem,
      1
    ), // Obdurite Helmet
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: ObduriteIngotUiItem :: ObduriteIngotUiItem :: ObduriteIngotUiItem :: ObduriteIngotUiItem :: EmptyUiItem :: ObduriteIngotUiItem :: Nil,
      ObduriteHelmetUiItem,
      1
    ),
    WorkbenchRecipe(
      ObduriteIngotUiItem :: EmptyUiItem :: ObduriteIngotUiItem :: ObduriteIngotUiItem :: ObduriteIngotUiItem :: ObduriteIngotUiItem :: ObduriteIngotUiItem :: ObduriteIngotUiItem :: ObduriteIngotUiItem :: Nil,
      ObduriteChestplateUiItem,
      1
    ), // Obdurite Chestplate
    WorkbenchRecipe(
      ObduriteIngotUiItem :: ObduriteIngotUiItem :: ObduriteIngotUiItem :: ObduriteIngotUiItem :: EmptyUiItem :: ObduriteIngotUiItem :: ObduriteIngotUiItem :: EmptyUiItem :: ObduriteIngotUiItem :: Nil,
      ObduriteLeggingsUiItem,
      1
    ), // Obdurite Leggings
    WorkbenchRecipe(
      ObduriteIngotUiItem :: EmptyUiItem :: ObduriteIngotUiItem :: ObduriteIngotUiItem :: EmptyUiItem :: ObduriteIngotUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      ObduriteGreavesUiItem,
      1
    ), // Obdurite Greaves
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: ObduriteIngotUiItem :: EmptyUiItem :: ObduriteIngotUiItem :: ObduriteIngotUiItem :: EmptyUiItem :: ObduriteIngotUiItem :: Nil,
      ObduriteGreavesUiItem,
      1
    ),
    WorkbenchRecipe(
      AluminumIngotUiItem :: AluminumIngotUiItem :: AluminumIngotUiItem :: AluminumIngotUiItem :: EmptyUiItem :: AluminumIngotUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      AluminumHelmetUiItem,
      1
    ), // Aluminum Helmet
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: AluminumIngotUiItem :: AluminumIngotUiItem :: AluminumIngotUiItem :: AluminumIngotUiItem :: EmptyUiItem :: AluminumIngotUiItem :: Nil,
      AluminumHelmetUiItem,
      1
    ),
    WorkbenchRecipe(
      AluminumIngotUiItem :: EmptyUiItem :: AluminumIngotUiItem :: AluminumIngotUiItem :: AluminumIngotUiItem :: AluminumIngotUiItem :: AluminumIngotUiItem :: AluminumIngotUiItem :: AluminumIngotUiItem :: Nil,
      AluminumChestplateUiItem,
      1
    ), // Aluminum Chestplate
    WorkbenchRecipe(
      AluminumIngotUiItem :: AluminumIngotUiItem :: AluminumIngotUiItem :: AluminumIngotUiItem :: EmptyUiItem :: AluminumIngotUiItem :: AluminumIngotUiItem :: EmptyUiItem :: AluminumIngotUiItem :: Nil,
      AluminumLeggingsUiItem,
      1
    ), // Aluminum Leggings
    WorkbenchRecipe(
      AluminumIngotUiItem :: EmptyUiItem :: AluminumIngotUiItem :: AluminumIngotUiItem :: EmptyUiItem :: AluminumIngotUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      AluminumGreavesUiItem,
      1
    ), // Aluminum Greaves
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: AluminumIngotUiItem :: EmptyUiItem :: AluminumIngotUiItem :: AluminumIngotUiItem :: EmptyUiItem :: AluminumIngotUiItem :: Nil,
      AluminumGreavesUiItem,
      1
    ),
    WorkbenchRecipe(
      LeadIngotUiItem :: LeadIngotUiItem :: LeadIngotUiItem :: LeadIngotUiItem :: EmptyUiItem :: LeadIngotUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      LeadHelmetUiItem,
      1
    ), // Lead Helmet
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: LeadIngotUiItem :: LeadIngotUiItem :: LeadIngotUiItem :: LeadIngotUiItem :: EmptyUiItem :: LeadIngotUiItem :: Nil,
      LeadHelmetUiItem,
      1
    ),
    WorkbenchRecipe(
      LeadIngotUiItem :: EmptyUiItem :: LeadIngotUiItem :: LeadIngotUiItem :: LeadIngotUiItem :: LeadIngotUiItem :: LeadIngotUiItem :: LeadIngotUiItem :: LeadIngotUiItem :: Nil,
      LeadChestplateUiItem,
      1
    ), // Lead Chestplate
    WorkbenchRecipe(
      LeadIngotUiItem :: LeadIngotUiItem :: LeadIngotUiItem :: LeadIngotUiItem :: EmptyUiItem :: LeadIngotUiItem :: LeadIngotUiItem :: EmptyUiItem :: LeadIngotUiItem :: Nil,
      LeadLeggingsUiItem,
      1
    ), // Lead Leggings
    WorkbenchRecipe(
      LeadIngotUiItem :: EmptyUiItem :: LeadIngotUiItem :: LeadIngotUiItem :: EmptyUiItem :: LeadIngotUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      LeadGreavesUiItem,
      1
    ), // Lead Greaves
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: LeadIngotUiItem :: EmptyUiItem :: LeadIngotUiItem :: LeadIngotUiItem :: EmptyUiItem :: LeadIngotUiItem :: Nil,
      LeadGreavesUiItem,
      1
    ),
    WorkbenchRecipe(
      WoodUiItem :: WoodUiItem :: WoodUiItem :: WoodUiItem :: EmptyUiItem :: WoodUiItem :: WoodUiItem :: WoodUiItem :: WoodUiItem :: Nil,
      WoodenChestUiItem,
      1), // Wooden Chest
    WorkbenchRecipe(
      StoneUiItem :: StoneUiItem :: StoneUiItem :: StoneUiItem :: WoodenChestUiItem :: StoneUiItem :: StoneUiItem :: StoneUiItem :: StoneUiItem :: Nil,
      StoneChestUiItem,
      1), // Stone Chest
    WorkbenchRecipe(
      CopperIngotUiItem :: CopperIngotUiItem :: CopperIngotUiItem :: CopperIngotUiItem :: StoneChestUiItem :: CopperIngotUiItem :: CopperIngotUiItem :: CopperIngotUiItem :: CopperIngotUiItem :: Nil,
      CopperChestUiItem,
      1
    ), // Copper Chest
    WorkbenchRecipe(
      IronIngotUiItem :: IronIngotUiItem :: IronIngotUiItem :: IronIngotUiItem :: StoneChestUiItem :: IronIngotUiItem :: IronIngotUiItem :: IronIngotUiItem :: IronIngotUiItem :: Nil,
      IronChestUiItem,
      1
    ), // Iron Chest
    WorkbenchRecipe(
      SilverIngotUiItem :: SilverIngotUiItem :: SilverIngotUiItem :: SilverIngotUiItem :: StoneChestUiItem :: SilverIngotUiItem :: SilverIngotUiItem :: SilverIngotUiItem :: SilverIngotUiItem :: Nil,
      SilverChestUiItem,
      1
    ), // Silver Chest
    WorkbenchRecipe(
      GoldIngotUiItem :: GoldIngotUiItem :: GoldIngotUiItem :: GoldIngotUiItem :: StoneChestUiItem :: GoldIngotUiItem :: GoldIngotUiItem :: GoldIngotUiItem :: GoldIngotUiItem :: Nil,
      GoldChestUiItem,
      1
    ), // Gold Chest
    WorkbenchRecipe(
      ZincIngotUiItem :: ZincIngotUiItem :: ZincIngotUiItem :: ZincIngotUiItem :: StoneChestUiItem :: ZincIngotUiItem :: ZincIngotUiItem :: ZincIngotUiItem :: ZincIngotUiItem :: Nil,
      ZincChestUiItem,
      1
    ), // Zinc Chest
    WorkbenchRecipe(
      RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: StoneChestUiItem :: RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: RhymestoneIngotUiItem :: Nil,
      RhymestoneChestUiItem,
      1
    ), // Rhymestone Chest
    WorkbenchRecipe(
      ObduriteIngotUiItem :: ObduriteIngotUiItem :: ObduriteIngotUiItem :: ObduriteIngotUiItem :: StoneChestUiItem :: ObduriteIngotUiItem :: ObduriteIngotUiItem :: ObduriteIngotUiItem :: ObduriteIngotUiItem :: Nil,
      ObduriteChestUiItem,
      1
    ), // Obdurite Chest
    WorkbenchRecipe(
      GlassUiItem :: GlassUiItem :: GlassUiItem :: GlassUiItem :: LumenstoneUiItem :: GlassUiItem :: GlassUiItem :: ZythiumWireUiItem :: GlassUiItem :: Nil,
      ZythiumLampUiItem,
      1
    ), // Zythium Lamp
    WorkbenchRecipe(
      GlassUiItem :: GlassUiItem :: GlassUiItem :: ZythiumWireUiItem :: ZythiumOreUiItem :: ZythiumWireUiItem :: GlassUiItem :: GlassUiItem :: GlassUiItem :: Nil,
      ZythiumAmplifierUiItem,
      1
    ), // Zythium Amplifier
    WorkbenchRecipe(
      GlassUiItem :: GlassUiItem :: GlassUiItem :: ZythiumOreUiItem :: ZythiumWireUiItem :: ZythiumOreUiItem :: GlassUiItem :: GlassUiItem :: GlassUiItem :: Nil,
      ZythiumInverterUiItem,
      1
    ), // Zythium Inverter
    WorkbenchRecipe(
      GlassUiItem :: ZythiumWireUiItem :: GlassUiItem :: ZythiumWireUiItem :: ZythiumWireUiItem :: ZythiumWireUiItem :: GlassUiItem :: ZythiumWireUiItem :: GlassUiItem :: Nil,
      ZythiumDelayer1UiItem,
      1
    ), // Zythium Delayer
    WorkbenchRecipe(
      WoodUiItem :: WoodUiItem :: EmptyUiItem :: WoodUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      WorkbenchUiItem,
      1), // Workbench
    WorkbenchRecipe(
      EmptyUiItem :: WoodUiItem :: WoodUiItem :: EmptyUiItem :: WoodUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      WorkbenchUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: WoodUiItem :: EmptyUiItem :: WoodUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      WorkbenchUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: WoodUiItem :: EmptyUiItem :: WoodUiItem :: WoodUiItem :: Nil,
      WorkbenchUiItem,
      1),
    WorkbenchRecipe(
      BarkUiItem :: BarkUiItem :: EmptyUiItem :: BarkUiItem :: BarkUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      WoodUiItem,
      1), // Bark -> Wood
    WorkbenchRecipe(
      EmptyUiItem :: BarkUiItem :: BarkUiItem :: EmptyUiItem :: BarkUiItem :: BarkUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      WoodUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: BarkUiItem :: BarkUiItem :: EmptyUiItem :: BarkUiItem :: BarkUiItem :: EmptyUiItem :: Nil,
      WoodUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: BarkUiItem :: BarkUiItem :: EmptyUiItem :: BarkUiItem :: BarkUiItem :: Nil,
      WoodUiItem,
      1),
    WorkbenchRecipe(
      StoneUiItem :: StoneUiItem :: EmptyUiItem :: StoneUiItem :: StoneUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      CobblestoneUiItem,
      4), // Cobblestone
    WorkbenchRecipe(
      EmptyUiItem :: StoneUiItem :: StoneUiItem :: EmptyUiItem :: StoneUiItem :: StoneUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      CobblestoneUiItem,
      4),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: StoneUiItem :: StoneUiItem :: EmptyUiItem :: StoneUiItem :: StoneUiItem :: EmptyUiItem :: Nil,
      CobblestoneUiItem,
      4),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: StoneUiItem :: StoneUiItem :: EmptyUiItem :: StoneUiItem :: StoneUiItem :: Nil,
      CobblestoneUiItem,
      4),
    WorkbenchRecipe(
      ChiseledStoneUiItem :: ChiseledStoneUiItem :: EmptyUiItem :: ChiseledStoneUiItem :: ChiseledStoneUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      ChiseledCobblestoneUiItem,
      4
    ), // Chiseled Cobblestone
    WorkbenchRecipe(
      EmptyUiItem :: ChiseledStoneUiItem :: ChiseledStoneUiItem :: EmptyUiItem :: ChiseledStoneUiItem :: ChiseledStoneUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      ChiseledCobblestoneUiItem,
      4
    ),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: ChiseledStoneUiItem :: ChiseledStoneUiItem :: EmptyUiItem :: ChiseledStoneUiItem :: ChiseledStoneUiItem :: EmptyUiItem :: Nil,
      ChiseledCobblestoneUiItem,
      4
    ),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: ChiseledStoneUiItem :: ChiseledStoneUiItem :: EmptyUiItem :: ChiseledStoneUiItem :: ChiseledStoneUiItem :: Nil,
      ChiseledCobblestoneUiItem,
      4
    ),
    WorkbenchRecipe(
      ChiseledCobblestoneUiItem :: ChiseledCobblestoneUiItem :: EmptyUiItem :: ChiseledCobblestoneUiItem :: ChiseledCobblestoneUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      StoneBricksUiItem,
      4
    ), // Stone Bricks
    WorkbenchRecipe(
      EmptyUiItem :: ChiseledCobblestoneUiItem :: ChiseledCobblestoneUiItem :: EmptyUiItem :: ChiseledCobblestoneUiItem :: ChiseledCobblestoneUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      StoneBricksUiItem,
      4
    ),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: ChiseledCobblestoneUiItem :: ChiseledCobblestoneUiItem :: EmptyUiItem :: ChiseledCobblestoneUiItem :: ChiseledCobblestoneUiItem :: EmptyUiItem :: Nil,
      StoneBricksUiItem,
      4
    ),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: ChiseledCobblestoneUiItem :: ChiseledCobblestoneUiItem :: EmptyUiItem :: ChiseledCobblestoneUiItem :: ChiseledCobblestoneUiItem :: Nil,
      StoneBricksUiItem,
      4
    ),
    WorkbenchRecipe(
      StoneUiItem :: StoneUiItem :: StoneUiItem :: StoneUiItem :: EmptyUiItem :: StoneUiItem :: StoneUiItem :: StoneUiItem :: StoneUiItem :: Nil,
      FurnaceUiItem,
      1), // Furnace
    WorkbenchRecipe(
      ZythiumBarUiItem :: ZythiumBarUiItem :: ZythiumBarUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      ZythiumWireUiItem,
      10
    ), // Zythium Wire
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: ZythiumBarUiItem :: ZythiumBarUiItem :: ZythiumBarUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      ZythiumWireUiItem,
      20
    ),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: ZythiumBarUiItem :: ZythiumBarUiItem :: ZythiumBarUiItem :: Nil,
      ZythiumWireUiItem,
      20
    ),
    WorkbenchRecipe(
      StoneUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: StoneUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      StoneLighterUiItem,
      1), // Stone Lighter
    WorkbenchRecipe(
      EmptyUiItem :: StoneUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: StoneUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      StoneLighterUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: StoneUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: StoneUiItem :: EmptyUiItem :: Nil,
      StoneLighterUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: StoneUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: StoneUiItem :: Nil,
      StoneLighterUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: StoneUiItem :: EmptyUiItem :: StoneUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      StoneLighterUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: StoneUiItem :: EmptyUiItem :: StoneUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      StoneLighterUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: StoneUiItem :: EmptyUiItem :: StoneUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      StoneLighterUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: StoneUiItem :: EmptyUiItem :: StoneUiItem :: EmptyUiItem :: Nil,
      StoneLighterUiItem,
      1),
    WorkbenchRecipe(
      WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      WoodenTorchUiItem,
      4), // Wooden Torch
    WorkbenchRecipe(
      EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      WoodenTorchUiItem,
      4),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      WoodenTorchUiItem,
      4),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      WoodenTorchUiItem,
      4),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      WoodenTorchUiItem,
      4),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: Nil,
      WoodenTorchUiItem,
      4),
    WorkbenchRecipe(
      CoalUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      CoalTorchUiItem,
      4), // Coal Torch
    WorkbenchRecipe(
      EmptyUiItem :: CoalUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      CoalTorchUiItem,
      4),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: CoalUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      CoalTorchUiItem,
      4),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: CoalUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      CoalTorchUiItem,
      4),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: CoalUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      CoalTorchUiItem,
      4),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: CoalUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: Nil,
      CoalTorchUiItem,
      4),
    WorkbenchRecipe(
      LumenstoneUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      LumenstoneTorchUiItem,
      4), // Lumenstone Torch
    WorkbenchRecipe(
      EmptyUiItem :: LumenstoneUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      LumenstoneTorchUiItem,
      4),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: LumenstoneUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      LumenstoneTorchUiItem,
      4),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: LumenstoneUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      LumenstoneTorchUiItem,
      4),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: LumenstoneUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      LumenstoneTorchUiItem,
      4),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: LumenstoneUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: Nil,
      LumenstoneTorchUiItem,
      4),
    WorkbenchRecipe(
      ZythiumOreUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      ZythiumTorchUiItem,
      4), // Zythium Torch
    WorkbenchRecipe(
      EmptyUiItem :: ZythiumOreUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      ZythiumTorchUiItem,
      4),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: ZythiumOreUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      ZythiumTorchUiItem,
      4),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: ZythiumOreUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      ZythiumTorchUiItem,
      4),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: ZythiumOreUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      ZythiumTorchUiItem,
      4),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: ZythiumOreUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: Nil,
      ZythiumTorchUiItem,
      4),
    WorkbenchRecipe(
      WoodUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      WoodenPressurePlateUiItem,
      1), // Wooden Pressure Plate
    WorkbenchRecipe(
      EmptyUiItem :: WoodUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      WoodenPressurePlateUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      WoodenPressurePlateUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: WoodUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      WoodenPressurePlateUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: WoodUiItem :: EmptyUiItem :: Nil,
      WoodenPressurePlateUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: WoodUiItem :: WoodUiItem :: Nil,
      WoodenPressurePlateUiItem,
      1),
    WorkbenchRecipe(
      StoneUiItem :: StoneUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      StonePressurePlateUiItem,
      1), // Stone Pressure Plate
    WorkbenchRecipe(
      EmptyUiItem :: StoneUiItem :: StoneUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      StonePressurePlateUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: StoneUiItem :: StoneUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      StonePressurePlateUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: StoneUiItem :: StoneUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      StonePressurePlateUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: StoneUiItem :: StoneUiItem :: EmptyUiItem :: Nil,
      StonePressurePlateUiItem,
      1),
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: StoneUiItem :: StoneUiItem :: Nil,
      StonePressurePlateUiItem,
      1),
    WorkbenchRecipe(
      ChiseledStoneUiItem :: ZythiumOreUiItem :: ChiseledStoneUiItem :: EmptyUiItem :: ZythiumWireUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      ZythiumPressurePlateUiItem,
      1
    ), // Zythium Pressure Plate
    WorkbenchRecipe(
      EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: ChiseledStoneUiItem :: ZythiumOreUiItem :: ChiseledStoneUiItem :: EmptyUiItem :: ZythiumWireUiItem :: EmptyUiItem :: Nil,
      ZythiumPressurePlateUiItem,
      1
    )
  )

  def createShapelessRecipes: List[ShapelessRecipe] = List(
    ShapelessRecipe(
      WoodUiItem :: VarnishUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      VarnishedWoodUiItem,
      1),
    ShapelessRecipe(
      ChiseledStoneUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: EmptyUiItem :: Nil,
      ButtonUiItem,
      1)
  )

  // END CONSTRUCTOR

  def addItem(item: UiItem, quantity: Short): Short = {
    val maxstacks              = item.maxStacks
    var updatedQuantity: Short = quantity
    (0 until 40).foreach { i =>
      if (ids(i) === item && nums(i) < maxstacks) {
        if (maxstacks - nums(i) >= updatedQuantity) {
          nums(i) = (nums(i) + updatedQuantity).toShort
          update(i)
          return 0
        } else {
          updatedQuantity = (updatedQuantity - maxstacks - nums(i)).toShort
          nums(i) = maxstacks
          update(i)
        }
      }
    }
    (0 until 40).foreach { i =>
      if (ids(i) === EmptyUiItem) {
        ids(i) = item
        if (updatedQuantity <= maxstacks) {
          nums(i) = updatedQuantity
          durs(i) = UiItem.toolDurability(item)
          update(i)
          return 0
        } else {
          nums(i) = maxstacks
          updatedQuantity = (updatedQuantity - maxstacks).toShort
          durs(i) = UiItem.toolDurability(item)
        }
      }
    }
    updatedQuantity
  }

//  def removeItem(item: Short, quantity: Short): Short = {
//    var updatedQuantity: Short = quantity
//    (0 until 40).foreach { i =>
//      if (ids(i) === EmptyUiItem) {
//        if (nums(i) <= updatedQuantity) {
//          nums(i) = (nums(i) - updatedQuantity).toShort
//          if (nums(i) === 0) {
//            ids(i) = EmptyUiItem
//          }
//          update(i)
//          return 0
//        } else {
//          updatedQuantity = (updatedQuantity - nums(i)).toShort
//          nums(i) = 0
//          ids(i) = EmptyUiItem
//          update(i)
//        }
//      }
//    }
//    updatedQuantity
//  }

  def addLocation(i: Int, item: UiItem, quantity: Short): Short = {
    var updatedQuantity: Short = quantity
    val maxstacks              = item.maxStacks
    if (ids(i) === item) {
      if (maxstacks - nums(i) >= updatedQuantity) {
        nums(i) = (nums(i) + updatedQuantity).toShort
        update(i)
        return 0
      } else {
        updatedQuantity = (updatedQuantity - maxstacks - nums(i)).toShort
        nums(i) = maxstacks

        update(i)
      }
    } else {
      if (updatedQuantity <= maxstacks) {
        ids(i) = item
        nums(i) = updatedQuantity
        durs(i) = UiItem.toolDurability(item)
        update(i)
        return 0
      } else {
        updatedQuantity = (updatedQuantity - maxstacks).toShort
      }
    }
    updatedQuantity
  }

  def removeLocation(i: Int, quantity: Short): Short = {
    var updatedQuantity: Short = quantity
    if (nums(i) >= updatedQuantity) {
      nums(i) = (nums(i) - updatedQuantity).toShort
      if (nums(i) === 0) {
        ids(i) = EmptyUiItem
      }
      update(i)
      return 0
    } else {
      updatedQuantity = (updatedQuantity - nums(i)).toShort
      nums(i) = 0
      ids(i) = EmptyUiItem
      update(i)
    }
    updatedQuantity
  }

  def reloadImage(): Unit = {
    image = new BufferedImage(466, 190, BufferedImage.TYPE_INT_ARGB)
    box = loadImage("interface/inventory.png").get
    box_selected = loadImage("interface/inventory_selected.png").get
    g2 = image.createGraphics()
    (0 until 10).foreach { x =>
      (0 until 4).foreach { y =>
        if (x === 0 && y === 0) {
          drawImage(g2, box_selected, x * 46 + 6, y * 46 + 6, x * 46 + 46, y * 46 + 46, 0, 0, 40, 40)
          if (y === 0) {
            g2.setFont(font)
            g2.setColor(Color.BLACK)
            g2.drawString(f(x) + " ", x * 46 + trolx, y * 46 + troly)
          }
        } else {
          drawImage(g2, box, x * 46 + 6, y * 46 + 6, x * 46 + 46, y * 46 + 46, 0, 0, 40, 40)
          if (y === 0) {
            g2.setFont(font)
            g2.setColor(Color.BLACK)
            g2.drawString(f(x) + " ", x * 46 + trolx, y * 46 + troly)
          }
        }
      }
    }
    (0 until 40).foreach { i =>
      update(i)
    }
  }

  def update(i: Int): Unit = {
    py = i / 10
    px = i - (py * 10)
    (px * 46 + 6 until px * 46 + 46).foreach { x =>
      (py * 46 + 6 until py * 46 + 46).foreach { y =>
        image.setRGB(x, y, 9539985)
      }
    }
    g2 = image.createGraphics()
    if (i === selection) {
      drawImage(g2, box_selected, px * 46 + 6, py * 46 + 6, px * 46 + 46, py * 46 + 46, 0, 0, 40, 40)
      if (py === 0) {
        g2.setFont(font)
        g2.setColor(Color.BLACK)
        g2.drawString(f(px) + " ", px * 46 + trolx, py * 46 + troly)
      }
    } else {
      drawImage(g2, box, px * 46 + 6, py * 46 + 6, px * 46 + 46, py * 46 + 46, 0, 0, 40, 40)
      if (py === 0) {
        g2.setFont(font)
        g2.setColor(Color.BLACK)
        g2.drawString(f(px) + " ", px * 46 + trolx, py * 46 + troly)
      }
    }
    val item = ids(i)
    item match {
      case imageUiItem: ImageUiItem =>
        val itemImg = imageUiItem.image
        width = itemImg.getWidth()
        height = itemImg.getHeight()
        drawImage(
          g2,
          itemImg,
          px * 46 + 14 + ((24 - 12.toDouble / mh.max(width, height, 12) * width * 2) / 2).toInt,
          py * 46 + 14 + ((24 - 12.toDouble / mh.max(width, height, 12) * height * 2) / 2).toInt,
          px * 46 + 38 - ((24 - 12.toDouble / mh.max(width, height, 12) * width * 2) / 2).toInt,
          py * 46 + 38 - ((24 - 12.toDouble / mh.max(width, height, 12) * height * 2) / 2).toInt,
          0,
          0,
          width,
          height
        )
        if (nums(i) > 1) {
          g2.setFont(font)
          g2.setColor(Color.WHITE)
          g2.drawString(nums(i) + " ", px * 46 + 15, py * 46 + 40)
        }
      case _ =>
    }
  }

  def select(i: Int): Unit = {
    if (i === 0) {
      n = selection
      selection = 9
      update(n)
      update(9)
    } else {
      n = selection
      selection = i - 1
      update(n)
      update(i - 1)
    }
  }

  def select2(i: Int): Unit = {
    n = selection
    selection = i
    update(n)
    update(i)
  }

  def tool(): UiItem = {
    ids(selection)
  }

  def renderCollection(ic: ItemCollection): Unit = {
    if (ic.icType === Crafting) {
      (0 until 4).foreach { i =>
        updateIC(ic, i)
      }
    }
    if (ic.icType === Armor) {
      CX = 1
      CY = 4
      (0 until 4).foreach { i =>
        updateIC(ic, i)
      }
    }
    if (ic.icType === Workbench) { //TODO: type classes
      (0 until 9).foreach { i =>
        updateIC(ic, i)
      }
    }
    if (ic.icType === WoodenChest) {
      CX = 3
      CY = 3
      (0 until 9).foreach { i =>
        updateIC(ic, i)
      }
    }
    if (ic.icType === StoneChest) {
      CX = 5
      CY = 3
      (0 until 15).foreach { i =>
        updateIC(ic, i)
      }
    }
    if (ic.icType === CopperChest) {
      CX = 5
      CY = 4
      (0 until 20).foreach { i =>
        updateIC(ic, i)
      }
    }
    if (ic.icType === IronChest) {
      CX = 7
      CY = 4
      (0 until 28).foreach { i =>
        updateIC(ic, i)
      }
    }
    if (ic.icType === SilverChest) {
      CX = 7
      CY = 5
      (0 until 35).foreach { i =>
        updateIC(ic, i)
      }
    }
    if (ic.icType === GoldChest) {
      CX = 7
      CY = 6
      (0 until 42).foreach { i =>
        updateIC(ic, i)
      }
    }
    if (ic.icType === ZincChest) {
      CX = 7
      CY = 8
      (0 until 56).foreach { i =>
        updateIC(ic, i)
      }
    }
    if (ic.icType === RhymestoneChest) {
      CX = 8
      CY = 9
      (0 until 72).foreach { i =>
        updateIC(ic, i)
      }
    }
    if (ic.icType === ObduriteChest) {
      CX = 10
      CY = 10
      (0 until 100).foreach { i =>
        updateIC(ic, i)
      }
    }
    if (ic.icType === Furnace) {
      (-1 until 4).foreach { i =>
        updateIC(ic, i)
      }
    }
  }

  def addLocationIC(ic: ItemCollection, i: Int, item: UiItem, quantity: Short): Short = {
    var updatedQuantity: Short = quantity
    val maxstacks              = item.maxStacks
    if (ic.ids(i) === item) {
      if (maxstacks - ic.nums(i) >= updatedQuantity) {
        ic.nums(i) = (ic.nums(i) + updatedQuantity).toShort
        updateIC(ic, i)
        return 0
      } else {
        updatedQuantity = (updatedQuantity - maxstacks - ic.nums(i)).toShort
        ic.nums(i) = maxstacks

        updateIC(ic, i)
      }
    } else {
      if (updatedQuantity <= maxstacks) {
        ic.ids(i) = item
        ic.nums(i) = updatedQuantity
        ic.durs(i) = UiItem.toolDurability(item)
        updateIC(ic, i)
        return 0
      } else {
        updatedQuantity = (updatedQuantity - maxstacks).toShort
      }
    }
    updatedQuantity
  }

  def removeLocationIC(ic: ItemCollection, i: Int, quantity: Short): Short = {
    var updatedQuantity: Short = quantity
    if (ic.nums(i) >= updatedQuantity) {
      ic.nums(i) = (ic.nums(i) - updatedQuantity).toShort
      if (ic.nums(i) === 0) {
        ic.ids(i) = EmptyUiItem
      }
      updateIC(ic, i)
      return 0
    } else {
      updatedQuantity = (updatedQuantity - ic.nums(i)).toShort
      ic.nums(i) = 0
      ic.ids(i) = EmptyUiItem
      updateIC(ic, i)
    }
    updatedQuantity
  }

  def updateIC(ic: ItemCollection, i: Int): Unit = {
    if (ic.icType === Crafting) { //TODO: typeclass per trait
      py = i / 2
      px = i - (py * 2)
      (px * 40 until px * 40 + 40).foreach { x =>
        (py * 40 until py * 40 + 40).foreach { y =>
          ic.icType.image.setRGB(x, y, 9539985)
        }
      }
      g2 = ic.icType.image.createGraphics()
      drawImage(g2, box, px * 40, py * 40, px * 40 + 40, py * 40 + 40, 0, 0, 40, 40)
      val itemi = ic.ids(i)
      itemi match {
        case imageUiItem: ImageUiItem =>
          val itemImg = imageUiItem.image
          width = itemImg.getWidth()
          height = itemImg.getHeight()
          drawImage(
            g2,
            itemImg,
            px * 40 + 8 + ((24 - 12.toDouble / mh.max(width, height, 12) * width * 2) / 2).toInt,
            py * 40 + 8 + ((24 - 12.toDouble / mh.max(width, height, 12) * height * 2) / 2).toInt,
            px * 40 + 32 - ((24 - 12.toDouble / mh.max(width, height, 12) * width * 2) / 2).toInt,
            py * 40 + 32 - ((24 - 12.toDouble / mh.max(width, height, 12) * height * 2) / 2).toInt,
            0,
            0,
            width,
            height
          )
          if (ic.nums(i) > 1) {
            g2.setFont(font)
            g2.setColor(Color.WHITE)
            g2.drawString(ic.nums(i) + " ", px * 40 + 9, py * 40 + 34)
          }
        case _ =>
      }

      ic.ids(4) = EmptyUiItem
      import scala.util.control.Breaks._
      breakable {
        cicRecipes.foreach { cicRecipe =>
          valid = true

          breakable {
            cicRecipe.ingredients.foreach { ingredient =>
              if (ic.ids(i) =/= ingredient) {
                valid = false
                break
              }
            }
          }
          if (valid) {
            ic.ids(4) = cicRecipe.result
            ic.nums(4) = cicRecipe.count
            ic.durs(4) = UiItem.toolDurability(cicRecipe.result)
            break
          }
        }
      }
      val r3 = mutable.ArrayBuffer.empty[UiItem]
      breakable { // TODO: recipe logic for matching ingredients needs some help
        shapelessCicRecipes.foreach { shapelessCicRecipe =>
          valid = true
          r3.clear()
          shapelessCicRecipe.ingredients.foreach { j =>
            r3 += j
          }
          breakable {
            (0 until 4).foreach { j =>
              n = r3.indexOf(ic.ids(j))
              if (n === -1) {
                valid = false
                break
              } else {
                r3.remove(n)
              }
            }
          }
          if (valid) {
            ic.ids(4) = shapelessCicRecipe.result
            ic.nums(4) = shapelessCicRecipe.count
            ic.durs(4) = UiItem.toolDurability(shapelessCicRecipe.result)
            break
          }
        }
      }
      (3 * 40 until 3 * 40 + 40).foreach { x =>
        (20 until 20 + 40).foreach { y =>
          ic.icType.image.setRGB(x, y, 9539985)
        }
      }
      g2 = ic.icType.image.createGraphics()
      drawImage(g2, box, 3 * 40, 20, 3 * 40 + 40, 20 + 40, 0, 0, 40, 40)
      val item4 = ic.ids(4)
      item4 match {
        case imageUiItem: ImageUiItem => //TODO: this doesn't require anything from Inventory: render typeclass?
          val itemImg = imageUiItem.image
          width = itemImg.getWidth()
          height = itemImg.getHeight()
          drawImage(
            g2,
            itemImg,
            3 * 40 + 8 + ((24 - 12.toDouble / mh.max(width, height, 12) * width * 2) / 2).toInt,
            20 + 8 + ((24 - 12.toDouble / mh.max(width, height, 12) * height * 2) / 2).toInt,
            3 * 40 + 32 - ((24 - 12.toDouble / mh.max(width, height, 12) * width * 2) / 2).toInt,
            20 + 32 - ((24 - 12.toDouble / mh.max(width, height, 12) * height * 2) / 2).toInt,
            0,
            0,
            width,
            height
          )
          if (ic.nums(4) > 1) {
            g2.setFont(font)
            g2.setColor(Color.WHITE)
            g2.drawString(ic.nums(4) + " ", 3 * 40 + 9, 20 + 34)
          }

        case _ =>
      }
    }
    if (ic.icType === Armor) {
      py = i / CX
      px = i - (py * CX)
      (px * 46 until px * 46 + 40).foreach { x =>
        (py * 46 until py * 46 + 40).foreach { y =>
          ic.icType.image.setRGB(x, y, 9539985)
        }
      }
      g2 = ic.icType.image.createGraphics()
      drawImage(g2, box, px * 46, py * 46, px * 46 + 40, py * 46 + 40, 0, 0, 40, 40)
      UiItem.onImageItem(ic.ids(i)) { imageUiItem =>
        val itemImg = imageUiItem.image
        width = itemImg.getWidth()
        height = itemImg.getHeight()
        drawImage(
          g2,
          itemImg,
          px * 46 + 8 + ((24 - 12.toDouble / mh.max(width, height, 12) * width * 2) / 2).toInt,
          py * 46 + 8 + ((24 - 12.toDouble / mh.max(width, height, 12) * height * 2) / 2).toInt,
          px * 46 + 32 - ((24 - 12.toDouble / mh.max(width, height, 12) * width * 2) / 2).toInt,
          py * 46 + 32 - ((24 - 12.toDouble / mh.max(width, height, 12) * height * 2) / 2).toInt,
          0,
          0,
          width,
          height
        )

        if (ic.nums(i) > 1) {
          g2.setFont(font)
          g2.setColor(Color.WHITE)
          g2.drawString(ic.nums(i) + " ", px * 46 + 9, py * 46 + 34)
        }
      }
    }
    if (ic.icType === Workbench) {
      py = i / 3
      px = i - (py * 3)
      (px * 40 until px * 40 + 40).foreach { x =>
        (py * 40 until py * 40 + 40).foreach { y =>
          ic.icType.image.setRGB(x, y, 9539985)
        }
      }
      g2 = ic.icType.image.createGraphics()
      drawImage(g2, box, px * 40, py * 40, px * 40 + 40, py * 40 + 40, 0, 0, 40, 40)
      val itemi = ic.ids(i)
      itemi match {
        case imageUiItem: ImageUiItem =>
          val itemImg = imageUiItem.image
          width = itemImg.getWidth()
          height = itemImg.getHeight()
          drawImage(
            g2,
            itemImg,
            px * 40 + 8 + ((24 - 12.toDouble / mh.max(width, height, 12) * width * 2) / 2).toInt,
            py * 40 + 8 + ((24 - 12.toDouble / mh.max(width, height, 12) * height * 2) / 2).toInt,
            px * 40 + 32 - ((24 - 12.toDouble / mh.max(width, height, 12) * width * 2) / 2).toInt,
            py * 40 + 32 - ((24 - 12.toDouble / mh.max(width, height, 12) * height * 2) / 2).toInt,
            0,
            0,
            width,
            height
          )

          if (ic.nums(i) > 1) {
            g2.setFont(font)
            g2.setColor(Color.WHITE)
            g2.drawString(ic.nums(i) + " ", px * 40 + 9, py * 40 + 34)
          }

        case _ =>
      }
      ic.ids(9) = EmptyUiItem
      import scala.util.control.Breaks._
      breakable {
        workbenchRecipes.foreach { workbenchRecipe =>
          valid = true
          breakable {
            (0 until 9).foreach { i =>
              if (ic.ids(i) =/= workbenchRecipe.ingredients(i)) {
                valid = false
                break
              }
            }
          }
          if (valid) {
            ic.ids(9) = workbenchRecipe.result
            ic.nums(9) = workbenchRecipe.count
            ic.durs(9) = UiItem.toolDurability(workbenchRecipe.result)
            break
          }
        }
      }
      val r3 = mutable.ListBuffer.empty[UiItem]
      breakable {
        shapelessRecipes.foreach { shapelessRecipe =>
          valid = true
          r3.clear()
          shapelessRecipe.ingredients.foreach { j =>
            r3 += j
          }
          breakable {
            (0 until 9).foreach { j =>
              n = r3.indexOf(ic.ids(j))
              if (n === -1) {
                valid = false
                break
              } else {
                r3.remove(n)
              }
            }
          }
          if (valid) {
            ic.ids(9) = shapelessRecipe.result
            ic.nums(9) = shapelessRecipe.count
            ic.durs(9) = UiItem.toolDurability(shapelessRecipe.result)
            break
          }
        }
      }
      (4 * 40 until 4 * 40 + 40).foreach { x =>
        (1 * 40 until 1 * 40 + 40).foreach { y =>
          ic.icType.image.setRGB(x, y, 9539985)
        }
      }
      g2 = ic.icType.image.createGraphics()
      drawImage(g2, box, 4 * 40, 1 * 40, 4 * 40 + 40, 1 * 40 + 40, 0, 0, 40, 40)
      val item9 = ic.ids(9)
      item9 match {
        case imageUiItem: ImageUiItem =>
          val itemImg = imageUiItem.image
          width = itemImg.getWidth()
          height = itemImg.getHeight()
          drawImage(
            g2,
            itemImg,
            4 * 40 + 8 + ((24 - 12.toDouble / mh.max(width, height, 12) * width * 2) / 2).toInt,
            1 * 40 + 8 + ((24 - 12.toDouble / mh.max(width, height, 12) * height * 2) / 2).toInt,
            4 * 40 + 32 - ((24 - 12.toDouble / mh.max(width, height, 12) * width * 2) / 2).toInt,
            1 * 40 + 32 - ((24 - 12.toDouble / mh.max(width, height, 12) * height * 2) / 2).toInt,
            0,
            0,
            width,
            height
          )

          if (ic.nums(9) > 1) {
            g2.setFont(font)
            g2.setColor(Color.WHITE)
            g2.drawString(ic.nums(9) + " ", 4 * 40 + 9, 1 * 40 + 34)
          }
        case _ =>
      }
    }
    if (ic.icType === WoodenChest || ic.icType === StoneChest ||
        ic.icType === CopperChest || ic.icType === IronChest ||
        ic.icType === SilverChest || ic.icType === GoldChest ||
        ic.icType === ZincChest || ic.icType === RhymestoneChest ||
        ic.icType === ObduriteChest) { //TODO: chest trait?
      py = i / CX
      px = i - (py * CX)
      (px * 46 until px * 46 + 40).foreach { x =>
        (py * 46 until py * 46 + 40).foreach { y =>
          ic.icType.image.setRGB(x, y, 9539985)
        }
      }
      g2 = ic.icType.image.createGraphics()
      drawImage(g2, box, px * 46, py * 46, px * 46 + 40, py * 46 + 40, 0, 0, 40, 40)
      val uiItem = ic.ids(i)
      uiItem match {
        case imageUiItem: ImageUiItem =>
          val itemImg = imageUiItem.image
          width = itemImg.getWidth()
          height = itemImg.getHeight()
          drawImage(
            g2,
            itemImg,
            px * 46 + 8 + ((24 - 12.toDouble / mh.max(width, height, 12) * width * 2) / 2).toInt,
            py * 46 + 8 + ((24 - 12.toDouble / mh.max(width, height, 12) * height * 2) / 2).toInt,
            px * 46 + 32 - ((24 - 12.toDouble / mh.max(width, height, 12) * width * 2) / 2).toInt,
            py * 46 + 32 - ((24 - 12.toDouble / mh.max(width, height, 12) * height * 2) / 2).toInt,
            0,
            0,
            width,
            height
          )

          if (ic.nums(i) > 1) {
            g2.setFont(font)
            g2.setColor(Color.WHITE)
            g2.drawString(ic.nums(i) + " ", px * 46 + 9, py * 46 + 34)
          }

        case _ =>
      }

    }
    if (ic.icType === Furnace) {
      if (i === -1) {
        (0 until 5).foreach { y =>
          (0 until (ic.FUELP * 38).toInt).foreach { x =>
            ic.icType.image.setRGB(x + 1, y + 51, new Color(255, 0, 0).getRGB)
          }
          ((ic.FUELP * 38).toInt until 38).foreach { x =>
            ic.icType.image.setRGB(x + 1, y + 51, new Color(145, 145, 145).getRGB)
          }
        }
        (0 until 5).foreach { x =>
          (0 until (ic.SMELTP * 38).toInt).foreach { y =>
            ic.icType.image.setRGB(x + 40, y + 1, new Color(255, 0, 0).getRGB)
          }
          ((ic.SMELTP * 38).toInt until 38).foreach { y =>
            ic.icType.image.setRGB(x + 40, y + 1, new Color(145, 145, 145).getRGB)
          }
        }
      } else {
        if (i === 0) {
          fpx = 0
          fpy = 0
        }
        if (i === 1) {
          fpx = 0
          fpy = 1.4
        }
        if (i === 2) {
          fpx = 0
          fpy = 2.4
        }
        if (i === 3) {
          fpx = 1.4
          fpy = 0
        }
        ((fpx * 40).toInt until (fpx * 40 + 40).toInt).foreach { x =>
          ((fpy * 40).toInt until (fpy * 40 + 40).toInt).foreach { y =>
            ic.icType.image.setRGB(x, y, 9539985)
          }
        }
        g2 = ic.icType.image.createGraphics()
        drawImage(
          g2,
          box,
          (fpx * 40).toInt,
          (fpy * 40).toInt,
          (fpx * 40 + 40).toInt,
          (fpy * 40 + 40).toInt,
          0,
          0,
          40,
          40)
        val itemi = ic.ids(i)
        itemi match {
          case imageUiItem: ImageUiItem =>
            val itemImg = imageUiItem.image
            width = itemImg.getWidth()
            height = itemImg.getHeight()
            drawImage(
              g2,
              itemImg,
              (fpx * 40 + 8 + ((24 - 12.toDouble / mh.max(width, height, 12) * width * 2) / 2).toInt).toInt,
              (fpy * 40 + 8 + ((24 - 12.toDouble / mh.max(width, height, 12) * height * 2) / 2).toInt).toInt,
              (fpx * 40 + 32 - ((24 - 12.toDouble / mh.max(width, height, 12) * width * 2) / 2).toInt).toInt,
              (fpy * 40 + 32 - ((24 - 12.toDouble / mh.max(width, height, 12) * height * 2) / 2).toInt).toInt,
              0,
              0,
              width,
              height
            )

            if (ic.nums(i) > 1) {
              g2.setFont(font)
              g2.setColor(Color.WHITE)
              g2.drawString(ic.nums(i) + " ", (fpx * 40 + 9).toInt, (fpy * 40 + 34).toInt)
            }

          case _ =>
        }
      }
    }
  }

  def useRecipeWorkbench(ic: ItemCollection): Unit = {
    workbenchRecipes.foreach { workbenchRecipe =>
      valid = true
      import scala.util.control.Breaks._
      breakable {
        (0 until 9).foreach { i =>
          if (ic.ids(i) =/= workbenchRecipe.ingredients(i)) {
            valid = false
            break
          }
        }
      }
      if (valid) {
        (0 until 9).foreach { i =>
          removeLocationIC(ic, i, 1.toShort)
          updateIC(ic, i)
        }
      }
    }
    val r3 = mutable.ArrayBuffer.empty[UiItem]
    import scala.util.control.Breaks._
    breakable {
      shapelessRecipes.foreach { shapelessRecipe =>
        valid = true
        r3.clear()
        shapelessRecipe.ingredients.foreach { ingredient =>
          r3 += ingredient
        }
        breakable {
          (0 until 9).foreach { k =>
            n = r3.indexOf(ic.ids(k))
            if (n === -1) {
              valid = false
              break
            } else {
              r3.remove(n)
            }
          }
        }
        if (valid) {
          r3.clear()
          shapelessRecipe.ingredients.foreach { ingredient =>
            r3 += ingredient
          }
          (0 until 9).foreach { k =>
            n = r3.indexOf(ic.ids(k))
            r3.remove(n)
            removeLocationIC(ic, k, 1.toShort)
            updateIC(ic, k)
          }
          break
        }
      }
    }
  }

  def useRecipeCIC(ic: ItemCollection): Unit = {
    cicRecipes.foreach { cicRecipe =>
      valid = true
      import scala.util.control.Breaks._
      breakable {
        (0 until 4).foreach { i =>
          if (ic.ids(i) =/= cicRecipe.ingredients(i)) {
            valid = false
            break
          }
        }
      }
      if (valid) {
        (0 until 4).foreach { i =>
          removeLocationIC(ic, i, 1.toShort)
          updateIC(ic, i)
        }
      }
    }
    val r3 = mutable.ArrayBuffer.empty[UiItem]
    import scala.util.control.Breaks._
    breakable {
      shapelessCicRecipes.foreach { shapelessCicRecipe =>
        valid = true
        r3.clear()
        shapelessCicRecipe.ingredients.foreach { r =>
          r3 += r
        }
        breakable {
          (0 until 4).foreach { k =>
            n = r3.indexOf(ic.ids(k))
            if (n === -1) {
              valid = false
              break
            } else {
              r3.remove(n)
            }
          }
        }
        if (valid) {
          r3.clear()
          shapelessCicRecipe.ingredients.foreach { k =>
            r3 += k
          }
          (0 until 4).foreach { k =>
            n = r3.indexOf(ic.ids(k))
            r3.remove(n)
            removeLocationIC(ic, k, 1.toShort)
            updateIC(ic, k)
          }
          break
        }
      }
    }
  }

}
