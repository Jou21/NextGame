package com.game.next.nextgame.fragments;


import com.game.next.nextgame.notifications.MyResponse;
import com.game.next.nextgame.notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAMVdp4Uw:APA91bEyNZs4zQInxWjJeWJQwpyAHeHGHxQskh9ZgREJg4K5WESkmrsOdBy6jKVlrfW8vLHiHQzMebz8aCJ07kRVwEdGkQmHaATDLHMn6hHADfxS9QZLJIpoqIJZfuvVOpFKZ3MrG2LB"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
