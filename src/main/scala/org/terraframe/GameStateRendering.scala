package org.terraframe

import java.awt.image.BufferedImage
import java.awt.{ Color, Graphics2D, Point }

import GraphicsHelper._
import TypeSafeComparisons._
import Images.loadImage
import TerraFrame._
import Biome._
import org.terraframe.{ MathHelper => mh }
import Layer._

trait GameStateRendering[T] {
  def render(screenGraphics: Graphics2D, terraFrame: TerraFrame): Unit
}

object GameStateRendering {

  def renderer[A <: GameState](implicit ev: GameStateRendering[A]): GameStateRendering[A] = {
    ev
  }

  object Implicits {
    implicit lazy val inGameRendering: GameStateRendering[InGame.type]                   = InGameRendering
    implicit lazy val loadingGraphicsRendering: GameStateRendering[LoadingGraphics.type] = LoadingGraphicsRendering
    implicit lazy val titleScreenRendering: GameStateRendering[TitleScreen.type]         = TitleScreenRendering
    implicit lazy val selectWorldRendering: GameStateRendering[SelectWorld.type]         = SelectWorldRendering
    implicit lazy val newWorldRendering: GameStateRendering[NewWorld.type]               = NewWorldRendering
    implicit lazy val generatingWorldRendering: GameStateRendering[GeneratingWorld.type] = GeneratingWorldRendering
    implicit lazy val loadingWorldRendering: GameStateRendering[LoadingWorld.type]       = LoadingWorldRendering
  }
}

object InGameRendering extends GameStateRendering[InGame.type] {

  lazy val layersBImage = loadImage("interface/layersB.png").get
  lazy val layersNImage = loadImage("interface/layersN.png").get
  lazy val layersFImage = loadImage("interface/layersF.png").get

