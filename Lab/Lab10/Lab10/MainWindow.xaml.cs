using laboratorium_10;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Forms;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace Lab10
{
    /// <summary>
    /// Logika interakcji dla klasy MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        private CarBindingList myCarBindingList;
        private BindingSource carBindingSource;

        //Słownik do przechowywania informacji o sortowaniu kolummn
        private Dictionary<string, bool> sortingColumnsAscending = new Dictionary<string, bool>();

        private delegate int CompareCarsPowerDelegate(Car car1, Car car2);

        public static List<Car> myCars = new List<Car>(){
                new Car("E250", new Engine(1.8, 204, "CGI"), 2009),
                new Car("E350", new Engine(3.5, 292, "CGI"), 2009),
                new Car("A6", new Engine(2.5, 187, "FSI"), 2012),
                new Car("A6", new Engine(2.8, 220, "FSI"), 2012),
                new Car("A6", new Engine(3.0, 295, "TFSI"), 2012),
                new Car("A6", new Engine(2.0, 175, "TDI"), 2011),
                new Car("A6", new Engine(3.0, 309, "TDI"), 2011),
                new Car("S6", new Engine(4.0, 414, "TFSI"), 2012),
                new Car("S8", new Engine(4.0, 513, "TFSI"), 2012)
        };
        public MainWindow()
        {

            LINQ();


            //Zadanie 2

            List<Car> myCarsCopy = new List<Car>(myCars);
            CompareCarsPowerDelegate arg1 = CompareCarsPowers;
            Predicate<Car> arg2 = IsTDI;
            Action<Car> arg3 = ShowMessageBox;

            myCarsCopy.Sort(new Comparison<Car>(arg1));

            //Posortowane po mocy
            Trace.WriteLine("Posorotowane po mocy:");

            foreach (var elem in myCarsCopy)
            {
                Trace.WriteLine(elem);
            }

            // wyświetlanie MB z TDI
            myCarsCopy.FindAll(arg2).ForEach(arg3);



            //DataController.SortingAndSearchingWithCarBindingList();



            myCarBindingList = new CarBindingList(myCars);
            carBindingSource = new BindingSource();


            //Zad3- możliwości sorotwania listy i szukania w niej wartości

            //sortujemy liste alfabetycznie po modelu - widać przy starcie UI
            myCarBindingList.Sort("model", System.ComponentModel.ListSortDirection.Ascending);



            //Szukanie po wartości liczbowej
            Trace.WriteLine("Wyszukiwanie samochodów o mocy 414 HP");
            var searchResult = myCarBindingList.FindCars("motor.horsePower", 414);

            foreach (var car in searchResult)
            {
                Trace.WriteLine(car);
            }


            //szukanie po String
            Trace.WriteLine("Wyszukiwanie A6");
            searchResult = myCarBindingList.FindCars("model", "A6");
            foreach (var car in searchResult)
            {
                Trace.WriteLine(car);
            }


            InitializeComponent();

            CreateSearchComboBox();
            InitialSortingColumns();
            UpdateDataGrid();
        }

        private void LINQ()
        {
            //Zapytania LINQ

            //kaskada metod
            var query1 = myCars
                .Where(car => car.model.Equals("A6"))
                .Select(car => new {
                    engineType = String.Compare(car.motor.model, "TDI") == 0 ? "diesel" : "petrol",
                    hppl = car.motor.horsePower / car.motor.displacement,
                })
                .GroupBy(element => element.engineType)
                .Select(elem => new {
                    type = elem.First().engineType.ToString(),
                    avgHppl = elem.Average(s => s.hppl).ToString() // średnia wartość Hppl z wszytkich elementów grupy
                })
                .OrderByDescending(t => t.avgHppl);


            //linq
            var query2 = from elem in (from car in myCars
                                       where car.model.Equals("A6")
                                       select new
                                       {
                                           engineType = String.Compare(car.motor.model, "TDI") == 0 ? "diesel" : "petrol",
                                           hppl = car.motor.horsePower / car.motor.displacement,
                                       })
                         group elem by elem.engineType into elementsGrouped
                         select new
                         {
                             type = elementsGrouped.First().engineType.ToString(),
                             avgHppl = elementsGrouped.Average(s => s.hppl).ToString()
                         } into elemSelected
                         orderby elemSelected.avgHppl descending
                         select elemSelected;

            //Wypisujemy wyniki obu
            Trace.WriteLine("Query 1:");
            foreach (var item in query1)
            {
                Trace.WriteLine(item.type + ": " + item.avgHppl);
            }
            Trace.WriteLine("Query 2:");
            foreach (var item in query2)
            {
                Trace.WriteLine(item.type + ": " + item.avgHppl);
            }
        }
        private void InitialSortingColumns()
        {
            sortingColumnsAscending.Clear();
            sortingColumnsAscending.Add("model", false);
            sortingColumnsAscending.Add("motor", false);
            sortingColumnsAscending.Add("year", false);

        }

        private void ButtonSearch(object sender, RoutedEventArgs e)
        {
            //Sprawdzamy czy pojawiły się nowe wartości i je zapisujemy
            CheckForNewItems();

            //Wynik wszukiwania
            List<Car> resultListOfCars;
            Int32 tmp;
            if (!searchTextBox.Text.Equals(""))
            {
                //odczyt wartości po ktroej bedziemy szukac
                string property = comboBox.SelectedItem.ToString();
                


                //Szukamy
                if (Int32.TryParse(searchTextBox.Text, out tmp))
                {
                    resultListOfCars = myCarBindingList.FindCars(property, tmp);
                }
                else
                {
                    resultListOfCars = myCarBindingList.FindCars(property, searchTextBox.Text);
                }

                //wyswietlamy wyniki
                myCarBindingList = new CarBindingList(resultListOfCars);
                UpdateDataGrid();
            }
        }

        private void CheckForNewItems()
        {
            //sprawdzamy czy pojawiły się nowe wiersze, któe nie są na liście myCars i ew. je dodajemy
            foreach (Car item in myCarBindingList)
            {
                if (!myCars.Contains(item))
                {
                    myCars.Add(item);
                }
            }
        }

        private void ButtonReload(object sender, RoutedEventArgs e)
        {
            //sprawdzamy czy są nowe wiersze i ładujemy liste od nowa - np. po wyszukiwaniu
            CheckForNewItems();
            myCarBindingList = new CarBindingList(myCars);
            UpdateDataGrid();
        }
        private void SortColumn(object sender, RoutedEventArgs e)
        {
            var columnHeader = sender as DataGridColumnHeader;
            //pobieramy nazwę kolumny
            string columnName = columnHeader.ToString().Split(' ')[1].ToLower();
            //sprawdzamy czy kolumna jest ascending - rosnąca

            bool isAsc = sortingColumnsAscending[columnName];
            //resetujemy sortowanie 
            InitialSortingColumns();
            //sortujemy po wybranej kolumnie
            if (isAsc == true)
            {
                myCarBindingList.Sort(columnName, ListSortDirection.Descending);
            }
            else
            {
                myCarBindingList.Sort(columnName, ListSortDirection.Ascending);
            }
            //ustawiamy odpowiendnią wartość
            sortingColumnsAscending[columnName] = !isAsc;
            UpdateDataGrid();
        }
        private void ButtonDeleteRow(object sender, RoutedEventArgs e)
        {
            for (var vis = sender as Visual; vis != null; vis = VisualTreeHelper.GetParent(vis) as Visual)
                if (vis is DataGridRow)
                {
                    //pobieramy dane z wiersza
                    var row = (DataGridRow)vis;
                    Car car = (Car)row.Item;
                    myCarBindingList.Remove(car);
                    myCars.Remove(car);
                    UpdateDataGrid();
                    break;
                }

        }

        private void ButtonSave(object sender, RoutedEventArgs e)
        {
            //szukamy zmian, zapisujemy je i odswiezamy UI
            CheckForNewItems();
            UpdateDataGrid();
        }

        private void UpdateDataGrid()
        {
            //Aktualizacja 
            carBindingSource.DataSource = myCarBindingList;
            dataGridView1.ItemsSource = carBindingSource;

        }

        private void CreateSearchComboBox()
        {

            //Tworzymy zawartość comoboboxa
            BindingList<string> list = new BindingList<string>();
            list.Add("model");
            list.Add("year");
            list.Add("motor.displacement");
            list.Add("motor.model");
            list.Add("motor.horsePower");
            comboBox.ItemsSource = list;
            comboBox.SelectedIndex = 0;
        }

        private static int CompareCarsPowers(Car car1, Car car2)
        {
            //porównanie mocy dwóch samochodów
            if (car1.motor.horsePower >= car2.motor.horsePower)
            {
                return 100;
            }
            else
            {
                return -100;
            }
        }

        private static bool IsTDI(Car car)
        {
            // sprawdzamy czy model silnika to TDI
            return car.motor.model.Equals("TDI");
        }

        private static void ShowMessageBox(Car car)
        {
            //wyświetlenie MB z informacjami o samochodzie
            string message = car.ToString();
            string caption = "Car";
            MessageBoxButtons buttons = MessageBoxButtons.OK;
            DialogResult result = System.Windows.Forms.MessageBox.Show(message, caption, buttons);
        }

        private void saveNewRow(object sender, DataGridRowEditEndingEventArgs e)
        {
            //zapisujemy nowy wiersz przy
            CheckForNewItems();
            Trace.WriteLine("Row edit");
            UpdateDataGrid();
        }
    }
}
