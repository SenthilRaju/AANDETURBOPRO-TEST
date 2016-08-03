-- Select from joMaster table records for 'JOB' and Insert to search_index table: 

USE BartosCompany;

INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields`, `bidDate` )
SELECT 'Job' as entity, 
        CONCAT(joMaster.JobNumber,'|',joMaster.Description,'|',tsUserLogin.FullName) AS searchText, 
       'joMaster' as resultedTableName,
        joMaster.joMasterID AS pk_fields,
	joMaster.BidDate AS bidDate
FROM joMaster 
JOIN tsUserLogin ON joMaster.cuAssignmentID0 = tsUserLogin.UserLoginID;

-- Job BidDate With Assignment Id is 0 --

INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields`, `bidDate` )
SELECT 'Job' as entity, 
        CONCAT(joMaster.JobNumber,'|',joMaster.Description) AS searchText, 
       'joMaster' as resultedTableName,
        joMaster.joMasterID AS pk_fields,
	joMaster.BidDate AS bidDate
FROM joMaster where joMaster.cuAssignmentID0 is null OR joMaster.cuAssignmentID0 = '';

-- Select from rxMaster table records for 'Customer' and Insert to search_index table:

INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
SELECT 'customers' as entity, 
       CONCAT(Name,'|',SearchName,'|',Phone1) AS searchText,
       'rxMaster' as resultedTableName ,
       rxMasterID as pk_fields
FROM rxMaster where InActive='0' and IsCustomer='1';

-- Select from rxMaster table records for 'Vendor' and Insert to search_index table:

INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
SELECT 'vendors' as entity, 
       CONCAT(Name,'|',SearchName,'|',Phone1) AS searchText,
       'rxMaster' as resultedTableName ,
       rxMasterID as pk_fields
FROM rxMaster where InActive='0' and IsVendor='1';

-- Select from rxMaster table records for 'Employee' and Insert to search_index table:

INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
SELECT 'employeelist' as entity, 
       CONCAT(Name,'|',SearchName,'|',Phone1) AS searchText,
       'rxMaster' as resultedTableName ,
       rxMasterID as pk_fields
FROM rxMaster where InActive='0' and IsEmployee='1';

-- Select from rxMaster table records for 'Architect' and Insert to search_index table: 

INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
SELECT 'architect' as entity, 
       CONCAT(Name,'|',SearchName,'|',Phone1) AS searchText,
       'rxMaster' as resultedTableName ,
       rxMasterID as pk_fields
FROM rxMaster where InActive='0' and IsCategory1 = 1  and IsCategory2 = 0;

-- Select from rxMaster table records for 'Engineer' and Insert to search_index table:

INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
SELECT 'engineer' as entity, 
       CONCAT(Name,'|',SearchName,'|',Phone1) AS searchText,
       'rxMaster' as resultedTableName ,
       rxMasterID as pk_fields
FROM rxMaster where InActive='0' and IsCategory1 = 0 and IsCategory2 = 1;

-- Select from rxMaster table records for 'G.C' and Insert to search_index table:

INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
SELECT 'generalcontractors' as entity, 
       CONCAT(Name,'|',SearchName,'|',Phone1) AS searchText,
       'rxMaster' as resultedTableName ,
       rxMasterID as pk_fields
FROM rxMaster where InActive='0' and IsCategory3 = 1;

-- Select from rxMaster table records for 'Architect and Engineer' and Insert to search_index table:

INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
SELECT 'architect/engineer' as entity, 
       CONCAT(Name,'|',SearchName,'|',Phone1) AS searchText,
       'rxMaster' as resultedTableName ,
       rxMasterID as pk_fields
FROM rxMaster where InActive='0' and IsCategory1 = 1 and IsCategory2 = 1;

-- Select from coAccount table records for 'Charts of Accounts' and Insetr to Serach table --

INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
SELECT 'accounts' as entity, 
       CONCAT(Number,'|',Description) AS searchText,
       'coAccount' as resultedTableName ,
       coAccountID as pk_fields
FROM coAccount;
