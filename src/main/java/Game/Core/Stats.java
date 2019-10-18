package Game.Core;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Класс для хранения информации о характеристиках
 */
public class Stats {
    public static String[] allStats = {
            "agility", "strength", "intelligence", "endurance",
            "baseHp", "additionalHp", "maxHp", "hp",
            "mana", "maxMana",
            "criticalRate", "evasionRate",
            "criticalChance", "evasionChance",
            "level", "exp", "expToLevel",
            "actionPoints", "maxActionPoints",
            "weight", "maxWeight", "baseMaxWeight",
            "movePoints", "movePointsPerAP", // сколько очков движения можно получить за очко действия
    };

    // Статы, которые нельзя складывать/вычитать друг из друга при сложении/вычитании объектов
    public static String[] nonFoldingStats = {
        "expToLevel", "level", "hp", /* потому что увеличиваться может только максимальное количество хп */
            "actionPoints", "weight", "maxWeight"
    };

    private HashMap<String, Integer> stats;

    public Stats(HashMap<String, Integer> stats) {
        this.stats = stats;
        for (var key : allStats) {
            if (!this.stats.containsKey(key)) {
                this.stats.put(key, 0);

            }
        }
        recountStats();
    }

    public Stats() {
        this.stats = new HashMap<>();
        for (var key : allStats) {
            if (!this.stats.containsKey(key)) {
                this.stats.put(key, 0);

            }
        }
        recountStats();
    }

    public Stats add(Stats stat2) {
        Stats stat3 = new Stats(new HashMap<>());
        for (var key : this.stats.keySet()) {
            if (!Arrays.asList(nonFoldingStats).contains(key))
                stat3.stats.replace(key, this.stats.get(key) + stat2.stats.get(key));
            else
                stat3.stats.replace(key, this.stats.get(key));
        }
        return stat3;
    }

    public Stats sub(Stats stat2) {
        Stats stat3 = new Stats(new HashMap<>());
        for (var key : this.stats.keySet()) {
            if (!Arrays.asList(nonFoldingStats).contains(key)) {
                int newStat = this.stats.get(key) - stat2.stats.get(key);
                if (newStat < 0)
                    newStat = 0;
                stat3.stats.replace(key, newStat);
            } else
            {
                stat3.stats.replace(key, this.stats.get(key));
            }
        }
        return stat3;
    }

    public int getStat(String stat) {
        if (stats.containsKey(stat))
            return this.stats.get(stat);
        else
        {
            JavaRPG.log("Попытка обратиться к несуществующей характеристике \"" + stat + "\"");
            System.err.println("Попытка обратиться к несуществующей характеристике \"" + stat + "\"");
            return -1;
        }
    }

    public void setStat(String stat, int value)
    {
        if (Arrays.asList(allStats).contains(stat)) {
            this.stats.replace(stat, value);
            recountStats();
        }
    }

    // setStat здесь использовать нельзя
    public void recountStats() {
        //////
        // hp
        //////
        int level;
        if (stats.get("level") > 0)
        {
            level = stats.get("level");
        } else
        {
            level = 1;
        }
        int maxHp = stats.get("baseHp") * level + stats.get("strength") * 8 + stats.get("additionalHp");
        stats.replace("maxHp", maxHp);
        if (stats.get("hp") > stats.get("maxHp")) stats.replace("hp", stats.get("maxHp"));

        //////
        // mana
        //////
        int maxMana = stats.get("intelligence") * 12;
        stats.replace("maxMana", maxMana);
        if (stats.get("mana") > stats.get("maxMana")) stats.replace("mana", stats.get("maxMana"));

        //////
        // action points
        //////
        int actionPoints = stats.get("agility") / 4 + stats.get("endurance") / 2;
        if (actionPoints < 1) actionPoints = 1; // чтобы у любого существа было хотя бы одно очко действий
        stats.put("maxActionPoints", actionPoints);
        if (stats.get("actionPoints") > stats.get("maxActionPoints")) stats.replace("actionPoints", stats.get("maxActionPoints"));

        //////
        // weight
        //////
        int maxWeight = stats.get("baseMaxWeight") + 10 * stats.get("strength");
        stats.put("maxWeight", maxWeight);

        //////
        // movePoints
        //////
        stats.put("movePointsPerAP", 1);

        if (stats.get("movePoints") < 0)
        {
            stats.replace("movePoints", 0);
        }
    }
}
