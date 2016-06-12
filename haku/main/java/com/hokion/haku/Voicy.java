package com.hokion.haku;

import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by NGo on 2016/04/07.
 * Voice handling
 * Based on http://www.androidhive.info/2014/07/android-speech-to-text-tutorial/
 *
 * 4/23: Decide to stop with voice for the moment
 */

//public class Voicy {
//    final int SPEECH_CODE = 101;
//    private Service context;

//    public Voicy (Context context) {
//        this.context = (Service) context;
//    }

    /**
     * Showing google speech input dialog
     * */
//    protected void promptSpeechInput() {
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "<Temp> Speak now");
//        try {
//           context.startActivityForResult(intent, SPEECH_CODE);
//        } catch (ActivityNotFoundException a) {
//            Toast.makeText(this, "Speech prompt error", Toast.LENGTH_SHORT).show();
//        }
//    }

    /**
     * Receiving speech input
     * */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

//        switch (requestCode) {
//            case SPEECH_CODE: {
//                if (resultCode == RESULT_OK && null != data) {

//                    ArrayList<String> result = data
//                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    Toast.makeText(this, result.get(0), Toast.LENGTH_SHORT).show();
//                }
//                break;
//            }
//        }
//    }
//}