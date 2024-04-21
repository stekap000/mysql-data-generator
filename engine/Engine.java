package engine;

import core.Core;

public class Engine{
	public static void main(String[] args){
		Core core = new Core();
		
		// It will look for this standard script is nothing is supplied
		String script = "./script.dgs";

		if(args.length == 1 && args[0].endsWith(".dgs"))
			script = args[0];
			
		core.init(script);
		
		// We can print engine state like this
		// System.out.println(core);
	}
}
