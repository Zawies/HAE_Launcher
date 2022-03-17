package uk.co.dooapp.hae_launcher;

import androidx.appcompat.app.AppCompatActivity;



import android.content.Intent;

import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import uk.co.dooapp.hae_launcher.repositories.WeatherRepository;
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
        initWeatherInfoLiveData();
    }

    private void addAppListListener() {
        ImageButton appList = findViewById(R.id.appList);
        appList.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AppLauncherActivity.class);
            startActivity(intent);
        });
    }

    private void initWeatherInfoLiveData(){
        mainViewModel.makeWeatherRequest();
        mainViewModel.receiveBatteryUpdates();
        mainViewModel.getWeatherInfo().observe(this, weatherInfo -> {
            cityName.setText(weatherInfo.getCityName());
            countryName.setText(weatherInfo.getCounty());
            temperature.setText(weatherInfo.getTemperature() + "\u2103" );
            description.setText(weatherInfo.getDescription());
        });

        mainViewModel.getIsUpdating().observe(this, aBoolean -> {
            if(aBoolean)
                progressBar.setVisibility(View.VISIBLE);
            else
                progressBar.setVisibility(View.GONE);
        });

        mainViewModel.getBatteryLevel().observe(this, batteryInfo -> {
            if(batteryInfo.getStatus() == BatteryManager.BATTERY_STATUS_CHARGING ||
                    batteryInfo.getStatus() == BatteryManager.BATTERY_STATUS_FULL)
                batteryLevel.setText("Charging " + batteryInfo.getLevel() + " %");
            else
                batteryLevel.setText(batteryInfo.getLevel() + " %");
        });
    }
}