Software Requirement:
1. Java 1.8
2. Maven 3.8.6
3. Apache AMQ server 5.17.1
4. Docker Container
5. Eclipse Version: 2022-03 (4.23.0)
6. Postman version 9.27.0

Prerequisites:
1. Get Apache AMQ server running
2. Get following docker image running in docker container 
    https://hub.docker.com/r/xyzassessment/backend-services

Project setup & run aggregator-service 
1. Clone project in eclipse from github repository https://github.com/Santosh-1982/Aggregator-Service.git
2. Configure it as maven project and perform maven update, clean and build
3. Update application.properties file which is located on path src/main/resources
	a. update values for following AMQ server related properties
  		spring.activemq.broker-url=<amq-brocker-url>
		spring.activemq.user=<activemq-username>
		spring.activemq.password=<activemq-password>

	b. update following properties to replace localhost with host-ip/host-name as per docker container 
           pricing.api.base.url=http://localhost:9080/pricing?q=
           tracking.api.base.url=http://localhost:9080/track?q=
           shipment.api.base.url=http://localhost:9080/shipments?q=

3. Run as Spring Boot Application

4. Open Postman and test aggregator-service api hitting following end point
	http://localhost:8080/v1/aggregation-api?pricing=CN,NL&track=123456891,109347263&shipments=123456891,109347263
	
	
	
