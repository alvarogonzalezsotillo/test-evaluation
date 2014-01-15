package evaluation.engine;

import boofcv.core.image.ConvertBufferedImage
import boofcv.struct.image.ImageFloat32
import boofcv.struct.image.MultiSpectral

import georegression.struct.homo.Homography2D_F64

import java.awt.image.BufferedImage
import java.awt.Color
import evaluation.engine.Image._
import evaluation.engine.Geom._


sealed class Img()

case object NoImg extends Img


class Image(val visualizable: Visualizable) extends Img{
  assert( visualizable != null )
}

object Image{
  type Visualizable = BufferedImage
  type Homography = Homography2D_F64
  
  implicit def toOps(v:Visualizable) = new VisualizableOps(v)
  
  def apply(visualizable: Visualizable) = new Image(visualizable)
  def unapply( img: Image ) = img.visualizable match{
    case null => None
    case v => new Some( v )
  }
}

case class SubImg( override val visualizable: Visualizable, parent: Image, x: Int, y: Int ) extends Image(visualizable)

case class ImgAndPattern( override val visualizable: Visualizable, pattern: Visualizable, homography: Homography ) extends Image(visualizable)


class VisualizableOps( v: Visualizable ){
  def crop( r: Rect ) = {
    val dx1 = 0
    val dy1 = 0
    val dx2 = r.width.asInstanceOf[Int]
    val dy2 = r.height.asInstanceOf[Int]
    val sx1 = r.left.asInstanceOf[Int]
    val sy1 = r.top.asInstanceOf[Int]
    val sx2 = r.right.asInstanceOf[Int]
    val sy2 = r.bottom.asInstanceOf[Int]
    val ret = new BufferedImage( dx2, dy2, BufferedImage.TYPE_INT_RGB )
    ret.getGraphics.drawImage(v,dx1,dy1,dx2,dy2,sx1,sy1,sx2,sy2,Color.YELLOW,null)
    ret
  }
}
