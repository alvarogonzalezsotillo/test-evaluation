package evaluation.actor

import evaluation.engine.{Img, ImgAndPattern, Stitcher}
import evaluation.actor.ImageMessages.{GetImage, LastImage, Time}
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

    def paintCenterOfPattern( images: IndexedSeq[Img]) : Img = {
      val img = images(0)
      if( img == null ){
        null
      }
      else img match{
        case ImgAndPattern(v,vp,h) if( v != null  && vp != null && h != null ) =>
          Log( "Hay que pintar el punto del centro de acuerdo a la homografia" )
          Img(v)
        case Img(v) =>
          Log( "No hay homografía que pintar" )
          null
      }
      
    }

    new ProcessImagesActor(IndexedSeq(stitchImageActor), paintCenterOfPattern )
  }
}
