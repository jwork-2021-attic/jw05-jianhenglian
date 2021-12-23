package com.anish.mycreatures;

public class World {

    public static final int WIDTH = 70;
    public static final int HEIGHT = 50;

    private Tile<Thing>[][] tiles;

    public World() {

        if (tiles == null) {
            tiles = new Tile[WIDTH][HEIGHT];
        }

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                tiles[i][j] = new Tile<>(i, j);
                tiles[i][j].setThing(new Floor(this));
            }
        }
    }

    public Thing get(int x, int y) {
        return this.tiles[x][y].getThing();
    }

    public synchronized void put(Thing t, int x, int y) {
        this.tiles[x][y].setThing(t);
    }

}
