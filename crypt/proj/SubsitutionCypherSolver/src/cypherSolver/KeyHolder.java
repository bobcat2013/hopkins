package cypherSolver;

import java.util.ArrayList;

public class KeyHolder {
	private String key;
	private ArrayList<String> dudList;
	public KeyHolder(String inKey) throws Throwable
	{
		
		key = inKey.toLowerCase();
		dudList = new ArrayList<String>();
		if(key.length()>26)
		{
			
			throw new Throwable("Key is to big with "+key);
		}
		else if(key.length()<26)
		{
			
			throw new Throwable("Key is to small with "+key);
		}
			
	}
	
	public void setDudList(ArrayList<String> dl)
	{
		this.dudList=dl;
		return;
	}
	
	public String getkey()
	{
		return key;
	}
	
	public char sub(char inChar)
	{
		int place  = inChar -'a';
		return key.toCharArray()[place];
	}
	 
	public String translateText(String txt)
	{
		String outStr = "";
		for(char c : txt.toCharArray())
		{
			outStr += sub(c);
		}
		return outStr;
	}
	
	public boolean areBothDuds(int a, int b)
	{
		if(this.dudList.size()<2)
			return false;
		String stra = "";
		stra += this.key.charAt(a);
		String strb = "";
		strb += this.key.charAt(b);
		return this.dudList.contains(stra) && this.dudList.contains(strb);
	}
	
	public ArrayList<KeyHolder> makeNextSetOfKeys()
	{
		ArrayList<KeyHolder> keys = new ArrayList<KeyHolder>();
		for(int ii =0; ii< 26; ii++)
			for(int jj =0; jj<26; jj++)
			{
				if(ii!=jj && (!this.areBothDuds(ii, jj)))
				{
					String newKey = this.key;
					char temp = newKey.charAt(jj);
					if(jj!=25)
						newKey = newKey.substring(0,jj)+newKey.charAt(ii)+newKey.substring(jj+1);
					else
						newKey = newKey.substring(0,jj)+newKey.charAt(ii);
					if(ii!=25)
						newKey = newKey.substring(0,ii)+temp+newKey.substring(ii+1);
					else
						newKey = newKey.substring(0,ii)+temp;
					
					try {
						keys.add(new KeyHolder(newKey));
					} catch (Throwable e) {
						System.out.println("How did the generation new keys get messed up?");
					}
					
				}
			}
		return keys;
	}
	
}
