package Game.Effects;

import Game.Creature;
import Game.Interfaces.IEffect;

public abstract class Effect implements IEffect {
    int duration;

    public Effect(int duration)
    {
        this.duration = duration;
    }

    @Override
    public int getDuration() {
        return duration;
    }
}
