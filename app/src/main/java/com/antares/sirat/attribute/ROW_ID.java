package com.antares.sirat.attribute;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by goman on 11/19/2014.
 * if we set as autoincrement in field, WARNING!!! the name of the field in table will change to be <b>rowid</b>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ROW_ID {
}
