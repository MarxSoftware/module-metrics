/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorstenmarx.webtools.modules.metrics.module;

import com.thorstenmarx.modules.api.annotation.Extension;
import com.thorstenmarx.webtools.api.analytics.AnalyticsDB;
import com.thorstenmarx.webtools.api.extensions.SecureRestResourceExtension;
import com.thorstenmarx.webtools.modules.metrics.engine.Engine;
import com.thorstenmarx.webtools.modules.metrics.engine.Resolution;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author marx
 */
@Extension(SecureRestResourceExtension.class)
public class MetricSecureRestResource extends SecureRestResourceExtension {

	@Inject
	private AnalyticsDB analyticsDb;
	
	private Engine engine;
	
	@Override
	public void init() {
		engine = new Engine(analyticsDb);
	}
	
	@GET
	@Path("/kpi")
	@Produces(MediaType.APPLICATION_JSON)
	public KPIResult kpi(@QueryParam("name") final String name, @QueryParam("site") final String site, @QueryParam("start") final long start, @QueryParam("end") final long end) {
		KPIResult bean = new KPIResult();
		
		bean.setName(name);
		bean.setValue(engine.getKPI(name, site, start, end));
		
		return bean;
	}
	
	@GET
	@Path("/range")
	@Produces(MediaType.APPLICATION_JSON)
	public KPIResult range(@QueryParam("name") final String name, @QueryParam("site") final String site, @QueryParam("start") final long start, @QueryParam("end") final long end) {
		KPIResult bean = new KPIResult();
		
		bean.setName(name);
		bean.setValue(engine.getKPI(name, site, start, end, Resolution.MONTH));
		
		return bean;
	}
	
	
}
