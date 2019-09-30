import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


public class Compiler{

    public static int code;
    public static int value;
    public static class Record{
	String name = "", index = "";
	int type, value, size,elType,newValue,newType;
	boolean used = false;

	public Record(){
	    name = "";
	    type = value = size = elType;
	    newValue = newType = 0;
	}

	public Record(String n, int t, int v){
	    name = n;
	    type = t;
	    value = v;
	    elType = 0;
	    size = 0;
	}

	@Override
	public String toString(){
	    String pr = "";
	    if(name.length() < 8){
	        pr += name + "\t\t ";
		if(newType != 0)
		    pr += newType + "\t ";
		else
		    pr += type + "\t ";
		if(type != RES)
		    pr += newValue + "\t ";
		else
		    pr += value + "\t ";
		pr += size + "\t " + elType;
		return pr;
	    }
	    else
		pr += name + "\t ";
	    if(newType != 0)
		pr += newType + "\t ";
	    else
		pr += type + "\t ";
	    if(newValue != 0)
		pr += newValue + "\t ";
	    else
		pr += value + "\t ";
	    pr += size + "\t " + elType;
	    return pr;
	}
    }
    
    static final int INT = 101;    //integer
    static final int INC = 102;    //integer constant
    static final int INV = 103;    //integer variable
    static final int CHAR = 107;   //character
    static final int CHAC = 108;   //character constant
    static final int CHAV = 109;   //character variable
    static final int arrayV = 110;     //array variable

    static final int RES = 111;   //reserved words
    static final int Int = 1;
    static final int Char = 2;
    static final int Const = 3;
    static final int If = 4;
    static final int Else = 5;
    static final int While = 6;
    static final int For = 7;
    static final int Void = 8;
    static final int Main = 9;
    static final int Include = 10;
    static final int IOStream = 11;
    static final int Endl = 12;
    static final int CIn = 13;
    static final int COut = 14;
    static final int GoTo = 15;
    static final int Return = 16;
    static final int Using = 17;
    static final int Namespace = 18;
    static final int Std = 19;
        
    static final int OP = 112;    //operators
    static final int Mod = 37;    //%
    static final int Mul = 42;    //*
    static final int Add = 43;    //+
    static final int Sub = 45;    //-
    static final int Div = 47;    ///
    static final int Incr = 208;  //++
    static final int Decr = 209;  //--
    static final int GT = 62;     //>
    static final int EQ = 61;     //=
    static final int LT = 60;     //<
    static final int EQT = 203;   //==
    static final int NE = 200;    //!=
    static final int LE = 201;    //<=
    static final int GE = 202;    //>=
    static final int Neg = 33;    //!
    static final int and = 38;   //&
    static final int or = 124;    //|
    static final int AND = 205;   //&&
    static final int OR = 204;    //||
    static final int IN = 206;    //>>
    static final int OUT = 207;   //<<
    
    static final int PUN = 113;   //punctuation
    static final int Quote = 34;  //"
    static final int Pound = 35;  //#
    static final int SQuote = 39; //'
    static final int LParen = 40; //(
    static final int RParen = 41; //)
    static final int Comma = 44;  //,
    static final int Period = 46; //.
    static final int SemiCol = 59; //;
    static final int LBrak = 91;   //[
    static final int RBrak = 93;   //]
    static final int LBrac = 123;  //{
    static final int RBrac = 125;  //}

    public static int hashVal(String tok){
	return tok.charAt(0) % 20;
    }
    public static  ArrayList<Record>[] symTable = new ArrayList[20];
    public static class LexicalAnalyzer {


