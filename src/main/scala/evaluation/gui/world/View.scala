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

  implicit val self = this

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

  type MouseListener = (MouseEvent) => Unit

  private var _mouseListeners = Set[MouseListener]()

  def +=(l: MouseListener) = _mouseListeners = _mouseListeners + l

  def -=(l: MouseListener) = _mouseListeners = _mouseListeners - l

  def invokeMouseEvent( me: MouseEvent ) =  _mouseListeners.foreach(_(me))

}


