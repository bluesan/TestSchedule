package schedule;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import schedule.entity.ScheduleItem;
import schedule.entity.ScheduleKind;

public class ScheduleEditor extends Stage{
	
	/*******************************************************************************************************************
	 * 																												   *
	 *  Compornents  																								   *
	 * 																												   *
	 *******************************************************************************************************************/
	private TextField title;
	private DatePicker startDate;
	private ComboBox<String> startHH;
	private ComboBox<String> startMM;
	private DatePicker endDate;
	private ComboBox<String> endHH;
	private ComboBox<String> endMM;
	private CheckBox allDay;
	private CheckBox repeat;
	private ColorPicker backgroundColor;
	private TextField locate;
	private ComboBox<String> kind;
	private TextArea detail;
	private Button saveButton;
	private Button backButton;
	private Button deleteButton;
	
	/*******************************************************************************************************************
	 * 																												   *
	 *  Propertys  																								       *
	 * 																												   *
	 *******************************************************************************************************************/
	private CLOSE_MODE mode;
	private ScheduleItem schedule;
	private List<ScheduleKind> kinds;
	public enum CLOSE_MODE {OK, CANCEL, DELETE};
	
	
	/*******************************************************************************************************************
	 * 																												   *
	 *  Constractor   																							       *
	 * 																												   *
	 *******************************************************************************************************************/
	/**
	 * @param wnd
	 * @param date
	 */
	public ScheduleEditor(Window wnd, List<ScheduleKind> kinds) {
		
		setTitle("B-san / Edit Schedule");
		initStyle(StageStyle.UTILITY);
		initOwner(wnd);
		initModality(Modality.APPLICATION_MODAL);
		mode = CLOSE_MODE.CANCEL;
		this.kinds = kinds;
		
		//--タイトル-------------------------------------------------------
		title = new TextField();
		title.getStyleClass().add("title_text");
		title.setPromptText("タイトルを入力");
		HBox titleBox = new HBox(title);
		titleBox.setAlignment(Pos.CENTER);
		
		//--開始日時-------------------------------------------------------
		startDate = createDatePicker();
		startHH = createHHComboBox();
		startMM = createMMComboBox();
		HBox start = new HBox(new Label("開始日時 "), startDate, startHH, new Label("時"), startMM, new Label("分"));
		start.getStyleClass().add("date_box");
		
		//--終了日時-------------------------------------------------------
		endDate = createDatePicker();
		endHH = createHHComboBox();
		endMM = createMMComboBox();
		HBox end = new HBox(new Label("終了日時 "), endDate, endHH, new Label("時"), endMM, new Label("分"));
		end.getStyleClass().add("date_box");
		
		//--終日---------------------------------------------------
		allDay = new CheckBox("終日");
		allDay.setOnAction(event -> {
			setAllDayDisabled();
		});
		//--繰り返し-----------------------------------------------
		repeat = new CheckBox("繰り返し");
		
		//--チェックボックスまとめ-------------------------------------------
		HBox checker = new HBox(allDay, repeat);
		checker.getStyleClass().add("checker");
		VBox timeBox = new VBox(start, end, checker);
		timeBox.getStyleClass().add("time_box");
		
		//--色-----------------------------------------------------------
        backgroundColor = new ColorPicker();
        HBox colors = new HBox(new Label("　背景色 "), backgroundColor);
        colors.getStyleClass().add("colors_box");
        
		//--場所-----------------------------------------------------------
		locate = new TextField();
		locate.getStyleClass().add("locate_text");
		locate.setPromptText("場所を入力");
		Label locateLabel = new Label("　　場所 ");
		HBox locateBox = new HBox(locateLabel, locate);
		locateBox.getStyleClass().add("locate_box");
		
		//--カレンダー-----------------------------------------------------
		kind = new ComboBox<>();
		kinds.forEach(k -> kind.getItems().add(k.getName()));
		kind.getSelectionModel().selectFirst();
		kind.getStyleClass().add("combobox");
		Label calendarLabel = new Label("カレンダー ");
		HBox calendarBox = new HBox(calendarLabel, kind);
		calendarBox.getStyleClass().add("calendar_box");
		
		//--詳細-----------------------------------------------------------
		detail = new TextArea();
		detail.setPrefSize(316, 214);
		Label detailLabel = new Label("　　詳細 ");
		HBox detailBox = new HBox(detailLabel, detail);
		detailBox.getStyleClass().add("detail_box");
		
		//--保存ボタン-----------------------------------------------------
		saveButton = new Button("保存");
		saveButton.getStyleClass().add("task_button");
		saveButton.setOnAction(event -> {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("B-SAN");
			alert.setHeaderText("エラー");
			
			//タイトル未入力チェック
			if("".equals(title.getText())) {
				alert.setContentText("タイトルを入力してください。");
				alert.showAndWait();
				title.requestFocus();
				return;
			}
			//日付チェック
			if(!checkDateTime()) {
				alert.setContentText("開始日時と終了日時を正しく選択してください。");
				alert.showAndWait();
				startDate.requestFocus();
				return;
			}
			
			setSchedule();
			mode = CLOSE_MODE.OK;
			this.close();
		});
		
		//--戻るボタン-----------------------------------------------------
		backButton = new Button("戻る");
		backButton.getStyleClass().add("task_button");
		backButton.setOnAction(event -> {
			mode = CLOSE_MODE.CANCEL;
			this.close();
		});
		
		//--削除ボタン-----------------------------------------------------
		deleteButton = new Button("削除");
		deleteButton.getStyleClass().add("task_button");
		deleteButton.setOnAction(event -> {
			mode = CLOSE_MODE.DELETE;
			this.close();
		});
		
		//--ボタンヘッダー-------------------------------------------------
		HBox buttons = new HBox(backButton, saveButton, deleteButton);
		buttons.getStyleClass().add("header");
		
		//--ルート---------------------------------------------------------
		VBox root = new VBox(buttons, titleBox, timeBox, colors, locateBox, calendarBox, detailBox);
		root.setSpacing(10.0);
		root.getStylesheets().add(ScheduleEditor.class.getResource("taskEditor.css").toExternalForm());
		root.getStylesheets().add(ScheduleEditor.class.getResource("datepicker.css").toExternalForm());
		
		this.setResizable(false);
		this.setWidth(400);
		this.setHeight(550);
		setScene(new Scene(root));
	}
	
