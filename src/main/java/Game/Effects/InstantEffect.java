package Game.Effects;

import Game.Creature;
import Game.JavaRPG;
import Game.Resists.DamageType;

public abstract class InstantEffect extends Effect
{
    int amount;
    DamageType type;
    public InstantEffect(String name, DamageType type, int amount)
    {
        super(name);
        this.type = type;
        this.amount = amount;
    }
}
