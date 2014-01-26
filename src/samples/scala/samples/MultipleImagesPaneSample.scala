package samples

import evaluation.engine._
import evaluation.actor.{CaptureImageActor, StitchImageActor, FixedImageActor}
import evaluation.gui.{EngineStepsPane, ImagePanel}
import javax.swing.JFrame
import javax.imageio.ImageIO
import java.io.File
import evaluation.gui._

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 30/12/13
 * Time: 10:13
 * To change this template use File | Settings | File Templates.
 */
object MultipleImagesPaneSample extends App{

  val img = ImageIO.read( new File("./src/testimages/stitch/corner-three-empty-holes.JPG"))
  MultipleImagesPane.show( Cropper( Image(img), 3) )
  
  val f = new JFrame("Original")
  f add ImagePanel( Image(img), "Original" )
  f.show();

}
