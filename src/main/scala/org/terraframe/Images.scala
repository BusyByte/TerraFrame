package org.terraframe

import java.awt.image.BufferedImage
import java.net.URL
import javax.imageio.ImageIO

import scala.util.control.NonFatal

object Images {
  private val logger = org.log4s.getLogger

  def loadImage(path: String): Option[BufferedImage] = {
    val url: URL = getClass.getResource("/" + path)
    try {
      Option(ImageIO.read(url))
    } catch {
      case NonFatal(e) =>
        logger.error(e)(s"Error loading image for $path")
        None
    }
  }
}
