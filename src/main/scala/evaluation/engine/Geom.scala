package evaluation.engine


object Geom {

  type Coord = Double

  class Point(val x: Coord, val y: Coord) extends Pair(x,y) {

    def +(p: Point) = Point(x + p.x, y + p.y)

    def *(f: Coord) = Point(x * f, y * f)

    def -(p: Point) = this + p * -1

    override def toString = s"($x,$y)"

    def toInts = (x.toInt,y.toInt)
  }

  object Point {
    def apply(x: Coord, y: Coord) = new Point(x, y)
  }

  class Rect(val left: Coord, val top: Coord, val width: Coord, val height: Coord) {

    def toInts = (left.toInt,top.toInt,width.toInt,height.toInt)
    def toIntCorners = (left.toInt,top.toInt,right.toInt,bottom.toInt)

    def +(p: Point) = Rect(left + p.x, top + p.y, width, height)

    def -(p: Point) = this + p * -1

    def center = Point(left + width/2, top + height/2 )
    
    def cornerPoints = IndexedSeq(
      Point(left,top),
      Point(right,top),
      Point(right,bottom),
      Point(left,bottom)
    )

    def moveCenter( p: Point ) = {
      val l = p.x - width/2
      val t = p.y - height/2
      Rect( l, t, width, height )
    }

    def +(r: Rect) = {
      val le = left min r.left
      val to = top min r.top
      val ri = right max r.right
      val bo = bottom max r.bottom

      Rect( le, to, ri - le, bo - to )
    }

    def grow(inc: Coord) = Rect(left - inc, top - inc, width + 2 * inc, height + 2 * inc)

    lazy val bottom = top + height
    lazy val right = left + width

    override def toString = s"($left,$top,$width,$height)"

    def inside( p: Point ) = p.x > left && p.x < right && p.y > top && p.y < bottom
  }

  object Rect {
    def apply(left: Coord, top: Coord, width: Coord, height: Coord) : Rect = new Rect(left, top, width, height)
    def apply( p1: Point, p2: Point ) : Rect= {
      val left = p1.x min p2.x
      val top = p1.y min p2.y
      val right = p1.x max p2.x
      val bottom = p1.y max p2.y
      Rect( left, top, right-left, bottom - top)
    }
  }

}
