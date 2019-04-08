package com.gdhuo.secmodule;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class SecActivity extends AppCompatActivity {
    private static final String TAG = SecActivity.class.getSimpleName();
    private TextView tvTitle;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec);
        tvTitle = findViewById(R.id.tvTitle);
        imageView = findViewById(R.id.imageView);
        //1.shareUid相同的多个apk pid是不一样的，但是Uid一样！！！
        Log.d(TAG,"second process pid:"+Process.myPid());
        Log.d(TAG,"second process uid:"+Process.myUid());
        //2.测试不通过制定路径而是通过context获取file，并对其操作
        try {
            //获取程序A的context
            Context ctx = this.createPackageContext(
                    "com.gdhuo.testshareuid",             Context.CONTEXT_IGNORE_SECURITY);
            String msg = ReadSettings(ctx);
            Toast.makeText(this, "DealFile2 Settings read" + msg,
                    Toast.LENGTH_SHORT).show();
            WriteSettings(ctx, "deal file2 write");

            //3.测试通过createPackageContext获取对应的String资源，图片资源
            getResInfo(ctx);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public String ReadSettings(Context context) {
        FileInputStream fIn = null;
        InputStreamReader isr = null;
        char[] inputBuffer = new char[255];
        String data = null;
        try {
            //此处调用并没有区别，但context此时是从程序A里面获取的
            fIn = context.openFileInput("settings.dat");
            isr = new InputStreamReader(fIn);
            isr.read(inputBuffer);
            data = new String(inputBuffer);
            tvTitle.setText(data);
            Toast.makeText(context, "Settings read", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Settings not read", Toast.LENGTH_SHORT)
                    .show();
        } finally {
            try {
                isr.close();
                fIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public void WriteSettings(Context context, String data) {
        FileOutputStream fOut = null;
        OutputStreamWriter osw = null;
        try {
            fOut = context.openFileOutput("settings.dat", MODE_PRIVATE);
            //此处调用并没有区别，但context此时是从程序A里面获取的
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

    private void getResInfo(Context context) {
        Resources res = context.getResources();
        //R.Drawable.contact
        int xId = res.getIdentifier("contact", "drawable", "com.gdhuo.testshareuid");
        //R.String.test_str
        int yId = res.getIdentifier("test_str", "string", "com.gdhuo.testshareuid");
        imageView.setImageDrawable(res.getDrawable(xId));
        Toast.makeText(this, "remote str is"+res.getString(yId), Toast.LENGTH_SHORT).show();
    }
}
