package database;

import com.akawane0813.database.Array;
import com.akawane0813.database.CustomObject;
import com.akawane0813.database.Database;
import com.akawane0813.decorator.DatabaseExecutor;
import com.akawane0813.exception.IncompatibleTypeException;
import com.akawane0813.exception.KeyNotFoundException;
import com.akawane0813.fileio.FileOperations;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

public class DatabaseTests {

    FileOperations fileOperations;
    DatabaseExecutor databaseExecutor;
    private final String DATABASE_MEMENTO_FILEPATH = "src/main/resources/dbSnapshot.txt";
    private final String COMMANDS_FILEPATH = "src/main/resources/commands.txt";

    @BeforeEach
    public void setUp() {
        databaseExecutor = new DatabaseExecutor(new Database());
        fileOperations = new FileOperations();
        fileOperations.clearFile(new File(DATABASE_MEMENTO_FILEPATH));
        fileOperations.clearFile(new File(COMMANDS_FILEPATH));
    }

    @Test
    public void testPutInDB() throws Exception {
        databaseExecutor.put("Key", "John");
        Assert.assertEquals("John", databaseExecutor.get("Key"));
    }

    @Test
    public void testRemoveFromDB() throws Exception {
        databaseExecutor.put("Key", "John");
        databaseExecutor.remove("Key");
        Assertions.assertThrows(KeyNotFoundException.class, () -> databaseExecutor.get("Key"));
    }

    @Test
    public void testPutFromStringArray() throws Exception {
        String value = "";
        databaseExecutor.put("Key", new Array().fromString("[\"Anuj\", 2.0, {\"name\": \"Anuj\", \"Number\": 21.0}]"));
        Array array = new Array();
        array.put("Anuj");
        array.put(2.0);

        CustomObject customObject = new CustomObject();
        customObject.put("name","Anuj");
        customObject.put("Number",21.0);
        array.put(customObject);

        Assert.assertEquals(array, databaseExecutor.get("Key"));
    }

    @Test
    public void testPutFromStringObject() throws Exception {
        String value = "";
        databaseExecutor.put("Key", new CustomObject().fromString("{\"name\": \"Roger\", \"age\": 21.0}"));
        CustomObject customObject = new CustomObject();
        customObject.put("name","Roger");
        customObject.put("age",21.0);
        Assert.assertEquals(customObject, databaseExecutor.get("Key"));
    }

    @Test
    public void testGetXException() throws Exception {
        databaseExecutor.put("Key", "John");
        Assertions.assertThrows(IncompatibleTypeException.class, () -> databaseExecutor.getInt("Key"));
    }
}
