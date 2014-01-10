package evaluation.engine;

import java.awt.image.BufferedImage;
import georegression.struct.homo.Homography2D_F64;

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 27/12/13
 * Time: 23:36
 * To change this template use File | Settings | File Templates.
 */
public abstract class Stitcher {

    public static class StitchResult{
        private BufferedImage _image;
        private Homography2D_F64 _homography;
        
        public StitchResult(BufferedImage i, Homography2D_F64 h){
            _image = i;
            _homography = h;
        }
        
        public BufferedImage image(){
          return _image;
        }
        
        public Homography2D_F64 homography(){
            return _homography;
        }
    }


    private static final boolean DRAW_LINES = true;
    protected final BufferedImage _pattern;

    protected Stitcher(BufferedImage pattern) {
        _pattern = pattern;
    }

    public static Stitcher create(BufferedImage pattern) {
        //return new SlowStitcher(pattern);
        return new FastStitcher(pattern);
    }

    public abstract StitchResult stitch(BufferedImage image);
}
