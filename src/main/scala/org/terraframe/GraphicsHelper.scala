package org.terraframe

import java.awt.{ Graphics, Image }

object GraphicsHelper {
  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  @inline
  def drawImage(g: Graphics,
                img: Image,
                dx1: Int,
                dy1: Int,
                dx2: Int,
                dy2: Int,
                sx1: Int,
                sy1: Int,
                sx2: Int,
                sy2: Int): Boolean =
    g.drawImage(img: Image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null)

}
