package exceptions;

public class ReputItemException extends RuntimeException
{
	public ReputItemException()
	{
		
	}
	public ReputItemException(String s)
	{
		System.out.println(s);
	}
}
