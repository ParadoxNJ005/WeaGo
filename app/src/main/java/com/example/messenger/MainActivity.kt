package com.example.messenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.SearchView
import com.example.messenger.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// f5addd946f843104f1d1664078b157af
//https://api.openweathermap.org/data/2.5/weather?q=${city}&units=metric&appid=${APIkey}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )


        fetachWeatherData("Jaipur")
        SearchCity()


    }

    private fun SearchCity(){

        val searchview = binding.searchView
        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?):Boolean {
                if (query != null) {
                    fetachWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun fetachWeatherData(cityName: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response = retrofit.getWeatherData(cityName,"f5addd946f843104f1d1664078b157af", "metric")
        response.enqueue(object: Callback<WeatherList> {
            override fun onResponse(call: Call<WeatherList>, response: Response<WeatherList>) {
                val responseBody = response.body()
                if(response.isSuccessful && responseBody != null){

                    binding.temp.text =  "${responseBody.main.temp.toString()} C"
                    binding.maxTemp.text = "Max Temp : ${responseBody.main.temp_max.toString()}"
                    binding.minTemp.text = "Min Temp : ${responseBody.main.temp_min.toString()}"
                    binding.humidity.text = "${responseBody.main.humidity.toString()} %"
                    binding.sea.text = "${responseBody.main.pressure.toString()} hpa"

                    binding.condition.text = responseBody.weather.firstOrNull()?.main?:"unknown"
                    binding.dayType.text = responseBody.weather.firstOrNull()?.main?:"unknown"

                    binding.sunrise.text = "${time(responseBody.sys.sunrise.toLong())}"
                    binding.sunset.text = "${time(responseBody.sys.sunset.toLong())}"

                    binding.windspeed.text = "${responseBody.wind.speed} m/s"

                    binding.day.text= dayName(System.currentTimeMillis())
                        binding.date.text = date()
                        binding.place.text = "$cityName"


                    changeImagesaccordingToWeatherCondition(responseBody.weather.firstOrNull()?.main?:"unknown")

                }
            }

            override fun onFailure(call: Call<WeatherList>, t: Throwable) {
            }

        })


    }

    private fun changeImagesaccordingToWeatherCondition(condition: String) {
        when (condition){

            "Clear Sky","Sunny","Clear" ->{
                binding.root.setBackgroundResource(R.drawable.sunny)
                binding.animationView.setAnimation(R.raw.sunny)
            }

            "Partly Clouds", "Clouds","Overcast","Mist","Foggy","Haze" ->{
                binding.root.setBackgroundResource(R.drawable.cloudy)
                binding.animationView.setAnimation(R.raw.cloud)
            }

            "Light Rain","Drizzle","Moderate Rain","Showers","Heavy Rain","Rain" ->{
                binding.root.setBackgroundResource(R.drawable.rain)
                binding.animationView.setAnimation(R.raw.rain)
            }

            "Light Snow", "Moderate Snow", "Heavy Snow", "Blizzed","Snow" ->{
                binding.root.setBackgroundResource(R.drawable.snow)
                binding.animationView.setAnimation(R.raw.snow)
            }
        }
        binding.animationView.playAnimation()
    }

    fun dayName(timestamp: Long):String{
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }

    private fun time(timestamp: Long):String{
        val sdf = SimpleDateFormat("HH.mm", Locale.getDefault())
        return sdf.format((Date(timestamp*1000)))
    }

    private fun date():String{
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }

}

