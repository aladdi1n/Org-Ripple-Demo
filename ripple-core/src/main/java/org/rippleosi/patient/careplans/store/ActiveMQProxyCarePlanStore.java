/*
 * Copyright 2015 Ripple OSI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.rippleosi.patient.careplans.store;

import org.apache.camel.Produce;
import org.rippleosi.patient.careplans.model.CarePlanDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class ActiveMQProxyCarePlanStore implements CarePlanStore {

    private static final Logger logger = LoggerFactory.getLogger(ActiveMQProxyCarePlanStore.class);

    @Value("${repository.config.jms:500}")
    private int priority;

    @Produce(uri = "activemq:topic:VirtualTopic.Ripple.CarePlan.Create")
    private CarePlanStore createTopic;

    @Produce(uri = "activemq:topic:VirtualTopic.Ripple.CarePlan.Update")
    private CarePlanStore updateTopic;

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getSource() {
        return "activemq";
    }

    @Override
    public void create(String patientId, CarePlanDetails carePlan) {
        try {
            createTopic.create(patientId, carePlan);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void update(String patientId, CarePlanDetails carePlan) {
        try {
            updateTopic.update(patientId, carePlan);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
