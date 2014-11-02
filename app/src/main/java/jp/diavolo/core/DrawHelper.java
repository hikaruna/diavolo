package jp.diavolo.core;

import java.util.HashMap;
import java.util.Map.Entry;

import jp.diavolo.R;

import android.graphics.*;

public class DrawHelper {

    public static void drawTile(
            Canvas canvas, int resource_id,
            int srcunit, int srcx, int srcy,
            Coord pos) {
        drawTile(canvas, resource_id, srcunit, srcx, srcy, pos, 0, 0);
    }

    public static void drawTile(
            Canvas canvas, int resource_id,
            int srcunit, int srcx, int srcy,
            Coord pos, int offsetx, int offsety) {
        drawTileDetail(canvas, resource_id, srcunit, srcx, srcy, pos, offsetx, offsety, TileDetail.NORMAL);
    }

    private enum TileDetail{
        NORMAL,
        EDGE_UP, EDGE_DOWN, EDGE_RIGHT, EDGE_LEFT,
        INNER_CORNER_UR, INNER_CORNER_UL, INNER_CORNER_DR, INNER_CORNER_DL,
        OUTER_CORNER_UR, OUTER_CORNER_UL, OUTER_CORNER_DR, OUTER_CORNER_DL, SHADOW
    }

    static Rect srcrect = new Rect();
    static Rect dstrect = new Rect();
    static Paint shadow = new Paint();
    static {
        shadow.setAlpha(100);
    }
    private static void drawTileDetail(
            Canvas canvas,
            int resource_id,
            int srcunit, int srcx, int srcy,
            Coord pos, int offsetx, int offsety,
            TileDetail detail
            ) {
        Device d = Device.getInstance();
        final int dstunit = d.getUnitSize();
        final int centerX = d.screenCenterX;
        final int centerY = d.screenCenterY;

        Bitmap bitmap = d.getTextureStore().get(resource_id);
        final int srcL = srcx * srcunit;
        final int srcT = srcy * srcunit;
        final int dstL = centerX + pos.x *dstunit + offsetx;
        final int dstT = centerY + pos.y *dstunit + offsety;

        final int su10 = srcunit / 10; //10%
        final int su90 = 9 * srcunit / 10; //90%

        final int du10 = dstunit / 10;
        final int du90 = 9 * dstunit / 10;
        Paint paint = null;
        switch(detail){
        case EDGE_UP:
            srcrect.set(srcL, srcT, srcL+srcunit, srcT + su10);
            dstrect.set(dstL, dstT, dstL+dstunit, dstT + du10);
            break;
        case EDGE_LEFT:
            srcrect.set(srcL, srcT, srcL + su10, srcT+srcunit);
            dstrect.set(dstL, dstT, dstL + du10, dstT+dstunit);
            break;
        case EDGE_DOWN:
            srcrect.set(srcL, srcT + su90, srcL+srcunit, srcT + srcunit);
            dstrect.set(dstL, dstT + du90, dstL+dstunit, dstT + dstunit);
            break;
        case EDGE_RIGHT:
            srcrect.set(srcL + su90, srcT, srcL + srcunit, srcT+srcunit);
            dstrect.set(dstL + du90, dstT, dstL + dstunit, dstT+dstunit);
            break;

        case OUTER_CORNER_UL:
            srcrect.set(srcL + 16, srcT + 16, srcL + 16 + su10, srcT + 16 + su10);
            dstrect.set(dstL, dstT, dstL + du10, dstT + du10);
            break;
        case OUTER_CORNER_UR:
            srcrect.set(srcL + 20, srcT + 16, srcL + 20 + su10, srcT + 16 + su10);
            dstrect.set(dstL+du90, dstT, dstL + dstunit, dstT + du10);
            break;
        case OUTER_CORNER_DR:
            srcrect.set(srcL+ 20, srcT+20, srcL + 20 + su10, srcT + 20 + su10);
            dstrect.set(dstL+du90, dstT+du90, dstL + dstunit, dstT + dstunit);
            break;
        case OUTER_CORNER_DL:
            srcrect.set(srcL+16, srcT+20, srcL + 20, srcT + 20 + su10);
            dstrect.set(dstL, dstT+du90, dstL + du10, dstT + dstunit);
            break;

        case INNER_CORNER_UL:
            srcrect.set(srcL, srcT, srcL + su10*2, srcT + su10*2);
            dstrect.set(dstL, dstT, dstL + du10*2, dstT + du10*2);
            break;
        case INNER_CORNER_UR:
            srcrect.set(srcL+8*su10, srcT, srcL + srcunit, srcT + su10*2);
            dstrect.set(dstL+8*du10, dstT, dstL + dstunit, dstT + du10*2);
            break;
        case INNER_CORNER_DR:
            srcrect.set(srcL+8*su10, srcT+su10*2, srcL + srcunit, srcT + srcunit);
            dstrect.set(dstL+8*du10, dstT+du10*2, dstL + dstunit, dstT + dstunit);
            break;
        case INNER_CORNER_DL:
            srcrect.set(srcL, srcT+su10*2, srcL + su10*2, srcT + srcunit);
            dstrect.set(dstL, dstT+du10*2, dstL + du10*2, dstT + dstunit);
            break;
        case SHADOW:
            srcrect.set(srcL, srcT, srcL + srcunit, srcT + srcunit + srcunit / 8);
            dstrect.set(dstL, dstT, dstL + dstunit, dstT + dstunit + dstunit / 8);
            paint = shadow;
            break;
        default:
            srcrect.set(srcL, srcT, srcL + srcunit, srcT + srcunit);
            dstrect.set(dstL, dstT, dstL + dstunit, dstT + dstunit);
            break;
        }
        canvas.drawBitmap(bitmap, srcrect, dstrect, paint);
    }

