using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;
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
            public ICommand OnAddButtonClicked { get; }

            public CitiesPageViewModel()
            {
                if (App.Current.Properties.ContainsKey("cities"))
                {
                   MenuItems = JsonConvert.DeserializeObject<ObservableCollection<CityItem>>(Application.Current.Properties["cities"].ToString());

                } else
                {
                    MenuItems = new ObservableCollection<CityItem>();
                }

                

                OnAddButtonClicked = new Command(async() => { await Application.Current.MainPage.Navigation.PushModalAsync(new CityItemPage()); });

                MessagingCenter.Subscribe<CityItemPage, CityItem>(this, "AddItem", async (obj, item) =>
                {                  
                    var copy = new ObservableCollection<CityItem>(MenuItems);
                    bool found = false;
                    var newCityItem = item as CityItem;

                    foreach (var city in copy)
                    {
                        if (city.Title == newCityItem.Title)
                        {
                            obj.popItemExists(newCityItem.Title);
                            found = true;
                        }

                    }

                    if (!found)
                    {
                        obj.popNewCityItemPage();
                        MenuItems.Add(newCityItem);
                        var json = JsonConvert.SerializeObject(MenuItems);
                        Application.Current.Properties["cities"] = json;
                        await App.Current.SavePropertiesAsync();
                    }                    
                        
                      
                });


                MessagingCenter.Subscribe<WeatherPage, string>(this, "RemoveItem", async (obj, name) =>
                {
                    var copy = new ObservableCollection<CityItem>(MenuItems);
                    foreach (var item in copy)
                    {
                        if (item.Title == name)
                        {
                            MenuItems.Remove(item);
                            var json = JsonConvert.SerializeObject(MenuItems);
                            Application.Current.Properties["cities"] = json;
                            await App.Current.SavePropertiesAsync();
                        }
                    }
                });
                //  void OnAddButtonClicked(object sender, EventArgs e) => MenuItems.Add(new CityItem { Title = "Viana" });// OnPropertyChanged("ListViewItems");
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