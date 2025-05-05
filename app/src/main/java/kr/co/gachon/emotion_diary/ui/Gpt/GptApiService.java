package kr.co.gachon.emotion_diary.ui.Gpt;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GptApiService {
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer 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\n" 
    })
    @POST("v1/chat/completions")
    Call<GptResponse> getGptMessage(@Body GptRequest request);
}
