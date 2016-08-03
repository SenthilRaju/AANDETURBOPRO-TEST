package com.turborep.turbotracker.PDF;

import java.awt.Color;
import java.io.File;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.dao.Rxcontact;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.job.dao.Jomaster;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.vendor.dao.VeFactory;
import com.turborep.turbotracker.vendor.dao.Vepo;
import com.turborep.turbotracker.vendor.dao.Vepodetail;
import com.turborep.turbotracker.vendor.dao.Veshipvia;

public class PDFGenerator {

	Logger itsLogger = Logger.getLogger(PDFGenerator.class);

	public PDFGenerator() {
		super();
	}

	public File generatePOGeneralPDF(Integer theVePoId) {
		File aPDFfile = new File("");
		return aPDFfile;
	}

	public void getProjectInformation(Document theDocument,
			TsUserSetting theUserLoginSetting, Font theBoldFont,
			Font theNormalFont, PdfWriter thePdfWriter, Rxaddress theRxAddress,
			Vepo theVePO, UserBean theUserBean, Veshipvia theVeshipvia,
			Rxcontact theRxcontact, Rxmaster theRxmaster,
			Rxaddress theRxAddressBillTo, List<Prwarehouse> thePrwarehouses,
			Jomaster theJomaster, Rxaddress theRxAddressShipTo,
			Rxmaster theRxmasterAddress, VeFactory theVendorDetail,
			Integer theShipToAddrID, Rxaddress theRxAddressOtherShipTo,
			Rxaddress theRxAddressOtherBillTo, TsUserLogin theLogin,
			Rxmaster theRxmasterName, Integer noOfLines) {
		itsLogger.info("Bill to Index:"+theVePO.getBillToIndex());
		
		String aVendorAddress = "";
		String aShipToAddress = "";
		String aBilltoAddress = "";
		String aVendorName = "";
		String aFrieght = "";
		String aShipVia = "";
		String aOrderBy = "";
		String aOrderByDate = "";
		
		try {
			if (theShipToAddrID == null) {
				theShipToAddrID = 0;
			}
			if (theJomaster != null) {
				if (theJomaster.getLocationAddress2() == null) {
					theJomaster.setLocationAddress2("");
				}
			}

			if (theRxmaster == null) {
				theRxmaster = new Rxmaster();
				theRxmaster.setName("");
			}
			PdfPTable aTable = new PdfPTable(2);
			aTable.setWidthPercentage(100);
			PdfPCell aPdfWordProjectCell = new PdfPCell();
			//aPdfWordProjectCell.setBorder(Rectangle.NO_BORDER);
			aPdfWordProjectCell.setFixedHeight(70);
			PdfContentByte cb = thePdfWriter.getDirectContent();
			if (theUserLoginSetting.getHeaderPurchaseOrders() == 1) {
				/**
				 * Basically these values are common for all header line
				 * numbers. We need to fix the header padding. That is enough.
				 **/
				/*if (noOfLines == 4 || noOfLines == 5) {
					cb.roundRectangle(30f, 620f, 265f, 80f, 10f);
					cb.roundRectangle(30f, 585f, 265f, 30f, 10f);
					cb.roundRectangle(300f, 585f, 265f, 115f, 10f);
					cb.roundRectangle(30f, 490f, 265f, 90f, 10f);
					cb.roundRectangle(300f, 490f, 265f, 90f, 10f);
				} else if (noOfLines == 2 || noOfLines == 1) {
					cb.roundRectangle(30f, 620f, 265f, 80f, 10f);
					cb.roundRectangle(30f, 585f, 265f, 30f, 10f);
					cb.roundRectangle(300f, 585f, 265f, 115f, 10f);
					cb.roundRectangle(30f, 490f, 265f, 90f, 10f);
					cb.roundRectangle(300f, 490f, 265f, 90f, 10f);
				} else {
					*//** Vendor Address Table curve **//*
					cb.roundRectangle(30f, 620f, 265f, 80f, 10f);
					*//** tag box Table curve **//*
					cb.roundRectangle(30f, 585, 265f, 30f, 10f);
					*//** Order date box Table curve **//*
					cb.roundRectangle(300f, 585f, 265f, 115f, 10f);
					*//** Bill To box Table curve **//*
					cb.roundRectangle(30f, 490f, 265f, 90f, 10f);
					*//** SHIP tO box Table curve **//*
					cb.roundRectangle(300f, 490f, 265f, 90f, 10f);
				}*/
			} else {/*
				*//** Vendor Address Table curve **//*
				cb.roundRectangle(30f, 595f, 265f, 80f, 10f);
				*//** tag box Table curve **//*
				cb.roundRectangle(30f, 560f, 265f, 30f, 10f);
				*//** Order date box Table curve **//*
				cb.roundRectangle(300f, 560f, 265f, 115f, 10f);
				*//** Bill To box Table curve **//*
				cb.roundRectangle(30f, 465f, 265f, 90f, 10f);
				*//** SHIP tO box Table curve **//*
				cb.roundRectangle(300f, 465f, 265f, 90f, 10f);
			*/}
			//cb.stroke();
		
			if (theRxmasterName != null) {
				aVendorName = theRxmasterName.getName();
			}
			if (theRxAddress != null) {
				if (aVendorName!=null && !aVendorName.equalsIgnoreCase("")) {
					aVendorAddress = aVendorName + "\n";
				}
				if (theRxAddress.getAddress1()!=null && !theRxAddress.getAddress1().equalsIgnoreCase("")) {
					aVendorAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
							+ theRxAddress.getAddress1() + "\n";
				}
				if (theRxAddress.getAddress2()!=null && !theRxAddress.getAddress2().equalsIgnoreCase("")
						&& !theRxAddress.getAddress1().equalsIgnoreCase("")) {
					aVendorAddress = "";
					aVendorAddress = aVendorName
							+ "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
							+ theRxAddress.getAddress1() + ", "
							+ theRxAddress.getAddress2() + "\n";
				}
				if (theRxAddress.getCity()!=null && !theRxAddress.getCity().equalsIgnoreCase("")) {
					aVendorAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
							+ theRxAddress.getCity();
				}
				if (theRxAddress.getState()!=null && !theRxAddress.getState().equalsIgnoreCase("")) {
					if (theRxAddress.getCity().equalsIgnoreCase("")) {
						aVendorAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
								+ theRxAddress.getState();
					} else {
						aVendorAddress += ", " + theRxAddress.getState();
					}
				}
				if (!theRxAddress.getZip().equalsIgnoreCase("")) {
					aVendorAddress += " " + theRxAddress.getZip();
				}
				/*
				 * aVendorAddress = aVendorName+
				 * "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
				 * +theRxAddress.getAddress1() + ", " +
				 * theRxAddress.getAddress2() +
				 * "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" +
				 * theRxAddress.getCity() + ", " + theRxAddress.getState() +
				 * ", " + theRxAddress.getZip();
				 */
			} else {
				aVendorAddress = aVendorName;
			}
			if (theVePO.getBillToIndex() == null) {
				if (theUserLoginSetting.getItsBillTo() != 0) {
					itsLogger.info("Bill to Index-null :"+theUserLoginSetting.getBillToDescription());
					if (!theUserLoginSetting.getBillToDescription()
							.equalsIgnoreCase("")) {
						aBilltoAddress = theUserLoginSetting
								.getBillToDescription() + "\n";
					}
					if (!theUserLoginSetting.getBillToAddress1()
							.equalsIgnoreCase("")) {
						aBilltoAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
								+ theUserLoginSetting.getBillToAddress1()
								+ "\n";
					}
					if (!theUserLoginSetting.getBillToAddress2()
							.equalsIgnoreCase("")) {
						aBilltoAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
								+ theUserLoginSetting.getBillToAddress2()
								+ "\n";
					}
					if (!theUserLoginSetting.getBillToCity().equalsIgnoreCase(
							"")) {
						aBilltoAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
								+ theUserLoginSetting.getBillToCity();
					}
					if (!theUserLoginSetting.getBillToState().equalsIgnoreCase(
							"")) {
						if (theUserLoginSetting.getBillToCity()
								.equalsIgnoreCase("")) {
							aBilltoAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
									+ theUserLoginSetting.getBillToState();
						} else {
							aBilltoAddress += ", "
									+ theUserLoginSetting.getBillToState();
						}
					}
					if (!theUserLoginSetting.getBillToZip()
							.equalsIgnoreCase("")) {
						aBilltoAddress += " "
								+ theUserLoginSetting.getBillToZip();
					}
				}
			}else if(theVePO.getBillToIndex() == 0) {
				//if (theUserLoginSetting.getItsBillTo() != 0) {
					itsLogger.info("Bill to Index-0 :"+theUserLoginSetting.getBillToDescription());
					if (!theUserLoginSetting.getBillToDescription()
							.equalsIgnoreCase("")) {
						aBilltoAddress = theUserLoginSetting
								.getBillToDescription() + "\n";
					}
					if (!theUserLoginSetting.getBillToAddress1()
							.equalsIgnoreCase("")) {
						aBilltoAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
								+ theUserLoginSetting.getBillToAddress1()
								+ "\n";
					}
					if (!theUserLoginSetting.getBillToAddress2()
							.equalsIgnoreCase("")) {
						aBilltoAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
								+ theUserLoginSetting.getBillToAddress2()
								+ "\n";
					}
					if (!theUserLoginSetting.getBillToCity().equalsIgnoreCase(
							"")) {
						aBilltoAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
								+ theUserLoginSetting.getBillToCity();
					}
					if (!theUserLoginSetting.getBillToState().equalsIgnoreCase(
							"")) {
						if (theUserLoginSetting.getBillToCity()
								.equalsIgnoreCase("")) {
							aBilltoAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
									+ theUserLoginSetting.getBillToState();
						} else {
							aBilltoAddress += ", "
									+ theUserLoginSetting.getBillToState();
						}
					}
					if (!theUserLoginSetting.getBillToZip()
							.equalsIgnoreCase("")) {
						aBilltoAddress += " "
								+ theUserLoginSetting.getBillToZip();
					}
				//}
			} 
			else if (theVePO.getBillToIndex() == 1) {
				if (theRxAddressBillTo != null) {
					if (!theRxAddressBillTo.getName().equalsIgnoreCase("")) {
						aBilltoAddress = theRxAddressBillTo.getName() + "\n";
					} else if (!theRxmaster.getName().equalsIgnoreCase("")) {
						aBilltoAddress = theRxmaster.getName() + "\n";
					}
					if (!theRxAddressBillTo.getAddress1().equalsIgnoreCase("")) {
						aBilltoAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
								+ theRxAddressBillTo.getAddress1() + "\n";
					}
					if (!theRxAddressBillTo.getAddress2().equalsIgnoreCase("")) {
						aBilltoAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
								+ theRxAddressBillTo.getAddress2() + "\n";
					}
					if (!theRxAddressBillTo.getCity().equalsIgnoreCase("")) {
						aBilltoAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
								+ theRxAddressBillTo.getCity();
					}
					if (!theRxAddressBillTo.getState().equalsIgnoreCase("")) {
						if (theRxAddressBillTo.getCity().equalsIgnoreCase("")) {
							aBilltoAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
									+ theRxAddressBillTo.getState();
						} else {
							aBilltoAddress += ", "
									+ theRxAddressBillTo.getState();
						}
					}
					if (!theRxAddressBillTo.getZip().equalsIgnoreCase("")) {
						aBilltoAddress += " " + theRxAddressBillTo.getZip();
					}
					/*
					 * aBilltoAddress = theRxmaster.getName()+
					 * "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
					 * +theRxAddressBillTo.getAddress1() + ", " +
					 * theRxAddressBillTo.getAddress2() +
					 * "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" +
					 * theRxAddressBillTo.getCity() + ", " +
					 * theRxAddressBillTo.getState() + ", " +
					 * theRxAddressBillTo.getZip();
					 */
				}
			} else if (theVePO.getBillToIndex() == 2) {
				if (theRxAddressOtherBillTo != null) {
					if (!theRxAddressOtherBillTo.getName().equalsIgnoreCase("")) {
						aBilltoAddress = theRxAddressOtherBillTo.getName()
								+ "\n";
					}
					if (!theRxAddressOtherBillTo.getAddress1()
							.equalsIgnoreCase("")) {
						aBilltoAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
								+ theRxAddressOtherBillTo.getAddress1() + "\n";
					}
					if (!theRxAddressOtherBillTo.getAddress2()
							.equalsIgnoreCase("")) {
						aBilltoAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
								+ theRxAddressOtherBillTo.getAddress2() + "\n";
					}
					if (!theRxAddressOtherBillTo.getCity().equalsIgnoreCase("")) {
						aBilltoAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
								+ theRxAddressOtherBillTo.getCity();
					}
					if (!theRxAddressOtherBillTo.getState()
							.equalsIgnoreCase("")) {
						if (theRxAddressOtherBillTo.getCity().equalsIgnoreCase(
								"")) {
							aBilltoAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
									+ theRxAddressOtherBillTo.getState();
						} else {
							aBilltoAddress += ", "
									+ theRxAddressOtherBillTo.getState();
						}
					}
					if (!theRxAddressOtherBillTo.getZip().equalsIgnoreCase("")) {
						aBilltoAddress += " "
								+ theRxAddressOtherBillTo.getZip();
					}
					/*
					 * aBilltoAddress = theRxAddressOtherBillTo.getName()+
					 * "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
					 * +theRxAddressOtherBillTo.getAddress1() + ", " +
					 * theRxAddressOtherBillTo.getAddress2() +
					 * "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" +
					 * theRxAddressOtherBillTo.getCity() + ", " +
					 * theRxAddressOtherBillTo.getState() + ", " +
					 * theRxAddressOtherBillTo.getZip();
					 */
				}
			}

			if (theVePO.getShipToMode()==0) {
				if (thePrwarehouses.size()>0) {
					for (int index = 0; index < thePrwarehouses.size(); index++) {
							if (thePrwarehouses.get(index).getPrWarehouseId() == theVePO.getPrWarehouseId()) {
								if (!thePrwarehouses.get(index).getDescription()
										.equalsIgnoreCase("")) {
									aShipToAddress = thePrwarehouses.get(index)
											.getDescription() + "\n";
									/*if (theVePO != null) {
										if (!theVePO.getTag().equalsIgnoreCase(
												"")) {
											aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t c/o :   "
													+ theVePO.getTag() + "\n";
										}
									}*/
								}
								if (thePrwarehouses.get(index).getAddress1() !=null &&!thePrwarehouses.get(index).getAddress1()
										.equalsIgnoreCase("")) {
									aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
											+ thePrwarehouses.get(index)
													.getAddress1() + "\n";
								}
								if (thePrwarehouses.get(index).getAddress2() !=null && !thePrwarehouses.get(index).getAddress2()
										.equalsIgnoreCase("")) {
									aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
											+ thePrwarehouses.get(index)
													.getAddress2() + "\n";
								}
								if (thePrwarehouses.get(index).getCity() !=null && !thePrwarehouses.get(index).getCity()
										.equalsIgnoreCase("")) {
									aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
											+ thePrwarehouses.get(index).getCity();
								}
								if (thePrwarehouses.get(index).getState() !=null && !thePrwarehouses.get(index).getState()
										.equalsIgnoreCase("")) {
									if (thePrwarehouses.get(index).getCity()
											.equalsIgnoreCase("")) {
										aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
												+ thePrwarehouses.get(index)
														.getState();
									} else {
										aShipToAddress += ", "
												+ thePrwarehouses.get(index)
														.getState();
									}
								}
								if (thePrwarehouses.get(index).getZip() !=null && !thePrwarehouses.get(index).getZip()
										.equalsIgnoreCase("")) {
									aShipToAddress += " "
											+ thePrwarehouses.get(index).getZip();
								}
						}
					}
				}
					} else if (theVePO.getShipToMode() == 1) {
					if (theRxAddressShipTo != null) {
						if (!theRxmaster.getName().equalsIgnoreCase("")) {
							aShipToAddress = theRxmaster.getName() + "\n";
							if (theVePO != null) {
								if (!theVePO.getTag().equalsIgnoreCase("")) {
									aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t c/o :   "
											+ theVePO.getTag() + "\n";
								}
							}
						}
						if (theRxAddressShipTo.getAddress1() != null && !theRxAddressShipTo.getAddress1().equalsIgnoreCase(
								"")) {
							aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
									+ theRxAddressShipTo.getAddress1() + "\n";
						}
						if (theRxAddressShipTo.getAddress2() != null && !theRxAddressShipTo.getAddress2().equalsIgnoreCase(
								"")) {
							aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
									+ theRxAddressShipTo.getAddress2() + "\n";
						}
						if (theRxAddressShipTo.getCity() != null && !theRxAddressShipTo.getCity().equalsIgnoreCase("")) {
							aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
									+ theRxAddressShipTo.getCity();
						}
						if (theRxAddressShipTo.getState() != null && !theRxAddressShipTo.getState().equalsIgnoreCase("")) {
							if (theRxAddressShipTo.getCity().equalsIgnoreCase(
									"")) {
								aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
										+ theRxAddressShipTo.getState();
							} else {
								aShipToAddress += ", "
										+ theRxAddressShipTo.getState();
							}
						}
						if (theRxAddressShipTo.getZip() != null && !theRxAddressShipTo.getZip().equalsIgnoreCase("")) {
							aShipToAddress += " " + theRxAddressShipTo.getZip();
						}
						/*
						 * aShipToAddress = theRxmaster.getName()+
						 * "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
						 * +theRxAddressShipTo.getAddress1() +
						 * ",\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
						 * + theRxAddressShipTo.getAddress2() +
						 * "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
						 * + theRxAddressShipTo.getCity() + ", " +
						 * theRxAddressShipTo.getState() + ", " +
						 * theRxAddressShipTo.getZip();
						 */
					}
				} else if (theVePO.getShipToMode() == 2) {
					if (theJomaster != null) {
						if (!theRxmaster.getName().equalsIgnoreCase("")) {
							aShipToAddress = theRxmaster.getName() + "\n";
							if (theVePO != null) {
								if (!theVePO.getTag().equalsIgnoreCase("")) {
									aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t c/o :   "
											+ theVePO.getTag() + "\n";
								}
							}
						}
						if (!theJomaster.getLocationAddress1()
								.equalsIgnoreCase("")) {
							aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
									+ theJomaster.getLocationAddress1() + "\n";
						}
						if (!theJomaster.getLocationAddress2()
								.equalsIgnoreCase("")) {
							aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
									+ theJomaster.getLocationAddress2() + "\n";
						}
						if (!theJomaster.getLocationCity().equalsIgnoreCase("")) {
							aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
									+ theJomaster.getLocationCity();
						}
						if (!theJomaster.getLocationState()
								.equalsIgnoreCase("")) {
							if (theJomaster.getLocationCity().equalsIgnoreCase(
									"")) {
								aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
										+ theJomaster.getLocationState();
							} else {
								aShipToAddress += ", "
										+ theJomaster.getLocationState();
							}
						}
						if (!theJomaster.getLocationZip().equalsIgnoreCase("")) {
							aShipToAddress += " "
									+ theJomaster.getLocationZip();
						}
					} else {
						if (theRxAddressOtherShipTo != null) {
							if (!theRxAddressOtherShipTo.getName()
									.equalsIgnoreCase("")) {
								aShipToAddress = theRxAddressOtherShipTo
										.getName() + "\n";
								if (theVePO != null) {
									if (!theVePO.getTag().equalsIgnoreCase("")) {
										aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t c/o :   "
												+ theVePO.getTag() + "\n";
									}
								}
							}
							if (!theRxAddressOtherShipTo.getAddress1()
									.equalsIgnoreCase("")) {
								aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
										+ theRxAddressOtherShipTo.getAddress1()
										+ "\n";
							}
							if (!theRxAddressOtherShipTo.getAddress2()
									.equalsIgnoreCase("")) {
								aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
										+ theRxAddressOtherShipTo.getAddress2()
										+ "\n";
							}
							if (!theRxAddressOtherShipTo.getCity()
									.equalsIgnoreCase("")) {
								aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
										+ theRxAddressOtherShipTo.getCity();
							}
							if (!theRxAddressOtherShipTo.getState()
									.equalsIgnoreCase("")) {
								if (theRxAddressOtherShipTo.getCity()
										.equalsIgnoreCase("")) {
									aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
											+ theRxAddressOtherShipTo
													.getState();
								} else {
									aShipToAddress += ", "
											+ theRxAddressOtherShipTo
													.getState();
								}
							}
							if (!theRxAddressOtherShipTo.getZip()
									.equalsIgnoreCase("")) {
								aShipToAddress += " "
										+ theRxAddressOtherShipTo.getZip();
							}
							/*
							 * aShipToAddress =
							 * theRxAddressOtherShipTo.getName()+
							 * "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
							 * +theRxAddressOtherShipTo.getAddress1() +
							 * ",\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
							 * + theRxAddressOtherShipTo.getAddress2() +
							 * "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
							 * + theRxAddressOtherShipTo.getCity() + ", " +
							 * theRxAddressOtherShipTo.getState() + ", " +
							 * theRxAddressOtherShipTo.getZip();
							 */
						}
					}
				} else if (theVePO.getShipToMode() == 3) {
					if (theRxAddressOtherShipTo != null) {
						if (!theRxAddressOtherShipTo.getName()
								.equalsIgnoreCase("")) {
							aShipToAddress = theRxAddressOtherShipTo.getName()
									+ "\n";
							if (theVePO != null) {
								if (!theVePO.getTag().equalsIgnoreCase("")) {
									aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t c/o :   "
											+ theVePO.getTag() + "\n";
								}
							}
						}
						if (!theRxAddressOtherShipTo.getAddress1()
								.equalsIgnoreCase("")) {
							aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
									+ theRxAddressOtherShipTo.getAddress1()
									+ "\n";
						}
						if (!theRxAddressOtherShipTo.getAddress2()
								.equalsIgnoreCase("")) {
							aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
									+ theRxAddressOtherShipTo.getAddress2()
									+ "\n";
						}
						if (!theRxAddressOtherShipTo.getCity()
								.equalsIgnoreCase("")) {
							aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
									+ theRxAddressOtherShipTo.getCity();
						}
						if (!theRxAddressOtherShipTo.getState()
								.equalsIgnoreCase("")) {
							if (theRxAddressOtherShipTo.getCity()
									.equalsIgnoreCase("")) {
								aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
										+ theRxAddressOtherShipTo.getState();
							} else {
								aShipToAddress += ", "
										+ theRxAddressOtherShipTo.getState();
							}
						}
						if (!theRxAddressOtherShipTo.getZip().equalsIgnoreCase(
								"")) {
							aShipToAddress += " "
									+ theRxAddressOtherShipTo.getZip();
						}
					}
				}
				 else {
					if (theRxAddressOtherShipTo != null) {
						if (theRxAddressOtherShipTo.getName()!=null && !theRxAddressOtherShipTo.getName()
								.equalsIgnoreCase("")) {
							aShipToAddress = theRxAddressOtherShipTo.getName()
									+ "\n";
							if (theVePO != null) {
								if (!theVePO.getTag().equalsIgnoreCase("")) {
									aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t c/o :   "
											+ theVePO.getTag() + "\n";
								}
							}
						}
						if (!theRxAddressOtherShipTo.getAddress1()
								.equalsIgnoreCase("")) {
							aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
									+ theRxAddressOtherShipTo.getAddress1()
									+ "\n";
						}
						if (!theRxAddressOtherShipTo.getAddress2()
								.equalsIgnoreCase("")) {
							aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
									+ theRxAddressOtherShipTo.getAddress2()
									+ "\n";
						}
						if (!theRxAddressOtherShipTo.getCity()
								.equalsIgnoreCase("")) {
							aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
									+ theRxAddressOtherShipTo.getCity();
						}
						if (!theRxAddressOtherShipTo.getState()
								.equalsIgnoreCase("")) {
							if (theRxAddressOtherShipTo.getCity()
									.equalsIgnoreCase("")) {
								aShipToAddress += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
										+ theRxAddressOtherShipTo.getState();
							} else {
								aShipToAddress += ", "
										+ theRxAddressOtherShipTo.getState();
							}
						}
						if (!theRxAddressOtherShipTo.getZip().equalsIgnoreCase(
								"")) {
							aShipToAddress += " "
									+ theRxAddressOtherShipTo.getZip();
						}
					}
				 }
			String aATTN = "";
			if (theRxcontact != null) {
				if (theRxcontact.getFirstName() != ""
						&& theRxcontact.getLastName() != "") {
					aATTN = theRxcontact.getFirstName().replaceAll("null", "")
							+ " "
							+ theRxcontact.getLastName().replaceAll("null", "");
				}
			}

			/** Project Name in Header Box **/
			Phrase aProject = new Phrase("  VENDOR: ", FontFactory.getFont(
					FontFactory.COURIER, 11, Font.BOLD));
			aProject.add(new Phrase(aVendorAddress, theNormalFont));
			Phrase aATTNProject = new Phrase("    ATTN: ", FontFactory.getFont(
					FontFactory.COURIER, 11, Font.BOLD));
			aATTNProject.add(new Phrase(aATTN, theNormalFont));
			Phrase aTag = new Phrase("     TAG: ", FontFactory.getFont(
					FontFactory.COURIER, 11, Font.BOLD));
			aTag.add(new Phrase(theVePO.getTag(), theNormalFont));
			Phrase aEmpty = new Phrase(" ", theBoldFont);
			aPdfWordProjectCell.addElement(aProject);
			aPdfWordProjectCell.addElement(aATTNProject);
			aPdfWordProjectCell.addElement(aEmpty);
			aPdfWordProjectCell.addElement(aTag);
			aTable.addCell(aPdfWordProjectCell);
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			PdfPCell pdfWordBidDateCell = new PdfPCell();
			//pdfWordBidDateCell.setBorder(Rectangle.NO_BORDER);
		
			if (theVePO.getVeFreightChargesId() != null) {
				if (theVePO.getVeFreightChargesId() == 1) {
					aFrieght = "3rd Party";
				} else if (theVePO.getVeFreightChargesId() == 2) {
					aFrieght = "Allowed";
				} else if (theVePO.getVeFreightChargesId() == 3) {
					aFrieght = "Collect";
				} else if (theVePO.getVeFreightChargesId() == 4) {
					aFrieght = "None";
				} else if (theVePO.getVeFreightChargesId() == 5) {
					aFrieght = "Prepaid";
				} else if (theVePO.getVeFreightChargesId() == 6) {
					aFrieght = "See Frt Col";
				} else if (theVePO.getVeFreightChargesId() == 7) {
					aFrieght = "See Note";
				} else if (theVePO.getVeFreightChargesId() == 8) {
					aFrieght = "EXLA";
				}
			}
			if (theVeshipvia != null) {
				if (theVeshipvia.getDescription() != null
						&& theVeshipvia.getDescription() != "") {
					aShipVia = theVeshipvia.getDescription();
				}
			}
			if (theVePO.getOrderDate() != null) {
				aOrderByDate = dateFormat.format(theVePO.getOrderDate());
			}
			/** Bid Date in Header Box **/
			Phrase aBidDate = new Phrase("    ORDER DATE: ", theBoldFont);
			aBidDate.add(new Phrase(aOrderByDate, theNormalFont));

			if (theLogin != null) {
				aOrderBy = theLogin.getFullName();
			} else {
				aOrderBy = "";
			}

			/** Architect in Header Box **/
			Phrase aArchitect = new Phrase("    ORDERED BY: ", theBoldFont);
			aArchitect.add(new Phrase(aOrderBy, theNormalFont));

			/** Architect in Header Box **/
			Phrase aEngineer = new Phrase("      SHIP VIA: ", theBoldFont);
			aEngineer.add(new Phrase(aShipVia, theNormalFont));

			/** Plan Date in Header Box **/
			Phrase aPlanDate = new Phrase("   DATE WANTED: ", theBoldFont);
			aPlanDate.add(new Phrase(theVePO.getDateWanted(), theNormalFont));

			/** Revision in Header Box **/
			Phrase aRevision = new Phrase("  CUSTOMER PO#: ", theBoldFont);
			aRevision.add(new Phrase(theVePO.getCustomerPonumber(),
					theNormalFont));

			Phrase aFreightChag = new Phrase("  FREIGHT CHGs: ", theBoldFont);
			aFreightChag.add(new Phrase(aFrieght, theNormalFont));

			/*
			 * Phrase aPurchaseorder = new Phrase("PURCHASE ORDER: ",
			 * theBoldFont); aPurchaseorder.add(new
			 * Phrase(theVePO.getPonumber(), theNormalFont));
			 */
			//pdfWordBidDateCell.setCellEvent(rr);
			pdfWordBidDateCell.addElement(aBidDate);
			pdfWordBidDateCell.addElement(aArchitect);
			pdfWordBidDateCell.addElement(aEngineer);
			pdfWordBidDateCell.addElement(aPlanDate);
			pdfWordBidDateCell.addElement(aRevision);
			pdfWordBidDateCell.addElement(aFreightChag);
			// pdfWordBidDateCell.addElement(aPurchaseorder);
			pdfWordBidDateCell.addElement(aEmpty);
			aTable.addCell(pdfWordBidDateCell);
			theDocument.add(aTable);
			
			Paragraph aParagraphempty = new Paragraph("     ");
			aParagraphempty.setSpacingBefore(0);
			theDocument.add(aParagraphempty);
			
			PdfPTable aTableBillTo = new PdfPTable(2);
			aTableBillTo.setWidthPercentage(100);
			PdfPCell aPdfBillToCell = new PdfPCell();
			//aPdfBillToCell.setPaddingTop(-20);
			//aPdfBillToCell.setBorder(Rectangle.NO_BORDER);
			/** Project Name in Header Box **/
			Phrase aBillTo = new Phrase("BILL TO: ", theBoldFont);
			aBillTo.add(new Phrase(aBilltoAddress.replaceAll("null", ""),
					theNormalFont));
			aPdfBillToCell.addElement(aBillTo);
			aPdfBillToCell.addElement(aEmpty);
			aTableBillTo.addCell(aPdfBillToCell);

			PdfPCell pdfShipToCell = new PdfPCell();
			//pdfShipToCell.setBorder(Rectangle.NO_BORDER);
			//pdfShipToCell.setPaddingTop(-20);
			/** Bid Date in Header Box **/
			Phrase aShipTo = new Phrase(" SHIP TO: ", theBoldFont);
			aShipTo.add(new Phrase(aShipToAddress.replaceAll("null", ""),
					theNormalFont));
			pdfShipToCell.addElement(aShipTo);
			pdfShipToCell.addElement(aEmpty);
			aTableBillTo.addCell(pdfShipToCell);
			theDocument.add(aTableBillTo);

			Paragraph aParagraph = null;
			if (theUserLoginSetting.getTermsPurchaseOrders() == 1) {
				aParagraph = new Paragraph();
				String aHtmlString = theUserLoginSetting.getTerms();
				String aText1 = aHtmlString.replaceAll("`and`amp;", "&");
				String aText2 = aText1.replaceAll("`and`nbsp;", " ");
				String aText3 = aText2 + "\n\n\n";
				ArrayList aInputString = new ArrayList();
				StringReader aStrReader = new StringReader(aText3);
				aInputString = HTMLWorker.parseToList(aStrReader, null);
				for (int k = 0; k < aInputString.size(); ++k) {
					aParagraph.add((com.lowagie.text.Element) aInputString
							.get(k));
				}
				aParagraph.setAlignment(Element.ALIGN_CENTER);
				aParagraph.setSpacingAfter(5);
				aParagraph = new Paragraph("   ");
				theDocument.add(aParagraph);
			} else {
				aParagraph = new Paragraph();
				aParagraph.setAlignment(Element.ALIGN_CENTER);
				aParagraph.setSpacingAfter(5);
				aParagraph = new Paragraph("   ");
				theDocument.add(aParagraph);
			}
			/*PdfPTable aSpaceTable = new PdfPTable(1);
			PdfPCell pdfSpaceCell = new PdfPCell();
			pdfSpaceCell.setBorder(Rectangle.NO_BORDER);
			Phrase aSpaceTo = new Phrase(" ", theBoldFont);
			aSpaceTo.add(new Phrase("", theNormalFont));
			pdfSpaceCell.addElement(aSpaceTo);
			aSpaceTable.addCell(pdfSpaceCell);
			theDocument.add(aSpaceTable);*/
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			
		
		}
		finally
		{
			 aVendorAddress = null;
			 aShipToAddress = null;
			 aBilltoAddress = null;
			 aVendorName = null;
			 aFrieght = null;
			 aShipVia = null;
			 aOrderBy = null;
			 aOrderByDate = null;
		}
	}

