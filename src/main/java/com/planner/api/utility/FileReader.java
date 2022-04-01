package com.planner.api.utility;

import javax.enterprise.context.ApplicationScoped;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class FileReader {

    /**
     * @param fileName in the directory: resources/sql-statements/fileName
     * @return content of file as string
     * @throws IOException if file not found
     */
    public String readStaticSqlStatement(String fileName) throws IOException {
        return readResourceFile("sql-statements/" + fileName);
    }

    /**
     * @param resourcePath path within the resource directory (example: sql-statements/file.sql)
     * @return content of file as string
     * @throws IOException if file not found
     */
    private String readResourceFile(String resourcePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(
                                FileReader.class.getClassLoader().getResourceAsStream(resourcePath))))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
