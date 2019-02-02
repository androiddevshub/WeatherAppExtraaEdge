package app.weathertask;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NetworkApi {

    @GET("data/2.5/weather")
    Call<ResponseWeather> showDataCurrent(@Query("q") String q,@Query("appid") String appid, @Query("units") String units);

    @GET("data/2.5/forecast")
    Call<ResponseWeather> showDataForecast(@Query("q") String q,@Query("appid") String appid, @Query("units") String units);

}
