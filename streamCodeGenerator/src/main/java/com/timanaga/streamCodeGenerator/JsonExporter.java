package com.timanaga.streamCodeGenerator;

import com.timanaga.streamCodeGenerator.helpers.helper.GenericHelper;

import java.io.BufferedReader;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: x
 * Date: 12/4/13
 * Time: 6:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class JsonExporter {
    public static void main(String[] args) throws Exception {
        if (args.length==0) {
            printHelp();
            return;
        }
        HashMap<String,String> params = readParams(args);
        //TODO treban vamo ucitat parametre i prosljedit ih klasi i onda exportirat!


    }
    private static HashMap<String,String> readParams (String[] args) throws Exception{
        HashMap<String,String> params = new HashMap<String, String>();
        for (int i=0; i< args.length ; i=i+2){
            if (!args[i].startsWith("-")) throw new Exception("Param "+args[i]+" is not valid");
            params.put(args[i].replace("-",""),args[i+1]);
        }
        String possibleParams = "D,l,p,e,s";
        for(String param : possibleParams.split(",")){
            if (!params.containsKey(param)) throw new Exception("Parameter: "+param+" missing. Please read help.");
        }
        return params;
    }
    public static void printHelp() throws Exception {
        BufferedReader reader =GenericHelper.resourceToBufReader("/JsonExporterHelp.txt");
        String help = GenericHelper.BufferedReader2String(reader);
        for(String line : help.split("\n")){
            System.out.println(line);
            System.out.println("\n");
        }
    }
}
