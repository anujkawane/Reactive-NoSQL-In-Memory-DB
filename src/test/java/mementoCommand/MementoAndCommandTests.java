package mementoCommand;

import com.akawane0813.database.Array;
import com.akawane0813.database.CustomObject;
import com.akawane0813.database.Database;
import com.akawane0813.decorator.DatabaseExecutor;
import com.akawane0813.exception.KeyNotFoundException;
import com.akawane0813.fileio.FileOperations;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MementoAndCommandTests {

    FileOperations fileOperations;
    DatabaseExecutor databaseExecutor;
    private final String DATABASE_MEMENTO_FILEPATH = "src/main/resources/dbSnapshot.txt";
    private final String COMMANDS_FILEPATH = "src/main/resources/commands.txt";
    private final int BACKUP_INTERVAL_SECONDS = 6;

    @BeforeEach
    public void setUp() {
        databaseExecutor = new DatabaseExecutor(new Database());
        fileOperations = new FileOperations();
        fileOperations.clearFile(new File(DATABASE_MEMENTO_FILEPATH));
        fileOperations.clearFile(new File(COMMANDS_FILEPATH));
    }

    @Test
    public void testCommand() throws Exception {
        List testData = getTestData();
        List<String> expected = new ArrayList<>();
        for(int i = 1 ; i <= testData.size(); i++){
            databaseExecutor.put("Key"+i, testData.get(i-1));
            expected.add("PUT->Key"+i+"->" + databaseExecutor.get("Key"+i).toString());
        }

        List<String> actual = fileOperations.readCommandsFromFile(new File(COMMANDS_FILEPATH));

        Assert.assertEquals(expected, actual);

    }

    @Test
    public void mementoCreation() throws Exception {
        List testData = getTestData();
        int i = 1;
        for(Object o : testData){
            databaseExecutor.put("Key"+i, o);
            i++;
        }
        Thread.sleep(BACKUP_INTERVAL_SECONDS * 1000);
        databaseExecutor.put("Job", "SDE");

        DatabaseExecutor restoredDB = new DatabaseExecutor(new Database());

        i = 1;
        for(Object o : testData){
            Assert.assertEquals(o, restoredDB.get("Key"+i));
            i++;
        }
    }

//     This test will recover memento and then perform command execution from file
    @Test
    public void testRecover() throws Exception {
        List testData = getTestData();
        int i;
        for(i = 1 ; i <= testData.size(); i++){
            databaseExecutor.put("Key"+i, testData.get(i-1));
        }

        Thread.sleep(BACKUP_INTERVAL_SECONDS * 1000);

        databaseExecutor.put("Key"+i++, "Developer");
        databaseExecutor.put("Key"+i++, "SDSU");

        DatabaseExecutor restoredDB = new DatabaseExecutor(new Database());

        i = 1;
        for(Object o : testData){
            Object o1 = restoredDB.get("Key" + i);
            Assert.assertEquals(o, o1);
            i++;
        }
    }

    public List getTestData() throws KeyNotFoundException {
        List testData = new ArrayList();

        Array array = new Array();
        array.put("Anuj");
        array.put(26);

        CustomObject customObject = new CustomObject();
        customObject.put("Location", "San Diego");
        customObject.put("Contact", "[000-000-0000]");

        testData.add(array);
        testData.add(customObject);
        testData.add("Engineer");
        testData.add(100);

        return testData;
    }
}
