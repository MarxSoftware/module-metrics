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

import com.thorstenmarx.webtools.api.analytics.query.ShardDocument;
import java.util.function.ToIntFunction;

/**
 *
 * @author marx
 */
public class Conversion<T extends Number> implements ConsumerFunction<ShardDocument, Float>{

	private final ConsumerFunction<ShardDocument, T> baseFunction;
	private final ConsumerFunction<ShardDocument, T> goalFunction;

	public Conversion(final ConsumerFunction<ShardDocument, T> baseFunction, final ConsumerFunction<ShardDocument, T> goalFunction) {
		this.baseFunction = baseFunction;
		this.goalFunction = goalFunction;
	}
	
	public void accept (final ShardDocument document) {
		baseFunction.accept(document);
		goalFunction.accept(document);
	}
	
	public Float get () {
		final Number base = baseFunction.get();
		if (base.floatValue() == 0) {
			return 0f;
		}
		final Number goal = goalFunction.get();
		return (goal.floatValue() / base.floatValue()) * 100f;
	}
	
	
}
