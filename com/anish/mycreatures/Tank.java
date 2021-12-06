package com.anish.mycreatures;

import com.anish.mycreatures.Creature;
import com.anish.mycreatures.World;

import java.awt.*;

/**
 * 接下来要做的是给坦克增加方向属性，然后坦克会根据方向，突出子弹
 * @author ljh
 * @create 2021-11-21 19:24
 */
public class Tank extends Creature
{
    public Tank(Color color, World world)
    {
        super(color, (char) 5, world);
    }
}
