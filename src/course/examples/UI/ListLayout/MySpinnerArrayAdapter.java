package course.examples.UI.ListLayout;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{

		View v = super.getView(position, convertView, parent);

		((TextView) v).setTextSize(20);
		((TextView) v).setTextColor(Color.BLACK);
		((TextView) v).setPadding(50, 0, 0, 0);
        return v;
		
		
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent)
	{

		View row = convertView;
		if (row == null)
		{
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(R.layout.spinner_item, parent, false);
		}

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
