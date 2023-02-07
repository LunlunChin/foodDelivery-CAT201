package com.example.fooddelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final EditText editMatric= findViewById(R.id.editTextTextPersonName);

        loginButton = findViewById(R.id.loginBtn);
        loginButton.setOnClickListener(this);

        register = findViewById(R.id.registerBtn);
        register.setOnClickListener(this);



    }
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://lunfood-251a1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

    private View register, loginButton;




    @Override
    public void onClick(View view) {
        final EditText editMatric= findViewById(R.id.editTextTextPersonName);
        final EditText editPass= findViewById(R.id.editTextTextPassword);
        switch(view.getId()){
            case R.id.loginBtn:
                final String matricTxt = editMatric.getText().toString();
                final String passTxt= editPass.getText().toString();

                if(matricTxt.isEmpty() || passTxt.isEmpty()){
                    Toast.makeText(this, "Please enter matric number and password" ,Toast.LENGTH_SHORT).show();
                }

                else {

                    // check if data exist

                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //check if matric exist

                            if (snapshot.hasChild(matricTxt)) {
                                //check password matches

                                final String getPass = snapshot.child(matricTxt).child("password").getValue(String.class);

                                if (getPass.equals(passTxt)) {
                                    Toast.makeText(MainActivity.this, "Successfully Logged in!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this, HomePage.class));
                                    finish();

                                } else {
                                    Toast.makeText(MainActivity.this, "Wrong Password!", Toast.LENGTH_SHORT).show();

                                }


                            } else {
                                Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                break;

                    case R.id.registerBtn:
                        startActivity(new Intent(this, RegisterUser.class));
                        break;
                    default:
                         break;
        }

    }



}