package com.stone.transition;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by xmuSistone on 2016/9/18.
 */
public class CommonFragment extends Fragment implements DragLayout.GotoDetailListener {
    private ImageView imageView; // 人物图片
    private View address1, address2, gotodetail; // 隐藏起来了
    private TextView hero_place, birth_year, death_year, hero_power; // 籍贯、生、卒、势力
    private String place, birth, death, power, imageUrl;
    private Hero theHero;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_common, null);
        DragLayout dragLayout = (DragLayout) rootView.findViewById(R.id.drag_layout);
        imageView = (ImageView) dragLayout.findViewById(R.id.image);
        ImageLoader.getInstance().displayImage(imageUrl, imageView);
        Log.d("myTag", "image loaded");

        address1 = dragLayout.findViewById(R.id.address1);
        address2 = dragLayout.findViewById(R.id.address2);
        gotodetail = dragLayout.findViewById(R.id.gotodetail);
        hero_place = (TextView) dragLayout.findViewById(R.id.hero_place);
        birth_year = (TextView) dragLayout.findViewById(R.id.birth_year);
        death_year = (TextView) dragLayout.findViewById(R.id.death_year);
        hero_power = (TextView) dragLayout.findViewById(R.id.hero_power);

        hero_place.setText(place);
        birth_year.setText(birth);
        death_year.setText(death);
        hero_power.setText(power);

        gotodetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDetail();
            }
        });
        dragLayout.setGotoDetailListener(this);
        return rootView;
    }

    @Override
    public void gotoDetail() {
        Intent intent = new Intent();
        Activity activity = (Activity) getContext();
        intent.setClass(activity, DetailActivity.class);

        //数据不够
        intent.putExtra("edit",0);
        intent.putExtra("detail",1);
        intent.putExtra("name", theHero.getName());
        intent.putExtra("power", theHero.getPower());
        intent.putExtra("birth_year", theHero.getBirth_year());
        intent.putExtra("death_year", theHero.getDeath_year());
        intent.putExtra("place", theHero.getPlace());
        intent.putExtra("sex", theHero.getSex());
        intent.putExtra("event", theHero.getEvent());
        intent.putExtra("profile", imageUrl);
        intent.putExtra("id",theHero.getId());

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    public void bindData(Hero h) {
        theHero = h;
        place = "籍贯： "+h.getPlace();
        birth = "生： "+h.getBirth_year();
        death = "卒： "+h.getDeath_year();
        power = h.getPower();
        this.imageUrl = h.getProfile();
    }
}
