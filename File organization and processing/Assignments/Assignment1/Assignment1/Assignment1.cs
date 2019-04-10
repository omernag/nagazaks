using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml;
using System.Xml.XPath;

namespace ConsoleApplication1
{
    class Assignment1
    {

        //queries
        public XmlNodeList Query1 (XmlDocument xmlDoc)// returns all the movies
        {
            XmlNodeList xmlNodesList = xmlDoc.SelectNodes("Netflix/movies/movie");
            return xmlNodesList;
        }

        public XmlNodeList Query2(XmlDocument xmlDoc)// returns all the  movies after 2014
        {
            XmlNodeList xmlNodesList = xmlDoc.SelectNodes("Netflix/movies/movie[year >= 2014]");
            return xmlNodesList;
        }

        public XmlNodeList Query3(XmlDocument xmlDoc, String actorFirstName, String actorLastName)// returns all the awards of all TV-shows of an actor
        {
            XmlNodeList xmlNodesList = xmlDoc.SelectNodes("Netflix/TV-shows/TV-show/actors/actor[first-name = '" +actorFirstName+ "' and last-name = '" + actorLastName +"']/awards/award");
            return xmlNodesList;
        }
        public XmlNodeList Query4(XmlDocument xmlDoc)// returns all the TV-shows with more than one seasons
        {
            XmlNodeList xmlNodesList = xmlDoc.SelectNodes("Netflix/TV-shows/TV-show[count(seasons/season)>1]");
            return xmlNodesList;
        }
        public int Query5(XmlDocument xmlDoc, String genre)// retuens the amount of movies in the genre
        {
            XmlNodeList xmlNodesList = xmlDoc.SelectNodes("Netflix/movies/movie[genre = '" + genre +"']");
            return xmlNodesList.Count;
        }
        public int Query6(XmlDocument xmlDoc, String yearOfBirth, int amountOfAwards)// returns the amount of different actors that were born after the year and that have more than the award amount in one movie or one TV-show
        {
            XmlNodeList xmlTVactors = xmlDoc.SelectNodes("Netflix/movies/movie/actors/actor[year-of-birth>'" +int.Parse(yearOfBirth) + "' and count(awards/award) > '"+amountOfAwards+"']");
            XmlNodeList xmlMactors = xmlDoc.SelectNodes("Netflix/TV-shows/TV-show/actors/actor[year-of-birth>'" + int.Parse(yearOfBirth) + "' and count(awards/award) > '" + amountOfAwards + "']");
            int sum = (xmlTVactors.Count + xmlMactors.Count);
            int ans = 0;
            for (int i = 0; i< xmlTVactors.Count; i++)
            {
                bool dist = true;
                for(int j = i+1; j < xmlTVactors.Count; j++)
                {
                    if(xmlTVactors[i].ChildNodes[0].InnerText== xmlTVactors[j].ChildNodes[0].InnerText && xmlTVactors[i].ChildNodes[1].InnerText == xmlTVactors[j].ChildNodes[1].InnerText)
                    {
                        dist = false;
                    }
                }
                if (dist)
                {
                    ans++;
                }
            }
            for (int i = 0; i < xmlMactors.Count; i++)
            {
                bool dist = true;
                for (int j = i + 1; j < xmlMactors.Count; j++)
                {
                    if (xmlMactors[i].ChildNodes[0].InnerText == xmlMactors[j].ChildNodes[0].InnerText && xmlMactors[i].ChildNodes[1].InnerText == xmlMactors[j].ChildNodes[1].InnerText)
                    {
                        dist = false;
                    }
                }
                if (dist == true)
                {
                    ans++;
                }
            }
            for (int i = 0; i < xmlTVactors.Count; i++)
            {
                bool dist = true;
                for (int j = 0; j < xmlMactors.Count; j++)
                {
                    if (xmlTVactors[i].ChildNodes[0].InnerText == xmlMactors[j].ChildNodes[0].InnerText && xmlTVactors[i].ChildNodes[1].InnerText == xmlMactors[j].ChildNodes[1].InnerText)
                    {
                        dist = false;
                    }
                }
                if (dist == false)
                {
                    ans--;
                }
            }
            return ans;
        }
        public XmlNodeList Query7(XmlDocument xmlDoc, int amountOfEpisodes)// returns the TV-shows that have more than the amount of epidods in all its seasons
        {
            XmlNodeList xmlNodesList = xmlDoc.SelectNodes("Netflix/TV-shows/TV-show[sum(seasons/season/episodes)>'"+amountOfEpisodes+"']");
            return xmlNodesList;
        }

