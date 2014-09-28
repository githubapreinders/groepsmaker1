package course.examples.UI.ListLayout;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;



public class MyCbArrayAdapter extends ArrayAdapter<Person> 
{
	public ArrayList<Person> group;
	private final Context context;
	private final String TAG= "CbArraylistadapter";

	public MyCbArrayAdapter(Context context,int textViewResourceId, ArrayList<Person> group) 
	{
		super(context,textViewResourceId, group);
		this.group = group;
		this.context = context;
	}

	

	
	@Override
	public View getView( int position, View convertView, ViewGroup parent) 
	{
		TextView tv;
		View view = convertView;
		if (view == null) 
		{
			LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.list_item_with_checkbox, null);
			tv = (TextView)view.findViewById(R.id.listitem_property);
			view.setTag(tv);
		}
		final Person person = getItem(position);
		final CheckBox cb; ;
		cb = (CheckBox)view.findViewById(R.id.checkBox1);
		cb.setTag(person);
		if(person.ischecked)
			cb.setChecked(true);
		else
		{
			cb.setChecked(false);
		}
		cb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				final Person myperson = (Person)v.getTag();
				if(cb.isChecked())
				{
					myperson.ischecked=true;
				}
				else 
				{	
					myperson.ischecked=false;
				}
				int index = 0;
				for(Person p : group)
				{
					if(p.getName().equals(myperson.getName()))
					{
						index = group.indexOf(p);
						break;
					}
				}
				group.remove(index);
				group.add(index,myperson);
				changeData(group);
				
			}
		});

		  Person p = (Person)cb.getTag();
		  p.setIschecked(person.ischecked);
		  p.setName(person.name);
		  TextView t = (TextView)view.getTag();
		  t.setText(person.name);
		return view;
	}

	public class ViewHolder
	{
		TextView tv;
		CheckBox cb;
		
		public ViewHolder(TextView t, CheckBox c)
		{
			this.tv = t;
			this.cb = c;
		}
		
	} 
	
	public void changeData(ArrayList<Person> data)
	{
		this.group = data;
		notifyDataSetChanged();
	}
		
}
	
	
	
	

