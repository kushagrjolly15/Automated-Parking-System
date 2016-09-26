package com.potenza_pvt_ltd.AAPS.util;

import android.util.Log;

import com.potenza_pvt_ltd.AAPS.function.PocketPos;

public class Printer {
	
	public Printer(){}
	
	public static byte[] printfont (String content,byte fonttype,byte fontalign,byte linespace,byte language){
		if (content != null && content.length() > 0) {
			
			content = content + "\n";
			byte[] temp = null;
			temp = PocketPos.convertPrintData(content, 0,content.length(), language, fonttype,fontalign,linespace);
			return temp;
		}else{
			return null;
		}
	}
	
}