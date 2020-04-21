package com.example.supermakettool;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddItem extends AppCompatActivity {

    public String listjson;
    Spinner spCategorya, spUM;
    EditText et_producto, et_precio, et_cantidad;
    private int indexList;
    private int id_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        et_producto = findViewById(R.id.editText);
        et_precio = findViewById(R.id.editText2);
        et_cantidad = findViewById(R.id.editText7);
        spUM = (Spinner) findViewById(R.id.spinner);
        spCategorya = (Spinner) findViewById(R.id.spinner2);

        // Obtener datos de la clase Lists.java
        Bundle parametros = getIntent().getExtras();
        if (parametros != null) {
            indexList = parametros.getInt("IndexList");
        }

        try {
            getApiData();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void getApiData() throws IOException {

        String url = "https://supermarkettoolswebapi.azurewebsites.net/TblCategory";
        OkHttpClient client = new OkHttpClient();

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
                AddItem.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (ok) {
                            case 200:
                                createSpinner(responseData);
                                break;
                            default:
                                Toast.makeText(AddItem.this, R.string.message_inesperado, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }

    public void createSpinner(String json) {

        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<TblCategoryResponse>>(){}.getType();
        Collection<TblCategoryResponse> enumlist = gson.fromJson(json, collectionType);
        final TblCategoryResponse[] arraylist = enumlist.toArray(new TblCategoryResponse[enumlist.size()]);

        String[] datos = new String[arraylist.length];

        for (int i = 0; i < arraylist.length; i++) {
            datos[i] = arraylist[i].getCategoryName();
        }
        spCategorya.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, datos));

        spCategorya.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_category = arraylist[position].getPkCategory();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

    }

    public void AddNewItem(View view) {
        try {
            setApiList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setApiList() throws IOException {

        String url = "https://supermarkettoolswebapi.azurewebsites.net/TblItems";
        //final String Categorya = spCategorya.getSelectedItem().toString();
        //final int IdCategory = spCategorya.getId();
        final String UM = spUM.getSelectedItem().toString();
        final String producto = et_producto.getText().toString();
        final double precio = Double.parseDouble(et_precio.getText().toString());
        final double cantidad = Double.parseDouble(et_cantidad.getText().toString());

        OkHttpClient client = new OkHttpClient();

        Gson gson = new Gson();
        TblItemsResponse newList = new TblItemsResponse();

        newList.setFkUser(((ClaseGlobal) getApplication()).getId_user());
        newList.setFkIndexListas(indexList);
        //newList.setPkId();
        //newList.setFkCb();
        //newList.setFkItem();
        newList.setItemName(producto);
        newList.setCantidad(cantidad);
        newList.setFkCategory(id_category);
        newList.setPrice(precio);
        newList.setUm(UM);

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
                AddItem.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        switch (ok) {
                            case 200:
                                Toast.makeText(AddItem.this, R.string.message_add_Item, Toast.LENGTH_SHORT).show();
                                Intent siguiente = new Intent(AddItem.this, itemsList.class);
                                Bundle extras = new Bundle();
                                extras.putInt("IndexList", indexList);
                                siguiente.putExtras(extras);
                                startActivity(siguiente);
                                break;
                            default:
                                Toast.makeText(AddItem.this, R.string.message_inesperado, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }

}
