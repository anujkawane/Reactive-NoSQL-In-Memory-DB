package com.akawane0813.decorator;

import com.akawane0813.database.Database;
import com.akawane0813.fileio.FileOperations;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Executor {

    private final String COMMANDS_FILEPATH = "src/main/resources/commands.txt";
    private Database db;
    private FileOperations fileOperation;

    protected File commandFile;

    private static Executor executor = null;

    public static Executor getInstance(Database db, File commandFile) {
        if(executor == null) {
            executor = new Executor( db, commandFile);
        }
        return executor;
    }

    public static Executor Executor() {
        return executor;
    }

    private Executor(Database database, File commandFile) {
        this.commandFile = commandFile;
        db = database;
        fileOperation = new FileOperations();
    }

    public Database getDatabase() {
        return db;
    }

    /**
     * Return list of commands in given file
     * @param file From which commands need to be restored
     * @return List of Commands
     */
    public List<List<String>> getCommands(File file ) {
        List<List<String>> commands = new ArrayList<>();
        try {
            List<String> operations = fileOperation.readCommandsFromFile(file);
            for (String operation : operations) {
                if(operation.length()>0) {
                    String[] res = operation.split("->");
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

    public void writeToFile(String operation) {
        if (commandFile == null) {
            commandFile = new File(COMMANDS_FILEPATH);
        }
        fileOperation.writeCommandsToFile(commandFile,operation);
    }
}