	public static ArrayList[] createSymTable(FileInputStream src, PrintWriter pw) throws IOException{

	    for(int i = 0; i < symTable.length; i++)
		symTable[i] = new ArrayList();
	    symTable[hashVal("int")].add(new Record("int", RES, Int));
	    symTable[hashVal("char")].add(new Record("char", RES, Char));
	    symTable[hashVal("const")].add(new Record("const", RES, Const));
	    symTable[hashVal("if")].add(new Record("if", RES, If));
	    symTable[hashVal("else")].add(new Record("else", RES, Else));
	    symTable[hashVal("while")].add(new Record("while", RES, While));
	    symTable[hashVal("for")].add(new Record("for", RES, For));
	    symTable[hashVal("void")].add(new Record("void", RES, Void));
	    symTable[hashVal("main")].add(new Record("main", RES, Main));
	    symTable[hashVal("include")].add(new Record("include", RES, Include));
	    symTable[hashVal("iostream")].add(new Record("iostream", RES, IOStream));
	    symTable[hashVal("endl")].add(new Record("endl", RES, Endl));
	    symTable[hashVal("cin")].add(new Record("cin", RES, CIn));
	    symTable[hashVal("cout")].add(new Record("cout", RES, COut));
	    symTable[hashVal("goto")].add(new Record("goto", RES, GoTo));
	    symTable[hashVal("return")].add(new Record("return", RES, Return));
	    symTable[hashVal("using")].add(new Record("using", RES, Using));
	    symTable[hashVal("namespace")].add(new Record("namespace", RES, Namespace));
	    symTable[hashVal("std")].add(new Record("std", RES, Std));
	    char ch = ' ';
	    char prev = ch;
	    boolean posID = false, posCom = false, ws = false, posInt = false, incCount = false, posInc = false, posDec = false,
		posEQ = false, posGTE = false, posLTE = false, posOR = false, posAND = false, posNeg = false, printTmp = false,
		stringPr = false, posChar = false, posOUT = false, posIN = false, posCom2 = false, posEndCom = false;
	    StringBuilder tmpName = new StringBuilder();
	    StringBuilder tmpInt = new StringBuilder();
	    int stringCount = 0;
	    int length = src.available();
	    int print = 1;
	    int idCount = 0;
	    String tmpName2 = "";
	    String tmpName3 = "";
	    int tmpType = 0, tmpType2 = 0;
	    int tmpValue = 0, tmpValue2 = 0;
	    for(int i = 0; i < length; i++){
		Record tmp = new Record();
		Record tmp2 = new Record();
		prev = ch;
		ch = (char)src.read();
		if(ch == '\n' || ch == '\t');
	    
		else if(ch == '/'){
		    tmpName2 += ch;
		    tmpType = OP;
		    tmpValue = ch;
		    if(posCom == true){
			ch = (char)src.read();
			while(ch != '\n'){
			    ch = (char)src.read();
			}
			ch = (char)src.read();
			posCom = false;
			tmpName2 = "";
			tmpType = 0;
			tmpValue = 0;
		    }
		    else{
			posCom = true;
			posCom2 = true;
		    }						  
		}
	    
		else if(posCom == true && ch != '/'){
		    if(ch != '*'){
			posCom2 = false;
			printTmp = true;
		    }
		    posCom = false;
		}
		    
		if(ch == '*' && posCom2 == true){
		    while(posCom2 == true){
			ch = (char)src.read();
			while(true){
			    ch = (char)src.read();
			    if(ch == '*')
				posEndCom = true;
			    if(ch == '/' && posEndCom == true){
				posCom2 = false;
				posEndCom = false;
				break;
			    }
			}
		    }
		    tmpName2 = "";
		    tmpType = 0;
		    tmpValue = 0;
	         
		}
		else if(ch == '"'){
		    pw.println(PUN + "\t" + Quote);
		    ch = (char)src.read();
		    while(ch != '"'){
			tmpName.append(ch);
			ch = (char)src.read();
		    }
		    pw.println(PUN + "\t" + Quote);
		    tmp.name = tmpName.toString();
		    tmpName = new StringBuilder();
		    symTable[hashVal(tmp.name)].add(tmp);
		    tmp.type = tmp.name.charAt(0);
		    tmp.newValue = stringCount++;
		
		}
		else if(posChar == true && Character.isLetter(ch)){
		    tmp2.name += ch;
		    tmp2.type = CHAR;
		    tmp2.value = ch;
		
		}
		else if(ch == '\''){
		    pw.println(PUN + "\t" + SQuote);
		    if(posChar == true)
			posChar = false;
		    else
			posChar = true;
		}
		else if(Character.isLetter(ch) || ch == '_'){
		    posID = true;
		    posCom = false;
		    tmpName.append(ch);
		}
		else if(Character.isDigit(ch) && posID == true){
		    tmpName.append(ch);
		}
		else if(Character.isDigit(ch) && posID == false && posInt == false){
		    posInt = true;
		    tmpInt.append(ch);
		}
		else if(Character.isDigit(ch) && posInt == true){
		    tmpInt.append(ch);
		}

		else if(!Character.isDigit(ch) && posInt == true){
		    tmp.type = INT;
		    tmp.value = Integer.parseInt(tmpInt.toString());
		    posInt = false;
		    if(print % 4 != 0){
			if(tmp.name.length() >= 8)
			    pw.println(tmp.type + "\t" + tmp.value);
			else
			    pw.println(tmp.type + "\t" + tmp.value);
			print++;
		    }
		    else if(print % 4 == 0){
			pw.println(tmp.type + "\t" + tmp.value);
			print++;
		    }
		    tmpInt = new StringBuilder();
		}
		else if(!(Character.isLetter(ch) || ch == '_' || Character.isDigit(ch)) && posID == true){
		    boolean exists = false;
		    tmp.name = tmpName.toString();
		    if(!symTable[hashVal(tmp.name)].isEmpty() && tmp.name != ""){
			for(int k = 0; k < symTable[hashVal(tmp.name)].size(); k++){
			    if(symTable[hashVal(tmp.name)].get(k).name.equals(tmp.name)){
				if(symTable[hashVal(tmp.name)].get(k).type == RES){
				    tmp.type = symTable[hashVal(tmp.name)].get(k).type;
			
				}
				tmp.value = symTable[hashVal(tmp.name)].get(k).value;
				exists = true;
				incCount = false;
				break;
			    }
			    else{
				tmp.type = hashVal(tmp.name);
				incCount = true;
			    }
			}
		    }
		    if(incCount == true)
			tmp.value = idCount++;
		    if(print % 4 != 0 && tmp.name != "" && printTmp == false){
			if(tmp.name.length() >= 8){
			    if(tmp.newValue == 0)
				pw.println(tmp.type + "\t" + tmp.value);
			    else
				pw.println(tmp.type + "\t" + tmp.newValue);
			}	
			else{
			    if(tmp.newValue == 0)
				pw.println(tmp.type + "\t" + tmp.value);
			    else
				pw.println(tmp.type + "\t" + tmp.newValue);
			}			
			print++;			
			stringPr = true;
		    }
		    else if(print % 4 == 0 && tmp.name != "" && printTmp == false){
			if(tmp.newValue == 0)
			    pw.println(tmp.type + "\t" + tmp.value);
			else
			    pw.println(tmp.type + "\t" + tmp.newValue);
			print++;
			stringPr = true;
		    }
	     	
		    if(exists == false)
			symTable[hashVal(tmp.name)].add(tmp);
		    tmpName = new StringBuilder();
		    posID = false;
		}
		if(ch == Mod){
		    tmp2.name += ch;
		    tmp2.type = OP;
		    tmp2.value = Mod;
		}
		else if(ch == Mul){
		    tmp2.name += ch;
		    tmp2.type = OP;
		    tmp2.value = Mul;
		}
		else if(ch == Add){
		    tmpName2 += ch;
		    tmpType = OP;
		    tmpValue = Add;
		    if(posInc == true){
			tmp2.name = "++";
			tmp2.type = OP;
			tmp2.value = Incr;
			tmpName2 = "";
			tmpType = 0;
			tmpValue = 0;
			posInc = false;
		    }
		    else
			posInc = true;
		}
		else if(ch == Sub){
		    tmpName2 += ch;
		    tmpType = OP;
		    tmpValue = Sub;
		    if(posDec == true){
			tmp2.name = "--";
			tmp2.type = OP;
			tmp2.value = Decr;
			tmpName2 = "";
			tmpType = 0;
			tmpValue = 0;
			posDec = false;
		    }
		    else
			posDec = true;
		}
		else if(ch == GT){
		    if(tmpName2.equals("<")){
			tmpName3 += ch;
			tmpType2 = OP;
			tmpValue2 = GT;
		    }
		    else{
			tmpName2 += ch;
			tmpType = OP;
			tmpValue = GT;
		    }
		    if(posIN == true){
			tmp2.name = ">>";
			tmp2.type = OP;
			tmp2.value = IN;
			posIN = false;
			tmpName2 = "";
			tmpType = 0;
			tmpValue = 0;
		    }
		    else{
			posGTE = true;
			posIN = true;
		    }
		}
		else if(ch == LT){
		    tmpName2 += ch;
		    tmpType = OP;
		    tmpValue = LT;
		    if(posOUT == true){
			tmp2.name = "<<";
			tmp2.type = OP;
			tmp2.value = OUT;
			tmpName2 = "";
			tmpType = 0;
			tmpValue = 0;
			posOUT = false;
		    }
		    else{
			posOUT = true;
			posLTE = true;
		    }
		}
		else if(ch == Neg){
		    tmpName2 += ch;
		    tmpType = OP;
		    tmpValue = Neg;
		    if(posNeg == true){
			tmp2.name = "!=";
			tmp2.type = OP;
			tmp2.value = NE;
			tmpName2 = "";
			tmpType = 0;
			tmpValue = 0;
			posNeg = false;
		    }
		    else
			posNeg = true;
		}
		else if(ch == EQ){
		    tmpName2 += ch;
		    tmpType = OP;
		    tmpValue = EQ;
		    if(posEQ == true){
			tmp2.name = "==";
			tmp2.type = OP;
			tmp2.value = EQT;
			tmpName2 = "";
			tmpType = 0;
			tmpValue = 0;
			posEQ = false;
		    }
		    if(posLTE == true){
			tmp2.name = "<=";
			tmp2.type = OP;
			tmp2.value = LE;
			tmpName2 = "";
			tmpType = 0;
			tmpValue = 0;
			posLTE = false;
		    }
		     if(posGTE == true){
		    	tmp2.name = ">=";
		    	tmp2.type = OP;
		   	tmp2.value = GE;
		    	tmpName2 = "";
		    	tmpType = 0;
		    	tmpValue = 0;
		    	posGTE = false;
		    }
		    else{
			posEQ = true;
		    }
		}
		else if(ch == Pound){
		    tmp2.name += ch;
		    tmp2.type = PUN;
		    tmp2.value = Pound;
		}
		else if(ch == LParen){
		    tmp2.name += ch;
		    tmp2.type = PUN;
		    tmp2.value = LParen;
		}
		else if(ch == RParen){
		    tmp2.name += ch;
		    tmp2.type = PUN;
		    tmp2.value = RParen;
		}
		else if(ch == Comma){
		    tmp2.name += ch;
		    tmp2.type = PUN;
		    tmp2.value = Comma;
		}
		else if(ch == Period){
		    tmp2.name += ch;
		    tmp2.type = PUN;
		    tmp2.value = Period;
		}
		else if(ch == SemiCol){
		    tmp2.name += ch;
		    tmp2.type = PUN;
		    tmp2.value = SemiCol;
		}
		else if(ch == LBrak){
		    tmp2.name += ch;
		    tmp2.type = PUN;
		    tmp2.value = LBrak;
		}
		else if(ch == RBrak){
		    tmp2.name += ch;
		    tmp2.type = PUN;
		    tmp2.value = RBrak;
		}
		else if(ch == LBrac){
		    tmp2.name += ch;
		    tmp2.type = PUN;
		    tmp2.value = LBrac;
		}
		else if(ch == RBrac){
		    tmp2.name += ch;
		    tmp2.type = PUN;
		    tmp2.value = RBrac;
		}
		else if(ch == and){
		    tmpName2 += ch;
		    tmpType = OP;
		    tmpValue = ch;
		    if(posAND == true){
			tmp2.name = "&&";
			tmp2.type = OP;
			tmp2.value = AND;
			tmpName2 = "";
			tmpType = 0;
			tmpValue = 0;
			posAND = false;
		    }
		    else
			posAND = true;
		}
		else if(ch == or){
		    tmpName2 += ch;
		    tmpType = OP;
		    tmpValue = ch;
		    if(posOR == true){
			tmp2.name = "||";
			tmp2.type = OP;
			tmp2.value = OR;
			tmpName2 = "";
			tmpType = 0;
			tmpValue = 0;
			posOR = false;
		    }
		    else
			posOR = true;
		}

		if(posInc == true && ch != Add){
		    posInc = false;
		    printTmp = true;
		}
		if(posDec == true && ch != Sub){
		    posDec = false;
		    printTmp = true;
		}
		if(posEQ == true && ch != EQ){
		    posEQ = false;
		    printTmp = true;
		}
		if(posGTE == true && (ch != EQ && ch != GT)){
		    posGTE = false;		    
		    if(ch != GT){
			printTmp = true;
			posIN = false;
		    }
		}
		if(posIN == true && ch != GT){
		    posIN = false;
		    if(ch != EQ){
			printTmp = true;
			posGTE = false;
		    }
		}
		if(posOUT == true && ch != LT){
		    posOUT = false;
		    if(ch != EQ){
			printTmp = true;
			posLTE = false;
		    }
		}
		if(posLTE == true && (ch != EQ && ch != LT)){
		  posLTE = false;
		  if(ch != LT){
			printTmp = true;
			posOUT = false;
		    }
		}
		if(posAND == true && ch != and){
		    posAND = false;
		    printTmp = true;
		}
		if(posOR == true && ch != or){
		    posOR = false;
		    printTmp = true;
		}
		if(posNeg == true && ch != Neg){
		    posNeg = false;
		    printTmp = true;
		}
		if(print % 4 != 0 && tmp.name != "" && printTmp == false && stringPr == false){
		    if(tmp.name.length() >= 8){
			if(tmp.newValue == 0)
			    pw.println(tmp.type + "\t" + tmp.value);
			else
			    pw.println(tmp.type + "\t" +tmp.newValue);
		    }
		    else{
			if(tmp.newValue == 0)
			    pw.println(tmp.type + "\t" + tmp.value);
			else
			    pw.println(tmp.type + "\t" + tmp.newValue);
		    }
		    print++;
		}
		else if(print % 4 == 0 && tmp.name != "" && printTmp == false && stringPr == false){
		    if(tmp.newValue == 0)
			pw.println(tmp.type + "\t" + tmp.value);
		    else
			pw.println(tmp.type + "\t" + tmp.newValue);
		    print++;
		}
		if(printTmp == true){
		    if(print % 4 != 0 && tmpName2 != ""){
			if(tmpName2.length() >= 8)
			    pw.println(tmpType + "\t" + tmpValue);
			else
			    pw.println(tmpType + "\t" + tmpValue);
			print++;
		    }
		    else if(print % 4 == 0 && tmpName2 != ""){
			pw.println(tmpType + "\t" + tmpValue);
			print++;
		    }
		    if(tmpName3 != ""){
			if(print % 4 != 0 && tmpName3 != ""){
			    if(tmpName2.length() >= 8)
				pw.println(tmpType2 + "\t" + tmpValue2);
			    else
				pw.println(tmpType2 + "\t" + tmpValue2);
			    print++;
			}
			else if(print % 4 == 0 && tmpName3 != ""){
			    pw.println(tmpType2 + "\t" + tmpValue2);
			    print++;
			}
		    }
		    tmpType = 0;
		    tmpValue = 0;
		    tmpName2 = "";
		    tmpName3 = "";
		    printTmp = false;
		}
		if(print % 4 != 0 && tmp2.name != ""){
		    if(tmp2.name.length() >= 8)
			pw.println(tmp2.type + "\t" + tmp2.value);
		    else
			pw.println(tmp2.type + "\t" + tmp2.value);
		    print++;
		}
		else if(print % 4 == 0 && tmp2.name != ""){
		    pw.println(tmp2.type + "\t" + tmp2.value);
		    print++;
		}
		if(stringPr == true)
		    stringPr = false;

	    }
	    pw.close();	    
	    return symTable;
	}
    }
    public static class Parser{
	public static void createParseTree(Scanner a){
	    boolean constID = false, constIntID = false, constCharID = false, intID = false, charID = false, exists = false,
		exists2 = false, assign = false, assigned = false, list = false, endAssign = false, posArray = false,
		idTest = false, posMainMethod = false, arraySized = false, idNameTest = false, posIllType = false, mustAssign = false;
	    int tmpType = 0, tmpPos = 0, pos = 0;
	    while(a.hasNext()){
		String check = a.next();
		if(check.equals("SymbolTable"))
		    break;
		int type = Integer.parseInt(check);
		int value = Integer.parseInt(a.next());
		if(type == RES && value == Main)
		    break;
		if(arraySized && value != RBrak){
		    System.out.println("ERROR: Array Declaration Error");
		}
		if(type == RES){
		    if(idTest){
			if(value == LParen && posMainMethod){
			    idTest = false;
			    posMainMethod = false;
			}
			if(value == Main){
			    posMainMethod = true;
			}
			else if(value == Return)
			    idTest = false;
			else
			    System.out.println("ERROR: Reserved word cannot be used as variable");
		    }
		    idNameTest = true;
		    if(value == Const){
			constID = true;
		    }
		    else if(value == Int && constID == false){
			intID = true;
			idTest = true;
		    }
		    else if(value == Int && constID == true){
			constIntID = true;
			constID = false;
			intID = false;
			idTest = true;
			mustAssign = true;
		    }
		    else if(value == Char && constID == false){
			charID = true;
			idTest = true;
		    }
		    else if(value == Char && constID == true){
			constCharID = true;
			constID = false;
			charID = false;
			idTest = true;
			mustAssign = true;
		    }
		    else
			posIllType = true;
		    endAssign = false;
		}
		else{
		    posIllType = false;
		    if(assigned == true){
			if(value != EQ && value != Comma && value != SemiCol){
			    constIntID = constCharID = intID = charID = false;
			    System.out.println("ERROR: Missing semicolon/comma/equal in var declaration");
			}
			else if(value == Comma){
			    list = true;
			}
			else if(value == SemiCol){
			    if(mustAssign){
				System.out.println("ERROR: Must assign const a value");
				mustAssign = false;
			    }
			    list = false;
			    constIntID = constCharID = intID = charID = false;
			    endAssign = true;
			}
			assigned = false;
		    }
		    if(posArray == true){
			if(type == INT){
			    symTable[tmpType].get(tmpPos).size = value;
			    symTable[tmpType].get(tmpPos).newType = arrayV;
			}
			if(intID)
			    symTable[tmpType].get(tmpPos).elType = INV;
			if(charID)
			    symTable[tmpType].get(tmpPos).elType = CHAV;
			posArray = false;
			arraySized = true;
		    }
		    if(type != OP && type != PUN && type != INT && type != CHAR && assign == false){
			for(int i = 0; i < symTable[type].size(); i++){
			    if(symTable[type].get(i).type != RES){
				if(symTable[type].get(i).value == value){
				    if(idTest)
					idTest = false;
				    if(idNameTest){
					if(symTable[type].get(i).used)
					    System.out.println("ERROR: Variable name already declared");
					idNameTest = false;
				    }
				    if(posIllType){
					System.out.println("ERROR: Illegal type");
				    }
				    if(mustAssign)
					mustAssign = false;
				    posIllType = false;
				    symTable[type].get(i).used = true;
				    pos = i;
				    exists = true;
				    break;
				}
			    }
			    else
				continue;
			}
		    }
		    else if(type == OP || type == PUN){
			if(value == EQ && (constIntID == true || constCharID == true || intID == true || charID == true)){
			    assign = true;
			}
			else if(value == LBrak){
			    posArray = true;
			}
			if(arraySized && value == RBrak)
			    arraySized = false;
		    }
		    if(exists == true){
			tmpType = type;
			tmpPos = pos;
			if(constIntID == true){
			    symTable[type].get(pos).newType = INC;
			}
			else if(constCharID == true){
			    symTable[type].get(pos).newType = CHAC;
			}
			else if(intID == true && charID == false){
			    symTable[type].get(pos).newType = INV;
			}
			else if(charID == true){
			    symTable[type].get(pos).newType = CHAV;
			}
			exists = false;
		    }
		    if(assign == true && type != PUN && type != OP){
			if(type != OP && type != PUN && type != INT && type != CHAR){
			    for(int j = 0; j < symTable[type].size(); j++){
				if(symTable[type].get(j).type != RES){
				    if(symTable[type].get(j).value == value){
					pos = j;
					exists2 = true;
					break;
				    }
				}
				else
				    continue;
			    }
			}
			if(exists2 == true){
			    if(symTable[type].get(pos).newValue > 0){
				symTable[tmpType].get(tmpPos).newValue = symTable[type].get(pos).newValue;
			    }
			    else
				symTable[tmpType].get(tmpPos).newValue = symTable[type].get(pos).value;
			    exists2 = false;
			}
			else
			    symTable[tmpType].get(tmpPos).newValue = value;
			assign = false;
			assigned = true;
		    }
		}
	    }
	}
    }
    public static class ParserTwo{
        public static ParserThree p3;
	public static int tmpCount;
	public static String tmpS = "", arr = "";
	public static boolean neg = false, lp = false, f = false, f2 = false, f3 = false, q = false, sq = false;
	public static PrintWriter pw;
	public ParserTwo(Scanner a, PrintWriter pw){
	    this.pw = pw;
	    p3 = new ParserThree(a,pw);
	    parseAssign(a);
	}
	public static String newTmp(){
	    StringBuilder tmp = new StringBuilder("t_");
	    tmp.append(tmpCount++);
	    return tmp.toString();
	}
	public static void parseAssign(Scanner a){
	    Record arg = new Record();
	    Record index = new Record();
	    while(code != RES || value != Main){
		code = a.nextInt();
	    	value = a.nextInt();
	    }
	    for(int i = 0; i < 4; i++){
		code = a.nextInt();
		value = a.nextInt();
	    }
	    while(a.hasNext()){
		String print = "";
		if(code == RES && value == CIn){
		    while(a.hasNext()){
			code = a.nextInt();
			value = a.nextInt();
			if(code == OP && value == IN){
			    code = a.nextInt();
			    value = a.nextInt();
			}
			else{
			    code = a.nextInt();
			    value = a.nextInt();
			    break;
			}

			if(code <= symTable.length){
			    for(int i = 0 ; i < symTable[code].size(); i++){
				if(symTable[code].get(i).type == code && symTable[code].get(i).value == value){
				    pw.println("cin\t" + symTable[code].get(i).name);
				    break;
				}
			    }
			}
		    }
		    
		}
		else if(code == RES && value == Return)
		    break;
		else if(code == RES && value == If){
		    //System.out.println("IF");
		    p3.ifExp(a);
		    if(p3.nestCnt > 1){
			pw.println(p3.rootEnd);
		    }
		    if(code == PUN && value == RBrac){
			code = a.nextInt();
			value = a.nextInt();
		}
		    p3.nestCnt = 0;
		        
		}
		else if(code == RES && value == While){
		    //System.out.println("WHILE");
		    p3.whileExp(a);
		}

		else if(code == PUN && value == RBrac){
		    if(p3.bElse){
			pw.println(p3.root.end);
			p3.bElse = false;
		    }
		    code = a.nextInt();
		    value = a.nextInt();
		}
		else if((code == RES && value == COut) || f){
		    if(!f){
			code = a.nextInt();
			value = a.nextInt();
		    }
		    f2 = true;
		    while(a.hasNext()){
			if(!f && value != OUT){
			    code = a.nextInt();
			    value = a.nextInt();
			}
			if(code == PUN && value == SemiCol){
			    	code = a.nextInt();
			    	value = a.nextInt();
				break;
			     }
			f = false;
			if(code == OP && value == OUT){
			    code = a.nextInt();
			    value = a.nextInt();
			}
			if(code == INT){
			    exp(arg,a);
			    pw.println("cout\t" + arg.name);
			}
			if((code == PUN && value == Quote) || (code == PUN && value == SQuote)){
			    print += (char)value;
			    if(value == Quote)
				q = true;
			    if(value == SQuote)
				sq = true;
			    code = a.nextInt();
			    value = a.nextInt();
			    code = a.nextInt();
			    value = a.nextInt();
			    for(int i = 0 ; i < symTable[code%20].size(); i++){
				if(symTable[code%20].get(i).type == code && symTable[code%20].get(i).newValue == value){
				    print += symTable[code%20].get(i).name;
				}
		        
			    }
			    if(q){
				print += (char)Quote;
				q = false;
			    }
			    if(sq){
				print += (char)SQuote;
				sq = false;
			    }
			    pw.println("cout\t" + print);
			    print = "";;
			    
			}
			else if(code <= symTable.length){
			    for(int i = 0 ; i < symTable[code].size(); i++){
				if(symTable[code].get(i).value == value && symTable[code].get(i).type != RES){
				    if(symTable[code].get(i).newType != arrayV)
					pw.println("cout\t" + symTable[code].get(i).name);
				    else{
				       	arr = symTable[code].get(i).name;
					code = a.nextInt();
					value = a.nextInt();
					code = a.nextInt();
					value = a.nextInt();
					exp(index,a);
					arg.name = newTmp();
					pw.println("=\t" + index.name + "\t\t" + arg.name);
					index.name = arg.name;
					arg.name = newTmp();
					pw.println("=[]\t" + arr + "\t" + index.name + "\t" + arg.name);
					pw.println("cout\t" + arg.name);
				       	code = a.nextInt();
					value = a.nextInt();
					if(code > symTable.length){
					    break;
					}
				    }
				}
			    }
			}
			
			else if(code == RES && value == Endl)
			    pw.println("cout\t" + "'\\n'");
			else if(value != OUT){
			    break;
			}
		    }
		}
		else{
		    assignment(a);
		}
		if((code != PUN || value != SemiCol) && !f2){
		    f2 = false;
		}
		tmpCount = 0;
	    }
	}
	public static void assignment(Scanner a){
	    if(a.hasNext()){
		Record arg = new Record();
		Record index = new Record();
		Record index2 = new Record();
		Record p = null;
		if(code <= symTable.length){
		    for(int i = 0 ; i < symTable[code].size(); i++){
			if(symTable[code].get(i).value == value)
			    p = symTable[code].get(i);
		    }
		}
		if(p.newType == arrayV){
		    arr = p.name;
		    code = a.nextInt();
		    value = a.nextInt();
		    code = a.nextInt();
		    value = a.nextInt();
		    exp(index,a);
		    arg.name = newTmp();
		    pw.println("=\t" + index.name + "\t\t" + arg.name);
		    index.name = arg.name;
		}
		code = a.nextInt();
		value = a.nextInt();
		if(code != OP || value != EQ)
		    pw.println("Missing assignment operator");
		else{
		    code = a.nextInt();
		    value = a.nextInt();
		}
		exp(arg, a);
		if(code == PUN && value == SemiCol){
		    code = a.nextInt();
		    value = a.nextInt();
		}
		if(p.newType == arrayV)
		    pw.println("[]=\t" + arg.name + "\t" + index.name + "\t" + p.name);
		else
		    pw.println("=\t" + arg.name + "\t\t" + p.name);
	    
	    }
	}
	public static void exp(Record result, Scanner a){
	    Record arg1 = new Record();
	    Record arg2 = new Record();
	    int op;
	    term(arg1,a);
	    while((code == OP && value == Add) || (code == OP && value == Sub)){
		op = value;
	       	code = a.nextInt();
	       	value = a.nextInt();
		if(value == Sub)
		    neg = true;
		if(!arg1.name.equals("") || !result.name.equals(""))
		    term(arg2,a);
		else
		    term(arg1,a);
		result.name = newTmp();
		tmpS = result.name;
		if(op == Add)
		    pw.println("+\t" + arg1.name + "\t" + arg2.name + "\t" + result.name);
		else if(op == Sub)
		    pw.println("-\t" + arg1.name + "\t" + arg2.name + "\t" + result.name);
		arg1.name = result.name;
	    }
	    result.name = arg1.name;
	    result.type = arg1.type;
	}
	public static void term(Record result, Scanner a){
	    Record arg1 = new Record();
	    Record arg2 = new Record();
	    int op;
	    factor(arg1,a);
	    while((code == OP && value == Mul) || (code == OP && value == Div) ||(code == OP && value == Mod)){
		op = value;
	       	code = a.nextInt();
	       	value = a.nextInt();
		factor(arg2,a);
	        result.name = newTmp();
	        if(op == Mul)
		    pw.println("*\t" + arg1.name + "\t" + arg2.name + "\t" + result.name);
		else if(op == Div)
		    pw.println("/\t" + arg1.name + "\t" + arg2.name + "\t" + result.name);
		else if(op == Mod)
		    pw.println("%\t" + arg1.name + "\t" + arg2.name + "\t" + result.name);
	        arg1.name = result.name;
	    }
	    result.name = arg1.name;
	    result.type = arg1.type;
	}
	public static void factor(Record result, Scanner a){
	    Record arg1 = new Record();
	    int op;
	    if(code == PUN && value == LParen){
		code = a.nextInt();
		value = a.nextInt();
	       	exp(arg1,a);
	    }
	    if(code <= symTable.length){
		for(int i = 0 ; i < symTable[code].size(); i++){
		    if(symTable[code].get(i).value == value){
			result.name = symTable[code].get(i).name;
			if(symTable[code].get(i).newType == arrayV)
			    result.type = symTable[code].get(i).newType;
		    }
		        
		}
	    }
	    if(result.type == arrayV){
		code = a.nextInt();
		value = a.nextInt();
		code = a.nextInt();
		value = a.nextInt();
		exp(result,a);
		arg1.name = newTmp();
       		pw.println("=\t" + result.name + "\t\t" + arg1.name);
		result.name = arg1.name;
		arg1.name = newTmp();
		pw.println("=[]\t" + arr + "\t" + result.name + "\t" + arg1.name);
		result.name = arg1.name;
	    }
	    if(value == SQuote){
		code = a.nextInt();
		value = a.nextInt();
	    }
	    if(code == INT || code == CHAR){		
		result.name = Integer.toString(value);

	    }
	    if(code == PUN && value == RParen){
		result.name = arg1.name;
	    }
	    if(neg){
		neg = false;
		code = a.nextInt();
		value = a.nextInt();
		f = true;
		factor(result,a);
		String tmp = newTmp();
		if(result.name.equals(""))
		    result.name = tmpS;
		pw.println("-\t" + result.name + "\t" + arg1.name + "\t" + tmp);
		result.name = tmp;
	    }
	    if(value != Sub && value != Add){
		code = a.nextInt();
		value = a.nextInt();
	    }
	    if(value == SQuote){
		a.nextInt();
		value = a.nextInt();
	    }
	}
    }

