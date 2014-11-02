package jp.diavolo;

import jp.diavolo.core.Device;
import jp.diavolo.core.Direction8;

class Motion{
    protected final static int[] dirTextureOffset = { 0, 0, 4, 1, 5, 2, 7, 3, 6 };
    protected int finishCount;
    protected int motionCount = 0;
    protected int animCount = 0;
    public Motion(int finishCount, Motion prev) {
        this.finishCount = finishCount;
        animCount = 0;
    }

    void tick(){
        if(isFinished()){
            return;
        }
        motionCount++;
        animCount++;

        if(isFinished()){
            onFinished();
        }
    }

    public int getCount(){
        return motionCount;
    }
    public boolean isFinished() {
        return motionCount >= finishCount;
    }
    public boolean isBusy(){
        return !isFinished();
    }

    protected void onFinished(){
    }

    public int getTextureOffset(Direction8 dir) {
        return 0;
    }

    public int getPositionOffset() {
        return 0;
    }

    public int getTxtureIndex() {
        return 0;
    }
}

class StayWalkMotion extends Motion {
    static private final int[] walkAnimTextureOffset = { 0,0,0,0, 1,1, 2,2,2,2, 1,1 };

    public StayWalkMotion(Motion prev) {
        super(walkAnimTextureOffset.length, prev);
        if(prev != null){
            animCount = prev.animCount;
        }else{
            animCount = 0;
        }
    }

    /**
     * 歩きモーションなのでいつでも遷移可能
     */
    @Override
    public boolean isBusy(){
        return false;
    }

    @Override
    public int getTextureOffset(Direction8 dir){
        return dirTextureOffset[dir.value()]*3 + walkAnimTextureOffset[animCount % walkAnimTextureOffset.length];
    }
};

class WalkMotion extends Motion {
    static private final int[] walkAnimTextureOffset = { 0,0,0,0, 1,1, 2,2,2,2, 1,1 };

    public WalkMotion(Motion prev) {
        super(5, prev);
        if(prev != null){
            animCount = prev.animCount;
        }else{
            animCount = 0;
        }
    }

    @Override
    public int getTextureOffset(Direction8 dir){
        return dirTextureOffset[dir.value()]*3 + walkAnimTextureOffset[animCount % walkAnimTextureOffset.length];
    }

    @Override
    public int getPositionOffset(){
        Device d = Device.getInstance();
        return d.getUnitSize() * (motionCount -5) / 5;
    }
}

class AttackMotion extends Motion {
    static private final int[] attackAnimTextureOffset = { 0,0,1,2,2,2,2,2,2,0,0,0};
    static private final int[] attackAnimPositionOffset = { 2,2,4,6,6,6,6,2,0,0,0,0};
    public AttackMotion(Motion prev) {
        super(attackAnimTextureOffset.length, prev);
    }
    @Override
    public int getTextureOffset(Direction8 dir){
        return dirTextureOffset[dir.value()]*3 + 32 + attackAnimTextureOffset[motionCount];
    }
    @Override
    public int getPositionOffset(){
        return attackAnimPositionOffset[motionCount];
    }
}

class DamageMotion extends Motion {
    int damage;
    static final int[] positionOffset = {0,2,3,4,4,3,3,2,0,0};
    public DamageMotion(int damage, Motion prev) {
        super(10, prev);
        this.damage = damage;
    }
    @Override
    public int getTextureOffset(Direction8 dir){
        return 24 + dirTextureOffset[dir.value()];
    }

    @Override
    public int getPositionOffset(){
        return -positionOffset[motionCount];
    }
}

class DeathMotion extends Motion {
    public DeathMotion(Motion prev) {
        super(15, prev);
    }
    @Override
    public int getTextureOffset(Direction8 dir){
        if(motionCount < 10 || (motionCount/2 ) % 2 == 0){
            return 24 + dirTextureOffset[dir.value()];
        }else{
            // 描画しない
            return -1;
        }
    }
    @Override
    public int getPositionOffset(){
        switch(motionCount){
        case 0:
            return 0;
        case 1:
            return -2;
        case 2:
            return -3;
        }
        return -4;
    }
}