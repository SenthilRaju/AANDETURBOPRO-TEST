package com.turborep.turbotracker.Inventory.service;
public class InventoryConstant {
	static enum MasterSettings {
				CompanySettings(2014001000),
				CustomerSettings(2014002000),
				VendorSettings(2014003000),
				EmployeeSettings(2014004000),
				JobSettings(2014005000),
				InventorySettings(2014006000),
				BankingSettings(2014007000),
				CommissionCalculations(2014008000),
				//CompanySettings
				
				//CustomerSettings
				StatementsshallbegroupedbyJob(2014002001),
				StatementsshallshowBillingRemainder(2014002002),
				CheckcreditlimitinSalesOrderOutsideofJob(2014002003),
				CheckcreditlimitinQuickBook(2014002004),
				RequireDivisioninSalesOrderOutsideofJob(2014002005),
				RequireDivisioninCustomerInvoice(2014002006),
				IncludeSalesTaxwhencalculatingdiscounts(2014002007),
				IncludeFreightwhencalculatingdiscounts(2014002008),
				UseDivisionaddressinPickTickets(2014002009),
				AllowblanklineitemsinSalesOrderInsideOutsideofJob(2014002010),
				AllowblanklineitemsinCustomerInvoice(2014002011),
				AllowblankProductItemCodeinSalesOrderInsideOutsideofJob(2014002012),
				RequirePromiseDateinSalesOrderoutsideofJob(2014002013),
				RequireFreightwhencalculatingTaxonCustomerInvoices(2014002014),
				RequireNewNumbersForCuInvoices(2014002015),
				IncludeListcolumnoninvoices(2014002016),
				IncludeExtListcolumnoninvoices(2014002017),
				IncludeMultcolumnoninvoices(2014002018),
				RemoveEXTLISTcolumnfromSalesOrderPDF(2014002019),
				RemoveMULTcolumnfromSalesOrderPDF(2014002020),
				DoNotAllowTaxTerritoryAfterSavingCustomerInvoice(2014002021),
				
				//VendorSettings
				DefaultPODescItemCode(2014003001),
				UseCompanyLogoAndLbl(2014003002),
				ShwVndrPNonPO(2014003003),
				ShwVndrPIDonPO(2014003004),
				AskClPOwnItemsRecieved(2014003005),
				ReqInvWithPO(2014003006),
				VndrInvDfltOnHold(2014003007),
				IncTaxOnPOAndInvoices(2014003008),
				
				//EmployeeSettings
				InvoicesCombinedforJobs(2014004001),
				AdjustmentDescription1(2014004002),
				AdjustmentDescription2(2014004003),
				AdjustmentDescription3(2014004004),
				AdjustmentDescription4(2014004005),
				CommissionDescription1(2014004006),
				CommissionDescription2(2014004007),
				CommissionDescription3(2014004008),
				CommissionDescription4(2014004009),
				AllowEmployeestoviewtheirownCommissionStatements(2014004010),
				Onlyshowallocatedprofitonsplitcommissions(2014004011),
				ApplyCreditswhenUsed(2014004012),
				CommissionSpecialCode(2014004013),
				CommissionOnGross(2014004014),
			    CommissionOnInvoiceDate(2014004015),
			    PercentageProfit(2014004016),
			    TakeCreditsWhenUsed(2014004017),
				
