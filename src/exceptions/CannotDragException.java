package exceptions;

public class CannotDragException extends RuntimeException
{
	public CannotDragException()
	{
		
	}
	
	public CannotDragException(String s)
	{
		System.out.println(s);
	}
}
