package samples

import javax.swing.JFrame
import evaluation.gui.world._
import evaluation.gui.world.awt.AWTView
import evaluation.gui.world.Cursor._
import com.typesafe.scalalogging.slf4j.Logging

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 2/03/14
 * Time: 15:30
 * To change this template use File | Settings | File Templates.
 */
object WorldSample extends App with Logging {

  logger.debug( "debug" )
  logger.info( "info" )
  logger.warn( "warn" )
  logger.error( "error" )

  val frame = new JFrame("World Sample")

  val world = new World

  val h = DHandle(10, ResizeEWCursor)
  world.add(h)

  val i = DIcon("./src/testimages/corners/pink_flower_corner.png")
  world.add(i)

  val view = new AWTView
  view.drawable = world

  val att = view += View.moveWithPointerBehaviour( h )

  view += View.moveWithDragBehaviour(i)

  view += {
    case MouseClicked(_) => att.dettach
  }

  frame.add(view)

  frame.setVisible(true)

}
