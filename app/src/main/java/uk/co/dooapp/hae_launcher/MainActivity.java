package uk.co.dooapp.hae_launcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import uk.co.dooapp.hae_launcher.models.AppInfo;
import uk.co.dooapp.hae_launcher.models.BatteryInfo;
import uk.co.dooapp.hae_launcher.models.WeatherInfo;
import uk.co.dooapp.hae_launcher.repositories.AppListRepository;
import uk.co.dooapp.hae_launcher.repositories.WeatherRepository;
import uk.co.dooapp.hae_launcher.viewmodels.AppLauncherViewModel;
import uk.co.dooapp.hae_launcher.viewmodels.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private ProgressBar progressBar;
    private TextView cityName, countryName, temperature, description, batteryLevel;
    private WeatherRepository weatherRepository = new WeatherRepository(executor);
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mainViewModel = new MainViewModel(getApplication(), weatherRepository);
        progressBar = findViewById(R.id.progressBar);
        cityName = findViewById(R.id.cityName);
        countryName = findViewById(R.id.countryName);
        temperature = findViewById(R.id.temperature);
        description = findViewById(R.id.description);
        batteryLevel = findViewById(R.id.batteryLevel);
        addAppListListener();
        //registerBatteryReceiver();
        initWeatherInfoLiveData();
    }

    private void addAppListListener() {
        ImageButton appList = findViewById(R.id.appList);
        appList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AppLauncherActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initWeatherInfoLiveData(){
        mainViewModel.makeWeatherRequest();
        mainViewModel.receiveBatteryUpdates();
        mainViewModel.getWeatherInfo().observe(this, new Observer<WeatherInfo>() {
            @Override
            public void onChanged(WeatherInfo weatherInfo) {
                cityName.setText(weatherInfo.getCityName());
                countryName.setText(weatherInfo.getCounty());
                temperature.setText(weatherInfo.getTemperature() + "\u2103" );
                description.setText(weatherInfo.getDescription());
            }
        });

        mainViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean)
                    progressBar.setVisibility(View.VISIBLE);
                else
                    progressBar.setVisibility(View.GONE);
            }
        });

        mainViewModel.getBatteryLevel().observe(this, new Observer<BatteryInfo>() {
            @Override
            public void onChanged(BatteryInfo batteryInfo) {
                if(batteryInfo.getStatus() == BatteryManager.BATTERY_STATUS_CHARGING ||
                        batteryInfo.getStatus() == BatteryManager.BATTERY_STATUS_FULL)
                    batteryLevel.setText("Charging " + batteryInfo.getLevel() + " %");
                else
                    batteryLevel.setText(batteryInfo.getLevel() + " %");
            }
        });
    }
}