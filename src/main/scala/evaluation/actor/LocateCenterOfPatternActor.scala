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

  def apply(stitchImageActor: Actor*) = {

    def paintCenterOfPatterns( images: IndexedSeq[Img]) = {

      val originalVisualizable = images.find( _.isInstanceOf[ImgAndPattern] ) match{
        case Some(ImgAndPattern(vo,_,_)) => vo
        case None => null
      }
      
      val points = images.map( _ match {
        case ImgAndPattern(vo,vp,h) if( vo != null  && vp != null && h != null ) =>
          Stitcher.transformPoint(vp.getWidth/2,vp.getHeight/2,h)
       
        case Image(v) =>
          Log( "No hay homografia que pintar" )
          null
        
        case NoImg =>
          Log( "No hay imagen" )
          null
      } ).filter( _ != null )
      
      if( originalVisualizable != null )
    	Image( originalVisualizable.drawMark( points :_* ) )
      else
        NoImg
    }

    new ProcessImagesActor( stitchImageActor.toIndexedSeq , paintCenterOfPatterns )
  }
}