    public static class ParserThree{
	public static int code2, value2, lblCnt = 1, tmpCnt = 1, nestCnt;
	public static Node root;
	public static boolean bElse = false, whileInIf = false;
	public static String rootEnd;
	public static PrintWriter pw;
	public ParserThree(Scanner a, PrintWriter pw){  //add printwriter here
	    this.pw = pw;
	    //parseThree(a);
	}
	static class Node{
	    String oper,bFalse,bTrue,quad,eRes,end,begin;
	    Node left,right,root;
	    public Node(){

	    }
	    public Node(String type, Node leftChild){
		eRes = oper = type;
		left = leftChild;
	    }
	    public Node(String type, Node leftChild, Node rightChild){
		eRes = oper = type;
		left = leftChild;
		right = rightChild;
	    }

	    @Override
	    public String toString(){
		return oper;
	    }
	}

	public static String newTmp(){
	    return "t_" + Integer.toString(tmpCnt++);
	}
	public static String newLbl(){
	    return "L_" + Integer.toString(lblCnt++);
	}

	public static void preorder(Node n){
	    if(n != null){
		//visit(n);
		preVisit(n);
		preorder(n.left);
		preorder(n.right);
	    }
	}

	public static void preVisit(Node n){
	    n.root = root;
	    if(n.oper.equals("&&")){
		n.left.bTrue = newLbl();
		n.left.bFalse = n.bFalse;
		n.right.bTrue = n.bTrue;
		n.right.bFalse = n.bFalse;
	    }
	    else if(n.oper.equals("||")){
		n.left.bTrue = n.bTrue;
		n.left.bFalse = newLbl();
		n.right.bTrue = n.bTrue;
		n.right.bFalse = n.bFalse;
	    }
	    else if(n.oper.equals("!")){
		n.left.bTrue = n.bFalse;
		n.left.bFalse = n.bTrue;
	    }
	}