    public static void drawEdgedText(Canvas canvas, ScreenPosition pos, String text, Paint paint){
        PixelPosition pxpos = Device.getInstance().conv(pos);
        drawEdgedText(canvas, pxpos, text, paint);
    }

    public static void drawEdgedText(Canvas canvas, PixelPosition pxpos, String text, Paint paint){
        Paint edge = new Paint(paint);
        edge.setStyle(Paint.Style.STROKE);
        edge.setStrokeWidth(2.0f);
        edge.setColor(Color.BLACK);

        canvas.drawText(text, pxpos.x, pxpos.y + edge.getTextSize(), edge);
        canvas.drawText(text, pxpos.x, pxpos.y + paint.getTextSize(), paint);
    }

    private static final HashMap<Integer, TileDetail> table = new HashMap<Integer, TileDetail>(){
        private static final long serialVersionUID = 8239609314025571923L;
    {
        put(0x0500, TileDetail.INNER_CORNER_UL);
        put(0x0600, TileDetail.INNER_CORNER_UR);
        put(0x0900, TileDetail.INNER_CORNER_DL);
        put(0x0a00, TileDetail.INNER_CORNER_DR);
        put(0x1500, TileDetail.OUTER_CORNER_UL);
        put(0x2600, TileDetail.OUTER_CORNER_UR);
        put(0x4a00, TileDetail.OUTER_CORNER_DR);
        put(0x8900, TileDetail.OUTER_CORNER_DL);
    }};

    public static void drawWater(Canvas cvs, int mapRes, int visualType, int waterAnimTick, int meta, Coord pos, int ox, int oy) {
        //水の描画
        DrawHelper.drawTile(cvs, mapRes, 40, 38 + waterAnimTick, visualType, pos, ox, oy);

        if((meta & 0x0400) > 0){
            DrawHelper.drawTileDetail(cvs, mapRes, 40, 35, visualType, pos, ox, oy, TileDetail.EDGE_UP);
        }
        if((meta & 0x0800) > 0){
            DrawHelper.drawTileDetail(cvs, mapRes, 40, 35, visualType, pos, ox, oy, TileDetail.EDGE_DOWN);
        }
        if((meta & 0x0100) > 0){
            DrawHelper.drawTileDetail(cvs, mapRes, 40, 36, visualType, pos, ox, oy, TileDetail.EDGE_LEFT);
        }
        if((meta & 0x0200) > 0){
            DrawHelper.drawTileDetail(cvs, mapRes, 40, 36, visualType, pos, ox, oy, TileDetail.EDGE_RIGHT);
        }
        // inner corner
        for(Entry<Integer, TileDetail> entry : table.entrySet()){
            int i = entry.getKey();
            if(i < 0x1000){
                if((meta & i) == i){
                    DrawHelper.drawTileDetail(cvs, mapRes, 40, 37, visualType, pos, ox, oy, entry.getValue());
                }
            }else{
                if((meta & i) == (i & 0xf000)){
                    DrawHelper.drawTileDetail(cvs, mapRes, 40, 37, visualType, pos, ox, oy, entry.getValue());
                }
            }
        }
    }

    public static void drawShadow(Canvas canvas, Coord pos, int offsetX, int offsetY) {
        drawTileDetail(canvas, R.drawable.img_mychara, 40, 39, 0, pos, offsetX, offsetY, TileDetail.SHADOW);
    }

