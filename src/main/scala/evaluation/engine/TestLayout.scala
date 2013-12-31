package evaluation.engine

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 29/12/13
 * Time: 23:09
 * To change this template use File | Settings | File Templates.
 */

import Geom._

object TestLayout {
  def apply(questionNumber: Int, optionNumber: Int) = new TestLayout(questionNumber, optionNumber)
}

class TestLayout(val questionNumber: Int, val optionNumber: Int) {

  val questionsPerLine = 5

  val factor = 3

  val lineSpacing = 5 * factor
  val boxHeight = 10 * factor
  val questionNumberColumnWidth = 20 * factor
  val questionNumberWidth = 15 * factor
  val answerWidth = 10 * factor
  val answerColumnWidth = 15 * factor


  lazy val questionWidth = questionNumberColumnWidth + answerColumnWidth * optionNumber


  private def questionRect(n: Int) = {
    assert(n < questionNumber)
    assert(n >= 0)
    val line = n / questionsPerLine
    val column = n % questionsPerLine
    val xOffset = questionWidth * column
    val yOffset = (lineSpacing + boxHeight) * line
    Rect(xOffset, yOffset, questionNumberWidth, boxHeight)
  }

  def answerOptionRect(n: Int, option: Int) = {
    assert(option < optionNumber)
    assert(option >= 0)
    val qr = questionRect(n)
    val xOffset = answerColumnWidth * option + questionNumberColumnWidth
    Rect(qr.left + xOffset, qr.top, answerWidth, boxHeight)
  }

  private def answerOptionHeaderRect(column: Int, option: Int) = {
    val ar = answerOptionRect(column, option)
    Rect(ar.left, ar.top - lineSpacing - boxHeight, answerWidth, boxHeight)
  }

  lazy val questionRects = (0 until questionNumber).map(questionRect(_))
  lazy val answerOptionRects = (0 until questionNumber).map(q =>
    (0 until optionNumber).map(answerOptionRect(q, _))
  )
  lazy val answerOptionHeaderRects = (0 until questionsPerLine).map(column =>
    (0 until optionNumber).map(answerOptionHeaderRect(column, _))
  )

  lazy val allRects = questionRects ++ answerOptionRects.flatten ++ answerOptionHeaderRects.flatten

  lazy val boundingBox = {
    val top = allRects.map(_.top).min
    val left = allRects.map(_.left).min
    val bottom = allRects.map(_.bottom).max
    val right = allRects.map(_.right).max
    Rect(left, top, right - left, bottom - top)
  }
}
