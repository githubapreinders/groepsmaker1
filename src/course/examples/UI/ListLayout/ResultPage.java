package course.examples.UI.ListLayout;

import java.util.ArrayList;
import java.util.Collections;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import course.examples.UI.ListLayout.MyArrayListAdapter.ViewHolder;

public class ResultPage extends ListActivity 
{
private final int GROUP_ID = 0;
private final int MENU_WIJZIGEN = 1;
private final int MENU_VERPLAATSEN = 2;
private final int MENU_VERWIJDEREN = 3;
ListView lv;
MyArrayListAdapter myadapter;
ViewHolder holder;
View clicked;
ArrayList<String> groupresults;
private int HOWMUCHGROUPS;
String hrstring = "<hr style = 'background-color:#000000;  color:#000000; height:2px; ' />";
private TextView parking;
private int position;
private int length;
private boolean linearlayoutvisibility;
private EditText ed;
private String oldstring;


	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_page);
		Intent intent = getIntent();
		String[] helper = intent.getStringArrayExtra ("groupings");
		HOWMUCHGROUPS = intent.getIntExtra("howmuchgroups", 2);
		groupresults= new ArrayList<String>();
		for(String s : helper)
		{
			groupresults.add(s);
		}
		myadapter = new MyArrayListAdapter(this, R.id.listitem,groupresults);
		setListAdapter(myadapter);
		registerForContextMenu(getListView());

		Button btnshare = (Button)findViewById(R.id.verdeel);
		btnshare.setText(R.string.share);
		btnshare.setOnClickListener(new btnshareOnClickListener());
		Drawable img1 = getResources().getDrawable(R.drawable.mail_icon);
		img1.setBounds(10,0,48,48);
		btnshare.setCompoundDrawables(img1, null, null, null);
		
		
		Button btnsms = (Button)findViewById(R.id.properties);
		btnsms.setText(R.string.sms);
		btnsms.setOnClickListener(new btnsmsOnClickListener());
		btnsms.setEnabled(true);
		Drawable img2 = getResources().getDrawable( R.drawable.sms_icon );
		img2.setBounds(10,0,48,48);;
		btnsms.setCompoundDrawables(img2, null, null, null);

		Button spinbtn = (Button)findViewById(R.id.group_properties_spinner1);
		spinbtn.setVisibility(View.GONE);
		
		Button btnedit = (Button)findViewById(R.id.button1);
		btnedit.setOnClickListener(new btneditClickListener());
		
		ed = (EditText)findViewById(R.id.editText1);
		ed.setHint("Vul hier iets in...");
		
		
		LinearLayout layout = (LinearLayout)findViewById(R.id.edittextlayout);
		layout.setVisibility(View.GONE);
		linearlayoutvisibility=false;
		
	}
	
	
	
	
	@Override
	public void onBackPressed() {
		if(linearlayoutvisibility)
		{
			setLinearLayoutVisibility();
		}
		else
		super.onBackPressed();
	}




	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) 
	{
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		String invokingitem = ((TextView)info.targetView).getText().toString() ;
		String item1 = getResources().getString(R.string.naam_wijzigen);
	    String item2 = getResources().getString(R.string.verplaatsen_naar);
	    String item3 = getResources().getString(R.string.verwijderen);
	    menu.setHeaderTitle(invokingitem);
	    menu.add(GROUP_ID, MENU_WIJZIGEN, 0, item1);
	    SubMenu submenu =  menu.addSubMenu(GROUP_ID, MENU_VERPLAATSEN, 0, item2);
	    for(int i = 0;i<HOWMUCHGROUPS;i++)
	    {
	    	submenu.add(GROUP_ID,i+11,0,"Groep "+ String.valueOf(i+1));
	    }
	    menu.add(GROUP_ID, MENU_VERWIJDEREN, 2, item3);
	}
	
	
	
	@Override
	public boolean onContextItemSelected(MenuItem item) 
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		linearlayoutvisibility = true;
		setLinearLayoutVisibility();
		int groupid = item.getGroupId();
		int itemid = item.getItemId();
		
		if(itemid==1)
		{
			oldstring = ((TextView)info.targetView).getText().toString() ;
			ed.setText(oldstring);
			linearlayoutvisibility = false;
			setLinearLayoutVisibility();
		}
		
		if(itemid==2)
		{
				parking = (TextView)info.targetView;
				length = parking.getText().toString().length();
				position = info.position;
				return super.onContextItemSelected(item);
		}
		
		if(itemid==3)
		{
			oldstring = ((TextView)info.targetView).getText().toString() ;
			int index =0;
			for(String str : groupresults)
			{
				if(str.equals(oldstring))
				{
					index = groupresults.indexOf(str);
					break;
				}
			}	
				boolean legalindex = true;
				
				//checking whether index is not a groupheading
				for(Integer i : myadapter.groupheadings)
				{
					if(i==index)
						legalindex = false;
				}
				
				if(legalindex == false)
				{
					return super.onContextItemSelected(item);
				}
				
				ArrayList<Integer> parking = new ArrayList<Integer>();
				for(Integer i : myadapter.groupheadings)
				{
					if(i<=index)
					{
						parking.add(i);
					}
					else
					{
						parking.add(i-1);
					}
				}
				Collections.copy(myadapter.groupheadings, parking);
				groupresults.remove(index);
				myadapter.changeData(groupresults);
				
				
			
			
			
		}
		
		if (itemid > 10) 
		{
			
			//preventing deletion or illegal move of groupheadings(the titles of each group in the listing)
			for(Integer i : myadapter.groupheadings)
			{
				if(i==position)
				{
					closeContextMenu();
					return super.onContextItemSelected(item);
				}
			}
			//a legal move from one group to another
			int newgroup = itemid-11;
			groupresults.remove(position);
			ArrayList<Integer> parking186 = new ArrayList<Integer>();
			for(Integer i: myadapter.groupheadings)
			{
				if(i>position)
				{
					parking186.add(i-1);
				}
				else
				{
					parking186.add(i);
				}
			}
			Collections.copy(myadapter.groupheadings, parking186);
			int index = myadapter.groupheadings.get(newgroup);
			groupresults.add(index+1,(String) parking.getText());
			parking186.clear();
			
			for(Integer i: myadapter.groupheadings)
			{
				if(i>index)
				{
					parking186.add(i+1);
				}
				else
				{
					parking186.add(i);
				}
			}
			Collections.copy(myadapter.groupheadings, parking186);
			myadapter.changeData(groupresults);
			
			Log.e("Contextmenu", "groupid = " + String.valueOf(groupid));
			Log.e("Contextmenu", "itemid = " + String.valueOf(itemid));
		}
		return super.onContextItemSelected(item);
	}

	private  class btneditClickListener implements View.OnClickListener
	{
			@Override
			public void onClick(View v) 
			{
				String newtext = (String)ed.getText().toString();
				//case nothing happened
				if(newtext.equals(oldstring))
				{
					setLinearLayoutVisibility();
					return;
				}
				int index=0;
				
				//getting the index of the item in the list 
				for(String str : groupresults)
				{
					if(str.equals(oldstring))
					{
						index = groupresults.indexOf(str);
					}
				}
				boolean legalindex = true;
				
				//checking whether index is not a groupheading
				for(Integer i : myadapter.groupheadings)
				{
					if(i==index)
						legalindex = false;
				}

				
				//groupheading can only change name but cannot be deleted out of the list
				if(!legalindex&&newtext.equals(""))
				{
					groupresults.set(index,newtext);
					setLinearLayoutVisibility();
					return;
				}
				
				//deletion of a groupmember
				if(newtext.equals("")&&legalindex)
				{
					ArrayList<Integer> parking = new ArrayList<Integer>();
					for(Integer i : myadapter.groupheadings)
					{
						if(i<=index)
						{
							parking.add(i);
						}
						else
						{
							parking.add(i-1);
						}
					}
					Collections.copy(myadapter.groupheadings, parking);
					groupresults.remove(index);
					myadapter.changeData(groupresults);
					setLinearLayoutVisibility();
					return;
				}
				
				//change of a value
				else
				{
					groupresults.set(index, newtext);
					myadapter.changeData(groupresults);
					setLinearLayoutVisibility();
				}
			}
	}
	
	
	

	private void setLinearLayoutVisibility() 
	{
		/**
		 *
		 */
		 InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
		LinearLayout layout = (LinearLayout)findViewById(R.id.edittextlayout);
		int visi = 0;
		if(linearlayoutvisibility){visi=1;}
		
		switch(visi)
		{
		case 0:
			layout.setVisibility(View.VISIBLE);
			ed.requestFocus();
		    imm.toggleSoftInputFromWindow(ed.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
			linearlayoutvisibility = true;
			break;
		
		case 1:
			layout.setVisibility(View.GONE);
			//hide the soft keyboard after editing  
			imm.hideSoftInputFromWindow(ed.getWindowToken(), 0);
			linearlayoutvisibility = false;
			break;
		}
		
	}

	private class btnshareOnClickListener implements View.OnClickListener
	{

		@Override
		
		
		public void onClick(View v) {
			//Email message with the group divisions in it
			linearlayoutvisibility = true;
			setLinearLayoutVisibility();
		//Button b = (Button)findViewById(v.getId());
		//b.setEnabled(false);
		StringBuilder emailbody = new StringBuilder();
		for(String str : groupresults)
		{
			
			if(str.length()>5 && str.substring(0,5).equals("Groep"))
			{
				if(emailbody.length()>1 && emailbody.charAt(emailbody.length()-2)==',')
				{
					emailbody.deleteCharAt(emailbody.length()-2);
				}
				emailbody.append( "<h3>" + str + " :"+"</h3>" );
				
			}
			else
			{
				emailbody.append( "<i>"+str+"</i>" + ", ");
			}
		}
 		emailbody.deleteCharAt(emailbody.length()-2);

		
		
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("text/html");
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Groepsverdeling");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(emailbody.toString()));
		startActivity(Intent.createChooser(emailIntent, "Email met verdeling..."));

			
			
			
		}
	}
	
	
	
	private class btnsmsOnClickListener implements View.OnClickListener
	{

		//sms message with the group division in it.
		@Override
		public void onClick(View v) {
			linearlayoutvisibility = true;
			setLinearLayoutVisibility();
			//Button b = (Button)findViewById(v.getId());
			//b.setEnabled(false);
			StringBuilder smsbody = new StringBuilder();
			for(String str : groupresults)
			{
				
				if(str.length()>5 && str.substring(0,5).equals("Groep"))
				{
					if(smsbody.length()>1 && smsbody.charAt(smsbody.length()-2)==',')
					{
						smsbody.deleteCharAt(smsbody.length()-2);
					}
					smsbody.append( "\n" + str + " :" );
				}
				else
				{
					smsbody.append(str + ", ");
				}
			}
			smsbody.deleteCharAt(smsbody.length()-2);
			Intent smsIntent = new Intent(Intent.ACTION_VIEW);
			smsIntent.setType("vnd.android-dir/mms-sms");
			smsIntent.putExtra("address", "12125551212");
			smsIntent.putExtra("sms_body",smsbody.toString());
			startActivity(smsIntent);
			
			
		}
	}
	
	
	
	
	
	
	
	
	
	
}
