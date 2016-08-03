package com.turborep.turbotracker.job.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.connection.ConnectionProvider;

import com.turborep.turbotracker.job.dao.Jomaster;
import com.turborep.turbotracker.job.exception.JobException;

public interface PDFService {

	public void openPDFStream(Jomaster aJomaster, HttpSession session, HttpServletResponse theResponse) throws MalformedURLException, IOException, URISyntaxException, JobException;
	
	public Integer getQuoteHeaderID(String theQuoteTypeID, String theQuoteRev, String theJoMasterID, String theQuoteRevId);

	public Integer getHeaderID(Integer theQuoteTypeID, Integer theJoMasterID);

	public String getQuotesRev(String theQuoteTypeID, String theQuoteRev,
			String theJoMasterID);

	public ConnectionProvider connectionForJasper();
	
}
