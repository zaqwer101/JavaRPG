package Game.Effects;


import Game.Core.Creature;

public abstract class Effect {
    public enum EffectType {NONE, STATS, DAMAGE_DEBUFF}
    EffectType effectType;
    String name;

    public Effect(String name)
    {
        this.name = name;
        effectType = EffectType.NONE;
    }

    public String getName()
    {
        return name;
    }

    public EffectType getEffectType()
    {
        return effectType;
    }

    public abstract void apply(Creature target);
}
