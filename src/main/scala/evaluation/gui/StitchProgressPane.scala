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

  private val patternPanel = ImagePanel(pattern)

  private val imagePanel = ImagePanel(imageActor)

  private val stitchActor = new StitchImageActor(pattern, imageActor)

  private val stitchedPanel = ImagePanel(stitchActor)

  setLayout(new BorderLayout)
  add(stitchedPanel, BorderLayout.CENTER)

  private val leftPanel = new JPanel
  add(leftPanel, BorderLayout.NORTH)

  leftPanel.setLayout(new GridLayout(1, 2))
  leftPanel.add(patternPanel)
  leftPanel.add(imagePanel)
  leftPanel.setPreferredSize( new Dimension(200,200) )
}
