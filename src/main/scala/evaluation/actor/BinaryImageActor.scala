package evaluation.actor

import scala.actors.Actor
import evaluation.actor.ImageMessages.Img
import evaluation.engine.BinaryImage

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 31/12/13
 * Time: 14:50
 * To change this template use File | Settings | File Templates.
 */
class BinaryImageActor( imageActor: Actor, erodeAndDilate: Boolean = true ) extends ProcessImageActor(imageActor){
  def processImage(image: Img)  = {
    BinaryImage.toBinaryimage(image,erodeAndDilate)
  }
}
