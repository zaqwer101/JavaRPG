import Game.Stats;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatsTest
{
    Stats stats1, stats2;

    HashMap<String, Integer> params1 = new HashMap<>();
    HashMap<String, Integer> params2 = new HashMap<>();

    @Before
    public void init()
    {
        // заполняем таблицы случайными корректными значениями
        System.out.println("Putting stats...\n------------------------------------");
        for (var key : Stats.allStats)
        {
            params1.put(key, new Random().nextInt(100));
            System.out.println(key + "1: " + params1.get(key));
            params2.put(key, new Random().nextInt(100));
            System.out.println(key + "2: " + params2.get(key));
        }
        stats1 = new Stats(params1);
        stats2 = new Stats(params2);

        System.out.println("Before task completed");
    }

    @Test
    public void constructorTest()
    {
        for (var key : Stats.allStats)
        {
            assertEquals(params1.get(key), stats1.getStat(key));
            assertEquals(params2.get(key), stats2.getStat(key));
        }
    }
}