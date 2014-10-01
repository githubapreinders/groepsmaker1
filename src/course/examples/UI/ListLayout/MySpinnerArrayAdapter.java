package course.examples.UI.ListLayout;

import course.examples.UI.ListLayout.MyArrayListAdapter.ViewHolder;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

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

		View mypopupview = convertView;
		if(mypopupview==null)
		{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		final PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.popupwindow, null, false),400,600, true);
		pw.showAtLocation(parent, Gravity.CENTER, 0, 0);
		mypopupview = pw.getContentView();
		
		TextView t0 = (TextView)mypopupview.findViewById(R.id.textViewpopup0);
		t0.setText(data[position]);
		final TextView t1 = (TextView)mypopupview.findViewById(R.id.textViewpopup1);
		t1.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				pw.dismiss();
			}
		});
		}
		
		return mypopupview;
		
		
		
		/**
		 * View row = convertView;
		
		if (row == null)
		{
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(R.layout.spinner_item, parent, false);
		}
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

		 */
		
	}

}
