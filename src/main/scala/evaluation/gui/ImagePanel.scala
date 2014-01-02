package evaluation.gui

import javax.swing.JPanel
import java.awt.{Color, RenderingHints, Graphics2D, Graphics}
import scala.actors.Actor
import evaluation.actor.ImagePanelActor
import evaluation.engine.Img
import java.awt.image.AffineTransformOp
import java.awt.geom.AffineTransform

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 27/12/13
 * Time: 22:28
 * To change this template use File | Settings | File Templates.
 */
object ImagePanel {
  def apply(imageActor: Actor, label: String ) = {
    val ip = new ImagePanel
    ip.label = label
    val ipa = new ImagePanelActor(imageActor, ip)
    ip
  }

  def apply(image: Img, label: String ) = {
    val ip = new ImagePanel
    ip.label = label
    ip.image = image
    ip
  }


}

class ImagePanel() extends JPanel {


  private var _image: Img = null
  private var _label = "Change label"

  def image: Img = _image
  def image_=(n: Img) {
    _image = n
    repaint()
  }

  def label = _label
  def label_=(l: String){
    _label =l
    repaint()
  }


  def img() = _image

  override def paint(g: Graphics) {
    val size = getSize()


    def paintImage {
      if (image != null && image.visualizable != null) {
        val g2d = g.create().asInstanceOf[Graphics2D]
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        val img = image.visualizable
        val af = AffineTransform.getScaleInstance(size.getWidth/img.getWidth, size.getHeight/img.getHeight)
        g2d.transform(af)
        g2d.drawImage(img, 0, 0, null)
        g2d.dispose()
      }
    }
    def paintLabel {
      val margin = 5
      g.setColor(Color.WHITE)
      (-1 to 1).foreach( dx =>
        (-1 to 1).foreach( dy => g.drawString(label,margin+dx, size.height - margin + dy)
        )
      )
      g.setColor(Color.BLACK)
      g.drawString(label,margin, size.height-margin)
    }

    paintImage
    paintLabel
  }
}
