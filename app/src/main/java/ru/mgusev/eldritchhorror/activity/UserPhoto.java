package ru.mgusev.eldritchhorror.activity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import com.google.firebase.auth.FirebaseUser;
import java.io.InputStream;
import java.net.URL;

public class UserPhoto {

    private static UserPhoto userPhoto;
    private static Drawable drawablePhoto;
    private MainActivity main;

    public static UserPhoto initUserPhoto() {
        if (userPhoto == null) {
            userPhoto = new UserPhoto();
        }
        return userPhoto;
    }

    public Drawable getPhoto(FirebaseUser user) {
        if (drawablePhoto == null) {
            new AsyncTask<String, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(String... params) {
                    try {
                        URL url = new URL(params[0]);
                        InputStream in = url.openStream();
                        return BitmapFactory.decodeStream(in);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    drawablePhoto = new BitmapDrawable(Resources.getSystem(), BitmapCircle.getCircleBitmap(bitmap, 96));
                    main.setPhoto();
                }
            }.execute(user.getPhotoUrl().toString());
        }
        return drawablePhoto;
    }

    public void clearPhoto() {
        drawablePhoto = null;
    }

    public void setMain(MainActivity main) {
        this.main = main;
    }
}