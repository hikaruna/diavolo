package jp.diavolo;

import java.util.*;

import jp.diavolo.core.*;

public class FloorMap {
    private int width, height;
    private int visualType;
    private int [][] npcID;
    private int [][] floor;
    private int [][] floorVisual;
    private byte [][] roomNumber;
    private Coord startPos;
    private int mapchip_res_id;

    public enum FloorType {
        WALL(0), GROUND(1), WATER(2), PATH(3), ROOM(4);
        private int _value;
        private FloorType(int n){
            this._value = n;
        }
        public int value(){
            return this._value;
        }
        public static FloorType valueOf(int n){
            for(FloorType f : FloorType.values()){
                if(f.value() == n)
                    return f;
            }
            return null;
        }
    };

    public FloorMap(Random rnd, int width, int height, String mapchipName, int[][] floor, byte[][] roomNumber){
        this.setWidth(width);
        this.setHeight(height);
        this.floor = floor;
        this.roomNumber = roomNumber;
        npcID = new int[height][width];

        setFloorVisual(rnd);
        startPos = getRandomFloorPosition(rnd);

        TextureStore ts = Device.getInstance().getTextureStore();
        mapchip_res_id = ts.toRID(mapchipName);
    }

    /**
     * NPCなどで塞がっているかどうか
     * @param x
     * @param y
     * @return 進入できなければtrue
     */
    public boolean isOccupied(int x, int y){
        return npcID[y][x] > 0;
    }

    public boolean isWall(int x, int y) {
        if(y < 0 || y >= getHeight()) return true;
        if(x < 0 || x >= getWidth()) return true;
        return floor[y][x] == FloorType.WALL.value();
    }
    public boolean isWater(int x, int y) {
        if(y < 0 || y >= getHeight()) return false;
        if(x < 0 || x >= getWidth()) return false;
        return floor[y][x] == FloorType.WATER.value();
    }

    public boolean isNoPassage(int x, int y){
        return isWall(x, y) || isWater(x, y) || isOccupied(x, y);
    }

    public int getHeight() {
        return height;
    }

    private void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    private void setWidth(int width) {
        this.width = width;
    }

    public Coord getStartPosition() {
        return startPos;
    }

    public int getFloorVisual(int x, int y) {
        return floorVisual[y][x] & 0xff;
    }

    public int getFloorVisualMeta(int x, int y) {
        return floorVisual[y][x] & 0xff00;
    }

    public int getVisualType() {
        return visualType;
    }

    public Coord getRandomRoomPosition(Random rnd) {
        for(int i=0; i<1000; ++i){
            int y = rnd.nextInt(height);
            int x = rnd.nextInt(width);
            if(isWall(x, y) || isWater(x, y) || isOccupied(x, y)){
                continue;
            }
            if(roomNumber[y][x]>0){
                return new Coord(x, y);
            }
        }
        throw new AssertionError("no place to spawn");
    }

    public Coord getRandomFloorPosition(Random rnd) {
        for(int i=0; i<1000; ++i){
            int y = rnd.nextInt(height);
            int x = rnd.nextInt(width);
            if(isWall(x, y) || isWater(x, y) || isOccupied(x, y)){
                continue;
            }
            return new Coord(x, y);
        }
        throw new AssertionError("no place to spawn");
    }

    public void registerActorPosition(Actor actor) {
        Coord pos = actor.getPosition();
        if(pos != null){
            npcID[pos.y][pos.x] = actor.getID(); // size がid.
        }
    }

    public void clearActorPosition(Actor actor){
        for(int y = 0; y < height; ++y){
            for(int x = 0; x < width; ++x){
                if(npcID[y][x] == actor.getID()){
                    npcID[y][x] = 0;
                    return;
                }
            }
        }
    }

