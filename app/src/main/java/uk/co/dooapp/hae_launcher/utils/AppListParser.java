package uk.co.dooapp.hae_launcher.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.List;

import uk.co.dooapp.hae_launcher.models.AppInfo;
import uk.co.dooapp.hae_launcher.repositories.Result;

public class AppListParser {
    private Context context;
    public AppListParser(Context context){
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
}
