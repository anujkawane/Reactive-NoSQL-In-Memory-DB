package com.akawane0813;


import com.akawane0813.database.Array;
import com.akawane0813.database.CustomObject;
import com.akawane0813.database.Database;
import com.akawane0813.decorator.DatabaseExecutor;

public class Test {

    public static void main(String[] args) throws Exception {

        DatabaseExecutor databaseExecutor = new DatabaseExecutor(new Database());


        Array array = new Array();
        array.put("Anuj");
        array.put(26);

        Array dob = new Array();
        dob.put("March");
        dob.put(26);
        dob.put(1996);

        CustomObject customObject = new CustomObject();
        customObject.put("CustomDOB", dob);


        databaseExecutor.put("Name", array);
        databaseExecutor.put("DOB", customObject);


        databaseExecutor.getArray("Name").put("Kshitij");
        databaseExecutor.put("AGE", 30);

        Thread.sleep(6000);

        CustomObject newCO = new CustomObject();
        newCO.put("Test1", "test1");
        newCO.put("Test2", "test2");

        databaseExecutor.put("TEST", newCO);

        databaseExecutor.getObject("TEST").remove("Test2");

        System.out.println(databaseExecutor);
    }
}
