package core.nativetypes;

import core.Core;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class Chain{
	public Type chained;
	public List<Type> chainers = new ArrayList<>();
	
	public Chain(){
		
	}
	
	public Chain(Type chained, List<Type> chainers){
		this.chained = chained;
		this.chainers = chainers;
	}
	
	public static int parse(Core core, String chainedName, String[] chainersNames){
		Optional<Type> optChained = core.types.stream().filter(t -> t.name.equals(chainedName.trim())).findFirst();
		
		if(!optChained.isPresent()) return -1;
		
		List<Type> chainersList = core.types.stream().filter(t -> {
			for(String chainerName : chainersNames)
				if(t.name.equals(chainerName.trim()))
					return true;
				
			return false;
		}).collect(Collectors.toList());
		
		Chain chain = new Chain(optChained.get(), chainersList);
		
		optChained.get().chain = chain;
		
		core.chains.add(chain);
		
		return 0;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(chained.name + " -O-O- {\n");
		chainers.forEach(c -> sb.append("  " + c.name + "\n"));
		sb.append("}\n");
		
		return sb.toString();
	}
}