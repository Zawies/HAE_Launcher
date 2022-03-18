package uk.co.dooapp.hae_launcher.repositories;


import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import uk.co.dooapp.hae_launcher.models.WeatherInfo;
import uk.co.dooapp.hae_launcher.utils.WeatherResultParser;

public class WeatherRepository {
    private final ScheduledExecutorService executor;
    private ScheduledFuture<?> future;
    private final List<String> cityList = Arrays.asList("beijing", "berlin", "cardiff", "edinburgh", "london", "nottingham");
    public WeatherRepository(ScheduledExecutorService executor){
        this.executor = executor;
    }

    public void receiveWeatherUpdates(final RepositoryCallback<WeatherInfo> callback){
        WeatherResultParser weatherResultParser = new WeatherResultParser();
        future = executor.scheduleAtFixedRate(new Runnable() {
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

    public void cancelWeatherUpdates(){
        future.cancel(true);
    }

    public boolean isUpdatesCancelled(){
        return future.isCancelled();
    }
}
