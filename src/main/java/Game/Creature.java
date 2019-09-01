package Game;

import Game.Interfaces.IEffect;

import java.util.ArrayList;

public class Creature
{
    ArrayList<IEffect> effects;
    Stats mainStats, additionalStats;
    Resists mainResists, additionalResists;
    Position position;

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
        mainStats.recountStats();
        additionalStats.recountStats();
        return mainStats.add(additionalStats);
    }

    public Resists getResists()
    {
        return mainResists.add(additionalResists);
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
}
