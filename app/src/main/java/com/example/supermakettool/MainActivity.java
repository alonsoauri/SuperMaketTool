package com.example.supermakettool;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    EditText et_usuario, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_usuario = findViewById(R.id.editText);
        et_password = findViewById(R.id.editText2);

    }
    public void Registrar(View view)
    {
        Intent registrar = new Intent(this, Registro.class);
        startActivity(registrar);
    }

    public void Principal(View view)
    {
        try {
            getApiData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void getApiData() throws IOException {

        String url = "https://supermarkettoolswebapi.azurewebsites.net/User/";
        OkHttpClient client = new OkHttpClient();

        final String username = et_usuario.getText().toString();
        final String password = et_password.getText().toString();

        if ((!password.isEmpty()) && (!username.isEmpty())){

            url = url + username + "/" + password;

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
                    // ... check for failure using `isSuccessful` before proceeding
                    // Read data on the worker thread
                    final int ok = response.code();
                    final String responseData = response.body().string();

                    // Run view-related code back on the main thread
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Se revisa el estatus del request, si es 200 si existe el usaurio si no nelpasel
                            if (ok == 200) {

                                //Se creaa una variable superglobal con el id del usuario
                                Gson gson = new Gson();
                                UserResponse userdata = gson.fromJson(responseData, UserResponse.class);

                                ((ClaseGlobal) getApplication()).setId_user(userdata.getUserrId());

                                //Se llama a la pantalla de inicio
                                Intent principal = new Intent(MainActivity.this, PantallaPrincipal.class);
                                startActivity(principal);
                            }else {
                                Toast.makeText(MainActivity.this, R.string.message_credenciales_invalidas, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

        }else {
            Toast.makeText(MainActivity.this, R.string.message_error_pass_user_empty, Toast.LENGTH_SHORT).show();
        }
    }

}
