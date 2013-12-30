package evaluation.actor

import scala.actors.Actor
import evaluation.gui.ImagePanel
import javax.swing.SwingUtilities
import evaluation.Log
import evaluation.actor.ImageMessages.{LastImage, GetImage}

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 30/12/13
 * Time: 11:29
 * To change this template use File | Settings | File Templates.
 */
class ImagePanelActor(imageActor: Actor, imagePanel: ImagePanel) extends Actor {
  val self = this

  private var _lastImage: LastImage = null

  def postUpdate() = SwingUtilities.invokeLater(new Runnable {
    def run() = {
      imagePanel.image = _lastImage.image
    }
  })


  def act {
    Log("Starting ImagePanelActor")
    imageActor ! GetImage(self, ImageMessages.noTime)

    Log("Sent initial GetlastImage")

    loop {
      receive {
        case li: LastImage =>
          _lastImage = li
          postUpdate()
          imageActor ! GetImage(self, _lastImage.time)

        case anything =>
          Log(s"Unexpected: $anything")
      }
    }
  }

  start()
}
