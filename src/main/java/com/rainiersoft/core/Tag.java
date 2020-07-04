package com.rainiersoft.core;

public class Tag 
{

	/**
	 * A model class that represents a tag.
	 *
	 */
	    private String name;
	    private String value;
	    private String id;
	    private TagType tagType;
	     
	    public Tag(String name, String value, TagType tagType, String id) {
	        this.name = name;
	        this.value = value;
	        this.tagType = tagType;
	        this.id= id;
	    }
	    
	    public String getName() {
	        return name;
	    }
	 
	    public void setName(String name) {
	        this.name = name;
	    }
	    
	    public String getValue() {
	        return value;
	    }
	    
	    public String getId()
	    {
	    	return id;
	    }
	 
	    public void setId(String id)
	    {
	    	this.id = id;
	    }
	    public void setValue(String value) {
	        this.value = value;
	    }
	    
	    public TagType getTagType() {
	        return tagType;
	    }
	 
	    public void setTagType(TagType tagType) {
	        this.tagType = tagType;
	    }
	    

}
