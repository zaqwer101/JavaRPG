package Game;

import Game.Interfaces.IEffect;
import Game.Resists.DamageType;
import java.util.ArrayList;
import java.util.HashMap;

public class Creature extends WorldObject
{
    ArrayList<IEffect> effects;
    Stats stats;
    Resists resists;
    Position position;

    /* Дефолтное существо имеет всех статов по 1
    = 8 hp
     */
    public Creature(String name, char icon) {
        super(name, icon);
        resists = new Resists(new HashMap());
        stats = new Stats();
        effects = new ArrayList<>();
        stats.setStat("level", 1);
        stats.setStat("expToLevel", 100);
        stats.setStat("strength", 1);
        stats.setStat("agility", 1);
        stats.setStat("intelligence", 1);
        stats.setStat("baseHp", 0);
        stats.setStat("hp", stats.getStat("maxHp"));
        stats.recountStats();
    }

    public Position getPosition()
    {
        return position;
    }

    public void recountEffects()
    {
        for (int i = 0; i < effects.size(); i++)
        {
            effects.get(i).apply(this);

            // если длительность = -100, эффект без длительности
            if(effects.get(i).getDuration() <= 0 && effects.get(i).getDuration() != -100)
            {
                effects.get(i).remove(this);
                effects.remove(effects.get(i));
            }
        }
    }

    public Stats getStats()
    {
        stats.recountStats();
        return stats;
    }

    public Resists getResists()
    {
        return resists;
    }

    /**
     * Получить очки здоровья существа
     * @return массив, где 0 - текущее количество очков, 1 - максимальное
     */
    public int[] getHp()
    {
        return new int[] {
                getStats().getStat("hp"),
                getStats().getStat("maxhp")
        };
    }

    public void addEffect(IEffect effect)
    {
        effects.add(effect);
    }

    public void takeDamage(int amount, DamageType type)
    {
        getStats().setStat("hp",
                getStats().getStat("hp") - (amount - amount * (resists.getResist(type) / 100))
                );
    }

    //TODO получение уровня, получение экспы, различные атаки
}
