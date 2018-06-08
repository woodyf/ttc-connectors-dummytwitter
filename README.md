# BluePrint - Trending Topic Campaigns: Dummy Twitter Connector
This project is an implementation of an Activiti Cloud Connector, 
which is a simple bi-directional 3rd party system-to-system integration using Spring Cloud Streams.
 
This project provides the interaction between a Social Media Feed such as twitter and the rest of our services. This Dummy Twitter feed
simulates a Twitter Feed by creating random tweets and pushing them into Rabbit MQ using the content of the tweet as JSON Payload.

At the same time this connector simulates the functionality of publishing a new tweet, by pushing data to an external system.  


# Run

In order to run this project locally, you need to clone the source code and then run inside the root directory

> mvn -Dserver.port=808x spring-boot:run

**Note**: replace "x" for your desired port number

You can use the following docker-compose file in order to start Rabbit MQ so the service can connect and send messages.



# Endpoints
- GET http://localhost:808x/ -> welcome message, configuration properties and HAL Links to other endpoints
- GET http://localhost:808x/feed/ -> returns false if the feed is stopped or true if the feed is active
- POST http://localhost:808x/feed/start -> starts the feed
- POST http://localhost:808x/feed/stop -> stop the feed
- POST http://localhost:808x/feed/tweet -> with tweet body will send a single tweet

# Single Input Tweet Format
```json
{
  "text": "Hey this is my first tweet",
  "author": "salaboy",
  "lang": "en"
}
```

# Configuration
The feed can be configured by changing the properties inside the application.properties file.

Tweet Feed Rate in milliseconds (1 tweet per second = 1000)
> tweet.rate=1000
