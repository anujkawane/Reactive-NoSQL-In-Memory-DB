package com.akawane0813.fileio;

import java.io.*;
import java.util.*;

public class FileOperations {

    final String FILEPATH_TO_STORE_OBJECT_STATE = "src/main/resources/dbSnapshot.txt";
    final String FILEPATH_TO_STORE_COMMANDS = "src/main/resources/commands.txt";
    FileInputStream fileInputStream;
    FileOutputStream fileOutputStream;
    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;

    public boolean writeObjectToFile(String fileName, Object serObj) {
        try {
            fileOutputStream = new FileOutputStream(fileName);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(serObj);
            objectOutputStream.close();
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public Object readObjectFromFile(String fileName){
        try {

            fileInputStream = new FileInputStream(fileName);
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

    public static void clearFile(String file) {
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
