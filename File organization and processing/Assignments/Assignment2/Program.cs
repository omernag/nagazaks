using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Xml;

namespace assignment2
{
    class Program
    {


      
        static void Main(string[] args)
        {
            List<string> result = new List<string>();
            Assignment2 ass = new Assignment2();
            ass.Index("C:\\Users\\USER\\Desktop\\Employees_data.csv", "C:\\Users\\USER\\Desktop\\data.txt");
            XmlDocument xml = new XmlDocument();
            xml.Load("C:\\Users\\USER\\Desktop\\query1.xml");
            List<string> ss = ass.SelectVectors(xml, "C:\\Users\\USER\\Desktop\\data.txt");
            foreach(string s in ss)
            {
                Console.WriteLine(s);
            }
            string ans = ass.CreateOutputVector(xml, ss);
            Console.WriteLine(ans);
            result = ass.SelectRecords("C:\\Users\\USER\\Desktop\\Employees_data.csv", ans);
            foreach(string u in result)
            {
                Console.WriteLine(u);

            }
            Console.ReadLine();

        }



    }
}
