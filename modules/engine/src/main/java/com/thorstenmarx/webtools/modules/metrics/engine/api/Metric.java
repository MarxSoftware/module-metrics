/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorstenmarx.webtools.modules.metrics.engine.api;

/*-
 * #%L
 * metrics-api
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

import com.thorstenmarx.webtools.api.analytics.AnalyticsDB;
import com.thorstenmarx.webtools.api.analytics.query.Aggregator;
import com.thorstenmarx.webtools.api.analytics.query.Query;
import com.thorstenmarx.webtools.api.analytics.query.ShardDocument;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author marx
 * @param <V>
 */
public class Metric<V extends Number> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Metric.class);
	
	private final Supplier<ConsumerFunction<ShardDocument, V>> functionSupplier;
	
	private final V defaultValue;

	public Metric(final Supplier<ConsumerFunction<ShardDocument, V>> functionSupplier, final V defaultValue) {
		this.functionSupplier = functionSupplier;
		this.defaultValue = defaultValue;
	}
	
	public V calculate (final AnalyticsDB db, final Query query) {
				
		CompletableFuture<V> result = db.query(query, new Aggregator<V>() {
			@Override
			public V call() throws Exception {
				ConsumerFunction<ShardDocument, V> conversion = functionSupplier.get();
				
				documents.forEach(conversion::accept);
				
				return conversion.get();
			}
		});
		
		try {
			V resultValue = result.get();
			return resultValue != null ? resultValue : defaultValue;
		} catch (InterruptedException | ExecutionException ex) {
			LOGGER.error("", ex);
			throw new IllegalStateException(ex);
		}
	}
}
