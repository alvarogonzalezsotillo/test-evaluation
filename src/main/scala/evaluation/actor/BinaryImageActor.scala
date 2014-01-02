package evaluation.actor

import scala.actors.Actor
import evaluation.engine.{Img, BinaryImage}

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 31/12/13
 * Time: 14:50
 * To change this template use File | Settings | File Templates.
 */
class BinaryImageActor( imageActor: Actor, erodeAndDilate: Boolean = true ) extends ProcessImageActor(imageActor){
  def processImage(image: Img)  = {
    val ret = BinaryImage.toBinaryimage(image.visualizable,erodeAndDilate)
    Img(ret)
  }
}
