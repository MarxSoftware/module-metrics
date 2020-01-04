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

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author marx
 */
public class Months {

	/**
	 * Splits a date range into months. The method provides for each set the
	 * start and the end date, so that specific days (1-3-2016 or 5-17-2016) can
	 * be covered.
	 *
	 * @param from The implied start-date
	 * @param to The implied end-date
	 * @return a collection whit the start and the end of each month laying
	 * between the two days. Also covering the months of the start- and the
	 * end-date itself.
	 * @throws IllegalArgumentException Is thrown when the from-date is later
	 * than the to-date.
	 */
	static Collection<OffsetDateTime[]> splitDateIntoMonths(Date from, Date to) throws IllegalArgumentException {

		Collection<OffsetDateTime[]> dates = new ArrayList<OffsetDateTime[]>();
		
//		DateTime dFrom = new DateTime(from);
//		DateTime dTo = new DateTime(to);

		OffsetDateTime dFrom = from.toInstant().atOffset(ZoneOffset.UTC);
		OffsetDateTime dTo = to.toInstant().atOffset(ZoneOffset.UTC);
		System.out.println(dFrom);
		
		if (dFrom.compareTo(dTo) >= 0) {

			throw new IllegalArgumentException("Provide a to-date greater than the from-date");

		}

		while (dFrom.compareTo(dTo) < 0) {

			OffsetDateTime[] dar = new OffsetDateTime[2];
			dar[0] = dFrom.withHour(0).withMinute(0).withSecond(0).withNano(0);
			dar[1] = dFrom.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);
//			dar[1] = dFrom
//					.withDayOfMonth(dFrom.getMonthValue() == dTo.getMonthValue()&& dFrom.getYear() == dTo.getYear()
//							? dTo.getDayOfMonth() : dFrom.getDayOfMonth());

			dates.add(dar);

			dFrom = dFrom.plusMonths(1).withDayOfMonth(1);

		}

		return dates;

	}
}
