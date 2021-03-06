package Game.Effects;

import Game.Core.Creature;

/**
 * Родитель для всех эффектов, действующих на протяжении нескольких ходов
 */
public abstract class PeriodicEffect extends Effect
{
    public static final int FOREVER = -100;
    int duration;
    boolean applied;
    public PeriodicEffect(int duration, String name) {
        super(name);
        this.applied = false;
        this.duration = duration;
    }

    public int getDuration()
    {
        return duration;
    }

    public boolean isApplied()
    {
        return applied;
    }

    public abstract void apply(Creature target);
    public abstract void remove(Creature target);
}
