package evaluation

import scala.actors.Actor
import evaluation.ImageMessages.{Img, LastImage, GetLastImage}

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 28/12/13
 * Time: 0:14
 * To change this template use File | Settings | File Templates.
 */
class FixedImageActor(image: Img) extends Actor {

  def act() {

    loop {
      receive {
        case GetLastImage(requester) =>
          requester ! LastImage(image)
      }
    }
  }

  start()

}
