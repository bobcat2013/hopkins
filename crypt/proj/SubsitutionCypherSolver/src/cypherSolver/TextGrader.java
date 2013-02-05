package cypherSolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextGrader {

	private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
	private static final String lettersInOrderOfFrequency = "etaoinshrdlcumwfgypbvkjxqz";
	private String text;
	public TextGrader(String txt)
	{
		text =txt;
	}
	
	public String getText()
	{
		return this.text;
	}
	
	private HashMap<String,Integer> emptyLetterFrequencyTable()
	{
		HashMap<String,Integer> out = new HashMap<String,Integer>();
		for(char letter:alphabet.toCharArray())
		{
			String temp ="";
			temp += letter;
			out.put(temp, 0);
		}
		return out;
	}
	
	public HashMap<String,Integer> letterFrequency()
	{
		HashMap<String,Integer> out = emptyLetterFrequencyTable();
		for(char letter:this.text.toCharArray())
		{
			String temp ="";
			temp += letter;
			out.put(temp,out.get(temp) + 1);
		}
		return out;
		 
	}
}
