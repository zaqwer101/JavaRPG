package Game.Effects;

/**
 * Родитель для всех инстант-эффектов
 */
public abstract class InstantEffect extends Effect
{
    int amount;
    public InstantEffect(String name, int amount)
    {
        super(name);
        this.amount = amount;
    }
}
