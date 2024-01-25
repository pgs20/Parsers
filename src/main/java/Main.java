import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // CSV - JSON парсер
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileNameCSV = "data.csv";
        List<Employee> listEmployee = parseCSV(columnMapping, fileNameCSV);
        writeString(listToJson(listEmployee), "data1.json");

        // XML - JSON парсер
        String fileNameXML = "data.xml";
        writeString(listToJson(parseXML(fileNameXML)), "data2.json");

        // JSON - Object Employee
        List<Employee> list = jsonToList(readString("data1.json"));

    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> listEmployee = null;
        try(CSVReader csvr = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy cpms = new ColumnPositionMappingStrategy();
            cpms.setType(Employee.class);
            cpms.setColumnMapping(columnMapping);

            CsvToBeanBuilder ctbb = new CsvToBeanBuilder<>(csvr);

            listEmployee = (List<Employee>) ctbb.withMappingStrategy(cpms).build().parse();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return listEmployee;
    }

    public static List<Employee> parseXML(String filename) {
        List<Employee> listEmployee = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(filename));
            NodeList nodeList = doc.getElementsByTagName("employee");
            for (int i = 0; i < nodeList.getLength(); ++i) {
                Node node_ = nodeList.item(i);
                if (node_.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node_;
                    listEmployee.add(new Employee(
                            Long.parseLong(getTagValue("id", element)),
                            getTagValue("firstName", element),
                            getTagValue("lastName", element),
                            getTagValue("country", element),
                            Integer.parseInt(getTagValue("age", element))
                    ));
                }
            }
        } catch (IOException | SAXException | ParserConfigurationException ex) {
            System.out.println(ex.getMessage());
        }
        return listEmployee;
    }

    public static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

    public static String listToJson(List<Employee> employeeList) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        return gson.toJson(employeeList);
    }

    public static List<Employee> jsonToList(String json) {
        List<Employee> listEmpployee = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        Gson gson = new GsonBuilder().create();
        try {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(json);
            for (int i = 0; i < jsonArray.size(); ++i) {
                listEmpployee.add(gson.fromJson(jsonArray.get(i).toString(), Employee.class));
            }
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());;
        }
        return listEmpployee;
    }

    public static void writeString(String str, String fileSaveName) {
        try(FileWriter fw = new FileWriter(new File(fileSaveName))) {
            fw.write(str);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static String readString(String filename) {
        String res = null;
        StringBuilder stringBuilder = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new FileReader(new File(filename)))) {
            while ((res = br.readLine()) != null) {
                stringBuilder.append(res);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return stringBuilder.toString().replace("  ", "");
    }


}
