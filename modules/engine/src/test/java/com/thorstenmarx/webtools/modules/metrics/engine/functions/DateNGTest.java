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

import java.time.Instant;
import java.util.Date;
import org.testng.annotations.Test;

/**
 *
 * @author marx
 */
public class DateNGTest {
	

	@Test
	public void javascript_date () {
		Date date = new Date();
		date.setTime(271988418000l);
		System.out.println(date);
		
		System.out.println("");
		Instant ofEpochMilli = Instant.ofEpochMilli(271988418000l);
		System.out.println(ofEpochMilli.toString());
	}
}
