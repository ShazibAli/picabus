package com.zdm.picabus.imageprocessing;

import static com.googlecode.javacv.cpp.opencv_highgui.CV_WINDOW_AUTOSIZE;
import static com.googlecode.javacv.cpp.opencv_highgui.cvNamedWindow;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvShowImage;
/*
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;*/
import java.nio.ByteBuffer;


//import sun.text.normalizer.UBiDiProps;

import android.graphics.Color;

import com.googlecode.javacv.cpp.opencv_core.IplImage;
/**
 * ColorDetector Class contains methods regarding color detection
 * and object castings
 * 
 */

public class ColorDetector 
{

	static int [] arr = null;
	static IplImage image = null;
	
	private static int GetColor(IplImage img, int x, int y)
	{
		int location = img.widthStep() * y + (x * 3);
		
		ByteBuffer buffer = img.getByteBuffer();
		int b = (int)buffer.get(location);
		b = b < 0 ? b + 256 : b;
		
		int g = (int)buffer.get(location + 1);
		g = g < 0 ? g + 256 : g;

		int r = (int)buffer.get(location + 2);
		r = r < 0 ? r + 256 : r;
		
		int result = Color.rgb(r, g, b);
		return result;
	}
	
	private static void SetColor(IplImage img, int x, int y, int c)
	{
		int location = img.widthStep() * y + (x * 3);
		
		ByteBuffer buffer = img.getByteBuffer();
		
		byte b = (byte)Color.blue(c);
		buffer.put(location, b);

		byte g = (byte)Color.green(c);
		buffer.put(location + 1, g);

		byte r = (byte)Color.red(c);
		buffer.put(location + 2, r);
	}
	/**
	 * The algorithm converts the input image to a binary by checking is pixel color is within a given distance from a desired color.
	 * Pixels with color close to the desired color are white, other black.
	 *
	 * To make operations on image pixels easier and more efficient
	 * OpenCV color image is converted to ImageJ representation during processing.
	* @param img - the input iplImage - after resize
	* @param bCrop - a flag if 1 then crop - if 0 don't crop
	* @return a ColorDetector object containing the result image and an array with coords of the detected area
	*/
	public static ColorDetector DetectColor(IplImage img, int bCrop)
	{
//		BufferedImage buffDest = null;
		ColorDetector res = new ColorDetector();
		 // Convert to ImageJ's ColorProcessor for easier pixel access
		//ColorProcessor src = toColorProcessor(img);

        // Create output image
		//ByteProcessor dest = new ByteProcessor(src.getWidth(), src.getHeight());
		
		////////////to be configed
		//target color
		int myRGB; //= new Color(250, 250, 5);
		//distance from target color - myRGB
		//int[] delta = {100 , 100, 100}; // R,G,B
				
		/////////////
		
		myRGB = getSignColor(img);
		
		//IplImage dst = IplImage.create(img.width(), img.height(), img.depth(), img.nChannels());
		IplImage dst = img.clone();
		
		
		// Iterate through pixels and check if their distance from the target color is
        // Within the distance threshold, if it is set `dest` to 255.
		for(int y = 0; y < img.height(); y++)
		{
			for(int x = 0; x < img.width(); x++)
			{
				if (distance(GetColor(img, x, y), myRGB) )
				{
					SetColor(dst, x, y, Color.rgb(255, 255, 255));
					//dest.set(x, y, 255);
				}
			}
		}
		
		
//		buffDest = toBufferedImage(dst);
		
		
        cvNamedWindow( "Example1", CV_WINDOW_AUTOSIZE );
        cvShowImage("Example1", img);
        
        String path = "d:\\temp\\testBW.jpg";
        cvSaveImage(path, img);
        
		res.setArr(DetectArea(dst));
		
		if(bCrop == 1)
		{
			res.setImage(cropImg(dst, res.getArr()));
		}
		else
		{
			res.setImage(img);
		}
		
		
		return res;
		
	}
	
	


	/**
	* Finds the group of close valued colors that appear the most in the sign
	* by clustering and counting all the colors in the given image
	* @param ColorProcessor image
	* @return the color that approximately appears the most in the sign
	*/
	private static int getSignColor(IplImage src) 
	{
		int myRGB = 0;
		int res = 0;
		int cells = 20;
		int spec = 256 / (cells - 1);
		int blue, green, red;
		int colors [][][] = new int[cells][cells][cells]; // this array holds in each slot (spec) colors -  for a spec of 25 values - 255/25 = 10.2 ~ 11
		//the first slot is for 0-24, second 25 -50 and so on (this example is for a 1D array, ours is 3D)
		
		for(int y = 0; y < src.height(); y++)
		{
			for(int x = 0; x < src.width(); x++)
			{
				myRGB = GetColor(src, x , y);
				red =  Color.red(myRGB) / spec;
				green = Color.green(myRGB) / spec;
				blue = Color.blue(myRGB) / spec;
								
				colors[red][green][blue]++;
				
			}
		}
		
		int maxColor [] = new int[3];;
		int maxVal = 0;
		
		//find the color that we have the most of
		for(int i = 0; i < cells; i++)
		{
			for(int j = 0; j < cells; j++)
			{
				for(int n = 0; n < cells; n++)
				{
					if(colors[i][j][n] > maxVal)
					{
						maxVal = colors[i][j][n];
						maxColor[0] = i;
						maxColor[1] = j;
						maxColor[2] = n;
					}
				}
			}
		}
		
		
		//res = new Color(maxColor[0] * spec, maxColor[1] * spec, maxColor[2] * spec);
		res = Color.rgb(maxColor[0] * spec, maxColor[1] * spec, maxColor[2] * spec);
		return res;
	}


