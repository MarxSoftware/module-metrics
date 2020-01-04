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

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Date;
import org.testng.annotations.Test;

/**
 *
 * @author marx
 */
public class MonthsNGTest {

	@Test
	public void testSomeMethod() {
		System.out.println("");
		System.out.println("*****************************");
		System.out.println("Split 2.1.2004 to 26.3.2004: ");
//		LocalDate from = LocalDate.of(204, Month.JANUARY, 2);
		Instant from = Instant.parse("2004-01-01T01:01:01.00Z");
		Instant to = Instant.parse("2004-03-26T01:01:01.00Z");
		for (OffsetDateTime[] date : Months.splitDateIntoMonths(new Date(from.toEpochMilli()),
				new Date(to.toEpochMilli()))) {

			System.out.println(date[0] + " and " + date[1]);

		}
	}

}
