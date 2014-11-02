package jp.diavolo.core;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;

public abstract class SceneBase implements SurfaceHolder.Callback, Runnable {
    private Thread thread;
    private boolean isAttached;
    private List<IDrawable> drawChain;
    private List<IUpdatable> updateChain;
    private int globalTick;
    private int fps;

    public SceneBase(Context context) {
        Device.getInstance().getSurfaceView().getHolder().addCallback(this);
        drawChain = new ArrayList<IDrawable>();
        updateChain = new ArrayList<IUpdatable>();
        isAttached = false;
        globalTick = 0;
        fps = 20;
    }


    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("SceneBase", "surfaceChanged called."
                + String.format("format:%d width:%d height:%d", format, width, height));
        Device.getInstance().setDimention(width, height);
        if(isAttached){
            thread = new Thread(this);
            thread.start();
        }else{
            Device.getInstance().getTextureStore().clear();
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // SurfaceViewが最初に生成されたときに呼び出されるメソッド
        Log.d("SampleSurView", "surfaceCreated called.");
        isAttached = true;
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        //ゲームスレッドの終了を待つ
        isAttached = false;
        while(thread.isAlive());

        Log.d("SampleSurView", "surfaceDestroyed");
        Device.getInstance().getTextureStore().clear();
    }

    /**
     * ゲームループ
     */
    public void run(){
        // FPSを維持する
        long before = System.nanoTime();
        long now = before;
        long next = now + 1000000000 / fps;
        try {
            while(isAttached){
                now = System.nanoTime();
                if(next > now){
                    Thread.sleep( (next - now) / 1000000 );
                    continue;
                }
                next += 1000000000 / fps;
                if(next < 0){ next += 1000000000 / fps; }
                globalTick ++;
                before = now;
                //Log.v("@", "fps:" + 1000000000f/(next-before) +" diff:" + (next-before)/1000000f + " next:" + next / 1000000f + " before:" + before / 1000000f);

                updateEntities();
                drawEntities();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addDrawable(IDrawable drawable) {
        drawChain.add(drawable);
    }
    public void addUpdatable(IUpdatable updatable){
        updateChain.add(updatable);
    }

    private void updateEntities(){
        globalTick++;
        for(IUpdatable u: updateChain){
            u.update(this);
        }
    }
    private void drawEntities(){
        SurfaceHolder holder = Device.getInstance().getSurfaceView().getHolder();
        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(Color.BLACK); // クリア
        for(IDrawable d : drawChain){
            d.draw(canvas);
        }
        holder.unlockCanvasAndPost(canvas);
    }

    public long getGlobalTick(){
        return globalTick;
    }
    public void end() {
        Device.getInstance().getTextureStore().clear();
    }

}
