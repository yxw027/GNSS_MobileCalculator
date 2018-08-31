package com.rogeriocarmo.gnss_mobilecalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import Codigos.Ecef2LlaConverter;
import Codigos.EpocaGPS;

import static Codigos.ProcessamentoPPS.calcCoordenadas;
import static Codigos.ProcessamentoPPS.calcPseudorange;
import static Codigos.ProcessamentoPPS.calcularMMQ;
import static Codigos.ProcessamentoPPS.escolherEpoca;
import static Codigos.ProcessamentoPPS.escolherEpocaEP01;
import static Codigos.ProcessamentoPPS.readLogger_RawAssets;
import static Codigos.ProcessamentoPPS.readRINEX_RawAssets;

public class MainActivity extends AppCompatActivity {

    private Button btnVisualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnVisualizar = findViewById(R.id.idVisualizar);

        btnVisualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Resultado.class);
                Ecef2LlaConverter.GeodeticLlaValues valores =

                Ecef2LlaConverter.convertECEFToLLACloseForm(
                            3687512.700731742,
                            -4620834.607939523,
                            -2387174.1063294816);

                intent.putExtra("Coord",valores);
                startActivity(intent);

                Log.i("THE_END","O PROGRAMA FOI FINALIZADO COM SUCESSO! xD");
            }
        });

        try {
            readLogger_RawAssets(MainActivity.this);
        } catch (IOException e) {
            Log.e("ERR_log","Erro ao abrir o arquivo de Log");
            String msg = e.getMessage();
            Toast.makeText(getApplicationContext(),
                "Erro ao abrir o arquivo de log: " + msg, Toast.LENGTH_LONG).show();
        }

        try{
            calcPseudorange();
        } catch (Exception e){
            Log.e("ERR_pr","Erro ao calcular pseudodistâncias");
            String msg = e.getMessage();
            Toast.makeText(getApplicationContext(),
                "Erro ao calcular as pseudodistâncias: " + msg, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        try {
            readRINEX_RawAssets(MainActivity.this);
        } catch (IOException e) {
            Log.e("ERR_ef","Erro ao abrir o RINEX");
            String msg = e.getMessage();
            Toast.makeText(getApplicationContext(),
                "Erro ao abrir o arquivo de efemérides: " + msg, Toast.LENGTH_LONG).show();
        }

        //TODO PROCESSAMENTO DE UMA ÚNICA ÉPOCA
        try{
            EpocaGPS epoca = escolherEpocaEP01(0);
            calcCoordenadas(epoca);
            calcularMMQ(); // para a época atual
        } catch (Exception e){
            Log.e("ERR_coord","Execucao unica");
            e.printStackTrace();
            String msg = e.getMessage();
            Toast.makeText(getApplicationContext(),
                "Erro ao calcular as coordenadas do satélite: " + msg,
                Toast.LENGTH_LONG).show();
        }

    }
}



















