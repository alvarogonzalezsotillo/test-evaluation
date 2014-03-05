package evaluation.gui.world

import evaluation.engine.Geom.Rect
import evaluation.gui.world.Cursor._
import com.typesafe.scalalogging.slf4j.Logging

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 24/02/14
 * Time: 12:21
 * To change this template use File | Settings | File Templates.
 */
trait View extends Logging{

  implicit val self = this

  var _transform: Transform = null
  var _drawable: Drawable = null
  var _cursor: Cursor = NormalCursor

  def brush: Brush

  def box: Rect

  def drawable: Drawable = _drawable

  def drawable_=(d: Drawable) = _drawable = d

  def transform: Transform = _transform

  def transform_=(t: Transform) = _transform = t

  def cursor: Cursor = _cursor

  def cursor_=(c: Cursor) = _cursor = c

  def reDraw {
    reDraw(brush)
  }

  def eraseBackground(br:Brush){
    br.setColor("#cccccc")
    br.fillRect(box)
  }

  def reDraw(br: Brush) {
    logger.debug( "reDraw" )
    eraseBackground(br)
    val b = br.transform(transform)
    drawable.draw(b)
  }

  type MouseListener = (MouseEvent) => Unit

  private var _mouseListeners = Set[MouseListener]()

  def +=(l: MouseListener) = _mouseListeners = _mouseListeners + l

  def -=(l: MouseListener) = _mouseListeners = _mouseListeners - l

  def invokeMouseEvent(me: MouseEvent) = _mouseListeners.foreach(_(me))

  this += {
    case MouseMoved(p) =>
      cursor = drawable.cursor(p)
    case _ =>
  }


}


