package mazegenerator;

import asciiPanel.AsciiPanel;
import com.anish.mycreatures.Sign;
import com.anish.mycreatures.Wall;
import com.anish.mycreatures.World;
import com.anish.fivescreens.FightScreen;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * @author ljh
 * @create 2021-10-19 17:59
 */
public class MazeScreen implements FightScreen.Screen
{
    private World world;
    private MazeGenerator mazeGenerator;
    private Calabash calabash = new Calabash(new Color(180, 50, 50), 0, this.world);
    private String[][] mazeLocation;
    private StringBuilder moveString = new StringBuilder();
    public static String[] dfsMoveString;
    private boolean isFlag = true;
    private boolean anotherFlag = true;

    public MazeScreen()
    {
        world = new World();
        mazeGenerator = new MazeGenerator(50);
        mazeGenerator.generateMaze();
        //接下来处理原始数据
        String rawMaze = mazeGenerator.getRawMaze();
        String[] lineMaze = rawMaze.split("\n");
        mazeLocation = new String[lineMaze.length][];
        for (int i = 0; i < lineMaze.length; i++)
        {
            mazeLocation[i] = lineMaze[i].substring(1, lineMaze[i].length() - 1).split(", ");
        }
        //妈的，搞到现在才发现，横轴是x，数轴是y
        //现在的问题是我们的数组存储的数据和放置位置不一致，应该要转置一波
        for (int i = 0; i < mazeLocation.length; i++)
        {
            for (int j = 0; j < mazeLocation[1].length; j++)
            {
                if (mazeLocation[i][j].equals("0"))
                {
                    world.put(new Wall(world), i, j);
                }
            }
        }
        String[][] inter = new String[mazeLocation.length][];
        for (int i = 0; i < mazeLocation.length; i++)
        {
            inter[i] = new String[mazeLocation[0].length];
            for (int j = 0; j < inter.length; j++)
            {
                inter[i][j] = mazeLocation[j][i];
            }
        }
        mazeLocation = inter;
        world.put(calabash, 0, 0);
        dfsFindWay();
        dfsMoveString = moveString.toString().split(", ");
    }

    @Override
    public void displayOutput(AsciiPanel terminal)
    {
        for (int x = 0; x < World.WIDTH; x++)
        {
            for (int y = 0; y < World.HEIGHT; y++)
            {
                terminal.write(world.get(x, y).getGlyph(), x, y, world.get(x, y).getColor());
            }
        }
    }

    /*37 <-
      40 ^
      39 ->
      38 xia
    */
    @Override
    public FightScreen.Screen respondToUserInput(KeyEvent key)
    {
        int x = calabash.getX();
        int y = calabash.getY();
        switch (key.getKeyCode())
        {
            case 37:
                if (x < 1) break;
                if ((world.get(x - 1, y) instanceof Wall)&&anotherFlag) break;
                world.put(new Sign(world), x, y);
                world.put(calabash, x - 1, y);
                break;
            case 38:
                if (y < 1) break;
                if ((world.get(x, y - 1) instanceof Wall)&&anotherFlag) break;
                world.put(new Sign(world), x, y);
                world.put(calabash, x, y - 1);
                break;
            case 39:
                if (x > World.HEIGHT) break;
                if ((world.get(x + 1, y) instanceof Wall)&&anotherFlag) break;
                world.put(new Sign(world), x, y);
                world.put(calabash, x + 1, y);
                break;
            case 40:
                if (y > World.WIDTH) break;
                if ((world.get(x, y + 1) instanceof Wall)&&anotherFlag) break;
                world.put(new Sign(world), x, y);
                world.put(calabash, x, y + 1);
                break;
            case 32:
                displayDfs();
                break;
            case 10:
                anotherFlag = false;
                break;
        }
        return this;
    }

