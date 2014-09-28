package course.examples.UI.ListLayout;

public class Person implements Comparable<Person>
{
	public int[] colordrawables = { R.drawable.ic_menu_green1kopie,
			R.drawable.ic_menu_green2kopie, R.drawable.ic_menu_green3kopie,
			R.drawable.ic_menu_red1kopie, R.drawable.ic_menu_red2kopie,
			R.drawable.ic_menu_red3kopie, R.drawable.ic_menu_transparentkopie };
	public Integer GREEN1 = colordrawables[0];
	public Integer GREEN2 = colordrawables[1];
	public Integer GREEN3 = colordrawables[2];
	public Integer RED1 = colordrawables[3];
	public Integer RED2 = colordrawables[4];
	public Integer RED3 = colordrawables[5];
	public Integer BLACK = colordrawables[6];

	String name;
	boolean ischecked;
	int colorvalue;

	public Person(String name)
	{
		this.name = name;
		this.ischecked = false;
		this.colorvalue = R.drawable.ic_menu_transparent;
	}

	public Person(String name, boolean ischecked, int color)
	{
		this.name = name;
		this.ischecked = ischecked;
		this.colorvalue = color;
	}

	public int getColorvalue()
	{
		return colorvalue;
	}

	public void setColorvalue(int colorvalue)
	{
		this.colorvalue = colorvalue;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isIschecked()
	{
		return ischecked;
	}

	public void setIschecked(boolean ischecked)
	{
		this.ischecked = ischecked;
	}

	public String toString()
	{
		return this.name + " " + this.ischecked + " " + this.colorvalue + " = "
				+ translateTOString(this.colorvalue);
	}

	public String translateTOString(int colorvalue)
	{
		if (colorvalue == GREEN1)
			return "GREEN1";
		
		if (colorvalue == GREEN2)
			return "GREEN2";
		
		if (colorvalue == GREEN3)
			return "GREEN3";
		
		if (colorvalue == RED1)
			return "RED1";
		
		if (colorvalue == RED2)
			return "RED2";
		
		if (colorvalue == RED3)
			return "RED3";
		
		return "BLACK";
	}

	@Override
	public int compareTo(Person another)
	{
		Boolean b1 = this.ischecked;
		Boolean b2 = another.ischecked;

		if (this.getColorvalue() == GREEN1 && another.getColorvalue() == GREEN1
				|| this.getColorvalue() == GREEN3
				&& another.getColorvalue() == GREEN3
				|| this.getColorvalue() == GREEN2
				&& another.getColorvalue() == GREEN2
				|| this.getColorvalue() == RED1
				&& another.getColorvalue() == RED1
				|| this.getColorvalue() == RED2
				&& another.getColorvalue() == RED2
				|| this.getColorvalue() == RED3
				&& another.getColorvalue() == RED3)
				
			return 0;

		if (this.getColorvalue() == GREEN1)
		{
			return 1;
		}

		if (this.getColorvalue() == GREEN2)
		{
			if (another.getColorvalue() == GREEN1)
				return -1;
			return 1;
		}

		if (this.getColorvalue() == GREEN3)
		{
			if (another.getColorvalue() == GREEN1
					|| another.getColorvalue() == GREEN2)
				return -1;
			return 1;
		}

		if (this.getColorvalue() == RED1)
		{
			if (another.getColorvalue() == GREEN1
					|| another.getColorvalue() == GREEN2
					|| another.getColorvalue() == GREEN3)
				return -1;
			return 1;
		}

		if (this.getColorvalue() == RED2)
		{
			if (another.getColorvalue() == GREEN1
					|| another.getColorvalue() == GREEN2
					|| another.getColorvalue() == GREEN3
					|| another.getColorvalue() == RED1)
				return -1;
			return 1;
		}

		if (this.getColorvalue() == RED3)
		{
			if (another.getColorvalue() == GREEN1
					|| another.getColorvalue() == GREEN2
					|| another.getColorvalue() == GREEN3
					|| another.getColorvalue() == RED1
					|| another.getColorvalue() == RED2)
				return -1;
			return 1;
		}

		if (this.getColorvalue() == BLACK)
		{
			if (another.getColorvalue() == GREEN1
					|| another.getColorvalue() == GREEN2
					|| another.getColorvalue() == GREEN3
					|| another.getColorvalue() == RED1
					|| another.getColorvalue() == RED2
					|| another.getColorvalue() == RED3)
				return -1;
		}

		if(this.getColorvalue() == BLACK&&another.getColorvalue() == BLACK)
		return b1.compareTo(b2);
		
		return 0;

	}

}