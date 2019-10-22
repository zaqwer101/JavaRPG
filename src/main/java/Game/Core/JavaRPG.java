package Game.Core;

import Game.Actions.AttackAction;
import Game.Attacks.Attack;
import Game.Attacks.MeleeAttack;
import Game.Effects.PeriodicStatsEffect;

public class JavaRPG
{
    public static void log(String message)
    {
        System.out.println(message);
    }

    public static void drawLocation(Location location)
    {
        for(int i = 0; i < location.getSize()[1]; i++)
        {
            for(int f = 0; f < location.getSize()[0]; f++)
            {
               System.out.print(location.getPosition(f, i).getIcon());
            }
            System.out.println();
        }
    }

    public static void main(String[] args)
    {
        System.out.println("It works!");
    }
}