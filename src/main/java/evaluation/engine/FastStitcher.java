package evaluation.engine;

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
public class FastStitcher extends Stitcher{


    private static final boolean DRAW_LINES = true;

    public FastStitcher(BufferedImage pattern) {
        super(pattern);
        setInputA(pattern);
    }

    /**
     * Detects features inside the two images and computes descriptions at those points.
     */
    private static <T extends ImageSingleBand, FD extends TupleDesc>
    void describeImage(T image,
                       DetectDescribePoint<T, FD> detDesc,
                       List<Point2D_F64> points,
                       FastQueue<FD> listDescs) {
        detDesc.detect(image);

        listDescs.reset();
        for (int i = 0; i < detDesc.getNumberOfFeatures(); i++) {
            points.add(detDesc.getLocation(i).copy());
            listDescs.grow().setTo(detDesc.getDescription(i));
        }
    }

    /**
     * Using abstracted code, find a transform which minimizes the difference between corresponding features
     * in both images.  This code is completely model independent and is the core algorithms.
     */
    private Homography2D_F64
    computeTransform(ImageFloat32 imageB,
                     DetectDescribePoint<ImageFloat32, SurfFeature> detDesc,
                     AssociateDescription<SurfFeature> associate,
                     ModelMatcher<Homography2D_F64, AssociatedPair> modelMatcher) {
        // get the length of the description
        List<Point2D_F64> pointsB = new ArrayList<Point2D_F64>();
        FastQueue<SurfFeature> descB = UtilFeature.createQueue(detDesc, 100);

        // extract feature locations and descriptions from each image
        describeImage(imageB, detDesc, pointsB, descB);

        // Associate features between the two images
        associate.setSource(descA);
        associate.setDestination(descB);
        associate.associate();

        // create a list of AssociatedPairs that tell the model matcher how a feature moved
        FastQueue<AssociatedIndex> matches = associate.getMatches();
        List<AssociatedPair> pairs = new ArrayList<AssociatedPair>();

        for (int i = 0; i < matches.size(); i++) {
            AssociatedIndex match = matches.get(i);

            Point2D_F64 a = pointsA.get(match.src);
            Point2D_F64 b = pointsB.get(match.dst);

            pairs.add(new AssociatedPair(a, b, false));
        }

        // find the best fit model to describe the change between these images
        if (!modelMatcher.process(pairs))
            throw new IllegalStateException( "No match found");

        // return the found image transform
        return modelMatcher.getModel().copy();
    }

    private ImageFloat32 inputA;
    private List<Point2D_F64> pointsA;
    private FastQueue<SurfFeature> descA;

    private void setInputA( BufferedImage pattern ){
        inputA = ConvertBufferedImage.convertFromSingle(pattern, null, ImageFloat32.class);
        pointsA = new ArrayList<Point2D_F64>();
        DetectDescribePoint detDesc = createDetectDescribePoint();
        descA = UtilFeature.createQueue(detDesc, 100);
        describeImage(inputA, detDesc, pointsA, descA);
    }


    private DetectDescribePoint createDetectDescribePoint(){
        ConfigFastHessian originalConfig = new ConfigFastHessian(1, 2, 200, 1, 9, 4, 4);
        ConfigFastHessian config = new ConfigFastHessian(1, 2, 200, 1, 9, 4, 4);
        DetectDescribePoint detDesc = FactoryDetectDescribe.surfStable(config, null, null, ImageFloat32.class);
        return detDesc;
    }

    /**
     */
    public Homography2D_F64 stitchHomography(BufferedImage imageB) {

        ImageFloat32 inputB = ConvertBufferedImage.convertFromSingle(imageB, null, ImageFloat32.class);


        // Detect using the standard SURF feature descriptor and describer
        DetectDescribePoint detDesc = createDetectDescribePoint();
        ScoreAssociation<SurfFeature> scorer = FactoryAssociation.scoreEuclidean(SurfFeature.class, true);
        AssociateDescription<SurfFeature> associate = FactoryAssociation.greedy(scorer, 2, true);

        // fit the images using a homography.  This works well for rotations and distant objects.
        GenerateHomographyLinear modelFitter = new GenerateHomographyLinear(true);
        DistanceHomographySq distance = new DistanceHomographySq();

        ModelMatcher<Homography2D_F64, AssociatedPair> modelMatcher =
                new Ransac<Homography2D_F64, AssociatedPair>(123, modelFitter, distance, 60, 9);

        Homography2D_F64 H = computeTransform(inputB, detDesc, associate, modelMatcher);

        return H;
    }

    public StitchResult stitch(BufferedImage image, boolean computeStichedImage) {
        try {
            Homography2D_F64 H = stitchHomography(image);
            BufferedImage ret = null;
            if( computeStichedImage ){
                ret = renderStitching(_pattern, image, H);
            }
            return new StitchResult(ret,H);
        }
        catch (IllegalStateException e) {
            System.err.println("Problem stitching image: " + e.toString());
            return null;
        }
    }
}
