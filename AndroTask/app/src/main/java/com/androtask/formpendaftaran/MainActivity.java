package com.androtask.formpendaftaran;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private ImageView imageViewPreview;
    private Spinner spinnerSchoolType, spinnerProvince, spinnerCity;
    private EditText editTextSchoolName, editTextAddress, editTextPostalCode, editTextPhoneNumber, editTextEmail, editTextNumberOfStudents;

    private String[] provinces;
    private String[] citiesJawaBarat = {"Bandung", "Bekasi", "Depok"};
    private String[] citiesJawaTengah = {"Semarang", "Surakarta", "Yogyakarta"};
    private String[] citiesJawaTimur = {"Surabaya", "Malang", "Probolinggo"};

    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi views
        spinnerSchoolType = findViewById(R.id.spinnerSchoolType);
        spinnerProvince = findViewById(R.id.spinnerProvince);
        spinnerCity = findViewById(R.id.spinnerCity);
        editTextSchoolName = findViewById(R.id.editTextSchoolName);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextPostalCode = findViewById(R.id.editTextPostalCode);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextNumberOfStudents = findViewById(R.id.editTextNumberOfStudents);
//        imageViewPreview = findViewById(R.id.imageViewPreview);

        // Mengisi spinner dengan daftar tipe sekolah dari resources
        ArrayAdapter<CharSequence> schoolTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.school_types, android.R.layout.simple_spinner_item);
        schoolTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSchoolType.setAdapter(schoolTypeAdapter);

        // Mengisi spinner provinsi dengan daftar provinsi dari resources
        provinces = getResources().getStringArray(R.array.provinces);
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinces);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvince.setAdapter(provinceAdapter);

        // Menangani pemilihan provinsi untuk mengisi spinner kota/kabupaten
        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedProvince = provinces[position];
                loadCities(selectedProvince);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Mengatur aksi ketika tombol Unggah Gambar ditekan
