package com.akawane0813;

import com.akawane0813.database.Array;
import com.akawane0813.database.CustomObject;
import com.akawane0813.database.DatabaseImpl;

import java.util.*;

public class Test {

    public int playlist(List<Integer> songs) {
        int remain[] = new int[60];
        int count = 0;
        for (int t: songs) {
            if (t % 60 == 0) {
                count += remain[0];
            } else {
                count += remain[60 - t % 60];
            }
            remain[t % 60]++;
        }
        return count;
    }

    public static void main(String[] args) throws Exception {

        DatabaseImpl db = new DatabaseImpl();
//
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


//        System.out.println(db);
//        db.clear();
//
//        System.out.println(db);

//        db.put("Array", 10);
//        System.out.println(db);
//        db.recover();
        System.out.println(db);



    }
}
