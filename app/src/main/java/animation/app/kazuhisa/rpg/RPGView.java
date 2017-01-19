package animation.app.kazuhisa.rpg;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import static animation.app.kazuhisa.rpg.Constants.*;

import java.util.Random;

/**
 * Created by 和久 on 2016/08/22.
 */
public class RPGView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    //画面サイズ定数
    private final static int
        W = 880,    //画面幅
        H = 480;    //画面高さ

    //勇者定数
    private final static int[]
        YU_MAXHP = {0, 30, 50, 70, 90, 110, 130, 150}, //最大体力
        YU_ATTACK = {0, 5, 10, 30, 45, 60, 75, 100}, //攻撃力
        YU_DEFENCE = {0, 0, 5, 10, 15, 20, 40, 50}, //守備力
        YU_EXP = {0, 0, 3, 6, 9, 15, 20, 40};       //必要経験値

    //敵定数
    private final static String[]
        EN_NAME = {"スライム", "ボス","レアスライム","悪魔","裏ボス"}; //名前
    private final static int[]
        EN_MAXHP = {10, 30, 20, 15, 150},    //最大体力
        EN_ATTACK = {10, 26, 10, 3, 60},   //攻撃力
        EN_DEFENCE = {0, 16, 0, 10, 50},   //守備力
        EN_EXP = {1, 25, 10, 3, 0};       //取得経験値

    //システム
    private SurfaceHolder holder;   //サーフェイスホルダー
    private Graphics g;             //グラフィックス
    private Thread thread;          //スレッド
    private int init = Constants.SCENE.START;    //初期化
    private int scene;              //シーン
    private int key;                //キー
    private Bitmap[] bmpCharacter = new Bitmap[BMP.CHARACTER];
    private Bitmap[] bmpMap = new Bitmap[BMP.CHARACTER];

    //勇者パラメータ
    private int yuX = 1;    //X座標
    private int yuY = 2;    //Y座標
    private int yuLV = 1;   //レベル
    private int yuHP = 30;  //体力
    private int yuEXP = 0;  //経験値

    //敵パラメータ
    private int enType; //種類
    private int enHP;   //体力

    private int healPoint=0;
    private int mapInit = 1;

    //コンストラクタ
    public RPGView(Activity activity){
        super(activity);

        //キャラクターのビットマップ読み込み
        for(int i = 0; i < BMP.CHARACTER; i++){
            bmpCharacter[i] = readBitmap(activity, "rpgc"+i);
        }

        //マップのビットマップ読み込み
        for(int i = 0; i < BMP.MAPGROUND; i++){
            bmpMap[i] = readBitmap(activity, "rpgm"+i);
        }

        //サーフェイスホルダーの生成
        holder = getHolder();
        holder.setFormat(PixelFormat.RGBA_8888);
        holder.addCallback(this);

        //画面サイズの指定
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        int dw = H * p.x / p.y;
        holder.setFixedSize(dw,H);

        //グラフィックスの生成
        g = new Graphics(holder);
        g.setOrigin((dw - W) / 2, 0);
    }

    //サーフェイス生成時に呼ばれる
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new Thread(this);
        thread.start();
    }

    //サーフェイス終了時に呼ばれる
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread = null;
    }

    //サーフェイス変更時に呼ばれる
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
    }

    //スレッドの処理
    public void run(){
        while(thread != null){
            //シーンの初期化
            if(init >= 0){
                scene = init;
                //スタート
                if(scene == SCENE.START){
                    scene = SCENE.MAP1;
                    yuX = 1;    //X座標
                    yuY = 2;    //Y座標
                    yuLV = 1;   //レベル
                    yuHP = 30;  //体力
                    yuEXP = 0;  //経験値
                }

                init = -1;
                key = KEY.NONE;
            }

            //マップ
            if(scene == SCENE.MAP1) {
                mapInit = SCENE.MAP1;
                //移動
                boolean flag = false;
                //ステータスの確認
                if (key == KEY.STATUS) {
                    init = SCENE.STATUS;
                }
                //キャラクターの移動
                else if (key == KEY.UP) {
                    if (MAP.ONE[yuY - 1][yuX] != 3) {
                        yuY--;
                        flag = true;
                    }
                } else if (key == KEY.DOWN) {
                    if (MAP.ONE[yuY + 1][yuX] != 3) {
                        yuY++;
                        flag = true;
                    }
                } else if (key == KEY.LEFT) {
                    if (MAP.ONE[yuY][yuX - 1] != 3) {
                        yuX--;
                        flag = true;
                    }
                } else if (key == KEY.RIGHT) {
                    if (MAP.ONE[yuY][yuX + 1] != 3) {
                        yuX++;
                        flag = true;
                    }
                }
                if (flag) {

                    //敵出現の計算
                    if (MAP.ONE[yuY][yuX] == 0) {
                        //スライム
                        if (rand(10) == 0) {
                            enType = 0;
                            init = SCENE.APPEAR;
                        }
                        //レアスライム
                        else if (rand(50) == 0) {
                            enType = 2;
                            init = SCENE.APPEAR;
                        }

                    }
                    //勇者の回復
                    else if (MAP.ONE[yuY][yuX] == 1) {
                        healPoint = YU_MAXHP[yuLV] - yuHP;

                        yuHP = YU_MAXHP[yuLV];
                        sleep(200);
                    }
                    //敵の城
                    else if (MAP.ONE[yuY][yuX] == 2) {
                        enType = 1;
                        init = SCENE.APPEAR;
                    }
                    //マップの移動
                    else if(MAP.ONE[yuY][yuX] == 4){
                        MAP.ONE[yuY][yuX] = 2;
                        yuX = 8;
                        yuY = 7;
                        init = SCENE.MAP2;
                    }
                }

                //描画
                g.lock();
                for(int j = -3; j <= 3; j++){
                    for(int i = -5; i <= 5; i++){
                        int idx = 3;
                        if(0 <= yuX+i && yuX+i < MAP.ONE[0].length && 0 <= yuY+j && yuY+j < MAP.ONE.length){
                            idx = MAP.ONE[yuY+j][yuX+i];
                        }
                        g.drawBitmap(bmpMap[idx], W/2-40+80*i, H/2-40+80*j);
                    }
                }
                g.drawBitmap(bmpCharacter[0], W/2-40, H/2-40);
                drawStatus();
                if(MAP.ONE[yuY][yuX] == 1) {
                    drawMessage("HPが" + healPoint + "回復した！！");
                }
                g.unlock();
            }
            else if(scene == SCENE.MAP2){
                mapInit = SCENE.MAP2;
                //移動
                boolean flag = false;
                //ステータスの確認
                if(key == KEY.STATUS){
                    init = SCENE.STATUS;
                }
                //キャラクターの移動
                else if(key == KEY.UP){
                    if(MAP.TWO[yuY-1][yuX] != 3){
                        yuY--;
                        flag = true;
                    }
                }
                else if(key == KEY.DOWN){
                    if(MAP.TWO[yuY+1][yuX] != 3){
                        yuY++;
                        flag = true;
                    }
                }
                else if(key == KEY.LEFT){
                    if(MAP.TWO[yuY][yuX-1] != 3){
                        yuX--;
                        flag = true;
                    }
                }
                else if(key == KEY.RIGHT){
                    if(MAP.TWO[yuY][yuX+1] != 3){
                        yuX++;
                        flag = true;
                    }
                }
                if(flag){

                    //敵出現の計算
                    if(MAP.TWO[yuY][yuX] == 0){
                        //硬いスライム
                        if(rand(10) == 0){
                            enType = 3;
                            init = SCENE.APPEAR;
                        }
                        //レアスライム
                        else if(rand(50) == 0) {
                            enType = 2;
                            init = SCENE.APPEAR;
                        }

                    }
                    //勇者の回復
                    if(MAP.TWO[yuY][yuX] == 1){
                        healPoint = YU_MAXHP[yuLV]-yuHP;

                        yuHP = YU_MAXHP[yuLV];
                        sleep(200);
                    }
                    //敵の城
                    if (MAP.TWO[yuY][yuX] == 2){
                        enType = 4;
                        init = SCENE.APPEAR;
                    }
                }

                //描画
                g.lock();
                for(int j = -3; j <= 3; j++){
                    for(int i = -5; i <= 5; i++){
                        int idx = 3;
                        if(0 <= yuX+i && yuX+i < MAP.TWO[0].length && 0 <= yuY+j && yuY+j < MAP.TWO.length){
                            idx = MAP.TWO[yuY+j][yuX+i];
                        }
                        g.drawBitmap(bmpMap[idx], W/2-40+80*i, H/2-40+80*j);
                    }
                }
                g.drawBitmap(bmpCharacter[0], W/2-40, H/2-40);
                drawStatus();
                if(MAP.TWO[yuY][yuX] == 1) {
                    drawMessage("HPが" + healPoint + "回復した！！");
                }
                g.unlock();

            }
            //出現の処理
            else if(scene == SCENE.APPEAR){
                //初期化
                enHP = EN_MAXHP[enType];

                //フラッシュ
                sleep(300);
                for(int i = 0; i < 6; i++){
                    g.lock();
                    if(i%2 == 0){
                        g.setColor(Color.rgb(0, 0, 0));
                    }
                    else{
                        g.setColor(Color.rgb(255, 255, 255));
                    }
                    g.fillRect(0, 0, W, H);
                    g.unlock();
                    sleep(100);
                }
                //メッセージ
                drawBattle(EN_NAME[enType] + "があらわれた");
                waitSelect();
                init = SCENE.COMMAND;
            }

            //コマンド
            else if(scene == SCENE.COMMAND){
                drawBattle("   1.攻撃　　　　　2.逃げる");
                key = KEY.NONE;
                while (init == -1){
                    if(key == KEY.ONE) init = SCENE.ATTACK;
                    if(key == KEY.TWO) init = SCENE.ESCAPE;
                    sleep(100);
                }
            }

            //攻撃の処理
            else if(scene == SCENE.ATTACK){
                boolean endflag = false;
                //メッセージ
                drawBattle("勇者の攻撃");
                waitSelect();

                //フラッシュ
                for(int i = 0; i < 10; i++){
                    drawBattle("勇者の攻撃", i%2 == 0);
                    sleep(100);
                }

                //攻撃の計算
                int damage = YU_ATTACK[yuLV] - EN_DEFENCE[enType] + rand(10);
                if(damage <= 1) damage = 1;
                if(damage >= 99) damage = 99;

                //メッセージ
                drawBattle(damage + "ダメージ与えた！");
                waitSelect();

                //体力の計算
                enHP -= damage;
                if(enHP <= 0) enHP = 0;

                //勝利
                init = SCENE.DEFENCE;
                if(enHP == 0){
                    //メッセージ
                    drawBattle(EN_NAME[enType] + "を倒した");
                    waitSelect();

                    //経験値計算
                    int lebel = 0;
                    yuEXP += EN_EXP[enType];
                    if(yuLV<YU_MAXHP.length -1 && YU_EXP[yuLV+1] <= yuEXP) {
                        while (yuLV < YU_MAXHP.length - 1 && YU_EXP[yuLV + 1] <= yuEXP) {
                            yuLV++;
                            lebel++;
                        }
                        drawBattle("レベルが" + lebel + "アップした");
                        waitSelect();
                    }

                    //エンディング
                    if(enType == 1){
                        g.lock();
                        g.setColor(Color.rgb(0, 0, 0));
                        g.fillRect(0, 0, W, H);
                        g.setColor(Color.rgb(255, 255, 255));
                        g.setTextSize(32);
                        String str = "Fin....?";
                        g.drawText(str, (W-g.measureText(str))/2, 180-(int)g.getFontMetrics().top);
                        g.unlock();
                        sleep(500);
                        waitSelect();
                        MAP.ONE[yuY][yuX] = 4;
                        yuX = 1;    //X座標
                        yuY = 2;    //Y座標
                    }
                    if(enType == 4){
                        g.lock();
                        g.setColor(Color.rgb(0, 0, 0));
                        g.fillRect(0, 0, W, H);
                        g.setColor(Color.rgb(255, 255, 255));
                        g.setTextSize(32);
                        String str = "Fin.";
                        g.drawText(str, (W-g.measureText(str))/2, 180-(int)g.getFontMetrics().top);
                        g.unlock();
                        sleep(500);
                        waitSelect();
                        endflag = true;
                    }

                    if(endflag){
                        init = SCENE.START;
                    }
                    else {
                        init = mapInit;
                    }
                }
            }

            //敵攻撃
            else if(scene == SCENE.DEFENCE){
                //メッセージ
                drawBattle(EN_NAME[enType] + "の攻撃");
                waitSelect();

                //フラッシュ
                for(int i = 0; i < 10; i++){
                    if(i%2 == 0){
                        g.lock();
                        g.setColor(Color.rgb(255, 255, 255));
                        g.fillRect(0, 0, W, H);
                        g.unlock();
                    }
                    else{
                        drawBattle(EN_NAME[enType] + "の攻撃");
                    }
                    sleep(100);
                }

                //防御の計算
                int damage = EN_ATTACK[enType] - YU_DEFENCE[yuLV] + rand(10);
                if(damage <= 1) damage = 1;
                if(damage >= 99) damage = 99;

                //メッセージ
                drawBattle(damage + "ダメージ受けた！");
                waitSelect();

                //体力の計算
                yuHP -= damage;
                if(yuHP <= 0)  yuHP = 0;

                //敗北
                init = SCENE.COMMAND;
                if(yuHP == 0){
                    drawBattle("勇者は力尽きた。");
                    waitSelect();
                    drawBattle("HP回復やレベルを上げよう！");
                    waitSelect();
                    init = SCENE.MAP1;
                    yuX = 1;    //X座標
                    yuY = 2;    //Y座標
                    yuHP = YU_MAXHP[yuLV];  //勇者ＨＰ
                }
            }

            //逃げる
            else if(scene == SCENE.ESCAPE){
                //メッセージ
                drawBattle("勇者は逃げ出した！");
                waitSelect();

                //逃げるの計算
                init = mapInit;
                if(enType == 1 || rand(100) <= 10){
                    drawBattle(EN_NAME[enType] + "は回り込んだ");
                    waitSelect();
                    init = SCENE.DEFENCE;
                }
            }

            else if(scene == SCENE.STATUS){
                drawStatusDetail();
                if(key == KEY.STATUS){
                    init = mapInit;
                }
            }

            //スリープ
            key = KEY.NONE;
            sleep(200);
        }
    }

    //戦闘画面の描画
    private void drawBattle(String message){
        drawBattle(message, enHP >= 0);
    }

    //戦闘画面の描画
    private void drawBattle(String message, boolean visible){
        int color = (yuHP != 0) ? Color.rgb(255, 255, 255) : Color.rgb(2555, 0, 0);
        g.lock();
        g.setColor(color);
        g.fillRect(0, 0, W, H);
        drawStatus();
        if(visible){
            g.drawBitmap(bmpCharacter[1+enType],(W-bmpCharacter[1+enType].getWidth())/2,H-100-bmpCharacter[1+enType].getHeight());
        }
        g.setColor(Color.rgb(0, 0, 0));
        g.fillRect((W-504)/2, H -122, 504, 104);
        g.setColor(color);
        g.fillRect((W-500)/2, H -120, 500, 100);
        g.setColor(Color.rgb(0, 0, 0));
        g.setTextSize(32);
        g.drawText(message, (W-500)/2+50, 370-(int)g.getFontMetrics().top);
        g.unlock();
    }

    //メッセージの描画
    private void drawMessage( String message){
        int color = (yuHP != 0) ? Color.rgb(255, 255, 255) : Color.rgb(255, 0 , 0);
        g.setColor(Color.rgb(0, 0, 0));
        g.fillRect((W-504)/2, H -122, 504, 104);
        g.setColor(color);
        g.fillRect((W-500)/2, H -120, 500, 100);
        g.setColor(Color.rgb(0, 0, 0));
        g.setTextSize(32);
        g.drawText(message, (W-500)/2+50, 370-(int)g.getFontMetrics().top);
    }

    //ステータスの描画
    private void drawStatus(){
        int color = (yuHP != 0) ? Color.rgb(255, 255, 255) : Color.rgb(255, 0, 0);
        g.setColor(Color.rgb(0, 0, 0));
        g.fillRect((W-504)/2, 8, 504, 54);
        g.setColor(color);
        g.fillRect((W-500)/2, 10, 500, 50);
        g.setColor(Color.rgb(0, 0, 0));
        g.setTextSize(32);
        g.drawText("勇者 LV" + yuLV + " HP" + yuHP + "/" + YU_MAXHP[yuLV], (W-500)/2+80, 15-(int)g.getFontMetrics().top);
    }

    //詳細ステータスの描画
    private void drawStatusDetail(){
        int color = (yuHP != 0) ? Color.rgb(0, 0, 0) : Color.rgb(255, 0, 0);
        g.lock();
        g.setColor(color);
        g.fillRect(0, 0, W/2, H);
        g.setColor(Color.rgb(255, 255, 255));
        g.fillRect((W + 10), 8, W/2, H-10);
        g.setColor(color);
        g.fillRect((W + 14), 10, W/2-4, H-14);
        g.setColor(Color.rgb(255, 255, 255));
        g.setTextSize(32);
        g.drawText("勇者 LV" + yuLV, W/8+10, 10-(int)g.getFontMetrics().top);
        g.drawText("HP     " + yuHP + "  /  " + YU_MAXHP[yuLV], W/8-10, 80-(int)g.getFontMetrics().top);
        g.drawText("ATK    " + YU_ATTACK[yuLV], W/8-10, 115-(int)g.getFontMetrics().top);
        g.drawText("DEF    " + YU_DEFENCE[yuLV], W/8-10, 150-(int)g.getFontMetrics().top);
        if(yuLV < YU_EXP.length-1) {
            g.drawText("経験値 " + yuEXP + "  /  " + YU_EXP[yuLV+1], W / 8 - 10, 185 - (int) g.getFontMetrics().top);
        }
        else if(yuLV == YU_EXP.length-1){
            g.drawText("経験値 " + yuEXP + "  /  MAX", W / 8 - 10, 185 - (int) g.getFontMetrics().top);
        }
        g.unlock();
    }

    //タッチ時に呼ばれる
    @Override
    public boolean onTouchEvent(MotionEvent event){
        int touchX = (int)(event.getX()*W/getWidth());
        int touchY = (int)(event.getY()*H/getHeight());
        int touchAction = event.getAction();
        if(touchAction == MotionEvent.ACTION_DOWN){
            if(scene == mapInit || scene == SCENE.STATUS){
                if(W-100 < touchX && touchX < W &&
                        0 < touchY && touchY < 100){
                    key = KEY.STATUS;
                }
                else {
                    if (Math.abs(touchX - W / 2) > Math.abs(touchY - H / 2)) {
                        key = (touchX - W / 2 < 0) ? KEY.LEFT : KEY.RIGHT;
                    } else {
                        key = (touchY - H / 2 < 0) ? KEY.UP : KEY.DOWN;
                    }
                }
            }
            else if(scene == SCENE.APPEAR || scene == SCENE.ATTACK || scene == SCENE.DEFENCE || scene == SCENE.ESCAPE){
                key = KEY.SELECT;
            }
            else if(scene == SCENE.COMMAND){
                if(W/2-250 < touchX && touchX < W/2 &&
                        H-190 < touchY && touchY < H){
                    key = KEY.ONE;
                }
                else if(W/2 < touchX && touchX < W/2+250 &&
                        H-190 < touchY && touchY < H){
                    key = KEY.TWO;
                }
            }
        }
        return true;
    }

    //決定キー待ち
    private void waitSelect(){
        key = KEY.NONE;
        while(key != KEY.SELECT) sleep(100);
    }

    //スリープ
    private void sleep(int time){
        try{
            Thread.sleep(time);
        }catch (Exception e){
        }
    }

    //乱数の取得
    private static Random rand = new Random();
    private static int rand(int num){
        return (rand.nextInt() >>> 1)%num;
    }

    //ビットマップの読み込み
    private static Bitmap readBitmap(Context context, String name){
        int resID = context.getResources().getIdentifier(
                name, "drawable", context.getPackageName());
        return BitmapFactory.decodeResource(
                context.getResources(),resID);
    }
}
