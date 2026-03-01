package com.mahmoud.book.file;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mahmoud.book.book.Book;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageService {
	@Value("${application.file.upload.photos-output-path}")
	private String fileUploadPath;
	public String saveFile(@NotNull MultipartFile sourceFile,@NotNull Integer userId) {
		// TODO Auto-generated method stub
		final String fileUploadSubPath="users"+ File.separator+userId;
		return uploadFile(sourceFile,fileUploadSubPath);
	}
	private String uploadFile(@NotNull MultipartFile sourceFile,@NotNull String fileUploadSubPath) {
		// TODO Auto-generated method stub
		final String finalUploadPath=fileUploadPath+File.separator+fileUploadSubPath;
		File targetFolder=new File(finalUploadPath);
		if (!targetFolder.exists()) {
			boolean folderCreated=targetFolder.mkdirs();
			if (!folderCreated) {
				log.warn("failed to ctrate the target folder");
				return null;
			}
		}
		final String fileExtention=getFileExtention(sourceFile.getOriginalFilename());
		String targetFilePath=finalUploadPath+File.separator+System.currentTimeMillis()+"."+fileExtention; 
		Path targetPath=Paths.get(targetFilePath);
		try {
			Files.write(targetPath,sourceFile.getBytes());
			log.info("file saved to" +targetFilePath);
			return targetFilePath;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	private String getFileExtention(@Nullable String fileName) {
		// TODO Auto-generated method stub
		if(fileName == null|| fileName.isEmpty()) {
			return "";
		}
		int lastDotIndex=fileName.lastIndexOf(".");
		if(lastDotIndex==-1) {
			return "";
		}
		return fileName.substring(lastDotIndex+1).toLowerCase();
	}

}
