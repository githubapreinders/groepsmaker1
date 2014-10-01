package course.examples.UI.ListLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import course.examples.UI.ListLayout.MyArrayListAdapter.ViewHolder;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class ListViewActivity extends ListActivity
{

	PopupWindow popup;
	ListView lv;
	Context context;
	Button spinnerpopup;
	private final String TAG = "in ListViewActivity";
	static int escapecounter = 0;
	boolean doneflag = false;
	String storednames;
	int HOWMUCHGROUPS;
	ViewHolder holder;
	final ArrayList<String> EMPTY = new ArrayList<String>();
	String[] names;
	String colorstring = "#667788";
	HashMap<String, Person> subgroupmap;
	//HashMap<String,Person> mapalt;
	ArrayList<String> group;
	MyArrayListAdapter myadapter;
	SharedPreferences prefs;
	Spinner spinner;
	final int REQUEST_GROUPMEMBERS = 33;
	public Integer GREEN1;
	public Integer GREEN2;
	public Integer GREEN3;
	public Integer RED1;
	public Integer RED2;
	public Integer RED3;
	public Integer BLACK;
	public int SUBGROUPSIZE;
	public int[] colordrawables = { R.drawable.ic_menu_green1kopie, R.drawable.ic_menu_green2kopie, R.drawable.ic_menu_green3kopie,
			R.drawable.ic_menu_red1kopie, R.drawable.ic_menu_red2kopie, R.drawable.ic_menu_red3kopie, R.drawable.ic_menu_transparentkopie };

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_page);
		this.context = getApplicationContext();
		this.HOWMUCHGROUPS=2;
		// Een spinner wordt gedefinieerd
