package com.stebanramos.convertautomata;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private TableLayout tLayoutAFND, tLayoutAFD;
    private String[] estados = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private List<String> estadosAFD;
    private Map<String, Map<Integer, String>> afndMap;

    //Con ese atributo decimos que la anchura de la fila será con el atributo WRAP_CONTENT
    private TableRow.LayoutParams lp;
    private TableRow tRowFND, tRowFD;
    private LinearLayout layoutInitData, layoutAFND, layoutAFD;

    private int nEntradas;
    private String[] sEntradas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText etEstados = findViewById(R.id.etEstados);
        EditText etEntradas = findViewById(R.id.etEntradas);

        tLayoutAFND = findViewById(R.id.tableAFND);
        tLayoutAFD = findViewById(R.id.tableAFD);
        layoutInitData = findViewById(R.id.datosIniciales);
        layoutAFND = findViewById(R.id.layoutAFND);
        layoutAFD = findViewById(R.id.layoutAFD);

        Button btnConvert = findViewById(R.id.btnConvert);
        Button btnNuevo = findViewById(R.id.btnNuevo);
        Button btnContinuar = findViewById(R.id.btncontinuar);

        lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int estados = Integer.parseInt(etEstados.getText().toString());
                nEntradas = Integer.parseInt(etEntradas.getText().toString());

                layoutInitData.setVisibility(View.GONE);
                layoutAFND.setVisibility(View.VISIBLE);

                btnConvert.setVisibility(View.VISIBLE);
                btnNuevo.setVisibility(View.VISIBLE);

                initTableAFND(estados);
            }
        });

        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                layoutInitData.setVisibility(View.VISIBLE);
                layoutAFND.setVisibility(View.GONE);
                layoutAFD.setVisibility(View.GONE);
                btnConvert.setVisibility(View.GONE);
                btnNuevo.setVisibility(View.GONE);

                tLayoutAFD.removeAllViews();
                tLayoutAFND.removeAllViews();
            }
        });

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutAFD.setVisibility(View.VISIBLE);
                validateAutomata();
            }
        });

    }

    private void initTableAFND(int nEstados) {

        tRowFND = new TableRow(this);
        tRowFND.setLayoutParams(lp);
        tRowFND.setBackgroundResource(R.drawable.border_table);

        //El elemento de la izquierda
        TextView tv = new TextView(this);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setText("Estados/Entradas");
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tRowFND.addView(tv);

        // Ahora agregar las entradas
        for (int x = 0; x < nEntradas; x++) {
            EditText editText = new EditText(this);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setHint("Entrada " + (x + 1));
            editText.setWidth(10);
            editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tRowFND.addView(editText);
        }
        // Finalmente agregar la fila en la primera posición
        tLayoutAFND.addView(tRowFND, 0);
        // Ahora por cada color hacer casi lo mismo
        for (int x = 0; x < nEstados; x++) {
            TableRow filaEstado = new TableRow(this);
            filaEstado.setLayoutParams(lp);
            // Borde
            filaEstado.setBackgroundResource(R.drawable.border_table);
            // El nombre del estado
            TextView textViewestado = new TextView(this);
            textViewestado.setText(estados[x]);
            textViewestado.setTypeface(null, Typeface.BOLD);
            textViewestado.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            filaEstado.addView(textViewestado);
            // Y ahora por cada estado, agregar un campo de texto
            for (int y = 0; y < nEntradas; y++) {
                EditText editText = new EditText(this);
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS | InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                editText.setWidth(10);
                editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                filaEstado.addView(editText);
                filaEstado.setMinimumWidth(10);
            }
            // Finalmente agregar la fila
            tLayoutAFND.addView(filaEstado);

        }
    }

    private void validateAutomata() {
        getDataAFND();
        convertToAFD();

        if (isAFND()) {
            showTabAFD();
        } else {
            showDialog();
        }
    }

    private boolean isAFND() {
        boolean isFND = false;

        for (int i = 0; i < estadosAFD.size(); i++) {

            if (estadosAFD.get(i).length() != 1) {
                isFND = true;
            }

        }

        return isFND;
    }

    private void showTabAFD() {
        tLayoutAFD.removeAllViews();

        tRowFD = new TableRow(this);
        tRowFD.setLayoutParams(lp);
        // Borde
        tRowFD.setBackgroundResource(R.drawable.border_table);

        //El elemento de la izquierda
        TextView tv = new TextView(this);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setText("Estados/Entradas");
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tRowFD.addView(tv);

        // Ahora agregar las entradas
        for (int x = 0; x < nEntradas; x++) {
            TextView textViewEntradas = new TextView(this);
            textViewEntradas.setText(sEntradas[x]);
            textViewEntradas.setTypeface(null, Typeface.BOLD);
            textViewEntradas.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tRowFD.addView(textViewEntradas);
        }
        // Finalmente agregar la fila en la primera posición
        tLayoutAFD.addView(tRowFD, 0);
        // Ahora por cada color hacer casi lo mismo
        for (int x = 0; x < estadosAFD.toArray().length; x++) {
            TableRow filaEstado = new TableRow(this);
            filaEstado.setLayoutParams(lp);
            // Borde
            filaEstado.setBackgroundResource(R.drawable.border_table);
            // El nombre del estado
            TextView textViewestado = new TextView(this);

            String estadoAFD = estadosAFD.get(x);

            if (estadoAFD.equals("")) {
                estadoAFD = "Error";
            }

            textViewestado.setText(estadoAFD);
            textViewestado.setTypeface(null, Typeface.BOLD);
            textViewestado.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            filaEstado.addView(textViewestado);
            // Y ahora por cada estado, agregar un campo de texto

            for (int y = 0; y < nEntradas; y++) {

                String estado = estadosAFD.get(x);
                Log.i("Estado :", estado);

                StringBuilder entrada = new StringBuilder();
                Map<Integer, String> entries;
                if (estado.length() >= 2) {
                    for (int j = 0; j < estado.length(); j++) {
                        char c = estado.charAt(j);
                        entries = afndMap.get(String.valueOf(c));
                        Log.i("Map entries :", String.valueOf(entries));

                        String s = entries.get(y + 1);

                        for (int k = 0; k < s.length(); k++) {

                            if(entrada.toString().indexOf(s.charAt(k)) == -1){
                                entrada.append(s.charAt(k));
                            }
                        }


                    }
                } else {
                    if (estado.equals("")) {
                        entrada = new StringBuilder("Error");
                    } else {

                        entries = afndMap.get(estado);
                        Log.i("Map entries :", String.valueOf(entries));
                        entrada = new StringBuilder(entries.get(y + 1));

                        if (entrada.toString().equals("")) {
                            entrada = new StringBuilder("Error");
                        }
                    }

                }

                char[] StringtoChar = entrada.toString().toCharArray();
                Arrays.sort(StringtoChar);
                String SortedString = new String(StringtoChar);

                TextView textViewEntradas = new TextView(this);
                textViewEntradas.setText(SortedString);
                textViewEntradas.setTypeface(null, Typeface.BOLD);
                textViewEntradas.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                filaEstado.addView(textViewEntradas);
                filaEstado.setMinimumWidth(10);
            }
            // Finalmente agregar la fila
            tLayoutAFD.addView(filaEstado);

        }
    }

    private void getDataAFND() {
        afndMap = new HashMap<>();
        sEntradas = new String[nEntradas];

        View row = tLayoutAFND.getChildAt(0);
        if (row instanceof TableRow) {
            TableRow rowC = (TableRow) row;

            for (int j = 1; j < rowC.getChildCount(); j++) {
                TextView texto = (TextView) rowC.getChildAt(j);
                String edttext = texto.getText().toString();
                Log.i("Entrada:", edttext);

                sEntradas[j - 1] = edttext;
            }
        }

        for (int i = 1; i < tLayoutAFND.getChildCount(); i++) {
            row = tLayoutAFND.getChildAt(i);
            if (row instanceof TableRow) {
                TableRow rowC = (TableRow) row;
                Log.i("Estado:", estados[i - 1]);
                Map<Integer, String> entries = new HashMap<>();

                for (int j = 1; j < rowC.getChildCount(); j++) {
                    TextView texto = (TextView) rowC.getChildAt(j);
                    String edttext = texto.getText().toString();
                    Log.i("Informacion:", edttext);

                    entries.put(j, edttext);

                    afndMap.put(estados[i - 1], entries);

                }
            }
        }

        Log.i("Map:", String.valueOf(afndMap));

    }

    private void convertToAFD() {
        Log.i(TAG, "convertToAFD()");

        estadosAFD = new ArrayList<>();
        estadosAFD.add(estados[0]);

        Map<Integer, String> entries;
        boolean isEmpty = false;
        for (int j = 0; j < estadosAFD.size(); j++) {

            String estado = estadosAFD.get(j);
            Log.i(TAG, "convertToAFD() Estado " + estado);

            for (int i = 0; i < nEntradas; i++) {

                StringBuilder entrada = new StringBuilder();

                if (estadosAFD.get(j).length() >= 2) {
                    for (int y = 0; y < estadosAFD.get(j).length(); y++) {
                        char c = estadosAFD.get(j).charAt(y);

                        entries = afndMap.get(String.valueOf(c));

                        String s = entries.get(i + 1);

                        Log.i(TAG, "convertToAFD() Entrada s = " + s);

                        for (int k = 0; k < s.length(); k++) {

                            if(entrada.toString().indexOf(s.charAt(k)) != -1){

                            }else{
                                entrada.append(s.charAt(k));
                            }
                        }
                    }
                } else {
                    entries = afndMap.get(estado);
                    entrada = new StringBuilder(entries.get(i + 1));

                }
                Log.i(TAG, "convertToAFD() Entrada " + entrada);

                char[] StringtoChar = entrada.toString().toCharArray();
                Arrays.sort(StringtoChar);
                String SortedString = new String(StringtoChar);

                if (!SortedString.equals("")) {
                    boolean isEstado = estadosAFD.contains(SortedString);
                    if (!isEstado) {
                        estadosAFD.add(SortedString);
                    }
                } else {
                    isEmpty = true;
                }

            }
        }
        if (isEmpty) {
            estadosAFD.add("");
        }
        Log.i(TAG, "convertToAFD() estadosAFD " + estadosAFD.toString());


    }

    private void showDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("El automata no es FND.  \nPor favor ingrese uno valido")
                .setTitle("Validate Automata")
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    dialog.dismiss();
                });


        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

}