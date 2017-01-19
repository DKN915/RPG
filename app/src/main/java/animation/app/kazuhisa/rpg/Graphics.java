package animation.app.kazuhisa.rpg;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.view.SurfaceHolder;

/**
 * Created by 和久 on 2016/08/15.
 */

//グラフィックス
public class Graphics {
    private SurfaceHolder holder;   //サーフェイスホルダー
    private Paint         paint;    //ペイント
    private Canvas        canvas;   //キャンバス
    private int          originX;   //原点Ｘ
    private int          originY;   //原点Ｙ

    //コンストラクタ
    public Graphics(SurfaceHolder holder){
        this.holder = holder;
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    //ロック
    public void lock(){
        canvas = holder.lockCanvas();
        if(canvas == null) return;
        canvas.translate(originX,originY);
    }

    //アンロック
    public void unlock(){
        if(canvas == null) {
            return;
        }
        holder.unlockCanvasAndPost(canvas);
    }

    //描画原点の指定
    public void setOrigin(int x, int y){
        originX = x;
        originY = y;
    }

    //描画原点のX座標の取得
    public int getOriginX(){
        return originX;
    }

    //描画原点のY座標の取得
    public int getOriginY(){
        return originY;
    }

    //色の指定
    public void setColor(int color){
        paint.setColor(color);
    }

    //フォントサイズの指定
    public void setTextSize(int fontSize){
        paint.setTextSize(fontSize);
    }

    //フォントメトリックスの取得
    public FontMetrics getFontMetrics(){
        return paint.getFontMetrics();
    }

    //文字幅の取得
    public int measureText(String string){
        return (int)paint.measureText(string);
    }

    //塗りつぶし矩形の描画
    public void fillRect(int x, int y, int w, int h){
        if(canvas == null) return;
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(new Rect(x, y, x+w, y+h),paint);
    }

    //ビットマップの描画
    public void drawBitmap(Bitmap bitmap, int x, int y){
        if(canvas == null) return;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Rect src = new Rect(0, 0, w, h);
        Rect dst = new Rect(x, y, x+w, y+h);
        canvas.drawBitmap(bitmap, src, dst, null);
    }

    //ビットマップの描画
    public void drawBitmap(Bitmap bitmap, Rect src, Rect dst){
        if(canvas == null) return;
        canvas.drawBitmap(bitmap, src, dst, null);
    }

    //文字列の描画
    public void drawText(String string, int x, int y){
        if(canvas == null) return;
        canvas.drawText(string, x, y, paint);
    }
}
