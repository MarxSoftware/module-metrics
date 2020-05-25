package com.thorstenmarx.webtools.modules.metrics.api;

import java.util.Map;

/**
 *
 * @author marx
 */
public interface MetricsService {
	/**
	 * 
	 * @param name Name of the kpi
	 * @param site The site for the kpi
	 * @param start The start value
	 * @param end The end value
	 * @return 
	 */
	Number getKpi (final String name, final String site, final long start, final long end);
}
