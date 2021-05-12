package com.sendbird.groupchannel;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.sendbird.R;
import com.sendbird.main.sendBird.Chat;
import com.sendbird.main.sendBird.User;
import com.sendbird.main.sendBird.UserData;
import com.sendbird.network.createUser.ConnectUserRequest;

import kotlin.Unit;


public class GroupChannelActivity extends AppCompatActivity {

    private onBackPressedListener mOnBackPressedListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_channel);

        ConnectUserRequest connectUserData = new ConnectUserRequest("1827", "Taiwo Adebayo", "https://ulesson-staging.s3.eu-west-2.amazonaws.com/learners/avatars/defaults/thumb/missing.png",
                true);

        new User().connectUser(this, connectUserData, "a9175f71c52e8e53ecd7900e3e93a5e1f3063591", (userResponse) -> {

            new Chat().showChatList(this, R.id.container_group_channel, new UserData(userResponse.getUser_id(), userResponse.getNickname(), userResponse.getAccess_token()));

            return Unit.INSTANCE;
        }, (errorData) -> {
            Log.d("okh", errorData.getMessage() + "message");
            return Unit.INSTANCE;
        }, (updateAccessToken) -> {
            Log.d("okh", updateAccessToken + "update token");
            return Unit.INSTANCE;
        });

        String channelUrl = getIntent().getStringExtra("groupChannelUrl");
        if (channelUrl != null) {
            // If started from notification
            Fragment fragment = GroupChatFragment.newInstance(channelUrl);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.container_group_channel, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (mOnBackPressedListener != null && mOnBackPressedListener.onBack()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    interface onBackPressedListener {
        boolean onBack();
    }

}
