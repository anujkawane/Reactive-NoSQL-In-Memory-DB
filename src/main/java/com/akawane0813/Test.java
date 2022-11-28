package com.akawane0813;

import com.akawane0813.database.Array;
import com.akawane0813.database.CustomObject;
import com.akawane0813.database.Database;
import com.akawane0813.decorator.ArrayExecuter;
import com.akawane0813.decorator.DatabaseExecutor;
import com.akawane0813.fileio.FileOperations;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Test {

    public static void main(String[] args) throws Exception {

        String COMMANDS_FILEPATH = "src/main/resources/commands.txt";
        String DATABASE_MEMENTO_FILEPATH = "src/main/resources/dbSnapshot.txt";

        Database db = new Database();
//        Array a = new Array();
//        a.put("ANUJ");
//        a.put(1);
//
//        Array b = new Array();
//        b.put("2ndObj");
//        b.put(2);
////
//        CustomObject ob = new CustomObject();
//        ob.put("ANUJ", 4);
//        ob.put("KP",b);
//        ob.put("AR", "SDASDSDSADS");
//
//        a.put(ob);
//
//        CustomObject ob2 = new CustomObject();
//        ob2.put("String in ob2", "This is CustomObject 2");
//        ob2.put("Integer in Ob2", 100);
//        ob2.put("CustomObject reference", ob);
//
//        db.put("Array", a);
//        db.put("String", "This is my string");
//        db.put("Integer", 10);
//        db.put("CustomObject", ob2);
//
//        db.put("Integer asdasd", 20);
//        db.put("Integer asd", 30);
//
//
//        db.put("ANUJ" , 100);
//        db.put("SHRI" , 500);
//        db.put("KSHI" , 800);
//        db.put("ABHI" , 900);
//        db.put("DHIR" , 150);
//        db.put("BHUV" , 930);


        System.out.println(db);
//
//
//        System.out.println(db);
//        db.clear();
//
//        System.out.println(db);
//
//        db.put("Array", 10);
//        System.out.println(db);
//        db.recover();



//        Array firstArr = new Array();
//        ArrayExecuter array = new ArrayExecuter(firstArr);
//
//        CustomObject secondObj = new CustomObject();
//        CustomObject thirdObj = new CustomObject();
//        thirdObj.put("NEW", "NEW");
//
//        secondObj.put("Kp",1);
//        firstArr.put(secondObj);
//        DatabaseExecutor db = new DatabaseExecutor(new Database());
//        db.put("KP",firstArr);
//        db.put("KKK",1);
//        db.put("dasd",2);
//        db.put("KasdasdadP",3);
//        db.put("KdsadsaddasdsadsadP",4);
////
//        db.getArray("KP").getObject(0).put("Abhi",thirdObj);
//        db.getArray("KP").put("Anuj");

//        System.out.println(db);
    }
}
