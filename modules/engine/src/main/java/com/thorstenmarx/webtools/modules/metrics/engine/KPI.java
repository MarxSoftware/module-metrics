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
import com.thorstenmarx.webtools.modules.metrics.api.Average;
import com.thorstenmarx.webtools.modules.metrics.api.Conversion;
import com.thorstenmarx.webtools.modules.metrics.api.Metric;
import com.thorstenmarx.webtools.modules.metrics.engine.functions.CartFunction;
import com.thorstenmarx.webtools.modules.metrics.engine.functions.EventFunction;
import com.thorstenmarx.webtools.modules.metrics.engine.functions.PageViewFunction;
import com.thorstenmarx.webtools.modules.metrics.engine.functions.UniqueUsersFunction;
import com.thorstenmarx.webtools.modules.metrics.engine.functions.VisitsFunction;
/**
 *
 * @author marx
 */
public enum KPI {
	
	UNIQUE_USERS("unique_users", new Metric<Integer>(() -> {
		return new UniqueUsersFunction();
	}, 0)),
	PAGEVIEWS_PER_USER("pageviews_per_user", new Metric<Float>(() -> {
		return new Average(new UniqueUsersFunction(), new PageViewFunction());
	}, 0f)),
	PAGEVIEWS_PER_VISIT("pageviews_per_visit", new Metric<Float>(() -> {
		return new Average(new VisitsFunction(), new PageViewFunction());
	}, 0f)),
	VISITS_PER_USER("visits_pre_user", new Metric<Float>(() -> {
		return new Average(new UniqueUsersFunction(), new VisitsFunction());
	}, 0f)),
	UNIQUE_ORDERS("unique_orders", new Metric<Integer>(() -> {
		return new EventFunction(Events.Order.value());
	}, 0)),
	ORDER_CONVERSEN_RATE("order_conversion_rate", new Metric<Float>(() -> {
		return new Conversion(new UniqueUsersFunction(), new EventFunction(Events.Order.value()));
	}, 0f)),
	ORDERS_PER_USER("orders_per_user", new Metric<Float>(() -> {
		return new Average(new UniqueUsersFunction(), new EventFunction(Events.Order.value()));
	}, 0f)),
	CART_ABANDONED_CONVERSION("cart_abandoned_rate", new Metric<Float>(() -> {
		return new Conversion(new CartFunction(CartFunction.Type.ALL), new CartFunction(CartFunction.Type.ABANDONED));
	}, 0f)),
	;
	
	private final Metric metric;
	private final String name;

	private KPI(final String name, final Metric metric) {
		this.metric = metric;
		this.name = name;
	}

	public Metric getMetric() {
		return metric;
	}

	public String getName() {
		return name;
	}
	
	
	
	public static KPI get (final String name) {
		
		for (final KPI kpi : values()) {
			if (kpi.getName().equalsIgnoreCase(name)) {
				return kpi;
			}
		}
		
		throw new IllegalArgumentException("unknow metric " + name);
	}
}
