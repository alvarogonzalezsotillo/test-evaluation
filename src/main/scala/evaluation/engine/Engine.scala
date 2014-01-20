package evaluation.engine

import scala.actors.Actor

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 1/01/14
 * Time: 19:51
 * To change this template use File | Settings | File Templates.
 */
trait Engine {
  def imageActors : IndexedSeq[Actor]
}

object Engine{
  def apply(pattern: Img) = StitchEngine(pattern)
  def apply(pattern: Img, imageActor: Actor) = StitchEngine(pattern, imageActor)
}
