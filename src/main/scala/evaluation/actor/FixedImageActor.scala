package evaluation.actor

import scala.actors.Actor
import evaluation.actor.ImageMessages.{LastImage, GetImage, Img}
import evaluation.Log

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 28/12/13
 * Time: 0:14
 * To change this template use File | Settings | File Templates.
 */
class FixedImageActor(image: Img, continuous: Boolean = false) extends Actor {

  var time = System.currentTimeMillis()

  def act() {

    loop {
      receive {
        case GetImage(requester,lastTime) =>
          Log( s"$this: GetImage($requester,$lastTime)  time:$time")
          if( continuous ){
            time = System.currentTimeMillis();
          }
          if( lastTime < time ){
            requester ! LastImage(image,time)
          }
      }
    }
  }

  start()

}
