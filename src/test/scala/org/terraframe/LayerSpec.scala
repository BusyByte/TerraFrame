package org.terraframe

import org.specs2.mutable.Specification
import org.terraframe.Layer.{BackgroundLayer, ForegroundLayer, PrimaryLayer}

class LayerSpec extends Specification {
  "Layer must values must be in sequential order" in {
    Layer.values.toList must_== List[Layer](BackgroundLayer, PrimaryLayer, ForegroundLayer)
  }
}
