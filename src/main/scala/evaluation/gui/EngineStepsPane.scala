package evaluation.gui

import javax.swing.{Timer, JPanel}
import java.awt.{Dimension, GridLayout, BorderLayout}
import java.awt.Component
import java.awt.event.{ActionEvent, ActionListener, MouseEvent, MouseAdapter}
import evaluation.engine.Engine
import evaluation.Log

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 31/12/13
 * Time: 17:33
 * To change this template use File | Settings | File Templates.
 */
class EngineStepsPane( images: Seq[ImagePanel] ) extends JPanel{

  setLayout(new BorderLayout)

  object proxy extends ImagePanel{
    override def image() = if(_componentClicked != null) _componentClicked.image else null
    override def label() = if(_componentClicked != null) _componentClicked.label else "Click on any image panel"
    new Timer(100, new ActionListener(){
      def actionPerformed(e: ActionEvent){
        proxy.repaint()
      }
    }).start()
  }

  var _componentClicked : ImagePanel = null

  private val imagesPanel = new JPanel
  add(imagesPanel, BorderLayout.NORTH)
  add( proxy, BorderLayout.CENTER )

  imagesPanel.setLayout(new GridLayout(1, images.size))
  images.foreach( imagesPanel.add(_) )

  images.foreach( _.addMouseListener( new MouseAdapter(){
    override def mouseClicked(e : MouseEvent){
      _componentClicked = e.getComponent.asInstanceOf[ImagePanel]
      Log( s"Clicked on ${_componentClicked}")
      proxy.repaint()
    }
  }))

  imagesPanel.setPreferredSize( new Dimension(200,200) )
}

object EngineStepsPane{
  def apply( e: Engine ) = {
    val imagePanels = e.imageActors.map( a => ImagePanel(a, a.getClass.getName) )
    new EngineStepsPane(imagePanels)
  }
}
