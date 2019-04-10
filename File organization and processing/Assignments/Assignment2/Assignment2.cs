using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml;

namespace assignment2
{
    class Assignment2
    {
        public void Index(string DBFilePath, string vectorFilePath)
        {
            StreamReader reader = new StreamReader(DBFilePath);

            String[] cols = reader.ReadLine().Split(',');
            String[] line;
            int numOfLines = 0;
            string[][] table = new string[cols.Length - 1][];

            while (!reader.EndOfStream)
            {
                reader.ReadLine();
                numOfLines++;
            }

            for (int i = 0; i < cols.Length - 1; i++)
            {
                table[i] = new string[numOfLines + 1];
            }

            int rows = 0;
            reader.Close();
            reader = new StreamReader(DBFilePath);
            while (!reader.EndOfStream)
            {

                line = reader.ReadLine().Split(',');
                for (int i = 0; i < line.Length - 1; i++)
                {
                    table[i][rows] = line[i + 1];
                }
                rows++;
            }
            String[] sumOfLine = new string[numOfLines + 3];

            StreamWriter destFile = new StreamWriter(vectorFilePath);
            for (int i = 0; i < table.Length; i++)
            {

                int numOfVars = 1;
                String var = table[i][1];
                List<string> vars = new List<string>();
                vars.Add(var);
                for (int j = 1; j < table[0].Length; j++)
                {
                    if (!vars.Contains(table[i][j]))
                    {
                        numOfVars++;
                        vars.Add(table[i][j]);
                    }


                }



                for (int j = 0; j < vars.Count; j++)
                {
                    sumOfLine[0] = table[i][0];
                    sumOfLine[1] = vars[j];
                    int place = 2;
                    for (int z = 0; z < table[0].Length; z++)
                    {
                        if (table[i][z].Equals(vars[j]))
                        {
                            sumOfLine[place] = "1";
                        }
                        else sumOfLine[place] = "0";
                        place++;

                    }
                    for (int z = 0; z < place; z++)
                    {
                        destFile.Write(sumOfLine[z]);
                        if (z != place - 1)
                            destFile.Write(",");
                    }
                    destFile.WriteLine("");

                }


            }
            destFile.Close();
            reader.Close();

        }

        public List<string> SelectVectors(XmlDocument xmlDoc, string vectorFilePath)
        {
			throw new NotImplementedException();
        }


        public string CreateOutputVector(XmlDocument xmlDoc, List<string> vectors)
        {
            throw new NotImplementedException();
        }

        public List<string> SelectRecords(string DBFilePath, string outputVector)
        {
			throw new NotImplementedException();
        }

    }
}
