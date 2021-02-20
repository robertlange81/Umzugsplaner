package com.planner.generic.virus.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.planner.generic.virus.R;

public class IntroFragmentIncidence extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView webview = (WebView) inflater.inflate(R.layout.fragment_intro_incidence, container, false);
        String src = getString(R.string.srcIncidence);
        String customHtml = "<html><head></head><body><iframe src=\"" + src +  "\" frameborder=\"0\" style=\"width:0;min-width:100%;border:none\" height=\"690px\"></iframe></body></html>";
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadData(customHtml, "text/html", "UTF-8");
        return webview;
    }
}