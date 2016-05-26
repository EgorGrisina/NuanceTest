package com.nuance.speechkitsample;

import android.net.Uri;

import com.nuance.speechkit.PcmFormat;

/**
 * All Nuance Developers configuration parameters can be set here.
 *
 * Copyright (c) 2015 Nuance Communications. All rights reserved.
 */
public class Configuration {

    //All fields are required.
    //Your credentials can be found in your Nuance Developers portal, under "Manage My Apps".
    public static final String APP_KEY = "c6f7955f6c311040ac0f26cd374d317f46223cc68a8c3b77cbf3fb319ee28b64d921c44652d486fee3accb1af9fd993f1fd506a96241212d341c5ed2feea37d7";
    public static final String APP_ID = "NMDPTRIAL_w_lin_motorolasolutions_com20150707114019";
    public static final String SERVER_HOST = "nmsps.dev.nuance.com";
    public static final String SERVER_PORT = "443";

    public static final Uri SERVER_URI = Uri.parse("nmsps://" + APP_ID + "@" + SERVER_HOST + ":" + SERVER_PORT);

    //Only needed if using NLU
    public static final String CONTEXT_TAG = "M2600_A1265_V1";

    public static final PcmFormat PCM_FORMAT = new PcmFormat(PcmFormat.SampleFormat.SignedLinear16, 16000, 1);
}

