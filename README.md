# EntityResolution
AngularJS+Spring+Hibernate

To clone the application in Eclipse :
File->Import->Git->Projects from Git->Clone URI->Enter uri as "https://github.com/am88tech/EntityResolution"->Select local storage and click next until finish.

Download the database backup/schema/images from  : "https://github.com/am88tech/EntityResolution-resources"

Install node. 

Save and unzip the file EntityResolution-img.zip to C drive. Shift+Right click to open command prompt window. Type "http-server -p 9090" to start the local server on which the images for the application would be available.



Components : 
UI(Frontend) :  
1)Developed using HTML, Bootstrap and AngularJS.
2)You can view the headlineof all article on the first page. Thereafter, on clicking Read More button, the details of the article is shown in a modal.You can search for an article using the search box at the top.You can also add new article by clicking Add New Article.Here you can enter the headline, author and description along with uploading an image.
3)On starting the application, index.html gets loaded. The resources folder inside webappcontains the required js and css files.Partials contain the required HTML files.


APIs :
1)Developed using Maven, Spring 4 and Hibernate.
2)Maven has been used for fast and easy application build. The dependencies are mentioned in pom.xml .Maven's dependency mechanism help to download all the necessary dependency libraries automatically.
3)I have followed the MVC pattern for development.  The request flows from front end to handler to controller to service to dao and daoimpl.
Various files and folders used:
1)web.xml : Dispatcher servlet setup
2)spring-config.xml : Bean setup
3)Controller : SportsController.java receives request via handler.
4)services: The service layer consists of business logic.
5)dao : consists of interface SportsDAO and SportsDAOImpl (here all the database related transactions occur).
6)dto : Data transfer object from controller to service and back from service to controller.
7)entity : mapping database table.
8)model : receiving request body
9)Response.java : Send response to front end in json format.
10)database.properties file: details for database connection.

Database : Postgresql 9.4. Used sequence for auto generation of id.

Tomcat :7.0.55
Java jdk : 1.8






