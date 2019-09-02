package Game.Effects;

import Game.Creature;
import Game.JavaRPG;
import Game.Resists.DamageType;

public class InstantDamage extends Effect
{
    int amount;
    DamageType type;
    public InstantDamage(String name, DamageType type, int amount)
    {
        super(name);
        this.type = type;
        this.amount = amount;
    }
    @Override
    public void apply(Creature target)
    {
        target.takeDamage(amount, type);
        JavaRPG.log(target.getName() + " получил " + amount + " единиц урона от \"" + name + "\"");
    }
}
