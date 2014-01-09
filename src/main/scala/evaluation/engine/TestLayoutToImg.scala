package evaluation.engine

import java.awt.image.BufferedImage
import java.awt.geom.AffineTransform
import java.awt.{Color, Graphics2D}
import evaluation.Log
import evaluation.engine.Geom.Rect
import scala.collection.immutable

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 29/12/13
 * Time: 23:43
 * To change this template use File | Settings | File Templates.
 */
object TestLayoutToImg {

  def apply(layout: TestLayout): Img.Visualizable = {
    val boundingBox = layout.boundingBox
    val margin = boundingBox.width * 0.2f
    val size = boundingBox.grow(margin)

    val w = size.width.asInstanceOf[Int]
    val h = size.height.asInstanceOf[Int]

    val ret = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

    val g2d = ret.getGraphics.asInstanceOf[Graphics2D]
    g2d.setColor(Color.WHITE)
    g2d.fillRect(0,0,w,h)


    val t = AffineTransform.getTranslateInstance(margin, margin)
    g2d.setTransform(t)
    g2d.setColor(Color.BLACK)

    def drawRect( r: Rect, drawer : (Int,Int,Int,Int) => Unit ) = {
      val x = r.left.asInstanceOf[Int]
      val y = r.top.asInstanceOf[Int]
      val w = r.width.asInstanceOf[Int]
      val h = r.height.asInstanceOf[Int]
      drawer(x,y,w,h)
    }

    //layout.questionRects.map( drawRect(_, g2d.fillRect) )
    //layout.answerOptionHeaderRects.flatten.map( drawRect(_, g2d.fillRect) )
    //layout.answerOptionRects.flatten.map( drawRect(_, (x,y,w,h) => g2d.drawRoundRect(x,y,w,h,5,5) ) )

    def combine( rects: Seq[Rect]) = rects.foldLeft(rects.head)( (a, b) => a+b)

    layout.columns.map(layout.questionColumnsRect).foreach( rects =>{
      drawRect( combine(rects), g2d.fillRect )
    })

    drawRect( combine(layout.answerOptionHeaderRects.flatten), g2d.fillRect )

    g2d.dispose()
    ret
  }

  def apply(layout: TestLayout, visualizable: Img.Visualizable ){

  }
}
