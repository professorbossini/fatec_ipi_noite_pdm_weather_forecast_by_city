package br.com.bossini.fatec_ipi_noite_pdm_weather_forecast_by_city;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class WeatherAdapter extends ArrayAdapter<Weather> {

    private Context context;
    private List <Weather> previsoes;
    public WeatherAdapter (Context context,
                           List<Weather> previsoes){
        super(context, -1, previsoes);
        this.context = context;
        this.previsoes = previsoes;
    }

    @Override
    public int getCount() {
        return previsoes.size();
    }

    private class WeatherViewHolder {
        ImageView conditionImageView;
        TextView dayTextView;
        TextView lowTextView;
        TextView highTextView;
        TextView humidityTextView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        WeatherViewHolder vh = null;
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(
                    R.layout.list_item,
                    parent,
                    false
            );
            vh = new WeatherViewHolder();
            vh.conditionImageView =
                    convertView.findViewById (R.id.conditionImageView);
            vh.dayTextView =
                    convertView.findViewById(R.id.dayTextView);
            vh.lowTextView =
                    convertView.findViewById(R.id.lowTextView);
            vh.highTextView =
                    convertView.findViewById(R.id.highTextView);
            vh.humidityTextView =
                    convertView.findViewById(R.id.humidityTextView);
            convertView.setTag(vh);
        }
        else
            vh = (WeatherViewHolder) convertView.getTag();

        Weather previsaoDaVez = previsoes.get(position);


        vh.dayTextView.setText(
                context.getString(
                        R.string.day_description,
                        previsaoDaVez.dayOfWeek,
                        previsaoDaVez.description
                )
        );
        /*dayTextView.setText(
                Integer.toString(convertView.hashCode())
        );*/
        vh.lowTextView.setText(
                context.getString(
                        R.string.low_temp,
                        previsaoDaVez.minTemp
                )
        );
        vh.highTextView.setText(
                context.getString(
                        R.string.high_temp,
                        previsaoDaVez.maxTemp
                )
        );
        vh.humidityTextView.setText(
                context.getString(
                        R.string.humidity,
                        previsaoDaVez.humidity
                )
        );
        baixarImagem (previsaoDaVez,  vh.conditionImageView);

        return convertView;

    }

    private void baixarImagem (Weather previsaoDaVez,
                               ImageView conditionImageView){
        new Thread (() -> {
            try{
                URL url = new URL (previsaoDaVez.iconURL);
                HttpURLConnection conn =
                        (HttpURLConnection)
                        url.openConnection();
                InputStream is =
                        conn.getInputStream();
                Bitmap figura =
                        BitmapFactory.decodeStream(is);
                ((Activity)context).runOnUiThread(
                        () -> {
                            conditionImageView.setImageBitmap(
                                    figura
                            );
                        }
                );
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }).start();
    }
}
