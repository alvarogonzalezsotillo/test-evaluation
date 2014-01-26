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


object LocateCenterOfPatternActor{

  def apply(stitchImageActor: Actor) = {

    def paintCenterOfPattern( images: IndexedSeq[Img]) = images(0) match{
      case ImgAndPattern(vo,vp,h) if( vo != null  && vp != null && h != null ) =>
        val tPoint = Stitcher.transformPoint(vp.getWidth/2,vp.getHeight/2,h)
        val newV = vo.drawMark(tPoint)
        Image(newV)
        
      case Image(v) =>
        Log( "No hay homografía que pintar" )
        NoImg
        
      case NoImg =>
        Log( "No hay imagen" )
        NoImg
    }

    new ProcessImagesActor(IndexedSeq(stitchImageActor), paintCenterOfPattern )
  }
}