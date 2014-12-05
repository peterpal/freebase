package parser_indexer.parser;

import java.util.ArrayList;

public class Subject
{

	private String id = "";
	private String name = "";
	private ArrayList<String> aliases = new ArrayList<String>();
	private ArrayList<String> types = new ArrayList<String>();
	
	
	public Subject(String id)
	{
		
		this.id = id;
		
	}
	
	public void addProperty(Triplet t)
	{
		
		String predicate = t.getPredicateString();
		String value = t.getObjectString();
		
		if(predicate.equals("type.object.type"))
		{
			this.types.add(value);
		}
		else if(predicate.equals("common.topic.alias"))
		{
			this.aliases.add(value);
		}
		else if(predicate.equals("type.object.name"))
		{
			this.name = value;
		}
		
	}
	
	// check if subject contain certain property
	public Boolean hasProperty(String propertyType, String property)
	{
		
		// if property isnt null, we check specific property, else we check if any property of that type exists
		
		if(propertyType.equals("type"))
		{
			if((property != null && this.types.contains(property)) || (property == null && this.types.size() > 0))
			{
				return true;
			}
		}
		else if(propertyType.equals("alias"))
		{
			if((property != null && this.aliases.contains(property)) || (property == null && this.aliases.size() > 0))
			{
				return true;
			}
		}
		else if(propertyType.equals("name"))
		{
			if(this.name.equals(property) || !this.name.equals(""))
			{
				return true;
			}
		}
		else if(propertyType.equals("id"))
		{
			if(this.id.equals(property))
			{
				return true;
			}
		}
		
		return false;
		
	}
	
	public String toString()
	{
		
		// build XML output
		
		/*
		
		<object id="m.11fdvnf" name="Werner Riedl">
			<type object_ref="base.type_ontology.physically_instantiable" />
			<type object_ref="common.topic" />
			<type object_ref="base.type_ontology.animate" />
			<type object_ref="base.type_ontology.agent" />
			<type object_ref="music.group_member" />
			<type object_ref="people.person" />
			<alias value="Virginia 'Ginny' Van Jones">
		</object>
		
		*/
		
		String xml = "";
		
		if(this.name.equals(""))
		{
			xml += "\t<object id=\"" + this.id + "\">\n";
		} else
		{
			xml += "\t<object id=\"" + this.id + "\" name=\"" + this.name + "\">\n";
		}
		
		for(int i = 0; i < this.types.size(); i++){
			xml += "\t\t<type object_ref=\"" + this.types.get(i).replaceAll("&", "&amp;") + "\" />\n";
		}
		
		for(int i = 0; i < this.aliases.size(); i++){
			xml += "\t\t<alias value=\"" + this.aliases.get(i) + "\" />\n";
		}
		
		xml += "\t</object>\n";
		
		return xml;
		
	}
	
	
}
