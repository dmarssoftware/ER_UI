package com.EntityResolution.Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;

public class RowNumGenerator {
	public static boolean generateRowNum(String inputpath,String outputpath)
	{
		try {
			File fout = new File(outputpath);
			FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			BufferedReader br = new BufferedReader(new FileReader(inputpath));
		    String line;
		    Integer i=1;
		    File file=new File(inputpath);
		    while ((line = br.readLine()) != null) {
		    	
		    	// process the line.
		    	line= file.getName()+"_"+i+","+line;
		    	//System.out.println(line);
		    	 bw.write(line);
		    	 bw.newLine();
		    	 i++;
		    }
		    
		    br.close();
		    bw.close();
		    return true;
		    
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

}
