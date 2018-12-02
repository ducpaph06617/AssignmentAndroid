package com.dev.ducpaph.assignmentandroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class FunctionActivity extends AppCompatActivity {

    private EditText edLink;
    private ImageView ImgPicture;
    private Button btShareLink;
    private Button btShareImage;
    private ShareDialog shareDialog;
    private ShareLinkContent shareLinkContent;
    public static int selectImg = 1;
    public Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.function_activity);




        edLink = (EditText) findViewById(R.id.edLink);
        ImgPicture = (ImageView) findViewById(R.id.ImgPicture);
        btShareLink = (Button) findViewById(R.id.btShareLink);
        btShareImage = (Button) findViewById(R.id.btShareImage);

        shareDialog = new ShareDialog(this);
        btShareLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shareDialog.canShow(ShareLinkContent.class)){
                    shareLinkContent = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(edLink.getText().toString().trim()))
                            .build();
                }
                shareDialog.show(shareLinkContent);
            }
        });
        ImgPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,selectImg);
            }
        });
        btShareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(bitmap)
                        .build();
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                shareDialog.show(content);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == selectImg && resultCode == RESULT_OK){
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                 bitmap  = BitmapFactory.decodeStream(inputStream);
                 ImgPicture.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
