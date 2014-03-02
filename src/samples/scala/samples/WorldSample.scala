package samples

import javax.swing.JFrame
import evaluation.gui.world.{ViewWorldCoordinates, MouseClicked, DHandle, World}
import evaluation.gui.world.awt.{AWTView, AWTDHandle}
import evaluation.gui.world.Cursor._
import evaluation.gui.world.ViewWorldCoordinates._

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 2/03/14
 * Time: 15:30
 * To change this template use File | Settings | File Templates.
 */
object WorldSample extends App{

  val frame = new JFrame( "World Sample")

  val world = new World
  val h = DHandle(10,NormalCursor)
  world.add( h )

  val view = new AWTView
  view.drawable = world

  view += { m: MouseClicked =>
    println( m )
    h.moveCenter( m.p )
    view.reDraw
  }

  frame.add( view )

  frame.setVisible( true )
  
}
