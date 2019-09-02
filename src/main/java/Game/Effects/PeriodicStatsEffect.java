package Game.Effects;

import Game.Creature;
import Game.Stats;

public class PeriodicStatsEffect extends PeriodicEffect
{
    Stats additionalStats;
    public PeriodicStatsEffect(int duration, String name, Stats additionalStats)
    {
        super(duration, name);
        this.additionalStats = additionalStats;
    }
    @Override
    public void apply(Creature target)
    {
        target.addStats(additionalStats);
    }
    @Override
    public void remove(Creature target)
    {
        target.subStats(additionalStats);
    }
}
