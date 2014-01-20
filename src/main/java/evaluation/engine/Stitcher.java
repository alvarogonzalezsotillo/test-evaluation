package evaluation.engine;

import java.awt.image.BufferedImage;
import georegression.struct.homo.Homography2D_F64;
import boofcv.abst.feature.associate.AssociateDescription;
import boofcv.abst.feature.associate.ScoreAssociation;
import boofcv.abst.feature.detdesc.DetectDescribePoint;
import boofcv.abst.feature.detect.interest.ConfigFastHessian;
import boofcv.alg.distort.ImageDistort;
import boofcv.alg.distort.PixelTransformHomography_F32;
import boofcv.alg.distort.impl.DistortSupport;
import boofcv.alg.feature.UtilFeature;
import boofcv.alg.interpolate.impl.ImplBilinearPixel_F32;
import boofcv.alg.sfm.robust.DistanceHomographySq;
import boofcv.alg.sfm.robust.GenerateHomographyLinear;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.factory.feature.associate.FactoryAssociation;
import boofcv.factory.feature.detdesc.FactoryDetectDescribe;
import boofcv.struct.FastQueue;
import boofcv.struct.feature.AssociatedIndex;
import boofcv.struct.feature.SurfFeature;
import boofcv.struct.feature.TupleDesc;
import boofcv.struct.geo.AssociatedPair;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageSingleBand;
import boofcv.struct.image.MultiSpectral;
import georegression.struct.homo.Homography2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point2D_I32;
import georegression.transform.homo.HomographyPointOps_F64;
import org.ddogleg.fitting.modelset.ModelMatcher;
import org.ddogleg.fitting.modelset.ransac.Ransac;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

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
    
    public BufferedImage pattern(){
      return _pattern;
    }

    public abstract StitchResult stitch(BufferedImage image, boolean computeStichedImage);
    

    /**
     * Renders and displays the stitched together images
     */
    public static BufferedImage renderStitching(BufferedImage imageA, BufferedImage imageB,
                                                Homography2D_F64 fromAtoB) {
        // specify size of output image
        double scale = .5;
        int outputWidth = (int) (scale*imageA.getWidth());
        int outputHeight = (int) (scale*imageA.getHeight());

        // Convert into a BoofCV color format
        MultiSpectral<ImageFloat32> colorA = ConvertBufferedImage.convertFromMulti(imageA, null, ImageFloat32.class);
        MultiSpectral<ImageFloat32> colorB = ConvertBufferedImage.convertFromMulti(imageB, null, ImageFloat32.class);

        // Where the output images are rendered into
        MultiSpectral<ImageFloat32> work = new MultiSpectral<ImageFloat32>(ImageFloat32.class, outputWidth, outputHeight, 3);

        // Adjust the transform so that the whole image can appear inside of it
        Homography2D_F64 fromAToWork = new Homography2D_F64(scale, 0, 0, 0, scale, 0, 0, 0, 1);
        Homography2D_F64 fromWorkToA = fromAToWork.invert(null);

        // Used to render the results onto an image
        PixelTransformHomography_F32 model = new PixelTransformHomography_F32();
        ImageDistort<MultiSpectral<ImageFloat32>> distort =
                DistortSupport.createDistortMS(ImageFloat32.class, model, new ImplBilinearPixel_F32(), null);

        // Render first image
        model.set(fromWorkToA);
        distort.apply(colorA, work);

        // Render second image
        Homography2D_F64 fromWorkToB = fromWorkToA.concat(fromAtoB, null);
        model.set(fromWorkToB);
        distort.apply(colorB, work);

        // Convert the rendered image into a BufferedImage
        BufferedImage output = new BufferedImage(work.width, work.height, BufferedImage.TYPE_INT_RGB);
        ConvertBufferedImage.convertTo(work, output);

        Graphics2D g2 = output.createGraphics();

        // draw lines around the distorted image to make it easier to see
        if (DRAW_LINES) {
            Homography2D_F64 fromBtoWork = fromWorkToB.invert(null);
            Point2D_I32 corners[] = new Point2D_I32[4];
            corners[0] = renderPoint(0, 0, fromBtoWork);
            corners[1] = renderPoint(colorB.width, 0, fromBtoWork);
            corners[2] = renderPoint(colorB.width, colorB.height, fromBtoWork);
            corners[3] = renderPoint(0, colorB.height, fromBtoWork);

            g2.setColor(Color.ORANGE);
            g2.setStroke(new BasicStroke(4));
            g2.drawLine(corners[0].x, corners[0].y, corners[1].x, corners[1].y);
            g2.drawLine(corners[1].x, corners[1].y, corners[2].x, corners[2].y);
            g2.drawLine(corners[2].x, corners[2].y, corners[3].x, corners[3].y);
            g2.drawLine(corners[3].x, corners[3].y, corners[0].x, corners[0].y);
        }

        g2.dispose();
        return output;
    }

    protected static Point2D_I32 renderPoint(int x0, int y0, Homography2D_F64 fromBtoWork) {
        Point2D_F64 result = new Point2D_F64();
        HomographyPointOps_F64.transform(fromBtoWork, new Point2D_F64(x0, y0), result);
        return new Point2D_I32((int) result.x, (int) result.y);
    }
    
    public static java.awt.Point transformPoint( int x, int y, Homography2D_F64 t ){
      Point2D_I32 p = renderPoint(x,y,t);
      return new java.awt.Point( (int)p.x, (int)p.y );
    }
    
}
