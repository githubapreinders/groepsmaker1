package course.examples.UI.ListLayout;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Application;
import android.content.SharedPreferences;

public class GroepsMaker extends Application 

{
	static final String TAG = "Groepsmaker";
	public HashMap <String,Person> gwendolyn;
	public SharedPreferences prefs;
	
	@Override
	public void onCreate() {
		super.onCreate();
		gwendolyn = new HashMap<String,Person>();
		try
		{
			prefs = getSharedPreferences("storednames1", MODE_PRIVATE);
			makegrouplist(prefs.getString("storednames1", ""));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void makegrouplist(String textboxstring)
	{
		if(textboxstring=="")
		{
			return;
		}
		gwendolyn.clear();
		String[] string =  textboxstring.split("[\\s+,\\s+]"); 
		ArrayList<String> helper = new ArrayList<String>();
		for(int i =0;i<string.length;i+=3)
		{
		String s="";	
			string[i].trim();
			if(!(string[i].equals("")))
			{
				s = correctName(string[i]);
				helper.add(s);
			}
			
			string[i+1].trim();
			boolean checkedvalue = Boolean.parseBoolean((string[i+1]));
			string[i+2].trim();
			int colorvalue;
			try
			{
				colorvalue = Integer.parseInt(string[i + 2]);
			} catch (NumberFormatException e)
			{
				colorvalue = R.drawable.ic_menu_transparentkopie;
			}
			Person p = new Person(s,checkedvalue,colorvalue);
			gwendolyn.put(p.name, p);
		}
		
		return;
	}
	
	public String correctName(String name)
	{
		ArrayList<Integer>capitalindexes = new ArrayList<Integer>();
		capitalindexes.add(0);
		name.trim();
		StringBuilder str = new StringBuilder(name);
		for(int i =0;i<str.length()-1;i++)
		{
			if(str.charAt(i)==' ')
			{
				capitalindexes.add(i+1);
			}
			else str.setCharAt(i, Character.toLowerCase(str.charAt(i)));
		}
		for(int j : capitalindexes)
		
		str.setCharAt(j, Character.toUpperCase(str.charAt(j)));
		return str.toString();
	}
	
	public HashMap<String, Person> getGwendolyn()
	{
		return gwendolyn;
	}

	public void setGwendolyn(HashMap<String, Person> gwendolyn)
	{
		this.gwendolyn = gwendolyn;
	}

	public SharedPreferences getPrefs() {
		return prefs;
	}

	public void setPrefs(SharedPreferences prefs) {
		this.prefs = prefs;
	}

	
}