	//helper method to check a BufferedImage color
	public static boolean compareColor(IplImage src, int color, int x, int y)
	{
		int c2 = GetColor(src, x, y);
		
		boolean result = (c2 == color);
		return result;
	}
	
	/**
	* detects the area where the sign is by counting white pixels
	* @param BufferedImage
	* @return array of coords
	 	 * 0 - the y value of the bottom of the sign
         * 1 - the y value of the top of the sign
         * 2 - leftmost x value 
         * 3 - rightmost x value
	*/
	public static int[] DetectArea(IplImage src)
	{
		
		int white = Color.rgb(255, 255,255);
		
		
        // get dimentions
		int srcH = src.height();
		int srcW = src.width();
		
		//vars for maximum white line length		
		int lineLength = 0, maxLine = 0, maxLineY = 0;
		int yArr [] = new int[4];
		int xArr [][] = new int[2][2];
		
		
		// Iterate through pixels from start of pic to half it's height
		//check how many white pixels are there
		//get the y value of the one with the most white pixels
		//assume that this one is the start of the sign - bottom
		//assumes that the picture is approximately in the middle of the picture
		
		boolean flag = true;
		
		for(int y = 0;(y < (srcH /2)) && flag; y++)
		{
			for(int x = 0; x < srcW; x++) //count the length of the white line for every y coord
			{
				if (compareColor(src, white, x, y))
				{
					lineLength++; 
				}
			}
			
			if(maxLine < lineLength) //find the max length of a white line
			{
				maxLine = lineLength;
				maxLineY = y;
				flag = false;
			}
			
			lineLength = 0;
		}
		
		//the bottom start of sign y value
		yArr[0] = maxLineY;

		//initialize
		maxLine = 0;
		maxLineY = 0;
		
		flag = true;
		
		//do the same for the other half i.e. search for the end of the sign (top)
		for(int y = (srcH - 1); (y > (srcH / 2)) && flag; y--)
		{
			for(int x = 0; x < srcW; x++)
			{
				if (compareColor(src, white, x, y))
				{
					lineLength++;
				}
			}
			
			if(lineLength > (srcW / 2))
			{
				maxLine = lineLength;
				maxLineY = y;
				flag = false;
			}
			
			lineLength = 0;
		}
		
		
		//the top start of sign y value
		yArr[1] = maxLineY;

		
		for(int x = 0; x < srcW; x++)
		{
			if (compareColor(src, white, x, yArr[0]))
			{
				xArr[0][0] = x; //start of sign x value at y value yArr[0]
				break;
			}
		}
		
		for(int x = (srcW - 1); x > 0; x--)
		{
			if (compareColor(src, white, x, yArr[0]))
			{
				xArr[0][1] = x; //end of sign x value at y value yArr[0]
				break;
			}
		}
		
		for(int x = 0; x < srcW; x++)
		{
			if (compareColor(src, white, x, yArr[1]))
			{
				xArr[1][0] = x; //start of sign x value at y value yArr[1]
				break;
			}
		}
		
		for(int x = (srcW - 1); x > 0; x--)
		{
			if (compareColor(src, white, x, yArr[1]))
			{
				xArr[1][1] = x; //end of sign x value at y value yArr[1]
				break;
			}
		}
		
	
/*		//////////////////////////////////for debug
		System.out.println("y values:");
		System.out.println(yArr[0]);
        System.out.println(yArr[1]);
        System.out.println("x values:");
        System.out.println("first Y:");
        System.out.println(xArr[0][0]);
        System.out.println(xArr[0][1]);
        System.out.println("2nd Y:");
        System.out.println(xArr[1][0]);
        System.out.println(xArr[1][1]);
        /////////////////////////////////
*/       
        //take the extreme x values
        if(xArr[0][0] < xArr[1][0])
        	yArr[2] = xArr[0][0];
        else
        	yArr[2] = xArr[1][0];
        
        if(xArr[0][1] > xArr[1][1])
        	yArr[3] = xArr[0][1];
        else
        	yArr[3] = xArr[1][1];
        
 /*      
        ///////////debugging
        System.out.println("Returned array - yArr");
        for(int i =0; i < yArr.length; i++)
        	System.out.println(yArr[i]);
 
        
  */      
        
        /* yArr valus:
         * 0 - the y value of the bottom of the sign
         * 1 - the y value of the top of the sign
         * 2 - leftmost x value 
         * 3 - rightmost x value
         */
        
		return yArr;
	}

