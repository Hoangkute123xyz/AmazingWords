package com.hoangpro.amazingwords.activity;

import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hoangpro.amazingwords.R;
import com.hoangpro.amazingwords.base.BaseActivity;
import com.hoangpro.amazingwords.model.Account;
import com.hoangpro.amazingwords.morefunc.DatabaseAccess;
import com.hoangpro.amazingwords.morefunc.MyAnimation;
import com.hoangpro.amazingwords.sqlite.User;

import org.json.JSONException;
import org.json.JSONObject;

import static com.hoangpro.amazingwords.morefunc.MySession.currentAccount;
import static com.hoangpro.amazingwords.morefunc.MySession.updateAccount;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";
    private ImageView imgAmazing;
    private ImageView imgWords;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_login);
        initView();
        firebaseAuth = FirebaseAuth.getInstance();
        DatabaseAccess databaseAccess = new DatabaseAccess(this);
        databaseAccess.createDatabase();
        callbackManager = CallbackManager.Factory.create();
        loginButton.setPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFaceBookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Dialog dialog;
    private FirebaseAuth firebaseAuth;

    private void showLoading() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(LayoutInflater.from(this).inflate(R.layout.dialog_loading, null, false));
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        dialog = alertDialog;
        alertDialog.show();
    }

    private void handleFaceBookAccessToken(final AccessToken token) {
        showLoading();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    GraphRequest graphRequest = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            if (object != null) {
                                try {
                                    Log.e(TAG, response.toString());
                                    final String email = object.isNull("email") ? "" : object.getString("email");
                                    final String name = object.isNull("name") ? "" : object.getString("name");
                                    final String idFacebook = object.isNull("id") ? "" : object.getString("id");
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("accounts").child(idFacebook);
                                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            currentAccount = dataSnapshot.getValue(Account.class);
                                            if (currentAccount==null) {
                                                currentAccount = new Account(idFacebook, name, email);
                                                updateAccount(LoginActivity.this, currentAccount);
                                                Log.e("isExistData", "false");
                                            } else {
                                                Log.e("isExistData", currentAccount.toString());
                                                User.writeUser(LoginActivity.this);
                                            }
                                            openActivity(MainActivity.class, true);
                                            overridePendingTransition(0, 0);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    Bundle parameter = new Bundle();
                    parameter.putString("fields", "id,name,email");
                    graphRequest.setParameters(parameter);
                    graphRequest.executeAsync();

                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        User.loadUser(this);
        if (!currentAccount.idFacebook.isEmpty()) {
            openActivity(MainActivity.class, true);
            overridePendingTransition(0, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        imgAmazing = findViewById(R.id.imgAmazing);
        imgWords = findViewById(R.id.imgWords);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(MyAnimation.setAnimScaleXY(imgWords)).after(MyAnimation.setAnimScaleXY(imgAmazing));
        animatorSet.start();
        loginButton = findViewById(R.id.login_button);
    }

    public void loginAction(View view) {
        loginButton.performClick();
    }
}
