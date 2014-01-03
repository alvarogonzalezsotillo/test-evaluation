package evaluation.engine

import evaluation.actor._
import scala.actors.Actor
import evaluation.Log

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 31/12/13
 * Time: 17:46
 * To change this template use File | Settings | File Templates.
 */
class BinaryStitchEngine(patternActor: Actor, imageActor: Actor) extends Engine {

  val explodeErode = false

  private val binaryImageActor = new BinaryImageActor(imageActor, explodeErode)

  private val binaryPatternActor = new BinaryImageActor(patternActor, explodeErode)

  //private val stitchActor = new StitchImageActor(patternActor,imageActor)


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

  private val stitchActor = new ProcessImagesActor(IndexedSeq(patternActor,imageActor), stitcher.stitchImages )

  val imageActors = IndexedSeq(patternActor, binaryPatternActor, imageActor, binaryImageActor, stitchActor)
}

object BinaryStitchEngine {
  def apply(pattern: Img) = new BinaryStitchEngine(new FixedImageActor(pattern), new CaptureImageActor)
}
