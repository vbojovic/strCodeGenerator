CREATE OR REPLACE FUNCTION #tableName#_#field#_switch (#params#) RETURNS boolean AS
$body$
BEGIN
	update #tableName#
	set #field# = not #field#
	where #whereStatements#; 
	
	
	return (
		select  #field#
		from #tableName#
		where #whereStatements#
	); 
END;
$body$
LANGUAGE 'plpgsql'
VOLATILE 
SECURITY INVOKER
COST 100;