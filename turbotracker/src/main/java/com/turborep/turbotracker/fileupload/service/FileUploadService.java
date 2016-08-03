package com.turborep.turbotracker.fileupload.service;

import java.io.InputStream;
import java.sql.Blob;

import com.turborep.turbotracker.fileupload.exception.FileUploadException;

public interface FileUploadService {

	public String insertExcelData(InputStream theInputStream, int vePOId) throws FileUploadException;
	
	public String uploadCompanyLogo(Blob blob) throws FileUploadException;
}
