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
            Assignment2 ass = new Assignment2();
            ass.Index("C:\\Users\\onagar\\Desktop\\bgu\\nagazaks\\File organization and processing\\Assignments\\Assignment2\\Employees_data.csv", "C:\\Users\\onagar\\Desktop\\New folder\\data.txt");
            XmlDocument xml = new XmlDocument();
            xml.Load("C:\\Users\\onagar\\Desktop\\New folder\\query.xml");
            List<string> ss = ass.SelectVectors(xml, "C:\\Users\\onagar\\Desktop\\New folder\\data.txt");
            foreach(string s in ss)
            {
                Console.WriteLine(s);
            }
            Console.ReadLine();
            }


    }
}
