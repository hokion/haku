package com.hokion.haku;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class Launcher extends AppCompatActivity {

    private final int REQUEST_CODE = 1;
    private final int SELECT_PHOTO = 2;
    private Button save;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        //TODO confirm service is not currently running? Is this intuitively done?

        checkDrawOverlayPermission();

        ListView list=(ListView) findViewById(R.id.listView);
        list.setAdapter(new ListHandler(this));

        Button change = (Button) findViewById(R.id.change);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO make persistent
                DrawerService.changeIcon(imageUri);
            }
        });
        save.setEnabled(false);
    }

    public void checkDrawOverlayPermission() {
        if (Build.VERSION.SDK_INT > 22 && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            startService(new Intent(this, DrawerService.class));
        }
    }

    @TargetApi(23)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case REQUEST_CODE:
            if (Settings.canDrawOverlays(this)) {
                startService(new Intent(this, DrawerService.class));
            }
            break;
        case SELECT_PHOTO:
            if(resultCode == RESULT_OK){
                imageUri = data.getData();
                ImageView icon = (ImageView) findViewById(R.id.current);
                icon.setImageBitmap(ImageProcessing.scaleImage(this, imageUri, 432));
                save.setEnabled(true);
            }
            break;
        }
    }
}