package evaluation.actor

import scala.actors.Actor
import evaluation.engine.Stitcher
import evaluation.actor.ImageMessages.{GetImage, LastImage, Img}
import evaluation.Log

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

  var _lastImage : LastImage = null

  var _pendingRequests = List[GetImage]()

  def act(){
    loop{
      receive{

        case LastImage(image,time) =>
          Log( s"$this: LastImage received")
          _lastImage = LastImage(stitcher.stitch(image),time)
          _pendingRequests.filter(_.lastTime < time).foreach( _.requester ! _lastImage )
          _pendingRequests = _pendingRequests.filter(_.lastTime >= time)

        case GetImage(requester,lastTime) =>
          Log( s"$this: GetImage received")
          if ( _lastImage != null && lastTime < _lastImage.time ) {
            // cached image newer than lastTime
            Log( s"  cached image is new enough")
            requester ! _lastImage
          }
          else {
            // cached image not updated, request a new one
            Log( s"  requesting newer image")
            imageActor ! GetImage(self,lastTime)
            _pendingRequests = GetImage(requester,lastTime) :: _pendingRequests
          }


      }
    }
  }

  start()
}
