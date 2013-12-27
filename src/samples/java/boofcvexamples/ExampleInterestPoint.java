package boofcvexamples;

import java.awt.*;
import java.io.*;
import java.awt.image.*;
import georegression.struct.point.*;
import georegression.struct.*;
import georegression.*;
import boofcv.core.image.*;
import boofcv.core.*;
import boofcv.gui.feature.*;
import boofcv.struct.image.*;
import boofcv.abst.feature.detect.interest.*;
import boofcv.factory.feature.detect.interest.*;
import boofcv.struct.*;
import boofcv.gui.image.*;
import boofcv.io.image.*;


public class ExampleInterestPoint {
 
	public static <T extends ImageSingleBand>
	void detect( BufferedImage image , Class<T> imageType ) {
		T input = ConvertBufferedImage.convertFromSingle(image, null, imageType);
 
		// Create a Fast Hessian detector from the SURF paper.
		// Other detectors can be used in this example too.
		InterestPointDetector<T> detector = FactoryInterestPoint.fastHessian(new ConfigFastHessian(10, 2, 100, 2, 9, 3, 4));
 
		// find interest points in the image
		detector.detect(input);
 
		// Show the features
		displayResults(image, detector);
	}
 
	private static <T extends ImageSingleBand> void displayResults(BufferedImage image,
															 InterestPointDetector<T> detector)
	{
		Graphics2D g2 = image.createGraphics();
		FancyInterestPointRender render = new FancyInterestPointRender();
 
 
		for( int i = 0; i < detector.getNumberOfFeatures(); i++ ) {
			Point2D_F64 pt = detector.getLocation(i);
 
			// note how it checks the capabilities of the detector
			if( detector.hasScale() ) {
				double scale = detector.getScale(i);
				int radius = (int)(scale* BoofDefaults.SCALE_SPACE_CANONICAL_RADIUS);
				render.addCircle((int)pt.x,(int)pt.y,radius);
			} else {
				render.addPoint((int) pt.x, (int) pt.y);
			}
		}
		// make the circle's thicker
		g2.setStroke(new BasicStroke(3));
 
		// just draw the features onto the input image
		render.draw(g2);
		ShowImages.showWindow(image, "Detected Features");
	}
 
  public static void detect( String directory ){
    File dir = new File(directory);
    for( String f: dir.list() ){
      if( !f.toUpperCase().endsWith(".JPG") ){
        continue;
      }
      try{
        String fileName = new File(dir,f).getAbsolutePath();
        BufferedImage image = UtilImageIO.loadImage(fileName);
        detect(image, ImageFloat32.class);
      }
      catch(Exception e){
        System.out.println( "Trouble with " + f + ":" );
        e.printStackTrace(System.out);
      }
    }
  }
 
	public static void main( String args[] ) {
	  /*
	  String imageFile = "./src/testimages/interestPoint/img001.jpg";
		BufferedImage image = UtilImageIO.loadImage(imageFile);
		detect(image, ImageFloat32.class);
		*/
		detect( "./src/testimages/interestPoint" );
	}

}