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
import android.widget.TextView;
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

public class UpdateTourist extends AppCompatActivity {
    ImageView imgTour;
    TextInputLayout nameTour, descTour, priceTour, locTour;
    Button btnUpdate, btnReset;
    ImageButton ibtnCamera, ibtnFolder;
    Database database;
    int REQUEST_CODE_CAMERA = 123;
    int REQUEST_CODE_FOLDER = 456;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tourist);

        imgTour = findViewById(R.id.ivImage);

        nameTour = findViewById(R.id.txt_name_tourist);
        descTour = findViewById(R.id.txt_desc_tourist);
        priceTour = findViewById(R.id.txt_price_tourist);
        locTour = findViewById(R.id.txt_location_tourist);

        btnUpdate = findViewById(R.id.btn_update_tourist);
        btnReset = findViewById(R.id.btn_reset_tourist);

        ibtnCamera       = findViewById(R.id.ibtnCamera);
        ibtnFolder       = findViewById(R.id.ibtnFolder);

        database = new Database(this);

        getDataAdapter();
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

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Chuyển data imageview -> byte[]
                BitmapDrawable bitmapDrawable = (BitmapDrawable)imgTour.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, false);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] img_tour_update          = byteArrayOutputStream.toByteArray();

                //Kiem tra lai kich thuoc anh
                img_tour_update = imagemTratada(img_tour_update);

                String  name_tour_update        = nameTour.getEditText().getText().toString();
                String  price_tour_update       = priceTour.getEditText().getText().toString();
                String  location_tour_update    = locTour.getEditText().getText().toString();
                String  desc_tour_update        = descTour.getEditText().getText().toString();

                if (name_tour_update.isEmpty() || price_tour_update.isEmpty() || location_tour_update.isEmpty() || desc_tour_update.isEmpty()){
                    Toast.makeText(UpdateTourist.this, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                } else {
                    TouristModel newTourist = new TouristModel(name_tour_update, Integer.parseInt(price_tour_update), img_tour_update, desc_tour_update, location_tour_update);
                    boolean add_status = database.updateTouristData(getIntent().getStringExtra("nameTour"), newTourist);
                    if(add_status){
                        Toast.makeText(UpdateTourist.this, "Successful Update", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(UpdateTourist.this, EditTourist.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(UpdateTourist.this, "Fail Update", Toast.LENGTH_LONG).show();
                        reset();
                    }
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataAdapter();
            }
        });
    }
    private void getDataAdapter() {
        if (getIntent().hasExtra("imgTour") && getIntent().hasExtra("nameTour") && getIntent().hasExtra("descTour") && getIntent().hasExtra("priceTour")){
            byte[] image_tour = getIntent().getByteArrayExtra("imgTour");
            String name_tour = getIntent().getStringExtra("nameTour");
            String desc_tour = getIntent().getStringExtra("descTour");
            int price_tour = getIntent().getIntExtra("priceTour", 0);
            String loc_tour = getIntent().getStringExtra("locTour");

            setDataDetail(image_tour, name_tour, desc_tour, price_tour, loc_tour);
        }
    }
    private void setDataDetail(byte[] image_tour, String name_tour, String desc_tour, int price_tour, String loc_tour) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(image_tour, 0, image_tour.length);
        imgTour.setImageBitmap(bitmap);

        nameTour.getEditText().setText(name_tour);
        descTour.getEditText().setText(desc_tour);
        priceTour.getEditText().setText(Integer.toString(price_tour));
        locTour.getEditText().setText(loc_tour);
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
    public void reset(){
        nameTour.getEditText().setText(null);
        priceTour.getEditText().setText(null);
        locTour.getEditText().setText(null);
        descTour.getEditText().setText(null);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgTour.setImageBitmap(bitmap);
        }

        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null){
            Uri selectImageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgTour.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
