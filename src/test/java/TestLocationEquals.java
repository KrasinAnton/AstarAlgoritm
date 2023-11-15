import org.junit.Test;
import ru.krasin.AStarAlgorithm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestLocationEquals {

    @Test
    public void testLocationEquals() {
        AStarAlgorithm.Location location1 = new AStarAlgorithm.Location(1, 2);
        AStarAlgorithm.Location location2 = new AStarAlgorithm.Location(1, 2);
        AStarAlgorithm.Location location3 = new AStarAlgorithm.Location(3, 4);

        assertEquals(location1, location2);    //проверяет, что два объекта с одинаковыми координатами считаются равными
        assertNotEquals(location1, location3); //проверяет, что объекты с разными координатами считаются неравными
    }
}