				//JobSettings
			    AllowchangingJob(2014005001),
			    OptionalJobQuoteCheckoff(2014005002),
			    OptionalMemoFieldonQuote(2014005003),
			    JobNoticelabel1(2014005004),
			    JobNoticeReportType1(2014005005),
			    JobNoticelabel2(2014005006),
			    JobNoticeReportType2(2014005007),
			    JobNoticelabel3(2014005008),
			    JobNoticeReportType3(2014005009),
			    DefaultBidDatetoCurrentDate(2014005010),
			    DefaultTakeOfftoUsercreatingtheJob(2014005011),
			    DefaultSalesReptoUserCreatingtheJob(2014005012),
			    DefaultDivisiontoUserCreatingtheJob(2014005013),
			    //Require Received and Quote Thru in Addendum box (Changed the label like this)
			    Musthaveaddendumquotethruandbiddatebeforesave(2014005014),
			    DefaulttoApprovedcreditforanewJob(2014005015),
			    DefaultQuoteBytoUSercreatingtheJob(2014005016),
			    JobDetailsOtherlabel(2014005017),
			    AllowEditingofSentQuotes(2014005018),
			    
			    
			    JobDetailsReportlabel(2014005019),
			    Applycoststocustomerinvoice(2014005020),
			    Closeoutlastinvoicecheckdaysold(2014005021),
			    RequireaDivisionwhencreatingJobs(2014005022),
			    UseCustomersCreditLimitwhencreatingJobs(2014005023),
			    AllowbookingJobswithnoContractAmount(2014005024),
			    ImportPurchaseOrderdetailsintonewCustomerInvoices(2014005025),
			    ShowBillingRemainderonJobStatements(2014005026),
			    HidePostponedoptioninJobs(2014005027),
			    OnlyallowownersorsupervisorstochangeQuoteTemplates(2014005028),
			    AllowreleasesifFinalWaiverchecked(2014005029),
			    WarnifNOCnotreceivedwhenordering(2014005030),
			    TrackCostonChangeOrders(2014005031),
			    ShowBranches(2014005032),
			    ShowcreditsonJobStatements(2014005033),
			    RequireaEngineerwhenbookingJobs(2014005034),
			    PlanSpecLabel1(2014005035),
			    PlanSpecLabel2(2014005036),
			    
			    SourceCheckBox1(2014005037),
			    SourceCheckBox2(2014005038),
			    SourceCheckBox3(2014005039),
			    SourceCheckBox4(2014005040),
			    SourceLabel1(2014005041),
			    SourceLabel2(2014005042),
			    
			    JobNoticelabel1Txt(2014005043),
			    JobNoticeReportType1Txt(2014005044),
			    JobNoticelabel2Txt(2014005045),
			    JobNoticeReportType2Txt(2014005046),
			    JobNoticelabel3Txt(2014005047),
			    JobNoticeReportType3Txt(2014005048),
			    JobDetailsReportlabelTxt(2014005049),
			    CloseoutlastinvoicecheckdaysoldDays(2014005050),
			    DefaultJobTaxTerritorytoCustomerTax(2014005051),
//			    UseCustomerscreditlimitwhencreatingjobs(2014005052),
			    SplitCommissionRequiredOnRelease(2014005053),
			    OverrideReleaseTaxTerritory(2014005054),
			    
			    //Quote Settings
			    QuotechkI2I3QtyYN(2014005055),
			    QuotechkI2I3CostYN(2014005056),
			    QuotechkI3SellPriceYN(2014005057),
			    QuotechkI2I3ManufYN(2014005058),
			    QuotechkI2I3CatYN(2014005059),
			    QuotechkSPpriceYN(2014005060),
				
			    CustomerPOReqYN(2014005061),
			    //Added by Leo ID#485
			    QuotechkfontsizepriceYN(2014005062),
			    
				//Inventory Settings
				OverrideallWarehousingCostMarkupinsideCategory(2014006001),
				SpecifyPurchaseOrdercost(2014006002),
				MultipleWarehousesAverageCosting(2014006003),
				ShowBinLocationonPickTickets(2014006004),
				ShowWeightonPickTickets(2014006005),
				UseWarehousesaddressonPickTickets(2014006006)
				
				
				//BankingSetting
				
				
				;
				
				
		Integer sysvariableid;

		MasterSettings(Integer sysvariableid) {
			this.sysvariableid = sysvariableid;
		}

		public Integer getSysvariableid() {
			return sysvariableid;
		}

		public void setSysvariableid(Integer sysvariableid) {
			this.sysvariableid = sysvariableid;
		}
	}

	public static Integer getConstantSysvariableId(String menu) {
		return MasterSettings.valueOf(menu).getSysvariableid();
	}

}
