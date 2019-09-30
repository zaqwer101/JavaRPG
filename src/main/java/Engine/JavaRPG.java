package Engine;

import Game.Actions.AttackAction;
import Game.Attacks.Attack;
import Game.Attacks.MeleeAttack;
import Game.Core.Creature;
import Game.Core.Location;
import Game.Core.Position;
import Game.Core.Stats;
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

    public static void combinedTest()
    {
        int turn = 0;

        Creature dummy;
        Creature target;
        Location location;

        Stats baseStats = new Stats();
        baseStats.setStat("baseHp", 100);

        location = new Location(10, 10);
        dummy = new Creature("Dummy", '$', new Position(0, 0), baseStats, location);
        target = new Creature("Target", 'T', new Position(1, 1), baseStats, location);

        Stats buffStats = new Stats();
        buffStats.setStat("agility", 1);
        buffStats.setStat("endurance", 1);
        buffStats.setStat("baseHp", 1000);

        Attack trainingAttack = new MeleeAttack(5);

        dummy.addEffect(new PeriodicStatsEffect(10, "Тренировочный бонус", buffStats));
        target.addEffect(new PeriodicStatsEffect(10, "Тренировочный бонус", buffStats));

        dummy.addAttack(trainingAttack);

        while (dummy.getHp()[0] > 0 && target.getHp()[0] > 0)
        {
            if (turn == 1)
            {
                dummy.heal(1000);
                target.heal(1000);
            }

            // dummy's turn
            while (dummy.getAP()[0] > 0)
            {
                dummy.addAction(new AttackAction(dummy, target, 1, trainingAttack));
            }
            dummy.endTurn();

            // target's turn
//            while (target.getAP()[0] > 0)
//            {
//                target.addAction(new AttackAction(target, dummy, 1, trainingAttack));
//            }
            target.endTurn();

            System.out.println();
            JavaRPG.log("------------------ Конец хода " + turn + " --------------------");
            System.out.println();
            turn++;
        }
    }

    public static void main(String[] args)
    {

        combinedTest();
//        Location testSite = new Location(10, 10);
//        Creature dummy = new Creature("Dummy", '$', new Position(1,1), testSite);
//        Creature target = new Creature("Target", 'T', new Position(5,5), testSite);
//                while(true)
//        {
//            System.out.print("\033[H\033[2J");
//            System.out.flush();
//            drawLocation(testSite);
//            if(testSite.getPosition(dummy.getPosition().getX(), dummy.getPosition().getY() + 1).isPassable())
//            {
//                try
//                {
//                    dummy.teleport(new Position(dummy.getPosition().getX(), (dummy.getPosition().getY() + 1)));
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//            }
//
//            try
//            {
//                Thread.sleep(1000);
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//        }
    }
}