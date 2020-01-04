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

import com.thorstenmarx.webtools.api.analytics.Events;
import com.thorstenmarx.webtools.api.analytics.Fields;
import com.thorstenmarx.webtools.api.analytics.query.Query;
import com.thorstenmarx.webtools.test.MockAnalyticsDB;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author marx
 */
public class KPINGTest {

	MockAnalyticsDB analyticsDB;

	static final String TEST_SITE = "test_site";

	@BeforeMethod
	public void setup() throws Exception {
		analyticsDB = new MockAnalyticsDB();
	}

	private Map<String, Map<String, Object>> createEvent() {
		Map<String, Map<String, Object>> event = new HashMap<>();
		event.put("data", new HashMap<>());
		event.get("data").put(Fields.Site.value(), TEST_SITE);
		return event;
	}

	@Test
	public void test_unique_users() {

		Number result = KPI.UNIQUE_USERS.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo(0);

		Map<String, Map<String, Object>> event = createEvent();
		event.get("data").put(Fields.UserId.value(), "u1");
		analyticsDB.track(event);
		result = KPI.UNIQUE_USERS.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo(1);

		event = createEvent();
		event.get("data").put(Fields.UserId.value(), "u1");
		analyticsDB.track(event);
		result = KPI.UNIQUE_USERS.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo(1);

		event = createEvent();
		event.get("data").put(Fields.UserId.value(), "u2");
		analyticsDB.track(event);
		result = KPI.UNIQUE_USERS.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo(2);
	}

	@Test
	public void test_orders_per_user() {
		Number result = KPI.ORDERS_PER_USER.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo(0f);

		Map<String, Map<String, Object>> event = createEvent();
		event.get("data").put(Fields.UserId.value(), "u1");
		analyticsDB.track(event);
		result = KPI.ORDERS_PER_USER.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo(0f);
		
		event = createEvent();
		event.get("data").put(Fields.UserId.value(), "u1");
		event.get("data").put(Fields.Event.value(), Events.Order.value());
		analyticsDB.track(event);
		result = KPI.ORDERS_PER_USER.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo(1f);
		
		event = createEvent();
		event.get("data").put(Fields.UserId.value(), "u2");
		event.get("data").put(Fields.Event.value(), Events.Order.value());
		analyticsDB.track(event);
		result = KPI.ORDERS_PER_USER.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo(1f);
		
		event = createEvent();
		event.get("data").put(Fields.UserId.value(), "u3");
		analyticsDB.track(event);
		result = KPI.ORDERS_PER_USER.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo((2 / 3f));
	}

	@Test
	public void test_order_conversion_rate() {
		Number result = KPI.ORDERS_PER_USER.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo(0f);

		Map<String, Map<String, Object>> event = createEvent();
		event.get("data").put(Fields.UserId.value(), "u1");
		analyticsDB.track(event);
		result = KPI.ORDER_CONVERSEN_RATE.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo(0f);
		
		event = createEvent();
		event.get("data").put(Fields.UserId.value(), "u1");
		event.get("data").put(Fields.Event.value(), Events.Order.value());
		analyticsDB.track(event);
		result = KPI.ORDER_CONVERSEN_RATE.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo(100.0f);
		
		event = createEvent();
		event.get("data").put(Fields.UserId.value(), "u2");
		event.get("data").put(Fields.Event.value(), Events.Order.value());
		analyticsDB.track(event);
		result = KPI.ORDER_CONVERSEN_RATE.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo(100.0f);
		
		event = createEvent();
		event.get("data").put(Fields.UserId.value(), "u3");
		analyticsDB.track(event);
		result = KPI.ORDER_CONVERSEN_RATE.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo((2 / 3f) * 100);
	}

