package evaluation.actor

import evaluation.engine.{Img, Stitcher}
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
      private var _lastVisualizable : Img.Visualizable = null

      def stitcher( img: Img ) = {
        val visualizable = img.visualizable
        if( visualizable != null && visualizable == _lastVisualizable ){
          _stitcher = Stitcher.create(visualizable)
        }
        _lastVisualizable = visualizable
        _stitcher
      }

      def stitchImages( images: IndexedSeq[Img] ) = {
        if( images.exists(_==null) ){
          null
        }
        else{
          val ret = stitcher(images(0)).stitch( images(1).visualizable )
          Img(ret)
        }
      }
    }

    new ProcessImagesActor(IndexedSeq(patternActor,imageActor), stitcher.stitchImages )
  }
}
