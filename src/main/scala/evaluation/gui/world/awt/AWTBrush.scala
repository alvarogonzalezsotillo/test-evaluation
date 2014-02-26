package evaluation.gui.world.awt

import java.awt.{Font, Color, Graphics2D}
import evaluation.gui.world.{Transform, Brush}
import evaluation.engine.Geom.Point
import evaluation.engine.Geom.Rect
import evaluation.engine.Image
import evaluation.gui.world.awt.AWTBrush._
import evaluation.gui.world.awt.AWTTransform._

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

  def drawText(text: String, to: Point ) = {
    val (x,y) = to.toInts
    graphics.drawString(text,x,y)
  }

  def drawLine(ini: Point, end: Point ) = temporaryTransform(t){
    val (x1,y1) = ini.toInts
    val (x2,y2) = end.toInts
    graphics.drawLine(x1,y1,x2,y2)
  }

  def setFont(font: String) = {
    val f = Font.decode(font)
    graphics.setFont(f)
  }

  def setColor(color: String) = {
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
}