	public static void inVisit(Node n){
	    if(n.oper.equals("&&")){
		pw.println(n.left.bTrue);
	    }
	    else if(n.oper.equals("||")){
		pw.println(n.left.bFalse);
	    }
	    else if(n.oper.equals("<")){
	        pw.println("<\t" + n.left.eRes + "\t" + n.right.eRes + "\tgoto\t" + n.bTrue);
		pw.println("goto\t" + n.bFalse);
	    }
	    else if(n.oper.equals(">")){
	        pw.println(">\t" + n.left.eRes + "\t" + n.right.eRes + "\tgoto\t" + n.bTrue);
		pw.println("goto\t" + n.bFalse);
	    }
	    else if(n.oper.equals("<=")){
	        pw.println("<=\t" + n.left.eRes + "\t" + n.right.eRes + "\tgoto\t" + n.bTrue);
		pw.println("goto\t" + n.bFalse);
	    }
	    else if(n.oper.equals(">=")){
	        pw.println(">=\t" + n.left.eRes + "\t" + n.right.eRes + "\tgoto\t" + n.bTrue);
		pw.println("goto\t" + n.bFalse);
	    }
	    else if(n.oper.equals("==")){
	        pw.println("==\t" + n.left.eRes + "\t" + n.right.eRes + "\tgoto\t" + n.bTrue);
		pw.println("goto\t" + n.bFalse);
	    }
	}
	
