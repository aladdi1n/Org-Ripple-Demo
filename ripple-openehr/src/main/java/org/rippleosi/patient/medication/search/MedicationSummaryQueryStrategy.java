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
package org.rippleosi.patient.medication.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.service.AbstractListQueryStrategy;
import org.rippleosi.patient.medication.model.MedicationSummary;

/**
 */
public class MedicationSummaryQueryStrategy extends AbstractListQueryStrategy<MedicationSummary> {

    MedicationSummaryQueryStrategy(String patientId) {
        super(patientId);
    }

    @Override
    public String getQuery(String namespace, String patientId) {
        return "select a/uid/value as uid, " +
               "a_a/items/items/data[at0001]/items/items[at0001]/value/value as name, " +
               "a_a/items/items/data[at0001]/items/items[at0020]/value/value as dose_amount " +
               "from EHR e " +
               "contains COMPOSITION a[openEHR-EHR-COMPOSITION.care_summary.v0] " +
               "contains SECTION a_a[openEHR-EHR-SECTION.medication_medical_devices_rcp.v1] " +
               "where a/name/value='Current medication list' " +
                "and e/ehr_status/subject/external_ref/namespace = '" + namespace + "' " +
                "and e/ehr_status/subject/external_ref/id/value = '" + patientId + "'";
    }

    @Override
    public List<MedicationSummary> transform(List<Map<String, Object>> resultSet) {
        return CollectionUtils.collect(resultSet, new MedicationSummaryTransformer(), new ArrayList<>());
    }
}
