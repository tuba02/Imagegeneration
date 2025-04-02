package com.example.imagegen;

import android.os.Bundle;
import android.os.AsyncTask;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import android.util.Log;
import java.util.HashMap;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity {

    private EditText charname;
    private Button genbutton,helpButton, copyButton, closePopupButton, clearButton;
    private String selectedOption;
    private LinearLayout helpPopup, loadingPopup;
    private String prompt="masterpiece";
    private HashMap<String, String> optionValueMap;
    private Spinner spinner_h, spinner_c, spinner_p, spinner_f, spinner_cl, spinner_a;

    private String selectedHair = "";
    private String selectedColor = "";
    private String selectedPose = "";
    private String selectedFace = "";
    private String selectedClothing = "";
    private String selectedAngle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);

        charname = findViewById(R.id.charname);
        genbutton = findViewById(R.id.genbutton);
        helpButton = findViewById(R.id.helpButton);
        helpPopup = findViewById(R.id.helpPopup);
        loadingPopup = findViewById(R.id.loadingPopup);
        copyButton = findViewById(R.id.copyButton);
        closePopupButton = findViewById(R.id.closePopupButton);
        helpButton.setOnClickListener(v -> helpPopup.setVisibility(View.VISIBLE));
        closePopupButton.setOnClickListener(v -> helpPopup.setVisibility(View.GONE));
        clearButton = findViewById(R.id.clearButton);

        copyButton.setOnClickListener(v -> {
            String textToCopy = "1girl, arima kana, oshi no ko, solo";
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("prompt example", textToCopy);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(MainActivity.this, "コピーしました", Toast.LENGTH_SHORT).show();
        });

        clearButton.setOnClickListener(v -> {
            charname.setText("");
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        optionValueMap = new HashMap<>();
        optionValueMap.put("ストレート", ",straight hair");
        optionValueMap.put("ボブカット", ",bob cut");
        optionValueMap.put("ポニーテール", ",ponytail");
        optionValueMap.put("サイドポニーテール", ",side ponytail");
        optionValueMap.put("ドリルカット", ",drill hair");
        optionValueMap.put("三つ編み", ",braid hair");
        optionValueMap.put("ハーフアップ", ",half updo");
        optionValueMap.put("ツインテール", ",twintails");
        optionValueMap.put("赤", ",red hair");
        optionValueMap.put("オレンジ", ",orange hair");
        optionValueMap.put("黄", ",yellow hair");
        optionValueMap.put("緑", ",green hair");
        optionValueMap.put("青", ",blue hair");
        optionValueMap.put("紫", ",purple hair");
        optionValueMap.put("白", ",white hair");
        optionValueMap.put("黒", ",black hair");
        optionValueMap.put("立っている", ",standing");
        optionValueMap.put("座っている", ",sitting");
        optionValueMap.put("背伸び", ",arched back");
        optionValueMap.put("ジャンプ", ",jumping");
        optionValueMap.put("ピース", ",peace fingers");
        optionValueMap.put("敬礼", ",salute");
        optionValueMap.put("威嚇1", ",claw pose");
        optionValueMap.put("威嚇2", ",paw pose");
        optionValueMap.put("笑顔", ",smile");
        optionValueMap.put("照れ顔", ",shy");
        optionValueMap.put("無表情", ",expressionless");
        optionValueMap.put("怒り顔", ",angry");
        optionValueMap.put("泣き顔", ",cry");
        optionValueMap.put("笑顔(歯)", ",Toothy grin");
        optionValueMap.put("制服", ",school uniform");
        optionValueMap.put("水着", ",swim suit");
        optionValueMap.put("ドレス", ",dress");
        optionValueMap.put("カジュアル", ",casual uniform");
        optionValueMap.put("Tシャツ", ",T-shirts");
        optionValueMap.put("ブラウス", ",brouse");
        optionValueMap.put("パジャマ", ",pajamas");
        optionValueMap.put("全身", ",full body");
        optionValueMap.put("太ももから頭", ",cowboy shot");
        optionValueMap.put("胸から頭", ",upper body");
        optionValueMap.put("首から頭", ",portrait");
        optionValueMap.put("顔のみ", ",face");
        optionValueMap.put("指定なし", "a");

        charname = findViewById(R.id.charname);
        genbutton = findViewById(R.id.genbutton);

        Spinner spinner_h = findViewById(R.id.spinner_hear);
        ArrayAdapter<CharSequence> adapter_h = ArrayAdapter.createFromResource(
                this,
                R.array.spinner_hear,
                android.R.layout.simple_spinner_item);
        adapter_h.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_h.setAdapter(adapter_h);
        spinner_h.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOption = parent.getItemAtPosition(position).toString();
                selectedHair = optionValueMap.getOrDefault(selectedOption, "");
                updatePrompt();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedHair = "";
            }
        });

        Spinner spinner_c = findViewById(R.id.spinner_color);
        ArrayAdapter<CharSequence> adapter_c = ArrayAdapter.createFromResource(
                this,
                R.array.spinner_color,
                android.R.layout.simple_spinner_item);
        adapter_c.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_c.setAdapter(adapter_c);
        spinner_c.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOption = parent.getItemAtPosition(position).toString();
                selectedColor = optionValueMap.getOrDefault(selectedOption, "");
                updatePrompt();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedColor = "";
            }
        });

        Spinner spinner_p = findViewById(R.id.spinner_pose);
        ArrayAdapter<CharSequence> adapter_p = ArrayAdapter.createFromResource(
                this,
                R.array.spinner_pose,
                android.R.layout.simple_spinner_item);
        adapter_p.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_p.setAdapter(adapter_p);
        spinner_p.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOption = parent.getItemAtPosition(position).toString();
                selectedPose = optionValueMap.getOrDefault(selectedOption, "");
                updatePrompt();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedPose = "";
            }
        });

        Spinner spinner_f = findViewById(R.id.spinner_face);
        ArrayAdapter<CharSequence> adapter_f = ArrayAdapter.createFromResource(
                this,
                R.array.spinner_face,
                android.R.layout.simple_spinner_item);
        adapter_f.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_f.setAdapter(adapter_f);
        spinner_f.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOption = parent.getItemAtPosition(position).toString();
                selectedFace = optionValueMap.getOrDefault(selectedOption, "");
                updatePrompt();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedFace = "";
            }
        });

        Spinner spinner_cl = findViewById(R.id.spinner_clothing);
        ArrayAdapter<CharSequence> adapter_cl = ArrayAdapter.createFromResource(
                this,
                R.array.spinner_clothing,
                android.R.layout.simple_spinner_item);
        adapter_cl.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_cl.setAdapter(adapter_cl);
        spinner_cl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOption = parent.getItemAtPosition(position).toString();
                selectedClothing = optionValueMap.getOrDefault(selectedOption, "");
                updatePrompt();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedClothing = "";
            }
        });

        Spinner spinner_a = findViewById(R.id.spinner_angle);
        ArrayAdapter<CharSequence> adapter_a = ArrayAdapter.createFromResource(
                this,
                R.array.spinner_angle,
                android.R.layout.simple_spinner_item);
        adapter_a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_a.setAdapter(adapter_a);
        spinner_a.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOption = parent.getItemAtPosition(position).toString();
                selectedAngle = optionValueMap.getOrDefault(selectedOption, "");
                updatePrompt();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedAngle = "";
            }
        });

        genbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputValue = charname.getText().toString();



                if (!inputValue.isEmpty() && prompt!=null) {
                    updatePrompt();
                    prompt=prompt.concat(",");
                    prompt=prompt.concat(inputValue);
                    loadingPopup.setVisibility(View.VISIBLE);

                    genbutton.setEnabled(false);
                    charname.setEnabled(false);
                    copyButton.setEnabled(false);
                    helpButton.setEnabled(false);
                    clearButton.setEnabled(false);

                    new PostMethod().execute(prompt);
                } else {
                    Toast.makeText(MainActivity.this, "入力またはオプションを選択してください", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void updatePrompt() {
        prompt = "masterpiece";

        if (!selectedHair.isEmpty()) prompt = prompt.concat(selectedHair);
        if (!selectedColor.isEmpty()) prompt = prompt.concat(selectedColor);
        if (!selectedPose.isEmpty()) prompt = prompt.concat(selectedPose);
        if (!selectedFace.isEmpty()) prompt = prompt.concat(selectedFace);
        if (!selectedClothing.isEmpty()) prompt = prompt.concat(selectedClothing);
        if (!selectedAngle.isEmpty()) prompt = prompt.concat(selectedAngle);
    }
    private class PostMethod extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            String prompt = params[0];
            Bitmap bitmap = null;

            try {

                URL url = new URL("https://cce0-131-206-228-37.ngrok-free.app/api");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                OutputStream os = conn.getOutputStream();
                String postData = "prompt=" + URLEncoder.encode(prompt, "UTF-8");
                os.write(postData.getBytes("UTF-8"));
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                System.out.println("Response Code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(in);
                }

                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            loadingPopup.setVisibility(View.GONE);

            genbutton.setEnabled(true);
            charname.setEnabled(true);
            copyButton.setEnabled(true);
            helpButton.setEnabled(true);
            clearButton.setEnabled(true);

            if (result != null) {
                Intent intent = new Intent(MainActivity.this, GenActivity.class);
                try {
                    File file = new File(getCacheDir(), "generated_image.png");
                    FileOutputStream fos = new FileOutputStream(file);
                    result.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();

                    intent.putExtra("imagePath", file.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "通信に失敗しました。ネットワークの接続を確認してください", Toast.LENGTH_SHORT).show();
            }
        }


    }
}

