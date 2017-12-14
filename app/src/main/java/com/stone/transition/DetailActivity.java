package com.stone.transition;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;

/**
 * Created by xmuSistone on 2016/9/19.
 */
public class DetailActivity extends FragmentActivity {


    private ImageView hero_img;
    private TextView hero_name;
    private TextView hero_power;
    private TextView hero_sex;
    private TextView birth_year;
    private TextView death_year;
    private TextView hero_place;
    private TextView hero_event;
    private LinearLayout editView;
    private LinearLayout detailView;
    private EditText name;
    private RadioGroup radioGroup;
    private Spinner birth_spinner;
    private Spinner death_spinner;
    private Spinner place_spinner;
    private Spinner power_spinner;
    private EditText event;
    private ImageButton confirm_button;
    private ImageButton cancel_button;
    //private Button delete_button;
    //private Button edit_button;
    private View back_button;
    private int editrequest;
    private String origin_name;
    private String origin_sex;
    private String origin_power;
    private String origin_birth;
    private String origin_death;
    private String origin_event;
    private String origin_place;
    private String origin_letter;
    private Bitmap origin_img;
    private String imgpath;
    private int total;
    private int hero_id;
    private View menuView;
    private PopupMenu popup;

    private final int PHOTO_REQUEST = 1;

