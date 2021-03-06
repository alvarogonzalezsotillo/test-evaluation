package samples

import javax.swing.JFrame
import javax.imageio.ImageIO
import java.io.File
import evaluation.actor.{StitchImageActor, FixedImageActor}
import evaluation.gui.ImagePanel
import evaluation.engine._

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 27/12/13
 * Time: 23:57
 * To change this template use File | Settings | File Templates.
 */
object StitchImageAndPanelSample extends App{

  val image = ImageIO.read( new File("./src/testimages/stitch/borders-05.jpg"))
  val ia = new FixedImageActor(Image(image))
  val pattern = ImageIO.read( new File("./src/testimages/stitch/borders-00.jpg"))
  val sia = StitchImageActor(new FixedImageActor(Image(pattern)),ia)
  val panel = ImagePanel(sia)

  val frame = new JFrame("Stitch image sample")
  frame add panel
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

  frame.setSize(200,200)
  frame.setVisible(true)


}
