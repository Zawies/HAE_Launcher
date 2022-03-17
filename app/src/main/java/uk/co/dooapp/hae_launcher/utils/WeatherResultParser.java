package uk.co.dooapp.hae_launcher.utils;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import uk.co.dooapp.hae_launcher.models.WeatherInfo;
import uk.co.dooapp.hae_launcher.repositories.Result;

public class WeatherResultParser {
    public Result<WeatherInfo> getWeatherInfo(String city){
        final WeatherInfo[] locationItems = {new WeatherInfo()};
        Handler handler = new Handler(Looper.getMainLooper());
        final String[] response = new String[1];
        try {
            HttpURLConnection con = (HttpURLConnection) new URL("http://weather.bfsah.com/" + city).openConnection();
            response[0] = convertStreamToString(con.getInputStream());
            locationItems[0] = new WeatherInfo();
            JSONObject object = new JSONObject(response[0]);
            locationItems[0].setCityName(object.getString("city"));
            locationItems[0].setCounty(object.getString("country"));
            locationItems[0].setTemperature(object.getInt("temperature"));
            locationItems[0].setDescription(object.getString("description"));
            //more string like ;
            return new Result.Success<WeatherInfo>(locationItems[0]);
        }catch (Exception e){
            return new Result.Error<WeatherInfo>(e);
        }

    }

    public static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }
}
