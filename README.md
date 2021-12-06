[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-f059dc9a6f8d3a56e377f745f24479a46679e63a5d9fe6f495e02850cd0d8118.svg)](https://classroom.github.com/online_ide?assignment_repo_id=6258666&assignment_repo_type=AssignmentRepo)

# jw05

请将jw04的迷宫任务改造为一个ruguelike的葫芦娃与妖精两方对战游戏，游戏如下图所示。

![](image-11.jpeg)

需求如下：

- 每个生物体都是一个线程
- 每个生物体的移动、攻击等行为决策可使用Minimax或其他算法（可参考https://www.baeldung.com/java-minimax-algorithm）
- 请特别注意线程race condition（两个生物体不能占据同一个tile，对同一生物体的两个攻击行为应该先后发生作用，等）
- 请发挥想象力
- 完成后录屏发qq群或小破站

# 我的思考

## 1.我们要做的事

创建多线程，实现妖怪葫芦娃自主移动；实现葫芦娃妖怪对战；实现伤害，血量，目标等各种逻辑；搭建合理地图；创造较合理的剧情逻辑；

## 2.时间

大概还有三周
咳咳，狗屎了。
现在我们从头开始，我们首先改一下我们的文件夹，太混乱了，好多无关的类
## 3.预估进程

首先搭建基本地图，然后实现多线程，然后实现自主移动，再创建游戏逻辑，再自主对战



不过现在我们应该先想好自己想做什么样的游戏，不然连下笔都没法下笔。

可以写个坦克动荡，不过我们可以改菜一下，不然整不明白

那逻辑是，随意生成迷宫，然后随机生成几个坦克，坦克会随机找人打，可以发子弹，子弹撞墙返回。

有几个特殊功能，包括，子弹和坦克穿墙，发射爆破弹，埋地雷等等。

暂时先这样

那我们先实现一个坦克

