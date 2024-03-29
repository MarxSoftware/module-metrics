/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

import com.thorstenmarx.webtools.api.analytics.Fields;
import com.thorstenmarx.webtools.api.analytics.query.ShardDocument;
import com.thorstenmarx.webtools.modules.metrics.engine.api.ConsumerFunction;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author marx
 */
public class EventFunction implements ConsumerFunction<ShardDocument, Integer> {

	private final AtomicInteger counter;
	
	private final String event;
	public EventFunction (final String event) {
		this.event = event;
		this.counter = new AtomicInteger(0);
	}
	
	@Override
	public Integer get() {
		return counter.get();
	}

	@Override
	public void accept(final ShardDocument event) {
		if (event.document.containsKey(Fields.Event.value()) 
				&& event.document.getString(Fields.Event.value()).equals(this.event)) {
			counter.incrementAndGet();
		}
	}
	
}
