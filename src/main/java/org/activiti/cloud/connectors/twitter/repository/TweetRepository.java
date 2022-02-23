package org.activiti.cloud.connectors.twitter.repository;

import org.activiti.cloud.connectors.twitter.model.TweetEntity;
import org.springframework.data.repository.CrudRepository;

public interface TweetRepository extends CrudRepository<TweetEntity, Long> {
}
