package com.example.collegemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIME_OUT=1000;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //This method is used so that your splash activity
        //can cover the entire screen.

        setContentView(R.layout.activity_main);
        //this will bind your MainActivity.class file with activity_main.

        new Handler().postDelayed(() -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null)
                db.collection("admin")
                        .document(user.getUid())
                        .get()
                        .addOnCompleteListener(t -> {
                            if (t.getResult().exists()) {
                                Intent i = new Intent(MainActivity.this, AdminPage.class);
                                startActivity(i);
                                finish();
                            }
                            else
                                db.collection("teacher").document(user.getUid()).get().addOnCompleteListener(task -> {
                                    if(task.getResult().exists()) {
                                        Intent i = new Intent(MainActivity.this, TeacherPage.class);
                                        startActivity(i);
                                        finish();
                                    }
                                    else {
                                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                });
                        });
            else {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
//            Toast.makeText(this, user.getUid() + "", Toast.LENGTH_SHORT).show();
//            Intent i=new Intent(MainActivity.this,MainActivity.class);
//            //Intent is used to switch from one activity to another.
//
//            startActivity(i);
//            //invoke the SecondActivity.
//
//            finish();
            //the current activity will get finished.
        }, SPLASH_SCREEN_TIME_OUT);
    }
}