	/*******************************************************************************************************************
	 * 																												   *
	 *  Creators   																								       *
	 * 																												   *
	 *******************************************************************************************************************/
	/**
	 * DatePickerを生成
	 * @return
	 */
	private DatePicker createDatePicker() {
		DatePicker date = new DatePicker();
		date.setPrefWidth(130);
		date.setEditable(false);
		return date;
	}
	
	/**
	 * 時間コンボボックスを生成
	 * @return
	 */
	private ComboBox<String> createHHComboBox() {
		ComboBox<String> hh = new ComboBox<>();
		hh.getItems().addAll("0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
							 "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
						     "20", "21", "22", "23");
		hh.getStyleClass().add("combobox");
		
		return hh;
	}
	
	/**
	 * 分コンボボックスを生成
	 * @return
	 */
	private ComboBox<String> createMMComboBox() {
		ComboBox<String> mm = new ComboBox<>();
		mm.getItems().addAll("0", "15", "30", "45");
		mm.getStyleClass().add("combobox");
		
		return mm;
	}
	
	/*******************************************************************************************************************
	 * 																												   *
	 *  Control Method																							       *
	 * 																												   *
	 *******************************************************************************************************************/
	/**
	 * スケジュールを設定
	 */
	private void setSchedule() {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	
		schedule = new ScheduleItem();
		schedule.setTitle(title.getText());
		schedule.setStartDate(startDate.getValue().format(formatter));
		schedule.setStartHH(startHH.getValue());
		schedule.setStartMM(startMM.getValue());
		schedule.setEndDate(endDate.getValue().format(formatter));
		schedule.setEndHH(endHH.getValue());
		schedule.setEndMM(endMM.getValue());
		schedule.setAllDay(allDay.isSelected() ? "1" : "0");
		schedule.setRepeat(repeat.isSelected() ? "1" : "0");
		schedule.setLocate(locate.getText());
		schedule.setKind(kinds.get(kind.getSelectionModel().getSelectedIndex()).getCode());
		schedule.setDetail(detail.getText());
		schedule.setBackgroundColor(backgroundColor.getValue().toString().substring(2, 8));
		Color color = backgroundColor.getValue();
		String textColor = (color.getBlue() + color.getRed() + color.getGreen())/3.0 > 0.5 ? "000000" : "ffffff";
		schedule.setTextColor(textColor);
	}
	
