package com.zdm.picabus.imageprocessing;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvReleaseImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_INTER_LINEAR;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvResize;

import java.util.ArrayList;
import java.util.List;



import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

//public class MyActivity extends Activity implements HelperCallbackInterface
//{
//private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(this) {
//@Override
//public void onManagerConnected(int status) {
//   switch (status) {
//       case LoaderCallbackInterface.SUCCESS:
//       {
//      Log.i(TAG, "OpenCV loaded successfully");
//      // Create and set View
//      mView = new puzzle15View(mAppContext);
//      setContentView(mView);
//       } break;
//       default:
//       {
//      super.onManagerConnected(status);
//       } break;
//   }
//    }
//};
//
///** Called when the activity is first created. */
//@Override
//public void onCreate(Bundle savedInstanceState)
//{
//    Log.i(TAG, "onCreate");
//    super.onCreate(savedInstanceState);
//
//    Log.i(TAG, "Trying to load OpenCV library");
//    if (!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_2, this, mOpenCVCallBack))
//    {
//      Log.e(TAG, "Cannot connect to OpenCV Manager");
//    }
//}
//
//// ...
//}

public class costumizeImg 
{

	public static List<Integer> processImage(Bitmap bitmap, Context con) 
	{
		IplImage origImage = null;
		origImage = IplImage.create(bitmap.getWidth(), bitmap.getHeight(), IPL_DEPTH_8U ,3 );
		bitmap.copyPixelsToBuffer(origImage.getByteBuffer());
		
		List<Integer> lineList = new ArrayList<Integer>();
	
        
		IplImage cropped = null;
		IplImage line = null;
		IplImage image = null;
		//ColorDetector colordetected = new ColorDetector(); 
		int [][] houghlines = null;
		
		int [] lineNum = null;
		
		//String pathTemp = "d:\\temp\\TemplateMatch\\temp\\"; //template matching images root
		
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
	            
	           // colordetected = ColorDetector.DetectColor(image, 1);
	                       
	              
	           
	            //cropped = ImgOperations.cropIpl(image, colordetected.getArr());
	            
	            //TODO: in case cropping and hogh didn't work well can use the orig image and try again
	            //then compare to see who has better results and output it
	            
	        	cropped = image;

				
	            houghlines = HoughLines.HoughLines(cropped);
	            
	            for(int i = 0; i < houghlines.length; i++)
	            {
	            	line = ImgOperations.cropIpl(cropped, houghlines[i]);
	   			
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
	
        
        
        cvReleaseImage(image);
        cvReleaseImage(cropped);
        cvReleaseImage(line);
	
        cvReleaseImage(origImage);
        lineList = null;
        return lineList;
        
        
	}

}

