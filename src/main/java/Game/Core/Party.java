package Game.Core;

import java.util.ArrayList;

/**
 * Отряд существ, передвигающийся по глобальной карте
 */
public class Party {
    private ArrayList<Creature> party;

    public Creature[] getParty()
    {
        return (Creature[]) party.toArray();
    }

    /**
     * Напрямую лучше не использовать, потому что Creature.party будет null в итоге
     */
    public boolean __addToParty(Creature creature)
    {
        if (creature.getParty() != null)
            return false;
        party.add(creature);
        return true;
    }

    /**
     * Напрямую лучше не использовать
     */
    public void __deleteFromParty(Creature creature)
    {
        if (party.contains(creature))
        {
            party.remove(creature);
        }
    }
}
