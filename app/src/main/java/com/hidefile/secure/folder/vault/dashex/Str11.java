package com.hidefile.secure.folder.vault.dashex;

import static android.os.Build.VERSION.SDK_INT;
import static com.hidefile.secure.folder.vault.cluecanva.SupPref.isDarkModeOn;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hidefile.secure.folder.vault.AdActivity.Call_Back_Ads;
import com.hidefile.secure.folder.vault.AdActivity.Common_Adm;
import com.hidefile.secure.folder.vault.AdActivity.GDPR;
import com.hidefile.secure.folder.vault.AdActivity.Open_App_Manager;
import com.hidefile.secure.folder.vault.AdActivity.Preference_Ads;
import com.hidefile.secure.folder.vault.AdActivity.SharedPref;
import com.hidefile.secure.folder.vault.R;
import com.hidefile.secure.folder.vault.cluecanva.SupPref;
import com.hidefile.secure.folder.vault.cluecanva.TilsCo;
import com.hidefile.secure.folder.vault.cluecanva.TooRfl;
import com.hidefile.secure.folder.vault.dpss.PrefHandler;

import java.util.ArrayList;

public class Str11 extends AppCompatActivity {

    private static final String TAG = "Str11";
    LinearLayout privacyacceptedlayout;
    LinearLayout showActionContainads;
    Preference_Ads adsDataPrefs;
    CheckBox checkboxview;
    AdRequest build2;
    TextView askprivacyAccepteText;
    Button getstartingEnableButton ,getstartingdisableButton ;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.str11);
        Common_Adm.mInstance = null;

        this.adsDataPrefs = new Preference_Ads(this);
        build2 = new AdRequest.Builder().build();
        hideNavigationBar();

        privacyacceptedlayout=findViewById(R.id.privacyacceptedlayout);
        showActionContainads=findViewById(R.id.showActionContainads);
        checkboxview=findViewById(R.id.checkboxview);
        askprivacyAccepteText=findViewById(R.id.askprivacyAccepteText);
        getstartingEnableButton=findViewById(R.id.getstartingEnableButton);
        getstartingdisableButton=findViewById(R.id.getstartingdisableButton);

        if (!PrefHandler.getBooleanPref(this, PrefHandler.PRIVACY_ACCEPTED)) {
            AskToUserPrivacyPermissionAccept();
        } else {

            checkinternet();
        }
    }



    private void checkinternet() {
        ArrayList<String> testDevices = new ArrayList<>();
        testDevices.add("DD2E35E506D1C99B6F4D4146B7B7F0E4");
        GDPR.initGDPR(this, testDevices, new GDPR.getAdsDataListner() {
            @Override
            public void onSuccess() {
                callFirebaseTest();
//                connectFirebase();
            }
            @Override
            public void onFail() {
                Toast.makeText(Str11.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callFirebaseTest() {
        FirebaseFirestore.getInstance().collection(getResources().getString(R.string.firebase_name)).document("Ads").get().
                addOnSuccessListener(documentSnapshot -> {
            Log.e("vv", "onDataChange: " + documentSnapshot);
            Preference_Ads adsDataPrefs = new Preference_Ads(this);
            adsDataPrefs.setAdmBannerID("ca-app-pub-3940256099942544/9214589741");
            adsDataPrefs.setAdmInterstitialID("ca-app-pub-3940256099942544/1033173712");
            adsDataPrefs.setAdmNativeID("ca-app-pub-3940256099942544/2247696110");
            adsDataPrefs.setAdmNativeVideoID("ca-app-pub-3940256099942544/1044960115");
            adsDataPrefs.setAdmAppOpenID("ca-app-pub-3940256099942544/9257395921");
            adsDataPrefs.setbanner_enable(true);
            adsDataPrefs.setAppOpenSplashEnable(true);
//            adsDataPrefs.setAppOpenSplashEnable(documentSnapshot.getBoolean("APP_OPEN_ENABLE").booleanValue());
            adsDataPrefs.setFbBannerID("IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID");
            adsDataPrefs.setFbInterstitialID("VID_HD_9_16_39S_APP_INSTALL#YOUR_PLACEMENT_ID");
            adsDataPrefs.setFbNativeID("VID_HD_9_16_39S_APP_INSTALL#YOUR_PLACEMENT_ID");
            adsDataPrefs.setFbNativeBannerID("VID_HD_9_16_39S_APP_INSTALL#YOUR_PLACEMENT_ID");
            adsDataPrefs.setInterstitialBackPressClickGap(documentSnapshot.getString("INTERSTITIAL_BACKPRESS_CLICK_MODULE"));
            adsDataPrefs.setInterstitialClickGap(String.valueOf(4));
            adsDataPrefs.setinterstitial_enable(true);
            adsDataPrefs.setInterstitialEnableBackPress(documentSnapshot.getBoolean("INTERSTITIAL_ENABLE_BACKPRESS").booleanValue());
//            adsDataPrefs.setInterstitialGapEnable(documentSnapshot.getBoolean("INTERSTITIAL_GAP_ENABLE").booleanValue());
            adsDataPrefs.setnative_ads_enable(true);
            adsDataPrefs.set_priority("AM");

        }).addOnFailureListener(e -> {
//                        Toast.makeText(activity, "Failer", Toast.LENGTH_SHORT).show();
            init();
        });


            if (adsDataPrefs.getAppOpenSplashEnable()) {//if true

            AppOpenAd.load(Str11.this, adsDataPrefs.getAdmAppOpenID(), build2, 1, new AppOpenAd.AppOpenAdLoadCallback() {
                public void onAdLoaded(AppOpenAd appOpenAd) {//load app open
                    super.onAdLoaded((AppOpenAd) appOpenAd);
                    appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);
                            init();
                        }
                        @Override
                        public void onAdDismissedFullScreenContent() {//dismiss app open
                            super.onAdDismissedFullScreenContent();
                            init();
                        }
                    });
                    appOpenAd.show(Str11.this);
                }
                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {//error in load app open ad
                    super.onAdFailedToLoad(loadAdError);
                    init();
                }
            });
        } else {
            init();
        }
    }

    private void connectFirebase() {
        FirebaseFirestore.getInstance().collection(getResources().getString(R.string.firebase_name))
                .document("Ads").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.e("vv", "onDataChange: " + documentSnapshot);
                        adsDataPrefs.setAdmBannerID(documentSnapshot.get("AM_BANNER_ID").toString());
                        adsDataPrefs.setAdmInterstitialID(documentSnapshot.get("AM_INTERSTITIAL_ID").toString());
                        adsDataPrefs.setAdmNativeID(documentSnapshot.get("AM_NATIVE_ID").toString());
                        adsDataPrefs.setAppOpenSplashEnable(documentSnapshot.getBoolean("APP_OPEN_ENABLE").booleanValue());
                        adsDataPrefs.setAdmAppOpenID(documentSnapshot.get("APP_OPEN_ID").toString());
                        adsDataPrefs.setbanner_enable(documentSnapshot.getBoolean("BANNER_ENABLE").booleanValue());
                        adsDataPrefs.setFbBannerID(documentSnapshot.get("FB_BANNER_ID").toString());
                        adsDataPrefs.setFbInterstitialID(documentSnapshot.get("FB_INTERSTITIAL_ID").toString());
                        adsDataPrefs.setFbNativeID(documentSnapshot.get("FB_NATIVE_ID").toString());
                        adsDataPrefs.setInterstitialBackPressClickGap(documentSnapshot.getString("INTERSTITIAL_BACKPRESS_CLICK_MODULE"));
                        adsDataPrefs.setInterstitialClickGap(documentSnapshot.getString("INTERSTITIAL_CLICK_MODULE"));
                        adsDataPrefs.setinterstitial_enable(documentSnapshot.getBoolean("INTERSTITIAL_ENABLE").booleanValue());
                        adsDataPrefs.setInterstitialEnableBackPress(documentSnapshot.getBoolean("INTERSTITIAL_ENABLE_BACKPRESS").booleanValue());
                        adsDataPrefs.setnative_ads_enable(documentSnapshot.getBoolean("NATIVE_ENABLE").booleanValue());
                        adsDataPrefs.set_priority(documentSnapshot.get("PRIORITY").toString());
                        adsDataPrefs.setFbNativeBannerID(documentSnapshot.get("FB_NATIVE_BANNER_ID").toString());
                        adsDataPrefs.setAdmNativeVideoID(documentSnapshot.get("AM_NATIVE_VIDEO_ID").toString());

                    }
                });

        FirebaseFirestore.getInstance().collection(getResources().getString(R.string.firebase_name))
                .document("Ads").get().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        init();
                    }
                });

            if (adsDataPrefs.getAppOpenSplashEnable()) {//if true
            AdRequest build2 = new AdRequest.Builder().build();
            AppOpenAd.load(Str11.this, adsDataPrefs.getAdmAppOpenID(), build2, 1, new AppOpenAd.AppOpenAdLoadCallback() {
                public void onAdLoaded(AppOpenAd appOpenAd) {//load app open
                    super.onAdLoaded((AppOpenAd) appOpenAd);
                    appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);
                            init();
                        }
                        @Override
                        public void onAdDismissedFullScreenContent() {//dismiss app open
                            super.onAdDismissedFullScreenContent();
                            init();//intent next activity
                        }
                    });
                    appOpenAd.show(Str11.this);
                }
                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {//error in load app open ad
                    super.onAdFailedToLoad(loadAdError);
                    init();//intent next activity
                }
            });
        } else {
            init();
        }
    }


    public void hideNavigationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }
    }

    private void AskToUserPrivacyPermissionAccept(){
        privacyacceptedlayout.setVisibility(View.VISIBLE);
        showActionContainads.setVisibility(View.GONE);
        checkboxview.setVisibility(View.VISIBLE);
        getstartingdisableButton.setVisibility(View.VISIBLE);
        getstartingEnableButton.setVisibility(View.GONE);
        SpannableString text = new SpannableString("Privacy Policy");
        ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(getResources().getColor(R.color.appColor));
        text.setSpan(foregroundSpan, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        askprivacyAccepteText.setText(Html.fromHtml("I agree and accept <a href='https://securefolderapp.blogspot.com/2023/01/secure-folder-hide-image-vid.html'>" + text + "</a>"));
        askprivacyAccepteText.setMovementMethod(LinkMovementMethod.getInstance());
        checkboxview.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getstartingdisableButton.setVisibility(View.GONE);
                    getstartingEnableButton.setVisibility(View.VISIBLE);
                } else {
                    getstartingdisableButton.setVisibility(View.VISIBLE);
                    getstartingEnableButton.setVisibility(View.GONE);
                }
            }
        });
        getstartingEnableButton.setOnClickListener(v -> {
            PrefHandler.setBooleanPref(this,PrefHandler.PRIVACY_ACCEPTED,true);
            privacyacceptedlayout.setVisibility(View.GONE);
            showActionContainads.setVisibility(View.VISIBLE);
                          checkinternet();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        System.exit(0);
    }

    private void init() {
        TilsCo.isStr = true;
        TooRfl.deleteTempFolder();
        if (isPermitted()) {
            String hidden_URI = SupPref.getHideUri(Str11.this);
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (hidden_URI == null || hidden_URI.trim().isEmpty()) {
                    permitAccess();
                } else {
                    try {
                        getContentResolver().takePersistableUriPermission(Uri.parse(hidden_URI), Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        nextMove();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                        permitAccess();
                    }
                }
            } else {
                nextMove();
            }
        } else {
            permitAccess();
        }
    }
    private boolean isPermitted() {
        boolean isPermissionGranted = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            String permissionAudio = android.Manifest.permission.READ_MEDIA_AUDIO;
            String permissionVideo = android.Manifest.permission.READ_MEDIA_VIDEO;
            String permissionImage = android.Manifest.permission.READ_MEDIA_IMAGES;
            String post_notifications = android.Manifest.permission.POST_NOTIFICATIONS;
            String permissionCamera = Manifest.permission.CAMERA;
            String read_phone_state = Manifest.permission.READ_PHONE_STATE;
            if (  checkCallingOrSelfPermission(permissionAudio) == PackageManager.PERMISSION_GRANTED &&
                    checkCallingOrSelfPermission(permissionVideo) == PackageManager.PERMISSION_GRANTED &&
                    checkCallingOrSelfPermission(permissionImage) == PackageManager.PERMISSION_GRANTED &&
                    checkCallingOrSelfPermission(permissionCamera) == PackageManager.PERMISSION_GRANTED &&
                    checkCallingOrSelfPermission(post_notifications) == PackageManager.PERMISSION_GRANTED &&
                    checkCallingOrSelfPermission(read_phone_state) == PackageManager.PERMISSION_GRANTED
            ) {
                isPermissionGranted = true;
            }
        } else {
            String permissionCamera = android.Manifest.permission.CAMERA;
            String read_phone_state = android.Manifest.permission.READ_PHONE_STATE;

            if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                String permissionStorage = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
                String permissionStorageRead = android.Manifest.permission.READ_EXTERNAL_STORAGE;

                if (checkCallingOrSelfPermission(permissionStorage) == PackageManager.PERMISSION_GRANTED &&
                        checkCallingOrSelfPermission(permissionStorageRead) == PackageManager.PERMISSION_GRANTED &&
                        checkCallingOrSelfPermission(permissionCamera) == PackageManager.PERMISSION_GRANTED &&
                        checkCallingOrSelfPermission(read_phone_state) == PackageManager.PERMISSION_GRANTED) {
                    isPermissionGranted = true;
                }

            } else {
                String permissionStorageRead = android.Manifest.permission.READ_EXTERNAL_STORAGE;
                if (checkCallingOrSelfPermission(permissionStorageRead) == PackageManager.PERMISSION_GRANTED &&
                        checkCallingOrSelfPermission(permissionCamera) == PackageManager.PERMISSION_GRANTED &&
                        checkCallingOrSelfPermission(read_phone_state) == PackageManager.PERMISSION_GRANTED) {
                    isPermissionGranted = true;
                }
            }
        }
        return isPermissionGranted;
    }
    private void permitAccess() {
        if(!isLanguageSet()){
            SupPref.putString(Str11.this,"launguageBack",true);
            Intent i = new Intent(Str11.this, MangamtiBhasaPasandKarvaniActivity.class);
            startActivity(i);
            finish();
        }
        else {
            Intent i = new Intent(Str11.this, PermitAccess.class);
            startActivity(i);
            finish();
        }
    }
    private void nextMove() {
        Intent i = new Intent(Str11.this, Pswd.class);
        startActivity(i);
        finish();
    }
    private boolean isLanguageSet() {
        SharedPref sharedPref = new SharedPref(this);
        String BhashaKod = sharedPref.getLanguageCode();
        return BhashaKod != null && !BhashaKod.trim().isEmpty();
    }
}
