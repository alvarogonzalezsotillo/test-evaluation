package evaluation.actor

import evaluation.engine._
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


object StitchImagesActor{

  def apply( patterns: Seq[Image], imageActor: Actor) = {

    object stitcher{

      private var _stitchers = patterns.map( s => Stitcher.create(s.visualizable) )

      def stitchImages( images: IndexedSeq[Img] ) = {
        images(1) match{
          case Image(i,label) =>
            val ret = _stitchers.map( _.stitch( i, false ) )
            if( ret.forall( _ != null ) ){
              ImgAndPatterns(i, patterns.map( _.visualizable), ret.map( _.homography ) )
            }
            else{
              NoImg
            }
            
          case _ => NoImg  
        }
      }
    }

    new ProcessImagesActor(IndexedSeq(imageActor), stitcher.stitchImages )
  }
}
