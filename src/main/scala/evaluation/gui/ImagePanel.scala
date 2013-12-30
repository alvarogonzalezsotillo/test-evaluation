package evaluation.gui

import javax.swing.JPanel
import java.awt.Image
import java.awt.Graphics
import scala.actors.Actor
import evaluation.actor.ImagePanelActor

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 27/12/13
 * Time: 22:28
 * To change this template use File | Settings | File Templates.
 */
object ImagePanel {
  def apply(imageActor: Actor) = {
    val ip = new ImagePanel
    val ipa = new ImagePanelActor(imageActor, ip)
    ip
  }
}

class ImagePanel() extends JPanel {

  private var kk = 3

  private var _image: Image = null

  def image_=(i: Image) {
    _image = i
    repaint()
  }



  def image() = _image

  override def paint(g: Graphics) {
    if (image != null) {
      val size = getSize()
      g.drawImage(image, 0, 0, size.width, size.height, null)
    }
    else {
      super.paint(g)
    }
  }
}
