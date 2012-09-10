package com.zdm.picabus.imageprocessing;

import static com.googlecode.javacv.cpp.opencv_core.cvReleaseImage;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.googlecode.javacv.cpp.opencv_core.CvSize;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_INTER_LINEAR;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvResize;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;

public class costumizeImg 
{
		
	public static List<Integer> processImage(Context con) 
	{
		ColorDetector colordetected = null;
		IplImage origImage = null;
		IplImage image = null;
		
		origImage = cvLoadImage(Environment.getExternalStorageDirectory() + "/cameraAct.png");

		List<Integer> lineList = new ArrayList<Integer>();

		
		
		int lineNum[] = new int[20];
		

		IplImage cropped = null;
		IplImage line = null;		
		int [][] houghlines = null;

		
		if(origImage != null)
		{
			if((origImage.height() > 300) || (origImage.width() > 300))
			{
				//resize the image
				int scale = ((origImage.height() / 256) + (origImage.width() / 256)) / 2;
				CvSize size = new CvSize(origImage.width() / scale, origImage.height() / scale);
				image = cvCreateImage(size , origImage.depth(), origImage.nChannels());
				cvResize(origImage, image, CV_INTER_LINEAR);
			}
			else
			{  
				image = origImage;
			}
		
	        if (image != null) 
	        {
	            
	            //colordetected = ColorDetector.DetectColor(image, 1);
	                       
	              
	           
	            //cropped = ImgOperations.cropIpl(image, colordetected.getArr());
	            
	        	cropped = image;

				
	            //houghlines = HoughLines.HoughLines(cropped);
	            
	            //for(int i = 0; i < houghlines.length; i++)
	            for(int i = 0; i < 1; i++)
	            {
	            	line =  cropped; //ImgOperations.cropIpl(cropped, houghlines[i]);
	   			
	            	if(line != null)
	            	{
		            	lineNum = ImgOperations.templateMatchLib(line, con);
		            			            	
		            	for(int j = 0; j < lineNum.length; j++)
		            	{
		            		
		            		if((lineNum[j] > 0))
		            		{
		            			lineList.add(lineNum[j]);
		            		}
		            		
		            	}		   
	            	}
	          }
  
	            
	       
	       }//if (image != null)     
			
			
		}//if origImag!=null
	
        cvReleaseImage(cropped);
        cvReleaseImage(line); 
        //cvReleaseImage(colordetected.getImage());
        cvReleaseImage(image);
        cvReleaseImage(origImage);
        
        image = null;
        origImage = null;
        
        if(lineList.size() == 0)
        {
        	lineList = null;
        }
        
        return lineList;
        
        
	}

}

