package schedule;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import schedule.entity.ScheduleBox;
import schedule.entity.ScheduleComparator;
import schedule.entity.ScheduleItem;
import schedule.entity.ScheduleKind;
import schedule.entity.ScheduleLabel;
import schedule.entity.SplitCommaText;

/**
 * @author teruma
 *
 */
public class Schedule extends BorderPane {
	
	/*******************************************************************************************************************
	 * 																												   *
	 *  Constant   																								       *
	 * 																												   *
	 *******************************************************************************************************************/
	enum ControlMode  {NONE, CUT, COPY};
	final static String DATA_PATH = "./data";
	final static String SCHEDULE_FILE = "schedule.dat";
	
	YearMonth yearMonth;
	
	/*******************************************************************************************************************
	 * 																												   *
	 *  Datas  																								           *
	 * 																												   *
	 *******************************************************************************************************************/
	List<ScheduleItem> schedules;
	List<ScheduleKind> kinds;
	Map<LocalDate, ScheduleBox> cells;
	
	/*******************************************************************************************************************
	 * 																												   *
	 *  Compornents    																								   *
	 * 																												   *
	 *******************************************************************************************************************/
	VBox kindsBox;
	VBox scheduleBox;
	GridPane calendar;
	ScheduleEditor editor;
	Button yearMonthButton;
	Alert alert;

	/*******************************************************************************************************************
	 * 																												   *
	 *  Control Property																							   *
	 * 																												   *
	 *******************************************************************************************************************/
	boolean disabled;
	ScheduleItem selected;
	ScheduleItem copySchedule;
	ControlMode mode;
	
	/******************************************************************************************************************
	 *																												  *
	 *  Constractor																									  *
	 * 																												  *
	 ******************************************************************************************************************/
	
	public Schedule(Stage stage) {
		//スーパークラスコンストラクタ
		super();
		
		//----------------------------------------------------------------------
		//	コントロール用変数初期化
		//
		disabled = false;
		selected = null;
		copySchedule = null;
		mode = ControlMode.NONE;
		
		//今年月取得
		yearMonth = YearMonth.now();
		
		//---------------------------------------------------------------------
		//	データ読み込み
		//
		kinds = readScheduleKinds();
		readSchedules();
		
		//---------------------------------------------------------------------
		//	ウィンドウ設定
		//
		//エラー用ウィンドウ
		alert = new Alert(AlertType.ERROR);
		alert.setTitle("B-SAN");
		alert.setHeaderText("エラー");
		
		//スケジュール編集用オブジェクトを生成
		editor = new ScheduleEditor(stage, kinds);
		
		//---------------------------------------------------------------------
		//	カレンダー設定
		//
		kindsBox = new VBox();
		
		//種類ヘッダー
		ScrollPane sp = new ScrollPane();
		sp.prefHeightProperty().bind(Bindings.subtract(this.prefHeightProperty(), 1));
		sp.getStyleClass().add("scroll_pane");
		
		kindsBox.setPrefWidth(175);
		kindsBox.getChildren().addAll(sp);
		
		
		//--------------------------------------------------
		//	月スケジュール設定
		//
		scheduleBox = new VBox();

		//カレンダー設定
		createCalendar();
		setCalendar();
		
		scheduleBox.prefWidthProperty().bind(Bindings.subtract(this.prefWidthProperty(), kindsBox.prefWidthProperty()));
		scheduleBox.getChildren().addAll(createScheduleHeader(), createWeekLabels(), calendar);

		this.setLeft(kindsBox);
		this.setCenter(scheduleBox);
		this.getStylesheets().add(Schedule.class.getResource("calendar.css").toExternalForm());
		
	}
	
