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

	public Solver(String in, int time)
	{
		cs = new CypherSearcher(in);
		timeBetweenRuns = Math.abs(time);
		timeSet = (timeBetweenRuns>0);
	}
	
	public String readLine()
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

	public void main(String args[]) throws InterruptedException
	{
		if(!(args.length==1 || args.length==2))
		{
			System.out.println("How to use:");
			System.out.println("java Solver fileToRead");
			System.out.println("java Solver fileToRead SecondsToPauseEachStep");
			return;
		}


		String textFromFile = TextGrader.fileToStrings(args[0]);


		Solver solver = null;
		if(args.length==1)
			solver = new Solver(textFromFile);
		if(args.length==2)
			solver = new Solver(textFromFile,Integer.parseInt(args[1]));
		Thread t = new Thread(solver);
		t.start();
		String testStr = "not q";
		System.out.println("Press q to quit:");
		testStr = readLine();
		while(!testStr.equalsIgnoreCase("q"))
		{
			System.out.println("Press q to quit:");
			testStr = readLine();
		}
		t.interrupt();
		t.join();

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
				keyH.translateText(this.cs.ecnh.grader.getText());
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
				keyH.translateText(this.cs.ecnh.grader.getText());
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
