package com.example.hp.teste1;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.github.nisrulz.sensey.OrientationDetector;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.TouchTypeDetector;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    TextToSpeech textoParaFalar;
    private static final String TAG = "TesteTCC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Sensey.getInstance().init(this);
        Sensey.getInstance().startTouchTypeDetection(this,touchTypListener);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textoParaFalar = new TextToSpeech(this,this);
    }

    public void TextToSpeechFunction(String textholder) {


        textoParaFalar.speak(textholder, TextToSpeech.QUEUE_FLUSH, null);

    }

    @Override
    public void onDestroy() {

        textoParaFalar.shutdown();

        super.onDestroy();
    }

    @Override
    public void onInit(int Text2SpeechCurrentStatus) {

        if (Text2SpeechCurrentStatus == TextToSpeech.SUCCESS) {

            textoParaFalar.setLanguage(new Locale("pt", "BR"));

            TextToSpeechFunction("Bem vindo ao Facebook ! Escolha uma das opções," +
                    "para fazer publicação deslize o dedo para cima," +
                    "para escutar o feed de notícias deslize o dedo para baixo");
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
            TextToSpeechFunction("Confirmar Ação");
            Log.d(TAG,"DUPLO TOQUE OK");
        }

        @Override
        public void onScroll(int direcaoScoll) {
//            switch (direcaoScoll) {
//                case TouchTypeDetector.SCROLL_DIR_UP: {
//                    TextToSpeechFunction("Fazer Publicação");
//                    Log.d(TAG,"SCROLL CIMA OK");
//                }
//                case TouchTypeDetector.SCROLL_DIR_DOWN: {
//                    TextToSpeechFunction("Ir para Feed de Notícias");
//                    Log.d(TAG,"SCROLL BAIXO OK");
//                }
//                default:
//                    // nada a fazer
//                    break;
//            }

        }

        @Override
        public void onSingleTap() {

        }

        @Override
        public void onSwipe(int swipeDirection) {
            switch (swipeDirection) {
                case TouchTypeDetector.SWIPE_DIR_UP:
                    // Swipe Up
                    TextToSpeechFunction("Fazer Publicação");
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,PublishActivity.class);

                    startActivity(intent);

                    finish();
                    break;
                case TouchTypeDetector.SWIPE_DIR_DOWN:
                    // Swipe Down
                    TextToSpeechFunction("Ir para Feed de Notícias");
                    Log.d(TAG,"SCROLL BAIXO OK");
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
