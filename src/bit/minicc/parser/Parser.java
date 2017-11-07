package bit.minicc.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


import javax.management.loading.PrivateClassLoader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.omg.PortableServer.ID_ASSIGNMENT_POLICY_ID;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Parser {
		
	private AnalyseWord PROGRAM;
	private AnalyseWord FUNCTIONS;
	private AnalyseWord FUNCTION;
	private AnalyseWord TYPE;
	private AnalyseWord ARGS;
	private AnalyseWord ALIST;
	private AnalyseWord FUNC_BODY;
	private AnalyseWord EX_FUNC;
	private AnalyseWord STMS;
	private AnalyseWord STM;
	private AnalyseWord ASSIGN;
	private AnalyseWord EX_ASSIGN;
	private AnalyseWord COMPARE;
	private AnalyseWord OP;
	private AnalyseWord RE;
	private AnalyseWord EX_RE;
	private AnalyseWord ID;
	private AnalyseWord INT;
	private AnalyseWord CHAR;
	private AnalyseWord SHORT;
	private AnalyseWord FLOAT;
	private AnalyseWord DOUBLE;
	private AnalyseWord ADD;
	private AnalyseWord MINUS;
	private AnalyseWord MULTI;
	private AnalyseWord DIV;
	private AnalyseWord GREATER;
	private AnalyseWord SMALLER;
	private AnalyseWord EQUAL;
	private AnalyseWord IF_EQUAL;
	private AnalyseWord NONEQ;
	private AnalyseWord GR_EQ;
	private AnalyseWord SM_EQ;
	private AnalyseWord LBR;
	private AnalyseWord RBR;
	private AnalyseWord LPAR;
	private AnalyseWord RPAR;
	private AnalyseWord SEMI;
	private AnalyseWord RETURN;
	private AnalyseWord IF;
	private AnalyseWord ELSE;
	private AnalyseWord WHILE;
	private AnalyseWord COMMA;
	
	
	
	private Stack<AnalyseWord> analyseStack; //语法分析栈
	private Stack<Element> elementStack;  //语法栈对应的结点栈
	private List<Word> inputWords;  //输入单词流
	
	private boolean errorFlag;
	
	private Element root;

	
	public Parser(String inputPath) throws ParserConfigurationException, SAXException, IOException{
		
		this.analyseStack = new Stack<>();
		this.elementStack = new Stack<>();
		this.inputWords = new ArrayList<>();
		errorFlag = false;
		
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = null;
		File file = new File(inputPath);
		InputStream iStream = new FileInputStream(file);
		document = documentBuilder.parse(iStream);
		NodeList nodeList = document.getElementsByTagName("token");
		
		//初始化输入流
		initInputWords(nodeList,document);
		
		//初始化分析字
		initAnalyseWord();
		
		
		initOutput();
		
	}
	

	private void initInputWords(NodeList nodeList,Document document){
		
		for(int i = 0;i < nodeList.getLength();i++){
			Node token = nodeList.item(i);
			Word word = new Word();

			word.setNumber(Integer.parseInt(document.getElementsByTagName("number").item(i).getFirstChild().getNodeValue()));
			word.setValue(document.getElementsByTagName("value").item(i).getFirstChild().getNodeValue());
			word.setType(document.getElementsByTagName("type").item(i).getFirstChild().getNodeValue());
			word.setLine(Integer.parseInt(document.getElementsByTagName("line").item(i).getFirstChild().getNodeValue()));
			word.setValid(Boolean.getBoolean(document.getElementsByTagName("valid").item(i).getFirstChild().getNodeValue()));
			
			inputWords.add(word);
			if(word.getValue() == "#")
				break;
			
//			System.out.println(inputWords.size());
				
		}

	}
	
	private void initAnalyseWord() {
		
		//文法非终结符
		PROGRAM = new AnalyseWord("PROGRAM",null,null,false);
		FUNCTIONS = new AnalyseWord("FUNCTIONS",null, null, false);
		FUNCTION = new AnalyseWord("FUNCTION",null,null,false);
		TYPE = new AnalyseWord("TYPE",null,null,false);
		ARGS = new AnalyseWord("ARGS",null,null,false);
		ALIST = new AnalyseWord("ALIST",null,null,false);
		FUNC_BODY = new AnalyseWord("FUNC_BODY",null,null,false);
		EX_FUNC = new AnalyseWord("EX_FUNC",null, null, false);
		STMS = new AnalyseWord("STMS",null,null,false);
		STM = new AnalyseWord("STM",null,null,false);
		ASSIGN = new AnalyseWord("ASSIGN",null,null,false);
		EX_ASSIGN = new AnalyseWord("EX_ASSIGN",null, null, false);
		COMPARE = 	new AnalyseWord("COMPARE",null,null,false);
		OP = new AnalyseWord("OPERATOR",null,null,false);
		RE = new AnalyseWord("RE_CONTENT",null,null,false);
		EX_RE = new AnalyseWord("EX_RE_CONTENT",null,null,false);
		ID = new AnalyseWord("ID",null,null,false);
		
		//文法终结符
		INT = new AnalyseWord("INT","int","keyword",true);
		CHAR = new AnalyseWord("CHAR","char","keyword",true);
		SHORT = new AnalyseWord("SHORT","short","keyword",true);
		FLOAT = new AnalyseWord("FLOAT","float","keyword",true);
		DOUBLE = new AnalyseWord("DOUBLE","double","keyword",true);
		ADD = new AnalyseWord("ADD","+","operator",true);
		MINUS = new AnalyseWord("MINUS","-","operator",true);
		MULTI = new AnalyseWord("MULTI","*","operator",true);
		DIV = new AnalyseWord("DIV","/","operator",true);
		GREATER = new AnalyseWord("GREATER",">","operator",true);
		SMALLER = new AnalyseWord("SMALLER","<","operator",true);
		EQUAL = new AnalyseWord("EQUAL","=","operator",true);
		IF_EQUAL = new AnalyseWord("IF_EQUAL","==","operator",true);
		NONEQ = new AnalyseWord("NONEQ","!=","operator",true);
		GR_EQ = new AnalyseWord("GR_EQ",">=","operator",true);
		SM_EQ = new AnalyseWord("SM_EQ","<=","operator",true);
		LBR = new AnalyseWord("LBR","{","separator",true);
		RBR = new AnalyseWord("RBR","}","separator",true);
		LPAR = new AnalyseWord("LPAR","(","separator",true);
		RPAR = new AnalyseWord("RPAR",")","separator",true);
		RETURN = new AnalyseWord("RETURN","return","keyword",true);
		SEMI = new AnalyseWord("SEMI",";","separator",true);
		IF = new AnalyseWord("IF","if","keyword",true);
		ELSE = new AnalyseWord("ELSE","else","keyword",true);
		WHILE = new AnalyseWord("WHILE","while","keyword",true);
		COMMA = new AnalyseWord("COMMA",",","separator",true);

	}
	
	public void initOutput(){
		root = new Element("ParserResult");
		
	}
	
	private void joinTree(Element child,Element father){
		father.addContent(child);
	}
	
	public void startParse(){
	
		
		analyseStack.push(new AnalyseWord("#","#", "#", true));
		analyseStack.push(PROGRAM);
		Element programElement = new Element(PROGRAM.getName());
		elementStack.push(programElement);
		root.addContent(programElement);
		
		while(!analyseStack.empty()&&!inputWords.isEmpty()){
			
//			System.out.println(i++);
			
			if(errorFlag == true)
				break;
			AnalyseWord analyseTop = analyseStack.pop();
			System.out.println(analyseTop.getName());
			Element father = null;
			if(!analyseTop.isTerminal()){
				father = elementStack.pop();
			}
			Word wordTop = inputWords.get(0);
			switch (analyseTop.getName()) {
			case "PROGRAM":
				if(wordTop.isDefType()||"void".equals(wordTop.getValue())){
					analyseStack.push(FUNCTIONS);
					Element functions = new Element(FUNCTIONS.getName());
					elementStack.push(functions);
					joinTree(functions, father);
				}else{
					errorFlag = true;
				}
				break;
			case "FUNCTIONS":
				if(wordTop.isDefType()||"void".equals(wordTop.getValue())){
					Element ex_func = new Element(EX_FUNC.getName());
					Element function = new Element(FUNCTION.getName());
					analyseStack.push(EX_FUNC);
					elementStack.push( ex_func);
					analyseStack.push(FUNCTION);
					elementStack.push(function);
					joinTree(function, father);
					joinTree(ex_func, father);
				}else{
					errorFlag = true;
				}
				break;
			case "FUNCTION":
				if(wordTop.isDefType()||"void".equals(wordTop.getValue())){
					Element func_body = new Element(FUNC_BODY.getName());
					Element args = new Element(ARGS.getName());
					Element id = new Element(ID.getName());
					Element type = new Element(TYPE.getName());
					analyseStack.push(FUNC_BODY);
					elementStack.push(func_body);
					analyseStack.push(RPAR);
					analyseStack.push(ARGS);
					elementStack.push(args);
					analyseStack.push(LPAR);
					analyseStack.push(ID);
					elementStack.push(id);
					analyseStack.push(TYPE);
					elementStack.push(type);
					joinTree(type, father);
					joinTree(id, father);
					joinTree(new Element(LPAR.getType()).setText("("), father);
					joinTree(args, father);
					joinTree(new Element(RPAR.getType()).setText(")"), father);
					joinTree(func_body, father);
				}else{
					errorFlag = true;
				}
				break;
			case "TYPE":
				if(wordTop.getValue().equals("int")){
					analyseStack.push(INT);
					joinTree(new Element(INT.getType()).setText(INT.getValue()), father);
				}else if(wordTop.getValue() == "short"){
					analyseStack.push(SHORT);
					joinTree(new Element(SHORT.getType()).setText(SHORT.getValue()), father);
				}else if(wordTop.getValue() == "char"){
					analyseStack.push(CHAR);
					joinTree(new Element(CHAR.getType()).setText(CHAR.getValue()), father);
				}else if(wordTop.getValue() == "float"){
					analyseStack.push(FLOAT);
					joinTree(new Element(FLOAT.getType()).setText(FLOAT.getValue()), father);
				}else if(wordTop.getValue() == "double"){
					analyseStack.push(DOUBLE);
					joinTree(new Element(DOUBLE.getType()).setText(DOUBLE.getValue()), father);
				}else if(wordTop.getType().equals("identifier")){
					
				}else{
					errorFlag = true;
				}
				break;
			case "ARGS":
				if(wordTop.isDefType()){
					Element alist = new Element(ALIST.getName());
					Element id = new Element(ID.getName());
					Element type = new Element(TYPE.getName());
					analyseStack.push(ALIST);
					elementStack.push(alist);
					analyseStack.push(ID);
					elementStack.push(id);
					analyseStack.push(TYPE);
					elementStack.push(type);
					joinTree(type, father);
					joinTree(id, father);
					joinTree(alist, father);
				}else{
					errorFlag = true;
				}
				break;
			case "ALIST":
				if(wordTop.getValue().equals(",")){
					Element alist = new Element(ALIST.getName());
					Element id = new Element(ID.getName());
					Element type = new Element(TYPE.getName());
					Element comma = new Element(COMMA.getType()).setText(COMMA.getValue());
					analyseStack.push(ALIST);
					elementStack.push(alist);
					analyseStack.push(ID);
					elementStack.push(id);
					analyseStack.push(TYPE);
					elementStack.push(type);
					analyseStack.push(COMMA);
					joinTree(comma, father);
					joinTree(type, father);
					joinTree(id, father);
					joinTree(alist, father);	
				}else if(wordTop.getValue().equals(")")){
					//不操作
				}else{
					errorFlag = true;
				}
				break;
			case "FUNC_BODY":
				if(wordTop.getValue() .equals("{")){
					Element stms = new Element(STMS.getName());
					analyseStack.push(RBR);
					analyseStack.push(STMS);
					elementStack.push(stms);
					analyseStack.push(LBR);
					joinTree(new Element(LBR.getType()).setText(LBR.getValue()), father);
					joinTree(stms, father);
					joinTree(new Element(RBR.getType()).setText(RBR.getValue()), father);
				}else{
					errorFlag = true;
				}
				break;
			case "EX_FUNC":
				if(wordTop.isDefType()){
					Element ex_func = new Element(EX_FUNC.getName());
					Element function = new Element(FUNCTION.getName());
					analyseStack.push(EX_FUNC);
					elementStack.push(ex_func);
					analyseStack.push(FUNCTION);
					elementStack.push(function);
					joinTree(ex_func, father);
					joinTree(function, father);
				}else if(wordTop.getValue() .equals("#")){
					//不操作
				}else{
					errorFlag = true;
				}
				break;
			case "STMS":
				if(!wordTop.getValue().equals("}")){
					Element stms = new Element(STMS.getName());
					Element stm = new Element(STM.getName());
					analyseStack.push(STMS);
					elementStack.push(stms);
					analyseStack.push(STM);
					elementStack.push(stm);
					joinTree(stm, father);
					joinTree(stms, father);
				}else if(wordTop.getValue().equals("}")){
					//不操作
				}else{
					errorFlag = true;
				}
				break;
			case "STM":
				if(wordTop.isDefType() || wordTop.getType().equals("identifier")){
					Element assign = new Element(ASSIGN.getName());
					Element id = new Element(ID.getName());
					Element type = new Element(TYPE.getName());
					analyseStack.push(ASSIGN);
					elementStack.push(assign);
					analyseStack.push(ID);
					elementStack.push(id);
					analyseStack.push(TYPE);
					elementStack.push(type);
					joinTree(type, father);
					joinTree(id, father);
					joinTree(assign, father);
				}else if(wordTop.getValue().equals("if")){
					Element stms1 = new Element(STMS.getName());
					Element stms2 = new Element(STMS.getName());
					Element compare = new Element(COMPARE.getName());
					analyseStack.push(RBR);
					analyseStack.push(STMS);
					elementStack.push(stms2);
					analyseStack.push(LBR);
					analyseStack.push(ELSE);
					analyseStack.push(RBR);
					analyseStack.push(STMS);
					elementStack.push(stms1);
					analyseStack.push(LBR);
					analyseStack.push(RPAR);
					analyseStack.push(COMPARE);
					elementStack.push(compare);
					analyseStack.push(LPAR);
					analyseStack.push(IF);
					joinTree(new Element(IF.getType()).setText(IF.getValue()), father);
					joinTree(new Element(LPAR.getType()).setText(LPAR.getValue()), father);
					joinTree(compare, father);
					joinTree(new Element(RPAR.getType()).setText(RPAR.getValue()), father);
					joinTree(new Element(LBR.getType()).setText(LBR.getValue()), father);
					joinTree(stms1, father);
					joinTree(new Element(RBR.getType()).setText(RBR.getValue()), father);
					joinTree(new Element(ELSE.getType()).setText(ELSE.getValue()), father);
					joinTree(new Element(LBR.getType()).setText(LBR.getValue()), father);
					joinTree(stms2, father);
					joinTree(new Element(RBR.getType()).setText(RBR.getValue()), father);
				}else if(wordTop.getValue().equals("while")){
					Element stms = new Element(STMS.getName());
					Element compare = new Element(COMPARE.getName());
					analyseStack.push(RBR);
					analyseStack.push(STMS);
					elementStack.push(stms);
					analyseStack.push(LBR);
					analyseStack.push(RPAR);
					analyseStack.push(COMPARE);
					elementStack.push(compare);
					analyseStack.push(LPAR);
					analyseStack.push(WHILE);
					joinTree(new Element(WHILE.getType()).setText(WHILE.getValue()), father);
					joinTree(new Element(LPAR.getType()).setText(LPAR.getValue()), father);
					joinTree(compare, father);
					joinTree(new Element(RPAR.getType()).setText(RPAR.getValue()), father);
					joinTree(new Element(LBR.getType()).setText(LBR.getValue()), father);
					joinTree(stms, father);
					joinTree(new Element(RBR.getType()).setText(RBR.getValue()), father);
				}else if(wordTop.getValue().equals("return")){
					Element re = new Element(RE.getName());
					analyseStack.push(RE);
					elementStack.push(re);
					analyseStack.push(RETURN);
					joinTree(new Element(RETURN.getType()).setText(RETURN.getValue()), father);
					joinTree(re, father);
				}else{
					errorFlag = true;
				}
				break;
			case "ASSIGN":
				if(wordTop.getValue().equals(";")){
					analyseStack.push(SEMI);
					joinTree(new Element(SEMI.getType()).setText(SEMI.getValue()), father);
				}else if(wordTop.getValue().equals("=")){
					Element ex_assign = new Element(EX_ASSIGN.getName());
					Element id = new Element(ID.getName());
					analyseStack.push(EX_ASSIGN);
					elementStack.push(ex_assign);
					analyseStack.push(ID);
					elementStack.push(id);
					analyseStack.push(EQUAL);
					joinTree(new Element(EQUAL.getType()).setText(EQUAL.getValue()), father);
					joinTree(id, father);
					joinTree(ex_assign, father);
				}else{
					errorFlag = true;
				}
				break;
			case "EX_ASSIGN":
				if(wordTop.getValue().equals(";")){
					analyseStack.push(SEMI);
					joinTree(new Element(SEMI.getType()).setText(SEMI.getValue()), father);
				}else if(wordTop.isOperator()){
					Element id = new Element(ID.getName());
					Element op = new Element(OP.getName());
					analyseStack.push(SEMI);
					analyseStack.push(ID);
					elementStack.push(id);
					analyseStack.push(OP);
					elementStack.push(op);
					joinTree(op, father);
					joinTree(id, father);
					joinTree(new Element(SEMI.getType()).setText(SEMI.getValue()), father);
				}else{
					errorFlag = true;
				}
				break;
			case "COMPARE":
				if(wordTop.getType().equals("identifier")){
					Element id1 = new Element(ID.getName());
					Element op = new Element(OP.getName());
					Element id2 = new Element(ID.getName());
					analyseStack.push(ID);
					elementStack.push(id2);
					analyseStack.push(OP);
					elementStack.push(op);
					analyseStack.push(ID);
					elementStack.push(id1);
					joinTree(id1, father);
					joinTree(op, father);
					joinTree(id2, father);
				}else if(wordTop.isConstant()){
					joinTree(new Element("constant").setText(wordTop.getValue()), father);
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			case "OPERATOR":
				if(wordTop.getValue().equals("+")){
					analyseStack.push(ADD);
					joinTree(new Element(ADD.getType()).setText(ADD.getValue()), father);
				}else if(wordTop.getValue().equals("-")){
					analyseStack.push(MINUS);
					joinTree(new Element(MINUS.getType()).setText(MINUS.getValue()), father);
				}else if(wordTop.getValue().equals("*")){
					 analyseStack.push(MULTI);
					 joinTree(new Element(MULTI.getType()).setText(MULTI.getValue()), father);
				}else if(wordTop.getValue().equals("/")){
					analyseStack.push(DIV);
					joinTree(new Element(DIV.getType()).setText(DIV.getValue()), father);
				}else if(wordTop.getValue().equals(">")){
					analyseStack.push(GREATER);
					joinTree(new Element(GREATER.getType()).setText(GREATER.getValue()), father);
				}else if(wordTop.getValue().equals("<")){
					analyseStack.push(SMALLER);
					joinTree(new Element(SMALLER.getType()).setText(SMALLER.getValue()), father);
				}else if(wordTop.getValue().equals("==")){
					analyseStack.push(IF_EQUAL);
					joinTree(new Element(IF_EQUAL.getType()).setText(IF_EQUAL.getValue()), father);
				}else if(wordTop.getValue().equals("!=")){
					analyseStack.push(NONEQ);
					joinTree(new Element(NONEQ.getType()).setText(NONEQ.getValue()), father);
				}else if(wordTop.getValue().equals(">=")){
					analyseStack.push(GR_EQ);
					joinTree(new Element(GR_EQ.getType()).setText(GR_EQ.getValue()), father);
				}else if(wordTop.getValue().equals("<=")){
					analyseStack.push(SM_EQ);
					joinTree(new Element(SM_EQ.getType()).setText(SM_EQ.getValue()), father);
				}else{
					errorFlag = true;
				}
				break;
			case "RE_CONTENT":
				if(wordTop.isConstant()){
					joinTree(new Element("constant").setText(wordTop.getValue()),father);
					inputWords.remove(0);
				}else if(wordTop.getType().equals("identifier")){
					Element ex_re = new Element(EX_RE.getName());
					Element id = new Element(ID.getName());
					analyseStack.push(EX_RE);
					elementStack.push(ex_re);
					analyseStack.push(ID);
					elementStack.push(id);
					joinTree(id, father);
					joinTree(ex_re, father);
				}else{
					errorFlag = true;
				}
				break;
			case "EX_RE_CONTENT":
				if(wordTop.getValue().equals(";")){
					analyseStack.push(SEMI);
					joinTree(new Element(SEMI.getType()).setText(SEMI.getValue()), father);
				}else if(wordTop.isOperator()){
					Element id = new Element(ID.getName());
					Element op = new Element(OP.getName());
					analyseStack.push(SEMI);
					analyseStack.push(ID);
					elementStack.push(id);
					analyseStack.push(OP);
					elementStack.push(op);
					joinTree(op, father);
					joinTree(id, father);
					joinTree(new Element(SEMI.getType()).setText(SEMI.getValue()), father);
				}else{
					errorFlag = true;
				}
				break;
			case "ID":
				if(!analyseTop.isTerminal() && wordTop.getType().equals("identifier")){
					AnalyseWord id = new AnalyseWord("ID",wordTop.getValue(),"identifier",true);
					analyseStack.push(id);
					joinTree(new Element("identifier").setText(wordTop.getValue()), father);
				}else if(analyseTop.isTerminal() && wordTop.getType().equals("identifier")){
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			case "INT":
				if(wordTop.getValue().equals("int")){
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			case "SHORT":
				if(wordTop.getValue().equals("short")){
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			case "CHAR":
				if(wordTop.getValue().equals("char")){
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			case "FLOAT":
				if(wordTop.getValue().equals("short")){
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			case "DOUBLE":
				if(wordTop.getValue().equals("double")){
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			case "ADD":
				if(wordTop.getValue().equals("+")){
					inputWords.remove(0);
				}else {
					errorFlag = true;
				}
				break;
			case "MINUS":
				if(wordTop.getValue().equals("-")){
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			case "MULTI":
				if(wordTop.getValue().equals("*")){
					inputWords.remove(0);
				}else {
					errorFlag = true;
				}
				break;
			case "DIV":
				if(wordTop.getValue().equals("/")){
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			case "EQUAL":
				if(wordTop.getValue().equals("=")){
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			case "GREATER":
				if(wordTop.getValue().equals(">")){
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			case "SMALLER":
				if(wordTop.getValue().equals("<")){
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			case "IF_EQUAL":
				if(wordTop.getValue().equals("==")){
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			case "NONEQ":
				if(wordTop.getValue().equals("!=")){
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			case "GR_EQ":
				if(wordTop.getValue().equals(">=")){
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			case "SM_EQ":
				if(wordTop.getValue().equals("<=")){
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			case "LBR":
				if(wordTop.getValue().equals("{")){
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			case "RBR":
				if(wordTop.getValue().equals("}")){
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			case "LPAR":
				if(wordTop.getValue().equals("(")){
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			case "RPAR":
				if(wordTop.getValue().equals(")")){
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			case "RETURN":
				if(wordTop.getValue().equals("return")){
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			case "SEMI":
				if(wordTop.getValue().equals(";")){
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			case "COMMA":
				if(wordTop.getValue().equals(",")){
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			case "IF":
				if(wordTop.getValue().equals("if")){
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			case "ELSE":
				if(wordTop.getValue().equals("else")){
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			case "WHILE":
				if(wordTop.getValue().equals("while")){
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			case "#":
				if(wordTop.getValue().equals("#")){
					inputWords.remove(0);
				}else{
					errorFlag = true;
				}
				break;
			
				
			default:
				errorFlag = true;
				break;
			}
			
		}
		
	}
	
	public void outputResult(String outPath){
		if(errorFlag == true){
			System.out.println("parse error");
			System.out.println(inputWords.get(0).getValue());
			root = new Element("ParseResult");
			Element error = new Element("Error");
			error.addContent(new Element("info").setText("parse error"));
			error.addContent(new Element("location").setText(String.valueOf(inputWords.get(0).getNumber())));
			error.addContent(new Element("value").setText(inputWords.get(0).getValue()));
			root.addContent(error);
			org.jdom.Document doc = new org.jdom.Document(root);
			Format format = Format.getPrettyFormat();
			XMLOutputter outputter = new XMLOutputter(format);
			try {
				outputter.output(doc, new FileOutputStream(outPath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			if(analyseStack.empty()&& inputWords.isEmpty()){
				org.jdom.Document doc = new org.jdom.Document(root);
				Format format = Format.getPrettyFormat();
				XMLOutputter outputter = new XMLOutputter(format);
				try {
					outputter.output(doc, new FileOutputStream(outPath));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException{
		Parser parser = new Parser("inputParser.xml");
		parser.startParse();
		parser.outputResult("outputParser.xml");
		
	}
	
	
	

}
