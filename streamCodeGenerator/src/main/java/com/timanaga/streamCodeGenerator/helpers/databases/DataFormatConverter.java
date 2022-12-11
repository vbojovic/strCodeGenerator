package com.timanaga.streamCodeGenerator.helpers.databases;

import com.timanaga.streamCodeGenerator.helpers.helper.GenericHelper;
import com.timanaga.streamCodeGenerator.helpers.helper.collections.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vbojovic
 * Date: 06.01.14.
 * Time: 15:30
 * To change this template use File | Settings | File Templates.
 */
public class DataFormatConverter {
    private HashMap<String, Tuple> map = new HashMap<String, Tuple>();
    public DataFormatConverter(){
        super();
    }
    public DataFormatConverter(String resourcePath) throws Exception {
        loadFromresource(resourcePath);
    }
    public void loadFromresource(String resourcePath) throws Exception {
        List<String> txt = GenericHelper.resourceToList(resourcePath);
        for (String line : txt){
            String[] values = line.split("\t");
            map.put(values[0],new Tuple(values[1],values[2]));
        }
    }
    public String nativeToTopLevel(String nativeType){
        if (map.isEmpty() || !map.containsKey(nativeType)) return null;
        return this.map.get(nativeType).getVal2().toString();
    }
    public String nativeToGeneric(String nativeType){
        if (map.isEmpty() || !map.containsKey(nativeType)) return null;
        return this.map.get(nativeType).getVal1().toString();
    }
    public List<String> genericToNatives(String generic){
        List<String> natives = new ArrayList<String>();
        for (String key : this.map.keySet()){
            if (this.map.get(key).getVal1().toString().equalsIgnoreCase(generic))  natives.add(key);
        }
        return natives;
    }

}
