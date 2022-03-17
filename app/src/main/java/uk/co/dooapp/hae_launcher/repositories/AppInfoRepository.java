package uk.co.dooapp.hae_launcher.repositories;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import uk.co.dooapp.hae_launcher.models.AppInfo;

public class AppInfoRepository {
    private Executor executor;
    private Context context;

    public AppInfoRepository(Executor executor, Context context){
        this.executor = executor;
        this.context = context;
    }


    public Result<List<AppInfo>> getAppList(){
        PackageManager pm = context.getPackageManager();
        List<AppInfo> apps = new ArrayList<>();

        try {
            Intent i = new Intent(Intent.ACTION_MAIN, null);
            i.addCategory(Intent.CATEGORY_LAUNCHER);

            List<ResolveInfo> allApps = pm.queryIntentActivities(i, 0);
            for (ResolveInfo ri : allApps) {
                AppInfo app = new AppInfo();
                app.setName(ri.loadLabel(pm));
                app.setPackageName(ri.activityInfo.packageName);
                app.setIcon(ri.activityInfo.loadIcon(pm));
                apps.add(app);
            }
            return new Result.Success<List<AppInfo>>(apps);
        } catch (Exception e){
            return new Result.Error<List<AppInfo>>(e);
        }
    }

    public void makeAppListRequest(final RepositoryCallback<List<AppInfo>> callback){

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Result<List<AppInfo>> response = getAppList();
                    callback.onComplete(response);
                } catch (Exception e) {
                    Result<List<AppInfo>> errorResult = new Result.Error<>(e);
                    callback.onComplete(errorResult);
                }
            }
        });
    }
}
