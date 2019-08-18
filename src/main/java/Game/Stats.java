package Game;

import java.util.Arrays;
import java.util.HashMap;

public class Stats
{
    public static String[] allStats = {
            "agility", "strength", "intelligence",
            "baseHP", "maxHp", "hp",
            "armorRate",
            "criticalRate", "evasionRate", "armorRate",
            "criticalChance", "evasionChance", "armorBlock",
            "level", "exp", "exp_to_level"
    };

    private HashMap<String, Integer> stats;

    public Stats(HashMap<String, Integer> stats)
    {
        this.stats = stats;
        for (var key : allStats)
        {
            if (!this.stats.containsKey(key))
            {
                int defaultStat = 0;
                switch (key)
                {
                    case "level":
                    {
                        defaultStat = 1;
                        break;
                    }
                }
                this.stats.put(key, defaultStat);

            }
        }
        recountStats();
    }

    public Stats add(Stats stat2)
    {
        Stats stat3 = new Stats(new HashMap<>());
        for (var key : this.stats.keySet())
        {
            stat3.stats.replace(key, this.stats.get(key) + stat2.stats.get(key));
        }
        return stat3;
    }

    public Stats sub(Stats stat2)
    {
        Stats stat3 = new Stats(new HashMap<>());
        for (var key : this.stats.keySet())
        {
            int newStat = this.stats.get(key) - stat2.stats.get(key);
            if (newStat < 0)
                newStat = 0;

            stat3.stats.replace(key, newStat);
        }
        return stat3;
    }

    public int getStat(String stat)
    {
        if(stats.keySet().contains(stat))
            return this.stats.get(stat);
        else
            return -1;
    }

    public int setStat(String stat, int value)
    {
        if(Arrays.asList(allStats).contains(stat))
        {
            this.stats.replace(stat, value);
            return 0;
        }
        return 1;
    }

    public void recountStats()
    {
        int maxHp = getStat("baseHp") * getStat("level") + getStat("strength") * 8;
        if (getStat("hp") > getStat("maxHp")) setStat("hp", getStat("maxHp"));
    }
}
