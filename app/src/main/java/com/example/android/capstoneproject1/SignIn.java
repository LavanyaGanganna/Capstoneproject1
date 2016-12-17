package com.example.android.capstoneproject1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lavanya.myapplication.backend.usersApi.UsersApi;
import com.example.lavanya.myapplication.backend.usersApi.model.Users;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
//import com.paypal.android.sdk.payments.PayPalConfiguration;
//import com.paypal.android.sdk.payments.PayPalService;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignIn extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = SignIn.class.getSimpleName();

    boolean valid = true;
    @InjectView(R.id.input_email)
    EditText emailText;
    @InjectView(R.id.input_password)
    EditText passwordText;
    @InjectView(R.id.btn_login)
    Button loginButton;
    @InjectView(R.id.link_signup)
    TextView signupLink;
    String errormsg = null;
    private static final int REQUEST_SIGNUP = 0;
    boolean isLogin = false;
    double price;
    boolean paypals;
    String email = null;
    String password = null;
    ProgressBar progressBar;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        paypals = sharedPreferences.getBoolean(getString(R.string.paypalsh), false);
        if (paypals) {
            price = getIntent().getDoubleExtra(getString(R.string.price), 0);
        }
        ButterKnife.inject(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                    // ...
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Typeface darllarch = Typeface.createFromAsset(getAssets(), getString(R.string.personalttf));
        TextView mytitle = (TextView) toolbar.getChildAt(0);
        mytitle.setTypeface(darllarch);
        mytitle.setTextSize(30);
        getSupportActionBar().setTitle(R.string.signins);
        boolean tabletsize = getResources().getBoolean(R.bool.isTablet);
        if (tabletsize) {
            mytitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45);
        }
        TextWatcher mtextwatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkFieldsForEmptyValues();
            }
        };
        emailText.addTextChangedListener(mtextwatcher);
        passwordText.addTextChangedListener(mtextwatcher);
        checkFieldsForEmptyValues();
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SignIn.this);
                paypals = sharedPreferences.getBoolean(getString(R.string.paypalsh), false);
                if (paypals) {
                    intent.putExtra(getString(R.string.price), price);
                }
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });


    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);
        email = emailText.getText().toString();
        password = passwordText.getText().toString();
        new EndpointsAsyncTask().execute(email, password);


    }

    void checkFieldsForEmptyValues() {

        String s1 = emailText.getText().toString();
        String s2 = passwordText.getText().toString();

        if (s1.equals("") || s2.equals("")) {
            loginButton.setEnabled(false);
        } else {
            loginButton.setEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                Intent intent = new Intent(SignIn.this, SignIn.class);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SignIn.this);
                paypals = sharedPreferences.getBoolean(getString(R.string.paypalsh), false);
                if (paypals) {
                    intent.putExtra(getString(R.string.price), getIntent().getDoubleExtra(getString(R.string.price), 0));
                }
                startActivity(intent);

            }
        }

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            if (acct != null) {
                Log.e(TAG, "display name: " + acct.getDisplayName());
                String personName = acct.getDisplayName();
                email = acct.getEmail();
                Log.e(TAG, "Name: " + personName + ", email: " + email);
                new GoogleAsyncTask().execute(email, null, null);
            }
        } else {
            Toast.makeText(this, R.string.unablegoogle, Toast.LENGTH_LONG).show();
        }
    }

    public void onLoginSuccess() {
        Toast.makeText(getApplicationContext(), R.string.loginsuce, Toast.LENGTH_SHORT).show();
        loginButton.setEnabled(true);
        isLogin = true;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putBoolean(getString(R.string.islogin), isLogin).apply();
        sharedPreferences.edit().putString(getString(R.string.emailaddr), email).apply();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        paypals = prefs.getBoolean(getString(R.string.paypalsh), false);
        if (paypals) {

            //   double amount=getIntent().getDoubleExtra("price",0);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            preferences.edit().putBoolean(getString(R.string.paylogin), true).apply();
            Intent intentcheck = new Intent(SignIn.this, Checkout.class);
            intentcheck.putExtra(getString(R.string.price), price);
            startActivity(intentcheck);
        }
        if (email != null && password != null) {
            if (email.equals(getString(R.string.adminss)) && (password.equals(getString(R.string.mannamspass)))) {
                Intent intent = new Intent(SignIn.this, Admindisplay.class);
                startActivity(intent);
            }
        } else {
            finish();
        }
    }

    public boolean validate() {
        emailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String email = emailText.getText().toString();
                if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailText.setError(getString(R.string.validemail));

                } else {
                    emailText.setError(null);
                }
            }
        });
        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String password = passwordText.getText().toString();
                if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
                    passwordText.setError(getString(R.string.enterfour));

                } else {
                    passwordText.setError(null);
                }
            }
        });

        return valid;
    }


    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

        dlgAlert.setMessage(R.string.wrongpassword);
        dlgAlert.setTitle(R.string.errorrr);
        dlgAlert.setPositiveButton(R.string.oks, null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();


        dlgAlert.setPositiveButton(R.string.okk,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        loginButton.setEnabled(true);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    class EndpointsAsyncTask extends AsyncTask<String, Void, Users[]> {
        private UsersApi myApiService = null;
        Users[] usersarray = new Users[2];
        Users retuser;
        Users pretuser;

        @Override
        protected Users[] doInBackground(String... strings) {
            if (myApiService == null) {  // Only do this once
                UsersApi.Builder builder = new UsersApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                        // .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                        .setRootUrl(getString(R.string.appspotsign))
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                // end options for devappserver

                myApiService = builder.build();
            }

            String email = strings[0];
            String pswrd = strings[1];


            try {
                retuser = myApiService.checkUser(email).execute();
                usersarray[0] = retuser;
                if (retuser != null) {

                    pretuser = myApiService.checkpword(email, pswrd).execute();
                    usersarray[1] = pretuser;
                }


            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return usersarray;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(Users[] result) {
//            Toast.makeText(SignUp.this, returnuser.getEmail(), Toast.LENGTH_LONG).show();
            if (progressBar != null)
                progressBar.setVisibility(View.GONE);
            if (result[0] == null) {
                Toast.makeText(SignIn.this, R.string.notmember, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SignIn.this, SignUp.class);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SignIn.this);
                paypals = sharedPreferences.getBoolean(getString(R.string.paypalsh), false);
                if (paypals) {
                    intent.putExtra(getString(R.string.price), price);
                }
                startActivityForResult(intent, REQUEST_SIGNUP);
                //finish();
            } else if (result[1] == null) {
                onLoginFailed();
            } else {
                onLoginSuccess();
            }


        }
    }

    class GoogleAsyncTask extends AsyncTask<String, Void, Users> {
        private UsersApi myApiService = null;
        Users retuser;
        long retid;

        @Override
        protected Users doInBackground(String... strings) {
            if (myApiService == null) {  // Only do this once
                UsersApi.Builder builder = new UsersApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                        // .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                        .setRootUrl(getString(R.string.appspotsignins))
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                // end options for devappserver

                myApiService = builder.build();
            }

            String email = strings[0];
            String pswrd = strings[1];
            String phno = strings[2];
           /* */
            try {
                //      User user = new User();
                // user.setEmail("lavanyabg@gmail.com");
                retuser = myApiService.checkUser(email).execute();

                if (retuser == null) {
                    Users users = new Users();
                    users.setEmail(email);
                    users.setPword(pswrd);
                    users.setPhno(phno);
                    Users returnuser = myApiService.insert(users).execute();
                }


            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return retuser;
        }

        @Override
        protected void onPostExecute(Users result) {
//            Toast.makeText(SignUp.this, returnuser.getEmail(), Toast.LENGTH_LONG).show();
            if (result == null) {
                Toast.makeText(SignIn.this, R.string.acntcreat, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(SignIn.this, R.string.loginsuc, Toast.LENGTH_LONG).show();
            }
            onLoginSuccess();

        }
    }

}
