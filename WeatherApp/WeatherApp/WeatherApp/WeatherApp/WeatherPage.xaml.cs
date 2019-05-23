using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;
using Newtonsoft.Json;


namespace WeatherApp
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class WeatherPage : ContentPage
    {
        public string City { get; set; }

        public WeatherPage(string City)
        {
            if(City == "item.Title")
            {
                City = "Porto";
            }
            WeatherInfo info = new WeatherInfo();
            MyWebRequest request = new MyWebRequest("http://f29a894c.ngrok.io/" + City + ",Portugal", "GET");
            info = JsonConvert.DeserializeObject<WeatherInfo>(request.GetResponse());
            BindingContext = info;
            InitializeComponent();

        }


    
    }

    
}