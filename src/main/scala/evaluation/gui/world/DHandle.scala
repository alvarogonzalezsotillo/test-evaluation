package evaluation.gui.world

import evaluation.engine.Geom.{Point, Coord, Rect}
import evaluation.gui.world.Drawable
import Cursor._

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 16/02/14
 * Time: 12:55
 * To change this template use File | Settings | File Templates.
 */
abstract class DHandle( size: Coord, cursor: Cursor ) extends Drawable{
  private var _box = Rect(0,0,size,size)
  def box = _box

  def cursor(p: Point) = cursor

  private def adjustDelta( delta: Point ) = cursor match{
    case ResizeCursor => delta
    case ResizeSNCursor => Point(box.center.x,delta.y)
    case ResizeEWCursor => Point(delta.x,box.center.y)
    case _ => Point(0,0)
  }

  def moveCenter(delta: Point) = _box = _box.moveCenter(adjustDelta(delta))


}

object DHandle{
  def apply( size: Coord, cursor: Cursor ) = new AWTDHandle(size,cursor)
}
