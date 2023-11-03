import com.timanaga.StreamCodeGenerator;
import com.timanaga.streamCodeGenerator.databases.dbClasses.PgDb;
import com.timanaga.streamCodeGenerator.databases.dbClasses.PgDbElementReader;
import com.timanaga.streamCodeGenerator.databases.dbModels.DatabaseField;
import com.timanaga.streamCodeGenerator.databases.dbModels.DatabaseSettings;
import com.timanaga.streamCodeGenerator.databases.dbModels.DatabaseTypeEnum;
import com.timanaga.streamCodeGenerator.helpers.helper.GenericHelper;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class StreamCodeGeneratorTest {
    @Test
    public void testStreamCodeGenerator() throws Exception {
        StreamCodeGenerator.main(new String[]{});
        Assert.assertTrue(true);
    }
    private List<String> getResourceFiles(String path) throws IOException {
        List<String> filenames = new ArrayList<>();

        try (
                InputStream in = getResourceAsStream(path);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
        ) {
            String resource;

            while ((resource = br.readLine()) != null) {
                filenames.add(resource);
            }
        }

        return filenames;
    }
//@Test
//public void testLoadResource() {
//    InputStream inputStream = getClass().getResourceAsStream("/pgDataTypes.txt");
//    assertNotNull(inputStream);
//
//    // Process the resource (e.g., load properties)
//    Properties properties = new Properties();
//    try {
//        properties.load(inputStream);
//    } catch (Exception e) {
//        fail("Failed to load properties from resource");
//    }
//
//    // Assert the content of the resource
//    assertEquals("value1", properties.getProperty("key1"));
//    assertEquals("value2", properties.getProperty("key2"));
//}
//    @Test
    public void testGetCharacterFieldLength() throws Exception {
        DatabaseSettings settings = new DatabaseSettings();
        settings.setDataBase("growr");
        settings.setLogin("postgres");
        settings.setPass("sragne");
        settings.setHost("localhost");
        settings.setSchema("public");
        settings.setDataBaseType(DatabaseTypeEnum.postgres);

        PgDb dataBase = new PgDb();
        dataBase.setSettings(settings);
        dataBase.connect();
        PgDbElementReader reader = new PgDbElementReader(dataBase);
        DatabaseField field = reader.getFieldData("public", "places", "country_id");
        dataBase.disconnect();
        assertNotNull(field);
    }
    private InputStream getResourceAsStream(String resource) {
        final InputStream in
                = getContextClassLoader().getResourceAsStream(resource);

        return in == null ? getClass().getResourceAsStream(resource) : in;
    }

    private ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
    private Resource[] getTxtResources(String path) throws IOException
    {
        ClassLoader classLoader = MethodHandles.lookup().getClass().getClassLoader();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(classLoader);

        return resolver.getResources("classpath:"+path+"*.txt");
    }

    @Test
    public void testPrintHelp() throws Exception {
        var relPath = Paths.get("src", "main", "resources", "parameters.txt"); // src/test/resources/image.jgp
        var absPath = relPath.toFile().getAbsolutePath(); // /home/<user>/../<project-root>/src/test/resources/image.jpg
        var anyFileUnderThisPath = new File(absPath).exists(); // true
        assertEquals(true,anyFileUnderThisPath);
    }
    @Test
    public void testLoadFile() throws Exception {
        List<String> res = GenericHelper.resourceToList("/pgDataTypes.txt");
        System.out.println(res);
        assertEquals(true,res.size()>0);
    }

    @Test
    public void testPrintHelp3() throws Exception {
        String res = GenericHelper.resourceToString("/parameters.txt");
        System.out.println(res);
        assertEquals(true,res.length()>0);
    }

    @Test
    public void testPrintHelp2() throws Exception {
        var relPath = Paths.get("src", "main", "resources", "/parameters.txt"); // src/test/resources/image.jgp
        var absPath = relPath.toFile().getAbsolutePath(); // /home/<user>/../<project-root>/src/test/resources/image.jpg
        var anyFileUnderThisPath = new File(absPath).exists(); // true
        assertEquals(true,anyFileUnderThisPath);
    }
}