        //insertions
        public void InsertTVShow(XmlDocument xmlDoc, String name, String genre, String year)
        {
            XmlElement e_show = xmlDoc.CreateElement("TV-show");
            XmlElement e_name = CreateNewXmlElement(xmlDoc, "name", name);
            XmlElement e_genre = CreateNewXmlElement(xmlDoc, "genre", genre);
            XmlElement e_year = CreateNewXmlElement(xmlDoc, "year", year);
            XmlNode xmlNode = xmlDoc.SelectSingleNode("Netflix/TV-shows");
            xmlNode.AppendChild(e_show);
            xmlNode = xmlNode.LastChild;
            xmlNode.AppendChild(e_name);
            xmlNode.AppendChild(e_genre);
            xmlNode.AppendChild(e_year);
        }



        public void InsertMovie(XmlDocument xmlDoc, String name, String genre, String year)
        {
            XmlElement e_movie = xmlDoc.CreateElement("movie");
            XmlElement e_name = CreateNewXmlElement(xmlDoc, "name",name);
            XmlElement e_genre = CreateNewXmlElement(xmlDoc, "genre", genre);
            XmlElement e_year = CreateNewXmlElement(xmlDoc, "year", year);
            XmlNode xmlNode = xmlDoc.SelectSingleNode("Netflix/movies");
            xmlNode.AppendChild(e_movie);
            xmlNode = xmlNode.LastChild;
            xmlNode.AppendChild(e_name);
            xmlNode.AppendChild(e_genre);
            xmlNode.AppendChild(e_year);

        }

        public void InsertActorToMovie(XmlDocument xmlDoc, String movieName, String actorFirstName, String actorLastName,
            String actorBirthYear)
        {

            XmlElement e_actor = xmlDoc.CreateElement("actor");

            XmlElement e_actorFirstName = CreateNewXmlElement(xmlDoc, "first-name", actorFirstName);
            XmlElement e_actorLastName = CreateNewXmlElement(xmlDoc, "last-name", actorLastName);
            XmlElement e_actorBirthYear = CreateNewXmlElement(xmlDoc, "year-of-birth", actorBirthYear);

            XmlNode xmlNode = xmlDoc.SelectSingleNode("Netflix/movies/movie[name ='" +movieName+"']/actors");
            if (xmlNode == null)
            {
                xmlNode = xmlDoc.SelectSingleNode("Netflix/movies/movie[name ='" + movieName + "']");
                XmlElement e_actors = xmlDoc.CreateElement("actors");
                xmlNode.AppendChild(e_actors);
                xmlNode = xmlNode.LastChild;
            }
            xmlNode.AppendChild(e_actor);
            xmlNode = xmlNode.LastChild;
            xmlNode.AppendChild(e_actorFirstName);
            xmlNode.AppendChild(e_actorLastName);
            xmlNode.AppendChild(e_actorBirthYear);

        }

        public void InsertActorToTVShow(XmlDocument xmlDoc, String showName, String actorFirstName, String actorLastName,
    String actorBirthYear)
        {
            XmlElement e_actor = xmlDoc.CreateElement("actor");

            XmlElement e_actorFirstName = CreateNewXmlElement(xmlDoc, "first-name", actorFirstName);
            XmlElement e_actorLastName = CreateNewXmlElement(xmlDoc, "last-name", actorLastName);
            XmlElement e_actorBirthYear = CreateNewXmlElement(xmlDoc, "year-of-birth", actorBirthYear);

            XmlNode xmlNode = xmlDoc.SelectSingleNode("Netflix/TV-shows/TV-show[name ='" + showName + "']/actors");
            if(xmlNode==null)
            {
                xmlNode = xmlDoc.SelectSingleNode("Netflix/TV-shows/TV-show[name ='" + showName + "']");
                if (xmlNode == null)
                {
                    return ;
                }
                XmlElement e_actors = xmlDoc.CreateElement("actors");
                xmlNode.AppendChild(e_actors);
                xmlNode = xmlNode.LastChild;
            }
            xmlNode.AppendChild(e_actor);
            xmlNode = xmlNode.LastChild;
            xmlNode.AppendChild(e_actorFirstName);
            xmlNode.AppendChild(e_actorLastName);
            xmlNode.AppendChild(e_actorBirthYear);
        }

        public void InsertSeasonToTVShow(XmlDocument xmlDoc, String showName, String numberOfEpisodes)
        {
            XmlElement e_season = xmlDoc.CreateElement("season");

            XmlElement e_episodes = CreateNewXmlElement(xmlDoc, "episodes", numberOfEpisodes);
            

            XmlNode xmlNode = xmlDoc.SelectSingleNode("Netflix/TV-shows/TV-show[name ='" + showName + "']/seasons");
            if (xmlNode == null)
            {
                xmlNode = xmlDoc.SelectSingleNode("Netflix/TV-shows/TV-show[name ='" + showName + "']");
                XmlElement e_seasons = xmlDoc.CreateElement("seasons");
                xmlNode.AppendChild(e_seasons);
                xmlNode = xmlNode.LastChild;
            }
            xmlNode.AppendChild(e_season);
            xmlNode = xmlNode.LastChild;
            xmlNode.AppendChild(e_episodes);
        }

