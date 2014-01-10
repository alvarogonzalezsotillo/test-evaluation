package evaluation.actor

import scala.actors.Actor
import java.awt.Image
import java.awt.image.BufferedImage
import boofcv.struct.image.{MultiSpectral, ImageFloat32}
import boofcv.core.image.ConvertBufferedImage
import boofcv.gui.image.VisualizeImageData
import evaluation.engine.Img

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 27/12/13
 * Time: 22:32
 * To change this template use File | Settings | File Templates.
 */

object ImageMessages {

  type Time = Long

  val noTime : Time = -1
  
  class Msg( val time: Time )
  
  object Msg{
    def unapply( msg: Msg ) = Some(msg.time)
  }

  case class GetImage(requester: Actor, lastTime: Time ) extends Msg(lastTime)

  case class LastImage(sender: Actor, image: Img, generatedTime: Time) extends Msg(generatedTime)

}
