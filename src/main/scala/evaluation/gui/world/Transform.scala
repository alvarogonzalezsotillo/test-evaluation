package evaluation.gui.world

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 24/02/14
 * Time: 11:45
 * To change this template use File | Settings | File Templates.
 */



trait Transform {
  type myType
  def concatenate( t: myType ) : myType
  def preConcatenate( t: myType ) : myType
  def inverse: Option[myType]
}




