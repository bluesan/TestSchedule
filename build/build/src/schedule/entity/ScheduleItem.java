package schedule.entity;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ScheduleItem {
	
	/*******************************************************************************************************************
	 * 																												   *
	 *  Fields  																								       *
	 * 																												   *
	 *******************************************************************************************************************/
	private String title;
	private String startDate;
	private String startHH;
	private String startMM;
	private String endDate;
	private String endHH;
	private String endMM;
	private String allDay;
	private String repeat;
	private String locate;
	private String kind;
	private String detail;
	private String backgroundColor;
	private String textColor;
	
	/*******************************************************************************************************************
	 * 																												   *
	 *  Constractors  																							       *
	 * 																												   *
	 *******************************************************************************************************************/
	/**
	 * デフォルトコンストラクタ
	 */
	public ScheduleItem() {
		this.title = "";
		this.startDate = "";
		this.startHH = "0";
		this.startMM = "0";
		this.endDate = "";
		this.endHH = "0";
		this.endMM = "0";
		this.allDay = "0";
		this.repeat = "0";
		this.locate = "";
		this.kind = "";
		this.detail = "";
		this.backgroundColor = "#ffffff";
		this.textColor = "#000000";
	}
	
	/**
	 * csvデータ設定用コンストラクタ
	 * @param items
	 */
	public ScheduleItem(String[] items) {
		this.title = items[0];
		this.startDate = items[1];
		this.startHH = items[2];
		this.startMM = items[3];
		this.endDate = items[4];
		this.endHH = items[5];
		this.endMM = items[6];
		this.allDay = items[7];
		this.repeat = items[8];
		this.backgroundColor = items[9];
		this.textColor = items[10];
		this.locate = items[11];
		this.kind = items[12];
		this.detail = items[13].replaceAll("#R13#L10", "\n");
	}
	
	/*******************************************************************************************************************
	 * 																												   *
	 *  Setter																								           *
	 * 																												   *
	 *******************************************************************************************************************/
	/**
	 * @param title セットする title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @param startDate セットする startDate
	 */
	public void setStartDate(String date) {
		this.startDate = date;
	}
	/**
	 * @param startHH セットする startHH
	 */
	public void setStartHH(String startHH) {
		this.startHH = startHH;
	}
	/**
	 * @param startMM セットする startMM
	 */
	public void setStartMM(String startMM) {
		this.startMM = startMM;
	}
	/**
	 * @param endDate セットする endDate
	 */
	public void setEndDate(String date) {
		this.endDate = date;
	}
	/**
	 * @param endHH セットする endHH
	 */
	public void setEndHH(String endHH) {
		this.endHH = endHH;
	}
	/**
	 * @param endMM セットする endMM
	 */
	public void setEndMM(String endMM) {
		this.endMM = endMM;
	}
	/**
	 * @param allDay セットする allDay
	 */
	public void setAllDay(String allDay) {
		this.allDay = allDay;
	}
	/**
	 * @param repeat セットする repeat
	 */
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	/**
	 * @param backgoundColor セットする backgoundColor
	 */
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	/**
	 * @param textColor セットする textColor
	 */
	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}
	/**
	 * @param locate セットする locate
	 */
	public void setLocate(String locate) {
		this.locate = locate;
	}
	/**
	 * @param kind セットする kind
	 */
	public void setKind(String kind) {
		this.kind = kind;
	}
	/**
	 * @param detail セットする detail
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	/*******************************************************************************************************************
	 * 																												   *
	 *  Getter																								           *
	 * 																												   *
	 *******************************************************************************************************************/
	/**
	 * @return title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @return startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @return
	 */
	public LocalDate getStartLocalDate() {
		return LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
	}
	/**
	 * @return startHH
	 */
	public String getStartHH() {
		return startHH;
	}
	/**
	 * @return startMM
	 */
	public String getStartMM() {
		return startMM;
	}
	/**
	/**
	 * @return
	 */
	public LocalTime getStartTime() {
		return LocalTime.of(Integer.parseInt(startHH),Integer.parseInt(startMM));
	}
	/**
	 * @return
	 */
	public String getStrStartTime() {
		return getStartTime().format(DateTimeFormatter.ofPattern("H:mm"));
	}
	/**
	 * @return
	 */
	public String getEndDate() {
		return this.endDate;
	}
	/**
	 * @return
	 */
	public LocalDate getEndLocalDate() {
		return LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
	}
	/**
	 * @return endHH
	 */
	public String getEndHH() {
		return endHH;
	}
	/**
	 * @return endMM
	 */
	public String getEndMM() {
		return endMM;
	}
	/**
	 * @return
	 */
	public LocalTime getEndTime() {
		return LocalTime.of(Integer.parseInt(endHH),Integer.parseInt(endMM));
	}
	/**
	 * @return
	 */
	public String getStrEndTime() {
		return getEndTime().format(DateTimeFormatter.ofPattern("H:mm"));
	}
	/**
	 * @return allDay
	 */
	public String getAllDay() {
		return allDay;
	}
	/**
	 * @return repeat
	 */
	public String getRepeat() {
		return repeat;
	}
	/**
	 * @return backgoundColor
	 */
	public String getBackgroundColor() {
		return backgroundColor;
	}
	/**
	 * @return textColor
	 */
	public String getTextColor() {
		return textColor;
	}
	/**
	 * @return locate
	 */
	public String getLocate() {
		return locate;
	}
	/**
	 * @return kind
	 */
	public String getKind() {
		return kind;
	}
	/**
	 * @return detail
	 */
	public String getDetail() {
		return detail;
	}

	/*******************************************************************************************************************
	 * 																												   *
	 *  Class Methods																						           *
	 * 																												   *
	 *******************************************************************************************************************/
	/**
	 * 日付差を取得する
	 */
	public long getSubDate() {
		return Duration.between(getStartLocalDate().atTime(0, 0, 0), getEndLocalDate().atTime(0, 0, 0)).toDays();
	}
		
	/**
	 * 指定した日付でクローンを生成
	 * @param date
	 * @return
	 */
	public ScheduleItem clone(LocalDate date) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		ScheduleItem s = new ScheduleItem();
		
		s.title = this.title;
		s.startDate = date.format(formatter);
		s.startHH = this.startHH;
		s.startMM = this.startMM;
		s.endDate = date.plusDays(this.getSubDate()).format(formatter);
		s.endHH = this.endHH;
		s.endMM = this.endMM;
		s.allDay = this.allDay;
		s.repeat = this.repeat;
		s.locate = this.locate;
		s.kind = this.kind;
		s.detail = this.detail;
		s.backgroundColor = this.backgroundColor;
		s.textColor = this.textColor;
		
		return s;
	}
	
	/**
	 * CVSデータを出力
	 * @return
	 */
	public String toCVS() {
		StringBuffer str = new StringBuffer();
		str.append("\"").append(this.title.replace("\"", "\"\"")).append("\",")
		   .append("\"").append(this.startDate).append("\",")
		   .append("\"").append(this.startHH).append("\",")
		   .append("\"").append(this.startMM).append("\",")
		   .append("\"").append(this.endDate).append("\",")
		   .append("\"").append(this.endHH).append("\",")
		   .append("\"").append(this.endMM).append("\",")
		   .append("\"").append(this.allDay).append("\",")
		   .append("\"").append(this.repeat).append("\",")
		   .append("\"").append(this.backgroundColor).append("\",")
		   .append("\"").append(this.textColor).append("\",")
		   .append("\"").append(this.locate.replace("\"", "\"\"")).append("\",")
		   .append("\"").append(this.kind).append("\",")
		   .append("\"").append(this.detail.replace("\"", "\"\"").replace("\n", "#R13#L10")).append("\"");
		return str.toString();
	}
	
	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		if(startDate.equals(endDate)) {
			str.append(startDate);
			if(!"1".equals(allDay)) {
				str.append(" ").append(this.getStrStartTime()).append(" ～ ").append(this.getStrEndTime());
			}
		} else {
			str.append(startDate).append("1".equals(allDay) ? "" : " " + this.getStrStartTime());
			str.append(" ～ ").append(endDate).append("1".equals(allDay) ? "" : " " + this.getStrEndTime());
		}
		str.append("\n");
		if(!"".equals(locate)) {
			str.append("場所：").append(locate).append("\n");
		}
		str.append("カレンダー：").append(kind).append("\n\n");
		str.append(detail);
		
		return str.toString();
	}
}
