package evaluation.gui.world.awt

import evaluation.gui.world.View
import evaluation.engine.Geom.Rect
import java.awt.{Graphics, Canvas}
import evaluation.gui.world.awt.AWTBrush._
import java.awt.event.{MouseAdapter, MouseEvent}
import evaluation.gui.world.ViewWorldCoordinates.VPoint

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 2/03/14
 * Time: 15:31
 * To change this template use File | Settings | File Templates.
 */
class AWTView extends Canvas with View {
  val self = this

  def brush = getGraphics

  transform = AWTTransform.identity


  def box = {
    val size = getSize
    Rect(0, 0, size.width, size.height)
  }

  override def paint(g: Graphics) = reDraw(g)

  addMouseListener(new MouseAdapter() {
    override def mouseClicked(e: MouseEvent) {
      val vp = VPoint(e.getX, e.getY)(self)
      invokeMouseClicked(vp)
    }
  })
}
