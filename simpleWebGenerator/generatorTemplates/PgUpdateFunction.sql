CREATE OR REPLACE FUNCTION #functionName#_update (#params#) RETURNS varchar AS
$body$
BEGIN
	update #tableName#
	set #fieldsAndValues#
	where #whereStatements#; 
  	return 'Successfully updated';
END;
$body$
LANGUAGE 'plpgsql'
VOLATILE 
SECURITY INVOKER
COST 100;