package evaluation.gui.world

import evaluation.engine.Geom._
import evaluation.gui.world.Cursor._
import evaluation.gui.world.ViewWorldCoordinates.DPoint


/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 16/02/14
 * Time: 12:46
 * To change this template use File | Settings | File Templates.
 */
trait Drawable {

  var _container : Container = null

  def container = _container
  def container_=( c: Container ) = _container = c


  val box = Prop( Rect(0,0,0,0) )
  def cursor( p: DPoint ) : Cursor = NormalCursor
  def moveCenter( to: DPoint )
  def inside( p: DPoint ) = box().inside(p)
  def draw( brush: Brush )

}