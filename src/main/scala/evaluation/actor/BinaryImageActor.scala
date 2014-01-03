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

object BinaryImageActor {

  def binarizer(erodeAndDilate: Boolean)(images: IndexedSeq[Img]) = {
    if (images(0) != null && images(0).visualizable != null) {
      Img(BinaryImage.toBinaryimage(images(0).visualizable, erodeAndDilate))
    }
    else {
      null
    }
  }

  def apply(imageActor: Actor, erodeAndDilate: Boolean = true) = {
    new ProcessImagesActor(IndexedSeq(imageActor), binarizer(erodeAndDilate))
  }
}
