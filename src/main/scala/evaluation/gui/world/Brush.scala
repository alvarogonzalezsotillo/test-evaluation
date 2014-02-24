package evaluation.gui.world

import evaluation.engine.Image
import evaluation.engine.Geom.Point

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 24/02/14
 * Time: 11:43
 * To change this template use File | Settings | File Templates.
 */
trait Brush {
  def drawImg( img: Image, t: Transform = IdentityTransform )
  def drawText( text: String, t: Transform = IdentityTransform)
  def drawLine( ini: Point, end: Point, t: Transform = IdentityTransform)
  def setFont( font: String )
  def setColor( color: String )
  def transform( t: Transform ) : Brush
}
