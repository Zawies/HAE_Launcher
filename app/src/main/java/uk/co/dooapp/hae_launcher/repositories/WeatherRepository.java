package uk.co.dooapp.hae_launcher.repositories;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.Executor;

import uk.co.dooapp.hae_launcher.models.WeatherInfo;
import uk.co.dooapp.hae_launcher.utils.WeatherResultParser;

public class WeatherRepository {
    private final Executor executor;
    public WeatherRepository(Executor executor){
        this.executor = executor;
    }

    public void makeWeatherRequest(final RepositoryCallback<WeatherInfo> callback){
        WeatherResultParser weatherResultParser = new WeatherResultParser();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Result<WeatherInfo> response = weatherResultParser.getWeatherInfo("lndon");
                    callback.onComplete(response);
                } catch (Exception e) {
                    Result<WeatherInfo> errorResult = new Result.Error<>(e);
                    callback.onComplete(errorResult);
                }
            }
        });
    }
}