//		spinner = (Spinner) findViewById(R.id.group_properties_spinner1);
//		//spinner.setBackgroundResource(R.drawable.btn_dropdown_normal);
//		String[] items = new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "10" };
//		MySpinnerArrayAdapter adapter = new MySpinnerArrayAdapter(this, android.R.layout.simple_spinner_item, items);
//		spinner.setAdapter(adapter);

		// groepsnamen en hoeveelheid subgroepjes worden uit de intent gehaald
		// uit de intentstring worden namen gefilterd(gebruikers mogen namen op
		// verschillende manieren invoeren, spaties en kommas worden als
		// scheidingstekens gezien)
		group = new ArrayList<String>();
		//mapalt = new HashMap<String,Person>();

		for (Map.Entry<String, Person> entry : ((GroepsMaker) getApplication()).gwendolyn.entrySet())
		{
			group.add(entry.getKey());
			// mapalt.put(entry.getKey(), entry.getValue());
		}
		myadapter = new MyArrayListAdapter(this, R.id.listitem, group);
		myadapter.gwendolyn = ((GroepsMaker) getApplication()).gwendolyn;

		int counter = 0;
		for (String s : group)
		{
			int color = ((GroepsMaker) getApplication()).gwendolyn.get(s).getColorvalue();
			if (color != R.drawable.ic_menu_transparentkopie)
			{
				myadapter.selectedIds.put(s, color);
				counter++;
			}
		}
		setListAdapter(myadapter);
		lv = getListView();
		registerForContextMenu(lv);
		/**
		 * een enkele klik op een item verandert de kleur van het item 1 stapje,
		 * lang klikken geeft het contextmenu
		 */
		lv.setOnItemClickListener(new OnItemClickListener()
		{

			public void onItemClick(AdapterView<?> parent, View view, int position, long id)

			{
				String name = (String) lv.getItemAtPosition(position);
				int colorint = getColorCodes(name);

				boolean value = ((GroepsMaker) getApplication()).gwendolyn.get(name).isIschecked();
				((GroepsMaker) getApplication()).gwendolyn.put(name, new Person(name, value, colorint));

				holder = (ViewHolder) view.getTag();
				holder.textView.setBackgroundDrawable(getResources().getDrawable(colorint));
				if (myadapter.selectedIds.containsKey(name))
				{
					myadapter.selectedIds.remove(name);
					myadapter.selectedIds.put(name, colorint);
				} else
				{
					myadapter.selectedIds.put(name, colorint);
				}

			}
		});

		/**
		 * knop voor de functie die de eigenlijke verdeling maakt en een knop om
		 * de namenlijst aan te passen
		 */
		Button btnmakesubgroups = (Button) findViewById(R.id.verdeel);
		btnmakesubgroups.setOnClickListener(new tomakeSubGroups());

		spinnerpopup = (Button)findViewById(R.id.group_properties_spinner1);
		spinnerpopup.setOnClickListener(new showSpinnerpopup());
		spinnerpopup.setText("2");
		
		Button btnGroupProperties = (Button) findViewById(R.id.properties);
		btnGroupProperties.setOnClickListener(new gotoProperties());

		LinearLayout layout = (LinearLayout) findViewById(R.id.edittextlayout);
		layout.setVisibility(View.GONE);
	}

	private class gotoProperties implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent(ListViewActivity.this, GroupProperties.class);
			startActivityForResult(intent, REQUEST_GROUPMEMBERS);
		}
	}

	private class showSpinnerpopup implements View.OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			final PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.popupwindow_spinner, null, false),400,550, true);
			pw.showAtLocation(v.getRootView(), Gravity.CENTER, 0, 25);
			View mypopupview = pw.getContentView();
			
			
			
			final TextView t1 = (TextView)mypopupview.findViewById(R.id.textViewspinnerpopup1);
			t1.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					HOWMUCHGROUPS = 2;
					spinnerpopup.setText("2");
					pw.dismiss();
				}
			});


			final TextView t2 = (TextView)mypopupview.findViewById(R.id.textViewspinnerpopup2);
			t2.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					HOWMUCHGROUPS = 3;
					spinnerpopup.setText("3");
					pw.dismiss();
				}
			});
			
			final TextView t3 = (TextView)mypopupview.findViewById(R.id.textViewspinnerpopup3);
			t3.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					HOWMUCHGROUPS = 4;
					spinnerpopup.setText("4");
					pw.dismiss();
				}
			});
			
			final TextView t4 = (TextView)mypopupview.findViewById(R.id.textViewspinnerpopup4);
			t4.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					HOWMUCHGROUPS = 5;
					spinnerpopup.setText("5");
					pw.dismiss();
				}
			});
			
			final TextView t5 = (TextView)mypopupview.findViewById(R.id.textViewspinnerpopup5);
			t5.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					HOWMUCHGROUPS = 6;
					spinnerpopup.setText("6");
					pw.dismiss();
				}
			});
			
			final TextView t6 = (TextView)mypopupview.findViewById(R.id.textViewspinnerpopup6);
			t6.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					HOWMUCHGROUPS = 7;
					spinnerpopup.setText("7");
					pw.dismiss();
				}
			});
			
			final TextView t7 = (TextView)mypopupview.findViewById(R.id.textViewspinnerpopup7);
			t7.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					HOWMUCHGROUPS = 8;
					spinnerpopup.setText("8");
					pw.dismiss();
				}
			});
			
			final TextView t8 = (TextView)mypopupview.findViewById(R.id.textViewspinnerpopup8);
			t8.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					HOWMUCHGROUPS = 9;
					spinnerpopup.setText("9");
					pw.dismiss();
				}
			});
			
			final TextView t9 = (TextView)mypopupview.findViewById(R.id.textViewspinnerpopup9);
			t9.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					HOWMUCHGROUPS = 10;
					spinnerpopup.setText("10");
					pw.dismiss();
				}
			});
			
					}
		
	}
	
	
	
	
	/**
	 * Verandert de dataset in een andere view wanneer er geen data zijn
	 * (gebruiksaanwijzing) of wanneer er op de contexthelp wordt gedrukt.
	 */
	public void onSetEmpty()
	{
		myadapter.changeData(EMPTY);
	}

	public void onSetData()
	{
		myadapter.changeData(group);
	}

	/**
	 * Haalt de volgende kleurcode op bij klikken op een item in de listview
	 * 
	 * @param name
	 * @return
	 */

	public int getColorCodes(String name)
	{
		int returnvalue = 0;
		int index = 0;
		for (int drawable : colordrawables)
		{
			if (drawable == ((GroepsMaker) getApplication()).gwendolyn.get(name).getColorvalue())
			{
				returnvalue = colordrawables[index + 1];
				break;
			} else
			{
				index++;
				if (index == (colordrawables.length - 1))
				{
					index = -1;
				}
			}
		}
		return returnvalue;
	}

	
	//TODO : pile all the separate clickhandlers into one neat private class.
	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, final ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, view, menuInfo);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		final PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.popupwindow, null, false),400,600, true);
		pw.showAtLocation(view, Gravity.CENTER, 0, 0);
		View mypopupview = pw.getContentView();
		
		
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		final String name = (String) lv.getItemAtPosition(info.position);
		TextView tv = (TextView) info.targetView;
		holder = myadapter.new ViewHolder();
		holder.textView = (TextView) tv;
		
		TextView t0 = (TextView)mypopupview.findViewById(R.id.textViewpopup0);
		t0.setText(name);
		final TextView t1 = (TextView)mypopupview.findViewById(R.id.textViewpopup1);
		t1.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				handlepopupWindow(menuInfo, t1.getId());
				pw.dismiss();
			}
		});

		final TextView t2 = (TextView)mypopupview.findViewById(R.id.textViewpopup2);
		t2.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				handlepopupWindow(menuInfo, t2.getId());
				pw.dismiss();
			}
		});
		
		final TextView t3 = (TextView)mypopupview.findViewById(R.id.textViewpopup3);
		t3.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				handlepopupWindow(menuInfo, t3.getId());
				pw.dismiss();
			}
		});
		
		final TextView t4 = (TextView)mypopupview.findViewById(R.id.textViewpopup4);
		t4.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				handlepopupWindow(menuInfo, t4.getId());
				pw.dismiss();
			}
		});
		
		final TextView t5 = (TextView)mypopupview.findViewById(R.id.textViewpopup5);
		t5.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				handlepopupWindow(menuInfo, t5.getId());
				pw.dismiss();
			}
		});
		
		final TextView t6 = (TextView)mypopupview.findViewById(R.id.textViewpopup6);
		t6.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				handlepopupWindow(menuInfo, t6.getId());
				pw.dismiss();
			}
		});
		
		final TextView t7 = (TextView)mypopupview.findViewById(R.id.textViewpopup7);
		t7.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				handlepopupWindow(menuInfo, t7.getId());
				pw.dismiss();
			}
		});
		
		final TextView t8 = (TextView)mypopupview.findViewById(R.id.textViewpopup8);
		t8.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				handlepopupWindow(menuInfo, t8.getId());
				pw.dismiss();
			}
		});
		
		
	}

	/**
	 * Contextmenu in the form of a popupwindow as an alternative for clicking repeatedly on 
	 * a listview-item.
	 */
	
	
	public boolean handlepopupWindow(ContextMenuInfo item, int textviewid)
	{

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item;
		String name = (String) lv.getItemAtPosition(info.position);
		int id = (int)info.id;
		int position = info.position;
		int colorint = R.drawable.ic_menu_transparentkopie;
		Drawable dr;
		TextView tv = (TextView) info.targetView;
		holder = myadapter.new ViewHolder();
		holder.textView = (TextView) tv;

		switch (textviewid)
		{
		case R.id.textViewpopup2:
			colorint = colordrawables[0];
			dr = getResources().getDrawable(colorint);
			holder.getTextView().setBackgroundDrawable(dr);
			boolean val = ((GroepsMaker) getApplication()).gwendolyn.get(name).isIschecked();
			((GroepsMaker) getApplication()).gwendolyn.put(name, new Person(name, val, colorint));
			break;
		case R.id.textViewpopup3:
			colorint = colordrawables[1];
			dr = getResources().getDrawable(colorint);
			holder.getTextView().setBackgroundDrawable(dr);
			boolean val1 = ((GroepsMaker) getApplication()).gwendolyn.get(name).isIschecked();
			((GroepsMaker) getApplication()).gwendolyn.put(name, new Person(name, val1, colorint));
			break;
		case R.id.textViewpopup4:
			colorint = colordrawables[2];
			dr = getResources().getDrawable(colorint);
			holder.getTextView().setBackgroundDrawable(dr);
			boolean val2 = ((GroepsMaker) getApplication()).gwendolyn.get(name).isIschecked();
			((GroepsMaker) getApplication()).gwendolyn.put(name, new Person(name, val2, colorint));
			break;
		case R.id.textViewpopup5:
			colorint = colordrawables[3];
			dr = getResources().getDrawable(colorint);
			holder.getTextView().setBackgroundDrawable(dr);
			boolean val3 = ((GroepsMaker) getApplication()).gwendolyn.get(name).isIschecked();
			((GroepsMaker) getApplication()).gwendolyn.put(name, new Person(name, val3, colorint));
			break;
		case R.id.textViewpopup6:
			colorint = colordrawables[4];
			dr = getResources().getDrawable(colorint);
			holder.getTextView().setBackgroundDrawable(dr);
			boolean val4 = ((GroepsMaker) getApplication()).gwendolyn.get(name).isIschecked();
			((GroepsMaker) getApplication()).gwendolyn.put(name, new Person(name, val4, colorint));
			break;
		case R.id.textViewpopup7:
			colorint = colordrawables[5];
			dr = getResources().getDrawable(colorint);
			holder.getTextView().setBackgroundDrawable(dr);
			boolean val5 = ((GroepsMaker) getApplication()).gwendolyn.get(name).isIschecked();
			((GroepsMaker) getApplication()).gwendolyn.put(name, new Person(name, val5, colorint));
			break;
		case R.id.textViewpopup1:
			colorint = colordrawables[6];
			dr = getResources().getDrawable(colorint);
			holder.getTextView().setBackgroundDrawable(dr);
			boolean val6 = ((GroepsMaker) getApplication()).gwendolyn.get(name).isIschecked();
			((GroepsMaker) getApplication()).gwendolyn.put(name, new Person(name, val6, colorint));
			break;
		case R.id.textViewpopup8:
			
			for(Map.Entry<String, Person>entry : ((GroepsMaker) getApplication()).gwendolyn.entrySet())
			{
				entry.setValue(new Person(entry.getValue().getName(),entry.getValue().isIschecked(),R.drawable.ic_menu_transparentkopie));
			}
			
			Set<String> keys = myadapter.selectedIds.keySet();
			for (String i : keys)
			{
				myadapter.selectedIds.put(i, R.drawable.ic_menu_transparentkopie);
			}
			myadapter.changeData(group);
			break;

		default:
			return true;
		}

		if (myadapter.selectedIds.containsKey(name))
		{
			myadapter.selectedIds.remove(name);
			myadapter.selectedIds.put(name, colorint);
		} else
		{
			myadapter.selectedIds.put(name, colorint);
		}
		boolean val = ((GroepsMaker) getApplication()).gwendolyn.get(name).isIschecked();
		((GroepsMaker) getApplication()).gwendolyn.put(name, new Person(name, val, colorint));
		// myadapter.changeData(data);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_GROUPMEMBERS && resultCode == RESULT_OK)
		{
			group.clear();
			for (Map.Entry<String, Person> entry : ((GroepsMaker) getApplication()).gwendolyn.entrySet())
			{
				group.add(entry.getKey());
			}
			onSetData();
		}
	}

	@Override
	protected void onResume()
	{

		super.onResume();
		doneflag = false;
	}

	public void makeSmartDivision()
	{
		if (doneflag == true)
			return;
		if (((GroepsMaker) getApplication()).gwendolyn.size() < 1
				|| (((GroepsMaker) getApplication()).gwendolyn.size() / HOWMUCHGROUPS) < 1)
		{
			showAlertDialogNogroupmembers();
			return;
		}

		GREEN1 = colordrawables[0];
		GREEN2 = colordrawables[1];
		GREEN3 = colordrawables[2];
		RED1 = colordrawables[3];
		RED2 = colordrawables[4];
		RED3 = colordrawables[5];
		BLACK = colordrawables[6];
		final int GROUPSIZE = ((GroepsMaker) getApplication()).gwendolyn.size();
		SUBGROUPSIZE = GROUPSIZE / HOWMUCHGROUPS;
		int MAXSUBGROUPSIZE = SUBGROUPSIZE;
		if (GROUPSIZE % HOWMUCHGROUPS > 0)
		{
			MAXSUBGROUPSIZE++;
		}

		Log.i(TAG, "groupsize: " + GROUPSIZE + " subgroupsize: " + MAXSUBGROUPSIZE);

		ArrayList<Person> schonelei = new ArrayList<Person>();
		for (Map.Entry<String, Person> entry : ((GroepsMaker) getApplication()).gwendolyn.entrySet())
		{
			// schonelei.add(new GroupMember(entry.getKey(),entry.getValue()));
			schonelei.add(entry.getValue());
		}

		// make a list with empty subgroups
		ArrayList<SubGroup> schoneleisubs = new ArrayList<SubGroup>();
		for (int i = 0; i < HOWMUCHGROUPS; i++)
		{
			schoneleisubs.add(new SubGroup());
		}

		Collections.sort(schonelei);

		// TODO
		/**
		 * maxsubgroupsize trycatchblokken voor ongeoorloofde waarden.
		 */
		ArrayList<Person> helper = new ArrayList<Person>();
		do
		{
			int redcounter = 0;
			boolean flag = false;
			int indexgreen = 100;
			int color = schonelei.get(schonelei.size() - 1).getColorvalue();
			for (Person p : schonelei)
			{
				if (p.getColorvalue() == color)
					helper.add(p);
			}
			for (Person person : helper)
			{
				if (color == GREEN1 || color == GREEN2 || color == GREEN3)
				{
					if (flag == false)
					{
						indexgreen = getSmallestAvailableSubgroup(schoneleisubs);
						flag = true;
					}
					schoneleisubs.get(indexgreen).addPerson(person);
					schonelei.remove(person);
					if (schoneleisubs.get(indexgreen).getSize() > MAXSUBGROUPSIZE)
					{
						showAlertDialogInvalidConfigs();
						return;
					}

				}
				if (color == RED1 || color == RED2 || color == RED3)
				{
					if (flag == false)
					{
						redcounter = getSmallestAvailableSubgroup(schoneleisubs);
						flag = true;
					}

					schoneleisubs.get(redcounter).addPerson(person);
					schonelei.remove(person);
					if (schoneleisubs.get(redcounter).getSize() > MAXSUBGROUPSIZE)
					{
						showAlertDialogInvalidConfigs();
						return;
					}
					redcounter++;
					if (redcounter == schoneleisubs.size())
					{
						redcounter = 0;
					}
				}
				if (color == BLACK && person.isIschecked() == true)
				{
					schoneleisubs.get(getSmallestAvailableSubgroupForCheckedItem(schoneleisubs)).addPerson(person);
					schonelei.remove(person);
				}
				if (color == BLACK && person.isIschecked() == false)
				{
					schoneleisubs.get(getSmallestAvailableSubgroup(schoneleisubs)).addPerson(person);
					schonelei.remove(person);
				}
			}
			helper.clear();
		} while (schonelei.size() > 0);

		// check for invalid configs
		for (SubGroup sg : schoneleisubs)
		{
			if (sg.getSize() > MAXSUBGROUPSIZE || sg.getSize() < SUBGROUPSIZE)
			{
				escapecounter++;
				schoneleisubs.clear();
				if (escapecounter == 11)
				{
					showAlertDialogInvalidConfigs();
					return;
				}
				makeSmartDivision();
				return;
			}
		}
		escapecounter = 0;

		// prepare a string for the resultpage
		int groupnumber = 0;
		int memberindex = 0;
		String[] resultgroups = new String[GROUPSIZE + HOWMUCHGROUPS];

		for (int index1 = 0; index1 < resultgroups.length; index1++)
		{

			if (memberindex == 0)
			{
				resultgroups[index1] = "Groep" + (String.valueOf(groupnumber + 1));
				memberindex++;
			} else
			{
				resultgroups[index1] = schoneleisubs.get(groupnumber).getPersons().get(memberindex - 1).getName();
				memberindex++;
				if (memberindex == schoneleisubs.get(groupnumber).getSize() + 1)
				{
					memberindex = 0;
					groupnumber++;
				}
			}
		}
		saveToSharedPrefs();
		Intent intent = new Intent(ListViewActivity.this, ResultPage.class);
		intent.putExtra("groupings", resultgroups);
		intent.putExtra("howmuchgroups", HOWMUCHGROUPS);
		startActivity(intent);
		doneflag = true;

	}

	public int getSmallestAvailableSubgroup(ArrayList<SubGroup> schoneleisubs)
	{
		int small = 100;
		for (SubGroup s : schoneleisubs)
		{
			if (s.getSize() < small)
			{
				small = s.getSize();
			}
		}
		ArrayList<Integer> listofsmallests = new ArrayList<Integer>();
		int j = 0;
		for (SubGroup s : schoneleisubs)
		{
			if (s.getSize() == small)
			{
				listofsmallests.add(j);
			}
			j++;
		}
		Random ran = new Random();
		return listofsmallests.get(ran.nextInt(listofsmallests.size()));
	}

	public int getSmallestAvailableSubgroupForCheckedItem(ArrayList<SubGroup> schoneleisubs)
	{
		int smallest = 100;
		int smallestsize = 100;
		for (SubGroup s : schoneleisubs)
		{
			int sz = s.getSize();
			if (sz < smallestsize)
			{
				smallestsize = sz;
			}
			int ci = s.getCheckedItems();
			if (ci < smallest)
			{
				smallest = ci;
			}
		}
		ArrayList<Integer> listofsmallests = new ArrayList<Integer>();
		int j = 0;
		for (SubGroup s : schoneleisubs)
		{
			if (s.getCheckedItems() == smallest && s.getSize() == smallestsize)
			{
				listofsmallests.add(j);
				break;
			}
			if (s.getCheckedItems() == smallest)
			{
				listofsmallests.add(j);
			}
			j++;
		}
		Random ran = new Random();
		return listofsmallests.get(ran.nextInt(listofsmallests.size()));
	}

	public void makeSubGroups()
	{
		if (doneflag == true)
			return;
		HOWMUCHGROUPS = Integer.parseInt(spinner.getSelectedItem().toString());
		if (((GroepsMaker) getApplication()).gwendolyn.size() < 1
				|| (((GroepsMaker) getApplication()).gwendolyn.size() / HOWMUCHGROUPS) < 1)
		{
			showAlertDialogNogroupmembers();
			return;
		}

		GREEN1 = colordrawables[0];
		GREEN2 = colordrawables[1];
		GREEN3 = colordrawables[2];
		RED1 = colordrawables[3];
		RED2 = colordrawables[4];
		RED3 = colordrawables[5];
		BLACK = colordrawables[6];
		final int GROUPSIZE = ((GroepsMaker) getApplication()).gwendolyn.size();
		SUBGROUPSIZE = GROUPSIZE / HOWMUCHGROUPS;
		int MAXSUBGROUPSIZE = SUBGROUPSIZE;
		if (GROUPSIZE % HOWMUCHGROUPS > 0)
		{
			MAXSUBGROUPSIZE++;
		}

		Log.i(TAG, "groupsize: " + GROUPSIZE + " subgroupsize: " + MAXSUBGROUPSIZE);

		ArrayList<Person> schonelei = new ArrayList<Person>();
		for (Map.Entry<String, Person> entry : ((GroepsMaker) getApplication()).gwendolyn.entrySet())
		{
			// schonelei.add(new GroupMember(entry.getKey(),entry.getValue()));
			schonelei.add(entry.getValue());
		}

		Collections.sort(schonelei);

		// check for impossible color preferences
		boolean validconfig = checkforValidConfig(schonelei);
		if (!(validconfig))
		{
			showAlertDialogInvalidConfigs();
			return;
		}

		// make a list with empty subgroups
		ArrayList<SubGroup> schoneleisubs = new ArrayList<SubGroup>();
		for (int i = 0; i < HOWMUCHGROUPS; i++)
		{
			schoneleisubs.add(new SubGroup());
		}

		// choose a random item from the subgroupmap until is is a red or a
		// green one ; all the colors
		// will be put in subgroups first, the rest will be distributed later
		// among the subgroups
		int pointer1 = 0;
		boolean colorspresent = false;
		for (Person m : schonelei)
		{
			if (!(m.getColorvalue() == (BLACK)))
			{
				colorspresent = true;
				break;
			}
		}

		while (colorspresent)
		{

			Integer value = BLACK;
			while (value.equals(BLACK))
			{
				Random random = new Random();
				value = schonelei.get(random.nextInt(schonelei.size())).getColorvalue();
			}
			// Group the red choices and put them in different groups
			if (value.equals(RED1) || value.equals(RED2) || value.equals(RED3))
			{
				ArrayList<Integer> helper = new ArrayList<Integer>();
				for (Person m : schonelei)
				{
					if (m.getColorvalue() == (value))
					{
						schoneleisubs.get(pointer1).addPerson(m);
						int index = schonelei.indexOf(m);
						helper.add(index);
						pointer1++;
						if (pointer1 == schoneleisubs.size())
						{
							pointer1 = 0;
						}
					}
				}
				Collections.sort(helper, Collections.reverseOrder());
				for (int i = 0; i < helper.size(); i++)
				{
					schonelei.remove((int) helper.get(i));
				}
			}
			// Group green choices and put them if pos in the same group
			if (value.equals(GREEN1) || value.equals(GREEN2) || value.equals(GREEN3))
			{
				ArrayList<Integer> helper = new ArrayList<Integer>();
				SubGroup sg = new SubGroup();
				int counter = 0;
				for (Person m : schonelei)
				{
					if (m.getColorvalue() == (value))
					{
						sg.addPerson(m);
						int index = schonelei.indexOf(m);
						helper.add(index);
						counter++;
					}
				}
				// determine the smallest subgroup
				int parking = 0;
				for (int i = 0; i < HOWMUCHGROUPS - 1; i++)
				{
					if (schoneleisubs.get(i).getSize() > schoneleisubs.get(i + 1).getSize())
					{
						parking = i + 1;
					}
				}
				// put green members together in the smalles available subgroup
				for (int i = 0; i < counter; i++)
				{
					schoneleisubs.get(parking).addPerson(sg.getPersons().get(i));
				}
				Collections.sort(helper, Collections.reverseOrder());
				for (int i = 0; i < helper.size(); i++)
				{
					schonelei.remove((int) helper.get(i));
				}

			}
			// check if still colors are present in the arraylist
			colorspresent = false;
			for (Person m : schonelei)
			{
				if (!(m.getColorvalue() == (BLACK)))
				{
					colorspresent = true;
					break;
				}

			}

		}
		// distribute the rest of the persons over the remaining space
		// DONE remaining members have to be distributed evenly
		if (schonelei.size() > 0)
		{
			Collections.shuffle(schonelei);
			int index = 0;
			int index2 = 0;
			for (Person m : schonelei)
			{
				boolean given = false;
				do
				{
					int x = getSmallestAvailableSubgroup(schoneleisubs);

					if (index < (HOWMUCHGROUPS))
					{
						if (schoneleisubs.get(index).getSize() < SUBGROUPSIZE)
						{
							schoneleisubs.get(index).addPerson(m);
							given = true;
						} else
						{
							index++;
						}
					} else
					{
						schoneleisubs.get(index2).addPerson(m);
						given = true;
						index2++;
					}
				} while (!given);
			}

		}
		// check for subgroupsizeifferences > 1
		for (SubGroup sg : schoneleisubs)
		{
			if (sg.getSize() > MAXSUBGROUPSIZE)
			{
				escapecounter++;
				schoneleisubs.clear();
				makeSubGroups();
				if (escapecounter == 11)
				{
					showAlertDialogInvalidConfigs();
					return;
				}
				return;
			}
		}
		escapecounter = 0;
		int groupnumber = 0;
		int memberindex = 0;
		String[] resultgroups = new String[GROUPSIZE + HOWMUCHGROUPS];

		for (int index1 = 0; index1 < resultgroups.length; index1++)
		{

			if (memberindex == 0)
			{
				resultgroups[index1] = "Groep" + (String.valueOf(groupnumber + 1));
				memberindex++;
			} else
			{
				resultgroups[index1] = schoneleisubs.get(groupnumber).getPersons().get(memberindex - 1).getName();
				memberindex++;
				if (memberindex == schoneleisubs.get(groupnumber).getSize() + 1)
				{
					memberindex = 0;
					groupnumber++;
				}
			}
		}
		saveToSharedPrefs();
		Intent intent = new Intent(ListViewActivity.this, ResultPage.class);
		intent.putExtra("groupings", resultgroups);
		intent.putExtra("howmuchgroups", HOWMUCHGROUPS);
		startActivity(intent);
		doneflag = true;

	}

	public void saveToSharedPrefs()
	{
		String tostorestring = "";
		for (Map.Entry<String, Person> p : ((GroepsMaker) getApplication()).getGwendolyn().entrySet())
		{
			tostorestring += p.getValue().getName() + " " + p.getValue().isIschecked() + " "
					+ p.getValue().getColorvalue() + " ";
		}

		SharedPreferences.Editor prefseditor = getSharedPreferences("storednames1", MODE_PRIVATE).edit();
		prefseditor.putString("storednames1", tostorestring);
		prefseditor.commit();
	}

	private class tomakeSubGroups implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			// makeSubGroups();
			makeSmartDivision();
		}

	};

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

	public boolean checkforValidConfig(ArrayList<Person> schonelei)
	{

		for (Person m : schonelei)
		{
			Integer workingvalue = m.getColorvalue();
			int samevaluecounter = 1;
			for (int i = schonelei.indexOf(m) + 1; i < schonelei.size(); i++)
			{
				if (schonelei.get(i).getColorvalue() == (workingvalue))
				{
					samevaluecounter++;
					if (samevaluecounter > SUBGROUPSIZE
							&& (workingvalue.equals(GREEN1) || workingvalue.equals(GREEN2) || workingvalue
									.equals(GREEN3)))
					{
						return false;
					}
					if (samevaluecounter > HOWMUCHGROUPS
							&& (workingvalue.equals(RED1) || workingvalue.equals(RED2) || workingvalue.equals(RED3)))
					{
						return false;
					}
				}
			}
		}
		return true;
	}

	private void showAlertDialogNogroupmembers()
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListViewActivity.this);

		// dialoogboodschap
		alertDialogBuilder.setMessage(getResources().getString(R.string.nogroupmembers)).setCancelable(false)
				.setPositiveButton(R.string.leden_toevoegen, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						/**
						 * naar activiteit leden toevoegen sturen
						 */
						Intent intent = new Intent(ListViewActivity.this, GroupProperties.class);
						startActivityForResult(intent, REQUEST_GROUPMEMBERS);
						dialog.cancel();
					}
				})
				.setNegativeButton(getResources().getString(R.string.annuleren), new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						/**
						 * terug naar het scherm en dit keer zonder NPE
						 */
						dialog.cancel();
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();
	}

	// Gebruiker kiest kleurencombinatie die niet kan
	private void showAlertDialogInvalidConfigs()
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListViewActivity.this);

		// dialoogboodschap
		alertDialogBuilder.setMessage(getResources().getString(R.string.kleurencombinatiesnietmogelijk))
				.setCancelable(false)
				.setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						/**
						 * terug naar het scherm en dit keer zonder NPE
						 */
						dialog.cancel();
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();
	}

}
