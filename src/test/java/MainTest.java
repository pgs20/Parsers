import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainTest {
    @Test
    public void parseCSVTest() {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        List<Employee> expected = new ArrayList<>(Arrays.asList(
                new Employee(12, "Gosha", "Smith", "USA", 25),
                new Employee(2, "Inav", "Petrov", "RU", 23)
        ));

        List<Employee> actual = Main.parseCSV(columnMapping, fileName);

        Assertions.assertNotEquals(expected, actual);
    }

    @Test
    public void parseXMLTest() {
        String fileName = "data.xml";

        List<Employee> expected = new ArrayList<>(Arrays.asList(
                new Employee(12, "Gosha", "Smith", "USA", 25),
                new Employee(2, "Inav", "Petrov", "RU", 23)
        ));

        List<Employee> actual = Main.parseXML(fileName);

        Assertions.assertNotEquals(expected, actual);
    }

    @Test
    public void listToJsonTest() {
        List<Employee> employeeList = List.of(
                new Employee(1, "John", "Doe", "USA", 30),
                new Employee(2, "Jane", "Smith", "UK", 25)
        );

        String expected = "[\n" +
                "  {\n" +
                "    \"id\": 1,\n" +
                "    \"firstName\": \"John\",\n" +
                "    \"lastName\": \"Doe\",\n" +
                "    \"country\": \"USA\",\n" +
                "    \"age\": 30\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 2,\n" +
                "    \"firstName\": \"Jane\",\n" +
                "    \"lastName\": \"Smith\",\n" +
                "    \"country\": \"UK\",\n" +
                "    \"age\": 25\n" +
                "  }\n" +
                "]";

        String actual = Main.listToJson(employeeList);

        Assertions.assertEquals(expected, actual);
    }
}
