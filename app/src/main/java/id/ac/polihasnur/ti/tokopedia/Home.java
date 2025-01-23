package id.ac.polihasnur.ti.tokopedia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home extends AppCompatActivity {

    Integer photo_max = 1;
    Bitmap bitmap;
    ActivityResultLauncher<Intent> activityResultLauncher;
    Button btn_upload;
    Uri photo_location;
    ImageView home_image;
    ListView home_listview;
    int[] list_index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btn_upload = findViewById(R.id.btn_upload);
        home_image = findViewById(R.id.home_image);
        home_listview = findViewById(R.id.home_listview); // Sesuaikan dengan ID ListView di layout XML Anda
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getResultCode() == Activity.RESULT_OK) {
                            Intent data = o.getData();
                            Uri uri = data.getData();
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                home_image.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        itemList();
        // Button to add photo
        home_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);
            }
        });

        // Button to upload
        btn_upload.setOnClickListener(v -> post());

    }

    // Function to select a photo from gallery
//    public void findPhoto() {
//        Intent pic = new Intent();
//        pic.setType("image/*");
//        pic.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(pic, photo_max);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            photo_location = data.getData();
//            Picasso.with(Home.this).load(photo_location)
//                    .centerCrop().fit().into(home_image);
//        }
//    }
    public void itemList() {
        List<Item> itemList = new ArrayList<>();
        ItemAdapter adapter = new ItemAdapter(this, itemList);
        home_listview.setAdapter(adapter);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.47.150//tokopedia-db/itemall.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        itemList.clear();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String name = jsonObject.getString("item_name");
                                String description = jsonObject.getString("description");
                                String imageUrl = "http://192.168.47.150/tokopedia-db/" + jsonObject.getString("image_path");

                                // Tambahkan item ke list
                                itemList.add(new Item(name, description, imageUrl));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Home.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);

        // Set klik listener pada item
        home_listview.setOnItemClickListener((parent, view, position, id) -> {
            Item selectedItem = itemList.get(position);

            // Pindah ke halaman DetailActivity
            Intent intent = new Intent(Home.this, DetailAct.class);
            intent.putExtra("item_name", selectedItem.getName());
            intent.putExtra("item_description", selectedItem.getDescription());
            intent.putExtra("item_image", selectedItem.getImageUrl());
            startActivity(intent);
        });

    }


//    public void itemList() {
//        List<String> Item = new ArrayList<String>();
//        ArrayAdapter<String> dataitem = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Item);
//        dataitem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        home_listview.setAdapter(dataitem);
//        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//        String url = "http://192.168.47.150//tokopedia-db/itemall.php";
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Item.clear();
//                        try {
//                            JSONArray jsonArray = new JSONArray(response);
//                            list_index = new int[jsonArray.length()];
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                String item = jsonObject.getString("item_name");
//                                String descripsi = jsonObject.getString("description");
//
//                                list_index[i] = Integer.parseInt(jsonObject.getString("id"));
//
//                                //memasukkan data ke listview
//                                Item.add(item);
//                                dataitem.notifyDataSetChanged();
//                            }
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }) {
//        };
//
//        queue.add(stringRequest);
//    }

    public void post() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Log.d("Home", "Tombol Upload diklik");
        EditText home_item = findViewById(R.id.home_item);
        EditText home_deskripsi = findViewById(R.id.home_deskripsi);
        String item = home_item.getText().toString();
        String deskripsi = home_deskripsi.getText().toString();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
//            final String base = Base64.encodeToString(bytes,Base64.DEFAULT);
            final String base = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);

            // Create Volley request to upload the item and description
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = "http://192.168.47.150//tokopedia-db/upload.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Upload", "Response: " + response);
                            if (response.equals("success")) {
                                Toast.makeText(Home.this, "Data berhasil diupload.", Toast.LENGTH_SHORT).show();
                                itemList();
                            } else {
                                Toast.makeText(Home.this, "Data gagal diupload.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Upload", "Error: " + error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> paramV = new HashMap<>();
                    paramV.put("item", item);
                    paramV.put("deskripsi", deskripsi);
                    paramV.put("image_path", base);

                    return paramV;
                }
            };

            queue.add(stringRequest);
        } else {
            Toast.makeText(Home.this, "upload gambar dulu.", Toast.LENGTH_SHORT).show();

        }
    }

}
