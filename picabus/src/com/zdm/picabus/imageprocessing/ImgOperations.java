package com.zdm.picabus.imageprocessing;

import static com.googlecode.javacv.cpp.opencv_core.CV_FILLED;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_32F;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCopy;
import static com.googlecode.javacv.cpp.opencv_core.cvMinMaxLoc;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;
import static com.googlecode.javacv.cpp.opencv_core.cvReleaseImage;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;
import static com.googlecode.javacv.cpp.opencv_core.cvSetImageROI;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_TM_CCOEFF_NORMED;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvMatchTemplate;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;

import javax.annotation.Resource;


import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.zdm.picabus.R.drawable;




public class ImgOperations {
	/**
	 * Cropping function for IplImages
	 * 
	 * @param src
	 *            - an IplImage to crop
	 * @param coord
	 *            - an int array of size 4 containing coordinates to crop -
	 *            {lowY, highY, lowX, highX}
	 * @return cropped - an IplImage of the size specified by coord if all
	 *         coords are 0 - return null if width or height of the to be
	 *         cropped image is zero - returns the original image
	 */
	public static IplImage cropIpl(IplImage src, int[] coord) {

		int width = Math.abs(coord[3] - coord[2]);
		int height = Math.abs(coord[1] - coord[0]);
		int margin = 0;

		if ((coord[0] == 0) && (coord[1] == 0) && (coord[2] == 0)
				&& (coord[3] == 0)) {
			return null;
		}
		if ((width == 0) || (height == 0)) {
			return src;
		}
		CvRect rect = new CvRect(coord[2], coord[0], width + margin, height
				+ margin);
		IplImage cropped = IplImage.create(width + margin, height + margin,
				src.depth(), src.nChannels());

		cvSetImageROI(src, rect);
		cvCopy(src, cropped);

		return cropped;
	}

	/**
	 * Template matching function that uses cvMatchTemplate using
	 * CV_TM_CCOEFF_NORMED
	 * 
	 * @param img
	 *            - an iplimage to search the template in
	 * @param temp
	 *            - an iplimage to use as template and find in - img
	 * @return if the matching ratio (maxVal[0]) is in range - an array of
	 *         points where the two points are two edges of a rectangle where
	 *         the template was found at if doesn't match the ratio the points
	 *         will be null
	 * @see http://opencv.willowgarage.com/documentation/c/object_detection.html
	 *      http://www.aishack.in/2010/01/template-matching/
	 */
	public static CvPoint[] templateMatch(IplImage img, IplImage temp) {

		IplImage res;
		int imgW, imgH, tempW, tempH, resW, resH;

		CvPoint minloc = new CvPoint(1);
		CvPoint maxloc = new CvPoint(1);
		double[] minval = { 1 };
		double[] maxval = { 1 };

		CvPoint[] rect = new CvPoint[2];
		rect[0] = null;
		rect[1] = null;

		imgW = img.width();
		imgH = img.height();
		tempW = temp.width();
		tempH = temp.height();

		resW = imgW - tempW + 1;
		resH = imgH - tempH + 1;

		if ((resW <= 0) || (resH <= 0)) {
			return rect;
		} else {
			res = IplImage.create(resW, resH, IPL_DEPTH_32F, 1);

			cvMatchTemplate(img, temp, res, CV_TM_CCOEFF_NORMED); // CV_TM_CCORR_NORMED
																	// 0.955

			cvMinMaxLoc(res, minval, maxval, minloc, maxloc, null);

			rect[0] = cvPoint(maxloc.x(), maxloc.y());
			rect[1] = cvPoint(maxloc.x() + tempW, maxloc.y() + tempH);

			cvReleaseImage(res);

			if (maxval[0] > 0.62) // 0.74
			{
				// System.out.println("True MaxVal = " + maxval[0]);

			} else {
				rect[0] = null;
				rect[1] = null;
				// System.out.println("False MaxVal = " + maxval[0]);
			}

			return rect;

		}

	}

