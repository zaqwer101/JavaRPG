package Game.Core;

import java.util.ArrayList;

public class GlobalMap {
    private ArrayList<GlobalMapPosition> globalMap;
    private int sizeX, sizeY;

    public GlobalMap(int sizeX, int sizeY)
    {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        globalMap = new ArrayList<>();

        for (int i = 0; i < sizeY; i++)
        {
            for (int f = 0; f < sizeX; f++)
            {
                globalMap.add(new GlobalMapPosition(f, i, ' '));
            }
        }
    }
}
