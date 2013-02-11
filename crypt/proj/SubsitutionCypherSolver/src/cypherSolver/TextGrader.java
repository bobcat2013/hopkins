package cypherSolver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class TextGrader {

	public static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
	public static final String lettersInOrderOfFrequency = "etaoinshrdlcumwfgypbvkjxqz";
	public static String listOfWordsFilePath = "/home/bobcat/gits/hopkins/crypt/data/5000MostCommonWords.txt";
	private boolean loaded;
	private String text;
	private HashMap<Integer,ArrayList<String>> wordHolder;
	public TextGrader(String txt)
	{
		loaded = false;
		text =txt.toLowerCase().trim();
		wordHolder = new HashMap<Integer,ArrayList<String>> ();
	}
	public TextGrader(String txt, String wordlist)
	{
		loaded = false;
		text =txt.toLowerCase().trim();
		listOfWordsFilePath = wordlist;
		wordHolder = new HashMap<Integer,ArrayList<String>> ();
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



	public ArrayList<String> getListOfZeros(HashMap<String,Integer> ftable)
	{
		ArrayList<String> out = new ArrayList<String>();
		for(String key:ftable.keySet())
		{
			if(ftable.get(key)==0)
			{
				out.add(key);
			}
		}
		return out;
	}

	public double indexOfCoincidence(String inText)
	{
		double out =0.0;
		int[] counts = new int[26];
		for(int ii=0; ii<26; ii++)
			counts[ii] =0;
		for(int ii=0; ii<this.text.length();ii++)
		{
			counts[this.text.charAt(ii) - 'a'] += 1;
		}	
		int n = text.length();
		for(int ii=0; ii<26; ii++)
		{
			if(counts[ii] >0)
			{
				out += (counts[ii]*(counts[ii]-1))/(n*(n-1));
			}
		}
		return out;
	}

	public int findMatchingWordsOfCertainLength(int strLen, String inText)
	{
		int count =0;
		int totalLength = inText.length();
		int index = 0;
		while(index+strLen<totalLength)
		{
			for(String word: this.wordHolder.get(strLen))
			{
				if(word.equalsIgnoreCase(inText.substring(index, index+strLen)))
				{
					count++;
					break;
				}
			}
			index++;
		}
		return count;
	}

	public double grade(KeyHolder key)
	{
		double out = 0.0;
		String tranText = key.translateText(this.text);
		if(!this.loaded)
			this.loadWordList();
		for(int length: this.wordHolder.keySet())
			out += length*findMatchingWordsOfCertainLength(length, tranText);
		return out;
	}
	
	public static String fileToStrings(String filename)
	{
		String out = "";
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = br.readLine()) != null) {
				out += line.trim().toLowerCase();
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;
	}

	public ArrayList<String> fileToListOfStrings(String filename)
	{
		ArrayList<String> out = null;
		BufferedReader br;
		try {
			out = new ArrayList<String>();
			br = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = br.readLine()) != null) {
				out.add(line.trim().toLowerCase());
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;
	}

	public void loadWordList()
	{
		this.loaded = true;
		ArrayList<String> temp = fileToListOfStrings(listOfWordsFilePath);
		for(String word : temp)
		{
			if(this.wordHolder.containsKey(word.length()))
			{
				this.wordHolder.get(word.length()).add(word);
			}
			else
			{
				this.wordHolder.put(word.length(), new ArrayList<String>());
				this.wordHolder.get(word.length()).add(word);
			}
		}
	}

	public KeyHolder makeOptimalKeyHolderFromSigleLetterFrequency()
	{
		KeyHolder out = null;
		HashMap<String,Integer> freqs =  letterFrequency();
		String lettersInOrderOfFreqInTxt = "";
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
			lettersInOrderOfFreqInTxt+=bestLetter;
		}
		HashMap<String,String> keyBuilder = new HashMap<String,String>();
		for(int ii =0; ii<26; ii++)
		{
			String key = "";
			key += lettersInOrderOfFrequency.toCharArray()[ii];
			String value = "";
			value += lettersInOrderOfFreqInTxt.toCharArray()[ii];
			keyBuilder.put(key, value);
		}
		String newKey ="";
		for(int ii =0; ii<26; ii++)
		{
			String key = "";
			key += alphabet.toCharArray()[ii];
			newKey += keyBuilder.get(key) ;
		}
		try
		{
			out = new KeyHolder(newKey);
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		};
		return out;
	}

}
