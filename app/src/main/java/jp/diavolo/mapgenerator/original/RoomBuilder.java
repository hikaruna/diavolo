package jp.diavolo.mapgenerator.original;

import java.util.Random;

class RoomBuilder{
    public int width = 12;
    public int height = 10;
    public void build(Random rnd, int[][] floor, byte[][] roomNumber, DoorWay[] doorways, byte roomno, int x, int y) {
        for(int i=0; i<4; ++i) doorways[i].invalid();
    }
}
