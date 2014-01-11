package evaluation.engine;

import boofcv.core.image.ConvertBufferedImage
import boofcv.struct.image.ImageFloat32
import boofcv.struct.image.MultiSpectral

import georegression.struct.homo.Homography2D_F64

import java.awt.image.BufferedImage
import evaluation.engine.Img.Visualizable
import evaluation.engine.Img.Homography



class Img(val visualizable: Visualizable)




object Img{
  type Visualizable = BufferedImage
  type Homography = Homography2D_F64
  
  def apply(visualizable: Visualizable) = new Img(visualizable)
  def unapply( img: Img ) = new Some( img.visualizable )
}

case class ImgAndPattern( override val visualizable: Visualizable, pattern: Visualizable, homography: Homography ) extends Img(visualizable)