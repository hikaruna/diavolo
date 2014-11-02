package jp.diavolo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import jp.diavolo.core.Camera;
import jp.diavolo.core.SceneBase;
import jp.diavolo.mapgenerator.original.DungeonData;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

class DungeonScene extends SceneBase {
    private Context context;
    private Actor boss;
    private World world;
    private HUD hud;
    private Camera camera;
    private ActorFactory actorFactory;
    private DungeonData dungeonData;

    public DungeonScene(Context context) {
        super(context);
        this.context = context;
        onStart();
    }

    protected void onStart(){
        Log.d("@", "onStart");

        AssetManager assets = context.getAssets();
        try {
            InputStream is = assets.open("enemies.txt", AssetManager.ACCESS_STREAMING | AssetManager.ACCESS_BUFFER);
            actorFactory = new ActorFactory(is);
        } catch (IOException e) {
            e.printStackTrace();
            throw new AssertionError("cannot read enemies.txt");
        }

        try {
            InputStream is = assets.open("dungeons/01_outside_the_hotel.txt");
            dungeonData = new DungeonData(1, is);
        } catch (IOException e) {
            e.printStackTrace();
            throw new AssertionError("cannot read dungeons/*");
        }

        world = new World(this, dungeonData, new Random(System.currentTimeMillis()));


        boss = new Player(this, world);
        world.spawnActor(boss);

        for(int i=0; i<20; ++i){
            world.spawnActor();
        }

        hud = new HUD();
        hud.follow(boss);
        camera = new Camera();
        camera.follow(boss);
        world.setCamera(camera);

        addDrawable(world);
        addDrawable(hud);

        addUpdatable(world);
        addUpdatable(camera);
        addUpdatable(hud);
    }

    public ActorFactory getActorFactory() {
        return actorFactory;
    }
}
