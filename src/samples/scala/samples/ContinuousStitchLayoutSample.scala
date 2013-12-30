package samples

import evaluation.engine.{TestLayoutToImg, TestLayout}
import evaluation.actor.{CaptureImageActor, StitchImageActor, FixedImageActor}
import evaluation.gui.{StitchProgressPane, ImagePanel}
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
object ContinuousStitchLayoutSample extends App{


  val layout = TestLayout(135,4)


  val pattern = TestLayoutToImg(layout)
  val ia = new CaptureImageActor

  val frame = new JFrame("Test continuous Layout sample")
  frame add new StitchProgressPane(pattern,ia)

  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

  frame.setSize(200, 200)
  frame.setVisible(true)


}
