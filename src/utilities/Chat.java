package utilities;

public class Chat 
{

	public static final int SPELL = 1;
	public static final int TEXT = 2;
	public static final int COMMAND = 3;
	public static final int COMMAND_UNKNOWN = 0;
	public static final int COMMAND_GODMOD = 1;
	public static final int COMMAND_PUT = 2;
	public static final int COMMAND_TAKE = 3;
	public static final int COMMAND_SAVE = 4;
	public static final int COMMAND_LEVELUP = 5;
	private static int COMMAND_TYPE;
	private static int SPELL_TYPE;
	public static final int SPELL_HASTE = 1;
	public static final int SPELL_EXURA = 2;
	private static Chat instanceOf;
	private Chat()
	{
	}
	
	public static void removeLast()
	{
	}
	
	public static Chat getInstanceOf()
	{
		if(instanceOf == null) instanceOf = new Chat();
		return instanceOf;
	}
	
	private static int handleText(String input)
	{
		return Chat.TEXT;
	}
	
	private static int handleSpell(String input)
	{
		String lowerCase = input.toLowerCase();
		if(lowerCase.matches("exura"))
		{
			SPELL_TYPE = SPELL_EXURA;
			return Chat.SPELL;
		}
		else if(lowerCase.matches("utani hur"))
		{
			SPELL_TYPE = SPELL_HASTE;
			return Chat.SPELL;
		}
		else 
		{
			return handleText(input);
		}
	}
	
	public static int checkInput(String input)
	{
		if(input.length()>0)
		{
			System.out.println(input);
			char[] chatTable = input.toCharArray();
			if(chatTable[0]=='/')
			{
				if(chatTable[1]=='g')
				{
					COMMAND_TYPE = COMMAND_GODMOD;
				}
				else if(chatTable[1]=='p')
				{
					COMMAND_TYPE = COMMAND_TAKE;
				}
				else if(chatTable[1]=='o')
				{
					COMMAND_TYPE = COMMAND_PUT;
				}
				else if(chatTable[1]=='v')
				{
					COMMAND_TYPE = COMMAND_SAVE;
				}
				else if(input.matches("/levelup"))
				{
					COMMAND_TYPE = COMMAND_LEVELUP;
				}
				else
				{
					COMMAND_TYPE = COMMAND_UNKNOWN;
				}
				return Chat.COMMAND;
			}
			else 
			{
				return handleSpell(input);
			}
		}
		else return Chat.TEXT;
	}
	
	public static int getCommandType()
	{
		return COMMAND_TYPE;
	}
	
	public static int getSpellType()
	{
		return SPELL_TYPE;
	}
}
