﻿using System;
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
            ass.Index("C:\\Users\\USER\\Desktop\\Employees_data.csv", "C:\\Users\\USER\\Desktop\\data.txt");

        }


    }
}