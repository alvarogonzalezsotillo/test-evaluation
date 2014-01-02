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


  val layout = TestLayout(135,4)



  //val pattern = TestLayoutToImg(layout)
  val pattern = ImageIO.read( new File("./src/testimages/stitch/borders-00.jpg"))



  val frame = new JFrame("Test continuous Layout sample")
  frame add EngineStepsPane( BinaryStitchEngine(Img(pattern)) )

  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

  frame.setSize(200, 200)
  frame.setVisible(true)


}
