package necer.edit;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private ListView mListView;
    private AppInfoAdapter appInfoAdapter;
    private List<AppInfo> appInfoList;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.lv_);

        appInfoList = new ArrayList<>();
        appInfoAdapter = new AppInfoAdapter(this, appInfoList);
        mListView.setAdapter(appInfoAdapter);
        mListView.setOnItemClickListener(this);


        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> listAppcations = packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        Collections.sort(listAppcations, new ApplicationInfo.DisplayNameComparator(packageManager));// 排序

        for (ApplicationInfo app : listAppcations) {
            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                appInfoList.add(new AppInfo(app.loadIcon(packageManager), app.loadLabel(packageManager).toString(), app.packageName, app.enabled));
            }
        }

        appInfoAdapter.notifyDataSetChanged();


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final AppInfo appInfo = appInfoList.get(position);
        new AlertDialog.Builder(this)
                .setTitle(appInfo.isEnable() ? ("确认冻结" + appInfo.getAppLabel()) : ("确认解冻" + appInfo.getAppLabel()))
                .setNegativeButton(appInfo.isEnable() ? "冻结" : "解冻", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dealApp(appInfo);
                    }
                }).setPositiveButton("取消", null).create().show();
    }

    private void dealApp(final AppInfo appInfo) {
        Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                if (appInfo.isEnable()) {
                    subscriber.onNext(freezeApp(appInfo));
                } else {
                    subscriber.onNext(unFreezeApp(appInfo));
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        dialog = new ProgressDialog(MainActivity.this);
                        dialog.setMessage(appInfo.isEnable() ? "正在冻结..." : "正在解冻...");
                        dialog.show();
                    }

                    @Override
                    public void onCompleted() {
                        dialog.dismiss();
                        appInfo.setEnable(!appInfo.isEnable());
                        appInfoAdapter.notifyDataSetChanged();

                        Toast.makeText(MainActivity.this, "操作成功！", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }
                });
    }

    private Void unFreezeApp(AppInfo appInfo) {
        Utils.runCmd("pm unblock " + appInfo.getPackName() + ";pm enable " + appInfo.getPackName());
        return null;
    }

    private Void freezeApp(AppInfo appInfo) {
        Utils.runCmd("pm block " + appInfo.getPackName() + ";pm disable " + appInfo.getPackName());
        return null;
    }

}
