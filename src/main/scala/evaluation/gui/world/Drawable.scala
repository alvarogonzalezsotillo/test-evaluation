package evaluation.gui.world

import evaluation.engine.Geom._
import evaluation.gui.world.Cursor._

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 16/02/14
 * Time: 12:46
 * To change this template use File | Settings | File Templates.
 */
trait Drawable {

  abstract class DPoint(x:Coord,y:Coord) extends Point(x,y)

  object DPoint{
    class ConcreteDPoint(x:Coord,y:Coord) extends DPoint(x,y)
    def apply(x:Coord,y:Coord) = new ConcreteDPoint(x,y)
    def apply(x:Coord,y:Coord,v:View) = ???
  }

  def box : Rect
  def cursor( p: Point ) : Cursor
  def moveCenter( delta: Point )
  def inside( p: Point ) = box.inside(p)
  def draw( brush: Brush )
}
