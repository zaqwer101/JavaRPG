package Game.Effects;

import Game.Creature;
import Game.Interfaces.IEffect;

public abstract class Effect implements IEffect {
    int duration;
    String name;

    public Effect(int duration, String name)
    {
        this.duration = duration;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    public String getName()
    {
        return name;
    }
}
