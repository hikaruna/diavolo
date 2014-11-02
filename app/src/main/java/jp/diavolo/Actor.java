package jp.diavolo;

import java.util.*;

import jp.diavolo.controller.*;
import jp.diavolo.core.*;
import android.graphics.Canvas;
import android.util.Log;

public class Actor implements IDrawable, IUpdatable {
    private static int[] expTable = {
        0,8,20,50,100,
        180,290,500,800,1200,
        1800,2500,3300,4100,5000,
        6100,7300,8600,10000,11500,
        14000,17000,20000,24000,29000,
        35000,42000,50000,60000,70000,
        85000,100000,120000,140000,160000,
        180000,200000,230000,260000,290000,
        330000,370000,410000,460000,510000,
        560000,620000,680000,750000,820000,
        900000,1000000,1100000,1200000,1300000,
        1400000,1500000,1600000,1700000,1800000,
        1900000,2000000,2100000,2200000,2300000,
        2400000,2500000,2600000,2700000,2800000,
        2900000,3100000,3300000,3500000,3700000,
        3900000,4100000,4300000,4500000,4700000,
        4900000,5100000,5300000,5500000,5700000,
        5900000,6100000,6300000,6500000,6700000,
        6900000,7100000,7300000,7500000,7700000,
        7900000,8400000,9000000,9999999,-1
    };

    public interface IPositionListener {
        void notifyPosition(Actor actor);
    }

    public enum TurnGroup{
        PLAYER, ALLY, ENEMY;
    }

    private static int maxid = 1;
    private int id;

    protected World world;
    protected Random rand;
    protected int texture_id;
    private int texture_index;

    protected Motion motion;
    private IActorController actorController;
    protected Coord tilePosition;
    protected Direction8 charDir;
    private TurnGroup turnGroup;
    private boolean isTurnFinished;

    private List<IPositionListener> listeners;
    protected int level;
    private int hp;
    private int maxhp;
    protected int attackPower;
    private int def;
    private int exp;
    protected int sp;

    public Actor(World world, int texture_id, TurnGroup turnGroup, int hp, int atk, int def, int sp, int exp){
        id = maxid;
        maxid++;
        level = 1;
        //Log.d("Character", "id=" + id);
        this.world = world;
        this.texture_id = texture_id;
        this.rand = new Random(1);
        tilePosition = new Coord(10, 10);
        charDir = Direction8.DOWN;
        motion = new StayWalkMotion(null);
        this.maxhp = this.hp = hp;
        this.attackPower = atk;
        this.def = def;
        this.sp = sp;
        this.exp = exp;
        listeners = new ArrayList<Actor.IPositionListener>();
        this.turnGroup = turnGroup;
        isTurnFinished = false;
        setActorController(new RandomWalkController(this));
    }

    public int getID(){
        return id;
    }

    public void update(SceneBase scene){
        motion.tick();

        Coord camerapos = world.getCamera().getTilePosition();
        if(Math.abs(camerapos.x - tilePosition.x) > 4 ||
            Math.abs(camerapos.y - tilePosition.y) > 4 ||
            motion.isFinished()
        ){
            if(motion instanceof DeathMotion){
                goDead();
                return;
            }
            motion = new StayWalkMotion(motion);
        }
    }

    protected static Coord pos = new Coord();

    public void drawShadow(Canvas canvas) {
        final Camera camera = world.getCamera();
        if(camera == null) return;

        final Coord cameraOffset = camera.getPositionOffset();
        pos.set(tilePosition).sub_assign(camera.getTilePosition());

        int posOffset = motion.getPositionOffset();
        DrawHelper.drawShadow(canvas, pos,
                charDir.horizontal() * posOffset - cameraOffset.x,
                charDir.vertical() * posOffset - cameraOffset.y);
    }
    public void draw(Canvas canvas){
        final Camera camera = world.getCamera();
        if(camera == null) return;

        final Coord cameraOffset = camera.getPositionOffset();
        pos.set(tilePosition).sub_assign(camera.getTilePosition());

        int posOffset = motion.getPositionOffset();

        DrawHelper.drawTile(canvas, texture_id,
                40, motion.getTextureOffset(charDir), texture_index,
                pos,
                charDir.horizontal() * posOffset - cameraOffset.x,
                charDir.vertical() * posOffset - cameraOffset.y);

        if(motion instanceof DamageMotion){
            DamageMotion m = (DamageMotion) motion;
            int t_i;
            if(m.damage > 40) t_i = 13;
            else if(m.damage < 15) t_i = 15;
            else t_i = 14;

            DrawHelper.drawTile(canvas, R.drawable.img_effect,
                    64, motion.getCount(), t_i, //小ダメージ
                    pos,
                    charDir.horizontal() * posOffset - cameraOffset.x,
                    charDir.vertical() * posOffset - cameraOffset.y);
        }
    }

