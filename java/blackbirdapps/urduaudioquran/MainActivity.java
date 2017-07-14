package blackbirdapps.urduaudioquran;

import android.content.Context;
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
import android.widget.MediaController;
import android.media.MediaPlayer.OnPreparedListener;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnPreparedListener, MediaController.MediaPlayerControl, SeekBar.OnSeekBarChangeListener {
    private static MediaPlayer mediaPlayer;
    private static Uri myUri;
    private Handler handler = new Handler();
    private ImageButton playButton, nextButton, previousButton, rewindButton;
    private SeekBar songS;
    private static Runnable myRunnable;
    private static boolean stop = false, running = false;
    public ArrayList<String> surahList = new ArrayList<>();
    private static int number;
    private TextView durationLabel, elaspedLabel, surahText;
    int result;

    static AudioManager am;
    static AudioManager.OnAudioFocusChangeListener afChangeListener;

    //====================================== Media State Handler =================================//
    public void onPrepared(final MediaPlayer mediaPlayer) {

        handler.post(myRunnable = new Runnable() {
            public void run() {
                if (!(stop)) {
                    running = true;
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
                    ;
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
                        songS = (SeekBar) findViewById(R.id.songSeek);
                        songS.setProgress(getProgressPercentage(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration()));
                    }
                    checkPlayState();
                    ;
                    handler.postDelayed(this, 50);
                }
            }
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
        super.onResume();
        checkPlayState();
        getText();
    }

    //====================================== Populate List =======================================//
    public void populateList() {
        if (surahList.size() == 0) {
            surahList.add("al_fatihah");
            surahList.add("al_baqarah");
            surahList.add("al_imran");
            surahList.add("an_nisa");
            surahList.add("al_maidah");
            surahList.add("al_anam");
            surahList.add("al_araf");
            surahList.add("al_anfal");
            surahList.add("al_tawbah");
            surahList.add("yunus");
            surahList.add("hud");
            surahList.add("yusuf");
            surahList.add("al_rad");
            surahList.add("ibrahim");
            surahList.add("al_hijr");
            surahList.add("an_nahl");
            surahList.add("al_isra");
            surahList.add("al_kahf");
            surahList.add("maryam");
            surahList.add("taha");
        }
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
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            songS.setEnabled(false);
        }
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
        if (running) {
            onPrepared(mediaPlayer);
        }
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            public void onAudioFocusChange(int focusChange) {
                if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                    mediaPlayer.pause();
                } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                    mediaPlayer.pause();
                    am.abandonAudioFocus(afChangeListener);
                } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                    mediaPlayer.start();
                } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                    mediaPlayer.pause();
                }
            }
        };
        try {
            Typeface mFont = Typeface.createFromAsset(getAssets(), "Nafees.ttf");
            surahText = (TextView) findViewById(R.id.surahT);
            surahText.setTypeface(mFont);
        } catch (Exception ignored) {
        }
    }

    public void previousMethod() {
        try {
            myUri = Uri.parse("android.resource://blackbirdapps.urduaudioquran/raw/" + surahList.get(number - 1));
        } catch (IndexOutOfBoundsException e) {
            myUri = Uri.parse("android.resource://blackbirdapps.urduaudioquran/raw/" + surahList.get(0));
        } finally {
            play();
        }
    }

    public void nextMethod() {
        try {
            myUri = Uri.parse("android.resource://blackbirdapps.urduaudioquran/raw/" + surahList.get(number + 1));
        } catch (IndexOutOfBoundsException e) {
            myUri = Uri.parse("android.resource://blackbirdapps.urduaudioquran/raw/" + surahList.get(0));
        } finally {
            play();
        }
    }

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
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    //====================================== Play ================================================//
    public void play() {
        focus();
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            //playing = true;
            if (!songS.isEnabled()) {
                songS.setEnabled(true);
            }
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(getApplicationContext(), myUri);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                handler.removeCallbacks(myRunnable);
                mediaPlayer.prepare();
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        try {
                            myUri = Uri.parse("android.resource://blackbirdapps.urduaudioquran/raw/" + surahList.get(number + 1));
                            play();
                        } catch (IndexOutOfBoundsException e) {
                            myUri = Uri.parse("android.resource://blackbirdapps.urduaudioquran/raw/" + surahList.get(0));
                            play();
                        }

                    }
                });
                checkArray();
                getText();
            } catch (Exception e) {
                Toast.makeText(this, e.toString(),
                        Toast.LENGTH_LONG).show();
                Log.e("Quran", "Play Error!", e);
            }
        }
    }


    public void focus() {
        result = am.requestAudioFocus(afChangeListener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);
    }

    //====================================== Navigation Selection ================================//
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String[] id = getResources().getResourceName(item.getItemId()).split("/");
        number = surahList.indexOf(id[1]);
        myUri = Uri.parse("android.resource://blackbirdapps.urduaudioquran/raw/" + surahList.get(number));
        play();
        checkPlayState();
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

                mediaPlayer.pause();
                am.abandonAudioFocus(afChangeListener);
                //playing = false;
            } else {
                focus();
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaPlayer.start();
                    //playing = true;
                }
            }
        }
        checkPlayState();
    }

    //====================================== Check Player State ==================================//
    public void checkPlayState() {
        if (mediaPlayer.isPlaying()) {
            playButton.setImageResource(R.drawable.pause);
        } else {
            playButton.setImageResource(R.drawable.play);
        }
    }

    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }


    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return mediaPlayer.getAudioSessionId();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(myRunnable);
        mediaPlayer.pause();
        stop = true;
        checkPlayState();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int currentPosition = progressToTimer(songS.getProgress(), mediaPlayer.getDuration());
        mediaPlayer.seekTo(currentPosition);
        stop = false;
        mediaPlayer.start();
        onPrepared(mediaPlayer);
        checkPlayState();
    }

    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration;
        totalDuration = (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);
        return currentDuration * 1000;
    }
    public void getText() {
        try {
            surahText.setText("");
            String sNumber = "s" + (number +1) + ".uaq";
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(sNumber)));
            String line;
            Log.e("Reader Stuff",reader.readLine());
            while ((line = reader.readLine()) != null) {
                surahText.append(line);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
