package evaluation.actor

import evaluation.engine._
import evaluation.engine.Image._
import evaluation.actor.ImageMessages._
import scala.actors.Actor
import evaluation.Log
import java.awt.Point

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 27/12/13
 * Time: 23:58
 * To change this template use File | Settings | File Templates.
 */


object ComputeStitchedImageActor{

  def apply(stitchImageActor: Actor) = {

    def computeStitchedImage( images: IndexedSeq[Img]) = images(0) match{
      case ImgAndPattern(vo,vp,h,label) if( vo != null  && vp != null && h != null ) =>
        val newV = Stitcher.renderStitching(vp,vo,h)
        Image(newV,label + " stitched")
        
      case Image(v) =>
        Log( "No hay homografía que pintar" )
        NoImg
        
      case NoImg =>
        Log( "No hay imagen" )
        NoImg
    }

    new ProcessImagesActor(IndexedSeq(stitchImageActor), computeStitchedImage )
  }
}
