import edu.princeton.cs.algs4.*;
import java.awt.Color;

public class SeamCarver
{
	private Picture picture;
	private int height;
	private int width;
	private int [][] color;

	public SeamCarver(Picture picture)                // create a seam carver object based on the given picture
	{
		this.picture = new Picture(picture);
		width = this.picture.width();
		height = this.picture.height();
		color = new int[width][height];
	
		// initialize color
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
			{
				color[x][y] = this.picture.get(x, y).getRGB();
			}
	}

	private void relaxH(int x, int y, double[][] distHTo, int[][] edgeHTo)
	{
		if (x < 0 || x >= width)	throw new IndexOutOfBoundsException("x out of range:" + x);
		if (y < 0 || y >= height)	throw new IndexOutOfBoundsException("y out of range");
		// relax 3 vertices that connected from x,y
		if (distHTo[x+1][y] > distHTo[x][y] + energy(x+1, y))	  
		{
			distHTo[x+1][y] = distHTo[x][y] + energy(x+1, y);
			edgeHTo[x+1][y] = y;
		}
		if (y < height - 1 && distHTo[x+1][y+1] > distHTo[x][y] + energy(x+1, y+1))	  
		{
			distHTo[x+1][y+1] = distHTo[x][y] + energy(x+1, y+1);
			edgeHTo[x+1][y+1] = y;
		}
		if (y > 0 && distHTo[x+1][y-1] > distHTo[x][y] + energy(x+1, y-1))	  
		{
			distHTo[x+1][y-1] = distHTo[x][y] + energy(x+1, y-1);
			edgeHTo[x+1][y-1] = y;
		}
	}

	private void relaxV(int x, int y, double[][] distVTo, int[][] edgeVTo)
	{
		if (x < 0 || x >= width)	throw new IndexOutOfBoundsException("x out of range:" + x);
		if (y < 0 || y >= height)	throw new IndexOutOfBoundsException("y out of range");
		// relax 3 vertices that connected from x,y
		if (distVTo[x][y+1] > distVTo[x][y] + energy(x, y+1))	  
		{
			distVTo[x][y+1] = distVTo[x][y] + energy(x, y+1);
			edgeVTo[x][y+1] = x;
		}
		if (x < width - 1 && distVTo[x+1][y+1] > distVTo[x][y] + energy(x+1, y+1))
		{
			distVTo[x+1][y+1] = distVTo[x][y] + energy(x+1, y+1);
			edgeVTo[x+1][y+1] = x;
		}
		if (x > 0 && distVTo[x-1][y+1] > distVTo[x][y] + energy(x-1, y+1))	
		{
			distVTo[x-1][y+1] = distVTo[x][y] + energy(x-1, y+1);
			edgeVTo[x-1][y+1] = x;
		}
	}
		
    public Picture picture()                          // current picture
	{
		// create new picture use color array that has refreshed
		Picture newP = new Picture(width, height);
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
			{
				newP.set(i, j, new Color (color[i][j])); 
			}
		picture = newP;
		return picture;
	}

    public     int width()                            // width of current picture
	{	return width;	}

    public     int height()                           // height of current picture
	{	return height;	}

    public  double energy(int x, int y)               // energy of pixel at column x and row y
	{	
		if (x < 0 || x >= width)	throw new IndexOutOfBoundsException("x out of range:" + x);
		if (y < 0 || y >= height)	throw new IndexOutOfBoundsException("y out of range");
		if (x == 0 || x == width - 1 || y == 0 || y == height - 1)	return 1000.0;
		int xGradientRed = ((color[x+1][y] >> 16) & 0xff) - ((color[x-1][y] >> 16) & 0xff);
		int xGradientGreen = ((color[x+1][y] >> 8) & 0xff) - ((color[x-1][y] >> 8) & 0xff);
		int xGradientBlue = ((color[x+1][y] & 0xff)) - ((color[x-1][y] & 0xff));
		int yGradientRed = ((color[x][y+1] >> 16) & 0xff) - ((color[x][y-1] >> 16) & 0xff);
		int yGradientGreen = ((color[x][y+1] >> 8) & 0xff) - ((color[x][y-1] >> 8) & 0xff);
		int yGradientBlue = (color[x][y+1] & 0xff) - (color[x][y-1] & 0xff);

		double xGradient = xGradientRed * xGradientRed + xGradientBlue * xGradientBlue + xGradientGreen * xGradientGreen;
		double yGradient = yGradientRed * yGradientRed + yGradientBlue * yGradientBlue + yGradientGreen * yGradientGreen;
		return Math.sqrt(xGradient + yGradient);
	}


