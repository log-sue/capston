package com.example.capston1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class joinActivity extends AppCompatActivity {

    EditText joinName;
    EditText joinId;
    EditText joinPassword;
    EditText joinPwck;
    EditText joinEmail;
    Button joinButton;

    String userName;
    String userId;
    String userPassword;
    String userPwck;
    String userEmail;

    String joinstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        joinName=(EditText)findViewById(R.id.join_name);
        joinId=(EditText)findViewById(R.id.join_id);
        joinPassword=(EditText)findViewById(R.id.join_password);
        joinPwck=(EditText)findViewById(R.id.join_pwck);
        joinEmail=(EditText)findViewById(R.id.join_email);
        joinButton=(Button)findViewById(R.id.join_button);

        joinButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                userName=joinName.getText().toString();
                userId=joinId.getText().toString();
                userPassword=joinPassword.getText().toString();
                userPwck=joinPwck.getText().toString();
                userEmail=joinEmail.getText().toString();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            join();
                        }catch (Exception ex){
                            System.out.println("error : " + ex);
                        }
                    }
                });

                if(!userPassword.equals(userPwck)){
                    //??????????????? ???????????? ???????????? ??????
                    Toast.makeText(getApplicationContext(),"??????????????? ???????????? ????????????.",Toast.LENGTH_SHORT).show();
                }
                else if(userName.length()==0||userId.length()==0||userPassword.length()==0||userPwck.length()==0||userEmail.length()==0){
                    //???????????? ?????? ????????? ????????? ?????????
                    Toast.makeText(getApplicationContext(),"???????????? ?????? ????????? ????????????.",Toast.LENGTH_SHORT).show();
                }
                else{
                    //DB??? ????????? ?????? ??????
                    //?????? ????????? ???????????? ?????????
                    try{
                        thread.start();
                        thread.join();
                    }catch(Exception ex){
                        System.out.println("error : " + ex);
                    }

                    System.out.println("??????????????? : " + joinstatus);

                    if (joinstatus.equals("True\n")){
                        Intent intent = new Intent(getApplicationContext(), loginActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"????????? ?????????????????? ?????????????????? ???????????????.",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }


    public void join() throws UnsupportedEncodingException {

        // Create data
        //URL??? ???????????? ????????? ??? ?????? ?????? ???????????? URLencoder??? ??????
        String data = URLEncoder.encode("id", "UTF-8")
                + "=" + URLEncoder.encode(userId, "UTF-8")
                + "&"
                + URLEncoder.encode("name", "UTF-8")
                + "=" + URLEncoder.encode(userName, "UTF-8")
                + "&"
                + URLEncoder.encode("passwd", "UTF-8")
                + "=" + URLEncoder.encode(userPassword, "UTF-8")
                + "&"
                + URLEncoder.encode("email", "UTF-8")
                + "=" + URLEncoder.encode(userEmail, "UTF-8");

        String text = "";
        BufferedReader reader=null;

        try {
            // Defined URL where to send data
            URL url = new URL("http://192.168.123.104:3000/user/join");

            // Send POST data request
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get the server response
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }
            text = sb.toString();


        } catch(Exception ex) {
            System.out.println("error : " + ex);
        }

        finally {
            try {
                reader.close();
            } catch(Exception ex) {
                System.out.println("error : " + ex);
            }
        }

        // Update global text
        joinstatus = text;
    }
}
