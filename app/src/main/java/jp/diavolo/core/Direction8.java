package jp.diavolo.core;

import java.util.Random;

public enum Direction8{
    NONE(0), UP(5), DOWN(1), LEFT(3), RIGHT(7), UPLEFT(4), UPRIGHT(6), DOWNLEFT(2), DOWNRIGHT(8);

    private int _value;
    private Direction8(int i){
        _value = i;
    }
    public int value(){
        return _value;
    }

    public static Direction8 from(int i){
        switch(i){
        default: return NONE;
        case 1: return DOWN;
        case 2: return DOWNLEFT;
        case 3: return LEFT;
        case 4: return UPLEFT;
        case 5: return UP;
        case 6: return UPRIGHT;
        case 7: return RIGHT;
        case 8: return DOWNRIGHT;
        }
    }

    public Direction8 cw90(){
        switch(this){
        case UP: return LEFT;
        case RIGHT: return DOWN;
        case DOWN: return LEFT;
        case LEFT: return UP;
        case UPLEFT: return UPRIGHT;
        case UPRIGHT: return DOWNRIGHT;
        case DOWNRIGHT: return DOWNLEFT;
        case DOWNLEFT: return UPLEFT;
        default: return NONE;
        }
    }

    public Direction8 cw45() {
        switch(this){
        case UP: return UPRIGHT;
        case RIGHT: return DOWNRIGHT;
        case DOWN: return DOWNLEFT;
        case LEFT: return UPLEFT;
        case UPLEFT: return UP;
        case UPRIGHT: return RIGHT;
        case DOWNRIGHT: return DOWN;
        case DOWNLEFT: return LEFT;
        default: return NONE;
        }
    }

    public int horizontal(){
        switch(this){
        case RIGHT: case UPRIGHT: case DOWNRIGHT:
            return 1;
        case LEFT: case UPLEFT: case DOWNLEFT:
            return -1;
        case UP: case DOWN: default:
            return 0;
        }
    }
    public int vertical(){
        switch(this){
        case DOWN: case DOWNRIGHT: case DOWNLEFT:
            return 1;
        case UP: case UPRIGHT: case UPLEFT:
            return -1;
        case LEFT: case RIGHT: default:
            return 0;
        }
    }

    public Coord getCoord(){
        return new Coord(horizontal(), vertical());
    }

    public static Direction8 estimate(int dx, int dy, int deadzone) {
        if(Math.abs(dx) <= deadzone && Math.abs(dy) <= deadzone){
            return NONE;
        }

        //左右方向
        if(Math.abs(dx) > 2.41421356 * Math.abs(dy)){
            if(dx>0) return RIGHT;
            else return LEFT;
        }else if(Math.abs(dy) > 2.41421356 * Math.abs(dx)){
            //上下方向
            if(dy>0) return DOWN;
            return UP;
        }else{
            //斜め
            if(dx<0){
                if(dy<0) return UPLEFT;
                return DOWNLEFT;
            }else{
                if(dy<0) return UPRIGHT;
                return DOWNRIGHT;
            }
        }
    }

    public static Direction8 random(Random rnd){
        return from(1+rnd.nextInt(8));
    }
}