package jp.diavolo.mapgenerator.original;

import java.io.InputStream;
import java.util.*;

import jp.diavolo.util.CSVReader;

public class DungeonData {

    public class FloorData{
        int dungeonID;
        public String mapType;
        public String mapchipName;
        public List<Integer> enemyID;
    }

    private int dungeonId;
    private Map<Integer, List<FloorData>> data;

    public DungeonData(int id, InputStream is){
        dungeonId = id;
        data = new HashMap<Integer, List<FloorData>>();

        List<String[]> lines = CSVReader.load(is);
        for(String[] columns : lines){
            final int depth = Integer.parseInt(columns[0], 10);

            FloorData floorData = new FloorData();
            floorData.dungeonID = id;
            floorData.mapType = columns[1];
            floorData.mapchipName = columns[2];
            floorData.enemyID = new ArrayList<Integer>();
            for(int i = 3; i<columns.length; ++i){
                floorData.enemyID.add(Integer.parseInt(columns[i].trim(), 10));
            }

            if(!data.containsKey(depth)){
                data.put(depth, new ArrayList<FloorData>());
            }
            data.get(depth).add(floorData);
        }
    }

    public int ID(){
        return dungeonId;
    }
    public FloorData select(Random rnd, int depth) {
        List<FloorData> s = data.get(depth);
        return s.get(rnd.nextInt(s.size()));
    }
}
