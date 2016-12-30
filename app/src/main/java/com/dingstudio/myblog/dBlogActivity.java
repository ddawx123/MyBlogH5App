package com.dingstudio.myblog;

import com.readystatesoftware.systembartint.SystemBarTintManager;  //导入Github开源的沉浸式状态栏模块（jar包位于libs目录，已在build.gradle中进行了声明。）

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class dBlogActivity extends AppCompatActivity {

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case 1:            //Menu.FIRST对应itemid为1
                startActivity(new Intent(dBlogActivity.this,myActivity.class));
                return true;
            case 2:
                Toast toast = Toast.makeText(getApplicationContext(), "公测版本暂无此功能，敬请期待。如果需要获取内测版本，请联系浅忆！", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            default:
                return false;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,R.string.app_name);    //添加选项
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    private long exitTime = 0; //声明再按一次退出程序所需的变量
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast toast = Toast.makeText(getApplicationContext(), "再按一次退出浅忆博客", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                exitTime = System.currentTimeMillis();
            }
            else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {  //设置透明状态栏
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    private void webviewinit(){
        final WebView webview = (WebView) findViewById(R.id.webView);  //建立WebView所需控件
        WebSettings settings = webview.getSettings();  //获取webview设置
        settings.setJavaScriptEnabled(true);  //打开JS支持
        //WebView加载web资源
        webview.loadUrl("http://blog.dingstudio.cn");
        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
        webview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {  //表示按返回键
                        webview.goBack();   //后退
                        return true;    //已处理
                    }
                }
                return false;
            }
        });
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webview.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view,url);
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, android.net.http.SslError error) {
                // 重写此方法可以让webview处理https请求
                handler.proceed();
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                webview.loadUrl("file:///android_asset/error.html");  //没有网络的错误处理页面
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_blog);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); setContentView(R.layout.activity_d_blog);
        // 4.4及以上版本开启沉浸
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        // 自定义颜色
        tintManager.setTintColor(Color.parseColor("#0000CD"));

        Toast toast = Toast.makeText(getApplicationContext(), "正在载入浅忆博客，加载速度由您的网络状况决定！", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        webviewinit();
    }
}



