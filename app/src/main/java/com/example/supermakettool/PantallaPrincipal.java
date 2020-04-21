package com.example.supermakettool;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class PantallaPrincipal extends AppCompatActivity {

    private TextView opciones;
    private TextView cronometro;
    private TextView jugar;
    private TextView supermarkettool;
    private TextView canasta;
    private Typeface restaurantmenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        String fuente1 = "fuentes/fullpack.ttf";
        this.restaurantmenu = Typeface.createFromAsset(getAssets(),fuente1);

        supermarkettool = (TextView) findViewById(R.id.textView);
        supermarkettool.setTypeface(restaurantmenu);

        canasta = (TextView) findViewById(R.id.button9);
        canasta.setTypeface(restaurantmenu);

        jugar = (TextView) findViewById(R.id.button10);
        jugar.setTypeface(restaurantmenu);

        cronometro = (TextView) findViewById(R.id.button11);
        cronometro.setTypeface(restaurantmenu);

        opciones = (TextView) findViewById(R.id.button12);
        opciones.setTypeface(restaurantmenu);

    }


    public void Siguiente(View view)
    {
        Intent siguiente = new Intent(PantallaPrincipal.this, Lists.class);
        startActivity(siguiente);
    }
}
