package es.upv.master.pruebatwiter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;

//import static android.icu.text.RelativeDateTimeFormatter.Direction.THIS;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "	1upGedXidBGuacfhfvUOMRldz";
    private static final String TWITTER_SECRET = "	9U6Ze7AZM6oNYfDxGcs1Ckga83MFuySiPQ5TJ41kuiGrZf1vKy";
    TwitterLoginButton btnTwiter;
    private final Activity THIS = this;
    private TextView txtShare ;
    private TwitterSession myTwitterSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        txtShare = (TextView) findViewById(R.id.edtTweet);
        btnTwiter = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        btnTwiter.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Toast.makeText(THIS, "Autenticado en twitter: " + result.data.getUserName(), Toast.LENGTH_LONG).show();
                myTwitterSession = result.data;
            }

            @Override
            public void failure(TwitterException e) {
                Toast.makeText(THIS, "Fallo en autentificación: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        btnTwiter.onActivityResult(requestCode, resultCode, data);
    }


    public void call_compartir(View quien) {
        StatusesService statusesService = Twitter.getApiClient(myTwitterSession).getStatusesService();
        Call<Tweet> call = statusesService.update(txtShare.getText().toString(), null, null, null, null, null, null, null, null);
        call.enqueue(new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                Toast.makeText(THIS, "Tweet publicado: "+ result.response.message(), Toast.LENGTH_LONG).show();}

            @Override
                public void failure(TwitterException e) {
                    Toast.makeText(THIS, "No se pudo publicar el tweet: "+ e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
    } // ()



}
