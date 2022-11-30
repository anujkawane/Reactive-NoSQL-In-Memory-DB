package transaction;

import com.akawane0813.database.Array;
import com.akawane0813.database.CustomObject;
import com.akawane0813.database.Database;
import com.akawane0813.decorator.DatabaseExecutor;
import com.akawane0813.exception.KeyNotFoundException;
import com.akawane0813.fileio.FileOperations;
import com.akawane0813.transaction.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TransactionTest {
    FileOperations fileOperations;
    DatabaseExecutor databaseExecutor;
    private final String DATABASE_MEMENTO_FILEPATH = "src/main/resources/dbSnapshot.txt";
    private final String COMMANDS_FILEPATH = "src/main/resources/commands.txt";
    private final int BACKUP_INTERVAL_SECONDS = 5;
    private Database database;
    @BeforeEach
    public void setUp() {
        database = new Database();
        databaseExecutor = new DatabaseExecutor(database);
        fileOperations = new FileOperations();
        fileOperations.clearFile(new File(DATABASE_MEMENTO_FILEPATH));
        fileOperations.clearFile(new File(COMMANDS_FILEPATH));
    }

    @Test
    void commitTransaction() throws Exception {
        List testData = getTestData();
        int i;
        for(i = 1 ; i <= testData.size(); i++){
            databaseExecutor.put("Key"+i, testData.get(i-1));
        }
        Transaction transaction = database.transaction();
        transaction.put("Key100","Test100");
        transaction.commit();
        Assertions.assertTrue(database.containsKey("Key100"));
    }

    @Test
    void abortTransactionAndIsActive() throws Exception {
        List testData = getTestData();
        int i;
        for(i = 1 ; i <= testData.size(); i++){
            databaseExecutor.put("Key"+i, testData.get(i-1));
        }
        Transaction transaction = database.transaction();
        transaction.put("Key100","Test100");
        transaction.abort();
        Assertions.assertFalse(database.containsKey("Key100"));
        Assertions.assertFalse(transaction.isActive());

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
