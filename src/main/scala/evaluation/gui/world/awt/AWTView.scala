package evaluation.gui.world.awt

import evaluation.gui.world._
import evaluation.engine.Geom.Rect
import java.awt.{Graphics, Canvas, Cursor => AWTCursor}
import evaluation.gui.world.awt.AWTBrush._
import java.awt.event.{MouseEvent => AWTMouseEvent, ComponentEvent, ComponentAdapter, MouseAdapter}
import evaluation.gui.world.ViewWorldCoordinates.VPoint
import evaluation.gui.world.MouseClicked
import evaluation.gui.world.Cursor._
import evaluation.engine.Image.Visualizable
import java.awt.image.BufferedImage


object AWTView {
  implicit def toVPoint(e: AWTMouseEvent)(implicit v: View) = VPoint(e.getX, e.getY)

  implicit def toAWTCursor(c: Cursor) = c match {
    case ResizeEWCursor => AWTCursor.getPredefinedCursor(AWTCursor.W_RESIZE_CURSOR)
    case ResizeSNCursor => AWTCursor.getPredefinedCursor(AWTCursor.S_RESIZE_CURSOR)
    case _ => AWTCursor.getDefaultCursor
  }

  implicit def toAWTCanvas(v: AWTView) = v._canvas
}

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 2/03/14
 * Time: 15:31
 * To change this template use File | Settings | File Templates.
 */
class AWTView extends View{

  import evaluation.gui.world.awt.AWTView._


  private val _canvas : Canvas = new Canvas() {
    override def update(g:Graphics) = paint(g)
    override def paint(g: Graphics) = {
      logger.debug( "AWTView._canvas.paint" )
      g.drawImage( doubleBuffer, 0, 0, null )
    }
    addComponentListener( new ComponentAdapter(){
      override def componentResized(ce: ComponentEvent ){
        self.reDraw
      }
    })
  }

  private var _doubleBuffer : Visualizable = null

  private def doubleBuffer = {
    val b = box
    if( _doubleBuffer == null || _doubleBuffer.getWidth != b.width || _doubleBuffer.getHeight != b.height ){
      _doubleBuffer = new Visualizable( b.width.toInt max 1, b.height.toInt max 1, BufferedImage.TYPE_INT_ARGB )
    }
    _doubleBuffer
  }

  def brush = doubleBuffer.getGraphics

  transform = AWTTransform.identity

  override def cursor_=(c: Cursor) = {
    super.cursor_=(c)
    _canvas.setCursor(c)
  }

  def box = {
    val size = _canvas.getSize
    Rect(0, 0, size.width, size.height)
  }

  val repaintFastForFewObjects = true

  override def reDraw(b: Brush){
    super.reDraw(b)
    if( repaintFastForFewObjects )
      _canvas.paint(_canvas.getGraphics)
    else
      _canvas.repaint
  }

  val _mouseListener = new MouseAdapter() {

    override def mouseClicked(e: AWTMouseEvent) = invokeMouseEvent(MouseClicked(e))

    override def mouseDragged(e: AWTMouseEvent) = invokeMouseEvent(MouseDragged(e))

    override def mousePressed( e: AWTMouseEvent ) = invokeMouseEvent(MouseDown(e))

    override def mouseReleased( e: AWTMouseEvent ) = invokeMouseEvent(MouseUp(e))

    override def mouseMoved(e: AWTMouseEvent) = invokeMouseEvent(MouseMoved(e))
  }

  _canvas.addMouseListener(_mouseListener)
  _canvas.addMouseMotionListener(_mouseListener)
}

