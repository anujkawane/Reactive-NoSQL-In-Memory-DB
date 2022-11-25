package com.akawane0813.memento;

public class MementoCaretaker {
    private final String DATABASE_MEMENTO_FILEPATH = "src/main/resources/dbSnapshot.txt";
    private final String COMMANDS_FILEPATH = "src/main/resources/commands.txt";

//    public void snapshot(){
//
//        FileOperations fileOperation = new FileOperations();
//        fileOperation.writeObjectToFile(new File(DATABASE_MEMENTO_FILEPATH), this);
//
////        clears the commands from file after backup
//        FileOperations.clearFile(new File(COMMANDS_FILEPATH));
//    }
//
//    public void snapshot(File commands, File dbSnapshot){
//        FileOperations fileOperation = new FileOperations();
//        fileOperation.writeObjectToFile(dbSnapshot, this);
//
////        clears the commands from file after backup
//        FileOperations.clearFile(commands);
//    }
//
//    public void recover(){
//        FileOperations fileOperation = new FileOperations();
//        Database restoredDB =
//                (Database) fileOperation.readObjectFromFile(new File(DATABASE_MEMENTO_FILEPATH));
//        if(restoredDB != null) {
//            database = restoredDB.database;
//        }
//        // CODE TO RE-EXECUTE COMMANDS FROM commands FILE INTO EXISTING OBJECT
//    }
//
//    public void clear(){
//        FileOperations.clearFile(new File(DATABASE_MEMENTO_FILEPATH));
//    }
//    public void recover(File commands, File dbSnapshot){
//        FileOperations fileOperation = new FileOperations();
//        Database restoredDB =
//                (Database) fileOperation.readObjectFromFile(dbSnapshot);
//        if(restoredDB != null) {
//            database = restoredDB.database;
//        }
//        // CODE TO RE-EXECUTE COMMANDS FROM commands FILE INTO EXISTING OBJECT
//    }
}
