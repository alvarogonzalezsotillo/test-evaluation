package evaluation.gui

import javax.swing.JPanel
import java.awt.{Color, Graphics2D, Graphics}
import java.awt.RenderingHints._
import scala.actors.Actor
import evaluation.actor.ImagePanelActor
import evaluation.engine._
import java.awt.geom.AffineTransform

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

  def apply(image: Img, label: String) = {
    val ip = new ImagePanel
    ip.image = image
    ip
  }


}

class ImagePanel() extends JPanel {
  private var _image: Img = null

  type Listener = () => Unit

  private var _listeners = Set[Listener]()

  def image: Img = _image

  def image_=(n: Img) {
    _image = n
    _listeners.foreach(_())
    repaint()
  }

  def addListener(l: Listener) = _listeners = _listeners + l

  def removeListener(l: Listener) = _listeners = _listeners - l

  def +(l: Listener) = addListener(l)

  def -(l: Listener) = removeListener(l)


  private val hints = List(
    (KEY_ANTIALIASING, VALUE_ANTIALIAS_ON),
    (KEY_RENDERING, VALUE_RENDER_QUALITY),
    (KEY_INTERPOLATION, VALUE_INTERPOLATION_BICUBIC)
  )

  override def paint(g: Graphics) {
    val size = getSize()


    def paintImage {
      image match {
        case Image(v, _) =>
          val g2d = g.create().asInstanceOf[Graphics2D]
          hints.foreach(p => g2d.setRenderingHint(p._1, p._2))
          val af = AffineTransform.getScaleInstance(size.getWidth / v.getWidth, size.getHeight / v.getHeight)
          g2d.transform(af)
          g2d.drawImage(v, 0, 0, null)
          g2d.dispose()
        case _ =>
          g.setColor(Color.BLACK)
          val w = size.getWidth.asInstanceOf[Int]
          val h = size.getHeight.asInstanceOf[Int]
          g.fillRect(0, 0, w, h)
          g.setColor(Color.WHITE)
          g.drawLine(0, 0, w, h)
          g.drawLine(w, 0, 0, h)
      }
    }

    def paintLabel(label: String) {
      val margin = 5
      g.setColor(Color.WHITE)
      (-1 to 1).foreach(dx =>
        (-1 to 1).foreach(dy => g.drawString(label, margin + dx, size.height - margin + dy)
        )
      )
      g.setColor(Color.BLACK)
      g.drawString(label, margin, size.height - margin)
    }

    paintImage
    paintLabel( image match{
      case Image(_,label) => label
      case _ => "No image"
    })
  }
}
