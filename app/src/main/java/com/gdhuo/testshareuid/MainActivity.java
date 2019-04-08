package com.gdhuo.testshareuid;

import android.content.Context;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTitle = findViewById(R.id.tvTitle);
        //1.shareUid相同的多个apk pid是不一样的，但是Uid一样！！！
        Log.d(TAG,"main process pid:"+Process.myPid());
        Log.d(TAG,"main process uid:"+Process.myUid());
        //2.测试不通过制定路径而是通过context获取file，并对其操作
        WriteSettings(this, "123");
        //3.测试通过createPackageContext获取对应的String资源，图片资源
    }

    public void WriteSettings(Context context, String data) {
        FileOutputStream fOut = null;
        OutputStreamWriter osw = null;
        try {
            //默认建立在data/data/xxx/file/
            fOut = openFileOutput("settings.dat", MODE_PRIVATE);
            osw = new OutputStreamWriter(fOut);
            osw.write(data);
            osw.flush();
            Toast.makeText(context, "Settings saved", Toast.LENGTH_SHORT)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Settings not saved", Toast.LENGTH_SHORT)
                    .show();
        } finally {
            try {
                osw.close();
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
