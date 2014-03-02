package evaluation.gui.world

import evaluation.engine.Geom._

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 2/03/14
 * Time: 16:47
 * To change this template use File | Settings | File Templates.
 */
object ViewWorldCoordinates {

  abstract sealed class VPoint(x: Coord, y: Coord)(implicit val view: View) extends Point(x, y)

  object VPoint {

    class ConcreteVPoint(x: Coord, y: Coord)(implicit view: View) extends VPoint(x, y)

    def apply(x: Coord, y: Coord)(implicit v: View) = new ConcreteVPoint(x, y)

    def apply(p: DPoint)(implicit v: View) = {
      val t = v.transform
      val it = t.inverse.get
      val vp = it(p)
      new ConcreteVPoint(vp.x, vp.y)
    }
  }


  abstract sealed class DPoint(x: Coord, y: Coord) extends Point(x, y)

  object DPoint {

    class ConcreteDPoint(x: Coord, y: Coord) extends DPoint(x, y)

    def apply(x: Coord, y: Coord) = new ConcreteDPoint(x, y)

    def apply(p: VPoint) = {
      val t = p.view.transform
      val dp = t(p)
      new ConcreteDPoint(dp.x, dp.y)
    }
  }

  implicit def toD( vp: VPoint ) = DPoint(vp)
  implicit def toV( dp: DPoint )(implicit view: View ) = VPoint(dp)

}
