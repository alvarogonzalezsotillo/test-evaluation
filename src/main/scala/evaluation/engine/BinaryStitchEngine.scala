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

  private val stitchActor = StitchImageActor(patternActor,imageActor)




  val imageActors = IndexedSeq(patternActor, binaryPatternActor, imageActor, binaryImageActor, stitchActor)
}

object BinaryStitchEngine {
  def apply(pattern: Img) = new BinaryStitchEngine(new FixedImageActor(pattern), new CaptureImageActor)
}
