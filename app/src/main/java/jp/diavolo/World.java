package jp.diavolo;

import java.util.*;

import jp.diavolo.Actor.IPositionListener;
import jp.diavolo.Actor.TurnGroup;
import jp.diavolo.FloorMap.FloorType;
import jp.diavolo.core.*;
import jp.diavolo.core.Camera;
import jp.diavolo.mapgenerator.MapGeneratorFactory;
import jp.diavolo.mapgenerator.original.*;
import jp.diavolo.mapgenerator.original.DungeonData.FloorData;
import android.graphics.*;
import android.util.Log;

public class World implements IDrawable, IUpdatable, IPositionListener {
    private DungeonScene scene;

    private List<Actor> actors;
    private List<Actor> deadActorList;
    private Camera camera;
    private Random rnd;
    private int waterAnimTick;
    private TurnGroup currentTurnGroup;
    private FloorMap floorMap;
    private int depth;
    private int turnCount;

    private FloorData floorData;

    public World(DungeonScene scene, DungeonData dungeonData, Random rnd){
        this.scene = scene;
        this.rnd = rnd;
        depth = 1;

        floorData = dungeonData.select(rnd, getDepth());
        waterAnimTick = 0;
        actors = new ArrayList<Actor>();
        deadActorList = new ArrayList<Actor>();
        OriginalMapGenerator generator = MapGeneratorFactory.getMapGenerator(MapGeneratorFactory.MapType.NORMAL);
        floorMap = generator.generate(rnd.nextLong(), 1, floorData);
        currentTurnGroup = TurnGroup.PLAYER;
        turnCount = 1;
    }

    public void update(SceneBase scene){
        for(Actor dead : deadActorList){
            //成仏させる
            actors.remove(dead);
        }
        deadActorList.clear();

        boolean onMotion = false;
        for(Actor actor: actors){
            if(actor.getTurnGroup() != currentTurnGroup) continue;
            if(actor.motion.isBusy()){
                onMotion = true;
                break;
            }
        }
        if(onMotion){
            waterAnimTick++;
            if(waterAnimTick >= 36) waterAnimTick = 0;

            for(Actor e : actors){
                e.update(scene);
            }
            return;
        }

        boolean isTurnGroupFinished = true;
        for(Actor actor: actors){
            if(actor.getTurnGroup() != currentTurnGroup) continue;
            if(actor.isTurnFinished()) continue;
            isTurnGroupFinished = false;
            actor.getActorController().think(this);
        }

        if(isTurnGroupFinished){
            nextTurn();
        }

        waterAnimTick++;
        if(waterAnimTick >= 36) waterAnimTick = 0;

        for(Actor actor : actors){
            actor.update(scene);
        }
    }

    private void nextTurn(){
        switch(currentTurnGroup){
        case PLAYER:
            turnCount ++;
            Log.v("World", "turn:" + turnCount);
            currentTurnGroup = TurnGroup.ALLY; break;
        case ALLY:
            currentTurnGroup = TurnGroup.ENEMY; break;
        case ENEMY:
            currentTurnGroup = TurnGroup.PLAYER; break;
        }
        //Log.d("@", currentTurnGroup.toString());
        for(Actor actor: actors){
            if(actor.getTurnGroup() == currentTurnGroup){
                actor.newTurn();
            }
        }
    }

    // 描画
    public void draw(Canvas cvs){
        if(camera == null) {
            Log.e("@", "camera is null");
            return;
        }
        int vx = camera.getTilePosition().x;
        int vy = camera.getTilePosition().y;
        int ox = camera.getPositionOffset().x;
        int oy = camera.getPositionOffset().y;

        Coord pos = new Coord(0,0);
        final int mapRes = floorMap.getMaptipResourceID();
        for(int y = - 4; y <= 4; ++y){
            //if(vy + y < 0 || vy + y >= floorMap.getHeight()) continue;
            for(int x = -4; x <= 4; ++x){
                //if(vx + x < 0 || vx + x >= floorMap.getWidth()) continue;
                pos.set(x, y);

                if(vy + y < 0 || vy + y >= floorMap.getHeight() ||
                        vx + x < 0 || vx + x >= floorMap.getWidth()
                ){
                    DrawHelper.drawWall(cvs, mapRes, 40, 9, 0, pos, -ox, -oy);
                continue;
                }

                final int fvisual = floorMap.getFloorVisual(vx+x, vy+y);
                final int meta = floorMap.getFloorVisualMeta(vx+x, vy+y);

                //壁を書く
                if(6 <= fvisual && fvisual <= 29){
                    DrawHelper.drawWall(cvs, mapRes, 40, fvisual, meta, pos, -ox, -oy);
                }else if(fvisual != 38){
                    //フロアを描く
                    DrawHelper.drawTile(cvs, mapRes, 40, fvisual, 0, pos, -ox, -oy);
                }else{
                    //水際の描画
                    DrawHelper.drawWater(cvs, mapRes, 0, waterAnimTick / 12, meta, pos, -ox, -oy);
                }
            }
        }
        for(Actor actor : actors){
            actor.drawShadow(cvs);
        }
        for(Actor actor : actors){
            actor.draw(cvs);
        }

        VirtualInput input = Device.getInstance().getVirtualInput();
        if(input.isMiniMapPressed){
            drawMiniMap(cvs);
        }
    }

