import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests
{
    int x;

    @Before
    public void init()
    {
        x = 10;
        System.out.println("Before task...");
    }

    @Test
    public void sampleTest1()
    {
        System.out.println("Sample test 1...");
        System.out.println(x);
        assertEquals(1, 1);
    }

    @Test
    public void sampleTest2()
    {
        System.out.println("Sample test 2...");
        assertEquals(1, 2);
    }
}