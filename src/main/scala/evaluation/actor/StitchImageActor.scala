package evaluation.actor

import evaluation.engine.{Img, Stitcher}
import evaluation.actor.ImageMessages.{GetImage, LastImage, Time}
import scala.actors.Actor

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 27/12/13
 * Time: 23:58
 * To change this template use File | Settings | File Templates.
 */


class StitchImageActor(patternActor: Actor, imageActor: Actor) extends ProcessImageActor(imageActor) {

  var stitcher: Stitcher = null

  class FromPattern extends Actor {
    val self = this
    patternActor ! GetImage(self, ImageMessages.noTime)

    def act() {
      loop {
        receive {
          case LastImage(sender: Actor, image: Img, time: Time) =>
            stitcher = Stitcher.create(image.visualizable)
            patternActor ! GetImage(self, time)
        }
      }
    }

    start()
  }

  new FromPattern

  def processImage(image: Img) = {
    if (stitcher != null) {
      val ret = stitcher.stitch(image.visualizable)
      Img(ret)
    }
    else {
      null
    }
  }

}
