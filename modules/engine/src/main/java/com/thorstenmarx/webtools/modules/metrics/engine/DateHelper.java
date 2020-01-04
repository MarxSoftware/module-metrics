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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author marx
 */
public class DateHelper {

	private List<DaySlot> getDaySlots(Date start, Date end) {
		List<DaySlot> ret = new ArrayList<>();
		Date current = start;
		while (current.before(end)) {
			Date startOfTheDay = getStartOfTheDay(current);
			Date endOfTheDay = getEndOfTheDay(startOfTheDay);
			ret.add(new DaySlot(current, endOfTheDay.before(end) ? endOfTheDay : end));
			current = getNextDay(startOfTheDay);
		}
		return ret;
	}

	private LocalDate getDateOnly(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	private Date getStartOfTheDay(Date current) {
		return Date.from(getDateOnly(current).atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	private Date getNextDay(Date startOftheDay) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startOftheDay);
		calendar.add(Calendar.DATE, 1);
		return calendar.getTime();
	}

	private Date getEndOfTheDay(Date startOftheDay) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startOftheDay);
		calendar.add(Calendar.HOUR_OF_DAY, 23);
		calendar.add(Calendar.MINUTE, 59);
		calendar.add(Calendar.SECOND, 59);
		calendar.add(Calendar.MILLISECOND, 599);
		return calendar.getTime();
	}

//    List<DaySlot> daySlots = getDaySlots(durationStartTime, durationEndTime);
	private static class DaySlot {

		public final Date start;
		public final Date end;

		public DaySlot(final Date start, final Date end) {
			this.start = start;
			this.end = end;
		}
	}

}
