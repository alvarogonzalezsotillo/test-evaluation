package evaluation.gui.world.awt

import evaluation.gui.world.DLine
import evaluation.engine.Geom.{Rect, Point}
import evaluation.gui.world.ViewWorldCoordinates.DPoint
import evaluation.gui.world.Brush._

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 17/03/14
 * Time: 10:27
 * To change this template use File | Settings | File Templates.
 */


class AWTLine(iniP: DPoint, endP: DPoint) extends DLine {
  ini() = iniP
  end() = endP
}
