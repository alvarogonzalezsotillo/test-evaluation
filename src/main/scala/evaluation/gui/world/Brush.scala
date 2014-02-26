package evaluation.gui.world

import evaluation.engine.Image
import evaluation.engine.Geom.{Rect, Point}

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 24/02/14
 * Time: 11:43
 * To change this template use File | Settings | File Templates.
 */
trait Brush {
  def drawImg( img: Image, to: Rect )
  def drawText( text: String, to: Point )
  def drawLine( ini: Point, end: Point )
  def setFont( font: String )
  def setColor( color: String )
  def transform( t: Transform ) : Brush

  def drawRect( r: Rect ) = {
    val c = r.cornerPoints
    drawLine( c(0), c(1) )
    drawLine( c(1), c(2) )
    drawLine( c(2), c(3) )
    drawLine( c(3), c(0) )
  }

}
