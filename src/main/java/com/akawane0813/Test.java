package com.akawane0813;

import com.akawane0813.cursor.Cursor;
import com.akawane0813.database.Array;
import com.akawane0813.database.CustomObject;
import com.akawane0813.database.Database;
import com.akawane0813.decorator.ArrayExecuter;
import com.akawane0813.decorator.DatabaseExecutor;
import com.akawane0813.observer.Observer;
import com.akawane0813.transaction.Transaction;

public class Test {

    public static void main(String[] args) throws Exception {

        String COMMANDS_FILEPATH = "src/main/resources/commands.txt";
        String DATABASE_MEMENTO_FILEPATH = "src/main/resources/dbSnapshot.txt";

        Array firstArr = new Array();

        CustomObject secondObj = new CustomObject();
        CustomObject thirdObj = new CustomObject();

        secondObj.put("Kp",1);
        firstArr.put(secondObj);

        DatabaseExecutor db = new DatabaseExecutor(new Database());
        System.out.println(db.toString());
//        db.put("KP",firstArr);
//        db.getArray("KP").getObject(0).put("Abhi",thirdObj);
//        db.getArray("KP").getObject(0).getObject("Abhi").put("Kshitij","Sexy");

        System.out.println(db);
//        Cursor cursor = db.getCursor("KP");
//        Observer o = new Observer();
//        cursor.addObserver(o);
//        db.getArray("KP").put("Anuj");
//        Transaction tr = db.transaction();
//        tr.put("Kshitij","1");
//        System.out.println(db.get("Anuj"));
//        tr.abort();
////        System.out.println();
////        System.out.println(db.getArray("KP").get(1));
//        System.out.println(db.get("KP"));
//        FileOperations fileOperation = new FileOperations();
    }
}
