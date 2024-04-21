package core.nativetypes;

import core.Core;
import util.RG;
import util.DataTransform;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Link{
	public String name;
	public LinkType linkType;
	public List<String> params = new ArrayList<>();
	public HashMap<Integer, Function<Double, Double>> piecewiseLinearTable = new HashMap<>();
	public int x = -1;
	public int y = -1;
	
	public Link(){}
	
	public Link(String name, LinkType linkType){
		this.name = name;
		this.linkType = linkType;
	}
	
	public Link(String name, LinkType linkType, List<String> params){
		this.name = name;
		this.linkType = linkType;
		this.params = params;
	}
	
	public Link(String name, LinkType linkType, List<String> params, int x, int y){
		this.name = name;
		this.linkType = linkType;
		this.params = params;
		this.x = x;
		this.y = y;
	}
	
	public static int parse(Core core, String linkName, String[] typeAndParams){
		if(typeAndParams.length == 0) return -1;
		
		LinkType linkType = LinkType.fromString(typeAndParams[0]);
		
		if(linkType == LinkType.EMPTY) return -1;
		
		List<String> params;
		
		if(typeAndParams.length == 1)
			params = new ArrayList<String>();
		else
			params = Arrays.asList(Arrays.copyOfRange(typeAndParams, 1, typeAndParams.length));
		
		Link newLink = new Link(linkName, linkType, params);
		
		// If it is custom link, then generate table representing piecewise linear function
		if(linkType == LinkType.CUSTOM){
			try{
				// Generate table representing piecewise linear function
				List<Double> paramsAsDoubles = params.stream().map(x -> Double.parseDouble(x.trim())).collect(Collectors.toList());
				newLink.piecewiseLinearTable = generatePiecewiseLinearTable(paramsAsDoubles);
			}
			catch(Exception e){
				return -1;
			}
		}
		
		core.links.add(newLink);
		
		return 0;
	}
	
	public static double generateValue(Core core, Link link, int instanceIndex){
		try{
			switch(link.linkType){
				case SIN:
					// amplitude, frequency, min x (phase), max x
					double amplitude = Double.parseDouble(link.params.get(0).trim());
					double frequency = Double.parseDouble(link.params.get(1).trim());
					double minX = Double.parseDouble(link.params.get(2).trim());
					double maxX = Double.parseDouble(link.params.get(3).trim());
					
					double argument = frequency * (instanceIndex * core.config.sampleWidth) + minX;
					
					if(argument > maxX)
						argument = maxX;
					
					return amplitude * Math.sin(argument);
				case ABSSIN:
					// amplitude, frequency, min x (phase), max x
					double amplitudeAbs = Double.parseDouble(link.params.get(0).trim());
					double frequencyAbs = Double.parseDouble(link.params.get(1).trim());
					double minXAbs = Double.parseDouble(link.params.get(2).trim());
					double maxXAbs = Double.parseDouble(link.params.get(3).trim());
					
					double argumentAbs = frequencyAbs * (instanceIndex * core.config.sampleWidth) + minXAbs;
					
					if(argumentAbs > maxXAbs)
						argumentAbs = maxXAbs;
					
					return Math.abs(amplitudeAbs * Math.sin(argumentAbs));
				case CUSTOM:
					if(link.piecewiseLinearTable.isEmpty()) return -1;
					
					// Normalize sample value since piecewise table operates on range [0,1]
					double inputX = (instanceIndex * core.config.sampleWidth) / core.config.customLinkMaxX;
					
					// If the normalized input exceeds one, meaning that original input exceeded max x value,
					// then clamp it (we assing 1 to it since it is normalized value)
					if(inputX > 1)
						inputX = 1;
					
					// Find specific part of piecewise linear function that should be applied
					int intervalBorderIndex = -1;
					for(int k = 0; k < link.params.size(); k+=2){
						// If specific interval is found
						if(intervalBorderIndex != -1) break;
						
						if(inputX <= Double.parseDouble(link.params.get(k)))
							intervalBorderIndex = k-2;
					}
					
					// Apply found function to given sample value, scale it by max y value, and return it
					return core.config.customLinkMaxY * link.piecewiseLinearTable.get(intervalBorderIndex < 0 ? 0 : intervalBorderIndex).apply(inputX);
				case EMPTY:
					break;
			}
		}
		catch(Exception e){
			return -1;
		}
		
		return 0;
	}
	
	// Returns linear function defined by 2 points
	public static Function<Double, Double> generateLinearFunction(double x0, double y0, double x1, double y1){
		double dy = y1-y0;
		double dx = x1-x0;
		
		// Handle edge case when two points lie on the same vertical line
		if(dx == 0)
			return x -> y0;
		
		return x -> (dy/dx)*(x-x0) + y0;
	}
	
	// Returns table for piecewise linear function based on the list of coordinates
	// Coords format is x0, y0, x1, y1, ...
	// Assumption is that x0 <= x1 <= x2 <= ...
	/*public HashMap<Integer, Function<Double, Double>> generatePiecewiseLinearTable(Integer... coords){
		// Create table of functions
		HashMap<Integer, Function<Double, Double>> functions = new HashMap<>();
		// Add first function
		functions.put(0, generateLinearFunction(coords[0], coords[1], coords[2], coords[3]));
		
		// If there are more than 2 points then add additional functions to the table
		if(coords.length > 4){
			int index = coords.length - 2;
			
			for(int i = 2; i < index; i+=2)
				functions.put(i, generateLinearFunction(coords[i], coords[i+1], coords[i+2], coords[i+3]));
		}
		
		return functions;
	}*/
	
	public static HashMap<Integer, Function<Double, Double>> generatePiecewiseLinearTable(List<Double> args){
		// Create table of functions
		HashMap<Integer, Function<Double, Double>> table = new HashMap<>();
		
		// Add first function
		table.put(0, generateLinearFunction(args.get(0), args.get(1), args.get(2), args.get(3)));
		
		// If there are more than 2 points then add additional functions to the table
		if(args.size() > 4){
			int index = args.size() - 2;
			
			for(int i = 2; i < index; i+=2)
				table.put(i, generateLinearFunction(args.get(i), args.get(i+1), args.get(i+2), args.get(i+3)));
		}
		
		return table;
	}
	
	/*
	NO NEED FOR THIS FUNCTION, BUT IT DEMONSTRATES USAGE OF PIECEWISE LINEAR FUNCTION
	// When giving values, it is assumed that y values are in range [0-maxValue] and x values are in range [0-255]
	// Important: This histogram is approximately normalized meaning its sum is approximately 1.0
	public float[] getCustomHistogramPiecewiseLinear(int maxValue, Integer... coords){
		float[] hist = new float[256];
		
		HashMap<Integer, Function<Integer, Float>> functions = getPiecewiseLinearTable(coords);
		
		// Find histogram values for every number in range [0-255]
		int intervalBorderIndex = -1;
		for(int i = 0; i < 256; ++i){
			intervalBorderIndex = -1;
			
			for(int k = 0; k < coords.length; k+=2){
				// If specific interval is found
				if(intervalBorderIndex != -1) break;
				
				if(i <= coords[k])
					intervalBorderIndex = k-2;
			}
			
			hist[i] = functions.get(intervalBorderIndex < 0 ? 0 : intervalBorderIndex).apply(i);
		}
		
		// Normalize potential histogram values (we still don't know if area under it will be 1 under normalization)
		// Also calculate discrete sum so that we can adjust histogram so that its sum is approximately 1
		float discreteSum = 0.0f;
		for(int i = 0; i < 256; ++i){
			hist[i] /= maxValue;
			discreteSum += hist[i];
		}
		
		// Discrete area is just the sum of histogram values, and we want to make sure it is approximately 1
		for(int i = 0; i < 256; ++i)
			hist[i] /= discreteSum;
		
		return hist;
	}
	*/
	
	// This is used when we want to assign certain link to certain type
	public Link getDeepCopy(){
		Link l = new Link(name, linkType);
		// Copy parameters
		for(int i = 0; i < params.size(); ++i)
			l.params.add(String.valueOf(params.get(i)));
		// Copy piecewise table
		for(Integer i : piecewiseLinearTable.keySet())
			l.piecewiseLinearTable.put(i, piecewiseLinearTable.get(i));
		
		return l;
	}
	
	@Override
	public boolean equals(Object o){
		// Two links are equal if they have the same name
		
		if(o instanceof Link)
			return name.equals(((Link)o).name);
		
		return false;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(name + " [" + linkType + "] {\n");
		sb.append("  ");
		params.forEach(p -> sb.append(p + ", "));
		sb.deleteCharAt(sb.length() - 1);
		sb.deleteCharAt(sb.length() - 1);
		if(params.size() == 0)
			sb.append("  ");
		else
			sb.append("\n  ");
		sb.append(x + " <---> " + y);
		sb.append("\n}\n");
		return sb.toString();
	}
}