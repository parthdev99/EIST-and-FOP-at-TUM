package de.tum.in.ase.fop;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CsvParser {
    public List<Map<String, String>> readFile(Path file) throws IOException, LineFomateException {
        if (file == null) {
            throw new IOException("Path is null");
        }
        List<Map<String, String>> ansList = new LinkedList<>();

        InputStream in = new FileInputStream(file.toString());
        Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
        BufferedReader fileReader = new BufferedReader(reader);
        //File csvReader = new File(file.toString());
        //if (csvReader.isFile()) {
        //BufferedReader fileReader = new BufferedReader(new FileReader(file.toFile()));
        try {
            String colValue;
            String[] col = null;
            colValue = fileReader.readLine();
            if (colValue == null) {
                throw new IOException("NO value found");
            }
            col = colValue.split(",", -1);
            int colCount = col.length;
            Pattern p = Pattern.compile("^[a-zA-Z0-9]+$");
            for (int i = 0; i < colCount; i++) {
                Matcher m1 = p.matcher(col[i]);
                if (!m1.matches()) {
                    throw new IOException("invalid Column values");
                }
            }
            String valueLine;

            while ((valueLine = fileReader.readLine()) != null) {
                String[] rowValue = valueLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (rowValue.length > 0 && colCount != rowValue.length) {
                    throw new LineFomateException("The Length of the Row value is not the same as in Column");
                }
                Map<String, String> temp = new HashMap<>();
                for (int i = 0; i < colCount; i++) {
                    if (rowValue[i] != null && rowValue[i].length() >= 2 && rowValue[i].startsWith("\"") && rowValue[i].endsWith("\"")) {
                        rowValue[i] = rowValue[i].substring(1, rowValue[i].length() - 1);
                    }
                    //rowValue[i] = rowValue[i].replaceAll("^\"|\"$", "");
                    rowValue[i] = rowValue[i].replaceAll("\"\"", "\\\"");
                    rowValue[i] = rowValue[i].replaceAll("^$", "nullval");
                    if (rowValue[i].equals("nullval")) {
                        temp.put(col[i], null);
                    } else {
                        temp.put(col[i], rowValue[i]);
                    }
                    if (i == colCount - 1) {
                        ansList.add(temp);
                        temp = new HashMap<>();
                    }
                }

                //}

            }
            fileReader.close();
            return ansList;
        } catch (IOException e) {
            throw new IOException("something went wrong");
        } finally {
            fileReader.close();
        }

    }

    public void writeFile(Path file, List<Map<String, String>> entries) throws IOException {
        if (entries == null || entries.isEmpty() || file == null) {
            throw new IOException("null values are passed");
        }
        //FileWriter writeTo=new FileWriter(file.toString());

        Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file.toString()), StandardCharsets.UTF_8));
        //BufferedWriter writeCsv= new BufferedWriter(writeTo);
        try {
            Set<String> start = entries.get(0).keySet();
            Object[] valuesKey = start.toArray();
            for (int i = 0; i < valuesKey.length; i++) {
                writer.write((String) valuesKey[i]);
                if (i < valuesKey.length - 1) {
                    writer.write(",");
                }
            }
            writer.write("\n");

            for (int i = 0; i < entries.size(); i++) {
                for (int j = 0; j < valuesKey.length; j++) {
                    String value = entries.get(i).get(valuesKey[j]);
                    if (value == null) {
                        writer.write("\"\"");
                    } else {
                        value = value.replaceAll("\"", "\"\"");
                        value = "\"" + value + "\"";
                        writer.write(value);
                    }
                    if (j < valuesKey.length - 1) {
                        writer.write(",");
                    }
                }
                if (i < entries.size() - 1) {
                    writer.write("\n");
                }

            }
            writer.close();
        } catch (IOException e) {
            throw new IOException("something went wrong");
        } finally {
            writer.close();
        }

    }

}
