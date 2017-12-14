package com.stone.transition;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Random;

public class WidgetActivity extends AppCompatActivity {
    private DynamicReceiver dynamicReceiver;
    private TextView poem;

    //古诗词数据集
    String poem1 = "东临碣石，以观沧海。"+"\n"+"水何澹澹，山岛竦峙。"+"\n"+"树木丛生，百草丰茂。"+"\n"
            +"秋风萧瑟，洪波涌起。"+"\n"+"日月之行，若出其中；"+"\n"+"星汉灿烂，若出其里。"+"\n"+"幸甚至哉，歌以咏志。";
    String poem2 = "神龟虽寿，犹有竟时。"+"\n"+"腾蛇乘雾，终为土灰。"+"\n"+"老骥伏枥，志在千里。"+"\n"
            +"烈士暮年，壮心不已。"+"\n"+"盈缩之期，不但在天；"+"\n"+"养怡之福，可得永年。"+"\n"+"幸甚至哉，歌以咏志。";
    String poem3 = "野旷吕蒙营，江深刘备城。"+"\n"+"寒天催日短，风浪与云平。"+"\n"+"洒落君臣契，飞腾战伐名。"+"\n"
            +"维舟倚前浦，长啸一含情。";
    String poem4 = "鸿雁出塞北，乃在无人乡。"+"\n"+"举翅万馀里，行止自成行。"+"\n"+"冬节食南稻，春日复北翔。"+"\n"
            +"田中有转蓬，随风远飘扬。"+"\n"+"长与故根绝，万岁不相当。"+"\n"+"奈何此征夫，安得驱四方！";
    String poem5 = "对酒当歌，人生几何！"+"\n"+"譬如朝露，去日苦多。"+"\n"+"慨当以慷，忧思难忘。"+"\n"
            +"何以解忧？唯有杜康。"+"\n"+"青青子衿，悠悠我心。"+"\n"+"但为君故，沉吟至今。"+"\n"+"呦呦鹿鸣，食野之苹。"+"\n"
            +"我有嘉宾，鼓瑟吹笙。"+"\n"+"明明如月，何时可掇？"+"\n"+"忧从中来，不可断绝。";
    String poem6 = "孟冬十月，北风徘徊，"+"\n"+"天气肃清，繁霜霏霏。"+"\n"+"鹍鸡晨鸣，鸿雁南飞，"+"\n"
            +"鸷鸟潜藏，熊罴窟栖。"+"\n"+"钱鎛停置，农收积场。"+"\n"+"逆旅整设，以通贾商。"+"\n"+"幸甚至哉，歌以咏志。";
    String poem7 = "天地英雄气，千秋尚凛然。"+"\n"+"势分三足鼎，业复五铢钱。"+"\n"+"得相能开国，生儿不象贤。"+"\n"
            +"凄凉蜀故妓，来舞魏宫前。";
    String poem8 = "滚滚长江东逝水，浪花淘尽英雄。"+"\n"+"是非成败转头空。"+"\n"+"青山依旧在，几度夕阳红。"+"\n"
            +"白发渔樵江渚上，惯看秋月春风。"+"\n"+"一壶浊酒喜相逢。"+"\n"+"古今多少事，都付笑谈中。";
    String[] poems = new String[]{ poem1, poem2, poem3, poem4, poem5, poem6, poem7, poem8 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);

        //注册动态广播
        dynamicReceiver = new DynamicReceiver();
        IntentFilter dynamic_filter = new IntentFilter();
        dynamic_filter.addAction("DYNAMICACTION");
        registerReceiver(dynamicReceiver, dynamic_filter);

        //随机推荐古诗词
        int n = 8;
        Random random = new Random();
        int index = random.nextInt(n);
        String message = poems[index].substring(0, poems[index].indexOf("\n"));
        Log.d("msg", message);

        poem = (TextView)findViewById(R.id.poems);
        poem.setText(poems[index]);

        //发送广播内容
        Intent intent = new Intent("DYNAMICACTION");
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销动态广播
        unregisterReceiver(dynamicReceiver);
    }

    //back键不退出应用
    @Override
    public void onBackPressed() {
        //回到手机桌面
        moveTaskToBack(true);
        Intent intent = new Intent(WidgetActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
