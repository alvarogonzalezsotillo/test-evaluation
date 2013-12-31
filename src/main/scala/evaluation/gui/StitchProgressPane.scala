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
import evaluation.actor.StitchImageActor
import evaluation.actor.ImageMessages.Img
import java.awt.{Dimension, GridLayout, BorderLayout}

class StitchProgressPane(pattern: Img, imageActor: Actor) extends JPanel {

  private val patternPanel = ImagePanel(pattern, "Pattern image")

  private val imagePanel = ImagePanel(imageActor, "Image to be stitched")

  private val stitchActor = new StitchImageActor(pattern, imageActor)

  private val stitchedPanel = ImagePanel(stitchActor, "Stitched")

  setLayout(new BorderLayout)
  add(stitchedPanel, BorderLayout.CENTER)

  private val leftPanel = new JPanel
  add(leftPanel, BorderLayout.NORTH)

  leftPanel.setLayout(new GridLayout(1, 2))
  leftPanel.add(patternPanel)
  leftPanel.add(imagePanel)
  leftPanel.setPreferredSize( new Dimension(200,200) )
}
