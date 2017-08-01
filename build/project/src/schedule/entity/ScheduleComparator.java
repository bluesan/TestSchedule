package schedule.entity;

import java.util.Comparator;

public class ScheduleComparator implements Comparator<ScheduleItem> {
	
	public int compare(ScheduleItem a, ScheduleItem b) {
		
		//開始日の比較
		if(a.getStartLocalDate().isAfter(b.getStartLocalDate())) {
			return 1;
		} else if (a.getStartLocalDate().isBefore(b.getStartLocalDate())) {
			return -1;
		} 
		
		//終日フラグの比較
		if ("1".equals(a.getAllDay()) && "0".equals(b.getAllDay())) {
			return -1;
		} else if ("0".equals(a.getAllDay()) && "1".equals(b.getAllDay())) {
			return 1;
		}
		
		//開始時刻の比較
		if(a.getStartTime().isAfter(b.getStartTime())) {
			return 1;
		} else if(a.getStartTime().isBefore(b.getStartTime())) {
			return -1;
		}
		
		//終了日の比較
		if(a.getEndLocalDate().isAfter(b.getEndLocalDate())) {
			return -1;
		} else if (a.getEndLocalDate().isBefore(b.getEndLocalDate())) {
			return 1;
		}
		
		//終了時刻の比較
		if(a.getEndTime().isAfter(b.getEndTime())) {
			return 1;
		} else if(a.getEndTime().isBefore(b.getEndTime())) {
			return -1;
		}
			

		return 0;
	}
}
