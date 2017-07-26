package blackbirdapps.urduaudioquran;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.media.MediaPlayer.OnPreparedListener;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnPreparedListener,
        SeekBar.OnSeekBarChangeListener, AudioManager.OnAudioFocusChangeListener,
        MediaPlayer.OnCompletionListener {
    //====================================== Declarations ========================================//
    private final static MediaPlayer mediaPlayer = new MediaPlayer();
    public static Uri myUri;
    private static Handler handler = new Handler();
    private ImageButton playButton, nextButton, previousButton, rewindButton;
    private SeekBar songS;
    private static Runnable myRunnable;
    public static ArrayList<String> surahList = new ArrayList<>();
    public static int number;
    private TextView durationLabel, elaspedLabel, surahText;
    private ScrollView sv;
    private AudioManager am;
    private static int requestAudioFocusResult = 0;
    private static Surahs s1 = new Surahs();
    private static Dialogs d1 = new Dialogs();

    //====================================== Media State Handler =================================//
    public void onPrepared(final MediaPlayer mediaPlayer) {
        handler.post(myRunnable = new Runnable() {
            public void run() {
                //if (mediaPlayer.isPlaying()) {
                checkPlayState();
                if (durationLabel != null) {
                    int hours = (mediaPlayer.getDuration() / (1000 * 60 * 60) * 60);
                    int minutes = (mediaPlayer.getDuration() % (1000 * 60 * 60)) / (1000 * 60) + hours;
                    int seconds = (mediaPlayer.getDuration() % (1000 * 60 * 60)) % (1000 * 60) / 1000;
                    durationLabel.setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                }
                if (elaspedLabel != null) {
                    int ehours = (mediaPlayer.getCurrentPosition() / (1000 * 60 * 60) * 60);
                    int eminutes = (mediaPlayer.getCurrentPosition() % (1000 * 60 * 60)) / (1000 * 60) + ehours;
                    int eseconds = (mediaPlayer.getCurrentPosition() % (1000 * 60 * 60)) % (1000 * 60) / 1000;
                    elaspedLabel.setText(String.format("%02d", eminutes) + ":" + String.format("%02d", eseconds));
                }
                if (myUri != null) {
                    String sString = myUri.getLastPathSegment();
                    StringBuffer stringbf = new StringBuffer();
                    Matcher m = Pattern.compile("([a-z])([a-z]*)",
                            Pattern.CASE_INSENSITIVE).matcher(sString);
                    while (m.find()) {
                        m.appendReplacement(stringbf,
                                m.group(1).toUpperCase() + m.group(2).toLowerCase());
                    }
                    sString = m.appendTail(stringbf).toString();
                    sString = sString.replace("_", "-");
                    setTitle((number + 1) + " - " + sString);
                }
                if (mediaPlayer.getCurrentPosition() != 0) {
                    songS.setProgress(getProgressPercentage(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration()));
                }
                handler.postDelayed(this, 200);
            }
            // }
        });
    }

    //====================================== Progress Percentage =================================//
    public int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage;
        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);
        percentage = (((double) currentSeconds) / totalSeconds) * 100;
        return percentage.intValue();
    }

    //====================================== onResume ============================================//
    @Override
    public void onResume() {
        if (myUri != null) {
            onPrepared(mediaPlayer);
            getText();
            songS.setEnabled(true);
        }
        super.onResume();
    }

    //====================================== Populate Surah List =================================//
    public void populateList() {
        s1.populate();
    }

    //====================================== onCreate ============================================//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.setDrawerListener(toggle);
        }

        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
        populateList();
        songS = (SeekBar) findViewById(R.id.songSeek);
        songS.setEnabled(false);
        mediaPlayer.setOnPreparedListener(this);

        if (songS != null) {
            songS.setOnSeekBarChangeListener(this);
        }
        durationLabel = (TextView) findViewById(R.id.duration);
        elaspedLabel = (TextView) findViewById(R.id.elapsed);
        //================================= Media Controls =======================================//
        playButton = (ImageButton) findViewById(R.id.play);
        playButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    controlPlay();
                } catch (Exception e) {
                    Log.e("Quran", "Play Button Error!", e);
                }
            }
        });
        previousButton = (ImageButton) findViewById(R.id.previous);
        previousButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    previousMethod();
                } catch (Exception e) {
                    Log.e("Quran", "Previous Button Error!", e);
                }
            }
        });
        nextButton = (ImageButton) findViewById(R.id.next);
        nextButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    nextMethod();
                } catch (Exception e) {
                    Log.e("Quran", "Next Button Error!", e);
                }
            }
        });
        rewindButton = (ImageButton) findViewById(R.id.rewind);
        rewindButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000);
                } catch (Exception e) {
                    Log.e("Quran", "Rewind Button Error!", e);
                }
            }
        });
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        surahText = (TextView) findViewById(R.id.surahT);
        try {
            Typeface mFont = Typeface.createFromAsset(getAssets(), "Nafees.ttf");
            surahText.setTypeface(mFont);
        } catch (Exception e) {
            Log.e("Quran", "typeFace Error!", e);
        }
        sv = (ScrollView) findViewById(R.id.scroll);
        mediaPlayer.setOnCompletionListener(this);
    }

    //====================================== Previous Surah ======================================//
    public void previousMethod() {
        try {
            myUri = Uri.parse("android.resource://blackbirdapps.urduaudioquran/raw/" + surahList.get(number - 1));
        } catch (IndexOutOfBoundsException e) {
            myUri = Uri.parse("android.resource://blackbirdapps.urduaudioquran/raw/" + surahList.get(0));
        } finally {
            play();
        }
    }

    //====================================== Next Surah ==========================================//
    public void nextMethod() {
        try {
            myUri = Uri.parse("android.resource://blackbirdapps.urduaudioquran/raw/" + surahList.get(number + 1));
        } catch (IndexOutOfBoundsException e) {
            myUri = Uri.parse("android.resource://blackbirdapps.urduaudioquran/raw/" + surahList.get(0));
        } finally {
            play();
        }
    }

    //====================================== Get Surah Number ====================================//
    public void checkArray() {
        String surahName = myUri.getLastPathSegment();
        number = surahList.indexOf(surahName);
    }

    //====================================== Back Key Pressed ====================================//
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    //====================================== Options Menu ========================================//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //====================================== Options Selected ====================================//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        d1.menuItem(this, item);
        return super.onOptionsItemSelected(item);
    }

    //====================================== Play ================================================//
    public void play() {
        try {
            if (!songS.isEnabled()) {
                songS.setEnabled(true);
            }
            mediaPlayer.reset();
            mediaPlayer.setDataSource(getApplicationContext(), myUri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            mediaPlay();
            checkArray();
            getText();
            sv.fullScroll(ScrollView.FOCUS_UP);
        } catch (Exception e) {
            Log.e("Quran", "Play Error!", e);
        }
    }

    //====================================== Navigation Selection ================================//
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String[] id = getResources().getResourceName(item.getItemId()).split("/");
        number = surahList.indexOf(id[1]);
        myUri = Uri.parse("android.resource://blackbirdapps.urduaudioquran/raw/" + surahList.get(number));
        play();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    //====================================== Play/Pause Keys =====================================//
    public void controlPlay() {
        if (myUri == null) {
            myUri = Uri.parse("android.resource://blackbirdapps.urduaudioquran/raw/" + surahList.get(0));
            play();
        } else {
            if (mediaPlayer.isPlaying()) {
                mediaPause();

            } else {
                mediaPlay();
            }
        }

    }

    //====================================== Check Player State ==================================//
    public void checkPlayState() {
        if (mediaPlayer.isPlaying()) {
            playButton.setImageResource(R.drawable.pause);
        } else {
            playButton.setImageResource(R.drawable.play);
        }
    }

    //====================================== SeekBar onProgressChanged  ==========================//
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

    //====================================== SeekBar onStartTouch  ===============================//
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        handler.removeMessages(0);
    }

    //====================================== SeekBar onStopTouch  ================================//
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int currentPosition = progressToTimer(seekBar.getProgress(), mediaPlayer.getDuration());
        mediaPlayer.seekTo(currentPosition);
        onPrepared(mediaPlayer);
    }

    //====================================== SeekTo onStop  ======================================//
    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration;
        totalDuration = (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);
        return currentDuration * 1000;
    }

    //====================================== Get Text For Surah  =================================//
    public void getText() {
        surahText.setText(s1.text(this));
    }

    //====================================== Media Play ==========================================//
    public void mediaPlay() {
        if (requestAudioFocusResult == 0) {
            requestAudioFocusResult = am.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            if (requestAudioFocusResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                mediaPlayer.start();
            }
        } else {
            mediaPlayer.start();
        }
        onPrepared(mediaPlayer);
    }

    //====================================== Media Pause =========================================//
    public void mediaPause() {
        am.abandonAudioFocus(this);
        handler.removeMessages(0);
        mediaPlayer.pause();
        requestAudioFocusResult = 0;
        checkPlayState();
    }

    //====================================== Audio Focus  ========================================//
    @Override
    public void onAudioFocusChange(int audioFocusChanged) {
        switch (audioFocusChanged) {
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                mediaPlayer.pause();
                requestAudioFocusResult = 0;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                mediaPlayer.pause();
                requestAudioFocusResult = 0;
                break;
            case AudioManager.AUDIOFOCUS_GAIN:
                mediaPlay();
                requestAudioFocusResult = 1;
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                mediaPause();
                requestAudioFocusResult = 0;
                break;
        }
    }

    //====================================== MediaPlayer OnCompletion  ===========================//
    @Override
    public void onCompletion(final MediaPlayer mediaPlayer) {
        try {
            nextMethod();
        } catch (Exception e) {
            Log.e("Quran", "OnCompletion Error!", e);
        }
    }
}