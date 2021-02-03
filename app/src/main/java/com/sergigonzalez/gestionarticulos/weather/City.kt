package com.sergigonzalez.gestionarticulos.weather

class City(_nameCity: String?, temp: Double, tempMax: Double, tempMin: Double, feelLike: Double, _url: String, weather: String?, _presion: Double, vel: Double, hum: Double, visi: Double
) {
    private var _nameCity: String? = null
    private var Temp = 0.0
    private var TempMax = 0.0
    private var TempMin = 0.0
    private var FeelLike = 0.0
    private var _url: String? = null
    private var weather: String? = null
    private var Presion = 0
    private var Velocidad_viento = 0
    private var Humedad = 0
    private var Visibilidad = 0

    init {
        this._nameCity = _nameCity
        Temp = Convert(temp)
        TempMax = Convert(tempMax)
        TempMin = Convert(tempMin)
        FeelLike = Convert(feelLike)
        this._url = "https://openweathermap.org/img/w/$_url.png"
        this.weather = weather
        Presion = _presion.toInt()
        Velocidad_viento = vel.toInt()
        Humedad = hum.toInt()
        Visibilidad = visi.toInt()
    }

    fun get_nameCity(): String? {
        return _nameCity
    }

    fun set_nameCity(_nameCity: String?) {
        this._nameCity = _nameCity
    }

    fun getTemp(): Double {
        return Temp
    }

    fun setTemp(temp: Double) {
        Temp = temp
    }

    fun getTempMax(): Double {
        return TempMax
    }

    fun setTempMax(tempMax: Double) {
        TempMax = tempMax
    }

    fun getTempMin(): Double {
        return TempMin
    }

    fun setTempMin(tempMin: Double) {
        TempMin = tempMin
    }

    fun getFeelLike(): Double {
        return FeelLike
    }

    fun setFeelLike(feelLike: Double) {
        FeelLike = feelLike
    }

    fun get_url(): String? {
        return _url
    }

    fun set_url(_url: String?) {
        this._url = _url
    }

    fun getWeather(): String? {
        return weather
    }

    fun setWeather(weather: String?) {
        this.weather = weather
    }

    fun getPresion(): Int {
        return Presion
    }

    fun setPresion(presion: Int) {
        Presion = presion
    }

    fun getVelocidad_viento(): Int {
        return Velocidad_viento
    }

    fun setVelocidad_viento(velocidad_viento: Int) {
        Velocidad_viento = velocidad_viento
    }

    fun getHumedad(): Int {
        return Humedad
    }

    fun setHumedad(humedad: Int) {
        Humedad = humedad
    }

    fun getVisibilidad(): Int {
        return Visibilidad
    }

    fun setVisibilidad(visibilidad: Int) {
        Visibilidad = visibilidad
    }

    fun Convert(x: Double): Double {
        return x - 273.15
    }

    override fun toString(): String {
        return "City{" +
                "_nameCity='" + _nameCity + '\'' +
                ", Temp=" + Temp +
                ", TempMax=" + TempMax +
                ", TempMin=" + TempMin +
                ", FeelLike=" + FeelLike +
                ", _url='" + _url + '\'' +
                ", weather='" + weather + '\'' +
                '}'
    }
}