	/**
	* Crop a BufferedImage
	* @param BufferedImage
	* @param coordinates 
	* @return a cropped IplImage
	*/
	public static IplImage cropImg(IplImage BFImage, int[] coord)
	{
		
		if(((coord[3] - coord[2]) == 0) || ((coord[1] - coord[0]) == 0))
		{
			return BFImage;
		}

		// Create output image
		IplImage dest = IplImage.create(Math.abs(coord[3] - coord[2]), Math.abs(coord[1] - coord[0]), 8, 3);
		
	
		int color = Color.rgb(255, 255, 255);
		
		// Iterate through pixels and check if their distance from the target color is
        // Within the distance threshold, if it is set `dest` to 255.
		
		
		for(int y = coord[0], yD = 0; y < coord[1]; y++ , yD++)
		{
			for(int x = coord[2], xD = 0; x < coord[3]; x++, xD++)
			{
				
				if (compareColor(BFImage, color, x, y))
				{
					SetColor(dest, xD, yD, Color.rgb(255, 255, 255));
				}
				else
				{
					SetColor(dest, xD, yD, Color.rgb(255, 255, 255));
				}
			}
		}
		
		
		return dest;
		
	}
	
	
	
	
	
	
	//Helper Methods 
	public static boolean distance(int src,int targetRGB)
	{
		int d = 60; //d = 60
		int[] delta = {d , d, d};
		boolean red, green, blue, isDelta = false;
		
		red = (Math.abs(Color.red(src) - Color.red(targetRGB)) < delta[0]);
		green =  Math.abs(Color.green(src) - Color.green(targetRGB)) < delta[1];
		blue = Math.abs(Color.blue(src) - Color.blue(targetRGB)) < delta[2]; 
		
		isDelta = red && blue && green;
		
		return isDelta;		
	}
	
	
//	/**
//     * Convert OpenCV `IplImage` to ImageJ's ImageProcessor. Depending on the type input image different instance
//     * of `ImageProcessor` will be created, for color images it will be `ColorProcessor`, for 8-bit gray level `ByteProcessor`.
//     * Other pixel types are currently not supported.
//     *
//     * @param IplImage image -  input image.
//     * @return ImageProcessor
//     */
//	public static ImageProcessor toImageProcessor(IplImage image)
//	{
//		if((image.width() > 0) && (image.height() > 0))
//		{
//			BufferedImage bImage = image.getBufferedImage();
//		
//			if (bImage.getType() ==  BufferedImage.TYPE_BYTE_GRAY)
//			{
//				return new ByteProcessor(bImage);
//			}
//			else if (bImage.getType() ==  BufferedImage.TYPE_3BYTE_BGR) 
//			{
//				return new ColorProcessor(bImage);
//			}
//		}
//		//exception
//		System.out.println("Wohohoho DUDE EXCEPTION!!!!!!!!!!!!!!!!!!!!!!!!");
//		return null; // none of the type above - invalid input	
//	}
	
//	/**
//	 * @param IplImage image -  input image.
//     * @return ColorProcessor
//     */
//	public static ColorProcessor toColorProcessor(IplImage image)
//	{
//		
//		ImageProcessor ip = toImageProcessor(image);
//		
//		if (ip instanceof ColorProcessor) {
//			ColorProcessor cp = (ColorProcessor) ip;
//			return cp;
//		}
//		
//		
//		
//		return null; //Input image is not a color image.
//	}
//	
//	/**
//	 * @param ImageProcessor image -  input image.
//     * @return BufferedImage
//     */
//	public static BufferedImage toBufferedImage(IplImage image)
//	{
//		
//		BufferedImage BP = null;
//		
//			           
//        if( image.getClass().equals(ByteProcessor.class))
//        {
//        	BP = image.getBufferedImage();
//        }
//        else if (image.getClass().equals(ColorProcessor.class)) 
//        {
//        	// Create BufferedImage of RGB type that JavaCV likes
//            BP = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
//            // Easiest way to transfer the data is to draw the input image on the output image,
//            // This handles all needed color representation conversions, since both are variants of
//            Graphics g = BP.getGraphics();
//            g.drawImage(image.getBufferedImage(), 0, 0, null);
//		}
//		            
//        return BP;     	       
//	}
//	

	
	
	
	//getter and setter methods
	public int[] getArr()
	{
		return arr;
	}
	
	public IplImage getImage()
	{
		return image;
	}
	
	public void setImage(IplImage img) 
	{
		image = img;
		
	}

	public void setArr(int[] array) 
	{
		arr = array;
	}
	
	
}
