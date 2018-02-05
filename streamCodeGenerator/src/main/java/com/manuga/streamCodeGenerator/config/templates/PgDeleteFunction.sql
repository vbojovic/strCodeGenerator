CREATE OR REPLACE FUNCTION #functionName#_delete (#params#) RETURNS varchar AS
$body$
BEGIN
	delete from #tableName#
	where #whereStatements#; 
  	return 'Successfully deleted';
END;
$body$
LANGUAGE 'plpgsql'
VOLATILE 
SECURITY INVOKER
COST 100;