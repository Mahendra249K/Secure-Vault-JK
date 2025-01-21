package com.hidefile.secure.folder.vault.dashex;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hidefile.secure.folder.vault.R;
import com.hidefile.secure.folder.vault.cluecanva.Bhasha;
import com.hidefile.secure.folder.vault.cluecanva.PreUpyogi;
import com.hidefile.secure.folder.vault.cluecanva.SupPref;
import com.hidefile.secure.folder.vault.databinding.ActivityMangamtiBhasaPasandKarvaniBinding;
import com.hidefile.secure.folder.vault.dpss.MangamtiBhasaPasandKarvaniAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MangamtiBhasaPasandKarvaniActivity extends FoundationActivity {
    private boolean ShuConfirgurationMathiChe = false;
    private String BhashaKod = "";
    ActivityMangamtiBhasaPasandKarvaniBinding binding;
    private View back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMangamtiBhasaPasandKarvaniBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ShuConfirgurationMathiChe = getIntent().getBooleanExtra("ShuConfirgurationMathiChe", false);

        BhashaKod = PreUpyogi.getLanguageCode(MangamtiBhasaPasandKarvaniActivity.this);
        String phoneLanguage = Locale.getDefault().getLanguage();
        if (BhashaKod == null || BhashaKod.trim().isEmpty()) {
            try {
                BhashaKod = getResources().getConfiguration().locale.getLanguage();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        List<Bhasha> bhashaList = new ArrayList<>();
        bhashaList.add(new Bhasha("French", "fr", R.drawable.french));
        bhashaList.add(new Bhasha("English", "en", R.drawable.english));
        bhashaList.add(new Bhasha("Spanish", "es", R.drawable.spanish));
        bhashaList.add(new Bhasha("Portuguese", "pt", R.drawable.portiguese));
        bhashaList.add(new Bhasha("Hindi", "hi", R.drawable.hindi));
        bhashaList.add(new Bhasha("Afrikaans", "af", R.drawable.africa_launguaze));
//        bhashaList.add(new Bhasha("Arabic", "ar", R.drawable.arabic_image));


        boolean ShuBhashaDefault_che = false;
        Bhasha defaultBhasha = null;
        if (BhashaKod != null && !BhashaKod.trim().isEmpty()) {
            for (Bhasha bhasha : bhashaList) {
                if (BhashaKod.equals(bhasha.getBhashacode())) {
                    bhasha.setShuBhashaPasandKari_lidhi_che(true);
                    ShuBhashaDefault_che = true;
                } else {
                    bhasha.setShuBhashaPasandKari_lidhi_che(false);
                }

                if (phoneLanguage != null && !phoneLanguage.trim().isEmpty() && phoneLanguage.equals(bhasha.getBhashacode())) {
                    defaultBhasha = bhasha;
                }
            }
        }

        if (!ShuBhashaDefault_che) {
            for (Bhasha bhasha : bhashaList) {
                if (bhasha.getBhashacode().equals("en")) {
                    bhasha.setShuBhashaPasandKari_lidhi_che(true);
                    defaultBhasha = bhasha;
                    break;
                }
            }
        }

        if (defaultBhasha != null) {
            bhashaList.remove(defaultBhasha);
            bhashaList.add(0, defaultBhasha);
        }

        back = findViewById(R.id.back);

        back.setOnClickListener(v -> {
            onBackPressed();
        });

        RecyclerView recyclerView = findViewById(R.id.listLanguages);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        MangamtiBhasaPasandKarvaniAdapter adapter = new MangamtiBhasaPasandKarvaniAdapter(this, bhashaList);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.imgRight).setOnClickListener(v -> {
            Bhasha bhasha = adapter.getSelectedItem();
            if (bhasha != null) {
                PreUpyogi.setLanguageCode(MangamtiBhasaPasandKarvaniActivity.this, bhasha.getBhashacode());
            } else {
                PreUpyogi.setLanguageCode(MangamtiBhasaPasandKarvaniActivity.this, "");
            }
            updateLanguage(MangamtiBhasaPasandKarvaniActivity.this);
            gotoFinalActivity();
        });
    }


    private void gotoFinalActivity() {
//        if (!ShuConfirgurationMathiChe) {
        if (SupPref.getString(MangamtiBhasaPasandKarvaniActivity.this, SupPref.launguageBack, true)) {
            startActivity(new Intent(MangamtiBhasaPasandKarvaniActivity.this, PermitAccess.class));
            finish();
        } else {
            String code = PreUpyogi.getLanguageCode(MangamtiBhasaPasandKarvaniActivity.this);
            if (code != null && !code.equals(BhashaKod)) {
                Intent intent = new Intent(MangamtiBhasaPasandKarvaniActivity.this, Configuration.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast toast = Toast.makeText(MangamtiBhasaPasandKarvaniActivity.this, getString(R.string.select_Different_launguage), Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 20);
                toast.show();
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (SupPref.getString(MangamtiBhasaPasandKarvaniActivity.this, SupPref.launguageBack, true)) {
//        if (!ShuConfirgurationMathiChe) {
            startActivity(new Intent(MangamtiBhasaPasandKarvaniActivity.this, PermitAccess.class));
            finish();
        } else {
            Intent intent = new Intent(MangamtiBhasaPasandKarvaniActivity.this, Configuration.class);
            startActivity(intent);
            finish();
        }
    }


}
