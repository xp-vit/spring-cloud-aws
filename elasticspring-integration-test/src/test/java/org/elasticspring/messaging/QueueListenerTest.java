/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elasticspring.messaging;

import org.elasticspring.core.support.documentation.RuntimeUse;
import org.elasticspring.messaging.core.QueueMessagingTemplate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Agim Emruli
 * @author Alain Sahli
 * @since 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("QueueListenerTest-context.xml")
public class QueueListenerTest {

	@Autowired
	private MessageListener messageListener;

	@Autowired
	private QueueMessagingTemplate queueMessagingTemplate;

	@Test
	public void testSendAndReceive() throws Exception {
		this.queueMessagingTemplate.send("QueueListenerTest", MessageBuilder.withPayload("Hello world!").build());
		Assert.assertTrue(this.messageListener.getCountDownLatch().await(15, TimeUnit.SECONDS));
	}

	public static class MessageListener {

		private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);
		private final CountDownLatch countDownLatch = new CountDownLatch(1);

		@RuntimeUse
		@MessageMapping("QueueListenerTest")
		public void receiveMessage(String message) {
			LOGGER.debug("Received message with content {}", message);
			Assert.assertEquals("Hello world!", message);
			this.getCountDownLatch().countDown();
		}

		CountDownLatch getCountDownLatch() {
			return this.countDownLatch;
		}
	}
}