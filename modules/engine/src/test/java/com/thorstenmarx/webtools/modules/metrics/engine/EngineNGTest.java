/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorstenmarx.webtools.modules.metrics.engine;

/*-
 * #%L
 * metrics-engine
 * %%
 * Copyright (C) 2019 - 2020 Thorsten Marx
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.thorstenmarx.webtools.api.analytics.Fields;
import com.thorstenmarx.webtools.test.MockAnalyticsDB;
import java.text.ParseException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author marx
 */
public class EngineNGTest {

	MockAnalyticsDB analyticsDB;
	Engine engine;
	
	static final String TEST_SITE = "test_site";
	
	@BeforeMethod
	public void setup() throws Exception {
	
		analyticsDB = new MockAnalyticsDB();
		engine = new Engine(analyticsDB);
	}

	private Map<String, Map<String, Object>> createEvent() {
		Map<String, Map<String, Object>> event = new HashMap<>();
		event.put("data", new HashMap<>());
		event.get("data").put(Fields.Site.value(), TEST_SITE);
		return event;
	}

	@Test
	public void test_single_kpi() {
		Number kpi = engine.getKPI(KPI.UNIQUE_USERS.getName(), TEST_SITE, 0, Long.MAX_VALUE);
		Assertions.assertThat(kpi).isEqualTo(0);
		
		Map<String, Map<String, Object>> event = createEvent();
		event.get("data").put(Fields.UserId.value(), "user_1");
		this.analyticsDB.track(event);
		
		kpi = engine.getKPI(KPI.UNIQUE_USERS.getName(), TEST_SITE, 0, Long.MAX_VALUE);
		Assertions.assertThat(kpi).isEqualTo(1);
	}

	@Test
	public void test_range_kpi() throws ParseException {
		OffsetDateTime startDate = OffsetDateTime.parse("2000-01-01T00:00:00+00:00");
		OffsetDateTime endDate = OffsetDateTime.parse("2000-03-01T00:01:00+00:00");
		
		Map<String, Number> kpi = engine.getKPI(KPI.UNIQUE_USERS.getName(), TEST_SITE, startDate.toInstant().toEpochMilli(), endDate.toInstant().toEpochMilli(), Resolution.MONTH);
		
		Assertions.assertThat(kpi).isNotEmpty().hasSize(3);
		Assertions.assertThat(kpi.keySet()).containsExactly("01-2000", "02-2000", "03-2000");
		Assertions.assertThat(kpi.get("01-2000")).isEqualTo(0);
		Assertions.assertThat(kpi.get("02-2000")).isEqualTo(0);
		Assertions.assertThat(kpi.get("03-2000")).isEqualTo(0);
	}
	
	@Test
	public void test_range_kpi_null() throws ParseException {
		OffsetDateTime startDate = OffsetDateTime.parse("2000-01-01T00:00:00+00:00");
		OffsetDateTime endDate = OffsetDateTime.parse("2000-03-01T00:01:00+00:00");
		
		Map<String, Number> kpi = engine.getKPI(KPI.ORDER_CONVERSEN_RATE.getName(), TEST_SITE, startDate.toInstant().toEpochMilli(), endDate.toInstant().toEpochMilli(), Resolution.MONTH);
		
		Assertions.assertThat(kpi).isNotEmpty().hasSize(3);
		Assertions.assertThat(kpi.keySet()).containsExactly("01-2000", "02-2000", "03-2000");
		Assertions.assertThat(kpi.get("01-2000")).isEqualTo(0f);
		Assertions.assertThat(kpi.get("02-2000")).isEqualTo(0f);
		Assertions.assertThat(kpi.get("03-2000")).isEqualTo(0f);
	}
	
}
