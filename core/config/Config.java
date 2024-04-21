package core.config;

import core.Core;

public class Config{
	public String databaseName = "";
	public int autoIncrementBase = -1;
	public double sampleWidth = 0.1;
	public int startDateYear = 2020;
	public int endDateYear = 2030;
	public int startDateMonth = 1;
	public int endDateMonth = 12;
	public int startDateDay = 1;
	public int endDateDay = 30;
	public double customLinkMaxX = 10;
	public double customLinkMaxY = 10;
	
	public static int parse(Core core, String[] parts){
		ConfigParameter configParameter = ConfigParameter.fromString(parts[0].trim());
		String value = parts[1].trim();
		
		try{
			switch(configParameter){
				case DATABASE_NAME:
					core.config.databaseName = value;
					break;
				case AUTO_INCREMENT_BASE:
					core.config.autoIncrementBase = Integer.parseInt(value);
					break;
				case SAMPLE_WIDTH:
					core.config.sampleWidth = Float.parseFloat(value);
					break;
				case START_DATE_YEAR:
					core.config.startDateYear = Integer.parseInt(value);
					break;
				case END_DATE_YEAR:
					core.config.endDateYear = Integer.parseInt(value);
					break;
				case START_DATE_MONTH:
					core.config.startDateMonth = Integer.parseInt(value);
					break;
				case END_DATE_MONTH:
					core.config.endDateMonth = Integer.parseInt(value);
					break;
				case START_DATE_DAY:
					core.config.startDateDay = Integer.parseInt(value);
					break;
				case END_DATE_DAY:
					core.config.endDateDay = Integer.parseInt(value);
					break;
				case CUSTOM_LINK_MAX_X:
					core.config.customLinkMaxX = Double.parseDouble(value);
					break;
				case CUSTOM_LINK_MAX_Y:
					core.config.customLinkMaxY = Double.parseDouble(value);
					break;
				case EMPTY:
					break;
			}
		}
		catch(Exception e){
			return -1;
		}
		
		return 0;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("database_name: " + databaseName + "\n");
		sb.append("auto_increment_base: " + autoIncrementBase + "\n");
		sb.append("sample_width: " + sampleWidth + "\n");
		sb.append("start_date_year: " + startDateYear + "\n");
		sb.append("end_date_year: " + endDateYear + "\n");
		sb.append("start_date_month: " + startDateMonth + "\n");
		sb.append("end_date_month: " + endDateMonth + "\n");
		sb.append("start_date_day: " + startDateDay + "\n");
		sb.append("end_date_day: " + endDateDay + "\n");
		sb.append("custom_link_max_x: " + customLinkMaxX + "\n");
		sb.append("custom_link_max_y: " + customLinkMaxY + "\n");
		
		return sb.toString();
	}
}
