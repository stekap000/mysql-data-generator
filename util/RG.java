package util;

import java.util.Random;
import java.util.Date;
import java.text.SimpleDateFormat;

public class RG{
	private static Random rand = new Random();
	
	public int getInt(int from, int to){
		return from + rand.nextInt() % (to - from);
	}
	
	public String getString(String root, int from, int to){
		return root + getInt(from, to);
	}
	
	public boolean getBoolean(){
		return rand.nextBoolean();
	}
	
	public float getFloat(float from, float to){
		return from + rand.nextFloat() * (to - from);
	}
	
	public double getDouble(double from, double to){
		return from + rand.nextDouble() * (to - from);
	}
	
	public Date getDate(int yFrom, int yTo, int MFrom, int MTo, int dFrom, int dTo){
		// -1900 in year field because that parameter takes offset from 1900
		return new Date(getInt(yFrom, yTo) - 1900, getInt(MFrom, MTo), getInt(dFrom, dTo));
	}
	
	public Date getDate(int yFrom, int yTo, int MFrom, int MTo, int dFrom, int dTo,
						int hFrom, int hTo, int mFrom, int mTo, int sFrom, int sTo){
		// -1900 in year field because that parameter takes offset from 1900
		return new Date(getInt(yFrom, yTo) - 1900, getInt(MFrom, MTo), getInt(dFrom, dTo),
						getInt(hFrom, hTo), getInt(mFrom, mTo), getInt(sFrom, sTo));
	}
	
	public String getRandom(String subtype){
		// One way to define all these boundary values is to make simple file that will contain that info
		// We currently assume that it is not needed and we use nonsense values
		// Another way is to use already defined "config" command and to introduce new ConfigParameters.
		// This way, we can define all these parameters in the same file
		if("int".equals(subtype)) return String.valueOf(getInt(0, 10000));
		if("float".equals(subtype)) return String.valueOf(getFloat(0, 10000));
		if("double".equals(subtype)) return String.valueOf(getDouble(0, 10000));
		if("string".equals(subtype)) return "'" + getString("string", 0, 10000) + "'";
		if("boolean".equals(subtype)) return String.valueOf(getBoolean());
		if("date".equals(subtype)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return "'" + sdf.format(getDate(2020, 2030, 0, 12, 0, 7)) + "'";
		}
		if("datetime".equals(subtype)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return "'" + sdf.format(getDate(2020, 2030, 0, 12, 0, 7, 0, 24, 0, 60, 0, 60)) + "'";
		};
		return "";
	}
}