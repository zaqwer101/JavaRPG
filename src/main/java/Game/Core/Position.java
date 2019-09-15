package Game.Core;

/**
 * Позиция объекта в игровой локации
 */
public class Position
{
    private int x, y;

    public Position(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public void add(Position position)
    {
        this.x += position.x;
        this.y += position.y;
    }

    public void sub(Position position)
    {
        this.x -= position.x;
        this.y -= position.y;
    }

    public int getDistance(Position position)
    {
        int x2 = (int) Math.pow(this.x - position.x, 2);
        int y2 = (int) Math.pow(this.y - position.y, 2);
        int distance = (int) Math.sqrt(x2 + y2);

        return distance;
    }

    /**
     * Функция поиска пути до точки
     * @param destination точка назначения
     * @return набор точек, которые необходимо пройти, чтобы достигнуть назначения
     */
    public Position[] findPath(Position destination)
    {
        // TODO
        return null;
    }
}