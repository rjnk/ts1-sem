# TbUIS-UIS

Defect free version UIS

## Building an application

To build a UIS application using Maven to WAR file that can be deployed on a web server with a servlet container (eg Tomcat), you can use the following Maven command in the root folder UIS project with POM file:

```
mvn clean install -DskipTests
```

After a successful build, the resulting WAR file will be located in the _target_ folder. The goal **-DskipTests** allows skipping tests that could end the assembly process when using beans with a deliberate error.

## Batch building error versions

To build many versions in batch you can use script `version_maker.cmd`. By default script expects at least one `seed.xml` in folder `version_seeds`. Script cleans project, perform rebuild and copy result into `release_archive/generated_versions`.
Result will be named same as script (e.g. `UIS-C0.H0.M0.L0_ALL_OK.xml` creates `UIS-C0.H0.M0.L0_ALL_OK.war` in output directory)
Script create backup of actual `seed.xml` in `seed.bck` if script fails, you coudl restore `seed.xml` manualy.

Script has some configuration fields:

- Name of output war from Maven process
- Seed input directory
- Output directory 

File `seed.xml` is configuration xml for Spring framework which declares used JavaBeans.

## Deployment of application
#### If you already have WAR file, you can continue with setting up a database - part: 2. a)
### 1. a) Running an application using the Maven plugin
The UIS application can be started using the Maven tomcat7 plugin, which allows manipulation of WAR projects within the built-in servlet container Tomcat version 7.x. To run a new application using the WAR file with clean assembly the following command can be used: 

```
mvn clean install -DskipTests tomcat7 :run -war
```

The goal _-DskipTests_ defines a test skip that could end the process assembly with intentionally inserted error version of bean.

### 1. b) Deploying UIS on Tomcat server

The assembled WAR application can be deployed on a web server Servlet with a container such as Tomcat. War file is on Tomcat server can be deployed in two ways. The first is to insert the created WAR file to a folder with other web applications and run the server (or restart). Tomcat browses the folder and any new or updated web WAR archives unpack and access the web application. The second option is so-called. _Hot Deployment_, which allows you to deploy a separate web application at runtime server. On Tomcat, you can use _Hot Deployment_ using _Tomcat Manager_.

### 2. a) Setting up a database

You must create a new database before running the UIS application for the first time, which application will use for persistence of data. To use this database UIS applications also need to set the user, password, database url (there are also other attributes) for the JDBC database connector in the file (path in WAR file) _WEB-INF/classes/META-INF/persistence.xml_.

Basic database settings:
- Name of database: 	**uis-web-db**
- Encoding: 	**UTF8_general_ci**
- Username: 	**uis-web**
- Password: 	**uis**

### 2. b) Change DB connection settings 

You can change DB connection settings before creating war file. You have to change connection properties in two files: **persistence.xml** and **application-context.xml**. 

### 3. Logging

Application creates default log file in runtime folder of program named **uis-log.txt**. This option can be changed by setting up environment variable named **UIS_LOG_FILE**. Whenever environment variable UIS_LOG_FILE is set up, application use value of this variable as path to log file (including filename). Ensure that folder is created before application starts. 

You can prove correct setting using program ```set``` in ```cmd```. Output should contains line similar to:

```
UIS_LOG_FILE=C:\xxx\log-file-name.txt
```
