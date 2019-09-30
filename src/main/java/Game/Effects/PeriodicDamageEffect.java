package Game.Effects;

import Game.Core.Creature;
import Engine.JavaRPG;
import Game.Core.Resists;

import static Engine.JavaRPG.log;

/**
 * Периодический эффект получения урона
 */
public class PeriodicDamageEffect extends PeriodicEffect
{
    int amount;
    public PeriodicDamageEffect(int duration, int amount) {
        super(duration, "Кровотечение");
        this.amount = amount;
        this.effectType = EffectType.DAMAGE_DEBUFF;
    }

    @Override
    public void apply(Creature target) {
        target.takeDamage(amount, Resists.DamageType.PURE);
        JavaRPG.log(target.getName() + ": получил " + amount + " единиц урона от \"" + name + "\", осталось " + target.getHp()[0] + " хп");
        applied = true;
        duration--;
    }

    @Override
    public void remove(Creature target) {
        log(target.getName() + ": эффект \"" + getName() + "\" прекратился");
        applied = false;
    }
}
