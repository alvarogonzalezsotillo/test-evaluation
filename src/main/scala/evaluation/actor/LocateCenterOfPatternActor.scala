package evaluation.actor

import evaluation.engine._
import evaluation.actor.ImageMessages._
import scala.actors.Actor
import evaluation.Log

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
      case ImgAndPattern(v,vp,h) if( v != null  && vp != null && h != null ) =>
        Log( "Hay que pintar el punto del centro de acuerdo a la homografia" )
        Image(v)
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