	public static void inorder(Node n){
	    if(n != null){
		inorder(n.left);
		//visit(n);
		inVisit(n);
		inorder(n.right);
	    }
	}
	
	public static void visit(Node n){
	    System.out.println(n);
	    System.out.println("LEFT: " + n.left);
	    System.out.println("RIGHT: " + n.right);
	}
	
	public static void parseThree(Scanner a){
	    while(code  != RES || value  != Main){
		code  = a.nextInt();
	    	value  = a.nextInt();
	    }
	    for(int i = 0; i < 4; i++){
		code  = a.nextInt();
		value  = a.nextInt();
	    }

	    while(a.hasNext()){
	        //assignment ??
		if(code  == RES && value  == Return)
		    break;
       		if(code  <= symTable.length){
		    for(int i = 0; i < symTable[code].size(); i++){
			if(symTable[code].get(i).value == value && symTable[code].get(i).type != RES){
			    //System.out.println("ASSIGNMENT");
			    assignment2(a,i);
			    break;
			}
		    }
		}

		//if ??
		else if(code == RES && value == If){
		    //System.out.println("IF");
		    ifExp(a);
		    if(nestCnt > 1){
			pw.println(rootEnd);
		    }
		    if(code == PUN && value == RBrac){
		    code = a.nextInt();
		    value = a.nextInt();
		}
		    nestCnt = 0;
		        
		}

		//while ??

		else if(code == RES && value == While){
		    //System.out.println("WHILE");
		    whileExp(a);
		}

		else if(code == PUN && value == RBrac){
		    if(bElse){
			pw.println(root.end);
			bElse = false;
		    }
		    code = a.nextInt();
		    value = a.nextInt();
		}
	    }
	}

