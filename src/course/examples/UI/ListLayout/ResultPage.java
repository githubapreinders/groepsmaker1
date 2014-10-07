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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
	private boolean linearlayoutvisibility;
	private EditText ed;
	private String oldstring;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_page);
		Intent intent = getIntent();
		String[] helper = intent.getStringArrayExtra("groupings");
		HOWMUCHGROUPS = intent.getIntExtra("howmuchgroups", 2);
		groupresults = new ArrayList<String>();
		for (String s : helper)
		{
			groupresults.add(s);
		}
		myadapter = new MyArrayListAdapter(this, R.id.listitem, groupresults);
		setListAdapter(myadapter);
		lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
//				building a popupwindow with menu items
				linearlayoutvisibility = false;
				final LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
				final PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.popupwindow_resultpage, null, false),
						400, 400, true);
				pw.showAtLocation(view.getRootView(), Gravity.CENTER, 0, 0);
				View mypopupview = pw.getContentView();
				final String name = (String) lv.getItemAtPosition(position);

				TextView tv0 = (TextView) mypopupview.findViewById(R.id.textViewresultpage0);
				tv0.setText((String) lv.getItemAtPosition(position));

//				menu item for editing an entry
				TextView tv1 = (TextView) mypopupview.findViewById(R.id.textViewresultpage1);
				tv1.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						pw.dismiss();
						setLinearLayoutVisibility();
						oldstring = name;
						ed.setText(name);
					}
				});
//				menu item to move groupmember to another group
				TextView tv2 = (TextView) mypopupview.findViewById(R.id.textViewresultpage2);
				tv2.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						pw.dismiss();
//						when you want to move a groupheader that will be illegal
						int	pos = groupresults.indexOf(name);
						for(Integer i : myadapter.groupheadings)
						{
							if(i==pos)
							{
								return;
							}
						}
//						building a new popup that functions as a submenu						
						final PopupWindow pop = new PopupWindow(inflater.inflate(
								R.layout.popupwindow_resultpage_submenu, null, false), 400, 400, true);
						View submenupopup = pop.getContentView();
						pop.showAtLocation(getListView(), Gravity.CENTER, 0, 0);
						LinearLayout ll = (LinearLayout) submenupopup.findViewById(R.id.submenu);
						TextView tv = (TextView) inflater.inflate(R.layout.textview_template, null);
						tv.setText(name + " " +getResources().getString(R.string.verplaatsen_naar));
						tv.setGravity(Gravity.CENTER);
						tv.setOnClickListener(new View.OnClickListener()
						{
							@Override
							public void onClick(View v)
							{
								pop.dismiss();
							}
						});
						ll.addView(tv);
						TextView dummy = (TextView) inflater.inflate(R.layout.textview_template, null);
						ll.addView(dummy);

						for (final Integer dest : myadapter.groupheadings)
						{
							TextView t = (TextView) inflater.inflate(R.layout.textview_template, null);
							String groupname = (String) lv.getItemAtPosition(dest);
							t.setText(groupname);
							t.setOnClickListener(new View.OnClickListener()
							{
								@Override
								public void onClick(View v)
								{
									moveGroupMember(dest,name);
									pop.dismiss();
								}
							});
							ll.addView(t);
						}

					}
				});
//				menu item delete entry 
				TextView tv3 = (TextView) mypopupview.findViewById(R.id.textViewresultpage3);
				tv3.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						oldstring = name;
						int index = 0;
						for (String str : groupresults)
						{
							if (str.equals(oldstring))
							{
								index = groupresults.indexOf(str);
								break;
							}
						}
						boolean legalindex = true;

						// checking whether index is not a groupheading
						for (Integer i : myadapter.groupheadings)
						{
							if (i == index)
								legalindex = false;
						}

						if (legalindex == false)
						{
							pw.dismiss();
							return;
						}

						ArrayList<Integer> parking = new ArrayList<Integer>();
						for (Integer i : myadapter.groupheadings)
						{
							if (i <= index)
							{
								parking.add(i);
							} else
							{
								parking.add(i - 1);
							}
						}
						Collections.copy(myadapter.groupheadings, parking);
						groupresults.remove(index);
						myadapter.changeData(groupresults);
						pw.dismiss();
					}
				});
