package uk.co.dooapp.hae_launcher.viewmodels;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import uk.co.dooapp.hae_launcher.models.AppInfo;
import uk.co.dooapp.hae_launcher.repositories.AppListRepository;
import uk.co.dooapp.hae_launcher.repositories.Result;

public class AppLauncherViewModel extends AndroidViewModel  {
    private final AppListRepository appListRepository;
    private MutableLiveData<List<AppInfo>> appList = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    public AppLauncherViewModel(@NonNull Application application, AppListRepository appListRepository) {
        super(application);
        this.appListRepository = appListRepository;
    }

    public void makeAppListRequest(){
        isUpdating.setValue(true);
        appListRepository.makeAppListRequest(result -> {
            if (result instanceof Result.Success) {
                appList.postValue(((Result.Success<List<AppInfo>>) result).data);
            } else {
                getApplication().getMainExecutor().execute(() ->
                        Toast.makeText(getApplication().getApplicationContext(), ((Result.Error<List<AppInfo>>) result).exception.getLocalizedMessage(), Toast.LENGTH_LONG).show());

            }
            isUpdating.postValue(false);
        });
    }

    public LiveData<List<AppInfo>> getAppList(){
        return appList;
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }
}
