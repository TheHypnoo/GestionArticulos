package com.sergigonzalez.gestionarticulos

import android.app.ProgressDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.StrictMode
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.matteobattilana.weather.PrecipType
import com.github.matteobattilana.weather.WeatherData
import com.github.matteobattilana.weather.WeatherView
import com.google.android.material.snackbar.Snackbar
import com.sergigonzalez.gestionarticulos.Interface.HttpService
import com.sergigonzalez.gestionarticulos.databinding.ActivityWeatherBinding
import com.sergigonzalez.gestionarticulos.weather.Weather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL


class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding

    companion object {
        const val BASEAPI = "http://api.openweathermap.org/data/2.5/"
        const val API_ID = "5ab85ccd70b8d88070aa1c166d5006bd"
        const val LANG = "es"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        binding.coord.background =
            ContextCompat.getDrawable(applicationContext, R.drawable.backgroundweather)

        visibility(View.GONE)
        binding.btnWea.setOnClickListener { Search() }

    }

    fun Search() {
        var weather: PrecipType
        val Dialog = ProgressDialog(this)
        Dialog.setCancelable(false)
        Dialog.setCanceledOnTouchOutside(false)
        Dialog.setMessage("Descargando datos...")
        Dialog.show()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASEAPI)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val httpService = retrofit.create(HttpService::class.java)

        val call: Call<Weather> =
            httpService.getJSON("weather?q=${binding.edtCiudad.text}&appid=$API_ID&lang=$LANG")

        call.enqueue(object : Callback<Weather> {

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                t.message?.let {
                    Snackbar.make(
                        binding.coord,
                        it, Snackbar.LENGTH_SHORT
                    ).show()
                }
                Dialog.hide()

            }


            override fun onResponse(call: Call<Weather>?, response: Response<Weather>?) {

                if (!response!!.isSuccessful) {

                    Snackbar.make(binding.coord, response.code().toString(), Snackbar.LENGTH_SHORT)
                        .show()
                    Dialog.hide()
                    return

                }
                Dialog.setMessage("Procesando datos...")
                val cityList = response.body()

                if (cityList != null) {

                    binding.tvCity.text = "Tiempo actual en ${cityList.name}"
                    binding.tvTempMax.text = "${Convert(cityList.main.tempMax).toInt()} º"
                    binding.tvTempMin.text = "${Convert(cityList.main.tempMin).toInt()} º"

                    binding.tvTemp.text = "${Convert(cityList.main.temp).toInt()} º"
                    binding.tvWeather.text = cityList.weather[0].description
                    when (cityList.weather[0].description) {
                        "lluvia ligera" -> {
                            weather = PrecipType.RAIN
                            binding.weatherView.apply {
                                setWeatherData(weather)
                                speed = 600
                            }
                        }
                        "nubes" ->
                            binding.weatherView.setWeatherData(PrecipType.CLEAR)
                        "nieve" -> {
                            weather = PrecipType.SNOW
                            binding.weatherView.apply {
                                setWeatherData(weather)
                                speed = 100
                                emissionRate = 5f
                                angle = 20
                                fadeOutPercent = .85f
                            }
                        }
                        "nevada intensa" -> {
                            weather = PrecipType.SNOW
                            binding.weatherView.apply {
                                setWeatherData(weather)
                                speed = 350
                                emissionRate = 100f
                                angle = 20
                                fadeOutPercent = .85f
                            }
                        }
                    }
                    binding.tvFeel.text =
                        "Sensacion termica: ${Convert(cityList.main.feelsLike).toInt()} º"
                    binding.tvPres.text = "Presion: ${cityList.main.pressure} mb"
                    binding.tvHum.text = "Humedad: ${cityList.main.humidity}"
                    binding.tvAlgo.text = "Visibilidad: ${cityList.visibility}"
                    binding.tvWind.text = "Vel.Viento ${cityList.wind.speed}"
                    val uri =
                        URL("https://openweathermap.org/img/wn/" + cityList.weather[0].icon + "@2x.png")
                    val bmp = BitmapFactory.decodeStream(uri.openConnection().getInputStream())
                    binding.IconWeather.setImageBitmap(bmp)
                    binding.edtCiudad.text = Editable.Factory.getInstance().newEditable("")

                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.edtCiudad.windowToken, 0)
                    visibility(View.VISIBLE)
                    Dialog.hide()
                }
            }
        })
    }

    fun visibility(_view: Int) {
        binding.tvCity.visibility = _view
        binding.tvWeather.visibility = _view
        binding.tvTemp.visibility = _view
        binding.tvTempMax.visibility = _view
        binding.tvTempMin.visibility = _view
        binding.tvFeel.visibility = _view
        binding.tvDetails.visibility = _view
        binding.tvPres.visibility = _view
        binding.tvHum.visibility = _view
        binding.tvAlgo.visibility = _view
        binding.tvWind.visibility = _view
        binding.IconWeather.visibility = _view
    }

    fun Convert(x: Double): Double {
        return x - 273.15
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}