//				cancel menu item
				TextView tv4 = (TextView) mypopupview.findViewById(R.id.textViewresultpage4);
				tv4.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						pw.dismiss();
					}
				});
				
			}
		});

		Button btnshare = (Button) findViewById(R.id.verdeel);
		btnshare.setText(R.string.share);
		btnshare.setOnClickListener(new btnshareOnClickListener());
		Drawable img1 = getResources().getDrawable(R.drawable.mail_icon);
		img1.setBounds(10, 0, 48, 48);
		btnshare.setCompoundDrawables(img1, null, null, null);

		Button btnsms = (Button) findViewById(R.id.properties);
		btnsms.setText(R.string.sms);
		btnsms.setOnClickListener(new btnsmsOnClickListener());
		btnsms.setEnabled(true);
		Drawable img2 = getResources().getDrawable(R.drawable.sms_icon);
		img2.setBounds(10, 0, 48, 48);
		;
		btnsms.setCompoundDrawables(img2, null, null, null);

		Button spinbtn = (Button) findViewById(R.id.group_properties_spinner1);
		spinbtn.setVisibility(View.GONE);

		Button btnedit = (Button) findViewById(R.id.button1);
		btnedit.setOnClickListener(new btneditClickListener());

		ed = (EditText) findViewById(R.id.editText1);
		ed.setHint(getResources().getString(R.string.hint_text));

		LinearLayout layout = (LinearLayout) findViewById(R.id.edittextlayout);
		layout.setVisibility(View.GONE);
		linearlayoutvisibility = false;

	}

	public void moveGroupMember(int destination,String name)
	{
		// preventing deletion or illegal move of groupheadings(the titles
		// of each group in the listing)
		
		int	pos = groupresults.indexOf(name);
		int counter =0;
		for (Integer i : myadapter.groupheadings)
		{
			if(i==destination)
			{
				break;
			}
			counter++;
		}
		// a legal move from one group to another;remove the old name, find the new group, and insert
		groupresults.remove(pos);
		ArrayList<Integer> parking186 = new ArrayList<Integer>();
		for (Integer i : myadapter.groupheadings)
		{
			if (i > pos)
			{
				parking186.add(i - 1);
			} else
			{
				parking186.add(i);
			}
		}
		Collections.copy(myadapter.groupheadings, parking186);
		int index = myadapter.groupheadings.get(counter);
		groupresults.add(index + 1, name);
		parking186.clear();

		for (Integer i : myadapter.groupheadings)
		{
			if (i > index)
			{
				parking186.add(i + 1);
			} else
			{
				parking186.add(i);
			}
		}
		Collections.copy(myadapter.groupheadings, parking186);
		myadapter.changeData(groupresults);

	}

	@Override
	public void onBackPressed()
	{
		if (linearlayoutvisibility)
		{
			setLinearLayoutVisibility();
		} else
			super.onBackPressed();
	}

	
	private class btneditClickListener implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			String newtext = (String) ed.getText().toString();
			// case nothing happened
			if (newtext.equals(oldstring))
			{
				setLinearLayoutVisibility();
				return;
			}
			int index = 0;

			// getting the index of the item in the list
			for (String str : groupresults)
			{
				if (str.equals(oldstring))
				{
					index = groupresults.indexOf(str);
				}
			}
			boolean legalindex = true;

			// checking whether index is not a groupheading
			for (Integer i : myadapter.groupheadings)
			{
				if (i == index)
					legalindex = false;
			}

			// groupheading can only change name but cannot be deleted out of
			// the list
			if (!legalindex && newtext.equals(""))
			{
				groupresults.set(index, newtext);
				setLinearLayoutVisibility();
				return;
			}

			// deletion of a groupmember
			if (newtext.equals("") && legalindex)
			{
				ArrayList<Integer> parking = new ArrayList<Integer>();
				for (Integer i : myadapter.groupheadings)
				{
					if (i <= index)
					{
						parking.add(i);
					} else
					{
						parking.add(i - 1);
					}
				}
				Collections.copy(myadapter.groupheadings, parking);
				groupresults.remove(index);
				myadapter.changeData(groupresults);
				setLinearLayoutVisibility();
				return;
			}

			// change of a value
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
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		LinearLayout layout = (LinearLayout) findViewById(R.id.edittextlayout);
		int visi = 0;
		if (linearlayoutvisibility)
		{
			visi = 1;
		}

		switch (visi)
		{
		case 0:
			layout.setVisibility(View.VISIBLE);
			ed.requestFocus();
			imm.toggleSoftInputFromWindow(ed.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
			linearlayoutvisibility = true;
			break;

		case 1:
			layout.setVisibility(View.GONE);
			// hide the soft keyboard after editing
			imm.hideSoftInputFromWindow(ed.getWindowToken(), 0);
			linearlayoutvisibility = false;
			break;
		}

	}

	private class btnshareOnClickListener implements View.OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			// Email message with the group divisions in it
			linearlayoutvisibility = true;
			setLinearLayoutVisibility();
			// Button b = (Button)findViewById(v.getId());
			// b.setEnabled(false);
			StringBuilder emailbody = new StringBuilder();
			for (String str : groupresults)
			{

				if (str.length() > 5 && str.substring(0, 5).equals("Groep"))
				{
					if (emailbody.length() > 1 && emailbody.charAt(emailbody.length() - 2) == ',')
					{
						emailbody.deleteCharAt(emailbody.length() - 2);
					}
					emailbody.append("<h3>" + str + " :" + "</h3>");

				} else
				{
					emailbody.append("<i>" + str + "</i>" + ", ");
				}
			}
			emailbody.deleteCharAt(emailbody.length() - 2);

			final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			emailIntent.setType("text/html");
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Groepsverdeling");
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(emailbody.toString()));
			startActivity(Intent.createChooser(emailIntent, "Email met verdeling..."));

		}
	}

	private class btnsmsOnClickListener implements View.OnClickListener
	{

		// sms message with the group division in it.
		@Override
		public void onClick(View v)
		{
			linearlayoutvisibility = true;
			setLinearLayoutVisibility();
			// Button b = (Button)findViewById(v.getId());
			// b.setEnabled(false);
			StringBuilder smsbody = new StringBuilder();
			for (String str : groupresults)
			{

				if (str.length() > 5 && str.substring(0, 5).equals("Groep"))
				{
					if (smsbody.length() > 1 && smsbody.charAt(smsbody.length() - 2) == ',')
					{
						smsbody.deleteCharAt(smsbody.length() - 2);
					}
					smsbody.append("\n" + str + " :");
				} else
				{
					smsbody.append(str + ", ");
				}
			}
			smsbody.deleteCharAt(smsbody.length() - 2);
			Intent smsIntent = new Intent(Intent.ACTION_VIEW);
			smsIntent.setType("vnd.android-dir/mms-sms");
			smsIntent.putExtra("address", "12125551212");
			smsIntent.putExtra("sms_body", smsbody.toString());
			startActivity(smsIntent);

		}
	}

}
