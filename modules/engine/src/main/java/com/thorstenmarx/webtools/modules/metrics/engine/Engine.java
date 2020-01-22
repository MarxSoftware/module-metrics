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
import com.google.common.base.Strings;
import com.thorstenmarx.webtools.api.analytics.AnalyticsDB;
import com.thorstenmarx.webtools.api.analytics.Fields;
import com.thorstenmarx.webtools.api.analytics.query.Query;
import com.thorstenmarx.webtools.api.cache.CacheLayer;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 *
 * @author marx
 */
public class Engine {

	private final AnalyticsDB db;
	private final CacheLayer cacheLayer;

	final DateTimeFormatter MONTH_YEAR = DateTimeFormatter.ofPattern("MM-yyyy");

	public Engine(final AnalyticsDB db, final CacheLayer cacheLayer) {
		this.db = db;
		this.cacheLayer = cacheLayer;
	}

	public String cacheKey(final String name, final String site, final String page, final long start, final long end) {
		return String.format("%s-%s-%s-%d-%d", name, site, page, start, end);
	}

	public Number getKPI(final String name, final String site, final long start, final long end) {
		return getKPI(name, site, null, start, end);
	}

	public Number getKPI(final String name, final String site, final String page, final long start, final long end) {

		final String cachekey = cacheKey(name, site, page, start, end);

		final Optional<Number> optional = cacheLayer.get(cachekey, Number.class);
		return optional.orElseGet(() -> {
			final KPI kpi = KPI.get(name);
			final Query.Builder queryBuilder = Query.builder().start(start).end(end)
					.term(Fields.Site.value(), site);
			if (!Strings.isNullOrEmpty(page)) {
				queryBuilder.term(Fields.Page.value(), page);
			}

			final Query query = queryBuilder.build();

			return kpi.getMetric().calculate(db, query);
		});

	}

	public Map<String, Number> getKPI(final String name, final String site, final long start, final long end, Resolution resolution) {
		return getKPI(name, site, null, start, end, resolution);
	}

	public Map<String, Number> getKPI(final String name, final String site, final String page, final long start, final long end, Resolution resolution) {

		final Collection<OffsetDateTime[]> splitDateIntoMonths = Months.splitDateIntoMonths(new Date(start), new Date(end));

		final Map<String, Number> result = new TreeMap<>();
		splitDateIntoMonths.forEach((range) -> {
			final long startDate = range[0].toInstant().toEpochMilli();
			final long endDate = range[1].toInstant().toEpochMilli();

			final Number kpi = getKPI(name, site, page, startDate, endDate);
			result.put(range[0].format(MONTH_YEAR), kpi);
		});

		return result;
	}
}
