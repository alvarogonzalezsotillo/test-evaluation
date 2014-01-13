package evaluation.engine

import evaluation.engine.Image._
import evaluation.engine.Geom._

object Cropper{

  def apply( img: Img, factor: Int ) : Seq[Img]= img match{
    case Image(v) => crop(v,factor)
    case NoImg => Seq(NoImg)
  }
  
  private def crop( v: Visualizable, factor: Int ) = {
  
    assert( factor > 1 )
  
    val vw = v.getWidth
    val vh = v.getHeight
    
    val w = vw/factor
    val h = vh/factor
    
    for( c <- 0 to factor*2-1 ; r <- 0 to factor*2-1 ) yield{
      val x = c * w/2
      val y = r * h/2
      val i = v.crop( Rect(x,y,w,h) )
      Image(i)
    }
  }
}