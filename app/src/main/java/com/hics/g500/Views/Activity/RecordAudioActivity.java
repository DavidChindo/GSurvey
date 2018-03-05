package com.hics.g500.Views.Activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hics.g500.Dal.Dal;
import com.hics.g500.Library.DesignUtils;
import com.hics.g500.Library.LogicUtils;
import com.hics.g500.Library.Statics;
import com.hics.g500.R;
import com.hics.g500.db.Gasolineras;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;

public class RecordAudioActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO = 0;
    private static final String AUDIO_FILE_PATH =
            Environment.getExternalStorageDirectory().getPath() +"/"+ Statics.NAME_FOLDER;
    String name;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_audio);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(
                    new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimaryDark)));
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            name = bundle.getString("name","");
        }

        url = "";
        LogicUtils.requestPermission(this, Manifest.permission.RECORD_AUDIO);
        LogicUtils.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        recordAudio();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RECORD_AUDIO) {
            if (resultCode == RESULT_OK) {
                Gasolineras gasolineras = Dal.gasolineraById(Long.valueOf(name));
                if (gasolineras != null){
                    gasolineras.setAudio(url);
                    Dal.updateGasolinera(gasolineras);
                }
                Toast.makeText(this, "Grabación exitosa", Toast.LENGTH_SHORT).show();
                finish();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "No se pudo guardar la grabación", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void recordAudio() {
        if (name != null && !name.isEmpty()) {
            url = AUDIO_FILE_PATH+"/"+name+".wav";
            AndroidAudioRecorder.with(this)
                    // Required
                    .setFilePath(url)
                    .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                    .setRequestCode(REQUEST_RECORD_AUDIO)

                    // Optionalx
                    .setSource(AudioSource.MIC)
                    .setChannel(AudioChannel.STEREO)
                    .setSampleRate(AudioSampleRate.HZ_32000)
                    .setAutoStart(false)
                    .setKeepDisplayOn(true)

                    // Start recording
                    .record();
        } else {
            DesignUtils.showToast(this, "No se puede grabar");
            finish();
        }
    }
}