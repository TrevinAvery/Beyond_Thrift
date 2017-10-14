package com.trevinavery.beyondthrift.app;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.trevinavery.beyondthrift.R;
import com.trevinavery.beyondthrift.model.Model;
import com.trevinavery.beyondthrift.model.Person;
import com.trevinavery.beyondthrift.proxy.Proxy;
import com.trevinavery.beyondthrift.request.IRequest;
import com.trevinavery.beyondthrift.request.LoginRequest;
import com.trevinavery.beyondthrift.request.RegisterRequest;
import com.trevinavery.beyondthrift.result.EventResult;
import com.trevinavery.beyondthrift.result.IResult;
//import com.trevinavery.beyondthrift.result.LoginResult;
import com.trevinavery.beyondthrift.result.PersonResult;
import com.trevinavery.beyondthrift.result.RegisterResult;

import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

/**
 * This fragment allows the user to register for a new account,
 * or login with an existing account. It handles the proxy connection
 * and loads any data received to the model.
 */
public class LoginFragment extends Fragment {
    private String BEYOND_THRIFT_DB = "beyondThriftDb";

    private OnLoginListener onLoginListener;
    CallbackManager callbackManager;
//    private boolean runningTask;
//
//    private EditText serverHost;
//    private EditText serverPort;
//    private EditText username;
//    private EditText password;
//    private EditText firstName;
//    private EditText lastName;
//    private EditText email;
//
//    private RadioButton male;

    private Button signUp;
    private Button login;
    private Button facebook;
    private Button google;
    private LoginButton loginButton;
    private TextView info;

    public LoginFragment() {
        // Required default constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        callbackManager = CallbackManager.Factory.create();



        loginButton = (LoginButton) view.findViewById(R.id.facebookLoginButton);
        loginButton.setReadPermissions("email");
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        info = (TextView)view.findViewById(R.id.info);
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                // App code


                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code

                                try{
                                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(BEYOND_THRIFT_DB, MODE_PRIVATE).edit();
                                    editor.putString("user_email", object.getString("email"));
                                    editor.commit();
                                }
                                catch(Exception JSONException ){
                                    Log.v("facebook error","error getting facebook email");
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,email");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {

                info.setText("Login attempt canceled.");            }

            @Override
            public void onError(FacebookException exception) {
                info.setText(exception.getMessage());            }
        });
//        runningTask = false;
//
//        serverHost = (EditText) view.findViewById(R.id.serverHostEditText);
//        serverPort = (EditText) view.findViewById(R.id.serverPortEditText);
//        username = (EditText) view.findViewById(R.id.usernameEditText);
//        password = (EditText) view.findViewById(R.id.passwordEditText);
//        firstName = (EditText) view.findViewById(R.id.firstNameEditText);
//        lastName = (EditText) view.findViewById(R.id.lastNameEditText);
//        email = (EditText) view.findViewById(R.id.emailEditText);
//
//        male = (RadioButton) view.findViewById(R.id.maleRadioButton);

        signUp = (Button) view.findViewById(R.id.signUpButton);
        login = (Button) view.findViewById(R.id.loginButton);
        //facebook = (Button) view.findViewById(R.id.facebookLoginButton);
        google = (Button) view.findViewById(R.id.googleLoginButton);

        // create a text watcher to enable/disable the buttons
        // as the user types
//        TextWatcher textWatcher = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                updateButtons();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        };
//
//        serverHost.addTextChangedListener(textWatcher);
//        serverPort.addTextChangedListener(textWatcher);
//        username.addTextChangedListener(textWatcher);
//        password.addTextChangedListener(textWatcher);
//        firstName.addTextChangedListener(textWatcher);
//        lastName.addTextChangedListener(textWatcher);
//        email.addTextChangedListener(textWatcher);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLoginListener != null) {
                    onLoginListener.onLogin(OnLoginListener.LoginType.SIGN_UP);
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLoginListener != null) {
                    onLoginListener.onLogin(OnLoginListener.LoginType.NORMAL);
                }
            }
        });

/*        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLoginListener != null) {
                    onLoginListener.onLogin(OnLoginListener.LoginType.FACEBOOK);
                }
            }
        });*/

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLoginListener != null) {
                    onLoginListener.onLogin(OnLoginListener.LoginType.GOOGLE);
                }
            }
        });

