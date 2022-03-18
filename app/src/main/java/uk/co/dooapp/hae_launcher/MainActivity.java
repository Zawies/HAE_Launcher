package uk.co.dooapp.hae_launcher;

import androidx.appcompat.app.AppCompatActivity;



import android.content.Intent;

import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import uk.co.dooapp.hae_launcher.databinding.ActivityMainBinding;
import uk.co.dooapp.hae_launcher.repositories.WeatherRepository;
import uk.co.dooapp.hae_launcher.viewmodels.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private WeatherRepository weatherRepository = new WeatherRepository(executor);
    private MainViewModel mainViewModel;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mainViewModel = new MainViewModel(getApplication(), weatherRepository);
        addAppListListener();
        initWeatherInfoLiveData();
    }

    private void addAppListListener() {
        binding.appList.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AppLauncherActivity.class);
            startActivity(intent);
        });
    }

    private void initWeatherInfoLiveData(){
        mainViewModel.makeWeatherRequest();
        mainViewModel.receiveBatteryUpdates();
        mainViewModel.getWeatherInfo().observe(this, weatherInfo -> {
            binding.cityName.setText(weatherInfo.getCityName());
            binding.countryName.setText(weatherInfo.getCounty());
            binding.temperature.setText(weatherInfo.getTemperature() + "\u2103" );
            binding.description.setText(weatherInfo.getDescription());
        });

        mainViewModel.getIsUpdating().observe(this, aBoolean -> {
            if(aBoolean)
                binding.progressBar.setVisibility(View.VISIBLE);
            else
                binding.progressBar.setVisibility(View.GONE);
        });

        mainViewModel.getBatteryLevel().observe(this, batteryInfo -> {
            if(batteryInfo.getStatus() == BatteryManager.BATTERY_STATUS_CHARGING ||
                    batteryInfo.getStatus() == BatteryManager.BATTERY_STATUS_FULL)
                binding.batteryLevel.setText("Charging " + batteryInfo.getLevel() + " %");
            else
                binding.batteryLevel.setText(batteryInfo.getLevel() + " %");
        });
    }

    @Override
    protected void onPause() {
        mainViewModel.cancelWeatherUpdates();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(mainViewModel.isUpdatesCancelled() )
            mainViewModel.makeWeatherRequest();
        super.onResume();
    }
}