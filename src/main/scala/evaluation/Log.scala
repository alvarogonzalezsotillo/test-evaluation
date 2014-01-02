package evaluation

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 27/12/13
 * Time: 22:48
 * To change this template use File | Settings | File Templates.
 */
object Log {
  def apply( s: AnyRef ) = {
    val threadName = Thread.currentThread.getName
    println( s"$threadName - ${s.toString} ")
  }
}
