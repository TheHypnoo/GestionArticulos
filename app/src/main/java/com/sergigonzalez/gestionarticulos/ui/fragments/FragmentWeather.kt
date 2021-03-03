package com.sergigonzalez.gestionarticulos.ui.fragments

import android.app.ProgressDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.StrictMode
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.matteobattilana.weather.PrecipType
import com.google.android.material.snackbar.Snackbar
import com.sergigonzalez.gestionarticulos.Interface.HttpService
import com.sergigonzalez.gestionarticulos.R
import com.sergigonzalez.gestionarticulos.databinding.FragmentWeatherBinding
import com.sergigonzalez.gestionarticulos.ui.activitys.WeatherActivity
import com.sergigonzalez.gestionarticulos.weather.Weather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL

class FragmentWeather : Fragment() {
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    val BASEAPI = "http://api.openweathermap.org/data/2.5/"
    val API_ID = "5ab85ccd70b8d88070aa1c166d5006bd"
    val LANG = "es"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Weather"
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        binding.background.background =
            ContextCompat.getDrawable(
                this@FragmentWeather.requireContext(),
                R.drawable.backgroundweather
            )
        visibility(View.GONE)
    }

    fun Search() {
        var weather: PrecipType
        val Dialog = ProgressDialog(this@FragmentWeather.requireContext())
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
            httpService.getJSON("weather?q=${binding.edtCiudad.text}&appid=${API_ID}&lang=${LANG}")

        call.enqueue(object : Callback<Weather> {

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                t.message?.let {
                    Snackbar.make(
                        binding.root,
                        it, Snackbar.LENGTH_SHORT
                    ).show()
                }
                Dialog.hide()

            }


            override fun onResponse(call: Call<Weather>?, response: Response<Weather>?) {

                if (!response!!.isSuccessful) {

                    Snackbar.make(binding.root, response.code().toString(), Snackbar.LENGTH_SHORT)
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
                        "lluvia" -> {
                            weather = PrecipType.RAIN
                            binding.weatherView.apply {
                                setWeatherData(weather)
                                speed = 600
                            }
                        }
                        "lluvia ligera" -> {
                            weather = PrecipType.RAIN
                            binding.weatherView.apply {
                                setWeatherData(weather)
                                speed = 600
                            }
                        }
                        "lluvia intensa" -> {
                            weather = PrecipType.RAIN
                            binding.weatherView.apply {
                                setWeatherData(weather)
                                speed = 600
                            }
                        }
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
                        "nevada ligera" -> {
                            weather = PrecipType.SNOW
                            binding.weatherView.apply {
                                setWeatherData(weather)
                                speed = 350
                                emissionRate = 100f
                                angle = 20
                                fadeOutPercent = .85f
                            }
                        }
                        else -> binding.weatherView.setWeatherData(PrecipType.CLEAR)
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
                    val imm = activity!!.getSystemService(
                        Context.INPUT_METHOD_SERVICE
                    ) as InputMethodManager
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
}