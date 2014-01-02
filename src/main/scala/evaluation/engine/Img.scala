package evaluation.engine;

import boofcv.core.image.ConvertBufferedImage
import boofcv.struct.image.ImageFloat32
import boofcv.struct.image.MultiSpectral

import java.awt.image.BufferedImage
import evaluation.engine.Img.Visualizable

class Img{
  private var _visualizable: Visualizable = null
  lazy val visualizable = _visualizable
}

object Img{
  type Visualizable = BufferedImage

  def apply(v:Visualizable) = {
    val ret = new Img
    ret._visualizable = v
    ret
  }
}
