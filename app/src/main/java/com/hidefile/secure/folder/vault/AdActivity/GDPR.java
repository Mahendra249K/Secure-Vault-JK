package com.hidefile.secure.folder.vault.AdActivity;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.ump.ConsentDebugSettings;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class GDPR {
    static ConsentInformation consentInformation;
    public static AtomicBoolean isMobileAdsInitializeCalled = new AtomicBoolean(false);

    public static void initGDPR(Activity activity,List<String> testDeviceIds,getAdsDataListner listner) {

            consentInformation = UserMessagingPlatform.getConsentInformation(activity);


        ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(activity)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                .addTestDeviceHashedId("7d1f2ae3-2769-4039-8295-47f328cd3e23")
                .build();

            ConsentRequestParameters params = new ConsentRequestParameters
                    .Builder()
                    .setConsentDebugSettings(debugSettings)
                    .setTagForUnderAgeOfConsent(false)
                    .build();
            consentInformation.requestConsentInfoUpdate(
                    activity,
                    params,
                    (ConsentInformation.OnConsentInfoUpdateSuccessListener) () -> {
                        UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                                activity,
                                (ConsentForm.OnConsentFormDismissedListener) loadAndShowError -> {
                                    if (loadAndShowError != null) {
                                        // Consent gathering failed.
                                        Log.w("TAG", String.format("%s: %s",
                                                loadAndShowError.getErrorCode(),
                                                loadAndShowError.getMessage()));
                                        AdmobSdk(activity,listner, testDeviceIds);
                                    }

                                    // Consent has been gathered.
                                    if (consentInformation.canRequestAds()) {
                                        initializeMobileAdsSdk(activity,listner, testDeviceIds);
                                    }
                                }
                        );

                    },
                    (ConsentInformation.OnConsentInfoUpdateFailureListener) requestConsentError -> {
                        // Consent gathering failed.
                        Log.w("TAG", String.format("%s: %s",
                                requestConsentError.getErrorCode(),
                                requestConsentError.getMessage()));

                        AdmobSdk(activity,listner, testDeviceIds);
//
                    });
            if (consentInformation.canRequestAds()) {
                initializeMobileAdsSdk(activity,listner, testDeviceIds);
            }

    }

    private static void initializeMobileAdsSdk(Activity activity,getAdsDataListner listner, List<String> testDeviceIds) {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return;
        }

        AdmobSdk(activity,listner, testDeviceIds);
    }

    private static void AdmobSdk(Activity activity,getAdsDataListner listner, List<String> testDeviceIds) {


        AdSettings.addTestDevices(testDeviceIds);

        AudienceNetworkAds.initialize(activity);

        MobileAds.initialize(activity, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {


                RequestConfiguration configuration =
                        new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
                MobileAds.setRequestConfiguration(configuration);

                Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                for (String adapterClass : statusMap.keySet()) {
                    AdapterStatus status = statusMap.get(adapterClass);
                    Log.d("MyApp", String.format(
                            "Adapter name: %s, Description: %s, Latency: %d",
                            adapterClass, status.getDescription(), status.getLatency()));
                }
//                PrintLog(TAG, "onInitializationComplete: admob");
                listner.onSuccess();
            }

        });


    }

    public interface getAdsDataListner {

        void onSuccess();
        void onFail();
    }

}
