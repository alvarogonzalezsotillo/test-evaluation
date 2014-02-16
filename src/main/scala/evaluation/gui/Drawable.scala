package evaluation.gui

import java.awt.Graphics2D
import evaluation.engine.Geom._

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 16/02/14
 * Time: 12:46
 * To change this template use File | Settings | File Templates.
 */
trait Drawable {
  def box : Rect
  def cursor( p: Point ) : Cursor
  def moveCenter( delta: Point )
  def inside( p: Point ) = box.inside(p)
}
