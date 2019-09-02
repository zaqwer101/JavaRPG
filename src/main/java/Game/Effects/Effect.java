package Game.Effects;

public abstract class Effect implements IEffect {
    String name;

    public Effect(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}
