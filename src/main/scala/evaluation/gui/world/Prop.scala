package evaluation.gui.world

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 17/03/14
 * Time: 10:57
 * To change this template use File | Settings | File Templates.
 */
class Prop[T]( initialValue: T ) extends (() => T) {

  import Prop._
  
  val self = this

  private var _value : T = initialValue

  def update( t: T ) = {
    if( _value != t ){
      val old = _value
      _value = t
      notifySet(old)
    }
  }

  def derive[A](props: Prop[A]*)( f : => T ) = {
    props.foreach( p => p.listen{  update(f) } )
  }

  def apply = _value

  private def notifySet( old: T ) = _listeners.foreach( l => l(old,this) )

  type Listener = (T, Prop[T]) => Unit

  class DetachableListener( l: Listener ){
    def detach() = self unlisten l
  }

  var _listeners = Set[Listener]()

  def listen( l: Listener ) : DetachableListener = {
    _listeners = _listeners + l
    new DetachableListener(l)
  }

  def listen( l: => Unit ) : DetachableListener = listen( (old: T, p: Prop[T]) => l )

  def unlisten( l: Listener ) = _listeners = _listeners - l

  override def toString = _value.toString
}


object Prop{
  def apply[T]( t: T ) : Prop[T] = new Prop(t)

  def derive[T,A](props: Prop[A]*)( f : => T ) : Prop[T]= {
    val initialValue = f
    val ret = Prop[T](initialValue)
    ret.derive(props:_*)(f)
    ret
  }


  def main( args: Array[String]) = {
    val prop = Prop("Hola")
    val derived : Prop[String] = Prop.derive( prop ){
      prop() + " derived"
    }

    val value1 = prop()
    prop() = "adios"
    val value2 = prop()

    println( s"prop:$prop  value1:$value1  value2:$value2  derived:$derived"  )
  }
}