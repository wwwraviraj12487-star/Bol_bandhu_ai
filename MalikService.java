package com.malik.finder;
import android.app.*;
import android.content.Intent;
import android.hardware.camera2.CameraManager;
import android.os.IBinder;
import android.speech.*;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class MalikService extends Service implements RecognitionListener {
    private SpeechRecognizer recognizer;
    private TextToSpeech tts;
    private String WAKE_WORD = "jai shree ram";
    private CameraManager camMan; private String camId;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null && intent.getStringExtra("WAKE")!=null) WAKE_WORD = intent.getStringExtra("WAKE").toLowerCase();

        Notification notif = new Notification.Builder(this, "malik").setContentTitle("Malik Finder On Hai").setContentText("Sun raha hu: "+WAKE_WORD).setSmallIcon(android.R.drawable.ic_btn_speak_now).build();
        NotificationManager nm = getSystemService(NotificationManager.class);
        nm.createNotificationChannel(new NotificationChannel("malik","Malik",NotificationManager.IMPORTANCE_LOW));
        startForeground(1, notif);

        tts = new TextToSpeech(this, s->{ tts.setLanguage(new Locale("hi","IN")); });
        camMan = getSystemService(CameraManager.class);
        try{ camId = camMan.getCameraIdList()[0]; }catch(Exception e){}

        startListening();
        return START_STICKY;
    }

    void startListening(){
        recognizer = SpeechRecognizer.createSpeechRecognizer(this);
        recognizer.setRecognitionListener(this);
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN");
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizer.startListening(i);
    }

    @Override public void onResults(Bundle results) {
        String heard = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0).toLowerCase();
        if(heard.contains(WAKE_WORD) || heard.contains("जय श्री राम")){
            tts.speak("Ham Yaha Hai Malik", TextToSpeech.QUEUE_FLUSH, null, null);
            new Thread(()->{
                try{ for(int k=0;k<10;k++){ camMan.setTorchMode(camId,true); Thread.sleep(300); camMan.setTorchMode(camId,false); Thread.sleep(300);} camMan.setTorchMode(camId,true); Thread.sleep(4000); camMan.setTorchMode(camId,false);}catch(Exception e){}
            }).start();
        }
        startListening(); // Fir se suno
    }
    @Override public void onError(int error){ startListening(); }
    @Override public IBinder onBind(Intent intent){ return null; }
    @Override public void onReadyForSpeech(Bundle params){}
    @Override public void onBeginningOfSpeech(){}
    @Override public void onRmsChanged(float rmsdB){}
    @Override public void onBufferReceived(byte[] buffer){}
    @Override public void onEndOfSpeech(){}
    @Override public void onPartialResults(Bundle partialResults){}
    @Override public void onEvent(int eventType, Bundle params){}
}
