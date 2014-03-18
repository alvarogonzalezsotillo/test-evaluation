package evaluation.gui.world

import evaluation.gui.world.ViewWorldCoordinates.DPoint
import evaluation.engine.Geom.Coord

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 18/03/14
 * Time: 15:25
 * To change this template use File | Settings | File Templates.
 */
object PoligonOfHandles{



  def apply( view: View, world: World, center: DPoint, radius: Coord, edges: Int = 5 ) = {

    val cursor = Cursor.MoveCursor
    val size = 4
    val handles = Array.fill(edges)( DHandle(size,cursor) )
    handles.foreach( h => world.add(h) )
    handles.foreach( view += View.moveWithDragBehaviour(_) )
    for( i <- 0 until edges ){
      val angle = 2*Math.PI*i/edges
      val x = radius*Math.cos(angle) + center.x
      val y = radius*Math.sin(angle) + center.y
      handles(i).moveCenter( DPoint(x,y) )
    }

    val lines = Array.tabulate(edges){ i =>
      DLine( handles(i), handles((i+1)%edges) )
    }
    lines.foreach( l => world.add(l) )

    lines ++ handles
  }

}
