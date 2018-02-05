package com.manuga;

import com.manuga.streamCodeGenerator.helpers.Helper;
import com.manuga.streamCodeGenerator.helpers.databases.dbClasses.IDb;
import com.manuga.streamCodeGenerator.helpers.databases.dbClasses.MysqDb;
import com.manuga.streamCodeGenerator.helpers.databases.dbClasses.PgDb;
import com.manuga.streamCodeGenerator.helpers.databases.dbModels.*;
import com.manuga.streamCodeGenerator.helpers.helper.GenericHelper;
import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class StreamCodeGenerator {
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
        //String paramHelp="";
        //URL helpUrl = StreamCodeGenerator.class.getClassLoader().getResource("parameters.txt");
        //paramHelp = GenericHelper.file2String(helpUrl.getFile().toString());
        String paramHelp = GenericHelper.resourceToString("/parameters.txt","\r\n");
		if (args == null | args.length == 0){
            System.out.println(paramHelp);
			//GeneratorsClass gc = new GeneratorsClass();
			//gc.runPhpGeneratorOld("C:\\temp\\out");
			return;
		}

        HashMap<String,String> keys = GenericHelper.loadParams(args,"--",paramHelp,true);
//        runApp(keys);
        runApp2(keys);
	}

    private static DatabaseSettings getSettingsFromKeys(HashMap<String,String> keys) throws Exception{

        if (!keys.containsKey("login")) throw new Exception("login needed");
        String login = keys.get("login");

        if (!keys.containsKey("pass")) throw new Exception("pass needed");
        String pass = keys.get("pass");

        if (!keys.containsKey("db")) throw new Exception("database needed");
        String database = keys.get("db").trim();

        String hostname = (keys.containsKey("hostname"))
                ? keys.get("hostname").toLowerCase().trim()
                : "localhost";

        String outFile = (keys.containsKey("file"))
                ? keys.get("file").trim()
                : "out.json";
        if (GenericHelper.fileOrDirectoryExists(outFile))
            throw  new Exception("output file allready exists");


        String schema = "public";
        if (keys.containsKey("sch")) schema = keys.get("sch").trim();

        String databaseType = "pg";
        if (keys.containsKey("dtype")){
            databaseType =  keys.get("dtype").trim().toLowerCase();
            String types[] = "mysql,pg".split(",");
            if (!Arrays.asList(types).contains(databaseType)) throw  new Exception("Unknown database type "+ databaseType);
        }

        int port = 0;

        if (keys.containsKey("port")){
            port = Integer.parseInt(keys.get("port"));
        }   else{
            if (databaseType.equals("pg")) port=5432;
            if (databaseType.equals("mysql")) port=3306;
        }


        DatabaseSettings settings = new DatabaseSettings();

        settings.setDataBase(database);

        if (databaseType.equalsIgnoreCase("pg")){
            settings.setDataBaseType(DatabaseTypeEnum.postgres);
        }else if (databaseType.equalsIgnoreCase("mysql")){
            settings.setDataBaseType(DatabaseTypeEnum.mysql);
        }else{
            throw new Exception("not supported database type");
        }

        settings.setHost(hostname);
        settings.setLogin(login);
        settings.setPass(pass);
        settings.setPort(port);
        settings.setSchema(schema);
        return settings;
    }

    public static void runApp2(HashMap<String,String> keys) throws Exception {
        DatabaseSettings settings = getSettingsFromKeys(keys);
        String format = "json";
        if (keys.containsKey("format")){
            format = keys.get("format").toLowerCase().trim();
            String formats[] = "json,xml".split(",");
            if (!Arrays.asList(formats).contains(format)) throw  new Exception("Unknown format "+ format);
        }

        IDb db;
        if (settings.getDataBaseType().equals(DatabaseTypeEnum.postgres)){
            db = new PgDb();
        } else if (settings.getDataBaseType().equals(DatabaseTypeEnum.mysql)){
            db = new MysqDb();
        }else{
            throw new Exception("not supported database type");
            //TODO N/A treban implementirat ostale baze
        }

//        PgDb db = new PgDb();

        db.setSettings(settings);
        db.connect();

        DatabaseModel dbm = new DatabaseModel();


        String schema = "";
        if (keys.containsKey("sch")) schema = keys.get("sch").trim();
        List<DatabaseSchema> schemas = new ArrayList<DatabaseSchema>();
        if (!schema.equalsIgnoreCase("")) {
            schemas = db.getElementReader().readSchemas();
        } else{
            schemas.add(db.getElementReader().readSchema(schema));
        }
        dbm.setSchemas(schemas);

        String outFile = (keys.containsKey("file"))
                ? keys.get("file").trim()
                : "out.json";
        if (GenericHelper.fileOrDirectoryExists(outFile))
            throw  new Exception("output file allready exists");
//dbm.getSchemas().add(loadSchema(db,settings));
//        db.getModel();
        if (format.equalsIgnoreCase("json")){
            dbm.saveToJson(outFile);
        } else{
            dbm.saveToXml(outFile);
        }
        db.disconnect();
    }



    public static DatabaseSchema loadSchema(IDb db, DatabaseSettings settings) throws Exception {
        DatabaseSchema schema = new DatabaseSchema();
        String schemaName = settings.getSchema();
        schema.setSchemaName(schemaName);
        schema.setComment("");
        schema.setDDL("");

        DatabasePath dbPath = new DatabasePath();
        dbPath.schema = schemaName;
        dbPath.database = settings.getDataBase();

        schema.setSchemaName(schemaName);
        schema.setTables(db.getElementReader().readTables(dbPath));
        schema.setComment("N/A");
        schema.setDDL("N/A");
        schema.setFunctions(db.getElementReader().getFunctions(dbPath.schema));
        if (!(db.getDbType().equals(DatabaseTypeEnum.pgOld) || db.getDbType().equals(DatabaseTypeEnum.postgres)))
            schema.setProcedures(db.getElementReader().readProcedures(dbPath));
        schema.setFunctions(db.getElementReader().readFunctions(dbPath));
        return schema;
    }
    }
