using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Text;

namespace WeatherApp
{
    public class WeatherInfo
    {
        [JsonProperty("city")]
        public string City { get; set; }

        [JsonProperty("region")]
        public string Region { get; set; }

        [JsonProperty("country")]
        public string Country { get; set; }

        [JsonProperty("last_updated")]
        public string LastUpdated { get; set; }

        [JsonProperty("temp_c")]
        public string TempC { get; set; }

        [JsonProperty("condition")]
        public string Condition { get; set; }

        [JsonProperty("icon")]
        public string Icon { get; set; }

        [JsonProperty("wind")]
        public string WindSpeed { get; set; }

        [JsonProperty("wind_dir")]
        public string WindDir { get; set; }

        [JsonProperty("precip")]
        public string Precip { get; set; }

        [JsonProperty("humidity")]
        public string Humidity { get; set; }

        [JsonProperty("feelslike_c")]
        public string FeelsLikeC { get; set; }

        [JsonProperty("uv")]
        public string Uv { get; set; }

        [JsonProperty("maxtemp_c")]
        public string MaxTempC { get; set; }

        [JsonProperty("mintemp_c")]
        public string MinTempC { get; set; }

        //FORECAST DAY 1

        [JsonProperty("maxtemp_c1")]
        public string MaxTempC1 { get; set; }

        [JsonProperty("mintemp_c1")]
        public string MinTempC1 { get; set; }

        [JsonProperty("condition_1")]
        public string Condition1 { get; set; }

        [JsonProperty("icon_1")]
        public string Icon1 { get; set; }

        [JsonProperty("date_1")]
        public string Date1 { get; set; }

        public string WeekDay1 { get; set; }

        //FORECAST DAY 2

        [JsonProperty("maxtemp_c2")]
        public string MaxTempC2 { get; set; }

        [JsonProperty("mintemp_c2")]
        public string MinTempC2 { get; set; }

        [JsonProperty("condition_2")]
        public string Condition2 { get; set; }

        [JsonProperty("icon_2")]
        public string Icon2 { get; set; }

        [JsonProperty("date_2")]
        public string Date2 { get; set; }

        public string WeekDay2 { get; set; }

        //FORECAST DAY 3

        [JsonProperty("maxtemp_c3")]
        public string MaxTempC3 { get; set; }

        [JsonProperty("mintemp_c3")]
        public string MinTempC3 { get; set; }

        [JsonProperty("condition_3")]
        public string Condition3 { get; set; }

        [JsonProperty("icon_3")]
        public string Icon3 { get; set; }

        [JsonProperty("date_3")]
        public string Date3 { get; set; }

        public string WeekDay3 { get; set; }

        //FORECAST DAY 4

        [JsonProperty("maxtemp_c4")]
        public string MaxTempC4 { get; set; }

        [JsonProperty("mintemp_c4")]
        public string MinTempC4 { get; set; }

        [JsonProperty("condition_4")]
        public string Condition4 { get; set; }

        [JsonProperty("icon_4")]
        public string Icon4 { get; set; }

        [JsonProperty("date_4")]
        public string Date4 { get; set; }

        public string WeekDay4 { get; set; }

        public bool Visible { get; set; }

        public bool LandingPage { get; set; }

    }
}
