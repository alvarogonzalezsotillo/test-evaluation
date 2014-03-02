package evaluation.gui.world

import evaluation.engine.Geom.Rect
import evaluation.gui.world.ViewWorldCoordinates.VPoint

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 24/02/14
 * Time: 12:21
 * To change this template use File | Settings | File Templates.
 */
trait View {

  var _transform: Transform = null
  var _drawable: Drawable = null

  def brush: Brush

  def box: Rect

  def drawable: Drawable = _drawable

  def drawable_=(d: Drawable) = _drawable = d

  def transform: Transform = _transform

  def transform_=(t: Transform) = _transform = t

  def reDraw{
    reDraw(brush)
  }

  def reDraw(br: Brush) {
    val b = br.transform(transform)
    println( "Redraw" )
    drawable.draw(b)
  }

  type MouseClickedListener = (MouseClicked) => Unit

  private var _mouseClickedListener = Set[MouseClickedListener]()

  def +=(l: MouseClickedListener) = _mouseClickedListener = _mouseClickedListener + l

  def -=(l: MouseClickedListener) = _mouseClickedListener = _mouseClickedListener - l

  def invokeMouseClicked( p: VPoint ) = {
    val mc = MouseClicked(p)
    _mouseClickedListener.foreach(_(mc))
  }
}


