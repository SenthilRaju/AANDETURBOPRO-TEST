-- Currently we have problem in Brockair database only
DELETE FROM BrockairCompany.search_index WHERE resultedTableName = 'rxMaster' AND searchText = '';