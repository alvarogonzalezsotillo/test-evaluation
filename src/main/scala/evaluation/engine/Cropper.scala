package evaluation.engine

import evaluation.engine.Image._
import evaluation.engine.Geom._

object Cropper{

  def apply( img: Img, factor: Int ) : Seq[Seq[Img]]= img match{
    case Image(v,label) => crop(v,factor)
    case NoImg => Seq(Seq(NoImg))
  }
  
  private def crop( v: Visualizable, factor: Int ): Seq[Seq[Img]] = {
  
    assert( factor > 1 )
  
    val vw = v.getWidth
    val vh = v.getHeight
    
    val w = vw/factor
    val h = vh/factor
    
    for( r <- 0 until factor*2-1 ) yield {
      for( c <- 0 until factor*2-1 ) yield{
        val x = c * w/2
        val y = r * h/2
        val i = v.crop( Rect(x,y,w,h) )
        Image(i)
      }
    }
  }
}