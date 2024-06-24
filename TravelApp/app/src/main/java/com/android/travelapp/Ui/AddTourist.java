package com.android.travelapp.Ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.travelapp.Database.Database;
import com.android.travelapp.Model.TouristModel;
import com.android.travelapp.R;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddTourist extends AppCompatActivity {
    TextInputLayout inpName_tour, inpPrice_tour, inpLocation_tour, inpDesc_tour;
    Button btnAdd_tour, btnReset_tour;
    ImageButton ibtnCamera, ibtnFolder;
    ImageView inpImg_tour;
    Database database;
    int REQUEST_CODE_CAMERA = 123;
    int REQUEST_CODE_FOLDER = 456;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tourist);

        inpName_tour     = findViewById(R.id.txt_name_tourist);
        inpPrice_tour    = findViewById(R.id.txt_price_tourist);
        inpLocation_tour = findViewById(R.id.txt_location_tourist);
        inpDesc_tour     = findViewById(R.id.txt_desc_tourist);

        inpImg_tour      = findViewById(R.id.ivImage);

        btnAdd_tour      = findViewById(R.id.btn_add_tourist);
        btnReset_tour    = findViewById(R.id.btn_reset_tourist);

        ibtnCamera       = findViewById(R.id.ibtnCamera);
        ibtnFolder       = findViewById(R.id.ibtnFolder);

        database = new Database(this);

        ibtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });

        ibtnFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_FOLDER);
            }
        });

        btnAdd_tour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Chuyển data imageview -> byte[]
                BitmapDrawable bitmapDrawable = (BitmapDrawable)inpImg_tour.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, false);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] img_tour          = byteArrayOutputStream.toByteArray();

                //Kiem tra lai kich thuoc anh
                img_tour = imagemTratada(img_tour);

                String  name_tour        = inpName_tour.getEditText().getText().toString();
                String  price_tour       = inpPrice_tour.getEditText().getText().toString();
                String  location_tour    = inpLocation_tour.getEditText().getText().toString();
                String  desc_tour        = inpDesc_tour.getEditText().getText().toString();

                if (name_tour.isEmpty() || price_tour.isEmpty() || location_tour.isEmpty() || desc_tour.isEmpty()){
                    Toast.makeText(AddTourist.this, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                } else {
                    TouristModel newTourist = new TouristModel(name_tour, Integer.parseInt(price_tour), img_tour, desc_tour, location_tour);
                    boolean add_status = database.addTouristData(newTourist);
                    if(add_status){
                        Toast.makeText(AddTourist.this, "Successful Add", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AddTourist.this, EditTourist.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AddTourist.this, "Fail Add", Toast.LENGTH_LONG).show();
                        reset();
                    }
                }
            }
        });

        btnReset_tour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });
    }

    public void reset(){
        inpName_tour.getEditText().setText(null);
        inpPrice_tour.getEditText().setText(null);
        inpLocation_tour.getEditText().setText(null);
        inpDesc_tour.getEditText().setText(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            inpImg_tour.setImageBitmap(bitmap);
        }

        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null){
            Uri selectImageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                inpImg_tour.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private byte[] imagemTratada(byte[] imagem_img){

        while (imagem_img.length > 500000){
            Bitmap bitmap = BitmapFactory.decodeByteArray(imagem_img, 0, imagem_img.length);
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*0.8), (int)(bitmap.getHeight()*0.8), true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imagem_img = stream.toByteArray();
        }
        return imagem_img;
    }

}
