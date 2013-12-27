package evaluation

import javax.swing.{SwingUtilities, JPanel}
import java.awt.Image
import java.awt.Graphics
import scala.actors.Actor
import evaluation.ImageMessages.{Img, LastImage, GetLastImage}

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 27/12/13
 * Time: 22:28
 * To change this template use File | Settings | File Templates.
 */
class ImagePanel(imageActor: Actor) extends JPanel {

  private var _lastImage: Img = null

  private var actor = new Actor {
    val self = this

    def postUpdate() = SwingUtilities.invokeLater(new Runnable {
      def run() = {
        Log("Repintando " + System.currentTimeMillis )
        repaint()
      }
    })


    def act {
      Log("Starting actor of ImagePanel")
      imageActor ! GetLastImage(self)

      Log("Sent initial GetlastImage")

      loop {
        receive {
          case LastImage(image) =>
            _lastImage = image
            postUpdate()
            imageActor ! GetLastImage(self)

          case anything =>
            Log( s"Unexpected: $anything" )
        }
      }
    }

    start()
  }

  override def paint(g: Graphics) {
    if (_lastImage != null) {
      val size = getSize()
      g.drawImage(_lastImage, 0, 0, size.width, size.height, null)
    }
  }
}
