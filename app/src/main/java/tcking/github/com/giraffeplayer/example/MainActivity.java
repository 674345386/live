package tcking.github.com.giraffeplayer.example;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import tcking.github.com.giraffeplayer.GiraffePlayer;
import tcking.github.com.giraffeplayer.GiraffePlayerActivity;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class MainActivity extends AppCompatActivity {
    GiraffePlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏，继承AppCompatActivity，所以无效，用设置NOactionBar theme
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉状态栏
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        player = new GiraffePlayer(this);
        player.onComplete(new Runnable() {
            @Override
            public void run() {
                //视频播放完成的时候回调
                Toast.makeText(getApplicationContext(), "video play completed",Toast.LENGTH_SHORT).show();
            }
        }).onInfo(new GiraffePlayer.OnInfoListener() {
            @Override
            public void onInfo(int what, int extra) {
                switch (what) {
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        Toast.makeText(MainActivity.this,"开始缓冲",Toast.LENGTH_LONG).show();
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        Toast.makeText(MainActivity.this,"缓冲结束",Toast.LENGTH_LONG).show();
                        break;
                    case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                        //刷新显示下载速度
                        ((TextView) findViewById(R.id.tv_speed)).setText(Formatter.formatFileSize(getApplicationContext(),extra)+"/s");
                        Toast.makeText(MainActivity.this,"网速:"+extra+"kb/s",Toast.LENGTH_SHORT).show();
                        break;
                    case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                        //do something when video rendering
//                        findViewById(R.id.tv_speed).setVisibility(View.GONE);
                        break;
                }
            }
        }).onError(new GiraffePlayer.OnErrorListener() {
            @Override
            public void onError(int what, int extra) {
                Toast.makeText(getApplicationContext(), "视频播放错误",Toast.LENGTH_SHORT).show();
            }
        });
        /**
         * 按钮监听
         */
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //播放
                if (v.getId() == R.id.btn_play) {
                    String url = ((EditText) findViewById(R.id.et_url)).getText().toString();
                    player.play(url);
                    player.setTitle(url);
                } else if (v.getId() == R.id.btn_play_sample_1) {
                    //m3u8
                    String url = "http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8";
                    ((EditText) findViewById(R.id.et_url)).setText(url);
                    player.play(url);
                    player.setTitle(url);
                } else if (v.getId() == R.id.btn_play_sample_2) {
                    //mp4
                    String url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
                    ((EditText) findViewById(R.id.et_url)).setText(url);
                    player.play(url);
                    player.setTitle(url);
                    //左上角返回导航图标
                    player.setShowNavIcon(false);
                }else if (v.getId() == R.id.btn_play_sample_3) {
                    String url = "";
                    ((EditText) findViewById(R.id.et_url)).setText(url);
                    player.play(url);
                    player.setTitle(url);
                    player.setShowNavIcon(false);
                }else if (v.getId() == R.id.btn_open) {
                    String url = ((EditText) findViewById(R.id.et_url)).getText().toString();
                    GiraffePlayerActivity.configPlayer(MainActivity.this).setTitle(url).play(url);
//                    more configuration example:
//                    GiraffePlayerActivity.configPlayer(MainActivity.this)
//                            .setScaleType(GiraffePlayer.SCALETYPE_FITPARENT)
//                            .setDefaultRetryTime(5 * 1000)
//                            .setFullScreenOnly(false)
//                            .setTitle(url)
//                            .play(url);
                }else if (v.getId() == R.id.btn_start) {
                    player.start();
                }else if (v.getId() == R.id.btn_pause) {
                    player.pause();
                }else if (v.getId() == R.id.btn_toggle) {
                    player.toggleFullScreen();
                }else if (v.getId() == R.id.btn_forward) {
                    player.forward(0.2f);
                }else if (v.getId() == R.id.btn_back) {
                    player.forward(-0.2f);
                }else if (v.getId() == R.id.btn_toggle_ratio) {
                    player.toggleAspectRatio();
                }
            }
        };
        findViewById(R.id.btn_play).setOnClickListener(clickListener);
        findViewById(R.id.btn_play_sample_1).setOnClickListener(clickListener);
        findViewById(R.id.btn_play_sample_2).setOnClickListener(clickListener);
        findViewById(R.id.btn_play_sample_3).setOnClickListener(clickListener);
        findViewById(R.id.btn_pause).setOnClickListener(clickListener);
        findViewById(R.id.btn_start).setOnClickListener(clickListener);
        findViewById(R.id.btn_toggle).setOnClickListener(clickListener);
        findViewById(R.id.btn_open).setOnClickListener(clickListener);
        findViewById(R.id.btn_forward).setOnClickListener(clickListener);
        findViewById(R.id.btn_back).setOnClickListener(clickListener);
        findViewById(R.id.btn_toggle_ratio).setOnClickListener(clickListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }
}
