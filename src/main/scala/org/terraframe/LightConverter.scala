package org.terraframe

import java.awt.image._
import java.io._
import javax.imageio.ImageIO

import Images.loadImage
import GraphicsHelper._

import scala.io.StdIn

object LightConverter {
  private val logger = org.log4s.getLogger

  val BLOCKSIZE: Int = 16
  val IMAGESIZE: Int = 8

  val dirs: Array[String] = Array(
    "center",
    "tdown_both",
    "tdown_cw",
    "tdown_ccw",
    "tdown",
    "tup_both",
    "tup_cw",
    "tup_ccw",
    "tup",
    "leftright",
    "tright_both",
    "tright_cw",
    "tright_ccw",
    "tright",
    "upleftdiag",
    "upleft",
    "downleftdiag",
    "downleft",
    "left",
    "tleft_both",
    "tleft_cw",
    "tleft_ccw",
    "tleft",
    "uprightdiag",
    "upright",
    "downrightdiag",
    "downright",
    "right",
    "updown",
    "up",
    "down",
    "single"
  )

  def main(args: Array[String]): Unit = {
    (0 until 17).foreach { i =>
      print("Generate new textures [" + i + "] for: ")
      val name: String         = StdIn.readLine()
      val light: BufferedImage = loadImage("light/" + i + ".png").get
      (1 until 9).foreach { j =>
        val texture: BufferedImage = loadImage("blocks/" + name + "/texture" + j + ".png").get
        drawImage(texture.createGraphics(), light, 0, 0, IMAGESIZE, IMAGESIZE, 0, 0, IMAGESIZE, IMAGESIZE)
        try {
          ImageIO.write(texture, "png", new File("blocks/" + name + "/texture" + j + ".png"))
        } catch {
          case e: IOException => logger.error(e)("Error in writing file.")
        }
      }
    }
  }

}
