package com.lab.writer.BatchWriter.lister;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.item.file.FlatFileParseException;

public class ProductListner {
	
	private String filename= "error/error_skipped";
	
	@OnSkipInRead
	public void oSkipRead(Throwable t) throws IOException {
		if(t instanceof FlatFileParseException) {
			FlatFileParseException ffep=(FlatFileParseException)t;
			onSkip(ffep, filename);
		}
	}
	
	public void onSkip(Object o,String Fname) throws IOException {
		FileOutputStream fos=null;
		fos=new FileOutputStream(Fname, true);
		fos.write(o.toString().getBytes());
		fos.write("\r\n".getBytes());
		fos.close();
	}
	
}
