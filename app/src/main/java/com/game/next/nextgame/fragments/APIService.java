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
                    "Authorization:key=AAAAMVdp4Uw:APA91bHrZwMufGJf7Qutz74sOxoIdQ6lD5VrVX0X8vH_ZFFpRH181DB6ui4OVSZnqWgBG4Uwphe0EmIHrZNJGDU8PzcRkEaHi3NYVpfZtROsoLTWzILeaW6amBXy7nimqSARCQez1GNE"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
