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
public class CartFunctionNGTest {
	

	@Test
	public void testAll() {
		JSONObject document = new JSONObject();
		document.put(Fields.Event.value(), Events.CartItemAdd.value());
		document.put("c_cart_id", "c1");
		ShardDocument doc = new ShardDocument("test", document);
		
		CartFunction allCarts = new CartFunction(CartFunction.Type.ALL);
		
		allCarts.accept(doc);
		allCarts.accept(doc);
		
		Assertions.assertThat(allCarts.get()).isEqualTo(1);
		
		document.put(Fields.Event.value(), Events.CartItemAdd.value());
		document.put("c_cart_id", "c2");
		allCarts.accept(doc);
		
		Assertions.assertThat(allCarts.get()).isEqualTo(2);
	}	
	@Test
	public void testCompleted() {
		JSONObject document = new JSONObject();
		document.put(Fields.Event.value(), Events.CartItemAdd.value());
		document.put("c_cart_id", "c1");
		ShardDocument doc = new ShardDocument("test", document);
		
		CartFunction function = new CartFunction(CartFunction.Type.COMPLETED);
		
		function.accept(doc);
		function.accept(doc);
		
		Assertions.assertThat(function.get()).isEqualTo(0);
		
		document.put(Fields.Event.value(), Events.Order.value());
		document.put("c_cart_id", "c1");
		function.accept(doc);
		
		Assertions.assertThat(function.get()).isEqualTo(1);
	}	
	@Test
	public void testCanceled() {
		JSONObject document = new JSONObject();
		document.put(Fields.Event.value(), Events.CartItemAdd.value());
		document.put("c_cart_id", "c1");
		ShardDocument doc = new ShardDocument("test", document);
		
		CartFunction function = new CartFunction(CartFunction.Type.ABANDONED);
		
		function.accept(doc);
		Assertions.assertThat(function.get()).isEqualTo(1);
		
		document.put(Fields.Event.value(), Events.Order.value());
		document.put("c_cart_id", "c1");
		function.accept(doc);
		
		Assertions.assertThat(function.get()).isEqualTo(0);
	}	
}
