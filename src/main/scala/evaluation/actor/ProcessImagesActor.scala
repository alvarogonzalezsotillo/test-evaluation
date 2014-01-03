package evaluation.actor

import scala.actors.Actor
import evaluation.engine.Img
import evaluation.actor.ImageMessages.{Time, LastImage, GetImage}
import evaluation.Log
import scala.util.{Failure, Success, Try}

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 3/01/14
 * Time: 19:04
 * To change this template use File | Settings | File Templates.
 */
class ProcessImagesActor(imageActors: IndexedSeq[Actor], imageProcessor: (IndexedSeq[Img]) => Img) extends Actor {


  private var actorToImg = Map[Actor, Img]().withDefaultValue(null)
  private var actorToTime = Map[Actor, Time]().withDefaultValue(null)

  private val self = this

  var _lastImage: LastImage = null;

  private var _pendingRequests = List[GetImage]()


  def act() = {

    // REQUEST FIRST IMAGE
    imageActors.foreach(_ ! GetImage(self, ImageMessages.noTime))

    loop {
      receive {

        case LastImage(sender, image, time) =>
          actorToImg = actorToImg + (sender -> image)
          actorToTime = actorToTime + (sender -> time)
          val images = imageActors.map(actorToImg).toIndexedSeq
          Try(imageProcessor(images)) match {
            case Success(img) if img != null =>
              _lastImage = LastImage(self, img, time)
              _pendingRequests.filter(_.lastTime < time).foreach(_.requester ! _lastImage)
              _pendingRequests = _pendingRequests.filter(_.lastTime >= time)

            case Success(null) =>
              sender ! GetImage(self, time)

            case Failure(ex) =>
              Log(s"$this: $ex")
              ex.printStackTrace()
              sender ! GetImage(self, time)
          }


        case GetImage(requester, lastTime) =>
          if (_lastImage != null && lastTime < _lastImage.time) {
            // cached image newer than lastTime
            requester ! _lastImage
          }
          else {
            // cached image not updated, request a new one
            imageActors.foreach(_ ! GetImage(self, lastTime))
            _pendingRequests = GetImage(requester, lastTime) :: _pendingRequests
          }

        case u =>
          Log(s"$this: Unexpected: $u")

      }
    }
  }

  start()
}
