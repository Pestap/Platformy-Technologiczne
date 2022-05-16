
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace Lab10
{
    public class Engine : IComparable
    {
        public double displacement { get; set; }

        public double horsePower { get; set; }

        public string model { get; set; }

        public Engine() {
            Trace.WriteLine("NEW ENGINE CREATED");
        }
        public Engine(double diplacement, double horsePower, string model)
        {
            this.displacement = diplacement;
            this.horsePower = horsePower;
            this.model = model;
        }
        public override string ToString()
        {
            return $"{model}, {displacement}l, {horsePower} HP";
        }

        public int CompareTo(object obj)
        {
            Engine other = (Engine)obj;
            
            if (horsePower.CompareTo(other.horsePower) != 0)
            {
                return horsePower.CompareTo(other.horsePower);
            }
            else if (displacement.CompareTo(other.displacement) != 0)
            {
                return displacement.CompareTo(other.displacement);
            }
            return model.CompareTo(other.model);
        }
    }
}
