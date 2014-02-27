package evaluation.gui.world.awt

import evaluation.gui.world.Transform
import java.awt.geom.AffineTransform
import evaluation.gui.world.awt.AWTTransform._

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 26/02/14
 * Time: 10:49
 * To change this template use File | Settings | File Templates.
 */
class AWTTransform( val transform : AffineTransform = new AffineTransform() ) extends Transform{

  type myType = AWTTransform

  def concatenate(t: AWTTransform) = {
    val newT = new AffineTransform(transform)
    newT.concatenate(t)
    new AWTTransform( newT )
  }

  def inverse = tryOption(){
    val ret : AWTTransform = new AWTTransform( transform.createInverse() )
    ret
  }

  def preConcatenate(t: AWTTransform) = {
    val newT = new AffineTransform(transform)
    newT.preConcatenate(t)
    new AWTTransform( newT )
  }

}

object AWTTransform{
  implicit def toAffineTransform(at: AWTTransform): AffineTransform = at.transform
  implicit def toAffineTransform(t: Transform) : AffineTransform = t match{
    case at: AWTTransform => at.transform
  }

  def tryOption[T]( handler: (Throwable)=> Unit = println )( proc: => T ) = {
    try{
      Option(proc)
    }
    catch{
      case t : Throwable =>
        handler(t)
        None
    }
  }

}