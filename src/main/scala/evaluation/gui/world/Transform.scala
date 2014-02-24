package evaluation.gui.world

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 24/02/14
 * Time: 11:45
 * To change this template use File | Settings | File Templates.
 */



trait Transform {
  def concatenate( t: Transform ) : Transform
  def preConcatenate( t: Transform ) : Transform
  def inverse: Option[Transform]
}

object Transform{
  val IdentityTransform : Transform = null
}
