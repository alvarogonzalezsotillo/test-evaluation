package evaluation.gui.world

import evaluation.engine.Geom.{Point, Coord, Rect}
import Cursor._
import evaluation.gui.world.awt.AWTDHandle
import evaluation.gui.world.ViewWorldCoordinates.DPoint

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 16/02/14
 * Time: 12:55
 * To change this template use File | Settings | File Templates.
 */
trait DHandle extends Drawable{

  def size : Coord
  def cursor : Cursor

  private var _box = Rect(-size/2,-size/2,size,size)
  def box = _box

  override def cursor(p: DPoint): Cursor = cursor

  private def adjustPoint( p: DPoint ) = cursor match{
    case ResizeCursor => p
    case NormalCursor => p
    case ResizeSNCursor => DPoint(box.center.x,p.y)
    case ResizeEWCursor => DPoint(p.x,box.center.y)
    case _ => Point(0,0)
  }

  def moveCenter(to: DPoint) = _box = _box.moveCenter(adjustPoint(to))
}

object DHandle{
  def apply( size: Coord, cursor: Cursor) = new AWTDHandle(size,cursor)
}
