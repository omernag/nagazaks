using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Xml;

namespace ConsoleApplication1
{
    class Program
    {

        static void Main(string[] args)
        {
            Assignment1 ass = new Assignment1();
            XmlDocument xml = new XmlDocument();
           xml.Load("C:\\Users\\onagar\\Desktop\\netflix2.xml");
            //ass.InsertTVShow(xml, "baladfadsba", "oded", "amaniac");
            // ass.InsertActorToTVShow(xml, "baadsba", "odedsasa", "odedsasafsddfs", "1993");
            //ass.InsertSeasonToTVShow(xml, "balaba", "11");
            //  ass.InsertAwardToActorInTVShow(xml, "peleg", "bijon", "balaba", "player of gold", "1994");
           int k= ass.Query6(xml, "1111",2);
           xml.Save("C:\\Users\\onagar\\Desktop\\netflix2.xml");
            Console.ReadLine();
        }

    }
}
