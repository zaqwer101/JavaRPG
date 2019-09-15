package Game.Effects;

import Game.Core.Creature;
import Game.Core.Stats;

/**
 * Бафф характеристик на некоторое количество ходов
 */
public class PeriodicStatsEffect extends PeriodicEffect
{
    Stats additionalStats;
    public PeriodicStatsEffect(int duration, String name, Stats additionalStats)
    {
        super(duration, name);
        this.additionalStats = additionalStats;
        this.effectType = EffectType.STATS;
    }

    @Override
    public void apply(Creature target)
    {
        if (!applied)
        {
            target.addStats(additionalStats);
            applied = true;
        }
        duration--;
    }
    @Override
    public void remove(Creature target)
    {
        target.subStats(additionalStats);
        applied = false;
    }
}
