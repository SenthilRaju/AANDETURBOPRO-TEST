	<div id="addvendor">
		<div class="tabs_main" style="padding-left: 0px;">
			<ul>
				<li><a href="#vendorGeneral">General</a></li>
				<li><a href="#vendorJournal">Journal</a></li>
				<li id="rolodexcustomer"><a  href="#rolodexCustomer">Customer</a></li>
				<li id="rolodexvendor"><a  href="#rolodexVendor">Vendor</a></li>
				<li id="employee"><a href="#rolodexEmployee">Employee</a></li>
				<li id="architect"><a href="#rolodexArchitect">Architect</a></li>
				<li id="engineer"><a href="#rolodexengineer">Engineer</a></li>
				
			</ul>
		<div id="vendorGeneral">
			<table>
				<tr><td><jsp:include page="vendorheader.jsp"></jsp:include></td></tr>
				<tr><td colspan="2"><hr width="850px"></td></tr>
			</table>
			<table>
				<tr><td style="padding-left: 0px;vertical-align: top;">
        			<fieldset class= " ui-widget-content ui-corner-all" style="width:200px">
               		<legend><label><b>Address</b></label></legend>          
			        <table >
			        	<tr><td><textarea rows="5" cols="24"></textarea></td></tr>
			        </table>
			        </fieldset>
			        <br>
			        <table>
			        	<tr><td><label><b>Phone</b></label></td><td><input type="text"></td></tr>
			         	<tr><td><label><b>Fax</b></label></td><td><input type="text"></td></tr>
					</table>
					<br>
			 	</td>
			 	
			 	<td style="width:100px">
				<td style="padding-left: 0px;vertical-align: top;">
				<fieldset class= " ui-widget-content ui-corner-all" style="width:300px">
				<legend><label><b>Categories</b></label></legend>
				    <table style="vertical-align: top;">
				    	<tr><td><input type="checkbox"><label>Prospect</label></td><td><input type="checkbox" id="architectche" onclick="architect()"><label>Architect</label></td><td><input type="checkbox"><label>Bond Agent</label></td></tr>
						<tr><td><input type="checkbox" id="customerche" onclick="customer()"><label>Customer</label></td><td><input type="checkbox" id="engineerche" onclick="engineer()"><label>Engineer</label></td><td><input type="checkbox"><label>Constr.Mgr.</label></td></tr>
						<tr><td><input type="checkbox" id="vendorche" onclick="vendorrolodex()"><label>Vendor</label></td><td><input type="checkbox"><label>GC</label></td></tr>
						<tr><td><input type="checkbox" id="employeeche" onclick="employee()"><label>Employee</label></td><td><input type="checkbox"><label>Owner</label></td></tr>
						
					</table>
				</fieldset>
				</td>
			 </tr>
		 </table>
			<table id="general" style="width:20px"></table><div id="vendorgeneralpager"></div>
		</div>
		
		<div id="vendorJournal">
			<table>
				<tr><td><jsp:include page="vendorheader.jsp"></jsp:include></td></tr>
				<tr><td colspan="2"><hr width="850px"></td></tr>
			</table>
			<table id="journal"></table><div id="vendorjournalpager"></div>
		</div>

		<div id="rolodexCustomer" >
			<table style="width:700px">
				<tr><td>
					<fieldset style="width:300px" class= " ui-widget-content ui-corner-all">
						<legend><label><b>Name</b></label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label><b>Active</b></label><input type="checkbox"></legend>
						<table>
							<tr><td colspan="2"><input type="text" style="width:200px"></td></tr>
							<tr><td><input type="checkbox"><label>First Name</label></td></tr>
						</table>
				
					</fieldset>
				</td>
				<td style="vertical-align: top">
				<fieldset style="width:350px" class= " ui-widget-content ui-corner-all">
				<legend><label><b>Customer Account #</b></label></legend>
					<table>
						<tr><td><input type="text"></td></tr>
						<tr><td><input type="checkbox"><label>Finance Change</label></td>
							<td><input type="checkbox"><label>Statement</label></td></tr>
					</table>
				</fieldset>
				</td></tr>
				<tr><td colspan="2"><hr width="850px"></td></tr>
			</table>
			<table>
				<tr><td style="vertical-align:top">
				 	<fieldset style="width:320px" class= " ui-widget-content ui-corner-all">
				 <legend><label><b>Tax Territories</b></label></legend>
					<table>
						<tr><td>
								<table>
									<tr><td><textarea rows="2" cols="20"></textarea></td>
								</table>
							</td>
							<td>
								<table>
									<tr>
										<td><select style="width:100px"></select>
									</tr>
								</table>		
							</td>
							</tr>
						</table>
					</fieldset>
					</td>
					
					<td style="vertical-align:top">
				<table>
					<tr><td>
					<fieldset style="width:180px" class= " ui-widget-content ui-corner-all">
						<legend><label><b>Price Level</b></label></legend>
						<table>
						  	<tr><td><select style="width:100px"><option></option></select></td></tr>
						</table>
					  </fieldset>
					  </td>
					  <td>
					  <fieldset style="width:180px" class= " ui-widget-content ui-corner-all">
					  <legend><label><b>Payment terms</b></label></legend>
					  <table>
					  <tr><td><select style="width:100px"><option></option></select></td></tr>
					  </table>
					  </fieldset>
					  </td>
					  </tr>
					  </table>
					</td></tr>
					</table>
					
					<table>
					<tr>
				<td style="vertical-align: top">
					<table>
					<tr><td>
					<fieldset style="width:145px" class= " ui-widget-content ui-corner-all">
						<legend><label><b>Customer Type</b></label></legend>
						<table>
							<tr><td><select style="width:75px"><option></option></select></td></tr>
						</table>
						</fieldset>
					</td>
					<td>
					<fieldset style="width:145px" class= " ui-widget-content ui-corner-all">
						<legend><label><b>Tax Exempt #</b></label></legend>
						<table>
						<tr><td><input type="text" style="width:75px"></td></tr>
						</table>
						</fieldset>
					</td>
					</table>
					</td>
					<td style="vertical-align: top">
					<table>
						<tr><td>
						<fieldset style="width:150px" class= " ui-widget-content ui-corner-all">
						<legend><label><b>Default Warehouse</b></label></legend>
						<table>
							<tr><td><select style="width:100px"><option></option></select></td></tr>
						</table>
						</fieldset >
						</td>
						<td>
						<fieldset style="width:130px" class= " ui-widget-content ui-corner-all">
						<legend><label><b>Price Multiplier</b></label></legend>
						<table>
							<tr><td><input type="text" style="width:75px"></td></tr>
						</table>
						</fieldset>
						</td></tr>
					</table>
					</td>
					
						<td><fieldset class= " ui-widget-content ui-corner-all"><input type="checkbox"><label>PO Required</label></fieldset></td>
				</tr>
			</table>
				
				
					<table>
					<tr>
					<td style="vertical-align:top">
					<fieldset class= " ui-widget-content ui-corner-all" style="width:210px;height:198px">
					<legend><label><b>Employee's Assigned</b></label></legend>
					<table>
						
						<tr><td><label>Salesrep</label></td><td><select style="width:75px"><option></option></select></td></tr>
						<tr><td><label>CSR</label></td><td><select style="width:75px"><option></option></select></td></tr>
						<tr><td><label>Sales Manager</label></td><td><select style="width:75px"><option></option></select></td></tr>
						<tr><td><label>Engineer</label></td><td><select style="width:75px"><option></option></select></td></tr>
						<tr><td><label>Project Manager</label></td><td><select style="width:75px"><option></option></select></td></tr>
					</table>
					</fieldset>
					</td>
					<td style="width:25px"> </td>
					
					<td style="vertical-align:top">
					<fieldset class= " ui-widget-content ui-corner-all" style="width:210px;height:198px">
			   		<legend><label><b>Customer Overall A/R</b></label></legend>
					<table>
						
						<tr><td><label>Current Due:</label></td><td>$0.00</td></tr>
						<tr><td><label>30 Days:</label></td><td>$0.00</td></tr>
						<tr><td><label>60 Days:</label></td><td>$0.00</td></tr>
						<tr><td><label>90 Days:</label></td><td>$0.00</td></tr>
						<tr><td><label>Total Due:</label></td><td>$0.00</td></tr>
						<tr><td><label>Avg Days to Pay Invoice:</label></td><td>0</td></tr>
						<tr><td><label>Since</label></td><td><input type="checkbox"></td></tr>
					</table>
					</fieldset>
				</td>
				<td style="width:25px"> </td>
				
			<td style="vertical-align:top">
			<table>
			<tr><td>
			
				<fieldset class= " ui-widget-content ui-corner-all" style="width:250px">
				<legend><label><b>Credit</b></label></legend>
					<table>
						
						<tr><td><label>Credit App:</label><input type="checkbox"></td></tr>
						<tr><td><label>Limit</label>&nbsp;<input type="text" style="width:75px">&nbsp;<input type="checkbox">Hold</td></tr>
						<tr><td><label>Override Credit Hold For Today</label><input type="checkbox"></td></tr>
					</table>
					</fieldset>
				
				</td></tr>
						
				<tr><td>
				<fieldset class= " ui-widget-content ui-corner-all" style="width:250px">
				<legend><label><b>Sales</b></label></legend>
					<table>
					
						<tr><td><label>YTD:</label></td><td>$0.00</td></tr>
						<tr><td><label>2011:</label></td><td>$0.00</td></tr>
						<tr><td><label>Last Sale:</label></td></tr>
					</table>
					</fieldset>
				</td></tr>
				
		 </table>
		 </td></tr>
		 </table>
		 
			<table>
			<tr><td style="vertical-align:top">
			<fieldset class= " ui-widget-content ui-corner-all" style="width:210px">
			<legend><label><b>Quote Method</b></label></legend>
				<table>
					<tr><td></td></tr>
					<tr><td><input type="checkbox"><label>Fax</label>&nbsp;<input type="checkbox"><label>Email</label>&nbsp;<input type="checkbox"><label>Mail</label></td></tr>
				</table>
				</fieldset>
				</td>
				
				<td style="width:25px"> </td>
				
				<td style="vertical-align:top">
				<fieldset class= " ui-widget-content ui-corner-all" style="width:210px">
				<legend><label><b>Invoicing Method</b></label></legend>
					<table>
						<tr><td></td></tr>
						<tr><td><input type="checkbox"><label>Fax</label>&nbsp;<input type="checkbox"><label>Email</label>&nbsp;<input type="checkbox"><label>Mail</label></td></tr>
					</table>
				</fieldset>
				</td>
				<td style="width:25px"> </td>
				
				<td style="vertical-align:top">
				<fieldset class= " ui-widget-content ui-corner-all" style="width:250px">
				<legend><label><b>Statement Method</b></label></legend>
					<table>
						<tr><td></td></tr>
						<tr><td><input type="checkbox"><label>Fax</label>&nbsp;<input type="checkbox"><label>Email</label>&nbsp;<input type="checkbox"><label>Mail</label></td></tr>
					</table>
					</fieldset>
					</td></tr>
				</table> 
		</div>
		
		<div id="rolodexVendor" >
			<table>
				<tr><td><jsp:include page="vendorheader.jsp"></jsp:include></td></tr>
				<tr><td colspan="2"><hr width="850px"></td></tr>
			</table>
					
			<table>
				<tr><td style="padding-left: 0px;vertical-align: top;">   
				  <fieldset class= " ui-widget-content ui-corner-all">
					<legend><label><b>Vendor Account #</b></label></legend>  
			        <table width="300px">	 
			        	<tr><td><input type="text"></td></tr>
			        </table>
			      </fieldset>
			        <br>
			      <fieldset class= " ui-widget-content ui-corner-all">
			      <legend><label><b>Payment Terms</b></label></legend>
			      <table width="300px">
			          <tr><td><label>Payment due:Net</label> &nbsp;<input type="text" size="3">
			          <select>
			          <option>days</option>
			          <option>month</option>
			          </select></td></tr>
			          <tr><td><label>Discount</label> &nbsp;<input type="text" size="3">%<input type="text" size="3">
			          <select>
			          <option>days</option>
			          <option>month</option>
			          </select></td></tr>
			          <tr><td><input type="checkbox"><label>Discount Includes Freight</label></td></tr>
			      </table>
			      </fieldset>
			      <br>	 
			      <fieldset class= " ui-widget-content ui-corner-all">
			      <legend><label><b>ID # / Social Sec #</b></label></legend>  
				  <table width="300px">
			          <tr><td><input type="text"></td></tr>
			      </table>
			      </fieldset>
			 </td>
			 <td style="width:35px"></td>
			 <td style="padding-left: 0px;vertical-align: top; width:350px">
			 	<fieldset class= " ui-widget-content ui-corner-all">
			 	<legend><label><b>Vendor Overall A/p</b></label></legend>
			       <table width="200px">
			          <tr><td><label>Current Due:</label></td><td>$0.00</td></tr>
			          <tr><td><label>30 Days:</label></td><td> $0.00</td></tr>
			          <tr><td><label>60 Days:</label> </td><td>           $0.00</td></tr>
			          <tr><td><label>90 Days:</label> </td><td>           $0.00</td></tr>
			          <tr><td><label>Total Due:</label></td><td>          $0.00</td></tr>
			          <tr><td><label>Avg Days to Pay Invoice:</label></td><td> 0</td></tr>
			       </table>
			     </fieldset>
				</td>
		 	</tr>
		</table>
		<table style="width:800px">
			  <tr><td>
			  		<fieldset class= " ui-widget-content ui-corner-all" style="width: 200px;">
			  		<legend><label><b>Default Expense Account</b></label></legend>
			         <table>
			         	<tr><td><select style="width: 155px;"><option></option></select></td></tr>
			         </table>
			         </fieldset>
			     </td>
			     <td>
			     <fieldset class= " ui-widget-content ui-corner-all" style="width: 200px;">
			     <legend><label><b>Factory Software</b></label></legend>
			     	<table>
			         	<tr><td><select><option>(none)</option></select></td></tr>
			        </table>
			     </fieldset>
			    </td>
			    <td>
			    	<fieldset class= " ui-widget-content ui-corner-all" style="width: 200px;">
			    	<legend><label><b>Alternate Manufacture</b></label></legend>
			    	<table>
			         	<tr><td><input type="checkbox"><label>Force Alternate Manufacture?</label></td></tr>
			        </table>
			        </fieldset>
			    </td>
			    </tr>
		</table>
		<table>
			<tr><td>
			    <div>
				<table id="vendor" style="width:20px"></table><div id="vendorpager1"></div>
				</div>
			 </td></tr>
    	</table>
		</div>
		<div id="rolodexEmployee" >
			<table style="width:700px">
				<tr><td>
					<fieldset style="width:300px" class= " ui-widget-content ui-corner-all">
						<legend><label>Name</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label>Active</label><input type="checkbox"></legend>
						<table>
							<tr><td colspan="2"><input type="text" style="width:200px"></td></tr>
							<tr><td><input type="checkbox"><label>First Name</label></td></tr>
						</table>
					</fieldset>
					</td>
				</tr>
			</table>
		   <table>
		   		<tr>
			    	<td colspan="3">
			          	  <div id="tabs_sub" style="width: 800px;">
							<ul>
								<li><a href="#tabsindex3">General</a></li>
								<li><a href="#tabsindex4">Directory</a></li>
								<li><a href="#tabsindex5">Commission</a></li>
								<li><a href="#tabsindex6">Payroll</a></li>
							</ul>
							<div id="tabsindex3">
									<table>
									  <tr>
									  	<td style="vertical-align: top">
									  		<fieldset style="width:300px" class= " ui-widget-content ui-corner-all">
									  		<legend><label><b>Info</b></label></legend>
									  		<table>
									  			<tr>
									  				<td><label>Division</label></td><td><select style="width: 75px"></select></td>
									  			</tr>
									  			<tr>
									  				<td><label>Login</label></td><td><select style="width: 75px"></select></td>
									  			</tr>
									  			<tr>
									  				<td><label>Status</label></td><td><select style="width: 75px"></select></td>
									  			</tr>
									  			<tr>
									  				<td><label>Type</label></td><td><select style="width:75px"></select></td>
									  			</tr>
									  			<tr>
									  				<td><label>Marital Status</label></td><td><select style="width:75px"></select></td>
									  			</tr>
									  			<tr>
									  				<td><label>Sex</label></td><td><select><option>Male</option><option>Female</option></select></td>
									  			</tr>
									  			<tr>
									  				<td><label>Social Sec #</label></td><td><select style="width:75px"></select></td>
									  			</tr>
									  			<tr>
									  				<td><label>Branch Manager</label><input type="checkbox"></td>
									  			</tr>
											</table>
											</fieldset>
										</td>
										<td style="vertical-align: top" >
										<fieldset style="width:300px" class= " ui-widget-content ui-corner-all">
										<legend><label><b>Dates</b></label></legend>
											<table>
												<tr>
													<td><label>Birth Date</label></td><td><input type="checkbox"></td>
												</tr>
												<tr>
													<td><label>Hire Date</label></td><td><input type="checkbox"></td>
												</tr>
												<tr>
													<td><label>Next Review</label></td><td><input type="checkbox"></td>
												</tr>
												<tr>
													<td><label>Last Raise</label></td><td><input type="checkbox"></td>
												</tr>
												<tr>
													<td><label>Termination</label></td><td><input type="checkbox"></td>
												</tr>
											</table>
										</fieldset>	
									</td>
								</tr>
							</table>
									<fieldset style="width:750px" class= " ui-widget-content ui-corner-all">
									<legend><label><b>Vacation & Sick Days</b></label></legend>
							 <table>
								 <tr>
								 	<td></td><td><label>Accrues</label></td><td><label>Starting</label></td><td><label>Hrs/Year</label></td><td><label>Maximum</label></td><td><label>Available</label></td><td><label>Carry Fwd</label></td>
								 </tr>
								 <tr>
								 	<td><label>Vacation</label></td><td><select style="width:50px"></select></td><td><select style="width:50px"></select></td><td><select style="width:50px"></select></td><td><select style="width:50px"></select></td><td><select style="width:50px"></select></td><td><input type="checkbox">(no)</td>
								 </tr>
								 <tr>
								 	<td><label>Sick Days</label></td><td><select style="width:50px"></select></td><td><select style="width:50px"></select></td><td><select style="width:50px"></select></td><td><select style="width:50px"></select></td><td><select style="width:50px"></select></td><td><input type="checkbox">(no)</td>
								 </tr>
																  		
									</table>
									</fieldset>	
									</div>
							<div id="tabsindex4">
								<fieldset style="width:750px" class= " ui-widget-content ui-corner-all">
								<legend><label><b>Employee Directory</b></label></legend>
									<table>
										<tr>
											<td><label>List In Directory?</label><input type="checkbox" style="vertical-align:bottom"></td>
										</tr>
										<tr>
											<td><label class="formLabel">Location: </label></td><td><input type="text" id="employeeDirectoryLocation"></td>
										</tr>
										<tr>
											<td><label class="formLabel">Position: </label></td><td><input type="text" id="employeeDirectoryPosition"></td>
										</tr>
										<tr>
											<td><label class="formLabel">Photo File: </label></td><td><input type="text" id="employeeDirectoryPhotoFile"></td>
										</tr>
										<tr>
											<td><label class="formLabel">Phone: </label></td><td><input type="text" id="employeeDirectoryPhone"></td>
										</tr>
										<tr>
											<td><label class="formLabel">Email: </label></td><td><input type="text" id="employeeDirectoryEmail"></td>
										</tr>
										<tr>
											<td><label class="formLabel">Comment: </label></td><td><input type="text" id="employeeDirectoryComment"></td>
										</tr>
									</table>
								</fieldset>
							</div>
							<div id="tabsindex5">
									<table>
										<tr>
											<td style="vertical-align: top">
												<fieldset style="width:300px;height: 250px" class= " ui-widget-content ui-corner-all" >
												<legend><label><b>Commision</b></label></legend>
												
													<table>
														<tr>
															<td><label>Gets Commission? </label></td><td><input type="checkbox" id="employeeDirectoryCommission"></td>
														</tr>
														<tr>
															<td><label>Job Profit </label></td><td><input type="text" style="width: 50px;" id="employeeDirectoryLocation"></td>
														</tr>
														<tr>
															<td><label>Buy/Resell </label></td><td><input type="text" style="width: 50px;" id="employeeDirectoryLocation"></td>
														</tr>
														<tr>
															<td><label>Stock Order </label></td><td><input type="text" style="width: 50px;" id="employeeDirectoryLocation"></td>
														</tr>
														<tr>
															<td><label>Factory Commission </label></td><td><input type="text" style="width: 50px;" id="employeeDirectoryLocation"></td>
														</tr>
														<tr>
															<td><label>Quota </label></td><td><input type="text" style="width: 50px;" id="employeeDirectoryLocation"></td>
														</tr>
														<tr>
															<td><label>Total Profit Bonus Only? </label> <input type="checkbox"></td>
														</tr>
														<tr>
															<td><label>Engineering Comm. </label></td><td><input type="text" style="width: 50px;" id="employeeDirectoryLocation"></td>
														</tr>
													</table>
												</fieldset>
											</td>
											<td style="width:25px">
											<td style="vertical-align: top">
												<fieldset style="width:300px" class= " ui-widget-content ui-corner-all">
												<legend><label><b>Bonus Commission</b></label></legend>
												<table>
													<tr>
														<td colspan="2"><label>Gets Bonus Commission</label><input type="checkbox"></td>
													</tr>
													<tr>
														<td><label><u>YTD Profit</u></label></td><td><label><u>Rate</u></label></td>
													</tr>
													<tr>
														<td><select style="width:50px"></select></td><td><select style="width:50px"></select>%</td>
													</tr>
													<tr>
														<td><select style="width:50px"></select></td><td><select style="width:50px"></select>%</td>
													</tr>
													<tr>
														<td><select style="width:50px"></select></td><td><select style="width:50px"></select>%</td>
													</tr>
													<tr>
														<td><select style="width:50px"></select></td><td><select style="width:50px"></select>%</td>
													</tr>
													<tr>
														<td><select style="width:50px"></select></td><td><select style="width:50px"></select>%</td>
													</tr>
													<tr>
														<td><select style="width:50px"></select></td><td><select style="width:50px"></select>%</td>
													</tr>
													<tr>
														<td><select style="width:50px"></select></td><td><select style="width:50px"></select>%</td>
													</tr>
													</table>
												</fieldset>
											</td>
										</tr>
										<tr><td style="height:5px"></td></tr>
										<tr>
											<td style="vertical-align: top">
												<fieldset style="width:300px" class= " ui-widget-content ui-corner-all">
												<legend><label><b>Commission Statement</b></label></legend>
												<table>
														<tr>
															<td><label><b><u>Deductions</u></b></label></td>
														</tr>
														<tr>
															<td><label>Salary</label></td><td><select style="width:75px"></select></td>
														</tr>
														<tr>
															<td><label>Insurance</label></td><td><select style="width:75px"></select></td>
														</tr>
														<tr>
															<td><label>Other</label></td><td><select style="width:75px"></select></td>
														</tr>
														<tr>
															<td><label>Payment Period Default</label></td><td><select style="width:75px"></select></td>
														</tr>												
													</table>
												</fieldset>
											</td>
											<td style="width:25px">
											<td style="vertical-align: top">
											<fieldset style="width:300px;height: 140px" class= " ui-widget-content ui-corner-all">
												<legend><label><b>Book Jobs</b></label></legend>
												<table>
														<tr>
															<td><label><b>Use Special Job number?</b></label></td><td><input type="checkbox"></td>
														</tr>
														<tr>
															<td><label>Prefix Job # with:</label></td><td><select style="width:75px"></select></td>
														</tr>
														<tr>
															<td><label>Sequence follows:</label></td><td><select style="width:75px"></select></td>
														</tr>
													</table>
													</fieldset>
											</td>
										</tr>
									</table>
									</div>
							<div id="tabsindex6">
								<fieldset class= " ui-widget-content ui-corner-all">
								<legend><label><b>Withholding Info</b></label></legend>
								
								<table>
									  <tr>
							             <td><label>State:</label></td><td><select style="width:75px"></select></td>
							             <td><label>Pay Frequency:</label></td><td><select style="width:75px"></select></td>
							             <td ><label>Dept:</label></td><td colspan="1"><select style="width:75px"></select></td>
							          </tr>
							          <tr>
							          	<td><label>Direct Deposite</label></td><td><input type="checkbox"  size="32" value="" /></td>
							             <td><label>Retirement Plan</label></td><td><input type="checkbox" size="32" value="" /></td>
							             <td><label>Statutory Employee</label></td><td><input type="checkbox" size="32" value="" /></td>
							          </tr>
							          <tr>
							             <td width=""></td>
							             <td width=""><b>Federal</b></td>
							             <td width=""><b>State</b></td>
							             <td><b>Local</b></td>
							          </tr>
							          <tr>
							             <td width=""><label>Marital Status:</label></td>
							             <td width="140"><input type="text" name="First_Name" style="width: 80px" value="" /></td>
							             <td width="140"><input type="text" name="First_Name" style="width: 80px" value="" /></td>
							             <td><input type="text" name="First_Name" style="width: 80px" value="" /></td>
							          </tr>
							          <tr>
							             <td width=""><label>Excemptions:</label></td>
							             <td width="140"><input type="text" name="First_Name" style="width: 80px" value="" /></td>
							             <td width="140"><input type="text" name="First_Name" style="width: 80px" value="" /></td>
							             <td><input type="text" name="First_Name" style="width: 80px" value="" /></td>
							          </tr>
							          <tr>
							             <td width=""><label>Sup. WH:</label></td>
							             <td width="140"><input type="text" name="First_Name" style="width: 80px" value="" /></td>
							             <td width="140"><input type="text" name="First_Name" style="width: 80px" value="" /></td>
							             <td><input type="text" name="First_Name" style="width: 80px" value="" /></td>
							          </tr>
							          <tr>
							             <td width=""><label>Special:</label></td>
							             <td width="140"><input type="text" name="First_Name" style="width: 80px" value="" /></td>
							             <td width="140"><input type="text" name="First_Name" style="width: 80px" value="" /></td>
							             <td><input type="text" name="First_Name" style="width: 80px" value="" /></td>
							          </tr>
							       </table>
								</fieldset>
								<table>
									<tr>
										<td style="vertical-align: top;width: 300px; height:150px">
										<fieldset class= " ui-widget-content ui-corner-all">
										<legend><label><b>Pay Type</b></label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label><b>Amount/Pct</b></label></legend>
										</fieldset>
										</td>
										<td style="vertical-align: top;width: 600px; height:150px">
										<fieldset class= " ui-widget-content ui-corner-all">
										<legend><label><b>Deduction</b></label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label><b>Amount/Pct</b></label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label><b>Yearly Max</b></label></legend>
										</fieldset>
										</td>
									</tr>
								</table>
								
							</div>
						  </div>
						</td>
			          </tr>
					</table>
		</div>
		
		<div id="rolodexArchitect" >
			<table>
				<tr><td><jsp:include page="vendorheader.jsp"></jsp:include></td></tr>
				<tr><td colspan="2"><hr width="850px"></td></tr>
			</table>
			<table>
				<tr>
					<td>
						<fieldset class= " ui-widget-content ui-corner-all">
						<legend><label><b>Employee's Assigned</b></label></legend>
						<table>
							<tr><td><label>Salesrep</label></td><td><select style="width:75px"><option></option></select></td></tr>
							<tr><td><label>CSR</label></td><td><select style="width:75px"><option></option></select></td></tr>
							<tr><td><label>Sales Manager</label></td><td><select style="width:75px"><option></option></select></td></tr>
							<tr><td><label>Engineer</label></td><td><select style="width:75px"><option></option></select></td></tr>
							<tr><td><label>Project Manager</label></td><td><select style="width:75px"><option></option></select></td></tr>
						</table>
						</fieldset>
					</td>
				</tr>
			</table>
		</div>
		<div id="rolodexengineer">
			<table>
				<tr><td><jsp:include page="vendorheader.jsp"></jsp:include></td></tr>
				<tr><td colspan="2"><hr width="850px"></td></tr>
			</table>
			<table>
				<tr>
					<td>
						<fieldset class= " ui-widget-content ui-corner-all">
						<legend><label><b>Employee's Assigned</b></label></legend>
						<table>
							<tr><td><label>Salesrep</label></td><td><select style="width:75px"><option></option></select></td></tr>
							<tr><td><label>CSR</label></td><td><select style="width:75px"><option></option></select></td></tr>
							<tr><td><label>Sales Manager</label></td><td><select style="width:75px"><option></option></select></td></tr>
							<tr><td><label>Engineer</label></td><td><select style="width:75px"><option></option></select></td></tr>
							<tr><td><label>Project Manager</label></td><td><select style="width:75px"><option></option></select></td></tr>
						</table>
						</fieldset>
					</td>
				</tr>
			</table>
		</div>
	</div>