    private static final Paint minimap = new Paint();
    private static final Paint actor_mark_player = new Paint();
    private static final Paint actor_mark_ally = new Paint();
    private static final Paint actor_mark_enemy = new Paint();
    private static final Paint actor_mark_other = new Paint();

    static {
        minimap.setColor(Color.argb(60, 0, 0, 255));
        minimap.setAntiAlias(false);

        actor_mark_player.setColor(Color.argb(200, 255, 255, 255));
        actor_mark_player.setAntiAlias(true);

        actor_mark_ally.setColor(Color.argb(200, 255, 255, 0));
        actor_mark_ally.setAntiAlias(true);

        actor_mark_enemy.setColor(Color.argb(200, 255, 0, 0));
        actor_mark_enemy.setAntiAlias(true);

        actor_mark_other.setColor(Color.argb(200, 255, 0, 0));
        actor_mark_other.setAntiAlias(true);
    }
    private void drawMiniMap(Canvas cvs){

        Rect r = new Rect();
        final int minimap_unit = Device.getInstance().getUnitSize() / 10;
        for(int y=0; y<floorMap.getHeight(); ++y){
            for(int x=0; x<floorMap.getWidth(); ++x){
                r.top = 40 + y*minimap_unit;
                r.bottom = 40 + (y+1)*minimap_unit;
                r.left = 40 + x*minimap_unit;
                r.right = 40+ (x+1)*minimap_unit;

                final FloorType type = FloorType.valueOf(floorMap.getFloorType(x, y));
                if(floorMap.getRoomID(x,y) > 0 || type == FloorType.GROUND || type == FloorType.PATH){
                    cvs.drawRect(r, minimap);
                }

                Actor c = getActorAt(x, y);
                if(c != null){
                    Paint actor_mark;
                    switch(c.getTurnGroup()){
                    case ENEMY:
                        actor_mark = actor_mark_enemy;
                        break;
                    case ALLY:
                        actor_mark = actor_mark_ally;
                        break;
                    case PLAYER:
                        actor_mark = actor_mark_player;
                        break;
                    default:
                        actor_mark = actor_mark_other;
                        break;
                    }
                    cvs.drawCircle(
                            40 + x * minimap_unit + minimap_unit/2,
                            40 + y * minimap_unit + minimap_unit/2,
                            minimap_unit/2,
                            actor_mark);
                }
            }
        }
    }

    public void spawnActor() {
        int index = floorData.enemyID.get(rnd.nextInt(floorData.enemyID.size()));
        Log.v("World", "spawn enemy ID:" + index);
        Actor actor = scene.getActorFactory().create(this, index);
        spawnActor(actor);
    }

    public void spawnActor(Actor actor) {
        Coord pos = floorMap.getRandomRoomPosition(rnd);
        actor.setPosition(pos);
        actor.addPositionListener(this);
        actors.add(actor);
        floorMap.registerActorPosition(actor);
    }

    public void notifyPosition(Actor actor) {
        //Log.d("@", "notifyPosition:" + id + "=(" + pos.x + "," + pos.y +")");
        floorMap.clearActorPosition(actor);
        //nullのときは単に消える。
        floorMap.registerActorPosition(actor);
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Actor getActorAt(Coord t) {
        return getActorAt(t.x, t.y);
    }

    public Actor getActorAt(int x, int y) {
        int id = floorMap.getActorIDAt(x,y);
        if(id==0){
            return null;
        }
        for(Actor c : actors){
            if(c.getID() == id){
                return c;
            }
        }
        return null;
    }

    public void deleteEnemy(Actor actor) {
        floorMap.clearActorPosition(actor);
        deadActorList.add(actor);
    }

    public Random getRand() {
        return rnd;
    }

    public FloorMap getFloorMap() {
        return floorMap;
    }

    public int getDepth() {
        return depth;
    }
}
