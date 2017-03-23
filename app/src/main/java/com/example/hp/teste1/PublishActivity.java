package com.example.hp.teste1;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.TouchTypeDetector;

import java.util.ArrayList;
import java.util.Locale;

public class PublishActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private String mensagemUsuario= "Esta mensagem é um teste, se funcionar, estarei feliz pra poha !";
    TextToSpeech textoParaFalarPublicacao;
    private static final int REQUEST_CODE = 1234;
    private ListView wordsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Sensey.getInstance().init(this);
        Sensey.getInstance().startTouchTypeDetection(this,touchTypListener);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        textoParaFalarPublicacao = new TextToSpeech(this,this);

        FacebookSdk.sdkInitialize(this);

        Bundle params = new Bundle();
        params.putString("message", mensagemUsuario);
        /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                params,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                    }
                }
        ).executeAsync();

    }


    public void TextToSpeechFunction(String textholder) {


        textoParaFalarPublicacao.speak(textholder, TextToSpeech.QUEUE_FLUSH, null);

    }

    @Override
    public void onDestroy() {

        textoParaFalarPublicacao.shutdown();

        super.onDestroy();
    }

    @Override
    public void onInit(int Text2SpeechCurrentStatus) {

        if (Text2SpeechCurrentStatus == TextToSpeech.SUCCESS) {

            textoParaFalarPublicacao.setLanguage(new Locale("pt", "BR"));

            TextToSpeechFunction("Esta é a tela de publicação. Para publicar, dê um duplo toque" +
                    "na tela para que você possa falar o que deseja publicar no facebook");
        }

    }

    @Override public boolean dispatchTouchEvent(MotionEvent event) {
        // Setup onTouchEvent for detecting type of touch gesture
        Sensey.getInstance().setupDispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    TouchTypeDetector.TouchTypListener touchTypListener=new TouchTypeDetector.TouchTypListener() {
        @Override public void onTwoFingerSingleTap() {
            // Two fingers single tap
        }

        @Override public void onThreeFingerSingleTap() {
            // Three fingers single tap
        }

        @Override public void onDoubleTap() {
            // Double tap
            startVoiceRecognitionActivity();
        }

        @Override public void onScroll(int scrollDirection) {
            switch (scrollDirection) {
                case TouchTypeDetector.SCROLL_DIR_UP:
                    // Scrolling Up
                    break;
                case TouchTypeDetector.SCROLL_DIR_DOWN:
                    // Scrolling Down
                    break;
                case TouchTypeDetector.SCROLL_DIR_LEFT:
                    // Scrolling Left
                    break;
                case TouchTypeDetector.SCROLL_DIR_RIGHT:
                    // Scrolling Right
                    break;
                default:
                    // Do nothing
                    break;
            }
        }

        @Override public void onSingleTap() {
            // Single tap
        }

        @Override public void onSwipe(int swipeDirection) {
            switch (swipeDirection) {
                case TouchTypeDetector.SWIPE_DIR_UP:
                    // Swipe Up
                    break;
                case TouchTypeDetector.SWIPE_DIR_DOWN:
                    // Swipe Down
                    break;
                case TouchTypeDetector.SWIPE_DIR_LEFT:
                    // Swipe Left
                    break;
                case TouchTypeDetector.SWIPE_DIR_RIGHT:
                    // Swipe Right
                    break;
                default:
                    //do nothing
                    break;
            }
        }

        @Override public void onLongPress() {
            // Long press
        }
    };

    private void startVoiceRecognitionActivity()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Demonstração de reconhecimento de voz...");
        startActivityForResult(intent, REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            // Populate the wordsList with the String values the recognition engine thought it heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            wordsList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    matches));

            mensagemUsuario = matches.get(1);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
