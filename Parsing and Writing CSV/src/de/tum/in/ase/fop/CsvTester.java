package de.tum.in.ase.fop;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CsvTester {
    public static void main(String[] args) throws IOException, URISyntaxException, LineFomateException {
        // TODO: Test your code here
        Path testFile = getPathForResource("test.csv");
        Path t2 = getPathForResource("writ.csv");
        CsvParser c1 = new CsvParser();
        List<Map<String, String>> l1 = new LinkedList<>();
        l1 = c1.readFile(testFile);
        c1.writeFile(t2, l1);
    }

    private static Path getPathForResource(String resource) throws URISyntaxException {
        return Paths.get(CsvTester.class.getResource(resource).toURI());
    }
}