  override def render(screenGraphics: Graphics2D, terraFrame: TerraFrame): Unit = {

    import terraFrame._
    import math.Pi
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
      screenGraphics.translate((getWidth / 2).toDouble, getHeight * 0.85)
      screenGraphics.rotate((timeOfDay - 70200) / 86400 * Pi * 2)

      drawImage(
        screenGraphics,
        sun,
        (-getWidth * 0.65).toInt,
        0,
        (-getWidth * 0.65 + sun.getWidth() * 2).toInt,
        sun.getHeight() * 2,
        0,
        0,
        sun.getWidth(),
        sun.getHeight())

      screenGraphics.rotate(Pi)

      drawImage(
        screenGraphics,
        moon,
        (-getWidth * 0.65).toInt,
        0,
        (-getWidth * 0.65 + moon.getWidth() * 2).toInt,
        moon.getHeight() * 2,
        0,
        0,
        moon.getWidth(),
        moon.getHeight())

      screenGraphics.rotate(-(timeOfDay - 70200) / 86400 * Pi * 2 - Pi)
      screenGraphics.translate((-getWidth / 2).toDouble, -getHeight * 0.85)

      cloudsx.indices.foreach { i =>
        val cloud: BufferedImage = clouds(cloudsn(i))
        drawImage(
          screenGraphics,
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
              screenGraphics,
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
      screenGraphics,
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
        screenGraphics,
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
        screenGraphics,
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
        screenGraphics,
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
          screenGraphics.translate(getWidth / 2 + 6, getHeight / 2)
          screenGraphics.rotate(toolAngle)

          drawImage(screenGraphics, t, 0, -t.getHeight() * 2, t.getWidth() * 2, 0, 0, 0, t.getWidth(), t.getHeight())

          screenGraphics.rotate(-toolAngle)
          screenGraphics.translate(-getWidth / 2 - 6, -getHeight / 2)
        }
        if (player.imgState === Player.StillLeft || player.imgState === Player.WalkLeft1 || player.imgState === Player.WalkLeft2) {
          screenGraphics.translate(getWidth / 2 - 6, getHeight / 2)
          screenGraphics.rotate((Pi * 1.5) - toolAngle)

          drawImage(screenGraphics, t, 0, -t.getHeight() * 2, t.getWidth() * 2, 0, 0, 0, t.getWidth(), t.getHeight())

          screenGraphics.rotate(-((Pi * 1.5) - toolAngle))
          screenGraphics.translate(-getWidth / 2 + 6, -getHeight / 2)
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
              screenGraphics,
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
        screenGraphics,
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
        screenGraphics,
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
          screenGraphics,
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
        screenGraphics,
        inventory.image,
        0,
        0,
        inventory.image.getWidth(),
        inventory.image.getHeight() / 4,
        0,
        0,
        inventory.image.getWidth(),
        inventory.image.getHeight() / 4
      )
    }

    ic.foreach { icTemp =>
      drawImage(
        screenGraphics,
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

    val layerImg: BufferedImage = layer match {
      case BackgroundLayer => layersBImage
      case PrimaryLayer    => layersNImage
      case ForegroundLayer => layersFImage
    }

    drawImage(
      screenGraphics,
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
        screenGraphics,
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
      val image  = mi.image
      val width  = image.getWidth
      val height = image.getHeight
      drawImage(
        screenGraphics,
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
        screenGraphics.setFont(font)
        screenGraphics.setColor(Color.WHITE)
        screenGraphics.drawString(moveNum + " ", mouseX + 13, mouseY + 38)
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
            screenGraphics.setFont(mobFont)
            screenGraphics.setColor(Color.WHITE)
            screenGraphics.drawString(entityName + " (" + entity.hp + "/" + entity.strategy.thp + ")", mouseX, mouseY)
            break
          }
        }
    }
    if (showInv) {
      ymax = 4
    } else {
      ymax = 1
    }
    (0 until 10).foreach { ux =>
      (0 until ymax).foreach { uy =>
        if (mouseX >= ux * 46 + 6 && mouseX <= ux * 46 + 46 &&
            mouseY >= uy * 46 + 6 && mouseY <= uy * 46 + 46 && inventory.ids(uy * 10 + ux) =/= EmptyUiItem) {
          UiItem.renderOverlayText(
            inventory.ids(uy * 10 + ux),
            inventory.durs(uy * 10 + ux),
            mouseX,
            mouseY,
            screenGraphics,
            mobFont)
        }
      }
    }
    screenGraphics.setFont(mobFont)
    screenGraphics.setColor(Color.WHITE)
    screenGraphics.drawString("Health: " + player.hp + "/" + Player.totalHP, getWidth - 125, 20)
    screenGraphics.drawString("Armor: " + player.sumArmor(), getWidth - 125, 40)
    if (DEBUG_STATS) {
      screenGraphics.drawString(
        "(" + (player.ix / BLOCKSIZE) + ", " + (player.iy / BLOCKSIZE) + ")",
        getWidth - 125,
        60)
      if (player.iy >= 0 && player.iy < HEIGHT * BLOCKSIZE) {
        screenGraphics.drawString(
          checkBiome(player.ix / BLOCKSIZE + u, player.iy / BLOCKSIZE + v, u, v, blocks, blockbgs).name + " " + lights(
            player.iy / BLOCKSIZE + v)(player.ix / BLOCKSIZE + u),
          getWidth - 125,
          80
        )
      }
    }
    if (showInv) {
      cic.foreach { c =>
        (0 until 2).foreach { ux =>
          (0 until 2).foreach { uy =>
            if (mouseX >= inventory.image.getWidth() + ux * 40 + 75 &&
                mouseX < inventory.image.getWidth() + ux * 40 + 115 &&
                mouseY >= uy * 40 + 52 && mouseY < uy * 40 + 92 && c.ids(uy * 2 + ux) =/= EmptyUiItem) {
              UiItem.renderOverlayText(
                c.ids(uy * 2 + ux),
                c.durs(uy * 2 + ux),
                mouseX,
                mouseY,
                screenGraphics,
                mobFont)
            }
          }
        }
        if (mouseX >= inventory.image.getWidth() + 3 * 40 + 75 &&
            mouseX < inventory.image.getWidth() + 3 * 40 + 115 &&
            mouseY >= 20 + 52 && mouseY < 20 + 92 && c.ids(4) =/= EmptyUiItem) {
          UiItem.renderOverlayText(c.ids(4), c.durs(4), mouseX, mouseY, screenGraphics, mobFont)
        }
      }
      (0 until 4).foreach { uy =>
        if (mouseX >= inventory.image.getWidth() + 6 && mouseX < inventory.image.getWidth() + 6 + armor.icType.image
              .getWidth() &&
            mouseY >= 6 + uy * 46 && mouseY < 6 + uy * 46 + 46 && armor.ids(uy) =/= EmptyUiItem) {
          UiItem.renderOverlayText(armor.ids(uy), armor.durs(uy), mouseX, mouseY, screenGraphics, mobFont)
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
                UiItem.renderOverlayText(
                  icTemp.ids(uy * 3 + ux),
                  icTemp.durs(uy * 3 + ux),
                  mouseX,
                  mouseY,
                  screenGraphics,
                  mobFont)
              }
            }
          }
          if (mouseX >= 4 * 40 + 6 && mouseX < 4 * 40 + 46 &&
              mouseY >= 1 * 40 + inventory.image.getHeight() + 46 &&
              mouseY < 1 * 40 + inventory.image.getHeight() + 86 &&
              icTemp.ids(9) =/= EmptyUiItem) {
            UiItem.renderOverlayText(icTemp.ids(9), icTemp.durs(9), mouseX, mouseY, screenGraphics, mobFont)
          }

        case ChestItemCollection(_) =>
          (0 until inventory.CX).foreach { ux =>
            (0 until inventory.CY).foreach { uy =>
              if (mouseX >= ux * 46 + 6 && mouseX < ux * 46 + 46 &&
                  mouseY >= uy * 46 + inventory.image.getHeight() + 46 &&
                  mouseY < uy * 46 + inventory.image.getHeight() + 86 &&
                  icTemp.ids(uy * inventory.CX + ux) =/= EmptyUiItem) {
                UiItem.renderOverlayText(
                  icTemp.ids(uy * inventory.CX + ux),
                  icTemp.durs(uy * inventory.CX + ux),
                  mouseX,
                  mouseY,
                  screenGraphics,
                  mobFont)
              }
            }
          }

        case Furnace =>
          if (mouseX >= 6 && mouseX < 46 &&
              mouseY >= inventory.image.getHeight() + 46 && mouseY < inventory.image.getHeight() + 86 &&
              icTemp.ids(0) =/= EmptyUiItem) {

            UiItem.renderOverlayText(icTemp.ids(0), icTemp.durs(0), mouseX, mouseY, screenGraphics, mobFont)
          }
          if (mouseX >= 6 && mouseX < 46 &&
              mouseY >= inventory.image.getHeight() + 102 && mouseY < inventory.image.getHeight() + 142 &&
              icTemp.ids(1) =/= EmptyUiItem) {
            screenGraphics.setFont(mobFont)
            screenGraphics.setColor(Color.WHITE)

            UiItem.renderOverlayText(icTemp.ids(1), icTemp.durs(1), mouseX, mouseY, screenGraphics, mobFont)
          }
          if (mouseX >= 6 && mouseX < 46 &&
              mouseY >= inventory.image.getHeight() + 142 && mouseY < inventory.image.getHeight() + 182 &&
              icTemp.ids(2) =/= EmptyUiItem) {
            UiItem.renderOverlayText(icTemp.ids(2), icTemp.durs(2), mouseX, mouseY, screenGraphics, mobFont)
          }
          if (mouseX >= 62 && mouseX < 102 &&
              mouseY >= inventory.image.getHeight() + 46 && mouseY < inventory.image.getHeight() + 86 &&
              icTemp.ids(3) =/= EmptyUiItem) {
            UiItem.renderOverlayText(icTemp.ids(3), icTemp.durs(3), mouseX, mouseY, screenGraphics, mobFont)
          }

        case _ =>
      }
    }

  }

}

