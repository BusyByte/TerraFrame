package org.terraframe

object OutlineDirection {
  lazy val dirs = List(
    CenterOutlineDirection,
    TdownBothOutlineDirection,
    TdownCwOutlineDirection,
    CdownCcwOutlineDirection,
    TdownOutlineDirection,
    TupBothOutlineDirection,
    TupCwOutlineDirection,
    TupCcwOutlineDirection,
    TupOutlineDirection,
    LeftrightOutlineDirection,
    TrightBothOutlineDirection,
    TrightCwOutlineDirection,
    TrightCcwOutlineDirection,
    TrightOutlineDirection,
    UpleftdiagOutlineDirection,
    UpleftOutlineDirection,
    DownleftdiagOutlineDirection,
    DownleftOutlineDirection,
    LeftOutlineDirection,
    TleftBothOutlineDirection,
    TleftCwOutlineDirection,
    TleftCcwOutlineDirection,
    TleftOutlineDirection,
    UprightdiagOutlineDirection,
    UprightOutlineDirection,
    DownrightdiagOutlineDirection,
    DownrightOutlineDirection,
    RightOutlineDirection,
    UpdownOutlineDirection,
    UpOutlineDirection,
    DownOutlineDirection,
    SingleOutlineDirection
  )
}

sealed abstract class OutlineDirection(val id: Byte, val imageName: String)
object CenterOutlineDirection        extends OutlineDirection(0, "center")
object TdownBothOutlineDirection     extends OutlineDirection(1, "tdown_both")
object TdownCwOutlineDirection       extends OutlineDirection(2, "tdown_cw")
object CdownCcwOutlineDirection      extends OutlineDirection(3, "tdown_ccw")
object TdownOutlineDirection         extends OutlineDirection(4, "tdown")
object TupBothOutlineDirection       extends OutlineDirection(5, "tup_both")
object TupCwOutlineDirection         extends OutlineDirection(6, "tup_cw")
object TupCcwOutlineDirection        extends OutlineDirection(7, "tup_ccw")
object TupOutlineDirection           extends OutlineDirection(8, "tup")
object LeftrightOutlineDirection     extends OutlineDirection(9, "leftright")
object TrightBothOutlineDirection    extends OutlineDirection(10, "tright_both")
object TrightCwOutlineDirection      extends OutlineDirection(11, "tright_cw")
object TrightCcwOutlineDirection     extends OutlineDirection(12, "tright_ccw")
object TrightOutlineDirection        extends OutlineDirection(13, "tright")
object UpleftdiagOutlineDirection    extends OutlineDirection(14, "upleftdiag")
object UpleftOutlineDirection        extends OutlineDirection(15, "upleft")
object DownleftdiagOutlineDirection  extends OutlineDirection(16, "downleftdiag")
object DownleftOutlineDirection      extends OutlineDirection(17, "downleft")
object LeftOutlineDirection          extends OutlineDirection(18, "left")
object TleftBothOutlineDirection     extends OutlineDirection(19, "tleft_both")
object TleftCwOutlineDirection       extends OutlineDirection(20, "tleft_cw")
object TleftCcwOutlineDirection      extends OutlineDirection(21, "tleft_ccw")
object TleftOutlineDirection         extends OutlineDirection(22, "tleft")
object UprightdiagOutlineDirection   extends OutlineDirection(23, "uprightdiag")
object UprightOutlineDirection       extends OutlineDirection(24, "upright")
object DownrightdiagOutlineDirection extends OutlineDirection(25, "downrightdiag")
object DownrightOutlineDirection     extends OutlineDirection(26, "downright")
object RightOutlineDirection         extends OutlineDirection(27, "right")
object UpdownOutlineDirection        extends OutlineDirection(28, "updown")
object UpOutlineDirection            extends OutlineDirection(29, "up")
object DownOutlineDirection          extends OutlineDirection(30, "down")
object SingleOutlineDirection        extends OutlineDirection(31, "single")
