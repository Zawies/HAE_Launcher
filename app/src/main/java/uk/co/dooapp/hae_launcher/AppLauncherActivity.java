package uk.co.dooapp.hae_launcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import uk.co.dooapp.hae_launcher.adapters.AppListRecyclerViewAdapter;
import uk.co.dooapp.hae_launcher.models.AppInfo;
import uk.co.dooapp.hae_launcher.repositories.AppListRepository;
import uk.co.dooapp.hae_launcher.viewmodels.AppLauncherViewModel;

public class AppLauncherActivity extends AppCompatActivity {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_launcher);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initViews();
        initAppListLiveData();
    }

    private void initViews(){
        recyclerView = findViewById(R.id.appRecyclerView);
        progressBar = findViewById(R.id.progressBar);
    }

    private void initAppListLiveData(){
        AppListRepository appInfoRepository = new AppListRepository(executor, this);
        AppLauncherViewModel appLauncherViewModel = new AppLauncherViewModel(getApplication(), appInfoRepository);
        appLauncherViewModel.makeAppListRequest();
        appLauncherViewModel.getAppList().observe(this, new Observer<List<AppInfo>>() {
            @Override
            public void onChanged(List<AppInfo> appInfos) {
                setUpRecyclerView(appInfos);
            }
        });
        appLauncherViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean)
                    progressBar.setVisibility(View.VISIBLE);
                else
                    progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setUpRecyclerView(List<AppInfo>  appList){
        AppListRecyclerViewAdapter adapter = new AppListRecyclerViewAdapter(this, (ArrayList<AppInfo>) appList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}