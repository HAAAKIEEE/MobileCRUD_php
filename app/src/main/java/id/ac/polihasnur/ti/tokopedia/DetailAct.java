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

import java.io.InputStream;

public class DetailAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Button sunting = findViewById(R.id.sunting);



        // Ambil data dari Intent
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

        // Memuat gambar menggunakan Bitmap (menggunakan AsyncTask untuk proses download gambar)
        new LoadImageTask(detailImage).execute(imageUrl);

        sunting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailAct.this, Edit.class);
                // Ambil data dari Intent
                intent.putExtra("item_name", name);
                intent.putExtra("item_description", description);
                intent.putExtra("item_image", imageUrl);


                startActivity(intent);
            }
        });
    }

    // AsyncTask untuk memuat gambar dari URL
    private static class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

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
