using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace Lab9
{
    [XmlType(TypeName = "car")]
    public class Car
    {
        public string model;
        public int year;
        [XmlElement(ElementName = "engine")]
        public Engine motor;

        public Car() { }
        public Car(string model,Engine engine, int year)
        {
            this.model = model; 
            this.year = year;
            this.motor = engine;
        }
    }
}
