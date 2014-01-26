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


object StitchImageActor{

  def apply(patternActor: Actor, imageActor: Actor) = {

    object stitcher{

      private var _stitcher : Stitcher = null
      private var _lastVisualizable : Image.Visualizable = null

      def stitcher( img: Img ) = {
        img match{
          case Image(v) if v != _lastVisualizable =>
            _stitcher = Stitcher.create(v)
            _lastVisualizable = v
          case _ =>  
        }
        _stitcher
      }

      def stitchImages( images: IndexedSeq[Img] ) = {
        val s = stitcher(images(0))
        images(1) match{
          case Image(i) if s != null =>
            val ret = s.stitch( i, false )
            if( ret != null ){
              ImgAndPattern(i, s.pattern, ret.homography )
            }
            else{
              NoImg
            }
            
          case _ => NoImg  
        }
      }
    }

    new ProcessImagesActor(IndexedSeq(patternActor,imageActor), stitcher.stitchImages )
  }
}
