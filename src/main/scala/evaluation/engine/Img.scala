package evaluation.engine;

import boofcv.core.image.ConvertBufferedImage
import boofcv.struct.image.ImageFloat32
import boofcv.struct.image.MultiSpectral

import georegression.struct.homo.Homography2D_F64

import java.awt.image.BufferedImage
import java.awt.Color
import java.awt.Color._
import evaluation.engine.Image._
import evaluation.engine.Geom._


sealed abstract class Img(){
  def label : String
}

case object NoImg extends Img{
  val label = "No image"
}


class Image(val visualizable: Visualizable, val label: String) extends Img{
  assert( visualizable != null )
}

object Image{
  type Visualizable = BufferedImage
  type Homography = Homography2D_F64
  
  implicit def toOps(v:Visualizable) = new VisualizableOps(v)
  
  def apply(visualizable: Visualizable, label: String = "Default label") = new Image(visualizable, label )
  def unapply( img: Image ) = img.visualizable match{
    case null => None
    case v => new Some( img.visualizable, img.label )
  }
}


class ImgAndPatterns( val original: Visualizable, val patterns: Seq[Visualizable], val homographies: Seq[Homography], label: String )
  extends Image(original,label)

object ImgAndPatterns{
  def apply( original: Visualizable, patterns: Seq[Visualizable], homographies: Seq[Homography], label: String="With patterns" ) = new ImgAndPatterns(original, patterns, homographies, label)
  def unapply( img: ImgAndPatterns ) = img.visualizable match{
    case null => None
    case v => new Some( (img.visualizable, img.patterns, img.homographies, img.label) )
  }
}

case class ImgAndPattern( override val original: Visualizable, pattern: Visualizable, homography: Homography, override val label: String )
     extends ImgAndPatterns(original, Seq(pattern), Seq(homography), label )


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
  
  def drawMark( points: java.awt.Point* ) = {
    val ret = new BufferedImage( v.getWidth, v.getHeight, BufferedImage.TYPE_INT_RGB )
    val s = 6
    val g = ret.getGraphics
    g.drawImage(v,0,0,null)
    for( pi <- points.zipWithIndex ; i <- 0 to 4 ){
      val p = pi._1
      g.setColor( if(i%2 == 0 ) WHITE else BLACK )
      g.drawOval( p.x-s/2-i, p.y-s/2-i, s+i*2, s+i*2)
    
      val label = pi._2.toString
      g.setColor(Color.WHITE)
      (-1 to 1).foreach(dx =>
        (-1 to 1).foreach(dy => g.drawString(label, p.x + dx, p.y + dy)
        )
      )
      g.setColor(Color.BLACK)
      g.drawString(label, p.x, p.y)
    }
    
    ret
  }
}
