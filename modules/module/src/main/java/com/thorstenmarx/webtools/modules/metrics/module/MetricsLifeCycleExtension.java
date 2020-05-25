/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorstenmarx.webtools.modules.metrics.module;

import com.thorstenmarx.modules.api.ModuleLifeCycleExtension;
import com.thorstenmarx.modules.api.annotation.Extension;
import com.thorstenmarx.webtools.api.analytics.AnalyticsDB;
import com.thorstenmarx.webtools.api.cache.CacheLayer;
import com.thorstenmarx.webtools.modules.metrics.api.MetricsService;
import com.thorstenmarx.webtools.modules.metrics.engine.Engine;
import javax.inject.Inject;

/**
 *
 * @author marx
 */
@Extension(ModuleLifeCycleExtension.class)
public class MetricsLifeCycleExtension extends ModuleLifeCycleExtension {

	public static Engine engine;
	
	
	@Inject
	private AnalyticsDB analyticsDb;
	@Inject
	private CacheLayer cachelayer;
	
	private MetricsService metricsService;
	
	@Override
	public void init() {
		
	}

	@Override
	public void activate() {
		engine = new Engine(analyticsDb, cachelayer);
		metricsService = new MetricsServiceImpl(engine);
		getContext().serviceRegistry().register(MetricsService.class, metricsService);
	}

	@Override
	public void deactivate() {
		getContext().serviceRegistry().unregister(MetricsService.class, metricsService);
		metricsService = null;
		engine = null;
	}
	
	
	private static class MetricsServiceImpl implements MetricsService {
		
		private final Engine engine;

		public MetricsServiceImpl(Engine engine) {
			this.engine = engine;
		}
		
		@Override
		public Number getKpi(String name, String site, long start, long end) {
			return engine.getKPI(name, site, start, end);
		}
		
	}
	
	
}
