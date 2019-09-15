package Game.Core;

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
        Location testSite = new Location(10, 10);
        Creature dummy = new Creature("Dummy", '$', new Position(1,1), testSite);
        Creature target = new Creature("Target", 'T', new Position(5,5), testSite);
        while(true)
        {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            drawLocation(testSite);
            if(testSite.getPosition(dummy.getPosition().getX(), dummy.getPosition().getY() + 1).isPassable())
            {
                try
                {
                    dummy.teleport(new Position(dummy.getPosition().getX(), (dummy.getPosition().getY() + 1)));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}