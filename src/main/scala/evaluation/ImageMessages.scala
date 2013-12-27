package evaluation

import scala.actors.Actor
import java.awt.Image

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 27/12/13
 * Time: 22:32
 * To change this template use File | Settings | File Templates.
 */
object ImageMessages{
  case class GetLastImage( requester: Actor )
  case class LastImage( image: Image )
}
