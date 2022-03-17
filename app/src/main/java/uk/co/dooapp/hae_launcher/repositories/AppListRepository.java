package uk.co.dooapp.hae_launcher.repositories;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import uk.co.dooapp.hae_launcher.models.AppInfo;
import uk.co.dooapp.hae_launcher.utils.AppListParser;

public class AppListRepository {
    private Executor executor;
    private Context context;

    public AppListRepository(Executor executor, Context context){
        this.executor = executor;
        this.context = context;
    }

    public void makeAppListRequest(final RepositoryCallback<List<AppInfo>> callback){
        AppListParser appListParser = new AppListParser(context);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Result<List<AppInfo>> response = appListParser.getAppList();
                    callback.onComplete(response);
                } catch (Exception e) {
                    Result<List<AppInfo>> errorResult = new Result.Error<>(e);
                    callback.onComplete(errorResult);
                }
            }
        });
    }
}
