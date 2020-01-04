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

import com.alibaba.fastjson.JSONObject;
import com.thorstenmarx.webtools.api.analytics.Events;
import com.thorstenmarx.webtools.api.analytics.Fields;
import com.thorstenmarx.webtools.api.analytics.query.ShardDocument;
import org.assertj.core.api.Assertions;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 *
 * @author marx
 */
public class EventFunctionNGTest {
	

	@Test
	public void testGet() {
		JSONObject document = new JSONObject();
		document.put(Fields.Event.value(), Events.Order.value());
		ShardDocument doc = new ShardDocument("test", document);
		
		EventFunction function = new EventFunction(Events.Order.value());
		
		function.accept(doc);
		function.accept(doc);
		
		Assertions.assertThat(function.get()).isEqualTo(2);
	}	
}
