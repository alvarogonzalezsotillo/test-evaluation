/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://boofcv.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package boofcvexamples;

import boofcv.struct.image.*;
import boofcv.alg.filter.binary.*;
import boofcv.alg.misc.*;
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
import boofcv.gui.image.ShowImages;
import boofcv.io.image.UtilImageIO;
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
 * <p> Exampling showing how to combines two images together by finding the best fit image transform with point
 * features.</p>
 * <p>
 * Algorithm Steps:<br>
 * <ol>
 * <li>Detect feature locations</li>
 * <li>Compute feature descriptors</li>
 * <li>Associate features together</li>
 * <li>Use robust fitting to find transform</li>
 * <li>Render combined image</li>
 * </ol>
 * </p>
 *
 * @author Peter Abeles
 */
public class ExampleImageStitching {

  public static final boolean DRAW_LINES = false;

	/**
	 * Using abstracted code, find a transform which minimizes the difference between corresponding features
	 * in both images.  This code is completely model independent and is the core algorithms.
	 */
	public static<T extends ImageSingleBand, FD extends TupleDesc> Homography2D_F64
	computeTransform( T imageA , T imageB ,
					  DetectDescribePoint<T,FD> detDesc ,
					  AssociateDescription<FD> associate ,
					  ModelMatcher<Homography2D_F64,AssociatedPair> modelMatcher )
	{
		// get the length of the description
		List<Point2D_F64> pointsA = new ArrayList<Point2D_F64>();
		FastQueue<FD> descA = UtilFeature.createQueue(detDesc,100);
		List<Point2D_F64> pointsB = new ArrayList<Point2D_F64>();
		FastQueue<FD> descB = UtilFeature.createQueue(detDesc,100);

		// extract feature locations and descriptions from each image
		describeImage(imageA, detDesc, pointsA, descA);
		describeImage(imageB, detDesc, pointsB, descB);

		// Associate features between the two images
		associate.setSource(descA);
		associate.setDestination(descB);
		associate.associate();

		// create a list of AssociatedPairs that tell the model matcher how a feature moved
		FastQueue<AssociatedIndex> matches = associate.getMatches();
		List<AssociatedPair> pairs = new ArrayList<AssociatedPair>();

		for( int i = 0; i < matches.size(); i++ ) {
			AssociatedIndex match = matches.get(i);

			Point2D_F64 a = pointsA.get(match.src);
			Point2D_F64 b = pointsB.get(match.dst);

			pairs.add( new AssociatedPair(a,b,false));
		}

		// find the best fit model to describe the change between these images
		if( !modelMatcher.process(pairs) )
			throw new RuntimeException("Model Matcher failed!");

		// return the found image transform
		return modelMatcher.getModel().copy();
	}

	/**
	 * Detects features inside the two images and computes descriptions at those points.
	 */
	private static <T extends ImageSingleBand, FD extends TupleDesc>
	void describeImage(T image,
					   DetectDescribePoint<T,FD> detDesc,
					   List<Point2D_F64> points,
					   FastQueue<FD> listDescs) {
		detDesc.detect(image);

		listDescs.reset();
		for( int i = 0; i < detDesc.getNumberOfFeatures(); i++ ) {
			points.add( detDesc.getLocation(i).copy() );
			listDescs.grow().setTo(detDesc.getDescription(i));
		}
	}
	
	
	public static ImageUInt8 improve(ImageFloat32 input){
    double mean = ImageStatistics.mean(input);
    ImageUInt8 ret = ThresholdImageOps.threshold(input,null,(float)mean,true);
    return ret;
	}

	/**
	 * Given two input images create and display an image where the two have been overlayed on top of each other.
	 */
	public static <T extends ImageSingleBand>
	void stitch( BufferedImage imageA , BufferedImage imageB , Class<T> imageType, String title )
	{
		T inputA = ConvertBufferedImage.convertFromSingle(imageA, null, imageType);
		T inputB = ConvertBufferedImage.convertFromSingle(imageB, null, imageType);


		// Detect using the standard SURF feature descriptor and describer
		ConfigFastHessian originalConfig = new ConfigFastHessian(1, 2, 200, 1, 9, 4, 4);
		ConfigFastHessian config = new ConfigFastHessian(1, 20, 200, 1, 9, 4, 4);
		DetectDescribePoint detDesc = FactoryDetectDescribe.surfStable(originalConfig, null,null, imageType);
		ScoreAssociation<SurfFeature> scorer = FactoryAssociation.scoreEuclidean(SurfFeature.class,true);
		AssociateDescription<SurfFeature> associate = FactoryAssociation.greedy(scorer,2,true);

		// fit the images using a homography.  This works well for rotations and distant objects.
		GenerateHomographyLinear modelFitter = new GenerateHomographyLinear(true);
		DistanceHomographySq distance = new DistanceHomographySq();

		ModelMatcher<Homography2D_F64,AssociatedPair> modelMatcher =
				new Ransac<Homography2D_F64,AssociatedPair>(123,modelFitter,distance,60,9);

		Homography2D_F64 H = computeTransform(inputA, inputB, detDesc, associate, modelMatcher);

		renderStitching(imageA,imageB,H, title);
	}