//        updateButtons();

        return view;
    }

//    private void updateButtons() {
//
//        // only change state of buttons if no task is running
//        if (!runningTask) {
//
//            // check host, port, username, and password
//            if (serverHost.getText().length() > 0
//                    && serverPort.getText().length() > 0
//                    && username.getText().length() > 0
//                    && password.getText().length() > 0) {
//
//                // enable sign in button
//                signIn.setEnabled(true);
//
//                // check firstName, lastName, and email
//                if (firstName.getText().length() > 0
//                        && lastName.getText().length() > 0
//                        && email.getText().length() > 0) {
//
//                    // enable register button
//                    register.setEnabled(true);
//
//                } else {
//                    // disable register button
//                    register.setEnabled(false);
//                }
//
//            } else {
//                // disable both buttons
//                signIn.setEnabled(false);
//                register.setEnabled(false);
//            }
//        }
//    }
//
//    private abstract class ProxyTask<Request extends IRequest, Result extends IResult>
//            extends AsyncTask<Void, Void, String> {
//
//        protected Proxy proxy;
//        protected Request request;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            // set runningTask true to prevent other tasks starting
//            // and running concurrently
//            runningTask = true;
//
//            // disable buttons so they can't be pressed again
//            register.setEnabled(false);
//            signIn.setEnabled(false);
//
//            proxy = new Proxy(
//                    serverHost.getText().toString(),
//                    serverPort.getText().toString()
//            );
//
//            // save proxy for re-sync
//            Model.setProxy(proxy);
//        }
//
//        @Override
//        protected void onPostExecute(String message) {
//            super.onPostExecute(message);
//
//            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
//
//            runningTask = false;
//
//            updateButtons();
//
//            if (Model.getAuthToken() != null) {
//                onLoginListener.onLogin();
//            }
//        }
//    }
//
//    private class RegisterTask extends ProxyTask<RegisterRequest, RegisterResult> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            String gender;
//            if (male.isChecked()) {
//                gender = "m";
//            } else {
//                gender = "f";
//            }
//
//            request = new RegisterRequest(
//                    username.getText().toString(),
//                    password.getText().toString(),
//                    email.getText().toString(),
//                    firstName.getText().toString(),
//                    lastName.getText().toString(),
//                    gender
//            );
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//
//            RegisterResult result = proxy.register(request);
//
//            String message;
//
//            if (result == null) {
//                message = getString(R.string.connection_failed);
//
//            } else if (result.getMessage() == null) {
//                // success
//
//                // save result to model
//                Model.login(
//                        result.getAuthToken(),
//                        result.getUserName(),
//                        result.getPersonID()
//                );
//
//                loadUserData(proxy);
//
//                Person user = Model.getPerson(result.getPersonID());
//
//                message = user.getLocation() + " " + user.getStart();
//            } else {
//                message = result.getMessage();
//            }
//
//            return message;
//        }
//    }
//
//    private class LoginTask extends ProxyTask<LoginRequest, LoginResult> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            request = new LoginRequest(
//                    username.getText().toString(),
//                    password.getText().toString()
//            );
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//
//            LoginResult result = proxy.login(request);
//
//            String message;
//
//            if (result == null) {
//                message = getString(R.string.connection_failed);
//
//            } else if (result.getMessage() == null) {
//                // success
//
//                // save result to model
//                Model.login(
//                        result.getAuthToken(),
//                        result.getUserName(),
//                        result.getPersonID()
//                );
//
//                loadUserData(proxy);
//
//                Person user = Model.getPerson(result.getPersonID());
//
//                message = user.getLocation() + " " + user.getStart();
//            } else {
//                message = result.getMessage();
//            }
//
//            return message;
//        }
//    }
//
//    private void loadUserData(Proxy proxy) {
//        PersonResult personResult = proxy.getPersons(Model.getAuthToken());
//        EventResult eventResult = proxy.getEvents(Model.getAuthToken());
//
//        Model.load(personResult.getData(), eventResult.getData());
//    }

    public void setOnLoginListener(OnLoginListener onLoginListener) {
        this.onLoginListener = onLoginListener;
    }

    public interface OnLoginListener {
        enum LoginType {SIGN_UP, NORMAL, FACEBOOK, GOOGLE}

        public void onLogin(LoginType loginType);
    }
}
