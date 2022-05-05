package com.planner.api.files;

import javax.enterprise.context.ApplicationScoped;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class FileReader {

    private static final String STATEMENT_DIR = "sql-statements/";

    /**
     * @return list of available statement files
     * @throws IOException if directory not found
     */
    public List<String> getStatementFiles() throws IOException {
        List<String> filenames = new ArrayList<>();
        try (BufferedReader reader = this.getResourceReader(STATEMENT_DIR)) {
            String resource;
            while ((resource = reader.readLine()) != null) {
                filenames.add(resource);
            }
        }
        if (filenames.isEmpty()) {
            // TODO fix for native build
            // https://github.com/oracle/graal/blob/master/docs/reference-manual/native-image/Resources.md#accessing-resources-in-native-images
            // https://quarkus.io/guides/writing-native-applications-tips#including-resources-2
            filenames = placeholderNativeFileList();
        }
        return filenames;
    }

    /**
     * @param fileName in the directory: resources/sql-statements/fileName
     * @return content of file as string
     * @throws IOException if content is empty
     */
    public String readStaticSqlStatement(String fileName) throws IOException {
        String staticSqlStatement;
        try (BufferedReader reader = this.getResourceReader(STATEMENT_DIR + fileName)) {
            staticSqlStatement = reader.lines().collect(Collectors.joining("\n"));
        }
        if (!staticSqlStatement.isEmpty()) {
            return staticSqlStatement;
        } else {
            throw new IOException("Content of file " + fileName + " not found!");
        }
    }

    /**
     * @param resourcePath path within the resource directory (example: sql-statements/file.sql)
     * @return reader of input stream
     * @throws IOException if input stream is null
     */
    private BufferedReader getResourceReader(String resourcePath) throws IOException {
        InputStream inputStream = FileReader.class.getClassLoader().getResourceAsStream(resourcePath);

        if (inputStream == null)
            throw new IOException("No content at " + resourcePath);

        return new BufferedReader(new InputStreamReader(inputStream));
    }

    /**
     * TODO remove after fix for native build
     *
     * @return hardcoded files from resources
     */
    private List<String> placeholderNativeFileList() {
        return Arrays.asList(
                "db2-create-ancestor-table.sql",
                "db2-create-stud-tables.sql",
                "db2-select-ancestor.sql",
                "fp-create-tables-and-data.sql",
                "fp-insert-cart.sql",
                "fp-insert-user.sql",
                "fp-select-cart.sql",
                "fp-select-carts-from-user.sql",
                "fp-select-low-storage-products.sql",
                "fp-select-recipes.sql");
    }
}
