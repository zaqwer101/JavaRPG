package Game.Effects;

import Game.Creature;
import Game.Interfaces.IEffect;
import Game.Resists;

import static Game.JavaRPG.log;

public class Bleeding extends PeriodicEffect
{
    int amount;
    public Bleeding(int duration, int amount) {
        super(duration, "Кровотечение");
        this.amount = amount;
    }

    @Override
    public void apply(Creature target) {
        target.takeDamage(amount, Resists.DamageType.PURE);
        duration--;
    }

    @Override
    public void remove(Creature target) {
        log("Кровотечение у " + target.getName() + " прекратилось");
    }


}
