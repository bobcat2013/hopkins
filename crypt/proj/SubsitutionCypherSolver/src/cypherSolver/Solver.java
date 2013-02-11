package cypherSolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Solver implements Runnable  {
	public CypherSearcher cs;
	public int timeBetweenRuns =0;
	public boolean timeSet = false;
	
	public Solver(String in)
	{
		cs = new CypherSearcher(in);
	}
	public Solver(String in, String wordlist)
	{
		cs = new CypherSearcher(in, wordlist);
		
	}

	public Solver(String in, int time)
	{
		cs = new CypherSearcher(in);
		timeBetweenRuns = Math.abs(time);
		timeSet = (timeBetweenRuns>0);
	}
	
	public static String readLine()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String outStr = null;
		try
		{
			outStr = br.readLine();
		} 
		catch (IOException ioe) 
		{
			System.out.println("IO error trying to read a line");
		}
		return outStr;
	}

	public static void main(String args[]) throws InterruptedException
	{
		
		if(!(args.length==2))
		{
			System.out.println("How to use:");
			System.out.println("java Solver wordlist fileToRead");
			return;
		}


		String textFromFile = TextGrader.fileToStrings(args[1]);
		System.out.println("Intial Jumbled Text:");
		System.out.println(textFromFile);

		Solver solver = null;
		if(args.length==2)
			solver = new Solver(textFromFile,args[0]);

		/*String testStr = "not q";
		System.out.println("Press q to quit:");
		testStr = readLine();
		*/
		double lastBestScore;
		double bestScore = Double.MIN_VALUE;
		String bestKey = "";
		String translation = "";
		int steps = 0;
		do
		{
			CypherNode cn = solver.cs.step();
			System.out.println("Best Score:");
			lastBestScore = bestScore;
			bestScore = cn.gethCost();
			System.out.println(cn.gethCost());
			System.out.println("Best Key:");
			KeyHolder keyH = cn.getKeyHolder();
			System.out.println(TextGrader.alphabet);
			bestKey = keyH.getkey();
			System.out.println(bestKey);
			System.out.println("Translated Text:");
			translation = keyH.translateText(solver.cs.ecnh.grader.getText());
			System.out.println(translation);
			steps ++;
			//System.out.println("Press q to quit:");
			//testStr = readLine();
		}
		while(true);/*lastBestScore < bestScore);*/
		/*
		System.out.println("Finished searching after "+steps +" steps");
		System.out.println("Best Key:");
		System.out.println(bestKey);
		System.out.println("Best Translation:");
		System.out.println(translation);
		*/
	}

	@Override
	public void run() {
		if(this.timeSet)
		{
			while(true)
			{
				CypherNode cn = this.cs.step();
				System.out.println("Best Key:");
				KeyHolder keyH = cn.getKeyHolder();
				System.out.println(TextGrader.alphabet);
				System.out.println(keyH.getkey());
				System.out.println("Translated Text:");
				System.out.println(keyH.translateText(this.cs.ecnh.grader.getText()));
			}
		}
		else
		{
			while(true)
			{
				CypherNode cn = this.cs.step();
				System.out.println("Best Key:");
				KeyHolder keyH = cn.getKeyHolder();
				System.out.println(TextGrader.alphabet);
				System.out.println(keyH.getkey());
				System.out.println("Translated Text:");
				System.out.println(keyH.translateText(this.cs.ecnh.grader.getText()));
				try
				{
					Thread.sleep(this.timeBetweenRuns*1000);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
		}
		
	}

}
