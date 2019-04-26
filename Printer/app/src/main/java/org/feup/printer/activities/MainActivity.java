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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.feup.printer.R;
import org.feup.printer.helpers.RequestQueueSingleton;
import org.feup.printer.models.Transaction;
import org.feup.printer.models.TransactionItem;
import org.feup.printer.models.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    RequestQueue queue;
    private List<TransactionItem> items;


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
        queue = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();
    }

    public void scan() {

        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        }
        catch (ActivityNotFoundException anfe) {
            showDialog(this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
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
                String transactionUrl = "http://a722be3a.ngrok.io" + "/transaction/printer/" + token;

                final JsonObjectRequest transaction = new JsonObjectRequest(Request.Method.GET, transactionUrl, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    items = new ArrayList<>();

                                    //transaction
                                    String token = response.getString("token");

                                    String day = response.getString("day");

                                    SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd");

                                    Date date = formatter.parse(day);
                                    int totalCost = 0;


                                    //user
                                    int idUser= response.getInt("idUser");
                                    String email = response.getString("email");
                                    String name= response.getString("name");
                                    String fiscalNumber = response.getString("fiscalNumber");
                                    String address = response.getString("address");

                                    JSONArray products = response.getJSONArray("products");
                                    for(int i = 0; i < products.length(); i++){
                                        JSONObject product = products.getJSONObject(i);

                                        int id = product.getInt("barcode");
                                        String maker = product.getString("maker");
                                        String model = product.getString("model");
                                        int price = product.getInt("price");
                                        totalCost += price;
                                        String description = product.getString("description");
                                        int quantity = product.getInt("quantity");

                                        TransactionItem item = new TransactionItem(id, quantity, model);
                                        items.add(item);

                                    }


                                    Transaction transaction1 = new Transaction(date, totalCost, items, token);
                                    User user = new User(name, address, email, fiscalNumber);


                                    Intent intent = new Intent();
                                    intent.setClass(MainActivity.this, InvoiceActivity.class);
                                    Bundle b = new Bundle();
                                    b.putSerializable("user", user);
                                    b.putSerializable("transaction", transaction1);
                                    intent.putExtras(b);
                                    startActivity(intent);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });


                queue.add(transaction);

            }
        }
    }
}
