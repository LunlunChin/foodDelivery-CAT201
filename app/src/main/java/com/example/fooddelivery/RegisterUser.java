package com.example.fooddelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference databaseReference = database.getReferenceFromUrl("https://lunfood-251a1-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://lunfood-251a1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

    private EditText editEmail, editPassword, editMatric;
    private TextView register;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);



        mAuth = FirebaseAuth.getInstance();



        editEmail = findViewById(R.id.editTextTextEmailAddress);
        editPassword = findViewById(R.id.editTextTextPassword2);
        editMatric = findViewById(R.id.editTextTextPersonName2);
        register = findViewById(R.id.registerbutton);
        register.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.registerbutton:
                registerUser();
             break;
        }
    }


    private void registerUser(){
        String email = editEmail.getText().toString().trim();
        String matric = editMatric.getText().toString().trim();
        String pass = editPassword.getText().toString().trim();

        if(email.isEmpty()){
            editEmail.setError("Email address is required!");
            editEmail.requestFocus();
            return;
        }
        if(matric.isEmpty()){
            editMatric.setError("Matric number is required!");
            editMatric.requestFocus();
            return;
        }
        if(pass.isEmpty()){
            editPassword.setError("Password is required!");
            editPassword.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editEmail.setError("Email is invalid");
            editEmail.requestFocus();
            return;
        }

        else{

            System.out.println("CHECKER");
            databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {


                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // check if user matric registered bfr

                    if(snapshot.hasChild(matric)){
                        Toast.makeText(RegisterUser.this, "User registered before",Toast.LENGTH_SHORT).show();
                        System.out.println("NOT SUCCESS");

                    }
                    else{
                        //sending data to firebase
                        System.out.println("SUCCESS");
                        databaseReference.child("users").child(matric).child("email").setValue(email);
                        databaseReference.child("users").child(matric).child("password").setValue(pass);
                        Toast.makeText(RegisterUser.this, "Register Successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }





//        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()){
//                    User user =  new User(matric, email, pass);
//
//
//                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
//                }
//            }
//        });

    }
}