package cypherSolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
	
	public KeyHolder makeOptimalKeyHolderFromSigleLetterFrequency()
	{
		KeyHolder out = null;
		HashMap<String,Integer> freqs =  letterFrequency();
		String lettersInOrderOfFreq = "";
		int bestCount=0;
		String bestLetter = "";
		Set<String> keySet = freqs.keySet();
		for(int ii =0; ii<26; ii++)
		{
			bestCount = freqs.get(keySet.toArray()[0]);
			bestLetter = (String)keySet.toArray()[0];
			for(String key:keySet)
			{
				if(bestCount<freqs.get(key))
				{
					bestCount = freqs.get(key);
					bestLetter = key;
				}
			}
			keySet.remove(bestLetter);
			lettersInOrderOfFreq+=bestLetter;
		}
		HashMap<String,String> keyBuilder = new HashMap<String,String>();
		
		try
		{
			out = new KeyHolder(" ");
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		};
		return out;
	}
	
}