    public void displayDfs()
    {
        int i = 0;
        int x;
        int y;
        int xUl = calabash.getX();
        int yUl = calabash.getY();
        world.put(calabash, 0, 0);
        while(i < dfsMoveString.length)
        {
            x = calabash.getX();
            y = calabash.getY();
            if (dfsMoveString[i].equals("Right"))
            {
                world.put(new Sign(world), x, y);
                world.put(calabash, x, y + 1);
            }
            if (dfsMoveString[i].equals("Left"))
            {
                world.put(new Sign(world), x, y);
                world.put(calabash, x , y - 1);
            }
            if (dfsMoveString[i].equals("Up"))
            {
                world.put(new Sign(world), x , y);
                world.put(calabash, x - 1, y );
            }
            if (dfsMoveString[i].equals("Down"))
            {
                world.put(new Sign(world), x, y);
                world.put(calabash, x + 1, y);
            }
            i++;
        }
        world.put(new Sign(world), mazeLocation.length-1, mazeLocation.length-1);
        world.put(calabash, xUl, yUl);
    }
    /**
     * 这里我们要写一个深度优先搜索算法找一条路
     * 问题是我们现在还不会深度优先算法
     * 0 墙
     * 1 路，
     * 那么我们要做的就是找到一条从（0， 0）通往（50， 50）的路
     * 我的想法：从（0， 0）开始，记录四个方向是否有路，然后每条路走到头，再次判断，直到到达（50， 50）
     * 那么看起来我们要用递归算法
     * 可以先定义一个Enum，四个方向，每次传入一个数组，记录可以走的方向，
     * 至于经过，可以用 方向 + 步数 表示
     */
    public void dfsFindWay()
    {
        int x = 0;
        int y = 0;
        Direction[] nextDirections = new Direction[4];
        if (!mazeLocation[x][y + 1].equals("0")) nextDirections[0] = Direction.Right;
        if (!mazeLocation[x + 1][y].equals("0")) nextDirections[1] = Direction.Down;
        recursiveFindWay(x, y, nextDirections, new StringBuilder(""));
    }

    //directions放四个，有概率为null，上下左右
    //现在有一个小bug，他会来到一个是四面环山的地方，妈的，要不我们一步一步走吧，如果哪一步到了三面都是墙，那就退回去
    public void recursiveFindWay(int x, int y, Direction[] moveDirection, StringBuilder targetWay)
    {
        int xGo;
        int yGo;
        int interX;
        int interY;
        StringBuilder sb;
        for (Direction d : moveDirection)
        {
            if (d != null && isFlag)//&& moveString == null
            {
                interX = x;
                interY = y;
                sb = new StringBuilder(targetWay.toString());
                xGo = d.getX();
                yGo = d.getY();
                if (interX + xGo < mazeLocation.length && interY + yGo < mazeLocation.length && interX + xGo >= 0 && interY + yGo >= 0 && mazeLocation[interX + xGo][interY + yGo].equals("1") && !((interX == mazeLocation.length - 1) && (interY == mazeLocation.length - 1)))
                {
                    interX += xGo;
                    interY += yGo;
                }
                sb.append(d + ", ");
                if ((interX == mazeLocation.length - 1) && (interY == mazeLocation.length - 1))
                {
                    isFlag = false;
                    moveString = sb;
                }
                Direction[] nextDirections = new Direction[4];
                if (interY < mazeLocation.length - 1 && mazeLocation[interX][interY + 1].equals("1") && !d.equals(Direction.Left))
                    nextDirections[0] = Direction.Right;
                if (interX < mazeLocation.length - 1 && mazeLocation[interX + 1][interY].equals("1") && !d.equals(Direction.Up))
                    nextDirections[1] = Direction.Down;
                if (interY > 0 && mazeLocation[interX][interY - 1].equals("1") && !d.equals(Direction.Right))
                    nextDirections[2] = Direction.Left;
                if (interX > 0 && mazeLocation[interX - 1][interY].equals("1") && !d.equals(Direction.Down))
                    nextDirections[3] = Direction.Up;
                recursiveFindWay(interX, interY, nextDirections, sb);
            }
        }
    }
}
