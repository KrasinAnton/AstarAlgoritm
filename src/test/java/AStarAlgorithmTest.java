import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import ru.krasin.AStarAlgorithm;

import java.util.List;

public class AStarAlgorithmTest {
    @Test
    public void testAStarPathFound() {
        int[][] labyrinth = {
                {1, 0, 1, 1, 1},
                {0, 1, 1, 0, 1},
                {0, 0, 0, 1, 0},
                {1, 1, 0, 0, 1},
                {1, 0, 1, 0, 0}
        };

        AStarAlgorithm.Location start = new AStarAlgorithm.Location(0, 0);
        AStarAlgorithm.Location end = new AStarAlgorithm.Location(4, 4);

        List<AStarAlgorithm.Location> path = new AStarAlgorithm().aStar(labyrinth, start, end);

        assertFalse(path.isEmpty());
        assertEquals(end, path.get(path.size() - 1));
    }

    @Test
    public void testAStarNoPathFound() {
        int[][] labyrinth = {
                {1, 0, 1, 1, 1},
                {0, 1, 1, 0, 1},
                {0, 0, 0, 1, 0},
                {1, 1, 0, 1, 1},
                {1, 0, 1, 0, 0}
        };

        AStarAlgorithm.Location start = new AStarAlgorithm.Location(0, 0);
        AStarAlgorithm.Location end = new AStarAlgorithm.Location(4, 4);

        List<AStarAlgorithm.Location> path = new AStarAlgorithm().aStar(labyrinth, start, end);

        assertTrue(path.isEmpty());
    }
}