    public void walkTo(Direction8 dir){
        if(isAnimating()) return;
        motion = new WalkMotion(motion);
        charDir = dir;
        tilePosition.add_assign(charDir);
        notifyPosition();

        isTurnFinished = true;
    }

    public Coord getPosition() {
        return tilePosition;
    }

    public Coord getPositionOffset() {
        if(motion instanceof WalkMotion){
            //WalkMotionのときだけ、カメラに追従させる。
            return charDir.getCoord().mul_assign(motion.getPositionOffset());
        }else{
            return Coord.ZERO;
        }
    }

    public boolean isAnimating() {
        return !(motion instanceof StayWalkMotion);
    }

    public void setPosition(Coord startPosition) {
        tilePosition.set(startPosition);
        notifyPosition();
    }
    public void normalAttackTo() {
        if(isAnimating()) return;

        Log.d("Character-Attack", "id:" + id + " hp:" + getHP());

        motion = new AttackMotion(motion);
        isTurnFinished = true;

        Coord t = tilePosition.clone().add_assign(charDir);
        Actor target = world.getActorAt(t);
        if(target != null){
            int damage = calcAttackPower();
            for(int i=0; i<target.def; ++i){
                damage *= 15;
                damage /= 16;
            }
            Log.d("Character","id:"+id+" to id:"+ target.getID()+" damage:" + damage);
            target.takeDamage(this, damage);
        }
    }

    protected int calcAttackPower(){
        int atk = this.attackPower * (level * 2 + 10) / 10;
        int damage = atk * (atk + sp -8)/16 + atk;
        return damage;
    }

    public void normalAttackTo(Direction8 dir) {
        if(isAnimating()) return;
        charDir = dir;
        normalAttackTo();
    }

    public void turnTo(Direction8 dir) {
        if(isAnimating()) return;
        charDir = dir;
    }

    public void takeDamage(Actor c, int damage){
        getActorController().onDamage(c);

        hp -= damage;
        Log.d("Character-damage", "id:" + id + " hp:" + getHP());
        charDir = Direction8.estimate(
                c.getPosition().x - tilePosition.x,
                c.getPosition().y - tilePosition.y, 0);
        if(hp < 0) hp = 0;
        if(hp == 0){
            Log.d("Character", "add exp:" +exp + " to id:" + c.getID());
            c.addExp(exp);
            motion = new DeathMotion(motion);
        }else{
            motion = new DamageMotion(damage, motion);
        }
    }

    private void addExp(int exp2) {
        this.exp += exp2;
        if(exp > expTable[level]){
            level++;
        }
        Log.d("Character", "level:" + level + " exp:" +exp);
    }

    private void goDead() {
        Log.d("Character-dead", "id:" + id);
        world.deleteEnemy(this);
    }

    private void notifyPosition(){
        //Log.d("@", "notifyPosition id:" + id);
        for(IPositionListener listener : listeners){
            listener.notifyPosition(this);
        }
    }

    public void addPositionListener(IPositionListener listener){
        listeners.add(listener);
    }

    public TurnGroup getTurnGroup() {
        return turnGroup;
    }

    public void newTurn(){
        isTurnFinished = false;
    }
    public boolean isTurnFinished() {
        return isTurnFinished;
    }

    /**
     * 何もせずにターンを終える
     */
    public void stay() {
        isTurnFinished = true;
    }

    public int getHP() {
        return hp;
    }

    public int getMAXHP(){
        return maxhp;
    }

    public int getGold() {
        return 0;
    }

    public int getFloor() {
        return world.getDepth();
    }

    public IActorController getActorController() {
        return actorController;
    }

    public void setActorController(IActorController actorController) {
        this.actorController = actorController;
    }
}
