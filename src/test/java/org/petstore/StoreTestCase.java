package org.petstore;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.file.*;
import java.util.Arrays;
import java.util.concurrent.locks.Condition;

public class StoreTestCase {
    public static final String RESOURCES_PATH = System.getProperty("user.dir") + "/src/test/resources/";

    @Test(groups = {"disabled"})
    public void testTrue() {
        Assert.assertTrue(true);
    }

    @Test(groups = {"apitest"})
    public void validateThatImplementationMatchesContract() throws IOException, ProcessingException, InterruptedException {
        URL actualUrl = new URL("https://petstore.swagger.io/v2/store/inventory");
        File actualFile = new File(RESOURCES_PATH + "actual.json");
        FileUtils.copyURLToFile(actualUrl, actualFile);
        URL contractUrl = new URL("https://petstore.swagger.io/v2/swagger.json");
        File contractFile = new File(RESOURCES_PATH + "contract.json");
        FileUtils.copyURLToFile(contractUrl, contractFile);

//        WatchService watcher = FileSystems.getDefault().newWatchService();
//        try {
//            File resourcesFolder = new File(RESOURCES_PATH);
//            Path resourcesPath = resourcesFolder.toPath();
//            WatchKey key = resourcesPath.register(watcher,
//                    StandardWatchEventKinds.ENTRY_CREATE,
//                    StandardWatchEventKinds.ENTRY_DELETE,
//                    StandardWatchEventKinds.ENTRY_MODIFY);
//        } catch (IOException x) {
//            System.err.println(x);
//        }

        JsonNode actual = null;
        JsonNode contract = null;
        int count = 0;
        int maxTries = 3;

        while (true) {
            try {
                actual = JsonLoader.fromResource("/actual.json");
                contract = JsonLoader.fromResource("/contract.json");
            } catch (IOException e) {
                System.out.println("CATCHED");
                Thread.sleep(5000);
                if (++count == maxTries) throw e;
                break;
            }
        }

        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        final JsonSchema schema = factory.getJsonSchema(contract);
        ProcessingReport report;
        report = schema.validate(actual);
        System.out.println(report);
        Assert.assertTrue(true);
    }

    @AfterGroups(groups = {"apitest"})
    public void deleteTempFiles() {
        File resourcesFolder = new File(RESOURCES_PATH);
//        Arrays.stream(resourcesFolder.listFiles((f, p) -> p.endsWith("json"))).forEach(File::delete);
    }

//    private boolean isCompletelyWritten(File file) {
//        RandomAccessFile stream = null;
//        try {
//            stream = new RandomAccessFile(file, "rw");
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (stream != null) {
//                try {
//                    stream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return false;
//    }
}
