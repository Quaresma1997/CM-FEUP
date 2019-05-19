using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace WeatherApp
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class CitiesPage : ContentPage
    {
        public ListView ListView;

        public CitiesPage()
        {
            InitializeComponent();

            BindingContext = new CitiesPageViewModel();
            ListView = MenuItemsListView;
        }

        class CitiesPageViewModel : INotifyPropertyChanged
        {
            public ObservableCollection<CityItem> MenuItems { get; set; }

            public CitiesPageViewModel()
            {
                MenuItems = new ObservableCollection<CityItem>(new[]
                {
                    new CityItem { Id = 0, Title = "Porto" },
                    new CityItem { Id = 1, Title = "Braga" },
                    new CityItem { Id = 2, Title = "Coimbra" },
                    new CityItem { Id = 3, Title = "Lisboa" },
                    new CityItem { Id = 4, Title = "Faro" },
                });
            }

            #region INotifyPropertyChanged Implementation
            public event PropertyChangedEventHandler PropertyChanged;
            void OnPropertyChanged([CallerMemberName] string propertyName = "")
            {
                if (PropertyChanged == null)
                    return;

                PropertyChanged.Invoke(this, new PropertyChangedEventArgs(propertyName));
            }
            #endregion
        }
    }
}