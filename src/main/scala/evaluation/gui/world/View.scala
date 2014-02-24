package evaluation.gui.world

import evaluation.engine.Geom.Rect

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 24/02/14
 * Time: 12:21
 * To change this template use File | Settings | File Templates.
 */
trait View {
  def brush : Brush
  def box: Rect

  def drawable : Drawable
  def drawable_=( d : Drawable )

  def transform : Transform
  def transform_=( t: Transform )

  def reDraw(){
    val b = brush.transform(transform)
    drawable.draw(b)
  }
}
