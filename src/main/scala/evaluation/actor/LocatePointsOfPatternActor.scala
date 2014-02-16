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


object LocatePointsOfPatternActor{

  def apply( corners: Boolean, stitchImageActor: Actor*) = {

    def paintCenterOfPatterns( images: IndexedSeq[Img] ) = {

      val (originalVisualizable,label) = images.find( _.isInstanceOf[ImgAndPattern] ) match{
        case Some(Image(vo,l)) => (vo,l)
        case None => (null,null)
      }
      
      val points = images.filter( _.isInstanceOf[ImgAndPattern] ).map{
        case ImgAndPattern(vo,vp,h,label) if( vo != null  && vp != null && h != null ) =>

          if( !corners )
            IndexedSeq( Stitcher.transformPoint(vp.getWidth/2,vp.getHeight/2,h) )
          else
            IndexedSeq( Stitcher.transformPoint(0,vp.getHeight,h),
                        Stitcher.transformPoint(vp.getWidth,vp.getHeight,h),
                        Stitcher.transformPoint(vp.getWidth,0,h),
                        Stitcher.transformPoint(0,0,h) )
      }.flatten

      val msg = if( corners ) " with corners" else " with centers"

      if( originalVisualizable != null )
    	  Image( originalVisualizable.drawMark( points :_* ), label + msg   )
      else
        NoImg
    }

    new ProcessImagesActor( stitchImageActor.toIndexedSeq , paintCenterOfPatterns )
  }
}
