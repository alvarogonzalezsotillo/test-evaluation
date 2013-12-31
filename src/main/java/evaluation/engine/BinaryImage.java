package evaluation.engine;

import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.ThresholdImageOps;
import boofcv.alg.misc.ImageStatistics;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.gui.binary.VisualizeBinaryData;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageUInt8;

import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 31/12/13
 * Time: 14:43
 * To change this template use File | Settings | File Templates.
 */
public class BinaryImage {


    public static BufferedImage toBinaryimage(BufferedImage image, boolean erodeAndDilate) {

        // convert into a usable format
        ImageFloat32 input = ConvertBufferedImage.convertFromSingle(image, null, ImageFloat32.class);
        ImageUInt8 binary = new ImageUInt8(input.width, input.height);

        // the mean pixel value is often a reasonable threshold when creating a binary image
        double mean = ImageStatistics.mean(input);

        // create a binary image by thresholding
        ThresholdImageOps.threshold(input, binary, (float) mean, false);

        ImageUInt8 filtered = binary;
        if (erodeAndDilate) {
            // remove small blobs through erosion and dilation
            // The null in the input indicates that it should internally declare the work image it needs
            // this is less efficient, but easier to code.
            filtered = BinaryImageOps.erode8(binary, null);
            filtered = BinaryImageOps.dilate8(filtered, null);
        }

        BufferedImage visualFiltered = VisualizeBinaryData.renderBinary(filtered, null);
        BufferedImage ret = new BufferedImage(visualFiltered.getWidth(),visualFiltered.getHeight(),BufferedImage.TYPE_INT_RGB);
        ret.getGraphics().drawImage(visualFiltered,0,0,null);

        return ret;
    }
}
