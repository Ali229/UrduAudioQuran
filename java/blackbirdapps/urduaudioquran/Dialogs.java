package blackbirdapps.urduaudioquran;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;

public class Dialogs {

    public void menuItem(Context c, MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Urdu Audio Quran");
                String sAux = "Check out this Quran Application I'm using:\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=blackbirdapps.urduaudioquran";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                c.startActivity(Intent.createChooser(i, "Share Via"));
            } catch (Exception e) {
                Log.e("Quran", "onOptionsItemSelected error!", e);
            }
        }
        else if (id == R.id.action_settings) {
            AlertDialog.Builder builder = new AlertDialog.Builder(c);
            LayoutInflater inflater = this.getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.dialog, null))
                    .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
