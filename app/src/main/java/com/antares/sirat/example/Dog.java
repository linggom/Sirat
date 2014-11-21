package com.antares.sirat.example;

import com.antares.sirat.SiratObject;
import com.antares.sirat.attribute.NOT_NULL;
import com.antares.sirat.attribute.PRIMARY_KEY;

/**
 * Created by goman on 11/21/2014.
 */
public class Dog extends SiratObject {
    @NOT_NULL private String name;
    @PRIMARY_KEY private String id;
    private String type;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
