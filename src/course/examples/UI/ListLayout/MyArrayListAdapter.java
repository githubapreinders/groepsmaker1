package course.examples.UI.ListLayout;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyArrayListAdapter extends ArrayAdapter<String>
{

	private ArrayList<String> group;
	public HashMap<String,Person> gwendolyn;
	public HashMap<String, Integer> selectedIds;
	public HashMap<Integer, Integer> selectedIds2;
	public ArrayList<Integer> groupheadings;
	private final Context context;
	Drawable img1;
	private final String TAG = "Arraylistadapter";

	public MyArrayListAdapter(Context context, int textViewResourceId,
			ArrayList<String> group)
	{
		super(context, textViewResourceId, group);
		this.group = group;
		this.context = context;
		this.gwendolyn = new HashMap<String,Person>();
		this.selectedIds = new HashMap<String, Integer>();
		this.selectedIds2 = new HashMap<Integer, Integer>();
		this.groupheadings = new ArrayList<Integer>();
		for (String str : group)
		{
			if (str.length() > 5 && str.substring(0, 5).equals("Groep"))
			{
				groupheadings.add(group.indexOf(str));
			}
		}
		img1 = context.getResources().getDrawable(
				R.drawable.resultpage_groepheader);

	}

	public void changeData(ArrayList<String> data)
	{
		this.group = data;
		notifyDataSetChanged();
	}

	public class ViewHolder
	{
		public TextView textView;
		public String label;

		public ViewHolder()
		{
		}

		public ViewHolder(TextView textView)
		{

			this.textView = textView;
		}

		public TextView getTextView()
		{
			return textView;
		}

		public void setTextView(TextView textView)
		{
			this.textView = textView;
		}

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{

		TextView textview = null;
		String item = (String) getItem(position);

		if (convertView == null)
		{
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.list_item, null);
			textview = (TextView) convertView.findViewById(R.id.listitem);
			convertView.setTag(new ViewHolder(textview));
			for (Integer i : groupheadings)
			{
				if (i == position)
				{
					textview.setBackgroundDrawable(img1);
					textview.setPadding(0, 10, 0, 0);
					textview.setText(item);
					return convertView;
				}
			}
			if (context instanceof ListViewActivity)
			{
				Drawable dr = context.getResources().getDrawable(gwendolyn.get(item).getColorvalue());
				textview.setBackgroundDrawable(dr);
			}
			if(context instanceof ResultPage)
			{
				Drawable dr = context.getResources().getDrawable(R.drawable.ic_menu_transparentkopie);
				textview.setBackgroundDrawable(dr);
			}

		} else
		{
			ViewHolder holder = (ViewHolder) convertView.getTag();
			textview = holder.getTextView();
			textview.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_menu_transparentkopie));
			textview.setPadding(20, 0, 0, 0);
			if (selectedIds.containsKey(item))
			{
				Drawable dr = context.getResources().getDrawable(
						selectedIds.get(item));
				textview.setBackgroundDrawable(dr);
			}

			for (Integer i : groupheadings)
			{
				if (i == position)
				{
					textview.setBackgroundDrawable(img1);
					textview.setPadding(0, 10, 0, 0);
				}
			}

		}

		textview.setText(item);

		return convertView;
	}

}
