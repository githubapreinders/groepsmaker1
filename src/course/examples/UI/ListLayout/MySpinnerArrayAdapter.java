package course.examples.UI.ListLayout;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MySpinnerArrayAdapter extends ArrayAdapter<String>

{
	private Activity context;
	String[] data = null;

	public MySpinnerArrayAdapter(Activity context, int resource, String[] data)
	{
		super(context, resource, data);
		this.context = context;
		this.data = data;
	}

	//the clickable view that invokes the spinner-menu
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{

		View v = super.getView(position, convertView, parent);

		((TextView) v).setTextSize(20);
		((TextView) v).setTextColor(Color.BLACK);
		((TextView) v).setPadding(50, 0, 0, 0);
        return v;
		
		
	}

	//the actual menu
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent)
	{

		View row = convertView;
		
		if (row == null)
		{
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(R.layout.spinner_item, parent, false);
		}
		//LinearLayout v = (LinearLayout)parent.getParent();
		//v.setVisibility(View.INVISIBLE);
		parent.setLayoutParams(new LinearLayout.LayoutParams(400, 450));
		Drawable dr = context.getResources().getDrawable(R.drawable.group1);
		parent.setBackgroundDrawable(dr);
		String item = data[position];

		if (item != null)
		{
			TextView myview = (TextView) row.findViewById(R.id.textView_spinner);

			if (myview != null)
				myview.setText(item);

		}

		return row;

	}

}
