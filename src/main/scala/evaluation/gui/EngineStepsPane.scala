package evaluation.gui

import javax.swing.JPanel
import java.awt.{Dimension, GridLayout, BorderLayout}
import java.awt.event.{MouseEvent, MouseAdapter}
import evaluation.engine.Engine
import evaluation.Log

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 31/12/13
 * Time: 17:33
 * To change this template use File | Settings | File Templates.
 */
class EngineStepsPane(images: Seq[ImagePanel]) extends JPanel {

  setLayout(new BorderLayout)

  object proxy extends ImagePanel {
    override def image() = _componentClicked.image

    override def label() = _componentClicked.label
  }

  private val repaintListener = () => proxy.repaint()

  private var _componentClicked: ImagePanel = images.last
  _componentClicked + repaintListener

  private val imagesPanel = new JPanel
  add(imagesPanel, BorderLayout.NORTH)
  add(proxy, BorderLayout.CENTER)

  imagesPanel.setLayout(new GridLayout(1, images.size))
  images.foreach(imagesPanel.add(_))

  private val mouseAdapter = new MouseAdapter() {
    override def mouseClicked(e: MouseEvent) {
      _componentClicked - repaintListener
      _componentClicked = e.getComponent.asInstanceOf[ImagePanel]
      _componentClicked + repaintListener
      repaintListener()
      Log(s"Clicked on ${_componentClicked}")
    }
  }

  images.foreach(_.addMouseListener(mouseAdapter))

  imagesPanel.setPreferredSize(new Dimension(200, 200))
}

object EngineStepsPane {
  def apply(e: Engine) = {
    val imagePanels = e.imageActors.map(a => ImagePanel(a, a.getClass.getName))
    new EngineStepsPane(imagePanels)
  }
}
