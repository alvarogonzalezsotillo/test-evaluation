package evaluation.actor

import scala.actors.Actor
import evaluation.engine.{Img, Image, NoImg, BinaryImage}

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 31/12/13
 * Time: 14:50
 * To change this template use File | Settings | File Templates.
 */

object BinaryImageActor {

  def binarizer(erodeAndDilate: Boolean)(images: IndexedSeq[Img]) = images(0) match{
    case Image(v) => Image(BinaryImage.toBinaryimage(v, erodeAndDilate))
    case _ => NoImg
  }

  def apply(imageActor: Actor, erodeAndDilate: Boolean = true) = {
    new ProcessImagesActor(IndexedSeq(imageActor), binarizer(erodeAndDilate))
  }
}
