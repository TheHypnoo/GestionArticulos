package com.sergigonzalez.gestionarticulos.weather

import android.app.ProgressDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.StrictMode
import android.text.Editable
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.sergigonzalez.gestionarticulos.R
import com.sergigonzalez.gestionarticulos.databinding.ActivityWeatherBinding
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URL


class Weather : AppCompatActivity() {

    private val api = Api()
    private var ciudad: JSONObject? = null
    private lateinit var _ciutat : City


    var tvFeelLike: TextView? = null
    var tvVis: TextView? = null
    var tvPre: TextView? = null
    var imgIcon: ImageView? = null
    var edt: AutoCompleteTextView? = null
    private lateinit var binding: ActivityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        binding.coord.background = getDrawable(R.drawable.gg)


        tvFeelLike = findViewById<TextView>(R.id.tvFeel)
        tvVis = findViewById<TextView>(R.id.tvAlgo)
        tvPre = findViewById<TextView>(R.id.tvPres)

        imgIcon = findViewById<ImageView>(R.id.IconWeather)

        edt = findViewById<AutoCompleteTextView>(R.id.edtCiudad)
        visibility(View.GONE)

        val btn = findViewById<View>(R.id.btnWea) as FloatingActionButton
        btn.setOnClickListener { Search() }

    }

    fun Search() {
        val Dialog = ProgressDialog(this)
        Dialog.setCancelable(false)
        Dialog.setCanceledOnTouchOutside(false)
        val client = AsyncHttpClient()
        client.setMaxRetriesAndTimeout(0, 10000)
        client[api.BaseApi + "?q=" + edt?.text + api.apiKey + api.IdiomEs, object : AsyncHttpResponseHandler() {
            @Override
            override fun onStart() {
                // called before request is started
                Dialog.setMessage("Descargando datos...")
                Dialog.show()
            }

            @Override
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                Dialog.setMessage("Procesando datos...")
                val str = String(responseBody)
                try {
                    ciudad = JSONObject(str)
                    _ciutat = City(ciudad!!.getString("name"),
                            ciudad!!.getJSONObject("main").getDouble("temp"),
                            ciudad!!.getJSONObject("main").getDouble("temp_max"),
                            ciudad!!.getJSONObject("main").getDouble("temp_min"),
                            ciudad!!.getJSONObject("main").getDouble("feels_like"),
                            ciudad!!.getJSONArray("weather").getJSONObject(0).getString("icon"),
                            ciudad!!.getJSONArray("weather").getJSONObject(0).getString("description"),
                            ciudad!!.getJSONObject("main").getDouble("pressure"),
                            ciudad!!.getJSONObject("wind").getDouble("speed"),
                            ciudad!!.getJSONObject("main").getDouble("humidity"),
                            ciudad!!.getDouble("visibility")
                    )
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                try {
                    InsertInfo()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                visibility(View.VISIBLE)
                Dialog.hide()
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                val Error: String = error.message.toString()
                Dialog.hide()
                val _snack = Snackbar.make(findViewById(R.id.coord), Error, Snackbar.LENGTH_SHORT)
                _snack.show()
                return
            }
        }]
    }

    fun visibility(_view: Int) {
        binding.tvCity.visibility = _view
        binding.tvWeather.visibility = _view
        binding.tvTemp.visibility = _view
        binding.tvTempMax.visibility = _view
        binding.tvTempMin.visibility = _view
        tvFeelLike?.visibility = _view
        binding.tvDetails.visibility = _view
        tvPre?.visibility = _view
        binding.tvHum.visibility = _view
        tvVis?.visibility = _view
        binding.tvWind.visibility = _view
        imgIcon?.visibility = _view
    }

    @Throws(IOException::class)
    fun InsertInfo() {
        binding.tvCity.text = _ciutat?.get_nameCity()
        binding.tvWeather.text = _ciutat?.getWeather()
        binding.tvTemp.text = (_ciutat!!.getTemp().toInt().toString() + "º")
        binding.tvTempMax.text = (_ciutat?.getTempMax()?.toInt().toString() + "º")
        binding.tvTempMin.text = (_ciutat?.getTempMin()?.toInt().toString() + "º")
        tvFeelLike?.text = ("Sensacion termica " + _ciutat?.getFeelLike()?.toInt().toString() + "º")
        tvPre?.text = ("Presion " + _ciutat?.getPresion() + "mb")
        binding.tvHum.text = ("Humedad " + _ciutat?.getHumedad())
        tvVis?.text = ("Visiblidad " + _ciutat?.getVisibilidad())
        binding.tvWind.text = ("Vel.Viento " + _ciutat?.getVelocidad_viento() + "km/h")
        val url = URL(_ciutat?.get_url())
        val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        imgIcon!!.setImageBitmap(bmp)
        edt?.text = Editable.Factory.getInstance().newEditable("")
    }

}