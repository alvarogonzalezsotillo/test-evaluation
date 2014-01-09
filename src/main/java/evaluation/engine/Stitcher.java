package evaluation.engine;

import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 27/12/13
 * Time: 23:36
 * To change this template use File | Settings | File Templates.
 */
public abstract class Stitcher {


    private static final boolean DRAW_LINES = true;
    protected final BufferedImage _pattern;

    protected Stitcher(BufferedImage pattern) {
        _pattern = pattern;
    }

    public static Stitcher create(BufferedImage pattern) {
        //return new SlowStitcher(pattern);
        return new FastStitcher(pattern);
    }

    public abstract BufferedImage stitch(BufferedImage image);
}
