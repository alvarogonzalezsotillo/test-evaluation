package evaluation.actor

import evaluation.actor.ImageMessages._
import evaluation.Log
import scala.actors.Actor
import evaluation.actor.ImageMessages.GetImage
import evaluation.actor.ImageMessages.LastImage

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 31/12/13
 * Time: 15:49
 * To change this template use File | Settings | File Templates.
 */
abstract class ProcessImageActor(imageActor: Actor) extends Actor {

  private var _lastImage: LastImage = null

  private val self = this

  private var _pendingRequests = List[GetImage]()

  def act() {
    loop {
      receive {

        case LastImage(_, image, time) =>
          Log(s"$this: LastImage received")
          val processedImage = processImage(image)
          if (processedImage != null) {
            _lastImage = LastImage(self, processedImage, time)
            _pendingRequests.filter(_.lastTime < time).foreach(_.requester ! _lastImage)
            _pendingRequests = _pendingRequests.filter(_.lastTime >= time)
          }

        case GetImage(requester, lastTime) =>
          Log(s"$this: GetImage received")
          if (_lastImage != null && lastTime < _lastImage.time) {
            // cached image newer than lastTime
            Log(s"  cached image is new enough")
            requester ! _lastImage
          }
          else {
            // cached image not updated, request a new one
            Log(s"  requesting newer image")
            imageActor ! GetImage(self, lastTime)
            _pendingRequests = GetImage(requester, lastTime) :: _pendingRequests
          }


      }
    }
  }

  def processImage(image: Img): Img

  start()

}
