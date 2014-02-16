package evaluation.gui

import evaluation.engine.Geom.{Point, Coord, Rect}

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 16/02/14
 * Time: 12:55
 * To change this template use File | Settings | File Templates.
 */
class Handle( size: Coord, cursor: Cursor ) extends Drawable{
  private var _box = Rect(0,0,size,size)
  def box = _box

  def cursor(p: Point) = cursor

  def moveCenter(delta: Point) = _box.moveCenter(delta)
}
