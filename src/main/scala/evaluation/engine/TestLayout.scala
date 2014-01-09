package evaluation.engine

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 29/12/13
 * Time: 23:09
 * To change this template use File | Settings | File Templates.
 */

import Geom._
import TestLayout.choose

object TestLayout {
  def apply(questionNumber: Int, optionNumber: Int, includeMargin: Boolean = false) = new TestLayout(questionNumber, optionNumber)

  implicit class choose( b: Boolean ) extends AnyRef{
    def -->[T]( options : Pair[T,T] ) = if( b ) options._1 else options._2
    def -->[T]( option: T ) = if( b ) option else 0.asInstanceOf[T]
  }

}



class TestLayout(val questionNumber: Int, val optionNumber: Int, includeMargin: Boolean = false){

  val questionsPerLine = 5

  val factor = 3

  val lineSpacing = 5 * factor
  val boxHeight = 10 * factor
  val questionNumberColumnWidth = 20 * factor
  val questionNumberWidth = 15 * factor
  val answerWidth = 10 * factor
  val answerColumnWidth = 15 * factor


  val questions = (0 until questionNumber)
  val columns = (0 until questionsPerLine)
  val rows = (0 until questionNumber/questionsPerLine)

  lazy val questionWidth = questionNumberColumnWidth + answerColumnWidth * optionNumber


  private def questionRect(n: Int) = {
    assert(n < questionNumber)
    assert(n >= 0)
    val line = n / questionsPerLine
    val column = n % questionsPerLine
    val xOffset = questionWidth * column
    val yOffset = (lineSpacing + boxHeight) * line
    Rect(xOffset, yOffset, questionNumberWidth + (includeMargin --> lineSpacing), boxHeight + (includeMargin --> lineSpacing))
  }


  def answerOptionRect(n: Int, option: Int ) = {
    assert(option < optionNumber)
    assert(option >= 0)
    val qr = questionRect(n)
    val xOffset = answerColumnWidth * option + questionNumberColumnWidth
    Rect(qr.left + xOffset, qr.top, answerWidth + (includeMargin --> lineSpacing), boxHeight + (includeMargin --> lineSpacing))
  }

  private def answerOptionHeaderRect(column: Int, option: Int ) = {
    val ar = answerOptionRect(column, option)
    Rect(ar.left, ar.top - lineSpacing - boxHeight, answerWidth + (includeMargin --> lineSpacing), boxHeight + (includeMargin --> lineSpacing))
  }

  lazy val questionRects = (0 until questionNumber).map(questionRect(_))
  lazy val answerOptionRects = (0 until questionNumber).map(q =>
    (0 until optionNumber).map(answerOptionRect(q, _))
  )
  lazy val answerOptionHeaderRects = (0 until questionsPerLine).map(column =>
    (0 until optionNumber).map(answerOptionHeaderRect(column, _))
  )

  lazy val allRects = questionRects ++ answerOptionRects.flatten ++ answerOptionHeaderRects.flatten

  def columnOfQuestion( n: Int ) = n % questionsPerLine

  lazy val questionColumnsRect = {
    val columns = questions.groupBy(columnOfQuestion)
    (n:Int) => columns(n).map( questionRect )
  }

  lazy val boundingBox = {
    val top = allRects.map(_.top).min
    val left = allRects.map(_.left).min
    val bottom = allRects.map(_.bottom).max
    val right = allRects.map(_.right).max
    Rect(left, top, right - left, bottom - top)
  }
}
