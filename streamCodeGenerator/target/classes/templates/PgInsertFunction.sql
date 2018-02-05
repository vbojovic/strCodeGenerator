CREATE OR REPLACE FUNCTION #functionName#_delete (#params#) RETURNS varchar AS
$body$
BEGIN
	insert into #tableName#(#fieldNames#)
	values(#paramNames#);
  	return 'Successfully inserted';
END;
$body$
LANGUAGE 'plpgsql'
VOLATILE 
SECURITY INVOKER
COST 100;