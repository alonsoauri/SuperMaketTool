package com.example.supermakettool;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Registro extends AppCompatActivity implements View.OnClickListener {

    public String userjson;
    EditText etnombre, etusuario, etpassword, etedad;
    Button btn_registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etnombre = findViewById(R.id.editText3);
        etusuario = findViewById(R.id.editText4);
        etpassword = findViewById(R.id.editText5);
        etedad = findViewById(R.id.editText6);

        btn_registrar = findViewById(R.id.button2);

        btn_registrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Gson gson = new Gson();

        final String name = etnombre.getText().toString();
        final String username = etusuario.getText().toString();
        final String password = etpassword.getText().toString();
        final int age = Integer.parseInt(etedad.getText().toString());

        UserResponse newuser = new UserResponse();

        newuser.setName(name);
        newuser.setUsername(username);
        newuser.setPassword(password);
        newuser.setAge(age);
        userjson = gson.toJson(newuser);

        try {
            setApiUser();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void setApiUser() throws IOException {

        String url = "https://supermarkettoolswebapi.azurewebsites.net/User";
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, userjson);

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
                Registro.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Se revisa el estatus del request, si es 200 si existe el usaurio si no nelpasel
                        Intent principal = new Intent(Registro.this, MainActivity.class);
                        switch (ok){
                            case 200:
                                //Se llama a la pantalla de Login
                                Toast.makeText(Registro.this, R.string.message_usuario_exito, Toast.LENGTH_SHORT).show();
                                startActivity(principal);
                                break;
                            case 400:
                                Toast.makeText(Registro.this, R.string.message_usuarioenuso, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(Registro.this, R.string.message_inesperado, Toast.LENGTH_SHORT).show();
                                startActivity(principal);
                        }
                    }
                });
            }
        });
    }

}
