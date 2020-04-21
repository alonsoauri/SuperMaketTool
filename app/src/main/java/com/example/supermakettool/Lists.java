package com.example.supermakettool;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;


public class Lists extends AppCompatActivity {

    public EditText editText;
    public String listjson;
    public String res;
    private ListView listview;
    private ArrayList<String> names;
    private ArrayAdapter<String> adapter;
    private TblListsResponse[] arraylist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.btn_add_list);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputBox("Nuena Lista", 1);
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

        String url = "https://supermarkettoolswebapi.azurewebsites.net/TblLists/";
        OkHttpClient client = new OkHttpClient();

        url = url + ((ClaseGlobal) getApplication()).getId_user();

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
                final String responseData = response.body().string();
                Lists.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        createList(responseData);
                    }
                });
            }
        });
    }


    public void createList(String json) {

        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<TblListsResponse>>() {
        }.getType();
        Collection<TblListsResponse> enumlist = gson.fromJson(json, collectionType);
        arraylist = enumlist.toArray(new TblListsResponse[enumlist.size()]);

        listview = (ListView) findViewById(R.id.List);
        names = new ArrayList<String>();

        for (int i = 0; i < arraylist.length; i++) {
            names.add(arraylist[i].getListName());
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        listview.setAdapter(adapter);


        listview.setClickable(true);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent siguiente = new Intent(Lists.this, itemsList.class);
                Bundle extras = new Bundle();
                extras.putInt("IndexList", arraylist[position].getPkIndexListas());
                siguiente.putExtras(extras);
                startActivity(siguiente);
            }
        });

        listview.setLongClickable(true);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                final int poition = pos;
                new AlertDialog.Builder(Lists.this)
                        .setTitle("Title")
                        .setMessage(R.string.Confirm_message)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    deleteApiData(poition);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

                return true;
            }
        });

    }

    public void showInputBox(String oldItem, final int index) {
        final Dialog dialog = new Dialog(Lists.this);
        dialog.setTitle("Input Box");
        dialog.setContentView(R.layout.input_box);
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtmessage);
        txtMessage.setText(R.string.texto_titulo_input_box);
        txtMessage.setTextColor(Color.parseColor("#ff2222"));
        editText = (EditText) dialog.findViewById(R.id.txtinput);
        editText.setText(oldItem);
        Button bt = (Button) dialog.findViewById(R.id.btdone);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    setApiList();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                dialog.dismiss();
            }
        });

        dialog.show();

    }

    void setApiList() throws IOException {

        String url = "https://supermarkettoolswebapi.azurewebsites.net/TblLists";
        OkHttpClient client = new OkHttpClient();

        Gson gson = new Gson();

        TblListsResponse newList = new TblListsResponse();

        newList.setListName(editText.getText().toString());
        newList.setFkUser(((ClaseGlobal) getApplication()).getId_user());

        listjson = gson.toJson(newList);

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, listjson);

        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final int ok = response.code();

                // Run view-related code back on the main thread
                Lists.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        switch (ok) {
                            case 200:
                                Toast.makeText(Lists.this, R.string.message_Lista_crada, Toast.LENGTH_SHORT).show();
                                names.add(editText.getText().toString());
                                adapter.notifyDataSetChanged();

                                break;
                            default:
                                Toast.makeText(Lists.this, R.string.message_inesperado, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }

    void deleteApiData(int position) throws IOException {

        final int pos = position;
        String url = "https://supermarkettoolswebapi.azurewebsites.net/TblLists/";
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");

        url = url + ((ClaseGlobal) getApplication()).getId_user() + "/" + arraylist[pos].getPkIndexListas();

        Request request = new Request.Builder()
                .url(url)
                .method("DELETE", body)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseData = response.body().string();
                final int ok = response.code();
                Lists.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (ok) {
                            case 200:
                                Toast.makeText(Lists.this, R.string.message_Lista_eliminada, Toast.LENGTH_SHORT).show();
                                names.remove(pos);
                                adapter.notifyDataSetChanged();
                                break;
                            default:
                                Toast.makeText(Lists.this, R.string.message_inesperado, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

}
