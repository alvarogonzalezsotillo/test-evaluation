package evaluation.gui

import javax.swing.JPanel
import java.awt.{Dimension, GridLayout, BorderLayout}
import java.awt.Component
import java.awt.event.{MouseEvent, MouseAdapter}

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 31/12/13
 * Time: 17:33
 * To change this template use File | Settings | File Templates.
 */
class StepsPane( images: Seq[ImagePanel] ) extends JPanel{

  setLayout(new BorderLayout)

  object proxy extends ImagePanel{
    override def image() = if(_componentClicked != null) _componentClicked.image else null
    override def label() = if(_componentClicked != null) _componentClicked.label else "Click on any image panel"
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
    }
  }))

  imagesPanel.setPreferredSize( new Dimension(200,200) )
}
