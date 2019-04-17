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
            string currline;
            int numOfLines = 0;  //num of data lines (without headers)
            string[][] table = new string[cols.Length - 1][];

            while ((currline=reader.ReadLine()) != null && currline != ",,,,,,,,,")

            {
                numOfLines++;
            }

            reader.Close();

            for (int i = 0; i < cols.Length - 1; i++)
            {
                table[i] = new string[numOfLines + 1];
            }

            int rows = 0;
            
            reader = new StreamReader(DBFilePath);
            while ((currline = reader.ReadLine()) != null && currline != ",,,,,,,,,")
            {

                line = currline.Split(',');
                for (int i = 0; i < line.Length - 1; i++)
                {
                    table[i][rows] = line[i + 1];
                }
                rows++;
            }
            reader.Close();
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
                        if (z == 0||z==1)
                            destFile.Write(",");
                    }
                    destFile.WriteLine("");

                }


            }
            
            destFile.Close();
           

        }

        public List<string> SelectVectors(XmlDocument xmlDoc, string vectorFilePath)
        {
            List<string> result = new List<string>();
            XmlNodeList xmlNodesList;
            string s_element;
            string s_value;
            StreamReader reader;// = new StreamReader(vectorFilePath);
            string line;
            string[] lineAsArray;
            string[] tmplineAsArray;
            char[] bitAsArray;
            char[] tmpBitAsArray;
            string r_line;
           if((xmlNodesList = xmlDoc.SelectNodes("DB_EX2_QUERY/Query_Elements/Element")).Count > 0) { }
           
            else
            {
                reader = new StreamReader(vectorFilePath);
                while ((line = reader.ReadLine()) != null && line != ",,,,,,,,," )
                {
                    
                    lineAsArray = line.Split(',');
                    result.Add(lineAsArray[2]);                    
                }
                reader.Close();
                return result;
            }

            List<string> values;
            foreach (XmlNode element in xmlNodesList)
            {
                s_element = element.Attributes["column_Name"].Value;
                s_value = element.InnerText;
                if (element.ChildNodes.Count > 1)
                {
                    values = new List<string>();
                    foreach (XmlNode value in element.ChildNodes) {
                        values.Add(value.InnerText);
                    }
                    
                    r_line = "";
                    reader = new StreamReader(vectorFilePath);
                    line = reader.ReadLine();
                    lineAsArray = line.Split(',');
                    bitAsArray = lineAsArray[2].ToCharArray();
                    while ((line = reader.ReadLine()) != null && line != ",,,,,,,,,")
                    {
                        tmplineAsArray = line.Split(',');
                        tmpBitAsArray = tmplineAsArray[2].ToCharArray();
                        if (tmplineAsArray[0] == s_element && values.Contains(tmplineAsArray[1]))
                        {
                            for (int i = 0; i < bitAsArray.Length; i++)
                            {
                                if (tmpBitAsArray[i] == '1')
                                {
                                    bitAsArray[i] = '1';
                                }
                            }
                        }
                    }
                    for (int i = 0; i < bitAsArray.Length; i++)
                    {
                        r_line = r_line+ bitAsArray[i];
                        
                    }
                    result.Add(r_line);
                    reader.Close();
                }
                else
                {
                    r_line = "";
                    reader = new StreamReader(vectorFilePath);
                    while ((line = reader.ReadLine()) != null && line != ",,,,,,,,,")
                    {
                        lineAsArray = line.Split(',');
                        bitAsArray = lineAsArray[2].ToCharArray();
                        if (lineAsArray[0] == s_element && lineAsArray[1]==s_value)
                        {
                            for (int i = 0; i < bitAsArray.Length; i++)
                            {
                                r_line = r_line + bitAsArray[i];

                            }
                            result.Add(r_line);
                        }
                    }
                    reader.Close();
                }
            }
            //reader.Close();
            return result;
        }


        public string CreateOutputVector(XmlDocument xmlDoc, List<string> vectors)
        {
            
            XmlNodeList xmlNodesList;
            string line = "";
            char[] lineAsArray = null;
            char[] tmplineAsArray;
            XmlNode op;
            if (vectors.Count > 0) {
                if ((op = xmlDoc.SelectSingleNode("DB_EX2_QUERY/Logical_Operation"))!=null) {
                    xmlNodesList = xmlDoc.SelectNodes("DB_EX2_QUERY/Query_Elements/Element");
                    if (op.InnerText == "AND")
                    {
                        lineAsArray = vectors[0].ToCharArray();
                        for (int i = 0; i < vectors.Count; i++)
                        {
                            tmplineAsArray = vectors[i].ToCharArray();
                            for (int j = 0; j < lineAsArray.Length; j++)
                            {
                                if (tmplineAsArray[j] == '0')
                                {
                                    lineAsArray[j] = '0';
                                }
                            }
                        }
                    }
                    else if (op.InnerText == "OR")
                    {
                        lineAsArray = vectors[0].ToCharArray();
                        for (int i = 0; i < vectors.Count; i++)
                        {
                            tmplineAsArray = vectors[i].ToCharArray();
                            for (int j = 0; j < lineAsArray.Length; j++)
                            {
                                if (tmplineAsArray[j] == '1')
                                {
                                    lineAsArray[j] = '1';
                                }
                            }
                        }
                    }
                    for (int i = 0; i < lineAsArray.Length; i++)
                    {
                        line = line + lineAsArray[i];

                    }

                }

      
                else if ((xmlNodesList = xmlDoc.SelectNodes("DB_EX2_QUERY/Query_Elements/Element")).Count == 1)
                {
                    if (vectors.Count == 1)
                    {
                        line = vectors[0];
                                             
                    }
                }

                else
                {
                    lineAsArray = vectors[0].ToCharArray();
                    for (int i = 1; i < vectors.Count; i++)
                    {
                        tmplineAsArray = vectors[i].ToCharArray();
                        for (int j = 0; j < lineAsArray.Length; j++)
                        {
                            if (tmplineAsArray[j] == '1')
                            {
                                lineAsArray[j] = '1';
                            }
                        }
                    }
                    for (int i = 0; i < lineAsArray.Length; i++)
                    {
                        line = line + lineAsArray[i];

                    }
                }

            }

            return line;
        }

        public List<string> SelectRecords(string DBFilePath, string outputVector)
        {
            string id;
            List<string> result = new List<string>();
            StreamReader reader = new StreamReader(DBFilePath);
            String line = reader.ReadLine();
            String [] lineAsArray;
            char[] s_vector = outputVector.ToCharArray();
            
            for (int bit = 0; bit< s_vector.Length;bit++) {
                line = reader.ReadLine();
                lineAsArray = line.Split(',');
                id = lineAsArray[0];
                if (s_vector[bit] == '1')
                {
                    
                    result.Add(id);
                }
                
                

            }
            reader.Close();
            return result;
        }

    }
}
