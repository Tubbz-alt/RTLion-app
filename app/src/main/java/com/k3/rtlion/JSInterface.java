package com.k3.rtlion;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class JSInterface {

    private Activity activity;
    private Context context;
    private WebView webView;
    private String jsInterfaceName;
    private enum JSCommands {
        ServerInfo("serverInfo", "fetchServerInfo", "getClientInfo");
        private String urlFrag, serverCmd, clientCmd;
        JSCommands(String urlFrag, String serverCmd, String clientCmd) {
            this.urlFrag = urlFrag;
            this.serverCmd = serverCmd;
            this.clientCmd = clientCmd;
        }
        public String getUrlFrag() {
            return urlFrag;
        }
        public String getServerCmd() {
            return serverCmd;
        }
        public String getClientCmd() {
            return clientCmd;
        }
    }

    public JSInterface(Activity activity){
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }
    public void initialize(WebView webView){
        this.webView = webView;
        webView.getSettings().setJavaScriptEnabled(true);
        jsInterfaceName = this.getClass().getSimpleName();
        webView.addJavascriptInterface(this, jsInterfaceName);
        webView.setWebViewClient(new webView_client());
    }
    private class webView_client extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            String js_getServerInfo = "javascript:JSInterface.fetchServerInfo(getClientInfo());";
            webView.loadUrl(js_getServerInfo);
        }
    }
    public void getServerInfo(String url){
        webView.loadUrl(url + "#serverInfo");
    }
    private String createJSCommand(int index, Object[] params){
        StringBuilder jsCommand = new StringBuilder();
        String jsHead = "javascript:",
                serverCmd = JSCommands.values()[index].getServerCmd(),
                clientCmd = JSCommands.values()[index].getClientCmd();
        jsCommand.append(jsHead);
        jsCommand.append(jsInterfaceName);
        jsCommand.append(".");
        jsCommand.append(serverCmd);
        jsCommand.append("(");
        jsCommand.append(clientCmd);
        jsCommand.append("(");
        if (params != null){
            for (int i = 0; i < params.length; i++){
                jsCommand.append(String.valueOf(params[i]));
                if (i != params.length - 1)
                    jsCommand.append(",");
            }
        }
        jsCommand.append("));");
        return jsCommand.toString();
    }
    @JavascriptInterface
    public void fetchServerInfo(String info){
        Toast.makeText(activity, info, Toast.LENGTH_SHORT).show();
    }
}