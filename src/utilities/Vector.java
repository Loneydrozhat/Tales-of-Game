package utilities;

public class Vector implements Cloneable
{
	private int xVector,yVector;
	public static final int SOUTH = 0;
	public static final int EAST = 1;
	public static final int WEST = 2;
	public static final int NORTH = 3;
	public Vector()
	{
		xVector = 0;
		yVector = 0;
	}
	
	public Vector(int x, int y)
	{
		xVector = x;
		yVector = y;
	}
	
	public void setVector(int x, int y)
	{
		xVector = x;
		yVector = y;
	}
	public int getXVector()
	{
		return xVector;
	}
	public int getYVector()
	{
		return yVector;
	}	
	
	public int direction()
	{
		
		if(xVector==-1) return WEST;
		else if(xVector == 1) return EAST;
		else if(yVector == 1) return SOUTH;
		else return NORTH;
	}
	
	public Vector clone()
	{
		Vector newVector = new Vector();
		newVector.setVector(xVector, yVector);
		return newVector;
	}
	
	public String toString()
	{
		return new String("Vector:  " + xVector + "," + yVector);
	}
}
