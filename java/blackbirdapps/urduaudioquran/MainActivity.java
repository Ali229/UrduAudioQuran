package blackbirdapps.urduaudioquran;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SeekBar.OnSeekBarChangeListener, AudioManager.OnAudioFocusChangeListener {
    //====================================== Declarations ========================================//
    private static MediaPlayer mediaPlayer;
    private static Uri myUri;
    private Handler handler = new Handler();
    private ImageButton playButton, nextButton, previousButton, rewindButton;
    private SeekBar songS;
    private static Runnable myRunnable;
    private static ArrayList<String> surahList = new ArrayList<>();
    private static int number;
    private TextView durationLabel, elaspedLabel, surahText;
    private ScrollView sv;
    private AudioManager am;

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
        if (myUri != null) {
            getText();
            if (mediaPlayer.isPlaying()) {
                mediaControlOn();
            } else {
                mediaControlUpdate();
            }

        }
    }

    //====================================== Populate Surah List =================================//
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
            surahList.add("al_anbya");
            surahList.add("al_haj");
            surahList.add("al_muminun");
            surahList.add("an_nur");
            surahList.add("al_furqan");
            surahList.add("ash_shuara");
            surahList.add("an_naml");
            surahList.add("al_qasas");
            surahList.add("al_ankabut");
            surahList.add("ar_rum");
            surahList.add("luqman");
            surahList.add("as_sajdah");
            surahList.add("al_ahzab");
            surahList.add("saba");
            surahList.add("fatir");
            surahList.add("ya_sin");
            surahList.add("as_saffat");
            surahList.add("sad");
            surahList.add("az_zumar");
            surahList.add("ghafir");
            surahList.add("fussilat");
            surahList.add("ash_shuraa");
            surahList.add("az_zukhruf");
            surahList.add("ad_dukhan");
            surahList.add("al_jathiyah");
            surahList.add("al_ahqaf");
            surahList.add("muhammad");
            surahList.add("al_fath");
            surahList.add("al_hujurat");
            surahList.add("qaf");
            surahList.add("adh_dhariyat");
            surahList.add("at_tur");
            surahList.add("an_najm");
            surahList.add("al_qamar");
            surahList.add("ar_rahman");
            surahList.add("al_waqiah");
            surahList.add("al_hadid");
            surahList.add("al_mujadila");
            surahList.add("al_hashr");
            surahList.add("al_mumtahanah");
            surahList.add("as_saf");
            surahList.add("al_jumuah");
            surahList.add("al_munafiqun");
            surahList.add("at_taghabun");
            surahList.add("at_talaq");
            surahList.add("at_tahrim");
            surahList.add("al_mulk");
            surahList.add("al_qalam");
            surahList.add("al_haqqah");
            surahList.add("al_maarij");
            surahList.add("nuh");
            surahList.add("al_jinn");
            surahList.add("al_muzzammil");
            surahList.add("al_muddaththir");
            surahList.add("al_qiyamah");
            surahList.add("al_insan");
            surahList.add("al_mursalat");
            surahList.add("an_naba");
            surahList.add("an_naziat");
            surahList.add("abasa");
            surahList.add("at_takwir");
            surahList.add("al_infitar");
            surahList.add("al_mutaffifin");
            surahList.add("al_inshiqaq");
            surahList.add("al_buruj");
            surahList.add("at_tariq");
            surahList.add("al_ala");
            surahList.add("al_ghashiyah");
            surahList.add("al_fajr");
            surahList.add("al_balad");
            surahList.add("ash_shams");
            surahList.add("al_layl");
            surahList.add("ad_duhaa");
            surahList.add("ash_sharh");
            surahList.add("at_tin");
            surahList.add("al_alaq");
            surahList.add("al_qadr");
            surahList.add("al_bayyinah");
            surahList.add("az_zalzalah");
            surahList.add("al_adiyat");
            surahList.add("al_qariah");
            surahList.add("at_takathur");
            surahList.add("al_asr");
            surahList.add("al_humazah");
            surahList.add("al_fil");
            surahList.add("quraysh");
            surahList.add("al_maun");
            surahList.add("al_kawthar");
            surahList.add("al_kafirun");
            surahList.add("an_nasr");
            surahList.add("al_masad");
            surahList.add("al_ikhlas");
            surahList.add("al_falaq");
            surahList.add("an_nas");
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
        try {
            Typeface mFont = Typeface.createFromAsset(getAssets(), "Nafees.ttf");
            surahText = (TextView) findViewById(R.id.surahT);
            surahText.setTypeface(mFont);
        } catch (Exception ignored) {
        }
        sv = (ScrollView) findViewById(R.id.scroll);

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
        int id = item.getItemId();
        if (id == R.id.action_share) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Urdu Audio Quran");
                String sAux = "\nCheck out this Quran Application I'm using:\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=blackbirdapps.urduaudioquran";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "Share Via"));
                return true;
            } catch (Exception ignored) {
            }
        } else if (id == R.id.action_settings) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // Get the layout inflater
            LayoutInflater inflater = this.getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.dialog, null))
                    // Add action buttons
                    .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    //====================================== Play ================================================//
    public void play() {
        if (!songS.isEnabled()) {
            songS.setEnabled(true);
        }
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(getApplicationContext(), myUri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            handler.removeCallbacks(myRunnable);
            mediaPlayer.prepare();
            mediaPlay();
            checkArray();
            getText();
            sv.fullScroll(ScrollView.FOCUS_UP);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                  nextMethod();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, e.toString(),
                    Toast.LENGTH_LONG).show();
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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    //====================================== SeekBar onStartTouch  ===============================//
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mediaPause();
    }

    //====================================== SeekBar onStopTouch  ================================//
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int currentPosition = progressToTimer(songS.getProgress(), mediaPlayer.getDuration());
        mediaPlayer.seekTo(currentPosition);
        mediaPlay();
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
        try {
            surahText.setText("");
            String sNumber = "s" + (number + 1) + ".uaq";
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(sNumber)));
            String line;
            Log.e("Reader Stuff", reader.readLine());
            while ((line = reader.readLine()) != null) {
                surahText.append(line);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void mediaPlay() {
        int requestAudioFocusResult = am.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (requestAudioFocusResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mediaPlayer.start();
        }
        mediaControlOn();
        Toast.makeText(this, "Started", Toast.LENGTH_SHORT).show();
    }

    public void mediaPause() {
        am.abandonAudioFocus(this);
        mediaPlayer.pause();
        mediaControlOff();
    }

    public void mediaControlUpdate() {
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
            songS = (SeekBar) findViewById(R.id.songSeek);
            songS.setProgress(getProgressPercentage(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration()));
        }
        checkPlayState();
    }

    public void mediaControlOn() {
        handler.post(myRunnable = new Runnable() {
            public void run() {
                mediaControlUpdate();
                handler.postDelayed(this, 50);
            }
        });
    }

    public void mediaControlOff() {
        handler.removeCallbacks(myRunnable);
        checkPlayState();
    }

    @Override
    public void onAudioFocusChange(int audioFocusChanged) {
        switch (audioFocusChanged) {
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                mediaPlayer.pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                mediaPlayer.pause();
                break;
            case AudioManager.AUDIOFOCUS_GAIN:
                mediaPlay();
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                mediaPause();
                break;
        }
    }
}
