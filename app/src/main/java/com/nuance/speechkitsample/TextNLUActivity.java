package com.nuance.speechkitsample;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nuance.speechkit.Interpretation;
import com.nuance.speechkit.Language;
import com.nuance.speechkit.Session;
import com.nuance.speechkit.Transaction;
import com.nuance.speechkit.TransactionException;
import com.nuance.speechkitsample.response.NLUresponse;

import org.json.JSONException;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This Activity is built to demonstrate how to perform NLU (Natural Language Understanding) with
 * text input instead of voice.
 *
 * NLU is the transformation of text into meaning.
 *
 * When performing speech recognition with SpeechKit, you have a variety of options. Here we demonstrate
 * Context Tag and Language.
 *
 * The Context Tag is assigned in the system configuration upon deployment of an NLU model.
 * Combined with the App ID, it will be used to find the correct NLU version to query.
 *
 * Languages can be configured. Supported languages can be found here:
 * http://developer.nuance.com/public/index.php?task=supportedLanguages
 *
 * Copyright (c) 2015 Nuance Communications. All rights reserved.
 */
public class TextNLUActivity extends DetailActivity implements View.OnClickListener {

    final static String TAG = TextNLUActivity.class.getSimpleName();

    private EditText textInput;
    private EditText nluContextTag;
    private EditText language;

    private TextView logs;
    private Button clearLogs;

    private Button toggleReco;

    private Session speechSession;
    private State state = State.IDLE;

    List<String> inputPhrases;
    List<String> testResults;
    int currentPosition = 0;

    final String DIR_SD  = "NuanceTesting";
    final String FILENAME_SD  = "NuanceTestResults.txt";

    ObjectMapper MAPPER = new ObjectMapper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_nlu);

        textInput = (EditText) findViewById(R.id.text_input);
        nluContextTag = (EditText)findViewById(R.id.nlu_context_tag);
        nluContextTag.setText(Configuration.CONTEXT_TAG);
        language = (EditText)findViewById(R.id.language);

        logs = (TextView)findViewById(R.id.logs);
        clearLogs = (Button)findViewById(R.id.clear_logs);
        clearLogs.setOnClickListener(this);

        toggleReco = (Button)findViewById(R.id.toggle_reco);
        toggleReco.setOnClickListener(this);

        //Create a session
        speechSession = Session.Factory.session(this, Configuration.SERVER_URI, Configuration.APP_KEY);

        AssetManager am = this.getAssets();
        InputStream is = null;
        try {
            is = am.open("inputPhrases");

            inputPhrases = readStringList(is);
            testResults = new ArrayList<>();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setState(State.IDLE);
    }

    private List<String> readStringList(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        List<String> results = new ArrayList<>();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                results.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    void writeFileSD(List<String> results) {
        if (results == null) {
            return;
        }

        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(TAG, "SD storage are not available: " + Environment.getExternalStorageState());
            return;
        }

        File sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        sdPath.mkdirs();

        File sdFile = new File(sdPath, FILENAME_SD);
        try {

            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            for (String string : results ) {
                bw.write(string+"\n");
            }
            bw.close();
            Log.d(TAG, "File saved to SD card: " + sdFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == clearLogs) {

            logs.setText("");

        } else if(v == toggleReco) {

            if (inputPhrases != null &&
                    currentPosition < inputPhrases.size()) {

                toggleReco();

            } else {

                logs.append("\nEND_TEST\n");
                Log.d(TAG, "Test completed: TOTAL "+inputPhrases.size()+ " RESULTS: "+testResults.size());
                if (inputPhrases!=null & testResults!=null) {
                    writeFileSD(testResults);
                    currentPosition = 0;
                }

            }
        }
    }

    /* Reco transactions */

    private void toggleReco() {
        switch (state) {
            case IDLE:
                recognize();
                break;
            case PROCESSING:
                break;
        }
    }

    /**
     * Send user's text query to the server
     */
    private void recognize() {

        String input = inputPhrases.get(currentPosition);

        if (input.length() > 0) {
            //Setup our Reco transaction options.
            Transaction.Options options = new Transaction.Options();
            options.setLanguage(new Language(language.getText().toString()));

            //Add properties to appServerData for use with custom service. Leave empty for use with NLU.
            JSONObject appServerData = new JSONObject();
            try {
                appServerData.put("message", input);

                speechSession.transactionWithService(nluContextTag.getText().toString(), appServerData, options, recoListener);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            setState(State.PROCESSING);
        }
        else {
            logs.append("\n" + getResources().getString(R.string.text_input_missing));
        }
    }

    private Transaction.Listener recoListener = new Transaction.Listener() {
        @Override
        public void onServiceResponse(Transaction transaction, JSONObject response) {
            try {
                // 2 spaces for tabulations.
                logs.append("\nonServiceResponse: " + response.toString(2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onInterpretation(Transaction transaction, Interpretation interpretation) {
            try {
                if (interpretation == null) {
                    return;
                }
                logs.append("\nonInterpretation: " + currentPosition);
                JSONObject jsonObject = interpretation.getResult().getJSONArray("interpretations").getJSONObject(0);
                if (interpretation.getResult().getJSONArray("interpretations").length() > 1)
                {
                    Log.d(TAG, "SEVERAL interpretations!");
                }
                Log.d(TAG, jsonObject.toString());

                try {
                    if (jsonObject == null) {
                        return;
                    }
                    NLUresponse response = MAPPER.readValue(jsonObject.toString(), NLUresponse.class);
                    if (response != null) {
                        String result = response.getLiteral() + "\t" + response.getAction().getIntent().getValue() + "\t" + response.getAction().getIntent().getConfidence();
                        if (response.getConcepts() != null && response.getConcepts().size() > 0) {
                            result += "\t";
                            for (String concept : response.getConcepts().keySet()) {
                                result += concept+", ";
                            }
                            if (result.contains(",")) {
                                result = result.substring(0, result.lastIndexOf(','));
                            }
                        }
                        testResults.add(result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSuccess(Transaction transaction, String s) {
            logs.append("\nonSuccess");

            //Notification of a successful transaction. Nothing to do here.
            setState(State.IDLE);
            currentPosition++;
            toggleReco.performClick();
        }

        @Override
        public void onError(Transaction transaction, String s, TransactionException e) {
            logs.append("\nonError: " + e.getMessage() + s==null?"":(". " + s));

            //Something went wrong. Check Configuration.java to ensure that your settings are correct.
            //The user could also be offline, so be sure to handle this case appropriately.
            setState(State.IDLE);
        }
    };

    /* State Logic: IDLE -> PROCESSING -> repeat */

    private enum State {
        IDLE,
        PROCESSING
    }

    /**
     * Set the state and update the button text.
     */
    private void setState(State newState) {
        state = newState;
        switch (newState) {
            case IDLE:
                toggleReco.setText(getResources().getString(R.string.transaction_with_service));
                break;
            case PROCESSING:
                toggleReco.setText(getResources().getString(R.string.processing));
                break;
        }
    }
}
