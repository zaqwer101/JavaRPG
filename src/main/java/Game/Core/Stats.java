package Game.Core;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Класс для хранения информации о характеристиках
 */
public class Stats {
    public static String[] allStats = {
            "agility", "strength", "intelligence",
            "baseHp", "additionalHp", "maxHp", "hp",
            "mana", "maxMana",
            "criticalRate", "evasionRate",
            "criticalChance", "evasionChance",
            "level", "exp", "expToLevel"
    };

    // Статы, которые нельзя складывать/вычитать друг из друга при сложении/вычитании объектов
    public static String[] nonFoldingStats = {
        "expToLevel", "level", "hp" /* потому что увеличиваться может только максимальное количество хп */
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
            }
        }
        return stat3;
    }

    public int getStat(String stat) {
        if (stats.keySet().contains(stat))
            return this.stats.get(stat);
        else
            return -1;
    }

    public int setStat(String stat, int value) {
        if (Arrays.asList(allStats).contains(stat)) {
            this.stats.replace(stat, value);
            recountStats();
            return 0;
        }
        return 1;
    }

    // setStat здесь использовать нельзя
    public void recountStats() {
        //////
        // hp
        //////
        int maxHp = stats.get("baseHp") * stats.get("level") + stats.get("strength") * 8 + stats.get("additionalHp");
        stats.replace("maxHp", maxHp);
        if (stats.get("hp") > stats.get("maxHp")) stats.replace("hp", stats.get("maxHp"));

        //////
        // mana
        //////
        int maxMana = stats.get("intelligence") * 12;
        stats.replace("maxMana", maxMana);
        if (stats.get("mana") > stats.get("maxMana")) stats.replace("mana", stats.get("maxMana"));

        // TODO: Дописать расчёт остальных параметров
    }
}
