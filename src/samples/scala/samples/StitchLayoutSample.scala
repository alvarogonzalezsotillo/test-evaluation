package samples

import evaluation.engine.{TestLayoutToImg, TestLayout}
import evaluation.actor.{StitchImageActor, FixedImageActor}
import evaluation.gui.ImagePanel
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
object StitchLayoutSample extends App{

  val layout = TestLayout(135,4)


  val pattern = TestLayoutToImg(layout)


  val image = ImageIO.read( new File("./src/testimages/layout/msword-135.jpg"))
  val ia = new FixedImageActor(image)
  val sia = new StitchImageActor(pattern,ia)
  val panel = ImagePanel(sia)

  val frame = new JFrame("Stitch layout image sample")
  frame add panel
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

  frame.setSize(200,200)
  frame.setVisible(true)


}
