package com.akawane0813.fileio;

import java.io.*;
import java.util.*;

public class FileOperations {

    final String FILEPATH_TO_STORE_OBJECT_STATE = "src/main/resources/State.txt";
    final String FILEPATH_TO_STORE_COMMANDS = "src/main/resources/Command.txt";
    FileInputStream fileInputStream;
    FileOutputStream fileOutputStream;
    ObjectOutputStream objectOutputStream;

    public boolean writeObjectToFile(Object serObj) {
        try {
            fileOutputStream = new FileOutputStream(FILEPATH_TO_STORE_OBJECT_STATE);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(serObj);
            objectOutputStream.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public Object readObjectFromFile(){
        try {

            fileInputStream = new FileInputStream(FILEPATH_TO_STORE_OBJECT_STATE);
            ObjectInputStream objectOut = new ObjectInputStream(fileInputStream);

            Object object = objectOut.readObject();
            objectOut.close();
            return object;

        } catch (Exception ex) {
            return null;
        }
    }


    public List<String> readCommandsFromFile() throws IOException {
        List<String> result = new ArrayList<>();
        Scanner scanner = null;
        try {
            fileInputStream = new FileInputStream(FILEPATH_TO_STORE_COMMANDS);
            scanner = new Scanner(fileInputStream, "UTF-8");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.add(line);
            }

            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (scanner != null) {
                scanner.close();
            }
        } catch (Exception e){
            return new ArrayList<>();
        }
        return result;
    }

    public boolean writeCommandsToFile(String value) {
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
}
