package jp.diavolo.core;

import jp.diavolo.core.Device;
import jp.diavolo.core.Direction8;
import android.util.Log;
import android.view.MotionEvent;

public class VirtualInput {
    public boolean isTouching;
    public Direction8 moveDirection;
    public int originX;
    public int originY;
    private boolean locked;

    public static final int MINIMAP = 0;
    public boolean isMiniMapPressed;

    public VirtualInput(){
        isTouching = false;
        moveDirection = Direction8.NONE;
    }

    public void lock(){
        locked = true;
    }
    public void unlock(){
        locked = false;
    }

    public void clear(){
        isTouching = false;
        moveDirection = Direction8.NONE;
    }

    public boolean processRawData(MotionEvent event) {
        if(locked) return false;

        int action = event.getAction();
        String actionstr = "";
        switch(action){
        case MotionEvent.ACTION_UP:
            actionstr = "ACTION_UP"; break;
        case MotionEvent.ACTION_CANCEL:
            actionstr = "ACTION_CANCEL"; break;
        case MotionEvent.ACTION_DOWN:
            actionstr = "ACTION_DOWN"; break;
        case MotionEvent.ACTION_MOVE:
            actionstr = "ACTION_MOVE"; break;
        }
        Log.v("VirtualInput", "aciton:" + actionstr + " isTouching:" + isTouching);

        if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL){
            //UPが飛んできたら、さわってない状態にする。
            isTouching = false;
            moveDirection = Direction8.NONE;
        }
        final int deadzone = Device.getInstance().screenWidth / 6;
        if(action == MotionEvent.ACTION_DOWN){
            isTouching = true;
            originX = Device.getInstance().screenWidth /2;
            originY = Device.getInstance().screenHeight /2;
            int x = (int)event.getX();
            int y = (int)event.getY();
            int dx = x - originX;
            int dy = y - originY;
            moveDirection = Direction8.estimate(dx, dy, deadzone);
        }
        if(action == MotionEvent.ACTION_MOVE){
            int x = (int)event.getX();
            int y = (int)event.getY();
            int dx = x - originX;
            int dy = y - originY;
            moveDirection = Direction8.estimate(dx, dy, deadzone);
        }

        return true;
    }

    public boolean processButtonData(int kind, MotionEvent event){
        if(locked) return false;

        int action = event.getAction();
        switch(kind){
        case MINIMAP:
            if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE){
                isMiniMapPressed = true;
            }else{
                isMiniMapPressed = false;
            }
        }
        return true;
    }
}
