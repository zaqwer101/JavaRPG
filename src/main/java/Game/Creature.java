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

    public Creature(String name, char icon) {
        super(name, icon);
        resists = new Resists(new HashMap());
        stats = new Stats();
        stats.setStat("level", 1);
        stats.setStat("expToLevle", 100);
        stats.setStat("strength", 10);
        stats.setStat("agility", 10);
        stats.setStat("intelligence", 10);
        stats.setStat("baseHp", 20);
        stats.setStat("hp", stats.getStat("maxHp"));
        stats.recountStats();
    }

    public Position getPosition()
    {
        return position;
    }

    public void recountEffects()
    {
        for (var effect : effects)
        {
            effect.apply(this);

            // если длительность = -100, эффект без длительности
            if(effect.getDuration() <= 0 && effect.getDuration() != -100)
            {
                effects.remove(effect);
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
