package jp.diavolo;

import java.io.InputStream;
import java.util.*;

import jp.diavolo.Actor.TurnGroup;
import jp.diavolo.core.Device;
import jp.diavolo.core.TextureStore;
import jp.diavolo.util.CSVReader;

public class ActorFactory {
    final static String TAG = "CharacterFactory";
    private Map<Integer, String[]> enemies;

    public ActorFactory(InputStream is){
        List<String[]> lines = CSVReader.load(is);
        enemies = new HashMap<Integer, String[]>();
        for(String[] columns : lines){
            int id = Integer.parseInt(columns[0].trim());
            enemies.put(id, columns);
        }
    }

    public Actor create(World world, int actor_id){
        TextureStore ts = Device.getInstance().getTextureStore();

        String[] enemy = enemies.get(actor_id);

//        Log.d(TAG, "innername:" + "enemy" + enemy[0]); //id
//        Log.d(TAG, "name:" + enemy[1]);      //name
//        Log.d(TAG, "texture:" + String.format("enemy%03d", Integer.parseInt(enemy[0])));   // from id
//        Log.d(TAG, "ATK,DEF,EXP,HP,:" + enemy[2] + "," + enemy[3] + "," + enemy[4] + "," +enemy[5]);
//        Log.d(TAG, "?:"+ enemy[6]);
//        Log.d(TAG, "Item Drop Rate:"+ enemy[7]);
//        Log.d(TAG, "NPC Type:"+ enemy[8]);
//        Log.d(TAG, "CatalogID:"+ Integer.parseInt(enemy[9]) / 100 + "-" + Integer.parseInt(enemy[9])%100);
//        Log.d(TAG, "desc:" + enemy[10]);
//        Log.d(TAG, "desc:" + enemy[11]);
//        Log.d(TAG, "desc:" + enemy[12]);
//        Log.d(TAG, "desc:" + enemy[13]);
        int r_id = ts.toRID(String.format("enemy%03d", Integer.parseInt(enemy[0])));
        Actor c = new Actor(world, r_id, TurnGroup.ENEMY,
                Integer.parseInt(enemy[5]), //hp
                Integer.parseInt(enemy[2]), //atk
                Integer.parseInt(enemy[3]), //def
                Integer.parseInt(enemy[2]), //sp 敵のSPはATKに等しい。
                Integer.parseInt(enemy[4]) //exp
            );
        return c;
    }

    public int size() {
        return enemies.size();
    }
}
