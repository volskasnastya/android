package com.example.myapplication;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class MainActivity extends AppCompatActivity {

    EditText mName;
    EditText mAge;

    private final static String FILE_NAME = "user.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mName = (EditText)findViewById(R.id.name);
        mAge = (EditText)findViewById(R.id.age);
        openText(null);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }
    public void toGet(View view){

        Button get = (Button) findViewById(R.id.get);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        WebView myWebView = (WebView) findViewById(R.id.web);
        myWebView.loadUrl("http://192.168.56.1:80");
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);


    }

    public  void toPost(View view) throws UnsupportedEncodingException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String name = mName.getText().toString();
        String age = mAge.getText().toString();

        WebView web = (WebView)findViewById(R.id.web);
        String postData = initUrlData(name,age);
        web.postUrl("http://192.168.56.1:80",postData.getBytes());

        saveText(view);

    }

    public String initUrlData(String name, String age){
        return name+"&"+age;
    }

    public void saveText(View view){

        FileOutputStream fos = null;
        try {
            String name = mName.getText().toString();
            String age = mAge.getText().toString();
            String text = "Имя: " + name + "& Возраст: " + age;

            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(text.getBytes());
            Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
        }
        catch(IOException  ex) {

            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally{
            try{
                if(fos!=null)
                    fos.close();
            }
            catch(IOException ex){

                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    // открытие файла
    public void openText(View view){

        FileInputStream fin = null;

        try {
            fin = openFileInput(FILE_NAME);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text = new String (bytes);
            String[] result = text.split("&");
            String[] name = result[0].split(":");
            String[] age = result[1].split(":");
            mName.setText(name[1]);
            mAge.setText(age[1]);
        }
        catch(IOException ex) {

            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally{

            try{
                if(fin!=null)
                    fin.close();
            }
            catch(IOException ex){

                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}