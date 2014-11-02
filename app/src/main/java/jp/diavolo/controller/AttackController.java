package jp.diavolo.controller;

import java.util.List;

import jp.diavolo.Actor;
import jp.diavolo.World;
import jp.diavolo.core.Coord;
import jp.diavolo.core.Direction8;

public class AttackController implements IActorController {

    private Actor target;
    private Actor attackTarget;
    public AttackController(Actor c){
        target = c;
    }

    public void think(World world) {
        //既に誰かを追跡しているなら、隣から探す。

        final List<Direction8> neighborActors = world.getFloorMap().getNeighborActor(target.getPosition());
        boolean attacked = false;
        Coord pos = target.getPosition();
        for(Direction8 d: neighborActors){
            int x = pos.x + d.horizontal();
            int y = pos.y + d.vertical();
            Actor c = world.getActorAt(x,y);
            //誰も追いかけていないときに隣に敵陣営が来たらターゲット認定
            if(attackTarget == null &&
                c.getTurnGroup() != target.getTurnGroup()){
                attackTarget = c;
            }
            //ターゲットがいたら攻撃。
            if(attackTarget != null){
                if(c.getID() == attackTarget.getID()){
                    target.normalAttackTo(d);
                    attacked = true;
                    break;
                }
            }
        }
        if(!attacked){
            //攻撃できなかったのでそのままstay
            target.stay();
        }
    }

    public void onDamage(Actor attacker) {
        attackTarget = attacker;
    }

}
