package exceptions;

public class CannotWalkException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	public CannotWalkException()
	{
		
	}
	public CannotWalkException(String s)
	{
		System.out.println(s);
	}
}
