
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace Lab9
{
    [XmlType(TypeName = "engine")]
    public class Engine
    {
        public double displacement;
        public double horsePower;
        [XmlAttribute]
        public string model;

        public Engine() { }
        public Engine(double diplacement, double horsePower, string model)
        {
            this.displacement = diplacement;
            this.horsePower = horsePower;
            this.model = model;
        }
    }
}
