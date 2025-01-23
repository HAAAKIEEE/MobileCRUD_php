package id.ac.polihasnur.ti.tokopedia;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Edit extends AppCompatActivity {
    BottomNavigationView edit_bot_nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        edit_bot_nav = findViewById(R.id.edit_bot_nav);
        edit_bot_nav.setSelectedItemId(R.id.menu_edit);

        edit_bot_nav.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.menu_edit){
                return true;
            } else{
                startActivity(new Intent(Edit.this, Home.class).putExtra("id_user", "1"));
                finish();
                return true;

            }
        });

    }
}