	public void getQuotesLineItems(Document thePDFDocument, Font theBoldFont,
			Font theNormalFont, Font theBoldTitleFont, Integer theVePoId,
			List<Vepodetail> theVePODetail) throws PDFException {
		
		String aDescription = "";
		String aItemCode = "";
		String aQty = "";
		String aUnitCostTot = "";
		String aPriceMulti = "";
		String aTotalTot = null;
		String aExtList = "";
		String aNetCaostTotal = "";
		
		try {
			// float[] aWidths = {0.4f,1.10f, 3.2f, 0.70f, 0.99f, 0.70f, 1.0f};
			float[] aWidths = { 1.40f, 2.7f, 1.0f, 0.90f, 0.70f, 0.89f, 1.3f };
			PdfPTable aPdfPTable = new PdfPTable(aWidths);
			aPdfPTable.setWidthPercentage(100);
			aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			aPdfPTable.getDefaultCell().setHorizontalAlignment(
					Element.ALIGN_LEFT);
			aPdfPTable.addCell(new Phrase("ITEM", theBoldTitleFont));
			aPdfPTable.getDefaultCell().setHorizontalAlignment(
					Element.ALIGN_LEFT);
			aPdfPTable.addCell(new Phrase("DESCRIPTION", theBoldTitleFont));
			aPdfPTable.getDefaultCell().setHorizontalAlignment(
					Element.ALIGN_CENTER);
			aPdfPTable.addCell(new Phrase("QTY", theBoldTitleFont));
			aPdfPTable.getDefaultCell().setHorizontalAlignment(
					Element.ALIGN_LEFT);
			aPdfPTable.addCell(new Phrase("LIST", theBoldTitleFont));
			/*
			 * aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT
			 * ); aPdfPTable.addCell(new Phrase("EXT LIST", theBoldTitleFont));
			 */
			aPdfPTable.getDefaultCell().setHorizontalAlignment(
					Element.ALIGN_LEFT);
			aPdfPTable.addCell(new Phrase("MULT.", theBoldTitleFont));
			aPdfPTable.getDefaultCell().setHorizontalAlignment(
					Element.ALIGN_RIGHT);
			aPdfPTable.addCell(new Phrase("NETEA", theBoldTitleFont));
			aPdfPTable.getDefaultCell().setHorizontalAlignment(
					Element.ALIGN_CENTER);
			aPdfPTable.addCell(new Phrase("TOTAL", theBoldTitleFont));
			thePDFDocument.add(aPdfPTable);
			PdfPTable aPdfPTableValues = new PdfPTable(aWidths);
			aPdfPTableValues.setWidthPercentage(100);
			aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
			BigDecimal aTotal = new BigDecimal(0);
		
			BigDecimal aQuantityOrder = null;
			Integer aIndex = 0;
			BigDecimal aNetCast = new BigDecimal(0);
			
			if (!theVePODetail.isEmpty()) {
				Format format = NumberFormat.getCurrencyInstance(new Locale(
						"en", "us"));
				for (int index = 0; index < theVePODetail.size(); index++) {
					BigDecimal aUnitCost = theVePODetail.get(index)
							.getUnitCost();
					BigDecimal aPriceMult = theVePODetail.get(index)
							.getPriceMultiplier();
					BigDecimal aQuantity = theVePODetail.get(index)
							.getQuantityOrdered();
					if (theVePODetail.get(index).getDescription() != null
							&& theVePODetail.get(index).getDescription() != "") {
						aDescription = theVePODetail.get(index)
								.getDescription();
					}
					if (theVePODetail.get(index).getNote() != null
							&& theVePODetail.get(index).getNote() != "") {
						aItemCode = theVePODetail.get(index).getNote();
						
						aDescription = aDescription.replace(aItemCode, "");
						
					}
					if (theVePODetail.get(index).getQuantityOrdered() != null) {
						aQuantityOrder = theVePODetail.get(index)
								.getQuantityOrdered();
						aQty = aQuantityOrder.toString().substring(0, aQuantityOrder.toString().indexOf("."));
					}
					if (theVePODetail.get(index).getUnitCost() != null) {
						aUnitCostTot = format.format(theVePODetail.get(index)
								.getUnitCost());
					}
					if (theVePODetail.get(index).getPriceMultiplier() != null) {
						aPriceMulti = theVePODetail.get(index)
								.getPriceMultiplier().toString();
					} else {
						aPriceMulti = "";
					}

					if (aUnitCost != null && aPriceMult != null
							&& aQuantity != null) {
						aTotal = aUnitCost.multiply(aPriceMult);
						aTotal = aTotal.multiply(aQuantity);
					} else if (aUnitCost != null && aQuantity != null) {
						aTotal = aUnitCost.multiply(aQuantity);
					} else if (aUnitCost != null && aPriceMult != null) {
						aTotal = aUnitCost.multiply(aQuantity);
					} else if (aUnitCost != null) {
						aTotal = theVePODetail.get(index).getUnitCost();
					}
					if (theVePODetail.get(index).getUnitCost() != null) {
						aExtList = format.format(aQuantityOrder
								.multiply(theVePODetail.get(index)
										.getUnitCost()));
					} 
					if (aUnitCost != null && aPriceMult != null) {
						if (Double.valueOf(aPriceMult.toString()) <= Double
								.valueOf(0.0000)) {
							aNetCaostTotal = format.format(aUnitCost);
						} else {
							aNetCast = aUnitCost.multiply(aPriceMult);
							aNetCaostTotal = format.format(aNetCast);
						}
					} else {
						aNetCaostTotal = "";
					}
					aTotalTot = format.format(aTotal);
					aIndex = aIndex + 1;
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(
							Element.ALIGN_LEFT);
					aPdfPTableValues.addCell(new Phrase(aItemCode,
							theNormalFont));
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(
							Element.ALIGN_LEFT);
					aPdfPTableValues.addCell(new Phrase(aDescription,
							theNormalFont));
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(
							Element.ALIGN_CENTER);
					aPdfPTableValues.addCell(new Phrase(aQty, theNormalFont));
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(
							Element.ALIGN_CENTER);
					aPdfPTableValues.addCell(new Phrase(aUnitCostTot,
							theNormalFont));
					/*
					 * aPdfPTableValues.getDefaultCell().setHorizontalAlignment(
					 * Element.ALIGN_LEFT); aPdfPTableValues.addCell(new
					 * Phrase(aExtList.toString(), theNormalFont));
					 */
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(
							Element.ALIGN_CENTER);
					aPdfPTableValues.addCell(new Phrase(aPriceMulti,
							theNormalFont));
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(
							Element.ALIGN_RIGHT);
					aPdfPTableValues.addCell(new Phrase(aNetCaostTotal,
							theNormalFont));
					if (aTotal != null) {
						aPdfPTableValues.getDefaultCell()
								.setHorizontalAlignment(Element.ALIGN_RIGHT);
						aPdfPTableValues.addCell(new Phrase(aTotalTot,
								theNormalFont));
					} else {
						aPdfPTableValues.getDefaultCell()
								.setHorizontalAlignment(Element.ALIGN_RIGHT);
						aPdfPTableValues.addCell(new Phrase("", theNormalFont));
					}
					// thePDFDocument.add(aPdfPTableValues);
					/*
					 * Paragraph aParagraph = null; String aInlineNote =
					 * theVePODetail.get(index).getInLineNote();
					 * if(aInlineNote!= null){ aParagraph = new Paragraph();
					 * String aHtmlString = aInlineNote; String aText1 =
					 * aHtmlString.replaceAll("`and`amp;", "&"); String aText2 =
					 * aText1.replaceAll("`and`nbsp;", " "); String aText3 =
					 * aText2+"\n\n"; ArrayList p=new ArrayList(); StringReader
					 * aStrReader = new StringReader(aText3); p =
					 * HTMLWorker.parseToList(aStrReader, null); for (int k = 0;
					 * k < p.size(); ++k){
					 * aParagraph.add((com.lowagie.text.Element)p.get(k)); }
					 * aParagraph.setAlignment(Element.ALIGN_LEFT);
					 * aParagraph.setIndentationLeft(20);
					 * aParagraph.setIndentationRight(90);
					 * thePDFDocument.add(aParagraph); }
					 */
					String aInlineNote = theVePODetail.get(index)
							.getInLineNote();
					if(null != aInlineNote)
						aInlineNote = aInlineNote.replace(aItemCode+"&^&", "");
					
					if (aInlineNote != null) {
						String text1 = aInlineNote;
						String text3 = text1 + "\n\n";
						if (text3.contains("<li>")) {
							text3 = text3.replaceAll("<br>", "");
						}
						ArrayList p = new ArrayList();
						StringReader strReader = new StringReader(text3);
						p = HTMLWorker.parseToList(strReader, null);
						PdfPCell inlineNote = new PdfPCell();
						inlineNote.setPaddingRight(0f);
						inlineNote.setPaddingLeft(94f);
						inlineNote.setPaddingTop(-8f);
						inlineNote.setColspan(10);
						inlineNote.setLeading(0, 0.8f);
						inlineNote.setBorder(Rectangle.NO_BORDER);
						for (int k = 0; k < p.size(); ++k) {
							inlineNote.addElement((com.lowagie.text.Element) p
									.get(k));
						}
						aPdfPTableValues.addCell(inlineNote);
					}
				}
			}
			thePDFDocument.add(aPdfPTableValues);
		} catch (Exception e) {
			itsLogger.error(e.getMessage());
			throw new PDFException(e.getMessage(), e);
		}
		finally
		{
			 aDescription = null;
			 aItemCode = null;
			 aQty = null;
			 aUnitCostTot = null;
			 aPriceMulti = null;
			 aTotalTot = null;
			 aExtList = null;
			 aNetCaostTotal = null;
		}
	}

