import Game.Core.Position;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PositionTest {
    @Test
    public void distanceTest()
    {
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(5, 5);
        assertEquals(7, pos1.getDistance(pos2));

        pos2 = new Position(12, 228);
        assertEquals(228, pos1.getDistance(pos2));
    }
}
