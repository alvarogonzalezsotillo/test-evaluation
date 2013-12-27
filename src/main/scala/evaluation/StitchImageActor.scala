package evaluation

import scala.actors.Actor
import evaluation.ImageMessages.{LastImage, GetLastImage, Img}

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 27/12/13
 * Time: 23:58
 * To change this template use File | Settings | File Templates.
 */
class StitchImageActor( pattern : Img, imageActor: Actor ) extends Actor{

  val stitcher = new Stitcher(pattern)

  val self = this

  var _lastImage : Img = null

  var _pendingRequests = List[Actor]()

  def act(){
    loop{
      receive{

        case LastImage(image) =>
          _lastImage = stitcher.stitch(image)
          _pendingRequests.foreach( _ ! LastImage(_lastImage) )
          _pendingRequests = Nil

        case GetLastImage(requester) =>
          val first = _pendingRequests.size == 0
          _pendingRequests = requester :: _pendingRequests
          if( first ){
            imageActor ! GetLastImage(self)
          }

      }
    }
  }

  start()
}
