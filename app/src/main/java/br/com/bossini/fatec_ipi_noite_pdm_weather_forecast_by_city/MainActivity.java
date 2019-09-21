package br.com.bossini.fatec_ipi_noite_pdm_weather_forecast_by_city;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText locationEditText;
    private List <Weather> weatherList;
    private ListView weatherListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        locationEditText = findViewById(R.id.locationEditText);
        weatherListView = findViewById(R.id.weatherListView);
        weatherList = new LinkedList<>();
        ArrayAdapter <Weather> adapter =
                new ArrayAdapter<Weather>(
                        this,
                        android.R.layout.simple_list_item_1,
                        weatherList
                );
        weatherListView.setAdapter(adapter);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((v) -> {
            String cidade =
                    locationEditText.getEditableText().toString();
            String end =
                    getString(
                        R.string.web_service_url,
                        cidade,
                        getString(
                                R.string.api_key
                        )
                    );

            new Thread (
                    () -> {
                        try{
                            URL url = new URL (end);
                            HttpURLConnection conn =
                                    (HttpURLConnection) url.openConnection();
                            InputStream is =
                                    conn.getInputStream();
                            InputStreamReader isr =
                                    new InputStreamReader(is);
                            BufferedReader reader =
                                    new BufferedReader(isr);
                            String linha = null;
                            StringBuilder resultado =
                                    new StringBuilder("");
                            while ((linha = reader.readLine()) != null){
                                resultado.append(linha);
                            }
                            reader.close();
                            conn.disconnect();
                            runOnUiThread( () -> {
                                Toast.makeText(
                                        this,
                                        resultado.toString(),
                                        Toast.LENGTH_SHORT
                                ).show();
                            });
                        }
                        catch (MalformedURLException e){
                            e.printStackTrace();
                            runOnUiThread(() ->{
                                Toast.makeText(
                                        this,
                                        getString(
                                                R.string.invalid_url
                                        ),
                                        Toast.LENGTH_SHORT
                                ).show();
                            });

                        }
                        catch (IOException e){
                            e.printStackTrace();
                            runOnUiThread(() ->{
                                Toast.makeText(
                                        this,
                                        getString(
                                                R.string.connect_error
                                        ),
                                        Toast.LENGTH_SHORT
                                ).show();
                            });
                        }
                    }
            ).start();


            //Log.i("meulog", end);
        });
    }

}