	/**
	 * 日程フォームの操作制御
	 */
	private void setAllDayDisabled() {
		
		if(allDay.isSelected()) {
			startHH.getSelectionModel().select(0);
			startMM.getSelectionModel().select(0);
			endHH.getSelectionModel().select(0);
			endMM.getSelectionModel().select(0);
		}
		
		startHH.setDisable(allDay.isSelected());
		startMM.setDisable(allDay.isSelected());
		endHH.setDisable(allDay.isSelected());
		endMM.setDisable(allDay.isSelected());	
	}
	
	/**
	 * 日付のチェック
	 * @return
	 */
	public boolean checkDateTime() {
		
		if(startDate.getValue().isBefore(endDate.getValue())) {
			return true;
		}
		if(startDate.getValue().isAfter(endDate.getValue())) {
			return false;
		}
		if(startHH.getSelectionModel().getSelectedIndex() < endHH.getSelectionModel().getSelectedIndex()) {
			return true;
		}
		if(startHH.getSelectionModel().getSelectedIndex() > endHH.getSelectionModel().getSelectedIndex()) {
			return false;
		}
		if(startMM.getSelectionModel().getSelectedIndex() <= endMM.getSelectionModel().getSelectedIndex()) {
			return true;
		}
		
		return false;
	}
	

	/*******************************************************************************************************************
	 * 																												   *
	 *  Show Method																							       *
	 * 																												   *
	 *******************************************************************************************************************/
	/**
	 * 編集用フォーム表示
	 * @param item
	 */
	public void showEditor(ScheduleItem item) {
		
		title.setText(item.getTitle());
		startDate.setValue(LocalDate.parse(item.getStartDate(), DateTimeFormatter.ofPattern("yyyy/MM/dd")));
		startHH.setValue(item.getStartHH());
		startMM.setValue(item.getStartMM());
		endDate.setValue(LocalDate.parse(item.getEndDate(), DateTimeFormatter.ofPattern("yyyy/MM/dd")));
		endHH.setValue(item.getEndHH());
		endMM.setValue(item.getEndMM());
		allDay.setSelected("1".equals(item.getAllDay()));
		repeat.setSelected("1".equals(item.getRepeat()));
		backgroundColor.setValue(Color.web("#" + item.getBackgroundColor()));
		locate.setText(item.getLocate());
		detail.setText(item.getDetail());
		deleteButton.setVisible(true);
		
		setAllDayDisabled();
		
		mode = CLOSE_MODE.CANCEL;
		
		title.requestFocus();
		
		this.showAndWait();
	}
	
	/**
	 * 新規作成用フォーム作成
	 * @param date
	 */
	public void showCreator(LocalDate date) {
		title.setText("");
		startDate.setValue(date);
		startHH.setValue("0");
		startMM.setValue("0");
		endDate.setValue(date);
		endHH.setValue("0");
		endMM.setValue("0");
		allDay.setSelected(false);
		repeat.setSelected(false);
		backgroundColor.setValue(Color.web("#ffffff"));
		locate.setText("");
		detail.setText("");
		deleteButton.setVisible(false);
		
		setAllDayDisabled();
		
		mode = CLOSE_MODE.CANCEL;
		
		title.requestFocus();
		
		this.showAndWait();
	}
	
	/*******************************************************************************************************************
	 * 																												   *
	 *  Setter/Getter																							       *
	 * 																												   *
	 *******************************************************************************************************************/
	/**
	 * @return
	 */
	public CLOSE_MODE getMode() {
		return mode;
	}
	
	/**
	 * @return
	 */
	public ScheduleItem getSchedule() {
		return this.schedule;
	}
	
}
