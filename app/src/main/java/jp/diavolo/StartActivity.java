package jp.diavolo;

import jp.diavolo.core.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnTouchListener;

public class StartActivity extends Activity implements OnTouchListener {

    private SceneBase scene;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        SurfaceView sv = (SurfaceView)findViewById(R.id.SurfaceView);
        sv.setOnTouchListener(this);
        Device.initialize(sv);
        scene = new DungeonScene(this);

        findViewById(R.id.button_minimap).setOnTouchListener(this);
    }

    public void onDestroy(){
        super.onDestroy();
        scene.end();
    }

    //戻るを処理
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode != KeyEvent.KEYCODE_BACK){
            return super.onKeyDown(keyCode, event);
        }else{
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("終了しますか？");
            alert.setMessage("終了しますか？");
            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    StartActivity.this.finish();
                }
            });
            alert.setNegativeButton(android.R.string.no, null);
            alert.show();
            return false;
        }
    }

    public boolean onTouch(View view, MotionEvent event) {
        VirtualInput input = Device.getInstance().getVirtualInput();
        switch(view.getId()){
        case R.id.SurfaceView:
            return input.processRawData(event);
        case R.id.button_minimap:
            view.onTouchEvent(event);
            return input.processButtonData(VirtualInput.MINIMAP, event);
        }
        return view.onTouchEvent(event);
    }
}
