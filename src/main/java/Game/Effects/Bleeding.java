package Game.Effects;

import Game.Creature;
import Game.Interfaces.IEffect;
import Game.Resists;

public class Bleeding extends Effect
{
    int amount;
    public Bleeding(int duration, int amount) {
        super(duration);
        this.amount = amount;
    }

    @Override
    public void apply(Creature target) {
        target.takeDamage(amount, Resists.DamageType.PURE);
        duration--;
    }
}
