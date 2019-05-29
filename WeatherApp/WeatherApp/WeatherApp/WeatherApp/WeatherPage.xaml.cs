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
        string City { get; set; }

        public WeatherPage(string City)
        {
            this.City = City;

            if (City == "item.Title" || City == "")
            {
                WeatherInfo startInfo = new WeatherInfo();
                startInfo.Condition = "Add some cities of your preference!";
                startInfo.Visible = false;
                startInfo.LandingPage = true;
                BindingContext = startInfo;
            }
            else
            {
                WeatherInfo info = new WeatherInfo();
                MyWebRequest request = new MyWebRequest("http://240d492f.ngrok.io/" + City, "GET");
                info = JsonConvert.DeserializeObject<WeatherInfo>(request.GetResponse());
                info.Date1 = parseDate(info.Date1);
                info.Date2 = parseDate(info.Date2);
                info.Date3 = parseDate(info.Date3);
                info.Date4 = parseDate(info.Date4);

                var today = DateTime.Now;

                info.WeekDay1 = today.AddDays(1).DayOfWeek.ToString();
                info.WeekDay2 = today.AddDays(2).DayOfWeek.ToString();
                info.WeekDay3 = today.AddDays(3).DayOfWeek.ToString();
                info.WeekDay4 = today.AddDays(4).DayOfWeek.ToString();

                info.Visible = true;
                info.LandingPage = false;

                BindingContext = info;

            }


            InitializeComponent();

        }

        public string parseDate(string date)
        {
            string[] words = date.Split('-');

            return words[2] + '/' + words[1];
        }

        async void Remove_City(object sender, EventArgs e)
        {
            bool answer = await DisplayAlert("Are you sure?", "Removing " + this.City + " from your list.", "Yes", "No");
            if (answer)
            {
                MessagingCenter.Send(this, "RemoveItem", this.City);
            }
           

        }


    }
}