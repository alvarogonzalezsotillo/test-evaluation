package samples

import javax.swing.JFrame
import evaluation.actor.{CaptureImageActor}
import evaluation.gui.ImagePanel

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 27/12/13
 * Time: 22:41
 * To change this template use File | Settings | File Templates.
 */
object CaptureImageAndPanelSample extends App{

  val cia = new CaptureImageActor
  val panel = ImagePanel(cia)

  val frame = new JFrame("Capture image and panel sample")
  frame add panel
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

  frame.setSize(200,200)
  frame.setVisible(true)
}
