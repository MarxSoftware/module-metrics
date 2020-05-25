/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorstenmarx.webtools.modules.metrics.engine.functions;

import com.google.common.util.concurrent.AtomicDouble;
import com.thorstenmarx.webtools.api.analytics.Events;
import com.thorstenmarx.webtools.api.analytics.Fields;
import com.thorstenmarx.webtools.api.analytics.query.ShardDocument;
import com.thorstenmarx.webtools.modules.metrics.engine.api.ConsumerFunction;

/**
 *
 * @author marx
 */
public class OrdersTotalValue implements ConsumerFunction<ShardDocument, Double> {
	
	AtomicDouble totalValue = new AtomicDouble(0);

	public OrdersTotalValue() {
	}

	@Override
	public Double get() {
		return totalValue.get();
	}

	@Override
	public void accept(ShardDocument document) {
		if (Events.Order.value().equalsIgnoreCase(document.document.getString(Fields.Event.value()))) {
			final String cart_id = document.document.getString("c_cart_id");
			final Object total = document.document.get("c_order_total");
			
			totalValue.addAndGet(toDouble(total));
		}
	}
	
	private double toDouble (final Object value) {
		if (value instanceof Double) {
			return (Double)value;
		} else if (value instanceof Float) {
			return Float.class.cast(value).doubleValue();
		} else if (value instanceof String){
			return Double.valueOf(((String) value).trim());
		}
		
		return 0d;
	}

}
