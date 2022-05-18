package com.stebanramos.convertautomata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

public class GrafoActivity extends AppCompatActivity {

    private ArrayList<String> estados, eAceptacion;
    private ArrayList<String> posEstados;
    Map<String, Map<Integer, String>> afdMap;
    private int nEntradas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new viewGrafo(this));

        posEstados = new ArrayList<String>();
        estados = getIntent().getStringArrayListExtra("estadosAFD");
        eAceptacion = getIntent().getStringArrayListExtra("eAceptacion");
        afdMap = (Map<String, Map<Integer, String>>) getIntent().getSerializableExtra("afdMap");
        nEntradas = getIntent().getIntExtra("nEntradas", 0);
    }

    public class viewGrafo extends View {
        public viewGrafo(Context context) {
            super(context);
        }

        protected void onDraw(Canvas canvas) {
            Paint paint = new Paint();
            Paint text = new Paint();

            paint.setColor(Color.GRAY);
            paint.setStrokeWidth(8);
            paint.setStyle(Paint.Style.FILL);

            text.setColor(Color.BLACK);
            text.setStrokeWidth(8);
            text.setTextSize(50);
            text.setStyle(Paint.Style.FILL);

            int x = 200, y = 200;
            int radio = 150;
            int xStart = x, yStart = y;
            for (int i = 0; i < estados.size(); i++) {
                String estado = estados.get(i);
                if (estado.equals("")) {
                    estado = "Error";
                }

                for (int j = 0; j < eAceptacion.size(); j++) {

                    if (estados.get(i).equals(eAceptacion.get(j))){
                        paint.setColor(Color.GREEN);
                    }else{
                        paint.setColor(Color.GRAY);
                    }

                }

                if (i == 0){
                    paint.setColor(Color.BLUE);
                }

                canvas.drawCircle(x, y, radio, paint);
                canvas.drawText(estado, x, y, text);
                posEstados.add(x + "," + y);

                if (i % 2 == 0) {
                    x = x * 2 + radio + 150;
                } else {
                    x = 200;
                    y = y + radio + 300;
                }

            }
            Log.i("d_funciones", "map " + afdMap);
            for (int i = 0; i < estados.size(); i++) {
                String estado = estados.get(i);
                if (estado.equals("")) {
                    estado = "Error";
                }
                String[] coorStart = posEstados.get(i).split(",");
                xStart = Integer.parseInt(coorStart[0]);
                yStart = Integer.parseInt(coorStart[1]);
                Log.i("d_funciones", "coorStart " + xStart + "," + yStart);

                Map<Integer, String> entries = afdMap.get(estado);
                Log.i("d_funciones", "entries " + entries);

                for (int j = 0; j < nEntradas; j++) {

                    String estatoTo = entries.get(j);

                    if (estatoTo.equals("")) {
                        estatoTo = "Error";
                    }

                    int xEnd = 0, yEnd = 0;
                    for (int k = 0; k < estados.size(); k++) {

                        String estadoEnd = estados.get(k);
                        Log.i("d_funciones", "estatoTo " + estatoTo + " = " + estadoEnd);
                        if (estadoEnd.equals("")) {
                            estadoEnd = "Error";
                        }
                        if (estatoTo.equals(estadoEnd)) {
                            String[] coorEnd = posEstados.get(k).split(",");
                            xEnd = Integer.parseInt(coorEnd[0]);
                            yEnd = Integer.parseInt(coorEnd[1]);
                            Log.i("d_funciones", "coorEnd " + xEnd + "," + yEnd);

                            if (!estatoTo.equals(estado)) {
                                text.setColor(Color.BLACK);
                                canvas.drawLine(xStart + 20, yStart + 20, xEnd - 20, yEnd - 20, text);
                                text.setColor(Color.RED);
                                canvas.drawPoint(xEnd - 20, yEnd - 20, text);
                            }

                        }
                    }


                }
            }
        }
    }
}