	public void diplayFooter(Document thePDFDocument, Font theBoldFont,
			Font theNormalFont, Font theBoldTitleFont,
			HttpServletResponse aResponse, Vepo theVePO,
			PdfWriter thePdfWriter, List<Vepodetail> theVePODetail) {
		try {
			Format format = NumberFormat.getCurrencyInstance(new Locale("en",
					"us"));
			PdfContentByte cb1 = thePdfWriter.getDirectContent();
			/** Special instr. **/
			cb1.roundRectangle(30f, 30f, 340f, 85f, 10f);
			/** subtotal **/
			cb1.roundRectangle(380f, 30f, 180f, 85f, 10f);
			cb1.stroke();
			Phrase aParseText = new Phrase("  SPECIAL INSTRUCTIONS: ",
					theBoldFont);
			aParseText.add(new Phrase(theVePO.getSpecialInstructions(),
					theNormalFont));
			PdfPTable table2 = new PdfPTable(new float[] { 0.4f, 1.5f, 0.8f,
					0.001f });
			table2.setTotalWidth(thePDFDocument.getPageSize().getWidth());
			PdfPCell cel = new PdfPCell();
			cel.setBorder(Rectangle.NO_BORDER);
			PdfPCell cell = new PdfPCell(aParseText);
			cell.setBorder(Rectangle.NO_BORDER);
			PdfPCell aTotalCell = new PdfPCell();
			aTotalCell.setBorder(Rectangle.NO_BORDER);
			PdfPCell cell1 = new PdfPCell();
			cell1.setBorder(Rectangle.NO_BORDER);
			
			double aPOTotalInt = 0;
			BigDecimal aTotalSub = new BigDecimal(0);
			Double aTotalSubInt = 0.00;
			String aSubTotal = "$0.00";
			String aFreightCost = "$0.00";
			String aTaxRate = "$0.00";
			String aPOTotal = "$0.00";
			String aTotalInt = "";
			if (!theVePODetail.isEmpty()) {
				for (int index = 0; index < theVePODetail.size(); index++) {
					BigDecimal aUnitCost = theVePODetail.get(index)
							.getUnitCost();
					BigDecimal aPriceMult = theVePODetail.get(index)
							.getPriceMultiplier();
					BigDecimal aQuantity = theVePODetail.get(index)
							.getQuantityOrdered();
					if (aUnitCost != null && aPriceMult != null
							&& aQuantity != null) {
						aTotalSub = aUnitCost.multiply(aPriceMult);
						aTotalSub = aTotalSub.multiply(aQuantity);
					} else if (aUnitCost != null && aQuantity != null) {
						aTotalSub = aUnitCost.multiply(aQuantity);
					} else if (aUnitCost != null && aPriceMult != null) {
						aTotalSub = aUnitCost.multiply(aQuantity);
					} else if (aUnitCost != null) {
						aTotalSub = theVePODetail.get(index).getUnitCost();
					}
					aTotalInt = aTotalSub.toString();
					Double aname = Double.valueOf(aTotalInt);
					aTotalSubInt += aname;
				}
				aSubTotal = format.format(aTotalSubInt);
			}
			if (theVePO.getFreight() != null) {
				aFreightCost = format.format(theVePO.getFreight());
			}
			if (theVePO.getTaxTotal() != null) {
				aTaxRate = format.format(theVePO.getTaxTotal());
			}
			if (theVePO.getTaxTotal() != null) {
				Double asub = aTotalSubInt;
				String aFright = "";
				String aTax = "";
				Double aFrightChanges = new Double(0);
				Double aTaxChanges = new Double(0);
				BigDecimal afreight = theVePO.getFreight();
				BigDecimal atax = theVePO.getTaxTotal();
				if (afreight != null) {
					aFright = afreight.toString();
					aFrightChanges = Double.valueOf(aFright);
				}
				if (atax != null) {
					aTax = atax.toString();
					aTaxChanges = Double.valueOf(aTax);
				}
				aPOTotalInt = asub + aFrightChanges + aTaxChanges;
				aPOTotal = format.format(aPOTotalInt);
			}
			aTotalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			Phrase aSubtotal = new Phrase("  SUBTOTAL: ", theBoldFont);
			aSubtotal.add(new Phrase(aSubTotal, theNormalFont));
			Phrase aFreight = new Phrase("   FREIGHT: ", theBoldFont);
			aFreight.add(new Phrase(aFreightCost, theNormalFont));
			Phrase aSalextax = new Phrase(" SALES TAX: ", theBoldFont);
			aSalextax.add(new Phrase(aTaxRate, theNormalFont));
			Phrase aPototal = new Phrase("  PO TOTAL: ", theBoldFont);
			aPototal.add(new Phrase(aPOTotal, theNormalFont));
			Phrase aEmpty = new Phrase(" ", theBoldFont);
			table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			aTotalCell.addElement(aSubtotal);
			aTotalCell.addElement(aFreight);
			aTotalCell.addElement(aSalextax);
			aTotalCell.addElement(aPototal);
			aTotalCell.addElement(aEmpty);
			cel.setBorderColor(Color.WHITE);
			table2.addCell(cel);
			cell.setPaddingBottom(0f);
			cell.setPaddingTop(8f);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table2.addCell(cell);
			cell1.setPaddingBottom(10f);
			cell1.setPaddingTop(8f);
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			aTotalCell.setPaddingBottom(0f);
			aTotalCell.setPaddingTop(0f);
			aTotalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table2.addCell(aTotalCell);
			table2.addCell(cell1);
			table2.setWidthPercentage(100);
			table2.setHorizontalAlignment(Element.ALIGN_CENTER);
			table2.writeSelectedRows(-25, -25, -35, 110, cb1);

			/** Footer Info **/
			Phrase aParsefootText = new Phrase(
					" INVOICES RECEIVED AFTER THE 25TH WILL BE TREATED AS IF DATED THE 1ST OF THE NEXT MONTH. ",
					theNormalFont);
			PdfPTable tablefoot = new PdfPTable(new float[] { 0.4f, 3.0f,
					0.001f });
			tablefoot.setTotalWidth(thePDFDocument.getPageSize().getWidth());
			PdfPCell celfoot = new PdfPCell();
			PdfPCell cellfoot = new PdfPCell(aParsefootText);
			PdfPCell cell1foot = new PdfPCell();
			celfoot.setBorderColor(Color.WHITE);
			cellfoot.setBorderColor(Color.WHITE);
			cell1foot.setBorderColor(Color.WHITE);
			tablefoot.addCell(celfoot);
			cell.setPaddingBottom(0f);
			cell.setPaddingTop(0f);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablefoot.addCell(cellfoot);
			cell1.setPaddingBottom(0f);
			cell1.setPaddingTop(0f);
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			tablefoot.addCell(cell1foot);
			tablefoot.setWidthPercentage(100);
			tablefoot.setHorizontalAlignment(Element.ALIGN_CENTER);
			tablefoot.writeSelectedRows(-25, -25, -35, 25, cb1);

		} catch (Exception e) {
			itsLogger.error(e.getMessage());
		}
	}

