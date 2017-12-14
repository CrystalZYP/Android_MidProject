package com.stone.transition;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.text.TextUtils;

public class DubService extends Service {
    private MediaPlayer mediaPlayer;

    public DubService() {}

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        String Name = intent.getStringExtra("name");
        if (TextUtils.equals(Name, "曹操")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.cao_cao);
        }
        else if (TextUtils.equals(Name, "曹丕")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.cao_pi);
        }
        else if (TextUtils.equals(Name, "貂蝉")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.diao_chan);
        }
        else if (TextUtils.equals(Name, "董卓")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.dong_zhuo);
        }
        else if (TextUtils.equals(Name, "关羽")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.guan_yu);
        }
        else if (TextUtils.equals(Name, "刘备")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.liu_bei);
        }
        else if (TextUtils.equals(Name, "孙权")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.sun_quan);
        }
        else if (TextUtils.equals(Name, "大乔")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.da_qiao);
        }
        else if (TextUtils.equals(Name, "周瑜")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.zhou_yu);
        }
        else if (TextUtils.equals(Name, "诸葛亮")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.zhu_ge_liang);
        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.start();
        mediaPlayer.setVolume(0.3f, 0.3f);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

}