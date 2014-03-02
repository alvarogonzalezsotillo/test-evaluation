package evaluation.gui.world

import evaluation.engine.Geom.Point

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 24/02/14
 * Time: 11:45
 * To change this template use File | Settings | File Templates.
 */



trait Transform {
  type Self <: Transform
  def concatenate( t: Self ) : Self
  def preConcatenate( t: Self ) : Self
  def inverse: Option[Self]
  def apply( t: Point ): Point
}


object Syntaxcheck{
  val t1: Transform = null
  val inverse = t1.inverse.get
  val operation = t1.concatenate(inverse)
}

