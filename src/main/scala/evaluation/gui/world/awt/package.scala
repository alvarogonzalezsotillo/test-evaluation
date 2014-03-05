package evaluation.gui.world

import com.typesafe.scalalogging.slf4j.Logging

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 4/03/14
 * Time: 12:21
 * To change this template use File | Settings | File Templates.
 */
package object awt extends Logging{

  def tryOptionIgnored(t: Throwable ) = Unit
  def tryOptionPropagated(t: Throwable ) = throw t
  def tryOptionLogged(t: Throwable) = logger.debug( "tryOptionLogged", t )
  val tryOptionDefaultHandler = tryOptionLogged _

  def tryOption[T]( handler: (Throwable)=> Unit = tryOptionDefaultHandler )( proc: => T ) : Option[T] = {
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
