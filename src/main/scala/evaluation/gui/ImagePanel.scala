package evaluation.gui

import javax.swing.JPanel
import java.awt.{RenderingHints, Graphics2D, Graphics}
import scala.actors.Actor
import evaluation.actor.ImagePanelActor
import evaluation.actor.ImageMessages.Img

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

  def apply(image: Img) = {
    val ip = new ImagePanel
    ip.image_=(image)
    ip
  }


}

class ImagePanel() extends JPanel {

  private var kk = 3

  private var _image: Img = null

  def image_=(i: Img) {
    _image = i
    repaint()
  }


  def image() = _image

  override def paint(g: Graphics) {
    if (image != null) {
      val size = getSize()
      g.asInstanceOf[Graphics2D].setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
      g.drawImage(image, 0, 0, size.width, size.height, null)
    }
    else {
      super.paint(g)
    }
  }
}
