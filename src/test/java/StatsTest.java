import Game.Stats;
import org.junit.After;
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

    @After
    public void over()
    {
        System.out.println("------------------------------------");
    }

    @Before
    public void init()
    {
        // заполняем таблицы случайными корректными значениями
        System.out.println("Putting stats...");
        for (var key : Stats.allStats)
        {
            params1.put(key, new Random().nextInt(100));
            System.out.println(key + "1: " + params1.get(key));
            params2.put(key, new Random().nextInt(100));
            System.out.println(key + "2: " + params2.get(key));
        }
        stats1 = new Stats(params1);
        stats2 = new Stats(params2);
    }

    @Test
    public void constructorStatsTest()
    {
        System.out.println("Testing constructor and getter...");
        for (var key : Stats.allStats)
        {
            assertEquals(params1.get(key), stats1.getStat(key));
            System.out.println(params1.get(key) + " == " + stats1.getStat(key));
            assertEquals(params2.get(key), stats2.getStat(key));
            System.out.println(params2.get(key) + " == " + stats2.getStat(key));
        }
    }

    @Test
    public void addStatsTest()
    {
        System.out.println("Testing addition...");
        Stats stats3 = stats1.add(stats2);

        for (var key : Stats.allStats)
        {
            assertEquals(
                    stats3.getStat(key),
                    stats1.getStat(key) + stats2.getStat(key)
            );
            System.out.println(key + ": " + stats3.getStat(key) + " == " + (stats1.getStat(key) + stats2.getStat(key)));
        }
    }

    @Test
    public void subStatsTest()
    {
        System.out.println("Testing subtraction...");
        Stats stats3 = stats1.sub(stats2);

        for (var key : Stats.allStats)
        {
            int subStat = stats1.getStat(key) - stats2.getStat(key);
            if (subStat < 0)
                subStat = 0;
            assertEquals(subStat, stats3.getStat(key));
            System.out.println(key + ": " + stats3.getStat(key) + " == " + subStat);
        }
    }

    @Test
    public void recountStatsTest()
    {
        stats1.setStat("armorRate", 10);
        stats1.setStat("strength", 10);
        stats1.setStat("intelligence", 10);
        stats1.setStat("level", 5);
        stats1.setStat("baseHp", 50);
        stats1.setStat("hp", 1000);
        stats1.setStat("mana", 1000);

        assertEquals(38, stats1.getStat("armorBlock"));
        assertEquals(330, stats1.getStat("maxHp"));
        assertEquals(stats1.getStat("maxHp"), stats1.getStat("hp"));
        assertEquals(120, stats1.getStat("maxMana"));
        assertEquals(stats1.getStat("maxMana"), stats1.getStat("mana"));

    }
}