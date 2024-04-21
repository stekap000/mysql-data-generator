package util;

import core.Core;

import java.util.Date;
import java.text.SimpleDateFormat;

public class DataTransform{
	public Core core;
	
	public DataTransform(){
		core = new Core();
	}
	
	public DataTransform(Core core){
		this.core = core;
	}
	
	public String doubleToSubtypeString(String subtype, double value){
		try{
			// CURRENTLY, WE DON'T ACTUALLY MAKE DISTINCTION BETWEEN DATE AND DATETIME (THEY ARE THE SAME THING)
			if("date".equals(subtype) || "datetime".equals(subtype)){
				// It is assumed that the value represents number of days here (when rounded)
				
				int y1 = core.config.startDateYear;
				int y2 = core.config.endDateYear;
				
				int m1 = core.config.startDateMonth;
				int m2 = core.config.endDateMonth;
				
				int d1 = core.config.startDateDay;
				int d2 = core.config.endDateDay;
				
				int valueDays = (int)Math.floor(value);
			
				int deltaYears = (int)(valueDays / 365.0);
				valueDays -= deltaYears * 365;
				int deltaMonths = (int)(valueDays / 30);
				valueDays -= deltaMonths * 30;
				int deltaDays = valueDays;
			
				Date d = new Date();
				d.setYear(y1 + deltaYears -1900);	// Function accepts offset from year 1900
				d.setMonth(m1 - 1 + deltaMonths);
				d.setDate(d1 + deltaDays);
				//Date d = new Date(y1 + deltaYears, deltaMonths, deltaDays);
				
				// If the day in the date comes after endDateDay, then clamp it
				if(d.getDate() > d2){
					d.setDate(d2);
				}
				
				// If the month in the date comes after endDateMonth, then clamp it
				if(d.getMonth() > m2 || (d.getMonth() == m2 && d.getDate() > 0)){
					d.setMonth(m2);
					d.setDate(0);
				}
				
				// If the date comes after endDateYear, then clamp it
				if(d.getYear() > y2 || (d.getYear() == y2 && (d.getMonth() > 0 || d.getDate() > 0))){
					//d = new Date(y2, 0, 0);
					d.setYear(y2 - 1900);
					d.setMonth(0);
					d.setDate(0); 
				}
			
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				return "'" + sdf.format(d) + "'";
			}
			else if("int".equals(subtype)){
				return String.valueOf((int)Math.floor(value));
			}
			else if("string".equals(subtype)){
				return "'" + String.valueOf("string" + value) + "'";
			}
			else if("boolean".equals(subtype)){
				// Returns string 'true' when value is >= 0
				return String.valueOf((value) >= 0 ? true : false);
			}
			else if("float".equals(subtype)){
				return String.valueOf((float)value);
			}
			else if("double".equals(subtype)){
				return String.valueOf(value);
			}
		}
		catch(Exception e){
			return "";
		}
		
		return "";
	}
}