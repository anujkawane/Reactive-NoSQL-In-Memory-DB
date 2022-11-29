package com.akawane0813.fileio;

import java.io.*;
import java.util.*;

public class FileOperations {

    private final String FILEPATH_TO_STORE_OBJECT_STATE = "src/main/resources/dbSnapshot.txt";
    private final String FILEPATH_TO_STORE_COMMANDS = "src/main/resources/commands.txt";
    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public boolean writeObjectToFile(File file, Object serObj) {
        try {
            fileOutputStream = new FileOutputStream(file);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(serObj);
            if(objectInputStream != null)
                objectOutputStream.close();
            if (fileOutputStream != null)
                fileOutputStream.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public Object readObjectFromFile(File file){
        try {

            fileInputStream = new FileInputStream(file);
            objectInputStream = new ObjectInputStream(fileInputStream);

            Object object = objectInputStream.readObject();
            objectInputStream.close();
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            return object;

        } catch (Exception ex) {
            return null;
        }
    }


    public List<String> readCommandsFromFile(File file){
        List<String> result = new ArrayList<>();
        Scanner scanner;
        try {
            fileInputStream = new FileInputStream(file);
            scanner = new Scanner(fileInputStream, "UTF-8");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(line.equals("")) continue;
                result.add(line);
            }

            if (fileInputStream != null) {
                fileInputStream.close();
            }
            scanner.close();
        } catch (Exception e){
            return new ArrayList<>();
        }
        return result;
    }

    public boolean writeCommandsToFile(File file, String value) {
        try{
            fileOutputStream = new FileOutputStream(FILEPATH_TO_STORE_COMMANDS, true);
            byte[] strToBytes = value.getBytes();
            fileOutputStream.write(System.getProperty("line.separator").getBytes());
            fileOutputStream.write(strToBytes);
            fileOutputStream.close();
            return true;
        } catch (Exception ex){
            return false;
        }
    }

    public static void clearFile(File file) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PrintWriter printWriter = new PrintWriter(fileWriter, false);
        printWriter.flush();
        printWriter.close();

        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
