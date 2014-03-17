package evaluation.gui.world

import evaluation.engine.Geom.{Rect, Point}
import evaluation.gui.world.ViewWorldCoordinates.DPoint
import evaluation.engine.Image
import evaluation.gui.world.awt.AWTIcon

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 4/03/14
 * Time: 12:15
 * To change this template use File | Settings | File Templates.
 */
trait DIcon extends Drawable {

  val center = Prop(DPoint(0,0))

  override def moveCenter( p: DPoint ) = center() = p

  def imageBox : Rect

  box.derive( center ){
    imageBox + center()
  }

  def image : Image

  override def draw( b: Brush ) = b.drawImg( image, box() )
}

object DIcon{
  def apply( locator: String ) = new AWTIcon(locator)
}
