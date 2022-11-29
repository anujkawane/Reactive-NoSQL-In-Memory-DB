package com.akawane0813.decorator;

import com.akawane0813.command.IDatabaseOperation;
import com.akawane0813.cursor.CursorMapper;
import com.akawane0813.database.Database;
import com.akawane0813.fileio.FileOperations;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Executor {
    private Stack<IDatabaseOperation> operationList;
    private Database db;
    private DatabaseExecutor databaseExecutor;
    private FileOperations fileOperation;
    protected Boolean isSavedOperation ;

    protected File commandFile;

    private static Executor singleInstance = null;

    public static Executor Executor(Database db, File commandFile) {
        if(singleInstance == null) {
            singleInstance = new Executor( db, commandFile);
        }
        return singleInstance;
    }

    public static Executor Executor() {
        return singleInstance;
    }

//    private Executor(Boolean isSavedOperation, Database database) {
//        fileOperation = new FileOperations();
//        this.isSavedOperation = isSavedOperation;
//        this.db = database;
//    }

    private Executor(Database database, File commandFile) {
        this.commandFile = commandFile;
        db = database;
        fileOperation = new FileOperations();
    }

    public Database getDatabase() {
        return db;
    }

    public List<List<String>> retrieveOperations(File file ) {
        List<List<String>> commands = new ArrayList<>();
        try {
            List<String> operations = fileOperation.readCommandsFromFile(file);
            for (String operation : operations) {
                if(operation.length()>0) {
                    String[] res = operation.split(" ");
                    List<String> resp = Arrays.stream(res).collect(Collectors.toList());
                    commands.add(resp);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fileOperation.clearFile(file);
        return commands;
    }
    public void writeToFile( String operation) {
        if (commandFile == null) {
            commandFile = new File("src/main/resources/commands.txt");
        }

        fileOperation.writeCommandsToFile(commandFile,operation);
    }
}
