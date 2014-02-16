package evaluation.gui

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 16/02/14
 * Time: 12:50
 * To change this template use File | Settings | File Templates.
 */
sealed class Cursor()
case class NormalCursor() extends Cursor
case class ResizeEWCursor() extends Cursor
case class ResizeSNCursor() extends Cursor
case class ResizeCursor() extends Cursor
