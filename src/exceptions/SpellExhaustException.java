package exceptions;

public class SpellExhaustException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	public SpellExhaustException()
	{
		
	}
	public SpellExhaustException(String s)
	{
		System.out.println(s);
	}
}

