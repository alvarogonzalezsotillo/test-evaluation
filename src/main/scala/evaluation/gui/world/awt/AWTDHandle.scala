package evaluation.gui.world.awt

import evaluation.gui.world.{Brush, DHandle}
import evaluation.engine.Geom._
import evaluation.gui.world.Cursor.Cursor

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 26/02/14
 * Time: 10:41
 * To change this template use File | Settings | File Templates.
 */
class AWTDHandle(val size: Coord, val cursor: Cursor) extends DHandle {
  def draw(brush: Brush) = {
    brush.color = color()
    brush.drawRect(box())
    brush.drawText(cursor.toString, box().center)
  }
}
