package ru.mgusev.eldritchhorror.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.mgusev.eldritchhorror.R;

public class DonateActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String donateURL = "https://money.yandex.ru/to/410014579099919";

    TextView version;
    Toolbar toolbar;
    Button donateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        initToolbar();

        version = (TextView) findViewById(R.id.version);
        String versionText = getResources().getString(R.string.version_header) + getVersionName();
        version.setText(versionText);

        donateBtn = (Button) findViewById(R.id.button_donate);
        donateBtn.setOnClickListener(this);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_donate);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.about);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String getVersionName() {
        String versionName = "";
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_donate:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(donateURL));
                startActivity(browserIntent);
                break;
            default:
                break;
        }
    }
}
