package core;

import core.nativetypes.Type;
import core.nativetypes.Chain;
import core.nativetypes.Link;
import core.nativetypes.Generator;
import core.lang.Command;
import core.config.Config;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.Optional;

public class Core{
	public Config config = new Config();
	public List<Type> types = new ArrayList<>();
	public List<Link> links = new ArrayList<>();
	public List<Chain> chains = new ArrayList<>();
	public List<Generator> generators = new ArrayList<>();
	
	// We could make enum for representation of error codes
	int errorCode = 0;
	
	public void init(String filename){
		try{
			List<String> lines = Files.readAllLines(Paths.get(filename));
		
			for(int i = 0; i < lines.size(); ++i){
				parse(lines.get(i));
			}
		}
		catch(IOException e){
			System.out.println("ERROR::SCRIPT_FILE::LOAD");
		}
		catch(Exception e){
			System.out.println("ERROR::PARSING");
		}
	}
	
	public int parse(String line){
		String[] parts = line.split(Pattern.quote("<="));
		
		Command command = Command.fromString(parts[0]);
		
		switch(command){
			case CONFIG:
				errorCode = Config.parse(this, parts[1].split(","));
				break;
			case TYPE:
				errorCode = Type.parse(this, parts[1].trim(), parts[2].split(","));
				break;
			case CHAIN:
				errorCode = Chain.parse(this, parts[1].trim(), parts[2].split(","));
				break;
			case LINK:
				errorCode = Link.parse(this, parts[1].trim(), parts[2].split(","));
				break;
			case BIND:
				errorCode = Type.bindLink(this, parts[1].split(","), parts[2].split(","));
				break;
			case UNBIND:
				errorCode = Type.unbindLink(this, parts[1].split(","));
				break;
			case GENERATOR:
				errorCode = Generator.parse(this, parts[1].trim(), parts[2].split(","));
				break;
			case EXECUTE:
				Core.execute(this, parts[1].trim());
				break;
			case EMPTY:
				break;
		}
		
		return errorCode;
	}
	
	public static void execute(Core core, String genName){
		if("all".equals(genName)){
			core.generators.forEach(g -> g.execute(core));
		}
		else{
			Optional<Generator> genOpt = core.generators.stream().filter(g -> g.name.equals(genName)).findFirst();
			if(genOpt.isPresent())
				genOpt.get().execute(core);
		}
	}
	
	public Optional<Type> getTypeByName(String typeName){
		return types.stream().filter(t -> t.name.equals(typeName.trim())).findFirst();
	}
	
	public Optional<Link> getLinkByName(String linkName){
		return links.stream().filter(l -> l.name.equals(linkName.trim())).findFirst();
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("+--------------+\n");
		sb.append("|    CONFIG    |\n");
		sb.append("+--------------+\n");
		sb.append(config.toString());
		
		sb.append("+-------------+\n");
		sb.append("|    TYPES    |\n");
		sb.append("+-------------+\n");
		types.forEach(t -> sb.append(t.toString()));
		
		sb.append("+-------------+\n");
		sb.append("|    LINKS    |\n");
		sb.append("+-------------+\n");
		links.forEach(l -> sb.append(l.toString()));
		
		sb.append("+-------------+\n");
		sb.append("|    BINDS    |\n");
		sb.append("+-------------+\n");
		types.forEach(t -> { if(t.links.size() > 0) sb.append(t.getBindedLinksPrintString()); });
		
		sb.append("+------------+\n");
		sb.append("|   CHAINS   |\n");
		sb.append("+------------+\n");
		chains.forEach(c -> sb.append(c.toString()));
		
		sb.append("+----------------+\n");
		sb.append("|   GENERATORS   |\n");
		sb.append("+----------------+\n");
		generators.forEach(g -> sb.append(g.toString()));
		
		return sb.toString();
	}
}