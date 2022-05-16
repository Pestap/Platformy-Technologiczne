
using Lab10;
using System;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

namespace laboratorium_10
{
    class CarBindingList : BindingList<Car>
    {

       //indeksy o konkrenej wartości danej właściwości
        private ArrayList selectedIndices;

        private PropertyDescriptor sortPropertyValue;
        private ListSortDirection sortDirectionValue;
        private bool isSortedValue = false;

        public CarBindingList(List<Car> list)
        {
            //dodajemy wszytkie elementy listy do CarBidningList
            
            if (list != null)
            {
                foreach (var car in list)
                {
                    Add(car);
                }
            }

        }

        protected override bool SupportsSearchingCore
        {
            get { return true; }
        }


        protected override bool SupportsSortingCore
        {
            get { return true; }
        }

        protected override PropertyDescriptor SortPropertyCore
        {
            get { return sortPropertyValue; }
        }

        protected override bool IsSortedCore
        {
            get { return isSortedValue; }
        }


        protected override void ApplySortCore(PropertyDescriptor prop, ListSortDirection direction)
        {
            var sortedList = new ArrayList();
            var unsortedList = new ArrayList(Count);

            if (prop.PropertyType.GetInterface("IComparable") != null) // sprawdzenie czy możemy sortować po tym property
            {
                sortPropertyValue = prop;
                sortDirectionValue = direction;

                //znajdujemy wszystkie wartości danego property
                foreach (Car car in Items)
                {
                    if (!sortedList.Contains(prop.GetValue(car)))
                    {
                        sortedList.Add(prop.GetValue(car));
                    }

                }
                //sortujemy listę wartośco property
                sortedList.Sort();

                //odwracamy jeżeli taka potrzebaa
                if (direction == ListSortDirection.Descending)
                {
                    sortedList.Reverse();
                }

                for (int i = 0; i < sortedList.Count; i++)
                {
                    //szukamy indeksów obiektów zawierających dane property o określonej wartości
                    var foundIndices = FindIndices(prop.Name, sortedList[i]);
                    if (foundIndices != null)
                    {
                        //iterujemy po indeksach - posorotwnych po danym atrybucie
                        foreach (var idx in foundIndices)
                        {
                            //dodajemy do listy wynikowej - elementy typu Car
                            unsortedList.Add(Items[idx]);
                        }
                    }
                }

                if (unsortedList != null)
                {
                    Clear();
                    //Czyscimy BindingList
                    //Dodajemy elementy w posortowanej kolejności
                    foreach (Car elem in unsortedList)
                    {
                        Add(elem);
                    }
                    isSortedValue = true;
                    OnListChanged(new ListChangedEventArgs(ListChangedType.Reset, -1));
                }


            }
        }

        public void Sort(string property, ListSortDirection direction)
        {
            PropertyDescriptorCollection properties = TypeDescriptor.GetProperties(typeof(Car));
            PropertyDescriptor prop = properties.Find(property, true);

            //znajdujemy property i po nim sortuejmy
            if (prop != null)
            {
                ApplySortCore(prop, direction);
            }
            else
            {
                throw new NotSupportedException($"Cannot sort by {prop.Name}, this property doesn\'t exist.");
            }
        }

        private int FindCore(PropertyDescriptor prop, object key, bool isEngine)
        {
            PropertyInfo propInfo;
            //sprawdzamy czy engine czy car
            if (isEngine)
            {
                propInfo = typeof(Engine).GetProperty(prop.Name);
            }
            else
            {
                propInfo = typeof(Car).GetProperty(prop.Name);
            }


            selectedIndices = new ArrayList(); // incijalizcja tablicy indeksów
            int found = 0;

            if (key != null)
            {
                //przeszukumey BindingList
                for (int i = 0; i < Count; i++)
                {
                    if (isEngine)
                    {
                        double neverused;
                        //sprawdzamy dla wartości Double
                        if (Double.TryParse(key.ToString(), out neverused))
                        {
                            if (propInfo.GetValue(Items[i].motor, null).Equals(Double.Parse(key.ToString())))
                            {
                                found++;
                                selectedIndices.Add(i);
                            }
                        }
                        else
                        {   
                            //Dla wartosci String
                            if (propInfo.GetValue(Items[i].motor, null).Equals(key))
                            {
                                found++;
                                selectedIndices.Add(i);
                            }
                        }

                    }
                    else
                    {
                        //sprawdzamy czy wartośc danego propery dla samochodu jest równa tej której szukamy
                        if (propInfo.GetValue(Items[i], null).Equals(key))
                        {
                            //dodajemy do listy znalezionych indeksów i inkrementujemy found
                            found++;
                            selectedIndices.Add(i);
                        }
                    }
                }
            }
            //zwracamy liczbe znalezionych indeksów
            return found;
        }

        public int[] FindIndices(string property, object key)
        {
            PropertyDescriptorCollection properties;
            bool isEngine = property.Contains("motor.");
            //sprawdzamy czy dane property dotyczy engine czy car
            if (isEngine)
            {
                //jeżeli engine to pobiermy jego nazwę po kropce
                properties = TypeDescriptor.GetProperties(typeof(Engine));
                property = property.Split('.').Last();
            }
            else
            {
                properties = TypeDescriptor.GetProperties(typeof(Car));
            }

            //znajdujemy odpowiednie property
            PropertyDescriptor prop = properties.Find(property, true);

            if (prop != null)
            {
                // prop - właściwość
                // key - wartość właściwości
                // isEngine - właściwość Car/Engine
                if (FindCore(prop, key, isEngine) > 0)
                { 
                    //jeżeli znalezliśmu jakieś elemtny posiadające daną wartośc właściwości to zwracamy tablicę indeksów
                    return (int[])(selectedIndices.ToArray(typeof(int)));
                }
            }
            return null;

        }

        public List<Car> FindCars(string property, object key)
        {
            List<Car> listOfMatchingCars = new List<Car>();
            //tablica indeksów samochodów o podanej właściwosći
            var indices = FindIndices(property, key);
            if (indices != null)
            {
                //Dodajemy samochody z indeksów do BindingList

                foreach (var idx in indices)
                {
                    listOfMatchingCars.Add(Items[idx]);
                }

                //zwracamy wynik wyszukiwania
                return listOfMatchingCars;
            }
            else return null;
        }


    }

}

