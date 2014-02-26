package evaluation.gui.world.awt

import evaluation.gui.world.{Brush, DHandle}
import evaluation.engine.Geom._
import evaluation.gui.world.Cursor._

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 26/02/14
 * Time: 10:41
 * To change this template use File | Settings | File Templates.
 */
class AWTDHandle(size: Coord, cursor: Cursor) extends DHandle(size,cursor){
  def draw(brush: Brush) = {
    val b = box
    brush.drawRect(box)
    brush.drawText(cursor.toString,box.center)
  }
}
