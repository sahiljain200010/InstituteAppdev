package com.example.anant.iitbhuvaranasi;

import org.json.JSONObject;

public interface ServerCallback {
    void onSuccess();
    void onSuccess(JSONObject jsonResponse);
}