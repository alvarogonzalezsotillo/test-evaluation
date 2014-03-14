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

abstract class MouseEvent( val p: VPoint )

object MouseEvent{
  def unapply( me: MouseEvent ) = Some(me.p)
}

case class MouseClicked( override val p: VPoint ) extends MouseEvent(p)

case class MouseDragged( override val p: VPoint ) extends MouseEvent(p)

case class MouseMoved( override val p: VPoint ) extends MouseEvent(p)

case class MouseDown( override val p: VPoint ) extends MouseEvent(p)

case class MouseUp( override val p: VPoint ) extends MouseEvent(p)
