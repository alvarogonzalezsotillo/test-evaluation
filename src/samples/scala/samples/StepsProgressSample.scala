package samples

import evaluation.engine._
import evaluation.actor.{CaptureImageActor, StitchImageActor, FixedImageActor}
import evaluation.gui.{EngineStepsPane, ImagePanel}
import javax.swing.JFrame
import javax.imageio.ImageIO
import java.io.File

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 30/12/13
 * Time: 10:13
 * To change this template use File | Settings | File Templates.
 */
object StepsProgressSample extends App{

  val patterns = Seq(
    //"corner-three-empty-holes",
    "corner-centralempty-hole"
    //"corner-two-empty-symmetric-holes",
    //"corner-two-empty-asymmetric-holes"
  )

  val webcamActor = new CaptureImageActor

  for( p <- patterns ){

    val pattern = ImageIO.read( new File(s"./src/testimages/stitch/$p.jpg"))

    val frame = new JFrame(p)
    frame add EngineStepsPane( Engine(Image(pattern), webcamActor ) )

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

    frame.setSize(200, 200)
    frame.setVisible(true)
    frame.requestFocus();
  }


}
