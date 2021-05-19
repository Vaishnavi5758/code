package service

import com.example.weatherForcasting.helper.WeatherBitHelper
import com.example.weatherForcasting.model.Duration
import com.example.weatherForcasting.model.Forecast
import com.example.weatherForcasting.model.Weather
import com.example.weatherForcasting.service.WeatherBitService
import helper.DateHelper
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WeatherBitServiceSpec extends Specification {
WeatherBitService weatherBitService
WeatherBitHelper weatherBitHelper

    def setup(){
      weatherBitHelper  = Mock()
        weatherBitService = new WeatherBitService(weatherBitHelper)
    }

    def"getHourlyForecastForZipCode:should return forecast with coolest hours"(){

        given:
        String zipcode = "413512"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localDateTime = LocalDateTime.now()
        LocalDateTime localDateTime1 = LocalDateTime.parse(DateHelper.getDate(1), formatter)
        LocalDateTime localDateTime2 = LocalDateTime.parse(DateHelper.getDate(2), formatter);

        List<Weather> weatherList = [new Weather(temperature: 30.0,localDateTime: localDateTime), new Weather(temperature: 31.0,localDateTime:localDateTime1), new Weather(temperature:  32.0,localDateTime:localDateTime2)]
        Forecast forecast = new Forecast(countryCode: "IN", stateCode: "MH", timezone: "Asia/Kolkata",weatherList:weatherList)


        when:
        def response = weatherBitService.getHourlyForecastForZipCode("IN","413512", "coolest")
        assert response.coolestHourOfDay != null
        assert response.weatherList.size() != weatherList.size()

        then:
        1 * weatherBitHelper.getHourlyForecast("IN", zipcode, Duration.Daily) >> forecast
    }
}
