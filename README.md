# TestShareUid
进程通信中可以设置使用同样的shareUid来共享数据
这是一个例子：在app中将"123"写入到data/data/xxx/file/settings.dat中，然后在secModule中取出来读取写入，并获取app中的drawable string等资源

两个应用设置android:sharedUserId="com.gdhuo.test"，两个应用使用同一个加密文件加密后运行在手机中。

从log看pid是不一样的，但是uid是一样的

1. 首先在app以及secModule中打印出自己的进程以及uid

App中
2019-04-08 10:32:08.535 13715-13715/? D/MainActivity: main process pid:13715
2019-04-08 10:32:08.535 13715-13715/? D/MainActivity: main process uid:10123
secModule中
2019-04-08 10:32:33.323 13911-13911/? D/SecActivity: second process pid:13911
2019-04-08 10:32:33.323 13911-13911/? D/SecActivity: second process uid:10123

从打出来log看属于不同的进程但是uid是一样的

2.app中通过IO流将"123"写入到settings.dat中

FileOutputStream fOut = null;
OutputStreamWriter osw = null;
try {
    //默认建立在data/data/xxx/file/
    fOut = openFileOutput("settings.dat", MODE_PRIVATE);
    osw = new OutputStreamWriter(fOut);
    osw.write(data);//data:"123"
    osw.flush();
    Toast.makeText(context, "Settings saved", Toast.LENGTH_SHORT)
            .show();
} catch (Exception e) {
...
}
