package evaluation

import scala.actors.Actor
import java.awt.Image
import java.awt.image.BufferedImage

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 27/12/13
 * Time: 22:32
 * To change this template use File | Settings | File Templates.
 */
object ImageMessages {

  type Img = BufferedImage

  case class GetLastImage(requester: Actor)

  case class LastImage(image: Img)

}
