using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace WeatherApp
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class CityItemPage : ContentPage
    {
        public CityItem CityItem { get; set; }

        public CityItemPage()
        {
            InitializeComponent();
            CityItem = new CityItem
            {
                Title = ""
            };

            BindingContext = this;
        }

        async void Save_Clicked(object sender, EventArgs e)
        {
            MyWebRequest request = new MyWebRequest("http://240d492f.ngrok.io/" + CityItem.Title , "GET");
            WeatherInfo info = JsonConvert.DeserializeObject<WeatherInfo>(request.GetResponse());
            if(info.City == "unknown" || CityItem.Title=="")
            {
               await DisplayAlert("Error", "The city inserted is not in our database, please try again.", "OK");
            }
            else
            {
                if(info.Region != "" && info.Region != info.City)
                {
                    CityItem.Title = info.City + ", " + info.Country;
                }
                else
                {
                    CityItem.Title = info.City + ", " + info.Country;
                }
                
                MessagingCenter.Send(this, "AddItem", CityItem);
                //await Navigation.PopModalAsync();
            }           
        }

        public async void Cancel_Clicked(object sender, EventArgs e)
        {
            await Navigation.PopModalAsync();
        }

        public async void popItemExists(string name)
        {
            await DisplayAlert("Warning", name + " is already in your list.", "OK");
            await Navigation.PopModalAsync();
        }

        public async void popNewCityItemPage()
        {
            await Navigation.PopModalAsync();
        }
    }
}