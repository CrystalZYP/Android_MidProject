package com.stone.transition;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Comment;
import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

/**
 * Created by xmuSistone on 2016/9/18.
 */
public class MainActivity extends FragmentActivity {

    private DrawerLayout drawerLayout; // 根布局
    private View positionView, searchToGetLeftLayout, editNewHero; // 顶部状态栏
    private ViewPager viewPager; // 中间可滑动的部分
    private TextView indicatorTv, name; // 底部指示器
    private List<CommonFragment> fragments = new ArrayList<>(); // 供ViewPager使用
    private List<Hero> heros = new ArrayList<>();

    //===========
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter autoCompleteTextViewAdapter;
    // 搜索界面数据
    String[] firstLetterItem = new String[] {"不限","A","B","C","D","E","F","G","H","I","J","K","L","M",
            "N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    String[] hero_powerItem = new String[] {"不限","群","魏","蜀","吴"};
    String[] hero_placeItem = new String[] {"不限","并州","冀州","交州","荆州","凉州","青州","司隶",
            "徐州","兖州","扬州","益州","幽州","豫州"};
    String[] hero_yearItem = new String[] {"不限", "不详", "184年", "185年", "186年", "187年", "188年", "189年", "190年",
            "191年", "192年", "193年", "194年", "195年", "196年", "197年", "198年", "199年", "200年",
            "201年", "202年", "203年", "204年", "205年", "206年", "207年", "208年", "209年", "210年",
            "211年", "212年", "213年", "214年", "215年", "216年", "217年", "218年", "219年", "220年",
            "221年", "222年", "223年", "224年", "225年", "226年", "227年", "228年", "229年", "230年",
            "231年", "232年", "233年", "234年", "235年", "236年", "237年", "238年", "239年", "240年",
            "241年", "242年", "243年", "244年", "245年", "246年", "247年", "248年", "249年", "250年",
            "251年", "252年", "253年", "254年", "255年", "256年", "257年", "258年", "259年", "260年",
            "261年", "262年", "263年", "264年", "265年", "266年", "267年", "268年", "269年", "270年",
            "271年", "272年", "273年", "274年", "275年", "276年", "277年", "278年", "279年", "280年"};
    String[] hero_sexItem = new String[] {"不限","男","女"};
    String[] hero_name = new String[] {"曹操","曹丕","貂蝉","董卓","关羽","刘备","孙权","大乔","周瑜","诸葛亮"};
    private ArrayList<String> nameList = new ArrayList<>();

    private List<Hero> herosTarget = new ArrayList<>();
    private List<CommonFragment> fragmentShow = new ArrayList<>();
    private Spinner firstLetterSpinner, hero_powerItemSpinner, hero_placeItemSpinner, hero_yearItemSpinner, hero_sexItemSpinner;
    private Button searchButton, resetButton, play, pause;
    private ArrayAdapter firstLetterAdapter, hero_powerItemAdapter, hero_placeItemAdapter, hero_yearItemAdapter, hero_sexItemAdapter;
    private List<String> firstLetterList, hero_powerItemList, hero_placeItemList, hero_yearItemList, hero_sexItemList;
    private String firstLetterTarget, hero_powerTarget, hero_placeTarget, hero_yearTarget, hero_sexTarget, hero_nameTarget;
    private int totalNum, currentItem;
    private List<Integer> herosIndexShow, herosIndexAll;
    private int herosNum;
    private int total;
    //=======================
    private FragmentStatePagerAdapter fspAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 1. 沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
                getWindow()
                        .getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {
                getWindow()
                        .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
        positionView = findViewById(R.id.position_view);
        dealStatusBar(); // 调整状态栏高度，增加按钮监听事件

        // 2. 初始化ImageLoader
        initImageLoader();

        initViewID();
        // 3. 初始化十个英雄
        initData();

        // 4. 填充ViewPager
        fillViewPager();

        // 5. 初始化搜索界面数据
        initSearchData();
        // 6. 初始化搜索界面视图
        initSearchView();
        // 7. 初始化搜索按钮逻辑
        initSearchButton();
        // 8. 初始化音乐播放按钮逻辑
        initPlayButton();

        View add = findViewById(R.id.edit_new_hero);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                it.setClass(MainActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("edit",1);
                bundle.putInt("detail",0);
                bundle.putInt("heroNum",total);
                it.putExtras(bundle);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(it);
            }
        });

