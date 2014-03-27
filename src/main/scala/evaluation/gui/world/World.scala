package evaluation.gui.world

import evaluation.engine.Geom.{Point, Rect}
import evaluation.gui.world.Cursor._
import evaluation.gui.world.ViewWorldCoordinates.DPoint

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 24/02/14
 * Time: 11:51
 * To change this template use File | Settings | File Templates.
 */
class World extends Drawable with Container{

  val _drawables = collection.mutable.Set[Drawable]()

  def add( d: Drawable ) = {
    _drawables.add(d)
    d.container = this
    d.box listen { box() = computeBox }
  }
  def remove( d: Drawable ) = {
    _drawables.remove(d)
    d.container = null
    // TODO: REMOVE BBOX LISTENER
  }

  def computeBox : Rect = if( _drawables.size > 1 ){
    val r = _drawables.iterator.next.box()
    _drawables.foldLeft( r )( (r,d) => r + d.box() )
  }
  else{
    Rect(0,0,0,0)
  }

  def drawablesAt( p: DPoint ) = _drawables.filter( _.inside(p) )

  override def cursor(p: DPoint): Cursor = drawablesAt(p) match{
    case d if d.size > 0 => d.iterator.next.cursor(p)
    case _ => super.cursor(p)
  }

  def moveCenter(delta: DPoint) = _drawables.foreach( _.moveCenter(delta) )

  def draw(brush: Brush) = _drawables.foreach( _.draw(brush) )

  def reDraw = container.reDraw
}
