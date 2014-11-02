package jp.diavolo.mapgenerator.original;

import java.util.Random;

import jp.diavolo.FloorMap.FloorType;
import android.util.Log;

class RandomRoomBuilder extends RoomBuilder{
    protected String map;
    public int[] doorwayCount = new int[4];

    public RandomRoomBuilder(Random rnd) {
        width = 5 + rnd.nextInt(4);
        height= 5 + rnd.nextInt(4);
        doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = width -2;
        doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = height -2;

        map = "";
        for(int j=0; j<height; ++j){
            String line = "";
            for(int i=0; i<width; ++i){
                if( (i==0 || i==width-1) && (j==0 || j==height-1)){
                    line += '#'; continue;
                }
                if(i==0){
                    line += '4'; continue;
                }
                if(i==width-1){
                    line += '6'; continue;
                }
                if(j==0){
                    line += '8'; continue;
                }
                if(j==height-1){
                    line += '2'; continue;
                }
                line += '.';
            }
            Log.v("RandomRoomBuilder",line);
            map += line;
        }

    }

    @Override
    public void build(Random rnd, int[][] floor, byte[][] roomNumber, DoorWay[] doorways, byte roomno, int x, int y) {
        int index = 0;

        final int u_door = rnd.nextInt(doorwayCount[DoorWay.UP])+1;
        final int d_door = rnd.nextInt(doorwayCount[DoorWay.DOWN])+1;
        final int l_door = rnd.nextInt(doorwayCount[DoorWay.LEFT])+1;
        final int r_door = rnd.nextInt(doorwayCount[DoorWay.RIGHT])+1;
        int u_count = 0, d_count = 0, l_count = 0, r_count = 0;
        for(int j=y; j<y+height; ++j){
            for(int i=x; i<x+width; ++i){
                if((j==y || j == y+height-1) && (i==x || i ==x+width-1)){
                    index++;
                    continue;
                }
                switch(map.charAt(index)){
                case '2' :
                    d_count++;
                    if(d_count == d_door){
                        doorways[DoorWay.DOWN].set(i,j);
                    }
                    break;
                case '4' :
                    l_count++;
                    if(l_count == l_door){
                        doorways[DoorWay.LEFT].set(i,j);
                    }
                    break;
                case '6' :
                    r_count++;
                    if(r_count == r_door){
                        doorways[DoorWay.RIGHT].set(i,j);
                    }
                    break;
                case '8' :
                    u_count++;
                    if(u_count == u_door){
                        doorways[DoorWay.UP].set(i,j);
                    }
                    break;
                case '#' :
                    floor[j][i] = FloorType.WALL.value(); break;
                case '^' :
                    floor[j][i] = FloorType.WATER.value(); break;
                case '.' :
                    floor[j][i] = FloorType.GROUND.value(); break;
                }
                if(j!=y && j != y+height-1 && i!=x && i!=x+width-1){
                    roomNumber[j][i] = roomno;
                }
                index++;
            }
        }
        for(int i=0; i<4; ++i){
            if(doorways[i].pos.x == 0 || doorways[i].pos.y == 0){
                throw new AssertionError("doorway is not set(x,y):" + map);
            }
        }
    }
}