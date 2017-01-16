package necer.edit;

import android.graphics.drawable.Drawable;

/**
 * Created by necer on 2017/1/16.
 */

public class AppInfo {

    private String appLabel;
    private Drawable appIcon;
    private String packName;
    private boolean isEnable;


    public AppInfo(Drawable appIcon,String appLabel,String packName,boolean isEnable) {
        this.appLabel = appLabel;
        this.appIcon = appIcon;
        this.packName = packName;
        this.isEnable = isEnable;
    }


    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getAppLabel() {
        return appLabel;
    }

    public void setAppLabel(String appLabel) {
        this.appLabel = appLabel;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
}
