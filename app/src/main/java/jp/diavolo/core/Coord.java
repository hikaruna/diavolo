package jp.diavolo.core;

public class Coord {
    public static final Coord ZERO = new Coord(0,0);

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Coord() {
        this.set(ZERO);
    }
    public Coord(Coord other) {
        this.x = other.x;
        this.y = other.y;
    }
    public Coord(Direction8 dir) {
        this.set(dir);
    }
    public int x, y;

    public Coord clone(){
        return new Coord(x, y);
    }

    public Coord set(int i, int j) {
        x = i;
        y = j;
        return this;
    }

    public Coord set(Coord other){
        x = other.x;
        y = other.y;
        return this;
    }

    public Coord set(Direction8 dir){
        x = dir.horizontal();
        y = dir.vertical();
        return this;
    }

    public Coord add_assign(Coord other){
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    public Coord add_assign(Direction8 dir){
        this.x += dir.horizontal();
        this.y += dir.vertical();
        return this;
    }

    public Coord sub_assign(Coord other){
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    public Coord mul_assign(int a){
        this.x *= a;
        this.y *= a;
        return this;
    }
    public Coord div_assign(int a) {
        this.x /= a;
        this.y /= a;
        return this;
    }
}
