package ru.mgusev.eldritchhorror.activity;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import com.adcolony.sdk.*;
import java.util.Date;
import ru.mgusev.eldritchhorror.R;

public class AdColonyHelper {

    private final static String APP_ID = "appfe3bcc9bb3be4c1190";
    private final static String ZONE_ID = "vze74bd5b196ed4ff49c";
    final private String TAG = "AdColonyDemo";

    private AdColonyInterstitial adc = null;
    private AdColonyInterstitialListener listener;
    private boolean isAdvertisingLoad = false;
    DateHelper dateHelper;

    public AdColonyHelper(final Activity activity) {


        dateHelper = new DateHelper(activity);

        AdColonyAppOptions app_options = new AdColonyAppOptions().setUserID(Installation.id(activity));

        /**
         * Configure AdColonyHelper in your launching Activity's onCreate() method so that cached ads can
         * be available as soon as possible.
         */
        AdColony.configure(activity, app_options, APP_ID, ZONE_ID);

        /** Create and set a reward listener */
        AdColony.setRewardListener(new AdColonyRewardListener() {
            @Override
            public void onReward(AdColonyReward reward) {
                /** Query reward object for info here */
                Log.d( TAG, "onReward" );
                adc = null;
                dateHelper.saveDate((new Date()).getTime());
                Toast.makeText(activity, R.string.view_success, Toast.LENGTH_LONG).show();
            }
        });

        /**
         * Set up listener for interstitial ad callbacks. You only need to implement the callbacks
         * that you care about. The only required callback is onRequestFilled, as this is the only
         * way to get an ad object.
         */
        listener = new AdColonyInterstitialListener() {
            /** Ad passed back in request filled callback, ad can now be shown */
            @Override
            public void onRequestFilled(AdColonyInterstitial ad) {
                adc = ad;
                isAdvertisingLoad = true;
                Log.d(TAG, "onRequestFilled");
            }

            /** Ad request was not filled */
            @Override
            public void onRequestNotFilled( AdColonyZone zone ) {
                adc = null;
                isAdvertisingLoad = false;
                Log.d(TAG, "onRequestNotFilled");
            }

            /** Ad opened, reset UI to reflect state change */
            @Override
            public void onOpened(AdColonyInterstitial ad) {
                Log.d(TAG, "onOpened");
            }

            /** Request a new ad if ad is expiring */
            @Override
            public void onExpiring(AdColonyInterstitial ad) {
                AdColony.requestInterstitial(ZONE_ID, this);
                Log.d( TAG, "onExpiring" );
            }
        };
    }

    public void startRequestInterstitial() {
        /**
         * It's somewhat arbitrary when your ad request should be made. Here we are simply making
         * a request if there is no valid ad available onResume, but really this can be done at any
         * reasonable time before you plan on showing an ad.
         */
        if (adc == null || adc.isExpired()) {
            /**
             * Optionally update location info in the ad options for each request:
             * LocationManager location_manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
             * Location location = location_manager.getLastKnownLocation( LocationManager.GPS_PROVIDER );
             * ad_options.setUserMetadata( ad_options.getUserMetadata().setUserLocation( location ) );
             */
            AdColony.requestInterstitial(ZONE_ID, listener);
        }
    }

    public boolean isAdvertisingLoad() {
        return isAdvertisingLoad;
    }

    public AdColonyInterstitial getAdc() {
        return adc;
    }
}