package blackbirdapps.urduaudioquran;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Surahs extends MainActivity {
    public void populate() {
        if (surahList.size() < 1) {
            surahList.add("al_fatihah");
            surahList.add("al_baqarah");
            surahList.add("al_imran");
//            surahList.add("an_nisa");
//            surahList.add("al_maidah");
//            surahList.add("al_anam");
//            surahList.add("al_araf");
//            surahList.add("al_anfal");
//            surahList.add("al_tawbah");
//            surahList.add("yunus");
//            surahList.add("hud");
//            surahList.add("yusuf");
//            surahList.add("al_rad");
//            surahList.add("ibrahim");
//            surahList.add("al_hijr");
//            surahList.add("an_nahl");
//            surahList.add("al_isra");
//            surahList.add("al_kahf");
//            surahList.add("maryam");
//            surahList.add("taha");
//            surahList.add("al_anbya");
//            surahList.add("al_haj");
//            surahList.add("al_muminun");
//            surahList.add("an_nur");
//            surahList.add("al_furqan");
//            surahList.add("ash_shuara");
//            surahList.add("an_naml");
//            surahList.add("al_qasas");
//            surahList.add("al_ankabut");
//            surahList.add("ar_rum");
//            surahList.add("luqman");
//            surahList.add("as_sajdah");
//            surahList.add("al_ahzab");
//            surahList.add("saba");
//            surahList.add("fatir");
//            surahList.add("ya_sin");
//            surahList.add("as_saffat");
//            surahList.add("sad");
//            surahList.add("az_zumar");
//            surahList.add("ghafir");
//            surahList.add("fussilat");
//            surahList.add("ash_shuraa");
//            surahList.add("az_zukhruf");
//            surahList.add("ad_dukhan");
//            surahList.add("al_jathiyah");
//            surahList.add("al_ahqaf");
//            surahList.add("muhammad");
//            surahList.add("al_fath");
//            surahList.add("al_hujurat");
//            surahList.add("qaf");
//            surahList.add("adh_dhariyat");
//            surahList.add("at_tur");
//            surahList.add("an_najm");
//            surahList.add("al_qamar");
//            surahList.add("ar_rahman");
//            surahList.add("al_waqiah");
//            surahList.add("al_hadid");
//            surahList.add("al_mujadila");
//            surahList.add("al_hashr");
//            surahList.add("al_mumtahanah");
//            surahList.add("as_saf");
//            surahList.add("al_jumuah");
//            surahList.add("al_munafiqun");
//            surahList.add("at_taghabun");
//            surahList.add("at_talaq");
//            surahList.add("at_tahrim");
//            surahList.add("al_mulk");
//            surahList.add("al_qalam");
//            surahList.add("al_haqqah");
//            surahList.add("al_maarij");
//            surahList.add("nuh");
//            surahList.add("al_jinn");
//            surahList.add("al_muzzammil");
//            surahList.add("al_muddaththir");
//            surahList.add("al_qiyamah");
//            surahList.add("al_insan");
//            surahList.add("al_mursalat");
//            surahList.add("an_naba");
//            surahList.add("an_naziat");
//            surahList.add("abasa");
//            surahList.add("at_takwir");
//            surahList.add("al_infitar");
//            surahList.add("al_mutaffifin");
//            surahList.add("al_inshiqaq");
//            surahList.add("al_buruj");
//            surahList.add("at_tariq");
//            surahList.add("al_ala");
//            surahList.add("al_ghashiyah");
//            surahList.add("al_fajr");
//            surahList.add("al_balad");
//            surahList.add("ash_shams");
//            surahList.add("al_layl");
//            surahList.add("ad_duhaa");
//            surahList.add("ash_sharh");
//            surahList.add("at_tin");
//            surahList.add("al_alaq");
//            surahList.add("al_qadr");
//            surahList.add("al_bayyinah");
//            surahList.add("az_zalzalah");
//            surahList.add("al_adiyat");
//            surahList.add("al_qariah");
//            surahList.add("at_takathur");
//            surahList.add("al_asr");
//            surahList.add("al_humazah");
//            surahList.add("al_fil");
//            surahList.add("quraysh");
//            surahList.add("al_maun");
//            surahList.add("al_kawthar");
//            surahList.add("al_kafirun");
//            surahList.add("an_nasr");
//            surahList.add("al_masad");
//            surahList.add("al_ikhlas");
//            surahList.add("al_falaq");
//            surahList.add("an_nas");
        }
    }
    public String text(Context c) {
        String sText = "";
        try {
            String sNumber = "s" + (number + 1) + ".uaq";
            BufferedReader reader = new BufferedReader(new InputStreamReader(c.getAssets().open(sNumber)));
            String line;
            Log.e("Reader Stuff", reader.readLine());
            while ((line = reader.readLine()) != null) {
                sText += line;
            }

        } catch (Exception e) {
            Log.e("Quran", "getText!", e);
        }
        return sText;
    }
}
