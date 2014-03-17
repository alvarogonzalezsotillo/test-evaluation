package evaluation.gui.world.awt

import java.awt.{Graphics, Font, Color, Graphics2D}
import evaluation.gui.world.{Transform, Brush}
import evaluation.engine.Geom.Point
import evaluation.engine.Geom.Rect
import evaluation.engine.Image
import evaluation.gui.world.awt.AWTBrush._
import evaluation.gui.world.awt.AWTTransform._
import evaluation.gui.world

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 26/02/14
 * Time: 10:43
 * To change this template use File | Settings | File Templates.
 */
class AWTBrush( graphics: Graphics2D ) extends Brush{

  private def temporaryTransform(t: Transform)( operation: => Unit ){
    val oldT = graphics.getTransform
    graphics.transform(t)
    operation
    graphics.setTransform(oldT)
  }


  def drawImg(img: Image, to: Rect ) = {
    val (x,y,w,h) = to.toInts
    graphics.drawImage(img.visualizable,x,y,w,h,transparentColor,null)
  }

  def fillRect(r: Rect) = {
    val (x,y,w,h) = r.toInts
    graphics.fillRect(x,y,w,h)
  }


  def drawText(text: String, to: Point ) = {
    val (x,y) = to.toInts
    graphics.drawString(text,x,y)
  }

  def drawLine(ini: Point, end: Point ) = {
    val (x1,y1) = ini.toInts
    val (x2,y2) = end.toInts
    graphics.drawLine(x1,y1,x2,y2)
  }

  override def font_=(font: String) = {
    super.font_=(font)
    val f = Font.decode(font)
    graphics.setFont(f)
  }

  override def color_=(color: String) = {
    super.color_=(color)
    val c = Color.decode(color)
    graphics.setColor(c)
  }

  def transform(t: Transform) = {
    val newGraphics = graphics.create().asInstanceOf[Graphics2D]
    newGraphics.transform(t)
    new AWTBrush( newGraphics )
  }

}

object AWTBrush{
  val transparentColor = new Color(0,0,0,0)


  implicit def fromGraphics( g: Graphics ) = {
    val g2d = g.asInstanceOf[Graphics2D]
    new AWTBrush(g2d)
  }
}
