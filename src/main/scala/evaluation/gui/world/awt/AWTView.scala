package evaluation.gui.world.awt

import evaluation.gui.world._
import evaluation.engine.Geom.Rect
import java.awt.{Graphics, Canvas}
import evaluation.gui.world.awt.AWTBrush._
import java.awt.event.{MouseAdapter, MouseEvent => AWTMouseEvent}
import evaluation.gui.world.ViewWorldCoordinates.VPoint
import evaluation.gui.world.MouseClicked

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 2/03/14
 * Time: 15:31
 * To change this template use File | Settings | File Templates.
 */
class AWTView extends Canvas with View {

  def brush = getGraphics

  transform = AWTTransform.identity

  implicit def toVPoint(e: AWTMouseEvent) = VPoint(e.getX, e.getY)


  def box = {
    val size = getSize
    Rect(0, 0, size.width, size.height)
  }

  override def paint(g: Graphics) = reDraw(g)

  val _mouseListener = new MouseAdapter() {

    override def mouseClicked(e: AWTMouseEvent) = invokeMouseEvent(MouseClicked(e))

    override def mouseDragged(e: AWTMouseEvent) = invokeMouseEvent(MouseDragged(e))

    override def mouseMoved(e: AWTMouseEvent) = invokeMouseEvent(MouseMoved(e))
  }

  addMouseListener(_mouseListener)
  addMouseMotionListener(_mouseListener)
}
