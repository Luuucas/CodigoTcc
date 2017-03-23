package com.example.hp.teste1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.TouchTypeDetector;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    CallbackManager callbackManager;
    TextToSpeech textoParaFalar;
    public static final String pref_key = "Preferencia";
    private SharedPreferences.OnSharedPreferenceChangeListener callback;
    private static final String TAG = "TesteTCC";
    private boolean resultBoolean;
    SharedPreferences preferencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //importante que o onCreate esteja nesta ordem
        Sensey.getInstance().init(this);
        Sensey.getInstance().startTouchTypeDetection(this,touchTypListener);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(this);
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
       // loginButton.setReadPermissions("publish_actions");
        loginButton.setPublishPermissions("publish_actions");
        callbackManager= CallbackManager.Factory.create();

        preferencia = getSharedPreferences((pref_key),Context.MODE_PRIVATE);


        textoParaFalar = new TextToSpeech(this,this);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,MainActivity.class);

                startActivity(intent);

                finish();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void TextToSpeechFunction(String textholder) {
        // Método para transformar texto em audio

        textoParaFalar.speak(textholder, TextToSpeech.QUEUE_FLUSH, null);

    }

    @Override
    public void onDestroy() {

        textoParaFalar.shutdown();

        super.onDestroy();
    }


    @Override
    public void onInit(int Text2SpeechCurrentStatus) {
        // método para setar a frase ou o texto que você queira que o app emita em forma de som
        if (Text2SpeechCurrentStatus == TextToSpeech.SUCCESS) {

            textoParaFalar.setLanguage(new Locale("pt", "BR"));

            if(resultBoolean == false){
            TextToSpeechFunction("Olá! Seja bem vindo ao Boo!" +
                    "Eu serei seu guia durante toda sua experiência no Facebook," +
                    "Para que possa fazer login, pessa a alguém de sua confiança para fazê-lo," +
                    "Prometo que esta será a única vez que precisará da ajuda de alguém!" +
                    "Se não deseja ouvir esta apresentação nos próximos acessos, dê um duplo" +
                    "toque na tela do seu celular.");
            }
            else{
                TextToSpeechFunction("Olá! Seja bem vindo ao Boo!");
            }

        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // Setup onTouchEvent for detecting type of touch gesture
        Sensey.getInstance().setupDispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    TouchTypeDetector.TouchTypListener touchTypListener = new TouchTypeDetector.TouchTypListener() {
        // toda detecção de gestos na tela serão feitas neste método
        @Override
        public void onTwoFingerSingleTap() {

        }

        @Override
        public void onThreeFingerSingleTap() {

        }

        @Override
        public void onDoubleTap() {
            SharedPreferences.Editor editorPreferencia = preferencia.edit();
            editorPreferencia.putBoolean("chave1", true);
            editorPreferencia.commit();
            resultBoolean= preferencia.getBoolean("chave1",false);
            TextToSpeechFunction("Preferência salva !");
            Log.d(TAG,"DUPLO TOQUE OK!");
        }

        @Override
        public void onScroll(int direcaoScoll) {

        }

        @Override
        public void onSingleTap() {

        }

        @Override
        public void onSwipe(int swipeDirection) {
            switch (swipeDirection) {
                case TouchTypeDetector.SWIPE_DIR_UP:
                    // Swipe Up
                    break;
                case TouchTypeDetector.SWIPE_DIR_DOWN:
                    // Swipe Down
        break;
        default:
                //do nothing
                break;
    }
}

        @Override
        public void onLongPress() {

        }
    };
}
