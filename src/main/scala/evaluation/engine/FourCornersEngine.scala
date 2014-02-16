package evaluation.engine

import evaluation.actor._
import scala.actors.Actor
import evaluation.Log
import javax.imageio.ImageIO
import java.io.File

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 31/12/13
 * Time: 17:46
 * To change this template use File | Settings | File Templates.
 */
class FourCornersEngine(imageActor: Actor) extends Engine {

  val patterns = Seq(
    "corner-three-empty-holes",
    "corner-centralempty-hole",
    "corner-two-empty-symmetric-holes",
    "corner-two-empty-asymmetric-holes"
  )
  
  val patternImgs = patterns.map( p => ImageIO.read( new File(s"./src/testimages/stitch/$p.jpg")) )
  
  val patternActors = patternImgs.map( i => new FixedImageActor(Image(i) ) )
  
  private val stitchActors = patternActors.map( p => StitchImageActor( p,imageActor) )
  
  private val showStitchActor = LocatePointsOfPatternActor(true, stitchActors :_* )

  val imageActors = IndexedSeq(imageActor, showStitchActor)
}

object FourCornersEngine {
  def apply(imageActor: Actor) = new FourCornersEngine(imageActor)
}