        //注册eventbus
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event){
        int op_tag;    // add-1  , delete-2 , edit-3
        String name;
        op_tag = event.op_tag;
        name = event.name;
        if (op_tag == 1) {
            String firstLetter = event.firstLetter;
            String power = event.power;
            String sex = event.sex;
            String birth_year = event.birth_year;
            String death_year = event.death_year;
            String place = event.place;
            String hevent = event.event;
            String b = event.img;
            int id = event.id;

            Hero hero = new Hero(firstLetter, name, sex, place, birth_year, death_year, power, hevent, b,id);
            total++;
            nameList.add(name);
            heros.add(hero);
            herosTarget.clear();
            herosTarget.addAll(heros);
            herosNum++;
            herosIndexAll.add(herosNum);
            herosIndexShow.clear();
            herosIndexShow.addAll(herosIndexAll);
            CommonFragment commonFragment = new CommonFragment();
            commonFragment.bindData(hero);
            fragments.add(commonFragment);
            initFragmentsShow();
            initIndicatorTv();
        }
        if (op_tag == 2) {
            int pos = -1;
            int id = event.id;
            for (int i = 0;i < heros.size();i++) {
                if (heros.get(i).getId() == id) {
                    pos = i;
                }
            }
            if (pos != -1) {
                nameList.remove(pos);
                heros.remove(pos);
                herosTarget.clear();
                herosTarget.addAll(heros);
                herosIndexAll.remove(pos);
                herosIndexShow.clear();
                herosIndexShow.addAll(herosIndexAll);
                initFragmentsShow();
                initIndicatorTv();
            }
        }
        if (op_tag == 3) {
            int pos = -1;
            int id = event.id;
            for (int i = 0;i < heros.size();i++) {
                if (heros.get(i).getId() == id) {
                    pos = i;
                }
            }
            if (pos != -1) {
                nameList.remove(pos);
                heros.remove(pos);
                herosIndexAll.remove(pos);

                String firstLetter = event.firstLetter;
                String new_name = event.new_name;
                String power = event.power;
                String sex = event.sex;
                String birth_year = event.birth_year;
                String death_year = event.death_year;
                String place = event.place;
                String hevent = event.event;

                String b = event.img;
                Hero hero = new Hero(firstLetter, new_name, sex, place, birth_year, death_year, power, hevent, b,id);
                nameList.add(new_name);
                heros.add(hero);
                herosTarget.clear();
                herosTarget.addAll(heros);
                herosNum++;
                herosIndexAll.add(herosNum);
                herosIndexShow.clear();
                herosIndexShow.addAll(herosIndexAll);
                CommonFragment commonFragment = new CommonFragment();
                commonFragment.bindData(hero);
                fragments.add(commonFragment);
                initFragmentsShow();
                initIndicatorTv();
            }
        }
    }

    //back键不退出应用，返回手机桌面
    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addCategory("android.intent.category.MONKEY");
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Intent intent = new Intent(MainActivity.this, MusicService.class);
        stopService(intent);
    }

    // 调整沉浸式菜单的title
    private void dealStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = getStatusBarHeight();
            ViewGroup.LayoutParams lp = positionView.getLayoutParams();
            lp.height = statusBarHeight;
            positionView.setLayoutParams(lp);
        }

        // 弹出左边栏后，使下层布局不能接收响应
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                drawerView.setClickable(true);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
            }
            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        searchToGetLeftLayout = (View) findViewById(R.id.search_to_get_left_layout);
        searchToGetLeftLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        editNewHero = (View) findViewById(R.id.edit_new_hero);
    }

    private int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    private void initViewID() {
        indicatorTv = (TextView) findViewById(R.id.indicator_tv);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        name = (TextView) findViewById(R.id.name);
        searchButton = (Button) findViewById(R.id.searchButton);
        resetButton = (Button) findViewById(R.id.resetButton);
        play = (Button) findViewById(R.id.play);
        pause = (Button) findViewById(R.id.pause);
    }

    private void initData() {
        herosNum = 9;
        total = 9;

        for (int i = 0; i < 10; i++) {
            nameList.add(hero_name[i]);
        }
        //heros_events
        String cao_cao_e = "曹操是西园八校尉之一，曾只身行刺董卓，失败后和袁绍共同联合天下诸侯讨伐董卓，后独自发展自身势力，一生中先后战胜了袁术、吕布、张绣、袁绍、刘表、张鲁、马超等割据势力，统一了北方。但是在南下讨伐江东的战役中，曹操在赤壁惨败。后来在和蜀汉的汉中争夺战中，曹操再次无功而返。曹操一生未称帝，他病死后，曹丕继位后不久称帝，追封曹操为魏武皇帝。";
        String cao_pi_e = "在争夺继承权问题上处心积虑，战胜了文才更胜一筹的弟弟曹植，被立为王世子。曹操逝世后，曹丕继位成为魏王，以不参加葬礼之罪逼弟弟曹植写下七步诗，险些将其杀害，又顺利夺下弟弟曹彰的兵权，坐稳了魏王之位。不久，曹丕逼汉献帝让位，代汉称帝，为魏国开国皇帝。刘备伐吴时，曹丕看出刘备要失败，但不听谋士之言，偏要坐山观虎斗，事后又起兵伐吴，结果被徐盛火攻击败。回洛阳后，曹丕大病，临终前托付曹睿给曹真、司马懿等人，终年四十岁。";
        String diao_chan_e = "舍身报国的可敬女子，她为了挽救天下黎民，为了推翻权臣董卓的荒淫统治，受王允所托，上演了可歌可泣的连环计（连环美人计），周旋于两个男人之间，成功的离间了董卓和吕布，最终吕布将董卓杀死，结束了董卓专权的黑暗时期。";
        String dong_zhuo_e = "原为汉河东太守，征讨黄巾不利，却因贿赂十常侍官至西凉刺史。董卓应大将军何进之邀进京铲除宦官，恰逢何进被杀，于是收编何进部曲，又赠赤兔马给吕布，诱其投降并杀死荆州刺史丁原，从此不可一世。董卓废汉少帝，立汉献帝，朝臣多敢怒不敢言；董卓生性残暴，经常纵兵略民，百姓皆恨。后来，司徒王允欲除掉董卓，将美女貂禅许给吕布，又献予董卓，使二人反目，遂与吕布合谋杀死了董卓。";
        String guan_yu_e = "因本处势豪倚势凌人，关羽杀之而逃难江湖。闻涿县招军破贼，特来应募。与刘备、张飞桃园结义，羽居其次。使八十二斤青龙偃月刀随刘备东征西讨。虎牢关温酒斩华雄，屯土山降汉不降曹。为报恩斩颜良、诛文丑，解曹操白马之围。后得知刘备音信，过五关斩六将，千里寻兄。刘备平定益州后，封关羽为五虎大将之首，督荆州事。羽起军攻曹，放水淹七军，威震华夏。围樊城右臂中箭，幸得华佗医治，刮骨疗伤。但未曾提防东吴袭荆州，关羽父子败走麦城，突围中被捕，不屈遭害。\n";
        String liu_bei_e = "刘备，蜀汉的开国皇帝，汉景帝之子中山靖王刘胜的后代。刘备少年孤贫，以贩鞋织草席为生。黄巾起义时，刘备与关羽、张飞桃园结义，成为异姓兄弟，一同剿除黄巾，有功，任安喜县尉，不久辞官；董卓乱政之际，刘备随公孙瓒讨伐董卓，三人在虎牢关战败吕布。后诸侯割据，刘备势力弱小，经常寄人篱下，先后投靠过公孙瓒、曹操、袁绍、刘表等人，几经波折，却仍无自己的地盘。赤壁之战前夕，刘备在荆州三顾茅庐，请诸葛亮出山辅助，在赤壁之战中，联合孙权打败曹操，奠定了三分天下的基础。刘备在诸葛亮的帮助下占领荆州，不久又进兵益州，夺取汉中，建立了横跨荆益两州的政权。后关羽战死，荆州被孙权夺取，刘备大怒，于称帝后伐吴，在夷陵之战中为陆逊用火攻打得大败，不久病逝于白帝城，临终托孤于诸葛亮。";
        String sun_quan_e = "孙权19岁就继承了其兄孙策之位，力据江东，击败了黄祖。后东吴联合刘备，在赤壁大战击溃了曹操军。东吴后来又和曹操军在合肥附近鏖战，并从刘备手中夺回荆州、杀死关羽、大破刘备的讨伐军。曹丕称帝后孙权先向北方称臣，后自己建吴称帝，迁都建业。";
        String da_qiao_e = "江东乔国老有二女，大乔和小乔。大乔有沉鱼落雁之资，倾国倾城之容。孙策征讨江东，攻取皖城，娶大乔为妻。自古美女配英雄，伯符大乔堪绝配。曹操赤壁鏖兵，虎视江东，曾有揽二乔娱暮年，还足平生之愿。";
        String zhou_yu_e = "偏将军、南郡太守。自幼与孙策交好，策离袁术讨江东，瑜引兵从之。为中郎将，孙策相待甚厚，又同娶二乔。策临终，嘱弟权曰：“外事不决，可问周瑜”。瑜奔丧还吴，与张昭共佐权，并荐鲁肃等，掌军政大事。赤壁战前，瑜自鄱阳归。力主战曹，后于群英会戏蒋干、怒打黄盖行诈降计、后火烧曹军，大败之。后下南郡与曹仁相持，中箭负伤，与诸葛亮较智斗，定假涂灭虢等计，皆为亮破，后气死于巴陵，年三十六岁。临终，上书荐鲁肃代其位，权为其素服吊丧。";
        String zhu_ge_liang_e = "人称卧龙先生，有经天纬地之才，鬼神不测之机。刘皇叔三顾茅庐，遂允出山相助。曾舌战群儒、借东风、智算华容、三气周瑜，辅佐刘备于赤壁之战大败曹操，更取得荆州为基本。后奉命率军入川，于定军山智激老黄忠，斩杀夏侯渊，败走曹操，夺取汉中。刘备伐吴失败，受遗诏托孤，安居平五路，七纵平蛮，六出祁山，鞠躬尽瘁，死而后已。其手摇羽扇，运筹帷幄的潇洒形象，千百年来已成为人们心中“智慧”的代名词。";

        // init heros
        heros.add(new Hero("C","曹操","男","豫州","155","220","魏",cao_cao_e,"assets://曹操.jpg",0));
        heros.add(new Hero("C","曹丕","男","豫州","187","226","魏",cao_pi_e,"assets://曹丕.jpg",1));
        heros.add(new Hero("D","貂蝉","女","不详","不详","不详","群",diao_chan_e,"assets://貂蝉.jpg",2));
        heros.add(new Hero("D","董卓","男","凉州","不详","192","群",dong_zhuo_e,"assets://董卓.jpg",3));
        heros.add(new Hero("G","关羽","男","司隶","不详","219","蜀",guan_yu_e,"assets://关羽.jpg",4));
        heros.add(new Hero("L","刘备","男","幽州","161","223","蜀",liu_bei_e,"assets://刘备.jpg",5));
        heros.add(new Hero("S","孙权","男","扬州","182","252","吴",sun_quan_e,"assets://孙权.jpg",6));
        heros.add(new Hero("X","大乔","女","扬州","不详","不详","吴",da_qiao_e,"assets://大乔.jpg",7));
        heros.add(new Hero("Z","周瑜","男","扬州","175","210","吴",zhou_yu_e,"assets://周瑜.jpg",8));
        heros.add(new Hero("Z","诸葛亮","男","徐州","181","224","蜀",zhu_ge_liang_e,"assets://诸葛亮.jpg",9));

        // 更新名字字体
        Typeface typeface = Typeface.createFromAsset(getAssets(),"font.ttf");
        name.setTypeface(typeface);
        // 初始化所有horos编号
        herosTarget.addAll(heros);
        herosIndexAll = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            herosIndexAll.add(i);
        }
        herosIndexShow = new ArrayList<>();
        herosIndexShow.addAll(herosIndexAll);
    }

    // 填充ViewPager
    private void fillViewPager() {
        // 1. viewPager添加parallax效果，使用PageTransformer就足够了
        viewPager.setPageTransformer(false, new CustPagerTransformer(this));

        // 2. viewPager添加adapter
        for (int i = 0; i < heros.size(); i++) {
            // 预先准备10个fragment
            CommonFragment commonFragment = new CommonFragment();
            commonFragment.bindData(heros.get(i));
            fragments.add(commonFragment);
        }
        fragmentShow.addAll(fragments);
        fspAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                // 得到10个fragment中的某一个，后面的位置循环重复放同样的fragment
                if (fragmentShow.size() != 0){
                    CommonFragment fragment = fragmentShow.get(position % herosIndexAll.size());
                    return fragment;
                } else return (new CommonFragment());
            }
            @Override
            public int getCount() {
                return herosIndexAll.size();
            }
        };
        viewPager.setAdapter(fspAdapter);
        // 3. viewPager滑动时，调整指示器以及顶部状态栏的名字显示
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updateIndicatorTv();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 初始化指示器
        initIndicatorTv();
    }

    // 初始化指示器
    private void initIndicatorTv() {
        if (herosIndexShow.size() != 0) {
            currentItem = 1;
            totalNum = viewPager.getAdapter().getCount();
            indicatorTv.setText(Html.fromHtml("<font color='#ffffff'>" + currentItem + "</font>  /  " + totalNum));

            String hero_name = herosTarget.get(viewPager.getCurrentItem()).getName();
            name.setText(hero_name);
        } else {
            indicatorTv.setText(Html.fromHtml("<font color='#ffffff'>" + 0 + "</font>  /  " + 0));
            name.setText(" ");
        }
    }
    //  更新指示器
    private void updateIndicatorTv() {
        currentItem = viewPager.getCurrentItem() + 1;
        totalNum = viewPager.getAdapter().getCount();
        indicatorTv.setText(Html.fromHtml("<font color='#ffffff'>" + currentItem + "</font>  /  " + totalNum));

        // 更改顶部状态栏的名字显示，虽然不太优雅，不过省事儿嘛
        String hero_name = herosTarget.get(viewPager.getCurrentItem()).getName();
        name.setText(hero_name);
    }

    @SuppressWarnings("deprecation")
    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this)
                .memoryCacheExtraOptions(480, 800)
                // default = device screen dimensions
                .threadPoolSize(3)
                // default
                .threadPriority(Thread.NORM_PRIORITY - 1)
                // default
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13) // default
                .discCacheSize(50 * 1024 * 1024) // 缓冲大小
                .discCacheFileCount(100) // 缓冲文件数目
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(this)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs().build();

        // 2.单例ImageLoader类的初始化
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    // 初始化搜索界面数据
    private void initSearchData() {
        // put String into List
        firstLetterList = new ArrayList<>();
        for (int i = 0; i < firstLetterItem.length; i++) {
            firstLetterList.add(firstLetterItem[i]);
        }
        hero_powerItemList = new ArrayList<>();
        for (int i = 0; i < hero_powerItem.length; i++) {
            hero_powerItemList.add(hero_powerItem[i]);
        }
        hero_placeItemList = new ArrayList<>();
        for (int i = 0; i < hero_placeItem.length; i++) {
            hero_placeItemList.add(hero_placeItem[i]);
        }
        hero_yearItemList = new ArrayList<>();
        for (int i = 0; i < hero_yearItem.length; i++) {
            hero_yearItemList.add(hero_yearItem[i]);
        }
        hero_sexItemList = new ArrayList<>();
        for (int i = 0; i < hero_sexItem.length; i++) {
            hero_sexItemList.add(hero_sexItem[i]);
        }
    }

    // 初始化搜索界面视图
    private void initSearchView() {
        // 初始化Spinner
        firstLetterSpinner = (Spinner) findViewById(R.id.firstLetter);
        hero_powerItemSpinner = (Spinner) findViewById(R.id.hero_power);
        hero_placeItemSpinner = (Spinner) findViewById(R.id.hero_place);
        hero_yearItemSpinner = (Spinner) findViewById(R.id.hero_year);
        hero_sexItemSpinner = (Spinner) findViewById(R.id.hero_sex);

        // 初始化Spinner Adapter
        firstLetterAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, firstLetterItem);
        firstLetterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        firstLetterSpinner.setAdapter(firstLetterAdapter);

        hero_powerItemAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, hero_powerItemList);
        hero_powerItemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hero_powerItemSpinner.setAdapter(hero_powerItemAdapter);

        hero_placeItemAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, hero_placeItemList);
        hero_placeItemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hero_placeItemSpinner.setAdapter(hero_placeItemAdapter);

        hero_yearItemAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, hero_yearItemList);
        hero_yearItemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hero_yearItemSpinner.setAdapter(hero_yearItemAdapter);

        hero_sexItemAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, hero_sexItemList);
        hero_sexItemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hero_sexItemSpinner.setAdapter(hero_sexItemAdapter);

        // 自动提示框
        autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        autoCompleteTextViewAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, nameList);
        autoCompleteTextView.setAdapter(autoCompleteTextViewAdapter);
    }

    // 初始化搜索按钮逻辑
    private void initSearchButton() {
        // 搜索按钮
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // init target heros every searching
                // 初始状态下所有heros都是target，通过属性进行不断筛选，删除herosTarget里面的heros
                herosTarget.clear();
                herosTarget.addAll(heros);

                herosIndexShow.clear();
                herosIndexShow.addAll(herosIndexAll);

                // 获取最终确定的搜索条件
                firstLetterTarget =  firstLetterSpinner.getSelectedItem().toString();
                hero_powerTarget = hero_powerItemSpinner.getSelectedItem().toString();
                hero_placeTarget = hero_placeItemSpinner.getSelectedItem().toString();
                hero_yearTarget = hero_yearItemSpinner.getSelectedItem().toString();
                hero_sexTarget = hero_sexItemSpinner.getSelectedItem().toString();
                hero_nameTarget = autoCompleteTextView.getText().toString();

                // 点击Search按钮后，左边栏缩回
                final DrawerLayout drawLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                drawLayout.closeDrawer(Gravity.LEFT);

                if (!firstLetterTarget.equals("不限")) {
                    for (int i = 0; i < herosTarget.size(); i++) {
                        if (!firstLetterTarget.equals(herosTarget.get(i).getFirstLetter())) {
                            herosTarget.remove(i);
                            herosIndexShow.remove(i);
                            i--;
                        }
                    }
                }
                if (!hero_powerTarget.equals("不限")) {
                    for (int i = 0; i < herosTarget.size(); i++) {
                        if (!hero_powerTarget.equals(herosTarget.get(i).getPower())) {
                            herosTarget.remove(i);
                            herosIndexShow.remove(i);
                            i--;
                        }
                    }
                }
                if (!hero_placeTarget.equals("不限")) {
                    for (int i = 0; i < herosTarget.size(); i++) {
                        if (!hero_placeTarget.equals(herosTarget.get(i).getPlace())) {
                            herosTarget.remove(i);
                            herosIndexShow.remove(i);
                            i--;
                        }
                    }
                }
                if (!hero_yearTarget.equals("不限")) {
                    for (int i = 0; i < herosTarget.size(); i++) {
                        int yearTarget = Integer.parseInt(hero_yearTarget.substring(0,3));
                        // 仅死亡年份不详
                        if (!herosTarget.get(i).getBirth_year().equals("不详")&& herosTarget.get(i).getDeath_year().equals("不详")) {
                            int birthYear = Integer.parseInt(herosTarget.get(i).getBirth_year());
                            if (yearTarget < birthYear) {
                                herosTarget.remove(i);
                                herosIndexShow.remove(i);
                                i--;
                            }
                        }
                        // 仅出生年份不详
                        if (herosTarget.get(i).getBirth_year().equals("不详")&& !herosTarget.get(i).getDeath_year().equals("不详")) {
                            int deathYear = Integer.parseInt(herosTarget.get(i).getDeath_year());
                            if (yearTarget > deathYear) {
                                herosTarget.remove(i);
                                herosIndexShow.remove(i);
                                i--;
                            }
                        }
                        // 出生和死亡年份都不详
                        if (herosTarget.get(i).getBirth_year().equals("不详") && herosTarget.get(i).getDeath_year().equals("不详")) {

                        }
                        // 出生和死亡年份都详细
                        if (!(herosTarget.get(i).getBirth_year().equals("不详") || herosTarget.get(i).getDeath_year().equals("不详"))) {
                            int birthYear = Integer.parseInt(herosTarget.get(i).getBirth_year());
                            int deathYear = Integer.parseInt(herosTarget.get(i).getDeath_year());
                            if (yearTarget > deathYear || yearTarget < birthYear) {
                                herosTarget.remove(i);
                                herosIndexShow.remove(i);
                                i--;
                            }
                        }
                    }
                }
                if (!hero_sexTarget.equals("不限")) {
                    for (int i = 0; i < herosTarget.size(); i++) {
                        if (!hero_sexTarget.equals(herosTarget.get(i).getSex())) {
                            herosTarget.remove(i);
                            herosIndexShow.remove(i);
                            i--;
                        }
                    }
                }
                if (!hero_nameTarget.equals("")) {
                    for (int i = 0; i < herosTarget.size(); i++) {
                        if (!hero_nameTarget.equals(herosTarget.get(i).getName())) {
                            herosTarget.remove(i);
                            herosIndexShow.remove(i);
                            i--;
                        }
                    }
                }

                if (herosTarget.size() == 0) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("查询结果")
                            .setMessage("很遗憾，未找到您搜索的内容，请换个条件再试试n(*≧▽≦*)n")
                            .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // 点击关闭后弹出左对话框
                                    drawLayout.openDrawer(Gravity.LEFT);
                                }
                            })
                            .show();
                }
                // 更新显示的fragments
                initFragmentsShow();
                // 初始化底部数字
                initIndicatorTv();
            }
        });

        // 重置按钮
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                herosTarget.clear();
                herosTarget.addAll(heros);

                herosIndexShow.clear();
                herosIndexShow.addAll(herosIndexAll);
                // 重置搜索条件
                firstLetterSpinner.setSelection(0);
                hero_powerItemSpinner.setSelection(0);
                hero_placeItemSpinner.setSelection(0);
                hero_yearItemSpinner.setSelection(0);
                hero_sexItemSpinner.setSelection(0);
                autoCompleteTextView.setText("");

                // 更新显示的fragments
                initFragmentsShow();
                // 初始化底部数字
                initIndicatorTv();
            }
        });
    }

    // 背景音乐点击事件(播放和停止）
    private void initPlayButton() {
        Intent intent = new Intent(MainActivity.this, MusicService.class);
        startService(intent);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MusicService.class);
                startService(intent);
                play.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MusicService.class);
                stopService(intent);
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.INVISIBLE);
            }
        });
    }

    // 更新fragments的显示
    private void initFragmentsShow() {
        fragmentShow.clear();
        for (int i = 0; i < herosIndexShow.size(); i++) {
            int temp = herosIndexShow.get(i);
            fragmentShow.add(fragments.get(temp));
        }
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                // 得到10个fragment中的某一个，后面的位置循环重复放同样的fragment
                if (!fragmentShow.isEmpty()){
                    CommonFragment commonFragment = fragmentShow.get(position % herosIndexShow.size());
                    return commonFragment;
                } else return (new CommonFragment());
            }
            @Override
            public int getCount() {
                return herosIndexShow.size();
            }
        });
    }
}
//TODO 修改搜索结果的Toast
//TODO 从别的页跳转回来的逻辑要改，翻到哪一页是个问题
//TODO 要不要加上翻页的封面