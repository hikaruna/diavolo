package jp.diavolo.controller;

import java.util.List;

import jp.diavolo.Actor;
import jp.diavolo.World;
import jp.diavolo.core.Direction8;

public class RandomWalkController implements IActorController {

    private Actor target;
    public RandomWalkController(Actor a){
        target = a;
    }

    public void think(World world) {
        final List<Direction8> neighbors = world.getFloorMap().getNeighborFloor(target.getPosition());

        if(neighbors.size() == 0){
            target.stay();
        }else{
            Direction8 dir = neighbors.get(world.getRand().nextInt(neighbors.size()));
            target.walkTo(dir);
        }
    }

    public void onDamage(Actor attacker) {
        target.setActorController(new AttackController(target));
    }
}
