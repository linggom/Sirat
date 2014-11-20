package com.antares.sirat;



import com.antares.sirat.attribute.Ignore;
import com.antares.sirat.attribute.NOT_NULL;
import com.antares.sirat.attribute.PRIMARY_KEY;
import com.antares.sirat.attribute.UNIQUE;
import com.antares.sirat.attribute.ROW_ID;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by goman on 11/19/2014.
 */
public class Sirat {

    public static final int VERSION = 1;
    public static final String NAME = "Notepad";
    public static final String AUTHORITY = "com.srin.notepad";

    private static final String ROW_ID = "ROWID";
    private static final String ROW_ID_ATTRIBUTE = " "+ ROW_ID + " integer PRIMARY KEY AUTOINCREMENT ";
    private static final String UNIQUE_ATTRIBUTE = " UNIQUE ";
    private static final String PRIMARY_KEY_ATTRIBUTE = " PRIMARY KEY ";
    private static final String NOT_NULL_ATTRIBUTE = " NOT NULL ";
    private static final String CREATE_TABLE_COMMAND = "CREATE TABLE ";
    private static final String OPEN_BRACKET = "(";
    private static final String CLOSE_BRACKET = ")";
    private static final String COMMA = ", ";

    public enum DataType {
        TEXT,
        INTEGER,
        DOUBLE,
        FLOAT,
        LONG,
        BOOLEAN,
        BYTE,
        SHORT,
        CHAR;

    }


    /**
     * Init the datatype that can be used to make the table
     */
    private static final Map<String, String> mMapDataTypeWithField = new HashMap<String, String>() {

        public final Map<String, String> initMapping() {
            Map<String, String> map = new HashMap<String, String>();
            map.put(String.class.getName(), DataType.TEXT.toString());
            map.put(Integer.class.getName(), DataType.INTEGER.toString());
            map.put("int", DataType.INTEGER.toString());
            map.put(Long.class.getName(), DataType.LONG.toString());
            map.put("long", DataType.LONG.toString());
            map.put(Double.class.getName(), DataType.DOUBLE.toString());
            map.put("double", DataType.DOUBLE.toString());
            map.put(Float.class.getName(), DataType.FLOAT.toString());
            map.put("float", DataType.FLOAT.toString());
            map.put(Boolean.class.getName(), DataType.BOOLEAN.toString());
            map.put("boolean", DataType.BOOLEAN.toString());
            map.put(Byte.class.getName(), DataType.BYTE.toString());
            map.put("byte", DataType.BYTE.toString());
            map.put(Short.class.getName(), DataType.SHORT.toString());
            map.put("short", DataType.SHORT.toString());
            map.put(Character.class.getName(), DataType.CHAR.toString());
            map.put("char", DataType.CHAR.toString());
            return map;
        }
    }.initMapping();


    /**
     * Add '' at the begin and end of a string
     *
     * @param value a string that will modify
     * @return a String after modift
     */
    private static final String helper_escape_string(String value) {
        return "\'" + value + "\'";
    }

    /**
     * Create a query from a simple pojo class. If there is more than 1 field has attribute
     * auto increment. It will take the first one of the attribute and ignore the rest
     *
     * @param cls                  Class that will generated the query
     * @param forceObjectAttribute if set true,  field data type will be set as text.
     *                             if set false, field will be ignore to insert as field table
     * @return String query
     */
    public static String generateQuery(Class<?> cls, boolean forceObjectAttribute) {
        String begin_query =
                CREATE_TABLE_COMMAND
                        + helper_escape_string(cls.getSimpleName().toString())
                        + OPEN_BRACKET;
        String field_query = "";
        String field_pk_query = "";
        String end_query = CLOSE_BRACKET;
        ArrayList<String> primary_key = new ArrayList<String>();
        ArrayList<String> unique_index = new ArrayList<String>();
        Field[] fields = cls.getDeclaredFields();
        boolean ai = false;
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            Annotation[] a = f.getAnnotations();
            String fieldMap = mMapDataTypeWithField.get(f.getType().getName());
            String fieldType = "";
            if (fieldMap == null && forceObjectAttribute) {
                fieldType = DataType.TEXT.toString();
            } else if (fieldMap != null) {
                fieldType = fieldMap;
            } else {
                continue;
            }
            String fieldName = helper_escape_string(f.getName());
            String newField = ((field_query.equals("") || field_query == null) ? "" : COMMA) + fieldName + " " + fieldType;
            if (f.getName().toLowerCase().equals(ROW_ID.toLowerCase()) && ai == true){
                continue;
            }
            for (Annotation an : a) {
                String annotation = an.annotationType().getSimpleName();
                if (annotation.equals(ROW_ID.class.getSimpleName())) {
                    if (ai == false) {
                        int idx = -1;
                        if ((idx = unique_index.indexOf(fieldName)) > -1) {
                            unique_index.remove(idx);
                        }
                        if ((idx = primary_key.indexOf(fieldName)) > -1) {
                            primary_key.remove(idx);
                        }
                        newField = ((field_query.equals("") || field_query == null) ? "" : COMMA) + ROW_ID_ATTRIBUTE;
                        ai = true;
                    }
                    break;
                } else if (annotation.equals(PRIMARY_KEY.class.getSimpleName())) {
                    primary_key.add(fieldName);
                } else if (annotation.equals(NOT_NULL.class.getSimpleName())) {
                    newField += NOT_NULL_ATTRIBUTE;
                } else if (annotation.equals(UNIQUE.class.getSimpleName())) {
                    unique_index.add(fieldName);
                } else if (annotation.equals(PRIMARY_KEY.class.getSimpleName())) {
                    newField += NOT_NULL_ATTRIBUTE;
                    primary_key.add(fieldName);
                }
                else if (annotation.equals(Ignore.class.getSimpleName())){
                    newField = "";
                    break;
                }
            }
            field_query += newField;
        }
        if (ai == false) {
            field_pk_query =
                    createElementAsText(COMMA + PRIMARY_KEY_ATTRIBUTE + OPEN_BRACKET, primary_key, CLOSE_BRACKET);
        }
        field_pk_query +=
                createElementAsText(COMMA + UNIQUE_ATTRIBUTE + OPEN_BRACKET, unique_index, CLOSE_BRACKET);
        return begin_query + field_query + field_pk_query + end_query;
    }


    /**
     * See {@link #generateQuery(Class, boolean)}
     *
     * @param cls
     * @return
     */
    public static String generateQuery(Class<?> cls) {
        return generateQuery(cls, false);
    }

    /**
     * Create a sentence separate with comma. Example : 1, 2, 3, 4
     *
     * @param prefix  prefix for the sentence
     * @param list    collection of data that would provide as sentence
     * @param postfix postfix for the sentence
     * @return combination of prefix, list, and postfix
     */
    public static String createElementAsText(String prefix, List<String> list, String postfix) {
        String combineText = "";
        for (int j = 0, leng = list.size(); j < leng; j++) {
            String field = list.get(j);
            if (leng == 1) {
                combineText += prefix + field + postfix;
            } else {
                if (j == 0) {
                    combineText += prefix + field;
                } else if (j == leng - 1) {
                    combineText += COMMA + field + postfix;
                } else {
                    combineText += COMMA + field;
                }
            }
        }
        return combineText;
    }


}

