package Game;

import java.util.HashMap;

public class Stats
{
    public static String[] allStats = {
            "agility", "strength", "intelligence",
            "hp", "maxHp",
            "armor"
    };
    private HashMap<String, Integer> stats;

    public Stats(HashMap<String, Integer> stats)
    {
        this.stats = stats;
        for (var stat : allStats)
        {
            if (!this.stats.containsKey(stat))
            {
                this.stats.put(stat, 0);
            }
        }
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
}
