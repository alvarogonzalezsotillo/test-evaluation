package evaluation.actor

import scala.actors.Actor
import java.awt.Image
import com.github.sarxos.webcam.Webcam
import evaluation.engine.VideoCapture
import VideoCapture.ImageCapturedListener
import evaluation.{Log}
import evaluation.actor.ImageMessages.{Time, GetImage, LastImage, Img}

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 26/12/13
 * Time: 22:30
 * To change this template use File | Settings | File Templates.
 */
class CaptureImageActor extends Actor {

  case class ImageCaptured(image: Img, time: Time)

  case class Stop(requester: Actor)

  case class Stopping(actor: CaptureImageActor)

  private var _stopASAP = false

  private var _lastImage: LastImage = null

  val _minMillisBetweenFrames = 0
  val self = this

  private val vc = new VideoCapture()
  private val vct = vc.startVideoCapture(new ImageCapturedListener {
    def imageCaptured(i: Img) {
      //Log( "Image captured" )
      self ! ImageCaptured(i, System.currentTimeMillis )
      Thread.sleep(_minMillisBetweenFrames)
    }
  })

  private var _pendingRequests = List[GetImage]()

  def act() {
    Log(s"Starting CaptureImageActor")

    loop {
      receive {
        case GetImage(requester,lastTime) =>
          //Log(s"Sending last image to $requester")
          if (_lastImage != null && lastTime < _lastImage.time ) {
            requester ! _lastImage
          }
          else {
            _pendingRequests = GetImage(requester,lastTime) :: _pendingRequests
          }

        case ImageCaptured(image, time) =>
          //Log( "Image captured -> to _lastImage" )
          _lastImage = LastImage(image, time )
          _pendingRequests.filter(_.lastTime < time).foreach( _.requester ! _lastImage )
          _pendingRequests = _pendingRequests.filter(_.lastTime >= time)

        case Stop(requester) =>
          vct.stopASAP()
          requester ! Stopping(self)

        case anything =>
          Log(s"CaptureImageActor Unexpected $anything")
      }
    }
  }

  start()
}
