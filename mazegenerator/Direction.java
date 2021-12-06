package mazegenerator;

/**
 * @author ljh
 * @create 2021-10-20 16:36
 */
public enum Direction
{
    Right(0, 1),
    Left(0, -1),
    Down(1, 0),
    Up(-1, 0);
    private int x;
    private int y;
    private Direction(int x, int y)
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
}
