package Game.Core;

import java.util.HashMap;

/**
 * Сопротивления типам урона объекта
 */
public class Resists {
    public enum DamageType { PHYSICAL, FIRE, PURE }

    private HashMap<DamageType, Integer> resists;

    public Resists(HashMap resists)
    {
        this.resists = resists;
        for (var type : DamageType.values())
        {
            if (!this.resists.keySet().contains(type))
            {
                this.resists.put(type, 0);
            }
        }
        resists.replace(DamageType.PURE, 0);
    }

    private Resists()
    {
        this.resists = new HashMap<>();
        for (var type : DamageType.values())
        {
            this.resists.put(type, 0);
        }
    }

    public int getResist(DamageType resist)
    {
        return resists.get(resist);
    }

    public Resists add(Resists resist2)
    {
        Resists resists3 = new Resists();
        for (var type : DamageType.values())
        {
            resists3.resists.replace(type, this.resists.get(type) + resist2.resists.get(type));
        }

        return resists3;
    }

    public Resists sub(Resists resist2)
    {
        Resists resists3 = new Resists();
        for (var type : DamageType.values())
        {
            resists3.resists.replace(type, this.resists.get(type) - resist2.resists.get(type));
        }

        return resists3;
    }
}
