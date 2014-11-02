package jp.diavolo.core;

import java.util.*;
import java.util.Map.Entry;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class TextureStore {
    private Context context;
    private Map<Integer, Bitmap> assets = new HashMap<Integer, Bitmap>();

    public TextureStore(Context context){
        this.context = context;
    }

    public int toRID(String filename){
        int rid = context.getResources().getIdentifier(filename, "drawable", context.getPackageName());
        if(rid == 0){
            throw new RuntimeException("RID not found for: " + filename);
        }
        return rid;
    }

    public Bitmap get(int bitmapID) {
        Bitmap bmp = assets.get(bitmapID);
        if(bmp != null){
            //開放済みかもしれないのでチェック
            if(!bmp.isRecycled()){
                return bmp;
            }
            //開放済みならコンテナから削除しておく
            assets.remove(bmp);
        }

        Resources resources = context.getResources();
        assets.put(bitmapID,
                BitmapFactory.decodeResource(resources, bitmapID));
        Log.d("TextureStore", "Load Bitmap:" + bitmapID + " (" + assets.get(bitmapID).getWidth() + "," + assets.get(bitmapID).getHeight() + ")");
        return assets.get(bitmapID);
    }

    public void clear() {
        for(Entry<Integer, Bitmap> entry : assets.entrySet()){
            entry.getValue().recycle();
        }
        assets.clear();
    }
}
