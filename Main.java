

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;
import com.anish.mycreatures.World;
import com.anish.screen.FightScreen;
import com.anish.screen.Screen;
import com.anish.screen.TestScreen;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
//我们现在要想办法实现多线程，首先屏幕本身应该是一个线程，每隔一段时间刷新自己
public class Main extends JFrame implements KeyListener,Runnable
{

    private AsciiPanel terminal;
    private Screen screen;

    public Main()
    {
        super();
        terminal = new AsciiPanel(World.WIDTH, World.HEIGHT, AsciiFont.TALRYTH_15_15);
        add(terminal);
        pack();
        screen = new FightScreen();
        new Thread((FightScreen)screen).start();
        addKeyListener(this);
        repaint();
    }

    @Override
    public void repaint()
    {
        terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e)
    {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        screen = screen.respondToUserInput(e);
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e)
    {

    }

    public static void main(String[] args)
    {
        Main app = new Main();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
        new Thread(app).start();
    }


    @Override
    public void run()
    {
        while(true)
        {
            repaint();
            try
            {
                Thread.sleep(500);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
