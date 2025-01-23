package id.ac.polihasnur.ti.tokopedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void openRegister(View view){
        Intent toRegister = new Intent(Login.this, Register.class);
        startActivity(toRegister);
    }
    String s_username="";
    String s_password="";
    public void login(View view){
        EditText loginUsername = findViewById(R.id.loginUsername);
        EditText loginPassword = findViewById(R.id.loginPassword);
        String username = loginUsername.getText().toString();
        String password = loginPassword.getText().toString();
        //==========================================================================================
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.47.150/tokopedia-db/login.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("invalid")){ //jika akun invalid
                            Toast.makeText(Login.this, "Akun dan password invalid",
                                    Toast.LENGTH_SHORT).show();
                        }else { //jika akun ditemukan/valid
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    s_username = jsonObject.getString("email");
                                    s_password = jsonObject.getString("password");
                                }
                        /*Toast.makeText(Login.this, "Login berhasil:\n"+
                                "Username: "+s_username+"\n"+
                                "Password: "+s_password, Toast.LENGTH_SHORT).show();*/
                                Intent toHome = new Intent(Login.this, Home.class);
                                startActivity(toHome);
                                finish();
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("username", username);
                paramV.put("password", password);
                return paramV;
            }
        };
        queue.add(stringRequest);
    }
}