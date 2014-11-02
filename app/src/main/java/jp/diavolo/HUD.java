package jp.diavolo;

import android.graphics.*;
import jp.diavolo.core.*;

public class HUD implements IDrawable, IUpdatable{

    static Paint textStyle = new Paint();
    static Paint staticTextStyle = new Paint();
    static {
        textStyle.setAntiAlias(true);
        textStyle.setStyle(Paint.Style.FILL);
        textStyle.setColor(Color.WHITE);
        textStyle.setTypeface(Typeface.MONOSPACE);

        staticTextStyle.setAntiAlias(true);
        staticTextStyle.setStyle(Paint.Style.FILL);
        staticTextStyle.setColor(Color.CYAN);
        staticTextStyle.setTypeface(Typeface.MONOSPACE);
    }

    private Actor target;
    private int gold;
    private int hp;
    private int level;
    private int floor;
    private int maxhp;

    public void update(SceneBase scene) {
        this.hp = target.getHP();
        this.maxhp = target.getMAXHP();
        this.gold = target.getGold();
        this.level = target.level;
        this.floor = target.getFloor();
    }

    public void draw(Canvas canvas) {
        final int sw = Device.getInstance().screenWidth;
        final int s = (int) (sw / 480 * 32);
        textStyle.setTextSize(s);
        staticTextStyle.setTextSize(s-5);

        //Floor
        DrawHelper.drawEdgedText(canvas, new ScreenPosition(0.04f, 0.04f), "  F", staticTextStyle);
        DrawHelper.drawEdgedText(canvas, new ScreenPosition(0.03f, 0.03f), String.format("%2d", this.floor), textStyle);

        //Lv
        DrawHelper.drawEdgedText(canvas, new ScreenPosition(0.19f, 0.04f), "Lv", staticTextStyle);
        DrawHelper.drawEdgedText(canvas, new ScreenPosition(0.18f, 0.03f), "  " + String.format("%2d", this.level), textStyle);

        if(maxhp > hp * 2){
            textStyle.setColor(Color.rgb(220, 0, 0));
        }else{
            textStyle.setColor(Color.WHITE);
        }

        //HP
        DrawHelper.drawEdgedText(canvas, new ScreenPosition(0.39f, 0.04f), "HP", staticTextStyle);
        DrawHelper.drawEdgedText(canvas, new ScreenPosition(0.56f, 0.04f), "/", staticTextStyle);
        DrawHelper.drawEdgedText(canvas, new ScreenPosition(0.38f, 0.03f), "  " + String.format("%3d", this.hp) + " " + String.format("%3d", this.maxhp), textStyle);

        textStyle.setColor(Color.WHITE);
        //Gold
        DrawHelper.drawEdgedText(canvas, new ScreenPosition(0.93f, 0.04f), "G", staticTextStyle);
        DrawHelper.drawEdgedText(canvas, new ScreenPosition(0.75f, 0.03f), String.format("%5d", this.gold), textStyle);

        //HP bar
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2.0f);
        paint.setColor(Color.WHITE);
        PixelPosition tl = Device.getInstance().conv(new ScreenPosition(0.39f, 0.10f));
        PixelPosition br = Device.getInstance().conv(new ScreenPosition(0, 0.12f));
        br.x = tl.x + maxhp * 2;
        if(br.x > sw - 10){
            tl.x -= (br.x - sw + 10);
            br.x = sw - 10;
        }
        if(tl.x < 10){
            tl.x = 10;
            br.x = sw - 10;
        }

        Rect rect = new Rect(tl.x,tl.y,br.x,br.y);
        canvas.drawRect(rect, paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        canvas.drawRect(rect, paint);

        rect.right = tl.x + (br.x - tl.x) * hp / maxhp;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GREEN);
        canvas.drawRect(rect, paint);

        //DrawHelper.drawDialog(canvas, new String[]{"test", "test1", "hoge"}, textStyle);
    }

    public void follow(Actor c){
        this.target = c;
    }
}