	public static void assignment2(Scanner a, int i){
	    Node n;
	    Record p = symTable[code].get(i);
	    code = a.nextInt();
	    value = a.nextInt();
	    if(code != OP || value != EQ)
		System.out.println("Missing assignment operator");
	    else{
		code = a.nextInt();
		value = a.nextInt();
	    }
	    n = exp2(a);
	    pw.println("=\t" + n.eRes + "\t\t" + p.name);
	    if(code != PUN && value != SemiCol){
		System.out.println("MISSING SEMICOL");
	    }
	    else{
		code = a.nextInt();
		value = a.nextInt();
	    }
	}

	public static void ifExp(Scanner a){
	    Node n = new Node();
	    root = n;
	    code = a.nextInt();
	    value = a.nextInt();
	    if(code != PUN && value != LParen)
		System.out.println("MISSING PAREN");
	    while(code == PUN && value == LParen){
		code = a.nextInt();
		value = a.nextInt();
	    }

	    n = orExp(a);
	    n.bTrue = newLbl();
	    n.bFalse = newLbl();
	    n.end = newLbl();
	    root = n;
	    if(nestCnt == 0){
		rootEnd = root.end;
	    }
	    preorder(n);
	    inorder(n);
	    pw.println(n.bTrue);
	    
	    while(code == PUN && value == RParen){
		code = a.nextInt();
		value = a.nextInt();
	    }
	    if(code == PUN && value == LBrac){
		code = a.nextInt();
		value = a.nextInt();
	    }
	    
	    if(code <= symTable.length){
		for(int i = 0; i < symTable[code].size(); i++){
		    if(symTable[code].get(i).value == value && symTable[code].get(i).type != RES){
			//System.out.println("ASSIGNMENT IN IF");
			assignment2(a,i);
			if(code > symTable.length)
				break;		
		    }
		}
	    }
	    if(code == PUN && value == RBrac){
		    code = a.nextInt();
		    value = a.nextInt();
	    }
	    if(code == RES && value == While){
		nestCnt++;
		//System.out.println("WHILE IN IF");
		whileInIf = true;
		whileExp(a);
	    }

	    if(code == RES && value == Else){
		code = a.nextInt();
		value = a.nextInt();
		if(code == RES && value == If){
		    //System.out.println("ELSE IF");
		    pw.println("goto\t" + n.root.end);
		    pw.println(n.root.bFalse);
		    ifExp(a);
		    return;
		}
		//System.out.println("ELSE IN IF");
		pw.println("goto\t" + n.end);
		pw.println(n.bFalse);
		if(code == PUN && value == LBrac){     //??
		    code = a.nextInt();
		    value = a.nextInt();
		}
		if(code <= symTable.length){
		    for(int j = 0; j < symTable[code].size(); j++){
			if(symTable[code].get(j).value == value && symTable[code].get(j).type != RES){
			    //System.out.println("ASSIGNMENT IN ELSE");
			    assignment2(a,j);
			    if(code > symTable.length)
				break;
			}
		    }
		    if(code == PUN && value == RBrac){
			code = a.nextInt();
			value = a.nextInt();
		    }
		}
		
		pw.println(n.end);
	    }
		
	    else{	
		return;
	    }
	}

