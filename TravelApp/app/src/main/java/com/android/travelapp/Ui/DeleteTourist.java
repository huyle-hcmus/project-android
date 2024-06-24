package com.android.travelapp.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.travelapp.Database.Database;
import com.android.travelapp.R;
import com.google.android.material.textfield.TextInputLayout;

public class DeleteTourist extends AppCompatActivity {
    Button btnDelete, btnBack;
    TextInputLayout inpnameTour;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_tourist);

        btnDelete = findViewById(R.id.btn_delete_tourist);
        btnBack = findViewById(R.id.btn_back_edit_tourist);

        inpnameTour = findViewById(R.id.tourname_find);

        database = new Database(this);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name_tour = inpnameTour.getEditText().getText().toString();

                if (name_tour.isEmpty()){
                    Toast.makeText(DeleteTourist.this, "Your input values can't be left blank", Toast.LENGTH_LONG).show();
                } else {
                    boolean delete_status = database.deleteTouristData(name_tour);
                    if(delete_status){
                        Toast.makeText(DeleteTourist.this, "Successful Delete", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(DeleteTourist.this, EditTourist.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(DeleteTourist.this, "Fail Delete", Toast.LENGTH_LONG).show();
                        reset();
                    }
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeleteTourist.this, EditTourist.class);
                startActivity(intent);
            }
        });
    }

    public void reset(){
        inpnameTour.getEditText().setText(null);
    }
}
