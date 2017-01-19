package animation.app.kazuhisa.rpg;

/**
 * Created by 和久 on 2016/11/21.
 */
public final class Constants {
    private Constants(){}

    //ビットマップ定数
    public static class BMP{
        public final static int CHARACTER = 6;  //キャラクターの枚数
        public final static int MAPGROUND = 5;  //マップの背景の枚数
    }

    //シーン定数
    public static class SCENE{
        public final static int
                START = 0,    //スタート
                MAP1 = 1,      //マップ
                APPEAR = 2,   //出現
                COMMAND = 3,  //コマンド
                ATTACK = 4,   //攻撃
                DEFENCE = 5,  //防御
                ESCAPE = 6,   //逃げる
                STATUS = 7,//ステータス
                MAP2 = 8; //マップ
    }

    //キー定数
    public static class KEY{
        public final static int
                NONE = -1,  //なし
                LEFT = 0,   //左
                RIGHT = 1,  //右
                UP = 2,     //上
                DOWN = 3,   //下
                ONE = 4,      //１
                TWO = 5,      //２
                SELECT = 6,//選択
                STATUS = 7;//ステータス
    }

    //マップ定数
    public static class MAP{
        public final static int[][]
                //マップの生成時定数
                ONE = {
                {3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
                {3, 1, 0, 0, 0, 0, 3, 0, 0, 3},
                {3, 0, 0, 0, 0, 2, 3, 3, 0, 3},
                {3, 0, 3, 3, 3, 3, 3, 3, 0, 3},
                {3, 0, 0, 3, 0, 0, 0, 3, 0, 3},
                {3, 3, 0, 3, 0, 3, 3, 3, 0, 3},
                {3, 0, 0, 3, 0, 0, 0, 0, 0, 3},
                {3, 0, 3, 3, 0, 3, 0, 3, 3, 3},
                {3, 0, 0, 0, 0, 3, 0, 0, 2, 3},
                {3, 3, 3, 3, 3, 3, 3, 3, 3, 3}};
        public final static int[][]
                TWO = {
                {3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
                {3, 2, 0, 0, 0, 0, 3, 0, 0, 3},
                {3, 0, 3, 0, 0, 0, 3, 3, 0, 3},
                {3, 0, 3, 3, 3, 3, 3, 3, 0, 3},
                {3, 0, 0, 3, 0, 0, 0, 3, 0, 3},
                {3, 3, 0, 3, 0, 3, 3, 3, 0, 3},
                {3, 0, 0, 3, 0, 0, 0, 0, 0, 3},
                {3, 0, 3, 3, 0, 3, 2, 0, 0, 3},
                {3, 0, 0, 0, 0, 3, 0, 0, 1, 3},
                {3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
        };
    }

}
