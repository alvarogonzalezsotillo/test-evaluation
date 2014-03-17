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
trait View extends Logging with Container {

  import View._

  implicit val self = this

  var _transform: Transform = null
  var _drawable: Drawable = null
  var _cursor: Cursor = NormalCursor

  def brush: Brush

  def box: Rect

  def drawable: Drawable = _drawable

  def drawable_=(d: Drawable) = {
    if (_drawable != null) {
      _drawable.container = null
    }
    _drawable = d
    if (_drawable != null) {
      _drawable.container = this
    }
  }

  def transform: Transform = _transform

  def transform_=(t: Transform) = _transform = t

  def cursor: Cursor = _cursor

  def cursor_=(c: Cursor) = _cursor = c

  def reDraw {
    reDraw(brush)
  }

  def eraseBackground(br: Brush) {
    br.color = "#cccccc"
    br.fillRect(box)
  }

  def reDraw(br: Brush) {
    logger.debug("reDraw")
    eraseBackground(br)
    val b = br.transform(transform)
    drawable.draw(b)
  }

  private var _mouseListeners = Set[MouseListener]()

  def +=(l: MouseListener) = {
    _mouseListeners = _mouseListeners + l
    new ListenerAttachment(this,l)
  }

  def -=(l: MouseListener) = _mouseListeners = _mouseListeners - l

  def invokeMouseEvent(me: MouseEvent) = _mouseListeners.foreach{ l =>
    if( l.isDefinedAt(me) ) l(me)
  }

  this += {
    case MouseMoved(p) =>
      cursor = drawable.cursor(p)
    case _ =>
  }


}


object View {
  type MouseListener = PartialFunction[MouseEvent,Unit]

  class ListenerAttachment( v:View, l:MouseListener){
    def attach = v += l
    def dettach = v -= l
  }

  def moveWithPointerBehaviour(drawables: Drawable*): MouseListener = {
    case MouseEvent(p) =>
      drawables.foreach(_.moveCenter(p))
      drawables.head.container.reDraw
  }

  def moveWithDragBehaviour( drawable : Drawable ) : MouseListener = {
    var dragging = false

    {
      case MouseDown(p) => if( drawable.inside(p) ) dragging = true
      case MouseUp(_) => dragging = false
      case MouseDragged(p) => if( dragging ) drawable.moveCenter(p)
    }
  }



}