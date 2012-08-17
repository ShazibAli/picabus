package com.zdm.picabus.imageprocessing;

import static com.googlecode.javacv.cpp.opencv_core.CV_AA;
import static com.googlecode.javacv.cpp.opencv_core.CV_FILLED;
import static com.googlecode.javacv.cpp.opencv_core.CV_RGB;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateMemStorage;
import static com.googlecode.javacv.cpp.opencv_core.cvDrawCircle;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSeqElem;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_core.cvSize;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_GRAY2BGR;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_HOUGH_STANDARD;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_RGB2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCanny;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvHoughLines2;

import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvPoint2D32f;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


/**
 * Houghlines Class is used to Detect the sections  in the input img.
 * that way the ROI could be found  
 */
public class HoughLines {

    /**
     * usage: java HoughLines imageDir\imageName TransformType
     */
	/**
	* Detect the horizontal lines in the image using cvHoughLines2 - the standard hough transform
	* @param img  - IplImage
	* @return an array containing coordinates of the segments depicted by the lines detected
	* continuous (coords) and filtered from any noise results
	* {lowY, highY, lowX, highX} 
	* @see http://docs.opencv.org/doc/tutorials/imgproc/imgtrans/hough_lines/hough_lines.html
	*/
    public static int[][] HoughLines(IplImage img) {
  	
    	int width = img.width();
    	int height = img.height();
  
    	IplImage src = cvCreateImage( cvSize( width, height ), IPL_DEPTH_8U, 1 );
    	cvCvtColor( img, src, CV_RGB2GRAY );
    	
    	
    	src.colorModel(0);
        IplImage dst;
        IplImage colorDst;
        CvMemStorage storage = cvCreateMemStorage(0);
        CvSeq lines = new CvSeq();

        
        dst = cvCreateImage(cvGetSize(src), src.depth(), 1);
        colorDst = cvCreateImage(cvGetSize(src), src.depth(), 3);

        cvCanny(src, dst, 50, 200, 3);
        cvCvtColor(dst, colorDst, CV_GRAY2BGR);

     
        /*
         * Default: apply the standard hough transform. Outputs: same as the multiscale output.
         */
            lines = cvHoughLines2(dst, storage, CV_HOUGH_STANDARD, 1, Math.PI / 180, 90, 0, 0);
            
            int [][] coord = new int[lines.total() + 1][4]; // return coordinated of lines
            int minCH;
            
            //init the first y value to zero so it will start from the top of the sign
            coord[0][0] = 0; //enter the y coord of the lines - for cropping
            coord[0][1] = 0; // will be delt later
            coord[0][2] = 0; // x coord 
            coord[0][3] = width; // x coord 
            
            for (int i = 1; i < lines.total() + 1; i++) {
                CvPoint2D32f point = new CvPoint2D32f(cvGetSeqElem(lines, i - 1));

                float rho=point.x();
                float theta=point.y();

                double a = Math.cos((double) theta), b = Math.sin((double) theta);
                double x0 = a * rho, y0 = b * rho;
                CvPoint pt1 = new CvPoint((int) Math.round(x0 + 1000 * (-b)), (int) Math.round(y0 + 1000 * (a)));
                CvPoint pt2 = new CvPoint((int) Math.round(x0 - 1000 * (-b)), (int) Math.round(y0 - 1000 * (a)));
//                System.out.println("Line spotted: ");
//                System.out.println("\t rho= " + rho);
//                System.out.println("\t theta= " + theta);
                
                CvPoint ptt = new CvPoint(20 , (int) y0);
                
                //cvLine(colorDst, pt1, pt2, CV_RGB(255, 255, 0), 3, CV_AA, 0);
                cvDrawCircle(colorDst, ptt, 5, CV_RGB(255, 255, 0), CV_FILLED, 8, 0);
                cvDrawCircle(colorDst, pt2, 50, CV_RGB(255, 0, 0), 3, CV_AA, 0);
                
                coord[i][0] = (int) y0; //enter the y coord of the lines - for cropping
                coord[i][1] = 0; // will be delt later
                coord[i][2] = 0; // x coord 
                coord[i][3] = width; // x coord 
                
                if(coord[i][0] < 0)
                {
                	coord[i][0] = 0;
                }
                
                //for debugging
//                System.out.println("Line spotted: ");
//                System.out.println("\t y= " + coord[i][0]);
/*               System.out.println("Line spotted " + i + ": ");
                //System.out.println("pt1.y= "+ (pt1.y()) + " pt2.y=" + (pt2.y()));
                System.out.println("y0:" + x0 +" height - y0:" +(height - y0));
                //System.out.println("a:" + a + " b:" + b);
 */               
            }


        //sort the values
        for (int i = 0; i < lines.total() + 1; i++) 
        {
        	for (int j = 0; j < lines.total(); j++) 
            {
        		if(coord[j][0] > coord[j + 1][0])
        		{
        			int temp = coord[j + 1][0];
        			coord[j + 1][0] = coord[j][0];
        			coord[j][0] = temp;
        		}
            	
            }
        }
        
        int j = 0;
        
        //take care of other y coord for crop by using the delta between lines
        for (int i = 1; i < lines.total() + 1; i++) 
        {
        	coord[i - 1][1] = coord[i][0];
        	if((j == 0) && (coord[i][0] != 0)) //first place where it's not zero
        	{
        		j = i;
        	}
        }
        
        coord[lines.total()][1] = height; //last coord is the end of the image
        
        int len = (lines.total()) + 2 - j;
        
        int [][] coordRet = new int[len][4]; 
        
        minCH = height / 10; //min crop height
        
        int zero = 0; //number of zero coords in array - so we'll know how many to clean up
        
        //copy the array //if cropping coords are too small (minCH) make them zeros
        for (int i = j - 1, m = 0; i < lines.total() + 1; i++, m++) 
        {
        	if((coord[i][1] - coord[i][0]) > minCH)
        	{
	        	for (int n = 0; n < 4; n++) 
	            {
	        		
	        		coordRet[m][n] = coord[i][n];
	            }
        	}
        	else
        	{
        		zero++; // count how many we zeroed out
        		for (int n = 0; n < 4; n++) 
	            {
	        		
	        		coordRet[m][n] = 0;
	            }
        	}
        }
        
        int [][] coordClean = new int[len - zero][4];
        
        //clean the zeros
        for(int i = 0, m = 0; i < len; i++)
        {
        	if(coordRet[i][1] != 0)
        	{
	        	for (int n = 0; n < 4; n++) 
	            {
	        		coordClean[m][n] = coordRet[i][n];
	            }
	        	m++;
        	}
        }
        
        //make cropping consistent so there will no gaps in between cropped sections
        for(int i =  0; i < (len - zero - 1); i++)
        {
        	coordClean[i][1] = coordClean[i + 1][0] + 3;
        	coordClean[i + 1][0] = coordClean[i + 1][0] - 3;
        }
        
        
        
        return coordClean;
    }
}