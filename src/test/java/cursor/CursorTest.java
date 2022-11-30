package cursor;

import com.akawane0813.cursor.Cursor;
import com.akawane0813.database.Array;
import com.akawane0813.database.CustomObject;
import com.akawane0813.database.Database;
import com.akawane0813.decorator.DatabaseExecutor;
import com.akawane0813.observer.Observer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class CursorTest {
    @Test
    void addCursor() throws Exception {
        Array finalArray = new Array();

        Array array = new Array();
        array.put("Anuj");
        array.put(26);

        CustomObject customObject = new CustomObject();
        customObject.put("Location", "San Diego");
        customObject.put("Contact", "[000-000-0000]");

        finalArray.put(array);
        finalArray.put(customObject);
        finalArray.put(100);

        Database database = new Database();
        DatabaseExecutor databaseExecutor = new DatabaseExecutor(database);
        databaseExecutor.put("AnujK",finalArray);
        Cursor cursor = database.getCursor("AnujK");
        Observer observer = new Observer();
        cursor.addObserver(observer);
        databaseExecutor.getArray("AnujK").put("India");
        Assertions.assertEquals(observer.getUpdates().get(0),"AnujK in the DB updated with [[\"Anuj\",26],{\"Location\":\"San Diego\",\"Contact\":\"[000-000-0000]\"},100]");

    }
}
