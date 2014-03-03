package evaluation.gui.world.awt

import evaluation.gui.world._
import evaluation.engine.Geom.Rect
import java.awt.{Graphics, Canvas, Cursor => AWTCursor}
import evaluation.gui.world.awt.AWTBrush._
import java.awt.event.{MouseAdapter, MouseEvent => AWTMouseEvent}
import evaluation.gui.world.ViewWorldCoordinates.VPoint
import evaluation.gui.world.MouseClicked
import evaluation.gui.world.Cursor._


object AWTView {
  implicit def toVPoint(e: AWTMouseEvent)(implicit v: View) = VPoint(e.getX, e.getY)

  implicit def toAWTCursor(c: Cursor) = c match {
    case ResizeEWCursor => AWTCursor.getPredefinedCursor(AWTCursor.W_RESIZE_CURSOR)
    case ResizeSNCursor => AWTCursor.getPredefinedCursor(AWTCursor.S_RESIZE_CURSOR)
    case _ => AWTCursor.getDefaultCursor
  }

  implicit def toAWTCanvas(v: AWTView) = v._canvas
}

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 2/03/14
 * Time: 15:31
 * To change this template use File | Settings | File Templates.
 */
class AWTView extends View {

  import evaluation.gui.world.awt.AWTView._

  private val _canvas = new Canvas() {
    override def paint(g: Graphics) = reDraw(g)
  }

  def brush = _canvas.getGraphics

  transform = AWTTransform.identity

  override def cursor_=(c: Cursor) = {
    super.cursor_=(c)
    _canvas.setCursor(c)
  }


  def box = {
    val size = _canvas.getSize
    Rect(0, 0, size.width, size.height)
  }

  val _mouseListener = new MouseAdapter() {

    override def mouseClicked(e: AWTMouseEvent) = invokeMouseEvent(MouseClicked(e))

    override def mouseDragged(e: AWTMouseEvent) = invokeMouseEvent(MouseDragged(e))

    override def mouseMoved(e: AWTMouseEvent) = invokeMouseEvent(MouseMoved(e))
  }

  _canvas.addMouseListener(_mouseListener)
  _canvas.addMouseMotionListener(_mouseListener)
}

