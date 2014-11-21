package com.antares.sirat;

import com.antares.sirat.attribute.Ignore;
import com.antares.sirat.attribute.NOT_NULL;
import com.antares.sirat.attribute.PRIMARY_KEY;
import com.antares.sirat.attribute.ROW_ID;
import com.antares.sirat.attribute.UNIQUE;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by goman on 11/21/2014.
 */
public class SiratField {
    private String name;
    private Boolean primaryKey;
    private Boolean unique;
    private Boolean auto_increment;
    private Boolean notNull;
    private String dataType;

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

    public Boolean isAutoIncrement() {
        return auto_increment;
    }

    public void setAuto_increment(Boolean auto_increment) {
        this.auto_increment = auto_increment;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }


    public SiratField(Field field) {
        String name = field.getName();
        String fieldMap = mMapDataTypeWithField.get(field.getType().getName());
        Annotation [] annotaion = field.getAnnotations();


    }

    public SiratField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Boolean isUnique() {
        return unique;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

    public Boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(Boolean notNull) {
        this.notNull = notNull;
    }

    public static SiratField create(Field field){
        return create(field.getName(),
                mMapDataTypeWithField.get(field.getType().getName()),
                field.getAnnotations()
                );
    }
    public static SiratField create(String name, String type, Annotation [] annotations){
        SiratField field = new SiratField(name);
        field.setDataType(type);
        for (Annotation an : annotations) {
            String annotation = an.annotationType().getSimpleName();
            if (annotation.equals(ROW_ID.class.getSimpleName())){
                field = new SiratField(name);
                field.setDataType(type);
                field.setPrimaryKey(true);
                field.setAuto_increment(true);
                return field;
            }else if (annotation.equals(PRIMARY_KEY.class.getSimpleName())) {
                field.setPrimaryKey(true);
                field.setNotNull(true);
                field.setUnique(true);
            } else if (annotation.equals(NOT_NULL.class.getSimpleName())) {
                field.setNotNull(true);
            } else if (annotation.equals(UNIQUE.class.getSimpleName())) {
                field.setUnique(true);
            } else if (annotation.equals(PRIMARY_KEY.class.getSimpleName())) {
                field.setUnique(true);
            }
            else if (annotation.equals(Ignore.class.getSimpleName())){
                return null;
            }
        }
        return  field;
    }
}
