package bit.minicc.parser;

public class AnalyseWord {
	
	private String name;
	private String value;
	private String type;
	private boolean isTerminal;
	
	
	public AnalyseWord() {
		super();
	}

	public AnalyseWord(String name, String value,String type, boolean isTerminal) {
		super();
		this.name = name;
		this.value = value;
		this.type = type;
		this.isTerminal = isTerminal;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isTerminal() {
		return isTerminal;
	}

	public void setTerminal(boolean isTerminal) {
		this.isTerminal = isTerminal;
	}
	
	

}
