/*
 * Copyright 2018 Alfresco, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.cloud.connectors.twitter;

import static org.assertj.core.api.Assertions.assertThat;

import org.activiti.cloud.connectors.twitter.repository.TweetRepository;
import org.activiti.cloud.connectors.twitter.services.SocialFeedService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(TwitterCloudConnectorAppIT.TWITTER_CLOUD_CONNECTOR_APP_IT)
class TwitterCloudConnectorAppIT {

	public static final String TWITTER_CLOUD_CONNECTOR_APP_IT = "TwitterCloudConnectorAppIT";

	@Autowired
	private ApplicationContext context;

	@Autowired
	private TweetRepository tweetRepository;

	@Autowired
	private SocialFeedService socialFeedService;

	@Test
	void shouldLoadContext() throws Exception {

		// then
		assertThat(context).isNotNull();
	}

	@Test
	void shouldLoadData() throws Exception {

		// then
		assertThat(tweetRepository.findAll()).isNotEmpty();
		assertThat(tweetRepository.count()).isPositive();
	}

	@Test
	void shouldRetrieveRandomTweet() throws Exception {
		assertThat(socialFeedService.getRandomTweet()).isNotNull();
	}
}