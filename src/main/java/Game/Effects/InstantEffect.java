package Game.Effects;

public abstract class InstantEffect extends Effect
{
    int amount;
    public InstantEffect(String name, int amount)
    {
        super(name);
        this.amount = amount;
    }
}
