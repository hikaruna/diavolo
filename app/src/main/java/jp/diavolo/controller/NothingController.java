package jp.diavolo.controller;

import jp.diavolo.Actor;
import jp.diavolo.World;

public class NothingController implements IActorController {
    private Actor target;
    public NothingController(Actor actor) {
        target = actor;
    }

    public void think(World world) {
        //do nothing
        target.stay();
    }

    public void onDamage(Actor attacker) {
        //do nothing
    }
}
