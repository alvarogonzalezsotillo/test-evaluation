package evaluation.gui.world.awt

import evaluation.gui.world.DIcon
import evaluation.engine.Geom.Rect
import javax.imageio.ImageIO
import java.io.{InputStream, FileInputStream, File}
import java.net.URL
import evaluation.engine.Image
import com.typesafe.scalalogging.slf4j.Logging

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 4/03/14
 * Time: 12:18
 * To change this template use File | Settings | File Templates.
 */
class AWTIcon(val imageLocator: String) extends DIcon {

  import AWTIcon._

  lazy val _visualizable = loadImage(imageLocator).get
  lazy val image = Image(_visualizable, imageLocator)

  val imageBox: Rect = {
    val w = 1.0f * _visualizable.getWidth
    val h = 1.0f * _visualizable.getHeight
    Rect(-w / 2, -h / 2, w, h)
  }

}

object AWTIcon extends Logging {

  private def inputStreamFrom(i: String): Option[InputStream] = {
    def fromResource() = tryOption() {
      getClass.getResourceAsStream(i)
    }
    def fromFile() = tryOption() {
      new FileInputStream(new File(i))
    }
    def fromURL() = tryOption() {
      new URL(i).openStream()
    }

    Seq(fromResource _, fromFile _, fromURL _).
      view.
      map(_()).
      find(!_.isEmpty).
      get
  }

  def loadImage(i: String) = tryOption() {
    val in = inputStreamFrom(i).get
    val ret = ImageIO.read(in)
    in.close()
    ret
  }
}