	@Test
	public void test_visits_per_user() {
		Number result = KPI.VISITS_PER_USER.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo(0f);

		Map<String, Map<String, Object>> event = createEvent();
		event.get("data").put(Fields.UserId.value(), "u1");
		event.get("data").put(Fields.VisitId.value(), "u1-v1");
		analyticsDB.track(event);
		result = KPI.VISITS_PER_USER.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo(1f);
		
		event = createEvent();
		event.get("data").put(Fields.UserId.value(), "u1");
		event.get("data").put(Fields.VisitId.value(), "u1-v1");
		analyticsDB.track(event);
		result = KPI.VISITS_PER_USER.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo(1f);
		
		event = createEvent();
		event.get("data").put(Fields.UserId.value(), "u2");
		event.get("data").put(Fields.VisitId.value(), "u2-v1");
		analyticsDB.track(event);
		result = KPI.VISITS_PER_USER.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo((2 / 2f));
		
		event = createEvent();
		event.get("data").put(Fields.UserId.value(), "u2");
		event.get("data").put(Fields.VisitId.value(), "u2-v2");
		analyticsDB.track(event);
		result = KPI.VISITS_PER_USER.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo((3 / 2f));
	}

	@Test
	public void test_views_per_user() {
		Number result = KPI.PAGEVIEWS_PER_USER.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo(0f);

		Map<String, Map<String, Object>> event = createEvent();
		event.get("data").put(Fields.UserId.value(), "u1");
		event.get("data").put(Fields.RequestId.value(), "u1-v1");
		analyticsDB.track(event);
		result = KPI.PAGEVIEWS_PER_USER.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo(1f);
		
		event = createEvent();
		event.get("data").put(Fields.UserId.value(), "u1");
		event.get("data").put(Fields.RequestId.value(), "u1-v1");
		analyticsDB.track(event);
		result = KPI.PAGEVIEWS_PER_USER.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo(1f);
		
		event = createEvent();
		event.get("data").put(Fields.UserId.value(), "u2");
		event.get("data").put(Fields.RequestId.value(), "u2-v1");
		analyticsDB.track(event);
		result = KPI.PAGEVIEWS_PER_USER.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo((2 / 2f));
		
		event = createEvent();
		event.get("data").put(Fields.UserId.value(), "u2");
		event.get("data").put(Fields.RequestId.value(), "u2-v2");
		analyticsDB.track(event);
		result = KPI.PAGEVIEWS_PER_USER.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo((3 / 2f));
	}

	@Test
	public void test_views_per_visit() {
		
		Number result = KPI.PAGEVIEWS_PER_VISIT.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo(0f);

		Map<String, Map<String, Object>> event = createEvent();
		event.get("data").put(Fields.UserId.value(), "u1");
		event.get("data").put(Fields.RequestId.value(), "u1-r1");
		event.get("data").put(Fields.VisitId.value(), "u1-v1");
		event.get("data").put(Fields.Event.value(), Events.PageView.value());
		analyticsDB.track(event);
		result = KPI.PAGEVIEWS_PER_VISIT.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo(1f);
		
		event = createEvent();
		event.get("data").put(Fields.UserId.value(), "u1");
		event.get("data").put(Fields.RequestId.value(), "u1-r2");
		event.get("data").put(Fields.VisitId.value(), "u1-v1");
		event.get("data").put(Fields.Event.value(), Events.PageView.value());
		analyticsDB.track(event);
		result = KPI.PAGEVIEWS_PER_VISIT.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo(2f);
		
		event = createEvent();
		event.get("data").put(Fields.UserId.value(), "u2");
		event.get("data").put(Fields.RequestId.value(), "u2-r1");
		event.get("data").put(Fields.VisitId.value(), "u2-v1");
		event.get("data").put(Fields.Event.value(), Events.PageView.value());
		analyticsDB.track(event);
		result = KPI.PAGEVIEWS_PER_VISIT.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo((3 / 2f));
		
		event = createEvent();
		event.get("data").put(Fields.UserId.value(), "u2");
		event.get("data").put(Fields.RequestId.value(), "u2-r2");
		event.get("data").put(Fields.VisitId.value(), "u2-v2");
		event.get("data").put(Fields.Event.value(), Events.PageView.value());
		analyticsDB.track(event);
		result = KPI.PAGEVIEWS_PER_VISIT.getMetric().calculate(analyticsDB, Query.builder().build());
		Assertions.assertThat(result).isEqualTo((4 / 3f));
		
	}
}
