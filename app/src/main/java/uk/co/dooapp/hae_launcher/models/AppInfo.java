package uk.co.dooapp.hae_launcher.models;

import android.graphics.drawable.Drawable;

public class AppInfo {
    private String packageName;
    private CharSequence name;
    private Drawable icon;


    public AppInfo(){

    }
    public AppInfo(CharSequence name, String packageName, Drawable icon) {
        this.name = name;
        this.packageName = packageName;
        this.icon = icon;
    }

    public CharSequence getName() {
        return name;
    }

    public void setName(CharSequence name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
