package uk.co.dooapp.hae_launcher.viewmodels;

import android.app.Activity;
import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import uk.co.dooapp.hae_launcher.MainActivity;
import uk.co.dooapp.hae_launcher.models.BatteryInfo;
import uk.co.dooapp.hae_launcher.models.WeatherInfo;
import uk.co.dooapp.hae_launcher.repositories.RepositoryCallback;
import uk.co.dooapp.hae_launcher.repositories.Result;
import uk.co.dooapp.hae_launcher.repositories.WeatherRepository;
import uk.co.dooapp.hae_launcher.utils.BatteryReceiver;

public class MainViewModel extends AndroidViewModel {
    private final WeatherRepository weatherRepository;
    private final MutableLiveData<WeatherInfo> weatherInfo = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    private  final MutableLiveData<BatteryInfo> batteryLevel = new MutableLiveData<>();
    public MainViewModel(@NonNull Application application, WeatherRepository weatherRepository) {
        super(application);
        this.weatherRepository = weatherRepository;
    }

    public void makeWeatherRequest(){
        isUpdating.setValue(true);
        weatherRepository.makeWeatherRequest(new RepositoryCallback<WeatherInfo>() {
            @Override
            public void onComplete(Result<WeatherInfo> result) {
                System.out.println("Making weather request");
                if (result instanceof Result.Success) {
                    weatherInfo.postValue(((Result.Success<WeatherInfo>) result).data);
                    isUpdating.postValue(false);
                } else {
                    System.out.println(((Result.Error<WeatherInfo>) result).exception.toString());
                    getApplication().getApplicationContext().getMainExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(getApplication().getApplicationContext().getClass().getSimpleName());
                            Toast.makeText(getApplication().getApplicationContext(), ((Result.Error<WeatherInfo>) result).exception.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                    System.out.println("blad");
                    isUpdating.postValue(false);
                }
            }
        });
    }
    public void receiveBatteryUpdates(){
        BatteryReceiver batteryReceiver = new BatteryReceiver(getApplication().getApplicationContext());
        batteryReceiver.registerBatteryReceiver(batteryLevel);
    }
    public MutableLiveData<WeatherInfo> getWeatherInfo(){
        return weatherInfo;
    }

    public MutableLiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }

    public MutableLiveData<BatteryInfo> getBatteryLevel(){
        return batteryLevel;
    }
}
