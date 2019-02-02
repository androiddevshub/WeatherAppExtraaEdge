package app.weathertask;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editCity;
    private Button proceedBtn ;
    private TextView tvCity, tvTemp, tvTempMin, tvTempMax, tvCloudDesc, tvForecast;
    private ImageView weatherIconImg, forecastBtn;
    private String cityName;
    private static String apiId = "ebb4be064be6f1f5bdbffea36487a567";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editCity = findViewById(R.id.editText_city);
        proceedBtn = findViewById(R.id.btn_proceed);
        forecastBtn = findViewById(R.id.btn_forecast);
        tvCity = findViewById(R.id.tv_city);
        tvTemp = findViewById(R.id.tv_temp);
        tvTempMin = findViewById(R.id.tv_min_temp);
        tvTempMax = findViewById(R.id.tv_max_temp);
        tvCloudDesc = findViewById(R.id.tv_cloud_desc);
        tvForecast = findViewById(R.id.tv_forecast);
        weatherIconImg = findViewById(R.id.weather_icon_img);

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cityName = editCity.getText().toString();

                if (cityName.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter a city name", Toast.LENGTH_SHORT).show();
                }else {
                    showData(cityName,apiId,"Imperial");
                }


            }
        });


    }


    private void showData(String city, String apiId, String units){

        final NetworkApi networkAPI = ApiClient.getClient().create(NetworkApi.class);

        final Call<ResponseWeather> responseCall = networkAPI.showDataCurrent(city,apiId,units);

        responseCall.enqueue(new Callback<ResponseWeather>() {
            @Override
            public void onResponse(Call<ResponseWeather> call, Response<ResponseWeather> response) {

                if (response.isSuccessful()){

                    double temp_int = response.body().getMain().getTemp();
                    double temp_int_min = response.body().getMain().getTempMin();
                    double temp_int_max = response.body().getMain().getTempMax();

                    tvCity.setText(response.body().getName());

                    String icon_url = "http://openweathermap.org/img/w/" + response.body().getWeather().get(0).getIcon() + ".png";

                    try {

                        Picasso.get()
                                .load(icon_url)
                                .placeholder(R.mipmap.ic_launcher)
                                .into(weatherIconImg);


                    } catch (Exception e){

                        e.printStackTrace();
                        Picasso.get()
                                .load(R.mipmap.ic_launcher)
                                .into(weatherIconImg);
                    }

                    double centi = (temp_int - 32)/1.8000;
                    centi = Math.round(centi * 100.0) / 100.0;
//                    int temp = (int)centi;

                    tvTemp.setText(String.valueOf(centi));

                    double centi1 = (temp_int_min - 32)/1.8000;
                    centi1 = Math.round(centi1 * 100.0) / 100.0;
//                    int tempMin = (int)centi1;

                    tvTempMin.setText("Min : " + String.valueOf(centi1));

                    double centi2 = (temp_int_min - 32)/1.8000;
                    centi2 = Math.round(centi2 * 100.0) / 100.0;
//                    int tempMax = (int)centi2;

                    tvTempMax.setText("Max : "+  String.valueOf(centi2));

                    tvCloudDesc.setText(response.body().getWeather().get(0).getDescription());

                }else {

                    ApiError error = ErrorUtils.parseError(response);
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseWeather> call, Throwable t) {

            }
        });


    }
}

