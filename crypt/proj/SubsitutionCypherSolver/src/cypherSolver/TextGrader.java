package cypherSolver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

	public static  HashMap<String,Integer> emptyLetterFrequencyTable()
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

	public static  double indexOfCoincidence(String inText)
	{
		double out =0.0;
		int[] counts = new int[26];
		for(int ii=0; ii<26; ii++)
			counts[ii] =0;
		for(int ii=0; ii<inText.length();ii++)
		{
			counts[inText.charAt(ii) - 'a'] += 1;
		}	
		int n = inText.length();
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
	
	public static String readin()
	{
	   BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	      String out = null;

	      
	      try {
	         out = br.readLine();
	      } catch (IOException ioe) {
	         System.out.println("How did you manage to break this??!?!?");
	         System.exit(1);
	      }
	      
	      return out;
	      
	}
	
	public static String cshift(String in, int amt)
	{
		String out ="";
		in = in.toLowerCase();
		for(char letter : in.toCharArray())
			out += ((((letter-'a')+((char) amt))%26)+'a');
		return out;
	}
	
	public static HashMap<String,Integer> makeFreqTable(String intext)
	{
		HashMap<String,Integer> out = emptyLetterFrequencyTable();
		for(char letter:intext.toCharArray())
		{
			String temp ="";
			temp += letter;
			out.put(temp,out.get(temp) + 1);
		}
		return out;
	}
	
	
	public static void main(String args[])
	{
		if(args.length< 1)
		{
			System.out.println("need a file to read in");
			return;
		}
		System.out.println("Vigenere Cipher Breaker");
		System.out.println("reading in file at "+args[0]);
		String text = "";
		text += fileToStrings(args[0]);
		int textlen = text.length();
		if(textlen==0)
		{
			System.out.println("No text in "+args[0]+" quiting");
			return;
		}
		System.out.println("Read file "+args[0]+" with " + textlen+" chars");
		ArrayList<String> samples;
		int maxKeyLength = textlen;
		if(args.length > 1)
		{
			maxKeyLength = Integer.parseInt(args[1]);
		}
		int bestKey =0;
		double bestscore = Double.MAX_VALUE*-1;
		for(int ii=1; ii<=maxKeyLength; ii++)
		{
			samples = new ArrayList<String>();
			for(int jj=0;jj<ii;jj++)
				samples.add("");
			for(int jj=0;jj<textlen; jj++)
				samples.set(jj%ii, samples.get(jj%ii)+ text.charAt(jj));
			double scoreForThisKeyLen = 0;
			for(String sample : samples)
				scoreForThisKeyLen+=indexOfCoincidence(sample);
			scoreForThisKeyLen = scoreForThisKeyLen/(1.0*ii);
			if(scoreForThisKeyLen>bestscore)
			{
				bestscore= scoreForThisKeyLen;
				bestKey = ii;
			}
			System.out.println("For a key length of "+ii
					+" the index of conincodence is "+scoreForThisKeyLen);
		}
		System.out.println("Best index of conincodence is "+ bestscore
				+" found at a key length of "+ bestKey);
		System.out.println("Do you want to try and break the Cipher? [y/n]");
		String ans = readin();
		while(!(ans.equalsIgnoreCase("y")||ans.equalsIgnoreCase("n")))
		{
			System.out.println("Please use y for yes and n for no.");
			System.out.println("Do you want to try and break the Cipher? [y/n]");
			ans = readin();
		}
		if(ans.equalsIgnoreCase("n"))
		{
			return;
		}
		samples = new ArrayList<String>();
		for(int ii =0; ii< bestKey; ii++)
			samples.add("");
		for(int jj=0;jj<textlen; jj++)
			samples.set(jj%bestKey, samples.get(jj%bestKey)+ text.charAt(jj));
		HashMap<String,Double> realLetterFreq = new HashMap<String,Double>();
		// from https://en.wikipedia.org/wiki/Letter_frequency
		realLetterFreq.put("e",12.702/100.0);
		realLetterFreq.put("t",9.056/100.0);
		realLetterFreq.put("a",8.167/100.0);
		realLetterFreq.put("o",7.507/100.0);
		realLetterFreq.put("i",6.966/100.0);
		realLetterFreq.put("n",6.749/100.0);
		realLetterFreq.put("s",6.327/100.0);
		realLetterFreq.put("h",6.094/100.0);
		realLetterFreq.put("r",5.987/100.0);
		realLetterFreq.put("d",4.253/100.0);
		realLetterFreq.put("l",4.025/100.0);
		realLetterFreq.put("c",2.782/100.0);
		realLetterFreq.put("u",2.758/100.0);
		realLetterFreq.put("m",2.406/100.0);
		realLetterFreq.put("w",2.360/100.0);
		realLetterFreq.put("f",2.228/100.0);
		realLetterFreq.put("g",2.015/100.0);
		realLetterFreq.put("y",1.974/100.0);
		realLetterFreq.put("p",1.929/100.0);
		realLetterFreq.put("b",1.492/100.0);
		realLetterFreq.put("v",0.978/100.0);
		realLetterFreq.put("k",0.772/100.0);
		realLetterFreq.put("j",0.153/100.0);
		realLetterFreq.put("x",0.150/100.0);
		realLetterFreq.put("q",0.095/100.0);
		realLetterFreq.put("z",0.074/100.0);
		HashMap<String,Integer> freqsInSample;
		for(int jj=0; jj<samples.size(); jj++)
		{
			int bestShift = -1;
			int textLenth = samples.get(jj).length();
			double bestIofC = Double.MAX_VALUE*-1;
			
			for(int ii =0;ii<26; ii++)
			{
				double icForThisShift = 0.0;
				freqsInSample = makeFreqTable(cshift(samples.get(jj), ii));
				for(String key :realLetterFreq.keySet())
				{
					icForThisShift += realLetterFreq.get(key)*(freqsInSample.get(key)/(textLenth*1.0));
				}
				if(icForThisShift>bestIofC)
				{
					bestIofC = icForThisShift;
					bestShift = ii;
				}
			}
			System.out.println("Positions "+jj+" are shifted "+bestShift);
		}
		
	}

}
