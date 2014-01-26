package evaluation.gui

import javax.swing._
import java.awt.{Dimension, GridLayout, BorderLayout}
import java.awt.event.{MouseEvent, MouseAdapter}
import evaluation.engine._
import evaluation.Log
import scala.collection._


class MultipleImagesPane(images: Seq[Seq[Img]]) extends JPanel {

  val rows = images.size
  val columns = images(0).size

  setLayout( new GridLayout(columns,rows) )
  
  images.flatten.foreach( i => add( ImagePanel(i,"cropped") ) )
}

object MultipleImagesPane{
  def show( images: Seq[Seq[Img]] ) = {
    val p = new MultipleImagesPane(images)
    val f = new JFrame( "Images" )
    f add p
    f.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE)
    f.setSize(200,200)
    f.setVisible(true)
  }
}
