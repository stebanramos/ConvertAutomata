package com.stebanramos.convertautomata;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TableLayout tableLayout;
    String[] colores = {"A", "B", "C", "D", "F", "G", "h", "I", "J", "K"};
    String[] tallas = {"BLANCO", "VERDE", "PÚRPURA", "AMARILLO", "MARRÓN"};

    //Con ese atributo decimos que la anchura de la fila será con el atributo WRAP_CONTENT
    TableRow.LayoutParams lp;
    TableRow tRowFND, tRowFD;

    LinearLayout layoutInitData, layoutAFND, layoutAFD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText etEstados = findViewById(R.id.etEstados);
        EditText etEntradas = findViewById(R.id.etEntradas);

        tableLayout = findViewById(R.id.tableAFND);
        layoutInitData = findViewById(R.id.datosIniciales);
        layoutAFND = findViewById(R.id.layoutAFND);
        layoutAFD = findViewById(R.id.layoutAFD);


        tRowFD = new TableRow(this);
        lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);

        tRowFD.setLayoutParams(lp);

        // Borde

        tRowFD.setBackgroundResource(R.drawable.border_table);

        Button btnContinuar = findViewById(R.id.btncontinuar);

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int estados = Integer.parseInt(etEstados.getText().toString());
                int entradas = Integer.parseInt(etEntradas.getText().toString());

                layoutInitData.setVisibility(View.GONE);
                layoutAFND.setVisibility(View.VISIBLE);
                initTableAFND(estados, entradas);
            }
        });

        Button btnNuevo = findViewById(R.id.btnNuevo);

        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                layoutInitData.setVisibility(View.VISIBLE);
                layoutAFND.setVisibility(View.GONE);
            }
        });

    }

    private void initTableAFND(int estados, int entradas){

        tRowFND = new TableRow(this);
        tRowFND.setLayoutParams(lp);
        tRowFND.setBackgroundResource(R.drawable.border_table);

        //El elemento de la izquierda
        TextView tv = new TextView(this);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setText("Estados/Entradas");
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tRowFND.addView(tv);

        // Ahora agregar las tallas
        for (int x = 0; x < entradas; x++) {
            EditText editText = new EditText(this);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setHint("Entrada " + (x+1));
            editText.setWidth(10);
            editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tRowFND.addView(editText);
        }
        // Finalmente agregar la fila en la primera posición
        tableLayout.addView(tRowFND, 0);
        // Ahora por cada color hacer casi lo mismo
        for (int x = 0; x < estados; x++) {
            TableRow filaColor = new TableRow(this);
            filaColor.setLayoutParams(lp);
            // Borde
            filaColor.setBackgroundResource(R.drawable.border_table);
            // El nombre del color
            TextView textViewColor = new TextView(this);
            textViewColor.setText(colores[x]);
            textViewColor.setTypeface(null, Typeface.BOLD);
            textViewColor.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            filaColor.addView(textViewColor);
            // Y ahora por cada talla, agregar un campo de texto
            for (int y = 0; y < entradas; y++) {
                EditText editText = new EditText(this);
                editText.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS | InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                editText.setWidth(10);
                editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                filaColor.addView(editText);
                filaColor.setMinimumWidth(10);
            }
            // Finalmente agregar la fila
            tableLayout.addView(filaColor);

        }
    }

}