	/******************************************************************************************************************
	 *																												  *
	 *	read Method                                                                                                   *
	 *                                                                                                                *
	 ******************************************************************************************************************/
	/**
	 * スケジュールの読み込み
	 * @return
	 */
	private void readSchedules() {
		schedules = new ArrayList<>();
		
		try {
			//ファイル作成
			Path filePath = Paths.get(DATA_PATH, SCHEDULE_FILE);
			if(!Files.exists(filePath)) 
				return;
			
			for(String str : Files.readAllLines(filePath)) {
				String[] items = SplitCommaText.splitCommaText(str);
				if(items == null) throw new Exception();
				schedules.add(new ScheduleItem(items));
			}
		} catch(Exception e) {
			alert.setContentText("スケジュールの読み込みに失敗しました。");
			alert.showAndWait();
			schedules = new ArrayList<>();
			return;
		}
	}
	
	/**
	 * スケジュール種類を読み込み
	 * @return
	 */
	private List<ScheduleKind> readScheduleKinds() { 
		List<ScheduleKind> list = new ArrayList<>();
		
		list.add(new ScheduleKind("1", "カレンダー1", "#000000"));
		list.add(new ScheduleKind("2", "カレンダー2", "#111111"));
		
		return list;
	}
	/******************************************************************************************************************
	 *																												  *
	 *	write Method                                                                                                  *
	 *                                                                                                                *
	 ******************************************************************************************************************/
	private void writeSchedules() throws Exception {
		//ディレクトリが存在しない場合作成
		Path dirPath = Paths.get(DATA_PATH);
		if(!Files.exists(dirPath)) {
			Files.createDirectory(dirPath);
		}
		//ファイルがない場合に作成
		Path filePath = Paths.get(DATA_PATH, SCHEDULE_FILE);
		if(!Files.exists(filePath)) {
			Files.createFile(filePath);
		}
		//出力データ取得
		List<String> list = schedules.stream()
				                     .map(s -> s.toCVS())
				                     .collect(Collectors.toList());
		//ファイル出力
		Files.write(filePath, list);
	}
	
	/******************************************************************************************************************
	 *																												  *
	 *	Creators                                                                                                      *
	 *                                                                                                                *
	 ******************************************************************************************************************/                                                                                                                
	/**
	 * スケジュールヘッダを生成
	 * @return
	 */
	private HBox createScheduleHeader() {
		
		//月日ボタン
		yearMonthButton = new Button(yearMonth.format(DateTimeFormatter.ofPattern("yyyy年MM月")));
		yearMonthButton.getStyleClass().add("calendar_month_button");
		
		//前月ボタン
		Button backButton = new Button("◀");
		backButton.getStyleClass().add("calendar_move_button");
		backButton.setOnAction(event -> backMonth());
		
		//次月ボタン
		Button nextButton = new Button("▶");
		nextButton.getStyleClass().add("calendar_move_button");
		nextButton.setOnAction(event -> nextMonth());
		
		//ヘッダー
		HBox header = new HBox();
		header.getChildren().addAll(backButton, yearMonthButton, nextButton);
		header.getStyleClass().add("calendar_pane_header");
		header.setSpacing(5.0);
		header.setAlignment(Pos.CENTER_LEFT);
		
		return header;
	}
	
	/**
	 * 曜日フォームを生成
	 * @return
	 */
	private GridPane createWeekLabels() {
		
		GridPane weekLabels = new GridPane();
		final String[] WEEK_JPN = {"日", "月", "火", "水", "木", "金", "土"};
		
		for(int d = 0; d < 7; d++) {
			Label weekLabel = new Label(WEEK_JPN[d]);
			
			weekLabel.getStyleClass().add("calendar_header");
			weekLabel.prefWidthProperty().bind(Bindings.divide(scheduleBox.prefWidthProperty(), 7));
			
			GridPane.setConstraints(weekLabel, d, 0);
			weekLabels.getChildren().add(weekLabel);
		}
		
		return weekLabels;
	}
	