	public void getQuotesAck(Document thePDFDocument, Font theBoldFont,
			Font theNormalFont, Font theBoldTitleFont, Integer theVePoId,
			List<Vepodetail> theVePODetail) {
		
		String aDescription = "";
		String aItemCode = "";
		String aOrdernumber = "";
		String aShipDate = "";
		String aQuantityStr = "";
		try {
		//	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			float[] aWidths = { 0.4f, 0.90f, 1.50f, 0.70f, 0.70f };
			PdfPTable aPdfPTable = new PdfPTable(aWidths);
			aPdfPTable.setWidthPercentage(100);
			aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			aPdfPTable.getDefaultCell().setHorizontalAlignment(
					Element.ALIGN_CENTER);
			aPdfPTable.addCell(new Phrase("QTY", theBoldTitleFont));
			aPdfPTable.getDefaultCell().setHorizontalAlignment(
					Element.ALIGN_LEFT);
			aPdfPTable.addCell(new Phrase("ITEM", theBoldTitleFont));
			aPdfPTable.getDefaultCell().setHorizontalAlignment(
					Element.ALIGN_LEFT);
			aPdfPTable.addCell(new Phrase("DESCRIPTION", theBoldTitleFont));
			aPdfPTable.getDefaultCell().setHorizontalAlignment(
					Element.ALIGN_CENTER);
			aPdfPTable.addCell(new Phrase("ORDER NUMBER", theBoldTitleFont));
			aPdfPTable.getDefaultCell().setHorizontalAlignment(
					Element.ALIGN_CENTER);
			aPdfPTable.addCell(new Phrase("SHIP DATE", theBoldTitleFont));
			thePDFDocument.add(aPdfPTable);
			PdfPTable aPdfPTableValues = new PdfPTable(aWidths);
			aPdfPTableValues.setWidthPercentage(100);
			aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
			BigDecimal aTotal = null;
		
			Integer aIndex = 0;
			BigDecimal aQty = null;
	
			if (!theVePODetail.isEmpty()) {
				for (int index = 0; index < theVePODetail.size(); index++) {
					BigDecimal aUnitCost = theVePODetail.get(index)
							.getUnitCost();
					BigDecimal aPriceMult = theVePODetail.get(index)
							.getPriceMultiplier();
					BigDecimal aQuantity = theVePODetail.get(index)
							.getQuantityOrdered();
					if (theVePODetail.get(index).getDescription() != null
							&& theVePODetail.get(index).getDescription() != "") {
						aDescription = theVePODetail.get(index)
								.getDescription();
					} else {
						aDescription = "";
					}
					if (theVePODetail.get(index).getNote() != null
							&& theVePODetail.get(index).getNote() != "") {
						aItemCode = theVePODetail.get(index).getNote();
					} else {
						aItemCode = "";
					}
					if (theVePODetail.get(index).getQuantityOrdered() != null) {
						aQty = theVePODetail.get(index).getQuantityOrdered();
						aQuantityStr = aQty.toString().replaceAll(".0000", "");
					} else {
						aQuantityStr = "";
					}
					if (theVePODetail.get(index).getVendorOrderNumber() != null) {
						aOrdernumber = theVePODetail.get(index)
								.getVendorOrderNumber();
					} else {
						aOrdernumber = "";
					}
					if (theVePODetail.get(index).getShipDate() != null) {
						aShipDate = theVePODetail.get(index).getShipDate();
					} else {
						aShipDate = "";
					}
					if (aUnitCost != null && aPriceMult != null
							&& aQuantity != null) {
						aTotal = aUnitCost.multiply(aPriceMult);
						aTotal = aTotal.multiply(aQuantity);
					} else if (aUnitCost != null && aQuantity != null) {
						aTotal = aUnitCost.multiply(aQuantity);
					} else if (aUnitCost != null && aPriceMult != null) {
						aTotal = aUnitCost.multiply(aQuantity);
					} else if (aUnitCost != null) {
						aTotal = theVePODetail.get(index).getUnitCost();
					}
					aIndex = aIndex + 1;
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(
							Element.ALIGN_CENTER);
					aPdfPTableValues.addCell(new Phrase(aQuantityStr,
							theNormalFont));
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(
							Element.ALIGN_LEFT);
					aPdfPTableValues.addCell(new Phrase(aItemCode,
							theNormalFont));
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(
							Element.ALIGN_LEFT);
					aPdfPTableValues.addCell(new Phrase(aDescription,
							theNormalFont));
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(
							Element.ALIGN_LEFT);
					aPdfPTableValues.addCell(new Phrase(aOrdernumber,
							theNormalFont));
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(
							Element.ALIGN_CENTER);
					aPdfPTableValues.addCell(new Phrase(aShipDate,
							theNormalFont));
				}
			}
			thePDFDocument.add(aPdfPTableValues);
		} catch (Exception e) {
			itsLogger.error(e.getMessage());
		}
		finally
		{
			 aDescription = null;
			 aItemCode = null;
			 aOrdernumber = null;
			 aShipDate = null;
			 aQuantityStr = null;
		}
	}

