package org.activiti.cloud.connectors.twitter.connectors;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.cloud.connectors.starter.channels.IntegrationResultSender;
import org.activiti.cloud.connectors.starter.model.IntegrationRequestEvent;
import org.activiti.cloud.connectors.starter.model.IntegrationResultEvent;
import org.activiti.cloud.connectors.starter.model.IntegrationResultEventBuilder;
import org.activiti.cloud.connectors.twitter.model.Reward;
import org.activiti.cloud.connectors.twitter.services.SocialFeedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static net.logstash.logback.marker.Markers.append;

@Component
@EnableBinding(TweetConnectorChannels.class)
public class TweetConnector {

    private Logger logger = LoggerFactory.getLogger(TweetConnector.class);

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private SocialFeedService socialFeedService;

    private final IntegrationResultSender integrationResultSender;

    public TweetConnector(IntegrationResultSender integrationResultSender) {
        this.integrationResultSender = integrationResultSender;
    }

    @StreamListener(value = TweetConnectorChannels.TWEET_CONSUMER)
    public void tweetRewards(IntegrationRequestEvent event) {
        List rewards = (List) event.getVariables().get("rewards");
        if (rewards != null) {
            for (Object rewardObject : rewards) {
                Reward r = mapper.convertValue(rewardObject,
                                               Reward.class);

                socialFeedService.produceTweet(r.getRankedAuthor().getUserName(),
                                               "(" + r.getRewardDate() + "): " + r.getCampaignName() + " -> " + r.getRewardsText());
            }
        } else {
            logger.info(append("service-name",
                               appName),
                        ">>> No Rewards Found! ");
        }
        Message<IntegrationResultEvent> message = IntegrationResultEventBuilder.resultFor(event)
                .withVariables(new HashMap<>())
                .buildMessage();
        integrationResultSender.send(message);
    }
}