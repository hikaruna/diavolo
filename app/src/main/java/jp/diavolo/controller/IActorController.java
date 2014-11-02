package jp.diavolo.controller;

import jp.diavolo.World;
import jp.diavolo.Actor;

public interface IActorController {
    public void think(World world);
    public void onDamage(Actor attacker);
}
