package org.feup.acmeeletronicsshop.activities;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import org.feup.acmeeletronicsshop.R;

import java.util.Hashtable;

public class DetailedTransactionActivity extends AppCompatActivity {
    private Toolbar toolbar;
    final static int DIMENSION=300;
    final static String CH_SET="ISO-8859-1";

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        String token = "TEST";
        initQRCode(token);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initQRCode(final String token){
        Thread t = new Thread(new Runnable() {    // convert in a separate thread to avoid possible ANR
            public void run() {
                final Bitmap bitmap;
                final ImageView qrCodeIv = (ImageView) findViewById(R.id.img_qr_code);

                bitmap = encodeAsBitmap(token);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        qrCodeIv.setImageBitmap(bitmap);
                    }
                });

            }
        });
        t.start();
    }

    Bitmap encodeAsBitmap(String str) {
        BitMatrix result;

        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, CH_SET);
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, DIMENSION, DIMENSION, hints);
        }
        catch (Exception exc) {
//            runOnUiThread(()->errorTv.setText(exc.getMessage()));
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int line = 0; line < h; line++) {
            int offset = line * w;
            for (int col = 0; col < w; col++) {
                pixels[offset + col] = result.get(col, line) ? getResources().getColor(R.color.colorPrimary):getResources().getColor(R.color.colorAccent);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }
}
