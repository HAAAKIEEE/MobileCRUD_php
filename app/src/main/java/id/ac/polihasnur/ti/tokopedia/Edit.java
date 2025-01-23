package id.ac.polihasnur.ti.tokopedia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Edit extends AppCompatActivity {
    BottomNavigationView edit_bot_nav;
    ActivityResultLauncher<Intent> activityResultLauncher;
    Bitmap bitmap;
    String itemId,name,description,imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Bottom Navigation setup
        edit_bot_nav = findViewById(R.id.edit_bot_nav);

        edit_bot_nav.setOnItemSelectedListener(item -> {

                startActivity(new Intent(Edit.this, Home.class).putExtra("id_user", "1"));
                finish();
                return true;
        });

        // Ambil data dari Intent
        itemId = getIntent().getStringExtra("item_id");
        name = getIntent().getStringExtra("item_name");
        description = getIntent().getStringExtra("item_description");
        imageUrl = getIntent().getStringExtra("item_image");

        // Temukan views di layout
        TextView editId = findViewById(R.id.id_edit);
        EditText editName = findViewById(R.id.edit_item);
        EditText editDescription = findViewById(R.id.edit_deskripsi);
        ImageView editImage = findViewById(R.id.edit_image);
        Button btnDelete = findViewById(R.id.btn_delete_item);
        Button btnEdit = findViewById(R.id.btn_edit_item);

        // Set data ke views
        editId.setText(itemId);
        editName.setText(name);
        editDescription.setText(description);

        // Memuat gambar menggunakan AsyncTask
        new LoadImageTask(editImage).execute(imageUrl);

        // Tambahkan listener untuk tombol Delete
        btnDelete.setOnClickListener(v -> deleteItem(itemId));

        // Tambahkan listener untuk tombol Edit
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ambil nilai input
                String item = editName.getText().toString().trim();
                String deskripsi = editDescription.getText().toString().trim();

                // Debug log
                Log.d("UpdateItem", "Item: " + item);
                Log.d("UpdateItem", "Deskripsi: " + deskripsi);

                // Cek jika nilai kosong
                if (item.isEmpty() || deskripsi.isEmpty()) {
                    Toast.makeText(Edit.this, "Nama dan Deskripsi tidak boleh kosong.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lanjutkan dengan update
                String encodedImage = encodeImage(bitmap);
                String url = "http://192.168.47.150/tokopedia-db/update.php";

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        response -> {
                            Log.d("UpdateResponse", response); // Tambahkan log respons server
                            if (response.trim().equals("success")) {
                                Toast.makeText(Edit.this, "Item berhasil diperbarui.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Edit.this, Home.class));
                                finish();
                            } else {
                                Toast.makeText(Edit.this, "Gagal memperbarui item.", Toast.LENGTH_SHORT).show();
                            }
                        },
                        error -> {
                            Log.e("UpdateError", error.toString());
                            Toast.makeText(Edit.this, "Terjadi kesalahan: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("item_id", itemId);
                        params.put("item", item);
                        params.put("deskripsi", deskripsi);
                        params.put("image_path", encodedImage);
                        return params;
                    }
                };

                queue.add(stringRequest);
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Uri selectedImageUri = data.getData();
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                                editImage.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(Edit.this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        editImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            activityResultLauncher.launch(intent);
        });
    }

    // Fungsi untuk mengupdate item
    private void updateItem(String name, String description) {


    }

    // Fungsi untuk menghapus item
    private void deleteItem(String id) {
        String url = "http://192.168.47.150/tokopedia-db/delete.php"; // Ganti dengan URL endpoint delete kamu

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.trim().equals("success")) {
                        // Item berhasil dihapus
                        Toast.makeText(Edit.this, "Item berhasil dihapus.", Toast.LENGTH_SHORT).show();
                        // Kembali ke halaman Home
                        startActivity(new Intent(Edit.this, Home.class));
                        finish();
                    } else {
                        // Jika gagal
                        Toast.makeText(Edit.this, "Gagal menghapus item.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Tangani error
                    Toast.makeText(Edit.this, "Terjadi kesalahan: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("item_id", id); // Kirimkan ID item sebagai parameter
                return params;
            }
        };

        queue.add(stringRequest);
    }

    // Mengubah gambar Bitmap menjadi Base64
    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);
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
