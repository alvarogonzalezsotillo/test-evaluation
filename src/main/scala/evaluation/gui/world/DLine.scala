package evaluation.gui.world

import evaluation.engine.Geom.{Rect, Point}
import evaluation.gui.world.awt.AWTLine
import evaluation.gui.world.ViewWorldCoordinates.DPoint

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 17/03/14
 * Time: 10:19
 * To change this template use File | Settings | File Templates.
 */

import Brush._

trait DLine extends Drawable {

  val ini = Prop( DPoint(0,0) )
  val end = Prop( DPoint(0,0) )
  val color = Prop[Color]( "#000000" )


  override def draw(b: Brush) = {
    b.color = color()
    b.drawLine( ini() , end() )
  }

  box.derive( ini, end ){
    Rect( ini(), end() )
  }

  box.listen{
    val c = box().center
    moveCenter( DPoint(c.x,c.y) )
  }

  def moveCenter(to: DPoint): Unit = {
    val currentCenter = box().center
    val delta = to - currentCenter
    val i = ini() + delta
    val e = end() + delta
    ini() = DPoint( i.x, i.y )
    end() = DPoint( e.x, e.y )
  }
}

object DLine {
  def apply(ini: DPoint, end: DPoint) : DLine = new AWTLine(ini, end)

  def apply( ini: Drawable, end: Drawable) : DLine = {
    def computeP(d:Drawable) = {
      val p = d.box().center
      DPoint(p.x,p.y)
    }

    val ret = new AWTLine( computeP(ini), computeP(end) )
    ret.ini.derive( ini.box )( computeP(ini) )
    ret.end.derive( end.box )( computeP(end) )
    ret
  }
}
