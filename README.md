# LoveWeather
# Introduce 
  This is an android application which servering the weather for users.In this application,I try to use more design patterns to make the application more extendible.
# Update
  version 1.01
  There are so many changes because of the server of HeWeather's changes.This time,all of the APIs are updated,and the server access speed is higher.
  What's more,this version try to use local city database to avoid accessing network for the city data,so the first lanuch will run more smoothly.
# ScreenShot
<img width="320" height="480" src="https://github.com/HudsonAndroid/LoveWeather/raw/master/screenshot/city_manager.png"/> <img width="320" height="480" src="https://github.com/HudsonAndroid/LoveWeather/raw/master/screenshot/city_remove.png"/> <img width="320" height="480" src="https://github.com/HudsonAndroid/LoveWeather/raw/master/screenshot/daily_words.png"/> <img width="320" height="480" src="https://github.com/HudsonAndroid/LoveWeather/raw/master/screenshot/device-2017-12-13-230913.png"/> <img width="320" height="480" src="https://github.com/HudsonAndroid/LoveWeather/raw/master/screenshot/find_city.png"/> <img width="320" height="480" src="https://github.com/HudsonAndroid/LoveWeather/raw/master/screenshot/home_page.png"/> <img width="320" height="480" src="https://github.com/HudsonAndroid/LoveWeather/raw/master/screenshot/more_weather.png"/> <img width="320" height="480" src="https://github.com/HudsonAndroid/LoveWeather/raw/master/screenshot/search_city.png"/>
version 1.01
<img width="320" height="480" src="https://github.com/HudsonAndroid/LoveWeather/raw/master/screenshot/version-1.01/android8.0-version-1.01.png"/><img width="320" height="480" src="https://github.com/HudsonAndroid/LoveWeather/raw/master/screenshot/version-1.01/android8.0-version1.01-daily-words.png"/>
# Desgin Logic
## 1.main logic
 After we launched application,main thread will start a run-in-background service which is used to schedule a lot of tasks,such as update weather task,update background picture task,and so on.If it is the first time you start the service(maybe it is the first time you start the app),this service will try to locate the device's location by using baidu location sdk.When location information gets successfully,WeatherChooseUtils (a class in this project for choosing a city) will help to save the current city into cache,and then UpdateUtils will try to access the server to ask for the latest weather data.When it done,UpdateUtils will parse the data which returned by the server,and notify WeatherActivity to update the UI.After the first launch,next launch will try to get weather data from cache in the same time.
<img width="720" height="305" src="https://github.com/HudsonAndroid/LoveWeather/raw/master/screenshot/other/main_logic.png"/>