    private void setFloorVisual(Random rnd){
        floorVisual = new int[height][width];
        for(int y = 0; y < height; ++y){
            for(int x = 0; x < width; ++x){
                floorVisual[y][x] = 6 + rnd.nextInt(3); //背景
            }
        }
        for(int y = 0; y < height; ++y){
            for(int x = 0; x < width; ++x){
                if(isWall(x, y)){
                    floorVisual[y][x] = 9 + rnd.nextInt(3); //上壁
                    if(!isWall(x-1,y-1)){
                        floorVisual[y][x] += 0x0100;
                    }
                    if(!isWall(x  ,y-1)){
                        floorVisual[y][x] += 0x0200;
                    }
                    if(!isWall(x+1,y-1)){
                        floorVisual[y][x] += 0x0400;
                    }
                    if(!isWall(x+1,y  )){
                        floorVisual[y][x] += 0x0800;
                    }
                    if(!isWall(x+1,y+1)){
                        floorVisual[y][x] += 0x1000;
                    }
                    if(!isWall(x  ,y+1)){
                        floorVisual[y][x] += 0x2000;
                    }
                    if(!isWall(x-1,y+1)){
                        floorVisual[y][x] += 0x4000;
                    }
                    if(!isWall(x-1,y  )){
                        floorVisual[y][x] += 0x8000;
                    }
                    continue;
                }
                if(!isWater(x, y)){
                    if(floor[y][x] == FloorType.GROUND.value()){
                        floorVisual[y][x] = 0 + rnd.nextInt(3); //部屋
                    }else{
                        floorVisual[y][x] = 3 + rnd.nextInt(3); //通路
                    }
                    continue;
                }
                floorVisual[y][x] = 38; //水
                if(!isWater(x-1, y)){
                    floorVisual[y][x] += 0x0100;
                }
                if(!isWater(x+1, y)){
                    floorVisual[y][x] += 0x0200;
                }
                if(!isWater(x, y-1)){
                    floorVisual[y][x] += 0x0400;
                }
                if(!isWater(x, y+1)){
                    floorVisual[y][x] += 0x0800;
                }
                if(!isWater(x-1, y-1)){
                    floorVisual[y][x] += 0x1000;
                }
                if(!isWater(x+1, y-1)){
                    floorVisual[y][x] += 0x2000;
                }
                if(!isWater(x+1, y+1)){
                    floorVisual[y][x] += 0x4000;
                }
                if(!isWater(x-1, y+1)){
                    floorVisual[y][x] += 0x8000;
                }
            }
        }
    }

    public int getMaptipResourceID() {
        return mapchip_res_id;
    }

    public int getNPCIDAt(Coord t) {
        return npcID[t.y][t.x];
    }

    public int getActorIDAt(int x, int y) {
        return npcID[y][x];
    }

    public boolean isWalkable(int x, int y, int x2, int y2) {
        if(Math.abs(x-x2) > 1) return false;
        if(Math.abs(y-y2) > 1) return false;
        if(isNoPassage(x2, y2)) return false;

        //斜め移動のとき
        if(x != x2 && y != y2){
            //通る角が通行不能ならダメ。
            if(isWall(x,y2)) return false;
            if(isWall(x2,y)) return false;
        }
        return true;
    }

    public int getFloorType(int x, int y) {
        return floor[y][x];
    }

    public List<Direction8> getNeighborFloor(Coord pos) {
        final List<Direction8> neighbors = new ArrayList<Direction8>();
        Direction8 d = Direction8.DOWN;
        for(int i=0; i<8; ++i,d = d.cw45()){
            int x = pos.x + d.horizontal();
            int y = pos.y + d.vertical();
            if(isWalkable(pos.x, pos.y, x,y)){
                neighbors.add(d);
            }
        }
        return neighbors;
    }


    public List<Direction8> getNeighborActor(Coord pos){
        final List<Direction8> neighborChars = new ArrayList<Direction8>();
        Direction8 d = Direction8.DOWN;
        for(int i=0; i<8; ++i,d = d.cw45()){
            int x = pos.x + d.horizontal();
            int y = pos.y + d.vertical();
            if(isOccupied(x,y)){
                neighborChars.add(d);
            }
        }
        return neighborChars;
    }

    public int getRoomID(int x, int y) {
        return roomNumber[y][x];
    }
}

