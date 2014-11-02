package jp.diavolo.mapgenerator;

import jp.diavolo.FloorMap;
import jp.diavolo.mapgenerator.original.OriginalMapGenerator;

public class MapGeneratorFactory {

    public enum MapType {
        ORIGINAL, NORMAL;
    }

    public interface IMapGenerator {
        public FloorMap generate(long seed, int width, int height);
    }

    public static OriginalMapGenerator getMapGenerator(MapType type){
        return new OriginalMapGenerator();
/*
        switch(type){
//        case ORIGINAL:
//            return new OriginalMapGenerator();
        case NORMAL:
            return new NormalMapGenerator();
        default:
            throw new RuntimeException("MapGenerator Type not implemented:" + type.toString());
        }
*/
    }
}