	public static void whileExp(Scanner a){
	    Node n = new Node();
	    String bTrue = newLbl();
	    String bFalse = newLbl();
	    String begin = newLbl();
	    root = n;
	    pw.println(begin);
	    code = a.nextInt();
	    value = a.nextInt();
	    if(code != PUN && value != LParen)
		System.out.println("MISSING PAREN");
	    else{
		code = a.nextInt();
		value = a.nextInt();
	    }
	    n = orExp(a);
	    n.bTrue = bTrue;
	    n.bFalse = bFalse;
	    n.begin = begin;
	    root = n;
	    preorder(n);
	    inorder(n);
	    tmpCnt = 1;
	    pw.println(n.bTrue);

	    //code2 = a.nextInt();
	    //value2 = a.nextInt();
	    if(code <= symTable.length){
		for(int i = 0; i < symTable[code].size(); i++){
		    if(symTable[code].get(i).value == value && symTable[code].get(i).type != RES){
			//System.out.println("ASSIGNMENT IN WHILE");
			assignment2(a,i);
			if(whileInIf){
			    whileInIf = false;
			}
			if(code > symTable.length)
				break;
		
		    }
		}
	    }
	    
	    if(code == RES && value == If){
		nestCnt++;
		//System.out.println("IF IN WHILE");
		ifExp(a);
	    }
	  
	    pw.println("goto\t" + n.begin);
	    pw.println(n.bFalse);
	}

	public static Node orExp(Scanner a){
	    Node n = andExp(a);//andExp(a);
	    n.root = root;
	    if(code == PUN && value == RParen){
		code = a.nextInt();
		value = a.nextInt();
	    }
	    while(code == OP && value == OR){
		code = a.nextInt();
		value = a.nextInt();
		n = new Node("||",n,andExp(a));
		n.root = root;
	    }
	    return n;
	}

	public static Node andExp(Scanner a){
	    Node n = notExp(a);//notExp(a);
	    n.root = root;
	    if(code == PUN && value == RParen){
		code = a.nextInt();
		value = a.nextInt();
	    }
	    while(code == OP && value == AND){
		code = a.nextInt();
		value = a.nextInt();
		n = new Node("&&",n,notExp(a));
		n.root = root;
	    }
	    return n;
	}

	public static Node notExp(Scanner a){
	    Node n = relOpExp(a);//relOpExp(a);
	    n.root = root;
	    while(code == OP && value == Neg){
		code = a.nextInt();
		value = a.nextInt();
	       	if(code == PUN && value == LParen){
		    code = a.nextInt();
		    value = a.nextInt();
		}
		n = new Node("!",relOpExp(a));//relOpExp(a));
		n.root = root;
	    }
	    return n;
	}



	public static Node relOpExp(Scanner a){
	    if (code2 == PUN && value2 == LParen){
                code2 = a.nextInt();
                value2 = a.nextInt();
                Node n = orExp(a);
                 if (code2 == PUN && value2 == RParen) {
                    code2 = a.nextInt();
                    value2 = a.nextInt();
                    return n;
                }
                else {
                    System.out.println("Right paren missing in boolean expression");
                }
            }
	    Node n = exp2(a);
	    n.root = root;
	    int relOp;
	    while(code == OP && (value == GT || value == GE || value == LT || value == LE || value == EQT || value == NE)){
		relOp = value;
		code = a.nextInt();
		value = a.nextInt();
		if(relOp == GT){
		    n = new Node(">",n,exp2(a));
		}
		else if(relOp == LT){
		    n = new Node("<",n,exp2(a));
		}
		else if(relOp == GE){
		    n = new Node(">=",n,exp2(a));
		}
		else if(relOp == LE){
		    n = new Node("<=",n,exp2(a));
		}
		else if(relOp == NE){
		    n = new Node("!=",n,exp2(a));
		}
		else if(relOp == EQT){
		    n = new Node("==",n,exp2(a));
		}
		else
		    n = new Node(Character.toString((char)value2),n,exp2(a));
	    }
	    
	    n.root = root;
	    return n;
	}

