package org.activiti.cloud.connectors.twitter.services;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import org.activiti.cloud.connectors.twitter.CampaignMessageChannels;
import org.activiti.cloud.connectors.twitter.TweetRepository;
import org.activiti.cloud.connectors.twitter.model.Tweet;
import org.activiti.cloud.connectors.twitter.model.TweetEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static net.logstash.logback.marker.Markers.append;

@Service
@EnableBinding(CampaignMessageChannels.class)
public class SocialFeedService {

    private Logger logger = LoggerFactory.getLogger(SocialFeedService.class);
    private AtomicBoolean started = new AtomicBoolean(false);
    private final MessageChannel campaignProducer;
    private final TweetRepository tweetRepository;

    @Value("${spring.application.name}")
    private String appName;

    public SocialFeedService(MessageChannel campaignProducer,
                             TweetRepository tweetRepository) {
        this.campaignProducer = campaignProducer;
        this.tweetRepository = tweetRepository;
    }

    public boolean isStarted() {
        return started.get();
    }

    public void start() {
        logger.info(append("service-name",
                           appName),">>> Starting Dummy Feed");
        started.set(true);
    }

    public void stop() {
        logger.info(append("service-name",
                           appName),">>> Stopping Dummy Feed");
        started.set(false);
    }

    @Scheduled(fixedRateString = "${tweet.rate}")
    public void startProcessWithTweet() {
        if (started.get()) {
            TweetEntity tweetEntity = getRandomTweet();

            Tweet t = new Tweet(tweetEntity.getText(),
                                tweetEntity.getAuthor(),
                                tweetEntity.getLang(),
                                System.currentTimeMillis());

            consumeTweet(t);
        }
    }

    public void consumeTweet(Tweet t) {
        logger.info(append("service-name",
                           appName),">>> Consuming Tweet: " + t);
        campaignProducer.send(MessageBuilder.withPayload(t).setHeader("lang",
                                                                      t.getLang()).build());
    }

    public void produceTweet(String to,
                             String text) {
        logger.info(append("service-name",
                           appName),
                    ">>> Producing Tweet to: " + to + " - Text: " + text);
    }

    public TweetEntity getRandomTweet() {
        long count = tweetRepository.count();
        int countInt = Integer.MAX_VALUE;
        if (count < Integer.MAX_VALUE) {
            countInt = (int) count;
        }
        Random random = new Random();
        int id = random.nextInt(countInt) + 1;
        return tweetRepository.findById(new Long(id)).get();
    }
}
