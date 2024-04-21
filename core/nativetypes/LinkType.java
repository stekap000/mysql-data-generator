package core.nativetypes;

public enum LinkType{
	SIN("sin"),
	ABSSIN("abssin"),
	CUSTOM("custom"),
	EMPTY("");
	
	private String linkType;
	
	private LinkType(String linkType){
		this.linkType = linkType;
	}
	
	public static LinkType fromString(String linkType){
		linkType = linkType.trim();
		
		if("sin".equals(linkType)) return SIN;
		if("abssin".equals(linkType)) return ABSSIN;
		if("custom".equals(linkType)) return CUSTOM;
		return EMPTY;
	}
	
	@Override
	public String toString(){
		return linkType;
	}
}

