package schedule.entity;

public class ScheduleKind {
	private String code;
	private String name;
	private String colorCode;
	
	public ScheduleKind(String code, String name, String colorCode) {
		this.code = code;
		this.name = name;
		this.colorCode  = colorCode;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name セットする name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return colorCode
	 */
	public String getColorCode() {
		return colorCode;
	}

	/**
	 * @return code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code セットする code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @param colorCode セットする colorCode
	 */
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
	
	
}
