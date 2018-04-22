package exceptions;

public class CannotMoveItemException extends RuntimeException
{
	public CannotMoveItemException()
	{
		
	}
	public CannotMoveItemException(String s)
	{
		System.out.println(s);
	}
}
