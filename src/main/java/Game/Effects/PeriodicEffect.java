package Game.Effects;

import Game.Creature;

public abstract class PeriodicEffect extends Effect
{
    public static final int FOREVER = -100;
    int duration;
    public PeriodicEffect(int duration, String name) {
        super(name);
        this.duration = duration;
    }

    public int getDuration()
    {
        return duration;
    }

    public abstract void apply(Creature target);
    public abstract void remove(Creature target);
}
