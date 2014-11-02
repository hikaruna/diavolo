package jp.diavolo;

import android.graphics.Canvas;
import jp.diavolo.controller.PlayerController;
import jp.diavolo.core.*;

public class Player extends Actor {
    public Player(SceneBase scene, World world) {
        super(world, R.drawable.img_mychara, TurnGroup.PLAYER, 5,1,0,8,0);
        //プレイヤーがコントロールする
        setActorController(new PlayerController(this));
    }

    public void draw(Canvas canvas){
        final Camera camera = world.getCamera();
        if(camera == null) return;
        final Coord cameraOffset = camera.getPositionOffset();
        pos.set(tilePosition).sub_assign(camera.getTilePosition());

        if(motion instanceof AttackMotion){
            DrawHelper.drawTile(canvas, texture_id,
                    40, motion.getTextureOffset(charDir)-32, 1,
                    pos,
                    charDir.horizontal() * motion.getPositionOffset() - cameraOffset.x,
                    charDir.vertical() * motion.getPositionOffset() - cameraOffset.y);
        }else{
            super.draw(canvas);
        }
    }

    @Override
    protected int calcAttackPower(){
        int atk = this.attackPower * (level * 2 + 10) / 10;
        int damage = (level * 2 + 3) * (atk + sp -8)/16 + (level * 2 + 3);
        return damage;
    }
}
