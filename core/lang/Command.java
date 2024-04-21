package core.lang;

public enum Command{
	CONFIG("config"),
	TYPE("type"),
	CHAIN("chain"),
	LINK("link"),
	BIND("bind"),
	UNBIND("unbind"),
	GENERATOR("generator"),
	EXECUTE("execute"),
	EMPTY("");
	
	private String command;
	
	private Command(String command){
		this.command = command;
	}
	
	public static Command fromString(String command){
		command = command.trim();
		
		if("config".equals(command)) return CONFIG;
		if("type".equals(command)) return TYPE;
		if("chain".equals(command)) return CHAIN;
		if("link".equals(command)) return LINK;
		if("bind".equals(command)) return BIND;
		if("unbind".equals(command)) return UNBIND;
		if("generator".equals(command)) return GENERATOR;
		if("execute".equals(command)) return EXECUTE;
		return EMPTY;
	}
	
	@Override
	public String toString(){
		return command;
	}
}
