package com.mahmoud.book.file;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtils {

	public static byte[] reeadFileFromFileLocation(String fileUrl) {
		// TODO Auto-generated method stub
		if(StringUtils.isBlank(fileUrl)) {
			return null;
		}
		try {
			Path filePath=new File(fileUrl).toPath();
			return Files.readAllBytes(filePath);
		} catch (IOException e) {
			// TODO: handle exception
			log.warn("no files found in path {}",fileUrl);
		}
		return null;
	}

}
