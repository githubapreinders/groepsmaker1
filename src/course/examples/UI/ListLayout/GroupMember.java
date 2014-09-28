package course.examples.UI.ListLayout;

public class GroupMember
{
String name;
Integer color;
Boolean checked;

	public GroupMember()
	{
		
	}
	
	public GroupMember(String name, Integer integer)
	{
		this.name = name;
		this.color = integer;
		this.checked = false;
	}

	
	public GroupMember(String name, Integer integer,Boolean checked)
	{
		this.name = name;
		this.color = integer;
		this.checked = checked;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getColor() {
		return color;
	}

	public void setColor(Integer color) {
		this.color = color;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	
}
