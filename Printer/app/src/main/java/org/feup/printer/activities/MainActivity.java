package org.feup.printer.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.feup.printer.R;

public class MainActivity extends AppCompatActivity {
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button scan = (Button) findViewById(R.id.btnScan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scan();
            }
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void scan() {
        Intent intent = new Intent();
        intent.setClass(this, InvoiceActivity.class);
        startActivity(intent);
//        try {
//            Intent intent = new Intent(ACTION_SCAN);
//            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//            startActivityForResult(intent, 0);
//        }
//        catch (ActivityNotFoundException anfe) {
//            showDialog(this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
//        }
    }

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                act.startActivity(intent);
            }
        });
        downloadDialog.setNegativeButton(buttonNo, null);
        return downloadDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String token = data.getStringExtra("SCAN_RESULT");
                //TODO Get the transaction from the API with the token
                //TODO creates the transaction and the user and sends it to the InvoiceActivity
            }
        }
    }
}
