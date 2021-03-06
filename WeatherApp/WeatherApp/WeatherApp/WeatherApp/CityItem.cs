﻿using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WeatherApp
{

    public class CityItem
    {
        public CityItem()
        {
            TargetType = typeof(CityItem);
        }

        [JsonProperty("title")]
        public string Title { get; set; }

        public Type TargetType { get; set; }
    }
}