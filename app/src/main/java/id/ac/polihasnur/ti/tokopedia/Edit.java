package id.ac.polihasnur.ti.tokopedia;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.InputStream;

public class Edit extends AppCompatActivity {
    BottomNavigationView edit_bot_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Bottom Navigation setup
        edit_bot_nav = findViewById(R.id.edit_bot_nav);
        edit_bot_nav.setSelectedItemId(R.id.menu_edit);

        edit_bot_nav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_edit) {
                return true;
            } else {
                startActivity(new Intent(Edit.this, Home.class).putExtra("id_user", "1"));
                finish();
                return true;
            }
        });

        // Ambil data dari Intent
        String name = getIntent().getStringExtra("item_name");
        String description = getIntent().getStringExtra("item_description");
        String imageUrl = getIntent().getStringExtra("item_image");

        // Temukan views di layout
        EditText editName = findViewById(R.id.edit_item);
        EditText editDescription = findViewById(R.id.edit_deskripsi);
        ImageView editImage = findViewById(R.id.imageView3);
        Button btnDelete = findViewById(R.id.btn_delete_item);
        Button btnEdit = findViewById(R.id.btn_edit_item);

        // Set data ke views
        editName.setText(name);
        editDescription.setText(description);

        // Memuat gambar menggunakan AsyncTask
        new LoadImageTask(editImage).execute(imageUrl);

        // Tambahkan listener untuk tombol Delete
        btnDelete.setOnClickListener(v -> {
            // Tambahkan aksi untuk menghapus item di sini
        });

        // Tambahkan listener untuk tombol Edit
        btnEdit.setOnClickListener(v -> {
            // Tambahkan aksi untuk menyimpan perubahan item di sini
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
