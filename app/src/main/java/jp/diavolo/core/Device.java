package jp.diavolo.core;

import android.content.Context;
import android.util.Log;
import android.view.*;

public class Device {
    private static boolean isInitialized = false;
    private static Device instance;

    private VirtualInput virtualInput;
    private TextureStore textureStore;
    private SurfaceView surfaceView;
    public int screenWidth;
    public int screenHeight;
    public int screenCenterX;
    public int screenCenterY;
    private int unit;

    public static void initialize(SurfaceView sv){
        instance = new Device(sv);
    }
    public static Device getInstance(){
        if(isInitialized){
            Log.e("Device", "Device is not initialized.");
            throw new RuntimeException("Device is not initialized.");
        }
        return instance;
    }

    private Device(SurfaceView sv){
        virtualInput = new VirtualInput();
        textureStore = new TextureStore(sv.getContext().getApplicationContext());
        surfaceView = sv;
        squareSurfaceView(sv);
    }

    public VirtualInput getVirtualInput(){
        return virtualInput;
    }

    public TextureStore getTextureStore(){
        return textureStore;
    }

    public SurfaceView getSurfaceView(){
        return surfaceView;
    }

    public int getUnitSize(){
        return unit;
    }

    public void setDimention(int width, int height){
        screenWidth = width;
        screenHeight = height;
        screenCenterX = width / 2 - unit / 2;
        screenCenterY = height / 2 - unit / 2;
    }

    public PixelPosition conv(ScreenPosition pos){
        return new PixelPosition(
                (int)(screenWidth * pos.x),
                (int)(screenHeight * pos.y));
    }
    private void squareSurfaceView(SurfaceView sv){
        //正方形にする
        ViewGroup.LayoutParams params = surfaceView.getLayoutParams();
        WindowManager wm = (WindowManager) sv.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        if(width < height){
            params.width = params.height = width;
            unit = ((width / 6) / 40) * 40;
        }else{
            params.width = params.height = height;
        }
        Log.d("@", "unit:" + unit);

        surfaceView.setLayoutParams(params);
    }
}
