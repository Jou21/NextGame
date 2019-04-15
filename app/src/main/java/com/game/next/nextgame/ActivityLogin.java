package com.game.next.nextgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.HashMap;

public class ActivityLogin extends AppCompatActivity{

    private EditText edtEmail, edtSenha;
    private Button btnEntrar, btnCadastrar;
    private Button loginButton;
    private TextView txtEsqueceuSenha;
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    String imageURL = "";
    String username = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setSystemBarTheme(ActivityLogin.this,true,R.color.branco);
        }

        edtEmail = (EditText) findViewById(R.id.edt_login_email);
        edtSenha = (EditText) findViewById(R.id.edt_login_senha);
        btnEntrar = (Button) findViewById(R.id.btn_login_entrar);
        btnCadastrar = (Button) findViewById(R.id.btn_login_cadastrar);
        loginButton = (Button) findViewById(R.id.login_button);
        txtEsqueceuSenha = (TextView) findViewById(R.id.txt_forgot_password) ;

        txtEsqueceuSenha.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telaResetPassword = new Intent(ActivityLogin.this, ResetPasswordActivity.class);
                startActivity(telaResetPassword);
                finish();
            }
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!edtEmail.getText().toString().isEmpty() || !edtSenha.getText().toString().isEmpty()){
                    mAuth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtSenha.getText().toString())
                            .addOnCompleteListener(ActivityLogin.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent it = new Intent(ActivityLogin.this, MainActivity.class);
                                        it.putExtra("EMAIL", user.getEmail());
                                        startActivity(it);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(ActivityLogin.this, "Erro: " + task.getException(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }else {
                    Toast.makeText(ActivityLogin.this, "Você precisa preencher o e-mail e a senha!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ActivityLogin.this, ActivityCadastro.class);
                startActivity(it);
                finish();
            }
        });

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(ActivityLogin.this, "Login efetuado com sucesso!", Toast.LENGTH_LONG).show();
                        handleFacebookAccessToken(loginResult.getAccessToken());
                        // App code
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {

                                        // Application code
                                        try {
                                            String facebookUserId = object.getString("id");
                                            //String email = object.getString("email");
                                            //String birthday = object.getString("birthday"); // 01/31/1980 format
                                            username = object.getString("name");
                                            imageURL = "https://graph.facebook.com/" + facebookUserId + "/picture?type=large";




                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday");
                        request.setParameters(parameters);
                        request.executeAsync();


                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(ActivityLogin.this, "Login Cancelado!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(ActivityLogin.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(ActivityLogin.this, Arrays.asList("public_profile", "email", "user_birthday"));
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String firebaseUserId = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUserId);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", firebaseUserId);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", imageURL);
                            hashMap.put("status", "offline");
                            hashMap.put("search", username.toLowerCase());

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        //intent.putExtra("EMAIL", email);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(ActivityLogin.this, "Falha na autenticação!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static final void setSystemBarTheme(final Activity pActivity, final boolean textIsDark, int corStatusBar) {

        Window window = pActivity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(pActivity,corStatusBar));

        // Fetch the current flags.
        final int lFlags = window.getDecorView().getSystemUiVisibility();
        // Update the SystemUiVisibility dependening on whether we want a Light or Dark theme.
        pActivity.getWindow().getDecorView().setSystemUiVisibility(textIsDark ? (lFlags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) : (lFlags & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR));
    }

    @Override
    protected void onStart() {
        super.onStart();
        verificarUsuarioLogado();
    }

    public void verificarUsuarioLogado(){
        mAuth = getFirebaseAutenticacao();

        if( mAuth.getCurrentUser() != null ){
            Intent it = new Intent(ActivityLogin.this, MainActivity.class);
            startActivity(it);
            finish();
        }
    }

    //retorna a instancia do FirebaseAuth
    public FirebaseAuth getFirebaseAutenticacao(){
        if( mAuth == null ){
            mAuth = FirebaseAuth.getInstance();
        }
        return mAuth;

    }


    @Override
    public void onBackPressed() {
        finish();
        //finishAffinity();
        super.onBackPressed();
    }
}

