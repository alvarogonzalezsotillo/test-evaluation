package evaluation.gui.world

import evaluation.engine.Geom.{Point, Coord, Rect}
import Cursor._
import evaluation.gui.world.ViewWorldCoordinates.DPoint
import com.typesafe.scalalogging.slf4j.Logging

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 16/02/14
 * Time: 12:55
 * To change this template use File | Settings | File Templates.
 */
class DHandle extends Drawable with Logging{

  import Brush._

  val size = Prop[Coord](4)
  val cursor = Prop(NormalCursor)
  
  override def cursorAt(p: DPoint) = cursor()

  
  val color : Prop[Color] = Prop( "#ffffff")
  redrawableProperty(color)
  

  private def adjustPoint( p: DPoint ) = cursor() match{
    case ResizeSNCursor => DPoint(box().center.x,p.y)
    case ResizeEWCursor => DPoint(p.x,box().center.y)
    case _ => p
  }

  box.derive(size){
    val center = box().center
    Rect(center.x-size()/2,center.y-size()/2,size(),size())
  }

  def draw(brush: Brush) = {
    logger.debug( s"draw: box:${box()} size:${size()} color:${color()}" )
    brush.color = color()
    brush.drawRect(box())
  }

  def moveCenter(to: DPoint) =  box() = box().moveCenter(adjustPoint(to))
}

object DHandle{
  def apply( size: Coord, cursor: Cursor ) = {
    val h = new DHandle
    h.size() = size
    h.cursor() = cursor
    h
  }
}
