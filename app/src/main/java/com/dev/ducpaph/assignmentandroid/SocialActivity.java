package com.dev.ducpaph.assignmentandroid;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SocialActivity extends AppCompatActivity {
    private ProfilePictureView friendProfilePicture;
    private LoginButton loginButton;
    private Button btLogout;
    private TextView tvName, tvEmail;
    private Button btFunction;
    private CallbackManager callbackManager;
    String email, pname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.social_activity);

        initViews();

        btFunction.setVisibility(View.INVISIBLE);
        btLogout.setVisibility(View.INVISIBLE);
        tvName.setVisibility(View.INVISIBLE);
        tvEmail.setVisibility(View.INVISIBLE);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        setLoginButton();
        setLogoutButton();
        changeScreen();


        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(
                    "com.dev.ducpaph.assignmentandroid",
                    PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        for (Signature signature : info.signatures) {
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    }

    private void initViews() {
        friendProfilePicture = (ProfilePictureView) findViewById(R.id.friendProfilePicture);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        btLogout = (Button) findViewById(R.id.btLogout);
        tvName = (TextView) findViewById(R.id.tvName);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        btFunction = (Button) findViewById(R.id.btFunction);

    }

    private void changeScreen() {

        btFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SocialActivity.this, FunctionActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setLogoutButton() {
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                btLogout.setVisibility(View.INVISIBLE);
                btFunction.setVisibility(View.INVISIBLE);
                tvName.setVisibility(View.INVISIBLE);
                tvEmail.setVisibility(View.INVISIBLE);
                tvName.setText("");
                tvEmail.setText("");
                friendProfilePicture.setProfileId(null);
                loginButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setLoginButton() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                loginButton.setVisibility(View.INVISIBLE);
                btFunction.setVisibility(View.VISIBLE);
                btLogout.setVisibility(View.VISIBLE);
                tvName.setVisibility(View.VISIBLE);
                tvEmail.setVisibility(View.VISIBLE);

                result();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    private void result() {
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.e("JSON", response.getJSONObject().toString());
                try {
                    email = object.getString("email");
                    pname = object.getString("name");

                    friendProfilePicture.setProfileId(Profile.getCurrentProfile().getId());

                    tvName.setText(pname);
                    tvEmail.setText(email);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();


    }

    @Override
    protected void onStart() {
        LoginManager.getInstance().logOut();
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
