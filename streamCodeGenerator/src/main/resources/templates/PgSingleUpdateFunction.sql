CREATE OR REPLACE FUNCTION #functionName#_single_update (#params#,#fieldName#,#newValue#) RETURNS varchar AS
$body$
BEGIN
	update #tableName#
	set #fieldName# = #newValue#
	where #whereStatements#; 
  	return 'Successfully updated';
END;
$body$
LANGUAGE 'plpgsql'
VOLATILE 
SECURITY INVOKER
COST 100;