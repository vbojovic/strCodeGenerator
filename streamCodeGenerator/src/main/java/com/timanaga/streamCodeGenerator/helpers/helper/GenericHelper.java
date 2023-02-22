package com.timanaga.streamCodeGenerator.helpers.helper;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import com.thoughtworks.xstream.XStream;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.*;
import java.util.zip.GZIPInputStream;


public class GenericHelper {

	public static List<String> resultSetToList(ResultSet results)
			throws Exception {
		List<String> r = new ArrayList<String>();
		if (results != null)
			while (results.next())
				r.add(results.getString(1));
		return r;
	} 
	public static void stringList2console(List<String> list){
		for (String str : list) {
			 System.out.println(str);
		}
	}
	
	 //http://stackoverflow.com/questions/187676/java-equivalents-of-c-sharp-string-format-and-string-join
	public static String join(Collection<?> s, String delimiter) {
	     StringBuilder builder = new StringBuilder();
	     Iterator<?> iter = s.iterator();
	     while (iter.hasNext()) {
	         builder.append(iter.next());
	         if (!iter.hasNext()) {
	           break;                  
	         }
	         builder.append(delimiter);
	     }
	     return builder.toString();
	 }

    public static String join(Collection<?> s, String delimiter, char quoteChar) {
        StringBuilder builder = new StringBuilder();
        Iterator<?> iter = s.iterator();
        while (iter.hasNext()) {
            builder.append(iter.next());
            if (!iter.hasNext())
                break;
            builder.append(quoteChar);
            builder.append(delimiter);
            builder.append(quoteChar);
        }
        return builder.toString();
    }

	public static HashMap<String, Integer> stringList2HashMapFreq(List<String> strList){
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		for (String pattern : strList) {
			int oldVal = 1;
			if (hm.containsKey(pattern))
				oldVal = hm.get(pattern)+1;
			hm.put(pattern, oldVal);			
		}
		return hm;
	}
	public static List<String> setToStringlist(Set<String> s ,boolean sort ){
		List<String> strList = new ArrayList<String>();
		for (String key  : s) {
			strList.add(key);
		}
		if (sort) Collections.sort(strList);
		return strList;
	}
	
