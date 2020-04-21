package com.example.supermakettool;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class itemsList extends AppCompatActivity {

    TextView textViewTotal;
    private int indexList;
    private ListView listitems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textViewTotal = findViewById(R.id.textView4);

        FloatingActionButton fab = findViewById(R.id.fab);

        // Obtener datos de la clase Lists.java
        Bundle parametros = getIntent().getExtras();
        if (parametros != null) {
            indexList = parametros.getInt("IndexList");
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent additemsc = new Intent(itemsList.this, AddItem.class);
                Bundle extras = new Bundle();
                extras.putInt("IndexList", indexList);
                additemsc.putExtras(extras);
                startActivity(additemsc);


            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            getApiData();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    void getApiData() throws IOException {

        String url = "https://supermarkettoolswebapi.azurewebsites.net/TblItems/";
        OkHttpClient client = new OkHttpClient();

        url = url + ((ClaseGlobal) getApplication()).getId_user() + "/" + indexList;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final int ok = response.code();
                final String responseData = response.body().string();
                itemsList.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (ok) {
                            case 200:
                                createList(responseData);
                                break;
                            default:
                                Toast.makeText(itemsList.this, R.string.message_inesperado, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }

    public void createList(String json) {

        double total = 0;
        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<TblItemsResponse>>() {
        }.getType();
        Collection<TblItemsResponse> enumlist = gson.fromJson(json, collectionType);
        TblItemsResponse[] arraylist = enumlist.toArray(new TblItemsResponse[enumlist.size()]);

        //Crea el  ListView
        listitems = (ListView) findViewById(R.id.lv_items);

        //Llenar la lista
        String[][] datos = new String[arraylist.length][4];

        for (int i = 0; i < arraylist.length; i++) {
            datos[i][0] = arraylist[i].getItemName();
            datos[i][1] = arraylist[i].getCantidad().toString();
            datos[i][2] = arraylist[i].getPrice().toString();
            datos[i][3] = arraylist[i].getUm();

            total = total + (arraylist[i].getCantidad() * arraylist[i].getPrice());
        }

        String totalAsString =String.valueOf(total);
        textViewTotal.setText("Total: " + totalAsString);

        listitems.setAdapter(new itemListAdapter(this, datos));
        /*
        listitems.setLongClickable(true);
        listitems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {

                Log.v("long clicked", "pos: " + pos);

                new AlertDialog.Builder(itemsList.this)
                        .setTitle("Title")
                        .setMessage(R.string.Confirm_message)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(itemsList.this, "Yaay", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

                return true;
            }
        });
         */

    }

}
