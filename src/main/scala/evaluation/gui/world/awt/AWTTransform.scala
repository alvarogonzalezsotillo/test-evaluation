package evaluation.gui.world.awt

import evaluation.gui.world.Transform
import java.awt.geom.{Point2D, AffineTransform}
import evaluation.gui.world.awt.AWTTransform._
import evaluation.engine.Geom.Point

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 26/02/14
 * Time: 10:49
 * To change this template use File | Settings | File Templates.
 */
class AWTTransform( val transform : AffineTransform = new AffineTransform() ) extends Transform{

  type Self = AWTTransform

  override def concatenate(t: AWTTransform) = {
    val newT = new AffineTransform(transform)
    newT.concatenate(t)
    new AWTTransform( newT )
  }

  override def inverse : Option[AWTTransform] = tryOption(){
    val i = transform.createInverse()
    new AWTTransform( i )
  }

  override def preConcatenate(t: AWTTransform) = {
    val newT = new AffineTransform(transform)
    newT.preConcatenate(t)
    new AWTTransform( newT )
  }

  def apply(t: Point): Point = {
    val newT = new Point2D.Float(t.x,t.y)
    transform.transform(newT,newT)
    new Point(newT.x, newT.y)
  }
}


object AWTTransform{
  implicit def toAffineTransform(at: AWTTransform): AffineTransform = at.transform
  implicit def toAffineTransform(t: Transform) : AffineTransform = t match{
    case at: AWTTransform => at.transform
    case at if( at == null ) => new AffineTransform()
  }


  val identity = new AWTTransform( new AffineTransform )

}