	/**
	 * Renders and displays the stitched together images
	 */
	public static void renderStitching( BufferedImage imageA, BufferedImage imageB , 
										Homography2D_F64 fromAtoB, String title )
	{
		// specify size of output image
		double scale = 0.5;
		int outputWidth = imageA.getWidth();
		int outputHeight = imageA.getHeight();

		// Convert into a BoofCV color format
		MultiSpectral<ImageFloat32> colorA = ConvertBufferedImage.convertFromMulti(imageA, null, ImageFloat32.class);
		MultiSpectral<ImageFloat32> colorB = ConvertBufferedImage.convertFromMulti(imageB, null, ImageFloat32.class);

		// Where the output images are rendered into
		MultiSpectral<ImageFloat32> work = new MultiSpectral<ImageFloat32>(ImageFloat32.class,outputWidth,outputHeight,3);

		// Adjust the transform so that the whole image can appear inside of it
		Homography2D_F64 fromAToWork = new Homography2D_F64(scale,0,colorA.width/4,0,scale,colorA.height/4,0,0,1);
		Homography2D_F64 fromWorkToA = fromAToWork.invert(null);

		// Used to render the results onto an image
		PixelTransformHomography_F32 model = new PixelTransformHomography_F32();
		ImageDistort<MultiSpectral<ImageFloat32>> distort =
				DistortSupport.createDistortMS(ImageFloat32.class, model, new ImplBilinearPixel_F32(), null);

		// Render first image
		model.set(fromWorkToA);
		distort.apply(colorA,work);

		// Render second image
		Homography2D_F64 fromWorkToB = fromWorkToA.concat(fromAtoB,null);
		model.set(fromWorkToB);
		distort.apply(colorB,work);

		// Convert the rendered image into a BufferedImage
		BufferedImage output = new BufferedImage(work.width,work.height,imageA.getType());
		ConvertBufferedImage.convertTo(work,output);

		Graphics2D g2 = output.createGraphics();

		// draw lines around the distorted image to make it easier to see
		if( DRAW_LINES ){
      Homography2D_F64 fromBtoWork = fromWorkToB.invert(null);
      Point2D_I32 corners[] = new Point2D_I32[4];
      corners[0] = renderPoint(0,0,fromBtoWork);
      corners[1] = renderPoint(colorB.width,0,fromBtoWork);
      corners[2] = renderPoint(colorB.width,colorB.height,fromBtoWork);
      corners[3] = renderPoint(0,colorB.height,fromBtoWork);

      g2.setColor(Color.ORANGE);
      g2.setStroke(new BasicStroke(4));
      g2.drawLine(corners[0].x,corners[0].y,corners[1].x,corners[1].y);
      g2.drawLine(corners[1].x,corners[1].y,corners[2].x,corners[2].y);
      g2.drawLine(corners[2].x,corners[2].y,corners[3].x,corners[3].y);
      g2.drawLine(corners[3].x,corners[3].y,corners[0].x,corners[0].y);
    }

		ShowImages.showWindow(output,"Stitched Images:" + title);
	}

	private static Point2D_I32 renderPoint( int x0 , int y0 , Homography2D_F64 fromBtoWork )
	{
		Point2D_F64 result = new Point2D_F64();
		HomographyPointOps_F64.transform(fromBtoWork, new Point2D_F64(x0, y0), result);
		return new Point2D_I32((int)result.x,(int)result.y);
	}

	public static void main( String args[] ) {
		BufferedImage imageA,imageB;
		
		
    imageA = UtilImageIO.loadImage("./src/testimages/stitch/borders-00.jpg");
		for( int i = 1 ; i <= 10 ; i++  ){
      String suffix = String.format("%02d",i);
      String filename= "./src/testimages/stitch/borders-" + suffix+ ".jpg";
      imageB = UtilImageIO.loadImage(filename);
      try{
        stitch(imageA,imageB, ImageFloat32.class, filename);
      }
      catch( Exception e ){
        System.out.println( filename );
        e.printStackTrace(System.out);
      }
    }
		/*
		imageA = UtilImageIO.loadImage("../data/evaluation/stitch/kayak_01.jpg");
		imageB = UtilImageIO.loadImage("../data/evaluation/stitch/kayak_03.jpg");
		stitch(imageA,imageB, ImageFloat32.class);
		imageA = UtilImageIO.loadImage("../data/evaluation/scale/rainforest_01.jpg");
		imageB = UtilImageIO.loadImage("../data/evaluation/scale/rainforest_02.jpg");
		stitch(imageA,imageB, ImageFloat32.class);
		*/
	}
}
