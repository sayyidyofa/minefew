package io.github.sayyidyofa.minefew.utils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import io.github.sayyidyofa.minefew.Minefew;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Helpers {
    public static String serialize(Object object) {
        return new Gson().toJson(object);
    }

    public static Object deserialize(String jsonString, Class refClass) {
        Object object = null;
        try {
            object =  new Gson().fromJson(jsonString, refClass);
        } catch (JsonParseException exception) {
            Minefew.LOGGER.error(
                    String.format(
                            "FATAL: can't convert JSON to type: %s. String content: %s",
                            refClass.getCanonicalName(),
                            jsonString
                    ));
        }
        return object;
    }

    public static HashMap<?, ?> readJSONFile(String path) {
        Gson gson = new Gson();
        Reader reader = null;
        try {
            reader = Files.newBufferedReader(Paths.get(path));
        } catch (IOException e) {
            Minefew.LOGGER.error(
                    String.format(
                            "FATAL: can't read JSON file on path: %s",
                            path
                    ));
        }
        assert reader != null;
        return gson.fromJson(reader, HashMap.class);
    }

    public static HashMap<Object, Object> arrayToHashMap(Object[][] objectArray) {
        HashMap<Object, Object> retHashMap = new HashMap<>();
        for (Object[] subArray : objectArray) {
            if (subArray.length > 2) {
                System.out.println("FATAL: HashMap index length is more than 2. Returned null");
                return null;
            }
            else if (subArray.length < 2) {
                retHashMap.put(subArray[0], null);
            } else {
                retHashMap.put(subArray[0], subArray[1]);
            }
        }
        return retHashMap;
    }

    public static String getIpFromAddress(String remoteAddress) { // remove '/' and port number from remote address
        String ipAddress = remoteAddress.replace("/", "");
        ipAddress = StringUtils.substringBefore(ipAddress, ":");
        return ipAddress;
    }

    public static Date calendarToDate(String timestamp) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        try {
            calendar.setTime(sdf.parse(timestamp));
        } catch (ParseException e) {
            System.out.println("FATAL: Failed to convert java.util.Calendar type to java.util.Date type");
            e.printStackTrace();
        }
        return calendar.getTime();
    }

    public static Integer getRowCount(ResultSet rows) {
        try {
            return rows.getRow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<List<String>> resultSetToList(ResultSet rows, String[] columnNames) {
        List<List<String>> rowsList = new ArrayList<>();
        List<String> columnList = new ArrayList<String>();
        try {
            while (rows.next()) {
                for (String columnName : columnNames) {
                    columnList.add(rows.getString(columnName));
                }
                rowsList.add(new ArrayList<>(columnList));
                columnList.clear();
            }
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
        return rowsList;
    }

    public static List<List<String>> resultSetToList(ResultSet rows, HashMap<Object, Object> rawColumnMap) {
        HashMap<String, Callbacks> columnMap = new HashMap(rawColumnMap);
        List<List<String>> rowsList = new ArrayList<>();
        List<String> columnList = new ArrayList<>();
        try {
            while (rows.next()) {
                for (Map.Entry<String, Callbacks> columnMapElement : columnMap.entrySet()) {
                    if (columnMapElement.getValue() == null) {
                        columnList.add(rows.getString(columnMapElement.getKey()));
                    } else {
                        columnList.add(
                                String.valueOf(
                                        columnMapElement.getValue().singleMorpher(
                                                rows.getString(columnMapElement.getKey()
                                                )
                                        )
                                )
                        );
                    }
                }
                rowsList.add(new ArrayList<>(columnList));
                columnList.clear();
            }
        }
        catch (SQLException exception) {
            System.out.println("FATAL: Failed to read query rows data.");
            exception.printStackTrace();
        }
        catch (NullPointerException exception) {
            System.out.println("FATAL: Check the HashMap<> value!");
            exception.printStackTrace();
        }
        return rowsList;
    }
}
