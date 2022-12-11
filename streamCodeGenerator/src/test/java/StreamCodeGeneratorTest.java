import com.timanaga.StreamCodeGenerator;
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
        Assert.assertEquals(true,anyFileUnderThisPath);
    }
}
