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
    public partial class MainPage : MasterDetailPage
    {
        public MainPage()
        {
            InitializeComponent();
            CitiesPage.ListView.ItemSelected += ListView_ItemSelected;
        }

        private void ListView_ItemSelected(object sender, SelectedItemChangedEventArgs e)
        {
            var item = e.SelectedItem as CityItem;

            if(item != null)
            {
                var page = (Page)Activator.CreateInstance(typeof(WeatherPage));
                page.Title = item.Title;

                Detail = new NavigationPage(page);
                IsPresented = false;

                CitiesPage.ListView.SelectedItem = null;
            }
               
          
        }
    }
}