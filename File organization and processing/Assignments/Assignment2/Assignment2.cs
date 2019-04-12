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
            int numOfLines = 0;  //num of data lines (without headers)
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
                String var = table[i][1];    // can cause out of array
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



                for (int j = 0; j < vars.Count; j++)   //sumOfLineExmple: {name,omer,1,0,0,0,1,1,0}
                {
                    sumOfLine[0] = table[i][0];   //column header
                    sumOfLine[1] = vars[j];       //current distinct var
                    int place = 2;
                    for (int z = 1; z < table[0].Length; z++) //z - line number in the origin DB
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
            List<string> result = new List<string>();
            XmlNodeList xmlNodesList;
            string s_element;
            string s_value;
            StreamReader reader = new StreamReader(vectorFilePath);
            string line;
            string[] lineAsArray;
            string[] tmplineAsArray;

            if ((xmlNodesList= xmlDoc.SelectNodes("DB_EX2_QUERY/Logical_Operation/Query_Elements/Element")).Count>0 ) { }
            
            else if((xmlNodesList = xmlDoc.SelectNodes("DB_EX2_QUERY/Query_Elements/Element")).Count > 0) { }
           
            else
            {
                reader = new StreamReader(vectorFilePath);
                while ((line = reader.ReadLine()) != null)
                {
                    
                        result.Add(line);
                    
                }
                reader.Close();
                return result;
            }
            string r_line;
            foreach (XmlNode element in xmlNodesList)
            {
                s_element = element.Attributes["column_Name"].Value;
                s_value = element.InnerText;
                if (element.ChildNodes.Count > 1)
                {                    
                    r_line = "" + s_element + "," + s_value + ",";
                    reader = new StreamReader(vectorFilePath);
                    line = reader.ReadLine();
                    lineAsArray = line.Split(',');
                    while ((line = reader.ReadLine()) != null)
                    {
                        tmplineAsArray = line.Split(',');
                        if (tmplineAsArray[0] == s_element)
                        {
                            for (int i = 2; i < lineAsArray.Length; i++)
                            {
                                if (tmplineAsArray[i] == "1")
                                {
                                    lineAsArray[i] = "1";
                                }
                            }
                        }
                    }
                    for (int i = 2; i < lineAsArray.Length; i++)
                    {
                        r_line = r_line + lineAsArray[i];
                        if (i != lineAsArray.Length - 1)
                        {
                            r_line = r_line + ",";
                        }

                    }
                    result.Add(r_line);
                    reader.Close();
                }
                else
                {
                    reader = new StreamReader(vectorFilePath);
                    while ((line = reader.ReadLine()) != null)
                    {
                        lineAsArray = line.Split(',');
                        if (lineAsArray[0] == s_element && lineAsArray[1] == s_value)
                        {
                            result.Add(line);
                        }
                    }
                    reader.Close();
                }
            }
            return result;
        }


        public string CreateOutputVector(XmlDocument xmlDoc, List<string> vectors)
        {
            
            XmlNodeList xmlNodesList;
            string line = "";
            string[] lineAsArray = null;
            string[] tmplineAsArray;
            XmlNode op;
            if (vectors.Count > 0) {
                if ((op = xmlDoc.SelectSingleNode("DB_EX2_QUERY/Logical_Operation"))!=null) {
                    xmlNodesList = xmlDoc.SelectNodes("DB_EX2_QUERY/Query_Elements/Element");
                    if (op.InnerText == "AND")
                    {
                        lineAsArray = vectors[0].Split(',');
                        for (int i = 1; i < vectors.Count; i++)
                        {
                            tmplineAsArray = vectors[i].Split(',');
                            for (int j = 2; j < lineAsArray.Length; j++)
                            {
                                if (tmplineAsArray[j] == "0")
                                {
                                    lineAsArray[j] = "0";
                                }
                            }
                        }
                    }
                    else if (op.InnerText == "OR")
                    {
                        lineAsArray = vectors[0].Split(',');
                        for (int i = 1; i < vectors.Count; i++)
                        {
                            tmplineAsArray = vectors[i].Split(',');
                            for (int j = 2; j < lineAsArray.Length; j++)
                            {
                                if (tmplineAsArray[j] == "1")
                                {
                                    lineAsArray[j] = "1";
                                }
                            }
                        }
                    }
                    for (int i = 2; i < lineAsArray.Length; i++)
                    {
                        line = line + lineAsArray[i];
                        if (i != lineAsArray.Length - 1)
                        {
                            line = line + ",";
                        }

                    }

                }

      
                else if ((xmlNodesList = xmlDoc.SelectNodes("DB_EX2_QUERY/Query_Elements/Element")).Count == 1)
                {
                    if (vectors.Count == 1)
                    {
                        lineAsArray = vectors[0].Split(',');
                        for (int i = 2; i < lineAsArray.Length; i++)
                        {
                            line = line + lineAsArray[i];
                            if (i != lineAsArray.Length - 1)
                            {
                                line = line + ",";
                            }

                        }
                        
                    }
                }

            }

            return line;
        }

        public List<string> SelectRecords(string DBFilePath, string outputVector)
        {
			throw new NotImplementedException();
        }

    }
}
