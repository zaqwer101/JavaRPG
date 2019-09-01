package Game.Interfaces;

import Game.Creature;

public interface IEffect {
    void apply(Creature target);
    int getDuration();
    void remove(Creature target);
}
