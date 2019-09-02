package Game.Effects;

import Game.Creature;
import Game.Resists;

import static Game.JavaRPG.log;

public class Bleeding extends PeriodicEffect
{
    int amount;
    public Bleeding(int duration, int amount) {
        super(duration, "Кровотечение");
        this.amount = amount;
        this.effectType = EffectType.DAMAGE_DEBUFF;
    }

    @Override
    public void apply(Creature target) {
        target.takeDamage(amount, Resists.DamageType.PURE);
        applied = true;
        duration--;
    }

    @Override
    public void remove(Creature target) {
        log("Кровотечение у " + target.getName() + " прекратилось");
        applied = false;
    }


}
