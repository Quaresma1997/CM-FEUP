package org.feup.acmeeletronicsshop.activities;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import org.feup.acmeeletronicsshop.R;
import org.feup.acmeeletronicsshop.adapters.TransactionItemRecyclerAdapter;
import org.feup.acmeeletronicsshop.adapters.TransactionRecyclerAdapter;
import org.feup.acmeeletronicsshop.model.Transaction;
import org.feup.acmeeletronicsshop.model.TransactionItem;
import org.feup.acmeeletronicsshop.model.User;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class DetailedTransactionActivity extends AppCompatActivity {
    private Toolbar toolbar;
    final static int DIMENSION=300;
    final static String CH_SET="ISO-8859-1";

    Transaction transaction;

    private Dialog qrCodeDialog;

    TransactionItemRecyclerAdapter transactionItemRecyclerAdapter;
    RecyclerView recyclerViewTransactionItem;
    List<TransactionItem> items;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_transaction);

        transaction = (Transaction) getIntent().getSerializableExtra("transaction");

        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        AppCompatTextView idTransaction = (AppCompatTextView) findViewById(R.id.txtViewID);
        AppCompatTextView date = (AppCompatTextView) findViewById(R.id.txtViewDate);
        AppCompatTextView totalCost = (AppCompatTextView) findViewById(R.id.txtTotalCost);
        idTransaction.setText(transaction.getToken());
        date.setText((transaction.getDate()).toString());
        totalCost.setText(transaction.getTotalCost()+"€");

        recyclerViewTransactionItem = (RecyclerView) findViewById(R.id.recyclerViewTransactionItems);

        items = transaction.getItemlist();

        transactionItemRecyclerAdapter = new TransactionItemRecyclerAdapter(items);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewTransactionItem.setLayoutManager(mLayoutManager);
        recyclerViewTransactionItem.setItemAnimator(new DefaultItemAnimator());
        recyclerViewTransactionItem.setHasFixedSize(true);
        recyclerViewTransactionItem.setAdapter(transactionItemRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_qr, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.btnQR:
                QRCodeDialog(transaction.getToken());
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void QRCodeDialog(final String token){
        qrCodeDialog = new Dialog(DetailedTransactionActivity.this);
        qrCodeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        qrCodeDialog.setContentView(R.layout.qrcodedialog);
        qrCodeDialog.setTitle("QR Code");

        Thread t = new Thread(new Runnable() {    // convert in a separate thread to avoid possible ANR
            public void run() {
                final Bitmap bitmap;
                final ImageView qrCodeIv = (ImageView) qrCodeDialog.findViewById(R.id.imgView);
                final Button confirm = (Button)qrCodeDialog.findViewById(R.id.btnConfirm);

                bitmap = encodeAsBitmap(token);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        qrCodeIv.setImageBitmap(bitmap);
                        confirm.setEnabled(true);
                    }
                });



                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        qrCodeDialog.cancel();
                    }
                });


            }
        });
        t.start();



        qrCodeDialog.show();
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
