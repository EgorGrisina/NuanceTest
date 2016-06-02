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
    public static final String APP_KEY = "6793eaeb6bcc8103065cbca73d880ffe9e17bbef549abce1a2d0bb368d488ef4dc78df4744604a4abd06f1fa8e01f82d18b41c6c4ed94df0a908263651b42b2d";
    public static final String APP_ID = "HTTP_NMDPPRODUCTION_Wei_Lin_VIP_20150708101837";
    public static final String SERVER_HOST = "nmsps.dev.nuance.com";
    public static final String SERVER_PORT = "443";

    public static final Uri SERVER_URI = Uri.parse("nmsps://" + APP_ID + "@" + SERVER_HOST + ":" + SERVER_PORT);

    //Only needed if using NLU
    public static final String CONTEXT_TAG = "M2724_A1266_V1";

    public static final PcmFormat PCM_FORMAT = new PcmFormat(PcmFormat.SampleFormat.SignedLinear16, 16000, 1);
}

