package com.exe.string.test.dtc;

import java.math.BigInteger;

public class DTCTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String hexStr = "";
		DTCTest dt = new DTCTest();
		
		hexStr = "122312";
		dt.testGivenHexDTC(hexStr);
		
		hexStr = "ABCD";
		dt.testGivenHexDTC(hexStr);
		
		
		hexStr = "6789";
		dt.testGivenHexDTC(hexStr);
		
	
	}
	
	
	public void testGivenHexDTC(String hexStr) {
		
		System.out.println("Initial   Value " + hexStr);
		String saeConv = hexString2SAE(hexStr);
		System.out.println("SAE DTC   Value " + saeConv);
		String reverVal = sae2HexStr(saeConv);
		System.out.println("Reversed  Value " + reverVal);
		
	}

	
	public String hexString2SAE(String hexString) {
		String saeStr = null, bstr, letterType, type, status ="";
		String str2conv = hexString.replace("0x", "");
		//System.out.println("str2conv.length()  " + str2conv.length());
		if(str2conv.length() != 4 && str2conv.length() != 6) {
			System.out.println("Given DTC must be 4 or 6 characters, it wont be converted! " );
			return "";
		}
		if (str2conv.length() == 6 ) {
			status = str2conv.substring(4,6);
			str2conv = str2conv.substring(0,4);

		}

		//System.out.println("str2conv " + str2conv);
		if(!isHexStr(str2conv)) {
			System.out.println("Not a valid hex, it wont be converted!" );
			return "";
		}
		
		
		//System.out.println("str2conv " + str2conv);
		bstr = hexToBinary(str2conv);
		bstr = nibbleComplete(bstr);
		//System.out.println("bstr " + bstr);
		
		letterType = "P";
		type = bstr.substring(0,2);
		String dtcRest = nibbleComplete(bstr.substring(2,bstr.length()));
		String dtcRestHex = binToHexadecimal(dtcRest);
		if(type.equals("00")) {
			letterType = "P";
		}else if(type.equals("01")) {
			letterType = "C";
		}else if(type.equals("10")) {
			letterType = "B";
		}else {
			letterType = "U";
		}
		saeStr = letterType + dtcRestHex;
		if(!status.isEmpty()) {
			saeStr += status;
		}
		
		return saeStr;
	}
	
	
	public String sae2HexStr(String saeString) {
		String hexStr = null, letterType, restOfDtc, bstr, hexStart;
		int saeStrLen = saeString.length();
		int expBinStrLen = ((saeStrLen - 1) * 4) - 2; 
		if(saeString.length() != 5 && saeStrLen != 7) {
			System.out.println("Given SAE DTC must be 5 or 7 characters it wont be converted! " );
			return "";
		}
		letterType = saeString.substring(0, 1);
		restOfDtc = saeString.substring(1,saeString.length());
		
		if(!isHexStr(restOfDtc)) {
			System.out.println("Not a valid hex, it wont be converted!" );
			return "";
		}
		
		bstr = hexToBinary(restOfDtc);
		bstr = nibbleComplete(bstr);
		
		if(letterType.equals("P")) {
			hexStart = "00";
		}else if(letterType.equals("C")) {
			hexStart = "01";
		}else if(letterType.equals("B")) {
			hexStart = "10";
		}else if(letterType.equals("U")) {
			hexStart = "11";
		}else {
			System.out.println("First letter is not expected should be {P|C|B|U}, it wont be converted");
			return "";
		}
		//System.out.println("bstr " + bstr );
		int binStrLen = bstr.length();
		int diff = binStrLen - expBinStrLen;
		bstr = hexStart + bstr.substring(diff, bstr.length());
		hexStr = binToHexadecimal(bstr);
		
		return hexStr;
	}
	
	
	public static String nibbleComplete(String binaryArray) {
		String result = binaryArray;
		int size = binaryArray.length();
		int module  = size % 4;
		//System.out.println("Module " + module);
		if(module != 0) {
			//System.out.println("Must be filled with zeros!");
			for(int i = 0 ; i < 4 - module ; i++) {
				result = "0" + result;
			}
		}
		
		
		return result;
		
	}
	
	
	public static String hexToBinary(String hex) {
	    return new BigInteger(hex, 16).toString(2);
	}
	
	
	public static String binToHexadecimal(String bin) {
		return new BigInteger(bin, 2).toString(16);
	}
	
	
	public boolean isHexStr(String hexStr) {
		try
		{
			Integer.parseInt(hexStr, 16); 
		 }
		 catch(NumberFormatException nfe)
		 {
		    return false;
		 }
		return true;
	}
	
	
}
