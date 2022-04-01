package com.planner.api.utility;

import javax.enterprise.context.ApplicationScoped;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class FileReader {

    private static final String STATEMENT_DIR = "sql-statements/";

    /**
     * @param fileName in the directory: resources/sql-statements/fileName
     * @return content of file as string
     * @throws IOException if file not found
     */
    public String readStaticSqlStatement(String fileName) throws IOException {
        String staticSqlStatement = readStatementFile(STATEMENT_DIR + fileName);
        if (staticSqlStatement != null && !staticSqlStatement.isEmpty()) {
            return staticSqlStatement;
        } else {
            throw new IOException("Content of file " + fileName + " not found!");
        }
    }

    /**
     * @param resourcePath path within the resource directory (example: sql-statements/file.sql)
     * @return content of file as string
     * @throws IOException if file not found
     */
    private String readStatementFile(String resourcePath) throws IOException {
        try (BufferedReader reader = getResourceReader(resourcePath)) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    /**
     * @return list of available statement files
     * @throws IOException if directory not found
     */
    public List<String> getStatementFiles() throws IOException {
        List<String> filenames = new ArrayList<>();
        try (BufferedReader reader = getResourceReader(STATEMENT_DIR)) {
            String resource;
            while ((resource = reader.readLine()) != null) {
                filenames.add(resource);
            }
        }
        // TODO fix for native build
        // https://github.com/oracle/graal/blob/master/docs/reference-manual/native-image/Resources.md#accessing-resources-in-native-images
        // https://quarkus.io/guides/writing-native-applications-tips#including-resources-2
        return filenames;
    }

    private BufferedReader getResourceReader(String resourcePath) throws IOException {
        InputStream inputStream = FileReader.class.getClassLoader().getResourceAsStream(resourcePath);

        if (inputStream == null)
            throw new IOException("No file at " + resourcePath);

        return new BufferedReader(new InputStreamReader(inputStream));
    }
}
