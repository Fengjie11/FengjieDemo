package com.example.fengjiedemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.fengjiedemo.adapter.MyAdapter;
import com.example.fengjiedemo.bean.MyDataBean;
import com.example.fengjiedemo.utils.Ok;
import com.example.fengjiedemo.view.XListView;
import com.google.gson.Gson;

import java.util.List;

public class MainActivity extends AppCompatActivity implements XListView.IXListViewListener{

    private static final String TAG = "MainActivity";
    private XListView listView;
    private int start = 0;
    private static int refreshCnt = 0;
    private List<MyDataBean.ResultBean.RowsBean> rows;
    private MyAdapter myAdapter;
    private String s;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (XListView) findViewById(R.id.list_view);
        listView.setPullLoadEnable(true);
        url = "http://api.fang.anjuke." +
                "com/m/android/1.3/shouye/recInfosV3/?" +
                "city_id=14&lat=40.04652&lng=116.306033&api_key=" +
                "androidkey&sig=9317e9634b5fbc16078ab07abb6661c5&" +
                "macid=45cd2478331b184ff0e15f29aaa89e3e&app=a-ajk&" +
                "_pid=11738&o=PE-TL10-user+4.4.2+HuaweiPE-TL10+CHNC00B260+ota-rel-" +
                "keys%2Crelease-keys&from=mobile&m=Android-PE-TL10&" +
                "cv=9.5.1&cid=14&i=864601026706713&v=4.4.2&qtime=20160411091603&" +
                "pm=b61&uuid=1848c59c-185d-48d9-b0e9-782016041109&_chat_id="+start;
        Ok ok=new Ok(url,handler);
        boolean networkAvailable = ok.isNetworkAvailable(MainActivity.this);
        if (networkAvailable){
            Toast.makeText(MainActivity.this,"网络连接正常",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(MainActivity.this,"请检查您的网络",Toast.LENGTH_SHORT).show();
            showNoNetWorkDlg(MainActivity.this);

        }
        boolean wifiEnabled = ok.isWifiEnabled(MainActivity.this);
        if (wifiEnabled){
            Toast.makeText(MainActivity.this,"当前网络为WIFI状态",Toast.LENGTH_SHORT).show();
        }

        ok.sendRequestWithOkHttp();
        listView.setXListViewListener(this);


    }
    //判断是否有网路  然后跳转到设置网路页面
    public static void showNoNetWorkDlg(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.mipmap.ic_launcher)         //
                .setTitle(R.string.app_name)            //
                .setMessage("当前无网络").setPositiveButton("设置", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 跳转到系统的网络设置界面
                Intent intent = null;
                // 先判断当前系统版本
                if(android.os.Build.VERSION.SDK_INT > 10){  // 3.0以上
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                }else{
                    intent = new Intent();
                    intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
                }
                context.startActivity(intent);

            }
        }).setNegativeButton("知道了", null).show();
    }
    Handler handler=new Handler(){


        @Override
        public void handleMessage(Message msg) {
            s = (String) msg.obj;
            Gson gson=new Gson();
            MyDataBean myDataBean = gson.fromJson(s, MyDataBean.class);
            rows = myDataBean.getResult().getRows();
            myAdapter = new MyAdapter(MainActivity.this, rows);
            listView.setAdapter(myAdapter);
        }
    };


    private void onLoad() {
        listView.stopRefresh();
        listView.stopLoadMore();
        listView.setRefreshTime("刚刚");
    }    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                start = ++refreshCnt;
                rows.clear();
                Ok ok=new Ok(url,handler);
                ok.sendRequestWithOkHttp();
                //geneItems();
                // mAdapter.notifyDataSetChanged();
                //mAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.list_item, items);
                listView.setAdapter(new MyAdapter(MainActivity.this,rows));
                onLoad();
            }
        }, 2000);

    }

    @Override
    public void onLoadMore() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                start = ++refreshCnt;
                Ok ok=new Ok(url,handler);
                ok.sendRequestWithOkHttp();
                //geneItems();
                myAdapter.notifyDataSetChanged();
                onLoad();
            }
        }, 2000);

    }
}
