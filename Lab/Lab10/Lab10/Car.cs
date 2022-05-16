using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace Lab10
{
    
    public class Car
    {
        public string model { get; set; }
        public int year { get; set; }
        public Engine motor { get; set; }

        public Car() {
            this.model = "";
            this.motor = new Engine();
            this.year = 0;
        }
        public Car(string model,Engine engine, int year)
        {
            this.model = model; 
            this.year = year;
            this.motor = engine;
        }
        public override string ToString()
        {
            return $"Model: {model}, Year: {year}, Engine: {motor}";
        }
    }
}
