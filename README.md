# backend
backend application

Setting up development environment
1. Install Maven
2. Install Node.js
3. Install Ruby
4. Install/configure MySQL and create a database called "pace".
5. Open a Maven config file located in {YOUR_HOME_DIRECTORY}/.m2/settings.xml.
6. Add a profile to the profiles section and configure access to MySQL, it should look like
this:
<profiles>
<profile>
<id>pace-dev</id>
<activation>
<activeByDefault>true</activeByDefault>
</activation>
<properties>
<jdbc.url>jdbc:mysql://localhost:3306/pace</jdbc.url>
<jdbc.username>root</jdbc.username>
<jdbc.password></jdbc.password>
<xsrf.protection>false</xsrf.protection>
</properties>
</profile>
</profiles>

7. Create a root folder for the project, Here we use "pace" as the root folder:
mkdir pace
cd pace

8. Checkout the backend project:
git clone http://dev-server/trunk/pace-project

9. Checkout the frontend project:
git clone http://dev-server/trunk/pace-frontend

10. The folder structure should look like this:
|--pace
|-----pace-project
|-----pace-frontend

11. Build the backend project:
cd pace-project
mvn clean install -P dev

12. Launch the backend app:
cd pace-backend
mvn tomcat7:run-war-only

13. Check the Tomcat log file to see if there was any error during startup. The log is located
in /pace-project/pace-backend/target/tomcat/logs/pace-backend.log
