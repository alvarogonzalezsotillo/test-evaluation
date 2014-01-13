package evaluation.gui

import javax.swing._
import java.awt.{Dimension, GridLayout, BorderLayout}
import java.awt.event.{MouseEvent, MouseAdapter}
import evaluation.engine._
import evaluation.Log
import scala.collection._


class MultipleImagesPane(images: Seq[Img]) extends JPanel {

  val rows = Iterator.from(1).find( v => v*v > images.size ).get
  val columns = images.size / rows

  setLayout( new GridLayout(columns,rows) )
  
  images.foreach( i => add( ImagePanel(i,"cropped") ) )
}

object MultipleImagesPane{
  def show( images: Seq[Img] ) = {
    val p = new MultipleImagesPane(images)
    val f = new JFrame( "Images" )
    f add p
    f.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE)
    f.setSize(200,200)
    f.setVisible(true)
  }
}
