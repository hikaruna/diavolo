package jp.diavolo.core;

import jp.diavolo.Actor;

public class Camera implements IUpdatable{
    private Coord pos;
    private Coord offset;
    private Actor followTarget;

    public Camera(){
        followTarget = null;
        pos = new Coord(0, 0);
        offset= new Coord(0,0);
    }

    public void setTilePosition(int x, int y){
        pos.x = x;
        pos.y = y;
    }
    public void setOffset(int x, int y){
        offset.x = x;
        offset.y = y;
    }

    public void setTilePosition(Coord tilePosition) {
        pos.x = tilePosition.x;
        pos.y = tilePosition.y;
    }

    private void setOffset(Coord offset) {
        this.offset.x = offset.x;
        this.offset.y = offset.y;
    }

    public Coord getTilePosition(){
        return pos;
    }

    public Coord getPositionOffset() {
        return offset;
    }

    public void follow(Actor target) {
        followTarget = target;
    }

    public void update(SceneBase scene) {
        if(followTarget != null){
            setTilePosition(followTarget.getPosition());
            setOffset(followTarget.getPositionOffset());
        }
    }

}