    public   int[] findVerticalSeam()                 // sequence of indices for vertical seam
	{
		double[][] distVTo;
		int[][] edgeVTo;

		// initialize distTo and edgeTo
		distVTo = new double[width][height];  // distVTo[x][y] min distance from x,y to top
		edgeVTo = new int[width][height];
		for (int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				if (y == 0) distVTo[x][y] = 1000.0;  // initialize first line with weight 1000.0
				else distVTo[x][y] = Double.POSITIVE_INFINITY;
			}
			edgeVTo[x][0] = x;
		}

		// relax all vertices(pixel)
		for (int y = 0; y < height - 1; y++) // vertices in last line doesn't need relax
			for (int x = 0; x < width; x++)
				relaxV(x, y, distVTo, edgeVTo);

		// find sp
		int[] seam = new int[height];	
		
		int minIndex = 0;
		for (int x = 0; x < width; x++)
			if (distVTo[x][height-1] < distVTo[minIndex][height-1])	minIndex = x;
		seam[height-1] = minIndex; // last line index
		for (int y = height - 1; y > 0; y--)
		{
			minIndex = edgeVTo[minIndex][y];
			seam[y-1] = minIndex;
		}

		return seam;
	}

    public   int[] findHorizontalSeam()               // sequence of indices for horizontal seam
	{
		double[][] distHTo;
		int[][] edgeHTo;
		distHTo = new double[width][height];  // distHTo[x][y] min distance from x,y to rightside 
		edgeHTo = new int[width][height];
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				if (x == 0) distHTo[x][y] = 1000.0;
				else distHTo[x][y] = Double.POSITIVE_INFINITY;
			}
			edgeHTo[0][y] = y;
		}
		// relax all vertices(pixel)
		for (int x = 0; x < width - 1; x++)
			for (int y = 0; y < height; y++) 
			// vertices in last col doesn't need relax
				relaxH(x, y, distHTo, edgeHTo);

		// find sp
		int[] seam = new int[width];	
		
		int minIndex = 0;
		for (int y = 0; y < height; y++)
			if (distHTo[width-1][y] < distHTo[width-1][minIndex])	minIndex = y;
		seam[width-1] = minIndex; // last col index

		for (int x = width - 1; x > 0; x--)
		{
			minIndex = edgeHTo[x][minIndex];
			seam[x-1] = minIndex;
		}

		return seam;
	}

	private boolean isVerticalSeam(int[] seam)
	{
		if (seam.length != height)	return false;
		for (int i = 0; i < seam.length; i++)
		{
			if (seam[i] >= width || seam[i] < 0)	return false;
			//StdOut.printf("height: %d, width: %d, seam[%d]: %d ",height,width, i, seam[i]);
			if (i != seam.length - 1 && Math.abs(seam[i] - seam[i+1]) > 1)	return false;
			//StdOut.println(i + "passed adjcent test.");
		}
			
		return true;
	}

	private boolean isHorizontalSeam(int[] seam)
	{
		if (seam.length != width)	return false;
		for (int i = 0; i < seam.length; i++)
		{
			if (seam[i] >= height || seam[i] < 0)	return false;
			if (i != seam.length - 1 && Math.abs(seam[i] - seam[i+1]) > 1)	return false;
		}
		return true;
	}

    public    void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
	{
		if (seam == null) throw new java.lang.NullPointerException();
		if (!isHorizontalSeam(seam))	throw new java.lang.IllegalArgumentException();

		// refresh color array
		for (int y = 0; y < height - 1; y++)
			for (int x = 0; x < width; x++) // last line is uselesss because the picture shrink
				if (y >= seam[x])	color[x][y] = color[x][y+1];

		height--;
		//// refresh distHTo array
		//for (int y = 0; y < height; y++) 
		//	// vertices in last col doesn't need relax
		//	for (int x = 0; x < width - 1; x++)
		//		relaxH(x, y);
	}

    public    void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
	{
		if (seam == null) throw new java.lang.NullPointerException();
		if (!isVerticalSeam(seam))	throw new java.lang.IllegalArgumentException();
		
		// refresh color array
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width - 1; x++) // last col is uselesss because the picture shrink
				if (x >= seam[y])	color[x][y] = color[x+1][y];

		width--;
//		// refresh distVTo array
//		for (int y = 0; y < height - 1; y++) // vertices in last line doesn't need relax
//			for (int x = 0; x < width; x++)
//				relaxV(x, y);
	}
}