        public void InsertAwardToActorInMovie(XmlDocument xmlDoc, String actorFirstName, String actorLastName,String movieName ,String awardCategory, String yearOfWinning)
        {
            XmlElement e_award = xmlDoc.CreateElement("award");

            XmlElement e_awardCategory = CreateNewXmlElement(xmlDoc, "category", awardCategory);
            XmlElement e_yearOfWinning = CreateNewXmlElement(xmlDoc, "year", yearOfWinning);

            XmlNode xmlNode = xmlDoc.SelectSingleNode("Netflix/movies/movie[name ='" + movieName + "']/actors/actor[first-name ='" + actorFirstName + "' and last-name ='" + actorLastName + "']/awards");
            if (xmlNode == null)
            {
                xmlNode = xmlDoc.SelectSingleNode("Netflix/movies/movie[name ='" + movieName + "']/actors/actor[first-name ='" + actorFirstName + "' and last-name ='" + actorLastName + "']");
                XmlElement e_awards = xmlDoc.CreateElement("awards");
                xmlNode.AppendChild(e_awards);
                xmlNode = xmlNode.LastChild;
            }
            xmlNode.AppendChild(e_award);
            xmlNode = xmlNode.LastChild;
            xmlNode.AppendChild(e_awardCategory);
            xmlNode.AppendChild(e_yearOfWinning);
            

        }

        public void InsertAwardToActorInTVShow(XmlDocument xmlDoc, String actorFirstName, String actorLastName, String showName, String awardCategory, String yearOfWinning)
        {
            XmlElement e_award = xmlDoc.CreateElement("award");

            XmlElement e_awardCategory = CreateNewXmlElement(xmlDoc, "category", awardCategory);
            XmlElement e_yearOfWinning = CreateNewXmlElement(xmlDoc, "year", yearOfWinning);

            XmlNode xmlNode = xmlDoc.SelectSingleNode("Netflix/TV-shows/TV-show[name ='" + showName + "']/actors/actor[first-name ='" + actorFirstName + "' and last-name ='" + actorLastName + "']/awards");
            if (xmlNode == null)
            {
                xmlNode = xmlDoc.SelectSingleNode("Netflix/TV-shows/TV-show[name ='" + showName + "']/actors/actor[first-name ='" + actorFirstName + "' and last-name ='" + actorLastName + "']");
                XmlElement e_awards = xmlDoc.CreateElement("awards");
                xmlNode.AppendChild(e_awards);
                xmlNode = xmlNode.LastChild;
            }
            xmlNode.AppendChild(e_award);
            xmlNode = xmlNode.LastChild;
            xmlNode.AppendChild(e_awardCategory);
            xmlNode.AppendChild(e_yearOfWinning);

        }

        private XmlElement CreateNewXmlElement(XmlDocument xmlDoc, string elemName, string elemValue)
        {
            XmlElement newXmlElem = xmlDoc.CreateElement(elemName);
            newXmlElem.InnerText = elemValue;
            return newXmlElem;
        }

        private void exampleOfCreateXML()
        {
            XmlDocument xmlDoc = new XmlDocument();
            xmlDoc.LoadXml("<DataBaseImplementationCourse/>");//insert your XML file path here
            XmlElement newXmlElem = xmlDoc.CreateElement("Lecturer");
            newXmlElem.InnerText = "Dr. Robert Moskovitch";
            xmlDoc.FirstChild.AppendChild(newXmlElem);
            newXmlElem = xmlDoc.CreateElement("TeachingAssistants");
            xmlDoc.FirstChild.AppendChild(newXmlElem);
            XmlNode tempXmlNode = newXmlElem;
            newXmlElem = CreateNewXmlElement(xmlDoc, "TeachingAssistant", "TeachingAssistant");
            newXmlElem.InnerText = "Guy Shitrit";
            tempXmlNode.AppendChild(newXmlElem);
            newXmlElem = CreateNewXmlElement(xmlDoc, "TeachingAssistant", "Ofir Dvir");
            tempXmlNode.AppendChild(newXmlElem);

            XmlNode xmlNode = xmlDoc.SelectSingleNode("DataBaseImplementationCourse/TeachingAssistants");
            XmlNodeList xmlNodesList = xmlDoc.SelectNodes("DataBaseImplementationCourse/TeachingAssistants/TeachingAssistant");
        }


        }
    }
