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



  def apply( view: View, world: World, center: DPoint, radius: Coord, edges: Int = 4 ) = {

    val cursor = Cursor.MoveCursor
    val size = 20
    val handles = Array.fill(edges)( DHandle(size, cursor) )
    
    handles.foreach( h => world.add(h) )
    handles.foreach( view += View.moveWithDragBehaviour(_) )
    val angleStep = 2*Math.PI/edges
    for( i <- 0 until edges ){
      val angle = angleStep*i + angleStep/2
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
