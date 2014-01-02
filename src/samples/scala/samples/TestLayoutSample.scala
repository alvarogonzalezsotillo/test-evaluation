package samples

import javax.imageio.ImageIO
import java.io.File
import evaluation.actor.{StitchImageActor, FixedImageActor}
import evaluation.gui.{ImagePanel}
import javax.swing.JFrame
import evaluation.engine.{Img, TestLayoutToImg, TestLayout}

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 30/12/13
 * Time: 0:26
 * To change this template use File | Settings | File Templates.
 */
object TestLayoutSample extends App {

  val layout = TestLayout(135,4)


  val pattern = TestLayoutToImg(layout)
  val ia = new FixedImageActor( Img(ImageIO.read( new File( "./src/testimages/layout/msword-135.jpg"))))

  val frame = new JFrame("Test Layout sample")
  frame add new StitchProgressPane(pattern,ia)

  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

  frame.setSize(200, 200)
  frame.setVisible(true)


}
