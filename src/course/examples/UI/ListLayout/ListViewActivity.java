package course.examples.UI.ListLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
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

	Spinner spin1;
	Spinner spin2;
	ArrayList<Integer> spinner1itemsarray;
	ArrayList<Integer> spinner2itemsarray;
	PopupWindow popup;
	ListView lv;
	Context context;
	Button spinnerpopup;
	private final String TAG = "in ListViewActivity";
	static int escapecounter = 0;
	boolean doneflag = false;
	String storednames;
	int HOWMUCHGROUPS;
	int GROUPSIZE;
	ViewHolder holder;
	final ArrayList<String> EMPTY = new ArrayList<String>();
	String[] names;
	String colorstring = "#667788";
	HashMap<String, Person> subgroupmap;
	// HashMap<String,Person> mapalt;
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
	public int[] colordrawables = { R.drawable.green1, R.drawable.green2, R.drawable.green3, R.drawable.red1,
			R.drawable.red2, R.drawable.red3, R.drawable.transparentkopie };

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_page);
		this.context = getApplicationContext();
		this.HOWMUCHGROUPS = 2;
		GROUPSIZE = ((GroepsMaker) getApplication()).gwendolyn.size();
		group = new ArrayList<String>();

		for (Map.Entry<String, Person> entry : ((GroepsMaker) getApplication()).gwendolyn.entrySet())
		{
			group.add(entry.getKey());
		}
		myadapter = new MyArrayListAdapter(this, R.id.listitem, group);
		myadapter.gwendolyn = ((GroepsMaker) getApplication()).gwendolyn;

		int counter = 0;
		for (String s : group)
		{
			int color = ((GroepsMaker) getApplication()).gwendolyn.get(s).getColorvalue();
			if (color != R.drawable.transparentkopie)
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
				holder.textView.setPadding(15, 0, 0, 4);
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

		spinnerpopup = (Button) findViewById(R.id.group_properties_spinner1);
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
			final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.popupwindow_spinner, null, false), 400,
					550, true);
			pw.showAtLocation(v.getRootView(), Gravity.CENTER, 0, 25);
			View mypopupview = pw.getContentView();

			final TextView t0 = (TextView) mypopupview.findViewById(R.id.textViewspinnerpopup0a);
			t0.setText(getResources().getString(R.string.aantal_personen) + " : " + GROUPSIZE);

			final TextView t1 = (TextView) mypopupview.findViewById(R.id.textViewspinnerpopup1);
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

			final TextView t2 = (TextView) mypopupview.findViewById(R.id.textViewspinnerpopup2);
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

			final TextView t3 = (TextView) mypopupview.findViewById(R.id.textViewspinnerpopup3);
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

			final TextView t4 = (TextView) mypopupview.findViewById(R.id.textViewspinnerpopup4);
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

			final TextView t5 = (TextView) mypopupview.findViewById(R.id.textViewspinnerpopup5);
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

			final TextView t6 = (TextView) mypopupview.findViewById(R.id.textViewspinnerpopup6);
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

			final TextView t7 = (TextView) mypopupview.findViewById(R.id.textViewspinnerpopup7);
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

			final TextView t8 = (TextView) mypopupview.findViewById(R.id.textViewspinnerpopup8);
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

			final TextView t9 = (TextView) mypopupview.findViewById(R.id.textViewspinnerpopup9);
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

			final TextView t10 = (TextView) mypopupview.findViewById(R.id.textViewspinnerpopup10);
			t10.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					HOWMUCHGROUPS = 11;
					spinnerpopup.setText("11");
					pw.dismiss();
				}
			});

			final TextView t11 = (TextView) mypopupview.findViewById(R.id.textViewspinnerpopup11);
			t11.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					HOWMUCHGROUPS = GROUPSIZE / 2;
					spinnerpopup.setText(String.valueOf(GROUPSIZE / 2));
					pw.dismiss();
				}
			});

			final TextView t12 = (TextView) mypopupview.findViewById(R.id.textViewspinnerpopup12);
			t12.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					HOWMUCHGROUPS = GROUPSIZE / 3;
					spinnerpopup.setText(String.valueOf(GROUPSIZE / 3));
					pw.dismiss();
				}
			});

			final TextView t13 = (TextView) mypopupview.findViewById(R.id.textViewspinnerpopup13);
			t13.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					HOWMUCHGROUPS = GROUPSIZE / 4;
					spinnerpopup.setText(String.valueOf(GROUPSIZE / 4));
					pw.dismiss();
				}
			});

			final TextView t14 = (TextView) mypopupview.findViewById(R.id.textViewspinnerpopup14);
			t14.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					pw.dismiss();

					LayoutInflater inflater2 = getLayoutInflater();
					final PopupWindow pop = new PopupWindow(inflater2.inflate(R.layout.popupwindow_spinner_submenu,
							null, false), 400, 550, true);
					View submenupopup = pop.getContentView();
					pop.showAtLocation(getListView(), Gravity.CENTER, 0, 0);

					final TextView t14a = (TextView) submenupopup.findViewById(R.id.Spinner_submenu1);
					t14a.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							pop.dismiss();
						}
					});

					final TextView t14b = (TextView) submenupopup.findViewById(R.id.Spinner_submenu1a);
					t14b.setText(getResources().getString(R.string.aantal_personen) + " :" + GROUPSIZE);

					spinner1itemsarray = new ArrayList<Integer>();
					for (int counter = 2; counter < (GROUPSIZE / 2) + 1; counter++)
					{
						spinner1itemsarray.add(counter);
					}
					spin1 = (Spinner) submenupopup.findViewById(R.id.Spinner_submenu_spinner1);
					ArrayAdapter<Integer> spin1adapter = new ArrayAdapter<Integer>(ListViewActivity.this,
							android.R.layout.simple_spinner_item, spinner1itemsarray);

					spin1adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spin1.setAdapter(spin1adapter);
					spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
					{

						@Override
						public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
						{
							Log.d(TAG, "position = " + position + " id= " + id + " aantal groepjes: "
									+ (int) spinner1itemsarray.get(position));
							changeSpinnerValues(1, position);
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent)
						{
							// TODO Auto-generated method stub

						}
					});

					Set<Integer> spinner2itemsset = new HashSet<Integer>();
					for (Integer k : spinner1itemsarray)
					{
						Integer item = GROUPSIZE / k;
						spinner2itemsset.add(item);
					}
					spinner2itemsarray = new ArrayList<Integer>(spinner2itemsset);
					Collections.sort(spinner2itemsarray);
					spin2 = (Spinner) submenupopup.findViewById(R.id.Spinner_submenu_spinner2);
					ArrayAdapter<Integer> spin2adapter = new ArrayAdapter<Integer>(ListViewActivity.this,
							android.R.layout.simple_spinner_item, spinner2itemsarray);

					spin2adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spin2.setAdapter(spin2adapter);
					spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
					{

						@Override
						public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
						{
							Log.d(TAG, "position = " + position + " id= " + id + " aantal groepjes: "
									+ (int) spinner1itemsarray.get(position));
							changeSpinnerValues(2, position);
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent)
						{
							// TODO Auto-generated method stub

						}
					});

					Button btnok = (Button) submenupopup.findViewById(R.id.Spinner_submenu_button);
					btnok.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							Integer i = (Integer) spin1.getSelectedItem();
							HOWMUCHGROUPS = (int) i;
							spinnerpopup.setText(String.valueOf(HOWMUCHGROUPS));
							pop.dismiss();
						}
					});
				}
			});

		}

	}

	public void changeSpinnerValues(int SpinnerId, int arrayposition)
	{

		switch (SpinnerId)
		{
		case 1:
			int helper = GROUPSIZE / (int) spinner1itemsarray.get(arrayposition);
			int helper2 = -1;
			for (Integer i : spinner2itemsarray)
			{
				if (i == helper)
				{
					helper2 = spinner2itemsarray.indexOf(i);
				}
			}
			spin2.setSelection(helper2);
			break;
		case 2:
			int helper3 = GROUPSIZE / (int) spinner2itemsarray.get(arrayposition);
			int helper4 = -1;
			for (Integer i : spinner1itemsarray)
			{
				if (i == helper3)
				{
					helper4 = spinner1itemsarray.indexOf(i);
				}
			}
			spin1.setSelection(helper4);

			break;
		default:
			break;
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

	public void changeSelectedColorSpinnerPopup(View v)
	{

	}

	// TODO : pile all the separate clickhandlers into one neat private class.
	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, final ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, view, menuInfo);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.popupwindow, null, false), 400, 600, true);
		pw.showAtLocation(view, Gravity.CENTER, 0, 0);
		View mypopupview = pw.getContentView();

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		final String name = (String) lv.getItemAtPosition(info.position);

		LinearLayout ll = (LinearLayout) info.targetView;
		TextView tv = (TextView) ll.getChildAt(0);
		holder = myadapter.new ViewHolder();
		holder.textView = (TextView) tv;

		TextView t0 = (TextView) mypopupview.findViewById(R.id.textViewpopup0);
		t0.setText(name);
		final TextView t1 = (TextView) mypopupview.findViewById(R.id.textViewpopup1);
		t1.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				handlepopupWindow(menuInfo, t1.getId());
				pw.dismiss();
			}
		});

		final TextView t2 = (TextView) mypopupview.findViewById(R.id.textViewpopup2);
		t2.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				handlepopupWindow(menuInfo, t2.getId());
				pw.dismiss();
			}
		});

		final TextView t3 = (TextView) mypopupview.findViewById(R.id.textViewpopup3);
		t3.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				handlepopupWindow(menuInfo, t3.getId());
				pw.dismiss();
			}
		});

		final TextView t4 = (TextView) mypopupview.findViewById(R.id.textViewpopup4);
		t4.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				handlepopupWindow(menuInfo, t4.getId());
				pw.dismiss();
			}
		});

		final TextView t5 = (TextView) mypopupview.findViewById(R.id.textViewpopup5);
		t5.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				handlepopupWindow(menuInfo, t5.getId());
				pw.dismiss();
			}
		});

		final TextView t6 = (TextView) mypopupview.findViewById(R.id.textViewpopup6);
		t6.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				handlepopupWindow(menuInfo, t6.getId());
				pw.dismiss();
			}
		});

		final TextView t7 = (TextView) mypopupview.findViewById(R.id.textViewpopup7);
		t7.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				handlepopupWindow(menuInfo, t7.getId());
				pw.dismiss();
			}
		});

		final TextView t8 = (TextView) mypopupview.findViewById(R.id.textViewpopup8);
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
	 * Contextmenu in the form of a popupwindow as an alternative for clicking
	 * repeatedly on a listview-item.
	 */

	public boolean handlepopupWindow(ContextMenuInfo item, int textviewid)
	{

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item;
		String name = (String) lv.getItemAtPosition(info.position);
		int id = (int) info.id;
		int position = info.position;
		int colorint = R.drawable.transparentkopie;
		Drawable dr;
		LinearLayout ll = (LinearLayout) info.targetView;
		TextView tv = (TextView) ll.getChildAt(0);
		holder = myadapter.new ViewHolder();
		holder.textView = (TextView) tv;

		switch (textviewid)
		{
		case R.id.textViewpopup2:
			colorint = colordrawables[0];
			dr = getResources().getDrawable(colorint);
			holder.getTextView().setBackgroundDrawable(dr);
			holder.getTextView().setPadding(15, 0, 0, 4);
			boolean val = ((GroepsMaker) getApplication()).gwendolyn.get(name).isIschecked();
			((GroepsMaker) getApplication()).gwendolyn.put(name, new Person(name, val, colorint));
			break;
		case R.id.textViewpopup3:
			colorint = colordrawables[1];
			dr = getResources().getDrawable(colorint);
			holder.getTextView().setBackgroundDrawable(dr);
			holder.getTextView().setPadding(15, 0, 0, 4);
			boolean val1 = ((GroepsMaker) getApplication()).gwendolyn.get(name).isIschecked();
			((GroepsMaker) getApplication()).gwendolyn.put(name, new Person(name, val1, colorint));
			break;
		case R.id.textViewpopup4:
			colorint = colordrawables[2];
			dr = getResources().getDrawable(colorint);
			holder.getTextView().setBackgroundDrawable(dr);
			holder.getTextView().setPadding(15, 0, 0, 4);
			boolean val2 = ((GroepsMaker) getApplication()).gwendolyn.get(name).isIschecked();
			((GroepsMaker) getApplication()).gwendolyn.put(name, new Person(name, val2, colorint));
			break;
		case R.id.textViewpopup5:
			colorint = colordrawables[3];
			dr = getResources().getDrawable(colorint);
			holder.getTextView().setBackgroundDrawable(dr);
			holder.getTextView().setPadding(15, 0, 0, 4);
			boolean val3 = ((GroepsMaker) getApplication()).gwendolyn.get(name).isIschecked();
			((GroepsMaker) getApplication()).gwendolyn.put(name, new Person(name, val3, colorint));
			break;
		case R.id.textViewpopup6:
			colorint = colordrawables[4];
			dr = getResources().getDrawable(colorint);
			holder.getTextView().setBackgroundDrawable(dr);
			holder.getTextView().setPadding(15, 0, 0, 4);
			boolean val4 = ((GroepsMaker) getApplication()).gwendolyn.get(name).isIschecked();
			((GroepsMaker) getApplication()).gwendolyn.put(name, new Person(name, val4, colorint));
			break;
		case R.id.textViewpopup7:
			colorint = colordrawables[5];
			dr = getResources().getDrawable(colorint);
			holder.getTextView().setBackgroundDrawable(dr);
			holder.getTextView().setPadding(15, 0, 0, 4);
			boolean val5 = ((GroepsMaker) getApplication()).gwendolyn.get(name).isIschecked();
			((GroepsMaker) getApplication()).gwendolyn.put(name, new Person(name, val5, colorint));
			break;
		case R.id.textViewpopup1:
			colorint = colordrawables[6];
			dr = getResources().getDrawable(colorint);
			holder.getTextView().setBackgroundDrawable(dr);
			holder.getTextView().setPadding(15, 0, 0, 4);
			boolean val6 = ((GroepsMaker) getApplication()).gwendolyn.get(name).isIschecked();
			((GroepsMaker) getApplication()).gwendolyn.put(name, new Person(name, val6, colorint));
			break;
		case R.id.textViewpopup8:

			for (Map.Entry<String, Person> entry : ((GroepsMaker) getApplication()).gwendolyn.entrySet())
			{
				entry.setValue(new Person(entry.getValue().getName(), entry.getValue().isIschecked(),
						R.drawable.transparentkopie));
			}

			Set<String> keys = myadapter.selectedIds.keySet();
			for (String i : keys)
			{
				myadapter.selectedIds.put(i, R.drawable.transparentkopie);
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
			GROUPSIZE = ((GroepsMaker) getApplication()).gwendolyn.size();
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
		final int SUBGROUPSIZE = GROUPSIZE / HOWMUCHGROUPS;
		int MAXSUBGROUPSIZE = SUBGROUPSIZE;
		if (GROUPSIZE % HOWMUCHGROUPS > 0)
		{
			MAXSUBGROUPSIZE++;
		}

		Log.i(TAG, "groupsize: " + GROUPSIZE + " subgroupsize: " + MAXSUBGROUPSIZE);

		ArrayList<Person> schonelei = new ArrayList<Person>();
		for (Map.Entry<String, Person> entry : ((GroepsMaker) getApplication()).gwendolyn.entrySet())
		{
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
			int begin = -1;
			int howmuchreds = 0;
			boolean flag = false;
			int indexgreen = 100;
			int color = schonelei.get(0).getColorvalue();
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
						begin = redcounter;
						flag = true;
					}
					do
					{
						if (schoneleisubs.get(redcounter).getSize() < SUBGROUPSIZE)
						{
							schoneleisubs.get(redcounter).addPerson(person);
							howmuchreds++;
							schonelei.remove(person);
							flag = false;
						}
						if (howmuchreds > HOWMUCHGROUPS || schoneleisubs.get(redcounter).getSize() > MAXSUBGROUPSIZE)
						{
							showAlertDialogInvalidConfigs();
							return;
						}
						redcounter++;
						if (redcounter == schoneleisubs.size())
						{
							redcounter = 0;
						}
					} while (flag == true && redcounter != begin);
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

		// check for invalid configs; we try 10 times to make a partition, after
		// that we give the user a dialog to change his config
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
		// sort the subgroups alphabeticaly and prepare an array with strings to
		// put in the intent.
		ArrayList<String> helper4 = new ArrayList<String>();
		int groupnum = 0;
		for (SubGroup s : schoneleisubs)
		{
			ArrayList<String> helper3 = new ArrayList<String>();
			for (Person p : s.getPersons())
			{
				helper3.add(p.getName());
			}
			Collections.sort(helper3);
			helper4.add(getResources().getString(R.string.groep1) + " " + String.valueOf(groupnum + 1));
			for (String str : helper3)
			{
				helper4.add(str);
			}
			groupnum++;
		}
		String[] resultgr = new String[GROUPSIZE + HOWMUCHGROUPS];
		int index2 = 0;
		for (String s1 : helper4)
		{
			resultgr[index2] = s1;
			index2++;
		}

		saveToSharedPrefs();
		Intent intent = new Intent(ListViewActivity.this, ResultPage.class);
		intent.putExtra("groupings", resultgr);
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
			} else if (s.getCheckedItems() == smallest)
			{
				listofsmallests.add(j);
			}
			j++;
		}
		Random ran = new Random();
		return listofsmallests.get(ran.nextInt(listofsmallests.size()));
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
