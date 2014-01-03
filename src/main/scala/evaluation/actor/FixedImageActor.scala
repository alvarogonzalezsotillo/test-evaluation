package evaluation.actor

import scala.actors.Actor
import evaluation.actor.ImageMessages.{LastImage, GetImage}
import evaluation.Log
import evaluation.engine.Img

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 28/12/13
 * Time: 0:14
 * To change this template use File | Settings | File Templates.
 */
class FixedImageActor(image: Img, continuous: Boolean = false) extends Actor {

  var time = System.currentTimeMillis()

  val self = this

  def act() {

    loop {
      receive {
        case GetImage(requester,lastTime) =>
          if( continuous ){
            time = System.currentTimeMillis();
          }
          if( lastTime < time ){
            requester ! LastImage(self, image,time)
          }
      }
    }
  }

  start()

}
