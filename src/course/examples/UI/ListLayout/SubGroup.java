package course.examples.UI.ListLayout;

import java.util.ArrayList;

public class SubGroup 
{
ArrayList<Person> groupmembers;

	public SubGroup()
	{
		groupmembers = new ArrayList<Person>();
	}
	
	
	public int getCheckedItems()
	{
		int counter = 0; 
		for(Person p : groupmembers)
		{
			if(p.isIschecked())
			{
				counter++;
			}
		}
		
		return counter;
	}
	
	public String toString()
	{
		return "Subgroep met "+groupmembers.size()+" leden.";
	}
	
	
	public SubGroup(ArrayList<Person> groupmembers)
	{
		this.groupmembers = groupmembers;
	}

	public void addPerson(Person groupmember)
	{
		groupmembers.add(groupmember);
	}
	
	public void removePerson(Person groupmember)
	{
		groupmembers.remove(groupmember);
	}

	public int getSize()
	{
		return groupmembers.size();
	}
	
	
	public ArrayList<Person> getPersons() {
		return groupmembers;
	}

	public void setPersons(ArrayList<Person> groupmembers) {
		this.groupmembers = groupmembers;
	}

	
	
	
	
}
