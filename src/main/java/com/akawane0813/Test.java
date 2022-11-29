package com.akawane0813;


import com.akawane0813.database.Array;
import com.akawane0813.database.CustomObject;
import com.akawane0813.database.Database;
import com.akawane0813.decorator.DatabaseExecutor;

public class Test {

    public static void main(String[] args) throws Exception {

        String COMMANDS_FILEPATH = "src/main/resources/commands.txt";
        String DATABASE_MEMENTO_FILEPATH = "src/main/resources/dbSnapshot.txt";

//        Array array = new Array();
//        array.put("Anuj");
//        array.put(26);
//
//        Array dob = new Array();
//        dob.put("March");
//        dob.put(26);
//        dob.put(1996);
//
//        CustomObject customObject = new CustomObject();
//        customObject.put("CustomDOB", dob);
//
        DatabaseExecutor databaseExecutor = new DatabaseExecutor(new Database());
//
//        databaseExecutor.put("Name", array);
//        Thread.sleep(5000);
//        databaseExecutor.put("DOB", customObject);
//        databaseExecutor.put("AGE", 30);
//
//        Thread.sleep(5001);
//        databaseExecutor.getArray("Name").put("KAWANE");

        Thread.sleep(5001);
        System.out.println(databaseExecutor);
    }
}
