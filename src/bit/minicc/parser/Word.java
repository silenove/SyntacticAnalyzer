package bit.minicc.parser;

import java.util.ArrayList;
import java.util.List;

public class Word {
	
	private int number;
	private String value;
	private String type;
	private int line;
	private boolean valid;
	
	public Word() {
		super();
	}
	
	

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	//判断是否为定义类型关键字
	public boolean isDefType(){
		return def_type.contains(value);
	}
	
	//判断是否为运算符
	public boolean isOperator(){
		return operator.contains(value);
	}
	
	//判断是否为常数
	public boolean isConstant(){
		return def_type.contains(type);
	}
	
	public boolean isIF(){
		String string = "if";
		return string.equals(value);
	}
	
	public boolean isWHILE(){
		String string = "while";
		return string.equals(value);
	}
	
	public static List<String> def_type;
	public static List<String> operator;
	
	static{
		def_type = new ArrayList<>();
		operator = new ArrayList<>();
		
		def_type.add("int");
		def_type.add("char");
		def_type.add("short");
		def_type.add("float");
		def_type.add("double");
		
		operator.add("+");
		operator.add("-");
		operator.add("*");
		operator.add("/");
		operator.add(">");
		operator.add("<");
		operator.add("==");
		operator.add(">=");
		operator.add("!=");
		operator.add("<=");
		
	}
	
	
	

}
