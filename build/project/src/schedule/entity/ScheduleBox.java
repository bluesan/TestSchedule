package schedule.entity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.VBox;

public class ScheduleBox extends VBox {
	
	/*******************************************************************************************************************
	 * 																												   *
	 *  Fields																								           *
	 * 																												   *
	 *******************************************************************************************************************/
	LocalDate date;
	Label lbl;
	ScrollPane sp;
	VBox schedules;
	
	/*******************************************************************************************************************
	 * 																												   *
	 *  Constractor																								       *
	 * 																												   *
	 *******************************************************************************************************************/
	public ScheduleBox() {
		
		lbl = new Label();
		sp = new ScrollPane();
		schedules = new VBox();
		
		lbl.prefWidthProperty().bind(this.prefWidthProperty());
				
		sp.setContent(schedules);;
		sp.setHbarPolicy(ScrollBarPolicy.NEVER);
		sp.prefHeightProperty().bind(Bindings.subtract(this.prefHeightProperty(), lbl.getPrefHeight()));
		sp.getStyleClass().add("scroll_pane");
		
		schedules.setAlignment(Pos.TOP_CENTER);
		
		this.getChildren().addAll(lbl, sp);		
	}
	
	
	/*******************************************************************************************************************
	 * 																												   *
	 *  Class Method																								   *
	 * 																												   *
	 *******************************************************************************************************************/
	/**
	 * 日付を設定
	 * @param date
	 * @param yearMonth
	 */
	public void setDate(LocalDate date, YearMonth yearMonth) {
		
		this.date = date;
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d");
		DateTimeFormatter formatterF = DateTimeFormatter.ofPattern("M/d");
		
		lbl.setText(date.format(date.getDayOfMonth() == 1 ? formatterF : formatter));
		lbl.getStyleClass().remove(0);
		lbl.getStyleClass().add(date.compareTo(LocalDate.now()) == 0 ?              "calendar_today" : 
                                date.getMonthValue() != yearMonth.getMonthValue() ? "calendar_other" : 
                                date.getDayOfWeek() == DayOfWeek.SUNDAY ?          "calendar_sun" :
                                date.getDayOfWeek() == DayOfWeek.SATURDAY ?        "calendar_sat" :
                                                                                    "calendar_normal");
	}
	
	/**
	 * 日付を取得
	 * @return
	 */
	public LocalDate getDate() {
		return date;
	}
	
	/**
	 * スケジュールをリセット
	 */
	public void removeSchedules() {
		if(schedules.getChildren().size() == 0) return;
		schedules.getChildren().remove(0, schedules.getChildren().size());
	}
	
	/**
	 * スケジュールを追加
	 * @param lbl
	 */
	public void addSchedule(ScheduleLabel lbl) {
		schedules.getChildren().add(lbl);
	}
	
	/**
	 * コンテキストメニューを設定
	 * @param popup
	 */
	public void setContextMenu(ContextMenu popup) {
		lbl.setContextMenu(popup);
		sp.setContextMenu(popup);
	}
	
	/**
	 * スケジュールを取得
	 * @return
	 */
	public List<ScheduleLabel> getSchedules() {
		List<ScheduleLabel> list = new ArrayList<>();
		
		schedules.getChildren().forEach(s -> {
			ScheduleLabel lbl = (ScheduleLabel)s;
			list.add(lbl);
		});
		
		return list;
	}
	
	/**
	 * 選択状態を設定
	 * @param flg
	 */
	public void setSelected(boolean flg) {
		if(flg) {
			lbl.getStyleClass().add("selected_cell");
		} else {
			lbl.getStyleClass().removeAll("selected_cell");
		}
	}
	
	/**
	 * ドラッグ状態を設定
	 * @param flg
	 */
	public void setDraged(boolean flg) {
		if(flg) {
			lbl.getStyleClass().add("draged_cell");
		} else {
			lbl.getStyleClass().removeAll("draged_cell");
		}
	}
	

}
