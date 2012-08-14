package com.zdm.picabus.imageprocessing;

import static com.googlecode.javacv.cpp.opencv_core.cvReleaseImage;
import static com.googlecode.javacv.cpp.opencv_highgui.CV_WINDOW_AUTOSIZE;
import static com.googlecode.javacv.cpp.opencv_highgui.cvDestroyWindow;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvNamedWindow;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvShowImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvWaitKey;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_INTER_LINEAR;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvResize;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.zdm.picabus.imageprocessing.ColorDetector;



public class costumizeImg 
{

	public static List<Integer> processImage(Bitmap bitmap) 
	{
		
		IplImage origImage = IplImage.create(bitmap.getWidth(), bitmap.getHeight(), 8 ,3 );
		bitmap.copyPixelsToBuffer(origImage.getByteBuffer());
		
		List<Integer> lineList = new ArrayList<Integer>();
		
		IplImage cropped = null;
		IplImage line = null;
		IplImage image = null;
		ColorDetector colordetected = new ColorDetector(); 
		int [][] houghlines = null;
		
		int [] lineNum = null;
		
		String pathTemp = "d:\\temp\\TemplateMatch\\temp\\"; //template matching images root
		
		if(origImage != null)
		{
			if((origImage.height() > 500) || (origImage.width() > 500))
			{
				//resize the image
				int scale = ((origImage.height() / 256) + (origImage.width() / 256)) / 2;
				image = IplImage.create(origImage.width() / scale, origImage.height() / scale, origImage.depth(), origImage.nChannels());
				cvResize(origImage, image, CV_INTER_LINEAR);
			}
			else
			{  
				image = origImage;
			}
		
	        if (image != null) 
	        {
	            
	            colordetected = ColorDetector.DetectColor(image, 1);
	                       
	              
	           
	            cropped = ImgOperations.cropIpl(image, colordetected.getArr());
	            
	            //TODO: in case cropping and hogh didn't work well can use the orig image and try again
	            //then compare to see who has better results and output it
	            //cropped = image

				
	            houghlines = HoughLines.HoughLines(cropped);
	            
	            for(int i = 0; i < houghlines.length; i++)
	            {
	            	line = ImgOperations.cropIpl(cropped, houghlines[i]);
	   			
	            	if(line != null)
	            	{
		            	lineNum = ImgOperations.templateMatchLib(line, pathTemp);
		            			            	
		            	for(int j = 0; j < lineNum.length; j++)
		            	{
		            		
		            		if((lineNum[j] > 0))
		            		{
		            			lineList.add(lineNum[j]);
		            		}
		            		
		            	}
		            	

	            	}
	            }
	            
	            
	           
	            
	            

	            
	            
	            
	        }    
			
			
		}
		
        cvReleaseImage(image);
        cvReleaseImage(origImage);           
        cvReleaseImage(cropped);
        cvReleaseImage(line);
		return lineList;
	}

}

