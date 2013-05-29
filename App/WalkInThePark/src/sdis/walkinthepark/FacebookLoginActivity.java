package sdis.walkinthepark;

import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//	super.onCreate(savedInstanceState);
//	setContentView(R.layout.facebook_activity_login);
//    }
//
//    public void login(View v) {
//	System.out.println("Hello World!");
//
//	EditText fbusername = (EditText) findViewById(R.id.facebook_username);
//	EditText fbpassword = (EditText) findViewById(R.id.facebook_password);
//
//	if (fbusername.getText().toString().trim().equals("") || fbpassword.getText().toString().trim().equals("")) {
//	    System.out.println("Not filled!");
//
//	    Toast.makeText(getApplicationContext(), "Please fill all the fields.", Toast.LENGTH_SHORT).show();
//	} else {
//	    startActivity(new Intent(this,MainActivity.class));
//	}
//    }

public class FacebookLoginActivity extends Activity {
    private String accessToken = null;
    private String realName = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	// start Facebook Login
	Session.openActiveSession(this, true, new Session.StatusCallback() {

	    // callback when session changes state
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
		if (session.isOpened()) {
		    // make request to the /me API
		    Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

			// callback after Graph API response with user object
			@Override
			public void onCompleted(GraphUser user, Response response) {
			    if (user != null) {
				/* do something you want! */
			    }
			}
		    });
		}
	    }
	});
	Session.getActiveSession().requestNewPublishPermissions(
		new Session.NewPermissionsRequest(this, Arrays.asList("publish_actions")));

	accessToken = Session.getActiveSession().getAccessToken();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	Intent i = new Intent(this, MainActivity.class);
	i.putExtra("accessToken", accessToken);
	startActivity(i);
    }

}
