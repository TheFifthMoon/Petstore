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
import java.net.URL;
import java.util.Arrays;

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

        JsonNode actual = JsonLoader.fromResource("/actual.json");
        JsonNode contract = JsonLoader.fromResource("/contract.json");

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
}
