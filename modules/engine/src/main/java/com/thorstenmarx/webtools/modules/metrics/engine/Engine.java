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

import com.thorstenmarx.webtools.api.analytics.AnalyticsDB;
import com.thorstenmarx.webtools.api.analytics.Fields;
import com.thorstenmarx.webtools.api.analytics.query.Query;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author marx
 */
public class Engine {

	private final AnalyticsDB db;
	
	final DateTimeFormatter MONTH_YEAR  = DateTimeFormatter.ofPattern("MM-yyyy");
	
	public Engine (final AnalyticsDB db) {
		this.db = db;
	}
	
	public Number getKPI (final String name, final String site, final long start, final long end) {
		final KPI kpi = KPI.get(name);
		final Query query = Query.builder().start(start).end(end)
				.term(Fields.Site.value(), site).build();
		
		return kpi.getMetric().calculate(db, query);
	}
	
	public Map<String, Number> getKPI (final String name, final String site, final long start, final long end, Resolution resolution) {
		
		final Collection<OffsetDateTime[]> splitDateIntoMonths = Months.splitDateIntoMonths(new Date(start), new Date(end));
		
		final Map<String, Number> result = new TreeMap<>();
		splitDateIntoMonths.forEach((range) -> {
			final long startDate = range[0].toInstant().toEpochMilli();
			final long endDate = range[1].toInstant().toEpochMilli();
			
			final Number kpi = getKPI(name, site, startDate, endDate);
			result.put(range[0].format(MONTH_YEAR), kpi);
		});
		
		return result;
	}
}
