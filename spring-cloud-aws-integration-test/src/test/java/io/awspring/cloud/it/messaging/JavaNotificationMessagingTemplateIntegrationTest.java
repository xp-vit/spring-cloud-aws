/*
 * Copyright 2013-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.awspring.cloud.it.messaging;

import com.amazonaws.services.sns.AmazonSNS;
import io.awspring.cloud.core.env.ResourceIdResolver;
import io.awspring.cloud.it.IntegrationTestConfig;
import io.awspring.cloud.messaging.config.annotation.EnableSns;
import io.awspring.cloud.messaging.config.annotation.EnableSqs;
import io.awspring.cloud.messaging.core.NotificationMessagingTemplate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Alain Sahli
 */
@ContextConfiguration(
		classes = JavaNotificationMessagingTemplateIntegrationTest.NotificationMessagingTemplateIntegrationTestConfiguration.class)
class JavaNotificationMessagingTemplateIntegrationTest extends NotificationMessagingTemplateIntegrationTest {

	@Configuration
	@EnableSqs
	@EnableSns
	@Import(IntegrationTestConfig.class)
	protected static class NotificationMessagingTemplateIntegrationTestConfiguration {

		@Bean
		public NotificationMessagingTemplate notificationMessagingTemplate(AmazonSNS amazonSns,
				ResourceIdResolver resourceIdResolver) {
			NotificationMessagingTemplate notificationMessagingTemplate = new NotificationMessagingTemplate(amazonSns,
					resourceIdResolver);
			notificationMessagingTemplate.setDefaultDestinationName("SqsReceivingSnsTopic");
			return notificationMessagingTemplate;
		}

		@Bean
		public NotificationReceiver notificationReceiver() {
			return new NotificationReceiver();
		}

	}

}