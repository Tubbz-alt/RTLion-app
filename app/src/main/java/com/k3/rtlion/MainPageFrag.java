package com.k3.rtlion;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainPageFrag {

    private Activity activity;
    private Context context;
    private ViewGroup viewGroup;
    private RelativeLayout rlMainFrag;
    private TextView txvServerStatus;
    private TextInputLayout tilHostAddr;
    private EditText edtxHostAddr;
    private Button btnConnect;
    private String serverHost,
            statusNamespace = "/status";
    private int portNum;

    public MainPageFrag(Activity activity, ViewGroup viewGroup){
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.viewGroup = viewGroup;
    }

    private void initViews(){
        rlMainFrag = (RelativeLayout) viewGroup.findViewById(R.id.rlMainFrag);
        txvServerStatus = (TextView) viewGroup.findViewById(R.id.txvServerStatus);
        tilHostAddr = (TextInputLayout) viewGroup.findViewById(R.id.tilHostAddr);
        edtxHostAddr = (EditText) viewGroup.findViewById(R.id.edtxHostAddr);
        btnConnect = (Button) viewGroup.findViewById(R.id.btnConnect);
    }
    public void initialize(){
        initViews();
        edtxHostAddr.setOnEditorActionListener(new edtxHostAddr_onEditorAction());
        btnConnect.setOnClickListener(new btnConnect_onClick());
    }
    private class edtxHostAddr_onEditorAction implements TextView.OnEditorActionListener{
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_SEND) {
                tryConnect();
                return true;
            }
            return false;
        }
    }
    private class btnConnect_onClick implements Button.OnClickListener{
        @Override
        public void onClick(View v) {
            tryConnect();
        }
    }
    private void hideKeyboard(){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    context.getSystemService(context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(rlMainFrag.getWindowToken(), 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private Boolean checkHostAddr(String hostAddr){
        Boolean validAddr = false;
        try{
            if (!hostAddr.isEmpty() && hostAddr.contains(":")) {
                portNum = Integer.parseInt(hostAddr.split(":")[hostAddr.split(":").length-1]);
                serverHost = hostAddr.replace(":"+String.valueOf(portNum), "");
                if (serverHost.length() > 4 && portNum > 0)
                    validAddr = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return validAddr;
    }
    private void enableViews(Boolean state){
        btnConnect.setEnabled(state);
        edtxHostAddr.setEnabled(state);
    }
    private void tryConnect(){
        if(checkHostAddr(edtxHostAddr.getText().toString())){
            hideKeyboard();
            enableViews(false);
            txvServerStatus.setText(context.getString(R.string.server_connecting));
            txvServerStatus.setTextColor(ResourcesCompat.getColor(context.getResources(),
                    R.color.colorGray2, null));
            new FetchAsyncTask(new ServerResponse()).execute(edtxHostAddr.getText().toString()
                    + statusNamespace);
        }else{
            Toast.makeText(activity, context.getString(R.string.invalid_host),
                    Toast.LENGTH_SHORT).show();
        }
    }
    private class ServerResponse implements FetchAsyncTask.AsyncResponse{
        @Override
        public void onFetch(int statusCode, String source) {
            if(statusCode == 200){
                Toast.makeText(activity, source, Toast.LENGTH_SHORT).show();
                txvServerStatus.setText(context.getString(R.string.server_connected));
            }else{
                txvServerStatus.setText(context.getString(R.string.server_disconnected));
            }
            txvServerStatus.setTextColor(ResourcesCompat.getColor(context.getResources(),
                    R.color.colorGray1, null));
            enableViews(true);
        }
    }
}
