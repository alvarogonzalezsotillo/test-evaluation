package evaluation.engine

import java.awt.image.BufferedImage
import java.awt.geom.AffineTransform
import java.awt.{Color, Graphics2D}
import evaluation.Log
import evaluation.engine.Geom.Rect

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 29/12/13
 * Time: 23:43
 * To change this template use File | Settings | File Templates.
 */
object TestLayoutToImg {

  def apply(layout: TestLayout): Img = {
    val boundingBox = layout.boundingBox
    val margin = boundingBox.width * 0.2f
    val size = boundingBox.grow(margin)

    Log( s"TestLayoutToImg: size: $size")

    val w = size.width.asInstanceOf[Int]
    val h = size.height.asInstanceOf[Int]

    val ret = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)
    Log( s"  ret: ${ret.getWidth}, ${ret.getHeight}")

    val g2d = ret.getGraphics.asInstanceOf[Graphics2D]
    g2d.setColor(Color.WHITE)
    g2d.fillRect(0,0,w,h)

    val t = AffineTransform.getTranslateInstance(-size.left, -size.top)
    g2d.setTransform(t)
    g2d.setColor(Color.BLACK)

    def drawRect( r: Rect, drawer : (Int,Int,Int,Int) => Unit ) = {
      val x = r.left.asInstanceOf[Int]
      val y = r.top.asInstanceOf[Int]
      val w = r.width.asInstanceOf[Int]
      val h = r.height.asInstanceOf[Int]
      Log( s"  $x,$y,$w,$h")
      drawer(x,y,w,h)
    }

    layout.questionRects.map( drawRect(_, g2d.fillRect) )
    layout.answerOptionHeaderRects.flatten.map( drawRect(_, g2d.fillRect) )
    layout.answerOptionRects.flatten.map( drawRect(_, (x,y,w,h) => g2d.drawRoundRect(x,y,w,h,5,5) ) )


    g2d.dispose()
    Img(ret)
  }
}
