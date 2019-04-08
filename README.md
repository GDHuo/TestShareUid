# TestShareUid

进程通信中可以设置使用同样的shareUid来共享数据


这是一个例子：在app中将"123"写入到data/data/xxx/file/settings.dat中，然后在secModule中取出来读取写入，并获取app中的drawable string等资源


两个应用设置android:sharedUserId="com.gdhuo.test"，两个应用使用同一个加密文件加密后运行在手机中。


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
    
} catch (Exception e) {

...

}

secModule中通过createPackageContext("com.gdhuo.testshareuid",Context.CONTEXT_IGNORE_SECURITY);获取app的context，然后从"settings.dat"中读取及写入字符串

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
    
    
} catch (Exception e) {

...

}

从context中获取app的drawable 以及String

Resources res = context.getResources();

int xId = res.getIdentifier("contact", "drawable", "com.gdhuo.testshareuid");

int yId = res.getIdentifier("test_str", "string", "com.gdhuo.testshareuid");


可以获取到并显示