	public void diplayAckFooter(Document thePDFDocument, Font theBoldFont,
			Font theNormalFont, Font theBoldTitleFont,
			HttpServletResponse aResponse, Vepo theVePO, PdfWriter thePdfWriter) {
		
		
		try {
			
			PdfContentByte cb1 = thePdfWriter.getDirectContent();
			cb1.roundRectangle(30f, 20f, 530f, 60f, 10f);
			cb1.stroke();
			Phrase aParseText = new Phrase("  SPECIAL INSTRUCTIONS: ",
					theBoldFont);
			Phrase aParseText1 = new Phrase(theVePO.getSpecialInstructions(),
					theNormalFont);
			PdfPTable table2 = new PdfPTable(new float[] { 0.4f, 3.0f, 0.001f });
			table2.setTotalWidth(thePDFDocument.getPageSize().getWidth());
			table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			PdfPCell cel = new PdfPCell();
			cel.setBorder(Rectangle.NO_BORDER);
			PdfPCell cell = new PdfPCell(aParseText);
			cell.setBorder(Rectangle.NO_BORDER);
			PdfPCell cell1 = new PdfPCell(aParseText1);
			cell1.setBorder(Rectangle.NO_BORDER);
			cel.setBorderColor(Color.WHITE);
			table2.addCell(cel);
			cell.setPaddingBottom(40f);
			cell.setPaddingTop(8f);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table2.addCell(cell);
			cell1.setPaddingBottom(10f);
			cell1.setPaddingTop(8f);
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table2.addCell(cell1);
			table2.setWidthPercentage(100);
			table2.setHorizontalAlignment(Element.ALIGN_CENTER);
			table2.writeSelectedRows(-25, -25, -35, 80, cb1);
		} catch (Exception e) {
			itsLogger.error(e.getMessage());
		}
	}

}