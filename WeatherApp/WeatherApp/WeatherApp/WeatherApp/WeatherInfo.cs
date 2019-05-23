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
    }
}
