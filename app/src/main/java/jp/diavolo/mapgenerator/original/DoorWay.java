package jp.diavolo.mapgenerator.original;

import jp.diavolo.core.Coord;

class DoorWay{
    public final static int DOWN = 0;
    public final static int LEFT = 1;
    public final static int RIGHT = 2;
    public final static int UP = 3;
    public Coord pos = new Coord();
    public boolean used = false;
    public boolean invalid = false;

    public void set(int x, int y){
        pos.set(x,y);
    }

    public void used(){
        used = true;
    }

    public void invalid() {
        invalid = true;
    }

    public void reset() {
        used = false;
        invalid = false;
    }
}