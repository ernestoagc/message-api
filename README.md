# FORM API

#### **1.  Image docker**
I build the image docker: docker build -t ernestoagc/message-api:0.1 .

also you can download these version on 
docker pull ernestoagc/message-api:0.1


#### **2. Running backend application**
we going to use 2 docker container for up the application. The first container is for the mysql service and the second one will be for the backend application.

**running mysql container**
a) execute : 
*docker container run -d --network fplusf-net  --name dbMessageTest -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password123 mysql:8.0*

b) Enter into container console:  *docker exec -it dbMessageTest  /bin/sh*

c) Put mysql credentials: 
  -mysql -u root -p;
   -password123
   
 d) create database: CREATE DATABASE dbMessage;
 
 **Running container of application**
 execute: 
*docker run -p 8600:8600  -d --link dbMessageTest  --network fplusf-net  -e spring.profiles.active=test  --name=message-api ernestoagc/message-api:0.1*



#### **3. Executing Unit Test**
I've Built for MessageController (MessageControllerTest.java) 5 unit test with Mockito.

![](https://i.imgur.com/uAsPP9w.jpg)


#### **4. Testing webservice on postman**
Also you can find a postman colecction (FplusF) for test this web service
![](https://i.imgur.com/tm8cSz0.jpg)



#### **5. demo**
I've deploy these application on heroku. if you want to test online replace: http://localhost:8600  to https://messageangular.herokuapp.com