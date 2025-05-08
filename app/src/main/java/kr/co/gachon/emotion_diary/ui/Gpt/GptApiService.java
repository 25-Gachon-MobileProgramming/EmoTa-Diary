package kr.co.gachon.emotion_diary.ui.Gpt;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GptApiService {

    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    Call<GptResponse> getGptMessage(
            @Header("Authorization") String authHeader,
            @Body GptRequest request
    );
}
