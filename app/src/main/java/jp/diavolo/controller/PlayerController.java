package jp.diavolo.controller;

import jp.diavolo.Actor;
import jp.diavolo.World;
import jp.diavolo.core.*;

public class PlayerController implements IActorController {
    private Actor target;
    public PlayerController(Actor actor) {
        target = actor;
    }

    public void think(World world) {
        VirtualInput virtualInput = Device.getInstance().getVirtualInput();
        virtualInput.lock();
        boolean isTouching = virtualInput.isTouching;
        Direction8 dir = virtualInput.moveDirection;
        virtualInput.unlock();

        if(isTouching){
            if(dir == Direction8.NONE){
                //攻撃
                //Log.d("@", "attack!");
                target.normalAttackTo();
            }else{
                //移動
                Coord pos = target.getPosition();
                final int x = pos.x + dir.horizontal();
                final int y = pos.y + dir.vertical();
                if(world.getFloorMap().isWalkable(pos.x, pos.y, x,y)){
                    target.walkTo(dir);
                }else{
                    target.turnTo(dir);
                }
            }
        }
    }

    public void onDamage(Actor attacker) {
        // TODO 自動生成されたメソッド・スタブ

    }

}
