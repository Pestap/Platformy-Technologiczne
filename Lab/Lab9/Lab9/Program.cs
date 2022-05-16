

using System.Xml.Linq;
using System.Xml.Serialization;
using System.Xml.XPath;

namespace Lab9
{
    public class Program
    {
        static void Main()
        {
            List<Car> myCars = new List<Car>(){
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




            //LINQ query
            Console.WriteLine("Query to anonymous type:");
            var query1 = from car in myCars
                         where car.model == "A6"
                         select new
                         {
                             engineType = String.Compare(car.motor.model, "TDI") == 0 ? "diesel" : "petrol",
                             hppl = car.motor.horsePower / car.motor.displacement
                         };

            foreach(var item in query1)
            {
                Console.WriteLine(item.engineType + " - " + item.hppl);
            }


            Console.WriteLine("Grouping query:");
            var query2 = from car in query1
                         group car by car.engineType;

            foreach (var item in query2)
            {
                Console.WriteLine("Engine type - {0}: {1}", item.Key, item.Average(engine => engine.hppl));
            }


            //Serialization

            Serialization(myCars);
            Console.WriteLine("Rezultat deserializacji:");
            var deserializationResult = Deserialization();
            foreach(var item in deserializationResult)
            {
                Console.WriteLine("{0} {1} {2}", item.model, item.year, item.motor.horsePower);
            }


            //XPath

            XPath();

            createXmlFromLinq(myCars);
            XHTML(myCars);

            ModifyXML();
        }
        private static void Serialization(List<Car> cars)
        {
            var file = "CarsCollection.xml";
            var currDir = Directory.GetCurrentDirectory();
            var filePath = Path.Combine(currDir, file);
            SerializeList(cars, filePath);

        }
        private static List<Car> Deserialization()
        {
            var file = "CarsCollection.xml";
            var currDir = Directory.GetCurrentDirectory();
            var filePath = Path.Combine(currDir, file);
            return DeserializeList(filePath);
        }
        private static void XPath()
        {
            XElement rootNode = XElement.Load("CarsCollection.xml");

            // Average HP
            double avgHP = (double)rootNode.XPathEvaluate("sum(//car/engine[not(@model = \"TDI\")]/horsePower) div count(//car/engine[not(@model =\"TDI\")]/horsePower)");
            Console.WriteLine("Average HP of no-TDI cars: {0}", avgHP);

            // No duplicates
            // wezmiemy tylko ostatnie wystapienie modelu
            IEnumerable<XElement> noDuplicateModels = rootNode.XPathSelectElements("//car[not(model = following-sibling::car/model)]");

            IEnumerable<XElement> justModelNames = noDuplicateModels.Elements("model");

            List<string> modelNamesString = new List<string>();
            Console.WriteLine("Modele samochodów występujące na liscie:");
            foreach(var elem in justModelNames)
            {
                modelNamesString.Add(elem.Value);
               
            }
            foreach (var elem in modelNamesString)
            {
                Console.WriteLine(elem);
            }

        }

        private static void createXmlFromLinq(List<Car> myCars)
        {
            IEnumerable<XElement> nodes = from car in myCars
                                          select
                       new XElement("car",
                            new XElement("model", car.model), 
                            new XElement("engine",
                                new XAttribute("model", car.motor.model),
                                new XElement("displacement", car.motor.displacement),
                                new XElement("horsePower", car.motor.horsePower)
                            ),
                            new XElement("year", car.year)
                        );//LINQ query expressions
            XElement rootNode = new XElement("cars", nodes); //create a root node to contain the query results
            rootNode.Save("CarsFromLinq.xml");
        }

        private static void XHTML(List<Car> myCars)
        {
            IEnumerable<XElement> nodes = from car in myCars
                                          select
                       new XElement("tr", new XAttribute("style", "border: 1px solid black"),
                             new XElement("td", new XAttribute("style", "border: 1px solid black"), car.model),
                             new XElement("td", new XAttribute("style", "border: 1px solid black"), car.motor.model),
                             new XElement("td", new XAttribute("style", "border: 1px solid black"), car.motor.displacement),
                             new XElement("td", new XAttribute("style", "border: 1px solid black"), car.motor.horsePower),
                             new XElement("td", new XAttribute("style", "border: 1px solid black"), car.year)
                         );
            nodes = nodes.Prepend(new XElement("tr", new XAttribute("style", "border: 1px solid black"),
                             new XElement("td", new XAttribute("style", "border: 1px solid black"), "Model"),
                             new XElement("td", new XAttribute("style", "border: 1px solid black"), "Silnik"),
                             new XElement("td", new XAttribute("style", "border: 1px solid black"), "Pojemnosc"),
                             new XElement("td", new XAttribute("style", "border: 1px solid black"), "Moc"),
                             new XElement("td", new XAttribute("style", "border: 1px solid black"), "Rok")
                         ));
            XElement table = new XElement("table", new XAttribute("style", "border: 1px solid black"), nodes);
            XElement template = XElement.Load("template.html");
            XElement body = template.Element("{http://www.w3.org/1999/xhtml}body");
            body.Add(table);
            template.Save("table.html");
        }

        private static void ModifyXML()
        {
            XElement collection = XElement.Load("CarsCollection.xml");
            foreach(XElement car in collection.Elements())
            {
                foreach(XElement xElement in car.Elements())
                {
                    if(xElement.Name == "engine")
                    {
                        foreach(XElement engineElement in xElement.Elements())
                        {
                            if(engineElement.Name == "horsePower")
                            {
                                engineElement.Name = "hp";
                            }
                        }
                    }
                    if(xElement.Name == "model")
                    {
                        var year = car.Element("year");
                        XAttribute attrToAdd = new XAttribute("year", year.Value);
                        xElement.Add(attrToAdd);
                        year.Remove();
                    }
                }
            }
            collection.Save("CarsCollectionModified.xml");
        }

        public static void SerializeList(List<Car> list, string file)
        {
            XmlSerializer serializer = new XmlSerializer(typeof(List<Car>), new XmlRootAttribute("cars"));

            using (TextWriter writer = new StreamWriter(file))
            {
                serializer.Serialize(writer, list);
            }

        }

        public static List<Car> DeserializeList(string file)
        {
            XmlSerializer serializer = new XmlSerializer(typeof(List<Car>), new XmlRootAttribute("cars"));
            List<Car> result = new List<Car>();

            using (Stream reader = new FileStream(file, FileMode.Open))
            {
                result = (List<Car>)serializer.Deserialize(reader);
            }
            return result;

        }
    }
}