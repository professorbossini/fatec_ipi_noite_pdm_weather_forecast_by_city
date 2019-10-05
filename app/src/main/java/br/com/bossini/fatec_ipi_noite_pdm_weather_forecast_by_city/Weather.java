package br.com.bossini.fatec_ipi_noite_pdm_weather_forecast_by_city;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Weather {

    public final String dayOfWeek;
    public final String minTemp;
    public final String maxTemp;
    public final String humidity;
    public final String description;
    public final String iconURL;

    public Weather (
            long timeStamp,
            double minTemp,
            double maxTemp,
            double humidity,
            String description,
            String iconName
    ){
        this.dayOfWeek =
                convertTimestampToDay(timeStamp);
        NumberFormat nf =
                NumberFormat.getInstance();
        nf.setMinimumFractionDigits(0);
        this.minTemp = nf.format(minTemp);
        this.maxTemp = nf.format(maxTemp);
        NumberFormat pf =
                NumberFormat.getPercentInstance();
        this.humidity =
                pf.format(humidity / 100d);
        this.description = description;
        this.iconURL =
                String.format(
                        Locale.getDefault(),
                        "http://openweathermap.org/img/w/%s.png",
                        iconName
                );

    }
    private String convertTimestampToDay (long timeStamp){
        Calendar agora = Calendar.getInstance();
        agora.setTimeInMillis(timeStamp * 1000);
        TimeZone fusoHorario = TimeZone.getDefault();
        agora.add(Calendar.MILLISECOND,
                fusoHorario.getOffset(
                        agora.getTimeInMillis()
                ));
        return new SimpleDateFormat("E hh:mm").format(
                agora.getTime()
        );
    }

    @Override
    public String toString() {
        return "Weather{" +
                "dayOfWeek='" + dayOfWeek + '\'' +
                ", minTemp='" + minTemp + '\'' +
                ", maxTemp='" + maxTemp + '\'' +
                ", humidity='" + humidity + '\'' +
                ", description='" + description + '\'' +
                ", iconURL='" + iconURL + '\'' +
                '}';
    }
}
