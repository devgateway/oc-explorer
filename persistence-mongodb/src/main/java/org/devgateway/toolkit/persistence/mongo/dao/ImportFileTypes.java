package org.devgateway.toolkit.persistence.mongo.dao;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ImportFileTypes {

	public static final String LOCATIONS="Locations";
	
	public static final String PUBLIC_INSTITUTIONS="Public Intitutions";
	
	public static final String SUPPLIERS="Suppliers";
	
	public static final String PROCUREMENT_PLANS="Procurement Plans";
	
	public static final String BID_PLANS="Bid Plans";
	
	public static final String TENDERS="Tenders";
	
	public static final String EBID_AWARDS="eBid Awards";
	
	public static final String OFFLINE_AWARDS="Offline Awards";
	
	public static final List<String> allFileTypes = Collections.unmodifiableList(Arrays.asList(LOCATIONS,
			PUBLIC_INSTITUTIONS, SUPPLIERS, PROCUREMENT_PLANS, BID_PLANS, TENDERS, EBID_AWARDS, OFFLINE_AWARDS));

}
