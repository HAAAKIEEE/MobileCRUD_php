package id.ac.polihasnur.ti.tokopedia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Register", "Tombol Register diklik");
                EditText txtFullname = findViewById(R.id.txtFullname);
                EditText txtEmail = findViewById(R.id.txtEmail);
                EditText txtPassword = findViewById(R.id.txtPassword);
                String fullname = txtFullname.getText().toString();
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                // Buat RequestQueue untuk Volley
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.47.150/tokopedia-db/register.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Register", "Response: " + response);
                                if (response.equals("success")) {
                                    Toast.makeText(Register.this, "Data berhasil diinput.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Register.this, "Data gagal diinput.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Register", "Error: " + error.getMessage());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("fullname", fullname);
                        paramV.put("email", email);
                        paramV.put("password", password);
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });

    }
    public void openLogin(View view){
        Intent toLogin = new Intent(Register.this, Login.class);
        startActivity(toLogin);
    }
}