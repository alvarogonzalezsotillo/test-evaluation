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

  private val binaryImageActor = BinaryImageActor(imageActor, explodeErode)

  private val binaryPatternActor = BinaryImageActor(patternActor, explodeErode)

  private val stitchActor_binary = StitchImageActor(binaryPatternActor,binaryImageActor)
  private val stitchActor = StitchImageActor(patternActor,imageActor)




  val imageActors = IndexedSeq(patternActor, binaryPatternActor, imageActor, binaryImageActor, stitchActor_binary, stitchActor)
}

object BinaryStitchEngine {
  def apply(pattern: Img) = new BinaryStitchEngine(new FixedImageActor(pattern), new CaptureImageActor)
}
