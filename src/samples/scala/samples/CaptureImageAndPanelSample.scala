package samples

import javax.swing.JFrame
import evaluation.actor.{CaptureImageActor}
import evaluation.gui.ImagePanel
import javax.swing.JButton
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import evaluation.engine.Image
import javax.imageio.ImageIO
import java.io.File
import java.awt.BorderLayout

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 27/12/13
 * Time: 22:41
 * To change this template use File | Settings | File Templates.
 */
object CaptureImageAndPanelSample extends App{

  val cia = new CaptureImageActor
  val panel = ImagePanel(cia, "Webcam")

  val frame = new JFrame("Capture image and panel sample")
  val button = new JButton("Save")
  button addActionListener new ActionListener(){
    def actionPerformed(e: ActionEvent){
      cia.lastImage match{
        case Image(v) =>
          ImageIO.write(v,"png", new File("capture.png") )
      }
    }
  }
  
  frame setLayout new BorderLayout
  frame add( button, BorderLayout.SOUTH )
  frame add( panel, BorderLayout.CENTER )
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

  frame.setSize(200,200)
  frame.setVisible(true)
}
