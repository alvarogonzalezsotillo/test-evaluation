package evaluation.gui.world

import evaluation.gui.world.ViewWorldCoordinates.VPoint

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 2/03/14
 * Time: 16:33
 * To change this template use File | Settings | File Templates.
 */
sealed class Event()

abstract class MouseEvent( p: VPoint )

case class MouseClicked( p: VPoint ) extends MouseEvent(p)
