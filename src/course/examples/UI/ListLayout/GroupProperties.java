package course.examples.UI.ListLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class GroupProperties extends ListActivity

{

	final int REQUEST_GROUPMEMBERS = 33;
	String storednames;
	EditText edit1;
	ListView lv;
	LinearLayout linearlayout;
	SharedPreferences prefs;
	Boolean insplits = false;
	Button splitsGroup;
	ArrayList<Person> group;
	public MyCbArrayAdapter myadapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_properties);
		prefs = ((GroepsMaker) getApplication()).getPrefs();
		edit1 = (EditText) findViewById(R.id.group_properties_grouplist);
		edit1.setText(getSharedPreferences());
		linearlayout = (LinearLayout) findViewById(R.id.framelayout);
		linearlayout.setVisibility(View.GONE);
		lv = getListView();
		group = new ArrayList<Person>();
		for (Map.Entry<String, Person> m : ((GroepsMaker) getApplication()).gwendolyn.entrySet())
		{
			group.add(m.getValue());
		}
		ArrayList<String> helper = new ArrayList<String>();
		for(Person p : group)
		{
			helper.add(p.getName());
		}
		Collections.sort(helper);
		ArrayList<Person> helper2 = new ArrayList<Person>();
		for(String s : helper)
		{
			for(Person pers : group)
			{
				if(pers.getName().equals(s))
				{
					helper2.add(pers);
				}
			}
		}
		myadapter = new MyCbArrayAdapter(this, R.id.listitem_property, helper2);
		Button gotoShowGroupList = (Button) findViewById(R.id.button1);
		gotoShowGroupList.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showGroupList();
			}
		});

		splitsGroup = (Button) findViewById(R.id.button0);
		splitsGroup.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edit1.getWindowToken(), 0);
				splitsGroup();

			}
		});

	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	public void splitsGroup()
	{
		if (insplits == true)
		{
			edit1.setVisibility(View.VISIBLE);
			linearlayout.setVisibility(View.GONE);
			splitsGroup.setText(R.string.button_splits);
			insplits = false;
		} else
		{
			edit1.setVisibility(View.GONE);
			linearlayout.setVisibility(View.VISIBLE);
			splitsGroup.setText(R.string.namen);
			updateHashMap();
			group.clear();
			for (Map.Entry<String, Person> p : ((GroepsMaker) getApplication()).getGwendolyn().entrySet())
			{
				group.add(p.getValue());
			}
			ArrayList<String> helper = new ArrayList<String>();
			for(Person p : group)
			{
				helper.add(p.getName());
			}
			Collections.sort(helper);
			ArrayList<Person> helper2 = new ArrayList<Person>();
			for(String s : helper)
			{
				for(Person pers : group)
				{
					if(pers.getName().equals(s))
					{
						helper2.add(pers);
					}
				}
			}
			
			setListAdapter(myadapter);
			myadapter.changeData(helper2);
			insplits = true;

		}
	}

	public void showGroupList()
	{
		updateHashMap();
		for (Person p : myadapter.group)
		{
			((GroepsMaker) getApplication()).getGwendolyn().get(p.name).ischecked = p.ischecked;
		}

		String tostorestring = "";
		for (Map.Entry<String, Person> p : ((GroepsMaker) getApplication()).getGwendolyn().entrySet())
		{
			tostorestring += p.getValue().getName() + " " + p.getValue().isIschecked() + " "
					+ p.getValue().getColorvalue() + " ";
		}

		SharedPreferences.Editor prefseditor = getSharedPreferences("storednames1", MODE_PRIVATE).edit();
		prefseditor.putString("storednames1", tostorestring);
		prefseditor.commit();
		Intent intent = new Intent(GroupProperties.this, ListViewActivity.class);
		setResult(RESULT_OK, intent);
		finish();
	}

	public String getSharedPreferences()
	{
		String outputstring = "";
		ArrayList<String> helper = new ArrayList<String>();
		for (Map.Entry<String, Person> m : ((GroepsMaker) getApplication()).gwendolyn.entrySet())
		{
			helper.add(m.getKey());
		}
		Collections.sort(helper);
		for(String s : helper)
		{
			outputstring += s + " ";
		}
		return outputstring;
	}

	// converts the text in the textbox to an array of names
	public String[] makegrouplist(String textboxstring)
	{
		String[] string = textboxstring.split("[\\s+,\\s+]");
		ArrayList<String> helper = new ArrayList<String>();
		for (int i = 0; i < string.length; i++)
		{
			string[i].trim();
			if (!(string[i].equals("")))
			{
				helper.add(correctName(string[i]));
			}
		}
		String[] returnstring = new String[helper.size()];
		int counter = 0;
		for (String s : helper)
		{
			returnstring[counter] = s;
			counter++;
		}
		return returnstring;

	}

	// standard code for getting capital letters at the correct position
	public String correctName(String name)
	{
		ArrayList<Integer> capitalindexes = new ArrayList<Integer>();
		capitalindexes.add(0);
		name.trim();
		StringBuilder str = new StringBuilder(name);
		for (int i = 0; i < str.length() - 1; i++)
		{
			if (str.charAt(i) == ' ')
			{
				capitalindexes.add(i + 1);
			} else
				str.setCharAt(i, Character.toLowerCase(str.charAt(i)));
		}
		for (int j : capitalindexes)

			str.setCharAt(j, Character.toUpperCase(str.charAt(j)));
		return str.toString();
	}

	public void updateHashMap()
	{
		// adding new entries on the basis of user textboxinput, deleting old
		// ones and preserving the ones that were already there
		String[] grouparray = makegrouplist(edit1.getText().toString());

		for (String s : grouparray)
		{
			if (!((GroepsMaker) getApplication()).gwendolyn.containsKey(s))
			{
				((GroepsMaker) getApplication()).gwendolyn.put(s, new Person(s, false, R.drawable.transparentkopie));
			}
		}
		ArrayList<String> helper = new ArrayList<String>();
		for (Map.Entry<String, Person> p : ((GroepsMaker) getApplication()).getGwendolyn().entrySet())
		{
			boolean remove = true;
			for (String s : grouparray)
			{
				if (p.getKey().equals(s))
				{
					remove = false;
				}
			}
			if (remove == true)
			{
				helper.add(p.getKey());
			}
		}
		for (String s : helper)
		{
			((GroepsMaker) getApplication()).getGwendolyn().remove(s);
		}

		// updating the adapter
		ArrayList<Person> helper2 = new ArrayList<Person>(((GroepsMaker) getApplication()).getGwendolyn().values());
		myadapter.changeData(helper2);

	}
}