    String[] hero_yearItem = new String[]{"不详", "184年", "185年", "186年", "187年", "188年", "189年", "190年",
            "191年", "192年", "193年", "194年", "195年", "196年", "197年", "198年", "199年", "200年",
            "201年", "202年", "203年", "204年", "205年", "206年", "207年", "208年", "209年", "210年",
            "211年", "212年", "213年", "214年", "215年", "216年", "217年", "218年", "219年", "220年",
            "221年", "222年", "223年", "224年", "225年", "226年", "227年", "228年", "229年", "230年",
            "231年", "232年", "233年", "234年", "235年", "236年", "237年", "238年", "239年", "240年",
            "241年", "242年", "243年", "244年", "245年", "246年", "247年", "248年", "249年", "250年",
            "251年", "252年", "253年", "254年", "255年", "256年", "257年", "258年", "259年", "260年",
            "261年", "262年", "263年", "264年", "265年", "266年", "267年", "268年", "269年", "270年",
            "271年", "272年", "273年", "274年", "275年", "276年", "277年", "278年", "279年", "280年"};
    String[] hero_placeItem = new String[]{"并州", "冀州", "交州", "荆州", "凉州", "青州", "司隶",
            "徐州", "兖州", "扬州", "益州", "幽州", "豫州","不详"};
    String[] hero_powerItem = new String[]{"群","魏","蜀","吴"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        editView = (LinearLayout)findViewById(R.id.editView);
        detailView = (LinearLayout)findViewById(R.id.detailView);

        menuView = (View) findViewById(R.id.call_menu_out);
        //详情显示界面
        hero_img = (ImageView)findViewById(R.id.hero_img);   //头像
        hero_name = (TextView)findViewById(R.id.hero_name);      //名字
        Typeface typeface = Typeface.createFromAsset(getAssets(),"font.ttf");
        hero_name.setTypeface(typeface);
        hero_power = (TextView)findViewById(R.id.hero_power);   //势力
        hero_sex = (TextView)findViewById(R.id.hero_sex);       //性别
        hero_place = (TextView)findViewById(R.id.hero_place);   //籍贯
        hero_event = (TextView)findViewById(R.id.hero_event);   //事迹
        birth_year = (TextView)findViewById(R.id.birth_year);   //生年
        death_year = (TextView)findViewById(R.id.death_year);   //死年
        //delete_button = (Button)findViewById(R.id.delete);
        //edit_button = (Button)findViewById(R.id.edit);
        back_button = (View) findViewById(R.id.back);

        //新建界面
        name = (EditText)findViewById(R.id.name);            //编辑名字
        radioGroup = (RadioGroup)findViewById(R.id.sexSelect);
        birth_spinner = (Spinner)findViewById(R.id.birth_spinner);
        death_spinner = (Spinner)findViewById(R.id.death_spinner);
        place_spinner = (Spinner)findViewById(R.id.place_spinner);
        power_spinner = (Spinner)findViewById(R.id.power_spinner);
        event = (EditText)findViewById(R.id.edit_event);
        confirm_button = (ImageButton) findViewById(R.id.confirm);
        cancel_button = (ImageButton) findViewById(R.id.cancel);
        editrequest = 0;  //当前没有编辑请求

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


        //intent接收从主界面传送的数据
        Intent it = getIntent();
        int edit_state = it.getIntExtra("edit",0);
        int detail_state = it.getIntExtra("detail",1);

        //新建界面
        if (edit_state == 1 && detail_state == 0) {
            total = it.getIntExtra("heroNum",0);
            detailView.setVisibility(View.GONE);
            editView.setVisibility(View.VISIBLE);
            //人物头像和姓名、势力都要设置为默认值
            name.setHint("请输入姓名");
            hero_img.setFocusable(true);
            hero_img.setFocusableInTouchMode(true);

            menuView.setVisibility(View.INVISIBLE);
        }
        //详情界面
        if (detail_state == 1 && edit_state == 0){
            detailView.setVisibility(View.VISIBLE);
            editView.setVisibility(View.GONE);

            //获得intent信息
            origin_name = it.getStringExtra("name");
            origin_letter = origin_name.substring(0,1).toUpperCase();
            origin_power = it.getStringExtra("power");
            origin_sex = it.getStringExtra("sex");
            origin_birth = it.getStringExtra("birth_year");
            origin_death = it.getStringExtra("death_year");
            origin_place = it.getStringExtra("place");
            origin_event = it.getStringExtra("event");
            hero_id = it.getIntExtra("id",0);

            //设置人物信息显示
            hero_name.setText(origin_name);
            hero_power.setText(origin_power);
            hero_sex.setText(origin_sex);
            birth_year.setText(origin_birth);
            death_year.setText(origin_death);
            hero_place.setText(origin_place);
            hero_event.setText(origin_event);

            String imgPath = it.getStringExtra("profile");
            ImageLoader.getInstance().displayImage(imgPath, hero_img);
            imgpath = it.getStringExtra("profile");

            hero_img.setFocusable(false);
            hero_img.setFocusableInTouchMode(false);

            String name = hero_name.getText().toString();
            if (TextUtils.equals(name, "曹操") || TextUtils.equals(name, "曹丕") || TextUtils.equals(name, "貂蝉")
                    || TextUtils.equals(name, "董卓") || TextUtils.equals(name, "关羽") || TextUtils.equals(name, "刘备")
                    || TextUtils.equals(name, "孙权") || TextUtils.equals(name, "大乔") || TextUtils.equals(name, "周瑜")
                    || TextUtils.equals(name, "诸葛亮")) {
                String Name = hero_name.getText().toString();
                Intent intent1 = new Intent(DetailActivity.this, DubService.class);
                intent1.putExtra("name", Name);
                startService(intent1);
            }
        }


        //点击事件调用
        profileClick();
        birthClick();
        deathClick();
        placeClick();
        powerClick();
        buttonClick();

        menuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                popup = new PopupMenu(DetailActivity.this, menuView);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                menuView.setVisibility(View.INVISIBLE);
                                detailView.setVisibility(View.GONE);
                                editView.setVisibility(View.VISIBLE);
                                editrequest = 1;
                                hero_img.setFocusable(true);
                                hero_img.setFocusableInTouchMode(true);
                                //选项设置为人物信息值
                                event.setText(origin_event);
                                for (int i = 0; i < radioGroup.getChildCount(); i++) {
                                    RadioButton rb = (RadioButton)radioGroup.getChildAt(i);
                                    if (rb.getText().toString().equals(origin_sex)) {
                                        rb.setChecked(true);
                                        break;
                                    }
                                }
                                //年份跨度不够大
                                setSpinnerSelected(birth_spinner,"公元"+origin_birth+"年");
                                setSpinnerSelected(death_spinner,"公元"+origin_death+"年");

                                setSpinnerSelected(power_spinner,origin_power);
                                setSpinnerSelected(place_spinner,origin_place);
                                break;
                            case R.id.delete:
                                final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(DetailActivity.this);
                                alertDialog1.setTitle("删除此项").setMessage("确认删除这一人物数据？");
                                alertDialog1.setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //跳转回主界面并从主界面删除
                                        EventBus.getDefault().post(new MessageEvent(2,hero_name.getText().toString(),"","","","","","","","","",hero_id));
                                        Intent it = new Intent();
                                        it.setClass(DetailActivity.this, MainActivity.class);
                                        startActivity(it);
                                    }
                                });
                                alertDialog1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                                alertDialog1.create().show();
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
                popup.show(); //showing popup menu
            }
        });
        //TODO show the popup icons
        /*try {
            Field field = popup.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            MenuPopupHelper mHelper = (MenuPopupHelper) field.get(popup);
            mHelper.setForceShowIcon(true);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent1 = new Intent(DetailActivity.this, DubService.class);
        stopService(intent1);
    }

    //头像点击事件
    public void profileClick() {
        //点击头像
        hero_img.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PHOTO_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                imgpath = data.getData().toString();
                ImageLoader.getInstance().displayImage(imgpath, hero_img);
            }
        }
    }

    //出生点击事件
    public void birthClick() {
        ArrayAdapter<String> birthAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, hero_yearItem);
        birthAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        birth_spinner.setAdapter(birthAdapter);
    }

    //死亡点击事件
    public void deathClick() {
        ArrayAdapter<String> deathAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, hero_yearItem);
        deathAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        death_spinner.setAdapter(deathAdapter);
    }

    //籍贯点击事件
    public void placeClick() {
        ArrayAdapter<String> placeAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, hero_placeItem);
        placeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        place_spinner.setAdapter(placeAdapter);

    }

    //势力点击事件
    public void powerClick() {
        ArrayAdapter<String> powerAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, hero_powerItem);
        powerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        power_spinner.setAdapter(powerAdapter);
    }



    public void buttonClick() {
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editrequest == 0) {
                    String NAME = name.getText().toString();
                    String EVENT = event.getText().toString();
                    if (TextUtils.isEmpty(NAME)) {
                        String message = "请输入姓名♪(･ω･)ﾉ";
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                    else if (TextUtils.isEmpty(EVENT)) {
                        String message = "请输入事迹♪(･ω･)ﾉ";
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String letter = NAME.substring(0, 1).toUpperCase();
                        String sex = "";
                        for (int i = 0; i < radioGroup.getChildCount(); i++) {
                            RadioButton rb = (RadioButton) radioGroup.getChildAt(i);
                            if (rb.isChecked()) {
                                sex = rb.getText().toString();
                                break;
                            }
                        }
                        String place = place_spinner.getSelectedItem().toString();
                        String birth_year = birth_spinner.getSelectedItem().toString();
                        String death_year = death_spinner.getSelectedItem().toString();
                        String power = power_spinner.getSelectedItem().toString();


                        //向主界面传递数据
                        //图片传递

                        //跳转到主界面
                        EventBus.getDefault().post(new MessageEvent(1, NAME, "", letter, power, sex, birth_year, death_year, place, EVENT, imgpath, total));
                        Intent it = new Intent();
                        it.setClass(DetailActivity.this, MainActivity.class);
                        startActivity(it);
                    }
                } else if (editrequest == 1) {
                    //保存修改的数据，传到主界面
                    String NAME = name.getText().toString();
                    if (TextUtils.isEmpty(NAME)) {
                        String message = "请输入姓名♪(･ω･)ﾉ";
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    } else {
                        String EVENT = event.getText().toString();
                        String letter = NAME.substring(0, 1).toUpperCase();
                        String sex = "";
                        for (int i = 0; i < radioGroup.getChildCount(); i++) {
                            RadioButton rb = (RadioButton)radioGroup.getChildAt(i);
                            if (rb.isChecked()) {
                                sex = rb.getText().toString();
                                break;
                            }
                        }
                        String place = place_spinner.getSelectedItem().toString();
                        String birth_year = birth_spinner.getSelectedItem().toString();
                        String death_year = death_spinner.getSelectedItem().toString();
                        String power = power_spinner.getSelectedItem().toString();


                        //跳转到主界面
                        editrequest = 0;
                        EventBus.getDefault().post(new MessageEvent(3,origin_name,NAME,letter,power,sex,birth_year,death_year,place,EVENT,imgpath,hero_id));
                        Intent it = new Intent();
                        it.setClass(DetailActivity.this, MainActivity.class);
                        startActivity(it);
                    }
                }
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                it.setClass(DetailActivity.this, MainActivity.class);
                startActivity(it);
            }
        });
    }

    public void setSpinnerSelected(Spinner spinner,String value){
        SpinnerAdapter sa = spinner.getAdapter();
        for (int i = 0;i<sa.getCount();i++){
            if (value.equals(sa.getItem(i).toString())){
                spinner.setSelection(i);
                break;
            }
        }
    }
}
//TODO hind the popup button when editing
//TODO name edittext logic
//TODO edit 默认头像
//TODO logic bug: what if I set a name already exit
