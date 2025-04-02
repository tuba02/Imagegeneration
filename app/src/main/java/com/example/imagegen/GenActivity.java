package com.example.imagegen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;


public class GenActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen);

        imageView = findViewById(R.id.imageView);
        Button preservationButton = findViewById(R.id.preservationbotton);
        Button returnButton = findViewById(R.id.returnbotton);

        String imagePath = getIntent().getStringExtra("imagePath");
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(bitmap);
        }

        preservationButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(GenActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(GenActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                saveImageToGallery();
            }
        });
        returnButton.setOnClickListener(v -> {
            Intent intent = new Intent(GenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
    private void saveImageToGallery() {
        if (imageView.getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

            long currentTimeMillis = System.currentTimeMillis();
            String fileName = "saved_image_" + currentTimeMillis + ".png"; // 例: saved_image_1633048453000.png

            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyApp");

            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directory, fileName);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                Toast.makeText(this, "画像が保存されました", Toast.LENGTH_SHORT).show();

                MediaScannerConnection.scanFile(this, new String[]{file.getAbsolutePath()}, null, (path, uri) -> {

                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "画像の保存に失敗しました", Toast.LENGTH_SHORT).show();
            }
        }
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImageToGallery();
            } else {
                showPermissionDeniedDialog();
            }
        }
    }
    private void showPermissionDeniedDialog() {
        new AlertDialog.Builder(this)
                .setTitle("権限が必要です")
                .setMessage("画像を保存するにはストレージのアクセス権限が必要です。設定から許可を変更してください。")
                .setPositiveButton("設定を開く", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("キャンセル", null)
                .show();
    }

}