	public static Node exp2(Scanner a){
	    Node n = term2(a);
	    n.root = root;
	    int op;
	    while(code == OP && (value == Add || value == Sub)){
		op = value;
		code = a.nextInt();
		value = a.nextInt();
		n = new Node(Character.toString((char)op),n,term2(a));
		n.eRes = newTmp();
		pw.println((char)op + "\t" + n.left + "\t" + n.right + "\t" + n.eRes);
	    }
	    n.root = root;
	    return n;
	}

	public static Node term2(Scanner a){
	    Node n = factor2(a);
	    n.root = root;
	    int op;
	    while(code == OP && (value == Mul || value == Div || value == Mod)){
		op = value;
		code = a.nextInt();
		value = a.nextInt();
		n = new Node(Character.toString((char)value),n,factor2(a));
	    }
	    n.root = root;
	    return n;
	}

	public static Node factor2(Scanner a){
	    Node n = new Node();
	    n.root = root;
	    if(code == PUN && value ==LParen){
	       	code = a.nextInt();
		value = a.nextInt();
	       	n = exp2(a);
	    }
	    if(code <= symTable.length){
		for(int i = 0; i < symTable[code].size(); i++){
		    if(symTable[code].get(i).type != RES && symTable[code].get(i).value == value){
			n = new Node(symTable[code].get(i).name,null,null);
		    }
		}
	    }
	    if(code == INT || code == CHAR){		
		n = new Node(Integer.toString(value),null,null);
	    }
	    if(code == PUN && value == RParen){
		code = a.nextInt();
		value = a.nextInt();
	    }


	    if((code == INT) ||  (value != Sub && value != Add && value != Neg)){
		code = a.nextInt();
		value = a.nextInt();
	    }
	    n.root = root;
	    return n;
	}
	
    }

    public static class CodeGen{
	public static PrintWriter pw;
	public static String type;
	public String what;
	public static int lblCnt = 0, relCnt = 0;
	public static boolean gotoSt = false, inIf = false, afterRelOp = false, elseInIf = false;
	public CodeGen(Scanner a, PrintWriter pw){
	    this.pw = pw;
	    generate(a);
	    pw.println("return 0;");
	    pw.println("}");
	}

	public static void generate(Scanner a){
	    declarations();
	    while(a.hasNext()){
		type = a.next();
		if(type.equals("+") || type.equals("-") || type.equals("*") || type.equals("/"))
		    eq(a);
		else if(type.equals("="))
		    asg(a);
		else if(type.equals("[]="))
		    arr(a);
		else if(type.equals(">") || type.equals(">=") || type.equals("==") 
			|| type.equals("<=") || type.equals("<"))
		    relOp(a);
		else if(type.equals("goto"))
		    pw.println(type + " " + a.next());
		else if(type.startsWith("L_"))
		    pw.println(type);
		else if(type.equals("cout"))
		    coutPrt(a);	
		else if(type.equals("cin"))
		    cinPrt(a);		
	    }
	}

	public static void declarations(){
	    int decCount = 0;
	    pw.print("int ");
	    for(int i = 0; i < symTable.length; i++){
		if(symTable[i].size() > 0){
		    for(int j = 0; j < symTable[i].size(); j++){
			if(symTable[i].get(j).newType != RES){
			    if(symTable[i].get(j).newType == INC || symTable[i].get(j).newType == INV
			       || symTable[i].get(j).newType == CHAC || symTable[i].get(j).newType == CHAV){
				if(decCount != 0)
				    pw.print(",");
				decCount++;
				pw.print(symTable[i].get(j).name);
				if(symTable[i].get(j).newValue != 0)
				    pw.print(" = " + symTable[i].get(j).newValue);
			    }
			    else if(symTable[i].get(j).newType == arrayV){
				if(decCount != 0)
				    pw.print(",");
				decCount++;
				pw.print(symTable[i].get(j).name + "[" + symTable[i].get(j).size + "]");
				if(symTable[i].get(j).newValue != 0)
				    pw.print(" = " + symTable[i].get(j).newValue);
			    }
			    
			}
		    }
		    
		}
	    }
	    pw.println(";");
	    pw.println("int main(){");
	}
	public static void eq(Scanner a){
	    pw.println(a.next() + " " + type + " " + a.next() + " = " + a.next() + ";");
	}
	public static void asg(Scanner a){
	    String arg = a.next();
	    pw.println(a.next() + " = " + arg + ";");
	}
	public static void arr(Scanner a){
	    String arg = a.next();
	    String ind = a.next();
	    pw.println(a.next() + "[" + ind + "]" + " = " + arg + ";");
	}
	public static void relOp(Scanner a){
	    pw.println("if(" + a.next() + " " + type + " " + a.next() + ") " + a.next() + " " + a.next());
	}
	public static void coutPrt(Scanner a){
	    String printOut = type + " << " + a.nextLine().substring(1) + ";";
	    pw.println(printOut);
	}
	public static void cinPrt(Scanner a){
	    String printOut = type + " >> " + a.nextLine().substring(1) + ";";
	    pw.println(printOut);
	}
    
    }

    public static void main(String[] args) throws FileNotFoundException, IOException{
	PrintWriter pw = new PrintWriter("LAOutput.txt");
	PrintWriter pw2 = new PrintWriter("Quadruples.txt");
	PrintWriter pw3 = new PrintWriter("Code.txt");
	if(args.length > 0){
	    FileInputStream fs = new FileInputStream(args[0]);	
	    ArrayList[] st = LexicalAnalyzer.createSymTable(fs,pw);
	    Parser.createParseTree(new Scanner(new FileInputStream("LAOutput.txt")));
	     ParserTwo p2 = new ParserTwo(new Scanner(new FileInputStream("LAOutput.txt")),pw2);
	    pw2.flush();
	    pw2.close();
	     CodeGen cg = new CodeGen(new Scanner(new FileInputStream("Quadruples.txt")), pw3);
	    // pw.println("SymbolTable");
	    //    System.out.println("hashVal\tname\t\ttype\tvalue\tsize\telType");
	     // for(int i = 0; i < symTable.length; i++){
	     //	if(symTable[i].size() > 0){
	     //	    System.out.print(i);
	     //	    for(int j = 0; j < symTable[i].size(); j++)
	     //		System.out.println("\t" + symTable[i].get(j));
	     //	}
	     // }
	    pw.flush();
	    pw.close();
	    pw2.flush();
	    pw2.close();
	    pw3.flush();
	    pw3.close();
     
	}
	
    }

    
}

