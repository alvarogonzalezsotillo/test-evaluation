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
class StitchEngine(patternActor: Actor, imageActor: Actor) extends Engine {

  private val stitchActor = StitchImageActor(patternActor,imageActor)
  
  private val showStitchActor = LocateCenterOfPatternActor(stitchActor)

  val imageActors = IndexedSeq(patternActor, imageActor, stitchActor, showStitchActor)
}

object StitchEngine {
  def apply(pattern: Img) = new StitchEngine(new FixedImageActor(pattern), new CaptureImageActor)
  def apply(pattern: Img, imageActor: Actor) = new StitchEngine(new FixedImageActor(pattern), imageActor)
}
