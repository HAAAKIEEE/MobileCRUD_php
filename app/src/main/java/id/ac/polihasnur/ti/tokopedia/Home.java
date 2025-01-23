package id.ac.polihasnur.ti.tokopedia;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONException;

import androidx.activity.result.ActivityResultLauncher;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home extends AppCompatActivity {

    Integer photo_max = 1;
    Bitmap bitmap;
    ActivityResultLauncher<Intent> activityResultLauncher;
    Button btn_add_photo, btn_upload;
    Uri photo_location;
    ImageView home_image;
    ListView home_listview ;
    int[] list_index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btn_add_photo = findViewById(R.id.btn_add_photo);
        btn_upload = findViewById(R.id.btn_upload);
        home_image = findViewById(R.id.home_image);
        home_listview = findViewById(R.id.home_listview); // Sesuaikan dengan ID ListView di layout XML Anda

        itemList();
        // Button to add photo
        btn_add_photo.setOnClickListener(v -> findPhoto());

        // Button to upload
        btn_upload.setOnClickListener(v -> {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Log.d("Home", "Tombol Upload diklik");
            EditText home_item = findViewById(R.id.home_item);
            EditText home_deskripsi = findViewById(R.id.home_deskripsi);
            String item = home_item.getText().toString();
            String deskripsi = home_deskripsi.getText().toString();
            String image_path = "test";
if (bitma)
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
                    paramV.put("image_path", image_path);

                    return paramV;
                }
            };

            queue.add(stringRequest);
        });


    }

    // Function to select a photo from gallery
    public void findPhoto() {
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null) {
            photo_location = data.getData();
            Picasso.with(Home.this).load(photo_location)
                    .centerCrop().fit().into(home_image);
        }
    }
    public void itemList() {
        List<String> Item = new ArrayList<String>();
        ArrayAdapter<String> dataitem = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,Item);
        dataitem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        home_listview.setAdapter(dataitem);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.47.150//tokopedia-db/itemall.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Item.clear();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            list_index = new int[jsonArray.length()];
                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String item = jsonObject.getString("item_name");
                                String descripsi = jsonObject.getString("description");

                                list_index[i] = Integer.parseInt(jsonObject.getString("id"));

                                //memasukkan data ke listview
                                Item.add(item);
                                dataitem.notifyDataSetChanged();
                            }
                        }catch (JSONException e){
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){};

        queue.add(stringRequest);
    }

}
