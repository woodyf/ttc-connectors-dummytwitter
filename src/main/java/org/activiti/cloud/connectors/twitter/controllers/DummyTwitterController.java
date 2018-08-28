package org.activiti.cloud.connectors.twitter.controllers;

import java.util.Arrays;
import java.util.List;

import org.activiti.cloud.connectors.twitter.configuration.FeedConfiguration;
import org.activiti.cloud.connectors.twitter.model.Home;
import org.activiti.cloud.connectors.twitter.model.Tweet;
import org.activiti.cloud.connectors.twitter.services.SocialFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RequestMapping(path = "/v1")
@RestController
@RefreshScope
public class DummyTwitterController {

    private SocialFeedService socialFeedService;

    public DummyTwitterController(SocialFeedService socialFeedService) {
        this.socialFeedService = socialFeedService;
    }

    @Autowired
    private FeedConfiguration feedConfiguration;

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public Resource<Home> home() {
        Home home = new Home();
        home.setProperty("rate", String.valueOf(feedConfiguration.getRate()));
        home.setProperty("isStarted", String.valueOf(socialFeedService.isStarted()));
        return new Resource<>(home,
                                                getHomeLinks());
    }

    @RequestMapping(method = RequestMethod.POST, path = "/feed/start")
    public ResponseEntity<Void> startFeed() {
        if (!socialFeedService.isStarted()) {
            socialFeedService.start();
        }
        return ResponseEntity.accepted().build();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/feed/stop")
    public ResponseEntity<Void> stopFeed() {
        if (socialFeedService.isStarted()) {
            socialFeedService.stop();
        }
        return ResponseEntity.accepted().build();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/feed")
    public ResponseEntity<Boolean> isStarted() {
        return ResponseEntity.ok(socialFeedService.isStarted());
    }

    @RequestMapping(method = RequestMethod.POST, path = "/feed/tweet")
    public ResponseEntity<Void> consumeTweet(@RequestBody Tweet t) {
        socialFeedService.consumeTweet(t);
        return ResponseEntity.accepted().build();
    }

    private List<Link> getHomeLinks() {
        return Arrays.asList(linkTo(methodOn(DummyTwitterController.class).home()).withSelfRel(),
                             linkTo(methodOn(DummyTwitterController.class).startFeed()).withRel("start"),
                             linkTo(methodOn(DummyTwitterController.class).stopFeed()).withRel("stop"),
                             linkTo(methodOn(DummyTwitterController.class).isStarted()).withRel("isStarted"),
                             linkTo(methodOn(DummyTwitterController.class).consumeTweet(null)).withRel("newTweet")
                        );
    }
}

