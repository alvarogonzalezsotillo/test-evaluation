package evaluation

import scala.actors.Actor

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 26/12/13
 * Time: 22:19
 * To change this template use File | Settings | File Templates.
 */
class Bus extends Actor {

  case class AddActor(actor: Actor)
  case class ActorAdded(from: Bus)
  case class RemoveActor(actor: Actor)
  case class ActorRemoved(from: Bus)

  val self = this

  private var _actors = Set[Actor]()

  def act() = {

    while (true) {
      receive {
        case AddActor(actor) =>
          _actors = _actors + actor
          actor ! ActorAdded(self)

        case RemoveActor(actor) =>
          _actors = _actors + actor
          actor ! ActorRemoved(self)

        case msg =>
          _actors.foreach(_ ! msg)

      }
    }
  }
}
