package com.avis.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.avis.constants.AvisConstants;
import com.avis.constants.TestNGParams;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;

public class CSVUtils {
    private static String cellValue;
    static Object[][] obj1;
    static Object[][] obj2;
    static Object[][] obj3;
    static Object[][] obj4;
    static Object[][] obj5;
    static Object[][] obj6;
    static Object[][] obj7;
    static Object[][] obj8;
    static Object[][] obj9;
    static Object[][] obj10;

    public static String method = null;
    static String filepath = null;

    @SuppressWarnings("resource")
    public static Object[][] getTableArray(String methodName) throws Exception {
        filepath = getCsvTestDataFilePath();
        String[][] tabArray = null;
        BufferedReader br = null;
        String line = "";
        String[] value = null;
        int rowcount = 0;
        int colcount = 0;
        try {
            Thread.sleep(1000);
            br = new BufferedReader(new FileReader(filepath));
            while ((line = br.readLine()) != null) {
                value = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (value[0].equals(methodName)) {
                    rowcount++; // count total no of rows
                    colcount = value.length; // count total no of columns
                }
            }

            if (rowcount == 0) {
                if (filepath.contains("nonUS")) {
                    filepath = filepath.replace("nonUS", "US");
                    Object[][] temp = getTableArray(methodName);
                    filepath = filepath.replace("US", "nonUS");
                    return temp;
                } else {
                    Assert.fail();
                }
            }
            line = "";
            br = new BufferedReader(new FileReader(filepath));
            int i = 0;

            tabArray = new String[rowcount][colcount - 1];
            while ((line = br.readLine()) != null) {
                value = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); // ignore
                // comma
                // between
                // double
                // quotes
                int p = 1;
                if (value[0].equals(methodName)) {
                    for (int j = 0; j < colcount - 1; j++) {
                        tabArray[i][j] = value[p].replaceAll("^\"|\"$", ""); // remove
                        // double
                        // quotes
                        p++;
                    }
                    i++;
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (tabArray); // returns the complete rows which has same rowName

    }

    public static String getTextString(String text) {
        return getCsvData(text); // pass the error message
    }

    public static String getCsvData(String rowname) {
        BufferedReader br = null;
        try {
            String filepath = AvisConstants.TextComparisionFilePath;
            String line = "";
            String[] value = null;
            br = new BufferedReader(new FileReader(filepath));
            while ((line = br.readLine()) != null) {
                value = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); // ignore
                // comma
                // between
                // double
                // quotes
                if (value[0].equals(rowname)) {
                    cellValue = value[1].replaceAll("^\"|\"$", ""); // adds
                    // error
                    // message
                }
            }
            br.close();

        } catch (Exception e) {
            Reporter.log("File Not Found at " + rowname);
            e.printStackTrace();
        }
        return cellValue;

    }

    public static String getRandomText(String text) {
        return getRandomData(text); // pass the error message
    }

    public static String getRandomData(String rowname) {
        BufferedReader br = null;
        try {
            String filepath = "./src/Datasheet/CreditCardNumbers.csv";
            String line = "";
            String[] value = null;
            br = new BufferedReader(new FileReader(filepath));
            while ((line = br.readLine()) != null) {
                value = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); // ignore
                // comma
                // between
                // double
                // quotes
                if (value[0].equals(rowname)) {
                    cellValue = value[1].replaceAll("^\"|\"$", ""); // adds
                    // error
                    // message
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return cellValue;
    }

    @DataProvider
    public static Object[][] testData(Method method) throws Exception {
        return CSVUtils.getTableArray(method.getName());
    }

    public static void excelWrite(String textToBeWritten) throws Exception {

        //Create blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        //Create a blank sheet
        XSSFSheet spreadsheet = workbook.createSheet(" SheetName ");

        //Create row object
        XSSFRow row;

        //This data needs to be written (Object[])
        Map<String, Object[]> dataRow = new TreeMap<String, Object[]>();
        dataRow.put("1", new Object[]{
                textToBeWritten});

        //Iterate over data and write to sheet
        Set<String> keyid = dataRow.keySet();
        int rowid = 0;

        for (String key : keyid) {
            row = spreadsheet.createRow(rowid++);
            Object[] objectArr = dataRow.get(key);
            int cellid = 0;

            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellid++);
                cell.setCellValue((String) obj);
            }
        }
        //Write the workbook in file system
        FileOutputStream out = new FileOutputStream(
                new File("./ExcelWrittenlogs/WriteSheet.xlsx"));

        workbook.write(out);
        out.close();
        System.out.println(".xlsx written successfully");
    }

    public static String getCsvTestDataFilePath(){
        String DataParameter;
        DataParameter = AvisConstants.CSV_TESTDATA_BASE_PATH + TestNGParams.Brand + "_Datasheets/" + TestNGParams.Brand;
        if (TestNGParams.environment.contains("www")) {
            DataParameter = DataParameter + "Production";
        }
        DataParameter = DataParameter + "_DataParameter" + "_" + TestNGParams.domain + ".csv";
        System.out.println("The Data File used is  \"" + DataParameter + "\"");
     return DataParameter;
    }

}