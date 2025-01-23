package id.ac.polihasnur.ti.tokopedia;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hide statusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //change activity in 3 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent openLogin = new Intent(MainActivity.this, Login.class);
                startActivity(openLogin);
                finish();
            }
        }, 3000);
    }
}
