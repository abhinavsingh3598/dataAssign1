import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class QueryImplementation implements Query {
    public static final String NEXT_LINE = "\n";
    public static final String TABLE_NAME = "TableName:";
    public static final String SELECT = "select";
    private static final String FILE_NAME = "database.txt";

    public static final String REGEX = "[(), ;]+";


    public static final String CREATE = "create";
    public static final String INSERT = "insert";
    public static final String HASH = "#";
    public static final String FROM = "from";
    public static final String WHERE = "where";
    public static final String Dollar_REGEX = "\\$";
    public static final String UPDATE = "update";
    public static final String SEMICOLON = ";";
    public static final String EQUALS_TO = "=";
    public static final String SET = "set";
    public static final String DELETE = "delete";

    /***
     * For creating the table
     * @param scanner
     */
    public void createTable(Scanner scanner) {

        String inputString = scanner.nextLine();
        if (inputString.toLowerCase().startsWith(CREATE)) {
            if (inputString.contains(SEMICOLON)) {
                String[] values = inputString.split(REGEX);
                if (values.length > 3) {
                    String tableName = values[2];
                    StringBuilder sb = new StringBuilder();
                    for (int i = 3; i < values.length; i++) {
                        sb.append(HASH);

                        sb.append(values[i]);
                    }

                    String line = TABLE_NAME + tableName + sb.toString() + NEXT_LINE;

                    writeToFile(line);
                    System.out.println("Successfully created table");
                } else {
                    System.out.println("Incorrect Query");
                }
            } else {
                System.out.println("Semicolon is missing in the last");
            }
        } else {
            System.out.println("please enter create keyword");
        }


    }

    /***
     * Writing content into the file
     * @param data
     */
    private void writeToFile(String data) {
        try {
            // create a FileWriter object with append flag set to true
            FileWriter writer = new FileWriter(FILE_NAME, true);

            // create a BufferedWriter object
            BufferedWriter bufferWriter = new BufferedWriter(writer);

            // write the data to the file on a new line
            bufferWriter.write(data);

            // close the buffer writer and writer
            bufferWriter.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * insert data into table
     * @param scanner
     */
    public void insertIntoTable(Scanner scanner) {

        String inputString = scanner.nextLine();
        if (inputString.toLowerCase().startsWith(INSERT)) {
            if (inputString.contains(SEMICOLON)) {
                String[] values = inputString.split(REGEX);
                if (values.length > 4) {
                    String tableName = values[2];
                    if (checkTableName(tableName)) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 4; i < values.length; i++) {
                            sb.append("$");
                            sb.append(values[i]);
                        }

                        String line = tableName + sb.toString() + NEXT_LINE;
                        writeToFile(line);
                        System.out.println("Successfully inserted");
                    } else {
                        System.out.println("Incorrect table name");
                    }
                } else {
                    System.out.println("Incorrect Query");
                }
            } else {
                System.out.println("Semicolon is missing in the last");
            }
        } else {
            System.out.println("user insert keyword");
        }

    }

    /***
     * To verify table name
     * @param tableName
     * @return
     */
    private boolean checkTableName(String tableName) {
        try {
            FileReader reader = new FileReader(FILE_NAME);
            String searchStr = TABLE_NAME + tableName;

            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {

                if (line.matches(".*" + searchStr + ".*")) {

                    return true;
                }
            }


            bufferedReader.close();
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /***
     * fetching data from table
     * @param scanner
     */
    public void selectQuery(Scanner scanner) {
        String inputString = scanner.nextLine();
        if (inputString.toLowerCase().startsWith(SELECT)) {
            if (inputString.contains(SEMICOLON)) {
                int fromIndex = inputString.toLowerCase().indexOf(FROM) + 5; // add 5 to skip "from" and the following space
                int whereIndex = inputString.toLowerCase().indexOf(WHERE);
                if (whereIndex == -1) {
                    // if there is no "where" keyword, use the end of the string as the substring index
                    whereIndex = inputString.length() - 1;
                }
                String tableName = inputString.substring(fromIndex, whereIndex).trim();

                if (checkTableName(tableName)) {
                    /// code to be written

                    if (inputString.contains("*") && !(inputString.toLowerCase().contains(WHERE))) {
                        Map<String, List<String>> values = getAllFromTable(tableName);

                        System.out.println(values);

                    } else if (inputString.contains("*") && inputString.toLowerCase().contains(WHERE)) {
                        List<String> rows = getTableInfo(tableName);
                        Map<String, List<String>> values = getAllFromTable(tableName);

                        rows = rows.stream().filter(r -> r.contains(tableName + HASH)).map(r -> r.replace(TABLE_NAME + tableName, "|")).collect(Collectors.toList());

                        List<String> strings = Arrays.stream(rows.get(0).split("\\#")).filter(r -> !r.equals("|")).collect(Collectors.toList());
                        String column = inputString.substring((inputString.toLowerCase().indexOf(WHERE) + 6), (inputString.indexOf(EQUALS_TO)));

                        if (strings.contains(column)) {

                            String val = inputString.substring(inputString.indexOf(EQUALS_TO) + 1, (inputString.indexOf(SEMICOLON))).trim();

                            System.out.println(strings.indexOf(column) + " val " + val);
                            for (List<String> row : values.values()) {
                                if (row.contains(val)) {
                                    System.out.println(row);
                                }
                            }

                        } else {
                            System.out.println("Invalid column name");
                        }


                    } else {
                        System.out.println("||For now only supporting * operations||");
                    }

                } else {
                    System.out.println("Incorrect table name");
                }
            } else {
                System.out.println("Semicolon is missing in the last");
            }
        } else {
            System.out.println("use select keyword");
        }
    }

    /**
     * get the rows in Map format
     * @param tableName
     * @return
     */
    private Map<String, List<String>> getAllFromTable(String tableName) {
        List<String> rows = getTableInfo(tableName);
        rows = rows.stream().filter(r -> !r.contains(TABLE_NAME)).collect(Collectors.toList());
        rows = rows.stream().filter(r -> r.contains(tableName + "$")).map(r -> r.replace(tableName, "|")).collect(Collectors.toList());
        Map<String, List<String>> values = new HashMap<>();

        for (int i = 0; i < rows.size(); i++) {

            List<String> strings = Arrays.asList(rows.get(i).split(Dollar_REGEX)).stream().filter(r -> !r.equals("|")).collect(Collectors.toList());

            values.put("r" + (i + 1), strings);

        }
        return values;
    }

    /***
     * get all data related to the table
     * @param tableName
     * @return
     */
    private List<String> getTableInfo(String tableName) {
        List<String> lines = new ArrayList<>();
        try {  // create a FileReader object
            FileReader reader = new FileReader(FILE_NAME);
            // create a BufferedReader object
            BufferedReader bufferedReader = new BufferedReader(reader);
            // read the file line by line
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // check if the line contains the condition
                if (line.matches(".*" + tableName + ".*")) {
                    // do something with the line
                    lines.add(line);

                }
            }
            // close the bufferedReader and reader
            bufferedReader.close();
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    /***
     * update the data into the file
     * @param scanner
     */
    public void updateQuery(Scanner scanner) {

        String inputString = scanner.nextLine();
        if (inputString.toLowerCase().startsWith(UPDATE)) {
            if (inputString.contains(SEMICOLON)) {
                int fromIndex = inputString.toLowerCase().indexOf(UPDATE) + 7;
                int endIndex = inputString.indexOf(SET) - 1;
                String tableName = inputString.substring(fromIndex, endIndex).trim();

                if (checkTableName(tableName)) {

                    List<String> rows = getTableInfo(tableName);
                    Map<String, List<String>> values = getAllFromTable(tableName);

                    List<String> columnsInTable = getAllColumns(tableName, rows);

                    String column1 = inputString.substring((inputString.toLowerCase().indexOf(SET) + 4), (inputString.indexOf(EQUALS_TO)));
                    String column2 = inputString.substring((inputString.toLowerCase().indexOf(WHERE) + 6), inputString.indexOf(EQUALS_TO, inputString.indexOf(EQUALS_TO) + 1));

                    if (columnsInTable.contains(column1) && columnsInTable.contains(column2)) {

                        String replacementValue = inputString.substring(inputString.indexOf(EQUALS_TO) + 1, (inputString.indexOf(WHERE) - 1)).trim();
                        String conditionValue = inputString.substring(inputString.indexOf(EQUALS_TO, inputString.indexOf(EQUALS_TO) + 1) + 1, (inputString.indexOf(SEMICOLON))).trim();

                        String targetValue = "";
                        for (List<String> row : values.values()) {
                            if (row.contains(conditionValue)) {
                                targetValue = row.get(columnsInTable.indexOf(column1));
                            }
                        }

                        fileUpdation(replacementValue, targetValue);
                        System.out.println("Successfully Updated");

                    } else {
                        System.out.println("Invalid column name");
                    }


                } else {
                    System.out.println("Incorrect table name");
                }
            } else {
                System.out.println("Semicolon is not present");
            }
        } else {
            System.out.println("use update keyword");
        }
    }

    /***
     * updation of file content
     * @param replacementValue
     * @param targetValue
     */
    private static void fileUpdation(String replacementValue, String targetValue) {
        try {
            File file = new File(FILE_NAME);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            StringBuilder sb = new StringBuilder();
            String line;
            if ("".equals(replacementValue)) {

                while ((line = br.readLine()) != null) {
                    if (!line.contains("$"+targetValue+"$")) {
                        sb.append(line).append(NEXT_LINE);
                    }
                }

            } else {
                while ((line = br.readLine()) != null) {

                    line = line.replace(targetValue, replacementValue);
                    sb.append(line).append(NEXT_LINE);

                }
            }
            br.close();
            FileWriter fw = new FileWriter(file);
            fw.write(sb.toString());
            fw.close();
        } catch (Exception e) {
            ///
        }
    }

    /***
     * Get all column names
     * @param tableName
     * @param rows
     * @return
     */
    private static List<String> getAllColumns(String tableName, List<String> rows) {

        rows = rows.stream().filter(r -> r.contains(tableName + HASH)).map(r -> r.replace(TABLE_NAME + tableName, "|")).collect(Collectors.toList());

        List<String> strings = Arrays.stream(rows.get(0).split("\\#")).filter(r -> !r.equals("|")).collect(Collectors.toList());
        return strings;
    }

    /***
     * delete delete the data from table
     * @param scanner
     */
    public void deleteQuery(Scanner scanner) {

        String inputString = scanner.nextLine();
        //DELETE FROM table_name WHERE condition;
        if (inputString.toLowerCase().startsWith(DELETE)) {
            if (inputString.contains(SEMICOLON)) {

                int fromIndex = inputString.toLowerCase().indexOf(FROM) + 5; // add 5 to skip "from" and the following space
                int whereIndex = inputString.toLowerCase().indexOf(WHERE); // find the index of "where" keyword
                String tableName = inputString.substring(fromIndex, whereIndex).trim();
                if (checkTableName(tableName)) {
                    List<String> rows = getTableInfo(tableName);
                    Map<String, List<String>> values = getAllFromTable(tableName);

                    List<String> columnsInTable = getAllColumns(tableName, rows);


                    String column = inputString.substring((inputString.toLowerCase().indexOf(WHERE) + 6), inputString.indexOf(EQUALS_TO));

                    if (columnsInTable.contains(column)) {


                        String conditionValue = inputString.substring(inputString.indexOf(EQUALS_TO) + 1, (inputString.indexOf(SEMICOLON))).trim();

                        String targetValue = "";
                        for (List<String> row : values.values()) {
                            if (row.contains(conditionValue)) {
                                targetValue = row.get(columnsInTable.indexOf(column));
                                System.out.println(row + "value has been deleted");
                            }
                        }

                    fileUpdation("", targetValue);

                    } else {
                        System.out.println("Invalid column name");
                    }


                } else {
                    System.out.println("Invalid table name");
                }
            } else {
                System.out.println("Semicolon missing !!!");
            }
        } else {
            System.out.println("use delete keyword");
        }
    }
}