	/**
	 * The main Template matching function
	 * 
	 * @param img
	 *            - an iplimage - the specific line segment
	 * @param pathTemp
	 *            - the path for the templates folder this folder contains .jpg
	 *            files for every line number and their names contain the number
	 * @return an array containing all the line numbers that were found and
	 *         draws a rectangle over where the template were found
	 */
	public static int[] templateMatchLib(IplImage img, Context con) 
	{
		
		IplImage temp = null;
		String[] nameParsed = null;
		String name = null;
		CvPoint[] rectT = new CvPoint[2];

		int length = 0;
		int i = 0;;

		int[] line = new int[length * 7]; // the return line numbers
		CvPoint[][] rect = new CvPoint[length * 7][2]; // holds points for all
														// pictures detected
		int res;
		Field[] drawables = com.zdm.picabus.R.drawable.class.getFields();
		Drawable draw = null;
		
		
		for (Field f : drawables) // iterate over all files in the template folder
		{
		    try {
		       name =  f.getName();
		       nameParsed = name.split("_"); 
		       
		       if(nameParsed[0].equals("template")) //if template image
		       {
		    	   res = f.getInt(null);
		    	   draw = con.getResources().getDrawable(res);
		    	   Bitmap bitmap = ((BitmapDrawable)draw).getBitmap();
		    	   
		    	   temp = IplImage.create(bitmap.getWidth(), bitmap.getHeight(), IPL_DEPTH_8U ,3 );
		   		   bitmap.copyPixelsToBuffer(temp.getByteBuffer());		    	   		    	   
		    	
		    	// each template check 4 times on the picture to see if the template
					// is there more than once.
					// that way for a line numbered 244 we'll find the two '4' and not
					// only one
					// each time covers the result by a rectangle so it won't be found
					// again
					for (int n = 0; n < 4; n++) {

						// System.out.println("detection num: " + (n + 1) + " named: " +
						// name[0]);
						rectT = templateMatch(img, temp); // match the given template
															// with the picture

						rect[i][0] = rectT[0]; // get all the results in an array -
												// rect[][]
						rect[i][1] = rectT[1];

						if (rect[i][0] != null) // if the template was detected
						{

							line[i] = Integer.valueOf(nameParsed[0]); // enter the number
																// result
							i++;

							/*
							 * //CV_TM_CCORR_NORMED cvRectangle( img, cvPoint(
							 * minloc.x(), minloc.y() ), cvPoint( minloc.x() + tempW,
							 * minloc.y() + tempH ), cvScalar( 0, 0, 255, 0 ), 1, 0, 0
							 * );
							 */

							// draw a rectangle so the digit that was found wouldn't be
							// found again
							// CV_TM_CCOEFF_NORMED
							cvRectangle(img, rectT[0], rectT[1],
									cvScalar(0, 0, 255, 0), CV_FILLED, 0, 0); // 1
																				// instead
																				// of
																				// CV_FILLED
						} else {
							// if it wasn't found , stop searching
							n = 5;
						}
					}
						    	   
		    	   
		       }
		       

		    } 
		    catch (Exception e) 
		    {
		        e.printStackTrace();
		    }
		}
		
		

		

			


			

		filterPoints(rect, line);

		return line;
	}

	/**
	 * Helper method - removes any overlapping detections
	 * 
	 * @param rect
	 *            - rectangle points in the same indexes as lines in the line
	 *            array
	 * @param line
	 *            - all the line numbers detected
	 * @return changes the arrays by reference in any overlap put -1 as the line
	 *         number
	 */
	public static void filterPoints(CvPoint[][] rect, int[] line) {

		// this loop removes any overlapping detections
		for (int i = 0; i < rect.length; i++) {
			for (int j = i + 1; j < line.length; j++) {
				if ((rect[j][0] != null) && (rect[j][1] != null)
						&& (rect[i][0] != null) && (rect[i][1] != null)) {
					if ((rect[j][0].x() == rect[i][0].x())
							&& (rect[j][1].x() == rect[i][1].x())) {
						line[j] = -1;
					}
				}
			}
		}

	}

}
