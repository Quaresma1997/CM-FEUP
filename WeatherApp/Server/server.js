var express = require('express')
var app = express()
var request = require('request');
var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

var HTTP_PORT = 8000



// Start server
app.listen(HTTP_PORT, () => {
    console.log("Server running on port %PORT%".replace("%PORT%", HTTP_PORT))
});

app.get("/weather", (req, res) => {
    request('https://api.apixu.com/v1/forecast.json?key=fbf305ea8c454be0b53142701192904&q=Beja', function (error, response, body) {
        console.log('error:', error); // Print the error if one occurred and handle it
        console.log('statusCode:', response && response.statusCode); // Print the response status code if a response was received
        parsed = JSON.parse(body);


        //TODO FORECAST
        const city = parsed.location.name;
        const last_updated = parsed.current.last_updated;
        const temp_c = parsed.current.temp_c;
        const temp_f = parsed.current.temp_f;
        const is_day = parsed.current.is_day;
        const condition = parsed.current.condition.text;
        const icon = parsed.current.condition.icon;
        const wind = parsed.current.wind_kph;
        const wind_dir = parsed.current.wind_dir;
        const precip = parsed.current.precip_mm;
        const humidity = parsed.current.humidity;
        const feelslike_c = parsed.current.feelslike_c;
        const feelslike_f = parsed.current.feelslike_f;
        const uv = parsed.current.uv;
        const maxtemp_c = parsed.forecast.forecastday[0].day.maxtemp_c;
        const maxtemp_f = parsed.forecast.forecastday[0].day.maxtemp_f;
        const mintemp_c = parsed.forecast.forecastday[0].day.mintemp_c;
        const mintemp_f = parsed.forecast.forecastday[0].day.mintemp_f;



        res.json(
            {
                "city" : city,
                "last_updated": last_updated,
                "temp_c": temp_c,
                "temp_f": temp_f,
                "is_day": is_day,
                "condition": condition,
                "icon" : icon,
                "wind": wind,
                "wind_dir" : wind_dir,
                "precip" : precip,
                "humidity" : humidity,
                "feelslike_c" : feelslike_c,
                "feelslike_f" : feelslike_f,
                "uv" : uv,
                "maxtemp_c" : maxtemp_c,
                "mintemp_c" : mintemp_c,
                "maxtemp_f" : maxtemp_f,
                "mintemp_f" : mintemp_f
            })
            
        
    });
});

