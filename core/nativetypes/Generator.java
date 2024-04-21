package core.nativetypes;

import core.Core;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class Generator{
	public String name;
	public Type type;
	public int count;
	
	public Generator(){}
	
	public Generator(String name, Type type, int count){
		this.name = name;
		this.type = type;
		this.count = count;
	}
	
	public int execute(Core core){
		for(int i = 0; i < count; ++i){
			type.generateInstance(core, i);
		}
		
		return 0;
	}
	
	public static int parse(Core core, String generatorName, String[] typeAndNum){
		Optional<Type> optType = core.getTypeByName(typeAndNum[0]);
		
		if(!optType.isPresent()) return -1;
		
		int n = 0;
		try{
			n = Integer.parseInt(typeAndNum[1].trim());
		}
		catch(Exception e){
			return -1;
		}
		
		Generator generator = new Generator(generatorName, optType.get(), n);
		
		core.generators.add(generator);
		
		return 0;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(name + " {\n");
		sb.append("  " + type.name + ", " + count + "\n");
		sb.append("}\n");
		
		return sb.toString();
	}
}