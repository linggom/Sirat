package com.antares.sirat;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by goman on 11/21/2014.
 */
public class SiratTable {

    public String tableName;
    public ArrayList<SiratField> listOfFields;
    public ArrayList<String> listOfUniques;
    public ArrayList<String> listOfPrimaryKey;
    private boolean isAllowAnotherPrimary = false;


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public SiratTable(Class cls) {
        setTableName(cls.getName());
        isAllowAnotherPrimary = true;
        listOfPrimaryKey = new ArrayList<String>();
        listOfFields = new ArrayList<SiratField>();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields){
            SiratField sField = new SiratField(field);
            if (sField.isPrimaryKey()){
                if(sField.isAutoIncrement()){
                    isAllowAnotherPrimary = false;
                    listOfPrimaryKey = null;
                }
                else if(sField.isPrimaryKey()){
                    if (isAllowAnotherPrimary){
                        listOfPrimaryKey.add(sField.getName());
                    }
                    listOfUniques.add(sField.getName());
                }
                else if (sField.isUnique()){
                    listOfUniques.add(sField.getName());
                }
            }
            listOfFields.add(sField);
        }
    }

    public String generateQuery(String query){
        return  "";
    }
}
