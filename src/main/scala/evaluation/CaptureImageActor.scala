package evaluation

import scala.actors.Actor
import java.awt.Image
import com.github.sarxos.webcam.Webcam
import evaluation.VideoCapture.ImageCapturedListener
import evaluation.ImageMessages.{Img, LastImage, GetLastImage}

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 26/12/13
 * Time: 22:30
 * To change this template use File | Settings | File Templates.
 */
class CaptureImageActor extends Actor {

  case class ImageCaptured(image: Img)

  case class Stop(requester: Actor)

  case class Stopping(actor: CaptureImageActor)

  private var _stopASAP = false

  private var _lastImage: Img = null

  val _minMillisBetweenFrames = 200
  val self = this

  private val vc = new VideoCapture()
  private val vct = vc.startVideoCapture(new ImageCapturedListener {
    def imageCaptured(i: Img) {
      //Log( "Image captured" )
      self ! ImageCaptured(i)
      Thread.sleep(_minMillisBetweenFrames)
    }
  })

  private var _lastImageChanged = false
  private var _pendingRequests = List[Actor]()

  def act() {
    Log(s"Starting CaptureImageActor")

    loop {
      receive {
        case GetLastImage(requester) =>
          //Log(s"Sending last image to $requester")
          if (_lastImageChanged) {
            requester ! LastImage(_lastImage)
            _lastImageChanged = false
          }
          else {
            _pendingRequests = requester :: _pendingRequests

          }

        case ImageCaptured(image) =>
          //Log( "Image captured -> to _lastImage" )
          _lastImageChanged = true
          _lastImage = image
          _pendingRequests.foreach(_ ! LastImage(_lastImage))
          _pendingRequests = Nil

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