	/*
	 * カレンダーフォームを生成
	 * @return
	 */
	private void createCalendar() {
		
		calendar = new GridPane();
		calendar.prefWidthProperty().bind(scheduleBox.prefWidthProperty());
		calendar.prefHeightProperty().bind(this.prefHeightProperty());
		
		cells = new HashMap<>();
		
		for(int w = 0; w < 6; w++) {
			for(int d = 0; d < 7; d++) {
				ScheduleBox cell = new ScheduleBox();
				
				cell.prefWidthProperty().bind(Bindings.divide(calendar.prefWidthProperty(), 7));
				cell.prefHeightProperty().bind(Bindings.divide(calendar.prefHeightProperty(), 6));
				
				//クリックイベント
				cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						//ダブルクリック時処理
						if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
							//labelクリック時は動作させない
							if (disabled) {
								disabled = false;
								cell.setDisable(disabled);
							} else {
								//スケジュール作成
								openScheduleCreator(cell.getDate());
							}
						}
						//クリック時処理
						else if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
							if (disabled) {
								disabled = false;
								cell.setDisable(disabled);
							} else {
								selected = null;
								resetSelectedSchedules();
								cell.setSelected(true);
							}
						}
					}
				});
				
				//ドラッグイベント
				cell.setOnDragOver(new EventHandler <DragEvent>() {
					public void handle(DragEvent event) {
						if(event.getGestureSource() != cell && event.getDragboard().hasString()) {
							event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
							cell.setDraged(true);
						}
						
						event.consume();
					}
				});
				
				cell.setOnDragExited(new EventHandler <DragEvent>() {
					public void handle(DragEvent event) {
						if(event.getGestureSource() != cell && event.getDragboard().hasString()) {
							cell.setDraged(false);
						}
						
						event.consume();
					}
				});
				
				cell.setOnDragDropped(new EventHandler <DragEvent>() {
					public void handle(DragEvent event) {
						Dragboard db = event.getDragboard();
						boolean success = false;
						if(db.hasString()) {
							pasteSchedule(cell);
							success = true;
						}
						event.setDropCompleted(success);
						
						event.consume();
					}
				});
				
				//キーイベント
				cell.setOnKeyPressed(event -> keyEvent(event, cell));
				
				//コンテキストメニュー
				//追加
				MenuItem add = new MenuItem("追加\t\t\tCtrl+n");
				add.setOnAction(event -> openScheduleCreator(cell.getDate()));
				//貼り付け
				MenuItem paste = new MenuItem("貼り付け\t\tCtrl+v");
				paste.setOnAction(event -> pasteSchedule(cell));
				
				ContextMenu popup = new ContextMenu(add, paste);
				popup.getStyleClass().add("popup");
				cell.setContextMenu(popup);
				
				GridPane.setConstraints(cell, d, w + 1);
				calendar.getChildren().add(cell);
			}
		}
	}
	
	/**
	 * スケジュールラベルを生成
	 * @param cell
	 * @param item
	 * @return
	 */
	private ScheduleLabel createScheduleLabel(ScheduleBox cell, ScheduleItem item) {
		
		ScheduleLabel lbl = new ScheduleLabel(item);
		lbl.prefWidthProperty().bind(Bindings.subtract(cell.prefWidthProperty(), 5));
		
		//クリックイベント
		lbl.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//ダブルクリック時処理
				if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
					openScheduleEditor(item);
					//VBox クリックイベント回避
					disabled = true;
					cell.setDisable(disabled);
				} 
				//クリック時処理
				else if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
					disabled = true;
					selected = item;
					resetSelectedSchedules();
					cell.setSelected(true);
					lbl.getGroup().forEach(l -> l.selected(true));
				}
			}
		});
		//ドラッグイベント
		lbl.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Dragboard db = lbl.startDragAndDrop(TransferMode.ANY);
				ClipboardContent content = new ClipboardContent();
				content.putString(lbl.getText());
				db.setContent(content);
				
				copySchedule = item;
				mode = ControlMode.CUT;
				
				event.consume();
			}
		});
		
		Tooltip tooltip = new Tooltip();
		tooltip.setText(item.toString());
		lbl.setTooltip(tooltip);
		
		//コンテキストメニュー設定
		//追加
		MenuItem add = new MenuItem("追加\t\t\tCtrl+n");
		add.setOnAction(event -> openScheduleCreator(cell.getDate()));
		//編集
		MenuItem edit = new MenuItem("編集\t\t\tCtrl+e");
		edit.setOnAction(event -> openScheduleEditor(item));
		//切り取り
		MenuItem cut = new MenuItem("切り取り\t\tCtrl+x");
		cut.setOnAction(event -> {
			copySchedule = item;
			mode = ControlMode.CUT;
		});
		//コピー
		MenuItem copy = new MenuItem("コピー\t\t\tCtrl+c");
		copy.setOnAction(event -> {
			copySchedule = item;
			mode = ControlMode.COPY;
		});
		//貼り付け
		MenuItem paste = new MenuItem("貼り付け\t\tCtrl+v");
		paste.setOnAction(event -> pasteSchedule(cell));
		//削除
		MenuItem delete = new MenuItem("削除\t\t\tdelete");
		delete.setOnAction(event -> 	removeSchedule(item));
		
		ContextMenu popup = new ContextMenu(add, edit, cut, copy, paste, delete);
		popup.getStyleClass().add("popup");
		lbl.setContextMenu(popup);
		
		return lbl;
	}
	
	/******************************************************************************************************************
	 *																												  *
	 *	Conroll Schedule Method                                                                                       *
	 *                                                                                                                *
	 ******************************************************************************************************************/
	/**
	 * 前月処理
	 */
	private void backMonth() {
		resetSelectedSchedules();
		this.yearMonth = this.yearMonth.minusMonths(1);
		yearMonthButton.setText(this.yearMonth.format(DateTimeFormatter.ofPattern("yyyy年MM月")));
		setCalendar();
	}
	
	/**
	 * 次月処理
	 */
	private void nextMonth() {
		resetSelectedSchedules();
		this.yearMonth = this.yearMonth.plusMonths(1);
		yearMonthButton.setText(this.yearMonth.format(DateTimeFormatter.ofPattern("yyyy年MM月")));
		setCalendar();
	}
	
	/**
	 * 指定した月日のカレンダーを設定
	 * @param calendar
	 * @param yearMonth
	 */
	private void setCalendar() {
		
		this.cells.clear();
		
		LocalDate beginDate = yearMonth.atDay(1);
		LocalDate date = beginDate.minusDays(beginDate.getDayOfWeek().getValue() == 7 ? 0 : beginDate.getDayOfWeek().getValue());
		
		for(Node node : calendar.getChildren()) {
			ScheduleBox cell = (ScheduleBox)node;
			cell.setDate(date, this.yearMonth);
			this.cells.put(date, cell);
			date = date.plusDays(1);
		}
		
		//スケジュールを設定
		setSchedules();
	}
	
	/**
	 * カレンダーにスケジュールを設定
	 * @param calendar
	 * @param yearMonth
	 * @param schedules
	 */
	private void setSchedules() {
		
		//スケジュールをリセット
		resetSchedules();

		//スケジュールをセット
		for(ScheduleItem s : schedules) {
				
			LocalDate date = s.getStartLocalDate();
			List<ScheduleLabel> group = new ArrayList<>();
			
			for(int i = 0; i < s.getSubDate() + 1; i++) {
				LocalDate d = date.plusDays(i);
				
				if(!cells.containsKey(d)) continue;
				
				ScheduleBox cell = cells.get(d);
				ScheduleLabel lbl = createScheduleLabel(cell, s);
				lbl.setGroup(group);
				group.add(lbl);
				cell.addSchedule(lbl);
			}
		}
	}
	
	/**
	 * 	カレンダーのスケジュールをリセット
	 */
	private void resetSchedules() {
		for(Node node : calendar.getChildren()) {
			ScheduleBox cell = (ScheduleBox)node;
			cell.removeSchedules();
		}
	}
	
	/**
	 * スケジュールを追加
	 * @param item
	 */
	private void addSchedule(ScheduleItem item) {
		schedules.add(item);
		Collections.sort(schedules, new ScheduleComparator());
		
		try {
			writeSchedules();
			setSchedules();
		} catch (Exception e) {
			schedules.remove(item);
			alert.setContentText("スケジュールの書き込みに失敗しました。");
			alert.showAndWait();
		}
	}
	
	/**
	 * スケジュールを削除
	 * @param cell
	 * @param lbl
	 * @param item
	 */
	private void removeSchedule(ScheduleItem item) {
		schedules.remove(item);
		try {
			writeSchedules();
			setSchedules();
		} catch (Exception e) {
			schedules.remove(item);
			alert.setContentText("スケジュールの書き込みに失敗しました。");
			alert.showAndWait();
		}
	}
	
	/**
	 * スケジュールを貼り付け
	 * @param cell
	 */
	private void pasteSchedule(ScheduleBox cell) {
		
		if(copySchedule == null) return;
		
		ScheduleItem item = copySchedule.clone(cell.getDate());
		
		if(mode == ControlMode.COPY) {
			addSchedule(item);
		} else if (mode == ControlMode.CUT) {
			removeSchedule(copySchedule);
			addSchedule(item);
			mode = ControlMode.NONE;
		}
	}
	
	/**
	 * スケジュールの選択状態を解除
	 */
	private void resetSelectedSchedules() {
		for(ScheduleBox cell : new ArrayList<>(cells.values())) {
			for(ScheduleLabel lbl : cell.getSchedules()) {
				lbl.selected(false);
			}
			cell.setSelected(false);
		}
	}
	
	/**
	 * セルキーイベント
	 * @param event
	 */
	private void keyEvent(KeyEvent event, ScheduleBox cell) {
		
		if("DELETE".equals(event.getCode().toString())) {
			if(selected == null) return;
			removeSchedule(selected);
		}
		
		if(!event.isControlDown()) return;
		
		switch(event.getCode().toString()) {
		case "N":
			openScheduleCreator(cell.getDate());
			break;
		case "E":
			if(selected == null) return;
			openScheduleEditor(selected);
			break;
		case "X":
			if(selected == null) return;
			copySchedule = selected;
			mode = ControlMode.CUT;
			break;
		case "C":
			if(selected == null) return;
			copySchedule = selected;
			mode = ControlMode.COPY;
			break;
		case "V":
			pasteSchedule(cell);
			break;
		}
	}
	
	/******************************************************************************************************************
	 *																												  *
	 *	Schedule Editor Method                                                                                        *
	 *                                                                                                                *
	 ******************************************************************************************************************/
	
	/**
	 * 編集用画面を表示
	 * @param cell
	 * @param lbl
	 * @param item
	 */
	private void openScheduleEditor(ScheduleItem item) {
		
		//編集画面表示
		editor.setTitle("予定編集");
		editor.showEditor(item);
		
		//削除処理
		if (editor.getMode() == ScheduleEditor.CLOSE_MODE.OK || editor.getMode() == ScheduleEditor.CLOSE_MODE.DELETE) {
			removeSchedule(item);
		}
		
		//スケジュール更新
		if (editor.getMode() == ScheduleEditor.CLOSE_MODE.OK) {
			addSchedule(editor.getSchedule());
		}
	}
	
	/**
	 * 作成用画面表示
	 * @param date
	 */
	private void openScheduleCreator(LocalDate date) {
		
		//作成画面表示
		editor.setTitle("予定作成");
		editor.showCreator(date);
		
		//スケジュール更新
		if (editor.getMode() == ScheduleEditor.CLOSE_MODE.OK) {
			addSchedule(editor.getSchedule());
			setSchedules();
		}
	}
	
}
