package utilities;

public class Position implements Cloneable
{
	private int xPosition;
	private int yPosition;
	
	public Position()
	{
		xPosition = 0;
		yPosition = 0;
	}
	
	public Position(int x, int y)
	{
		xPosition = x;
		yPosition = y;
	}
	
	public void setXPosition(int x)
	{
		xPosition = x;
	}
	
	public void setYPosition(int y)
	{
		yPosition = y;
	}
	
	public void setXYposition(int x, int y)
	{
		xPosition = x;
		yPosition = y;
	}
	
	public int getXPosition()
	{
		return xPosition;
	}
	
	public int getYPosition()
	{
		return yPosition;
	}
	
	public int distance(Position position)
	{
		int x = position.getXPosition() - this.xPosition;
		int y = position.getYPosition() - this.yPosition;
		x = Math.abs(x);
		y = Math.abs(y);
		if(x>y)return x;
		else return y;
	}
	
	public Position clone()
	{
		return new Position(xPosition,yPosition);
	}
	
	public boolean equals(Position position)
	{
		if(position.getXPosition() == xPosition && position.getYPosition() == yPosition) return true;
		return false;
	}
	
	public String toString()
	{
		return new String(xPosition + "," + yPosition);
	}
}
