package uk.co.dooapp.hae_launcher.repositories;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import uk.co.dooapp.hae_launcher.models.WeatherInfo;
import uk.co.dooapp.hae_launcher.utils.WeatherResultParser;

public class WeatherRepository {
    private final ScheduledExecutorService executor;
    private final List<String> cityList = Arrays.asList("beijing", "berlin", "cardiff", "edinburgh", "london", "nottingham");
    public WeatherRepository(ScheduledExecutorService executor){
        this.executor = executor;
    }

    public void makeWeatherRequest(final RepositoryCallback<WeatherInfo> callback){
        WeatherResultParser weatherResultParser = new WeatherResultParser();
        executor.scheduleAtFixedRate(new Runnable() {
            int cityListPosition = 0;
            @Override
            public void run() {
                try {
                    Result<WeatherInfo> response = weatherResultParser.getWeatherInfo(cityList.get(cityListPosition));
                    callback.onComplete(response);
                    cityListPosition++;
                    if(cityListPosition == cityList.size())
                        cityListPosition = 0;
                } catch (Exception e) {
                    Result<WeatherInfo> errorResult = new Result.Error<>(e);
                    callback.onComplete(errorResult);
                }
            }
        }, 0, 5, TimeUnit.SECONDS);
    }
}
