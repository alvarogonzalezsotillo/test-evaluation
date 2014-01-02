package evaluation.gui

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 30/12/13
 * Time: 16:07
 * To change this template use File | Settings | File Templates.
 */

import javax.swing.JPanel
import scala.actors.Actor
import evaluation.actor.{FixedImageActor, BinaryImageActor, StitchImageActor}
import java.awt.{Dimension, GridLayout, BorderLayout}
import evaluation.engine.Img

class StitchProgressPane(pattern: Img, imageActor: Actor) extends JPanel {

  private val patternPanel = ImagePanel(pattern, "Pattern image")

  private val imagePanel = ImagePanel(imageActor, "Image to be stitched")

  private val binaryActor = new BinaryImageActor(imageActor)

  private val binaryPanel = ImagePanel(binaryActor, "Binary image")

  private val stitchActor = new StitchImageActor( new FixedImageActor(pattern), binaryActor)

  private val stitchedPanel = ImagePanel(stitchActor, "Stitched")

  setLayout(new BorderLayout)
  add(stitchedPanel, BorderLayout.CENTER)

  private val leftPanel = new JPanel
  add(leftPanel, BorderLayout.NORTH)

  leftPanel.setLayout(new GridLayout(1, 3))
  leftPanel.add(patternPanel)
  leftPanel.add(binaryPanel)
  leftPanel.add(imagePanel)
  leftPanel.setPreferredSize( new Dimension(200,200) )
}