    static Paint dialogBackground = new Paint();
    static Paint dialogFrame = new Paint();
    static {
        dialogBackground.setColor(Color.BLUE);
        dialogBackground.setStyle(Paint.Style.FILL);
        dialogBackground.setAlpha(150);

        dialogFrame.setColor(Color.WHITE);
        dialogFrame.setStyle(Paint.Style.STROKE);
        dialogFrame.setStrokeWidth(2.0f);
    }
    public static void drawDialog(
            Canvas canvas, String[] strings, Paint paint) {

        final int fontsize = Device.getInstance().screenWidth / 480 * 36;

        PixelPosition tl = Device.getInstance().conv(new ScreenPosition(0.05f,0.60f));
        PixelPosition br = Device.getInstance().conv(new ScreenPosition(0.95f,0.95f));

        Rect rect = new Rect(tl.x, tl.y, br.x, br.y);
        canvas.drawRect(rect, dialogBackground);
        rect.top += 2;
        rect.left += 2;
        rect.right-= 2;
        rect.bottom -= 2;
        canvas.drawRect(rect, dialogFrame);

        int i = 1;
        for(String s: strings){
            canvas.drawText(s, tl.x + 20, tl.y + (fontsize + 5) * i, paint);
            i++;
        }
    }

    public static void drawWall(Canvas cvs, int mapRes, int srcunit, int fvisual, int meta, Coord pos, int ox, int oy) {
        meta >>= 8;

        //まず天井を描く
        DrawHelper.drawTile(cvs, mapRes, 40, 6, 0, pos, ox, oy);

        if((meta & 0xaa) == 0xaa){ //孤立した壁
            drawTile(cvs, mapRes, srcunit, 29, 0, pos, ox, oy);
            return;
        }
        if((meta & 0xaa) == 0x8a){ //壁の上端
            drawTile(cvs, mapRes, srcunit, 25, 0, pos, ox, oy);
            return;
        }
        if((meta & 0xaa) == 0xa8){ //壁の下端
            drawTile(cvs, mapRes, srcunit, 26, 0, pos, ox, oy);
            return;
        }
        if((meta & 0xaa) == 0xa2){ //壁の左端
            drawTile(cvs, mapRes, srcunit, 27, 0, pos, ox, oy);
            return;
        }
        if((meta & 0xaa) == 0x2a){ //壁の右端
            drawTile(cvs, mapRes, srcunit, 28, 0, pos, ox, oy);
            return;
        }

        if((meta & 0x20) == 0x20){ //下壁
            drawTile(cvs, mapRes, srcunit, fvisual, 0, pos, ox, oy);
        }
        if((meta & 0x8a) == 0x02){ //上壁
            drawTile(cvs, mapRes, srcunit, 12, 0, pos, ox, oy);
        }
        if((meta & 0x80) == 0x80){ //左壁
            drawTile(cvs, mapRes, srcunit, 13, 0, pos, ox, oy);
        }
        if((meta & 0x08) == 0x08){ //右壁
            drawTile(cvs, mapRes, srcunit, 14, 0, pos, ox, oy);
        }
        if((meta & 0x82) == 0x82){ //左上コーナー
            drawTile(cvs, mapRes, srcunit, 15, 0, pos, ox, oy);
        }
        if((meta & 0x0a) == 0x0a){ //右上コーナー
            drawTile(cvs, mapRes, srcunit, 16, 0, pos, ox, oy);
        }
        if((meta & 0xa0) == 0xa0){ //左下コーナー
            drawTile(cvs, mapRes, srcunit, 17, 0, pos, ox, oy);
        }
        if((meta & 0x28) == 0x28){ //右下コーナー
            drawTile(cvs, mapRes, srcunit, 18, 0, pos, ox, oy);
        }
        if((meta & 0x83) == 0x01){ //左上、内コーナー
            drawTile(cvs, mapRes, srcunit, 19, 0, pos, ox, oy);
        }
        if((meta & 0x0e) == 0x04){ //右上、内コーナー
            drawTile(cvs, mapRes, srcunit, 20, 0, pos, ox, oy);
        }
        if((meta & 0xe0) == 0x40){ //左下、内コーナー
            drawTile(cvs, mapRes, srcunit, 21, 0, pos, ox, oy);
        }
        if((meta & 0x38) == 0x10){ //右下、内コーナー
            drawTile(cvs, mapRes, srcunit, 22, 0, pos, ox, oy);
        }

    }
}
