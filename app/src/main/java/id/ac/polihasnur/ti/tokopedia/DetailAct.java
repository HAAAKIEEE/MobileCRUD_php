package id.ac.polihasnur.ti.tokopedia;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.InputStream;

public class DetailAct extends AppCompatActivity {
    BottomNavigationView edit_bot_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // Bottom Navigation setup
        edit_bot_nav = findViewById(R.id.edit_bot_nav);

        edit_bot_nav.setOnItemSelectedListener(item -> {

            startActivity(new Intent(DetailAct.this, Home.class).putExtra("id_user", "1"));
            finish();
            return true;
        });
        Button sunting = findViewById(R.id.sunting);
        // Ambil data dari Intent
        String id = getIntent().getStringExtra("item_id");
        String name = getIntent().getStringExtra("item_name");
        String description = getIntent().getStringExtra("item_description");
        String imageUrl = getIntent().getStringExtra("item_image");

        // Temukan views
        TextView detailName = findViewById(R.id.detail_name);
        TextView detailDescription = findViewById(R.id.detail_description);
        ImageView detailImage = findViewById(R.id.detail_image);

        // Set data ke views
        detailName.setText(name);
        detailDescription.setText(description);

        // Memuat gambar menggunakan AsyncTask
        new LoadImageTask(detailImage).execute(imageUrl);

        // Listener untuk tombol sunting
        sunting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailAct.this, Edit.class);
                // Kirim data ke Edit.java
                intent.putExtra("item_id", id);
                intent.putExtra("item_name", name);
                intent.putExtra("item_description", description);
                intent.putExtra("item_image", imageUrl);
                startActivity(intent);
            }
        });
    }

    // AsyncTask untuk memuat gambar dari URL
    private static class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private final ImageView imageView;

        public LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            }
        }
    }
}
