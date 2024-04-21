package core.config;

public enum ConfigParameter{
	DATABASE_NAME("database_name"),
	AUTO_INCREMENT_BASE("auto_increment_base"),
	SAMPLE_WIDTH("sample_width"),
	START_DATE_YEAR("start_date_year"),
	END_DATE_YEAR("end_date_year"),
	START_DATE_MONTH("start_date_month"),
	END_DATE_MONTH("end_date_month"),
	START_DATE_DAY("start_date_day"),
	END_DATE_DAY("end_date_day"),
	CUSTOM_LINK_MAX_X("custom_link_max_x"),
	CUSTOM_LINK_MAX_Y("custom_link_max_y"),
	EMPTY("");
	
	private String parameter;
	
	private ConfigParameter(String parameter){
		this.parameter = parameter;
	}
	
	public static ConfigParameter fromString(String parameter){
		parameter = parameter.trim();
		
		if("database_name".equals(parameter)) return DATABASE_NAME;
		if("auto_increment_base".equals(parameter)) return AUTO_INCREMENT_BASE;
		if("sample_width".equals(parameter)) return SAMPLE_WIDTH;
		if("start_date_year".equals(parameter)) return START_DATE_YEAR;
		if("end_date_year".equals(parameter)) return END_DATE_YEAR;
		if("start_date_month".equals(parameter)) return START_DATE_MONTH;
		if("end_date_month".equals(parameter)) return END_DATE_MONTH;
		if("start_date_day".equals(parameter)) return START_DATE_DAY;
		if("end_date_day".equals(parameter)) return END_DATE_DAY;
		if("custom_link_max_x".equals(parameter)) return CUSTOM_LINK_MAX_X;
		if("custom_link_max_y".equals(parameter)) return CUSTOM_LINK_MAX_Y;
		return EMPTY;
	}
	
	@Override
	public String toString(){
		return parameter;
	}
}