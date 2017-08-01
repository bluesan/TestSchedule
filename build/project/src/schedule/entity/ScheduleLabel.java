package schedule.entity;

import java.util.List;

import javafx.scene.control.Label;

public class ScheduleLabel extends Label {
	
	/*******************************************************************************************************************
	 * 																												   *
	 *  Fields																								           *
	 * 																												   *
	 *******************************************************************************************************************/
	ScheduleItem schedule;
	List<ScheduleLabel> group;
	
	/*******************************************************************************************************************
	 * 																												   *
	 *  Constractor																							           *
	 * 																												   *
	 *******************************************************************************************************************/
	public ScheduleLabel(ScheduleItem schedule) {
		super(schedule.getTitle());
		
		this.schedule = schedule;
		
		this.setStyle("-fx-background-color:#" + schedule.getBackgroundColor() + ";" +
				      "-fx-text-fill:#" + schedule.getTextColor() + ";");
		
		this.getStyleClass().add("schedule_label");
	}
	
	/*******************************************************************************************************************
	 * 																												   *
	 *  Class Methos																								           *
	 * 																												   *
	 *******************************************************************************************************************/
	/**
	 * グループを追加
	 * @param lbl
	 */
	public void addGroup(ScheduleLabel... lbl) {
		for(int i = 0; i < lbl.length; i++) {
			this.group.add(lbl[i]);
		}
	}
	
	/**
	 * グループをセット
	 * @param group
	 */
	public void setGroup(List<ScheduleLabel> group) {
		this.group = group;
	}
	
	/**
	 * グループを取得
	 * @return
	 */
	public List<ScheduleLabel> getGroup() {
		return this.group;
	}
	
	/**
	 * 選択状態を設定
	 * @param flg
	 */
	public void selected(boolean flg) {
		if(flg) {
			this.setStyle("-fx-background-color:#00a5e7;" +
				           "-fx-text-fill:#fff;");
		} else {
			this.setStyle("-fx-background-color:#" + schedule.getBackgroundColor() + ";" +
				           "-fx-text-fill:#" + schedule.getTextColor() + ";");
		}
	}

}
