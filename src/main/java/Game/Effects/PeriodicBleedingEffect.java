package Game.Effects;

import Game.Core.Creature;
import Game.Core.Resists;

import static Game.Core.JavaRPG.log;

/**
 * Периодический эффект получения урона
 */
public class PeriodicBleedingEffect extends PeriodicEffect
{
    int amount;
    public PeriodicBleedingEffect(int duration, int amount) {
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