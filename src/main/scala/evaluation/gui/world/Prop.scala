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

  private var _value : T = initialValue

  def update( t: T ) = {
    if( _value != t ){
      val old = _value
      _value = t
      notifySet(old)
    }
  }

  def derive[A](props: Prop[A]*)( f : => T ) = {
    props.foreach( p => p += {  update(f) } )
  }

  def apply = _value

  private def notifySet( old: T ) = _listeners.foreach( l => l(old,this) )

  type Listener = (T, Prop[T]) => Unit
  var _listeners = Set[Listener]()

  class DetachableListener( l: Listener, prop: Prop[T] ){
    def detach() : Unit = prop -= l
    def attach() : Unit = prop += l
  }

  def +=( l: Listener ) : DetachableListener= {
    _listeners = _listeners + l
    new DetachableListener(l,this)
  }

  def +=( l:  => Unit ) : DetachableListener = this += ( (old: T, p: Prop[T]) => l )

  def -=( l: Listener ) = _listeners = _listeners - l

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