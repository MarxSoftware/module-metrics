package com.thorstenmarx.webtools.modules.metrics.engine.functions;

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
import com.alibaba.fastjson.JSONObject;
import com.thorstenmarx.webtools.api.analytics.Events;
import com.thorstenmarx.webtools.api.analytics.Fields;
import com.thorstenmarx.webtools.api.analytics.query.ShardDocument;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

/**
 *
 * @author marx
 */
public class OrdersTotalValueFunctionNGTest {

	@Test
	public void test_float_values() {
		OrdersTotalValue totalValue = new OrdersTotalValue();

		totalValue.accept(createOrderDocument(1.5f));
		totalValue.accept(createOrderDocument(1.5f));

		Assertions.assertThat(totalValue.get()).isEqualTo(3.0d);
	}

	@Test
	public void test_string_values() {
		OrdersTotalValue totalValue = new OrdersTotalValue();

		totalValue.accept(createOrderDocument("1.5"));
		totalValue.accept(createOrderDocument("1.5"));

		Assertions.assertThat(totalValue.get()).isEqualTo(3.0d);
	}

	@Test
	public void test_mixed_values() {
		OrdersTotalValue totalValue = new OrdersTotalValue();

		totalValue.accept(createOrderDocument(1.5f));
		totalValue.accept(createOrderDocument("1.5"));

		Assertions.assertThat(totalValue.get()).isEqualTo(3.0d);
	}

	private ShardDocument createOrderDocument(final Object total) {
		JSONObject document = new JSONObject();
		document.put(Fields.Event.value(), Events.Order.value());
		document.put("c_order_total", total);
		ShardDocument doc = new ShardDocument("test", document);
		
		return doc;
	}
}