	public static void hashMapFreq2console(HashMap<String, Integer> hm){
		List<String> sortedKeys = setToStringlist(hm.keySet(),true);
		for (String key : sortedKeys) {
			System.out.println(key+"\t"+hm.get(key));
		}
	}
	public static List<String> file2stringLines(String path) throws Exception{
		List<String> fileLines = new ArrayList<String>();
		String fileContent = file2String(path);
		String[] fileLinesArr = fileContent.split("\n");
		for (String line : fileLinesArr) {
			fileLines.add(line);
		}
		return fileLines;
	}
	public static String file2String(String path) throws Exception {
		FileInputStream stream = new FileInputStream(new File(path));
		try {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
					fc.size());
			return Charset.defaultCharset().decode(bb).toString();
		} finally {
			stream.close();
		}
	}
	public BufferedReader gzippedFile2stream(String fName) throws IOException{
		    FileInputStream fin = new FileInputStream(fName);
		    GZIPInputStream gzis = new GZIPInputStream(fin);
		    InputStreamReader xover = new InputStreamReader(gzis);
		    BufferedReader is = new BufferedReader(xover);
		    return is;
	}
	public static boolean fileOrDirectoryExists(String path) {
		File file = new File(path);
		return file.exists();
	}
	public static void object2xml(Object obj, String xmlFilePath)
			throws Exception {
		// http://www.developer.com/xml/article.php/1377961/Serializing-Java-Objects-as-XML.htm
		FileOutputStream os = new FileOutputStream(xmlFilePath);
		XMLEncoder encoder = new XMLEncoder(os);
		encoder.writeObject(obj);
		encoder.close();
	}
	public static void object2xmlX(Object obj, String xmlFilePath) throws Exception{
		XStream xstream = new XStream();
		String xml = xstream.toXML(obj);
		GenericHelper.string2fileOverWrite(xml, xmlFilePath);
	}
    public static String object2Json(Object obj) throws  Exception{
        return JsonWriter.objectToJson(obj, new HashMap<String, Object>());
    }
    public static void object2JsonFile(Object obj, String JsonFilePath) throws Exception{
        string2fileOverWrite(object2Json(obj),JsonFilePath);
    }
    public static Object json2object(String Json) throws Exception {
        return JsonReader.jsonToJava(Json);
    }
    public static Object jsonFile2object(String fileName) throws Exception {
        String json = file2String(fileName);
        return  json2object(json);
    }
	public static void string2fileOverWrite(String sourceString, String fileName)
			throws Exception {
		File aFile = new File(fileName);

		if (!aFile.exists())  
			aFile.createNewFile();
	 
		if (!aFile.isFile()) {
			throw new Exception("Should not be a directory: " + aFile);
		}
		if (!aFile.canWrite()) {
			throw new IllegalArgumentException("File cannot be written: "
					+ aFile);
		}

		Writer output = new BufferedWriter(new FileWriter(aFile));
		try {
			output.write(sourceString);
		} finally {
			output.close();
		}

	}
    public static List<String> loadParamsSwitches(String[] args,String argPrefix, String helpText, boolean throwIfEmpty) throws Exception {
        if (throwIfEmpty)
            if (args == null | args.length == 0) {
                System.out.println(helpText);
                throw new Exception("Missing arguments");
            }
        List<String> map = new ArrayList<String>();
        for (int i=0; i<args.length ; i++){
            String argument = args[i];
            if (argument.startsWith(argPrefix)){
                if (i+1>args.length || args[i+1].startsWith(argPrefix))
                    map.add(argument.replaceFirst(argPrefix,""));
            }
        }
        return map;
    }
    public static HashMap<String,String> loadParams(String[] args,String argPrefix, String helpText, boolean throwIfEmpty) throws Exception {
        if (throwIfEmpty)
            if (args == null | args.length == 0) {
                System.out.println(helpText);
                throw new Exception("Missing arguments");
            }
        HashMap<String,String> map = new HashMap<String, String>();
        for (int i=0; i<args.length ; i++){
            String argument = args[i];
            if (argument.startsWith(argPrefix)){
                if (i+1>args.length || args[i+1].startsWith(argPrefix)){
                    System.out.println(helpText);
                    throw new Exception("Missing argument for "+argument);
                }
                map.put(argument.replaceFirst(argPrefix,""),args[i+1]);
            }
        }
        return map;
    }
	public static void string2fileAppend(String sourceString, String fileName) throws Exception{
		FileWriter out = new FileWriter(fileName, true);
		out.write(sourceString);
		out.close();
	}
	public static void string2file(String sourceString, String fileName)
			throws Exception {
		File aFile = new File(fileName);

		if (!aFile.exists()) {
			throw new Exception("File does not exist: " + aFile);
		}
		if (!aFile.isFile()) {
			throw new Exception("Should not be a directory: " + aFile);
		}
		if (!aFile.canWrite()) {
			throw new IllegalArgumentException("File cannot be written: "
					+ aFile);
		}

		Writer output = new BufferedWriter(new FileWriter(aFile));
		try {
			output.write(sourceString);
		} finally {
			output.close();
		}

	}

	public static String getTempDir() {
		return System.getProperty("java.io.tmpdir");
	}

    public static List<String> resourceToList(String resourcePath) throws Exception{
//        List<String> lines = new ArrayList<String>();
//        final InputStream in = GenericHelper.class.getClass().getResourceAsStream(
//                resourcePath);
//        final BufferedReader bufReader = new BufferedReader(
//                new InputStreamReader(in));
//
//        String line = "";
//        while ((line = bufReader.readLine()) != null)
//            lines.add(line);
//        return lines;
//		var relPath = Paths.get("src", "main", "resources", resourcePath);
		var relPath = Paths.get("classes",  resourcePath);
		var absPath = relPath.toFile().getAbsolutePath();
		return file2stringLines(absPath);
    }

	public static String resourceToString(String resourcePath) throws Exception{
		try{
			var relPath = Paths.get("src", "main", "resources", resourcePath);
			var absPath = relPath.toFile().getAbsolutePath();
			return file2String(absPath);
		} catch (Exception e){
			final InputStream in = GenericHelper.class.getClass().getResourceAsStream(
                resourcePath
			);
			final BufferedReader bufReader = new BufferedReader(
				new InputStreamReader(in)
			);

			String content = "";
			String line = "";
			int i = 0;
			while ((line = bufReader.readLine()) != null) {
				if (i>0) content+="\n";
				content += line;
				i++;
			}
			return content;
		}
	}

//    public static String resourceToString(String resourcePath,String lineSeparator) throws Exception{
//        final InputStream in = GenericHelper.class.getClass().getResourceAsStream(
//                resourcePath);
//        final BufferedReader bufReader = new BufferedReader(
//                new InputStreamReader(in));
//
//        String content = "";
//        String line = "";
//        int i = 0;
//        while ((line = bufReader.readLine()) != null) {
//            if (i>0) content+=lineSeparator;
//            content += line;
//            i++;
//        }
//        return content;
//    }

//	public static String resourceToString(String resourcePath) throws Exception{
//		final InputStream in = GenericHelper.class.getClass().getResourceAsStream(
//				resourcePath);
//		final BufferedReader bufReader = new BufferedReader(
//				new InputStreamReader(in));
//
//		String content = "";
//		String line = "";
//		while ((line = bufReader.readLine()) != null)
//			content+=line;
//		return content;
//	}
	public static BufferedReader resourceToBufReader(String resourcePath){
		final InputStream in = GenericHelper.class.getClass().getResourceAsStream(
				resourcePath);
		final BufferedReader bufReader = new BufferedReader(
				new InputStreamReader(in));
		return bufReader;
	}
	public static String BufferedReader2String(BufferedReader bufReader) throws Exception{
		String content = "";
		String line = "";
		while ((line = bufReader.readLine()) != null) 
			content+=line;
		return content;
	}
	
	public static Object xml2object(String xmlFilePath) throws Exception {
		FileInputStream fis = new FileInputStream(xmlFilePath);
		XMLDecoder xdec = new XMLDecoder(fis);
		return xdec.readObject();
	}
	
	public static Object xml2objectX(String xmlFilePath) throws Exception {
		XStream xs = new XStream();
		return xs.fromXML(GenericHelper.file2String(xmlFilePath));
	}
	public static Object xmlFile2objectX(String xmlFilePath) throws Exception {
		XStream xs = new XStream();
		return xs.fromXML(xmlFilePath);
	}
	
	public static String repeatString(int times, String str) {
		String dest = "";
		for (int i = 1; i <= times; i++)
			dest += str;
		return dest;
	}

}