</div>
		
		<script type="text/javascript">
	 	jQuery(document).ready(function(){
		 	$("#rolodexcustomer").css("display","none");
		 	$("#rolodexvendor").css("display","none");
		 	$("#employee").css("display","none");
		 	$("#architect").css("display","none");
		 	$("#engineer").css("display","none");
			$(".tabs_main").tabs();
			$("#tabs_sub").tabs();
			$(".datepicker").datepicker();
			 vendorgeneral();
			 vendorjournal(); 
			 vendor();  
		 });
		function customer(){
	 		if ($('#customerche').is(':checked')) { 
			 	$("#rolodexcustomer").show(); 
		 	} else {
			 	 $("#rolodexcustomer").hide();
		 	} 
	 	}
	 	
	 	function vendorrolodex(){
			if($('#vendorche').is(':checked')){
				$("#rolodexvendor").show(); 
			} else {
				$("#rolodexvendor").hide();
			}
		}
	 	
	 	function employee(){
			if($('#employeeche').is(':checked')){
				$("#employee").show();
			} else {
				$("#employee").hide();
			}
		}
	 		
	 	function architect(){
			if($('#architectche').is(':checked')) {
				$("#architect").show();
			} else{
				$("#architect").hide();
			}
		 }	
	 	function engineer(){
			if($('#engineerche').is(':checked')){
				$("#engineer").show();
			} else {
				$("#engineer").hide();
			}
		}	 

		function vendorgeneral() {
			jQuery("#general").jqGrid({
				datatype: 'JSON',
				mtype: 'GET',
				//pager: jQuery('#vendorgeneralpager'),
				colNames: ['Name','Phone(s)'],
				colModel :[
		           	{name:'Name', index:'Name', align:'right', width:60, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
					{name:'Phone(s)', index:'Phone(s)', align:'center', width:90,hidden:false, editable:true, editoptions:{size:20,readonly:false, alignText:'right'},editrules:{edithidden:true,required:true}}],
				rowNum: 1000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false,
				sortname: 'Name', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Contact',
				height:130,	width: 1100,scrollOffset:0, altRows: true,
				altclass:'myAltRowClass',
				loadComplete: function(data) {
					
			    },
				loadError : function (jqXHR, textStatus, errorThrown){
				    
				},
				onSelectRow: function(id){
					if(id && id!==lastsel){
						jQuery('#quotesBidlist').jqGrid('restoreRow',lastsel);
						jQuery('#quotesBidlist').jqGrid('editRow',id,true);
						lastsel=id;
					}
				}
				//editurl:'/employeelistcontroller?type=manipulate'
				
			});/*.navGrid('#vendorgeneralpager',
					{add:true, edit:false,del:true,refresh:false,search:true}, //options
					//-----------------------edit options----------------------//
					{caption:"add"},
					//-----------------------add options----------------------//
					{},
					//-----------------------Delete options----------------------//
					{},
					//-----------------------search options----------------------//
					{}
			
				);*/ 
	 }
	 
	  function vendorjournal(){ 
			
		 jQuery("#journal").jqGrid({
				
				datatype: 'JSON',
				mtype: 'GET',
				//pager: jQuery('#vendorjournalpager'),
				colNames: ['Date','User','Entry'],
				colModel :[
		           	{name:'Date', index:'Date', align:'right', width:80, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
		           	{name:'User', index:'User', align:'right', width:80, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
					{name:'Entry', index:'Entry', align:'center', width:90,hidden:false, editable:true, editoptions:{size:20,readonly:false, alignText:'right'},editrules:{edithidden:true,required:true}}],
				rowNum: 1000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false,
				sortname: 'Date', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Journal',
				height:130,	width: 1100,scrollOffset:0, altRows: true,
				altclass:'myAltRowClass',
				loadComplete: function(data) {
					return;
			    },
				loadError : function (jqXHR, textStatus, errorThrown){
				    
				},
				onSelectRow: function(id){
					if(id && id!==lastsel){
						jQuery('#quotesBidlist').jqGrid('restoreRow',lastsel);
						jQuery('#quotesBidlist').jqGrid('editRow',id,true);
						lastsel=id;
					}
				}
			});/*.navGrid('#vendorjournalpager',
				{add:true, edit:false,del:true,refresh:false,search:true}, //options
				//-----------------------edit options----------------------//
				{},
				//-----------------------add options----------------------//
				{},
				//-----------------------Delete options----------------------//
				{},
				//-----------------------search options----------------------//
				{}
		
			);*/
			
	  }  
		
	 
	 function vendor(){
	 
		 jQuery("#vendor").jqGrid({
			
			datatype: 'JSON',
			mtype: 'GET',
			//pager: jQuery('#vendorpager1'),
			colNames: ['Description','Active'],
			colModel :[
	           	{name:'Description', index:'Description', align:'right', width:60, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
				{name:'Active', index:'Active', align:'center', width:90,hidden:false, editable:true, editoptions:{size:20,readonly:false, alignText:'right'},editrules:{edithidden:true,required:true}}],
				rowNum: 1000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: true,
				sortname: 'Product No', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Manufactures',
				height:100,	width: 1100, altRows: true,
				altclass:'myAltRowClass',
				scrollOffset: 0,
				loadComplete: function(data) {
					
			    },
				loadError : function (jqXHR, textStatus, errorThrown){
				    
				},
				onSelectRow: function(id){
					if(id && id!==lastsel){
						jQuery('#quotesBidlist').jqGrid('restoreRow',lastsel);
						jQuery('#quotesBidlist').jqGrid('editRow',id,true);
						lastsel=id;
					}
				}
		});/*.navGrid('#vendorpager1',
			{add:true, edit:false,del:true,refresh:false,search:false}, //options
			//-----------------------edit options----------------------//
			{caption:"add"},
			//-----------------------add options----------------------//
			{},
			//-----------------------Delete options----------------------//
			{},
			//-----------------------search options----------------------//
			{}
	
		);*/
	 } 

	 
			jQuery(function () {
					jQuery( "#addvendor" ).dialog({
						autoOpen: false,
						height: 750,
						width: 900,
						title:"Rolodex Details",
						modal: true,
						buttons:{
							"Submit": function(){
								
							},
							Cancel: function ()
							{
								jQuery( this ).dialog( "close" );
								return true;
							}
						},
						close: function () {
							return true;
						}
					});
					
			});
			
					
	</script>