object LoadingGraphicsRendering extends GameStateRendering[LoadingGraphics.type] {
  def render(screenGraphics: Graphics2D, terraFrame: TerraFrame): Unit = {
    import terraFrame._
    screenGraphics.setFont(loadFont)
    screenGraphics.setColor(Color.GREEN)
    screenGraphics.drawString("Loading graphics... Please wait.", getWidth() / 2 - 200, getHeight() / 2 - 5)
  }
}

object TitleScreenRendering extends GameStateRendering[TitleScreen.type] {
  def render(screenGraphics: Graphics2D, terraFrame: TerraFrame): Unit = {
    import terraFrame._
    drawImage(screenGraphics, title_screen, 0, 0, getWidth(), getHeight(), 0, 0, getWidth(), getHeight())
    ()
  }
}

object SelectWorldRendering extends GameStateRendering[SelectWorld.type] {
  def render(screenGraphics: Graphics2D, terraFrame: TerraFrame): Unit = {
    import terraFrame._
    drawImage(screenGraphics, select_world, 0, 0, getWidth(), getHeight(), 0, 0, getWidth(), getHeight())
    worldNames.indices.foreach { i =>
      screenGraphics.setFont(worldFont)
      screenGraphics.setColor(Color.BLACK)
      screenGraphics.drawString(worldNames(i), 180, 140 + i * 35)
      screenGraphics.fillRect(166, 150 + i * 35, 470, 3)
    }
  }
}

object NewWorldRendering extends GameStateRendering[NewWorld.type] {
  def render(screenGraphics: Graphics2D, terraFrame: TerraFrame): Unit = {
    import terraFrame._
    drawImage(screenGraphics, new_world, 0, 0, getWidth(), getHeight(), 0, 0, getWidth(), getHeight())
    drawImage(screenGraphics, newWorldName.image, 200, 240, 600, 270, 0, 0, 400, 30)
    ()
  }
}

object GeneratingWorldRendering extends GameStateRendering[GeneratingWorld.type] {
  override def render(screenGraphics: Graphics2D, terraFrame: TerraFrame): Unit = {
    import terraFrame._
    screenGraphics.setFont(loadFont)
    screenGraphics.setColor(Color.GREEN)
    screenGraphics.drawString("Generating new world... Please wait.", getWidth() / 2 - 200, getHeight() / 2 - 5)
  }
}

object LoadingWorldRendering extends GameStateRendering[LoadingWorld.type] {
  override def render(screenGraphics: Graphics2D, terraFrame: TerraFrame): Unit = {}
}
