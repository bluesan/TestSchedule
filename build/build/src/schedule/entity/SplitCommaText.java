package schedule.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SplitCommaText {
	
    static final String REGEX_CSV_COMMA = ",(?=(([^\"]*\"){2})*[^\"]*$)";  
    static final String REGEX_SURROUND_DOUBLEQUATATION = "^\"|\"$";  
    static final String REGEX_DOUBLEQUOATATION = "\"\"";  
    
    public static String[] splitCommaText(String str) {
    	String[] array = null;
    	
    	try {
    		Pattern cPattern = Pattern.compile(REGEX_CSV_COMMA);
    		String[] cols = cPattern.split(str, -1);
    		
    		array = new String[cols.length];
    		for(int i = 0, len = cols.length; i < len; i++) {
    			String col = cols[i].trim();
    			
    			Pattern sdpPattern = Pattern.compile(REGEX_SURROUND_DOUBLEQUATATION);
    			Matcher matcher = sdpPattern.matcher(col);
    			col = matcher.replaceAll("");
    			
    			Pattern dqPattern = Pattern.compile(REGEX_DOUBLEQUOATATION);
    			matcher = dqPattern.matcher(col);
    			col = matcher.replaceAll("\"");
    			
    			array[i] = col;
    		}
    		
    		return array;
    		
    	} catch (Exception e) {
    		return null;
    	}
    }

}