//        Button buttonUploadImage = findViewById(R.id.buttonUploadImage);
//        buttonUploadImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chooseImage();
//            }
//        });

        // Mengatur aksi ketika tombol Submit ditekan
        Button buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
    }

    // Method untuk memuat daftar kota/kabupaten berdasarkan provinsi yang dipilih
    private void loadCities(String selectedProvince) {
        String[] cities = new String[0];
        switch (selectedProvince) {
            case "Jawa Barat":
                cities = citiesJawaBarat;
                break;
            case "Jawa Tengah":
                cities = citiesJawaTengah;
                break;
            case "Jawa Timur":
                cities = citiesJawaTimur;
                break;
            // Tambahkan case untuk provinsi lain jika diperlukan
        }

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(cityAdapter);
    }

    // Method untuk memilih gambar dari galeri atau kamera
    private void chooseImage() {
        // Membuat dialog untuk memilih sumber gambar
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Pilih Sumber Gambar");
        builder.setItems(new CharSequence[]{"Kamera", "Galeri"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                takePhotoFromCamera();
                                break;
                            case 1:
                                choosePhotoFromGallery();
                                break;
                        }
                    }
                });
        builder.show();
    }

    // Method untuk memilih gambar dari galeri
    private void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
    }

    // Method untuk mengambil gambar dari kamera
    private void takePhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Membuat file sementara untuk menyimpan gambar yang akan diambil dari kamera
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.androtask.formpendaftaran.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }
    }

    private byte[] compressImage(String imagePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 5, stream); // Kompresi ke format JPEG dengan kualitas 5%
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();

        return byteArray;
    }


    // Method untuk membuat file gambar sementara
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Set gambar di ImageView dari lokasi file yang disimpan
            File imgFile = new File(mCurrentPhotoPath);
            if (imgFile.exists()) {
                imageViewPreview.setImageURI(Uri.fromFile(imgFile));
            }
        } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            imageViewPreview.setImageURI(imageUri);
        }
    }

    // Method untuk melakukan validasi dan submit form
    private void submitForm() {
//        //Mendapatkan data gambar

        String schoolName = editTextSchoolName.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String postalCode = editTextPostalCode.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String numberOfStudents = editTextNumberOfStudents.getText().toString().trim();
        String schoolType = spinnerSchoolType.getSelectedItem().toString();
        String province = spinnerProvince.getSelectedItem().toString();
        String city = spinnerCity.getSelectedItem().toString();

        // Validasi input pengguna
        if (TextUtils.isEmpty(schoolName) || TextUtils.isEmpty(address) || TextUtils.isEmpty(postalCode) ||
                TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(email) || TextUtils.isEmpty(numberOfStudents)) {
            // Tampilkan pesan kesalahan jika ada input yang kosong
            Toast.makeText(this, "Harap lengkapi semua bidang", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPostalCode(postalCode)) {
            // Tampilkan pesan kesalahan jika kode pos tidak valid
            editTextPostalCode.setError("Masukkan kode pos yang valid");
            editTextPostalCode.requestFocus();
            return;
        }

        if (!isValidPhoneNumber(phoneNumber)) {
            // Tampilkan pesan kesalahan jika nomor telepon tidak valid
            editTextPhoneNumber.setError("Masukkan nomor telepon yang valid");
            editTextPhoneNumber.requestFocus();
            return;
        }

        if (!isValidEmail(email)) {
            // Tampilkan pesan kesalahan jika alamat email tidak valid
            editTextEmail.setError("Masukkan alamat email yang valid");
            editTextEmail.requestFocus();
            return;
        }

        if (!isValidNumberOfStudents(numberOfStudents)) {
            // Tampilkan pesan kesalahan jika jumlah siswa tidak valid
            editTextNumberOfStudents.setError("Masukkan jumlah siswa antara 1 hingga 100");
            editTextNumberOfStudents.requestFocus();
            return;
        }

        new SendDataToServer().execute(schoolName, address, postalCode, phoneNumber, email, numberOfStudents, schoolType, province, city);

    }

    // AsyncTask untuk mengirim data ke server
    private class SendDataToServer extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... params) {
            String response = "";
            try {
                URL url = new URL("https://androtask.situsformal.web.id/post.php"); // Ganti dengan alamat server Anda
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                // Mendapatkan data yang akan dikirim
                String postData = "schoolName=" + URLEncoder.encode((String) params[0], "UTF-8")
                        + "&address=" + URLEncoder.encode((String) params[1], "UTF-8")
                        + "&postalCode=" + URLEncoder.encode((String) params[2], "UTF-8")
                        + "&phoneNumber=" + URLEncoder.encode((String) params[3], "UTF-8")
                        + "&email=" + URLEncoder.encode((String) params[4], "UTF-8")
                        + "&numberOfStudents=" + URLEncoder.encode((String) params[5], "UTF-8")
                        + "&schoolType=" + URLEncoder.encode((String) params[6], "UTF-8")
                        + "&province=" + URLEncoder.encode((String) params[7], "UTF-8")
                        + "&city=" + URLEncoder.encode((String) params[8], "UTF-8");

//                byte[] image = (byte[])params[9];

//                 Menyertakan data gambar dalam permintaan
//                conn.setRequestProperty("Content-Type", "application/octet-stream");
//                conn.setRequestProperty("Content-Length", String.valueOf(Base64.encodeToString(image, Base64.DEFAULT).length()));
//                conn.getOutputStream().write(Base64.encodeToString(image, Base64.DEFAULT).getBytes());

                // Mengirim data ke server
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(postData);
                writer.flush();
                writer.close();
                os.close();

                // Menerima respons dari server jika diperlukan

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Tanggapan sukses
                    response = "Data berhasil disimpan di server";
                } else {
                    // Tanggapan gagal
                    response = "Gagal menyimpan data di server, " + responseCode;
                }
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                response = "Exception: " + e.getMessage();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // Menampilkan pesan ke pengguna
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }


    // Method untuk validasi kode pos
    private boolean isValidPostalCode(String postalCode) {
        return Pattern.matches("^\\d{5}$", postalCode);
    }

    // Method untuk validasi nomor telepon
    private boolean isValidPhoneNumber(String phoneNumber) {
        return Patterns.PHONE.matcher(phoneNumber).matches();
    }

    // Method untuk validasi alamat email
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Method untuk validasi jumlah siswa
    private boolean isValidNumberOfStudents(String numberOfStudents) {
        try {
            int num = Integer.parseInt(numberOfStudents);
            return num >= 1 && num <= 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
